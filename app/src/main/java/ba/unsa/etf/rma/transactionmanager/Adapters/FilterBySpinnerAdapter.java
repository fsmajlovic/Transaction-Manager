package ba.unsa.etf.rma.transactionmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ba.unsa.etf.rma.transactionmanager.R;

public class FilterBySpinnerAdapter  extends ArrayAdapter<String> {
    private String[] contentArray;
    private Integer[] imageArray;
    private Context mContext;
    public FilterBySpinnerAdapter(Context context, int resource, String[] objects,
                          Integer[] imageArray) {
        super(context,  R.layout.custom_layout, R.id.spinnerTextView, objects);
        this.mContext = context;
        this.contentArray = objects;
        this.imageArray = imageArray;
    }

    public View getCustomView (int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_layout, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.spinnerTextView);
        textView.setText(contentArray[position]);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.spinnerImages);
        imageView.setImageResource(imageArray[position]);

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
