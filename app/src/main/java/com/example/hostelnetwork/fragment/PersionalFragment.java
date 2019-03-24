package com.example.hostelnetwork.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.activity.LoginActivity;
import com.example.hostelnetwork.activity.UpdateProfileActivity;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.AccountManager;
import com.example.hostelnetwork.model.UserModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Optional;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersionalFragment extends Fragment implements View.OnClickListener {


    public PersionalFragment() {
        // Required empty public constructor
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persional, container, false);

        SharedPreferences accountPreferences = getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        if (accountPreferences == null || accountPreferences.getString("userInfor",null) == null ){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("FRAGMENT_ID", R.id.menu_account);
            startActivity(intent);
        }else {
            TextView txtFullName = view.findViewById(R.id.txtFullName);
            TextView txtAmount = view.findViewById(R.id.txtTotal);
            TextView txtEmail = view.findViewById(R.id.txtEmail);
            TextView txtPhone = view.findViewById(R.id.txtPhone);
            TextView txtAddress = view.findViewById(R.id.txtAddress);
            ImageView imgAvatar = view.findViewById(R.id.userAvatar);

            Gson gson = new Gson();
            String json = accountPreferences.getString("userInfor", "");
            UserDTO userDTO = gson.fromJson(json, UserDTO.class);

            txtFullName.setText(userDTO.getFullname());
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            txtAmount.setText(nf.format(Optional.ofNullable(userDTO.getAmount()).orElse(0L)));
            txtEmail.setText(userDTO.getEmail());
            txtPhone.setText(userDTO.getPhone());
            txtAddress.setText(userDTO.getAddress());
            Picasso.with(getActivity()).load(userDTO.getImgAvatar()).into(imgAvatar);


            Button btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
            btnUpdateProfile.setOnClickListener(this);
            Button btnLogout = view.findViewById(R.id.btnLogout);
            btnLogout.setOnClickListener(this);
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateProfile:
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.no_change, R.anim.no_change);
                break;
            case R.id.btnLogout:
                SharedPreferences preferences = getActivity().getSharedPreferences("ACCOUNT",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                intent1.putExtra("FRAGMENT_ID", R.id.menu_account);
                startActivity(intent1);
                break;
        }
    }
}
