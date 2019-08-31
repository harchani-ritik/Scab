package com.example.android.scab69;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RoomListActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRoomDatabaseReference = firebaseDatabase.getReference().child("rooms");

        Button createRoomButton = findViewById(R.id.create_room_button);
        createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long time = System.currentTimeMillis();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM, yyyy  HH:mm");
                String timeToDisplay = dateFormatter.format(new Date(time));
                String roomId = mRoomDatabaseReference.push().getKey();
                Room room = new Room(MainActivity.getmUser(),timeToDisplay,"ABCD","DEF","TAG",roomId);
                mRoomDatabaseReference.child(roomId).setValue(room);
            }
        });
    }
}
