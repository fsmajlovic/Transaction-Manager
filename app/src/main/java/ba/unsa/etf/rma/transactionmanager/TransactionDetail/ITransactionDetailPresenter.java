package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

public interface ITransactionDetailPresenter {
    void addDeleteEdit(Context context, String query, int ID, Transaction transactionNew, int action);
    void saveTransactionToDatabase(Transaction transaction, int action, int transID);

    void deleteTransactionFromDatabase(Transaction thisTransaction);

//    //Old
//    void create(Date date, double amount, String title, Transaction.Type type, String itemDescription, int transactionInterval, Date endDate);
//    Transaction getTransaction();
//    TransactionsInteractor getInteractor();
}