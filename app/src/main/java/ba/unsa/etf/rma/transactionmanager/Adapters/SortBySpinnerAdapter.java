package ba.unsa.etf.rma.transactionmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ba.unsa.etf.rma.transactionmanager.R;

public class SortBySpinnerAdapter extends ArrayAdapter<String> {

    private String[] contentArray;
    private Context mContext;
    public SortBySpinnerAdapter(Context context, int resource, String[] items) {
        super(context,  R.layout.simple_spinner_item, R.id.sortSpinnerTextView, items);
        this.mContext = context;
        this.contentArray = items;
    }

    public View getCustomView (int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_spinner_item, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.sortSpinnerTextView);
        textView.setText(contentArray[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

}
