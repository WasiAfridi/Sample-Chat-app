package com.example.samplechat.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.samplechat.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Message> messageArrayList;
    int ITEM_SENT=1;
    int ITEM_RECEIVED=2;

    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_recieved, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.txtSent.setText(message.getMessage());
        }else{
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;
            receiverViewHolder.txtReceived.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }else{
            return ITEM_RECEIVED;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView txtSent;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSent = itemView.findViewById(R.id.messageSent);
        }
    }

    public  class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView txtReceived;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReceived = itemView.findViewById(R.id.messageReceived);
        }
    }

}
