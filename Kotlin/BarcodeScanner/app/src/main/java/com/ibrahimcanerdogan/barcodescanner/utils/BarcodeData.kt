package com.ibrahimcanerdogan.barcodescanner.utils

class BarcodeData {

    private var typeValue: String? = null
    private var formatType: String? = null

    fun getTypeValue() : String? {
        return typeValue
    }

    fun setTypeValue(typeValue : String?) {
        this.typeValue = typeValue
    }

    fun getFormatType() : String? {
        return formatType
    }

    fun setFormatType(formatType: String) {
        this.formatType = formatType
    }

    companion object {
        private var instance : BarcodeData? = null

        fun instance() : BarcodeData? {
            if (instance == null) instance = BarcodeData()
            return instance
        }
    }
}