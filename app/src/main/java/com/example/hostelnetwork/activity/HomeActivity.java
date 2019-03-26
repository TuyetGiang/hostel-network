package com.example.hostelnetwork.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.fragment.AccountFragment;
import com.example.hostelnetwork.fragment.AppointmentFragment;
import com.example.hostelnetwork.fragment.NewsFragment;
import com.example.hostelnetwork.fragment.WishListFragment;

public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_home);


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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        loadFragment(getFragment(menuItem.getItemId()));
        return true;
    }

    private Fragment getFragment(Integer itemId){
        switch (itemId){
            case R.id.menu_account:
                return new AccountFragment();
            case R.id.menu_appointments:
                return new AppointmentFragment();
            case R.id.menu_wishLists:
                return new WishListFragment();
            case R.id.menu_newss:
                return new NewsFragment();
        }
        return new NewsFragment();
    }
}
