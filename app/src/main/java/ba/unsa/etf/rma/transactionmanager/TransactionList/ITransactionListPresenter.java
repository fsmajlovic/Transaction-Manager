package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.content.Context;

import ba.unsa.etf.rma.transactionmanager.Account;

public interface ITransactionListPresenter {
    public void getTransactionTypes(Context context, String query);
}
