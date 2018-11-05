package com.saifi369.myfirebaseapp.network;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Utils {

    public static Task<Void> removeUser(String userId){

        Task<Void> task = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .setValue(null);

        return task;
    }

}
