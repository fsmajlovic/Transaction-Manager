//package ba.unsa.etf.rma.transactionmanager;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//
//import java.io.Serializable;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//import ba.unsa.etf.rma.transactionmanager.MainActivity;
//import ba.unsa.etf.rma.transactionmanager.R;
//import ba.unsa.etf.rma.transactionmanager.Transaction;
//
//public class TransactionDetailActivity extends AppCompatActivity{
//    private EditText titleEditText;
//    private EditText dateEditText;
//    private EditText amountEditText;
//    private EditText typeEditText;
//    private EditText descriptionEditText;
//    private EditText endDateEditText;
//    private EditText intervalEditText;
//    private Button saveBtn;
//    private Button deleteBtn;
//    private Transaction transaction;
//    private ArrayList<Transaction> transactions;
//    private boolean titleVal = true, dateVal = true, amountVal = true, typeVal = true, descriptionVal = true,
//            endDateVal = true, intervalVal = true, savetrigger = false;
//    private double monthSpent = 0.0;
//    private double totalSpent = 0.0;
//
//    private ITransactionDetailPresenter presenter;
//
//    public ITransactionDetailPresenter getPresenter() throws ParseException {
//        if (presenter == null) {
//            presenter = new TransactionDetailPresenter(this);
//        }
//        return presenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.transaction_details);
//        titleEditText = (EditText) findViewById(R.id.titleEditText);
//        dateEditText = (EditText) findViewById(R.id.dateEditText);
//        amountEditText = (EditText) findViewById(R.id.amountEditText);
//        typeEditText = (EditText) findViewById(R.id.typeEditText);
//        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
//        endDateEditText = (EditText) findViewById(R.id.endDateEditText);
//        intervalEditText = (EditText) findViewById(R.id.intervalEditText);
//        saveBtn = (Button) findViewById(R.id.saveBtn);
//        deleteBtn = (Button) findViewById(R.id.deleteBtn);
//
//
//        Intent intent = getIntent();
//        final String receivedTitle = intent.getStringExtra("title");
//        final String receivedDate = intent.getStringExtra("date");
//        final String receivedAmount = intent.getStringExtra("amount");
//        final String receivedType = intent.getStringExtra("type");
//        String receivedDescription = intent.getStringExtra("description");
//        String receivedEndDate = intent.getStringExtra("endDate");
//        String receivedInterval = intent.getStringExtra("interval");
//        final String[] typeArray = intent.getStringArrayExtra("typeArray");
//        final boolean editOrAdd = intent.getBooleanExtra("edit/add", false);
//
//
//
//        titleEditText.setText(receivedTitle);
//        dateEditText.setText(receivedDate);
//        amountEditText.setText(receivedAmount);
//        typeEditText.setText(receivedType);
//        descriptionEditText.setText(receivedDescription);
//        endDateEditText.setText(receivedEndDate);
//        intervalEditText.setText(receivedInterval);
//
//        //Disable delete button if user chose to add transaction
//        if(editOrAdd == true)
//            deleteBtn.setEnabled(false);
//
//        //Disable end date and interval if type is not a regular one
//        if (!typeEditText.getText().toString().equals("REGULARINCOME") &&
//                !typeEditText.getText().toString().equals("REGULARPAYMENT")){
//            endDateEditText.setEnabled(false);
//            endDateEditText.setText("This type has no end date.");
//            intervalEditText.setEnabled(false);
//            intervalEditText.setText("0");
//            endDateEditText.setBackgroundColor(Color.parseColor("#169617"));
//            intervalEditText.setBackgroundColor(Color.parseColor("#169617"));
//            endDateVal = true;
//            intervalVal = true;
//        }
//
//        //EditText Listeners
//        titleEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.length() < 4 || charSequence.length() > 15) {
//                    titleEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    titleVal = false;
//                }
//                else if(charSequence.length() > 0) {
//                    titleEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    titleVal = true;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        dateEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                boolean isDateValid = false;
//                DateFormat formatOne = new SimpleDateFormat("dd/MM/yyyy");
//                DateFormat formatTwo = new SimpleDateFormat("dd-MM-yyyy");
//
//                try {
//                    formatOne.parse(charSequence.toString());
//                    isDateValid = true;
//                } catch (ParseException e) {
//                    try {
//                        isDateValid = false;
//                        formatTwo.parse(charSequence.toString());
//                        isDateValid = true;
//                    } catch (ParseException e2) {
//                        //Already should be false
//                    }
//                }
//                if(isDateValid) {
//                    dateEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    dateVal = true;
//                }
//                else {
//                    dateEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    dateVal = false;
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        amountEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.toString().matches("-?\\d+(\\.\\d+)?")) {
//                    amountEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    amountVal = true;
//                }
//                else {
//                    amountEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    amountVal = false;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        typeEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                boolean isValid = false;
//                for(String s: typeArray){
//                    if(typeEditText.getText().toString().equals(s)) {
//                        isValid = true;
//                    }
//                }
//                if(typeEditText.getText().toString().equals(receivedType))
//                    isValid = false;
//                if(isValid) {
//                    typeEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    typeVal = true;
//                }
//                else {
//                    typeEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    typeVal = false;
//                }
//
//                if (!typeEditText.getText().toString().equals("REGULARINCOME") &&
//                        !typeEditText.getText().toString().equals("REGULARPAYMENT")) {
//                    endDateEditText.setEnabled(false);
//                    endDateEditText.setText("This type has no end date.");
//                    intervalEditText.setEnabled(false);
//                    intervalEditText.setText("0");
//                    endDateEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    intervalEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    endDateVal = true;
//                    intervalVal = true;
//                }
//                else{
//                    endDateEditText.setEnabled(true);
//                    endDateEditText.setText("");
//                    intervalEditText.setEnabled(true);
//                    intervalEditText.setText("");
//                    endDateEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    intervalEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    endDateVal = false;
//                    intervalVal = false;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//
//            }
//        });
//        descriptionEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.length() == 0) {
//                    descriptionEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    descriptionVal = false;
//                }
//                else if(charSequence.length() > 0) {
//                    descriptionEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    descriptionVal = true;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        endDateEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                boolean isDateValid = false;
//                DateFormat formatOne = new SimpleDateFormat("dd/MM/yyyy");
//                DateFormat formatTwo = new SimpleDateFormat("dd-MM-yyyy");
//
//                if (typeEditText.getText().toString().equals("REGULARINCOME") ||
//                        typeEditText.getText().toString().equals("REGULARPAYMENT")){
//                    try {
//                        formatOne.parse(charSequence.toString());
//                        isDateValid = true;
//                    } catch (ParseException e) {
//                        try {
//                            isDateValid = false;
//                            formatTwo.parse(charSequence.toString());
//                            isDateValid = true;
//                        } catch (ParseException e2) {
//                            //Already should be false
//                        }
//                    }
//                    if (isDateValid) {
//                        endDateEditText.setBackgroundColor(Color.parseColor("#169617"));
//                        endDateVal = true;
//                    }
//                    else {
//                        endDateEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                        endDateVal = false;
//                    }
//                }
//                else{
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        intervalEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(charSequence.toString().matches("\\d+")) {
//                    intervalEditText.setBackgroundColor(Color.parseColor("#169617"));
//                    intervalVal = true;
//                }
//                else {
//                    intervalEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
//                    intervalVal = false;
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        final Context ctx = this;
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!titleVal || !dateVal || !amountVal || !typeVal || !descriptionVal || !endDateVal || !intervalVal)
//                {
//                    savetrigger = false;
//                    new AlertDialog.Builder(ctx, R.style.AlertDialog)
//                            .setTitle("Changes")
//                            .setMessage("Seems like some of your changes might be wrong.(Red color indicates an incorrect change).")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                }
//                else
//                    savetrigger = true;
//
//                if(savetrigger) {
//
//                    double newAmount = 0;
//                    String text = amountEditText.getText().toString();
//                    if (!text.isEmpty())
//                        try {
//                            newAmount = Double.parseDouble(text);
//                        } catch (Exception e1) {
//
//                            e1.printStackTrace();
//                        }
//                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
//                    Calendar calOne = Calendar.getInstance();
//                    Calendar calTwo = Calendar.getInstance();
//                    try {
//                        calTwo.setTime(sdf2.parse(dateEditText.getText().toString()));
//                        presenter = new TransactionDetailPresenter(ctx);
//                        for(Transaction t: presenter.getInteractor().getTransactions()) {
//                            if(t.getType().toString().equals("PURCHASE") || t.getType().toString().equals("REGULARPAYMENT") ||
//                            t.getType().toString().equals("INDIVIDUALPAYMENT")){
//                                totalSpent = totalSpent + t.getAmount();
//                                calOne.setTime(t.getDate());
//                                int monthOne = calOne.get(Calendar.MONTH);
//                                int monthTwo = calTwo.get(Calendar.MONTH);
//                                if (monthOne == monthTwo)
//                                    monthSpent = monthSpent + t.getAmount();
//                            }
//                        }
//                        System.out.println("Total spent: " + totalSpent);
//                        System.out.println("Month spent: " + monthSpent);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    String whatWentOver = "";
//                    if(newAmount + monthSpent > 5000.0)
//                        whatWentOver = "month";
//                    if(newAmount + totalSpent > 20000.0)
//                        whatWentOver = "global";
//                    if ((newAmount + totalSpent > 20000.0 || newAmount + monthSpent > 5000.0) &&
//                            (typeEditText.getText().toString().equals("INDIVIDUALPAYMENT") ||
//                                    typeEditText.getText().toString().equals("PURCHASE") ||
//                                            typeEditText.getText().toString().equals("REGULARPAYMENT"))) {
//                        new AlertDialog.Builder(ctx, R.style.AlertDialog)
//                                .setTitle("Over limit")
//                                .setMessage("This transaction went over your " + whatWentOver +
//                                        " limit. In this month you've spent: $" + monthSpent + " (limit is $5000) and " +
//                                                "in total you've spent: $" + totalSpent + " (limit is $20000)." +
//                                        "Are you sure you want to continue?")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Intent returnIntent = new Intent();
//                                        returnIntent.putExtra("returnTitle", titleEditText.getText().toString());
//                                        returnIntent.putExtra("returnDate", dateEditText.getText().toString());
//                                        returnIntent.putExtra("returnAmount", amountEditText.getText().toString());
//                                        returnIntent.putExtra("returnType", typeEditText.getText().toString());
//                                        returnIntent.putExtra("returnDescription", descriptionEditText.getText().toString());
//                                        returnIntent.putExtra("returnEndDate", endDateEditText.getText().toString());
//                                        returnIntent.putExtra("returnInterval", intervalEditText.getText().toString());
//
//                                        /////////////////////////////////////////////////////////////////////////////////////
//                                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                                        Transaction newTransaction = new Transaction();
//
//                                        newTransaction.setTitle(titleEditText.getText().toString());
//                                        newTransaction.setAmount(Double.valueOf(amountEditText.getText().toString()));
//                                        newTransaction.setItemDescription(descriptionEditText.getText().toString());
//                                        if(typeEditText.getText().toString().equals( "INDIVIDUALPAYMENT"))
//                                            newTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
//                                        else if(typeEditText.getText().toString().equals("REGULARPAYMENT"))
//                                            newTransaction.setType(Transaction.Type.REGULARPAYMENT);
//                                        else if(typeEditText.getText().toString().equals("PURCHASE"))
//                                            newTransaction.setType(Transaction.Type.PURCHASE);
//                                        else if(typeEditText.getText().toString().equals("REGULARINCOME"))
//                                            newTransaction.setType(Transaction.Type.REGULARINCOME);
//                                        else if(typeEditText.getText().toString().equals("INDIVIDUALINCOME"))
//                                            newTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
//                                        try {
//                                            newTransaction.setDate(sdf.parse(dateEditText.getText().toString()));
//                                            if(intervalEditText.getText().toString().equals("0")) {
//                                                newTransaction.setEndDate(null);
//                                            }
//                                            else newTransaction.setEndDate(sdf.parse(endDateEditText.getText().toString()));
//                                            newTransaction.setTransactionInterval(Integer.valueOf(intervalEditText.getText().toString()));
//
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        if(editOrAdd) {
//                                            setResult(3, returnIntent);
//                                            try {
//                                                presenter = new TransactionDetailPresenter(ctx);
//                                                ((TransactionDetailPresenter) presenter).addTransaction(newTransaction);
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                            finish();
//                                        }
//                                        else {
//                                            setResult(1, returnIntent);
//                                            Intent intent = getIntent();
//                                            Transaction oldTransaction = new Transaction();
//                                            String returnTitle = intent.getStringExtra("title");
//                                            String returnDate = intent.getStringExtra("date");
//                                            String returnAmount = intent.getStringExtra("amount");
//                                            String returnType = intent.getStringExtra("type");
//                                            String returnDescription = intent.getStringExtra("description");
//                                            String returnEndDate = intent.getStringExtra("endDate");
//                                            String returnInterval = intent.getStringExtra("interval");
//
//                                            oldTransaction.setTitle(returnTitle);
//                                            oldTransaction.setAmount(Double.valueOf(returnAmount));
//                                            oldTransaction.setItemDescription(returnDescription);
//                                            if(returnType.equals( "INDIVIDUALPAYMENT"))
//                                                oldTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
//                                            else if(returnType.equals("REGULARPAYMENT"))
//                                                oldTransaction.setType(Transaction.Type.REGULARPAYMENT);
//                                            else if(returnType.equals("PURCHASE"))
//                                                oldTransaction.setType(Transaction.Type.PURCHASE);
//                                            else if(returnType.equals("REGULARINCOME"))
//                                                oldTransaction.setType(Transaction.Type.REGULARINCOME);
//                                            else if(returnType.equals("INDIVIDUALINCOME"))
//                                                oldTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
//                                            try {
//                                                oldTransaction.setDate(sdf.parse(returnDate));
//                                                if(returnInterval.equals("0")) {
//                                                    oldTransaction.setEndDate(null);
//                                                }
//                                                else oldTransaction.setEndDate(sdf.parse(returnEndDate));
//                                                oldTransaction.setTransactionInterval(Integer.valueOf(returnInterval));
//
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                            try {
//                                                presenter = new TransactionDetailPresenter(ctx);
//                                                ((TransactionDetailPresenter) presenter).saveTransaction(oldTransaction, newTransaction);
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                        setResult(1, returnIntent);
//                                        titleEditText.setBackgroundColor(Color.parseColor("#541068"));
//                                        dateEditText.setBackgroundColor(Color.parseColor("#541068"));
//                                        amountEditText.setBackgroundColor(Color.parseColor("#541068"));
//                                        typeEditText.setBackgroundColor(Color.parseColor("#541068"));
//                                        descriptionEditText.setBackgroundColor(Color.parseColor("#541068"));
//                                        endDateEditText.setBackgroundColor(Color.parseColor("#541068"));
//                                        intervalEditText.setBackgroundColor(Color.parseColor("#541068"));
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, null)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .show();
//                    } else {
//                        Intent returnIntent = new Intent();
//                        returnIntent.putExtra("returnTitle", titleEditText.getText().toString());
//                        returnIntent.putExtra("returnDate", dateEditText.getText().toString());
//                        returnIntent.putExtra("returnAmount", amountEditText.getText().toString());
//                        returnIntent.putExtra("returnType", typeEditText.getText().toString());
//                        returnIntent.putExtra("returnDescription", descriptionEditText.getText().toString());
//                        returnIntent.putExtra("returnEndDate", endDateEditText.getText().toString());
//                        returnIntent.putExtra("returnInterval", intervalEditText.getText().toString());
//                        /////////////////////////////////////////////////////////////////////////////////////
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//
//
//                        Transaction newTransaction = new Transaction();
//
//                        newTransaction.setTitle(titleEditText.getText().toString());
//                        newTransaction.setAmount(Double.valueOf(amountEditText.getText().toString()));
//                        newTransaction.setItemDescription(descriptionEditText.getText().toString());
//                        if(typeEditText.getText().toString().equals( "INDIVIDUALPAYMENT"))
//                            newTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
//                        else if(typeEditText.getText().toString().equals("REGULARPAYMENT"))
//                            newTransaction.setType(Transaction.Type.REGULARPAYMENT);
//                        else if(typeEditText.getText().toString().equals("PURCHASE"))
//                            newTransaction.setType(Transaction.Type.PURCHASE);
//                        else if(typeEditText.getText().toString().equals("REGULARINCOME"))
//                            newTransaction.setType(Transaction.Type.REGULARINCOME);
//                        else if(typeEditText.getText().toString().equals("INDIVIDUALINCOME"))
//                            newTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
//                        try {
//                            newTransaction.setDate(sdf.parse(dateEditText.getText().toString()));
//                            if(intervalEditText.getText().toString().equals("0")) {
//                                newTransaction.setEndDate(null);
//                            }
//                            else newTransaction.setEndDate(sdf.parse(endDateEditText.getText().toString()));
//                            newTransaction.setTransactionInterval(Integer.valueOf(intervalEditText.getText().toString()));
//
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        if(editOrAdd) {
//                            setResult(3, returnIntent);
//                            try {
//                                presenter = new TransactionDetailPresenter(ctx);
//                                ((TransactionDetailPresenter) presenter).addTransaction(newTransaction);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            finish();
//                        }
//                        else {
//                            setResult(1, returnIntent);
//                            Intent intent = getIntent();
//                            Transaction oldTransaction = new Transaction();
//                            String returnTitle = intent.getStringExtra("title");
//                            String returnDate = intent.getStringExtra("date");
//                            String returnAmount = intent.getStringExtra("amount");
//                            String returnType = intent.getStringExtra("type");
//                            String returnDescription = intent.getStringExtra("description");
//                            String returnEndDate = intent.getStringExtra("endDate");
//                            String returnInterval = intent.getStringExtra("interval");
//
//                            oldTransaction.setTitle(returnTitle);
//                            oldTransaction.setAmount(Double.valueOf(returnAmount));
//                            oldTransaction.setItemDescription(returnDescription);
//                            if(returnType.equals( "INDIVIDUALPAYMENT"))
//                                oldTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
//                            else if(returnType.equals("REGULARPAYMENT"))
//                                oldTransaction.setType(Transaction.Type.REGULARPAYMENT);
//                            else if(returnType.equals("PURCHASE"))
//                                oldTransaction.setType(Transaction.Type.PURCHASE);
//                            else if(returnType.equals("REGULARINCOME"))
//                                oldTransaction.setType(Transaction.Type.REGULARINCOME);
//                            else if(returnType.equals("INDIVIDUALINCOME"))
//                                oldTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
//                            try {
//                                oldTransaction.setDate(sdf.parse(returnDate));
//                                if(returnInterval.equals("0")) {
//                                    oldTransaction.setEndDate(null);
//                                }
//                                else oldTransaction.setEndDate(sdf.parse(returnEndDate));
//                                oldTransaction.setTransactionInterval(Integer.valueOf(returnInterval));
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                presenter = new TransactionDetailPresenter(ctx);
//                                ((TransactionDetailPresenter) presenter).saveTransaction(oldTransaction, newTransaction);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        setResult(1, returnIntent);
//                        titleEditText.setBackgroundColor(Color.parseColor("#541068"));
//                        dateEditText.setBackgroundColor(Color.parseColor("#541068"));
//                        amountEditText.setBackgroundColor(Color.parseColor("#541068"));
//                        typeEditText.setBackgroundColor(Color.parseColor("#541068"));
//                        descriptionEditText.setBackgroundColor(Color.parseColor("#541068"));
//                        endDateEditText.setBackgroundColor(Color.parseColor("#541068"));
//                        intervalEditText.setBackgroundColor(Color.parseColor("#541068"));
//                    }
//                }
//                totalSpent = 0.0;
//                monthSpent = 0.0;
//            }
//        });
//
//
//        deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new AlertDialog.Builder(ctx, R.style.AlertDialog)
//                        .setTitle("Delete transaction")
//                        .setMessage("Are you sure you want to delete this transaction?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//                                Intent intent = getIntent();
//                                Transaction oldTransaction = new Transaction();
//                                String returnTitle = intent.getStringExtra("title");
//                                String returnDate = intent.getStringExtra("date");
//                                String returnAmount = intent.getStringExtra("amount");
//                                String returnType = intent.getStringExtra("type");
//                                String returnDescription = intent.getStringExtra("description");
//                                String returnEndDate = intent.getStringExtra("endDate");
//                                String returnInterval = intent.getStringExtra("interval");
//
//                                oldTransaction.setTitle(returnTitle);
//                                oldTransaction.setAmount(Double.valueOf(returnAmount));
//                                oldTransaction.setItemDescription(returnDescription);
//                                if(returnType.equals( "INDIVIDUALPAYMENT"))
//                                    oldTransaction.setType(Transaction.Type.INDIVIDUALPAYMENT);
//                                else if(returnType.equals("REGULARPAYMENT"))
//                                    oldTransaction.setType(Transaction.Type.REGULARPAYMENT);
//                                else if(returnType.equals("PURCHASE"))
//                                    oldTransaction.setType(Transaction.Type.PURCHASE);
//                                else if(returnType.equals("REGULARINCOME"))
//                                    oldTransaction.setType(Transaction.Type.REGULARINCOME);
//                                else if(returnType.equals("INDIVIDUALINCOME"))
//                                    oldTransaction.setType(Transaction.Type.INDIVIDUALINCOME);
//                                try {
//                                    oldTransaction.setDate(sdf.parse(returnDate));
//                                    if(returnInterval.equals("0")) {
//                                        oldTransaction.setEndDate(null);
//                                    }
//                                    else oldTransaction.setEndDate(sdf.parse(returnEndDate));
//                                    oldTransaction.setTransactionInterval(Integer.valueOf(returnInterval));
//
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                try {
//                                    presenter = new TransactionDetailPresenter(ctx);
//                                    ((TransactionDetailPresenter) presenter).deleteTransaction(oldTransaction);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                                for(Transaction t: presenter.getInteractor().getTransactions())
//                                    System.out.println(t.getTitle());
//
//                                Intent returnIntent = new Intent();
//                                setResult(2, returnIntent);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
//            }
//        });
//    }
//
//
//}
