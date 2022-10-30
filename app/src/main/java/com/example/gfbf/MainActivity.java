package com.example.gfbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    EditText etEmail, etPassword;
    Button btnregister, btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnlogin = findViewById(R.id.btnLogin);
        btnregister = findViewById(R.id.btnRegister);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Creating Your Account");

        ProgressDialog dialog1 = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog1.setMessage("Logging To Your Account");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                connectivityManager.getNetworkInfo(
                        ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        } else {
            AlertDialog.Builder aleart = new AlertDialog.Builder(MainActivity.this);
            aleart.setTitle("Sorry");
            aleart.setMessage("You Are Not Connected With Network ");
            aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            aleart.create().show();
        }


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    dialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                firebaseAuth = FirebaseAuth.getInstance();
                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                String User_Id = firebaseAuth.getCurrentUser().getUid();

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("password", password);

                                firebaseDatabase.getReference().child("Users Register Data").child(User_Id).setValue(hashMap);
                                dialog.dismiss();

                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        showDialog();
                                    }
                                });

                            } else {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "fill The Blanks", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    dialog1.show();
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog1.dismiss();
                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    Toast.makeText(MainActivity.this, "Successfully Login", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Please Verify Your E-mail Id", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                dialog1.dismiss();
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Fill the blanks", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDialog() {
        AlertDialog.Builder aleart = new AlertDialog.Builder(MainActivity.this);
        aleart.setTitle("Email Sent");
        aleart.setMessage("Please verify your e-mail id before login");
        aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        aleart.create().show();
    }

}