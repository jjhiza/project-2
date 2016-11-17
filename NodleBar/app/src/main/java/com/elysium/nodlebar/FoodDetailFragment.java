package com.elysium.nodlebar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by jay on 11/10/16.
 */

public class FoodDetailFragment extends Fragment implements View.OnClickListener {

    ImageView imgView, btnAddToBasket;
    TextView txtName, txtDescription, txtCategory, txtQuantityInCart, txtStockCount;
    long foodId;
    Food food;

    FoodDBHelper dbHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_detail_layout, container, false);

        imgView = (ImageView) view.findViewById(R.id.imgViewDetail);
        btnAddToBasket = (ImageView) view.findViewById(R.id.imgBtnAddToCart);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtCategory = (TextView) view.findViewById(R.id.txtCategory);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);
        txtQuantityInCart = (TextView) view.findViewById(R.id.txtQuantityInBasket);
        txtStockCount = (TextView) view.findViewById(R.id.txtCount);

        foodId = getArguments().getLong(FoodDBHelper.COL_ID_FOOD, 1);
        dbHelper = FoodDBHelper.getInstance(getActivity().getApplicationContext());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_page_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        food = dbHelper.getFoodById(foodId);

        FoodInBasket foodInBasket = UserBasket.getInstance().getFoodItemInBasketListById(foodId);

        imgView.setImageResource(food.getImgResId());
        txtName.setText(food.getName());
        txtCategory.setText(food.getCategory());
        txtDescription.setText(food.getDesc());
        txtStockCount.setText(String.valueOf(food.getCount()));

        if (foodInBasket != null)
            txtQuantityInCart.setText(String.valueOf(foodInBasket.getQuantity()));
        else
            txtQuantityInCart.setText(String.valueOf(0));

        btnAddToBasket.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.imgBtnAddToCart:

                List<FoodInBasket> basketList = UserBasket.getInstance().mFoodItemList;

                Food food = dbHelper.getFoodById(foodId);

                int indexInList = -1;

                for (int i = 0; i < basketList.size(); i++) {
                    if (foodId == basketList.get(i).getId()) {
                        indexInList = i;
                        break;
                    }
                }

                FoodInBasket foodInBasket = null;

                if (indexInList < 0) {

                    if (food.getCount() > 0) {
                        foodInBasket = new FoodInBasket(food, 1, Constants.user.getId());
                        UserBasket.getInstance().addToBasket(foodInBasket);

                    } else {

                        Toast.makeText(getActivity(), "Sorry, this item is out of stock",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    foodInBasket = basketList.get(indexInList);
                    foodInBasket.addToQuantity();
                }

                if (foodInBasket != null)
                    txtQuantityInCart.setText(String.valueOf(foodInBasket.getQuantity()));

                else{
                    txtQuantityInCart.setText(String.valueOf(0));
                }
        }
    }
}
