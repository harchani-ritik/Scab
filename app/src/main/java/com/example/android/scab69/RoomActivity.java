package com.example.android.scab69;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.view.View.GONE;

public class RoomActivity extends AppCompatActivity {
    public int numberOfUsers = 0;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateTimeString = sdf.format(d);

        int hourPresent = Integer.parseInt(currentDateTimeString.substring(0, 2));
        int minPresent = Integer.parseInt(currentDateTimeString.substring(currentDateTimeString.length() - 2));

        Room R1 = new Room();

        String journeyTime = R1.getTime();
        int hourFuture = Integer.parseInt(journeyTime.substring(0, 2));
        int minFuture = Integer.parseInt(journeyTime.substring(journeyTime.length() - 2));
        int difference;
        if (hourFuture > hourPresent){
            difference = 61 + minFuture - minPresent;
        }
        else{
            difference = minFuture - minPresent;
        }
        new CountDownTimer(difference*60*1000, 1000 * 60) {
            TextView mTextField = findViewById(R.id.counter_text_view);

            public void onTick(long millisUntilFinished) {
                mTextField.setText("Minutes Remaining: " + millisUntilFinished / (1000 * 60));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                mTextField.setText("Happy Journey !");
            }

        }.start();

        TextView fromValue = findViewById(R.id.from_value);
        fromValue.setText(R1.getSource());

        TextView toValue = findViewById(R.id.to_value);
        toValue.setText(R1.getDestination());

        TextView timeValue = findViewById(R.id.time_value);
        timeValue.setText(journeyTime);

        TextView user1TextView = findViewById(R.id.user1_text_view);
        TextView user2TextView = findViewById(R.id.user2_text_view);
        TextView user3TextView = findViewById(R.id.user3_text_view);
        TextView user4TextView = findViewById(R.id.user4_text_view);

        if(numberOfUsers== 0 && (R1.getUser1().getName() != "NOT_ASSIGNED") && (R1.getUser1().getStatus()==2)) {
            user1TextView.setText(R1.getUser1().getName());
            numberOfUsers++;
        }
        else{
            user1TextView.setVisibility(GONE);
        }
        if(numberOfUsers== 1 && (R1.getUser2().getName() != "NOT_ASSIGNED") && (R1.getUser2().getStatus()==2)) {
            user2TextView.setText(R1.getUser2().getName());
            numberOfUsers++;
        }
        else{
            user2TextView.setVisibility(GONE);
        }

        if(numberOfUsers== 2 && (R1.getUser3().getName() != "NOT_ASSIGNED") && (R1.getUser3().getStatus()==2)) {
            user3TextView.setText(R1.getUser3().getName());
            numberOfUsers++;
        }
        else{
            user3TextView.setVisibility(GONE);
        }

        if(numberOfUsers== 3 && (R1.getUser4().getName() != "NOT_ASSIGNED") && (R1.getUser4().getStatus()==2)) {
        user4TextView.setText(R1.getUser4().getName());
        numberOfUsers++;
    }
        else{
            user4TextView.setVisibility(GONE);
        }
    }
}

