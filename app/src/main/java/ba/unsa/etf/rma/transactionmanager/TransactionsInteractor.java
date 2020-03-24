package ba.unsa.etf.rma.transactionmanager;

import java.text.ParseException;

public class TransactionsInteractor {
    private userModel user;

    public TransactionsInteractor(userModel user) {
        this.user = user;
    }

    public TransactionsInteractor() throws ParseException {
        user = new userModel();
    }

    public userModel getUser() {
        return user;
    }

    public void setUser(userModel user) {
        this.user = user;
    }
}
