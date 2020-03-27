package ba.unsa.etf.rma.transactionmanager.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

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


        Intent intent = getIntent();
        final String receivedTitle = intent.getStringExtra("title");
        String receivedDate = intent.getStringExtra("date");
        String receivedAmount = intent.getStringExtra("amount");
        String receivedType = intent.getStringExtra("type");
        String receivedDescription = intent.getStringExtra("description");
        String receivedEndDate = intent.getStringExtra("endDate");
        String receivedInterval = intent.getStringExtra("interval");


        titleEditText.setText(receivedTitle);
        dateEditText.setText(receivedDate);
        amountEditText.setText(receivedAmount + "$");
        typeEditText.setText(receivedType);
        descriptionEditText.setText(receivedDescription);
        endDateEditText.setText(receivedEndDate);
        typeImageView.setImageResource(R.drawable.ic_individual_payment_icon);
        intervalEditText.setText(receivedInterval);


        //EditTextListeners
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




    }
}
