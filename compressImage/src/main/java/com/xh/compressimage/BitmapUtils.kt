package com.xh.compressimage

import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import java.io.File
import kotlin.math.ceil

/**
 * 计算inSampleSize
 */
fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val width = options.outWidth
    val height = options.outHeight
    var inSampleSize = 1

    if (width > reqWidth || height > reqHeight) {
        val widthRatio = ceil(width.toFloat() / reqWidth.toFloat()).toInt()
        val heightRatio = ceil(height.toFloat() / reqHeight.toFloat()).toInt()
        inSampleSize = if (widthRatio > heightRatio) widthRatio else heightRatio
    }
    return inSampleSize
}

/**
 * 获取图片旋转角度
 */
fun getBitmapDegree(bitmapFile: File): Int {
    val exif = ExifInterface(bitmapFile)
    return when (exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }
}
