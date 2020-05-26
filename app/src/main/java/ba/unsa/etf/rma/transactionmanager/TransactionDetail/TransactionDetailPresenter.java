package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionDetail.ITransactionDetailPresenter;
import ba.unsa.etf.rma.transactionmanager.TransactionList.ITransactionListView;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

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

}
