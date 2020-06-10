package ba.unsa.etf.rma.transactionmanager.Budget;

import android.content.Context;
import android.os.Parcelable;

import ba.unsa.etf.rma.transactionmanager.Account;

public interface IBudgetPresenter {
    void create(double budget, double totalLimit, double monthLimit);
    void setAccount(Parcelable account);
    Account getAccount();
    void searchAccount(Context context, String query);
    Account getAccountFromDatabase();
    void setAccountToDatabase(Account account);

}
