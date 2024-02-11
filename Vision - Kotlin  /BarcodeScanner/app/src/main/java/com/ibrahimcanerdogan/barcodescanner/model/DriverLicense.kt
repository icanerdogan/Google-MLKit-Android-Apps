package com.ibrahimcanerdogan.barcodescanner.model

data class DriverLicense(
    val driverLicenseNumber : String?,
    val driverFirstName : String?,
    val driverMiddleName : String?,
    val driverLastName: String?,
    val driverGender : String?,
    val driverBirthDate : String?,
    val driverAddressStreet : String?,
    val driverAddressCity : String?,
    val driverAddressState : String?,
    val driverAddressZip : String?,
    val driverIssueDate : String?,
    val driverExpiryDate : String?,
    val driverIssuingCountry : String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "Driver Name \n" +
                "License Number: $driverLicenseNumber \n" +
                "First Name: $driverFirstName \n" +
                "Middle Name: $driverMiddleName \n" +
                "Last Name: $driverLastName \n" +
                "Gender: $driverGender \n" +
                "Birth Date: $driverBirthDate \n" +
                "Address - Street: $driverAddressStreet \n" +
                "Address - City: $driverAddressCity \n" +
                "Address - State: $driverAddressState \n" +
                "Address - Zip: $driverAddressZip \n" +
                "Issue Date: $driverIssueDate \n" +
                "Expiry Date: $driverExpiryDate \n" +
                "Issuing Country: $driverIssuingCountry"
    }
}