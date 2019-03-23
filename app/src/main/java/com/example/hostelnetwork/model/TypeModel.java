package com.example.hostelnetwork.model;

import android.os.StrictMode;

import com.example.hostelnetwork.constant.LocaleData;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.TypeDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class TypeModel {
    Gson gson = new Gson();

    public List<TypeDTO> getAllType() {
        List<TypeDTO> data = new ArrayList<>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.TYPE_GET_ALL_URL);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    TypeToken<List<TypeDTO>> typeToken = new TypeToken<List<TypeDTO>>() {};
                    data = gson.fromJson(json, typeToken.getType());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
