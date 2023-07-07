package com.example.a23b_11345_b_finalproject.Interfaces;

import com.example.a23b_11345_b_finalproject.Models.Claim;

public interface ClaimCallBack {
    void starClicked(Claim claim, int position);
    void itemClicked(Claim claim, int position);
    void removeItem(Claim claim, int position);

}
