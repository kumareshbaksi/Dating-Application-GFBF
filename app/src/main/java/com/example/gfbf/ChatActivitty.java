package com.example.gfbf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivitty extends AppCompatActivity {

    Toolbar toolbar;
    ImageView sendMassageButton;
    EditText typeMassage;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    RecyclerView chatrecyclearview;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activitty);

        toolbar = findViewById(R.id.chat_toolbar);
        TextView toolbarTitle = findViewById(R.id.title);
        ImageView back_arrow = findViewById(R.id.back_arrow);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivitty.this,ProfileViewActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            }
        });


        String userName = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");

        toolbar.setTitle("");
        toolbarTitle.setText(userName);

        setSupportActionBar(toolbar);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        } else {
            AlertDialog.Builder aleart = new AlertDialog.Builder(ChatActivitty.this);
            aleart.setTitle("Sorry");
            aleart.setMessage("You Are Not Connected With Network ");
            aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            aleart.create().show();
        }

        sendMassageButton = findViewById(R.id.sendMassageButton);
        typeMassage = findViewById(R.id.typeMaassage);
        chatrecyclearview = findViewById(R.id.private_massage_list);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();






        final  ArrayList<MassagesModels> massagesModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter( massagesModels , this);
        chatrecyclearview.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatrecyclearview.setLayoutManager(layoutManager);

        String senderId = firebaseAuth.getCurrentUser().getUid();
        String receiveId = getIntent().getStringExtra("post_key");

        String senderRoom = senderId + receiveId;
        String receiverRoom = receiveId + senderId;





        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        massagesModels.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){

                            MassagesModels models = snapshot1.getValue(MassagesModels.class);
                            massagesModels.add(models);

                        }
                        chatAdapter.notifyDataSetChanged();
                        chatrecyclearview.smoothScrollToPosition(chatrecyclearview.getAdapter().getItemCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        sendMassageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String massage = typeMassage.getText().toString();
                final MassagesModels model = new MassagesModels(senderId , massage);
                typeMassage.setText("");
                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .push()
                                .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });

            }
        });


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}