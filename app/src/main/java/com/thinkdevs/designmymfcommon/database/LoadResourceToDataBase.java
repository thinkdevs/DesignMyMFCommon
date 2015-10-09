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

        colorIds.add(R.color.deepOrange);
        colorIds.add(R.color.brown);
        colorIds.add(R.color.grey);

        colorIds.add(R.color.blueGrey);

        for(Integer id : colorIds){
            Color color = new Color();
            color.resourceId = id;
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

        for(Integer id : logoIds){
            Logo logo = new Logo();
            logo.resourceId = id;
            logo.setType(Logo.TYPE_CASH_ACCOUNT);
            logo.save();
        }
    }

    /**
     *  Добавление иконок категорий в таблицу базы данных
     */
    public static void loadLogoCategory(){
        ArrayList<Integer> logoIds = new ArrayList<>();

        logoIds.add(R.drawable.ic_home_white_24dp);
        logoIds.add(R.drawable.ic_directions_car_white_24dp);
        logoIds.add(R.drawable.ic_ambulance_white_24dp);

        logoIds.add(R.drawable.ic_basket_white_24dp);
        logoIds.add(R.drawable.ic_bus_white_24dp);
        logoIds.add(R.drawable.ic_dumbbell_white_24dp);

        logoIds.add(R.drawable.ic_hanger_white_24dp);
        logoIds.add(R.drawable.ic_paw_white_24dp);
        logoIds.add(R.drawable.ic_pill_white_24dp);

        logoIds.add(R.drawable.ic_readability_white_24dp);
        logoIds.add(R.drawable.ic_school_white_24dp);
        logoIds.add(R.drawable.ic_theater_white_24dp);
        
        logoIds.add(R.drawable.ic_silverware_variant_white_24dp);
        logoIds.add(R.drawable.ic_responsive_white_24dp);
        logoIds.add(R.drawable.ic_worker_white_24dp);

        logoIds.add(R.drawable.ic_remove_white_24dp);

        for(Integer id : logoIds){
            Logo logo = new Logo();
            logo.resourceId = id;
            logo.setType(Logo.TYPE_CATEGORY);
            logo.save();
        }
    }
}
