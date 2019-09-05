package com.example.android.scab69;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class JourneyPlan extends AppCompatActivity {
    EditText Initial, Destination,Time;
    Button SearchBox;

    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String UserPhoneNumber;
    public boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_plan);

        int DetailFlag=getIntent().getIntExtra("detailFlag",0);
        if(DetailFlag==1)
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).apply();
        checkUserDetails();

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
                                            new AuthUI.IdpConfig.AnonymousBuilder().build(),
                                            new AuthUI.IdpConfig.PhoneBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        SearchBox = (Button) findViewById(R.id.search_box);
        //Time=findViewById(R.id.time_to_arrive);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        SearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JourneyPlan.this,RoomListActivity.class);
                intent.putExtra("dest",Destination.getText().toString());
                intent.putExtra("src", Initial.getText().toString());
                intent.putExtra("time","4:30");
                intent.putExtra("tag","IIITA");
                startActivity(intent);
            }
        });


    }

    private void checkUserDetails() {
       isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show start
            Intent intent = new Intent(JourneyPlan.this, SliderActivity.class);
            startActivity(intent);
            //Toast.makeText(JourneyPlan.this, "First Run", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
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
        UserDetailsActivity.mUser=null;
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
}