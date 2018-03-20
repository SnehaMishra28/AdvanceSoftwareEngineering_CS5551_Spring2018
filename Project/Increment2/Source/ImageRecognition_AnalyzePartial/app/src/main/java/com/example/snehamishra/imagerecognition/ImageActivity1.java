package com.example.snehamishra.imagerecognition;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;

import java.io.File;
import java.io.InputStream;

public class ImageActivity1 extends AppCompatActivity {

    private Uri imageCapturedUri;
    private Button chooseImage, analyzeImage, shopByImage;
    private ImageView imageDisplay;
    private TextView imageDescription, backActivity, skipActivity;

    private VisionServiceClient visionServiceClient = new VisionServiceRestClient("11d5cb03ef0849cbbd107178b42ca683");

    private static final int CAMERA_INTENT = 1;
    private static final int GALLERY_INTENT = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        defineWidgets();

        //user's selection to move to login page
        backActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageActivity1.this, LoginActivity.class));
            }
        });

        //user's selection to skip image recognition & move to shopping page
        skipActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageActivity1.this, ShoppingActivity.class));
            }
        });

        //user's selection to use Image Analysis for shopping
        shopByImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageActivity1.this, ShoppingActivity.class));
            }
        });


        //user can analyze the selected image
        analyzeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(imageDisplay==null ){
                    Toast.makeText(getApplicationContext(), "No Image selected.", Toast.LENGTH_LONG).show();//display the text on image click event

                }
                final AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>() {
                    ProgressDialog dialog = new ProgressDialog(ImageActivity1.this);

                    @Override
                    protected String doInBackground(InputStream... inputStreams) {
                        try {
                            publishProgress("Analyzing...");
                            String[] details = {};
                            String[] features = {"Description"};

                            AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0], features, details);
                                String strResult = new Gson().toJson(result);
                                return strResult;


                            } catch (Exception e){
                                return null;
                            }
                        }

                        @Override
                        protected void onPreExecute() {
                            dialog.show();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            dialog.dismiss();

                            AnalysisResult result = new Gson().fromJson(s,AnalysisResult.class);

                            imageDescription = findViewById(R.id.imageDescriptionTextView);
                            StringBuilder stringBuilder = new StringBuilder();

                            for(Caption caption:result.description.captions){
                                stringBuilder.append(caption.text);

                            }
                            imageDescription.setText(stringBuilder);
                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                            dialog.setMessage(values[0]);
                        }

                    };

                   // visionTask.execute(inputStream);
            }
        });

        // user's selection to choose image (from camera or gallery)
        final String[] items = new String[] {"From Camera", "From Gallery"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image");

        final AlertDialog dialog = builder.create();

        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which==0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),"tmp_avatar"+String.valueOf(System.currentTimeMillis())+".jpg");
                    imageCapturedUri = Uri.fromFile(file);
                    try{
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapturedUri);
                        intent.putExtra("Return Data", true);

                        startActivityForResult(intent,CAMERA_INTENT);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dialog.cancel();
                }else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Complete Action using "),GALLERY_INTENT);
                }
            }
        });


        //user chooses the image
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;

        }
        Bitmap bitmap = null;
        String path;

        if(requestCode == GALLERY_INTENT){
            imageCapturedUri = data.getData();
            path = getRealPathFromURI(imageCapturedUri);

            if(path==null || path==""){
                path=imageCapturedUri.getPath();
            }
            if(path!=null || path!=""){
                bitmap= BitmapFactory.decodeFile(path);

            }
        }else {
            path=imageCapturedUri.getPath();
            bitmap=BitmapFactory.decodeFile(path);

        }
        imageDisplay.setImageBitmap(bitmap);

    }

    public String getRealPathFromURI(Uri contentURI){
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(contentURI, projection, null, null, null);

        if(cursor == null){
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    //assign the widgets
    public void defineWidgets(){
        chooseImage = findViewById(R.id.chooseImage_btn);
        analyzeImage  = findViewById(R.id.analyze_btn);
        shopByImage  = findViewById(R.id.shop_btn);
        imageDisplay = findViewById(R.id.imageView);
       // imageDescription = findViewById(R.id.imageDescriptionTextView);
        backActivity = findViewById(R.id.backActivityTextView);
        skipActivity = findViewById(R.id.skipActivityTextView);

    }
}
