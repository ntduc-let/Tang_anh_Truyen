package com.android.ntduc.baseproject.ui.component.main

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.databinding.ActivityMainBinding
import com.android.ntduc.baseproject.ui.base.BaseActivity
import com.android.ntduc.baseproject.utils.clickeffect.setOnClickShrinkEffectListener
import com.android.ntduc.baseproject.utils.toast.shortToast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()

    companion object {
        private const val WIDTH = 3024
        private const val HEIGHT = 4032
    }

    private val selectFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val path = it.data?.data ?: return@registerForActivityResult
                lifecycleScope.launch(Dispatchers.IO) {
                    var inputStream: InputStream? = null
                    try {
                        inputStream = contentResolver.openInputStream(path)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        val resizeBitmap = resizeBitmap(bitmap, WIDTH, HEIGHT)
                        Log.d("ntduc_debug", "w: ${resizeBitmap.width}")
                        Log.d("ntduc_debug", "h: ${resizeBitmap.height}")
                        withContext(Dispatchers.Main) {
                            Glide.with(this@MainActivity).load(resizeBitmap).into(binding.img)
                        }
                    } catch (e: Exception) {
                        Log.d("ntduc_debug", "error: ${e.message}")
                    } finally {
                        inputStream?.close()
                    }
                }
            } else {
                shortToast("Cancel")
            }
        }

    override fun addEvent() {
        super.addEvent()

        binding.apply {
            choose.setOnClickShrinkEffectListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                val uri = Uri.parse(Environment.getDownloadCacheDirectory().path)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setDataAndType(uri, "image/*")
                selectFileLauncher.launch(intent)
            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        // Tính toán tỉ lệ phóng to/thu nhỏ cho chiều cao và chiều rộng
        val scaleWidth = targetWidth.toFloat() / originalWidth
        val scaleHeight = targetHeight.toFloat() / originalHeight

        // Chọn tỉ lệ phóng to/thu nhỏ lớn nhất để đảm bảo ảnh không bị cắt và tập trung vào giữa
        val scaleFactor = Math.max(scaleWidth, scaleHeight)

        // Tính toán kích thước mới dựa trên tỉ lệ phóng to/thu nhỏ
        val newWidth = (originalWidth * scaleFactor).toInt()
        val newHeight = (originalHeight * scaleFactor).toInt()

        // Tạo ma trận biến đổi để thực hiện phóng to/thu nhỏ
        val matrix = Matrix()
        matrix.postScale(scaleFactor, scaleFactor)

        // Phóng to/thu nhỏ bitmap với kích thước mới
        val scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true)

        // Tính toán điểm bắt đầu để cắt bỏ phần dư và tập trung vào giữa
        val xOffset = (newWidth - targetWidth) / 2
        val yOffset = (newHeight - targetHeight) / 2

        // Kiểm tra xem offset có âm hay không
        val x = if (xOffset < 0) 0 else xOffset
        val y = if (yOffset < 0) 0 else yOffset

        // Kiểm tra kích thước bitmap đầu ra có thoả mãn yêu cầu hay không
        val outputWidth = Math.min(targetWidth, scaledBitmap.width - x)
        val outputHeight = Math.min(targetHeight, scaledBitmap.height - y)

        // Tạo bitmap đầu ra có kích thước yêu cầu và tập trung vào giữa
        return Bitmap.createBitmap(scaledBitmap, x, y, outputWidth, outputHeight)
    }
}