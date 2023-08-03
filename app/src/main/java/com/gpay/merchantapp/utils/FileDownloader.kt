package com.gpay.merchantapp.utils

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object FileDownloader {
    private const val TAG = "FileDownloader"
    private const val MEGABYTE = 1024 * 1024
    fun downloadFile(fileUrl: String, directory: File) {
        try {

            val url = URL(fileUrl)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            val inputStream = urlConnection.inputStream


            val fileOutputStream = FileOutputStream(directory)
            val totalSize = urlConnection.contentLength
            val buffer = ByteArray(MEGABYTE)
            var bufferLength = 0
            while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                fileOutputStream.write(buffer, 0, bufferLength)
            }
            fileOutputStream.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()

        } catch (e: MalformedURLException) {
            e.printStackTrace()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}