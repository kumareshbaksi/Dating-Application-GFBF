package com.example.gfbf;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    CircleImageView Image;
    TextView Name,Sex,Hight,Age,Profession,likesdisplayes;
    ImageView likeButton;
    int likesCount;
    DatabaseReference likeReference;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        Image  = itemView.findViewById(R.id.Image);
        Name  = itemView.findViewById(R.id.Name);
        Age  = itemView.findViewById(R.id.Age);
        Hight  = itemView.findViewById(R.id.Hight);
        Sex  = itemView.findViewById(R.id.Sex);
        Profession  = itemView.findViewById(R.id.Profession);




    }

    public void setLikeButtonStatus(final String post_key){

        likeButton = itemView.findViewById(R.id.likeButton);
        likesdisplayes = itemView.findViewById(R.id.like_textviews);
        likeReference = FirebaseDatabase.getInstance().getReference("Likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String User_Id = user.getUid();
        String likes = "Likes";

        likeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(post_key).hasChild(User_Id)){
                    likesCount = (int)(snapshot.child(post_key).getChildrenCount());
                    likeButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                    likesdisplayes.setText(Integer.toString(likesCount));
                }else{
                    likesCount = (int)(snapshot.child(post_key).getChildrenCount());
                    likeButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    likesdisplayes.setText(Integer.toString(likesCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
