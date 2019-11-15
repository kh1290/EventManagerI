package com.example.eventmanageri.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eventmanageri.Database;
import com.example.eventmanageri.R;
import com.example.eventmanageri.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText username, password;
    Button btnSignUp;
    TextView tvSignin;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignin = findViewById(R.id.textView2);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = username.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    username.setError("Please enter email ID");
                    username.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password. It must be over 6");
                    username.requestFocus();
                }
                else if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                User user = new User();
                                user.setEmail(email);

                                new Database().addUser(user, new Database.UserStatus() {
                                    @Override
                                    public void UserIsInserted() {
                                        Toast.makeText(SignUpActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            };
                        };
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

    }
}
