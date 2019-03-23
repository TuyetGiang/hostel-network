package com.example.hostelnetwork.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.hostelnetwork.R;
import com.example.hostelnetwork.dto.BenefitDTO;
import com.example.hostelnetwork.dto.PostDTO;
import com.example.hostelnetwork.dto.TypeDTO;
import com.example.hostelnetwork.model.BenefitModel;
import com.example.hostelnetwork.model.PostModel;
import com.example.hostelnetwork.model.TypeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = getSupportActionBar();
        toolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
        toolbar.setTitle("Bộ lọc tìm kiếm");
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
            toolbar.setDisplayShowHomeEnabled(true);
        }
        setContentView(R.layout.activity_filter);

        Spinner edtLocation = findViewById(R.id.edtLocationFilterPost);
        EditText edtMinPrice = findViewById(R.id.edtMinPriceFilterPost);
        EditText edtMaxPrice = findViewById(R.id.edtMaxPriceFilterPost);
        Spinner edtType = findViewById(R.id.edtTypeFilterPost);
        GridView gridBenefit = findViewById(R.id.gridBenefitFilterPost);

        String locationStr = getIntent().getStringExtra("LOCATION");
        String typeIdStr = getIntent().getStringExtra("TYPE_ID");
        String minPriceStr = getIntent().getStringExtra("MIN_PRICE");
        String maxPriceStr = getIntent().getStringExtra("MAX_PRICE");
        String jsonBenefit = getIntent().getStringExtra("LIST_BENEFIT");



        if(minPriceStr != null){
            edtMinPrice.setText(minPriceStr);
        }
        if(maxPriceStr != null){
            edtMaxPrice.setText(maxPriceStr);
        }

        //set adapter for benefit
        BenefitModel benefitModel = new BenefitModel();
        List<BenefitDTO> benefitDTOList = benefitModel.getAllbenefit();
        ArrayAdapter<BenefitDTO> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, benefitDTOList);
        gridBenefit.setAdapter(arrayAdapter);
        gridBenefit.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);

        if (jsonBenefit != null) {
            TypeToken<List<Integer>> typeToken = new TypeToken<List<Integer>>() {
            };
            List<Integer> listBenefit = new Gson().fromJson(jsonBenefit, typeToken.getType());
            for (int i = 0; i < listBenefit.size() ; i++) {
                gridBenefit.setItemChecked(listBenefit.get(i)-1, true);
                BenefitDTO benefitDTO = (BenefitDTO) gridBenefit.getItemAtPosition(listBenefit.get(i)-1);
                benefitDTO.setChecked(true);
            }
        }

        gridBenefit.setOnItemClickListener((parent, view, position, id) -> {
            BenefitDTO benefitDTO = (BenefitDTO) gridBenefit.getItemAtPosition(position);
            if (benefitDTO.getChecked() == null) {
                benefitDTO.setChecked(false);
            }
            benefitDTO.setChecked(!benefitDTO.getChecked());
        });

        //set adapter for list type
        TypeModel typeModel = new TypeModel();
        List<TypeDTO> listType = typeModel.getAllType();
        String[] arrType = new String[listType.size() + 1];
        arrType[0] = "Tất cả các thể loại";
        for (int i = 0; i < listType.size(); i++) {
            arrType[i + 1] = listType.get(i).getTypeName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrType);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        edtType.setAdapter(adapter);
        if(typeIdStr != null){
            edtType.setSelection(Integer.parseInt(typeIdStr));
        }

        //set adapter for list location
        ArrayAdapter<CharSequence> adapterLocation = ArrayAdapter.createFromResource(this, R.array.district_array, android.R.layout.simple_spinner_item);
        adapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtLocation.setAdapter(adapterLocation);
        if(locationStr != null){
            String[] locations =getResources().getStringArray(R.array.district_array);
            for (int i = 0; i < locations.length; i++) {
                if (locations[i].equals(locationStr)){
                    edtLocation.setSelection(i);
                }
            }
        }


        Button btnApplyFilter = findViewById(R.id.btnApplyFilter);
        btnApplyFilter.setOnClickListener(v -> {
            Intent intent = new Intent(FilterActivity.this, HomeActivity.class);
            intent.putExtra("FILTERED", true);
            Gson gson = new Gson();
            List<Integer> listBenefit = new ArrayList<>();
            for (int i = 0; i < benefitDTOList.size(); i++) {
                if (benefitDTOList.get(i).getChecked() != null && benefitDTOList.get(i).getChecked()) {
                    listBenefit.add(benefitDTOList.get(i).getId());
                }
                intent.putExtra("LIST_BENEFIT", gson.toJson(listBenefit));
            }
            String minPrice = null;
            if (!edtMinPrice.getText().toString().equals("")) {
                minPrice = edtMinPrice.getText().toString();
                intent.putExtra("MIN_PRICE", minPrice);
            }
            String maxPrice = null;
            if (!edtMaxPrice.getText().toString().equals("")) {
                maxPrice = edtMaxPrice.getText().toString();
                intent.putExtra("MAX_PRICE", maxPrice);
            }

            String location = null;
            if (edtLocation.getSelectedItemId() != 0) {
                location = edtLocation.getSelectedItem().toString();
                intent.putExtra("LOCATION", location);
            }

            Long typeId = null;
            if (edtType.getSelectedItemId() != 0) {
                typeId = edtType.getSelectedItemId();
                intent.putExtra("TYPE_ID", typeId.toString());
            }

            intent.putExtra("FRAGMENT_ID", R.id.menu_newss);
            startActivity(intent);
        });

        Button btnClearFilter = findViewById(R.id.btnClearFilter);
        btnClearFilter.setOnClickListener(v -> {
            edtMinPrice.setText("");
            edtMaxPrice.setText("");
            SparseBooleanArray sp = gridBenefit.getCheckedItemPositions();
            for (int i = 0; i < sp.size(); i++) {
                gridBenefit.setItemChecked(i, false);
            }
            edtLocation.setSelection(0);
            edtType.setSelection(0);
            for (int i = 0; i < benefitDTOList.size(); i++) {
                benefitDTOList.get(i).setChecked(false);
            }
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
}
