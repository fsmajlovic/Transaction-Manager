package ba.unsa.etf.rma.transactionmanager.Comparators;

import java.util.Comparator;

import ba.unsa.etf.rma.transactionmanager.Transaction;

public class PriceComparatorAscending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction transaction, Transaction t1) {
        if(transaction.getAmount() > t1.getAmount())
            return 1;
        else if(transaction.getAmount() < t1.getAmount())
            return -1;
        else return 0;
    }
}