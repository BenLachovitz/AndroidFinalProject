package com.example.a23b_11345_b_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.a23b_11345_b_finalproject.Interfaces.CallBack_sendClick;
import com.example.a23b_11345_b_finalproject.Models.Claim;
import com.example.a23b_11345_b_finalproject.Utillities.SignalGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoryActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private DetailsFragment detailsFragment;
    private FrameLayout det;
    private LinearLayout line;
    private boolean firstClick = false;
    private CallBack_sendClick callBack_sendClick = new CallBack_sendClick() {
        @Override
        public void claimChosen(Claim claim) {
            if(!firstClick) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) det.getLayoutParams();
                params.weight = 1.0f;
                det.setLayoutParams(params);
                firstClick = true;
                line.setVisibility(View.VISIBLE);
            }
            detailsFragment.changeDetails(claim);
        }

        @Override
        public void whatChosenBeforeDelete() {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) det.getLayoutParams();
            params.weight = 0.0f;
            det.setLayoutParams(params);
            firstClick = false;
            line.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle(getResources().getString(R.string.tracking));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViews();
        initFragments();
        beginTransactions();
    }

    private void findViews()
    {
        det = findViewById(R.id.history_FRAME_det);
        line = findViewById(R.id.history_LINE_sep);
    }
    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.history_FRAME_list,listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.history_FRAME_det,detailsFragment).commit();
    }

    private void initFragments() {
        listFragment = new ListFragment();
        listFragment.setCallback(callBack_sendClick);
        detailsFragment = new DetailsFragment();
    }
}