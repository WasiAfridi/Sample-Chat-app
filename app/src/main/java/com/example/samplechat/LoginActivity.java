package com.example.samplechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.samplechat.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;
    FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

//        progressDialog =new ProgressDialog(this);
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

        loginBinding.btnClickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // progressDialog.show();

                String email = loginBinding.txtLoginEmail.getText().toString();
                String password = loginBinding.txtLoginPassword.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                   // progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Email or Password is Missing", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                   // progressDialog.dismiss();
                    loginBinding.txtLoginEmail.setError("Please Enter a Valid Email");
                    Toast.makeText(LoginActivity.this, "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show();
                }else if(password.length()<6){
                  //  progressDialog.dismiss();
                    loginBinding.txtLoginPassword.setError("Password should be atLeast 6 character");
                    Toast.makeText(LoginActivity.this, "Password should be atLeast 6 character", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                Toast.makeText(LoginActivity.this, "User Loged In", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}