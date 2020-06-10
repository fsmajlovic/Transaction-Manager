package ba.unsa.etf.rma.transactionmanager.Budget;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Parcelable;

import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.Util.TransactionDBOpenHelper;

public class BudgetPresenter implements IBudgetPresenter, BudgetInteractor.OnAccountSearchDone{
    private Context context;
    private IBudgetView view;
    private Account account;

    public BudgetPresenter(IBudgetView view, Context context){
        this.view = view;
        this.context = context;
    }

    public BudgetPresenter(){

    }


    @Override
    public void create(double budget, double totalLimit, double monthLimit) {
        this.account = new Account(budget, totalLimit, monthLimit);
    }

    @Override
    public void setAccount(Parcelable account) {
        this.account = (Account) account;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void searchAccount(Context context, String query) {
        new BudgetInteractor(context, (BudgetInteractor.OnAccountSearchDone)this).execute(query);
    }

    @Override
    public Account getAccountFromDatabase() {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        String[] kolone = new String[]{
                TransactionDBOpenHelper.ACCOUNT_INTERNAL_ID,
                TransactionDBOpenHelper.ACCOUNT_BUDGET,
                TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT,
                TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT

        };
        Uri adresa = Uri.parse("content://rma.provider.accounts/elements");
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(adresa,kolone,where,whereArgs,order);

        Account retAccount = null;
        if(cur.getCount() <= 0){

        }
        else{
            cur.moveToFirst();
            int internalPod = cur.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_INTERNAL_ID);
            int budgetPos = cur.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_BUDGET);
            int monthPos = cur.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT);
            int totalPos = cur.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT);
            retAccount = new Account(cur.getDouble(budgetPos), cur.getDouble(totalPos), cur.getDouble(monthPos), cur.getInt(internalPod));
        }
        return retAccount;
    }

    @Override
    public void setAccountToDatabase(Account account) {
        if(account != null) {
            ContentResolver cr = context.getApplicationContext().getContentResolver();
            Uri accountUri = Uri.parse("content://rma.provider.accounts/elements");

            String[] kolone = new String[]{
                    TransactionDBOpenHelper.ACCOUNT_INTERNAL_ID,
                    TransactionDBOpenHelper.ACCOUNT_BUDGET,
                    TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT,
                    TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT
            };
            String where = null;
            String whereArgs[] = null;
            String order = null;
            Cursor cur = cr.query(accountUri, kolone, where, whereArgs, order);

            ContentValues values = new ContentValues();
            values.put(TransactionDBOpenHelper.ACCOUNT_BUDGET, account.getBudget());
            values.put(TransactionDBOpenHelper.ACCOUNT_MONTH_LIMIT, account.getMonthLimit());
            values.put(TransactionDBOpenHelper.ACCOUNT_TOTAL_LIMIT, account.getTotalLimit());

            if (cur.getCount() <= 0) {
                cr.insert(accountUri, values);
            } else {
                cr.delete(accountUri, null, null);
                cr.insert(accountUri, values);
            }
        }
    }


    @Override
    public void onDone(Account result) {
        account = result;
        setAccountToDatabase(account);
        view.refreshView(account);
    }

}




//        return databaseAccount;
//        SQLiteDatabase sqldb;
//        TransactionDBOpenHelper mHelper = new TransactionDBOpenHelper(context);
//        try {
//            sqldb = mHelper.getWritableDatabase();
//        }
//        catch (SQLiteException e){
//            sqldb = mHelper.getReadableDatabase();
//        }
//        String Query = "Select * from " + TransactionDBOpenHelper.ACCOUNT_TABLE;
//        Cursor cursor = sqldb.rawQuery(Query, null);
//        if(cursor.getCount() <= 0){
//            cursor.close();
//            System.out.println("Nema nista u bazi");
//        }
//        else {
//            System.out.println("Ima elemenata u bazi i to" + cursor.getCount());
//            cursor.moveToFirst();
//            int budgetPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.ACCOUNT_BUDGET);
//            System.out.println("vrijednost budzeta iz baze " + cursor.getDouble(budgetPos));
//        }
//        cursor.close();