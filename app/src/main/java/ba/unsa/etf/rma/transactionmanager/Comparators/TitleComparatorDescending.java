package ba.unsa.etf.rma.transactionmanager.Comparators;

import java.util.Comparator;

import ba.unsa.etf.rma.transactionmanager.Transaction;

public class TitleComparatorDescending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction transaction, Transaction t1) {
        return t1.getTitle().compareTo(transaction.getTitle());
    }
}
