package com.example.hostelnetwork.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostelnetwork.R;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button btnChagePassword = findViewById(R.id.btnChangePassword);
        btnChagePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edtOldPassword = findViewById(R.id.edtOldPassword);
                EditText edtNewPassword = findViewById(R.id.edtNewPasword);
                EditText edtRePassword = findViewById(R.id.edtReNewPassword);
                //call api change password

                Toast.makeText(ChangePasswordActivity.this,"Thay đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
                intent.putExtra("FRAGMENT_ID", R.id.menu_account);
                startActivity(intent);
            }
        });
    }

    public void closeLogin(View view) {
        Intent intent = new Intent(ChangePasswordActivity.this, HomeActivity.class);
        intent.putExtra("FRAGMENT_ID", R.id.menu_account);
        startActivity(intent);
    }
}
