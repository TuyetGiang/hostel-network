package com.example.hostelnetwork.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hostelnetwork.AppointmentListAdapter;
import com.example.hostelnetwork.PostDetailActivity;
import com.example.hostelnetwork.PostListAdapter;
import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.AppointmentDTO;
import com.example.hostelnetwork.dto.PostDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatedAppointmentFragment extends Fragment {


    public CreatedAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_created_appointment, container, false);

        List<AppointmentDTO> listData = getListData();
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new AppointmentListAdapter(listData, getActivity()));

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                Object o = listView.getItemAtPosition(position);
//                PostDTO postDTO = (PostDTO) o;
//
//                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//                intent.putExtra("APPOINTMENT_DETAIL", postDTO.getId());
//                startActivity(intent);
//            }
//
//        });

        return view;
    }


    @TargetApi(Build.VERSION_CODES.O)
    private List<AppointmentDTO> getListData() {
        List<AppointmentDTO> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            AppointmentDTO appointmentDTO = new AppointmentDTO();
            appointmentDTO.setStatus(1);
            appointmentDTO.setTime(LocalDateTime.now());
            list.add(appointmentDTO);
        }
        return list;
    }

}
