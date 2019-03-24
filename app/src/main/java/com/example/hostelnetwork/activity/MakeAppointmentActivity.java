package com.example.hostelnetwork.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.AppointmentDTO;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.AppointmentModel;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;

public class MakeAppointmentActivity extends AppCompatActivity {

    private ActionBar toolbar;
    static EditText dateEdit;
    static Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        toolbar.setTitle("Xác nhận đặt lịch hẹn");
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_make_appointment);

        TextView txtHostName = findViewById(R.id.txtHostFullNameMakeAppointment);
        TextView txtHostPhone = findViewById(R.id.txtHostPhoneMakeAppointment);
        TextView txtAddress = findViewById(R.id.txtAddressMakeAppointment);
        TextView txtRenterName = findViewById(R.id.txtRenterNameMakeAppointment);
        TextView txtRenterPhone = findViewById(R.id.txtRenterPhoneMakeAppointment);
        EditText edtNote = findViewById(R.id.edtNoteMakeAppointment);


        SharedPreferences accountPreferences = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = accountPreferences.getString("userInfor", "");
        UserDTO currentUser = gson.fromJson(json, UserDTO.class);

        UserDTO postedUser = new Gson().fromJson(getIntent().getStringExtra("USER_POSTED"), UserDTO.class);
        PostDTO postDetail = new Gson().fromJson(getIntent().getStringExtra("POST_DETAIL"), PostDTO.class);

        txtHostName.setText(postedUser.getFullname());
        txtHostPhone.setText(postedUser.getPhone());
        txtAddress.setText(postDetail.getLocation());
        txtRenterName.setText(currentUser.getFullname());
        txtRenterPhone.setText(currentUser.getPhone());

        dateEdit = findViewById(R.id.edtDayMakeAppointment);
        dateEdit.setOnClickListener(v -> {
            showTruitonTimePickerDialog(v);
            showTruitonDatePickerDialog(v);

        });

        Button btnAccept = findViewById(R.id.btnConfirmMakeAppointment);
        btnAccept.setOnClickListener(v -> {
            if (dateEdit.getText().toString().trim().equals("")) {
                Toast.makeText(MakeAppointmentActivity.this, "Không được để trống thời gian hẹn", Toast.LENGTH_LONG).show();
            } else {
                AppointmentDTO appointmentDTO = new AppointmentDTO();
                appointmentDTO.setRenterId(currentUser.getId());
                appointmentDTO.setHostId(postedUser.getId());
                appointmentDTO.setAddressAppointment(postDetail.getLocation());
                appointmentDTO.setTime(dateEdit.getText().toString());
                appointmentDTO.setCreateDate(DateFormat.format("dd/MM/yyyy", new Date()).toString());
                appointmentDTO.setNote(edtNote.getText().toString());
                appointmentDTO.setStatus(0);
                AppointmentModel appointmentModel = new AppointmentModel();
                AppointmentDTO result = appointmentModel.createNewAppoinment(appointmentDTO);
                if (result != null) {
                    Toast.makeText(MakeAppointmentActivity.this, "Tạo lịch hẹn thành công.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MakeAppointmentActivity.this, HomeActivity.class);
                    intent.putExtra("FRAGMENT_ID", R.id.menu_appointments);
                    startActivity(intent);
                } else {
                    Toast.makeText(MakeAppointmentActivity.this, "Tạo lịch hẹn thất bại. Thử lại sau!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MakeAppointmentActivity.this, PostDetailActivity.class);
                    intent.putExtra("POST_DETAIL", new Gson().toJson(postDetail));
                    intent.putExtra("SAVED_POST", getIntent().getBooleanExtra("SAVED_POST", false));
                    startActivity(intent);
                }
            }
        });
        Button btnCancel = findViewById(R.id.btnCancelMakeAppointment);
        btnCancel.setOnClickListener(v -> {
            finish();
        });
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
        }
        return super.onOptionsItemSelected(item);
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
            date.setMonth(month);
            date.setDate(day);
            date.setYear(1900 + year);
        }
    }

    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            date.setHours(hourOfDay);
            date.setMinutes(minute);
            dateEdit.setText(DateFormat.format("dd/MM/yyyy HH:mm", date));
        }
    }
}
