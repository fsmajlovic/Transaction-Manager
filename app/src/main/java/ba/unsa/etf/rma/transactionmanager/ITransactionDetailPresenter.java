package ba.unsa.etf.rma.transactionmanager;

import java.util.Date;

public interface ITransactionDetailPresenter {
    void create(Date date, double amount, String title, Transaction.Type type, String itemDescription, int transactionInterval, Date endDate);
    Transaction getTransaction();
    TransactionsInteractor getInteractor();
}