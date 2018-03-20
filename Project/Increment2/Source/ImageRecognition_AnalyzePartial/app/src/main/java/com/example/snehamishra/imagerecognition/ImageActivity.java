package com.example.snehamishra.imagerecognition;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;

public class ImageActivity extends AppCompatActivity {

    private Uri imageCapturedUri;
    Bitmap bitmap;
    private Button selectImage, captureImage, analyzeImage, shopByImage;
    private ImageView imageDisplay=null;
    private TextView  backActivity, skipActivity;
    private TextView imageDescription =null;

    private VisionServiceClient visionServiceClient = new VisionServiceRestClient("11d5cb03ef0849cbbd107178b42ca683");

    private static final int CAMERA_INTENT = 1;
    private static final int GALLERY_INTENT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        defineWidgets();

        //user's selection to move to login page
        backActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(ImageActivity.this, LoginActivity.class));
                finish();
            }
        });

        //user's selection to skip image recognition & move to shopping page
        skipActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageActivity.this, ShoppingActivity.class));
            }
        });

        //user's selection to use Image Analysis for shopping
        shopByImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageDisplay == null){
                    Toast.makeText(getApplicationContext(), "No Image selected. Kindly select Image and retry!!", Toast.LENGTH_LONG).show();
                }else if(imageDescription == null ){
                    Toast.makeText(getApplicationContext(), "No analysis available. Kindly try again!!", Toast.LENGTH_LONG).show();
                }else {
                    startActivity(new Intent(ImageActivity.this, ShoppingActivity.class));
                }
            }
        });

        //user chooses the image from gallery
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        //user captures image using camera
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();

            }
        });


        //user can analyze the selected image
        analyzeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageDisplay==null ){
                    Toast.makeText(getApplicationContext(), "No Image selected. Kindly select Image and retry!!", Toast.LENGTH_LONG).show();//display the text on image click event
                }else{
                    analyzeTheImage();
                }

            }
        });

    }

    private void analyzeTheImage(){
        AsyncTask<InputStream,String,String> visionTask = new AsyncTask<InputStream, String, String>() {
            ProgressDialog mDialog = new ProgressDialog(ImageActivity.this);
            @Override
            protected String doInBackground(InputStream... inputStreams) {
                try {
                    publishProgress("Analyzing ...");
                    String[] features = {"Description"};
                    String[] details = {};

                    AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0], features, details);

                    String strResult = new Gson().toJson(result);
                    return strResult;
                }
                catch (Exception e){
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                mDialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                mDialog.dismiss();

                AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);

                StringBuilder stringBuilder = new StringBuilder();

                for (Caption caption:result.description.captions){
                    stringBuilder.append(caption.text);
                }
                imageDescription.setText(stringBuilder);
            }

            @Override
            protected void onProgressUpdate(String... values) {
               mDialog.setMessage(values[0]);
            }
        };

        //get the image selected before
        if(bitmap==null && imageCapturedUri == null){
            //display that no image is selected
            Toast.makeText(getApplicationContext(), "No Image selected. Kindly select Image and retry!!", Toast.LENGTH_LONG).show();
        }
        else {
            //if image is captured using camera
            if(bitmap==null){
                InputStream inputStream;
                try {
                     inputStream = getContentResolver().openInputStream(imageCapturedUri);
                    Toast.makeText(getApplicationContext(), "Inside analyze by camera!!", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    return ;

                }
                visionTask.execute(inputStream);
            }
            //if image is selected using gallery
            if(imageCapturedUri == null){

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                InputStream inputStream = new ByteArrayInputStream(bitmapdata);
                Toast.makeText(getApplicationContext(), "Inside analyze by gallery!!", Toast.LENGTH_LONG).show();

                /*int byteSize = bitmap.getRowBytes() * bitmap.getHeight();
                ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
                bitmap.copyPixelsToBuffer(byteBuffer);
                // Get the byteArray.
                byte[] byteArray = byteBuffer.array();

                // Get the ByteArrayInputStream.
                InputStream inputStream = new InputStream(byteArray);
                */

                visionTask.execute(inputStream);
            }
        }
    }


    //opens the gallery
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_INTENT);

    }

    //capture image using camera
    private void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_INTENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case  CAMERA_INTENT:
                {
                    bitmap = (Bitmap)data.getExtras().get("data");
                    imageDisplay.setImageBitmap(bitmap);
                    imageCapturedUri=null;
                    break;
                }
                case GALLERY_INTENT:
                {
                    imageCapturedUri = data.getData();
                    imageDisplay.setImageURI(imageCapturedUri);
                    bitmap=null;
                    break;
                }
            }
          /*  if(requestCode == CAMERA_INTENT){

                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                imageDisplay.setImageBitmap(bitmap);

            }
            if( requestCode == GALLERY_INTENT){
                imageCapturedUri = data.getData();
                imageDisplay.setImageURI(imageCapturedUri);

            }*/
        }
    }

    //assign the widgets
    public void defineWidgets(){
        captureImage = findViewById(R.id.capture_btn);
        selectImage = findViewById(R.id.select_btn);
        analyzeImage  = findViewById(R.id.analyze_btn);
        imageDescription = findViewById(R.id.imageDescriptionTextView);
        shopByImage  = findViewById(R.id.shop_btn);
        imageDisplay = findViewById(R.id.imageView);
        backActivity = findViewById(R.id.backActivityTextView);
        skipActivity = findViewById(R.id.skipActivityTextView);

    }
}
