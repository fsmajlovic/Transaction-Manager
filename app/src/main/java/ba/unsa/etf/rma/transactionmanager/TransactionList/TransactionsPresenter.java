package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Budget.BudgetInteractor;
import ba.unsa.etf.rma.transactionmanager.Budget.BudgetPresenter;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;
import ba.unsa.etf.rma.transactionmanager.Util.TransactionDBOpenHelper;

public class TransactionsPresenter implements ITransactionListPresenter, TransactionsInteractor.OnGetTransactionTypesDone{
    private ITransactionListView view;
    private Context context;
    private ArrayList<Transaction> updateTransactions;


    public TransactionsPresenter(ITransactionListView view, Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    public void getTransactionTypes(Context context, String query) {
        new TransactionsInteractor(context, (TransactionsInteractor.OnGetTransactionTypesDone)this).execute(query);
    }


    @Override
    public void onDoneTransactionType(ArrayList<Transaction> trasactionsAll, ArrayList<Transaction> transactions,
                                      Account account) {
        view.setTransactions(transactions, trasactionsAll);
        view.notifyTransactionListDataSetChanged();
        double globalAmount = calculateGlobalAmount(trasactionsAll);
        view.setGlobalTotal(globalAmount, account.getTotalLimit());
        double spentOnly = calculateSpentOnly(trasactionsAll);
        view.passAccAndSpent(account.getTotalLimit(), account.getMonthLimit(), spentOnly, trasactionsAll);
    }

        public double calculateSpentOnly(ArrayList<Transaction> transactions){
            double spent = 0.0;
            for(Transaction t: transactions){
                if(t.getType().equals(Transaction.Type.REGULARPAYMENT)) {
                    Date startDate = t.getDate();
                    Date endDate = t.getEndDate();
                    int times = 0;
                    if (endDate != null) {
                        while (startDate.compareTo(endDate) < 0) {
                            int interval = t.getTransactionInterval();
                            Calendar c = Calendar.getInstance();
                            c.setTime(startDate);
                            c.add(Calendar.DATE, interval);
                            startDate = c.getTime();
                            times++;
                        }
                        if (times == 0) {
                            spent += t.getAmount();
                        } else
                            spent = spent + times * t.getAmount();
                    }
                }
                else if(t.getType().equals(Transaction.Type.INDIVIDUALPAYMENT) ||
                        t.getType().equals(Transaction.Type.PURCHASE)){
                    spent += t.getAmount();
                }
            }
            return spent;
    }


    public double calculateGlobalAmount(ArrayList<Transaction> transactions){

        double budget = 0.0;
        for(Transaction t: transactions){
            if(t.getType().equals(Transaction.Type.REGULARINCOME)) {
                Date startDate = t.getDate();
                Date endDate = t.getEndDate();
                int times = 0;
                if (endDate != null) {
                    while (startDate.compareTo(endDate) < 0) {
                        int interval = t.getTransactionInterval();
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        c.add(Calendar.DATE, interval);
                        startDate = c.getTime();
                        times++;
                    }
                    if (times == 0) {
                        budget += t.getAmount();
                    } else
                        budget = budget + times * t.getAmount();
                }
            }
            else if(t.getType().equals(Transaction.Type.REGULARPAYMENT)) {
                Date startDate = t.getDate();
                Date endDate = t.getEndDate();
                int times = 0;
                if (endDate != null) {
                    while (startDate.compareTo(endDate) < 0) {
                        int interval = t.getTransactionInterval();
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        c.add(Calendar.DATE, interval);
                        startDate = c.getTime();
                        times++;
                    }
                    if (times == 0) {
                        budget -= t.getAmount();
                    } else
                        budget = budget - times * t.getAmount();
                }
            }
            else if(t.getType().equals(Transaction.Type.INDIVIDUALPAYMENT) ||
                    t.getType().equals(Transaction.Type.PURCHASE)){
                budget -= t.getAmount();
            }
            else if(t.getType().equals(Transaction.Type.INDIVIDUALINCOME))
                budget += t.getAmount();
        }

        return budget;
    }

    @Override
    public Cursor getTransactionCursor(Context context){
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
        return cur;
    }

    @Override
    public Account getAccountInfoFromDatabase() {
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


}
