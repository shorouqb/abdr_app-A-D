package com.example.vip.abdr_app_a_d;

import android.graphics.Bitmap;

public class Luggage_info {
    private int lcount;
    private String bitmapstring ;
    private Bitmap image ;
    private String  barnumber;
    public Luggage_info(int ln,String bitmapstring ,Bitmap image ,String barnumber){
        this.lcount=ln;
        this.bitmapstring = bitmapstring;
        this.image=image;
        this.barnumber=barnumber;
    }

    public String getBarnumber() {
        return barnumber;
    }

    public void setBarnumber(String barnumber) {
        this.barnumber = barnumber;
    }

    public int getLcount() {
        return lcount;
    }

    public void setLcount(int lcount) {
        this.lcount = lcount;
    }

    public String getBitmapstring() {
        return bitmapstring;
    }

    public void setBitmapstring(String bitmapstring) {
        this.bitmapstring = bitmapstring;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }








}
