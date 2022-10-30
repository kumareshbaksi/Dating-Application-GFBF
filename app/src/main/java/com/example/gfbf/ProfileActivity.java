package com.example.gfbf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView uProfile;
    EditText uName , uSex , uBio , uHeight , uAge , uPassion , uPropose;
    Button btnClose , btnSave;
    Uri imageUri = null;
    StorageReference storageReference;
    String User_Id;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    Uri ProfileImageUri;
    Uri ProfileImageUri2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        User_Id = firebaseAuth.getCurrentUser().getUid();

        uName = findViewById(R.id.uName);
        uBio = findViewById(R.id.uBio);
        uSex = findViewById(R.id.uSex);
        uHeight = findViewById(R.id.uHeight);
        uAge = findViewById(R.id.uAge);
        uPassion = findViewById(R.id.uPassion);
        uPropose = findViewById(R.id.uPropose);
        btnClose = findViewById(R.id.btnClose);
        btnSave = findViewById(R.id.btnSave);
        uProfile = findViewById(R.id.uProfile);

        uProfile.setImageURI(imageUri);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        } else {
            AlertDialog.Builder aleart = new AlertDialog.Builder(ProfileActivity.this);
            aleart.setTitle("Sorry");
            aleart.setMessage("You Are Not Connected With Network ");
            aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            aleart.create().show();
        }




        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(User_Id);

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uName.setText(snapshot.child("name").getValue(String.class));
                uAge.setText(snapshot.child("age").getValue(String.class));
                uBio.setText(snapshot.child("bio").getValue(String.class));
                uHeight.setText(snapshot.child("height").getValue(String.class));
                uPassion.setText(snapshot.child("passion").getValue(String.class));
                uPropose.setText(snapshot.child("propose").getValue(String.class));
                uSex.setText(snapshot.child("sex").getValue(String.class));

                RequestOptions imageHolder = new RequestOptions();
                imageHolder.placeholder(R.drawable.defaultprofile);

                Glide.with(ProfileActivity.this).setDefaultRequestOptions(imageHolder).load(snapshot.child("ProfileImageUri").getValue(String.class)).into(uProfile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        uProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    } else {
                        gallaryIntent();
                    }

                } else {
                    //For Bellow M Version
                    gallaryIntent();

                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    int profilePicture = (R.drawable.defaultprofile);

                    ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
                    dialog.setTitle("Loading");
                    dialog.setMessage("Updating Profile");

                    databaseReference = firebaseDatabase.getReference().child("Users");

                    String name = uName.getText().toString();
                    String age = uAge.getText().toString();
                    String height = uHeight.getText().toString();
                    String sex = uSex.getText().toString();
                    String bio = uBio.getText().toString();
                    String passion = uPassion.getText().toString();
                    String propose = uPropose.getText().toString();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(height) && !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(bio) && !TextUtils.isEmpty(passion) && !TextUtils.isEmpty(propose) && ProfileImageUri != null) {
                    dialog.show();
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("name", name);
                    hashMap.put("age", age);
                    hashMap.put("height", height);
                    hashMap.put("sex", sex);
                    hashMap.put("bio", bio);
                    hashMap.put("passion", passion);
                    hashMap.put("propose", propose);
                    hashMap.put("ProfileImageUri", ProfileImageUri.toString());


                    databaseReference.child(User_Id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfileActivity.this, MainScreenActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    AlertDialog.Builder aleart = new AlertDialog.Builder(ProfileActivity.this);
                    aleart.setTitle("Sorry");
                    aleart.setMessage("Please Update your Profile & Fill The Blanks");
                    aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    aleart.create().show();
                }





                }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this,MainScreenActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

    }



    private void gallaryIntent() {
        Intent gallary = new Intent();
        gallary.setType("image/*");
        gallary.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallary, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Updating Profile Picture");


        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uProfile.setImageURI(imageUri);
            dialog.show();

            StorageReference image_path = storageReference.child("Profile_Image").child(User_Id + ".jpg");
            image_path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    image_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            dialog.dismiss();
                            ProfileImageUri = uri;
                            showDialog();
                        }
                    });
                }
            });
        }



    }
    private void showDialog() {
        androidx.appcompat.app.AlertDialog.Builder aleart = new AlertDialog.Builder(ProfileActivity.this);
        aleart.setTitle("Picture Updated");
        aleart.setMessage("Click on save button to view your profile");
        aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        aleart.create().show();
    }

    @Override
    public void finish() {
        super.finish();

        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}