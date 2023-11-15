package com.ibrahimcanerdogan.imagelabeler.utils

import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog

class AlertCreator(
    private var context: Context
) {

    fun create(title : String, message : String){
        val sDialog = SweetAlertDialog(context)

        sDialog.titleText = title
        sDialog.contentText = message

        sDialog.setCancelable(false)
        sDialog.show()
    }
}