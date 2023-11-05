package com.ibrahimcanerdogan.selfiesegmentation.utils

import android.graphics.Bitmap

class GroundInstance {

    var backupImage : Bitmap? = null
    var foreground : Bitmap? = null
    var background : Bitmap? = null

    companion object {
        private var instance: GroundInstance? = null

        fun getInstance() : GroundInstance? {
            if (instance == null) instance = GroundInstance()
            return instance
        }
    }
}