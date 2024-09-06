package com.example.sqlitedatabasedemo1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class UpdateProfile extends AppCompatActivity {

    private static final String TAG = UpdateProfile.class.getSimpleName();

    Button updateMyProfile;
    TextView up_email;
    EditText up_name;
    Spinner up_gender;
    ImageView up_image;
    UserModal userModal;


    //permission Constacts
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    //array of permissions
    private String[] cameraPermissions;    //camera and storage permission
    private String[] storagePermissions;  // only storage permission

    //variable (will contain data to save)
    Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        up_email = (TextView) findViewById(R.id.up_email);
        up_name = (EditText) findViewById(R.id.up_name);
        up_gender = (Spinner) findViewById(R.id.up_gender);
        up_image = (ImageView) findViewById(R.id.up_image);
        updateMyProfile = (Button) findViewById(R.id.updateMyProfile);

        userModal = (UserModal) getIntent().getSerializableExtra("key_usermodal");

        up_email.setText(userModal.getEmail());
        up_name.setText(userModal.getName());
//        up_gender.setSelection(userModal.getGender().equals("Male") ? 0 : 1);

        Log.e(TAG,"up_gender update "+userModal.getGender());
        if (userModal.getGender().equals("Male")) {
            up_gender.setSelection(0);
            Log.e(TAG,"Male avyu ");
        } else if(userModal.getGender().equals("Female")) {
            up_gender.setSelection(1);
            Log.e(TAG,"Female avyu ");
        } else if (userModal.getGender().equals("Others")){
            up_gender.setSelection(2);
            Log.e(TAG,"Other avyu ");
        }

        up_image.setImageURI(Uri.parse(userModal.getImage()));

        up_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //init permission arrays
                cameraPermissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                storagePermissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE};

                //show image picker dialog
                imagePickerDialog();
            }

            private void imagePickerDialog() {

                //options to display in dialog
                String[] options = {"Camera", "Gallery"};

                //dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfile.this);
                // title
                builder.setTitle("Pick Image From");
                //set items/options
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // handle item clicks
                        if (which == 0) {
                            //camera clicked
                            if (!checkCameraPermissions()) {
                                //request permission
                                requestCameraPermission();
                            } else {
                                //permission already granted
                                pickFromCamera();
                            }
                        } else if (which == 1) {
                            //gallery clicked
                            if (!checkStoragePemission()) {
                                //request permission
                                requestStoragePermission();
                            } else {
                                //permission already granted
                                pickFromGallery();
                            }
                        }
//                        if (imageUri.toString().equals("null")) {
//                            //no images set default
//                            up_image.setImageResource(R.drawable.ic_launcher_background);
//                        }
//                        else {
//                            //image set
//                            up_image.setImageURI(imageUri);
//                        }
                    }
                });

                //show dialog
                builder.create().show();
            }

        });


        updateMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri == null) {
                    String name1 = up_name.getText().toString();        //name ne string ma convert karyu
                    String gender1 = up_gender.getSelectedItem().toString();

                    DbHelper dbHelper = new DbHelper(UpdateProfile.this);

                    boolean b = dbHelper.updateProfileHelper(userModal.getEmail(), name1, gender1, userModal.getImage());

                    if (b == true)     // b true hashe to if chalshe nakar else chalshe ..   b ni value true chhhe
                    {
                        Toast.makeText(UpdateProfile.this, "values update successfully..", Toast.LENGTH_SHORT).show();
                        finish();
//                    startActivity(getIntent());    // ano upyog same time data save thay tyare automatic update thai jay
//                    Intent intent = new Intent(UpdateProfile.this,Profile.class);
//                    intent.putExtra("key_usermodal",userModal);
//                    startActivity(intent);
                    } else {
                        Toast.makeText(UpdateProfile.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String name1 = up_name.getText().toString();        //name ne string ma convert karyu
                    String gender1 = up_gender.getSelectedItem().toString();
                    String image1 = imageUri.toString();            //imageUri ne string ma convert karyu
                    Log.e(TAG, "update images " + image1);

                    DbHelper dbHelper = new DbHelper(UpdateProfile.this);

                    boolean b = dbHelper.updateProfileHelper(userModal.getEmail(), name1, gender1, image1);

                    if (b == true)     // b true hashe to if chalshe nakar else chalshe ..   b ni value true chhhe
                    {
                        Toast.makeText(UpdateProfile.this, "values update successfully..", Toast.LENGTH_SHORT).show();
                        finish();
//                    startActivity(getIntent());    // ano upyog same time data save thay tyare automatic update thai jay
//                    Intent intent = new Intent(UpdateProfile.this,Profile.class);
//                    intent.putExtra("key_usermodal",userModal);
//                    startActivity(intent);
                    } else {
                        Toast.makeText(UpdateProfile.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        up_email.setText(userModal.getEmail());
        up_name.setText(userModal.getName());
//        up_gender.setSelection(userModal.getGender().equals("Male") ? 0 : 1);
//        up_image.setImageURI(Uri.parse(userModal.getImage()));

        Log.e(TAG,"up_gender update "+userModal.getGender());
        if (userModal.getGender().equals("Male")) {
            up_gender.setSelection(0);
            Log.e(TAG,"Male resume  avyu ");
        } else if(userModal.getGender().equals("Female")) {
            up_gender.setSelection(1);
            Log.e(TAG,"Female resume avyu ");
        } else if (userModal.getGender().equals("Others")){
            up_gender.setSelection(2);
            Log.e(TAG,"Other resume avyu ");
        }


    }

    private void pickFromGallery() {
        // intent to pick image from gallery , the image will be returned in onActivityResult method
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); // we want only images
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        // intent to pick image from camera , the image will be returned in onActivityResult method
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        //put image uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent to open camera for result
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePemission() {

        // check if Storage  permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {

        //request Storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {

        // check if Storage  permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {

        //request The camera  permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

//    public boolean onSupportNavigateUp() {
//        onBackPressed();                 // go back button of actionBar
//        return super.onSupportNavigateUp();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //result of permission allowed/denied
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    // if allowed return true otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_DENIED;    //CAMERA PERMISIION LE TYARE STORAGE PERMISSION OPTIONAL RAHE.......

                    if (cameraAccepted && storageAccepted) {
                        // both permission alowed
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "camera and storage permission are required", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    // if allowed return true otherwise false
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        // Storage permission allowed
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage Permission is required ", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    /**
     * @noinspection UnreachableCode
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //image picked from camera or gallery wiil be received here
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            //image is picked
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // picked from gallery

                // crop image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //picked from camera
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //crop image recieved
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    //image cropped successfully
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    Log.e(TAG, "images set " + resultUri);
                    // set image to imageview
                    up_image.setImageURI(resultUri);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //error while cropping
                    Exception error = result.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}