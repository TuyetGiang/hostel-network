package com.example.hostelnetwork.fragment;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.activity.FilterActivity;
import com.example.hostelnetwork.activity.LoginActivity;
import com.example.hostelnetwork.activity.PostDetailActivity;
import com.example.hostelnetwork.activity.WritePostActivity;
import com.example.hostelnetwork.adapter.PostListAdapter;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.PostModel;
import com.example.hostelnetwork.model.WishListModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    //    private RequestQueue queue;
    private List<Integer> listSavedPostIdOfUser = null;
    private UserDTO currentAcount;


    public NewsFragment() {
        // Required empty public constructor
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

//        queue = Volley.newRequestQueue(getContext());

        //call api getAll
        List<PostDTO> listData = getListData();

        WishListModel wishListModel = new WishListModel();
        SharedPreferences accountPreferences = getActivity().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        if (accountPreferences != null && accountPreferences.getString("userInfor", null) != null) {
            Gson gson = new Gson();
            String json = accountPreferences.getString("userInfor", "");
            currentAcount = gson.fromJson(json, UserDTO.class);
            listSavedPostIdOfUser = wishListModel.getAllSavedPostId(currentAcount.getId());
        }

        final ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(new PostListAdapter(listData, listSavedPostIdOfUser, getActivity()));
//        listView.setSelector(R.drawable.item_state_press);
        listView.setOnItemClickListener((a, v, position, id) -> {
//            v.setBackgroundResource(R.drawable.item_state_press);
            Object o = listView.getItemAtPosition(position);
            PostDTO postDTO = (PostDTO) o;

            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            String postJson = new Gson().toJson(postDTO);
            intent.putExtra("POST_DETAIL", postJson);
            intent.putExtra("SAVED_POST", saved(listSavedPostIdOfUser, postDTO.getId()));
            startActivity(intent);
        });


        ImageView imgFilter = view.findViewById(R.id.imgFilterPost);
        imgFilter.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FilterActivity.class);
            intent.putExtra("FILTERED", true);
            String location = getActivity().getIntent().getStringExtra("LOCATION");
            String typeId = getActivity().getIntent().getStringExtra("TYPE_ID");
            String minPrice = getActivity().getIntent().getStringExtra("MIN_PRICE");
            String maxPrice = getActivity().getIntent().getStringExtra("MAX_PRICE");
            String jsonBenefit = getActivity().getIntent().getStringExtra("LIST_BENEFIT");

            if(location != null){
                intent.putExtra("LOCATION", location);
            }
            if(typeId != null){
                intent.putExtra("TYPE_ID", typeId);
            }
            if(minPrice != null){
                intent.putExtra("MIN_PRICE", minPrice);
            }
            if(maxPrice != null){
                intent.putExtra("MAX_PRICE", maxPrice);
            }
            if(jsonBenefit != null){
                intent.putExtra("LIST_BENEFIT", jsonBenefit);
            }
            startActivity(intent);
        });

        ImageView imgWritePost = view.findViewById(R.id.imgWritePost);
        imgWritePost.setOnClickListener(v -> {
            if (currentAcount == null) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("FRAGMENT_ID", R.id.menu_newss);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), WritePostActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }

    private Boolean saved(List<Integer> savedList, Integer postId) {
        return savedList != null && savedList.contains(postId);
    }

    private List<PostDTO> getListData() {
        List<PostDTO> listData = null;
        PostModel postModel = new PostModel();
        Boolean isFilter = getActivity().getIntent().getBooleanExtra("FILTERED", false);
        if (isFilter) {

            String location = getActivity().getIntent().getStringExtra("LOCATION");
            String typeId = getActivity().getIntent().getStringExtra("TYPE_ID");
            String minPrice = getActivity().getIntent().getStringExtra("MIN_PRICE");
            String maxPrice = getActivity().getIntent().getStringExtra("MAX_PRICE");
            String jsonBenefit = getActivity().getIntent().getStringExtra("LIST_BENEFIT");

            List<Integer> listBenefit = null;
            if (jsonBenefit != null) {
                TypeToken<List<Integer>> typeToken = new TypeToken<List<Integer>>() {
                };
                listBenefit = new Gson().fromJson(jsonBenefit, typeToken.getType());
            }

            listData = postModel.filterPost(listBenefit, location, typeId, minPrice, maxPrice);


        } else {
            listData = postModel.getAllPost();
        }
        return listData;
    }

}
