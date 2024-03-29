package com.haste.adapter;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haste.Dashboard;
import com.haste.R;
import com.haste.ReportObject.Notification;
import com.haste.util.CompactReportUtil;

import java.util.ArrayList;

/**
 * Created by Deborah on 29/02/24.
 */

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.NotificationViewHolder>{
    private static final String TAG = NotificationRecyclerViewAdapter.class.getSimpleName();
    private ArrayList<Notification> notificationList;

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView textview;
        TextView textView2;
        ImageView incidentTypeLogo;


        NotificationViewHolder(View viewItem){
            super(viewItem);
            textview = (TextView) viewItem.findViewById(R.id.row_notification_textview1);
            textView2 = (TextView) viewItem.findViewById(R.id.row_notification_textview2);
            incidentTypeLogo = (ImageView) viewItem.findViewById(R.id.subscription_incident_logo);
        }
    }

    public NotificationRecyclerViewAdapter(){}

    public NotificationRecyclerViewAdapter(ArrayList<Notification> list){
        this.notificationList = list;
    }

    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification_list, parent, false);
        NotificationViewHolder vh = new NotificationViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(holder!=null && holder.textview!=null) {

            Log.d(TAG, notificationList.get(position).getBody());

            holder.textview.setText(notificationList.get(position).getBody());

            CompactReportUtil cmpUtil = new CompactReportUtil();
            String reportLocation = notificationList.get(position).getZipcode();
            String[] coor = reportLocation.split("_");
            Location loc = new Location("");
            loc.setLatitude(Double.parseDouble(coor[1]));
            loc.setLongitude(Double.parseDouble(coor[0]));

            double distanceInMile = cmpUtil.distanceBetweenPoints(loc, Dashboard.location);
            String roundDistance = String.format("%.2f", distanceInMile);
            roundDistance = roundDistance + " mi";
            holder.textView2.setText("From " + roundDistance + " away");
            String reportTitle = notificationList.get(position).getType();

            switch(reportTitle){
                case "Medical":
                    holder.incidentTypeLogo.setImageResource(R.drawable.hospital);
                    break;
                case "Fire":
                    holder.incidentTypeLogo.setImageResource(R.drawable.flame);
                    break;
                case "Police":
                    holder.incidentTypeLogo.setImageResource(R.drawable.siren);
                    break;
                case "Traffic":
                    holder.incidentTypeLogo.setImageResource(R.drawable.cone);
                    break;
                case "Utility":
                    holder.incidentTypeLogo.setImageResource(R.drawable.repairing);
                    break;
                case "Weather":
                case "Missing":
                case "Crime":
                    holder.incidentTypeLogo.setImageResource(R.drawable.rss);
                    break;
                default:
                    holder.incidentTypeLogo.setImageResource(R.drawable.rss);
                    break;
            }
        }else Log.d(TAG, "null view");

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return notificationList.size();
    }


}
