package com.example.neps.tourapp;

import android.graphics.Bitmap;

public class TempClassToTransferParams {
    private Bitmap image;
    private String addr;
    private String summary;


    private String mapx;
    private String mapy;

    public TempClassToTransferParams(Bitmap image, String addr, String summary, String mapx, String mapy) {
        super();
        this.image = image;
        this.addr = addr;
        this.summary = summary;
        this.mapx = mapx;
        this.mapy = mapy;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getSummary(){ return summary;}
    public void setSummary(String summary){ this.summary = summary;}

    public String getMapx(){ return mapx;}
    public void setMapx(String mapx){ this.mapx = mapx;}

    public String getMapy(){ return mapy;}
    public void setMapy(String mapy){ this.mapy = mapy;}
}
