package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionDetail.ITransactionDetailPresenter;
import ba.unsa.etf.rma.transactionmanager.TransactionList.ITransactionListView;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

public class TransactionDetailPresenter implements ITransactionDetailPresenter {
    private TransactionDetailInteractor Detailsinteractor;
    private ITransactionDetailView view;
    private Context context;


    public TransactionDetailPresenter(ITransactionDetailView view, Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    public void addDeleteEdit(String query, int ID, Transaction transactionNew, int action) {
        new TransactionDetailInteractor(ID, transactionNew, action).execute(query);
    }

    //OLD
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

    public double calculateSpentOnly(){
            double spent = 0.0;
            for(Transaction t: interactor.getTransactions()){
                if(t.getType().equals(Transaction.Type.REGULARPAYMENT)){
                    Date startDate = t.getDate();
                    Date endDate = t.getEndDate();
                    int times = 0;
                    while(startDate.compareTo(endDate) < 0){
                        int interval = t.getTransactionInterval();
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        c.add(Calendar.DATE, interval);
                        startDate = c.getTime();
                        times++;
                    }
                    if(times == 0){
                        spent += t.getAmount();
                    }
                    else
                        spent = spent + times*t.getAmount();
                }
                else if(t.getType().equals(Transaction.Type.INDIVIDUALPAYMENT) ||
                        t.getType().equals(Transaction.Type.PURCHASE)){
                    spent += t.getAmount();
                }
            }
            return spent;
    }

}
