package com.ntduc.recyclerviewadvanced.draggable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public interface DraggableItemAdapter<T extends RecyclerView.ViewHolder> {

    /**
     * Called when user is attempt to drag the item.
     *
     * @param holder   The ViewHolder which is associated to item user is attempt to start dragging.
     * @param position The position of the item within the adapter's data set.
     * @param x        Touched X position. Relative from the itemView's top-left.
     * @param y        Touched Y position. Relative from the itemView's top-left.
     * @return Whether can start dragging.
     */
    boolean onCheckCanStartDrag(@NonNull T holder, int position, int x, int y);

    /**
     * Called after the {@link #onCheckCanStartDrag(androidx.recyclerview.widget.RecyclerView.ViewHolder, int, int, int)} method returned true.
     *
     * @param holder   The ViewHolder which is associated to item user is attempt to start dragging.
     * @param position The position of the item within the adapter's data set.
     * @return null: no constraints (= new ItemDraggableRange(0, getItemCount() - 1)),
     * otherwise: the range specified item can be drag-sortable.
     */
    @Nullable
    ItemDraggableRange onGetItemDraggableRange(@NonNull T holder, int position);

    /**
     * Called when item is moved. Should apply the move operation result to data set.
     *
     * @param fromPosition Previous position of the item.
     * @param toPosition   New position of the item.
     */
    void onMoveItem(int fromPosition, int toPosition);

    /**
     * Called while dragging in order to check whether the dragging item can be dropped to the specified position.
     * <p>
     * NOTE: This method will be called when the checkCanDrop option is enabled by {@link RecyclerViewDragDropManager#setCheckCanDropEnabled(boolean)}.
     *
     * @param draggingPosition The position of the currently dragging item.
     * @param dropPosition     The position to check whether the dragging item can be dropped or not.
     * @return Whether can be dropped to the specified position.
     */
    boolean onCheckCanDrop(int draggingPosition, int dropPosition);

    /**
     * Callback method to be invoked when started dragging an item.
     * <p>
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param position The position of the item.
     */
    void onItemDragStarted(int position);

    /**
     * Callback method to be invoked when finished dragging an item.
     * <p>
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param fromPosition Previous position of the item.
     * @param toPosition   New position of the item.
     * @param result       Indicates whether the dragging operation was succeeded.
     */
    void onItemDragFinished(int fromPosition, int toPosition, boolean result);
}
