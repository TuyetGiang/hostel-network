package com.example.hostelnetwork.model;

import android.content.Context;
import android.os.StrictMode;

import com.example.hostelnetwork.constant.LocaleData;
import com.example.hostelnetwork.dto.UserDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class AccountManager {
    Gson gson = new Gson();

    public UserDTO checkLogin(String username, String password) {
        UserDTO userDTO = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.AUTHENCATION_CHECKLOGIN_URL + "username=" + username + "&password=" + password);

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

    public Boolean logout(Context context){
        String filePath = context.getFilesDir().getParent()+"/shared_prefs/ACCOUNT.xml";
        File deletePrefFile = new File(filePath );
        deletePrefFile.delete();
        return true;
    }
}
