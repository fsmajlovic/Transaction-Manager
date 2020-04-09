package ba.unsa.etf.rma.transactionmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import ba.unsa.etf.rma.transactionmanager.Adapters.FilterBySpinnerAdapter;
import ba.unsa.etf.rma.transactionmanager.Adapters.SortBySpinnerAdapter;
import ba.unsa.etf.rma.transactionmanager.Adapters.TransactionsListViewAdapter;
import ba.unsa.etf.rma.transactionmanager.Comparators.DateComparatorAscending;
import ba.unsa.etf.rma.transactionmanager.Comparators.DateComparatorDescending;
import ba.unsa.etf.rma.transactionmanager.Comparators.PriceComparatorAscending;
import ba.unsa.etf.rma.transactionmanager.Comparators.PriceComparatorDescending;
import ba.unsa.etf.rma.transactionmanager.Comparators.TitleComparatorAscending;
import ba.unsa.etf.rma.transactionmanager.Comparators.TitleComparatorDescending;


public class MainActivity extends AppCompatActivity implements TransactionListFragment.OnItemClick,
        TransactionDetailFragment.OnDelete, TransactionDetailFragment.OnRefresh, TransactionListFragment.OnAddItem,
        TransactionListFragment.OnAddTextViewClick {

    private boolean twoPaneMode=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //dohvatanje FragmentManager-a
        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout details = findViewById(R.id.transaction_detail);
//slucaj layouta za ˇsiroke ekrane
        if (details != null) {
            twoPaneMode = true;
            TransactionDetailFragment detailFragment = (TransactionDetailFragment)
                    fragmentManager.findFragmentById(R.id.transaction_detail);
//provjerimo da li je fragment detalji ve´c kreiran
            if (detailFragment==null) {
//kreiramo novi fragment FragmentDetalji ukoliko ve´c nije kreiran
                detailFragment = new TransactionDetailFragment();
                fragmentManager.beginTransaction().
                        replace(R.id.transaction_detail,detailFragment)
                        .commit();
            }
        } else {
            twoPaneMode = false;
        }
//Dodjeljivanje fragmenta MovieListFragment
        Fragment listFragment =
                fragmentManager.findFragmentByTag("list");
//provjerimo da li je ve´c kreiran navedeni fragment
        if (listFragment==null){
//ukoliko nije, kreiramo
            listFragment = new TransactionListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.transactions_list,listFragment,"list")
                    .commit();
        }else{
//sluˇcaj kada mijenjamo orijentaciju uredaja
//iz portrait (uspravna) u landscape (vodoravna)
//a u aktivnosti je bio otvoren fragment MovieDetailFragment
//tada je potrebno skinuti MovieDetailFragment sa steka
//kako ne bi bio dodan na mjesto fragmenta MovieListFragment
            fragmentManager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }


    @Override
    public void onItemClicked(Transaction transaction, boolean eOa) {
        Bundle arguments = new Bundle();
        arguments.putParcelable("transaction", transaction);
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

        TransactionListFragment listFragment = new TransactionListFragment();
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

        TransactionListFragment listFragment = new TransactionListFragment();
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


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == 1) {
//            String returnTitle = data.getStringExtra("returnTitle");
//            String returnDate = data.getStringExtra("returnDate");
//            String returnAmount = data.getStringExtra("returnAmount");
//            String returnType = data.getStringExtra("returnType");
//            String returnDescription = data.getStringExtra("returnDescription");
//            String returnEndDate = data.getStringExtra("returnEndDate");
//            String returnInterval = data.getStringExtra("returnInterval");
//
//            Transaction currentItem = (Transaction) listView.getItemAtPosition(selectedPosition);
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                currentItem.setTitle(returnTitle);
//                currentItem.setDate(sdf.parse(returnDate));
//                currentItem.setAmount(Double.valueOf(returnAmount));
//                currentItem.setItemDescription(returnDescription);
//                if(returnType.equals( "INDIVIDUALPAYMENT"))
//                    currentItem.setType(Transaction.Type.INDIVIDUALPAYMENT);
//                else if(returnType.equals("REGULARPAYMENT"))
//                    currentItem.setType(Transaction.Type.REGULARPAYMENT);
//                else if(returnType.equals("PURCHASE"))
//                    currentItem.setType(Transaction.Type.PURCHASE);
//                else if(returnType.equals("REGULARINCOME"))
//                    currentItem.setType(Transaction.Type.REGULARINCOME);
//                else if(returnType.equals("INDIVIDUALINCOME"))
//                    currentItem.setType(Transaction.Type.INDIVIDUALINCOME);
//
//                if(returnInterval.equals("0")) {
//                    currentItem.setEndDate(null);
//                }
//                else currentItem.setEndDate(sdf.parse(returnEndDate));
//                currentItem.setTransactionInterval(Integer.valueOf(returnInterval));
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Calendar tDate = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
//            String dateString = sdf.format(currentItem.getDate().getTime());
//            if(!dateString.equals(monthTextView.getText().toString()))
//                filteredTransactions.remove(selectedPosition);
//            //Updating budget
//            double newBudget = budget;
//            if (returnType.equals("INDIVIDUALPAYMENT") || returnType.equals("PURCHASE") ||
//                    returnType.equals("INDIVIDUALPAYMENT")) {
//                newBudget = budget - Double.valueOf(returnAmount);
//                globalAmountTextView.setText("Global amount: $" + newBudget);
//            }
//            else if (returnType.equals("INDIVIDUALINCOME") || returnType.equals("REGULARINCOME")) {
//                newBudget = budget + Double.valueOf(returnAmount);
//                globalAmountTextView.setText("Global amount: $" + newBudget);
//            }
//            try {
//                TransactionsPresenter presenter = new TransactionsPresenter();
//                presenter.setBudget(newBudget);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        else if(resultCode == 2){
//            filteredTransactions.remove(selectedTransaction);
//            transactions.remove(selectedTransaction);
//        }
//        else if(resultCode == 3){
//            Transaction addTransaction = new Transaction();
//            String returnTitle = data.getStringExtra("returnTitle");
//            String returnDate = data.getStringExtra("returnDate");
//            String returnAmount = data.getStringExtra("returnAmount");
//            String returnType = data.getStringExtra("returnType");
//            String returnDescription = data.getStringExtra("returnDescription");
//            String returnEndDate = data.getStringExtra("returnEndDate");
//            String returnInterval = data.getStringExtra("returnInterval");
//
//            addTransaction.setTitle(returnTitle);
//            addTransaction.setAmount(Double.valueOf(returnAmount));
//            addTransaction.setItemDescription(returnDescription);
//            if(returnType.equals( "INDIVIDUALPAYMENT"))
//                addTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
//            else if(returnType.equals("REGULARPAYMENT"))
//                addTransaction.setType(Transaction.Type.REGULARPAYMENT);
//            else if(returnType.equals("PURCHASE"))
//                addTransaction.setType(Transaction.Type.PURCHASE);
//            else if(returnType.equals("REGULARINCOME"))
//                addTransaction.setType(Transaction.Type.REGULARINCOME);
//            else if(returnType.equals("INDIVIDUALINCOME"))
//                addTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
//            try {
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                addTransaction.setDate(sdf.parse(returnDate));
//                if(returnInterval.equals("0")) {
//                    addTransaction.setEndDate(null);
//                }
//                else addTransaction.setEndDate(sdf.parse(returnEndDate));
//                addTransaction.setTransactionInterval(Integer.valueOf(returnInterval));
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//
//            Calendar tDate = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
//            String dateString = sdf.format(addTransaction.getDate().getTime());
//            if(dateString.equals(monthTextView.getText().toString()))
//                filteredTransactions.add(addTransaction);
//
//
//            //Updating budget
//            double newBudget = budget;
//            if (returnType.equals("INDIVIDUALPAYMENT") || returnType.equals("PURCHASE") ||
//                    returnType.equals("INDIVIDUALPAYMENT")) {
//                newBudget = budget - Double.valueOf(returnAmount);
//                globalAmountTextView.setText("Global amount: $" + newBudget);
//            }
//            else if (returnType.equals("INDIVIDUALINCOME") || returnType.equals("REGULARINCOME")) {
//                newBudget = budget + Double.valueOf(returnAmount);
//                globalAmountTextView.setText("Global amount: $" + newBudget);
//            }
//            try {
//                TransactionsPresenter presenter = new TransactionsPresenter();
//                presenter.setBudget(newBudget);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        listViewAdapter.notifyDataSetChanged();
//        listView.invalidateViews();
//
//        //Filtering again
//        final String[] textArray = { "All", "INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME",
//                "REGULARINCOME"};
//        String dateString = monthTextView.getText().toString();
//        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
//        Calendar calendarPass = Calendar.getInstance();
//        try {
//            calendarPass.setTime(sdf.parse(dateString));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        int position = filterSpinner.getSelectedItemPosition();
//        if(position >= 0 && position <= textArray.length) {
//            getSelectedCategoryData(textArray[position], userTransactions, calendarPass);
//        }
//
//    }

}




//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedTransaction = filteredTransactions.get(i);
//                SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy");
//                String startDate = dateFormater.format(selectedTransaction.getDate().getTime());
//                String endDate = "";
//                if(selectedTransaction.getEndDate() != null)
//                    endDate = dateFormater.format(selectedTransaction.getEndDate().getTime());
//                else
//                    endDate = "This type has no end date.";
//                Intent intentListItem = new Intent(getActivity(), TransactionDetailActivity.class);
//                intentListItem.putExtra("title", selectedTransaction.getTitle());
//                intentListItem.putExtra("date", startDate);
//                intentListItem.putExtra("amount", String.valueOf(selectedTransaction.getAmount()));
//                intentListItem.putExtra("type", selectedTransaction.getType().toString());
//                intentListItem.putExtra("description", selectedTransaction.getItemDescription());
//                intentListItem.putExtra("endDate", endDate);
//                intentListItem.putExtra("interval", String.valueOf(selectedTransaction.getTransactionInterval()));
//                intentListItem.putExtra("typeArray", textArray);
//                intentListItem.putExtra("edit/add", false);
//                selectedPosition = i;
//                startActivityForResult(intentListItem, 0);
//            }
//        });

//Add transaction regulations
//        addTransactionTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentAddTransaction = new Intent(getActivity(), TransactionDetailActivity.class);
//                intentAddTransaction.putExtra("title", "");
//                intentAddTransaction.putExtra("date", "");
//                intentAddTransaction.putExtra("amount", "");
//                intentAddTransaction.putExtra("type", "");
//                intentAddTransaction.putExtra("description", "");
//                intentAddTransaction.putExtra("endDate", "");
//                intentAddTransaction.putExtra("interval", "");
//                intentAddTransaction.putExtra("typeArray", textArray);
//                intentAddTransaction.putExtra("edit/add", true);
//                startActivityForResult(intentAddTransaction, 0);
//            }
//        });
