package ba.unsa.etf.rma.transactionmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;

public class BudgetFragment extends Fragment {
    private EditText budgetEditText;
    private EditText totalLimitEditText;
    private EditText monthLimitEditText;
    private TextView saveTextView;
    private BudgetPresenter presenter;
    private boolean budgetVal = true, monthVal = true, totalVal = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetEditText = fragmentView.findViewById(R.id.BudgetEditText);
        totalLimitEditText = fragmentView.findViewById(R.id.TotalLimitEditText);
        monthLimitEditText = fragmentView.findViewById(R.id.monthLimitEditText);
        saveTextView = fragmentView.findViewById(R.id.saveTextView);

        try {
            presenter = new BudgetPresenter();
            budgetEditText.setText(String.valueOf(presenter.getInteractor().getBudget()));
            monthLimitEditText.setText(String.valueOf(presenter.getInteractor().getMonthLimit()));
            totalLimitEditText.setText(String.valueOf(presenter.getInteractor().getTotalLimit()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        budgetEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().matches("-?\\d+(\\.\\d+)?")) {
                    budgetEditText.setTextColor(Color.parseColor("#B8A228"));
                    budgetVal = true;
                } else {
                    budgetEditText.setTextColor(Color.parseColor("#B41D1D"));
                    budgetVal = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        monthLimitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().matches("-?\\d+(\\.\\d+)?")) {
                    monthLimitEditText.setTextColor(Color.parseColor("#B8A228"));
                    monthVal = true;
                } else {
                    monthLimitEditText.setTextColor(Color.parseColor("#B41D1D"));
                    monthVal = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totalLimitEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().matches("-?\\d+(\\.\\d+)?")) {
                    totalLimitEditText.setTextColor(Color.parseColor("#B8A228"));
                    totalVal = true;
                } else {
                    totalLimitEditText.setTextColor(Color.parseColor("#B41D1D"));
                    totalVal = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(budgetVal && monthVal && totalVal){
                    presenter.setBudget(Double.valueOf(budgetEditText.getText().toString()));
                    presenter.setMonthLimit(Double.valueOf(monthLimitEditText.getText().toString()));
                    presenter.setTotalLimit(Double.valueOf(totalLimitEditText.getText().toString()));
                }
                else {
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
            }
        });


        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            presenter = new BudgetPresenter();
            budgetEditText.setText(String.valueOf(presenter.getInteractor().getBudget()));
            monthLimitEditText.setText(String.valueOf(presenter.getInteractor().getMonthLimit()));
            totalLimitEditText.setText(String.valueOf(presenter.getInteractor().getTotalLimit()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
