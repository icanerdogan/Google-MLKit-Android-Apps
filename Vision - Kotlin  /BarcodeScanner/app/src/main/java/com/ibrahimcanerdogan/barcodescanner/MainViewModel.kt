package com.ibrahimcanerdogan.barcodescanner

import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.ibrahimcanerdogan.barcodescanner.model.BaseBarcodeModel
import com.ibrahimcanerdogan.barcodescanner.model.CalendarEvent
import com.ibrahimcanerdogan.barcodescanner.model.ContactInfo
import com.ibrahimcanerdogan.barcodescanner.model.DriverLicense
import com.ibrahimcanerdogan.barcodescanner.model.Email
import com.ibrahimcanerdogan.barcodescanner.model.Geo
import com.ibrahimcanerdogan.barcodescanner.model.Phone
import com.ibrahimcanerdogan.barcodescanner.model.Sms
import com.ibrahimcanerdogan.barcodescanner.model.Url
import com.ibrahimcanerdogan.barcodescanner.model.Wifi
import com.ibrahimcanerdogan.barcodescanner.utils.BarcodeData

class MainViewModel : ViewModel() {

    private val _cameraProvider = MutableLiveData<ProcessCameraProvider>()
    val cameraProvider : LiveData<ProcessCameraProvider> = _cameraProvider

    fun setCameraProvider(processCameraProvider: ProcessCameraProvider) {
        _cameraProvider.postValue(processCameraProvider)
    }

    fun getBarcodeData(barcodes : List<Barcode>, dialog: (BaseBarcodeModel, Int) -> Unit) {
        for (barcode in barcodes) {
            val format = barcode.format
            val rawValue = barcode.rawValue
            val valueType = barcode.valueType

            BarcodeData.instance()?.setTypeValue("Type: ${getValueType(valueType)}")
            BarcodeData.instance()?.setFormatType("Format: ${getFormatType(format)}")

            when(valueType) {
                Barcode.TYPE_CONTACT_INFO -> {
                    barcode.contactInfo?.let {
                        val contactInfo = ContactInfo(
                            contactName = it.name,
                            contactTitle = it.title,
                            contactPhone = it.phones,
                            contactMail = it.emails,
                            contactAddress = it.addresses,
                            contactUrl = it.urls,
                            contactOrganization = it.organization
                        )
                        dialog(contactInfo, Barcode.TYPE_CONTACT_INFO)
                    }
                }
                Barcode.TYPE_EMAIL -> {
                    barcode.email?.let {
                        val email = Email(
                            emailAddress = it.address,
                            emailSubject = it.subject,
                            emailBody = it.body
                        )
                        dialog(email, Barcode.TYPE_EMAIL)
                    }
                }
                Barcode.TYPE_PHONE -> {
                    barcode.phone?.let {
                        val phone = Phone(
                            phoneNumber = it.number,
                            phoneType = getPhoneType(it.type)
                        )
                        dialog(phone, Barcode.TYPE_PHONE)
                    }
                }
                Barcode.TYPE_SMS -> {
                    barcode.sms?.let {
                        val sms = Sms(
                            smsPhoneNumber = it.phoneNumber,
                            smsMessage = it.message
                        )
                        dialog(sms, Barcode.TYPE_SMS)
                    }
                }
                Barcode.TYPE_URL -> {
                    barcode.url?.let {
                        val url = Url(
                            urlTitle = it.title,
                            urlLink = it.url
                        )
                        dialog(url, Barcode.TYPE_URL)
                    }
                }
                Barcode.TYPE_WIFI -> {
                    barcode.wifi?.let {
                        val wifi = Wifi(
                            wifiPassword = it.password,
                            wifiSsid = it.ssid,
                            wifiEncryptionType = getEncryptionType(it.encryptionType)
                        )
                        dialog(wifi, Barcode.TYPE_WIFI)
                    }
                }
                Barcode.TYPE_GEO -> {
                    barcode.geoPoint?.let {
                        val geo = Geo(
                            geoLatitude = it.lat,
                            geoLongitude = it.lng
                        )
                        dialog(geo, Barcode.TYPE_GEO)
                    }
                }
                Barcode.TYPE_CALENDAR_EVENT -> {
                    barcode.calendarEvent?.let {
                        val calendarEvent = CalendarEvent(
                            calendarStatus = it.status,
                            calendarLocation = it.location,
                            calendarSummary = it.summary,
                            calendarOrganizer = it.organizer,
                            calendarStart = getCalenderTimeFormat(it.start),
                            calendarEnd = getCalenderTimeFormat(it.end),
                            calendarDescription = it.description
                        )
                        dialog(calendarEvent, Barcode.TYPE_CALENDAR_EVENT)
                    }
                }
                Barcode.TYPE_DRIVER_LICENSE -> {
                    barcode.driverLicense?.let {
                        val driverLicense = DriverLicense(
                            driverLicenseNumber = it.licenseNumber,
                            driverFirstName = it.firstName,
                            driverMiddleName = it.middleName,
                            driverLastName = it.lastName,
                            driverGender = getGender(it.gender!!.toInt()),
                            driverBirthDate = splitDate(it.birthDate!!),
                            driverAddressStreet = it.addressStreet,
                            driverAddressCity = it.addressCity,
                            driverAddressState = it.addressState,
                            driverAddressZip = it.addressZip,
                            driverIssueDate = splitDate(it.issueDate!!),
                            driverExpiryDate = splitDate(it.expiryDate!!),
                            driverIssuingCountry = it.issuingCountry
                        )
                        dialog(driverLicense, Barcode.TYPE_DRIVER_LICENSE)
                    }
                } else -> {
                    dialog(BaseBarcodeModel(rawValue), -1)
                }
            }
        }
    }

    private fun splitDate(date: String): String {
        val newDate = charArrayOf(date[0], date[1], '-', date[2], date[3], '-', date[4], date[5], date[6], date[7])
        return String(newDate)
    }

    private fun getGender(value: Int): String {
        return when(value) {
            1 -> "Male"
            2 -> "Female"
            else -> "UNKNOWN"
        }
    }

    private fun getCalenderTimeFormat(time: Barcode.CalendarDateTime?): String {
        time?.let {
            return if (time.isUtc) {
                "${it.year}-${it.month}-${it.day}T${it.hours}:${it.minutes}:${it.seconds}Z"
            } else {
                "${it.day}-${it.month}-${it.year} ${it.hours}:${it.minutes}:${it.seconds}"
            }
        }
        return "No Time Entered."
    }

    private fun getEncryptionType(encryptionType: Int): String = when(encryptionType) {
        1 -> "OPEN : Not Encrypted"
        2 -> "WPA : WPA Level Encryption"
        3 -> "WEP : WEP Level Encryption"
        else -> "UNKNOWN"
    }

    // https://developers.google.com/android/reference/com/google/mlkit/vision/barcode/common/Barcode.Phone#TYPE_UNKNOWN

    private fun getPhoneType(type: Int): String = when(type) {
        0 -> "UNKNOWN"
        1 -> "WORK"
        2 -> "HOME"
        3 -> "FAX"
        4 -> "MOBILE"
        else -> {
            "UNKNOWN"
        }
    }

    private fun getFormatType(format: Int): String = when(format) {
        -1 -> "UNKNOWN"
        1 -> "CODE 128"
        2 -> "CODE 39"
        4 -> "CODE 93"
        8 -> "CODABAR"
        16 -> "DATA MATRIX"
        32 -> "EAN 13"
        64 -> "EAN 8"
        128 -> "ITF"
        256 -> "QR CODE"
        512 -> "UPC A"
        1024 -> "UPC E"
        2048 -> "PDF417"
        4096 -> "AZTEC"
        else -> {
            "UNKNOWN"
        }
    }

    private fun getValueType(valueType: Int): String = when(valueType) {
        1 -> "CONTACT INFO"
        2 -> "EMAIL"
        3 -> "ISBN"
        4 -> "PHONE"
        5 -> "PRODUCT"
        6 -> "SMS"
        7 -> "TEXT"
        8 -> "URL"
        9 -> "WIFI"
        10 -> "GEO"
        11 -> "CALENDAR EVENT"
        12 -> "DRIVER LICENSE"
        else -> {
            "UNKNOWN"
        }
    }
}