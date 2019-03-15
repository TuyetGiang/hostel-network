package com.example.hostelnetwork.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.fragment.AccountFragment;
import com.example.hostelnetwork.fragment.AppointmentFragment;
import com.example.hostelnetwork.fragment.NewsFragment;
import com.example.hostelnetwork.fragment.WishListFragment;

public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));

        setContentView(R.layout.activity_home);
//        Button btnLogin = findViewById(R.id.btnTest);
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this, Main2Activity.class);
//                startActivity(intent);
//            }
//        });

        Integer fragmentId = getIntent().getIntExtra("FRAGMENT_ID", R.id.menu_newss);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigations);
        navigation.setSelectedItemId(fragmentId);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(getFragment(fragmentId));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setTitle(String title) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(title);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(20);
        textView.setTextAppearance(this, R.style.textStyleBold);


        toolbar.setDisplayShowCustomEnabled(true);
        toolbar.setCustomView(textView);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        loadFragment(getFragment(menuItem.getItemId()));
        return true;
    }

    private Fragment getFragment(Integer itemId){
        switch (itemId){
            case R.id.menu_account:
                setTitle("Tài khoản");
                return new AccountFragment();
            case R.id.menu_appointments:
                setTitle("Lịch hẹn");
                return new AppointmentFragment();
            case R.id.menu_wishLists:
                setTitle("Đã lưu");
                return new WishListFragment();
            case R.id.menu_newss:
                setTitle("Bảng tin");
                return new NewsFragment();
        }
        return new NewsFragment();
    }
}
