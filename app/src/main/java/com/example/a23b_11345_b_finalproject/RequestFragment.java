package com.example.a23b_11345_b_finalproject;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a23b_11345_b_finalproject.Interfaces.Map_CallBack;
import com.example.a23b_11345_b_finalproject.Utillities.SignalGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RequestFragment extends Fragment {

    private MaterialButton request_BTN_setLocation;
    private MaterialButton request_BTN_send;
    private TextView request_TXT_location;
    private View view;
    private Map_CallBack map_callBack;
    private Location location;
    private SignalGenerator signalGenerator;
    private CountDownTimer timer;
    private ProgressBar progressBar;
    private ShapeableImageView[] markers;
    private LinearLayout request_LYO_progress;
    private int index;
    boolean firstTick = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request, container, false);
        signalGenerator = SignalGenerator.getInstance();
        findViews(view);
        initViews();
        return view;
    }

    private void initViews() {
        request_BTN_setLocation.setOnClickListener(v -> setUserLocation());
        request_BTN_send.setOnClickListener(v -> sendRequest());
        progressBar.setProgress(0);
        progressBar.setMax(100);
    }

    private void sendRequest() {
        if (!request_TXT_location.getText().toString().isEmpty()&&
                !request_TXT_location.getText().toString().equals(getResources().getString(R.string.no_location_permission)))
        {
            request_LYO_progress.setVisibility(View.VISIBLE);
            index = 0;
            firstTick = true;
            timer = new CountDownTimer(TimeUnit.MILLISECONDS.toMillis(8500),2000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(!firstTick) {
                        progressBar.setProgress(progressBar.getProgress() + 25);
                        markers[index++].setImageResource(R.drawable.marker);
                    }
                    else
                        firstTick = false;
                }

                @Override
                public void onFinish() {
                    resetProgress();
                    signalGenerator.toast(getResources().getString(R.string.drag_arraived),0);
                    signalGenerator.playRequestSound();
                    timer.cancel();
                }
            }.start();
        }
        else
        {
            signalGenerator.toast(getResources().getString(R.string.must_location), Toast.LENGTH_SHORT);
            signalGenerator.vibrate(500);
        }
    }

    private void resetProgress() {
        for (int i = 0;i<4;i++)
            markers[i].setImageResource(R.drawable.hollowmarker);
        progressBar.setProgress(0);
        request_LYO_progress.setVisibility(View.INVISIBLE);
    }

    private void setUserLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            request_TXT_location.setText(getResources().getString(R.string.no_location_permission));
        }
        else {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    location = task.getResult();
                    map_callBack.userCurrLocation(location);
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                location.getLongitude(), 1);
                        request_TXT_location.setText(addresses.get(0).getLocality() + ", " +
                                addresses.get(0).getCountryName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void setCallBack(Map_CallBack map_callBack) {
        this.map_callBack = map_callBack;
    }

    private void findViews(View view) {
        request_BTN_setLocation = view.findViewById(R.id.request_BTN_setLocation);
        request_BTN_send = view.findViewById(R.id.request_BTN_send);
        request_TXT_location = view.findViewById(R.id.request_TXT_location);
        progressBar = view.findViewById(R.id.progressBarMain);
        markers = new ShapeableImageView[4];
        markers[0] = view.findViewById(R.id.marker1);
        markers[1] = view.findViewById(R.id.marker2);
        markers[2] = view.findViewById(R.id.marker3);
        markers[3] = view.findViewById(R.id.marker4);
        request_LYO_progress = view.findViewById(R.id.request_LYO_progress);
    }
}