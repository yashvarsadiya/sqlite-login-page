package com.example.sqlitedatabasedemo1;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Register extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    EditText reg_name, reg_email, reg_pass;
    ImageView reg_image;
    CardView reg_btn;    // register button imageview ma change karelu chhhe
    Spinner reg_gender;

    TextView nameError, emailError, PassError;
    CardView cardOne, cardTwo, cardThree, cardFour;

    private boolean isAtLeast8 = false, hasUppercase = false, hasNumber = false, hasSymbol = false, isRegistrationClickable = false;
    DbHelper dbHelper;
    TextView show_data;
    MyAdapter adapter;
    String userGender = "Male";

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
    private String name, email, pass, gender, condition, image;

    //notification mate na variable
    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;
    private static final int REQ_CODE = 100;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_pass = findViewById(R.id.reg_pass);
        reg_image = findViewById(R.id.reg_image);
        reg_btn = findViewById(R.id.reg_btn);
        reg_gender = findViewById(R.id.reg_gender);

        nameError = findViewById(R.id.nameError);
        emailError = findViewById(R.id.emailError);
        PassError = findViewById(R.id.PassError);

        cardOne = findViewById(R.id.frameOne);
        cardTwo = findViewById(R.id.frameTwo);
        cardThree = findViewById(R.id.frameThree);
        cardFour = findViewById(R.id.frameFour);

        show_data = findViewById(R.id.show_data);

        inputChange();
//
//        adapter = new MyAdapter(this, userModal, new MyAdapter.OnClick()){
//
//        }


//        dbHelper = new DbHelper(getApplicationContext());
        dbHelper = new DbHelper(this);

        show_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Userlist.class));
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name1 = reg_name.getText().toString();                     //get values
                String email1 = reg_email.getText().toString();
                String pass1 = reg_pass.getText().toString();
                String gender1 = reg_gender.getSelectedItem().toString();

                inputData();


//                if (name1.length() > 0 && email1.length() > 0 && pass1.length() > 0){
//                    if(isRestricted()){
//                        Toast.makeText(Register.this, "user register Successfully", Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    if (name1.length() == 0) {
//                        nameError.setVisibility(View.VISIBLE);
//                    }
//                    if (email1.length() == 0) {
//                        emailError.setVisibility(View.VISIBLE);
//                    }
//                    if (pass1.length() == 0) {
//                        PassError.setVisibility(View.VISIBLE);
//                    }
//                }

                if (name.isEmpty() && email.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Please fill in all fields" + name + email + pass + condition);
                    reg_name.setError("Please enter name");
                    reg_email.setError("Please enter email");
                    reg_pass.setError("Please enter password");
                    if (name.isEmpty() && email.isEmpty() && pass.isEmpty()) {
                        nameError.setVisibility(View.VISIBLE);
                        emailError.setVisibility(View.VISIBLE);
                        PassError.setVisibility(View.VISIBLE);
                    } else {
                        nameError.setVisibility(View.GONE);
                        emailError.setVisibility(View.GONE);
                        PassError.setVisibility(View.GONE);
                    }

                } else {

                    if (name.isEmpty()) {
                        reg_name.setError("Please enter name");
                        Toast.makeText(Register.this, "Please enter name", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Please enter name" + name);
                        if (name.isEmpty()) {
                            nameError.setVisibility(View.VISIBLE);
                        } else {
                            nameError.setVisibility(View.GONE);
                        }
                    }
                    if (email.isEmpty()) {
                        reg_email.setError("Please enter email");
                        Toast.makeText(Register.this, "Please enter email", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Please enter email" + email);

                        if (email.isEmpty()) {
                            emailError.setVisibility(View.VISIBLE);
                        } else {
                            emailError.setVisibility(View.GONE);
                        }
                    }
                    if (pass.isEmpty()) {
                        reg_pass.setError("Please enter password");
                        Toast.makeText(Register.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Please enter password" + pass);
                        if (pass.isEmpty()) {
                            PassError.setVisibility(View.VISIBLE);
                        } else {
                            PassError.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        reg_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //init permission arrays
                    cameraPermissions = new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                //show image picker dialog
                imagePickerDialog();
            }

            private void imagePickerDialog() {

                //options to display in dialog
                String[] options = {"Camera", "Gallery"};

                //dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
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
                    }
                });

                //show dialog
                builder.create().show();
            }
        });
    }

    private void inputChange() {
        Log.e(TAG,"input change data: ");
        reg_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passWordCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void inputData() {

        name = reg_name.getText().toString().trim();
        email = reg_email.getText().toString().trim();
        pass = reg_pass.getText().toString().trim();
        gender = reg_gender.getSelectedItem().toString().trim();   // get selected item from spinner becouse selected item is it male or female
        userGender = gender;    // Select thayelu gender userGender ma jay

//        condition = String.valueOf(reg_agree.isChecked());

//        if (name.isEmpty() && pass.isEmpty() && email.isEmpty()) {
//            Log.e(TAG, "inputData: badha data nakho");
//        } else if (name.isEmpty()) {
//            Log.e(TAG, "inputData: name nakho", );
//        } else if (email.isEmpty()) {
//            Log.e(TAG, "inputData: email nakho", );
//        } else if (pass.isEmpty()) {
//            Log.e(TAG, "inputData: pass nakho", );
//        } else {
//
//        }                  // gautambhai code date 07/06/2024

//        if (name.isEmpty() && email.isEmpty() && pass.isEmpty()) {
//            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "Please fill in all fields" + name + email + pass + condition);
//        } else {
//            if (name.isEmpty()) {
//                reg_name.setError("Please enter name");
//                Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Please enter name" + name);
//            }
//            if (email.isEmpty()) {
//                reg_email.setError("Please enter email");
//                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Please enter email" + email);
//            }
//            if (pass.isEmpty()) {
//                reg_pass.setError("Please enter password");
//                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Please enter password" + pass);
//            }
//        }


        boolean b = dbHelper.checkEmailIdExits(email);
        Log.e(TAG, "inputData method  " + b);
        if (b) {
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {

                if (name.isEmpty()) reg_name.setError("Please enter name");
                if (email.isEmpty()) reg_email.setError("Please enter email");
                if (pass.isEmpty()) reg_pass.setError("Please enter password");
            } else {
                Toast.makeText(this, "Email id already exist", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Email id already exist");
            }

        } else {

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                if (name.isEmpty()) reg_name.setError("Please enter name");
                if (email.isEmpty()) reg_email.setError("Please enter email");
                if (pass.isEmpty()) reg_pass.setError("Please enter password");

                if (name.isEmpty() && email.isEmpty() && pass.isEmpty()) {
                    nameError.setVisibility(View.VISIBLE);
                    emailError.setVisibility(View.VISIBLE);
                    PassError.setVisibility(View.VISIBLE);
                } else {
                    nameError.setVisibility(View.GONE);
                    emailError.setVisibility(View.GONE);
                    PassError.setVisibility(View.GONE);
                }
            } else {

//                if (name.length() < 0 && email.length() < 0 && pass.length() < 0) {
//                    if (isRegistrationClickable) {
//                        Toast.makeText(this, "User Register Successfully", Toast.LENGTH_SHORT).show();
//                    } else {
//                        if (name.length() < 0) {
//                            Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show();
//                        }
//                        if (email.length() < 0) {
//                            Toast.makeText(this, "Please enter the email", Toast.LENGTH_SHORT).show();
//                        }
//                        if (pass.length() < 0) {
//                            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }


                //save to db
                String timeStamp = "" + System.currentTimeMillis();

                if (imageUri == null) {

                    Log.e(TAG,"User gender is "+userGender);

                    // Check the gender and set the appropriate image
                    if (userGender.equalsIgnoreCase("Male")) {

                        // Replace with your male image resource
                        imageUri = Uri.parse("android.resource://com.example.sqlitedatabasedemo1/" + R.drawable.man);
                        Log.e(TAG, "Male images set " + imageUri);

                        long id = dbHelper.insertRecord("" + name, "" + imageUri, "" + email, "" + pass, "" + gender);
                        Log.e(TAG, "if male images set " + imageUri);

                        Toast.makeText(this, "Record Added against ID " + id, Toast.LENGTH_SHORT).show();

                    } else if (userGender.equalsIgnoreCase("Female")) {

                        // replace with your female image resource
                        imageUri = Uri.parse("android.resource://com.example.sqlitedatabasedemo1/" + R.drawable.human);
                        Log.e(TAG, "Female images set " + imageUri);

                        long id = dbHelper.insertRecord("" + name, "" + imageUri, "" + email, "" + pass, "" + gender);
                        Log.e(TAG, "if female images set " + imageUri);

                        Toast.makeText(this, "Record Added against ID " + id, Toast.LENGTH_SHORT).show();

                    } else if (userGender.equalsIgnoreCase("Others")) {

                        // replace with your Others image resource
                        imageUri = Uri.parse("android.resource://com.example.sqlitedatabasedemo1/" + R.drawable.ni);
                        Log.e(TAG,"Other images set " + imageUri);

                        long id = dbHelper.insertRecord("" + name, "" + imageUri, "" + email, "" + pass, "" + gender);
                        Log.e(TAG, "if Other images set " + imageUri);

                        Toast.makeText(this, "Record Added against ID " + id, Toast.LENGTH_SHORT).show();

                    } else {
                        // Set a default image if no gender is selected
                        imageUri = Uri.parse("android.resource://com.example.sqlitedatabasedemo1/" + R.drawable.user_img);
                        Log.e(TAG, "if drawable images set " + imageUri);

                        long id = dbHelper.insertRecord("" + name, "" + imageUri, "" + email, "" + pass, "" + gender);
                        Log.e(TAG, " if images set " + imageUri);

                        Toast.makeText(this, "Record Added against ID " + id, Toast.LENGTH_SHORT).show();
                    }
                }
                 else {

                    long id = dbHelper.insertRecord("" + name, "" + imageUri, "" + email, "" + pass, "" + gender);
                    Log.e(TAG, "else images set " + imageUri);

                    Toast.makeText(this, "Record Added against ID ", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }


        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.man, null);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;

        Bitmap largeIcon = bitmapDrawable.getBitmap();


        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification;

        Intent iNotify = new Intent(this, MainActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(this, REQ_CODE, iNotify, PendingIntent.FLAG_IMMUTABLE);

        //Big Picture Style
        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle()
                .bigPicture(((BitmapDrawable)(ResourcesCompat.getDrawable(getResources(), R.drawable.man, null))).getBitmap())
                .bigLargeIcon(largeIcon)
                .setBigContentTitle("Image send By Raman")
                .setSummaryText("Image Message");

        //Inbox Style

        Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                .addLine("A")
                .addLine("B")
                .addLine("C")
                .addLine("D")
                .addLine("E")
                .addLine("F")
                .addLine("G")
                .addLine("H")
                .addLine("I")
                .addLine("J")
                .addLine("K")
                .setBigContentTitle("Full Message")
                .setSummaryText("Summary Message");


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.man)
                    .setContentText("Show message")
                    .setSubText("User register successfully")
                    .setContentIntent(pi)
                    .setStyle(bigPictureStyle)
                    .setAutoCancel(true)   // AA notifiction mate impotent chhe
//                    .setAutoCancel(false)  //aane false rakhvathi notication click karta pun nai jay
                    .setChannelId(CHANNEL_ID)
                    .build();  // notifiaiton BUIlder karva matek
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH));
        } else {
            notification = new Notification.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.man)
                    .setContentText("Show message")
                    .setSubText("User register successfully")
                    .setStyle(bigPictureStyle)
                    .setAutoCancel(true)   // AA notifiction mate impotent chhe
                    .setContentIntent(pi)
                    .build();  // notifiaiton BUIlder karva matek
        }
        nm.notify(NOTIFICATION_ID, notification);
    }

    @SuppressLint("ResourceType")
    private void passWordCheck() {

        String name = reg_name.getText().toString(),
                email = reg_email.getText().toString(),
                password = reg_pass.getText().toString();
        Log.e(TAG,"reg name "+name);
        Log.e(TAG,"reg email "+email);
        Log.e(TAG,"reg pass "+password);

        // for password length 8
        if (password.length() >= 8) {
            isAtLeast8 = true;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            isAtLeast8 = false;
            cardOne.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for uppercase password
        if (password.matches("(.*[A-Z].*)")) {
            hasUppercase = true;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasUppercase = false;
            cardTwo.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //For number
        if (password.matches("(.*[0-9].*)")) {
            hasNumber = true;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasNumber = false;
            cardThree.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }

        //for symbol
        if (password.matches("^(?=.*[_.()@#$%^&+=-]).*$")) {
            hasSymbol = true;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
        } else {
            hasSymbol = false;
            cardFour.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
        }
        checkAllData(name,email,password);
    }

    // if all fields are filled properly the button color will change
    @SuppressLint("ResourceType")
    private void checkAllData(String name, String email, String password) {
        Log.e(TAG, "checkAllData: ");
        if (isAtLeast8 && hasUppercase && hasNumber && hasSymbol && name.length() > 0 && email.length() > 0 && password.length() > 0) {
            isRegistrationClickable = true;
//            reg_btn.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            if (!name.isEmpty() && !email.isEmpty()){
                reg_btn.setClickable(true);
                reg_btn.setCardBackgroundColor(Color.parseColor(getString(R.color.colorAccent)));
            }
//            else {
//                reg_btn.setClickable(false);
//                reg_btn.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
//            }
        } else {
            isRegistrationClickable = false;
            reg_btn.setCardBackgroundColor(Color.parseColor(getString(R.color.colorDefault)));
//            Toast.makeText(this, "Please Should Be all Password Details", Toast.LENGTH_SHORT).show();
            reg_btn.setClickable(false);


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
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {

        //request Storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {

        // check if Storage  permission is enabled or not
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

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
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //picked from camera
                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //crop image recieved
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    //image cropped successfully
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    // set image to imageview
                    reg_image.setImageURI(resultUri);

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //error while cropping
                    Exception error = result.getError();
                    Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}