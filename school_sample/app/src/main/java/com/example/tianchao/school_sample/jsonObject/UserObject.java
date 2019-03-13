package com.example.tianchao.school_sample.jsonObject;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class UserObject implements Serializable {
    private String[] name;
    private String[] id_school;
    private String[] id_card;
    private String[] phone;
    private String[] type;
    private String[] username;
    private String[] password;
    private String[] is_signed;
    private String[] class_num;
    private String[] icon;
    private ArrayList<Bitmap> picture;

    public String[] getName() {
        return name;
    }

    public String[] getId_school() {
        return id_school;
    }

    public String[] getId_card() {
        return id_card;
    }

    public String[] getPhone() {
        return phone;
    }

    public String[] getType() {
        return type;
    }

    public String[] getUsername() {
        return username;
    }

    public String[] getPassword() {
        return password;
    }

    public String[] getIs_signed() {
        return is_signed;
    }

    public String[] getClass_num() {
        return class_num;
    }

    public String[] getIcon() {
        return icon;
    }

    public ArrayList<Bitmap> getPicture() {
        return picture;
    }

    public void setName(String[] name) {
        this.name = name;
    }

    public void setId_school(String[] id_school) {
        this.id_school = id_school;
    }

    public void setId_card(String[] id_card) {
        this.id_card = id_card;
    }

    public void setPhone(String[] phone) {
        this.phone = phone;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public void setUsername(String[] username) {
        this.username = username;
    }

    public void setPassword(String[] password) {
        this.password = password;
    }

    public void setIs_signed(String[] is_signed) {
        this.is_signed = is_signed;
    }

    public void setClass_num(String[] class_num) {
        this.class_num = class_num;
    }

    public void setIcon(String[] icon) {
        this.icon = icon;
    }

    public void setPicture(ArrayList<Bitmap> picture) {
        this.picture = picture;
    }
}
