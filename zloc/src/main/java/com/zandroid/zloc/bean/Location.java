package com.zandroid.zloc.bean;

public class Location {

    private Long id;
    private String time;
    private String latitude;
    private String longitude;
    private String address;
    private String code;// 设备唯一标示

    public Location() {
        super();
    }

    public Location(String time, String latitude, String longitude, String address, String code) {
        super();
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
