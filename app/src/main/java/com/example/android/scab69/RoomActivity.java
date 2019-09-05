package com.example.android.scab69;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;

public class RoomActivity extends AppCompatActivity {
    private int numberOfUsers = 0;
    Room mRoom;
    int RoomPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        if(getIntent().hasExtra("position")) {
            RoomPosition= getIntent().getIntExtra("position", 0);
            mRoom = RoomListActivity.getRoomFromRoomsList(RoomPosition);
        }

        Button button = findViewById(R.id.chat_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomActivity.this,ChatRoom.class);
                intent.putExtra("position",RoomPosition);
                startActivity(intent);
            }
        });

        //Fetching Current Time
        Date d = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateTimeString = sdf.format(d);

        //Calculating hour and minutes for current time
        int hourPresent = Integer.parseInt(currentDateTimeString.substring(0, 2));
        int minPresent = Integer.parseInt(currentDateTimeString.substring(currentDateTimeString.length() - 2));


        //Calculating hour and minutes for journey time
        String journeyTime = mRoom.getJouneyTime();
        int hourFuture = Integer.parseInt(journeyTime.substring(0, 2));
        int minFuture = Integer.parseInt(journeyTime.substring(journeyTime.length() - 2));

        //Calculating Remaining Time for Journey
        int difference;
        if (hourFuture > hourPresent){
            difference = 61 + minFuture - minPresent;
        }
        else{
            difference = minFuture - minPresent;
        }

        //Timer showing Remaining Time
        new CountDownTimer(difference*60*1000, 1000 * 60) {
            TextView mTextField = findViewById(R.id.counter_text_view);

            public void onTick(long millisUntilFinished) {
                mTextField.setText("Minutes Remaining: " + millisUntilFinished / (1000 * 60));
            }

            public void onFinish() {
                mTextField.setText("Happy Journey !");
            }
        }.start();

        //Setting value in From TextView
        TextView fromValue = findViewById(R.id.from_value);
        fromValue.setText(mRoom.getSource());

        //Setting value in To TextView
        TextView toValue = findViewById(R.id.to_value);
        toValue.setText(mRoom.getDestination());

        //Setting value in Time TextView
        TextView timeValue = findViewById(R.id.time_value);
        timeValue.setText(journeyTime);

        //Finding user textviews
        TextView user1TextView = findViewById(R.id.user1_text_view);
        TextView user2TextView = findViewById(R.id.user2_text_view);
        TextView user3TextView = findViewById(R.id.user3_text_view);
        TextView user4TextView = findViewById(R.id.user4_text_view);

        //Finding RollNo TextViews
        TextView roll1TextView = findViewById(R.id.roll1_text_view);
        TextView roll2TextView = findViewById(R.id.roll2_text_view);
        TextView roll3TextView = findViewById(R.id.roll3_text_view);
        TextView roll4TextView = findViewById(R.id.roll4_text_view);

        //Adding users in the room
        if(numberOfUsers== 0 && (mRoom.getUser1().getStatus()==User.INAROOM)) {
            user1TextView.setText(mRoom.getUser1().getName());
            roll1TextView.setText(mRoom.getUser1().getCommunityStatus());
            numberOfUsers++;
            sendNotification(numberOfUsers, mRoom);
        }
        else{
            user1TextView.setVisibility(GONE);
            roll1TextView.setVisibility(GONE);
        }
        if(numberOfUsers== 1 && (mRoom.getUser2().getStatus()==2)) {
            user2TextView.setText(mRoom.getUser2().getName());
            roll2TextView.setText(mRoom.getUser1().getCommunityStatus());
            numberOfUsers++;
            sendNotification(numberOfUsers, mRoom);
        }
        else{
            user2TextView.setVisibility(GONE);
            roll2TextView.setVisibility(GONE);
        }

        if(numberOfUsers== 2  && (mRoom.getUser3().getStatus()==2)) {
            user3TextView.setText(mRoom.getUser3().getName());
            roll3TextView.setText(mRoom.getUser1().getCommunityStatus());
            numberOfUsers++;
            sendNotification(numberOfUsers, mRoom);
        }
        else{
            user3TextView.setVisibility(GONE);
            roll3TextView.setVisibility(GONE);
        }

        if(numberOfUsers== 3 && (mRoom.getUser4().getStatus()==2)) {
            user4TextView.setText(mRoom.getUser4().getName());
            roll4TextView.setText(mRoom.getUser1().getCommunityStatus());
            numberOfUsers++;
            sendNotification(numberOfUsers, mRoom);
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
}