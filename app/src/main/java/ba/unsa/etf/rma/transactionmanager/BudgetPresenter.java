package ba.unsa.etf.rma.transactionmanager;

import android.content.Context;

import java.text.ParseException;


public class BudgetPresenter {
    private TransactionsInteractor interactor;


    public BudgetPresenter() throws ParseException {
        interactor = new TransactionsInteractor();
    }

    public void setBudget(double budget){
        interactor.setBudget(budget);
    }

    public void setMonthLimit(double monthLimit){
        interactor.setMonthLimit(monthLimit);
    }

    public void setTotalLimit(double totalLimit){
        interactor.setTotalLimit(totalLimit);
    }

    public TransactionsInteractor getInteractor() {
        return interactor;
    }
}