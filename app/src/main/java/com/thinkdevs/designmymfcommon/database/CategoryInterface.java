package com.thinkdevs.designmymfcommon.database;

import java.util.List;

public interface CategoryInterface {

    int HIERARCHY_MAIN = 0;
    int HIERARCHY_SUB  = 1;

    int TYPE_EXPENSE   = 1;
    int TYPE_PROFIT    = 2;

    long   getId();

    String getName();
    void   setName();

    Logo   getLogo();
    void   setLogo();

    Color  getColor();
    void   setColor();

    int getHierarchy();

    int getType();
    int setType();

    List<Operation> getOperations();

}
