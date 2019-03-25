package com.example.hostelnetwork.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.AppointmentDTO;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.AppointmentModel;
import com.example.hostelnetwork.model.PostModel;
import com.example.hostelnetwork.model.UserModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentDetailActivity extends AppCompatActivity {

    private UserDTO userPosted;
    private UserDTO currentUser;
    private AppointmentDTO appointmentDTO;
    private ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        toolbar.setTitle("Chi tiết cuộc hẹn");
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_appointment_detail);

        Gson gson = new Gson();
        appointmentDTO = gson.fromJson(getIntent().getStringExtra("APPOINTMENT_DETAIL"), AppointmentDTO.class);
        TextView txtUserName = findViewById(R.id.txtNameUserAppointmentDetail);
        TextView txtPhone = findViewById(R.id.txtPhoneAppointmentDetail);
        TextView txtAddress = findViewById(R.id.txtAddressAppointmentDetail);
        ImageView imgUser = findViewById(R.id.imgAvatarUserAppointmentDetail);

        TextView txtType = findViewById(R.id.txtPostTypeAppointmentDetail);
        TextView txtTitle = findViewById(R.id.txtPostTitleAppointmentDetail);
        TextView txtCost = findViewById(R.id.txtPostCostAppointmentDetail);
        TextView txtLocation = findViewById(R.id.txtPostAddressAppointmentDetail);
        TextView txtTime = findViewById(R.id.txtPostDateAppointmentDetail);
        ImageView imgPost = findViewById(R.id.imgPostAppointmentDetail);

        TextView txtTimeAppointment = findViewById(R.id.txtTimeAppointmentDetail);
        TextView txtNote = findViewById(R.id.txtNoteAppointmentDetail);
        TextView txtStatus = findViewById(R.id.txtStatusAppointmentDetail);

        SharedPreferences accountPreferences = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        String json = accountPreferences.getString("userInfor", "");
        currentUser = gson.fromJson(json, UserDTO.class);

        UserModel userModel = new UserModel();
        Integer id = appointmentDTO.getHostId();
        if (currentUser.getId().equals(appointmentDTO.getHostId())) {
            id = appointmentDTO.getRenterId();
        }
        userPosted = userModel.getUserById(id);
        txtUserName.setText(userPosted.getFullname());
        txtPhone.setText(userPosted.getPhone());
        txtAddress.setText(userPosted.getAddress());
        Picasso.with(this).load(userPosted.getImgAvatar()).into(imgUser);

        PostModel postModel = new PostModel();
        PostDTO postDTO = postModel.getPostById(appointmentDTO.getPostId());
        txtType.setText(postDTO.getTypeStr().toUpperCase());
        txtTitle.setText(postDTO.getTitle());
        txtCost.setText(NumberFormat.getCurrencyInstance().format(postDTO.getPrice()));
        txtLocation.setText(postDTO.getLocation());
        txtTime.setText(postDTO.getPostDate());
        Picasso.with(this).load(postDTO.getImgLinkPoster()).into(imgPost);


        txtTimeAppointment.setText("THỜI GIAN HẸN:  " + appointmentDTO.getTime());
        if (appointmentDTO.getNote() == null) {
            txtNote.setText("GHI CHÚ:  không có ghi chú gì thêm");
        } else {
            txtNote.setText("GHI CHÚ:  " + appointmentDTO.getNote());
        }
        if (appointmentDTO.getStatus() != null) {
            if (appointmentDTO.getStatus() == 0) {
                txtStatus.setText("TRẠNG THÁI CUỘC HẸN:  Đang chờ...");
            } else if (appointmentDTO.getStatus() == 1) {
                txtStatus.setText("TRẠNG THÁI CUỘC HẸN:  Đã chấp nhận");
                txtStatus.setTextColor(Color.parseColor("#009944"));
            } else if (appointmentDTO.getStatus() == 2) {
                txtStatus.setText("TRẠNG THÁI CUỘC HẸN:  Đã từ chối");
                txtStatus.setTextColor(Color.parseColor("#ff0000"));
            }
        } else {
            txtStatus.setText("TRẠNG THÁI CUỘC HẸN: Đang chờ...");
        }

        ImageView btnGoDetail = findViewById(R.id.btnGoPostDetailFromAppointmentDetail);
        btnGoDetail.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentDetailActivity.this, PostDetailActivity.class);
            intent.putExtra("POST_DETAIL", gson.toJson(postDTO));
            startActivity(intent);
        });

        Button btnCall = findViewById(R.id.btnCallPhoneAppointmentDetail);
        btnCall.setOnClickListener(this::call);
        Button btnAccept = findViewById(R.id.btnAcceptAppointmentDetail);
        btnAccept.setOnClickListener(this::accept);
        Button btnReject = findViewById(R.id.btnRejectAppointmentDetail);
        btnReject.setOnClickListener(this::reject);
        Button btnDelete = findViewById(R.id.btnDeleteAppointmentDetail);
        btnDelete.setOnClickListener(this::delete);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            Intent intent = new Intent(AppointmentDetailActivity.this, HomeActivity.class);
            intent.putExtra("FRAGMENT_ID", R.id.menu_appointments);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    private void call(View v) {
        if (userPosted != null && userPosted.getPhone() != null) {
            Intent callIntent = new Intent();
            callIntent.setAction(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + userPosted.getPhone()));
            startActivity(callIntent);
        }
    }

    private void reject(View v) {
        if (currentUser.getId().equals(appointmentDTO.getRenterId())) {
            Toast.makeText(AppointmentDetailActivity.this, "Chỉ có chủ nhà mới từ chối lịch hẹn", Toast.LENGTH_LONG).show();
        } else {
            AppointmentModel appointmentModel = new AppointmentModel();
            AppointmentDTO result = appointmentModel.updateStatusAppointment(appointmentDTO.getId(), 2);
            if (result == null) {
                Toast.makeText(AppointmentDetailActivity.this, "Từ chối thất bại. Thử lại!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AppointmentDetailActivity.this, "Đã từ chối lịch hẹn", Toast.LENGTH_LONG).show();
                appointmentDTO = result;
                refresh();
            }
        }
    }

    private void accept(View v) {
        if (currentUser.getId().equals(appointmentDTO.getRenterId())) {
            Toast.makeText(AppointmentDetailActivity.this, "Chỉ có chủ nhà mới chấp nhận lịch hẹn", Toast.LENGTH_LONG).show();
        } else {
            AppointmentModel appointmentModel = new AppointmentModel();
            AppointmentDTO result = appointmentModel.updateStatusAppointment(appointmentDTO.getId(), 1);
            if (result == null) {
                Toast.makeText(AppointmentDetailActivity.this, "Chấp nhận thất bại. Thử lại!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AppointmentDetailActivity.this, "Đã chấp nhận lịch hẹn", Toast.LENGTH_LONG).show();
                appointmentDTO = result;
                refresh();
            }
        }
    }

    private void delete(View v) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            if (df.parse(appointmentDTO.getTime()).after(new Date())) {
                Toast.makeText(AppointmentDetailActivity.this, "Bạn không thể xóa khi chưa đến ngày hẹn", Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(this).setMessage("Xóa cuộc hẹn sẽ không thể khôi phục. Bạn chắc chưa?")
                        .setTitle("Xác nhận xóa cuộc hẹn")
                        .setPositiveButton("Chắc chắn", (arg0, arg1) -> {
                            AppointmentModel appointmentModel = new AppointmentModel();
                            if (!appointmentModel.deleteAppointment(appointmentDTO.getId())) {
                                Toast.makeText(AppointmentDetailActivity.this, "Xóa thất bại. Thử lại!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AppointmentDetailActivity.this, "Đã xóa lịch hẹn", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AppointmentDetailActivity.this, HomeActivity.class);
                                intent.putExtra("FRAGMENT_ID", R.id.menu_appointments);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }

                        })
                        .setNegativeButton("Hủy", (arg0, arg1) -> {
                        })
                        .show();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        finish();
        getIntent().putExtra("APPOINTMENT_DETAIL", new Gson().toJson(appointmentDTO));
        startActivity(getIntent());
        overridePendingTransition(R.anim.no_change, R.anim.no_change);
    }
}
