package ba.unsa.etf.rma.transactionmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;

public class BudgetFragment extends Fragment {
    private EditText budgetEditText;
    private EditText totalLimitEditText;
    private EditText monthLimitEditText;
    private boolean budgetVal = true, monthVal = true, totalVal = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetEditText = fragmentView.findViewById(R.id.BudgetEditText);
        totalLimitEditText = fragmentView.findViewById(R.id.TotalLimitEditText);
        monthLimitEditText = fragmentView.findViewById(R.id.monthLimitEditText);

        try {
            BudgetPresenter presenter = new BudgetPresenter();
            budgetEditText.setText(String.valueOf(presenter.getInteractor().getBudget()));
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
                    budgetEditText.setBackgroundColor(Color.parseColor("#169617"));
                    budgetVal = true;
                } else {
                    budgetEditText.setBackgroundColor(Color.parseColor("#B41D1D"));
                    budgetVal = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return fragmentView;
    }
}
