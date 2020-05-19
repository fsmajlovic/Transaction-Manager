package ba.unsa.etf.rma.transactionmanager.Budget;

import android.content.Context;
import android.os.Parcelable;

import ba.unsa.etf.rma.transactionmanager.Account;

public class BudgetPresenter implements IBudgetPresenter, BudgetInteractor.OnAccountSearchDone{
    private Context context;
    private IBudgetView view;
    private Account account;

    public BudgetPresenter(IBudgetView view, Context context){
        this.view = view;
        this.context = context;
    }

    public BudgetPresenter(){

    }


    @Override
    public void create(double budget, double totalLimit, double monthLimit) {
        this.account = new Account(budget, totalLimit, monthLimit);
    }

    @Override
    public void setAccount(Parcelable account) {
        this.account = (Account) account;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public void searchAccount(String query) {
        new BudgetInteractor((BudgetInteractor.OnAccountSearchDone)this).execute(query);
    }


    @Override
    public void onDone(Account result) {
        account = result;
        view.refreshView();
    }
}

//public class BudgetPresenter {
//    private TransactionsInteractor interactor;
//
//
//    public BudgetPresenter() throws ParseException {
//        interactor = new TransactionsInteractor();
//    }
//
//    public void setBudget(double budget){
//        interactor.setBudget(budget);
//    }
//
//    public void setMonthLimit(double monthLimit){
//        interactor.setMonthLimit(monthLimit);
//    }
//
//    public void setTotalLimit(double totalLimit){
//        interactor.setTotalLimit(totalLimit);
//    }
//
//    public TransactionsInteractor getInteractor() {
//        return interactor;
//    }
//}