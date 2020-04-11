package ba.unsa.etf.rma.transactionmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BudgetFragment extends Fragment {
    private EditText budgetEditText;
    private EditText totalLimitEditText;
    private EditText monthLimitEditText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetEditText = fragmentView.findViewById(R.id.BudgetEditText);
        totalLimitEditText = fragmentView.findViewById(R.id.TotalLimitEditText);
        monthLimitEditText = fragmentView.findViewById(R.id.monthLimitEditText);

        return fragmentView;
    }
}
