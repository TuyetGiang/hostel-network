package com.example.hostelnetwork.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.AccountManager;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            AccountManager accountManager = new AccountManager();

            EditText edtPhone = findViewById(R.id.edtPhone);
            EditText edtPassword = findViewById(R.id.edtPassword);
            TextView txtInvalid = findViewById(R.id.txtInvalid);

            if (validateLogin(edtPhone, edtPassword)) {
                UserDTO userDTO = accountManager.checkLogin(edtPhone.getText().toString(), edtPassword.getText().toString());
                if (userDTO == null) {
                    txtInvalid.setText("Số điện thoại và mật khẩu không đúng!");
                } else {
                    txtInvalid.setText("");
                    saveAccountLogin(userDTO);
                    Integer lastFragmentId = getIntent().getIntExtra("FRAGMENT_ID", R.id.menu_newss);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("FRAGMENT_ID", lastFragmentId);
                    startActivity(intent);
                }

            }
        });
    }


    public void goToRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        Integer lastFragmentId = getIntent().getIntExtra("FRAGMENT_ID", R.id.menu_newss);
        intent.putExtra("FRAGMENT_ID", lastFragmentId);
        startActivity(intent);
    }

    public void closeLogin(View view) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.putExtra("FRAGMENT_ID", R.id.menu_newss);
        startActivity(intent);
    }

    private boolean validateLogin(EditText edtPhone, EditText edtPassword) {
        if (edtPhone == null || edtPhone.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Số điện thoại không được trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtPassword == null || edtPassword.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Mật khẩu không được trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveAccountLogin(UserDTO userDTO) {
        SharedPreferences accountPreferences = this.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(userDTO);
        editor.putString("userInfor", json);
        editor.apply();
    }

}
