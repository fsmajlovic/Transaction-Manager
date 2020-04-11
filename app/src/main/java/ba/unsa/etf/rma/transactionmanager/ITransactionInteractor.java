package ba.unsa.etf.rma.transactionmanager;

import android.graphics.Movie;

import java.util.ArrayList;

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
