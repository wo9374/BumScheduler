package com.ljb.data

import android.util.Log

const val MyTag = "MyTag"
object DlogUtil {
    var debugMode = true
    fun d(TAG: String?, `object`: Any) {
        if (debugMode) {
            Log.d(TAG, buildMessage(`object`))
        }
    }

    private fun buildMessage(`object`: Any): String {
        val ste = Thread.currentThread().stackTrace[4]
        val sb = StringBuilder()
        sb.append("DlogUtil")
        sb.append(" :: ")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append(" :: ")
        sb.append(`object`.toString())
        return sb.toString()
    }
}