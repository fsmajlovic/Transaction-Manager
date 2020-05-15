package ba.unsa.etf.rma.transactionmanager.Budget;

import android.os.Parcelable;

import ba.unsa.etf.rma.transactionmanager.Account;

public interface IBudgetPresenter {
    void create(double budget, double totalLimit, double monthLimit);
    void setAccount(Parcelable account);
    Account getAccount();
    void searchAccount(String query);
}
