package com.thinkdevs.designmymfcommon.database;

import java.sql.Date;

public interface Operation {

    void delete();

    void setSubCategory(SubCategory subCategory);

    void setCash(Cash cash);

    void setComment(String comment);

    void setAmount(float amount);

    void setDate(Date date);

    Date getDate();

    float getAmount();

    String getComment();

    Cash getCash();

    SubCategory getSubCategory();
}
