package com.elysium.nodlebar;

/**
 * Created by jay on 11/9/16.
 */

public class FoodItem {

    private int quantity;
    long id, foodId, userId;
    private boolean purchased;

    public FoodItem(long foodId) {

        this.quantity = 1;
        this.userId = Constants.user.getId();
        this.foodId = foodId;
        this.purchased = false;
    }

    public FoodItem(boolean purchased, long id, int quantity, long foodId, long userId) {

        this.purchased = purchased;
        this.id = id;
        this.quantity = quantity;
        this.foodId = foodId;
        this.userId = userId;
    }

    public FoodItem(int quantity, long foodId, long userId) {

        this.purchased = true;
        this.quantity = quantity;
        this.foodId = foodId;
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
    }
}
