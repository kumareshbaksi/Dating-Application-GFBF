package com.example.gfbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileViewActivity extends AppCompatActivity {
    TextView uName,uHeight,uSex,uAge,uPropose,uBio,uPassion;
    CircleImageView profile1,profile2;
    Button closeBtn,chatBtn;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        uName = findViewById(R.id.uName);
        uHeight = findViewById(R.id.uHeight);
        uSex = findViewById(R.id.uSex);
        uAge = findViewById(R.id.uAge);
        uPropose = findViewById(R.id.uPropose);
        uBio = findViewById(R.id.uBio);
        uPassion = findViewById(R.id.uPassion);
        profile1 = findViewById(R.id.profile1);
        closeBtn = findViewById(R.id.closeBtn);
        chatBtn = findViewById(R.id.chatBtn);
        firebaseAuth = FirebaseAuth.getInstance();


        String name = getIntent().getStringExtra("Name");
        String image = getIntent().getStringExtra("Image");
        String post_key = getIntent().getStringExtra("post_key");
        String current_user = firebaseAuth.getCurrentUser().getUid();



        uName.setText(getIntent().getStringExtra("Name"));
        uAge.setText(getIntent().getStringExtra("Age")+ "+");
        uHeight.setText(getIntent().getStringExtra("Height")+ " feet");
        uSex.setText(getIntent().getStringExtra("Sex"));
        uBio.setText(getIntent().getStringExtra("Bio"));
        uPassion.setText(getIntent().getStringExtra("Passion"));
        uPropose.setText(getIntent().getStringExtra("Propose"));

        Glide.with(ProfileViewActivity.this).load(getIntent().getStringExtra("Image")).into(profile1);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("chats_list").child(current_user);

                databaseReference.setValue(post_key).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ProfileViewActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(ProfileViewActivity.this,ChatActivitty.class);
                intent.putExtra("name",name);
                intent.putExtra("image",image);
                intent.putExtra("post_key",post_key);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);


            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}