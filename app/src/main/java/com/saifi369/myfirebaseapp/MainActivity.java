package com.saifi369.myfirebaseapp;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.saifi369.myfirebaseapp.model.User;
import com.saifi369.myfirebaseapp.model.UserAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MyTag";
    private Button mSubmitButton, mReadData;
    private EditText mInputText,mInputNum;
    private TextView mOutputText;
    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ChildEventListener mChildListener;
    private List<User> mDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("users");

        this.mSubmitButton.setOnClickListener(this::runCode);
        this.mReadData.setOnClickListener(this::readData);
        mDataList=new ArrayList<>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter=new UserAdapter(this,mDataList);
        mRecyclerView.setAdapter(mUserAdapter);

        mChildListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                User user=dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());

                mDataList.add(user);
                mUserAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());

//                int position = mDataList.indexOf(user);
                mDataList.remove(user);
                mUserAdapter.notifyDataSetChanged();

//                mUserAdapter.updateDataSet(user);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mRef.addChildEventListener(mChildListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRef.removeEventListener(mChildListener);
    }

    private void readData(View view) {
        //read data here

    }


    private void runCode(View view) {
        //insert data here

        String name = mInputText.getText().toString();
        int age = Integer.parseInt(mInputNum.getText().toString());

        User user=new User(name,age);

        String key=mRef.push().getKey();
        mRef.child(key).setValue(user);

        Toast.makeText(this, "Data inserted...", Toast.LENGTH_SHORT).show();
        
    }


    private void initViews() {
        this.mSubmitButton = findViewById(R.id.btn_run_code);
        this.mReadData = findViewById(R.id.button2);
        this.mInputText = findViewById(R.id.et_name);
        this.mInputNum = findViewById(R.id.et_num);
        this.mOutputText = findViewById(R.id.output_text);
        mRecyclerView=findViewById(R.id.user_recyclerview);
    }
}
