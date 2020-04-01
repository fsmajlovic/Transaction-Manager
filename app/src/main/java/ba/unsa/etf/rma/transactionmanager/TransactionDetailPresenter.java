package ba.unsa.etf.rma.transactionmanager;

import android.content.Context;

import java.text.ParseException;
import java.util.Date;

public class TransactionDetailPresenter implements ITransactionDetailPresenter{
    private Context context;
    private Transaction transaction;
    private TransactionsInteractor interactor;


    public TransactionDetailPresenter(Context context) throws ParseException {
        this.context    = context;
        interactor = new TransactionsInteractor();
    }

    @Override
    public void create(Date date, double amount, String title, Transaction.Type type, String itemDescription, int transactionInterval, Date endDate) {
        this.transaction = new Transaction(date, amount, title, type, itemDescription, transactionInterval, endDate);
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionsInteractor getInteractor() {
        return interactor;
    }

    public void setInteractor(TransactionsInteractor interactor) {
        this.interactor = interactor;
    }



    public void addTransaction(Transaction newTransaction) {
        interactor.getTransactions().add(newTransaction);
    }

    public void saveTransaction(Transaction oldTransaction, Transaction newTransaction) {
        for(Transaction t: interactor.getTransactions()){
            if(oldTransaction.equals(t)){
                t = newTransaction;
                break;
            }
        }
    }

    public void deleteTransaction(Transaction oldTransaction){
        for(Transaction t: interactor.getTransactions()){
            if(oldTransaction.equals(t)){
                interactor.getTransactions().remove(oldTransaction);
                break;
            }
        }
    }

}
