package ba.unsa.etf.rma.transactionmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class userModel {
    private Account account;
    private ArrayList<Transaction> transactions;

    public userModel() throws ParseException {
        //Adding some account data
        account = new Account();
        transactions = new ArrayList<Transaction>();
        account.setBudget(100000.0);
        account.setTotalLimit(60000.0);
        account.setMonthLimit(5000.0);

        //Adding some transactions
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        transactions.add( new Transaction(sdf.parse("16-01-2020"), 100,
                "Electricity bill", Transaction.Type.REGULARPAYMENT, "Monthly bill for electricity.",
                30, sdf.parse("15-02-2020")));
        transactions.add( new Transaction(sdf.parse("17-02-2020"), 3125,
                "Motorbike purchase", Transaction.Type.INDIVIDUALPAYMENT, "New Yamaha motorbike.",
                0, null));
        transactions.add( new Transaction(sdf.parse("10-02-2020"), 3500,
                "Regular salary", Transaction.Type.REGULARINCOME, "Monthly salary.",
                30, sdf.parse("09-03-2020")));
        transactions.add( new Transaction(sdf.parse("11-02-2020"), 150,
                "Office subscription", Transaction.Type.REGULARPAYMENT,
                "Yearly microsoft office subscription.",
                365, sdf.parse("10-02-2021")));

    }

    public userModel(Account account, ArrayList<Transaction> transactions) {
        this.account = account;
        this.transactions = transactions;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }
}
