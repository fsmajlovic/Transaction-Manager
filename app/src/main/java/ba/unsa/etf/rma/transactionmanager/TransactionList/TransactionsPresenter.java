package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Budget.BudgetInteractor;
import ba.unsa.etf.rma.transactionmanager.Budget.BudgetPresenter;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

public class TransactionsPresenter implements ITransactionListPresenter, TransactionsInteractor.OnGetTransactionTypesDone{
    private ITransactionListView view;
    private Context context;
    private ArrayList<Transaction> transactions;


    public TransactionsPresenter(ITransactionListView view, Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    public void getTransactionTypes(String query) {
        new TransactionsInteractor((TransactionsInteractor.OnGetTransactionTypesDone)this).execute(query);

    }


    @Override
    public void onDoneTransactionType(ArrayList<String> results, ArrayList<Transaction> transactions) {
        view.setTransactionTypes(results);
        view.notifyTransactionTypeSetChanged();
        view.setTransactions(transactions);
        view.notifyTransactionListDataSetChanged();
    }





    //OLD
//    public TransactionsPresenter() throws ParseException {
//        interactor = new TransactionsInteractor();
//    }
//
//    public TransactionsPresenter(TransactionsInteractor interactor) throws ParseException {
//        this.interactor = interactor;
//    }
//
//    public TransactionsInteractor getInteractor() {
//        return interactor;
//    }
//
//    public void setInteractor(TransactionsInteractor interactor) {
//        this.interactor = interactor;
//    }
//
//    public void setBudget(double budget) {
//        interactor.getAccount().setBudget(budget);
//    }
//
//    public double calculateGlobalAmount(){
//        double budget = interactor.getBudget();
//        for(Transaction t: interactor.getTransactions()){
//            if(t.getType().equals(Transaction.Type.REGULARINCOME)){
//                Date startDate = t.getDate();
//                Date endDate = t.getEndDate();
//                int times = 0;
//                while(startDate.compareTo(endDate) < 0){
//                    int interval = t.getTransactionInterval();
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(startDate);
//                    c.add(Calendar.DATE, interval);
//                    startDate = c.getTime();
//                    times++;
//                }
//                if(times == 0){
//                    budget += t.getAmount();
//                }
//                else
//                    budget = budget + times*t.getAmount();
//            }
//            else if(t.getType().equals(Transaction.Type.REGULARPAYMENT)){
//                Date startDate = t.getDate();
//                Date endDate = t.getEndDate();
//                int times = 0;
//                while(startDate.compareTo(endDate) < 0){
//                    int interval = t.getTransactionInterval();
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(startDate);
//                    c.add(Calendar.DATE, interval);
//                    startDate = c.getTime();
//                    times++;
//                }
//                if(times == 0){
//                    budget -= t.getAmount();
//                }
//                else
//                    budget = budget - times*t.getAmount();
//            }
//            else if(t.getType().equals(Transaction.Type.INDIVIDUALPAYMENT) ||
//                    t.getType().equals(Transaction.Type.PURCHASE)){
//                budget -= t.getAmount();
//            }
//            else if(t.getType().equals(Transaction.Type.INDIVIDUALINCOME))
//                budget += t.getAmount();
//        }
//        return budget;
//    }


}