package com.example.hostelnetwork.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.activity.UpdateProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersionalFragment extends Fragment implements View.OnClickListener {


    public PersionalFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persional, container, false);
        Button btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        btnUpdateProfile.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateProfile:
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
                break;
        }
    }
}
