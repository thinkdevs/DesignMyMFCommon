package com.thinkdevs.designmymfcommon.activitycashaccounts;

import com.thinkdevs.designmymfcommon.R;
import com.thinkdevs.designmymfcommon.mock.CashAccount;
import com.thinkdevs.designmymfcommon.mock.CashAccountMock;
import com.thinkdevs.designmymfcommon.mock.Operation;
import com.thinkdevs.designmymfcommon.mock.OperationMock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Initializator {

    public static List<CashAccount> initializeCashAccounts(){

        ArrayList<CashAccount> accounts = new ArrayList<>();

        Operation operation = new OperationMock(new Date(), -500);
        accounts.add(
                new CashAccountMock(
                "Кошелек",
                R.drawable.ic_account_balance_wallet_white_36dp,
                true,
                666,
                R.drawable.ic_attach_money_black_24dp,
                operation));
        accounts.add(
                new CashAccountMock(
                        "Карта",
                        R.drawable.ic_credit_card_white_36dp,
                        false,
                        -185652,
                        R.drawable.ic_attach_money_black_24dp,
                        operation));

        return accounts;
    }
}
