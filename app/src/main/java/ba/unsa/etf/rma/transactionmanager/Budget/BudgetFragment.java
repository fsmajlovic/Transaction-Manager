package ba.unsa.etf.rma.transactionmanager.Budget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
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
import java.util.Observable;
import java.util.Observer;

import ba.unsa.etf.rma.transactionmanager.Account;
import ba.unsa.etf.rma.transactionmanager.Budget.BudgetPresenter;
import ba.unsa.etf.rma.transactionmanager.Budget.IBudgetPresenter;
import ba.unsa.etf.rma.transactionmanager.Budget.IBudgetView;
import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Util.NetworkChangeReceiver;

public class BudgetFragment extends Fragment implements IBudgetView, Observer {
    private TextView budgetEditText;
    private EditText totalLimitEditText;
    private EditText monthLimitEditText;
    private TextView saveTextView;
    private TextView offline;
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
        offline = fragmentView.findViewById(R.id.offline);
        //Comment cut from here
        if(isNetworkAvailable(getActivity())) {
            onlineMode();
        }
        else{
            offlineMode();
        }
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
                    if(isNetworkAvailable(getActivity())) {
                        getPresenter().searchAccount(getActivity(), "{\"budget\":" + budgetEditText.getText().toString()
                                + ",\"monthLimit\":" + monthLimitEditText.getText().toString()
                                + ",\"totalLimit\":" + totalLimitEditText.getText().toString() + "}");
                    }
                    else{
                        Account accountDB = getPresenter().getAccountFromDatabase();
                        accountDB.setBudget(Double.parseDouble(budgetEditText.getText().toString()));
                        accountDB.setTotalLimit(Double.parseDouble(totalLimitEditText.getText().toString()));
                        accountDB.setMonthLimit(Double.parseDouble(monthLimitEditText.getText().toString()));
                        getPresenter().setAccountToDatabase(accountDB);
                    }
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
    public void refreshView(Account account) {
        if(account != null) {
            budgetEditText.setText(String.valueOf(account.getBudget()));
            totalLimitEditText.setText(String.valueOf(account.getTotalLimit()));
            monthLimitEditText.setText(String.valueOf(account.getMonthLimit()));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        NetworkChangeReceiver.getObservable().deleteObserver(this);
        if(isNetworkAvailable(getActivity())) {
            onlineMode();
        }else{
            offlineMode();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        NetworkChangeReceiver.getObservable().addObserver(this);
        if(isNetworkAvailable(getActivity())) {
            onlineMode();
        }else{
            offlineMode();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if(isNetworkAvailable(getActivity())) {
            onlineMode();
        }else{
            offlineMode();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void onlineMode(){
        offline.setVisibility(View.INVISIBLE);
        getPresenter().searchAccount(getActivity(), "{\"budget\":" + budgetEditText.getText().toString()
                + ",\"monthLimit\":" + monthLimitEditText.getText().toString()
                + ",\"totalLimit\":" + totalLimitEditText.getText().toString() + "}");
    }

    public void offlineMode(){
        Account acc = getPresenter().getAccountFromDatabase();
        offline.setVisibility(View.VISIBLE);
        refreshView(acc);
    }
}

