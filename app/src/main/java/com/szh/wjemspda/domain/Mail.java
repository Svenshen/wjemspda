package com.szh.wjemspda.domain;

import java.util.Date;

import io.realm.RealmObject;

public class Mail extends RealmObject{

    String mailno;
    Integer zhongliang;
    String chepai;
    int pici;

    Date shijian;

    public String getMailno() {
        return mailno;
    }

    public void setMailno(String mailno) {
        this.mailno = mailno;
    }

    public Integer getZhongliang() {
        return zhongliang;
    }

    public void setZhongliang(Integer zhongliang) {
        this.zhongliang = zhongliang;
    }

    public String getChepai() {
        return chepai;
    }

    public void setChepai(String chepai) {
        this.chepai = chepai;
    }

    public int getPici() {
        return pici;
    }

    public void setPici(int pici) {
        this.pici = pici;
    }

    public Date getShijian() {
        return shijian;
    }

    public void setShijian(Date shijian) {
        this.shijian = shijian;
    }
}
