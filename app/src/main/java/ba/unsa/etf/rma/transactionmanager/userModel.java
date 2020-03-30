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
                "Electricity expenses", Transaction.Type.REGULARPAYMENT, "Standard monthly" +
                " bill for electricity expenses in January is $100. This amount goes to your power company. " +
                "Total amount of $100 has been included in your Transaction Manager app.",
                30, sdf.parse("15-02-2020")));
        transactions.add( new Transaction(sdf.parse("17-02-2020"), 4125,
                "Motorbike purchase", Transaction.Type.PURCHASE, "New Yamaha motorbike " +
                "bought at Yamaha Sarajevo motorbike store. Black color with the maximum speed of 220 km/h." +
                "Total amount of $4125 has been included in your Transaction Manager app.",
                0, null));
        transactions.add( new Transaction(sdf.parse("10-02-2020"), 3500,
                "Salary", Transaction.Type.REGULARINCOME, "Monthly salary income " +
                "from AppDev CO. Total amount of $3500 has been included in your Transaction Manager app. ",
                30, sdf.parse("09-03-2020")));
        transactions.add( new Transaction(sdf.parse("15-03-2020"), 600,
                "Freelancer project", Transaction.Type.INDIVIDUALINCOME,
                "Paycheck for finishing several tasks for your Freelancer client: App " +
                        "development, SQL database established, MD5 Hashing added to profile passwords. " +
                        "Total amount of $600 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("18-03-2020"), 500,
                "Speeding ticket", Transaction.Type.INDIVIDUALPAYMENT,
                "Camera caught you violating the speed limit law. You have been speeding " +
                        "with the speed of 100 km/h in the 70 km/h area. Destination: \"Zmaja od Bosne, Sarajevo\" " +
                        "Total amount of $500 has been included in your Transaction Manager app.",
                0,null));



        transactions.add( new Transaction(sdf.parse("11-02-2020"), 150,
                "MS Office", Transaction.Type.REGULARPAYMENT,
                "Yearly Microsoft office subscription that allows you to use all of the " +
                        "office features and programs. All of the updates have been included as well. " +
                        "Total amount of $150 has been included in your Transaction Manager app.",
                365, sdf.parse("10-02-2021")));
        transactions.add( new Transaction(sdf.parse("17-02-2020"), 700,
                "ASUS monitor", Transaction.Type.PURCHASE, "New ASUS computer " +
                "monitor purchase. ASUS ROG Swift PG348Q 34\" Curved Ultra-wide G-SYNC Gaming Monitor " +
                "Total amount of 700 has been included in your Transaction Manager app.",
                0, null));


        //continue here
        transactions.add( new Transaction(sdf.parse("10-02-2020"), 3500,
                "AirBNB", Transaction.Type.REGULARINCOME, "Monthly salary income " +
                "from AppDev CO. Total amount of $3500 has been included in your Transaction Manager app. ",
                30, sdf.parse("09-03-2020")));
        transactions.add( new Transaction(sdf.parse("15-03-2020"), 600,
                "Freelancer project", Transaction.Type.INDIVIDUALINCOME,
                "Paycheck for finishing several tasks for your Freelancer client: App " +
                        "development, SQL database established, MD5 Hashing added to profile passwords. " +
                        "Total amount of $600 has been included in your Transaction Manager app.",
                0,null));
        transactions.add( new Transaction(sdf.parse("18-03-2020"), 500,
                "Speeding ticket", Transaction.Type.INDIVIDUALPAYMENT,
                "Camera caught you violating the speed limit law. You have been speeding " +
                        "with the speed of 100 km/h in the 70 km/h area. Destination: \"Zmaja od Bosne, Sarajevo\" " +
                        "Total amount of $500 has been included in your Transaction Manager app.",
                0,null));



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
