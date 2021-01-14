package com.icanerdogan.posedetection;

import android.graphics.Bitmap;

public class Singleton {
    private Bitmap myImage;
    private static Singleton singleton;

    private Singleton(){

    }

    public Bitmap getMyImage(){
        return myImage;
    }

    public void setMyImage(Bitmap myImage){
        this.myImage = myImage;
    }

    public static Singleton getInstance(){
        if (singleton == null){
            singleton = new Singleton();
        }
        return singleton;
    }
}
