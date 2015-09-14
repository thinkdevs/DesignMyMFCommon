package com.thinkdevs.designmymfcommon.database;


import com.thinkdevs.designmymfcommon.R;

import java.util.ArrayList;


public class LoadResourceToDataBase {

   /**
    *  Добавление цветов в таблицу базы данных на основе color.xml
    */
    public static void loadColors() {

        ArrayList<Integer> colorIds = new ArrayList<>();

        colorIds.add(R.color.red);
        colorIds.add(R.color.pink);
        colorIds.add(R.color.purple);
        colorIds.add(R.color.deepPurple);
        colorIds.add(R.color.indigo);
        colorIds.add(R.color.blue);
        colorIds.add(R.color.lightBlue);
        colorIds.add(R.color.cyan);
        colorIds.add(R.color.teal);
        colorIds.add(R.color.green);
        colorIds.add(R.color.lightGreen);
        colorIds.add(R.color.lime);
        colorIds.add(R.color.yellow);
        colorIds.add(R.color.amber);
        colorIds.add(R.color.orange);
        colorIds.add(R.color.red);
        colorIds.add(R.color.deepOrange);
        colorIds.add(R.color.brown);
        colorIds.add(R.color.grey);
        colorIds.add(R.color.blueGrey);

        for(Integer integer : colorIds){
            Color color = new Color();
            color.resourceId = integer;
            color.save();
        }
    }

    /**
     *  Добавление иконок счетов в таблицу базы данных
     */
    public static void loadLogoCash() {

        ArrayList<Integer> logoIds = new ArrayList<>();

        logoIds.add(R.drawable.ic_account_balance_wallet_white_36dp);
        logoIds.add(R.drawable.ic_credit_card_white_36dp);

        for(Integer integer : logoIds){
            LogoCash logoCash = new LogoCash();
            logoCash.resourceId = integer;
            logoCash.save();
        }
    }

    /**
     *  Добавление иконок категорий в таблицу базы данных
     */
    public static void loadLogoCategory(){

        ArrayList<Integer> logoIds = new ArrayList<>();

        logoIds.add(R.drawable.ic_home_white_24dp);
        logoIds.add(R.drawable.ic_directions_car_white_24dp);
        logoIds.add(R.drawable.ic_shop_white_24dp);

        for(Integer integer : logoIds){
            LogoCategory logoCategory = new LogoCategory();
            logoCategory.resourceId = integer;
            logoCategory.save();
        }
    }
}
