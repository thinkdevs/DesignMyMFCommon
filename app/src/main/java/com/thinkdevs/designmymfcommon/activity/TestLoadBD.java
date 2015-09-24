package com.thinkdevs.designmymfcommon.activity;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.LoadResourceToDataBase;
import com.thinkdevs.designmymfcommon.database.Logo;
import com.thinkdevs.designmymfcommon.database.SubCategory;

import java.util.List;


public class TestLoadBD {

    public static void main(){

        LoadResourceToDataBase.loadColors();
        LoadResourceToDataBase.loadLogoCash();
        LoadResourceToDataBase.loadLogoCategory();

        List<Color> colors = new Select().from(Color.class).queryList();
        List<Logo> cashAccountLogos = Logo.getAllCashAccountLogos();
        List<Logo> categoryLogos    = Logo.getAllCategoryLogos();

        //Сохранение валют в базу данных ********************************
        Currency currency = new Currency();
        currency.setName("Рубль");
        currency.setStrSymbol("РУБ");
        currency.save();

        Currency currency1 = new Currency();
        currency1.setName("Dollar");
        currency1.setStrSymbol("$");
        currency1.save();

        Currency currency2 = new Currency();
        currency2.setName("Euro");
        currency2.setStrSymbol("€");
        currency2.save();
        //-------------------------------- ********************************

        // Сохранение кошельков в Базу данных********************************
        CashAccount cash = new CashAccount();
        cash.setName("Кошелек");
        cash.setComment("Наличные");
        cash.setColor(colors.get(17));
        cash.setAmount(0);
        cash.setLogo(cashAccountLogos.get(0));
        cash.setCurrency(currency);
        cash.save();

        CashAccount cash1 = new CashAccount();
        cash1.setName("Тинькофф");
        cash1.setComment("MasterCard / Debit");
        cash1.setColor(colors.get(12));
        cash1.setAmount(0);
        cash1.setLogo(cashAccountLogos.get(1));
        cash1.setCurrency(currency);
        cash1.save();

        CashAccount cash2 = new CashAccount();
        cash2.setName("Сбербанк");
        cash2.setComment("MasterCard / Debit");
        cash2.setColor(colors.get(9));
        cash2.setAmount(0);
        cash2.setLogo(cashAccountLogos.get(1));
        cash2.setCurrency(currency);
        cash2.save();
//
//        CashAccount cash3 = new CashAccount();
//        cash3.setName("Maestro");
//        cash3.setComment("Карта");
//        cash3.setColor(colors.get(3));
//        cash3.setAmount(1000);
//        cash3.setLogo(cashAccountLogos.get(1));
//        cash3.setCurrency(currency1);
//        cash3.save();
//
//        CashAccount cash4 = new CashAccount();
//        cash4.setName("Viza Electron");
//        cash4.setComment("Карта");
//        cash4.setColor(colors.get(4));
//        cash4.setAmount(123);
//        cash4.setLogo(cashAccountLogos.get(1));
//        cash4.setCurrency(currency);
//        cash4.save();

        // --------------------------------********************************

        // Сохранение категорий в Базу данных********************************
//        Category categoryExpense = new Category();
//        categoryExpense.setType(Category.TYPE_EXPENSE);
//        categoryExpense.setName("Дети");
//        categoryExpense.setColor(colors.get(0));
//        categoryExpense.setLogo(categoryLogos.get(0));
//        categoryExpense.save();
//
//        SubCategory subCategoryExpense = new SubCategory();
//        subCategoryExpense.setName("Игрушки");
//        subCategoryExpense.setCategory(categoryExpense);
//        subCategoryExpense.save();
//
//        SubCategory subCategoryExpense1 = new SubCategory();
//        subCategoryExpense1.setName("Питание");
//        subCategoryExpense1.setCategory(categoryExpense);
//        subCategoryExpense1.save();
//
//        SubCategory subCategoryExpense2 = new SubCategory();
//        subCategoryExpense2.setName("Подгузники");
//        subCategoryExpense2.setCategory(categoryExpense);
//        subCategoryExpense2.save();
//
//        Category categoryExpense1 = new Category();
//        categoryExpense1.setName("Автомобиль");
//        categoryExpense1.setColor(colors.get(2));
//        categoryExpense1.setLogo(categoryLogos.get(1));
//        categoryExpense1.save();
//
//        SubCategory subCategoryExpense3 = new SubCategory();
//        subCategoryExpense3.setName("Бензин");
//        subCategoryExpense3.setCategory(categoryExpense1);
//        subCategoryExpense3.save();
//
//        SubCategory subCategoryExpense4 = new SubCategory();
//        subCategoryExpense4.setName("Запчасти");
//        subCategoryExpense4.setCategory(categoryExpense1);
//        subCategoryExpense4.save();
//
//        Category categoryExpense3 = new Category();
//        categoryExpense3.setName("Образование");
//        categoryExpense3.setType(Category.TYPE_EXPENSE);
//        categoryExpense3.setColor(colors.get(10));
//        categoryExpense3.setLogo(categoryLogos.get(2));
//        categoryExpense3.save();
//
//        Category categoryProfit = new Category();
//        categoryProfit.setType(Category.TYPE_PROFIT);
//        categoryProfit.setLogo(categoryLogos.get(2));
//        categoryProfit.setColor(colors.get(15));
//        categoryProfit.setName("Работа");
//        categoryProfit.save();
//
//        SubCategory subCategoryProfit = new SubCategory();
//        subCategoryProfit.setCategory(categoryProfit);
//        subCategoryProfit.setName("Зарплата");
//        subCategoryProfit.save();
//
//        SubCategory subCategoryProfit1 = new SubCategory();
//        subCategoryProfit1.setCategory(categoryProfit);
//        subCategoryProfit1.setName("Аванс");
//        subCategoryProfit1.save();
//
//        Category categoryProfit1 = new Category();
//        categoryProfit1.setName(Category.TYPE_PROFIT);
//        categoryProfit1.setLogo(categoryLogos.get(2));
//        categoryProfit1.setColor(colors.get(19));
//        categoryProfit1.setName("Халтура");
//        categoryProfit1.save();
        // --------------------------------********************************
    }
}
