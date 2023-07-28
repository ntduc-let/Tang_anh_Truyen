package com.ntduc.dynamicgrid

import android.content.Context
import com.ntduc.dynamicgrid.DynamicGridUtils.reorder

abstract class BaseDynamicGridAdapter : AbstractDynamicGridAdapter {

    protected var context: Context
        private set

    private val mItems = ArrayList<Any>()

    val items: ArrayList<Any>
        get() = mItems

    private var mColumnCount: Int

    protected constructor(context: Context, columnCount: Int) {
        this.context = context
        mColumnCount = columnCount
    }

    constructor(context: Context, items: ArrayList<Any>, columnCount: Int) {
        this.context = context
        mColumnCount = columnCount
        init(items)
    }

    private fun init(items: ArrayList<Any>) {
        addAllStableId(items)
        mItems.addAll(items)
    }

    fun set(items: ArrayList<Any>) {
        clear()
        init(items)
        notifyDataSetChanged()
    }

    fun clear() {
        clearStableIdMap()
        mItems.clear()
        notifyDataSetChanged()
    }

    fun add(item: Any) {
        addStableId(item)
        mItems.add(item)
        notifyDataSetChanged()
    }

    fun add(position: Int, item: Any) {
        addStableId(item)
        mItems.add(position, item)
        notifyDataSetChanged()
    }

    fun add(items: ArrayList<Any>) {
        addAllStableId(items)
        mItems.addAll(items)
        notifyDataSetChanged()
    }

    fun remove(item: Any) {
        mItems.remove(item)
        removeStableID(item)
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override var columnCount: Int
        get() = mColumnCount
        set(columnCount) {
            mColumnCount = columnCount
            notifyDataSetChanged()
        }

    override fun reorderItems(originalPosition: Int, newPosition: Int) {
        if (newPosition < count) {
            reorder(mItems, originalPosition, newPosition)
            notifyDataSetChanged()
        }
    }

    override fun canReorder(position: Int): Boolean {
        return true
    }
}