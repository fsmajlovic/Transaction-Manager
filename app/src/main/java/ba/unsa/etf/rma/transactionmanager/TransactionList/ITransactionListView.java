package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.graphics.Movie;

import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Transaction;

public interface ITransactionListView {
    void setTransactions(ArrayList<Transaction> transactions);
    void notifyTransactionListDataSetChanged();
    void setGlobalTotal(double globalAmount, double totalLimit);
    void passAccAndSpent(double totalLimit, double monthLimit, double spentOnly, ArrayList<Transaction> transactions);
}
