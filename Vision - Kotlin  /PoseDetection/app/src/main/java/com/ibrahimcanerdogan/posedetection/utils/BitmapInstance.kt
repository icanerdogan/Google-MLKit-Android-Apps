package com.ibrahimcanerdogan.posedetection.utils

import android.graphics.Bitmap

class BitmapInstance {

    private var bitmap: Bitmap? = null

    fun setBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    fun getBitmap() : Bitmap? {
        return bitmap
    }

    companion object {
        private var instance: BitmapInstance? = null

        fun getInstance() : BitmapInstance? {
            if (instance == null) instance = BitmapInstance()
            return instance
        }
    }
}