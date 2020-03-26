package ba.unsa.etf.rma.transactionmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Movie;
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

import ba.unsa.etf.rma.transactionmanager.Activities.TransactionDetailActivity;
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
    private userModel user;
    private TransactionsListViewAdapter listViewAdapter;
    private ArrayList<Transaction> filteredTransactions;
    private TextView globalAmountTextView;
    private TextView limitTextView;

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
        final String[] textArray = { "All", "INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME",
                "REGULARINCOME"};
        String[] arrayStringSortBy = {
                "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending",
                "Date - Ascending", "Date - Descending"
        };


        //TransactionsPresenter regulations
        try {
            TransactionsPresenter presenter = new TransactionsPresenter();
            transactions = presenter.getInteractor().getUser().getTransactions();
            filteredTransactions = presenter.getInteractor().getUser().getTransactions();
            user = presenter.getInteractor().getUser();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        globalAmountTextView.setText("Global amount:" + user.getAccount().getBudget() + "$");
        limitTextView.setText("Limit:" + user.getAccount().getTotalLimit() + "$");



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
                    getSelectedCategoryData(textArray[position], user.getTransactions(), calendarPass);
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
                    getSelectedCategoryData(textArray[position], user.getTransactions(), calendarPass);
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
                    getSelectedCategoryData(textArray[position], user.getTransactions(), calendar);
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

                Intent intent1 = new Intent(getApplicationContext(), TransactionDetailActivity.class);


                startActivity(intent1);
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
}
