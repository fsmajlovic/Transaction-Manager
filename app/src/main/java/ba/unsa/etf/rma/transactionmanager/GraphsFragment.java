package ba.unsa.etf.rma.transactionmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GraphsFragment extends Fragment {
    private GraphPresenter presenter;
    private BarChart chartOne;
    private BarChart chartTwo;
    private BarChart chartThree;
    private TextView periodTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_graphs, container, false);
        chartOne = (BarChart) fragmentView.findViewById(R.id.chartOne);
        chartTwo = (BarChart) fragmentView.findViewById(R.id.chartTwo);
        chartThree = (BarChart) fragmentView.findViewById(R.id.chartThree);
        periodTextView = (TextView) fragmentView.findViewById(R.id.periodTextView);
        try {
            presenter = new GraphPresenter();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        periodTextView.setText("Monthly");
        periodTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(periodTextView.getText().toString().equals("Monthly")) {
                    periodTextView.setText("Weekly");
                    validateGraphs();
                    return;
                }
                else if(periodTextView.getText().toString().equals("Weekly")) {
                    periodTextView.setText("Daily");
                    validateGraphs();
                    return;
                }
                else if(periodTextView.getText().toString().equals("Daily"));
                {
                    periodTextView.setText("Monthly");
                    validateGraphs();
                    return;
                }

            }
        });

        validateGraphs();

        return fragmentView;
    }


    public void validateGraphs(){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        double[] money_spent_sums_array = new double[365];
        double[] money_earned_sums_array = new double[365];
        double[] money_total_sums_array = new double[365];
        Arrays.fill(money_spent_sums_array, 0.0);
        Arrays.fill(money_earned_sums_array, 0.0);
        Arrays.fill(money_total_sums_array, 0.0);

        for(Transaction t: presenter.getInteractor().getTransactions()){
            //Getting date from transaction
            date = t.getDate();
            cal.setTime(date);
            int period = 0;
            if(periodTextView.getText().toString().equals("Monthly")) {
                period = cal.get(Calendar.MONTH);
            }
            else if(periodTextView.getText().toString().equals("Weekly")) {
                period = cal.get(Calendar.WEEK_OF_YEAR);
            }
            else if(periodTextView.getText().toString().equals("Daily")) {
                period = cal.get(Calendar.DAY_OF_YEAR);
            }

            if(cal.get(Calendar.YEAR) == 2020) {
                //Payments
                if (t.getType().equals(Transaction.Type.PURCHASE)
                        || t.getType().equals(Transaction.Type.INDIVIDUALPAYMENT)) {
                    money_spent_sums_array[period] += t.getAmount();
                    money_total_sums_array[period] -= t.getAmount();
                }
                else if (t.getType().equals(Transaction.Type.REGULARPAYMENT)) {
                    Date startDate = t.getDate();
                    Date endDate = t.getEndDate();
                    while(startDate.compareTo(endDate) < 0){
                        int interval = t.getTransactionInterval();
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        int regularPeriod = 0;
                        if(periodTextView.getText().toString().equals("Monthly")) {
                            regularPeriod = c.get(Calendar.MONTH);
                        }
                        else if(periodTextView.getText().toString().equals("Weekly")) {
                            regularPeriod = c.get(Calendar.WEEK_OF_YEAR);
                        }
                        else if(periodTextView.getText().toString().equals("Daily")) {
                            regularPeriod = c.get(Calendar.DAY_OF_YEAR);
                        }
                        money_spent_sums_array[regularPeriod] += t.getAmount();
                        money_total_sums_array[regularPeriod] -= t.getAmount();
                        c.add(Calendar.DATE, interval);
                        startDate = c.getTime();
                    }
                }
                //Income
                else if (t.getType().equals(Transaction.Type.INDIVIDUALINCOME)) {
                    money_earned_sums_array[period] += t.getAmount();
                    money_total_sums_array[period] += t.getAmount();
                }
                else if (t.getType().equals(Transaction.Type.REGULARINCOME)) {
                    Date startDate = t.getDate();
                    Date endDate = t.getEndDate();
                    while(startDate.compareTo(endDate) < 0){
                        int interval = t.getTransactionInterval();
                        Calendar c = Calendar.getInstance();
                        c.setTime(startDate);
                        int regularPeriod = 0;
                        if(periodTextView.getText().toString().equals("Monthly")) {
                            regularPeriod = c.get(Calendar.MONTH);
                        }
                        else if(periodTextView.getText().toString().equals("Weekly")) {
                            regularPeriod = c.get(Calendar.WEEK_OF_YEAR);
                        }
                        else if(periodTextView.getText().toString().equals("Daily")) {
                            regularPeriod = c.get(Calendar.DAY_OF_YEAR);
                        }
                        money_earned_sums_array[regularPeriod] += t.getAmount();
                        money_total_sums_array[regularPeriod] += t.getAmount();
                        c.add(Calendar.DATE, interval);
                        startDate = c.getTime();
                    }
                }
            }
        }

        List<BarEntry> entriesSpent = new ArrayList<>();
        List<BarEntry> entriesEarned = new ArrayList<>();
        List<BarEntry> entriesTotal = new ArrayList<>();

        int numberOfEntries = 0;
        if(periodTextView.getText().toString().equals("Monthly")) {
            numberOfEntries = 12;
        }
        else if(periodTextView.getText().toString().equals("Weekly")) {
            numberOfEntries = 48;
        }
        else if(periodTextView.getText().toString().equals("Daily")) {
            numberOfEntries = 365;
        }


        for(int i = 0; i < numberOfEntries; i++){
            entriesSpent.add(new BarEntry(i, (int) money_spent_sums_array[i]));
            entriesEarned.add(new BarEntry(i, (int) money_earned_sums_array[i]));
            entriesTotal.add(new BarEntry(i, (int) money_total_sums_array[i]));
        }

        //Spent
        BarDataSet setSpent = new BarDataSet(entriesSpent, "Spent money");
        setSpent.setColor(Color.parseColor("#B41D1D"));
        setSpent.setValueTextColor(Color.parseColor("#B41D1D"));
        BarData dataSpent = new BarData(setSpent);
        dataSpent.setBarWidth(0.9f); // set custom bar width
        chartOne.setData(dataSpent);
        chartOne.setFitBars(true); // make the x-axis fit exactly all bars
        chartOne.invalidate(); //

        //Earned
        BarDataSet setEarned = new BarDataSet(entriesEarned, "Earned money");
        setEarned.setColor(Color.parseColor("#169617"));
        setEarned.setValueTextColor(Color.parseColor("#169617"));
        BarData dataEarned = new BarData(setEarned);
        dataEarned.setBarWidth(0.9f); // set custom bar width
        chartTwo.setData(dataEarned);
        chartTwo.setFitBars(true); // make the x-axis fit exactly all bars
        chartTwo.invalidate(); //

        //Total (Payment and Income)
        BarDataSet setTotal = new BarDataSet(entriesTotal, "Spent and Earned");
        setTotal.setColor(Color.parseColor("#B8A228"));
        setTotal.setValueTextColor(Color.parseColor("#B8A228"));
        BarData dataTotal = new BarData(setTotal);
        dataTotal.setBarWidth(0.9f); // set custom bar width
        chartThree.setData(dataTotal);
        chartThree.setFitBars(true); // make the x-axis fit exactly all bars
        chartThree.invalidate(); //
    }

}
