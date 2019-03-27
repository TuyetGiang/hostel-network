package com.example.hostelnetwork.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.BenefitDTO;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.TypeDTO;
import com.example.hostelnetwork.dto.UserDTO;
import com.example.hostelnetwork.model.BenefitModel;
import com.example.hostelnetwork.model.ImgurModel;
import com.example.hostelnetwork.model.PictureModel;
import com.example.hostelnetwork.model.PostModel;
import com.example.hostelnetwork.model.TypeModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WritePostActivity extends AppCompatActivity {
    private ActionBar toolbar;
    private UserDTO currentAcount;
    private static EditText edtDueDate;
    private Spinner edtType;
    private static int GET_FROM_GALLERY = 3;
    private List<String> listImg = new ArrayList<>();
    List<BenefitDTO> benefitDTOList;

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
        ImageView imageView = findViewById(R.id.imgUpload);
        imageView.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY));
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
        if(item.getItemId() == android.R.id.switch_widget){

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
        benefitDTOList = benefitModel.getAllbenefit();
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

        CheckBox chbAcceptPayment = findViewById(R.id.chbAcceptPayment);
        CheckBox chbAcceptPush = findViewById(R.id.chbAcceptPush);

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
            Long amount = currentAcount.getAmount();
            Long pay = 0L;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                long days = (sdf.parse(edtDueDate.getText().toString()).getTime() - new Date().getTime()) / 1000L / 60L / 60L / 24L;
                if (days > 0) {
                    pay = days * 1000;
                    if (chbAcceptPush.isChecked()) {
                        pay += days * 1500;
                    }
                } else {
                    edtDueDate.setError("Ngày của hạn bài đăng phải sau ngày hôm nay");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (pay > 0) {
                if (currentAcount.getAmount() < pay) {
                    new AlertDialog.Builder(this).setTitle("Thông báo")
                            .setMessage("Bạn không đủ tiền để đăng bài. Cần ít nhất " + pay + " đồng trong tài khoản. Liên hệ với Hotel.Net để nạp thêm.")
                            .setCancelable(true)
                            .setNeutralButton("Ok", (dialog, which) -> {
                            }).show();
                } else {
                    new AlertDialog.Builder(this).setTitle("Xác nhận đăng tin")
                            .setMessage("Tin đăng đến hạn vào ngày " + edtDueDate.getText().toString() + " với giá " + pay + ". Bạn đồng ý không?")
                            .setPositiveButton("Đồng ý", (arg0, arg1) -> {
                                PostDTO newPost = new PostDTO();
                                newPost.setUserId(currentAcount.getId());
                                newPost.setTitle(edtTitle.getText().toString());
                                newPost.setPrice(Double.parseDouble(edtCostWritePost.getText().toString()));
                                newPost.setDeposit(Double.parseDouble(edtDepositWritePost.getText().toString()));
                                newPost.setArea(Integer.parseInt(edtAreaWritePost.getText().toString()));
                                newPost.setPostDate(DateFormat.format("dd/MM/yyyy", (new Date())).toString());
                                newPost.setDueDate(edtDueDate.getText().toString());
                                newPost.setTypeId((int) edtType.getSelectedItemId() + 1);
                                newPost.setTypeStr((String) edtType.getSelectedItem());
                                newPost.setContent(edtNotesWritePost.getText().toString());
                                newPost.setLocation("Số " + edtNumberHouse.getText().toString() + ", Đường " + edtStreet.getText().toString()
                                        + ", Phường " + edtWard.getText().toString() + ", Quận " + edtCounty.getText().toString());
                                newPost.setPush(chbAcceptPush.isChecked());
                                if (listImg.size() > 0) {
                                    newPost.setImgLinkPoster(listImg.get(0));
                                }
                                PostModel postModel = new PostModel();
                                PostDTO result = postModel.createNewAppoinment(newPost);
                                if (result != null) {
                                    if (listImg.size() > 0) {
                                        PictureModel pictureModel = new PictureModel();
                                        for (int i = 0; i < listImg.size(); i++) {
                                            pictureModel.insertPicture(result.getId(), listImg.get(i));
                                        }
                                    }
                                    BenefitModel benefitModel = new BenefitModel();
                                    for (int i = 0; i < benefitDTOList.size(); i++) {
                                        if (benefitDTOList.get(i).getChecked() != null && benefitDTOList.get(i).getChecked()) {
                                            benefitModel.insertBenefit(result.getId(), benefitDTOList.get(i).getId());
                                        }
                                    }
                                    Toast.makeText(this, "Đăng bài thành công", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(WritePostActivity.this, PostDetailActivity.class);
                                    String postJson = new Gson().toJson(result);
                                    intent.putExtra("POST_DETAIL", postJson);
                                    intent.putExtra("SAVED_POST", false);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(this, "Đăng bài thất bại. Thử lại!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("Hủy", (arg0, arg1) -> {
                            })
                            .show();
                }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] imgByte = stream.toByteArray();
                ImgurModel imgurModel = new ImgurModel();
                listImg.add(imgurModel.uploadImage(imgByte));
                ImageView imageView = null;
                switch (listImg.size()) {
                    case 1:
                        imageView = findViewById(R.id.imgUpload1);
                        break;
                    case 2:
                        imageView = findViewById(R.id.imgUpload2);
                        break;
                    case 3:
                        imageView = findViewById(R.id.imgUpload3);
                        break;
                    case 4:
                        imageView = findViewById(R.id.imgUpload4);
                        break;
                    case 5:
                        imageView = findViewById(R.id.imgUpload5);
                        break;
                }
                Picasso.with(this).load(listImg.get(listImg.size() - 1)).into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
