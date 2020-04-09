package ba.unsa.etf.rma.transactionmanager;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public void saveTransaction(Transaction oldTransaction, Transaction newTransaction) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for(Transaction t: interactor.getTransactions()){
            if(oldTransaction.equals(t)){
                t.setTitle(newTransaction.getTitle());
                System.out.println(newTransaction.getDate().toString());
                t.setDate(newTransaction.getDate());
                t.setAmount(newTransaction.getAmount());
                t.setType(newTransaction.getType());
                t.setItemDescription(newTransaction.getItemDescription());
                t.setEndDate(newTransaction.getEndDate());
                t.setTransactionInterval(newTransaction.getTransactionInterval());
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
