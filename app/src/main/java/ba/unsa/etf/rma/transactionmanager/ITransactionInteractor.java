package ba.unsa.etf.rma.transactionmanager;

import android.graphics.Movie;

import java.util.ArrayList;

public interface ITransactionInteractor {
    ArrayList<Transaction> getTransactions();
    double getBudget();
    double getTotalLimit();
    double getMonthLimit();
    Account getAccount();
}
