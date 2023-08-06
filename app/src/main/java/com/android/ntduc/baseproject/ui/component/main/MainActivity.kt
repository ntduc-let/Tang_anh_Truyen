package com.android.ntduc.baseproject.ui.component.main

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieImageAsset
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.databinding.ActivityMainBinding
import com.android.ntduc.baseproject.ui.base.BaseActivity
import com.android.ntduc.baseproject.utils.clickeffect.setOnClickShrinkEffectListener
import com.android.ntduc.baseproject.utils.file.delete
import com.android.ntduc.baseproject.utils.file.open
import com.android.ntduc.baseproject.utils.lottie.FrameCreator
import com.android.ntduc.baseproject.utils.lottie.Recorder
import com.android.ntduc.baseproject.utils.lottie.RecordingOperation
import com.android.ntduc.baseproject.utils.toast.shortToast
import com.android.ntduc.baseproject.utils.view.gone
import com.android.ntduc.baseproject.utils.view.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModels()

    companion object {
        private const val WIDTH = 1080
        private const val HEIGHT = 1920
    }

    @SuppressLint("RestrictedApi")
    private val selectFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val path = it.data?.data ?: return@registerForActivityResult
                binding.loading.visible()
                binding.lottie.gone()
                if (binding.lottie.isAnimating){
                    binding.lottie.pauseAnimation()
                }
                lifecycleScope.launch(Dispatchers.IO) {
                    var resizeBitmap: Bitmap? = null
                    var inputStream: InputStream? = null
                    try {
                        inputStream = contentResolver.openInputStream(path)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        resizeBitmap = resizeAndCropBitmap(bitmap, HEIGHT, WIDTH)
//                        bitmap.recycle()
                    } catch (e: Exception) {
                        Log.d("ntduc_debug", "error: ${e.message}")
                    } finally {
                        inputStream?.close()
                    }
                    if (resizeBitmap == null) {
                        withContext(Dispatchers.Main) {
                            binding.loading.gone()
                            binding.lottie.visible()
                        }
                        return@launch
                    }

                    val byteArrayOutputStream = ByteArrayOutputStream()
                    resizeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    val base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    withContext(Dispatchers.Main) {
                        val outputImage = binding.lottie.composition?.images?.get("image_1")
                        if (outputImage != null){
                            binding.lottie.composition!!.images!!["image_1"] = LottieImageAsset(outputImage.width, outputImage.height, outputImage.id, "data:image/png;base64,$base64", outputImage.dirName)
                        }

                        binding.loading.gone()
                        binding.lottie.visible()
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

            export.setOnClickShrinkEffectListener {
                val lottieDrawable = LottieDrawable()
                lottieDrawable.composition = binding.lottie.composition
                lottieDrawable.bounds = Rect(0, 0, WIDTH, HEIGHT)

                binding.loading.visible()
                binding.lottie.gone()

                lifecycleScope.launch(Dispatchers.IO) {
                    val path = File(cacheDir, Environment.DIRECTORY_PICTURES).apply { mkdirs() }
                    val videoFile = File(path, "lottie_in_video.mp4").apply {
                        if (exists()) delete(this@MainActivity)
                    }
                    val recordingOperation = RecordingOperation(
                        Recorder(videoOutput = videoFile, width = WIDTH, height = HEIGHT),
                        FrameCreator(lottieDrawable)
                    ) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            binding.loading.gone()
                            videoFile.open(this@MainActivity, "$packageName.provider")
                        }
                    }
                    recordingOperation.start()
                }
            }
        }
    }

    private fun resizeAndCropBitmap(bitmap: Bitmap, targetHeight: Int, targetWidth: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        // Tính toán tỉ lệ phóng to/thu nhỏ cho chiều cao và chiều rộng
        val scaleWidth = targetWidth.toFloat() / originalWidth
        val scaleHeight = targetHeight.toFloat() / originalHeight

        // Chọn tỉ lệ phóng to/thu nhỏ lớn nhất để đảm bảo ảnh không bị cắt và tập trung vào giữa
        val scaleFactor = max(scaleWidth, scaleHeight)

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
        val outputWidth = min(targetWidth, scaledBitmap.width - x)
        val outputHeight = min(targetHeight, scaledBitmap.height - y)

        // Tạo bitmap đầu ra có kích thước yêu cầu và tập trung vào giữa
        val croppedBitmap = Bitmap.createBitmap(scaledBitmap, x, y, outputWidth, outputHeight)

        // Recycle bitmap đã scale, vì chỉ cần dùng đến croppedBitmap
//        scaledBitmap.recycle()

        return croppedBitmap
    }
}