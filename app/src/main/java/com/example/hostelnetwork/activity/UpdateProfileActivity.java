package com.example.hostelnetwork.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.hostelnetwork.R;

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
    }

    public void updateProfile(View view) {

        Intent intent = new Intent(UpdateProfileActivity.this, HomeActivity.class);
        intent.putExtra("FRAGMENT_ID", R.id.menu_account);
        startActivity(intent);
    }
}
