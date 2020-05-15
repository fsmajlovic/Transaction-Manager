package ba.unsa.etf.rma.transactionmanager;

import java.text.ParseException;

import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

public class GraphPresenter {
    private TransactionsInteractor interactor;


    public GraphPresenter() throws ParseException {
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
