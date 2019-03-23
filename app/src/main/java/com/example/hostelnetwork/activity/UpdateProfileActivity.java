package com.example.hostelnetwork.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.UserModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class UpdateProfileActivity extends AppCompatActivity {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        TextView textView = new TextView(this);
        textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText("Chỉnh sửa thông tin cá nhân");
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(20);
        textView.setTextAppearance(this, R.style.textStyleBold);


        toolbar = getSupportActionBar();
        toolbar.setDisplayShowCustomEnabled(true);
        toolbar.setCustomView(textView);
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));

        setContentView(R.layout.activity_update_profile);

        UserDTO userDTO = getUserInfor();

        EditText edtFullname = findViewById(R.id.edtFullName);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtAddress = findViewById(R.id.edtAddress);
        ImageView imgAvatarEdit = findViewById(R.id.userAvatarEdit);

        edtFullname.setText(userDTO.getFullname());
        edtEmail.setText(userDTO.getEmail());
        edtAddress.setText(userDTO.getAddress());
        Picasso.with(this).load(userDTO.getImgAvatar()).into(imgAvatarEdit);

    }

    public void updateProfile(View view) {

        UserDTO userDTO = getUserInfor();
        EditText edtFullname = findViewById(R.id.edtFullName);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtAddress = findViewById(R.id.edtAddress);

        userDTO.setFullname(edtFullname.getText().toString());
        userDTO.setAddress(edtAddress.getText().toString());
        userDTO.setEmail(edtEmail.getText().toString());
        //call api update img avatar
        UserModel userModel = new UserModel();
        userDTO = userModel.updateInforUser(userDTO);

        SharedPreferences accountPreferences = this.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userDTO);
        editor.putString("userInfor", json);
        editor.apply();

        Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
        intent.putExtra("FRAGMENT_ID", R.id.menu_account);

        Toast.makeText(getApplicationContext(), "Đã cập nhật thông tin thành công", Toast.LENGTH_LONG).show();
        startActivity(intent);
    }

    private UserDTO getUserInfor() {
        SharedPreferences accountPreferences = this.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = accountPreferences.getString("userInfor", "");
        return gson.fromJson(json, UserDTO.class);
    }
}
