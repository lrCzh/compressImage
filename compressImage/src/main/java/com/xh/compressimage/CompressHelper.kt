package com.xh.compressimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object CompressHelper {

    suspend fun compressImage(
        context: Context,
        imageFile: File,
        maxWidth: Int = 720,
        maxHeight: Int = 960,
        maxSize: Int = 256.KB,
        quality: Int = 80,
        subCompressPriorityType: CompressConfig.CompressPriorityType = CompressConfig.CompressPriorityType.TYPE_QUALITY,
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        bitmapConfig: Bitmap.Config = Bitmap.Config.RGB_565,
        destDirPath: String? = null,
        fileName: String? = null
    ): File? {
        val builder = CompressConfig.Builder()
            .setMaxWidth(maxWidth)
            .setMaxHeight(maxHeight)
            .setMaxSize(maxSize)
            .setQuality(quality)
            .setSubCompressPriorityType(subCompressPriorityType)
            .setCompressFormat(compressFormat)
            .setBitmapConfig(bitmapConfig)
        destDirPath?.let { builder.setDestDirPath(it) }
        fileName?.let { builder.setFileName(it) }
        return compressImage(context, imageFile, builder.build())
    }

    suspend fun compressImage(context: Context, imageFile: File, config: CompressConfig): File? {
        return withContext(Dispatchers.IO) {

            val compressDirPath = config.destDirPath ?: context.cacheDir
            val compressFileName =
                config.fileName
                    ?: "${System.currentTimeMillis()}.${config.compressFormat.name.lowercase()}"
            val compressFilePath = "$compressDirPath${File.separator}$compressFileName"
            val compressFile = File(compressFilePath)

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile.absolutePath, options)

            if (options.outWidth <= 0 || options.outHeight <= 0) return@withContext null

            options.inSampleSize = calculateInSampleSize(options, config.maxWidth, config.maxHeight)
            options.inJustDecodeBounds = false
            options.inPreferredConfig = config.bitmapConfig

            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
            val baos = ByteArrayOutputStream()
            bitmap.compress(config.compressFormat, config.quality, baos)

            if (baos.toByteArray().size > config.maxSize) {
                if (config.compressFormat == Bitmap.CompressFormat.PNG ||
                    config.subCompressPriorityType == CompressConfig.CompressPriorityType.TYPE_WIDTH_HEIGHT
                ) {
                    var maxWidth = config.maxWidth
                    var maxHeight = config.maxHeight
                    do {
                        maxWidth -= 50
                        maxHeight -= 50
                        baos.reset()
                        BitmapFactory.Options().apply {
                            inJustDecodeBounds = true
                            BitmapFactory.decodeFile(imageFile.absolutePath, this)
                            inSampleSize = calculateInSampleSize(this, maxWidth, maxHeight)
                            inJustDecodeBounds = false
                            inPreferredConfig = config.bitmapConfig
                            BitmapFactory.decodeFile(imageFile.absolutePath, this).let { bmp ->
                                bmp.compress(config.compressFormat, config.quality, baos)
                                bmp.recycle()
                            }
                        }
                    } while (baos.toByteArray().size > config.maxSize)
                } else {
                    var quality = config.quality
                    do {
                        quality -= 5
                        baos.reset()
                        bitmap.compress(config.compressFormat, quality, baos)
                    } while (baos.toByteArray().size > config.maxSize)
                }
            }
            bitmap.recycle()
            FileOutputStream(compressFile).use {
                it.write(baos.toByteArray())
                it.flush()
            }
            return@withContext compressFile
        }
    }
}