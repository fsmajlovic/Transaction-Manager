package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionDetail.ITransactionDetailPresenter;
import ba.unsa.etf.rma.transactionmanager.TransactionDetail.TransactionDetailPresenter;
import ba.unsa.etf.rma.transactionmanager.TransactionList.ITransactionListPresenter;
import ba.unsa.etf.rma.transactionmanager.TransactionList.TransactionsPresenter;

public class TransactionDetailFragment extends Fragment implements ITransactionDetailView{
    private EditText titleEditText;
    private EditText dateEditText;
    private EditText amountEditText;
    private EditText typeEditText;
    private EditText descriptionEditText;
    private EditText endDateEditText;
    private EditText intervalEditText;
    private Button saveBtn;
    private Button deleteBtn;
    private Transaction transactionParc;
    private boolean titleVal = true, dateVal = true, amountVal = true, typeVal = true, descriptionVal = true,
            endDateVal = true, intervalVal = true, savetrigger = false, isOk = false;
    private double monthSpent = 0.0, totalSpent = 0.0;
    private final String[] typeArray = { "All", "INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME",
            "REGULARINCOME"};
    private boolean eOa;
    private OnDelete oid;
    private OnRefresh or;

    private ITransactionDetailPresenter presenter;

    public interface OnDelete {
        void onItemDeleted(Transaction transaction);
    }

    public interface OnRefresh {
        void refreshFragment();
    }

    private ITransactionDetailPresenter TransactionDetailPresenter;
    public ITransactionDetailPresenter getPresenter() {
        if (TransactionDetailPresenter == null) {
            TransactionDetailPresenter = new TransactionDetailPresenter(this, getActivity());
        }
        return TransactionDetailPresenter;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            final Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_detail, container, false);

            titleEditText = (EditText) view.findViewById(R.id.titleEditText);
            dateEditText = (EditText) view.findViewById(R.id.dateEditText);
            amountEditText = (EditText) view.findViewById(R.id.amountEditText);
            typeEditText = (EditText) view.findViewById(R.id.typeEditText);
            descriptionEditText = (EditText) view.findViewById(R.id.descriptionEditText);
            endDateEditText = (EditText) view.findViewById(R.id.endDateEditText);
            intervalEditText = (EditText) view.findViewById(R.id.intervalEditText);
            saveBtn = (Button) view.findViewById(R.id.saveBtn);
            deleteBtn = (Button) view.findViewById(R.id.deleteBtn);

            if (getArguments() != null && getArguments().containsKey("editOrAdd")) {
                eOa = getArguments().getBoolean("editOrAdd");
            }

            if (getArguments() != null && getArguments().containsKey("transaction")) {
                transactionParc = (getArguments().getParcelable("transaction"));
                titleEditText.setText(transactionParc.getTitle());
                dateEditText.setText(transactionParc.getDate().toString());
                amountEditText.setText(String.valueOf(transactionParc.getAmount()));
                typeEditText.setText(transactionParc.getType().toString());
                descriptionEditText.setText(transactionParc.getItemDescription());
                if(transactionParc.getEndDate() != null)
                    endDateEditText.setText(transactionParc.getEndDate().toString());
                intervalEditText.setText(String.valueOf(transactionParc.getTransactionInterval()));


                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

                final String receivedTitle = transactionParc.getTitle();
                final String receivedDate = formatter.format(transactionParc.getDate());
                final String receivedAmount = String.valueOf(transactionParc.getAmount());
                final String receivedType = transactionParc.getType().toString();
                String receivedDescription = transactionParc.getItemDescription();
                String receivedEndDate = "";
                if(transactionParc.getEndDate() != null) {
                    receivedEndDate = formatter.format(transactionParc.getEndDate());
                }
                String receivedInterval = String.valueOf(transactionParc.getTransactionInterval());

                titleEditText.setText(receivedTitle);
                dateEditText.setText(receivedDate);
                amountEditText.setText(receivedAmount);
                typeEditText.setText(receivedType);
                descriptionEditText.setText(receivedDescription);
                endDateEditText.setText(receivedEndDate);
                intervalEditText.setText(receivedInterval);

            }


        try {
            oid = (OnDelete) getActivity();
        } catch (ClassCastException e) {

            throw new ClassCastException(getActivity().toString() +
                    "Treba implementirati OnItemDelete");
        }
        try {
            or = (OnRefresh) getActivity();
        } catch (ClassCastException e) {

            throw new ClassCastException(getActivity().toString() +
                    "Treba implementirati OnItemEdited");
        }

        //Disable delete button if user chose to add transaction
        if(eOa == true) {
            deleteBtn.setEnabled(false);
            dateVal = false;
        }
        //Disable end date and interval if type is not a regular one
        if (!typeEditText.getText().toString().equals("REGULARINCOME") &&
                !typeEditText.getText().toString().equals("REGULARPAYMENT")){
            endDateEditText.setEnabled(false);
            endDateEditText.setText("This type has no end date.");
            intervalEditText.setEnabled(false);
            intervalEditText.setText("0");
            endDateEditText.setBackgroundColor(Color.parseColor("#169617"));
            intervalEditText.setBackgroundColor(Color.parseColor("#169617"));
            endDateVal = true;
            intervalVal = true;
        }

        //EditText Listeners
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 4 || charSequence.length() > 15) {
                    titleEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    titleVal = false;
                }
                else if(charSequence.length() > 0) {
                    titleEditText.setBackgroundColor(Color.parseColor("#169617"));
                    titleVal = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isDateValid = false;
                DateFormat formatOne = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat formatTwo = new SimpleDateFormat("dd-MM-yyyy");

                try {
                    formatOne.parse(charSequence.toString());
                    isDateValid = true;
                } catch (ParseException e) {
                    try {
                        isDateValid = false;
                        formatTwo.parse(charSequence.toString());
                        isDateValid = true;
                    } catch (ParseException e2) {
                        //Already should be false
                    }
                }
                if(isDateValid) {
                    dateEditText.setBackgroundColor(Color.parseColor("#169617"));
                    dateVal = true;
                }
                else {
                    dateEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    dateVal = false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().matches("-?\\d+(\\.\\d+)?")) {
                    amountEditText.setBackgroundColor(Color.parseColor("#169617"));
                    amountVal = true;
                }
                else {
                    amountEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    amountVal = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        typeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isValid = false;
                for(String s: typeArray){
                    if(typeEditText.getText().toString().equals(s)) {
                        isValid = true;
                    }
                }
                if(transactionParc != null && typeEditText.getText().toString().equals(transactionParc.getType().toString()))
                    isValid = false;
                if(isValid) {
                    typeEditText.setBackgroundColor(Color.parseColor("#169617"));
                    typeVal = true;
                }
                else {
                    typeEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    typeVal = false;
                }

                if (!typeEditText.getText().toString().equals("REGULARINCOME") &&
                        !typeEditText.getText().toString().equals("REGULARPAYMENT")) {
                    endDateEditText.setEnabled(false);
                    endDateEditText.setText("This type has no end date.");
                    intervalEditText.setEnabled(false);
                    intervalEditText.setText("0");
                    endDateEditText.setBackgroundColor(Color.parseColor("#169617"));
                    intervalEditText.setBackgroundColor(Color.parseColor("#169617"));
                    endDateVal = true;
                    intervalVal = true;
                }
                else{
                    endDateEditText.setEnabled(true);
                    endDateEditText.setText("");
                    intervalEditText.setEnabled(true);
                    intervalEditText.setText("");
                    endDateEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    intervalEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    endDateVal = false;
                    intervalVal = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0) {
                    descriptionEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    descriptionVal = false;
                }
                else if(charSequence.length() > 0) {
                    descriptionEditText.setBackgroundColor(Color.parseColor("#169617"));
                    descriptionVal = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        endDateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                boolean isDateValid = false;
                DateFormat formatOne = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat formatTwo = new SimpleDateFormat("dd-MM-yyyy");

                if (typeEditText.getText().toString().equals("REGULARINCOME") ||
                        typeEditText.getText().toString().equals("REGULARPAYMENT")){
                    try {
                        formatOne.parse(charSequence.toString());
                        isDateValid = true;
                    } catch (ParseException e) {
                        try {
                            isDateValid = false;
                            formatTwo.parse(charSequence.toString());
                            isDateValid = true;
                        } catch (ParseException e2) {
                            //Already should be false
                        }
                    }
                    if (isDateValid) {
                        endDateEditText.setBackgroundColor(Color.parseColor("#169617"));
                        endDateVal = true;
                    }
                    else {
                        endDateEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                        endDateVal = false;
                    }
                }
                else{

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        intervalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().matches("\\d+")) {
                    intervalEditText.setBackgroundColor(Color.parseColor("#169617"));
                    intervalVal = true;
                }
                else {
                    intervalEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    intervalVal = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                        .setTitle("Delete transaction")
                        .setMessage("Are you sure you want to delete this transaction?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                    int transID = 0;
                                    if (getArguments() != null && getArguments().containsKey("transactionId")) {
                                        transID = getArguments().getInt("transactionId");
                                    }
                                    getPresenter().addDeleteEdit("", transID, null, 3);
                                oid.onItemDeleted(transactionParc);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    saveBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!titleVal || !dateVal || !amountVal || !typeVal || !descriptionVal || !endDateVal || !intervalVal)
                {
                    savetrigger = false;
                    new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                            .setTitle("Changes")
                            .setMessage("Seems like some of your changes might be wrong.(Red color indicates an incorrect change).")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else
                    savetrigger = true;

                if(savetrigger) {
                    double newAmount = 0;
                    String text = amountEditText.getText().toString();
                    if (!text.isEmpty())
                        try {
                            newAmount = Double.parseDouble(text);
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar calOne = Calendar.getInstance();
                    Calendar calTwo = Calendar.getInstance();
                    try {
                        double totalLimit = 0, monthLimit = 0, spentOnly = 0;
                        ArrayList<Transaction> transactionsAll = new ArrayList<Transaction>();
                        if (getArguments() != null && getArguments().containsKey("totalLimit")
                                && getArguments().containsKey("monthLimit")
                                && getArguments().containsKey("spentOnly")
                                && getArguments().containsKey("transactionsAll")) {
                            totalLimit = getArguments().getDouble("totalLimit");
                            monthLimit = getArguments().getDouble("monthLimit");
                            spentOnly = getArguments().getDouble("spentOnly");
                            transactionsAll = getArguments().getParcelableArrayList("transactionsAll");
                            System.out.println("DETAILS RESPONSE: totalLimit " + totalLimit + " monthlimit " + monthLimit + " spentonly " + spentOnly + " " + transactionsAll.get(0).getTitle());
                        }


                        calTwo.setTime(sdf2.parse(dateEditText.getText().toString()));
                        monthSpent = 0.0;
                        totalSpent = spentOnly;
                        for (Transaction t : transactionsAll) {
                            if (t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("REGULARPAYMENT") ||
                                    t.getType().toString().equals("INDIVIDUALPAYMENT")) {
                                calOne.setTime(t.getDate());
                                int monthOne = calOne.get(Calendar.MONTH);
                                int monthTwo = calTwo.get(Calendar.MONTH);
                                if (monthOne == monthTwo)
                                    monthSpent = monthSpent + t.getAmount();
                            }
                        }

                        String whatWentOver = "";
                        double value = monthLimit;
                        if (newAmount + monthSpent > monthLimit)
                            whatWentOver = "month";
                        if (newAmount + totalSpent > totalLimit)
                            whatWentOver = "global";
                        if ((newAmount + totalSpent > totalLimit
                                || newAmount + monthSpent > monthLimit) &&
                                (typeEditText.getText().toString().equals("INDIVIDUALPAYMENT") ||
                                        typeEditText.getText().toString().equals("PURCHASE") ||
                                        typeEditText.getText().toString().equals("REGULARPAYMENT"))) {
                            new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                                    .setTitle("Over limit")
                                    .setMessage("With this transaction you went over your " + whatWentOver +
                                            " limit. In this month you've spent: $" + monthSpent + " (limit is $" +
                                            monthLimit + ") and " +
                                            "in total you've spent: $" + totalSpent + " (limit is $" +
                                            totalLimit + ")." +
                                            "Are you sure you want to continue?")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            saveMethod();
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                        else {
                            saveMethod();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
         });

        return view;
        }


    public void saveMethod(){
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Transaction newTransaction = new Transaction();
            newTransaction.setTitle(titleEditText.getText().toString());
            newTransaction.setAmount(Double.valueOf(amountEditText.getText().toString()));
            newTransaction.setItemDescription(descriptionEditText.getText().toString());
            newTransaction.setDate(sdf.parse(dateEditText.getText().toString()));
            if (typeEditText.getText().toString().equals("INDIVIDUALPAYMENT"))
                newTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
            else if (typeEditText.getText().toString().equals("REGULARPAYMENT"))
                newTransaction.setType(Transaction.Type.REGULARPAYMENT);
            else if (typeEditText.getText().toString().equals("PURCHASE"))
                newTransaction.setType(Transaction.Type.PURCHASE);
            else if (typeEditText.getText().toString().equals("REGULARINCOME"))
                newTransaction.setType(Transaction.Type.REGULARINCOME);
            else if (typeEditText.getText().toString().equals("INDIVIDUALINCOME"))
                newTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
            try {
                if (intervalEditText.getText().toString().equals("0")) {
                    newTransaction.setEndDate(null);
                } else
                    newTransaction.setEndDate(sdf.parse(endDateEditText.getText().toString()));
                newTransaction.setTransactionInterval(Integer.valueOf(intervalEditText.getText().toString()));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (eOa) {
                //((TransactionDetailPresenter) presenter).addTransaction(newTransaction);
                getPresenter().addDeleteEdit("", 0, newTransaction, 1);
            } else {
                int transID = 0;
                if (getArguments() != null && getArguments().containsKey("transactionId")) {
                    transID = getArguments().getInt("transactionId");
                }
                getPresenter().addDeleteEdit("", transID, newTransaction, 2);
            }
            titleEditText.setBackgroundColor(Color.parseColor("#541068"));
            dateEditText.setBackgroundColor(Color.parseColor("#541068"));
            amountEditText.setBackgroundColor(Color.parseColor("#541068"));
            typeEditText.setBackgroundColor(Color.parseColor("#541068"));
            descriptionEditText.setBackgroundColor(Color.parseColor("#541068"));
            endDateEditText.setBackgroundColor(Color.parseColor("#541068"));
            intervalEditText.setBackgroundColor(Color.parseColor("#541068"));
            or.refreshFragment();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
