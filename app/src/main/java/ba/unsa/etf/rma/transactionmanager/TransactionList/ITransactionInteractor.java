package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.graphics.Movie;

import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Transaction;

public interface ITransactionInteractor {
    ArrayList<Transaction> getTransactions();
    double getBudget();
    double getTotalLimit();
    double getMonthLimit();
    void setBudget(double budget);
    void setMonthLimit(double monthLimit);
    void setTotalLimit(double totalLimit);
    Account getAccount();
}
