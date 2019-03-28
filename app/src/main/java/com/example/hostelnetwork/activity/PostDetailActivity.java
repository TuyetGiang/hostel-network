package com.example.hostelnetwork.activity;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.hostelnetwork.model.PostModel;
import com.example.hostelnetwork.model.UserModel;
import com.example.hostelnetwork.model.WishListModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    static EditText dueDate;

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

        if (currentUser != null && currentUser.getId().equals(userPosted.getId())) {
            myPost();
        } else {
            findViewById(R.id.tagButtonMyPost).setVisibility(View.INVISIBLE);
            findViewById(R.id.tagButtonBottom).setVisibility(View.VISIBLE);
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

    private void refresh() {
        finish();
        getIntent().putExtra("POST_DETAIL", new Gson().toJson(postDetail));
        getIntent().putExtra("SAVED_POST", saved);
        startActivity(getIntent());
        overridePendingTransition(R.anim.no_change, R.anim.no_change);
    }

    private void pushPost(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Long pay = 0L;
        long days = 0;
        try {
            days = (sdf.parse(postDetail.getDueDate()).getTime() - new Date().getTime()) / 1000L / 60L / 60L / 24L + 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (days > 0) {
            if (currentUser.getAmount() == null || currentUser.getAmount() != null && currentUser.getAmount() < pay) {
                new android.app.AlertDialog.Builder(this).setTitle("Thông báo")
                        .setMessage("Bạn không đủ tiền để đẩy tin. Cần ít nhất " + pay + " đồng trong tài khoản. Liên hệ với Hotel.Net để nạp thêm.")
                        .setCancelable(true)
                        .setNeutralButton("Ok", (dialog, which) -> {
                        }).show();
            } else {
                pay += days * 1500;
                new AlertDialog.Builder(this).setTitle("Xác nhận đẩy tin")
                        .setMessage("Đẩy tin đến ngày " + postDetail.getDueDate() + " với giá " + pay)
                        .setPositiveButton("Đồng ý", (dialog, which) -> {
                            PostModel postModel = new PostModel();
                            postModel.pushPost(postDetail.getId());
                            postDetail.setPush(true);
                            refresh();
                            Toast.makeText(this, "Đã đẩy tin lên đầu bảng tin", Toast.LENGTH_LONG).show();
                        })
                        .setNegativeButton("Hủy", ((dialog, which) -> {
                        })).show();
            }
        }
    }

    private void repostPost(View v) {

        EditText edtDueDateRepost = findViewById(R.id.edtDueDateRepost);
        if (edtDueDateRepost.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Nhập hạn của bài đăng", Toast.LENGTH_LONG).show();
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Long pay = 0L;
            long days = 0;
            try {
                days = (sdf.parse(edtDueDateRepost.getText().toString()).getTime() - new Date().getTime()) / 1000L / 60L / 60L / 24L + 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (days > 0) {
                pay += days * 1000;
                if (pay > 0) {
                    if (currentUser.getAmount() == null || currentUser.getAmount() != null && currentUser.getAmount() < pay) {
                        new android.app.AlertDialog.Builder(this).setTitle("Thông báo")
                                .setMessage("Bạn không đủ tiền để đẩy tin. Cần ít nhất " + pay + " đồng trong tài khoản. Liên hệ với Hotel.Net để nạp thêm.")
                                .setCancelable(true)
                                .setNeutralButton("Ok", (dialog, which) -> {
                                }).show();
                    } else {
                        new AlertDialog.Builder(this).setTitle("Xác nhận đăng lại")
                                .setMessage("Đăng lại đến ngày " + postDetail.getDueDate() + " với giá " + pay)
                                .setPositiveButton("Đồng ý", (dialog, which) -> {
                                    PostModel postModel = new PostModel();
                                    PostDTO repostDTO = new PostDTO();
                                    repostDTO.setDueDate(edtDueDateRepost.getText().toString());
                                    PostDTO result = postModel.repostPost(postDetail.getId(), repostDTO);
                                    if (result != null) {
                                        postDetail = result;
                                    }
                                    Toast.makeText(this, "Đã đăng lại tin thành công", Toast.LENGTH_LONG).show();
                                    refresh();

                                })
                                .setNegativeButton("Hủy", ((dialog, which) -> {
                                })).show();
                    }
                }

            }
        }
    }

    private void myPost() {
        findViewById(R.id.tagButtonMyPost).setVisibility(View.VISIBLE);
        findViewById(R.id.tagButtonBottom).setVisibility(View.INVISIBLE);
        findViewById(R.id.tagSave).setVisibility(View.INVISIBLE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (sdf.parse(postDetail.getDueDate()).before(new Date())) {
                findViewById(R.id.tagStatusPostDetail).setVisibility(View.VISIBLE);
                findViewById(R.id.tagPush).setVisibility(View.INVISIBLE);
                dueDate = findViewById(R.id.edtDueDateRepost);
                dueDate.setOnClickListener(this::showTruitonDatePickerDialog);
                Button btnRepost = findViewById(R.id.btnRepostPost);
                btnRepost.setOnClickListener(this::repostPost);
            }else {
                LinearLayout tagPush = findViewById(R.id.tagPush);
                tagPush.setVisibility(View.VISIBLE);
                if (postDetail.getPush() != null && postDetail.getPush()) {
                    TextView txt = findViewById(R.id.txtStatusPushPostDetail);
                    txt.setText("Tin đã đẩy");
                } else {
                    tagPush.setOnClickListener(this::pushPost);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Button delete = findViewById(R.id.btnDeletePost);
        delete.setOnClickListener(v -> {
            new AlertDialog.Builder(this).setTitle("Xác nhận xóa")
                    .setMessage("Bài đăng đã xóa không thể hoàn tác")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        PostModel postModel = new PostModel();
                        if (postModel.deletePost(postDetail.getId())) {
                            Toast.makeText(this, "Đã xóa bài đăng", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PostDetailActivity.this, HomeActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.no_change);
                        } else {
                            Toast.makeText(this, "Không thể xóa bài đăng. Thử lại", Toast.LENGTH_LONG).show();
                        }
                    }).setNegativeButton("Hủy", ((dialog, which) -> {
            })).show();
        });
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Date getDate = new Date();
            getDate.setDate(day);
            getDate.setMonth(month);
            getDate.setYear(year - 1900);
            dueDate.setText(DateFormat.format("dd/MM/yyyy", getDate));
        }
    }


}
