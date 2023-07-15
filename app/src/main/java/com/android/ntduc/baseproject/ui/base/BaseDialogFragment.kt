package com.android.ntduc.baseproject.ui.base

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.skydoves.bindables.BindingDialogFragment
import kotlin.math.roundToInt

open class BaseDialogFragment<T : ViewDataBinding> constructor(
    @LayoutRes val contentLayoutId: Int,
    private val widthRatio: Float = 0.9f,
    private val heightRatio: Float = WRAP_CONTENT,
    private val isCanceledOnTouchOutside: Boolean = false,
    private val gravity: Int = Gravity.CENTER
) : BindingDialogFragment<T>(contentLayoutId) {

    companion object {
        const val MATCH_PARENT = -1f
        const val WRAP_CONTENT = -2f
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mDialog = dialog
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
            if (mDialog.window != null) {
                mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                mDialog.window!!.setLayout(
                    if (widthRatio == WRAP_CONTENT) WindowManager.LayoutParams.WRAP_CONTENT else if (widthRatio == MATCH_PARENT) WindowManager.LayoutParams.MATCH_PARENT else (requireActivity().resources.displayMetrics.widthPixels * widthRatio).roundToInt(),
                    if (heightRatio == WRAP_CONTENT) WindowManager.LayoutParams.WRAP_CONTENT else if (heightRatio == MATCH_PARENT) WindowManager.LayoutParams.MATCH_PARENT else (requireActivity().resources.displayMetrics.heightPixels * heightRatio).roundToInt()
                )

                val layoutParams = mDialog.window!!.attributes
                layoutParams.gravity = gravity
                mDialog.window!!.attributes = layoutParams
            }
        }

        initView()
        addEvent()
        addObservers()
        initData()
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissListener?.let {
            it()
        }
        super.onDismiss(dialog)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (isAdded) {
            return
        }
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun dismiss() {
        if (isAdded) {
            try {
                super.dismissAllowingStateLoss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    open fun initView() {}

    open fun addEvent() {}

    open fun addObservers() {}

    open fun initData() {}

    private var onDismissListener: (() -> Unit)? = null

    fun setOnDismissListener(listener: () -> Unit) {
        onDismissListener = listener
    }
}