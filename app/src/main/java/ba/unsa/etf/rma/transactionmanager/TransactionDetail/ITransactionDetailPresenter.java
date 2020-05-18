package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

public interface ITransactionDetailPresenter {
    void addDeleteEdit(String query, Transaction transactionOld, Transaction transactionNew, int action);

    //Old
    void create(Date date, double amount, String title, Transaction.Type type, String itemDescription, int transactionInterval, Date endDate);
    Transaction getTransaction();
    TransactionsInteractor getInteractor();
}