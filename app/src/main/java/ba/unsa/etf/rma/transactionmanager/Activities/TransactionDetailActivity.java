package ba.unsa.etf.rma.transactionmanager.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ba.unsa.etf.rma.transactionmanager.R;

public class TransactionDetailActivity extends AppCompatActivity {
    private TextView txt1;
    private TextView txt2;
    private TextView txt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_details);
        txt1 = (TextView) findViewById(R.id.titleTextView);
        txt2 = (TextView) findViewById(R.id.dateTextView);
        txt3 = (TextView) findViewById(R.id.amountTextView);
        Intent intent = getIntent();
    }
}
