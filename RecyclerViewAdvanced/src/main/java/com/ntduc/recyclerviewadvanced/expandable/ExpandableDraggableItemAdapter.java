package com.ntduc.recyclerviewadvanced.expandable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntduc.recyclerviewadvanced.draggable.ItemDraggableRange;
import com.ntduc.recyclerviewadvanced.draggable.RecyclerViewDragDropManager;

public interface ExpandableDraggableItemAdapter<GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> {
    /**
     * Called when user is attempt to drag the group item.
     *
     * @param holder        The group ViewHolder which is associated to item user is attempt to start dragging.
     * @param groupPosition Group position.
     * @param x             Touched X position. Relative from the itemView's top-left.
     * @param y             Touched Y position. Relative from the itemView's top-left.
     * @return Whether can start dragging.
     */
    boolean onCheckGroupCanStartDrag(@NonNull GVH holder, int groupPosition, int x, int y);

    /**
     * Called when user is attempt to drag the child item.
     *
     * @param holder        The child ViewHolder which is associated to item user is attempt to start dragging.
     * @param groupPosition Group position.
     * @param childPosition Child position.
     * @param x             Touched X position. Relative from the itemView's top-left.
     * @param y             Touched Y position. Relative from the itemView's top-left.
     * @return Whether can start dragging.
     */
    boolean onCheckChildCanStartDrag(@NonNull CVH holder, int groupPosition, int childPosition, int x, int y);

    /**
     * Called after the {@link #onCheckGroupCanStartDrag(androidx.recyclerview.widget.RecyclerView.ViewHolder, int, int, int)} method returned true.
     *
     * @param holder        The ViewHolder which is associated to item user is attempt to start dragging.
     * @param groupPosition Group position.
     * @return null: no constraints (= new ItemDraggableRange(0, getGroupCount() - 1)),
     * otherwise: the range specified item can be drag-sortable.
     */
    ItemDraggableRange onGetGroupItemDraggableRange(@NonNull GVH holder, int groupPosition);

    /**
     * Called after the {@link #onCheckChildCanStartDrag(androidx.recyclerview.widget.RecyclerView.ViewHolder, int, int, int, int)} method returned true.
     *
     * @param holder        The ViewHolder which is associated to item user is attempt to start dragging.
     * @param groupPosition Group position.
     * @param childPosition Child position.
     * @return null: no constraints (= new ItemDraggableRange(0, getGroupCount() - 1)),
     * otherwise: the range specified item can be drag-sortable.
     */
    ItemDraggableRange onGetChildItemDraggableRange(@NonNull CVH holder, int groupPosition, int childPosition);

    /**
     * Called when group item is moved. Should apply the move operation result to data set.
     *
     * @param fromGroupPosition Previous group position of the item.
     * @param toGroupPosition   New group position of the item.
     */
    void onMoveGroupItem(int fromGroupPosition, int toGroupPosition);

    /**
     * Called when child item is moved. Should apply the move operation result to data set.
     *
     * @param fromGroupPosition Previous group position of the item.
     * @param fromChildPosition Previous child position of the item.
     * @param toGroupPosition   New group position of the item.
     * @param toChildPosition   New child position of the item.
     */
    void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition);


    /**
     * Called while dragging in order to check whether the dragging item can be dropped to the specified position.
     * <p>
     * NOTE: This method will be called when the checkCanDrop option is enabled by {@link RecyclerViewDragDropManager#setCheckCanDropEnabled(boolean)}.
     *
     * @param draggingGroupPosition The position of the currently dragging group item.
     * @param dropGroupPosition     The position of a group item whether to check can be dropped or not.
     * @return Whether can be dropped to the specified position.
     */
    boolean onCheckGroupCanDrop(int draggingGroupPosition, int dropGroupPosition);


    /**
     * Called while dragging in order to check whether the dragging item can be dropped to the specified position.
     * <p>
     * NOTE: This method will be called when the checkCanDrop option is enabled by {@link RecyclerViewDragDropManager#setCheckCanDropEnabled(boolean)}.
     *
     * @param draggingGroupPosition The group position of the currently dragging item.
     * @param draggingChildPosition The child position of the currently dragging item.
     * @param dropGroupPosition     The group position to check whether the dragging item can be dropped or not.
     * @param dropChildPosition     The child position to check whether the dragging item can be dropped or not.
     * @return Whether can be dropped to the specified position.
     */
    boolean onCheckChildCanDrop(int draggingGroupPosition, int draggingChildPosition, int dropGroupPosition, int dropChildPosition);

    /**
     * Callback method to be invoked when started dragging a group item.
     * <p>
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param groupPosition The position of the group item.
     */
    void onGroupDragStarted(int groupPosition);

    /**
     * Callback method to be invoked when started dragging a child item.
     * <p>
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param groupPosition The group position of the item.
     * @param childPosition The child position of the item.
     */
    void onChildDragStarted(int groupPosition, int childPosition);

    /**
     * Callback method to be invoked when finished dragging a group item.
     * <p>
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param fromGroupPosition Previous position of the group item.
     * @param toGroupPosition   New position of the group item.
     * @param result            Indicates whether the dragging operation was succeeded.
     */
    void onGroupDragFinished(int fromGroupPosition, int toGroupPosition, boolean result);

    /**
     * Callback method to be invoked when finished dragging a child item.
     * <p>
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param fromGroupPosition Previous group position of the item.
     * @param fromChildPosition Previous child position of the item.
     * @param toGroupPosition   New group position of the item.
     * @param toChildPosition   New child position of the item.
     * @param result            Indicates whether the dragging operation was succeeded.
     */
    void onChildDragFinished(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition, boolean result);
}
