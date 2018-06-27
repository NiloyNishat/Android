package com.example.android.safehome.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.safehome.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyGridAdapter extends BaseAdapter{
    public List<Appliance> applianceList;
    public Activity activity;
    ArrayList<Appliance> appliancesArrayList;

    public MyGridAdapter( Activity activity, List<Appliance> applianceList) {
        this.activity = activity;
        this.applianceList = applianceList;
        this.appliancesArrayList = new ArrayList<Appliance>();
        appliancesArrayList.addAll(applianceList);

    }


    @Override
    public int getCount() {
        return applianceList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder= null;

        if(rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.appliance_grid_style, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = rowView.findViewById(R.id.tv_appliance);
            viewHolder.image = rowView.findViewById(R.id.iv_appliance);
            viewHolder.status = rowView.findViewById(R.id.im_dot);
            rowView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(applianceList.get(position).name + " on");

        Picasso.get().load(applianceList.get(position).image_url).fit().into(viewHolder.image);

        if(applianceList.get(position).status.equals("false")){
            viewHolder.status.setVisibility(View.VISIBLE);
            viewHolder.name.setText(applianceList.get(position).name + " off");

        }

        return rowView;
    }

    public class ViewHolder{
        TextView name;
        ImageView image, status;
    }
}
