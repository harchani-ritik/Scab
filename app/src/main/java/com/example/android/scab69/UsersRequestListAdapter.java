package com.example.android.scab69;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersRequestListAdapter extends RecyclerView.Adapter<UsersRequestListAdapter.RoomObjectHolder> {

    private ArrayList<User> UsersList;
    private static MyClickListener myClickListener;
    private String Destination,Source;
    public static class RoomObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        TextView name,roll,age,gender,Dest,Src;
        Button confirmButton;

        RoomObjectHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name=itemView.findViewById(R.id.req_name);
            roll=itemView.findViewById(R.id.req_roll);
            age=itemView.findViewById(R.id.req_age);
            gender=itemView.findViewById(R.id.req_gender);
            Dest=itemView.findViewById(R.id.req_dest);
            Src=itemView.findViewById(R.id.req_src);
            confirmButton=itemView.findViewById(R.id.req_confirm_accept);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        UsersRequestListAdapter.myClickListener = myClickListener;
    }


    public UsersRequestListAdapter(ArrayList<User> myDataset,String Destination,String Source) {
        UsersList = myDataset;
        this.Destination=Destination;
        this.Source=Source;
    }

    @Override
    public RoomObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_join_request_dialog, parent, false);
        RoomObjectHolder dataObjectHolder = new RoomObjectHolder(view);
        return dataObjectHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RoomObjectHolder holder, final int position) {

        final User user = UsersList.get(position);
        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(JoinRequestsActivity.mRoom.getUser2() == null) {
                    JoinRequestsActivity.mRoom.setUser2(user);
                    JoinRequestsActivity.mRoom.getUser2().setStatus(User.INAROOM);
                }
                else if(JoinRequestsActivity.mRoom.getUser3() == null){
                    JoinRequestsActivity.mRoom.setUser3(user);
                    JoinRequestsActivity.mRoom.getUser3().setStatus(User.INAROOM);
                }
                else if(JoinRequestsActivity.mRoom.getUser4() == null){
                    JoinRequestsActivity.mRoom.setUser4(user);
                    JoinRequestsActivity.mRoom.getUser4().setStatus(User.INAROOM);
                }
                RoomActivity.mRoom=JoinRequestsActivity.mRoom;
                JoinRequestsActivity.sendRoomToFirebase();
                //Intent here
                Intent intent = new Intent(view.getContext(),RoomActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        holder.Dest.setText(Destination);
        holder.Src.setText(Source);

        if(user!=null) {
            holder.name.setText(user.getName());
            holder.age.setText(Integer.toString(user.getAge()));

            if (user.getGender() == 0)
                holder.gender.setText("Gender: MALE");
            else
                holder.gender.setText("Gender: FEMALE");

            holder.roll.setText(user.getCommunityStatus());
        }
    }

    public void addItem(User dataObj, int index) {
        UsersList.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        UsersList.remove(index);
        notifyItemRemoved(index);
        //notifyItemRangeChanged(index,CartListActivity.getNumberOfItemsInCart());
    }
    @Override
    public int getItemCount() {
        return UsersList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}
