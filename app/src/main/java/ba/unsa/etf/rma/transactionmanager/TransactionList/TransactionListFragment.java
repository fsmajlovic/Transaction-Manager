package ba.unsa.etf.rma.transactionmanager.TransactionList;

import androidx.fragment.app.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
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
import ba.unsa.etf.rma.transactionmanager.Util.NetworkChangeReceiver;
import ba.unsa.etf.rma.transactionmanager.Util.TransactionDBOpenHelper;
import ba.unsa.etf.rma.transactionmanager.Util.TransactionListCursorAdapter;


public class TransactionListFragment extends Fragment implements ITransactionListView, Observer {
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
    private Cursor myCursor;

    //Month
    private Calendar currentMonth;

    //Selected item variables
    int itemPosition, counter;

    //TransactionsList
    private TransactionsListViewAdapter transactionsListViewAdapter;
    private TransactionListCursorAdapter transactionListCursorAdapter;

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
        void onItemAdd(boolean eOa,double totalLimit,
                       double monthLimit, double spentOnly, ArrayList<Transaction> transactionsAll);
    }

    public interface OnAddTextViewClick{
        void onAddClicked(boolean eOa,double totalLimit,
                          double monthLimit, double spentOnly, ArrayList<Transaction> transactionsAll);
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
                    setupForAddItem();
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
                    setupForAddItem();
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
            setupForAddItem();

            listView.setOnItemClickListener(listItemClickListener);
            transactionsListViewAdapter = new TransactionsListViewAdapter(getActivity(), R.layout. list_item, new ArrayList<Transaction>());
            transactionListCursorAdapter = new TransactionListCursorAdapter(getActivity(), R.layout.list_item, null, false);
            if(isNetworkAvailable(getActivity())) {
                listView.setAdapter(transactionsListViewAdapter);
            }
            else{
                setCursor(getPresenter().getTransactionCursor(getActivity(), currentMonth));
                setAccountInfoFromDatabase();
                listView.setAdapter(transactionListCursorAdapter);
            }

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
                oatvc.onAddClicked(true, totalLimit, monthLimit, spentOnly, transactionsAll);
            }
            });

            return fragmentView;
        }

    @Override
    public void setTransactions(ArrayList<Transaction> transactions, ArrayList<Transaction> AllTransactions) {
        ArrayList<Transaction> additionalTransactions = new ArrayList<Transaction>();
        additionalTransactions = getAdditionalTransactions(AllTransactions);
        transactions.addAll(additionalTransactions);
        if(!transactions.isEmpty()) {
            additionalSort(filterSpinner.getSelectedItemPosition(), transactions, additionalTransactions);
        }
    }

    private ArrayList<Transaction> getAdditionalTransactions(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> additionalTransactions = new ArrayList<Transaction>();
        additionalTransactions.clear();
        int monthSelectedNumber =  currentMonth.get(Calendar.MONTH) + 1;
        for(Transaction t: transactions) {
            if (t.getType().equals(Transaction.Type.REGULARPAYMENT) ||
                    (t.getType().equals(Transaction.Type.REGULARINCOME))) {
                Date startDate = t.getDate();
                Date firstDate = t.getDate();
                Date endDate = t.getEndDate();
                if(endDate != null) {
                    while (startDate.compareTo(endDate) < 0) {
                        int interval = t.getTransactionInterval();
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        int monthCurrentNumber = c.get(Calendar.MONTH) + 1;
                        if (monthCurrentNumber == monthSelectedNumber) {
                            if (startDate.compareTo(firstDate) != 0) {
                                additionalTransactions.add(t);
                            }
                        }
                        c.add(Calendar.DATE, interval);
                        startDate = c.getTime();
                    }
                }
            }
        }

        return additionalTransactions;
    }

    private void additionalSort(int typeId, ArrayList<Transaction> transactions, ArrayList<Transaction> additionalTransactions) {
        filteredTransactions = new ArrayList<>();
        int monthSelectedNumber =  currentMonth.get(Calendar.MONTH) + 1;

        for(Transaction t: transactions) {
            Calendar tDate = Calendar.getInstance();
            tDate.setTime(t.getDate());
            int TransactionMonthNumber =  tDate.get(Calendar.MONTH) + 1;

            if (TransactionMonthNumber == monthSelectedNumber && typeId == 0) {
                filteredTransactions.add(t);
            }
            else if (TransactionMonthNumber == monthSelectedNumber && t.getTransactionTypeID() == typeId) {
                filteredTransactions.add(t);
            }
        }

        filteredTransactions.addAll(additionalTransactions);

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


        transactionsListViewAdapter.setTransactions(filteredTransactions);
        listView.setAdapter(transactionsListViewAdapter);
        transactionsListViewAdapter.notifyDataSetChanged();
        loading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void notifyTransactionListDataSetChanged() {
        transactionsListViewAdapter.notifyDataSetChanged();
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

        getPresenter().getTransactionTypes(getActivity(), query);

    }


        private AdapterView.OnItemClickListener listItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    TransactionsListViewAdapter transactionListAdapter = new TransactionsListViewAdapter(getActivity(), R.layout.list_item, filteredTransactions);
                    Transaction sendTransaction = (Transaction) listView.getItemAtPosition(position);
                    sendTransaction.setAction(0);
                    sendTransaction.setInternalD(-1);

                    if(itemPosition != position && counter == 1)
                        return;

                    oic.onItemClicked(sendTransaction, false, totalLimit, monthLimit, spentOnly, transactionsAll);

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

    private AdapterView.OnItemClickListener listCursorItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
            if(cursor != null) {
                Transaction sendTransaction = getThisTransaction(cursor);
                oic.onItemClicked(sendTransaction, false, totalLimit, monthLimit, spentOnly, transactionsAll);
            }
            if(itemPosition != position && counter == 1)
                return;

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
            transactionListCursorAdapter.notifyDataSetChanged();
        }
    };

    public void setupForAddItem(){
        oai.onItemAdd(true, totalLimit, monthLimit, spentOnly, transactionsAll);
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkChangeReceiver.getObservable().addObserver(this);
        loading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        filterTransactions();
        if(isNetworkAvailable(getActivity())) {
            onlineMode();
        }else{
            offlineMode();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        NetworkChangeReceiver.getObservable().deleteObserver(this);
        if(isNetworkAvailable(getActivity())) {
            onlineMode();
        }else{
            offlineMode();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if(isNetworkAvailable(getActivity())) {
            onlineMode();
        }else{
            offlineMode();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void onlineMode(){
        filterTransactions();
        listView.setOnItemClickListener(listItemClickListener);
        listView.setAdapter(transactionsListViewAdapter);
    }

    public void offlineMode(){
        setAccountInfoFromDatabase();
        loading.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void setCursor(Cursor cursor){
        myCursor = cursor;
        transactionListCursorAdapter.changeCursor(cursor);
        listView.setAdapter(transactionListCursorAdapter);
        listView.setOnItemClickListener(listCursorItemClickListener);
    }

    public Transaction getThisTransaction(Cursor cursor){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        int id = 0;
        try{
            int transactionIdPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ID);
            id = cursor.getInt(transactionIdPos);
        }catch (IllegalArgumentException e){
            System.out.println("My exception, ID does not exist.");
        }
        int titlePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);
        int amountPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT);
        int descriptionPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ITEM_DESCRIPTION);
        int datePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_DATE);
        int typePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE);
        int intervalPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERVAL);
        int endDatePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_END_DATE);
        int internalIdPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_INTERNAL_ID);
        int action = 0;
        try {
            int actionPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_ACTION);
            action = cursor.getInt(actionPos);
        }
        catch (IllegalArgumentException e){

        }
        String title = cursor.getString(titlePos);
        double amount = cursor.getDouble(amountPos);
        String description = cursor.getString(descriptionPos);
        String dateString = cursor.getString(datePos);
        String type = cursor.getString(typePos);
        int interval = cursor.getInt(intervalPos);
        String endDateString = cursor.getString(endDatePos);
        int internalId = cursor.getInt(internalIdPos);



        Transaction newTransaction = new Transaction();
        newTransaction.setId(id);
        newTransaction.setTitle(title);
        newTransaction.setAmount(amount);
        newTransaction.setItemDescription(description);
        newTransaction.setAction(action);
        newTransaction.setInternalD(internalId);

        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        Date d1 = null;
        try{
            d1 = sdf3.parse(dateString);
        }catch (Exception e){ e.printStackTrace(); }


        newTransaction.setDate(d1);


        if (type.equals("INDIVIDUALPAYMENT"))
            newTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
        else if (type.equals("REGULARPAYMENT"))
            newTransaction.setType(Transaction.Type.REGULARPAYMENT);
        else if (type.equals("PURCHASE"))
            newTransaction.setType(Transaction.Type.PURCHASE);
        else if (type.equals("REGULARINCOME"))
            newTransaction.setType(Transaction.Type.REGULARINCOME);
        else if (type.equals("INDIVIDUALINCOME"))
            newTransaction.setType(Transaction.Type.INDIVIDUALINCOME);


        Date d2 = null;
        try{
            d2 = sdf3.parse(endDateString);
        }catch (Exception e){ e.printStackTrace(); }
        newTransaction.setEndDate(d2);

        newTransaction.setTransactionInterval(interval);


        return  newTransaction;
    }

    void setAccountInfoFromDatabase(){
        Account acc = getPresenter().getAccountInfoFromDatabase();
        if(acc != null) {
            globalAmountTextView.setText("Global amount: $" + String.valueOf(acc.getBudget()));
            limitTextView.setText("Limit: $" + String.valueOf(acc.getTotalLimit()));
        }
    }

}
