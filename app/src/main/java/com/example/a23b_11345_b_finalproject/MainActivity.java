package com.example.a23b_11345_b_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private MaterialButton main_BTN_sos;
    private MaterialButton main_BTN_policyDetails;
    private MaterialButton main_BTN_tracking;
    private MaterialButton main_BTN_drag;
    private TextView main_TXT_name;
    private TextView main_TXT_licence;
    private TextView main_TXT_model;
    private TextView main_TXT_year;
    private DataOfUser dataOfUser;
    private DatabaseReference ref;
    private SignalGenerator signalGenerator;
    private boolean backPressed;
    private CountDownTimer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signalGenerator = SignalGenerator.getInstance();
        ref = FirebaseDatabase.getInstance()
                .getReference(Constants.DBKeys.USERS).child(FirebaseAuth.getInstance().getUid())
                .child(Constants.DBKeys.DATA_OF_USER);
        backPressed = false;
        findViews();
        initViews();
        checkAndAskLocationPermission();
        getSupportActionBar().setTitle(getResources().getString(R.string.main_menu));

    }

    private void checkAndAskLocationPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.
                    permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.main_MNU_option:
                moveToEditProfile();
                break;
            case R.id.main_MNU_Logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
        finish();
        signalGenerator.toast(getResources().getString(R.string.successfully_logedOut),0);
    }

    private void moveToEditProfile() {
        Intent intent = new Intent(this,profileEditActivity.class);
        startActivity(intent);
        finish();
    }

    private void initViews() {
        main_BTN_sos.setOnClickListener(v -> moveToSOS());
        main_BTN_policyDetails.setOnClickListener(v -> moveToPolicy());
        main_BTN_tracking.setOnClickListener(v -> moveToTracking());
        main_BTN_drag.setOnClickListener(v -> moveToDrag());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataOfUser = snapshot.getValue(DataOfUser.class);
                main_TXT_name.setText(dataOfUser.getUserName());
                main_TXT_licence.setText(dataOfUser.getLicencePlate());
                main_TXT_model.setText(dataOfUser.getModel());
                main_TXT_year.setText(dataOfUser.getYear()+"");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void moveToDrag() {
        Intent intent = new Intent(this,DragPicker.class);
        startActivity(intent);
    }

    private void moveToTracking() {
        Intent intent = new Intent(this,HistoryActivity.class);
        startActivity(intent);
    }

    private void moveToPolicy() {
        Intent intent = new Intent(this,policyDetailsActivity.class);
        startActivity(intent);
    }

    private void moveToSOS() {
        Intent intent = new Intent(this,sosActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        main_BTN_sos = findViewById(R.id.main_BTN_sos);
        main_BTN_policyDetails = findViewById(R.id.main_BTN_policyDetails);
        main_BTN_drag = findViewById(R.id.main_BTN_drag);
        main_TXT_name = findViewById(R.id.main_TXT_name);
        main_TXT_licence = findViewById(R.id.main_TXT_licence);
        main_TXT_model = findViewById(R.id.main_TXT_model);
        main_TXT_year = findViewById(R.id.main_TXT_year);
        main_BTN_tracking = findViewById(R.id.main_BTN_tracking);
    }

    @Override
    public void onBackPressed() {
        if(!backPressed)
        {
            backPressed = true;
            signalGenerator.toast(getResources().getString(R.string.press_to_exit),0);
            timer = new CountDownTimer(TimeUnit.MILLISECONDS.toMillis(2000),1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    backPressed = false;
                    timer.cancel();
                }
            }.start();
        }
        else
        {
            super.onBackPressed();
            //System.exit(0);
        }
    }
}