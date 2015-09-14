package com.thinkdevs.designmymfcommon.database;

import java.util.List;

public interface Category {
    public long getId();

    public String getName();

    public LogoCategory getLogo();

    public Color getColor();

    public List<? extends SubCategory> getSubCategory();

    public void setColor(Color color);

    public void setLogo(LogoCategory logo);

    public void setName(String name);
}
