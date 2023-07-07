package com.example.a23b_11345_b_finalproject;

import android.animation.Animator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a23b_11345_b_finalproject.Models.Claim;
import com.example.a23b_11345_b_finalproject.Utillities.SignalGenerator;
import com.google.android.material.imageview.ShapeableImageView;

import org.w3c.dom.Text;

public class DetailsFragment extends Fragment {

    private ShapeableImageView det_IMG_id;
    private ShapeableImageView det_IMG_insurance;
    private ShapeableImageView det_IMG_crash;
    private TextView det_TXT_description;
    private TextView det_TXT_witnesses;
    private TextView det_TXT_date;
    private TextView det_TXT_location;
    private SignalGenerator signalGenerator;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detailes, container, false);
        signalGenerator = SignalGenerator.getInstance();
        findViews(view);
        return view;
    }

    private void findViews(View view) {
        det_IMG_id = view.findViewById(R.id.det_IMG_id);
        det_IMG_insurance = view.findViewById(R.id.det_IMG_insurance);
        det_IMG_crash = view.findViewById(R.id.det_IMG_crash);
        det_TXT_date = view.findViewById(R.id.det_TXT_date);
        det_TXT_location = view.findViewById(R.id.det_TXT_location);
        det_TXT_description = view.findViewById(R.id.det_TXT_description);
        det_TXT_witnesses = view.findViewById(R.id.det_TXT_witnesses);
    }

    public void changeDetails(Claim claim)
    {
        signalGenerator.imageLoader(claim.getImages().get(0),det_IMG_id);
        signalGenerator.imageLoader(claim.getImages().get(1),det_IMG_insurance);
        signalGenerator.imageLoader(claim.getImages().get(2),det_IMG_crash);
        det_TXT_date.setText(claim.getClaimDate());
        det_TXT_location.setText(claim.getLocation());
        det_TXT_description.setText(claim.getDescription());
        det_TXT_witnesses.setText(claim.getWitnesses());
    }


}