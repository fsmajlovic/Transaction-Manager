package ba.unsa.etf.rma.transactionmanager.Activities;

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

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private ImageView typeImageView;
    private Button saveBtn;
    private Button deleteBtn;

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
        typeImageView = (ImageView) findViewById(R.id.typeDetailsImageView);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);


        Intent intent = getIntent();
        final String receivedTitle = intent.getStringExtra("title");
        final String receivedDate = intent.getStringExtra("date");
        final String receivedAmount = intent.getStringExtra("amount");
        final String receivedType = intent.getStringExtra("type");
        String receivedDescription = intent.getStringExtra("description");
        String receivedEndDate = intent.getStringExtra("endDate");
        String receivedInterval = intent.getStringExtra("interval");
        final String[] typeArray = intent.getStringArrayExtra("typeArray");


        titleEditText.setText(receivedTitle);
        dateEditText.setText(receivedDate);
        amountEditText.setText(receivedAmount);
        typeEditText.setText(receivedType);
        descriptionEditText.setText(receivedDescription);
        endDateEditText.setText(receivedEndDate);
        intervalEditText.setText(receivedInterval);




        //EditText Listeners
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0)
                    titleEditText.setBackgroundColor(Color.parseColor("#541068"));
                else if(charSequence.length() > 0)
                    titleEditText.setBackgroundColor(Color.parseColor("#008577"));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(titleEditText.getText().toString().equals(receivedTitle))
                    titleEditText.setBackgroundColor(Color.parseColor("#541068"));
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
                if(isDateValid)
                    dateEditText.setBackgroundColor(Color.parseColor("#008577"));
                else
                    dateEditText.setBackgroundColor(Color.parseColor("#541068"));

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(dateEditText.getText().toString().equals(receivedDate))
                    dateEditText.setBackgroundColor(Color.parseColor("#541068"));
            }
        });
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().matches("-?\\d+(\\.\\d+)?")) {
                    amountEditText.setBackgroundColor(Color.parseColor("#008577"));
                }
                else
                    amountEditText.setBackgroundColor(Color.parseColor("#541068"));
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
                    if(typeEditText.getText().toString().equals(s))
                        isValid = true;
                }
                if(typeEditText.getText().toString().equals(receivedType))
                    isValid = false;
                if(isValid)
                    typeEditText.setBackgroundColor(Color.parseColor("#008577"));
                else
                    typeEditText.setBackgroundColor(Color.parseColor("#541068"));

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
                if(charSequence.length() == 0)
                    descriptionEditText.setBackgroundColor(Color.parseColor("#541068"));
                else if(charSequence.length() > 0)
                    descriptionEditText.setBackgroundColor(Color.parseColor("#008577"));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(descriptionEditText.getText().toString().equals(receivedTitle))
                    descriptionEditText.setBackgroundColor(Color.parseColor("#541068"));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("returnTitle", titleEditText.getText().toString());
                returnIntent.putExtra("returnDate", dateEditText.getText().toString());
                returnIntent.putExtra("returnAmount", amountEditText.getText().toString());
                returnIntent.putExtra("returnType", typeEditText.getText().toString());
                returnIntent.putExtra("returnDescription", descriptionEditText.getText().toString());
                returnIntent.putExtra("returnEndDate", endDateEditText.getText().toString());
                returnIntent.putExtra("returnInterval", intervalEditText.getText().toString());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

}
