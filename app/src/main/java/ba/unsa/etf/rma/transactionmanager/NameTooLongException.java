package ba.unsa.etf.rma.transactionmanager;

public class NameTooLongException extends Exception {
    public NameTooLongException(String text){
        super(text);
    }
}
