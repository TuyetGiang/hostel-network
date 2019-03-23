package com.example.hostelnetwork.dto;

public class BenefitDTO {
    private Integer id;

    private String benefitName;

    private Boolean isChecked;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    @Override
    public String toString() {
        return  benefitName;
    }
}
