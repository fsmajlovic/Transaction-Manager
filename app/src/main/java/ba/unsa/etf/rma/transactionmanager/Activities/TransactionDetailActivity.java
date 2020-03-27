package ba.unsa.etf.rma.transactionmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;

public class TransactionDetailActivity extends AppCompatActivity{
    private EditText titleTextView;
    private EditText dateTextView;
    private EditText amountTextView;
    private EditText typeTextView;
    private EditText descriptionTextView;
    private EditText endDateTextView;
    private EditText intervalTextView;
    private ImageView typeImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);
        titleTextView = (EditText) findViewById(R.id.titleEditText);
        dateTextView = (EditText) findViewById(R.id.dateEditText);
        amountTextView = (EditText) findViewById(R.id.amountEditText);
        typeTextView = (EditText) findViewById(R.id.typeEditText);
        descriptionTextView = (EditText) findViewById(R.id.descriptionEditText);
        endDateTextView = (EditText) findViewById(R.id.endDateEditText);
        intervalTextView = (EditText) findViewById(R.id.intervalEditText);
        typeImageView = (ImageView) findViewById(R.id.transactionTypeImageView);

        Intent intent = getIntent();
        String receivedTitle = intent.getStringExtra("title");
        String receivedAmount = intent.getStringExtra("amount");
        String receivedType = intent.getStringExtra("type");
        String receivedDescription = intent.getStringExtra("description");
        String receivedEndDate = intent.getStringExtra("endDate");
        String receivedInterval = intent.getStringExtra("interval");


        titleTextView.setText(receivedTitle);
        amountTextView.setText(receivedAmount + "$");
        typeTextView.setText(receivedType);
        descriptionTextView.setText(receivedDescription);
        endDateTextView.setText(receivedEndDate);
        intervalTextView.setText(receivedInterval);



    }
}
