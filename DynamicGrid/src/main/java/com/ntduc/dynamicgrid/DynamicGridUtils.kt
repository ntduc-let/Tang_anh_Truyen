package com.ntduc.dynamicgrid

import android.view.View
import kotlin.math.abs

object DynamicGridUtils {

    /**
     * Delete item in `list` from position `indexFrom` and insert it to `indexTwo`
     */
    @JvmStatic
    fun reorder(list: ArrayList<Any>, indexFrom: Int, indexTwo: Int) {
        val obj: Any = list.removeAt(indexFrom)
        list.add(indexTwo, obj)
    }

    /**
     * Swap item in list at position firstIndex with item at position secondIndex
     */
    fun swap(list: MutableList<Any>, firstIndex: Int, secondIndex: Int) {
        val firstObject = list[firstIndex]
        val secondObject = list[secondIndex]
        list[firstIndex] = secondObject
        list[secondIndex] = firstObject
    }

    fun getViewX(view: View): Float {
        return abs((view.right - view.left) / 2).toFloat()
    }

    fun getViewY(view: View): Float {
        return abs((view.bottom - view.top) / 2).toFloat()
    }
}