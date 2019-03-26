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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.hostelnetwork.model.BenefitModel;
import com.example.hostelnetwork.model.PictureModel;
import com.example.hostelnetwork.model.UserModel;
import com.example.hostelnetwork.model.WishListModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;

public class PostDetailActivity extends AppCompatActivity {

    static final int COLOR_INACTIVE = Color.WHITE;
    static final int COLOR_ACTIVE = Color.BLUE;
    private PostDTO postDetail = null;
    private Boolean saved = null;
    private UserDTO currentUser = null;
    private ActionBar toolbar;
    private UserDTO userPosted = null;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        toolbar.setTitle("Chi tiết phòng");
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_post_detail);

        loadUser();
        postDetail = new Gson().fromJson(getIntent().getStringExtra("POST_DETAIL"), PostDTO.class);
        if (postDetail != null) {
            loadImage();
            loadDetailPost();
            loadUserPostedInfor();
            loadBenefit();
        }

        Button btncallPhone = findViewById(R.id.btnCallPhonePostDetail);
        btncallPhone.setOnClickListener(v -> {
            if (userPosted != null && userPosted.getPhone() != null) {
                Intent callIntent = new Intent();
                callIntent.setAction(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + userPosted.getPhone()));
                startActivity(callIntent);
            }
        });

        Button btnMakeAppointment = findViewById(R.id.btnMakeApointmentPostDetail);
        btnMakeAppointment.setOnClickListener(v -> {
            if (currentUser == null) {
                Intent intent = new Intent(PostDetailActivity.this, LoginActivity.class);
                intent.putExtra("POST_DETAIL", new Gson().toJson(postDetail));
                startActivity(intent);
            } else {
                Intent intent = new Intent(PostDetailActivity.this, MakeAppointmentActivity.class);
                intent.putExtra("SAVED_POST", saved);
                intent.putExtra("POST_DETAIL", new Gson().toJson(postDetail));
                intent.putExtra("USER_POSTED", new Gson().toJson(userPosted));
                startActivity(intent);
            }
        });

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
        if (currentUser != null) {
            TextView txtSaved = findViewById(R.id.txtStatusSavedPostDetail);
            ImageView imgHeartDetail = findViewById(R.id.imgHeartDetail);
            if (saved) {
                saved = false;
                imgHeartDetail.setColorFilter(getResources().getColor(R.color.color_gray), PorterDuff.Mode.SRC_IN);
                WishListModel wishListModel = new WishListModel();
                wishListModel.deletePostOutOfWishList(currentUser.getId(), postDetail.getId());
                txtSaved.setText("Lưu tin");
                txtSaved.setTextColor(getResources().getColor(R.color.color_gray));
                Toast.makeText(this, "Đã xóa bài viết khỏi danh sách xem sau", Toast.LENGTH_LONG).show();
            } else {
                saved = true;
                imgHeartDetail.setColorFilter(getResources().getColor(R.color.heart_saved), PorterDuff.Mode.SRC_IN);
                WishListModel wishListModel = new WishListModel();
                wishListModel.addPostToWishList(currentUser.getId(), postDetail.getId());
                txtSaved.setText("Đã lưu");
                txtSaved.setTextColor(getResources().getColor(R.color.heart_saved));
                Toast.makeText(this, "Đã lưu bài viết để xem sau", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent(PostDetailActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }

    private void loadUser() {
        SharedPreferences accountPreferences = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        if (accountPreferences != null && accountPreferences.getString("userInfor", null) != null) {
            Gson gson = new Gson();
            String json = accountPreferences.getString("userInfor", "");
            currentUser = gson.fromJson(json, UserDTO.class);
        }
    }

    //get List image of post
    private void loadImage() {
        PictureModel pictureModel = new PictureModel();
        List<String> listImg = pictureModel.insertPicture(postDetail.getId());
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
        userPosted = userModel.getUserById(postDetail.getUserId());

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

        txtTypePost.setText(postDetail.getTypeStr().toUpperCase());
        txtTitle.setText(postDetail.getTitle());
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        txtPrice.setText(nf.format(postDetail.getPrice()));
        txtContent.setText(postDetail.getContent());
        txtDeposit.setText(nf.format(Optional.ofNullable(postDetail.getDeposit()).orElse(0D)));
        txtLocation.setText(postDetail.getLocation());
        if (postDetail.getArea() != null) {
            txtAre.setText(postDetail.getArea().toString() + " m2");
        }
        saved = getIntent().getBooleanExtra("SAVED_POST", false);
        if (currentUser != null && saved) {
            txtSaved.setText("Đã lưu");
            imgHeartDetail.setColorFilter(getResources().getColor(R.color.heart_saved), PorterDuff.Mode.SRC_IN);
            txtSaved.setTextColor(getResources().getColor(R.color.heart_saved));
        }
    }

    private void loadBenefit() {
        BenefitModel benefitModel = new BenefitModel();
        List<BenefitDTO> benefitDTOList = benefitModel.getListBenefitOfPost(postDetail.getId());

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
