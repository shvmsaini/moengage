package io.github.shvmsaini.moengage.utils

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

class HttpHandler {
    fun makeServiceCall(reqUrl: String?): String? {
        var response: StringBuilder = java.lang.StringBuilder()
        try {
            val url = URL(reqUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            BufferedReader(InputStreamReader(conn.inputStream)).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
            }
            return response.toString()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
        return response.toString()
    }

    companion object {
        private val TAG = HttpHandler::class.java.simpleName
    }
}