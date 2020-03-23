package ba.unsa.etf.rma.transactionmanager;

import java.util.ArrayList;

public class userModel {
    private Account account;
    private ArrayList<Transaction> transactions;

    public userModel() {

    }

    public userModel(Account account, ArrayList<Transaction> transactions) {
        this.account = account;
        this.transactions = transactions;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
