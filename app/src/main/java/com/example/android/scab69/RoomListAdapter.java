package com.example.android.scab69;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomObjectHolder> {
    
    private ArrayList<Room> RoomList;
    private static MyClickListener myClickListener;
    private static int flag;

    public static class RoomObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        TextView JoinRoom;

        public RoomObjectHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            JoinRoom=(TextView)itemView.findViewById(R.id.join_room_button);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        RoomListAdapter.myClickListener = myClickListener;
    }


    public RoomListAdapter(ArrayList<Room> myDataset,int flag) {
        RoomList = myDataset;
        this.flag=flag;
    }

    @Override
    public RoomObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_row, parent, false);
        RoomObjectHolder dataObjectHolder = new RoomObjectHolder(view);
        return dataObjectHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final RoomObjectHolder holder, final int position) {

        if(flag==1)
            holder.JoinRoom.setText("REQUEST SENT");
        holder.JoinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.JoinRoom.setText("REQUEST SENT");
                RoomListActivity.YourRoomsList.add(RoomList.get(position));
            }
        });
    }

    public void addItem(Room dataObj, int index) {
        RoomList.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        RoomList.remove(index);
        notifyItemRemoved(index);
        //notifyItemRangeChanged(index,CartListActivity.getNumberOfItemsInCart());
    }
    @Override
    public int getItemCount() {
        return RoomList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}
