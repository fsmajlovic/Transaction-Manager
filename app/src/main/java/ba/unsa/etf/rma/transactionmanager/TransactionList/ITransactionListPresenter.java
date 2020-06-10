package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Transaction;

public interface ITransactionListPresenter {
    void getTransactionTypes(Context context, String query);
    Cursor getTransactionCursor(Context context, Calendar calendar);
    Account getAccountInfoFromDatabase();
}
