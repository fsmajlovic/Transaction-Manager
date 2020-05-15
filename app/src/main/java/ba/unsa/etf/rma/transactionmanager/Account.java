package ba.unsa.etf.rma.transactionmanager;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    int id;
    private double budget;
    private double totalLimit;
    private double monthLimit;

    protected Account(Parcel in) {
        id = in.readInt();
        budget = in.readDouble();
        totalLimit = in.readDouble();
        monthLimit = in.readDouble();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account() {
    }

    public Account(int id, double budget, double totalLimit, double monthLimit) {
        this.id = id;
        this.budget = budget;
        this.totalLimit = totalLimit;
        this.monthLimit = monthLimit;
    }

    public Account(double budget, double totalLimit, double monthLimit) {
        this.budget = budget;
        this.totalLimit = totalLimit;
        this.monthLimit = monthLimit;
    }



    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public double getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(double totalLimit) {
        this.totalLimit = totalLimit;
    }

    public double getMonthLimit() {
        return monthLimit;
    }

    public void setMonthLimit(double monthLimit) {
        this.monthLimit = monthLimit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeDouble(budget);
        dest.writeDouble(totalLimit);
        dest.writeDouble(monthLimit);
    }
}
