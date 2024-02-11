package com.ibrahimcanerdogan.barcodescanner.model

data class Phone(
    val phoneNumber : String?,
    val phoneType: String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "Phone \n" +
                "Number: ${phoneNumber.toString()} \n" +
                "Type: ${phoneType.toString()}"
    }
}
