package com.example.snehamishra.googlevision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_INTENT=1;
    private static final int GALLERY_INTENT=100;
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCRhxtGzbA_cJMcUnLP9AoliO_TkL5bg2A";

    ImageView imageDisplay;
    TextView description;
    Bitmap bitmap;
    private Uri imageCapturedUri;
    private Feature feature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button camera = findViewById(R.id.camera_btn);
        Button gallery = findViewById(R.id.gallery_btn);
        Button analyze = findViewById(R.id.analyze_btn);
        Button shop = findViewById(R.id.shop_btn);
        TextView moveBack = findViewById(R.id.backTextView);
         imageDisplay = findViewById(R.id.imageView);
         description = findViewById(R.id.descriptionTextView);

        feature = new Feature();
        feature.setType("LABEL_DETECTION");
        feature.setMaxResults(10);

        moveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        analyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGoogleVision();

            }
        });
    }

    //func to take pics using camera
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_INTENT);
    }

    //func to load images from gallery
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_INTENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Toast.makeText(getApplicationContext(),"Loading Image, kindly wait !!",Toast.LENGTH_LONG).show();
            switch (requestCode){
                case CAMERA_INTENT:
                {
                    bitmap = (Bitmap) data.getExtras().get("data");
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

        }else {
            Toast.makeText(getApplicationContext(),"Unable to load Image!!",Toast.LENGTH_LONG).show();
        }

    }

    private void callGoogleVision(){
        if(imageDisplay == null){
            Toast.makeText(getApplicationContext(),"No image available to analyze!! try again",Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getApplicationContext(),"Yuppies!!",Toast.LENGTH_LONG).show();

            final List<Feature> featureList = new ArrayList<>();
            featureList.add(feature);

            final List<AnnotateImageRequest> annotateImageRequests = new ArrayList<>();

            AnnotateImageRequest annotateImageReq = new AnnotateImageRequest();
            annotateImageReq.setFeatures(featureList);
            if(bitmap==null && imageCapturedUri!=null){
                Toast.makeText(getApplicationContext(),"Image selected from gallery!!",Toast.LENGTH_LONG).show();
                try {
                    Bitmap bMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageCapturedUri);
                    annotateImageReq.setImage(getImageEncodeImage(bMap));
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Cannot Convert image uri to BitMap!!"+e,Toast.LENGTH_LONG).show();
                }
            } else if(imageCapturedUri==null && bitmap!=null){
                Toast.makeText(getApplicationContext(),"Image selected by Camera!!",Toast.LENGTH_LONG).show();
                annotateImageReq.setImage(getImageEncodeImage(bitmap));
            }
            else {
                Toast.makeText(getApplicationContext(),"No Image available for Analysis!!",Toast.LENGTH_LONG).show();
            }
            annotateImageRequests.add(annotateImageReq);

            new AsyncTask<Object, Void, String>() {
                @Override
                protected String doInBackground(Object... params) {
                    try {

                        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                        VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY);

                        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                        builder.setVisionRequestInitializer(requestInitializer);

                        Vision vision = builder.build();

                        BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                        batchAnnotateImagesRequest.setRequests(annotateImageRequests);

                        Vision.Images.Annotate annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                        annotateRequest.setDisableGZipContent(true);
                        BatchAnnotateImagesResponse response = annotateRequest.execute();
                        return convertResponseToString(response);
                    } catch (GoogleJsonResponseException e) {
                        Toast.makeText(getApplicationContext(),"failed to make API request because " + e.getContent(),Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"failed to make API request because of other IOException " + e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    return "Cloud Vision API request failed. Check logs for details.";
                }

                protected void onPostExecute(String result) {
                    Toast.makeText(getApplicationContext(),"Analysis complete!!",Toast.LENGTH_LONG).show();
                    description.setText(result);

                }
            }.execute();


        }


    }

    private Image getImageEncodeImage(Bitmap bitmap) {
        Image base64EncodedImage = new Image();
        // Convert the bitmap to a JPEG
        // Just in case it's a format that Android understands but Cloud Vision
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Base64 encode the JPEG
        base64EncodedImage.encodeContent(imageBytes);
        return base64EncodedImage;
    }


    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        AnnotateImageResponse imageResponses = response.getResponses().get(0);
        List<EntityAnnotation> entityAnnotations;
               // entityAnnotations = imageResponses.getLandmarkAnnotations();
               // entityAnnotations = imageResponses.getLogoAnnotations();
                //SafeSearchAnnotation annotation = imageResponses.getSafeSearchAnnotation();
               // ImageProperties imageProperties = imageResponses.getImagePropertiesAnnotation();
                entityAnnotations = imageResponses.getLabelAnnotations();
        return formatAnnotation(entityAnnotations);
    }

    private String formatAnnotation(List<EntityAnnotation> entityAnnotation) {
        String message = "";

        if (entityAnnotation != null) {
            for (EntityAnnotation entity : entityAnnotation) {
                //message = message + "    " + entity.getDescription() + " " + entity.getScore();
                message = message + " " + entity.getDescription() + ",  " ;
                //message += "\n";
            }
        } else {
            message = "Nothing Found";
        }
        return message;
    }
}
