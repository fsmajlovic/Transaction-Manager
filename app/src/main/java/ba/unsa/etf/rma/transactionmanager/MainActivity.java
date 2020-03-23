package ba.unsa.etf.rma.transactionmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    private Spinner filterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filterSpinner = (Spinner) findViewById(R.id.filterSpinner);

    }
}
