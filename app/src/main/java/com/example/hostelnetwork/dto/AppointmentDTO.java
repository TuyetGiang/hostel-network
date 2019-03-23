package com.example.hostelnetwork.dto;

import android.content.Intent;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AppointmentDTO implements Serializable {
    private Integer id;

    private Integer renterId;

    private Integer hostId;

    private String addressAppointment;

    private String time;

    private String createDate;

    private Integer status;

    private String note;

    private UserDTO userInfor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRenterId() {
        return renterId;
    }

    public void setRenterId(Integer renterId) {
        this.renterId = renterId;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public String getAddressAppointment() {
        return addressAppointment;
    }

    public void setAddressAppointment(String addressAppointment) {
        this.addressAppointment = addressAppointment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UserDTO getUserInfor() {
        return userInfor;
    }

    public void setUserInfor(UserDTO userInfor) {
        this.userInfor = userInfor;
    }
}
