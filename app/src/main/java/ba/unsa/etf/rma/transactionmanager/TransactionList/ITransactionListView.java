package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.graphics.Movie;

import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Transaction;

public interface ITransactionListView {
    void setTransactions(ArrayList<Transaction> transactions);
    void setTransactionTypes(ArrayList<String> transactionTypes);
    void notifyTransactionListDataSetChanged();
    void notifyTransactionTypeSetChanged();
}
