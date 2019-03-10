package com.example.tianchao.school_sample.jsonObject;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class NewsObject {
    private  String[] tittle;
    private  String[] picString;
    private  String[] description;
    private  String[] link;
    private ArrayList<Bitmap> picture;

    public void setTittle(String[] tittle) {
        this.tittle = tittle;
    }

    public void setPicString(String[] picString) {
        this.picString = picString;
    }

    public ArrayList<Bitmap> getPicture() {
        return picture;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public void setPicture(ArrayList<Bitmap> picture) {
        this.picture = picture;
    }

    public void setLink(String[] link) {
        this.link = link;
    }

    public String[] getTittle() {
        return tittle;
    }

    public String[] getPicString() {
        return picString;
    }

    public String[] getDescription() {
        return description;
    }

    public String[] getLink() {
        return link;
    }
}
