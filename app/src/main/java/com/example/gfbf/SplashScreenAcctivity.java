package com.example.gfbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

import static java.util.jar.Attributes.*;

public class SplashScreenAcctivity extends AppCompatActivity {

    ImageView iv_heart;
    CharSequence charSequence;
    int index;
    long delay = 200;
    Handler handler = new Handler();
    TextView wlc_msg;
    String Name;
    boolean connected = false;
    DatabaseReference databaseReference;
    String User_Id;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_acctivity);

        iv_heart = findViewById(R.id.iv_heart);
        wlc_msg = findViewById(R.id.wlc_msg);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();




        if (firebaseAuth.getCurrentUser() != null) {

            User_Id = firebaseAuth.getCurrentUser().getUid();
            databaseReference = firebaseDatabase.getReference().child("Users").child(User_Id).child("name");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Name = snapshot.getValue(String.class);

                    if (firebaseAuth.getCurrentUser() != null) {
                        wlc_msg.setText("Welcome! " + Name);
                    } else {
                        wlc_msg.setText("Welcome! Someone is waiting for you");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        } else {
            AlertDialog.Builder aleart = new AlertDialog.Builder(SplashScreenAcctivity.this);
            aleart.setTitle("Sorry");
            aleart.setMessage("You Are Not Connected With Network ");
            aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            aleart.create().show();
        }



        // For Full Screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                iv_heart,
                PropertyValuesHolder.ofFloat("scaleX",1.2f),
                PropertyValuesHolder.ofFloat("scaleY",1.2f)
        );

        objectAnimator.setDuration(1000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();


        Thread thread = new Thread(){
            public  void  run(){
                try{
                    sleep(5000);
                }
                catch (Exception e){
                    e.printStackTrace();

                }
                finally {
                    if(firebaseAuth.getCurrentUser()  != null){
                        Intent intent = new Intent(SplashScreenAcctivity.this,MainScreenActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(SplashScreenAcctivity.this,MainActivity.class);
                        startActivity(intent);

                    }
                }finish();

            }
        };thread.start();
}
}

