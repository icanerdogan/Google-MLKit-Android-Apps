package com.ibrahimcanerdogan.barcodescanner.model

import com.google.mlkit.vision.barcode.common.Barcode

data class ContactInfo(
    val contactName : Barcode.PersonName?,
    val contactTitle : String?,
    val contactPhone : MutableList<Barcode.Phone>?,
    val contactMail : MutableList<Barcode.Email>?,
    val contactAddress : MutableList<Barcode.Address>?,
    val contactUrl : MutableList<String>?,
    val contactOrganization : String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "Contact Info \n" +
                "Name: ${contactName?.formattedName.toString()} \n" +
                "Title: ${contactTitle.toString()} \n" +
                "Phone: ${contactPhone?.get(0)?.number.toString()} \n" +
                "Mail: ${contactMail?.get(0)?.address.toString()} \n" +
                "Address: ${contactAddress?.get(0)?.addressLines?.get(0).toString()} \n" +
                "Url: ${contactUrl?.get(0).toString()} \n" +
                "Organization: ${contactOrganization.toString()}"
    }
}