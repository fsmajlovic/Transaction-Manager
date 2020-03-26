package ba.unsa.etf.rma.transactionmanager.Comparators;

import java.util.Comparator;

import ba.unsa.etf.rma.transactionmanager.Transaction;

public class TitleComparatorAscending implements Comparator<Transaction> {
    @Override
    public int compare(Transaction transaction, Transaction t1) {
        return transaction.getTitle().compareTo(t1.getTitle());
    }
}
