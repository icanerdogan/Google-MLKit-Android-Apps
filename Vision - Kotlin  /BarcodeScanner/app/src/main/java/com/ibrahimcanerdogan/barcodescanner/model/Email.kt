package com.ibrahimcanerdogan.barcodescanner.model

data class Email(
    val emailAddress : String?,
    val emailSubject : String?,
    val emailBody : String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "Email \n" +
                "Address: ${emailAddress.toString()} \n" +
                "Subject: ${emailSubject.toString()} \n" +
                "Body : ${emailBody.toString()}"
    }
}
