package ba.unsa.etf.rma.transactionmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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


public class MainActivity extends AppCompatActivity {
    private Spinner filterSpinner;
    private TextView monthTextView;
    private ImageView arrowBackImageView;
    private ImageView arrowForwardImageView;
    private Spinner sortBySpinner;
    private ArrayList<String> entries;
    private ListView listView;
    private ArrayList<Transaction> transactions;
    private ArrayList<Transaction> userTransactions;
    private TransactionsListViewAdapter listViewAdapter;
    private ArrayList<Transaction> filteredTransactions;
    private TextView globalAmountTextView;
    private TextView limitTextView;
    private int selectedPosition;
    private Transaction selectedTransaction;
    private TextView addTransactionTextView;
    private double budget;
    private double limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        sortBySpinner = (Spinner) findViewById(R.id.sortBySpinner);
        monthTextView = (TextView) findViewById(R.id.monthTextView);
        arrowBackImageView = (ImageView) findViewById(R.id.arrowBackwardImageView);
        arrowForwardImageView = (ImageView) findViewById(R.id.arrowForwardImageView);
        listView = (ListView) findViewById(R.id.listView);
        globalAmountTextView = findViewById(R.id.globalAmountTextView);
        limitTextView = findViewById(R.id.limitTextView);
        addTransactionTextView = findViewById(R.id.addTransactionTextView);
        final String[] textArray = { "All", "INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME",
                "REGULARINCOME"};
        String[] arrayStringSortBy = {
                "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending",
                "Date - Ascending", "Date - Descending"
        };


        //TransactionsPresenter regulations
        try {
            TransactionsPresenter presenter = new TransactionsPresenter();
            transactions = presenter.getInteractor().getTransactions();
            filteredTransactions = presenter.getInteractor().getTransactions();
            userTransactions = presenter.getInteractor().getTransactions();
            budget = presenter.getInteractor().getBudget();
            limit = presenter.getInteractor().getTotalLimit();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        globalAmountTextView.setText("Global amount: $" + budget);
        limitTextView.setText("Limit: $" + limit);



        //Month regulation
        final Calendar currentMonth = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyy");
        monthTextView.setText(dateFormat.format(currentMonth.getTime()));

        arrowBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonth.add(Calendar.MONTH, -1);
                monthTextView.setText(dateFormat.format(currentMonth.getTime()));
            }
        });

        arrowForwardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonth.add(Calendar.MONTH, +1);
                monthTextView.setText(dateFormat.format(currentMonth.getTime()));
            }
        });

        monthTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String dateString = monthTextView.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
                Calendar calendarPass = Calendar.getInstance();
                try {
                    calendarPass.setTime(sdf.parse(dateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int position = filterSpinner.getSelectedItemPosition();
                if(position >= 0 && position <= textArray.length) {
                    getSelectedCategoryData(textArray[position], userTransactions, calendarPass);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        //Sort by Spinner regulations
        ArrayAdapter<String> adapterSortBy = new SortBySpinnerAdapter(this,
                android.R.layout.simple_spinner_item, arrayStringSortBy);
        adapterSortBy.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sortBySpinner.setAdapter(adapterSortBy);
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String dateString = monthTextView.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
                Calendar calendarPass = Calendar.getInstance();
                try {
                    calendarPass.setTime(sdf.parse(dateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int position = filterSpinner.getSelectedItemPosition();
                if(position >= 0 && position <= textArray.length) {
                    getSelectedCategoryData(textArray[position], userTransactions, calendarPass);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //Filter by Spinner regulations
        filterSpinner = findViewById(R.id.filterSpinner);
        Integer[] imageArray = { R.drawable.ic_all_icon, R.drawable.ic_individual_payment_icon, R.drawable.ic_regular_payment_icon,
                R.drawable.ic_purchase_icon, R.drawable.ic_individual_income_icon, R.drawable.ic_regular_income_icon };
        FilterBySpinnerAdapter adapterFilterBySpinner = new FilterBySpinnerAdapter(this,
                R.layout.custom_layout, textArray, imageArray);
        filterSpinner.setAdapter(adapterFilterBySpinner);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String dateString = monthTextView.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(sdf.parse(dateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(position >= 0 && position <= textArray.length) {
                    getSelectedCategoryData(textArray[position], userTransactions, calendar);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //ListView regulations
        listViewAdapter = new TransactionsListViewAdapter(this, R.layout.list_item, transactions);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTransaction = filteredTransactions.get(i);
                SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy");
                String startDate = dateFormater.format(selectedTransaction.getDate().getTime());
                String endDate = "";
                if(selectedTransaction.getEndDate() != null)
                    endDate = dateFormater.format(selectedTransaction.getEndDate().getTime());
                else
                    endDate = "This type has no end date.";
                Intent intentListItem = new Intent(getApplicationContext(), TransactionDetailActivity.class);
                intentListItem.putExtra("title", selectedTransaction.getTitle());
                intentListItem.putExtra("date", startDate);
                intentListItem.putExtra("amount", String.valueOf(selectedTransaction.getAmount()));
                intentListItem.putExtra("type", selectedTransaction.getType().toString());
                intentListItem.putExtra("description", selectedTransaction.getItemDescription());
                intentListItem.putExtra("endDate", endDate);
                intentListItem.putExtra("interval", String.valueOf(selectedTransaction.getTransactionInterval()));
                intentListItem.putExtra("typeArray", textArray);
                intentListItem.putExtra("edit/add", false);
                selectedPosition = i;
                startActivityForResult(intentListItem, 0);
            }
        });

        //Add transaction regulations
        addTransactionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddTransaction = new Intent(getApplicationContext(), TransactionDetailActivity.class);
                intentAddTransaction.putExtra("title", "");
                intentAddTransaction.putExtra("date", "");
                intentAddTransaction.putExtra("amount", "");
                intentAddTransaction.putExtra("type", "");
                intentAddTransaction.putExtra("description", "");
                intentAddTransaction.putExtra("endDate", "");
                intentAddTransaction.putExtra("interval", "");
                intentAddTransaction.putExtra("typeArray", textArray);
                intentAddTransaction.putExtra("edit/add", true);
                startActivityForResult(intentAddTransaction, 0);
            }
        });
    }

    private void getSelectedCategoryData(String s, ArrayList<Transaction> transactions, Calendar calendar) {
        filteredTransactions = new ArrayList<>();
            for(Transaction t: transactions) {
                Calendar tDate = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
                String dateString = sdf.format(t.getDate().getTime());
                try {
                    tDate.setTime(sdf.parse(dateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (s == "All" && tDate.compareTo(calendar) == 0) {
                    filteredTransactions.add(t);
                }
                else if (tDate.compareTo(calendar) == 0 && t.getType().toString() == s) {
                    filteredTransactions.add(t);
                }
            }

            String selectedSort = sortBySpinner.getSelectedItem().toString();
            if(selectedSort.equals("Price - Ascending"))
                Collections.sort(filteredTransactions, new PriceComparatorAscending());
            else if(selectedSort.equals("Price - Descending"))
                Collections.sort(filteredTransactions, new PriceComparatorDescending());
            else if(selectedSort.equals("Title - Ascending"))
                Collections.sort(filteredTransactions, new TitleComparatorAscending());
            else if(selectedSort.equals("Title - Descending"))
                Collections.sort(filteredTransactions, new TitleComparatorDescending());
            else if(selectedSort.equals("Date - Ascending"))
                Collections.sort(filteredTransactions, new DateComparatorAscending());
            else if(selectedSort.equals("Date - Descending"))
                Collections.sort(filteredTransactions, new DateComparatorDescending());
            listViewAdapter = new TransactionsListViewAdapter(this, R.layout.list_item, filteredTransactions);
            listView.setAdapter(listViewAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1) {
            String returnTitle = data.getStringExtra("returnTitle");
            String returnDate = data.getStringExtra("returnDate");
            String returnAmount = data.getStringExtra("returnAmount");
            String returnType = data.getStringExtra("returnType");
            String returnDescription = data.getStringExtra("returnDescription");
            String returnEndDate = data.getStringExtra("returnEndDate");
            String returnInterval = data.getStringExtra("returnInterval");

            Transaction currentItem = (Transaction) listView.getItemAtPosition(selectedPosition);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                currentItem.setTitle(returnTitle);
                currentItem.setDate(sdf.parse(returnDate));
                currentItem.setAmount(Double.valueOf(returnAmount));
                currentItem.setItemDescription(returnDescription);
                if(returnType.equals( "INDIVIDUALPAYMENT"))
                    currentItem.setType(Transaction.Type.INDIVIDUALPAYMENT);
                else if(returnType.equals("REGULARPAYMENT"))
                    currentItem.setType(Transaction.Type.REGULARPAYMENT);
                else if(returnType.equals("PURCHASE"))
                    currentItem.setType(Transaction.Type.PURCHASE);
                else if(returnType.equals("REGULARINCOME"))
                    currentItem.setType(Transaction.Type.REGULARINCOME);
                else if(returnType.equals("INDIVIDUALINCOME"))
                    currentItem.setType(Transaction.Type.INDIVIDUALINCOME);

                if(returnInterval.equals("0")) {
                    currentItem.setEndDate(null);
                }
                else currentItem.setEndDate(sdf.parse(returnEndDate));
                currentItem.setTransactionInterval(Integer.valueOf(returnInterval));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar tDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
            String dateString = sdf.format(currentItem.getDate().getTime());
            if(!dateString.equals(monthTextView.getText().toString()))
                filteredTransactions.remove(selectedPosition);
            //Updating budget
            double newBudget = budget;
            if (returnType.equals("INDIVIDUALPAYMENT") || returnType.equals("PURCHASE") ||
                    returnType.equals("INDIVIDUALPAYMENT")) {
                newBudget = budget - Double.valueOf(returnAmount);
                globalAmountTextView.setText("Global amount: $" + newBudget);
            }
            else if (returnType.equals("INDIVIDUALINCOME") || returnType.equals("REGULARINCOME")) {
                newBudget = budget + Double.valueOf(returnAmount);
                globalAmountTextView.setText("Global amount: $" + newBudget);
            }
            try {
                TransactionsPresenter presenter = new TransactionsPresenter();
                presenter.setBudget(newBudget);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(resultCode == 2){
            filteredTransactions.remove(selectedTransaction);
            transactions.remove(selectedTransaction);
        }
        else if(resultCode == 3){
            Transaction addTransaction = new Transaction();
            String returnTitle = data.getStringExtra("returnTitle");
            String returnDate = data.getStringExtra("returnDate");
            String returnAmount = data.getStringExtra("returnAmount");
            String returnType = data.getStringExtra("returnType");
            String returnDescription = data.getStringExtra("returnDescription");
            String returnEndDate = data.getStringExtra("returnEndDate");
            String returnInterval = data.getStringExtra("returnInterval");

            addTransaction.setTitle(returnTitle);
            addTransaction.setAmount(Double.valueOf(returnAmount));
            addTransaction.setItemDescription(returnDescription);
            if(returnType.equals( "INDIVIDUALPAYMENT"))
                addTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
            else if(returnType.equals("REGULARPAYMENT"))
                addTransaction.setType(Transaction.Type.REGULARPAYMENT);
            else if(returnType.equals("PURCHASE"))
                addTransaction.setType(Transaction.Type.PURCHASE);
            else if(returnType.equals("REGULARINCOME"))
                addTransaction.setType(Transaction.Type.REGULARINCOME);
            else if(returnType.equals("INDIVIDUALINCOME"))
                addTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                addTransaction.setDate(sdf.parse(returnDate));
                if(returnInterval.equals("0")) {
                    addTransaction.setEndDate(null);
                }
                else addTransaction.setEndDate(sdf.parse(returnEndDate));
                addTransaction.setTransactionInterval(Integer.valueOf(returnInterval));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            Calendar tDate = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
            String dateString = sdf.format(addTransaction.getDate().getTime());
            if(dateString.equals(monthTextView.getText().toString()))
                filteredTransactions.add(addTransaction);


            //Updating budget
            double newBudget = budget;
            if (returnType.equals("INDIVIDUALPAYMENT") || returnType.equals("PURCHASE") ||
                    returnType.equals("INDIVIDUALPAYMENT")) {
                newBudget = budget - Double.valueOf(returnAmount);
                globalAmountTextView.setText("Global amount: $" + newBudget);
            }
            else if (returnType.equals("INDIVIDUALINCOME") || returnType.equals("REGULARINCOME")) {
                newBudget = budget + Double.valueOf(returnAmount);
                globalAmountTextView.setText("Global amount: $" + newBudget);
            }
            try {
                TransactionsPresenter presenter = new TransactionsPresenter();
                presenter.setBudget(newBudget);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        listViewAdapter.notifyDataSetChanged();
        listView.invalidateViews();

        //Filtering again
        final String[] textArray = { "All", "INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME",
                "REGULARINCOME"};
        String dateString = monthTextView.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
        Calendar calendarPass = Calendar.getInstance();
        try {
            calendarPass.setTime(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int position = filterSpinner.getSelectedItemPosition();
        if(position >= 0 && position <= textArray.length) {
            getSelectedCategoryData(textArray[position], userTransactions, calendarPass);
        }

    }

}
