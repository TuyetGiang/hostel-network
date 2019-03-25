package com.example.hostelnetwork.model;

import android.os.StrictMode;

import com.example.hostelnetwork.constant.LocaleData;
import com.example.hostelnetwork.dto.AppointmentDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpDelete;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class AppointmentModel {
    Gson gson = new Gson();

    public List<AppointmentDTO> getAllAppointment(Integer userId, Boolean created) {
        List<AppointmentDTO> data = new ArrayList<>();
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(LocaleData.APPOINTMENT_GETALL_CREATED_URL + created + "&userId=" + userId);

            HttpResponse response = httpclient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    TypeToken<List<AppointmentDTO>> typeToken = new TypeToken<List<AppointmentDTO>>() {};
                    data = gson.fromJson(json, typeToken.getType());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public AppointmentDTO createNewAppoinment(AppointmentDTO dto){
        AppointmentDTO result = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(LocaleData.APPOINTMENT_CREATE_URL);

            StringEntity params = new StringEntity(gson.toJson(dto),"UTF-8");
            httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(params);

            HttpResponse response = httpclient.execute(httpPost);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    result = gson.fromJson(json, AppointmentDTO.class);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public AppointmentDTO updateStatusAppointment(Integer id, Integer status) {
        AppointmentDTO result = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(LocaleData.APPOINTMENT_UPDATE_URL + id + "?status=" + status);

            HttpResponse response = httpclient.execute(httpPut);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    result = gson.fromJson(json, AppointmentDTO.class);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Boolean deleteAppointment(Integer id) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpDelete httpDelete = new HttpDelete(LocaleData.APPOINTMENT_UPDATE_URL + id);

            HttpResponse response = httpclient.execute(httpDelete);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            if (LocaleData.HandleErrorMessageResponse(response.getStatusLine().getStatusCode())) {
                if (json != null) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
