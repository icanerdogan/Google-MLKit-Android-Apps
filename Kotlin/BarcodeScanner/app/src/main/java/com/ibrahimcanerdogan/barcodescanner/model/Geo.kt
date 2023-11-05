package com.ibrahimcanerdogan.barcodescanner.model

data class Geo(
    val geoLatitude : Double?,
    val geoLongitude : Double?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "Geo \n" +
                "Latitude: $geoLatitude \n" +
                "Longitude: $geoLongitude"
    }
}
