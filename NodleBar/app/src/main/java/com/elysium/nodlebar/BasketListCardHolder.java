package com.elysium.nodlebar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jay on 11/9/16.
 */

public class BasketListCardHolder extends RecyclerView.ViewHolder {
    ImageView imgView;
    TextView txtName, txtPrice, txtQuantity, txtSubTotal, txtBtnChangeQuantity;

    public BasketListCardHolder(View itemView) {
        super(itemView);
        imgView = (ImageView) itemView.findViewById(R.id.imgViewBasketItem);
        txtName = (TextView) itemView.findViewById(R.id.txtName);
        txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
        txtQuantity = (TextView) itemView.findViewById(R.id.txtQuantity);
        txtSubTotal = (TextView) itemView.findViewById(R.id.txtSubtotal);
        txtBtnChangeQuantity = (TextView) itemView.findViewById(R.id.txtBtnChangeQuantity);
    }
}
