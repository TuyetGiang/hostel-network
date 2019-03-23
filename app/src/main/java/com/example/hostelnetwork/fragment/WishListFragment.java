package com.example.hostelnetwork.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.activity.LoginActivity;
import com.example.hostelnetwork.activity.PostDetailActivity;
import com.example.hostelnetwork.adapter.WishListAdapter;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.PostModel;
import com.google.gson.Gson;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WishListFragment extends Fragment {


    public WishListFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        SharedPreferences accountPreferences = getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        if (accountPreferences == null || accountPreferences.getString("userInfor", null) == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("FRAGMENT_ID", R.id.menu_wishLists);
            startActivity(intent);
        } else {
            Gson gson = new Gson();
            String json = accountPreferences.getString("userInfor", "");
            UserDTO userDTO = gson.fromJson(json, UserDTO.class);
            //call api get list post saved
            PostModel postModel = new PostModel();
            List<PostDTO> listData = postModel.getAllSavedPost(userDTO.getId());

            final ListView listView = view.findViewById(R.id.listView);


            listView.setAdapter(new WishListAdapter(listData, getActivity()));

            listView.setOnItemClickListener((a, v, position, id) -> {
                Object o = listView.getItemAtPosition(position);
                PostDTO postDTO = (PostDTO) o;

                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                String postJson = new Gson().toJson(postDTO);
                intent.putExtra("POST_DETAIL", postJson);
                intent.putExtra("SAVED_POST", true);
                startActivity(intent);
            });
        }
        return view;
    }
}
