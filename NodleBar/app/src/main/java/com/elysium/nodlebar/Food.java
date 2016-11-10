package com.elysium.nodlebar;

/**
 * Created by jay on 11/9/16.
 */

public class Food {

    private int id, count, imgResId, price;
    private String name, desc, category, tags;

    public Food(int id, int count, int imgResId, String name, String desc, String category, String tags, int price) {
        this.id = id;
        this.count = count;
        this.imgResId = imgResId;
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.tags = tags;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
