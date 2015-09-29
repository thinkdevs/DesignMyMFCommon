package com.thinkdevs.designmymfcommon.database;

import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

public abstract class Category extends BaseModel {

    static final int HIERARCHY_PARENT = 0;
    static final int HIERARCHY_SUB  = 1;

    public static final int TYPE_EXPENSE   = 1;
    public static final int TYPE_PROFIT    = 2;

    abstract public long  getId();

    abstract public String getName();

    abstract public void   setName(String name);

    abstract public Logo   getLogo();

    abstract public Color  getColor();

    abstract public int getHierarchy();

    abstract public int getType();

    abstract public List<Operation> getOperations();



}
