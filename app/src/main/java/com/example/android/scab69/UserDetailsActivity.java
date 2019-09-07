package com.example.android.scab69;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailsActivity extends AppCompatActivity {

    private TextView ErrorText;
    public static User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        mUser=new User();
        mUser.setPhoneNumber(getIntent().getStringExtra("phoneNumber"));

        ErrorText=findViewById(R.id.error_text);
        final Button proceedButton = findViewById(R.id.proceed);

        final RadioGroup genderRadioGroup = findViewById(R.id.radioSex);
        genderRadioGroup.clearCheck();

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
                    updateUsersDatabase();
                    JourneyPlan.mUser=mUser;
                    Intent intent = new Intent(UserDetailsActivity.this, JourneyPlan.class);
                    intent.putExtra("detailFlag",1);
                    startActivity(intent);
                }
            }
        });
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

    private void updateUsersDatabase()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mUsersDatabaseReference = firebaseDatabase.getReference().child("users");
        String uid = mUsersDatabaseReference.push().getKey();
        mUser.setUid(uid);
        mUsersDatabaseReference.child(uid).setValue(mUser);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
    }
}

