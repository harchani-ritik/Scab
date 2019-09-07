package com.example.android.scab69;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoomListActivity extends AppCompatActivity {

    static ArrayList<Room> YourRoomsList=new ArrayList<>();
    ArrayList<Room> AvailableRoomsList;
    private static ArrayList<Room> FilterRoomsList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRoomDatabaseReference;
    ChildEventListener mChildEventListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private String Dest,Src,JourneyTime,Tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        getIncomingIntent();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRoomDatabaseReference = firebaseDatabase.getReference().child("rooms");

        AvailableRoomsList=new ArrayList<>();
        FilterRoomsList=new ArrayList<>();
        fetchAvailableRooms();


        mRecyclerView = findViewById(R.id.availableRoomList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RoomListAdapter(AvailableRoomsList,0);
        mRecyclerView.setAdapter(mAdapter);


        Button createRoomButton = findViewById(R.id.create_room_button);
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRoom();

                /*Intent intent = new Intent(RoomListActivity.this,RoomActivity.class);
                startActivity(intent);*/
            }
        });
        final Button yourRooms = findViewById(R.id.yourRooms);
        final Button allRooms = findViewById(R.id.allRooms);
        yourRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourRooms.setBackgroundResource(R.drawable.custom_edit_text);
                allRooms.setBackgroundResource(R.drawable.custom_edit_text1);
                Toast.makeText(view.getContext(),"Fetched="+FilterRoomsList.size(),Toast.LENGTH_SHORT).show();
                mAdapter= new RoomListAdapter(YourRoomsList,1);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        allRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allRooms.setBackgroundResource(R.drawable.custom_edit_text);
                yourRooms.setBackgroundResource(R.drawable.custom_edit_text1);
                Toast.makeText(view.getContext(),"List of Selected Rooms",Toast.LENGTH_SHORT).show();
                mAdapter= new RoomListAdapter(AvailableRoomsList,0);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        ((RoomListAdapter) mAdapter).setOnItemClickListener(new RoomListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i("QueryListActivity", " Clicked on Item " + position);
                if (AvailableRoomsList.get(position) != null) {
                    Intent intent = new Intent(RoomListActivity.this, RoomActivity.class);
                    intent.putExtra("position",position);
                    startActivity(intent);

                }
            }
        });
    }

    private void fetchAvailableRooms() {
        Toast.makeText(RoomListActivity.this, "Fetching Rooms", Toast.LENGTH_SHORT).show();
        DatabaseReference myRoomsDatabaseReference = mRoomDatabaseReference.child(Tag);
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Room room = dataSnapshot.getValue(Room.class);
                    AvailableRoomsList.add(room);
                    if(room.getDestination().equals(Dest)&&room.getSource().equals(Src))
                    {
                        FilterRoomsList.add(room);
                        mAdapter = new RoomListAdapter(FilterRoomsList,0);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    /*mAdapter = new RoomListAdapter(AvailableRoomsList,0);
                    mRecyclerView.setAdapter(mAdapter);*/
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            myRoomsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void getIncomingIntent()
    {
        if(getIntent().hasExtra("dest"))
            Dest=getIntent().getStringExtra("dest");
        if(getIntent().hasExtra("src"))
            Src=getIntent().getStringExtra("src");
        if(getIntent().hasExtra("time"))
            JourneyTime=getIntent().getStringExtra("time");
        if(getIntent().hasExtra("time"))
            Tag=getIntent().getStringExtra("tag");
    }
    private void createNewRoom() {
        long time = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm");
        String timeToDisplay = dateFormatter.format(new Date(time));

        String roomId = mRoomDatabaseReference.push().getKey();
        Room room = new Room(JourneyPlan.mUser,timeToDisplay,Src,Dest,Tag,roomId);
        room.setUser1(JourneyPlan.mUser);

        mRoomDatabaseReference.child(Tag).child(roomId).setValue(room);
    }

    public static Room getRoomFromRoomsList(int position) {
        return FilterRoomsList.get(position);
    }
}

