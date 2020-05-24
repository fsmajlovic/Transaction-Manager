package ba.unsa.etf.rma.transactionmanager.TransactionList;

import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Queue;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Adapters.FilterBySpinnerAdapter;
import ba.unsa.etf.rma.transactionmanager.Adapters.SortBySpinnerAdapter;
import ba.unsa.etf.rma.transactionmanager.Adapters.TransactionsListViewAdapter;
import ba.unsa.etf.rma.transactionmanager.Comparators.DateComparatorAscending;
import ba.unsa.etf.rma.transactionmanager.Comparators.DateComparatorDescending;
import ba.unsa.etf.rma.transactionmanager.Comparators.PriceComparatorAscending;
import ba.unsa.etf.rma.transactionmanager.Comparators.PriceComparatorDescending;
import ba.unsa.etf.rma.transactionmanager.Comparators.TitleComparatorAscending;
import ba.unsa.etf.rma.transactionmanager.Comparators.TitleComparatorDescending;

import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;


public class TransactionListFragment extends Fragment implements ITransactionListView{
    private Spinner filterSpinner;
    private TextView monthTextView;
    private ImageView arrowBackImageView;
    private ImageView arrowForwardImageView;
    private Spinner sortBySpinner;
    private ListView listView;
    private ArrayList<Transaction> transactions;
    private ArrayList<Transaction> userTransactions;
    private TransactionsListViewAdapter listViewAdapter;
    private ArrayList<Transaction> filteredTransactions;
    private TextView globalAmountTextView;
    private TextView limitTextView;
    private TextView addTransactionTextView;
    private OnItemClick oic;
    private OnAddItem oai;
    private OnAddTextViewClick oatvc;
    private TextView loading;
    private ProgressBar progressBar;
    private double totalLimit, monthLimit, spentOnly;

    //Month
    private Calendar currentMonth;

    //Selected item variables
    int itemPosition, counter;

    //TransactionsList
    private TransactionsListViewAdapter transactionsListViewAdapter;

    //FilterBy
    private FilterBySpinnerAdapter filterBySpinnerAdapter;
    private Integer[] imageArray = {R.drawable.ic_all_icon, R.drawable.ic_regular_payment_icon, R.drawable.ic_regular_income_icon,
            R.drawable.ic_purchase_icon,R.drawable.ic_individual_income_icon, R.drawable.ic_individual_payment_icon};

    //Presenter
    private ITransactionListPresenter TransactionListPresenter;
    private ArrayList<Transaction> transactionsAll;

    public ITransactionListPresenter getPresenter() {
        if (TransactionListPresenter == null) {
            TransactionListPresenter = new TransactionsPresenter(this, getActivity());
        }
        return TransactionListPresenter;
    }

    //OnItemInterfaces
        public interface OnItemClick {
        void onItemClicked(Transaction transaction, boolean eOa, double totalLimit,
                           double monthLimit, double spentOnly, ArrayList<Transaction> transactionsAll);
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

            final View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);
            filterSpinner = (Spinner) fragmentView.findViewById(R.id.filterSpinner);
            sortBySpinner = (Spinner) fragmentView.findViewById(R.id.sortBySpinner);
            monthTextView = (TextView) fragmentView.findViewById(R.id.monthTextView);
            arrowBackImageView = (ImageView) fragmentView.findViewById(R.id.arrowBackwardImageView);
            arrowForwardImageView = (ImageView) fragmentView.findViewById(R.id.arrowForwardImageView);
            listView = (ListView) fragmentView.findViewById(R.id.listView);
            globalAmountTextView = fragmentView.findViewById(R.id.globalAmountTextView);
            limitTextView = fragmentView.findViewById(R.id.limitTextView);
            addTransactionTextView = fragmentView.findViewById(R.id.addTransactionTextView);
            arrowBackImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_backward));
            arrowForwardImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward));
            loading = (TextView) fragmentView.findViewById(R.id.loadingTextView);
            progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressBar);
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            loading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);


            //Month regulations
            currentMonth = Calendar.getInstance();
            final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyy");
            monthTextView.setText(dateFormat.format(currentMonth.getTime()));

            arrowBackImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentMonth.add(Calendar.MONTH, -1);
                    monthTextView.setText(dateFormat.format(currentMonth.getTime()));
                    counter = 0;
                    itemPosition = -1;
                    loading.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    filterTransactions();
                    //setupForAddItem();
                }
            });
            arrowForwardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentMonth.add(Calendar.MONTH, +1);
                    monthTextView.setText(dateFormat.format(currentMonth.getTime()));
                    counter = 0;
                    itemPosition = -1;
                    loading.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    filterTransactions();
                    //setupForAddItem();
                }
            });

            //Sort By
            String[] arrayStringSortBy = {
                    "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending",
                    "Date - Ascending", "Date - Descending"
            };
            ArrayAdapter<String> adapterSortBy = new SortBySpinnerAdapter(getActivity(),
                    android.R.layout.simple_spinner_item, arrayStringSortBy);
            adapterSortBy.setDropDownViewResource(android.R.layout.simple_spinner_item);
            sortBySpinner.setAdapter(adapterSortBy);
            sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    loading.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    filterTransactions();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            //Filter Call
            filterTransactions();

            //Filter Spinner regulations
            final String[] textArray = { "All", "Regular payment", "Regular income", "Purchase", "Individual income", "Individual payment"};
            FilterBySpinnerAdapter filterBySpinnerAdapter = new FilterBySpinnerAdapter(getActivity(),
                R.layout.custom_layout, textArray, imageArray);
            filterSpinner.setAdapter(filterBySpinnerAdapter);
            filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    loading.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    filterTransactions();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            //Item selected regulations
            counter = 0;
            itemPosition = -1;
            try {
                oai = (OnAddItem)getActivity();
            } catch (ClassCastException e) {

                throw new ClassCastException(getActivity().toString() +
                        "Treba implementirati OnAddItem");
            }
            //ListView regulations
            try {
                oic = (OnItemClick)getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString() +
                        "Treba implementirati OnItemClick");
            }
            try {
                oatvc = (OnAddTextViewClick) getActivity();
            } catch (ClassCastException e) {

                throw new ClassCastException(getActivity().toString() +
                        "Treba implementirati OnAddTextViewClicked");
            }

            listView.setOnItemClickListener(listItemClickListener);
            transactionsListViewAdapter = new TransactionsListViewAdapter(getActivity(), R.layout. list_item, new ArrayList<Transaction>());
            listView.setAdapter(transactionsListViewAdapter);
            listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ScrollView mScrollView = fragmentView.findViewById(R.id.scrollViewList);
                mScrollView.requestDisallowInterceptTouchEvent(true);
                int action = motionEvent.getActionMasked();
                switch (action){
                    case MotionEvent.ACTION_UP:
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

            //Add item
            addTransactionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oatvc.onAddClicked(true);
            }
            });


            return fragmentView;
        }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> additionalTransactions = new ArrayList<Transaction>();
        additionalTransactions = getAdditionalTransactions(transactions);
        transactions.addAll(additionalTransactions);
        transactionsListViewAdapter.setTransactions(transactions);
    }

    private ArrayList<Transaction> getAdditionalTransactions(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> additionalTransactions = new ArrayList<Transaction>();
        additionalTransactions.clear();
        int repeatCounter = 0;
        int monthSelectedNumber =  currentMonth.get(Calendar.MONTH) + 1;
        for(Transaction t: transactions) {
            if (t.getType().equals(Transaction.Type.REGULARPAYMENT) ||
                    (t.getType().equals(Transaction.Type.REGULARINCOME))) {
                Date startDate = t.getDate();
                Date endDate = t.getEndDate();
                while (startDate.compareTo(endDate) < 0) {
                    int interval = t.getTransactionInterval();
                    Calendar c = Calendar.getInstance();
                    c.setTime(startDate);
                    int monthCurrentNumber =  c.get(Calendar.MONTH) + 1;
                    if(monthCurrentNumber == monthSelectedNumber){
                        if(repeatCounter > 0) {
                            additionalTransactions.add(t);
                        }
                        repeatCounter++;
                    }
                    c.add(Calendar.DATE, interval);
                    startDate = c.getTime();
                }
            }
            return additionalTransactions;
        }

        return additionalTransactions;
    }


    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionsListViewAdapter.notifyDataSetChanged();
        loading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setGlobalTotal(double globalAmount, double totalLimit) {
        globalAmountTextView.setText("Global amount: $" + String.valueOf(globalAmount));
        limitTextView.setText("Limit: $" + String.valueOf(totalLimit));
    }

    @Override
    public void passAccAndSpent(double totalLimit, double monthLimit, double spentOnly, ArrayList<Transaction> transactionsAll) {
        this.totalLimit = totalLimit;
        this.monthLimit = monthLimit;
        this.spentOnly = spentOnly;
        this.transactionsAll = transactionsAll;
    }


    public void filterTransactions(){
        String query = "";

        if(sortBySpinner.getSelectedItemPosition() == 0)
            query = "sort=amount.asc";
        else if(sortBySpinner.getSelectedItemPosition() == 1)
            query = "sort=amount.desc";
        else if(sortBySpinner.getSelectedItemPosition() == 2)
            query = "sort=title.asc";
        else if(sortBySpinner.getSelectedItemPosition() == 3)
            query = "sort=title.desc";
        else if(sortBySpinner.getSelectedItemPosition() == 4)
            query = "sort=date.asc";
        else if(sortBySpinner.getSelectedItemPosition() == 5)
            query = "sort=date.desc";


        if(filterSpinner.getSelectedItemPosition() != 0)
            query += ("&typeId=" + String.valueOf(filterSpinner.getSelectedItemPosition()));

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyy");
        monthTextView.setText(dateFormat.format(currentMonth.getTime()));
        int month = -1;
        int year = -1;
        query += "&month=";
        month =  currentMonth.get(Calendar.MONTH) + 1;
        year =   currentMonth.get(Calendar.YEAR);
        if(month < 10 ) {
            query += "0";
        }
        query += (String.valueOf(month));
        query += "&year=";
        query += (String.valueOf(year));

        getPresenter().getTransactionTypes(query);

    }


        private AdapterView.OnItemClickListener listItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    TransactionsListViewAdapter transactionListAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, filteredTransactions);
                    Transaction sendTransaction = (Transaction) listView.getItemAtPosition(position);

                    if(itemPosition != position && counter == 1)
                        return;

                    oic.onItemClicked(sendTransaction, false, totalLimit, monthLimit, spentOnly, transactionsAll);

                    if(itemPosition == position && counter == 1){
                        //listView.setItemChecked(position, false);
                        view.setBackgroundColor(Color.parseColor("#f8f8ff"));
                        counter = 0;
                        itemPosition = -1;
                        //setupForAddItem();
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

    @Override
    public void onResume() {
        super.onResume();
        loading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        filterTransactions();
    }

}


//public class TransactionListFragment extends Fragment {
//    private Spinner filterSpinner;
//    private TextView monthTextView;
//    private ImageView arrowBackImageView;
//    private ImageView arrowForwardImageView;
//    private Spinner sortBySpinner;
//    private ListView listView;
//    private ArrayList<Transaction> transactions;
//    private ArrayList<Transaction> userTransactions;
//    private TransactionsListViewAdapter listViewAdapter;
//    private ArrayList<Transaction> filteredTransactions;
//    private TextView globalAmountTextView;
//    private TextView limitTextView;
//    private TextView addTransactionTextView;
//    private double budget;
//    private double limit;
//    private OnItemClick oic;
//    private OnAddItem oai;
//    private OnAddTextViewClick oatvc;
//    int itemPosition, counter;
//
//    public interface OnItemClick {
//        void onItemClicked(Transaction transaction, boolean eOa);
//    }
//
//
//    public interface OnAddItem {
//        void onItemAdd(boolean eOa);
//    }
//
//    public interface OnAddTextViewClick{
//        void onAddClicked(boolean eOa);
//    }
//
//
//
//    @Override
//    public View onCreateView(
//            LayoutInflater inflater,
//            ViewGroup container,
//            Bundle savedInstanceState) {
//
//
//
//        final View fragmentView = inflater.inflate(R.layout.fragment_list, container, false);
//        filterSpinner = (Spinner) fragmentView.findViewById(R.id.filterSpinner);
//        sortBySpinner = (Spinner) fragmentView.findViewById(R.id.sortBySpinner);
//        monthTextView = (TextView) fragmentView.findViewById(R.id.monthTextView);
//        arrowBackImageView = (ImageView) fragmentView.findViewById(R.id.arrowBackwardImageView);
//        arrowForwardImageView = (ImageView) fragmentView.findViewById(R.id.arrowForwardImageView);
//        listView = (ListView) fragmentView.findViewById(R.id.listView);
//        globalAmountTextView = fragmentView.findViewById(R.id.globalAmountTextView);
//        limitTextView = fragmentView.findViewById(R.id.limitTextView);
//        addTransactionTextView = fragmentView.findViewById(R.id.addTransactionTextView);
//        final String[] textArray = { "All", "INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME",
//                "REGULARINCOME"};
//        String[] arrayStringSortBy = {
//                "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending",
//                "Date - Ascending", "Date - Descending"
//        };
//        arrowBackImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_backward));
//        arrowForwardImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_forward));
//
//
//        //TransactionsPresenter regulations
//        try {
//            TransactionsPresenter presenter = new TransactionsPresenter();
//            transactions = presenter.getInteractor().getTransactions();
//            filteredTransactions = presenter.getInteractor().getTransactions();
//            userTransactions = presenter.getInteractor().getTransactions();
//            budget = presenter.calculateGlobalAmount();
//            limit = presenter.getInteractor().getTotalLimit();
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        counter = 0;
//        itemPosition = -1;
//        try {
//            oai = (OnAddItem)getActivity();
//        } catch (ClassCastException e) {
//
//            throw new ClassCastException(getActivity().toString() +
//                    "Treba implementirati OnAddItem");
//        }
//
//        try {
//            oatvc = (OnAddTextViewClick) getActivity();
//        } catch (ClassCastException e) {
//
//            throw new ClassCastException(getActivity().toString() +
//                    "Treba implementirati OnAddTextViewClicked");
//        }
//
//        //Setting up landscape details fragment for just updating
//        setupForAddItem();
//
//        globalAmountTextView.setText("Global amount: $" + budget);
//        limitTextView.setText("Limit: $" + limit);
//
//
//
//
//        //Month regulation
//        final Calendar currentMonth = Calendar.getInstance();
//        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyy");
//        monthTextView.setText(dateFormat.format(currentMonth.getTime()));
//
//        arrowBackImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                currentMonth.add(Calendar.MONTH, -1);
//                monthTextView.setText(dateFormat.format(currentMonth.getTime()));
//                counter = 0;
//                itemPosition = -1;
//                setupForAddItem();
//            }
//        });
//
//        arrowForwardImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                currentMonth.add(Calendar.MONTH, +1);
//                monthTextView.setText(dateFormat.format(currentMonth.getTime()));
//                counter = 0;
//                itemPosition = -1;
//                setupForAddItem();
//            }
//        });
//
//        monthTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String dateString = monthTextView.getText().toString();
//                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
//                Calendar calendarPass = Calendar.getInstance();
//                try {
//                    calendarPass.setTime(sdf.parse(dateString));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                int position = filterSpinner.getSelectedItemPosition();
//                if(position >= 0 && position <= textArray.length) {
//                    getSelectedCategoryData(textArray[position], userTransactions, calendarPass);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//
//
//        //Sort by Spinner regulations
//        ArrayAdapter<String> adapterSortBy = new SortBySpinnerAdapter(getActivity(),
//                android.R.layout.simple_spinner_item, arrayStringSortBy);
//        adapterSortBy.setDropDownViewResource(android.R.layout.simple_spinner_item);
//        sortBySpinner.setAdapter(adapterSortBy);
//        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String dateString = monthTextView.getText().toString();
//                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
//                Calendar calendarPass = Calendar.getInstance();
//                try {
//                    calendarPass.setTime(sdf.parse(dateString));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                int position = filterSpinner.getSelectedItemPosition();
//                if(position >= 0 && position <= textArray.length) {
//                    getSelectedCategoryData(textArray[position], userTransactions, calendarPass);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//
//        //Filter by Spinner regulations
//        filterSpinner = fragmentView.findViewById(R.id.filterSpinner);
//        Integer[] imageArray = { R.drawable.ic_all_icon, R.drawable.ic_individual_payment_icon, R.drawable.ic_regular_payment_icon,
//                R.drawable.ic_purchase_icon, R.drawable.ic_individual_income_icon, R.drawable.ic_regular_income_icon };
//        FilterBySpinnerAdapter adapterFilterBySpinner = new FilterBySpinnerAdapter(getActivity(),
//                R.layout.custom_layout, textArray, imageArray);
//        filterSpinner.setAdapter(adapterFilterBySpinner);
//        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String dateString = monthTextView.getText().toString();
//                SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
//                Calendar calendar = Calendar.getInstance();
//                try {
//                    calendar.setTime(sdf.parse(dateString));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if(position >= 0 && position <= textArray.length) {
//                    getSelectedCategoryData(textArray[position], userTransactions, calendar);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        //ListView regulations
//        listViewAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, transactions);
//        listView.setAdapter(listViewAdapter);
//
//        try {
//            oic = (OnItemClick)getActivity();
//        } catch (ClassCastException e) {
//
//            throw new ClassCastException(getActivity().toString() +
//                    "Treba implementirati OnItemClick");
//        }
//
//        listView.setOnItemClickListener(listItemClickListener);
//        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        //Setting up so that scrolling list in scrollView is scrollable
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                ScrollView mScrollView = fragmentView.findViewById(R.id.scrollViewList);
//                mScrollView.requestDisallowInterceptTouchEvent(true);
//                int action = motionEvent.getActionMasked();
//                switch (action){
//                    case MotionEvent.ACTION_UP:
//                        mScrollView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });
//
//        //Add Transaction TextView
//        addTransactionTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                oatvc.onAddClicked(true);
//            }
//        });
//
//
//
//        return fragmentView;
//    }
//
//
//
//
//    private AdapterView.OnItemClickListener listItemClickListener =
//            new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    TransactionsListViewAdapter transactionListAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, filteredTransactions);
//                    Transaction sendTransaction = filteredTransactions.get(position);
//
//                    if(itemPosition != position && counter == 1)
//                        return;
//
//                    oic.onItemClicked(sendTransaction, false);
//
//                    if(itemPosition == position && counter == 1){
//                        //listView.setItemChecked(position, false);
//                        view.setBackgroundColor(Color.parseColor("#f8f8ff"));
//                        counter = 0;
//                        itemPosition = -1;
//                        setupForAddItem();
//                    }
//                    else {
//                        //listView.setItemChecked(position, true);
//                        view.setBackgroundColor(Color.parseColor("#B8A228"));
//                        counter = 1;
//                        itemPosition = position;
//                    }
//                    transactionListAdapter.notifyDataSetChanged();
//
//                }
//            };
//
//
//    public void setupForAddItem(){
//        oai.onItemAdd(true);
//    }
//
//    private void getSelectedCategoryData(String s, ArrayList<Transaction> transactions, Calendar calendar) {
//        filteredTransactions = new ArrayList<>();
//        for(Transaction t: transactions) {
//            Calendar tDate = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyy");
//            String dateString = sdf.format(t.getDate().getTime());
//            try {
//                tDate.setTime(sdf.parse(dateString));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            if (s == "All" && tDate.compareTo(calendar) == 0) {
//                filteredTransactions.add(t);
//            }
//            else if (tDate.compareTo(calendar) == 0 && t.getType().toString() == s) {
//                filteredTransactions.add(t);
//            }
//        }
//
//        String selectedSort = sortBySpinner.getSelectedItem().toString();
//        if(selectedSort.equals("Price - Ascending"))
//            Collections.sort(filteredTransactions, new PriceComparatorAscending());
//        else if(selectedSort.equals("Price - Descending"))
//            Collections.sort(filteredTransactions, new PriceComparatorDescending());
//        else if(selectedSort.equals("Title - Ascending"))
//            Collections.sort(filteredTransactions, new TitleComparatorAscending());
//        else if(selectedSort.equals("Title - Descending"))
//            Collections.sort(filteredTransactions, new TitleComparatorDescending());
//        else if(selectedSort.equals("Date - Ascending"))
//            Collections.sort(filteredTransactions, new DateComparatorAscending());
//        else if(selectedSort.equals("Date - Descending"))
//            Collections.sort(filteredTransactions, new DateComparatorDescending());
//
//        listViewAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, filteredTransactions);
//        listView.setAdapter(listViewAdapter);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        TransactionsPresenter presenter = null;
//        try {
//            presenter = new TransactionsPresenter();
//            globalAmountTextView.setText("Global amount: $" + presenter.calculateGlobalAmount());
//            limitTextView.setText("Limit: $" + presenter.getInteractor().getTotalLimit());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//}