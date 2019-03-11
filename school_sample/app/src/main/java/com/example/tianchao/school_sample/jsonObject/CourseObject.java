package com.example.tianchao.school_sample.jsonObject;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class CourseObject implements Serializable {
    private  String[] name;
    private  String[] num;
    private  String[] teacher;
    private  String[] information;
    private  String[] time ;
    private  String[] place;
    private  String[] picString;
    private  String[] teacher_name;
    private ArrayList<Bitmap> picture;

    public void setName(String[] name) {
        this.name = name;
    }

    public String[] getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String[] teacher_name) {
        this.teacher_name = teacher_name;
    }

    public void setNum(String[] num) {
        this.num = num;
    }

    public void setTeacher(String[] teacher) {
        this.teacher = teacher;
    }

    public void setInformation(String[] information) {
        this.information = information;
    }

    public ArrayList<Bitmap> getPicture() {
        return picture;
    }

    public void setPicture(ArrayList<Bitmap> picture) {
        this.picture = picture;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    public void setPlace(String[] place) {
        this.place = place;
    }

    public void setPicString(String[] picString) {
        this.picString = picString;
    }

    public String[] getName() {

        return name;
    }

    public String[] getNum() {
        return num;
    }

    public String[] getTeacher() {
        return teacher;
    }

    public String[] getInformation() {
        return information;
    }

    public String[] getTime() {
        return time;
    }

    public String[] getPlace() {
        return place;
    }

    public String[] getPicString() {
        return picString;
    }
}
