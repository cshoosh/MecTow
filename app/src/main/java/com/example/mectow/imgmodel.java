package com.example.mectow;

import android.graphics.ColorSpace;

public class imgmodel {
    private String imageuri;
    public imgmodel(){

    }
    public imgmodel(String imageuri){
        this.imageuri=imageuri;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }
}
