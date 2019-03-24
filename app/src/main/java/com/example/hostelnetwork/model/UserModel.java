package com.example.hostelnetwork.model;

import android.os.StrictMode;

import com.example.hostelnetwork.constant.LocaleData;
import com.example.hostelnetwork.dto.UserDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class UserModel {

    Gson gson = new Gson();

    public UserDTO getUserById(Integer userId) {
        UserDTO userDTO = new UserDTO();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.USER_GET_USER_BY_ID_URL + userId);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    userDTO = gson.fromJson(json, UserDTO.class);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userDTO;
    }

    public UserDTO updateInforUser(UserDTO userDTO){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(LocaleData.USER_UPDATE_URL + userDTO.getId());

            // Add params to put request
            StringEntity params = new StringEntity(gson.toJson(userDTO),"UTF-8");
            httpPut.setHeader("Content-type", "application/json");
            httpPut.setEntity(params);

            HttpResponse response = httpclient.execute(httpPut);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String returnJson = reader.readLine();
            if(LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())){
                if(returnJson != null){
                    userDTO = gson.fromJson(returnJson, UserDTO.class);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userDTO;
    }

    public UserDTO createNewUser(UserDTO userDTO){
        UserDTO result = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(LocaleData.USER_CREATE_URL);

            // Add params to put request
            StringEntity params = new StringEntity(gson.toJson(userDTO),"UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(params);

            HttpResponse response = httpclient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String returnJson = reader.readLine();
            if(LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())){
                if(returnJson != null){
                    result = gson.fromJson(returnJson, UserDTO.class);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
