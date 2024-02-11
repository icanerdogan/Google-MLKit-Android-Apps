package com.ibrahimcanerdogan.barcodescanner.model

data class CalendarEvent(
    val calendarStatus : String?,
    val calendarLocation: String?,
    val calendarSummary : String?,
    val calendarOrganizer : String?,
    val calendarStart : String?,
    val calendarEnd : String?,
    val calendarDescription : String?
) : BaseBarcodeModel(null) {

    override fun toString(): String {
        return "CalendarEvent \n" +
                "Status : ${calendarStatus.toString()} \n" +
                "Location : ${calendarLocation.toString()} \n" +
                "Summary : ${calendarSummary.toString()} \n" +
                "Organizer : ${calendarOrganizer.toString()} \n" +
                "Start : ${calendarStart.toString()} \n" +
                "End : ${calendarEnd.toString()} \n" +
                "Description : ${calendarDescription.toString()}"
    }
}