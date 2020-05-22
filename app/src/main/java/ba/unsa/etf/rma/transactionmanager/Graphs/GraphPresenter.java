package ba.unsa.etf.rma.transactionmanager.Graphs;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.ITransactionListView;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsInteractor;

public class GraphPresenter implements IGraphsPresenter, GraphsInteractor.OnGetTransactionsDone {
    private TransactionsInteractor interactor;

    private IGraphsView view;
    private Context context;
    private ArrayList<Transaction> transactions;


    public GraphPresenter(IGraphsView view, Context context){
        this.view = view;
        this.context = context;
    }


    @Override
    public void getTransactions(String query) {
        new GraphsInteractor((GraphsInteractor.OnGetTransactionsDone) this).execute(query);
    }

    @Override
    public void onDoneTransactions(ArrayList<Transaction> transactions) {
        view.setTransactions(transactions);
        view.validateGraphs();
    }
}
