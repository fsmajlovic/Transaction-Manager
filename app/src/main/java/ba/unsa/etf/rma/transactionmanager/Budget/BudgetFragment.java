package ba.unsa.etf.rma.transactionmanager.Budget;

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

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Budget.BudgetPresenter;
import ba.unsa.etf.rma.transactionmanager.Budget.IBudgetPresenter;
import ba.unsa.etf.rma.transactionmanager.Budget.IBudgetView;
import ba.unsa.etf.rma.transactionmanager.R;

public class BudgetFragment extends Fragment implements IBudgetView {
    private TextView budgetEditText;
    private EditText totalLimitEditText;
    private EditText monthLimitEditText;
    private TextView saveTextView;
    private boolean budgetVal = true, monthVal = true, totalVal = true;

    private IBudgetPresenter presenter;

    public IBudgetPresenter getPresenter() {
        if (presenter == null) {
            presenter = new BudgetPresenter(this, getActivity());
        }
        return presenter;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_budget, container, false);
        budgetEditText = fragmentView.findViewById(R.id.BudgetEditText);
        totalLimitEditText = fragmentView.findViewById(R.id.TotalLimitEditText);
        monthLimitEditText = fragmentView.findViewById(R.id.monthLimitEditText);
        saveTextView = fragmentView.findViewById(R.id.saveTextView);
        //Comment cut from here
        getPresenter().searchAccount("");

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
                    //Post here
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
    public void refreshView() {
        Account account = getPresenter().getAccount();
        budgetEditText.setText(String.valueOf(account.getBudget()));
        totalLimitEditText.setText(String.valueOf(account.getTotalLimit()));
        monthLimitEditText.setText(String.valueOf(account.getMonthLimit()));
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }
}
//        try {
//            presenter = new BudgetPresenter();
//            budgetEditText.setText(String.valueOf(presenter.getInteractor().getBudget()));
//            monthLimitEditText.setText(String.valueOf(presenter.getInteractor().getMonthLimit()));
//            totalLimitEditText.setText(String.valueOf(presenter.getInteractor().getTotalLimit()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//

//
//
//        saveTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(budgetVal && monthVal && totalVal){
//                    presenter.setBudget(Double.valueOf(budgetEditText.getText().toString()));
//                    presenter.setMonthLimit(Double.valueOf(monthLimitEditText.getText().toString()));
//                    presenter.setTotalLimit(Double.valueOf(totalLimitEditText.getText().toString()));
//                }
//                else {
//                    new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
//                            .setTitle("Changes")
//                            .setMessage("Seems like some of your changes might be wrong.(Red color indicates an incorrect change).")
//                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                }
//                            })
//                            .setIcon(android.R.drawable.ic_dialog_alert)
//                            .show();
//                }
//            }
//        });
//
//
//        return fragmentView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        try {
//            presenter = new BudgetPresenter();
//            budgetEditText.setText(String.valueOf(presenter.getInteractor().getBudget()));
//            monthLimitEditText.setText(String.valueOf(presenter.getInteractor().getMonthLimit()));
//            totalLimitEditText.setText(String.valueOf(presenter.getInteractor().getTotalLimit()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//}
