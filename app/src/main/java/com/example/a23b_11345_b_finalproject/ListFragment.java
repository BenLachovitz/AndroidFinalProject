package com.example.a23b_11345_b_finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a23b_11345_b_finalproject.Adapters.ClaimAdapter;
import com.example.a23b_11345_b_finalproject.Interfaces.CallBack_sendClick;
import com.example.a23b_11345_b_finalproject.Interfaces.ClaimCallBack;
import com.example.a23b_11345_b_finalproject.Models.Claim;
import com.example.a23b_11345_b_finalproject.Utillities.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    private CallBack_sendClick callBack_sendClick;
    private View view;
    private RecyclerView history_LST_claims;
    private ArrayList<Claim> claims;
    private DatabaseReference ref;
    private ClaimAdapter claimAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        claims = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference(Constants.DBKeys.USERS).child(FirebaseAuth.getInstance().getUid())
                .child(Constants.DBKeys.DB_CLAIMS);
        findViews(view);
        initViews();
        return view;
    }

    public void setCallback(CallBack_sendClick callBack_sendClick)
    {
        this.callBack_sendClick = callBack_sendClick;
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        history_LST_claims.setLayoutManager(linearLayoutManager);
        claimAdapter = new ClaimAdapter(claims);
        history_LST_claims.setAdapter(claimAdapter);
        initAdapterCallback(claimAdapter);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Claim claim = snapshot.getValue(Claim.class);
                claims.add(claim);
                claimAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int index = Integer.parseInt(snapshot.getKey());
                claims.set(index,snapshot.getValue(Claim.class));
                claimAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        });
    }

    private void initAdapterCallback(ClaimAdapter claimAdapter)
    {
        claimAdapter.setClaimCallBack(new ClaimCallBack() {
            @Override
            public void starClicked(Claim claim, int position) {
                DatabaseReference starChangeRef = ref.child(position+"").child(Constants.DBKeys.STARED);
                starChangeRef.setValue(!claim.isStared());
            }

            @Override
            public void itemClicked(Claim claim, int position) {
                callBack_sendClick.claimChosen(claim);

            }

            @Override
            public void removeItem(Claim claim, int position) {
                StorageReference s = FirebaseStorage.getInstance().getReference()
                        .child(Constants.StorageKeys.IMAGES+ "/"
                        + FirebaseAuth.getInstance().getCurrentUser().getUid()
                        +"/"+Constants.StorageKeys.ST_CLAIMS+"/"+claims.get(position).getuId());
                s.child(Constants.StorageKeys.ID_IMAGE).delete();
                s.child(Constants.StorageKeys.INSURANCE_IMAGE).delete();
                s.child(Constants.StorageKeys.CRASH_IMAGE).delete();
                claims.remove(position);
                claimAdapter.notifyItemRemoved(position);
                ref.setValue(claims);
                callBack_sendClick.whatChosenBeforeDelete();
            }
        });
    }

    private void findViews(View view) {
        history_LST_claims = (RecyclerView) view.findViewById(R.id.history_LST_claims);
    }

}