package com.example.a23b_11345_b_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;

import com.example.a23b_11345_b_finalproject.Interfaces.Map_CallBack;

public class DragPicker extends AppCompatActivity {

    private RequestFragment requestFragment;
    private MapFragment mapFragment;
    private Map_CallBack map_callBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_picker);
        getSupportActionBar().setTitle(getResources().getString(R.string.drag_rescue));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCallBack();
        initFragments();
        beginTransactions();
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.drag_FRAME_request,requestFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.drag_FRAME_map, mapFragment).commit();
    }

    private void initCallBack() {
        map_callBack = new Map_CallBack() {
            @Override
            public void userCurrLocation(Location location) {
                mapFragment.changeMarker(location.getLatitude(),location.getLongitude());
            }
        };
    }

    private void initFragments() {
        requestFragment = new RequestFragment();
        requestFragment.setCallBack(map_callBack);
        mapFragment = new MapFragment();
    }
}