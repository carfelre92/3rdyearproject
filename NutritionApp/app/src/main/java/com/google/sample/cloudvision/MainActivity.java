/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sample.cloudvision;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

//import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {
    private static final String CLOUD_VISION_API_KEY = "AIzaSyCI_h7DyA9fhinSFPcN5CCq-8L2tQ-4PSI";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    private TextView mImageDetails;
    private ImageView mMainImage;
    private String bestRes;
    private List<String> results;
    private Boolean globalIsFood;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("App has entered the onCreate() method.");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Button takePhoto = (Button) findViewById(R.id.fab);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setMessage(R.string.dialog_select_prompt)
                        .setPositiveButton(R.string.dialog_select_gallery, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGalleryChooser();
                            }
                        })
                        .setNegativeButton(R.string.dialog_select_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                            }
                        });
                builder.create().show();
            }
        });

        mImageDetails = (TextView) findViewById(R.id.image_details);
        mMainImage = (ImageView) findViewById(R.id.main_image);
    }

    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //intent.
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                1200);

                callCloudVision(bitmap);
                mMainImage.setImageBitmap(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {

        System.out.println("App has entered the callCloudVision() method.");

        View allPageContent = findViewById(R.id.scrollView1);
        allPageContent.setVisibility(View.GONE);
        showSpinner();

        // Switch text to loading
        mImageDetails.setText(R.string.loading_message);

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                @Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(TAG, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                hideSpinner();
                View allPageContent = findViewById(R.id.scrollView1);
                allPageContent.setVisibility(View.VISIBLE);

                mImageDetails.setText(result);
                //Sets up and shows UI elements on the result page
                showResultsBtns();
            }
        }.execute();

    }

    private void showResultsBtns() {

        if(globalIsFood) {
            //Set content to visible
            View viewInfoBtnView = findViewById(R.id.viewInfoBtn);
            viewInfoBtnView.setVisibility(View.VISIBLE);
            View otherResultsBtnView = findViewById(R.id.otherResultsBtn);
            otherResultsBtnView.setVisibility(View.VISIBLE);
            View otherResultsTxtView = findViewById(R.id.otherResultsTxt);
            otherResultsTxtView.setVisibility(View.VISIBLE);

            //Change button text
            Button viewInfoBtn = (Button)findViewById(R.id.viewInfoBtn);
            viewInfoBtn.setText("View info about " + bestRes);
        } else {
            //Set content to invisible
            View viewInfoBtnView = findViewById(R.id.viewInfoBtn);
            viewInfoBtnView.setVisibility(View.INVISIBLE);
            View otherResultsBtnView = findViewById(R.id.otherResultsBtn);
            otherResultsBtnView.setVisibility(View.INVISIBLE);
            View otherResultsTxtView = findViewById(R.id.otherResultsTxt);
            otherResultsTxtView.setVisibility(View.INVISIBLE);
        }
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = "Nice ";
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();

        results = new ArrayList<String>(); //Holds the unfiltered results

        //Common or unwanted words to be filtered out (separated by a space)
        List<String> filterWords = new ArrayList<>(); //Holds the words to filter out
        filterWords.add("food");
        filterWords.add("produce");
        filterWords.add("family");
        filterWords.add("dish");
        filterWords.add("vegetable");
        filterWords.add("cuisine");

        //Unwanted words that could be part of a desired result
        List<String> specificWords = new ArrayList<String>(); //Holds the specific words to filter out
        specificWords.add("plant"); //could be part of a desired result. For example: eggplant
        specificWords.add("meal"); //could be part of a desired result. For example: oatmeal
        specificWords.add("fruit"); //could be part of a desired result. For example: kiwifruit
        specificWords.add("yellow"); //could be part of a desired result. For example: Yellow capsicum
        specificWords.add("green"); //could be part of green apple

        //Used to hold if the picture is food or not
        Boolean isFood = false;

        //Used to hold the best result
        String bestResult = "";

        if (labels != null) {
            for (EntityAnnotation label : labels) {
                results.add(String.format(label.getDescription()));
                for (int i = 0; i < results.size(); i++) {

                    //Checks if image is food
                    if (results.get(i).contains("food")) {
                        isFood = true;
                    }

                    //Searches for the most likely result
                    //Iterates through filterWords and checks result(i)
                    boolean filter = false; //true if result needs to be filtered
                    for (int j = 0; j < filterWords.size(); j++) {
                        if (results.get(i).contains(filterWords.get(j))) {
                            filter = true;
                        }
                    }
                    //Iterates through specificWords and checks result(i)
                    for (int j = 0; j < specificWords.size(); j++) {
                        if (results.get(i).equals(specificWords.get(j))) {
                            filter = true;
                        }
                    }
                    if (!filter && bestResult.isEmpty()) {
                        bestResult = results.get(i);
                        bestRes = bestResult;
                    }
                }
            }

            //For printing just the best result
            message += bestResult + "!";

            //For printing all results including the best result
            /*
            message += "\t\t\t" + bestResult + "\n\nAll results: \t\t\t";
            for (int i = 0; i < results.size(); i++) {
                message += results.get(i);
                message += "\n\t\t\t\t\t\t\t\t\t\t\t\t"; //Used to indent each result for formatting
            }
            */

        }     else {
            message += "no results";
        }
        if (!isFood) {
            message = "Please choose a photo of food.";
        }

        //Sends value to global variable for using in other method
        globalIsFood = isFood;

        return message;
    }

//    //View info button activity
//    Button viewInfoBtn = (Button) findViewById(R.id.viewInfoBtn);
//    viewInfoBtn.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            // handle click
//        }
//    });

    public void viewInfo(View view) {
        Intent intent = new Intent(this, NutritionInfo.class);
        intent.putExtra("bestRes", bestRes);
        intent.putExtra(EXTRA_MESSAGE, bestRes);
        startActivity(intent);
    }

    private void showSpinner() {
        progress = new ProgressDialog(this);
        progress.setMessage("Please wait while we digest this image... ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    private void hideSpinner() {
        progress.hide();
    }

    public void otherResults(View view) {
        Intent intent = new Intent(this, OtherResults.class);
        intent.putStringArrayListExtra("results", (ArrayList<String>) results);
        startActivity(intent);
    }

    /** Called when the user taps the Send button
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText2);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    } */

}
