package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionDetail.ITransactionDetailPresenter;
import ba.unsa.etf.rma.transactionmanager.TransactionList.ITransactionListView;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;
import ba.unsa.etf.rma.transactionmanager.Util.TransactionDBOpenHelper;

public class TransactionDetailPresenter implements ITransactionDetailPresenter {
    private TransactionDetailInteractor Detailsinteractor;
    private ITransactionDetailView view;
    private Context context;


    public TransactionDetailPresenter(ITransactionDetailView view, Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    public void addDeleteEdit(Context context, String query, int ID, Transaction transactionNew, int action) {
        new TransactionDetailInteractor(context, ID, transactionNew, action).execute(query);
    }

    @Override
    public void saveTransactionToDatabase(Transaction transaction, int action, int transID) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionsUri = Uri.parse("content://rma.provider.transactions/elements");


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ContentValues values = new ContentValues();
        values.put(TransactionDBOpenHelper.TRANSACTION_ID, transID);
        values.put(TransactionDBOpenHelper.TRANSACTION_TITLE, transaction.getTitle());
        values.put(TransactionDBOpenHelper.TRANSACTION_DATE, transaction.getDate().toString());
        values.put(TransactionDBOpenHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE, transaction.getType().toString());
        values.put(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION, transaction.getItemDescription());
        values.put(TransactionDBOpenHelper.TRANSACTION_INTERVAL, transaction.getTransactionInterval());
        if(transaction.getEndDate() != null) {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, transaction.getDate().toString());
        }
        else {
            values.put(TransactionDBOpenHelper.TRANSACTION_END_DATE, "null");
        }
        values.put(TransactionDBOpenHelper.TRANSACTION_TYPE_ID, transaction.getTransactionTypeID());
        values.put(TransactionDBOpenHelper.TRANSACTION_ACTION, action);

        SQLiteDatabase sqldb;
        TransactionDBOpenHelper mHelper = new TransactionDBOpenHelper(context);
        try {
            sqldb = mHelper.getWritableDatabase();
        }
        catch (SQLiteException e){
            sqldb = mHelper.getReadableDatabase();
        }

        String query = "SELECT _id, actionParam FROM transactions WHERE _id = " + transaction.getInternalD();
        Cursor cursor = sqldb.rawQuery(query, null);

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            int actionBeforePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ACTION);
            int actionBefore = cursor.getInt(actionBeforePos);
            System.out.println("Action before " + actionBefore);
            if(actionBefore == 1)
                values.put(TransactionDBOpenHelper.TRANSACTION_ACTION, 1);
            String transactionInternalId = String.valueOf(transaction.getInternalD());
            cr.update(transactionsUri, values, TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + " = ?", new String[] {transactionInternalId});
        }
        else {
            cr.insert(transactionsUri, values);
        }

        cursor.close();
    }

    @Override
    public void deleteTransactionFromDatabase(Transaction transaction) {
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionsUri = Uri.parse("content://rma.provider.transactions/elements");

        String transactionInternalId = String.valueOf(transaction.getInternalD());
        cr.delete(transactionsUri,TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID + " = ?", new String[] {transactionInternalId});


    }


}
