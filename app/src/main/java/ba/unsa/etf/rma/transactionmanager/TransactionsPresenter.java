package ba.unsa.etf.rma.transactionmanager;

import java.text.ParseException;

public class TransactionsPresenter {
    private TransactionsInteractor interactor;

    public TransactionsPresenter() throws ParseException {
        interactor = new TransactionsInteractor();
    }

    public TransactionsPresenter(TransactionsInteractor interactor) throws ParseException {
        this.interactor = interactor;
    }

    public TransactionsInteractor getInteractor() {
        return interactor;
    }

    public void setInteractor(TransactionsInteractor interactor) {
        this.interactor = interactor;
    }
}
