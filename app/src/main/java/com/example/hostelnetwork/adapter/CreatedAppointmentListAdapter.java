package com.example.hostelnetwork.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.AppointmentDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.UserModel;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class CreatedAppointmentListAdapter extends BaseAdapter {
    private List<AppointmentDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private Boolean created;

    public CreatedAppointmentListAdapter(List<AppointmentDTO> listData, Boolean created, Context context) {
        this.listData = listData;
        this.context = context;
        this.created = created;
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
        CreatedAppointmentListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.appointment_item_layout, null);
            holder = new CreatedAppointmentListAdapter.ViewHolder();
            holder.imgAppointmentView = convertView.findViewById(R.id.imgUserAppointment);
            holder.nameView = convertView.findViewById(R.id.txtAppointmentName);
            holder.phoneView = convertView.findViewById(R.id.txtAppointentPhone);
            holder.timeView = convertView.findViewById(R.id.txtAppointmentTime);
            holder.addressAppointmentView = convertView.findViewById(R.id.txtAddressAppointment);
            holder.statusView = convertView.findViewById(R.id.txtAppointmentStatus);
            convertView.setTag(holder);
        } else {
            holder = (CreatedAppointmentListAdapter.ViewHolder) convertView.getTag();
        }

        final AppointmentDTO appointmentDTO = this.listData.get(position);

        holder.nameView.setText(appointmentDTO.getUserInfor().getFullname());
        holder.phoneView.setText(appointmentDTO.getUserInfor().getPhone());
        holder.addressAppointmentView.setText(appointmentDTO.getAddressAppointment());
        Picasso.with(context).load(appointmentDTO.getUserInfor().getImgAvatar()).into(holder.imgAppointmentView);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm");
        holder.timeView.setText(appointmentDTO.getTime());
        if (appointmentDTO.getStatus() != null) {
            if (appointmentDTO.getStatus() == 0) {
                holder.statusView.setText("Đang chờ...");
            } else if (appointmentDTO.getStatus() == 1) {
                holder.statusView.setText("Đã chấp nhận");
                holder.statusView.setTextColor(Color.parseColor("#009944"));
            } else if (appointmentDTO.getStatus() == 2) {
                holder.statusView.setText("Đã bị từ chối");
                holder.statusView.setTextColor(Color.parseColor("#ff0000"));
            }
        }
        return convertView;
    }


    static class ViewHolder {
        ImageView imgAppointmentView;
        TextView nameView;
        TextView phoneView;
        TextView timeView;
        TextView addressAppointmentView;
        TextView statusView;
    }

}
