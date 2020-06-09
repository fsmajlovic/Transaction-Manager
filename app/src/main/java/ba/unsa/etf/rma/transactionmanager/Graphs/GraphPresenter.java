package ba.unsa.etf.rma.transactionmanager.Graphs;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.ITransactionListView;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;
import ba.unsa.etf.rma.transactionmanager.Util.TransactionDBOpenHelper;

public class GraphPresenter implements IGraphsPresenter, GraphsInteractor.OnGetTransactionsDone {
    private TransactionsInteractor interactor;

    private IGraphsView view;
    private Context context;
    private ArrayList<Transaction> transactions;


    public GraphPresenter(IGraphsView view, Context context){
        this.view = view;
        this.context = context;
    }


    @Override
    public void getTransactions(Context context, String query) {
        new GraphsInteractor(context, (GraphsInteractor.OnGetTransactionsDone) this).execute(query);
    }

    @Override
    public void setTransactionsFromDatabase() {
        ArrayList<Transaction> updateTransactions = new ArrayList<>();
        ContentResolver cr = context.getApplicationContext().getContentResolver();
        Uri transactionsUri = Uri.parse("content://rma.provider.transactions/elements");

        String[] columns = new String[]{
                TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID,
                TransactionDBOpenHelper.TRANSACTION_ID,
                TransactionDBOpenHelper.TRANSACTION_TITLE,
                TransactionDBOpenHelper.TRANSACTION_DATE,
                TransactionDBOpenHelper.TRANSACTION_AMOUNT,
                TransactionDBOpenHelper.TRANSACTION_TYPE,
                TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION,
                TransactionDBOpenHelper.TRANSACTION_INTERVAL,
                TransactionDBOpenHelper.TRANSACTION_END_DATE,
                TransactionDBOpenHelper.TRANSACTION_TYPE_ID,
                TransactionDBOpenHelper.TRANSACTION_ACTION

        };
        String where = null;
        String whereArgs[] = null;
        String order = null;
        Cursor cur = cr.query(transactionsUri,columns,where,whereArgs,order);

        if(cur != null && cur.getCount() > 0){
            cur.moveToFirst();
            do{
                Transaction newTransaction = getThisTransaction(cur);
                updateTransactions.add(newTransaction);
            }while(cur.moveToNext());
            view.setTransactions(updateTransactions);
            view.validateGraphs();
        }
    }

    @Override
    public void onDoneTransactions(ArrayList<Transaction> transactions) {
        view.setTransactions(transactions);
        view.validateGraphs();
    }

    public Transaction getThisTransaction(Cursor cursor){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        int titlePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);
        int amountPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT);
        int descriptionPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION);
        int datePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DATE);
        int typePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE);
        int intervalPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERVAL);
        int endDatePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_END_DATE);
        int internalIdPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID);
        int action = 0;
        try {
            int actionPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ACTION);
            action = cursor.getInt(actionPos);
        }
        catch (IllegalArgumentException e){

        }
        int id = 0;
        try{
            int transactionIdPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ID);
            id = cursor.getInt(transactionIdPos);
        }catch (IllegalArgumentException e){

        }
        String title = cursor.getString(titlePos);
        double amount = cursor.getDouble(amountPos);
        String description = cursor.getString(descriptionPos);
        String dateString = cursor.getString(datePos);
        String type = cursor.getString(typePos);
        int interval = cursor.getInt(intervalPos);
        String endDateString = cursor.getString(endDatePos);
        int internalId = cursor.getInt(internalIdPos);



        Transaction newTransaction = new Transaction();
        newTransaction.setId(id);
        newTransaction.setTitle(title);
        newTransaction.setAmount(amount);
        newTransaction.setItemDescription(description);
        newTransaction.setAction(action);
        newTransaction.setInternalD(internalId);

        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        Date d1 = null;
        try{
            d1 = sdf3.parse(dateString);

        }catch (Exception e){ e.printStackTrace(); }

        newTransaction.setDate(d1);

        if (type.equals("INDIVIDUALPAYMENT"))
            newTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
        else if (type.equals("REGULARPAYMENT"))
            newTransaction.setType(Transaction.Type.REGULARPAYMENT);
        else if (type.equals("PURCHASE"))
            newTransaction.setType(Transaction.Type.PURCHASE);
        else if (type.equals("REGULARINCOME"))
            newTransaction.setType(Transaction.Type.REGULARINCOME);
        else if (type.equals("INDIVIDUALINCOME"))
            newTransaction.setType(Transaction.Type.INDIVIDUALINCOME);

        Date d2 = null;
        try{
            d2 = sdf3.parse(endDateString);
        }catch (Exception e){ e.printStackTrace(); }
        newTransaction.setEndDate(d2);
        newTransaction.setTransactionInterval(interval);


        return  newTransaction;
    }
}
