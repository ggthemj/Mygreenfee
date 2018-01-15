package com.example.mygreenfee;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Julien on 13/01/2018.
 */

public class SpinTeeAdapter extends ArrayAdapter<TeeSpinnerDTO> {

    private Context context;

    private TeeSpinnerDTO[] values;

    public SpinTeeAdapter(Context context, int textViewResourceId,
                       TeeSpinnerDTO[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.length;
    }

    @Override
    public TeeSpinnerDTO getItem(int position){
        return values[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        //label.setTextColor(Color.BLACK);

        label.setText(values[position].getName());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        //label.setTextColor(Color.BLACK);
        label.setText(values[position].getName());

        return label;
    }
}
