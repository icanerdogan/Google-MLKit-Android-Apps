package com.ibrahimcanerdogan.selfiesegmentation.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.ibrahimcanerdogan.selfiesegmentation.R

class LoadingDialog(
    private var activity: Activity,
    private var inflater: LayoutInflater
) {

    private var dialog : AlertDialog? = null

    fun show() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(true)

        dialog = builder.create()
        dialog!!.show()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}