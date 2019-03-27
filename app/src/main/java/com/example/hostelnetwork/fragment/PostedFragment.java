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
import com.example.hostelnetwork.activity.LoginActivity;
import com.example.hostelnetwork.activity.PostDetailActivity;
import com.example.hostelnetwork.adapter.MyPostedAdapter;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.PostModel;
import com.google.gson.Gson;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostedFragment extends Fragment {


    public PostedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posted, container, false);

        SharedPreferences accountPreferences = getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        if (accountPreferences == null || accountPreferences.getString("userInfor", null) == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("FRAGMENT_ID", R.id.menu_account);
            startActivity(intent);
        } else {
            Gson gson = new Gson();
            String json = accountPreferences.getString("userInfor", "");
            UserDTO userDTO = gson.fromJson(json, UserDTO.class);

            PostModel postModel = new PostModel();
            List<PostDTO> listData = postModel.getAllCreatedPost(userDTO.getId());
            final ListView listView = view.findViewById(R.id.listView);

            listView.setAdapter(new MyPostedAdapter(listData, getActivity()));

            listView.setOnItemClickListener((a, v, position, id) -> {
                Object o = listView.getItemAtPosition(position);
                PostDTO postDTO = (PostDTO) o;

                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra("POST_DETAIL", new Gson().toJson(postDTO));
                startActivity(intent);
            });
        }
        return view;

    }

}
