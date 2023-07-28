package com.ntduc.dynamicgrid

interface DynamicGridAdapterInterface {
    /**
     * Determines how to reorder items dragged from `originalPosition` to `newPosition`
     */
    fun reorderItems(originalPosition: Int, newPosition: Int)

    /**
     * @return return columns number for GridView. Need for compatibility
     */
    val columnCount: Int

    /**
     * Determines whether the item in the specified `position` can be reordered.
     */
    fun canReorder(position: Int): Boolean
}