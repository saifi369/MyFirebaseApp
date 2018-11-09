package com.saifi369.myfirebaseapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MyTag";
    private static final int PICK_IMAGE_REQUEST = 1001;
    private Button mSubmitButton, mReadData;
    private EditText mInputText, mInputNum;
    private TextView mOutputText;
    private RecyclerView mRecyclerView;

    private StorageReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        this.mSubmitButton.setOnClickListener(this::runCode);
        this.mReadData.setOnClickListener(this::readData);

        mRef = FirebaseStorage.getInstance().getReference("docs/");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void readData(View view) {



    }


    private void runCode(View view) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(getFilesDir(),"lahore.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = mRef.child("images/lahore.jpg").putStream(inputStream);

        InputStream finalInputStream = inputStream;
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                if (finalInputStream != null) {
                    try {
                        finalInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Toast.makeText(MainActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        });


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
    }

}