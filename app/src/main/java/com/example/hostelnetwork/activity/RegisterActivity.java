package com.example.hostelnetwork.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.UserModel;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);
    }

    public void register(View view) {
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtPassword = findViewById(R.id.edtPasswordRegister);
        EditText edtRepassword = findViewById(R.id.edtRePassword);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtAddress = findViewById(R.id.edtAddress);
        EditText edtFullname = findViewById(R.id.edtFullname);
        TextView txtInvalid = findViewById(R.id.invalidRegister);

        if(checkDataInput()){
//call api resgister
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(edtPhone.getText().toString());
            userDTO.setPhone(edtPhone.getText().toString());
            userDTO.setPassword(edtPassword.getText().toString());
            userDTO.setEmail(edtEmail.getText().toString());
            userDTO.setAddress(edtAddress.getText().toString());
            userDTO.setFullname(edtFullname.getText().toString());
            userDTO.setRegistDate(DateFormat.format("dd/MM/yyyy", (new Date())).toString());

            UserModel userModel = new UserModel();
            userDTO = userModel.createNewUser(userDTO);
            if (userDTO != null){
                saveAccountRegister(userDTO);
                Toast.makeText(this,"Tạo tài khoản thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                Integer lastFragmentId = getIntent().getIntExtra("FRAGMENT_ID", R.id.menu_newss);
                intent.putExtra("FRAGMENT_ID", lastFragmentId);
                startActivity(intent);
            }else {
                Toast.makeText(this,"Tạo tài khoản thất bại",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkDataInput() {
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtPassword = findViewById(R.id.edtPasswordRegister);
        EditText edtRepassword = findViewById(R.id.edtRePassword);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtAddress = findViewById(R.id.edtAddress);
        EditText edtFullname = findViewById(R.id.edtFullname);
        TextView txtInvalid = findViewById(R.id.invalidRegister);
        if (edtPhone.getText().toString().equals("") || edtPassword.getText().toString().equals("") || edtRepassword.getText().toString().equals("")
                || edtEmail.getText().toString().equals("") || edtAddress.getText().toString().equals("") || edtFullname.getText().toString().equals("")) {
            txtInvalid.setText("Yêu cầu  nhập đầy đủ thông tin để tạo tài khoản");
            return  false;
        }else if (!edtPassword.getText().toString().equals(edtRepassword.getText().toString())){
            txtInvalid.setText("Mật khẩu nhập lại phải đúng với mật khẩu");
            return false;
        }
        return true;
    }

    public void closeRegister(View view) {
        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
        intent.putExtra("FRAGMENT_ID", R.id.menu_newss);
        startActivity(intent);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        Integer lastFragmentId = getIntent().getIntExtra("FRAGMENT_ID", R.id.menu_newss);
        intent.putExtra("FRAGMENT_ID", lastFragmentId);
        startActivity(intent);
    }

    private void saveAccountRegister(UserDTO userDTO) {
        SharedPreferences accountPreferences = this.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(userDTO);
        editor.putString("userInfor", json);
        editor.apply();
    }
}
