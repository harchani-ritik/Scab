package com.example.android.scab69;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView ErrorText;
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static User mUser;
    private ArrayList<User> userObjectArrayList = new ArrayList<>();
    private DatabaseReference mUserDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ErrorText=findViewById(R.id.error_text);

        mUser=new User();
        final Button proceedButton = findViewById(R.id.proceed);

        final RadioGroup genderRadioGroup = findViewById(R.id.radioSex);
        genderRadioGroup.clearCheck();

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserDatabaseReference= mFirebaseDatabase.getReference().child("users");


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

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ErrorText.setVisibility(View.INVISIBLE);
                int proceedFlag=1;


                EditText CommunityStatus = (EditText)findViewById(R.id.community_status_edit);
                String rollNo =CommunityStatus.getText().toString();
                if(rollNo.isEmpty()) {
                    showErrorMsg(4);
                    proceedFlag = 0;
                }
                else {
                    mUser.setCommunityStatus(rollNo);
                }

                EditText AgeEdit = (EditText)findViewById(R.id.age_edit_text);
                String age =AgeEdit.getText().toString();
                if(age.isEmpty()) {
                    showErrorMsg(3);
                    proceedFlag = 0;
                }
                else {
                    mUser.setAge(Integer.parseInt(age));
                }

                // get selected radio button from radioGroup
                int selectedId = genderRadioGroup.getCheckedRadioButtonId();
                if(selectedId==-1)
                {
                    showErrorMsg(2);
                    proceedFlag = 0;
                }
                else {
                    // find the radiobutton by returned id
                    RadioButton radioSexButton = (RadioButton) findViewById(selectedId);

                    if (radioSexButton.getText() == "MALE")
                        mUser.setGender(User.MALE);
                    else if (radioSexButton.getText() == "FEMALE")
                        mUser.setGender(User.FEMALE);
                }

                EditText NameEdit = (EditText)findViewById(R.id.name_edit_text);
                String name =NameEdit.getText().toString();
                if(name.isEmpty()) {
                    showErrorMsg(1);
                    proceedFlag = 0;
                }
                else {
                    mUser.setName(name);
                }

                if(proceedFlag==1) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference mUsersDatabaseReference = firebaseDatabase.getReference().child("users");
                    String uid = mUsersDatabaseReference.push().getKey();
                    mUser.setUid(uid);
                    mUsersDatabaseReference.child(uid).setValue(mUser);

                    Intent intent = new Intent(MainActivity.this, JourneyPlan.class);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
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
        mUser.setPhoneNumber(phoneNumber);
    }
    private void onSignedOutCleanup() {

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

    public static User getmUser() {
        return mUser;
    }

    private void showErrorMsg(int token)
    {
        ErrorText.setVisibility(View.VISIBLE);
        switch (token)
        {
            case 1:
            {
                ErrorText.setText("Name is a required field");
                break;
            }
            case 2:
            {
                ErrorText.setText("Please Select Gender");
                break;
            }
            case 3:
            {
                ErrorText.setText("Enter your Age");
                break;
            }
            case 4:
            {
                ErrorText.setText("Enrollment Number is Mandatory");
                break;
            }



        }

    }
}

