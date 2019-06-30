package com.saifi369.myfirebaseapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MyTag";
    private static final int PICK_IMAGE_REQUEST = 1001;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private Button mSubmitButton, mReadData;
    private EditText mInputText, mInputNum;
    private TextView mOutputText, mProgressText;
    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private boolean mGranted;
    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        this.mSubmitButton.setOnClickListener(this::runCode);
        this.mReadData.setOnClickListener(this::readData);

        mRef = FirebaseStorage.getInstance().getReference("docs/");

        getPermissions();

    }

    private void getPermissions() {

        String externalReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE.toString();
        String externalWritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE.toString();

        if (ContextCompat.checkSelfPermission(this, externalReadPermission) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, externalWritePermission) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{externalReadPermission, externalWritePermission}, STORAGE_PERMISSION_CODE);
            }
        }

    }

    private void readData(View view) {

        mRef.child("images/91").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Log.d(TAG, "onSuccess: download url: " + uri.toString());
                        Toast.makeText(MainActivity.this, "URL received", Toast.LENGTH_SHORT).show();

                        downloadFile(uri);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Error: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void downloadFile(Uri uri) {

        File file = new File(Environment.getExternalStorageDirectory(), "mypdffile.pdf");

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle("File Download")
                .setDescription("This is file download demo")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file));

        downloadManager.enqueue(request);

    }

    private void runCode(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);

    }

    private Bitmap readImage() {

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("lahore.jpg");

            BitmapDrawable bitmapDrawable = (BitmapDrawable) Drawable.createFromStream(inputStream, null);

            return bitmapDrawable.getBitmap();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void initViews() {
        this.mSubmitButton = findViewById(R.id.btn_run_code);
        this.mReadData = findViewById(R.id.button2);
        this.mInputText = findViewById(R.id.et_name);
        this.mInputNum = findViewById(R.id.et_num);
        this.mOutputText = findViewById(R.id.output_text);
        mRecyclerView = findViewById(R.id.user_recyclerview);
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressText = findViewById(R.id.tv_progress);
        mImageView = findViewById(R.id.image_view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                mGranted = true;
            } else {
                Toast.makeText(this, "Please allow the permission to read data", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null) {

            Uri imageUri = data.getData();
            StorageReference storageReference = mRef.child("images/" + imageUri.getLastPathSegment());

            UploadTask uploadTask = storageReference.putFile(imageUri);
            File file = new File(imageUri.toString());
            String fileName = file.getName();
            Log.d(TAG, "onActivityResult: filename: " + fileName);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setIndeterminate(false);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mProgressBar.setProgress((int) progress, true);
                    }
                    mProgressText.setText(progress + " %");
                    Log.d(TAG, "onProgress: file " + progress + " % uploaded");

                }
            })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            mProgressText.append(" File Uploaded");
                        }
                    });
        }
    }

}