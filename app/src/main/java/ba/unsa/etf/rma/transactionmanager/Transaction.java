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
}
