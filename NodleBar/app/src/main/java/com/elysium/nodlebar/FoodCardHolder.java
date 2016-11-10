package com.elysium.nodlebar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jay on 11/9/16.
 */

public class FoodCardHolder extends RecyclerView.ViewHolder {
    ImageView imgView;
    TextView txtName;
    TextView txtPrice;

    public FoodCardHolder(View itemView) {
        super(itemView);
        imgView = (ImageView) itemView.findViewById(R.id.imgView);
        txtName = (TextView) itemView.findViewById(R.id.txtFoodName);
        txtPrice = (TextView) itemView.findViewById(R.id.txtFoodPrice);
    }
}
