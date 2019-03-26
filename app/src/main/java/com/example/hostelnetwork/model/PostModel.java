package com.example.hostelnetwork.model;

import android.net.Uri;
import android.os.StrictMode;

import com.example.hostelnetwork.constant.LocaleData;
import com.example.hostelnetwork.dto.PostDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class PostModel {
    Gson gson = new Gson();

    public List<PostDTO> getAllPost() {
        List<PostDTO> data = new ArrayList<>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.POST_GETALL);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    TypeToken<List<PostDTO>> typeToken = new TypeToken<List<PostDTO>>() {
                    };
                    data = gson.fromJson(json, typeToken.getType());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public List<PostDTO> getAllSavedPost(Integer userId) {
        List<PostDTO> data = new ArrayList<>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.POST_SAVED_BY_USER_URL + userId);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    TypeToken<List<PostDTO>> typeToken = new TypeToken<List<PostDTO>>() {
                    };
                    data = gson.fromJson(json, typeToken.getType());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public List<PostDTO> getAllCreatedPost(Integer userId) {
        List<PostDTO> data = new ArrayList<>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.POST_CREATED_BY_USER_URL + userId);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    TypeToken<List<PostDTO>> typeToken = new TypeToken<List<PostDTO>>() {
                    };
                    data = gson.fromJson(json, typeToken.getType());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public PostDTO getPostById(Integer postId) {
        PostDTO postDTO = new PostDTO();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.POST_BY_ID_URL + postId);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    postDTO = gson.fromJson(json, PostDTO.class);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return postDTO;
    }

    public List<PostDTO> filterPost(List<Integer> benefitIds, String location, String typeId, String minPrice, String maxPrice) {
        List<PostDTO> data = new ArrayList<>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            String params = "";
            if (location != null) {
                params += "&location=" + Uri.encode(location);
            }
            if (minPrice != null) {
                params += "&minPrice=" + minPrice;

            }
            if (maxPrice != null) {
                params += "&maxPrice=" + maxPrice;

            }
            if (typeId != null) {
                params += "&type=" + typeId;

            }
            if (benefitIds.size() > 0) {
                for (int i = 0; i < benefitIds.size(); i++) {
                    params += "&benefits=" + benefitIds.get(i).toString();
                }
            }
            String uri = LocaleData.POST_GETALL;
            if (!params.equals("")) {
                uri = uri + "?" + params;
            }
            HttpGet httpGet = new HttpGet(uri);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    TypeToken<List<PostDTO>> typeToken = new TypeToken<List<PostDTO>>() {
                    };
                    data = gson.fromJson(json, typeToken.getType());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
    public PostDTO createNewAppoinment(PostDTO dto){
        PostDTO result = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(LocaleData.POST_GETALL);

            StringEntity params = new StringEntity(gson.toJson(dto),"UTF-8");
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(params);

            HttpResponse response = httpclient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    result = gson.fromJson(json, PostDTO.class);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
