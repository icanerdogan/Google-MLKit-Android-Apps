package com.ibrahimcanerdogan.barcodescanner.model

data class Sms(
    val smsPhoneNumber : String?,
    val smsMessage : String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "SMS \n" +
                "Phone Number: $smsPhoneNumber \n" +
                "Message: $smsMessage"
    }
}