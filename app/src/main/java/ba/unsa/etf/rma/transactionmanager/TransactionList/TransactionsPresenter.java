package ba.unsa.etf.rma.transactionmanager.TransactionList;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Budget.BudgetInteractor;
import ba.unsa.etf.rma.transactionmanager.Budget.BudgetPresenter;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

public class TransactionsPresenter implements ITransactionListPresenter, TransactionsInteractor.OnGetTransactionTypesDone{
    private ITransactionListView view;
    private Context context;


    public TransactionsPresenter(ITransactionListView view, Context context){
        this.view = view;
        this.context = context;
    }

    @Override
    public void getTransactionTypes(String query) {
        new TransactionsInteractor((TransactionsInteractor.OnGetTransactionTypesDone)this).execute(query);
    }


    @Override
    public void onDoneTransactionType(ArrayList<Transaction> trasactionsAll, ArrayList<Transaction> transactions,
                                      Account account) {
        view.setTransactions(transactions);
        view.notifyTransactionListDataSetChanged();
        double globalAmount = calculateGlobalAmount(trasactionsAll);
        view.setGlobalTotal(globalAmount, account.getTotalLimit());
        double spentOnly = calculateSpentOnly(trasactionsAll);
        view.passAccAndSpent(account.getTotalLimit(), account.getMonthLimit(), spentOnly, trasactionsAll);
    }

        public double calculateSpentOnly(ArrayList<Transaction> transactions){
            double spent = 0.0;
            for(Transaction t: transactions){
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


    public double calculateGlobalAmount(ArrayList<Transaction> transactions){

        double budget = 0.0;
        for(Transaction t: transactions){
            if(t.getType().equals(Transaction.Type.REGULARINCOME)){
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
                    budget += t.getAmount();
                }
                else
                    budget = budget + times*t.getAmount();
            }
            else if(t.getType().equals(Transaction.Type.REGULARPAYMENT)){
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
                    budget -= t.getAmount();
                }
                else
                    budget = budget - times*t.getAmount();
            }
            else if(t.getType().equals(Transaction.Type.INDIVIDUALPAYMENT) ||
                    t.getType().equals(Transaction.Type.PURCHASE)){
                budget -= t.getAmount();
            }
            else if(t.getType().equals(Transaction.Type.INDIVIDUALINCOME))
                budget += t.getAmount();
        }
        return budget;
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



}
