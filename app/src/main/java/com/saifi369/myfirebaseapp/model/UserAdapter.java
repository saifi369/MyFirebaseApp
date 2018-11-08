package com.saifi369.myfirebaseapp.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.saifi369.myfirebaseapp.DetailsActivity;
import com.saifi369.myfirebaseapp.R;
import com.saifi369.myfirebaseapp.network.Utils;

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

        holder.tvName.setText(user.getName());
        holder.tvAge.setText(String.valueOf(user.getAge()));
        holder.tvProfession.setText(user.getProfession());
        holder.tvCountry.setText(user.getCity());


        holder.parentView.setOnClickListener(v -> {
            String uid=user.getUid();
            Intent intent=new Intent(mContext,DetailsActivity.class);
            intent.putExtra(USER_KEY,uid);
            mContext.startActivity(intent);
        });

        holder.parentView.setOnLongClickListener(v -> {

            String userID=user.getUid();

            Task<Void> voidTask = Utils.removeUser(userID);

            voidTask.addOnSuccessListener(aVoid -> Toast.makeText(mContext, "User removed from database...", Toast.LENGTH_SHORT).show());
            voidTask.addOnFailureListener(aVoid-> Toast.makeText(mContext, "User not removed..", Toast.LENGTH_SHORT).show());

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvAge;
        TextView tvProfession;
        TextView tvCountry;
        View parentView;

        public MyViewHolder(View itemView) {
            super(itemView);
            parentView=itemView;
            tvName=itemView.findViewById(R.id.tv_name);
            tvAge=itemView.findViewById(R.id.tv_age);
            tvCountry=itemView.findViewById(R.id.tv_country);
            tvProfession=itemView.findViewById(R.id.tv_profession);
        }
    }
}
