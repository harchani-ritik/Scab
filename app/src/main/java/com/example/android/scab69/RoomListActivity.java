package com.example.android.scab69;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoomListActivity extends AppCompatActivity {

    static ArrayList<Room> YourRoomsList=new ArrayList<>();
    ArrayList<Room> AvailableRoomsList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRoomDatabaseReference;
    ChildEventListener mChildEventListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    String Dest,Src,Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        getIncomingIntent();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRoomDatabaseReference = firebaseDatabase.getReference().child("rooms");

        AvailableRoomsList=new ArrayList<>();
        fetchAvailableRooms();
        Toast.makeText(RoomListActivity.this,"Fetch Rooms= "+AvailableRoomsList.size(),Toast.LENGTH_SHORT).show();
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
                long time = System.currentTimeMillis();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy  HH:mm");
                String timeToDisplay = dateFormatter.format(new Date(time));
                String roomId = mRoomDatabaseReference.push().getKey();
                Room room = new Room(UserDetailsActivity.getmUser(),timeToDisplay,"ABCD","DEF","IIIT-Allahabad",roomId);
                mRoomDatabaseReference.child(roomId).setValue(room);

                Intent intent = new Intent(RoomListActivity.this,RoomActivity.class);
                startActivity(intent);
            }
        });
        Button yourRooms = findViewById(R.id.yourRooms);
        yourRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"List of Selected Rooms",Toast.LENGTH_SHORT).show();

                mAdapter= new RoomListAdapter(YourRoomsList,1);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        Button allRooms = findViewById(R.id.allRooms);
        allRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    startActivity(intent);
                }
            }
        });
    }

    private void fetchAvailableRooms()
    {
        /*
        Toast.makeText(RoomListActivity.this, "Fetching Rooms", Toast.LENGTH_SHORT).show();

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Room room = dataSnapshot.getValue(Room.class);
                    AvailableRoomsList.add(room);
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
            mRoomDatabaseReference.addChildEventListener(mChildEventListener);*/
        long time = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy  HH:mm");
        String timeToDisplay = dateFormatter.format(new Date(time));
        String roomId = mRoomDatabaseReference.push().getKey();
        Room room = new Room(UserDetailsActivity.getmUser(),timeToDisplay,Src,Dest,"IIIT-Allahabad",roomId);

        for(int i=0;i<10;i++)
            AvailableRoomsList.add(room);
        }

    private void getIncomingIntent()
    {
        if(getIntent().hasExtra("dest"))
            Dest=getIntent().getStringExtra("dest");
        if(getIntent().hasExtra("src"))
            Src=getIntent().getStringExtra("src");
        if(getIntent().hasExtra("time"))
            Time=getIntent().getStringExtra("time");
    }
}

