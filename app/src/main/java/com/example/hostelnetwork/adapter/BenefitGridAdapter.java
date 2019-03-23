package com.example.hostelnetwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.BenefitDTO;

import java.util.List;

public class BenefitGridAdapter extends BaseAdapter {

    private List<BenefitDTO> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public BenefitGridAdapter(Context aContext, List<BenefitDTO> listData) {
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
            convertView = layoutInflater.inflate(R.layout.benefit_item_layout, null);
            holder = new ViewHolder();
            holder.iconView = convertView.findViewById(R.id.iconBenefitPostDetail);
            holder.benefitNameView = convertView.findViewById(R.id.txtNameBenefitPostDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BenefitDTO benefit = this.listData.get(position);
        holder.benefitNameView.setText(benefit.getBenefitName());
        holder.iconView.setImageResource(getIconbenefitId(benefit.getId()));

        return convertView;
    }

    private int getIconbenefitId(Integer benefitId) {
        // Trả về 0 nếu không tìm thấy.
        int resID = 0;
        switch (benefitId) {
            case 1:
                resID = R.drawable.wc_icon;
                break;
            case 2:
                resID = R.drawable.bike_icon;
                break;
            case 3:
                resID = R.drawable.wifi_icon;
                break;
            case 4:
                resID = R.drawable.security_icon;
                break;
            case 5:
                resID = R.drawable.kitchen_icon;
                break;

            case 6:
                resID = R.drawable.time_icon;
                break;
            case 7:
                resID = R.drawable.water_icon;
                break;
            case 8:
                resID = R.drawable.mezzanine_icon;
                break;
            case 9:
                resID = R.drawable.bed_icon;
                break;
            case 10:
                resID = R.drawable.mattress_icon;
                break;
            case 11:
                resID = R.drawable.cabinet_icon;
                break;
            case 12:
                resID = R.drawable.window_icon;
                break;
            case 13:
                resID = R.drawable.air_condition_icon;
                break;
            case 14:
                resID = R.drawable.water_heater_icon;
                break;
            case 15:
                resID = R.drawable.free_key_icon;
                break;
        }
        return resID;
    }

    static class ViewHolder {
        ImageView iconView;
        TextView benefitNameView;
    }
}
