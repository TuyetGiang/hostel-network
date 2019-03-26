package com.example.hostelnetwork.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.BenefitDTO;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.TypeDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.BenefitModel;
import com.example.hostelnetwork.model.PostModel;
import com.example.hostelnetwork.model.TypeModel;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WritePostActivity extends AppCompatActivity {
    private ActionBar toolbar;
    private UserDTO currentAcount;
    private static EditText edtDueDate;
    private Spinner edtType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        toolbar.setTitle("Đăng tin");
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_write_post);

        getGridBenefit();
        getTypedata();
        edtDueDate = findViewById(R.id.edtDueDateWritePost);
        edtDueDate.setOnClickListener(this::showTruitonDatePickerDialog);

        SharedPreferences accountPreferences = getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = accountPreferences.getString("userInfor", "");
        currentAcount = gson.fromJson(json, UserDTO.class);

        Button btnAcceptWritePost = findViewById(R.id.btnAcceptWritePost);
        btnAcceptWritePost.setOnClickListener(this::uploadPost);
        Button btnCancel = findViewById(R.id.btnCancelWritePost);
        btnCancel.setOnClickListener(this::cancelPost);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTypedata() {
        edtType = findViewById(R.id.edtTypeWritePost);

        TypeModel typeModel = new TypeModel();
        List<TypeDTO> listType = typeModel.getAllType();
        String[] arrType = new String[listType.size()];
        for (int i = 0; i < listType.size(); i++) {
            arrType[i] = listType.get(i).getTypeName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrType);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        edtType.setAdapter(adapter);
    }

    private void getGridBenefit() {
        GridView gridBenefit = findViewById(R.id.gridBenefitWritePost);
        BenefitModel benefitModel = new BenefitModel();
        List<BenefitDTO> benefitDTOList = benefitModel.getAllbenefit();
        ArrayAdapter<BenefitDTO> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, benefitDTOList);
        gridBenefit.setAdapter(arrayAdapter);
        gridBenefit.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        gridBenefit.setOnItemClickListener((parent, view, position, id) -> {
            BenefitDTO benefitDTO = (BenefitDTO) gridBenefit.getItemAtPosition(position);
            if (benefitDTO.getChecked() == null) {
                benefitDTO.setChecked(false);
            }
            benefitDTO.setChecked(!benefitDTO.getChecked());
        });
    }

    private void uploadPost(View v) {

        EditText edtTitle = findViewById(R.id.edtTitleWritePost);

        EditText edtCostWritePost = findViewById(R.id.txtCostWritePost);
        EditText edtDepositWritePost = findViewById(R.id.edtDepositWritePost);
        EditText edtAreaWritePost = findViewById(R.id.edtAreaWritePost);
        EditText edtNumberHouse = findViewById(R.id.edtNumberHouse);
        EditText edtStreet = findViewById(R.id.edtStreet);
        EditText edtWard = findViewById(R.id.edtWard);
        EditText edtCounty = findViewById(R.id.edtCounty);

        EditText edtNotesWritePost = findViewById(R.id.edtNotesWritePost);
        edtDueDate = findViewById(R.id.edtDueDateWritePost);
        GridView gridBenefit = findViewById(R.id.gridBenefitWritePost);

        CheckBox chbAcceptPayment = findViewById(R.id.chbAcceptPayment);


        Boolean flag = true;
        if (edtTitle.getText().toString().trim().equals("")) {
            edtTitle.setError("Vui lòng điền tiêu đề");
            flag = false;
        }
        if (edtCostWritePost.getText().toString().trim().equals("")) {
            edtCostWritePost.setError("Vui lòng điền giá");
            flag = false;

        }
        if (edtDepositWritePost.getText().toString().trim().equals("")) {
            edtDepositWritePost.setError("Vui lòng điền số tiền đặt cọc");
            flag = false;

        }
        if (edtAreaWritePost.getText().toString().trim().equals("")) {
            edtAreaWritePost.setError("Vui lòng điền diện tích");
            flag = false;

        }
        if (edtNumberHouse.getText().toString().trim().equals("")) {
            edtNumberHouse.setError("Vui lòng điền số nhà");
            flag = false;

        }
        if (edtStreet.getText().toString().trim().equals("")) {
            edtStreet.setError("Vui lòng điền tên đường");
            flag = false;

        }
        if (edtWard.getText().toString().trim().equals("")) {
            edtWard.setError("Vui lòng điền tên phường");
            flag = false;

        }
        if (edtCounty.getText().toString().trim().equals("")) {
            edtCounty.setError("Vui lòng điền tên quận");
            flag = false;

        }
        if (edtDueDate.getText().toString().trim().equals("")) {
            edtDueDate.setError("Vui lòng chọn ngày hết hạn tin đăng");
            flag = false;

        }
        if (!chbAcceptPayment.isChecked()) {
            chbAcceptPayment.setError("Chọn đồng ý để đăng tin");
        }

        if (!flag) {
            Toast.makeText(this, "Yêu cầu điền đầy đủ thông tin", Toast.LENGTH_LONG).show();
        } else {
            PostDTO newPost = new PostDTO();
            newPost.setUserId(currentAcount.getId());
            newPost.setTitle(edtTitle.getText().toString());
            newPost.setPrice(Double.parseDouble(edtCostWritePost.getText().toString()));
            newPost.setDeposit(Double.parseDouble(edtDepositWritePost.getText().toString()));
            newPost.setArea(Integer.parseInt(edtDepositWritePost.getText().toString()));
            newPost.setPostDate(DateFormat.format("dd/MM/yyyy", (new Date())).toString());
            newPost.setDueDate(edtDueDate.getText().toString());
            newPost.setTypeId((int) edtType.getSelectedItemId() + 1);
            newPost.setTypeStr((String) edtType.getSelectedItem());
            newPost.setContent(edtNotesWritePost.getText().toString());
            newPost.setLocation("Số " + edtNumberHouse.getText().toString() + ", Đường " + edtStreet.getText().toString()
                    + ", Phường " + edtWard.getText().toString() + ", Quận " + edtCounty.getText().toString());
            newPost.setPush(false);
            PostModel postModel = new PostModel();
            PostDTO result = postModel.createNewAppoinment(newPost);
            if (result != null){
                Toast.makeText(this, "Đăng bài thành công", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(WritePostActivity.this, PostDetailActivity.class);
                String postJson = new Gson().toJson(result);
                intent.putExtra("POST_DETAIL", postJson);
                intent.putExtra("SAVED_POST", false);
                startActivity(intent);
            }else {
                Toast.makeText(this,"Đăng bài thất bại. Thử lại!", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void cancelPost(View v) {
        new AlertDialog.Builder(this).setMessage("Bạn không thể hoàn tác khi hủy. Bạn chắc chưa?")
                .setTitle("Xác nhận hủy")
                .setPositiveButton("Chắc chắn", (arg0, arg1) -> {
                    finish();
                })
                .setNegativeButton("Quay lại", (arg0, arg1) -> {
                })
                .show();
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
            Date date = new Date();
            date.setDate(day);
            date.setMonth(month);
            date.setYear(year - 1900);
            edtDueDate.setText(DateFormat.format("dd/MM/yyyy", date));
        }
    }
}
