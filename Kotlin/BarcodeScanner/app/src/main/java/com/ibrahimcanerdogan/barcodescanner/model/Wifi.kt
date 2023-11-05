package com.ibrahimcanerdogan.barcodescanner.model

data class Wifi(
    val wifiPassword : String?,
    val wifiSsid : String?,
    val wifiEncryptionType: String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "Wifi \n" +
                "Password: ${wifiPassword.toString()} \n" +
                "Ssid: ${wifiSsid.toString()} \n" +
                "Encryption Type: ${wifiEncryptionType.toString()}"
    }
}