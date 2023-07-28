package com.ntduc.recyclerviewadvanced.draggable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntduc.recyclerviewadvanced.draggable.annotation.DraggableItemStateFlags;

/**
 * Interface which provides required information for dragging item.
 * <p>
 * Implement this interface on your sub-class of the {@link androidx.recyclerview.widget.RecyclerView.ViewHolder}.
 */
public interface DraggableItemViewHolder {
    /**
     * Sets the state flags value for dragging item
     *
     * @param flags Bitwise OR of these flags;
     *              - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_DRAGGING}
     *              - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_IS_ACTIVE}
     *              - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_IS_IN_RANGE}
     *              - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_IS_UPDATED}
     * @see #getDragState()
     */
    void setDragStateFlags(@DraggableItemStateFlags int flags);

    /**
     * Gets the state flags value for dragging item. You can access these flags more human friendly way through {@link #getDragState()}.
     *
     * @return Bitwise OR of these flags;
     * - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_DRAGGING}
     * - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_IS_ACTIVE}
     * - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_IS_IN_RANGE}
     * - {@link com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants#STATE_FLAG_IS_UPDATED}
     * @see #getDragState()
     */
    @DraggableItemStateFlags
    int getDragStateFlags();

    /**
     * Gets the state object for dragging item.
     * This method can be used inside of the {@link androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}.
     *
     * @return {@link DraggableItemState} object
     */
    @NonNull
    DraggableItemState getDragState();
}
