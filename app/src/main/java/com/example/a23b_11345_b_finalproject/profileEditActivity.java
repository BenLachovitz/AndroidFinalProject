package com.example.a23b_11345_b_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a23b_11345_b_finalproject.Models.DataOfUser;
import com.example.a23b_11345_b_finalproject.Utillities.Constants;
import com.example.a23b_11345_b_finalproject.Utillities.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileEditActivity extends AppCompatActivity {

    private AppCompatEditText profile_EDT_licence;
    private AppCompatEditText profile_EDT_model;
    private AppCompatEditText profile_EDT_year;
    private MaterialButton profile_BTN_save;
    private TextView profile_TXT_headLine;
    private AppCompatEditText profile_EDT_name;
    private DataOfUser dataOfUser;
    private SignalGenerator signalGenerator;
    private DatabaseReference ref;
    private boolean afterSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().setTitle(getResources().getString(R.string.profile_edit));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        signalGenerator = SignalGenerator.getInstance();
        afterSignUp = false;
        ref = FirebaseDatabase.getInstance().getReference(
                        Constants.DBKeys.USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(Constants.DBKeys.DATA_OF_USER);
        findViews();
        initViews();
    }

    private void initViews() {
        profile_BTN_save.setOnClickListener(v -> saveData());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataOfUser = snapshot.getValue(DataOfUser.class);
                if(dataOfUser == null) {
                    dataOfUser = new DataOfUser();
                    profile_TXT_headLine.setText(R.string.profle_headLine_newUser);
                    afterSignUp = true;
                    displayNameTextBox();
                }
                else
                {
                    profile_EDT_name.setVisibility(View.GONE);
                    initTextBoxes();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initTextBoxes() {
        Log.d("DATA CHECK", dataOfUser+"");
        profile_EDT_licence.setText(dataOfUser.getLicencePlate());
        profile_EDT_model.setText(dataOfUser.getModel());
        profile_EDT_year.setText(dataOfUser.getYear() + "");
    }

    private void displayNameTextBox()
    {
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if (name == null)
        {
            profile_EDT_name.setVisibility(View.VISIBLE);
        }
        else
        {
            profile_EDT_name.setVisibility(View.GONE);
            dataOfUser.setUserName(name);
        }
    }

    private void saveData() {
        if (checkIfAllGood()) {
            dataOfUser.setLicencePlate(profile_EDT_licence.getText().toString())
                    .setModel(profile_EDT_model.getText().toString())
                    .setYear(Integer.parseInt(profile_EDT_year.getText().toString()));
            if (profile_EDT_name.getVisibility() == View.VISIBLE)
                dataOfUser.setUserName(profile_EDT_name.getText().toString());
            ref.setValue(dataOfUser);
            Intent intent = new Intent(this, MainActivity.class);
            afterSignUp = false;
            startActivity(intent);
            finish();
        } else {
            signalGenerator.toast(getResources().getString(R.string.all_fields_must_be_filled), 0);
            signalGenerator.vibrate(500);
        }
    }

    private boolean checkIfAllGood() {
        if(profile_EDT_licence.getText().toString().isEmpty())
            return false;
        if(profile_EDT_model.getText().toString().isEmpty())
            return false;
        if(profile_EDT_year.getText().toString().isEmpty())
            return false;
        if(profile_EDT_name.getVisibility() == View.VISIBLE)
            if(profile_EDT_name.getText().toString().isEmpty())
                return false;
        return true;
    }

    private void findViews() {
        profile_EDT_licence = findViewById(R.id.profile_EDT_licence);
        profile_EDT_model = findViewById(R.id.profile_EDT_model);
        profile_EDT_year = findViewById(R.id.profile_EDT_year);
        profile_BTN_save = findViewById(R.id.profile_BTN_save);
        profile_TXT_headLine = findViewById(R.id.profile_TXT_headLine);
        profile_EDT_name = findViewById(R.id.profile_EDT_name);
    }

    @Override
    public void onBackPressed() {
        if(afterSignUp) {
            signalGenerator.toast(getResources().getString(R.string.must_save), 0);
            signalGenerator.vibrate(500);
        }
        else
            super.onBackPressed();
    }

    @Override
    protected void onStop() {
        if (afterSignUp)
        {
            FirebaseAuth.getInstance().getCurrentUser().delete();
            Intent intent = new Intent(this, loginActivity.class);
            startActivity(intent);
        }
        super.onStop();
    }
}