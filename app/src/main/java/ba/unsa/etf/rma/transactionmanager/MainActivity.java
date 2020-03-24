package ba.unsa.etf.rma.transactionmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Spinner filterSpinner;
    private TextView monthTextView;
    private ImageView arrowBackImageView;
    private ImageView arrowForwardImageView;
    private Spinner sortBySpinner;
    private ArrayList<String> entries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filterSpinner = (Spinner) findViewById(R.id.filterSpinner);
        sortBySpinner = (Spinner) findViewById(R.id.sortBySpinner);
        monthTextView = (TextView) findViewById(R.id.monthTextView);
        arrowBackImageView = (ImageView) findViewById(R.id.arrowBackwardImageView);
        arrowForwardImageView = (ImageView) findViewById(R.id.arrowForwardImageView);

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

        //Sort by Spinner regulations
        String[] arrayStringSortBy = {
                "Price - Ascending", "Price - Descending", "Title - Ascending", "Title - Descending",
                "Date - Ascending", "Date - Descending"
        };
        ArrayAdapter<String> adapterSortBy = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arrayStringSortBy);
        adapterSortBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapterSortBy);

        //Filter by Spinner regulations
        filterSpinner = findViewById(R.id.filterSpinner);
        String[] textArray = { "INDIVIDUALPAYMENT", "REGULARPAYMENT", "PURCHASE", "INDIVIDUALINCOME",
                "REGURALINCOME"};
        Integer[] imageArray = { R.drawable.ic_individual_payment_icon, R.drawable.ic_regular_payment_icon,
                R.drawable.ic_purchase_icon, R.drawable.ic_individual_income_icon, R.drawable.ic_regular_income_icon };
        FilterBySpinnerAdapter adapterFilterBySpinner = new FilterBySpinnerAdapter(this,
                R.layout.custom_layout, textArray, imageArray);
        filterSpinner.setAdapter(adapterFilterBySpinner);
    }
}
