package ba.unsa.etf.rma.transactionmanager;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Transaction implements Parcelable {
    public enum Type {
        INDIVIDUALPAYMENT,
        REGULARPAYMENT,
        PURCHASE,
        INDIVIDUALINCOME,
        REGULARINCOME
    };

    private Date date;
    private double amount;
    private String title;
    private Type type;
    private String itemDescription;
    private int transactionInterval;
    private Date endDate;


    protected Transaction(Parcel in) {
        amount = in.readDouble();
        title = in.readString();
        itemDescription = in.readString();
        transactionInterval = in.readInt();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(amount);
        parcel.writeString(title);
        parcel.writeString(itemDescription);
        parcel.writeInt(transactionInterval);
    }

    public Transaction() {
    }

    public Transaction(Date date, double amount, String title, Type type, String itemDescription, int transactionInterval, Date endDate) {
        this.date = date;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.itemDescription = itemDescription;
        this.transactionInterval = transactionInterval;
        this.endDate = endDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public void setTitle(String title){
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
