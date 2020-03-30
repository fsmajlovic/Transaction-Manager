package ba.unsa.etf.rma.transactionmanager.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.MainActivity;
import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;

public class TransactionDetailActivity extends AppCompatActivity{
    private EditText titleEditText;
    private EditText dateEditText;
    private EditText amountEditText;
    private EditText typeEditText;
    private EditText descriptionEditText;
    private EditText endDateEditText;
    private EditText intervalEditText;
    private Button saveBtn;
    private Button deleteBtn;
    private boolean titleVal = true, dateVal = true, amountVal = true, typeVal = true, descriptionVal = true,
            endDateVal = true, intervalVal = true, savetrigger = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        endDateEditText = (EditText) findViewById(R.id.endDateEditText);
        intervalEditText = (EditText) findViewById(R.id.intervalEditText);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);


        Intent intent = getIntent();
        final double monthSpent = (double) intent.getDoubleExtra("monthSpent", 0.0);
        final double totalSpent = intent.getDoubleExtra("totalSpent", 0.0);
        final String receivedTitle = intent.getStringExtra("title");
        final String receivedDate = intent.getStringExtra("date");
        final String receivedAmount = intent.getStringExtra("amount");
        final String receivedType = intent.getStringExtra("type");
        String receivedDescription = intent.getStringExtra("description");
        String receivedEndDate = intent.getStringExtra("endDate");
        String receivedInterval = intent.getStringExtra("interval");
        final String[] typeArray = intent.getStringArrayExtra("typeArray");
        final boolean editOrAdd = intent.getBooleanExtra("edit/add", false);

        titleEditText.setText(receivedTitle);
        dateEditText.setText(receivedDate);
        amountEditText.setText(receivedAmount);
        typeEditText.setText(receivedType);
        descriptionEditText.setText(receivedDescription);
        endDateEditText.setText(receivedEndDate);
        intervalEditText.setText(receivedInterval);

        //Disable delete button if user chose to add transaction
        if(editOrAdd == true)
            deleteBtn.setEnabled(false);

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
//                if(titleEditText.getText().toString().equals(receivedTitle))
//                    titleEditText.setBackgroundColor(Color.parseColor("#541068"));
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
                //if(dateEditText.getText().toString().equals(receivedDate))
                  //  dateEditText.setBackgroundColor(Color.parseColor("#541068"));
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
                if(typeEditText.getText().toString().equals(receivedType))
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

        final Context ctx = this;
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!titleVal || !dateVal || !amountVal || !typeVal || !descriptionVal || !endDateVal || !intervalVal)
                {
                    savetrigger = false;
                    new AlertDialog.Builder(ctx, R.style.AlertDialog)
                            .setTitle("Changes")
                            .setMessage("Seems like some of your changes might be wrong or" +
                                    " only partially edited (Green color indicates a correct change).")
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

                    double doublevalue = 0;
                    String text = amountEditText.getText().toString();
                    if (!text.isEmpty())
                        try {
                            doublevalue = Double.parseDouble(text);
                        } catch (Exception e1) {

                            e1.printStackTrace();
                        }
                    if (5000 < doublevalue) {
                        new AlertDialog.Builder(ctx, R.style.AlertDialog)
                                .setTitle("Over limit")
                                .setMessage("This transaction went over your month or global limit." +
                                        "Are you sure you want to continue?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("returnTitle", titleEditText.getText().toString());
                                        returnIntent.putExtra("returnDate", dateEditText.getText().toString());
                                        returnIntent.putExtra("returnAmount", amountEditText.getText().toString());
                                        returnIntent.putExtra("returnType", typeEditText.getText().toString());
                                        returnIntent.putExtra("returnDescription", descriptionEditText.getText().toString());
                                        returnIntent.putExtra("returnEndDate", endDateEditText.getText().toString());
                                        returnIntent.putExtra("returnInterval", intervalEditText.getText().toString());
                                        if(editOrAdd) {
                                            setResult(3, returnIntent);
                                            finish();
                                        }
                                        else {
                                            setResult(1, returnIntent);
                                        }
                                        setResult(1, returnIntent);
                                        titleEditText.setBackgroundColor(Color.parseColor("#541068"));
                                        dateEditText.setBackgroundColor(Color.parseColor("#541068"));
                                        amountEditText.setBackgroundColor(Color.parseColor("#541068"));
                                        typeEditText.setBackgroundColor(Color.parseColor("#541068"));
                                        descriptionEditText.setBackgroundColor(Color.parseColor("#541068"));
                                        endDateEditText.setBackgroundColor(Color.parseColor("#541068"));
                                        intervalEditText.setBackgroundColor(Color.parseColor("#541068"));
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("returnTitle", titleEditText.getText().toString());
                        returnIntent.putExtra("returnDate", dateEditText.getText().toString());
                        returnIntent.putExtra("returnAmount", amountEditText.getText().toString());
                        returnIntent.putExtra("returnType", typeEditText.getText().toString());
                        returnIntent.putExtra("returnDescription", descriptionEditText.getText().toString());
                        returnIntent.putExtra("returnEndDate", endDateEditText.getText().toString());
                        returnIntent.putExtra("returnInterval", intervalEditText.getText().toString());
                        setResult(1, returnIntent);
                        titleEditText.setBackgroundColor(Color.parseColor("#541068"));
                        dateEditText.setBackgroundColor(Color.parseColor("#541068"));
                        amountEditText.setBackgroundColor(Color.parseColor("#541068"));
                        typeEditText.setBackgroundColor(Color.parseColor("#541068"));
                        descriptionEditText.setBackgroundColor(Color.parseColor("#541068"));
                        endDateEditText.setBackgroundColor(Color.parseColor("#541068"));
                        intervalEditText.setBackgroundColor(Color.parseColor("#541068"));
                        if(editOrAdd) {
                            setResult(3, returnIntent);
                            finish();
                        }
                        else {
                            setResult(1, returnIntent);
                        }
                    }
                }
            }
        });


        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx, R.style.AlertDialog)
                        .setTitle("Delete transaction")
                        .setMessage("Are you sure you want to delete this transaction?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent returnIntent = new Intent();
                                setResult(2, returnIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

}
