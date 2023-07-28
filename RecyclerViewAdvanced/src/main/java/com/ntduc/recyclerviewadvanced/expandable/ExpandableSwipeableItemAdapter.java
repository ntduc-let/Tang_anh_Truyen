package com.ntduc.recyclerviewadvanced.expandable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntduc.recyclerviewadvanced.swipeable.action.SwipeResultAction;

public interface ExpandableSwipeableItemAdapter<GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends BaseExpandableSwipeableItemAdapter<GVH, CVH> {

    /**
     * Called when group item is swiped.
     * <p>
     * *Note that do not change data set and do not call notifyDataXXX() methods inside of this method.*
     *
     * @param holder        The ViewHolder which is associated to the swiped item.
     * @param groupPosition Group position.
     * @param result        The result code of user's swipe operation.
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_CANCELED},
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_LEFT},
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_UP},
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_RIGHT} or
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_DOWN}
     * @return {@link SwipeResultAction} object.
     */
    SwipeResultAction onSwipeGroupItem(@NonNull GVH holder, int groupPosition, int result);

    /**
     * Called when child item is swiped.
     * <p>
     * *Note that do not change data set and do not call notifyDataXXX() methods inside of this method.*
     *
     * @param holder        The ViewHolder which is associated to the swiped item.
     * @param groupPosition Group position.
     * @param childPosition Child position.
     * @param result        The result code of user's swipe operation.
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_CANCELED},
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_LEFT},
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_UP},
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_RIGHT} or
     *                      {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_DOWN}
     * @return {@link SwipeResultAction} object.
     */
    SwipeResultAction onSwipeChildItem(@NonNull CVH holder, int groupPosition, int childPosition, int result);
}
