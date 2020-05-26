package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ba.unsa.etf.rma.transactionmanager.R;
import ba.unsa.etf.rma.transactionmanager.Transaction;

public class TransactionDetailInteractor extends AsyncTask<String, Integer, Void> implements ITransactionDetailInteractor {
    private int ID;
    private Transaction transactionNew;
    private int action;
    private ArrayList<Transaction> transactions;
    private ArrayList<Transaction> transactionsAll;
    private Context context;


    public TransactionDetailInteractor(Context context, int ID, Transaction transactionNew, int action){
        this.context = context;
        this.ID = ID;
        this.transactionNew = transactionNew;
        this.action = action;
        transactions = new ArrayList<Transaction>();
        transactionsAll = new ArrayList<Transaction>();
    }

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

            URL urlPost = null;
            try {
                if (action == 1) {
                    urlPost = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+api_id+"/transactions");
                } else if (action == 2 || action == 3) {
                    urlPost = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+api_id+"/transactions/"
                            + ID);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                HttpURLConnection con = (HttpURLConnection) urlPost.openConnection();
                if (action == 3) {
                    con.setRequestMethod("DELETE");
                } else {
                    con.setRequestMethod("POST");
                }
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                if (action != 3) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    String strDate = dateFormat.format(transactionNew.getDate());
                    String strEndDate = "null";
                    if (transactionNew.getEndDate() != null) {
                        strEndDate = dateFormat.format(transactionNew.getEndDate());
                    }
                    String jsonInputString = "{ \"date\": \"" + strDate + "\", \"title\": \"" +
                            transactionNew.getTitle() + "\", \"amount\":" + String.valueOf(transactionNew.getAmount())
                            + ", \"endDate\": \"" + strEndDate + "\", \"itemDescription\": \"" + transactionNew.getItemDescription()
                            + "\", \"transactionInterval\": \"" + String.valueOf(transactionNew.getTransactionInterval())
                            + "\", \"typeId\": " + String.valueOf(transactionNew.getTransactionTypeID()) + " }";
                    try (OutputStream os = con.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                }
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int page = 0; page < 10; page++) {
                String url2 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/"+api_id+"/transactions/?page=" + page;
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

            try {
                String url1 = "http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/" + api_id;
                URL urlPostBudget = new URL(url1);
                HttpURLConnection con = (HttpURLConnection) urlPostBudget.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                String jsonInputString = "{\"budget\":" + String.valueOf(calculateGlobalAmount(transactions)) + "}";
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);
        //caller.onDoneAddDeleteEdit();
    }

    public interface OnAddDeleteEditDone{
        public void onDoneAddDeleteEdit();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Transaction getTransactionNew() {
        return transactionNew;
    }

    public void setTransactionNew(Transaction transactionNew) {
        this.transactionNew = transactionNew;
    }

    public ArrayList<Transaction> getTransactionsAll() {
        return transactionsAll;
    }

    public void setTransactionsAll(ArrayList<Transaction> transactionsAll) {
        this.transactionsAll = transactionsAll;
    }

    public double calculateGlobalAmount(ArrayList<Transaction> transactions){
        double budget = 0.0;
        for(Transaction t: transactions){
            if(t.getType().equals(Transaction.Type.REGULARINCOME)){
                Date startDate = t.getDate();
                Date endDate = t.getEndDate();
                int times = 0;
                while(startDate.compareTo(endDate) < 0){
                    int interval = t.getTransactionInterval();
                    Calendar c = Calendar.getInstance();
                    c.setTime(startDate);
                    c.add(Calendar.DATE, interval);
                    startDate = c.getTime();
                    times++;
                }
                if(times == 0){
                    budget += t.getAmount();
                }
                else
                    budget = budget + times*t.getAmount();
            }
            else if(t.getType().equals(Transaction.Type.REGULARPAYMENT)){
                Date startDate = t.getDate();
                Date endDate = t.getEndDate();
                int times = 0;
                while(startDate.compareTo(endDate) < 0){
                    int interval = t.getTransactionInterval();
                    Calendar c = Calendar.getInstance();
                    c.setTime(startDate);
                    c.add(Calendar.DATE, interval);
                    startDate = c.getTime();
                    times++;
                }
                if(times == 0){
                    budget -= t.getAmount();
                }
                else
                    budget = budget - times*t.getAmount();
            }
            else if(t.getType().equals(Transaction.Type.INDIVIDUALPAYMENT) ||
                    t.getType().equals(Transaction.Type.PURCHASE)){
                budget -= t.getAmount();
            }
            else if(t.getType().equals(Transaction.Type.INDIVIDUALINCOME))
                budget += t.getAmount();
        }
        return budget;
    }

}
