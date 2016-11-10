package com.elysium.nodlebar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by skyfishjy on 10/31/14.
 */

//TODO -- again, credit to the creator for supplying this adapter. Found in the 'MyListCursorAdapter section here -- https://gist.github.com/skyfishjy/443b7448f59be978bc59

public class FoodListCursorAdapter extends CursorRecyclerViewAdapter<FoodCardHolder>{

    public FoodListCursorAdapter(Context context, Cursor cursor) {

        super(context, cursor);
    }

    @Override
    public FoodCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemCardView = inflater.inflate(R.layout.food_item_card_layout_basic, parent, false);

        return new FoodCardHolder(itemCardView);
    }

    @Override
    public void onBindViewHolder(final FoodCardHolder viewHolder, final Cursor cursor) {

        final long id = cursor.getLong(cursor.getColumnIndex(FoodDBHelper.COL_ID_FOOD));

        viewHolder.txtPrice
                .setText("$ " + String.valueOf(cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COL_PRICE_FOOD))));
        viewHolder.txtName
                .setText(cursor.getString(cursor.getColumnIndex(FoodDBHelper.COL_NAME_FOOD)));
        viewHolder.imgView
                .setImageResource(cursor.getInt(cursor.getColumnIndex(FoodDBHelper.COL_IMG_RES_ID_FOOD)));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), FoodDetailActivity.class);
                intent.putExtra(FoodDBHelper.COL_ID_FOOD, id);
                view.getContext().startActivity(intent);
            }
        });
    }
}