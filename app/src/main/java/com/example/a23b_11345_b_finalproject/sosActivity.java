package com.example.a23b_11345_b_finalproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.a23b_11345_b_finalproject.Logical.DatePickerFormatter;
import com.example.a23b_11345_b_finalproject.Models.Claim;
import com.example.a23b_11345_b_finalproject.Utillities.Constants;
import com.example.a23b_11345_b_finalproject.Utillities.SignalGenerator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class sosActivity extends AppCompatActivity {
    private final int NUM_OF_URIS = 3;
    private ShapeableImageView sos_IMG_id;
    private ShapeableImageView sos_IMG_insurance;
    private ShapeableImageView sos_IMG_crash;
    private AppCompatEditText sos_EDT_description;
    private AppCompatEditText sos_EDT_witnesses;
    private MaterialButton sos_BTN_send;
    private DatabaseReference databaseReference;
    private StorageReference reference;
    private SignalGenerator signalGenerator;
    private ActivityResultLauncher<String> idGetContent;
    private ActivityResultLauncher<String> insuranceGetContent;
    private ActivityResultLauncher<String> crashGetContent;
    private Uri[] imageUris;
    private ProgressBar progressBar;
    private int index;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        getSupportActionBar().setTitle("S.O.S");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signalGenerator = SignalGenerator.getInstance();
        initReferences();
        findViews();
        initViews();
        initActivityResult();
        getNumOfClaimsFromDB();
        imageUris = new Uri[3];
    }

    private void initReferences() {
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DBKeys.USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.DBKeys.DB_CLAIMS);
        reference = FirebaseStorage.getInstance().getReference()
                .child(Constants.StorageKeys.IMAGES+"/" + FirebaseAuth.getInstance().getUid()
                        + "/"+Constants.StorageKeys.ST_CLAIMS);
    }

    private void getNumOfClaimsFromDB() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                num = (int)snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initActivityResult() {
        idGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        if (uri != null) {
                            imageUris[0] = uri;
                            sos_IMG_id.setImageURI(imageUris[0]);
                        } else {
                            signalGenerator.toast(getResources().getString(R.string.err_load_image), 0);
                            signalGenerator.vibrate(500);
                        }
                    }
                });
        insuranceGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        if (uri != null) {
                            imageUris[1] = uri;
                            sos_IMG_insurance.setImageURI(imageUris[1]);
                        } else {
                            signalGenerator.toast(getResources().getString(R.string.err_load_image), 0);
                            signalGenerator.vibrate(500);
                        }
                    }
                });
        crashGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        // Handle the returned Uri
                        if (uri != null) {
                            imageUris[2] = uri;
                            sos_IMG_crash.setImageURI(imageUris[2]);
                        } else {
                            signalGenerator.toast(getResources().getString(R.string.err_load_image), 0);
                            signalGenerator.vibrate(500);
                        }
                    }
                });
    }

    private void initViews() {
        sos_BTN_send.setOnClickListener(v -> sendClaim());
        sos_IMG_id.setOnClickListener(v -> uploadIdImage());
        sos_IMG_insurance.setOnClickListener(v -> uploadInsuranceImage());
        sos_IMG_crash.setOnClickListener(v -> uploadCrashImage());
        progressBar.setVisibility(View.INVISIBLE);

    }

    private void uploadCrashImage() {
        crashGetContent.launch("image/*");
    }

    private void uploadInsuranceImage() {
        insuranceGetContent.launch("image/*");
    }

    private void uploadIdImage() {
        idGetContent.launch("image/*");
    }

    private void sendClaim() {
        if (checkIfAllGood()) {
            index = 0;
            Claim claim = new Claim().setImages(new ArrayList<>());
            claim.setClaimDate(DatePickerFormatter.getTodayDate(!Locale.getDefault().getLanguage().equals("en")));
            claim.setDescription(sos_EDT_description.getText().toString());
            claim.setWitnesses(sos_EDT_witnesses.getText().toString());
            claim.setDone(false);
            claim.setuId(databaseReference.push().getKey());
            setCurrLocation(claim);
            for (int i = 0; i < NUM_OF_URIS; i++) {
                StorageReference imageName;
                Uri individualImage = imageUris[i];
                if (i == 0)
                    imageName = reference.child(claim.getuId() + "/"+Constants.StorageKeys.ID_IMAGE);
                else if (i == 1)
                    imageName = reference.child(claim.getuId() + "/"+Constants.StorageKeys.INSURANCE_IMAGE);
                else
                    imageName = reference.child(claim.getuId() + "/"+Constants.StorageKeys.CRASH_IMAGE);
                imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                index++;
                                claim.getImages().add(uri.toString());
                                if (index == 3) {
                                    databaseReference.child(num+"").setValue(claim);
                                    signalGenerator.toast(getResources().getString(R.string.images_success), 0);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signalGenerator.playUploadSound();
                                    Intent intent = new Intent(sosActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signalGenerator.toast(getResources().getString(R.string.on_more_failed), 0);
                        signalGenerator.vibrate(500);
                    }
                });
            }
        }
    }

    private void setCurrLocation(Claim claim) {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            claim.setLocation(getResources().getString(R.string.no_location_permission));
        }
        else {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    Geocoder geocoder = new Geocoder(sosActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String loc = addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryName();
                        claim.setLocation(loc);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private boolean checkIfAllGood() {
        if (sos_EDT_description.getText().toString().isEmpty()
                || sos_EDT_witnesses.getText().toString().isEmpty()
                || checkIfUriIsMissing())
        {
            signalGenerator.toast(getResources().getString(R.string.fill_fields_and_photos),0);
            signalGenerator.vibrate(500);
            return false;
        }
        return true;
    }

    private boolean checkIfUriIsMissing()
    {
        for (int i = 0 ; i<NUM_OF_URIS;i++)
            if(imageUris[i]==null)
                return true;
        return false;
    }

    private void findViews() {
        sos_IMG_id = findViewById(R.id.sos_IMG_id);
        sos_IMG_insurance = findViewById(R.id.sos_IMG_insurance);
        sos_IMG_crash = findViewById(R.id.sos_IMG_crash);
        sos_EDT_description = findViewById(R.id.sos_EDT_description);
        sos_EDT_witnesses = findViewById(R.id.sos_EDT_witnesses);
        sos_BTN_send = findViewById(R.id.sos_BTN_send);
        progressBar = findViewById(R.id.sos_PRG_bar);
    }
}