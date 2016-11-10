package com.elysium.nodlebar;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jay on 11/8/16.
 */

public class UserBasket {
    List<FoodInBasket> mFoodItemList;

    private static UserBasket ourInstance = new UserBasket();

    public static UserBasket getInstance() {
        return ourInstance;
    }

    private UserBasket() {
        mFoodItemList = new LinkedList<>();

    }

    public void addToBasket(FoodInBasket foodItem){

        mFoodItemList.add(foodItem);
    }

    public List<FoodInBasket> getFoodItemInBasketList(long id){

        return mFoodItemList;
    }

    public FoodInBasket getFoodItemInBasketListById(long id){
        FoodInBasket foodInBasket = null;

        for (FoodInBasket food : mFoodItemList) {

            if (id == food.getId()){
                foodInBasket = food;
                break;
            }
        }

        return foodInBasket;
    }

    public int getTotalBill(){
        int total = 0;

        for (FoodInBasket foodInBasket : mFoodItemList) {
            total += foodInBasket.getQuantity() * foodInBasket.getPrice();
        }

        return total;
    }

}
