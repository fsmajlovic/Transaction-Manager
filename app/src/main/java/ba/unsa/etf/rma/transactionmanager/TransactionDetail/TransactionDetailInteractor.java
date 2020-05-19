package ba.unsa.etf.rma.transactionmanager.TransactionDetail;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ba.unsa.etf.rma.transactionmanager.Transaction;

public class TransactionDetailInteractor extends AsyncTask<String, Integer, Void> implements ITransactionDetailInteractor {
    private int ID;
    private Transaction transactionNew;
    private int action;

    private OnAddDeleteEditDone caller;

    public TransactionDetailInteractor(int ID, Transaction transactionNew, int action){
        this.ID = ID;
        this.transactionNew = transactionNew;
        this.action = action;
    }

    @Override
    protected Void doInBackground(String... strings) {

        URL urlPost = null;
        try {
            if(action == 1) {
                urlPost = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/1a90adbb-4968-4995-98f6-bde3431728d5/transactions");
            }
            else if(action == 2 || action == 3){
                urlPost = new URL("http://rma20-app-rmaws.apps.us-west-1.starter.openshift-online.com/account/1a90adbb-4968-4995-98f6-bde3431728d5/transactions/"
                        + ID);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection con = (HttpURLConnection) urlPost.openConnection();
            if(action == 3){
                con.setRequestMethod("DELETE");
            }
            else {
                con.setRequestMethod("POST");
            }
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            if(action != 3) {
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
                System.out.println("STRING OUTPT" + jsonInputString);
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

}
