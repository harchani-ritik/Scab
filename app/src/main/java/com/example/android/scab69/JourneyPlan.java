package com.example.android.scab69;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class JourneyPlan extends AppCompatActivity {
    EditText Initial, Destination;
    Button SearchBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_plan);
        Initial = (EditText) findViewById(R.id.initial);
        Destination = (EditText) findViewById(R.id.destinaton);
        SearchBox = (Button) findViewById(R.id.search_box);
    }
}
