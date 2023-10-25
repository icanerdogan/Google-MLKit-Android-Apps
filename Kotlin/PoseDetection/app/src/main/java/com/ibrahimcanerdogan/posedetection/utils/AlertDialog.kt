package com.ibrahimcanerdogan.posedetection.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.ibrahimcanerdogan.posedetection.R

object AlertDialog {

    fun make(
        context: Context,
        title: String,
        message: String,
        positive: () -> Unit,
        negative: () -> Unit
    ) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.icon_ask)

        builder.setPositiveButton("OK") { _, _ ->
            positive()
        }
        builder.setNegativeButton("Cancel") { _, _ ->
            negative()
        }

        builder.create().show()
    }

    fun informationDialog(
        context: Context,
        title: String,
        message: String?
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setIcon(R.drawable.icon_info)

        builder.setPositiveButton("OK") { _, _ ->
            builder.setCancelable(true)
        }

        builder.create().show()
    }
}