package com.thinkdevs.designmymfcommon.database;

public interface OperationFavorite {

    void delete();

    void save();

    void update();

    void setTitle(String title);

    String getTitle();

    void setAmount(float amount);

    float getAmount();

    void setSubCategory(SubCategory subCategory);

    SubCategory getSubCategory();
}
