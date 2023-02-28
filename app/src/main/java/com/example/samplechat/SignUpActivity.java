package com.example.samplechat;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;

//import android.app.ProgressDialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
//import android.widget.EditText;
import android.widget.Toast;

import com.example.samplechat.chat.User;
import com.example.samplechat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ActivitySignUpBinding signUpBinding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    int PICK_IMAGE = 10;
    Uri imageUri;
    String strImageUri;
   // ProgressDialog progressDialog;
//    EditText edName,edEmail,edPassword,edConfirmPassword;
//    AppCompatButton btnSignUp;

  //  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());
//
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        // firestore = FirebaseFirestore.getInstance();
//        edName = findViewById(R.id.txtSignUpName);
//        edEmail = findViewById(R.id.txtSignUpEmail);
//        edPassword = findViewById(R.id.txtPassword);
//        btnSignUp = findViewById(R.id.btnSignUp);

//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Loading...");

        signUpBinding.btnClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signUpBinding.setProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });



        signUpBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressDialog.show();

                String name = signUpBinding.txtSignUpName.getText().toString();
//                String phone = signUpBinding.txtPhoneNumber.getText().toString().trim();
                String email = signUpBinding.txtSignUpEmail.getText().toString();
                String password = signUpBinding.txtSignUpPassword.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                   // progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "All Fields Required!!!", Toast.LENGTH_SHORT).show();


                } else if (!email.matches(emailPattern)) {
                  //  progressDialog.dismiss();
                    signUpBinding.txtSignUpEmail.setError("Please Enter a valid email address");
                    Toast.makeText(SignUpActivity.this, "Email Address Is Not Valid! Please Enter a Valid One", Toast.LENGTH_SHORT).show();

                } else if (password.length() < 6) {
                  //  progressDialog.dismiss();
                    signUpBinding.txtSignUpPassword.setError("Password Cannot Be Less Than Six Characters");
                    Toast.makeText(SignUpActivity.this, "Password Cannot Be Less Than Six Characters", Toast.LENGTH_SHORT).show();
                } else {

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                DatabaseReference dbReference = database.getReference().child("users").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("profileimages").child(auth.getUid());

                                if(imageUri!=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        strImageUri = uri.toString();
                                                        User user = new User(auth.getUid(), name, email, strImageUri);
                                                        dbReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                                                }else{
                                                                    Toast.makeText(SignUpActivity.this, "While Creating User. Something Went Wrong", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });

                                }else{
                                    strImageUri = "https://firebasestorage.googleapis.com/v0/b/samplechat-1b125.appspot.com/o/profileImage.jpg?alt=media&token=f5ae331c-2bd7-448d-8cac-51d41c91ee3e";
                                    User user = new User(auth.getUid(), name, email, strImageUri);
                                    dbReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                              //  progressDialog.dismiss();
                                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                            }else{
                                                Toast.makeText(SignUpActivity.this, "While Creating User. Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }else{
                                //progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }


//

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if(data!=null){
                imageUri = data.getData();
                signUpBinding.setProfileImage.setImageURI(imageUri);
            }
        }
    }

}