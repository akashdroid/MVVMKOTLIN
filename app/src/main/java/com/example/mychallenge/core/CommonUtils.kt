package com.example.mychallenge.core

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.util.Patterns


import com.example.mychallenge.R

import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CommonUtils {

    val timestamp: String
        get() = SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(Date())

    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
        val manager = context.assets
        val `is` = manager.open(jsonFileName)

        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()

        return String(buffer)
    }

    fun showLoadingDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }
}// This utility class is not publicly instantiable
