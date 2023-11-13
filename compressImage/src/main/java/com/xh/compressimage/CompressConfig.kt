package com.xh.compressimage

import android.graphics.Bitmap

data class CompressConfig(
    val maxWidth: Int,
    val maxHeight: Int,
    val maxSize: Int,
    val quality: Int,
    val subCompressPriorityType: CompressPriorityType,
    val compressFormat: Bitmap.CompressFormat,
    val bitmapConfig: Bitmap.Config,
    val destDirPath: String?,
    val fileName: String?
) {

    class Builder {

        /**
         * 最大宽度，默认为720
         */
        private var maxWidth = 720

        /**
         * 最大高度，默认为960
         */
        private var maxHeight = 960

        /**
         * 最大大小，默认为256kb
         */
        private var maxSize = 256.KB

        /**
         * 默认压缩质量为80
         */
        private var quality = 80

        /**
         * 经过最大宽高和压缩质量的压缩后，如果仍未满足文件大小要求需要继续压缩的话，优先的压缩类型，默认为质量压缩
         */
        private var subCompressPriorityType = CompressPriorityType.TYPE_QUALITY

        /**
         * 默认压缩后的格式为JPEG
         */
        private var compressFormat = Bitmap.CompressFormat.JPEG

        /**
         * 默认的图片处理方式是RGB_565
         */
        private var bitmapConfig = Bitmap.Config.RGB_565

        /**
         * 存储路径
         */
        private var destDirPath: String? = null

        /**
         * 文件名
         */
        private var fileName: String? = null


        fun setMaxWidth(maxWidth: Int): Builder {
            this.maxWidth = maxWidth
            return this
        }

        fun setMaxHeight(maxHeight: Int): Builder {
            this.maxHeight = maxHeight
            return this
        }

        fun setMaxSize(maxSize: Int): Builder {
            this.maxSize = maxSize
            return this
        }

        fun setQuality(quality: Int): Builder {
            this.quality = quality
            return this
        }

        fun setSubCompressPriorityType(type: CompressPriorityType): Builder {
            this.subCompressPriorityType = type
            return this
        }

        fun setCompressFormat(compressFormat: Bitmap.CompressFormat): Builder {
            this.compressFormat = compressFormat
            return this
        }

        fun setBitmapConfig(bitmapConfig: Bitmap.Config): Builder {
            this.bitmapConfig = bitmapConfig
            return this
        }

        fun setDestDirPath(destDirPath: String): Builder {
            this.destDirPath = destDirPath
            return this
        }

        fun setFileName(fileName: String): Builder {
            this.fileName = fileName
            return this
        }

        fun build(): CompressConfig {
            return CompressConfig(
                maxWidth,
                maxHeight,
                maxSize,
                quality,
                subCompressPriorityType,
                compressFormat,
                bitmapConfig,
                destDirPath,
                fileName
            )
        }
    }

    enum class CompressPriorityType {
        TYPE_WIDTH_HEIGHT, TYPE_QUALITY
    }
}