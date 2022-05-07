package com.example.enotes;

import android.net.Uri;

public class Content {
    public int size;
    public String type, content, title;
    public boolean isBold, isItalic;
    public Uri image;

    public Content(){

    }

    public Content(String type, String content){
        this.type = type;
        this.content = content;
    }

    public Content(String type){
        this.type = type;
    }

    public Content(String type, String content, Uri image, String title, int size, boolean isBold, boolean isItalic){
        this.type = type;
        this.content = content;
        this.image = image;
        this.title = title;
        this.size = size;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }
}
