package com.ntduc.dynamicgrid

import android.widget.BaseAdapter

abstract class AbstractDynamicGridAdapter : BaseAdapter(), DynamicGridAdapterInterface {
    companion object {
        const val INVALID_ID = -1
    }

    private var nextStableId = 0
    private val mIdMap = HashMap<Any, Int>()

    /**
     * Adapter must have stable id
     */
    override fun hasStableIds(): Boolean {
        return true
    }

    /**
     * creates stable id for object
     */
    protected fun addStableId(item: Any) {
        mIdMap[item] = nextStableId++
    }

    /**
     * create stable ids for list
     */
    protected fun addAllStableId(items: ArrayList<Any>) {
        for (item in items) {
            addStableId(item)
        }
    }

    /**
     * get id for position
     */
    override fun getItemId(position: Int): Long {
        if (position < 0 || position >= mIdMap.size) {
            return INVALID_ID.toLong()
        }
        val item = getItem(position)
        return mIdMap[item]?.toLong() ?: 0L
    }

    /**
     * clear stable id map should called when clear adapter data;
     */
    protected fun clearStableIdMap() {
        mIdMap.clear()
    }

    /**
     * remove stable id for item. Should called on remove data item from adapter
     */
    protected fun removeStableID(item: Any) {
        mIdMap.remove(item)
    }
}