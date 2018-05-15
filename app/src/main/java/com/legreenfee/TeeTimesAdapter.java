package com.legreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Julien on 12/01/2018.
 */

public class TeeTimesAdapter extends ArrayAdapter<TeeTime> {

    private CoursesRepository coursesRepo;
    private ArrayList<TeeTime> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView time;
        TextView sale_price;
        TextView slots_free;
        Button book;
        TextView reduction;
        ImageView etiquette;
    }

    public TeeTimesAdapter(Context applicationContext) {
        super(applicationContext, R.layout.teetime_row_item);

        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        this.coursesRepo = new CoursesRepository(lang, (BookingActivity) applicationContext);
    }


    public TeeTimesAdapter(ArrayList<TeeTime> data, Context context) {
        super(context, R.layout.teetime_row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    private int lastPosition = -1;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        TeeTime dataModel = getItem(position);

        final ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.teetime_row_item, parent, false);
            viewHolder.slots_free = (TextView) convertView.findViewById(R.id.booking_dispo_view);
            viewHolder.time = (TextView) convertView.findViewById(R.id.booking_time_view);
            viewHolder.sale_price = (TextView) convertView.findViewById(R.id.booking_price_view);
            viewHolder.book = (Button) convertView.findViewById(R.id.booking_button);
            viewHolder.reduction = (TextView) convertView.findViewById(R.id.booking_reduc_view);
            viewHolder.etiquette = (ImageView) convertView.findViewById(R.id.booking_etiquette);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        BookingActivity context = (BookingActivity) getContext();
        int reduction = dataModel.getReduction();
        if (context.getClubCard() != null && context.getClubCard().getCoursesId() != null) {
            for (int i = 0; i < context.getClubCard().getCoursesId().size(); i++) {
                if (reduction < context.getClubCard().getDiscount()
                        && context.getClubCard().getCoursesId().get(i) == Integer.valueOf(context.getCourseId()).intValue()) {
                    reduction = context.getClubCard().getDiscount();
                    break;
                }
            }
        }

        viewHolder.slots_free.setText(String.valueOf(dataModel.getSlots_free()) + " " + getContext().getResources().getText(R.string.slotsFree));
        viewHolder.time.setText(String.valueOf(dataModel.getTime()));
        DecimalFormat df = new DecimalFormat("#.00");
        String moneyString = df.format((dataModel.getTotal_price() - dataModel.getTotal_price() * reduction / 100) * context.getNbPlayers());

        viewHolder.sale_price.setText(moneyString + "€");
        viewHolder.book.setTag(position);
        if (reduction != 0) {
            viewHolder.reduction.setVisibility(View.VISIBLE);
            viewHolder.reduction.setText("-" + reduction + "%");
            viewHolder.etiquette.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.reduction.setVisibility(View.INVISIBLE);
            viewHolder.etiquette.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                BookingActivity context = (BookingActivity) getContext();
                ViewHolder i = (ViewHolder) v.getTag();
                Button b = ((RelativeLayout) v).findViewById(R.id.booking_button);
                Integer position = (Integer) b.getTag();

                TeeTime teeTime = getItem(position);

                final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                SharedPreferences sharedPref = context.getSharedPreferences("appData", Context.MODE_PRIVATE);
                String is_logged = sharedPref.getString("user_email", "false");

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("order_arg1", context.getClubId());
                editor.putString("order_arg2", teeTime.getTee_public_id());
                editor.putString("order_arg3", teeTime.getTime());
                editor.putInt("order_arg4", context.getNbPlayers());
                editor.putString("order_arg5", dateFormat.format(context.getCalendarSelected().getTime()));
                editor.putString("order_arg6", context.getClub().getName());
                editor.putString("order_arg7", viewHolder.sale_price.getText().toString());

                editor.putString("order_price", viewHolder.sale_price.getText().toString());
                editor.putString("order_club", context.getClub().getName());
                editor.putString("order_date", dateFormat.format(context.getCalendarSelected().getTime()));

                editor.commit();

                Intent intent = new Intent(context.getApplicationContext(), OrderActivity.class);
                context.startActivity(intent);

            }
        });

        //viewHolder.info.setOnClickListener(this);

        return convertView;
    }

}
