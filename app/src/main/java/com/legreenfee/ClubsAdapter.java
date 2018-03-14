package com.legreenfee;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Julien on 12/01/2018.
 */

public class ClubsAdapter extends ArrayAdapter<ClubData> implements View.OnClickListener {

    private ArrayList<ClubData> dataSet;
    private List<ClubData> clubDataList = new ArrayList<ClubData>();
    Context mContext;


    private static class ViewHolder {
        TextView name;
        TextView km;
    }

    public ClubsAdapter(Context applicationContext) {
        super(applicationContext, R.layout.club_row_item);
    }


    public ClubsAdapter(ArrayList<ClubData> data, Context context) {
        super(context, R.layout.club_row_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.clubDataList.addAll(data);
    }

/*
    @Override
    public int getCount() {
        return clubDataList.size();
    }

    @Override
    public ClubData getItem(int position) {
        return clubDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
*/
    @Override
    public void onClick(View v) {

        int position = (Integer) v.getTag();
        Object object = getItem(position);


    }

    private int lastPosition = -1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClubData dataModel = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.club_row_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.club_list_name);
            viewHolder.km = (TextView) convertView.findViewById(R.id.club_list_km);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.name.setText(dataModel.name);
        String km = "";
        if (dataModel.distance != 0) {
            km = (int)dataModel.distance + " km";
        }

        viewHolder.km.setText(km);
        //viewHolder.info.setOnClickListener(this);

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        clubDataList.clear();
        if (charText.length() == 0) {
            if (dataSet != null) {
                clubDataList.addAll(dataSet);
            }
        } else {
            for (ClubData wp : dataSet) {
                if (wp.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    clubDataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
