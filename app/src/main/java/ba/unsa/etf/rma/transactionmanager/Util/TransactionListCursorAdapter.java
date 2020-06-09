package ba.unsa.etf.rma.transactionmanager.Util;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import ba.unsa.etf.rma.transactionmanager.R;

public class TransactionListCursorAdapter extends ResourceCursorAdapter {

    public TransactionListCursorAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView image = (ImageView)view.findViewById(R.id.transactionTypeImageView);
        int idPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TYPE_ID);
        int transTypeId = cursor.getInt(idPos);
        if(transTypeId == 5)
            image.setImageResource(R.drawable.ic_individual_payment_icon);
        else if(transTypeId == 1)
            image.setImageResource(R.drawable.ic_regular_payment_icon);
        else if(transTypeId== 3)
            image.setImageResource(R.drawable.ic_purchase_icon);
        else if(transTypeId == 4)
            image.setImageResource(R.drawable.ic_individual_income_icon);
        else if(transTypeId == 2)
            image.setImageResource(R.drawable.ic_regular_income_icon);

        TextView name = (TextView) view.findViewById(R.id.transactionNameTextView);
        int titlePos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_TITLE);
        name.setText(cursor.getString(titlePos));

        TextView amount = (TextView) view.findViewById(R.id.transactionAmountTextView);
        int amountPos = cursor.getColumnIndexOrThrow(TransactionDBOpenHelper.TRANSACTION_AMOUNT);
        amount.setText("$" + String.valueOf(cursor.getString(amountPos)));
    }
}
