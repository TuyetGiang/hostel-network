package com.example.hostelnetwork.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.adapter.BenefitGridAdapter;
import com.example.hostelnetwork.adapter.ImageSliderAdapter;
import com.example.hostelnetwork.dto.BenefitDTO;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.fragment.NewsFragment;
import com.example.hostelnetwork.model.BenefitModel;
import com.example.hostelnetwork.model.PictureModel;
import com.example.hostelnetwork.model.UserModel;
import com.example.hostelnetwork.model.WishListModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostDetailActivity extends AppCompatActivity {

    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.BLUE;
    private PostDTO postDTO = null;
    private Boolean saved = null;
    private UserDTO userDTO = null;
    private ActionBar toolbar;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        toolbar.setTitle("Chi tiết phòng");
        if (toolbar != null){
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_post_detail);

        loadUser();
        postDTO = new Gson().fromJson(getIntent().getStringExtra("POST_DETAIL"), PostDTO.class);
        if (postDTO != null) {
            loadImage();
            loadDetailPost();
            loadUserPostedInfor();
            loadBenefit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    View createDot(Context context, @ColorInt int color) {
        View dot = new View(context);
        LinearLayout.MarginLayoutParams dotParams = new LinearLayout.MarginLayoutParams(20, 20);
        dotParams.setMargins(20, 10, 20, 10);
        dot.setLayoutParams(dotParams);
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.setTint(color);
        dot.setBackground(drawable);
        return dot;
    }

    public void savedPostDetail(View view) {
        if (userDTO != null) {
            TextView txtSaved = findViewById(R.id.txtStatusSavedPostDetail);
            ImageView imgHeartDetail = findViewById(R.id.imgHeartDetail);
            if (saved) {
                saved = false;
                imgHeartDetail.setColorFilter(getResources().getColor(R.color.color_gray), PorterDuff.Mode.SRC_IN);
                WishListModel wishListModel = new WishListModel();
                wishListModel.deletePostOutOfWishList(userDTO.getId(), postDTO.getId());
                txtSaved.setText("Lưu tin");
                txtSaved.setTextColor(getResources().getColor(R.color.color_gray));
                Toast.makeText(this, "Đã xóa bài viết khỏi danh sách xem sau", Toast.LENGTH_LONG).show();
            } else {
                saved = true;
                imgHeartDetail.setColorFilter(getResources().getColor(R.color.heart_saved), PorterDuff.Mode.SRC_IN);
                WishListModel wishListModel = new WishListModel();
                wishListModel.addPostToWishList(userDTO.getId(), postDTO.getId());
                txtSaved.setText("Đã lưu");
                txtSaved.setTextColor(getResources().getColor(R.color.heart_saved));
                Toast.makeText(this, "Đã lưu bài viết để xem sau", Toast.LENGTH_LONG).show();
            }
        }else {
            Intent intent = new Intent(PostDetailActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

    private void loadUser() {
        SharedPreferences accountPreferences = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        if (accountPreferences != null && accountPreferences.getString("userInfor", null) != null) {
            Gson gson = new Gson();
            String json = accountPreferences.getString("userInfor", "");
            userDTO = gson.fromJson(json, UserDTO.class);
        }
    }

    //get List image of post
    private void loadImage() {
        PictureModel pictureModel = new PictureModel();
        List<String> listImg = pictureModel.getImgLinkOfPost(postDTO.getId());
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(listImg);
        ViewPager imagePostSlider = findViewById(R.id.imgPostSlider);
        imagePostSlider.setAdapter(sliderAdapter);

        LinearLayout indicator = findViewById(R.id.indicator);
        for (int i = 0; i < listImg.size(); i++) {
            View dot = createDot(indicator.getContext(), i == 0 ? COLOR_ACTIVE : COLOR_INACTIVE);
            indicator.addView(dot);
        }
        imagePostSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < listImg.size(); i++) {
                    indicator.getChildAt(i).getBackground().mutate().setTint((i == position ? COLOR_ACTIVE : COLOR_INACTIVE));
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void loadUserPostedInfor() {
        UserModel userModel = new UserModel();
        UserDTO userPosted = userModel.getUserById(postDTO.getUserId());

        ImageView imgAvatar = findViewById(R.id.imgAvatarUserPostDetail);
        TextView txtUserName = findViewById(R.id.txtNameUserPostDetail);
        TextView txtPhone = findViewById(R.id.txtPhonePostDetail);
        TextView txtAddressUser = findViewById(R.id.txtAddressPostDetail);

        if (userPosted.getImgAvatar() != null) {
            Picasso.with(this).load(userPosted.getImgAvatar()).into(imgAvatar);
        }
        txtUserName.setText(userPosted.getFullname());
        txtPhone.setText(userPosted.getPhone());
        txtAddressUser.setText(userPosted.getAddress());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void loadDetailPost() {
        TextView txtTypePost = findViewById(R.id.txtTypePostDetail);
        TextView txtTitle = findViewById(R.id.txtTitlePostDetail);
        TextView txtPrice = findViewById(R.id.txtPricePostDetail);
        TextView txtSaved = findViewById(R.id.txtStatusSavedPostDetail);
        ImageView imgHeartDetail = findViewById(R.id.imgHeartDetail);
        TextView txtContent = findViewById(R.id.txtNotePostDetail);
        TextView txtDeposit = findViewById(R.id.txtDepositPostDetail);
        TextView txtLocation = findViewById(R.id.txtLocationPostDetail);
        TextView txtAre = findViewById(R.id.txtAreaPostDetail);

        txtTypePost.setText(postDTO.getTypeStr().toUpperCase());
        txtTitle.setText(postDTO.getTitle());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        txtPrice.setText(nf.format(postDTO.getPrice()));
        txtContent.setText(postDTO.getContent());
        txtDeposit.setText(nf.format(Optional.ofNullable(postDTO.getDeposit()).orElse(0D)));
        txtLocation.setText(postDTO.getLocation());
        if (postDTO.getArea() != null) {
            txtAre.setText(postDTO.getArea().toString() + " m2");
        }
        saved = getIntent().getBooleanExtra("SAVED_POST", false);
        if (userDTO != null && saved) {
            txtSaved.setText("Đã lưu");
            imgHeartDetail.setColorFilter(getResources().getColor(R.color.heart_saved), PorterDuff.Mode.SRC_IN);
            txtSaved.setTextColor(getResources().getColor(R.color.heart_saved));
        }
    }

    private void loadBenefit() {
        BenefitModel benefitModel = new BenefitModel();
        List<BenefitDTO> benefitDTOList = benefitModel.getListBenefitOfPost(postDTO.getId());

        final GridView gridView = (findViewById(R.id.gridbenefitPostDetail));
        gridView.setAdapter(new BenefitGridAdapter(PostDetailActivity.this, benefitDTOList));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
