package com.example.a23b_11345_b_finalproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a23b_11345_b_finalproject.Interfaces.ClaimCallBack;
import com.example.a23b_11345_b_finalproject.Models.Claim;
import com.example.a23b_11345_b_finalproject.R;
import com.example.a23b_11345_b_finalproject.Utillities.SignalGenerator;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder> {
    private ArrayList<Claim> claims;
    private ClaimCallBack claimCallBack;

    public void setClaimCallBack(ClaimCallBack claimCallBack)
    {
        this.claimCallBack = claimCallBack;
    }
    public ClaimAdapter(ArrayList<Claim> claims)
    {
        this.claims = claims;
    }

    @NonNull
    @Override
    public ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_item,parent,false);
        ClaimViewHolder claimViewHolder = new ClaimViewHolder(view);
        return claimViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimViewHolder holder, int position) {
        Claim claim = getItem(position);
        holder.claim_LBL_date.setText(claim.getClaimDate());
        holder.claim_LBL_location.setText(claim.getLocation());
        SignalGenerator.getInstance().imageLoader(claims.get(position).getImages().get(0),holder.claim_IMG_image);
        if(claim.isStared())
            holder.claim_IMG_star.setImageResource(R.drawable.star);
        else
            holder.claim_IMG_star.setImageResource(R.drawable.starhollow);
        if(claim.isDone()) {
            holder.claim_IMG_done.setImageResource(R.drawable.check);
            holder.claim_IMG_trash.setVisibility(View.VISIBLE);
        }
        else {
            holder.claim_IMG_done.setImageResource(R.drawable.hollowcheck);
            holder.claim_IMG_trash.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return this.claims == null? 0 : claims.size();
    }

    private Claim getItem(int position)
    {
        return this.claims.get(position);
    }

    public class ClaimViewHolder extends RecyclerView.ViewHolder {

        private ShapeableImageView claim_IMG_image;
        private MaterialTextView claim_LBL_date;
        private MaterialTextView claim_LBL_location;
        private ShapeableImageView claim_IMG_star;
        private ShapeableImageView claim_IMG_done;
        private ShapeableImageView claim_IMG_trash;


        public ClaimViewHolder(@NonNull View itemView) {
            super(itemView);

            claim_IMG_image = itemView.findViewById(R.id.claim_IMG_image);
            claim_LBL_date = itemView.findViewById(R.id.claim_LBL_date);
            claim_LBL_location = itemView.findViewById(R.id.claim_LBL_location);
            claim_IMG_star = itemView.findViewById(R.id.claim_IMG_star);
            claim_IMG_done = itemView.findViewById(R.id.claim_IMG_done);
            claim_IMG_trash = itemView.findViewById(R.id.claim_IMG_trash);
            itemView.setOnClickListener(v ->
            {
                if (claimCallBack != null)
                    claimCallBack.itemClicked(getItem(getAdapterPosition()),getAdapterPosition());
            });

            claim_IMG_star.setOnClickListener(v -> {
                if (claimCallBack != null)
                    claimCallBack.starClicked(getItem(getAdapterPosition()),getAdapterPosition());
            });

            claim_IMG_trash.setOnClickListener(v ->
                    claimCallBack.removeItem(getItem(getAdapterPosition()),getAdapterPosition()));

        }
    }
}
