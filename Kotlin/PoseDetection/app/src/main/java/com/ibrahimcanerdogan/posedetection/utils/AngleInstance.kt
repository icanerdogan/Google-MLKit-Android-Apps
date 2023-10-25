package com.ibrahimcanerdogan.posedetection.utils

class AngleInstance {

    private var angle : String? = null

    fun setAngle(angle: String) {
        this.angle = angle
    }

    fun getAngle() : String? {
        return angle
    }

    companion object {
        private var instance: AngleInstance? = null

        fun getInstance(): AngleInstance? {
            if (instance == null) instance = AngleInstance()
            return instance
        }
    }
}