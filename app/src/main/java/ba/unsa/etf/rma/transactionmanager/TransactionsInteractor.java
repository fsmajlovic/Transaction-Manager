package ba.unsa.etf.rma.transactionmanager;

import java.util.ArrayList;

public class TransactionsInteractor implements ITransactionInteractor{
    @Override
    public ArrayList<Transaction> getTransactions() {
        return userModel.transactions;
    }

    @Override
    public double getBudget() {
        return userModel.account.getBudget();
    }

    @Override
    public double getTotalLimit() {
        return userModel.account.getTotalLimit();
    }

    @Override
    public double getMonthLimit() {
        return userModel.account.getMonthLimit();
    }

    @Override
    public Account getAccount() {
        return userModel.account;
    }

}
