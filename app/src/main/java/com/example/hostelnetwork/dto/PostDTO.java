package com.example.hostelnetwork.dto;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;

public class PostDTO implements Serializable {
    private Integer id;

    private Integer typeId;

    private Integer userId;

    private String title;

    private String content;

    private String location;

    private Integer area;

    private Double price;

    private Double deposit;

    private String postDate;

    private String dueDate;

    private Boolean status;

    private Boolean isPush;

    private String imgLinkPoster;

    private String typeStr;

    public PostDTO() {
    }

    public PostDTO(Integer id, Integer typeId, Integer userId, String title, String content, String location, Integer area, Double price, Double deposit, String postDate, String dueDate, Boolean status, Boolean isPush, String imgLinkPoster, String typeStr) {
        this.id = id;
        this.typeId = typeId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.location = location;
        this.area = area;
        this.price = price;
        this.deposit = deposit;
        this.postDate = postDate;
        this.dueDate = dueDate;
        this.status = status;
        this.isPush = isPush;
        this.imgLinkPoster = imgLinkPoster;
        this.typeStr = typeStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getPush() {
        return isPush;
    }

    public void setPush(Boolean push) {
        isPush = push;
    }

    public String getImgLinkPoster() {
        return imgLinkPoster;
    }

    public void setImgLinkPoster(String imgLinkPoster) {
        this.imgLinkPoster = imgLinkPoster;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }
}
