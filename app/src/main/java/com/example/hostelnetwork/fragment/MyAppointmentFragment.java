package com.example.hostelnetwork.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.activity.AppointmentDetailActivity;
import com.example.hostelnetwork.activity.LoginActivity;
import com.example.hostelnetwork.adapter.CreatedAppointmentListAdapter;
import com.example.hostelnetwork.dto.AppointmentDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.AppointmentModel;
import com.google.gson.Gson;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAppointmentFragment extends Fragment {


    public MyAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_appointment, container, false);

        SharedPreferences accountPreferences = getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        if (accountPreferences == null || accountPreferences.getString("userInfor", null) == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("FRAGMENT_ID", R.id.menu_appointments);
            startActivity(intent);
        } else {
            Gson gson = new Gson();
            String json = accountPreferences.getString("userInfor", "");
            UserDTO userDTO = gson.fromJson(json, UserDTO.class);

            AppointmentModel appointmentModel = new AppointmentModel();

            List<AppointmentDTO> listData = appointmentModel.getAllAppointment(userDTO.getId(), false);

            final ListView listView = view.findViewById(R.id.listView);
            listView.setAdapter(new CreatedAppointmentListAdapter(listData, false, getActivity()));

            listView.setOnItemClickListener((a, v, position, id) -> {
                Object o = listView.getItemAtPosition(position);
                AppointmentDTO appointmentDTO = (AppointmentDTO) o;

                Intent intent = new Intent(getActivity(), AppointmentDetailActivity.class);
                intent.putExtra("APPOINTMENT_DETAIL", gson.toJson(appointmentDTO));
                startActivity(intent);
            });
        }
        return view;
    }

}
