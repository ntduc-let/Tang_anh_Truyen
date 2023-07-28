package com.ntduc.recyclerviewadvanced.expandable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntduc.recyclerviewadvanced.swipeable.annotation.SwipeableItemDrawableTypes;
import com.ntduc.recyclerviewadvanced.swipeable.annotation.SwipeableItemReactions;

public interface BaseExpandableSwipeableItemAdapter<GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> {
    /**
     * Called when user is attempt to swipe the group item.
     *
     * @param holder The ViewHolder which is associated to item user is attempt to start swiping.
     * @param groupPosition Group position.
     * @param x Touched X position. Relative from the itemView's top-left.
     * @param y Touched Y position. Relative from the itemView's top-left.

     * @return Reaction type. Bitwise OR of these flags;
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_LEFT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_LEFT_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_LEFT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_UP}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_UP_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_UP}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_RIGHT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_RIGHT_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_RIGHT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_DOWN}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_DOWN_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_DOWN}
     */
    @SwipeableItemReactions
    int onGetGroupItemSwipeReactionType(@NonNull GVH holder, int groupPosition, int x, int y);

    /**
     * Called when user is attempt to swipe the child item.
     *
     * @param holder The ViewHolder which is associated to item user is attempt to start swiping.
     * @param groupPosition Group position.
     * @param childPosition Child position.
     * @param x Touched X position. Relative from the itemView's top-left.
     * @param y Touched Y position. Relative from the itemView's top-left.

     * @return Reaction type. Bitwise OR of these flags;
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_LEFT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_LEFT_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_LEFT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_UP}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_UP_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_UP}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_RIGHT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_RIGHT_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_RIGHT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_DOWN}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_NOT_SWIPE_DOWN_WITH_RUBBER_BAND_EFFECT}
     *         - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#REACTION_CAN_SWIPE_DOWN}
     */
    @SwipeableItemReactions
    int onGetChildItemSwipeReactionType(@NonNull CVH holder, int groupPosition, int childPosition, int x, int y);

    /**
     * Called when started swiping a group item.
     *
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param holder The ViewHolder that is associated the swiped item.
     * @param groupPosition Group position.
     */
    void onSwipeGroupItemStarted(@NonNull GVH holder, int groupPosition);

    /**
     * Called when started swiping a child item.
     *
     * Call the {@link RecyclerView.Adapter#notifyDataSetChanged()} method in this callback to get the same behavior with v0.10.x or before.
     *
     * @param holder The ViewHolder that is associated the swiped item.
     * @param groupPosition Group position.
     * @param childPosition Child position.
     */
    void onSwipeChildItemStarted(@NonNull CVH holder, int groupPosition, int childPosition);

    /**
     * Called when sets background of the swiping group item.
     *
     * @param holder The ViewHolder which is associated to the swiping item.
     * @param groupPosition Group position.
     * @param type Background type. One of the
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_NEUTRAL_BACKGROUND},
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_LEFT_BACKGROUND},
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_UP_BACKGROUND},
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_RIGHT_BACKGROUND} or
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_DOWN_BACKGROUND}.
     */
    void onSetGroupItemSwipeBackground(@NonNull GVH holder, int groupPosition, @SwipeableItemDrawableTypes int type);


    /**
     * Called when sets background of the swiping child item.
     *
     * @param holder The ViewHolder which is associated to the swiping item.
     * @param groupPosition Group position.
     * @param childPosition Child position.
     * @param type Background type. One of the
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_NEUTRAL_BACKGROUND},
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_LEFT_BACKGROUND},
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_UP_BACKGROUND},
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_RIGHT_BACKGROUND} or
     *          {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#DRAWABLE_SWIPE_DOWN_BACKGROUND}.
     */
    void onSetChildItemSwipeBackground(@NonNull CVH holder, int groupPosition, int childPosition, @SwipeableItemDrawableTypes int type);
}
