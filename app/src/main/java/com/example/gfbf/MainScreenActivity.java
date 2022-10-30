package com.example.gfbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



public class MainScreenActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseRecyclerOptions<RecyclearModel> options;
    FirebaseRecyclerAdapter<RecyclearModel , MyViewHolder> adapter;
    RecyclerView recyclerView;
    DatabaseReference databaseReference , likeReference;
    Boolean likeChecker = false;
    Toolbar toolbar;
    ProgressDialog dialog;
    String post_key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Fetching Profiles");

        dialog.show();


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        likeReference = FirebaseDatabase.getInstance().getReference().child("Likes");

        toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.title);

        toolbar.setTitle("");
        toolbarTitle.setText("GFBF");

        setSupportActionBar(toolbar);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||

                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

        } else {
            AlertDialog.Builder aleart = new AlertDialog.Builder(this);
            aleart.setTitle("Sorry");
            aleart.setMessage("You Are Not Connected With Network ");
            aleart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            aleart.create().show();
        }



        recyclerView = findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<RecyclearModel>().setQuery(databaseReference,RecyclearModel.class).build();

        adapter = new FirebaseRecyclerAdapter<RecyclearModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull RecyclearModel model) {

                holder.Name.setText(model.getName());
                holder.Age.setText(model.getAge()+ "+");
                holder.Hight.setText(model.getHeight()+ " feet");
                holder.Sex.setText(model.getSex());
                holder.Profession.setText(model.getPassion());

                Glide.with(holder.Image.getContext()).load(model.getProfileImageUri()).into(holder.Image);

                dialog.dismiss();



                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = user.getUid();
                post_key = getRef(position).getKey();


                holder.setLikeButtonStatus(post_key);
                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        likeChecker = true;

                        likeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(likeChecker.equals(true)){
                                    if (snapshot.child(post_key).hasChild(currentUserId)){
                                        likeReference.child(post_key).child(currentUserId).removeValue();
                                        likeChecker = false;
                                    }
                                    else{
                                        likeReference.child(post_key).child(currentUserId).setValue(true);
                                        likeChecker = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });



                holder.Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfilePicture.class);
                        intent.putExtra("Image",model.getProfileImageUri());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                });

                holder.Name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Age.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Hight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Sex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Profession.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);

                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen_menu,menu);

        MenuItem itemMenu = menu.findItem(R.id.search);

        SearchView searchView = (SearchView)itemMenu.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                firebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseSearch(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.settings:
                Toast.makeText(MainScreenActivity.this,"Setting Pressed",Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                firebaseAuth.signOut();
                Intent intent = new Intent(MainScreenActivity.this , MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                break;
            case R.id.about:
                Toast.makeText(MainScreenActivity.this,"About Pressed",Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                break;

            case R.id.share:
                Toast.makeText(MainScreenActivity.this,"Share Pressed",Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Intent intent3 = new Intent(MainScreenActivity.this,ProfileActivity.class);
                startActivity(intent3);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.bell:
                Intent intent4 = new Intent(MainScreenActivity.this , NotificationActivity.class);
                startActivity(intent4);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;
            case R.id.chat:
                Intent intent5 = new Intent(MainScreenActivity.this , ChatListActivity.class);
                startActivity(intent5);

                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;

            default:
                break;
        }

        return true;
    }

    private void firebaseSearch(String s){

        Query firebaseSearchQuarry = databaseReference.orderByChild("name").startAt(s).endAt(s + "\uf8ff");

        FirebaseRecyclerOptions<RecyclearModel> firebaseOptions = new FirebaseRecyclerOptions.Builder<RecyclearModel>()
                .setQuery(firebaseSearchQuarry,RecyclearModel.class)
                .build();

        FirebaseRecyclerAdapter<RecyclearModel,MyViewHolder> firebaseAdapter = new FirebaseRecyclerAdapter<RecyclearModel, MyViewHolder>(firebaseOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull RecyclearModel model) {
                holder.Name.setText(model.getName());
                holder.Age.setText(model.getAge()+ "+");
                holder.Hight.setText(model.getHeight()+ " feet");
                holder.Sex.setText(model.getSex());
                holder.Profession.setText(model.getPassion());

                Glide.with(holder.Image.getContext()).load(model.getProfileImageUri()).into(holder.Image);



                holder.Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfilePicture.class);
                        intent.putExtra("Image",model.getProfileImageUri());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                });

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = user.getUid();
                final String post_key = getRef(position).getKey();


                holder.setLikeButtonStatus(post_key);
                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        likeChecker = true;

                        likeReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(likeChecker.equals(true)){
                                    if (snapshot.child(post_key).hasChild(currentUserId)){
                                        likeReference.child(post_key).child(currentUserId).removeValue();
                                        likeChecker = false;
                                    }
                                    else{
                                        likeReference.child(post_key).child(currentUserId).setValue(true);
                                        likeChecker = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                holder.Name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Age.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Hight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Sex.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });

                holder.Profession.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainScreenActivity.this,ProfileViewActivity.class);
                        intent.putExtra("Name",model.getName());
                        intent.putExtra("Age",model.getAge());
                        intent.putExtra("Height",model.getHeight());
                        intent.putExtra("Sex",model.getSex());
                        intent.putExtra("Bio",model.getBio());
                        intent.putExtra("Passion",model.getPassion());
                        intent.putExtra("Propose",model.getPropose());
                        intent.putExtra("Image",model.getProfileImageUri());
                        intent.putExtra("post_key",post_key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    }
                });



            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);

                return new  MyViewHolder(itemView);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseAdapter.startListening();
        recyclerView.setAdapter(firebaseAdapter);
    }

}
