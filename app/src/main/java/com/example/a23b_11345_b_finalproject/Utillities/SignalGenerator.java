package com.example.a23b_11345_b_finalproject.Utillities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.a23b_11345_b_finalproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignalGenerator {

    private static SignalGenerator instance = null;
    private static Vibrator vibrator;
    private MediaPlayer uploadSuccess;
    private MediaPlayer requestArrived;
    private Context context;
    private SignalGenerator (Context context){this.context=context;
        uploadSuccess = MediaPlayer.create(context, R.raw.success_sound);
        requestArrived = MediaPlayer.create(context,R.raw.the_notification_email);
    }
    public static void init(Context context)
    {
        if(instance == null)
        {
            instance = new SignalGenerator(context);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    public static SignalGenerator getInstance(){return instance;}

    public Context getContext()
    {
        return context;
    }

    public void toast (String text, int length){
        Toast.makeText(context, text, length).show();
    }

    public void vibrate(long length) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(length, VibrationEffect.DEFAULT_AMPLITUDE));

        } else {
            //deprecated in API 26
            vibrator.vibrate(length);
        }
    }

    public void imageLoader(String url, ImageView imageView)
    {
        Glide
                .with(context)
                .load(url)
                .placeholder(R.drawable.add_photo)
                .fitCenter()
                .into(imageView);
    }

    public void playUploadSound()
    {
        uploadSuccess.start();
    }
    public void playRequestSound(){requestArrived.start();}
}
