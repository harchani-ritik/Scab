package com.example.android.scab69;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class JourneyPlan extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String Source, Destination,Time;
    Button SearchRide;

    public static User mUser= new User();
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String UserPhoneNumber;
    public boolean isFirstRun,isFRun;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_plan);

        //fillInTempData();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getPhoneNumber());
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setTheme(R.style.NewAppTheme)
                                    .setLogo(R.drawable.app_logo)
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.PhoneBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        SearchRide = (Button) findViewById(R.id.search_ride);

        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener( this);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener( this);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        final TextView selectedTag1 = findViewById(R.id.tag1);
        final TextView selectedTag2 = findViewById(R.id.tag2);
        final TextView selectedTag3 = findViewById(R.id.tag3);
        final TextView selectedTag4 = findViewById(R.id.tag4);
        selectedTag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag1.setBackgroundResource(R.drawable.custom_edit_text);
                selectedTag2.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag3.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag4.setBackgroundResource(R.drawable.custom_edit_text1);
            }
        });
        selectedTag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag1.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag2.setBackgroundResource(R.drawable.custom_edit_text);
                selectedTag3.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag4.setBackgroundResource(R.drawable.custom_edit_text1);
            }
        });
        selectedTag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag1.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag2.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag3.setBackgroundResource(R.drawable.custom_edit_text);
                selectedTag4.setBackgroundResource(R.drawable.custom_edit_text1);
            }
        });
        selectedTag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTag1.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag2.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag3.setBackgroundResource(R.drawable.custom_edit_text1);
                selectedTag4.setBackgroundResource(R.drawable.custom_edit_text);
            }
        });

        final TimePicker timePicker = findViewById(R.id.journey_time_picker);

        SearchRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Source.equals(Destination))
                {
                    Toast.makeText(view.getContext(),"Source and Destination cannot be Same",Toast.LENGTH_SHORT).show();
                }
                else {
                    int hr = timePicker.getHour();
                    int min = timePicker.getMinute();
                    Time = hr + " : " + min;
                    //Toast.makeText(view.getContext(), Time, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(JourneyPlan.this, RoomListActivity.class);
                    intent.putExtra("dest", Destination);
                    intent.putExtra("src", Source);
                    intent.putExtra("time", Time);
                    intent.putExtra("tag", "IIITA");
                    startActivity(intent);
                }
            }
        });


    }

    private void  fillInTempData() {
        mUser.setName("Anonymous");
        mUser.setPhoneNumber("100");
        mUser.setCommunityStatus("99");
        mUser.setAge(18);
        mUser.setGender(User.MALE);
        mUser.setUid("-Lkaajaminal");
        mUser.setStatus(User.IDLE);
    }

    private void checkUserDetails() {
       isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start
            Intent intent = new Intent(JourneyPlan.this, UserDetailsActivity.class);
            startActivity(intent);
            //Toast.makeText(JourneyPlan.this, "First Run", Toast.LENGTH_LONG).show();
        }

    }
    private void checkSliderActivity() {
        isFRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFRun", true);

        if (isFRun) {
            Intent intent = new Intent(JourneyPlan.this, SliderActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        int SliderFlag=getIntent().getIntExtra("sliderFlag",0);
        if(SliderFlag==1)
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFRun", false).apply();
        checkSliderActivity();

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

        int DetailFlag=getIntent().getIntExtra("detailFlag",0);
        if(DetailFlag==1)
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).apply();
        checkUserDetails();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
    private void onSignedInInitialize(String phoneNumber) {
        //UserDetailsActivity.mUser.setPhoneNumber(phoneNumber);
        UserPhoneNumber=phoneNumber;
    }
    private void onSignedOutCleanup() {
        JourneyPlan.mUser=null;
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", true).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(JourneyPlan.this, UserDetailsActivity.class);
                intent.putExtra("phoneNumber",UserPhoneNumber);
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spin = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;
        if(spin.getId() == R.id.spinner1)
        {
            Destination=parent.getItemAtPosition(position).toString();
        }
        if(spin2.getId() == R.id.spinner2)
        {
            Source=parent.getItemAtPosition(position).toString();
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}