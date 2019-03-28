package com.example.hostelnetwork.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
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

public class PostListAdapter extends BaseAdapter {
    private List<PostDTO> listData;
    private List<Integer> listSavedPostId;
    private LayoutInflater layoutInflater;
    private Context context;

    public PostListAdapter(List<PostDTO> listData, List<Integer> listSavedPostId, Context context) {
        this.listData = listData;
        this.listSavedPostId = listSavedPostId;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.post_item_layout, null);
            holder = new ViewHolder();
            holder.imgPostView = convertView.findViewById(R.id.imgPost);
            holder.typeView = convertView.findViewById(R.id.txtPostType);
            holder.titleView = convertView.findViewById(R.id.txtPostTitle);
            holder.priceView = convertView.findViewById(R.id.txtPostCost);
            holder.locationView = convertView.findViewById(R.id.txtPostAddress);
            holder.dateView = convertView.findViewById(R.id.txtPostDate);
            holder.heartView = convertView.findViewById(R.id.btnSavePostToWishList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PostDTO postDTO = this.listData.get(position);
        Picasso.with(context).load(postDTO.getImgLinkPoster()).into(holder.imgPostView);
        holder.typeView.setText(postDTO.getTypeStr().toUpperCase());
        holder.titleView.setText(postDTO.getTitle());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        holder.priceView.setText(nf.format(postDTO.getPrice()));
        holder.locationView.setText(postDTO.getLocation());
        holder.dateView.setText(postDTO.getPostDate());

        Boolean isSaved = isSavedPost(postDTO.getId());

        if (isSaved != null && isSaved) {
            holder.heartView.setColorFilter(convertView.getResources().getColor(R.color.heart_saved), PorterDuff.Mode.SRC_IN);
        } else {
            holder.heartView.setColorFilter(convertView.getResources().getColor(R.color.color_gray), PorterDuff.Mode.SRC_IN);

        }

        holder.heartView.setOnClickListener(v -> {
            //call api save post to wishList
            SharedPreferences accountPreferences = context.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
            if (accountPreferences != null && accountPreferences.getString("userInfor", null) != null) {
                Gson gson = new Gson();
                String json = accountPreferences.getString("userInfor", "");
                UserDTO userDTO = gson.fromJson(json, UserDTO.class);
                if (isSaved) {
                    holder.heartView.setColorFilter(v.getResources().getColor(R.color.color_gray), PorterDuff.Mode.SRC_IN);
                    WishListModel wishListModel = new WishListModel();
                    wishListModel.deletePostOutOfWishList(userDTO.getId(), postDTO.getId());
                    Toast.makeText(context, "Đã xóa bài viết khỏi danh sách xem sau", Toast.LENGTH_LONG).show();
                    listSavedPostId.remove(postDTO.getId());
                    notifyDataSetChanged();
                } else {
                    holder.heartView.setColorFilter(v.getResources().getColor(R.color.heart_saved), PorterDuff.Mode.SRC_IN);
                    WishListModel wishListModel = new WishListModel();
                    wishListModel.addPostToWishList(userDTO.getId(), postDTO.getId());
                    Toast.makeText(context, "Đã lưu bài viết để xem sau", Toast.LENGTH_LONG).show();
                    listSavedPostId.add(postDTO.getId());
                    notifyDataSetChanged();
                }
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
        ImageView heartView;
        TextView dateView;
    }

    private Boolean isSavedPost(Integer postId) {
        if (listSavedPostId != null) {
            if (listSavedPostId.contains(postId)) {
                return true;
            }
        }
        return false;
    }
}
