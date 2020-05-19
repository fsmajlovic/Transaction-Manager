package ba.unsa.etf.rma.transactionmanager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.FrameLayout;

import ba.unsa.etf.rma.transactionmanager.TransactionDetail.TransactionDetailFragment;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionListFragment;


public class MainActivity extends FragmentActivity implements TransactionListFragment.OnItemClick,
        TransactionDetailFragment.OnDelete, TransactionDetailFragment.OnRefresh, TransactionListFragment.OnAddItem,
        TransactionListFragment.OnAddTextViewClick {

    private boolean twoPaneMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.transaction_detail);

        if (details != null) {
            twoPaneMode = true;
            TransactionDetailFragment detailFragment = (TransactionDetailFragment)
                    fragmentManager.findFragmentById(R.id.transaction_detail);

            if (detailFragment==null) {

                detailFragment = new TransactionDetailFragment();
                fragmentManager.beginTransaction().
                        replace(R.id.transaction_detail,detailFragment)
                        .commit();
            }
        } else {
            twoPaneMode = false;

        }

        Fragment listFragment =
                fragmentManager.findFragmentByTag("list");

        if (listFragment==null){

            listFragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.transactions_list,listFragment,"list")
                    .commit();
        }else{

            fragmentManager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }


    }


    @Override
    public void onItemClicked(Transaction transaction, boolean eOa) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("transaction", transaction);
        arguments.putInt("transactionId", transaction.getId());
        arguments.putBoolean("editOrAdd", eOa);
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,detailFragment)
                    .addToBackStack(null).commit();
        }

    }

    @Override
    public void onItemDeleted(Transaction transaction) {

        HomeFragment listFragment = new HomeFragment();
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, listFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,listFragment)
                    .addToBackStack(null).commit();
        }
    }


    @Override
    public void refreshFragment() {

        HomeFragment listFragment = new HomeFragment();
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list, listFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,listFragment)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    public void onItemAdd(boolean eOa) {
        Bundle arguments = new Bundle();
        arguments.putBoolean("editOrAdd", eOa);
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
    }

    @Override
    public void onAddClicked(boolean eOa) {
        Bundle arguments = new Bundle();
        arguments.putBoolean("editOrAdd", eOa);
        TransactionDetailFragment detailFragment = new TransactionDetailFragment();
        detailFragment.setArguments(arguments);
        if (twoPaneMode){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transaction_detail, detailFragment)
                    .commit();
        }
        else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.transactions_list,detailFragment)
                    .addToBackStack(null).commit();
        }
    }

}




