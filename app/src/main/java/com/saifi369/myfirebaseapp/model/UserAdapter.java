package com.saifi369.myfirebaseapp.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.saifi369.myfirebaseapp.DetailsActivity;
import com.saifi369.myfirebaseapp.MainActivity;
import com.saifi369.myfirebaseapp.R;
import com.saifi369.myfirebaseapp.network.NetworkUtils;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    public static final String USER_KEY = "user_key";
    private Context mContext;
    private List<User> mDataList;

    public UserAdapter(Context mContext, List<User> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView=LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user=mDataList.get(position);

        holder.textView.setText(user.getName()+ "  |  "+user.getAge());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=user.getUid();
                Intent intent=new Intent(mContext,DetailsActivity.class);
                intent.putExtra(USER_KEY,uid);
                mContext.startActivity(intent);
            }
        });

        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //this line returns true
//                boolean remove = mDataList.contains(user);
                //if I try to remove the user here, its removed
                //this line works as I want
//                mDataList.remove(user);
//                Log.d(MainActivity.TAG, "updateDataSet: Contains: "+remove);

                Task<Void> removeUser = NetworkUtils.removeUser(user.getUid());

                removeUser.addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "User removed", Toast.LENGTH_SHORT).show();
                        //mDataList.remove(user);
                        //notifyDataSetChanged();
                    }
                });

                return true;
            }
        });

    }

//    public void updateDataSet(User user){
//        //this also returns false even the data of this user id displayed
//        boolean contains=mDataList.contains(user);
//        //this also does not works
//        mDataList.remove(user);
//        this.notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
        }
    }
}
