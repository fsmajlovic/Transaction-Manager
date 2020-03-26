package ba.unsa.etf.rma.transactionmanager.Comparators;

import java.util.Comparator;

import ba.unsa.etf.rma.transactionmanager.Transaction;

public class DateComparatorDescending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction transaction, Transaction t1) {
        return t1.getDate().compareTo(transaction.getDate());
    }
}
