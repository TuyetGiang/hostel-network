package com.example.hostelnetwork;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostelnetwork.dto.AppointmentDTO;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class AppointmentListAdapter extends BaseAdapter {
    private List<AppointmentDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public AppointmentListAdapter(List<AppointmentDTO> listData, Context context) {
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
        AppointmentListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.appointment_item_layout, null);
            holder = new AppointmentListAdapter.ViewHolder();
            holder.imgAppointmentView = convertView.findViewById(R.id.imgUserAppointment);
            holder.nameView = convertView.findViewById(R.id.txtAppointmentName);
            holder.phoneView = convertView.findViewById(R.id.txtAppointentPhone);
            holder.timeView = convertView.findViewById(R.id.txtAppointmentTime);
            holder.titlePostView = convertView.findViewById(R.id.txtPostTitle);
            holder.statusView = convertView.findViewById(R.id.txtAppointmentStatus);
            convertView.setTag(holder);
        } else {
            holder = (AppointmentListAdapter.ViewHolder) convertView.getTag();
        }

        final AppointmentDTO appointmentDTO = this.listData.get(position);
        //call api get user by renter id
        UserDTO userDTO = getUserDTO();
        holder.nameView.setText(userDTO.getFullname());
        holder.phoneView.setText(userDTO.getPhone());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm");
        holder.timeView.setText("12/03/2019 14:30");
        //call api get ppost by post_id
        PostDTO postDTO = getPostDTO();
        holder.titlePostView.setText(postDTO.getTitle());
        String txtStatus = "Đang chờ";
        if (appointmentDTO.getStatus() != null) {
            if (appointmentDTO.getStatus() == 1) {
                txtStatus = "Đã chấp nhận";
            } else if (appointmentDTO.getStatus() == 2) {
                txtStatus = "Đã bị từ chối";
            }
        }
        holder.statusView.setText(txtStatus);

        String url = "https://imgur.com/Cl7BDlV.jpg";
        Picasso.with(context).load(url).into(holder.imgAppointmentView);
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

    private UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setFullname("Nguyễn Phương Linh");
        userDTO.setPhone("0123456679965");
        return userDTO;
    }

    private PostDTO getPostDTO() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Phòng trọ mới xây giá rẻ Quận 12");
        return postDTO;
    }

    static class ViewHolder {
        ImageView imgAppointmentView;
        TextView nameView;
        TextView phoneView;
        TextView timeView;
        TextView titlePostView;
        TextView statusView;


    }

}
