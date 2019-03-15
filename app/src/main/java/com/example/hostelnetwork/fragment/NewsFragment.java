package com.example.hostelnetwork.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hostelnetwork.PostListAdapter;
import com.example.hostelnetwork.PostDetailActivity;
import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.PostDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        List<PostDTO> listData = getListData();
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new PostListAdapter(listData, getActivity()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                PostDTO postDTO = (PostDTO) o;

                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra("POST_DETAIL", postDTO.getId());
                startActivity(intent);
            }

        });

        return view;

    }

    private List<PostDTO> getListData() {
        List<PostDTO> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            PostDTO post1 = new PostDTO();
            post1.setImgName("https://imgur.com/7uav2Qg.jpg");
            post1.setTypeStr("KÍ TÚC XÁ");
            post1.setTitle("Kí túc xá Ngọc An");
            post1.setPrice(1500000);
            post1.setLocation("Quận 12, Hồ Chí Minh");
            list.add(post1);
        }
        return list;
    }

}
