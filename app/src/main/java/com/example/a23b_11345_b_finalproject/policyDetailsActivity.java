package com.example.a23b_11345_b_finalproject;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a23b_11345_b_finalproject.Logical.DatePickerFormatter;
import com.example.a23b_11345_b_finalproject.Models.Policy;
import com.example.a23b_11345_b_finalproject.Utillities.Constants;
import com.example.a23b_11345_b_finalproject.Utillities.SignalGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Locale;

public class policyDetailsActivity extends AppCompatActivity {
    private Policy policy;
    private DatabaseReference databaseReference;
    private StorageReference reference;
    private ShapeableImageView policy_IMG_pImage;
    private AppCompatEditText policy_EDT_price;
    private TextView policy_TXT_untilDate;
    private MaterialButton policy_BTN_save;
    private MaterialButton policy_BTN_upload;
    private MaterialButton policy_BTN_datePicker;
    private DatePickerDialog datePickerDialog;
    private SignalGenerator signalGenerator;
    ActivityResultLauncher<String> mGetContent;
    private Uri imageUri;
    private ProgressBar progressBar;
    private boolean isHeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_details);
        getSupportActionBar().setTitle(getResources().getString(R.string.policy_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signalGenerator = SignalGenerator.getInstance();
        isHeb = Locale.getDefault().toLanguageTag().equals("he-IL");
        Log.d("isHeb", isHeb+"");
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DBKeys.USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Constants.DBKeys.POLICY);
        reference = FirebaseStorage.getInstance().getReference()
                .child(Constants.StorageKeys.IMAGES+"/"
                        +FirebaseAuth.getInstance().getCurrentUser().getUid()
                        +"/"+Constants.StorageKeys.ST_POLICY+"/"+Constants.StorageKeys.THE_POLICY);
        findViews();
        initDatePicker();
        initViews();
        initActivityResult();
    }

    private void initActivityResult()
    {
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                // Handle the returned Uri
                if (uri != null)
                {
                    imageUri = uri;
                    policy_IMG_pImage.setImageURI(imageUri);
                    uploadToFirebase();
                }
                else
                {
                    signalGenerator.toast(getResources().getString(R.string.err_load_image),0);
                    signalGenerator.vibrate(500);
                }
            }
        });
    }

    private void initViews() {
        policy_BTN_save.setOnClickListener(v -> saveData());
        policy_BTN_upload.setOnClickListener(v -> uploadImage());
        policy_BTN_datePicker.setOnClickListener(v -> openDatePicker());
        progressBar.setVisibility(View.INVISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                policy = snapshot.getValue(Policy.class);
                if (policy != null) {
                    signalGenerator.imageLoader(policy.getPolicyIMG(),policy_IMG_pImage);
                    if(policy.getrDate() != null) {
                        policy_BTN_datePicker.setText(policy.getrDate());
                        policy_EDT_price.setText(policy.getPrice() + "");
                        policy_TXT_untilDate.setText(policy.getuDate());
                    }
                }
                else
                    policy = new Policy();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void openDatePicker() {
        datePickerDialog.show();
    }
    private void uploadImage() {
        mGetContent.launch("image/*");
    }

    private void saveData() {
        if(!policy_BTN_datePicker.getText().toString().equals(getResources().getString(R.string.select_date))
                && !policy_EDT_price.getText().toString().isEmpty())
        {
            policy.setrDate(policy_BTN_datePicker.getText().toString());
            policy.setPrice(Float.parseFloat(policy_EDT_price.getText().toString()));
            databaseReference.setValue(policy);
        }
        else
        {
            signalGenerator.toast(getResources().getString(R.string.must_fill_fileds),0);
            signalGenerator.vibrate(500);
        }
    }

    private void uploadToFirebase()
    {
        StorageReference fileRef = reference;
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        policy.setPolicyIMG(uri.toString());
                        databaseReference.setValue(policy);
                        progressBar.setVisibility(View.INVISIBLE);
                        signalGenerator.toast(getResources().getString(R.string.images_success),0);
                        signalGenerator.playUploadSound();
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
                signalGenerator.toast(getResources().getString(R.string.err_load_image),0);
                signalGenerator.vibrate(500);
            }
        });
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                policy_BTN_datePicker.setText(DatePickerFormatter
                        .makeDateString(dayOfMonth,month+1,year,isHeb));
                policy.setuDate(DatePickerFormatter
                        .makeDateString(dayOfMonth,month+1,year+1,isHeb));
            }
        };
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this
                ,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private void findViews() {
        policy_IMG_pImage  = findViewById(R.id.policy_IMG_pImage);
        policy_EDT_price = findViewById(R.id.policy_EDT_price);
        policy_TXT_untilDate = findViewById(R.id.policy_TXT_untilDate);
        policy_BTN_save = findViewById(R.id.policy_BTN_save);
        policy_BTN_upload = findViewById(R.id.policy_BTN_upload);
        progressBar = findViewById(R.id.policy_PRG_bar);
        policy_BTN_datePicker = findViewById(R.id.policy_BTN_datePicker);
    }
}