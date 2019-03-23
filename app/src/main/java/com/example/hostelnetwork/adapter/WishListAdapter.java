package com.example.hostelnetwork.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.WishListModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

public class WishListAdapter extends BaseAdapter {
    private List<PostDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public WishListAdapter(List<PostDTO> listData, Context context) {
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
        WishListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.wishlist_item_layout, null);
            holder = new WishListAdapter.ViewHolder();
            holder.imgPostView = convertView.findViewById(R.id.imgPost);
            holder.typeView = convertView.findViewById(R.id.txtPostType);
            holder.titleView = convertView.findViewById(R.id.txtPostTitle);
            holder.priceView = convertView.findViewById(R.id.txtPostCost);
            holder.locationView = convertView.findViewById(R.id.txtPostAddress);
            holder.dateView = convertView.findViewById(R.id.txtPostDate);
            holder.trashView = convertView.findViewById(R.id.btnDeletePostOutOfWishList);
            convertView.setTag(holder);
        } else {
            holder = (WishListAdapter.ViewHolder) convertView.getTag();
        }

        final PostDTO postDTO = this.listData.get(position);
        Picasso.with(context).load(postDTO.getImgLinkPoster()).into(holder.imgPostView);
        holder.typeView.setText(postDTO.getTypeStr().toUpperCase());
        holder.titleView.setText(postDTO.getTitle());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        holder.priceView.setText(nf.format(postDTO.getPrice()));
        holder.locationView.setText(postDTO.getLocation());
        holder.dateView.setText(postDTO.getPostDate());


        holder.trashView.setOnClickListener(v -> {
            SharedPreferences accountPreferences = context.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
            if (accountPreferences != null || accountPreferences.getString("userInfor", null) != null) {
                Gson gson = new Gson();
                String json = accountPreferences.getString("userInfor", "");
                UserDTO userDTO = gson.fromJson(json, UserDTO.class);
                WishListModel wishListModel = new WishListModel();
                wishListModel.deletePostOutOfWishList(userDTO.getId(), postDTO.getId());
                Toast.makeText(context, "Đã xóa bài viết khỏi danh sách xem sau", Toast.LENGTH_LONG).show();
                listData.remove(postDTO);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView imgPostView;
        TextView typeView;
        TextView titleView;
        TextView priceView;
        TextView locationView;
        ImageView trashView;
        TextView dateView;
    }
}
