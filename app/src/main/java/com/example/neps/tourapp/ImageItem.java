package com.example.neps.tourapp;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String title;
    private String url;
    private String contentId;
    private String contenttypeId;

    public ImageItem(Bitmap image, String title, String url, String contentId, String contenttypeId) {
        super();
        this.image = image;
        this.title = title;
        this.url = url;
        this.contentId = contentId;
        this.contenttypeId = contenttypeId;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl(){ return url;}
    public void setUrl(String url){ this.url = url;}

    public String getContentId(){ return contentId;}
    public void setContentId(String contentId){ this.contentId = contentId;}

    public String getContenttypeId(){ return contenttypeId;}
    public void setContenttypeId(String contenttypeId){ this.contenttypeId = contenttypeId;}
}
