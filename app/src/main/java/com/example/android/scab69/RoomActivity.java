package com.example.android.scab69;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;

public class RoomActivity extends AppCompatActivity {
    static Room mRoom;
    int RoomPosition;
    int MyRoomFlag;

    TextView user2TextView,user1TextView,user3TextView,
            user4TextView,roll1TextView,roll2TextView,roll3TextView,roll4TextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Button chatButton = findViewById(R.id.chat_button);
        Button bookRideButton = findViewById(R.id.book_ride_button);
        Button requestsButton = findViewById(R.id.see_all_requests_button);

        if(getIntent().hasExtra("position")) {
            RoomPosition= getIntent().getIntExtra("position", 0);
            mRoom = RoomListActivity.getRoomFromRoomsList(RoomPosition);
            chatButton.setVisibility(GONE);
            bookRideButton.setVisibility(GONE);
            requestsButton.setVisibility(GONE);
            MyRoomFlag=0;
        }
        else if (getIntent().hasExtra("positionInMyRooms"))
        {
            mRoom = RoomListActivity.YourRoomsList.get(RoomPosition);
            bookRideButton.setVisibility(GONE);
            requestsButton.setVisibility(GONE);
            MyRoomFlag=1;
        }
        else {
            mRoom=RoomListActivity.MyRoom;
            MyRoomFlag=1;
        }


        bookRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.olacabs.customer");
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else
                {
                    Uri uri = Uri.parse("market://details?id=com.olacabs.customer");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=com.olacabs.customer")));
                    }
                }
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomActivity.this,ChatRoom.class);
                intent.putExtra("roomId",mRoom.getRoomId());
                intent.putExtra("roomTag",mRoom.getRoomTag());
                startActivity(intent);
            }
        });

        requestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomActivity.this,JoinRequestsActivity.class);
                intent.putExtra("roomId",mRoom.getRoomId());
                startActivity(intent);
            }
        });
        //Setting value in From TextView
        TextView fromValue = findViewById(R.id.from_value);
        fromValue.setText(mRoom.getSource());

        //Setting value in To TextView
        TextView toValue = findViewById(R.id.to_value);
        toValue.setText(mRoom.getDestination());

        //Setting value in Time TextView
        TextView timeValue = findViewById(R.id.time_value);
        timeValue.setText(mRoom.getJourneyTime());

        //Finding user textviews
        user1TextView = findViewById(R.id.user1_text_view);
        user2TextView = findViewById(R.id.user2_text_view);
        user3TextView = findViewById(R.id.user3_text_view);
        user4TextView = findViewById(R.id.user4_text_view);

        //Finding RollNo TextViews
        roll1TextView = findViewById(R.id.roll1_text_view);
        roll2TextView = findViewById(R.id.roll2_text_view);
        roll3TextView = findViewById(R.id.roll3_text_view);
        roll4TextView = findViewById(R.id.roll4_text_view);

        AddUsersInTheRoom();
    }

    private void AddUsersInTheRoom() {

        if(mRoom.getUser1()!=null ) {
            user1TextView.setText(mRoom.getUser1().getName());
            roll1TextView.setText(mRoom.getUser1().getCommunityStatus());
            sendNotification(mRoom.getNumberOfUsers(), mRoom);
        }
        else{
            user1TextView.setVisibility(GONE);
            roll1TextView.setVisibility(GONE);
        }
        if(mRoom.getUser2()!=null) {
            user2TextView.setText(mRoom.getUser2().getName());
            roll2TextView.setText(mRoom.getUser2().getCommunityStatus());
            sendNotification(mRoom.getNumberOfUsers(), mRoom);
        }
        else{
            user2TextView.setVisibility(GONE);
            roll2TextView.setVisibility(GONE);
        }

        if(mRoom.getUser3()!=null) {
            user3TextView.setText(mRoom.getUser3().getName());
            roll3TextView.setText(mRoom.getUser3().getCommunityStatus());
            sendNotification(mRoom.getNumberOfUsers(), mRoom);
        }
        else{
            user3TextView.setVisibility(GONE);
            roll3TextView.setVisibility(GONE);
        }

        if(mRoom.getUser4()!=null ) {
            user4TextView.setText(mRoom.getUser4().getName());
            roll4TextView.setText(mRoom.getUser4().getCommunityStatus());
            sendNotification(mRoom.getNumberOfUsers(), mRoom);
        }
        else{
            user4TextView.setVisibility(GONE);
            roll4TextView.setVisibility(GONE);
        }

    }

    //Sending notification when user is successfully added into the room
    private void sendNotification(int userNo, Room mRoom){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("UserAdded", "RoomNotifications", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "UserAdded")
                .setContentTitle("Member Added")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        switch(userNo){
            case 1:
                mBuilder.setContentText(mRoom.getUser1().getName() + " is added to Room");
                break;
            case 2:
                mBuilder.setContentText(mRoom.getUser2().getName() + " is added to Room");
                break;
            case 3:
                mBuilder.setContentText(mRoom.getUser3().getName() + " is added to Room");
                break;
            case 4:
                mBuilder.setContentText(mRoom.getUser4().getName() + " is added to Room");
                break;
        }
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(userNo, mBuilder.build());
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if(MyRoomFlag==0)
            super.onBackPressed();
    }

}