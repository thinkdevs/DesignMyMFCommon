package com.thinkdevs.designmymfcommon.activity;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.LoadResourceToDataBase;
import com.thinkdevs.designmymfcommon.database.Logo;
import com.thinkdevs.designmymfcommon.database.ParentCategory;
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

        // Сохранение счетов в Базу данных********************************
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

        // --------------------------------********************************

        //Without Category
        ParentCategory category = new ParentCategory();
        category.setType(ParentCategory.TYPE_WITHOUT);
        category.setName("Без категории");
        category.setColor(colors.get(18));
        category.setLogo(categoryLogos.get(17));
        category.save();

        //Expense categories
        ParentCategory categoryExpense1 = new ParentCategory();
        categoryExpense1.setType(ParentCategory.TYPE_EXPENSE);
        categoryExpense1.setName("Продукты");
        categoryExpense1.setColor(colors.get(0));
        categoryExpense1.setLogo(categoryLogos.get(14));
        categoryExpense1.save();

            SubCategory subCategoryExpense11 = new SubCategory();
            subCategoryExpense11.setName("Мясо");
            subCategoryExpense11.setParentCategory(categoryExpense1);
            subCategoryExpense11.save();

            SubCategory subCategoryExpense12 = new SubCategory();
            subCategoryExpense12.setName("Сладости");
            subCategoryExpense12.setParentCategory(categoryExpense1);
            subCategoryExpense12.save();

            SubCategory subCategoryExpense13 = new SubCategory();
            subCategoryExpense13.setName("Макароны, крупы");
            subCategoryExpense13.setParentCategory(categoryExpense1);
            subCategoryExpense13.save();

            SubCategory subCategoryExpense14 = new SubCategory();
            subCategoryExpense14.setName("Молочные изделия");
            subCategoryExpense14.setParentCategory(categoryExpense1);
            subCategoryExpense14.save();

        ParentCategory categoryExpense2 = new ParentCategory();
        categoryExpense2.setName("Товары для дома");
        categoryExpense2.setColor(colors.get(1));
        categoryExpense2.setLogo(categoryLogos.get(2));
        categoryExpense2.save();

            SubCategory subCategoryExpense21 = new SubCategory();
            subCategoryExpense21.setName("Ремонт");
            subCategoryExpense21.setParentCategory(categoryExpense2);
            subCategoryExpense21.save();

            SubCategory subCategoryExpense22 = new SubCategory();
            subCategoryExpense22.setName("Мебель");
            subCategoryExpense22.setParentCategory(categoryExpense2);
            subCategoryExpense22.save();

            SubCategory subCategoryExpense23 = new SubCategory();
            subCategoryExpense23.setName("Кухня");
            subCategoryExpense23.setParentCategory(categoryExpense2);
            subCategoryExpense23.save();

        ParentCategory categoryExpense3 = new ParentCategory();
        categoryExpense3.setName("Авто");
        categoryExpense3.setColor(colors.get(2));
        categoryExpense3.setLogo(categoryLogos.get(3));
        categoryExpense3.save();

            SubCategory subCategoryExpense31 = new SubCategory();
            subCategoryExpense31.setName("Бензин");
            subCategoryExpense31.setParentCategory(categoryExpense3);
            subCategoryExpense31.save();

            SubCategory subCategoryExpense32 = new SubCategory();
            subCategoryExpense32.setName("Запчасти");
            subCategoryExpense32.setParentCategory(categoryExpense3);
            subCategoryExpense32.save();

        ParentCategory categoryExpense4 = new ParentCategory();
        categoryExpense4.setName("Животные");
        categoryExpense4.setType(ParentCategory.TYPE_EXPENSE);
        categoryExpense4.setColor(colors.get(3));
        categoryExpense4.setLogo(categoryLogos.get(9));
        categoryExpense4.save();

        ParentCategory categoryExpense5 = new ParentCategory();
        categoryExpense5.setName("Спорт");
        categoryExpense5.setType(ParentCategory.TYPE_EXPENSE);
        categoryExpense5.setColor(colors.get(4));
        categoryExpense5.setLogo(categoryLogos.get(7));
        categoryExpense5.save();

    // Profit Categories
        ParentCategory categoryProfit1 = new ParentCategory();
        categoryProfit1.setType(ParentCategory.TYPE_PROFIT);
        categoryProfit1.setLogo(categoryLogos.get(16));
        categoryProfit1.setColor(colors.get(5));
        categoryProfit1.setName("Работа");
        categoryProfit1.save();

            SubCategory subCategoryProfit11 = new SubCategory();
            subCategoryProfit11.setParentCategory(categoryProfit1);
            subCategoryProfit11.setName("Зарплата");
            subCategoryProfit11.save();

            SubCategory subCategoryProfit12 = new SubCategory();
            subCategoryProfit12.setParentCategory(categoryProfit1);
            subCategoryProfit12.setName("Аванс");
            subCategoryProfit12.save();

        ParentCategory categoryProfit2 = new ParentCategory();
        categoryProfit2.setType(ParentCategory.TYPE_PROFIT);
        categoryProfit2.setLogo(categoryLogos.get(15));
        categoryProfit2.setColor(colors.get(6));
        categoryProfit2.setName("Халтура");
        categoryProfit2.save();
//         --------------------------------********************************
    }
}
