package ba.unsa.etf.rma.transactionmanager;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TransactionsListViewAdapter extends ArrayAdapter<Transaction> {
    int resource;
    private List<Transaction> transactionsList = new ArrayList<>();
    private Context mContext;

    public TransactionsListViewAdapter(Context context, int _resource, List<Transaction> items) {
        super(context, _resource, items);
        resource = _resource;
        mContext = context;
        transactionsList = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        Transaction currentTransaction = transactionsList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.transactionTypeImageView);
        if(currentTransaction.getType() == Transaction.Type.INDIVIDUALPAYMENT)
            image.setImageResource(R.drawable.ic_individual_payment_icon);
        else if(currentTransaction.getType() == Transaction.Type.REGULARPAYMENT)
            image.setImageResource(R.drawable.ic_regular_payment_icon);
        else if(currentTransaction.getType() == Transaction.Type.PURCHASE)
            image.setImageResource(R.drawable.ic_purchase_icon);
        else if(currentTransaction.getType() == Transaction.Type.INDIVIDUALINCOME)
            image.setImageResource(R.drawable.ic_individual_income_icon);
        else if(currentTransaction.getType() == Transaction.Type.REGULARINCOME)
            image.setImageResource(R.drawable.ic_regular_income_icon);



        TextView name = (TextView) listItem.findViewById(R.id.transactionNameTextView);
        name.setText(currentTransaction.getTitle());

        TextView genre = (TextView) listItem.findViewById(R.id.transactionAmountTextView);
        genre.setText(String.valueOf(currentTransaction.getAmount()));

        return listItem;
    }
}
