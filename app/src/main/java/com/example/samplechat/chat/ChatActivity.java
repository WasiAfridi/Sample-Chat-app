package com.example.samplechat.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.samplechat.MainActivity;
import com.example.samplechat.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding chatBinding;

    String senderRoom, receiverRoom;
    String name, receiverUid, receiverImage, senderUid, senderImage, recImage;
    ArrayList<Message> messageArrayList;
    MessageAdapter messageAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference dbReference,chatReference;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,500);

        messageArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this,messageArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        chatBinding.chatRecyclerView.setAdapter(messageAdapter);

        chatBinding.txtBackslash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
            }
        });

        name = getIntent().getStringExtra("name");
        receiverUid = getIntent().getStringExtra("uid");
        receiverImage = getIntent().getStringExtra("image");

        senderUid = firebaseAuth.getUid();

        Picasso.get().load(receiverImage).into(chatBinding.circleImageView);
        chatBinding.txtReceiverName.setText(""+name);
       // chatBinding.RImageView.setImageURI();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        dbReference = database.getReference().child("users").child(firebaseAuth.getUid());
        chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Message message = dataSnapshot.getValue(Message.class);
                    messageArrayList.add(message);
                }
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImage = snapshot.child("profileImage").getValue().toString();
                recImage = receiverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        senderRoom = senderUid + receiverUid;
//        receiverRoom = receiverUid + senderUid;
//
        chatBinding.txtSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = chatBinding.edtxtTypeMessage.getText().toString().trim();
                chatBinding.edtxtTypeMessage.setText("");
                Date date = new Date();
                Long time = date.getTime();

                Message text = new Message(textMessage,senderUid, time);

                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .push()
                                .setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });

            }
        });




    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            ChatActivity.this.mHandler.postDelayed(m_Runnable, 500);
        }

    };//runnable


    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(m_Runnable);
        finish();

    }
}