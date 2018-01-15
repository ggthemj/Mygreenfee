package com.example.mygreenfee;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import static android.icu.text.NumberFormat.CURRENCYSTYLE;

/**
 * Created by Julien on 12/01/2018.
 */

public class TeeTimesAdapter extends ArrayAdapter<TeeTime> implements View.OnClickListener {

    private ArrayList<TeeTime> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView time;
        TextView sale_price;
        TextView slots_free;
    }

    public TeeTimesAdapter(Context applicationContext) {
        super(applicationContext, R.layout.teetime_row_item);
    }


    public TeeTimesAdapter(ArrayList<TeeTime> data, Context context) {
        super(context, R.layout.teetime_row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);
        TeeTime dataModel = (TeeTime) object;

        switch (v.getId()) {
            case R.id.booking_button:

                break;
        }
    }

    private int lastPosition = -1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TeeTime dataModel = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.teetime_row_item, parent, false);
            viewHolder.slots_free = (TextView) convertView.findViewById(R.id.booking_dispo_view);
            viewHolder.time = (TextView) convertView.findViewById(R.id.booking_time_view);
            viewHolder.sale_price = (TextView) convertView.findViewById(R.id.booking_price_view);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.slots_free.setText(String.valueOf(dataModel.getSlots_free()) + " " + getContext().getResources().getText(R.string.slotsFree));
        viewHolder.time.setText(String.valueOf(dataModel.getTime()));
        NumberFormat formatter = NumberFormat.getInstance(Locale.FRANCE);
        String moneyString = formatter.format(dataModel.getSale_price());
        viewHolder.sale_price.setText(moneyString + "€");

        //viewHolder.info.setOnClickListener(this);

        return convertView;
    }

}
