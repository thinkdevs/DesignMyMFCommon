package com.thinkdevs.designmymfcommon.main;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.thinkdevs.designmymfcommon.database.CashAccount;
import com.thinkdevs.designmymfcommon.database.Category;
import com.thinkdevs.designmymfcommon.database.Color;
import com.thinkdevs.designmymfcommon.database.Currency;
import com.thinkdevs.designmymfcommon.database.Icon;
import com.thinkdevs.designmymfcommon.database.LoadResourceToDataBase;

import java.util.List;


public class LoadToDataBase {

    public static void main(){
        LoadResourceToDataBase.loadColors();
        LoadResourceToDataBase.loadLogoCash();
        LoadResourceToDataBase.loadLogoCategory();

        List<Color> colors = new Select().from(Color.class).queryList();
        List<Icon> cashAccountIcons = Icon.getCashAccountIcons();
        List<Icon> categoryIcons = Icon.getCategoryIcons();

        //Сохранение валют в базу данных ********************************
        Currency currency = new Currency();
        currency.setName("Рубль");
        currency.setStrSymbol("\u20BD");
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
        cash.setDescription("Наличные");
        cash.setColor(colors.get(16));
        cash.setAmount(0);
        cash.setIcon(cashAccountIcons.get(0));
        cash.setCurrency(currency);
        cash.save();

        CashAccount cash1 = new CashAccount();
        cash1.setName("Тинькофф");
        cash1.setDescription("MasterCard / Debit");
        cash1.setColor(colors.get(14));
        cash1.setAmount(0);
        cash1.setIcon(cashAccountIcons.get(1));
        cash1.setCurrency(currency);
        cash1.save();

        CashAccount cash2 = new CashAccount();
        cash2.setName("Сбербанк");
        cash2.setDescription("MasterCard / Debit");
        cash2.setColor(colors.get(9));
        cash2.setAmount(0);
        cash2.setIcon(cashAccountIcons.get(1));
        cash2.setCurrency(currency);
        cash2.save();

        // --------------------------------********************************

        //Without Sub Category Expense
        Category categoryEmptySub = new Category();
        categoryEmptySub.setType(Category.WITHOUT_TYPE);
        categoryEmptySub.setName("");
        categoryEmptySub.setColor(colors.get(18));
        categoryEmptySub.save();

        //Without Parent Category Expense
        Category categoryEmptyParentExpense = new Category();
        categoryEmptyParentExpense.setType(Category.EXPENSE);
        categoryEmptyParentExpense.setName("Без категории");
        categoryEmptyParentExpense.setColor(colors.get(18));
        categoryEmptyParentExpense.setIcon(categoryIcons.get(15));
        categoryEmptyParentExpense.save();

        //Without Parent Category Profit
        Category categoryEmptyParentProfit = new Category();
        categoryEmptyParentProfit.setType(Category.PROFIT);
        categoryEmptyParentProfit.setName("Без категории");
        categoryEmptyParentProfit.setColor(colors.get(18));
        categoryEmptyParentProfit.setIcon(categoryIcons.get(15));
        categoryEmptyParentProfit.save();

        //Expense categories
        Category categoryExpense1 = new Category();
        categoryExpense1.setType(Category.EXPENSE);
        categoryExpense1.setName("Продукты");
        categoryExpense1.setColor(colors.get(0));
        categoryExpense1.setIcon(categoryIcons.get(12));
        categoryExpense1.save();

            Category subCategoryExpense11 = new Category();
            subCategoryExpense11.setName("Мясо");
            subCategoryExpense11.setParent(categoryExpense1);
            subCategoryExpense11.save();

            Category subCategoryExpense12 = new Category();
            subCategoryExpense12.setName("Сладости");
            subCategoryExpense12.setParent(categoryExpense1);
            subCategoryExpense12.save();

            Category subCategoryExpense13 = new Category();
            subCategoryExpense13.setName("Макароны, крупы");
            subCategoryExpense13.setParent(categoryExpense1);
            subCategoryExpense13.save();

            Category subCategoryExpense14 = new Category();
            subCategoryExpense14.setName("Молочные изделия");
            subCategoryExpense14.setParent(categoryExpense1);
            subCategoryExpense14.save();

        Category categoryExpense2 = new Category();
        categoryExpense2.setName("Товары для дома");
        categoryExpense2.setType(Category.EXPENSE);
        categoryExpense2.setColor(colors.get(1));
        categoryExpense2.setIcon(categoryIcons.get(0));
        categoryExpense2.save();

            Category subCategoryExpense21 = new Category();
            subCategoryExpense21.setName("Ремонт");
            subCategoryExpense21.setParent(categoryExpense2);
            subCategoryExpense21.save();

            Category subCategoryExpense22 = new Category();
            subCategoryExpense22.setName("Мебель");
            subCategoryExpense22.setParent(categoryExpense2);
            subCategoryExpense22.save();

            Category subCategoryExpense23 = new Category();
            subCategoryExpense23.setName("Кухня");
            subCategoryExpense23.setParent(categoryExpense2);
            subCategoryExpense23.save();

        Category categoryExpense3 = new Category();
        categoryExpense3.setName("Авто");
        categoryExpense3.setType(Category.EXPENSE);
        categoryExpense3.setColor(colors.get(2));
        categoryExpense3.setIcon(categoryIcons.get(1));
        categoryExpense3.save();

            Category subCategoryExpense31 = new Category();
            subCategoryExpense31.setName("Бензин");
            subCategoryExpense31.setParent(categoryExpense3);
            subCategoryExpense31.save();

            Category subCategoryExpense32 = new Category();
            subCategoryExpense32.setName("Запчасти");
            subCategoryExpense32.setParent(categoryExpense3);
            subCategoryExpense32.save();

        Category categoryExpense4 = new Category();
        categoryExpense4.setName("Животные");
        categoryExpense4.setType(Category.EXPENSE);
        categoryExpense4.setColor(colors.get(3));
        categoryExpense4.setIcon(categoryIcons.get(7));
        categoryExpense4.save();

        Category categoryExpense5 = new Category();
        categoryExpense5.setName("Спорт");
        categoryExpense5.setType(Category.EXPENSE);
        categoryExpense5.setColor(colors.get(4));
        categoryExpense5.setIcon(categoryIcons.get(5));
        categoryExpense5.save();

    // Profit Categories
        Category categoryProfit1 = new Category();
        categoryProfit1.setName("Работа");
        categoryProfit1.setType(Category.PROFIT);
        categoryProfit1.setIcon(categoryIcons.get(14));
        categoryProfit1.setColor(colors.get(5));
        categoryProfit1.save();

            Category subCategoryProfit11 = new Category();
            subCategoryProfit11.setParent(categoryProfit1);
            subCategoryProfit11.setName("Зарплата");
            subCategoryProfit11.save();

            Category subCategoryProfit12 = new Category();
            subCategoryProfit12.setParent(categoryProfit1);
            subCategoryProfit12.setName("Аванс");
            subCategoryProfit12.save();

        Category categoryProfit2 = new Category();
        categoryProfit2.setName("Халтура");
        categoryProfit2.setType(Category.PROFIT);
        categoryProfit2.setIcon(categoryIcons.get(13));
        categoryProfit2.setColor(colors.get(6));
        categoryProfit2.save();
//         --------------------------------********************************
    }
}
