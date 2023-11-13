package com.xh.compressimage

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.PictureFileUtils
import kotlinx.coroutines.launch
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn).setOnClickListener {
            selectPicture()
        }
    }

    private fun selectPicture() {
        PictureSelector.create(this).openSystemGallery(SelectMimeType.ofImage())
            .forSystemResult(object : OnResultCallbackListener<LocalMedia> {
                /**
                 * return LocalMedia result
                 *
                 * @param result
                 */
                override fun onResult(result: ArrayList<LocalMedia>?) {
                    result?.forEach {
                        lifecycleScope.launch {
                            CompressHelper.compressImage(
                                this@MainActivity,
                                File(
                                    PictureFileUtils.getPath(
                                        this@MainActivity,
                                        Uri.parse(it.path)
                                    )
                                ),
                                maxWidth = 1200,
                                maxHeight = 1600,
                                quality = 100,
                                subCompressPriorityType = CompressConfig.CompressPriorityType.TYPE_WIDTH_HEIGHT
                            )
                        }
                    }
                }

                /**
                 * Cancel
                 */
                override fun onCancel() {
                    Toast.makeText(this@MainActivity, "取消选择", Toast.LENGTH_SHORT).show()
                }
            })
    }
}