package io.github.shvmsaini.moengage.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object MyBitmapUtils {
    private val TAG: String = MyBitmapUtils::class.java.simpleName

//    fun saveFile(context: Context, b: Bitmap, picName: String?) {
//        var fos: FileOutputStream ?= null
//        try {
//            fos = context.openFileOutput(picName, Context.MODE_PRIVATE)
//            b.compress(Bitmap.CompressFormat.PNG, 100, fos)
//        } catch (e: FileNotFoundException) {
//            Log.d(TAG, "file not found")
//            e.printStackTrace()
//        } catch (e: IOException) {
//            Log.d(TAG, "io exception")
//            e.printStackTrace()
//        } finally {
//            fos?.close()
//        }
//    }
//
//    fun loadBitmap(context: Context, picName: String?): Bitmap? {
//        var b: Bitmap? = null
//        var fis: FileInputStream ?= null
//        try {
//            fis = context.openFileInput(picName)
//            b = BitmapFactory.decodeStream(fis)
//        } catch (e: FileNotFoundException) {
//            Log.d(TAG, "file not found")
//            e.printStackTrace()
//        } catch (e: IOException) {
//            Log.d(TAG, "io exception")
//            e.printStackTrace()
//        } finally {
//            fis?.close()
//        }
//        return b
//    }

//    fun downloadImageFromPath(path: String, context: Context): MutableLiveData<Bitmap> {
//        val b : MutableLiveData<Bitmap> = MutableLiveData()
//        val fileName = path.substring(path.lastIndexOf('/'))
//        val btmp = loadBitmap(context, fileName)
//        if (btmp == null) {
//            b.postValue(btmp)
//        }
//        val inputStream: InputStream?
//        val bmp: Bitmap?
//        val responseCode: Int
//        try {
//            val url = URL(path)
//            val con = url.openConnection() as HttpURLConnection
//            con.doInput = true
//            con.connect()
//            responseCode = con.responseCode
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                inputStream = con.inputStream
//                bmp = BitmapFactory.decodeStream(inputStream)
//                inputStream.close()
//                saveFile(context, bmp, fileName)
//                b.postValue(bmp)
//            }
//        } catch (ex: Exception) {
//            Log.e("Exception", ex.toString())
//        }
//
//        return b
//    }

    fun downloadImageFromPath(path: String?, imageView: ImageView) : Bitmap? {
        val inputStream: InputStream?
        val bmp: Bitmap?
        val responseCode: Int
        try {
            val url = URL(path)
            val con = url.openConnection() as HttpURLConnection
            con.doInput = true
            con.connect()
            responseCode = con.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = con.inputStream
                bmp = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                try {
                    if (bmp.width > 2048 || bmp.height > 2048)
                        return null;
                    imageView.setImageBitmap(bmp)
                    return bmp
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        } catch (ex: Exception) {
            Log.e("Exception", ex.toString())
        }
        return null
    }

//    fun resizeImage(image: Bitmap): Bitmap {
//        val width = image.width / 10
//        val height = image.height / 10
//        if (image.byteCount <= 1000000)
//            return image
//
//        return Bitmap.createScaledBitmap(image, width, height, false)
//    }
}