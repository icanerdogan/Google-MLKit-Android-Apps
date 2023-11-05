package com.ibrahimcanerdogan.barcodescanner.model

open class BaseBarcodeModel(
    val rawValue : String?
) {

    override fun toString(): String {
        return "Barcode Data \n" +
                "Value : $rawValue"
    }
}