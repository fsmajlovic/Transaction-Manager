package ba.unsa.etf.rma.transactionmanager.Graphs;

import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Transaction;

public interface IGraphsView {
    void setTransactions(ArrayList<Transaction> transactions);
    void validateGraphs();
}
