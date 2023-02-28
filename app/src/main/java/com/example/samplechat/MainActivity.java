package com.example.samplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.samplechat.chat.User;
import com.example.samplechat.chat.UserAdapter;
import com.example.samplechat.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    FirebaseDatabase database;
    ArrayList<User> usersList;
    UserAdapter userAdapter;
    FirebaseAuth auth;
    DatabaseReference dbReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        usersList = new ArrayList<>();


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference().child("users");

        if(auth.getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        }

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                    usersList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userAdapter = new UserAdapter(MainActivity.this, usersList);
        mainBinding.conversationsRecyclerView.setAdapter(userAdapter);

    }
}