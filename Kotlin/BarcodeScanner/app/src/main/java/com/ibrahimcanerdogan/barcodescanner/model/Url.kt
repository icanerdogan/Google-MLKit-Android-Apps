package com.ibrahimcanerdogan.barcodescanner.model

data class Url(
    val urlTitle : String?,
    val urlLink : String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "Url \n" +
                "Url Title: $urlTitle \n" +
                "Url Link: $urlLink"
    }
}
