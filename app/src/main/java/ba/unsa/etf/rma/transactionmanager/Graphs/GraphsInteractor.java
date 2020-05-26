package ba.unsa.etf.rma.transactionmanager.Graphs;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;
import ba.unsa.etf.rma.transactionmanager.TransactionList.ITransactionInteractor;

public class GraphsInteractor extends AsyncTask<String, Integer, Void> implements IGraphsInteractor {

    private ArrayList<Transaction> transactions;
    private OnGetTransactionsDone callerTypes;
    private Context context;

    public GraphsInteractor(Context context, OnGetTransactionsDone p) {
        this.context = context;
        callerTypes = p;
        transactions = new ArrayList<Transaction>();
    };

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    @Override
    protected Void doInBackground(String... strings) {

        String api_id = this.context.getResources().getString(R.string.api_id);

        for(int page = 0; page < 10; page++) {
            String url2 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/" + api_id + "/transactions/?page=" + page;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                URL url = new URL(url2);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String result = convertStreamToString(in);
                JSONObject jo = new JSONObject(result);
                JSONArray results = jo.getJSONArray("transactions");
                if(results.length() <= 0 )
                    break;
                for (int i = 0; i < results.length(); i++) {
                    JSONObject transactionJSON = results.getJSONObject(i);
                    int id = transactionJSON.getInt("id");
                    String date = transactionJSON.getString("date");
                    String title = transactionJSON.getString("title");
                    Double amount = transactionJSON.getDouble("amount");
                    String itemDescription = transactionJSON.getString("itemDescription");
                    String transactionInterval = transactionJSON.getString("transactionInterval");
                    String endDate = transactionJSON.getString("endDate");
                    int transactionTypeID = transactionJSON.getInt("TransactionTypeId");

                    if (transactionInterval.equals("null")) {
                        transactionInterval = "0";
                    }
                    try {
                        if (endDate.equals("null")) {
                            transactions.add(new Transaction(id, sdf.parse(date), amount, title, itemDescription,
                                    Integer.valueOf(transactionInterval), null, transactionTypeID));
                        } else {
                            transactions.add(new Transaction(id, sdf.parse(date), amount, title, itemDescription,
                                    Integer.valueOf(transactionInterval), sdf.parse(endDate), transactionTypeID));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public ArrayList<Transaction> getTransactions() {
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        callerTypes.onDoneTransactions(transactions);
    }

    public interface OnGetTransactionsDone{
        public void onDoneTransactions(ArrayList<Transaction> transactions);
    }

}
