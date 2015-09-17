package com.thinkdevs.designmymfcommon.database;

public interface OperationFavorite {

    void delete();

    void setTitle(String title);

    String getTitle();

    void setAmount(float amount);

    float getAmount();

    void setComment(String comment);

    String getComment();

    void setSubCategory(SubCategory subCategory);

    SubCategory getSubCategory();
}
