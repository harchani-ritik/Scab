package com.example.android.scab69;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomObjectHolder> {
    
    private ArrayList<Room> RoomList;
    private static MyClickListener myClickListener;
    static final int YourRoomsList =1;
    static final int FilterRoomsList =0;
    private int mFlag;

    public static class RoomObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        TextView JoinRoom,Owner,Tag,Dest,Src,JourneyTime;

        RoomObjectHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            JoinRoom=(TextView)itemView.findViewById(R.id.join_room_button);
            Owner=itemView.findViewById(R.id.card_owner);
            Tag=itemView.findViewById(R.id.card_tag);
            Dest=itemView.findViewById(R.id.card_dest);
            Src=itemView.findViewById(R.id.card_src);
            JourneyTime=itemView.findViewById(R.id.card_journey_time);
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
        mFlag = flag;
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

        final Room room = RoomList.get(position);
        if(mFlag==RoomListAdapter.YourRoomsList)
        {
            holder.JoinRoom.setBackgroundResource(R.drawable.custom_edit_text3);
            holder.JoinRoom.setText("Pending Request");
            if(room.getMyRequest()==Room.Accepted)
            {
                holder.JoinRoom.setBackgroundResource(R.drawable.custom_edit_text);
                holder.JoinRoom.setText("Confirm Join");
                holder.JoinRoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(),RoomActivity.class);
                        intent.putExtra("positionInMyRooms",position);
                        view.getContext().startActivity(intent);
                    }
                });
            }
        }
        else{
            holder.JoinRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String RequestedRoomId = room.getRoomId();
                    String RequestedRoomTag = room.getRoomTag();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference tempUserRef = firebaseDatabase.getReference().
                            child("rooms").child(RequestedRoomTag).child(RequestedRoomId).
                            child("TempUsers").child(JourneyPlan.mUser.getUid());
                    JourneyPlan.mUser.setUserRequestStatus(User.PENDING);
                    tempUserRef.setValue(JourneyPlan.mUser);
                    Snackbar.make(RoomListActivity.RoomListRootView, "REQUEST SENT TO OWNER", Snackbar.LENGTH_SHORT).show();
                    RoomListActivity.YourRoomsList.add(room);
                    RoomListActivity.removeRoomFromRoomsList(position);
                }
            });
        }

        holder.Owner.setText(room.getUser1().getName());
        holder.Src.setText(room.getSource());
        holder.Dest.setText(room.getDestination());
        holder.Tag.setText(room.getRoomTag());
        holder.JourneyTime.setText("Time: "+room.getJourneyTime());
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
