package com.example.loginapp.Boundary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * This class is to handle the login process of all users by calling Firebase authenticator
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    String uid;
    Boolean isAdmin;
    Boolean isDisabled;
    Boolean isClinicAdmin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * This function is used to validate if email and password exist in the user's database
             * Also checks for empty fields when user is trying to log in, by returning appropriate message
             */
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email field must not be empty.");
                    return;
                }

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userNameRef = rootRef.child("Users");
                Query queries=userNameRef.orderByChild("email").equalTo(email);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            mEmail.setError("email does not exist in database");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                queries.addListenerForSingleValueEvent(eventListener);

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password field must not be empty.");
                    return;
                }
                if (password.length() < 6) {
                    mPassword.setError("Password must be >= 6 characters.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            uid = fAuth.getCurrentUser().getUid();
                            Log.d("TAG", uid);

                            if (firebaseUser.isEmailVerified()) {
                                DatabaseReference User = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                                User.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    isDisabled = snapshot.child("Disabled").getValue(Boolean.class);
                                    if(isDisabled==null){
                                        isDisabled = snapshot.child("disabled").getValue(Boolean.class);
                                    }
                                    isAdmin = snapshot.child("Admin").getValue(Boolean.class);
                                    if(isAdmin==null){
                                        isAdmin = snapshot.child("admin").getValue(Boolean.class);
                                    }
                                    isClinicAdmin = snapshot.child("ClinicAdmin").getValue(Boolean.class);
                                    if(isClinicAdmin==null){
                                        isClinicAdmin = snapshot.child("clinicAdmin").getValue(Boolean.class);
                                    }

                                    if (isAdmin == true) {
                                        Toast.makeText(getApplicationContext(), "Welcome Admin", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), mainactivityAdmin.class));
                                    }

                                    else if (isClinicAdmin != null && isClinicAdmin == true) {
                                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                        startActivity(new Intent(getApplicationContext(), Clinic_admin_page.class));
                                    }


                                    else if (isDisabled == false) {
                                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));


                                    }
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                                        builder.setMessage("Your account has been disabled");

                                            builder.setPositiveButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                                            finish();
                                                        }
                                                    });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            } else {
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(getApplicationContext(), "check your email to verify your acccount", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

}





