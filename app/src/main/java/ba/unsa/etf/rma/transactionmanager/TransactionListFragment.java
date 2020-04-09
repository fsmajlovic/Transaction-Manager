package ba.unsa.etf.rma.transactionmanager;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ba.unsa.etf.rma.transactionmanager.Adapters.FilterBySpinnerAdapter;
import ba.unsa.etf.rma.transactionmanager.Adapters.SortBySpinnerAdapter;
import ba.unsa.etf.rma.transactionmanager.Adapters.TransactionsListViewAdapter;

public class TransactionListFragment extends Fragment {
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
    private TextView addTransactionTextView;
    private double budget;
    private double limit;
    private boolean twoPaneMode=false;
    private OnItemClick oic;
    private OnAddItem oai;
    private OnAddTextViewClick oatvc;
    int itemPosition, counter;
    private View checkedItemView;


    private int listItemPosition;

    public interface OnItemClick {
        void onItemClicked(Transaction transaction, boolean eOa);
    }


    public interface OnAddItem {
        void onItemAdd(boolean eOa);
    }

    public interface OnAddTextViewClick{
        void onAddClicked(boolean eOa);
    }



    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {


        View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);
        filterSpinner = (Spinner) fragmentView.findViewById(R.id.filterSpinner);
        sortBySpinner = (Spinner) fragmentView.findViewById(R.id.sortBySpinner);
        monthTextView = (TextView) fragmentView.findViewById(R.id.monthTextView);
        arrowBackImageView = (ImageView) fragmentView.findViewById(R.id.arrowBackwardImageView);
        arrowForwardImageView = (ImageView) fragmentView.findViewById(R.id.arrowForwardImageView);
        listView = (ListView) fragmentView.findViewById(R.id.listView);
        globalAmountTextView = fragmentView.findViewById(R.id.globalAmountTextView);
        limitTextView = fragmentView.findViewById(R.id.limitTextView);
        addTransactionTextView = fragmentView.findViewById(R.id.addTransactionTextView);
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


        counter = 0;
        itemPosition = -1;
        try {
            oai = (OnAddItem)getActivity();
        } catch (ClassCastException e) {

            throw new ClassCastException(getActivity().toString() +
                    "Treba implementirati OnAddItem");
        }

        try {
            oatvc = (OnAddTextViewClick) getActivity();
        } catch (ClassCastException e) {

            throw new ClassCastException(getActivity().toString() +
                    "Treba implementirati OnAddTextViewClicked");
        }

        setupForAddItem();

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
        ArrayAdapter<String> adapterSortBy = new SortBySpinnerAdapter(getActivity(),
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
        filterSpinner = fragmentView.findViewById(R.id.filterSpinner);
        Integer[] imageArray = { R.drawable.ic_all_icon, R.drawable.ic_individual_payment_icon, R.drawable.ic_regular_payment_icon,
                R.drawable.ic_purchase_icon, R.drawable.ic_individual_income_icon, R.drawable.ic_regular_income_icon };
        FilterBySpinnerAdapter adapterFilterBySpinner = new FilterBySpinnerAdapter(getActivity(),
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
        listViewAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, transactions);
        listView.setAdapter(listViewAdapter);

        try {
            oic = (OnItemClick)getActivity();
        } catch (ClassCastException e) {

            throw new ClassCastException(getActivity().toString() +
                    "Treba implementirati OnItemClick");
        }

        listView.setOnItemClickListener(listItemClickListener);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //Add Transaction TextView
        addTransactionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oatvc.onAddClicked(true);
            }
        });


        return fragmentView;
    }



    private AdapterView.OnItemClickListener listItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    TransactionsListViewAdapter transactionListAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, filteredTransactions);
                    Transaction sendTransaction = filteredTransactions.get(position);

                    if(itemPosition != position && counter == 1)
                        return;

                    oic.onItemClicked(sendTransaction, false);

                    if(itemPosition == position && counter == 1){
                        //listView.setItemChecked(position, false);
                        view.setBackgroundColor(Color.parseColor("#f8f8ff"));
                        counter = 0;
                        itemPosition = -1;
                        setupForAddItem();
                    }
                    else {
                        //listView.setItemChecked(position, true);
                        view.setBackgroundColor(Color.parseColor("#B8A228"));
                        counter = 1;
                        itemPosition = position;
                    }
                    transactionListAdapter.notifyDataSetChanged();

                }
            };


    public void setupForAddItem(){
        oai.onItemAdd(true);
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

            System.out.println(t.getTitle() + " " +  dateString +  " " + t.getType().toString());

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


        System.out.println("\n\n\n\nList of filtered transactions:  ");
        for(Transaction t: transactions)
            System.out.println(t.getTitle());

        listViewAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, filteredTransactions);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}