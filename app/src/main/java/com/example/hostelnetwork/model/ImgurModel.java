package com.example.hostelnetwork.model;

import android.graphics.Bitmap;
import android.os.StrictMode;

import com.example.hostelnetwork.constant.LocaleData;
import com.example.hostelnetwork.dto.PostDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ImgurModel {
    Gson gson = new Gson();

    public String uploadImage (byte[] imgByte){
        String imgLink = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(LocaleData.IMGUR_API);

            httpPost.setHeader("Authorization","Client-ID e602b3da5e3f464");
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            HttpEntity params = new ByteArrayEntity(imgByte);
            httpPost.setEntity(params);


            HttpResponse response = httpclient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    String str = json.substring(json.lastIndexOf("https:\\"));
                    imgLink = str.substring(0, str.indexOf("\""));
                   return imgLink;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
