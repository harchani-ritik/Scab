package com.example.android.scab69;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.transform.Source;

public class JourneyPlan extends AppCompatActivity {
    EditText Initial, Destination,Time;
    Button SearchBox;
    TextView Standard,Economical,Delux, mTextField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_plan);
        Initial = (EditText) findViewById(R.id.initial);
        mTextField = (TextView) findViewById(R.id.time_to_arrive);
        Destination = (EditText) findViewById(R.id.destinaton);
        SearchBox = (Button) findViewById(R.id.search_box);
        Standard = (TextView) findViewById(R.id.standard);
        Economical = (TextView) findViewById(R.id.eco);
        Time=findViewById(R.id.time_to_arrive);
        Delux = (TextView) findViewById(R.id.delux);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"You chose Standard mode",Toast.LENGTH_SHORT).show();
            }
        });
        Delux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"You chose delux mode",Toast.LENGTH_SHORT).show();
            }
        });
        Economical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"You chose Econmical mode",Toast.LENGTH_SHORT).show();

            }
        });
        SearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JourneyPlan.this,RoomListActivity.class);
                intent.putExtra("dest",Destination.getText().toString());
                intent.putExtra("src", Initial.getText().toString());
                intent.putExtra("time",Time.getText().toString());
                startActivity(intent);
            }
        });


    }
}