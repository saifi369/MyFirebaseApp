package com.saifi369.myfirebaseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saifi369.myfirebaseapp.model.User;
import com.saifi369.myfirebaseapp.model.UserAdapter;

public class DetailsActivity extends AppCompatActivity {
    private TextView mOutputText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mOutputText=findViewById(R.id.show_text);

        String uid=getIntent().getStringExtra(UserAdapter.USER_KEY);
        DatabaseReference databaseReference=
                FirebaseDatabase.getInstance().getReference("users").child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                mOutputText.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
