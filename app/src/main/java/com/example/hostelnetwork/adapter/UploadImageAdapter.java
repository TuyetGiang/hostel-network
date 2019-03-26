package com.example.hostelnetwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.hostelnetwork.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadImageAdapter extends BaseAdapter {

    private List<String> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public UploadImageAdapter(Context aContext, List<String> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.image_item_add_layout, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.itemImageUpload);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(listData.get(position)).into(holder.imageView);
        return convertView;
    }


    static class ViewHolder {
        ImageView imageView;
    }
}
