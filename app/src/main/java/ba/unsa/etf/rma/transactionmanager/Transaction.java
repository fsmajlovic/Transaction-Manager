package ba.unsa.etf.rma.transactionmanager;

import java.util.Date;

public class Transaction {
    private Date date;
    private double amount;
    private String title;
    private enum type {
        INDIVIDUALPAYMENT,
        REGULARPAYMENT,
        PURCHASE,
        INDIVIDUALINCOME,
        REGULARINCOME
    };
    private String itemDescription;
    private int transactionInterval;
    private Date endDate;

    public Transaction() {
    }

    public Transaction(Date date, double amount, String title, String itemDescription, int transactionInterval, Date endDate) throws NameTooLongException {
        this.date = date;
        this.amount = amount;
        setTitle(title);
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws NameTooLongException {
        if(title.length() < 3 || title.length() > 15)
            throw new NameTooLongException("Title is too long or too short.");
        this.title = title;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getTransactionInterval() {
        return transactionInterval;
    }

    public void setTransactionInterval(int transactionInterval) {
        this.transactionInterval = transactionInterval;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
