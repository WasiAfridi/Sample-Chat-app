package com.example.samplechat.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplechat.R;
import com.example.samplechat.databinding.RowLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;
    ArrayList<Message> messageArrayList;

    public UserAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = userArrayList.get(position);
//        Message message = messageArrayList.get(position);
        holder.rowLayoutBinding.txtName.setText(user.getName());
//        holder.rowLayoutBinding.txtLastMessage.setText(message.getMessage());
//        holder.rowLayoutBinding.txtLastMessage.setText(user.getEmail());
//        holder.rowLayoutBinding.txtTime.setText(String.valueOf(message.getTimeStamp()));
        Picasso.get().load(user.getProfileImage()).into(holder.rowLayoutBinding.profileImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("image",user.getProfileImage());
                intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RowLayoutBinding rowLayoutBinding;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rowLayoutBinding = RowLayoutBinding.bind(itemView);
        }
    }
}
