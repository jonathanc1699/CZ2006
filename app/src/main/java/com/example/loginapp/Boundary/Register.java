package com.example.loginapp.Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapp.Entity.User;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Register extends AppCompatActivity {
    EditText mFullName, mEmail, mPassword, mConfirmPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mConfirmPassword = findViewById(R.id.ConfirmPassword);
        mRegisterBtn = findViewById(R.id.RegisterBtn);
        mLoginBtn = findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirmpassword = mConfirmPassword.getText().toString().trim();
                String fullName = mFullName.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email field must not be empty.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password field must not be empty.");
                    return;
                }
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userNameRef = rootRef.child("Users");
                Query queries=userNameRef.orderByChild("email").equalTo(email);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            mEmail.setError("email already exists in database");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                queries.addListenerForSingleValueEvent(eventListener);

                if (password.length() < 6){
                    mPassword.setError("Password must be >= 6 characters.");
                    return;
                }
                if (!password.equals(confirmpassword)){
                    mConfirmPassword.setError("Password and Confirm Password fields must be the same.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // Register user into firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User user = new User(fullName, email,0, "nil" ,userId, false , false,false,"nil","nil"); // Set default to 0 and null
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override

                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            fAuth.signOut();
                                                            Toast.makeText(Register.this,
                                                                    "Account has been created successfully. Please verify your email.",
                                                                    Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                            startActivity(new Intent(getApplicationContext(), Login.class));

                                                        } else {
                                                            Toast.makeText(Register.this,
                                                                    "Failed to send verification email.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }


                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}