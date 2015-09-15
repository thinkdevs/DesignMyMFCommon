package com.thinkdevs.designmymfcommon.activity;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.database.Cash;
import com.thinkdevs.designmymfcommon.database.CategoryExpense;
import com.thinkdevs.designmymfcommon.database.CategoryProfit;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.LoadResourceToDataBase;
import com.thinkdevs.designmymfcommon.database.LogoCash;
import com.thinkdevs.designmymfcommon.database.LogoCategory;
import com.thinkdevs.designmymfcommon.database.SubCategoryExpense;
import com.thinkdevs.designmymfcommon.database.SubCategoryProfit;

import java.util.List;


public class TestLoadBD {

    public static void main(){

        LoadResourceToDataBase.loadColors();
        LoadResourceToDataBase.loadLogoCash();
        LoadResourceToDataBase.loadLogoCategory();

        List<Color> colors = new Select().from(Color.class).queryList();
        List<LogoCash> logos = new Select().from(LogoCash.class).queryList();
        List<LogoCategory> logos2 = new Select().from(LogoCategory.class).queryList();


        //Сохранение валют в базу данных ********************************
        Currency currency = new Currency();
        currency.setName("Рубль");
        currency.setShortHand("РУБ");
        currency.save();

        Currency currency1 = new Currency();
        currency1.setName("Dollar");
        currency1.setShortHand("$");
        currency1.save();

        Currency currency2 = new Currency();
        currency2.setName("Euro");
        currency2.setShortHand("€");
        currency2.save();
        //-------------------------------- ********************************

        // Сохранение кошельков в Базу данных********************************
        Cash cash = new Cash();
        cash.setName("Кошелек");
        cash.setType("Кошелек");
        cash.setColor(colors.get(0));
        cash.setAmount(100);
        cash.setLogo(logos.get(0));
        cash.setCurrency(currency);
        cash.save();

        Cash cash1 = new Cash();
        cash1.setName("VISA");
        cash1.setType("Карта");
        cash1.setColor(colors.get(1));
        cash1.setAmount(300);
        cash1.setLogo(logos.get(1));
        cash1.setCurrency(currency1);
        cash1.save();

        Cash cash2 = new Cash();
        cash2.setName("MasterCard");
        cash2.setType("Карта");
        cash2.setColor(colors.get(2));
        cash2.setAmount(800);
        cash2.setLogo(logos.get(1));
        cash2.setCurrency(currency2);
        cash2.save();

        Cash cash3 = new Cash();
        cash3.setName("Maestro");
        cash3.setType("Карта");
        cash3.setColor(colors.get(3));
        cash3.setAmount(1000);
        cash3.setLogo(logos.get(1));
        cash3.setCurrency(currency1);
        cash3.save();

        Cash cash4 = new Cash();
        cash4.setName("Viza Electron");
        cash4.setType("Карта");
        cash4.setColor(colors.get(4));
        cash4.setAmount(123);
        cash4.setLogo(logos.get(1));
        cash4.setCurrency(currency);
        cash4.save();

        // --------------------------------********************************

        // Сохранение категорий в Базу данных********************************
        CategoryExpense categoryExpense = new CategoryExpense();
        categoryExpense.setName("Дети");
        categoryExpense.setColor(colors.get(0));
        categoryExpense.setLogo(logos2.get(0));
        categoryExpense.save();

        SubCategoryExpense subCategoryExpense = new SubCategoryExpense();
        subCategoryExpense.setName("Игрушки");
        subCategoryExpense.setCategory(categoryExpense);
        subCategoryExpense.save();

        SubCategoryExpense subCategoryExpense1 = new SubCategoryExpense();
        subCategoryExpense1.setName("Питание");
        subCategoryExpense1.setCategory(categoryExpense);
        subCategoryExpense1.save();

        SubCategoryExpense subCategoryExpense2 = new SubCategoryExpense();
        subCategoryExpense2.setName("Подгузники");
        subCategoryExpense2.setCategory(categoryExpense);
        subCategoryExpense2.save();

        CategoryExpense categoryExpense1 = new CategoryExpense();
        categoryExpense1.setName("Автомобиль");
        categoryExpense1.setColor(colors.get(2));
        categoryExpense1.setLogo(logos2.get(1));
        categoryExpense1.save();

        SubCategoryExpense subCategoryExpense3 = new SubCategoryExpense();
        subCategoryExpense3.setName("Бензин");
        subCategoryExpense3.setCategory(categoryExpense1);
        subCategoryExpense3.save();

        SubCategoryExpense subCategoryExpense4 = new SubCategoryExpense();
        subCategoryExpense4.setName("Запчасти");
        subCategoryExpense4.setCategory(categoryExpense1);
        subCategoryExpense4.save();

        CategoryExpense categoryExpense3 = new CategoryExpense();
        categoryExpense3.setName("Образование");
        categoryExpense3.setColor(colors.get(10));
        categoryExpense3.setLogo(logos2.get(2));
        categoryExpense3.save();

        CategoryProfit categoryProfit = new CategoryProfit();
        categoryProfit.setLogo(logos2.get(2));
        categoryProfit.setColor(colors.get(15));
        categoryProfit.setName("Работа");
        categoryProfit.save();

        SubCategoryProfit subCategoryProfit = new SubCategoryProfit();
        subCategoryProfit.setCategory(categoryProfit);
        subCategoryProfit.setName("Зарплата");
        subCategoryProfit.save();

        SubCategoryProfit subCategoryProfit1 = new SubCategoryProfit();
        subCategoryProfit1.setCategory(categoryProfit);
        subCategoryProfit1.setName("Аванс");
        subCategoryProfit1.save();

        CategoryProfit categoryProfit1 = new CategoryProfit();
        categoryProfit1.setLogo(logos2.get(2));
        categoryProfit1.setColor(colors.get(19));
        categoryProfit1.setName("Халтура");
        categoryProfit1.save();
        // --------------------------------********************************
    }
}
