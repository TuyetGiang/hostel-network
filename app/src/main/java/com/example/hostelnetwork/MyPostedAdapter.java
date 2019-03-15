package com.example.hostelnetwork;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostelnetwork.dto.PostDTO;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class MyPostedAdapter extends BaseAdapter {
    private List<PostDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public MyPostedAdapter(List<PostDTO> listData, Context context) {
        this.listData = listData;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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

    public View getView(int position, View convertView, ViewGroup parent) {
        PostListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.my_posted_layout, null);
            holder = new PostListAdapter.ViewHolder();
            holder.imgPostView = convertView.findViewById(R.id.imgPost);
            holder.typeView = convertView.findViewById(R.id.txtPostType);
            holder.titleView = convertView.findViewById(R.id.txtPostTitle);
            holder.priceView = convertView.findViewById(R.id.txtPostCost);
            holder.locationView = convertView.findViewById(R.id.txtPostAddress);
            holder.dateView = convertView.findViewById(R.id.txtPostDate);
            convertView.setTag(holder);
        } else {
            holder = (PostListAdapter.ViewHolder) convertView.getTag();
        }

        final PostDTO postDTO = this.listData.get(position);
        holder.typeView.setText(postDTO.getTypeStr());
        holder.titleView.setText(postDTO.getTitle());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        holder.priceView.setText(nf.format(postDTO.getPrice()));
        holder.locationView.setText(postDTO.getLocation());

        String url = "https://imgur.com/7uav2Qg.jpg";
        Picasso.with(context).load(postDTO.getImgName()).into(holder.imgPostView);
        return convertView;
    }

    // Tìm ID của Image ứng với tên của ảnh (Trong thư mục mipmap).
    public int getMipmapResIdByName(String resName) {
        String pkgName = context.getPackageName();

        // Trả về 0 nếu không tìm thấy.
        int resID = context.getResources().getIdentifier(resName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    static class ViewHolder {
        ImageView imgPostView;
        TextView typeView;
        TextView titleView;
        TextView priceView;
        TextView locationView;
        TextView dateView;


    }
}
