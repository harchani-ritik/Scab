package com.example.android.scab69;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JoinRequestsActivity extends AppCompatActivity {

    private ChildEventListener mChildEventListener;
    private static DatabaseReference mRequestsDatabaseReference;
    static Room mRoom;
    private ArrayList<User> mUsersRequestList=new ArrayList<>();
    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    static FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_requests);

        mRoom=RoomListActivity.MyRoom;

        firebaseDatabase= FirebaseDatabase.getInstance();
        mRequestsDatabaseReference=firebaseDatabase.getReference().child("rooms").child(mRoom.getRoomTag())
                .child(mRoom.getRoomId()).child("TempUsers");


        attachDatabaseReadListener();

        mRecyclerView = findViewById(R.id.requests_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter= new UsersRequestListAdapter(mUsersRequestList,mRoom.getDestination(),mRoom.getSource());
        mRecyclerView.setAdapter(mAdapter);

        Toast.makeText(this,"Requests="+mUsersRequestList.size(),Toast.LENGTH_SHORT).show();

    }

    public void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    mUsersRequestList.add(user);
                    mAdapter= new UsersRequestListAdapter(mUsersRequestList,mRoom.getDestination(),mRoom.getSource());
                    mRecyclerView.setAdapter(mAdapter);
                }

                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            };
            mRequestsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }
    public static void sendRoomToFirebase()
    {
        firebaseDatabase.getReference().child("rooms").child(mRoom.getRoomTag())
                .child(mRoom.getRoomId()).setValue(mRoom);
    }
}
