package com.thanone.palc.bean;

import java.util.Date;

public class Member {

    private Long id;

    private String idcard;
    private String phone;
    private String password;
    private Date regTime;
    private Date lastLogin;

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getId() {
        return id;
    }

    public String getIdcard() {
        return idcard;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public Date getRegTime() {
        return regTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }
}
