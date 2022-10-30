package com.example.gfbf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends  RecyclerView.Adapter {

    ArrayList<MassagesModels> massagesModels;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MassagesModels> massagesModels, Context context) {
        this.massagesModels = massagesModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender_layout ,parent ,false);
            return  new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver_layout ,parent ,false);
            return  new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(massagesModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(massagesModels.get(position).getMassage());
        }
        else {
            ((ReceiverViewHolder)holder).receiverMSG.setText(massagesModels.get(position).getMassage());
        }



    }

    @Override
    public int getItemCount() {
        return massagesModels.size();
    }

    public  class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMSG , receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMSG = itemView.findViewById(R.id.receiver_massage_text);
        }
    }

    public  class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg;


        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.sender_massage_text);
        }
    }








}
