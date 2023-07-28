package com.ntduc.recyclerviewadvanced.swipeable;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntduc.recyclerviewadvanced.swipeable.annotation.SwipeableItemAfterReactions;

/**
 * Interface which provides required information for swiping item.
 * <p>
 * Implement this interface on your sub-class of the {@link androidx.recyclerview.widget.RecyclerView.ViewHolder}.
 */
public interface SwipeableItemViewHolder {
    /**
     * Sets the state flags value for swiping item
     *
     * @param flags Bitwise OR of these flags;
     *              - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#STATE_FLAG_SWIPING}
     *              - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#STATE_FLAG_IS_ACTIVE}
     *              - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#STATE_FLAG_IS_UPDATED}
     * @see #getSwipeState()
     */
    void setSwipeStateFlags(int flags);

    /**
     * Gets the state flags value for swiping item. You can access these flags more human friendly way through {@link #getSwipeState()}.
     *
     * @return Bitwise OR of these flags;
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#STATE_FLAG_SWIPING}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#STATE_FLAG_IS_ACTIVE}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.SwipeableItemConstants#STATE_FLAG_IS_UPDATED}
     * @see #getSwipeState()
     */
    int getSwipeStateFlags();

    /**
     * Gets the state object for swipeable item.
     * This method can be used inside of the {@link androidx.recyclerview.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}.
     *
     * @return {@link SwipeableItemState} object
     */
    @NonNull
    SwipeableItemState getSwipeState();

    /**
     * Sets the result code of swiping item.
     *
     * @param result Result code. One of these values;
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_NONE}
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_LEFT}
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_UP}
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_RIGHT}
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_DOWN}
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_CANCELED}
     */
    void setSwipeResult(int result);

    /**
     * Gets the result code of swiping item.
     *
     * @return Result code. One of these values;
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_NONE}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_LEFT}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_UP}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_RIGHT}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_SWIPED_DOWN}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#RESULT_CANCELED}
     */
    int getSwipeResult();

    /**
     * Sets the reaction type of after swiping item.
     *
     * @param reaction After-reaction type. One of these values;
     *                 - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_DEFAULT}
     *                 - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_MOVE_TO_ORIGIN}
     *                 - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_REMOVE_ITEM}
     *                 - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION}
     *                 - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_DO_NOTHING}
     */
    void setAfterSwipeReaction(@SwipeableItemAfterReactions int reaction);

    /**
     * Gets the reaction type of after swiping item.
     *
     * @return After-reaction type. One of these values;
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_DEFAULT}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_MOVE_TO_ORIGIN}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_REMOVE_ITEM}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#AFTER_SWIPE_REACTION_DO_NOTHING}
     */
    @SwipeableItemAfterReactions
    int getAfterSwipeReaction();

    /**
     * Sets proportional swipe amount mode enabled.
     *
     * @param enabled True if swipe amount is specified in proportional value, otherwise amount is specified in pixels.
     */
    void setProportionalSwipeAmountModeEnabled(boolean enabled);

    /**
     * Gets whether the proportional swipe amount mode enabled.
     *
     * @return True if swipe amount is specified in proportional value, otherwise false.
     */
    boolean isProportionalSwipeAmountModeEnabled();

    /**
     * Sets the amount of horizontal swipe.
     *
     * @param amount Item swipe amount. Generally the range is [-1.0 .. 1.0] if the proportional amount mode is enabled, otherwise distance in pixels.
     *               In additionally, these special values can also be accepted;
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_LEFT}
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_RIGHT}
     */
    void setSwipeItemHorizontalSlideAmount(float amount);

    /**
     * Gets the amount of horizontal swipe.
     *
     * @return Item swipe amount. Generally the range is [-1.0 .. 1.0] if the proportional amount mode is enabled, otherwise distance in pixels. Additionally these special values may also be returned;
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_LEFT}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_RIGHT}
     */
    float getSwipeItemHorizontalSlideAmount();

    /**
     * Sets the amount of vertical swipe.
     *
     * @param amount Item swipe amount. Generally the range is [-1.0 .. 1.0] if the proportional amount mode is enabled, otherwise distance in pixels.
     *               In additionally, these special values can also be accepted;
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_TOP}
     *               - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_BOTTOM}
     */
    void setSwipeItemVerticalSlideAmount(float amount);

    /**
     * Gets the amount of vertical swipe.
     *
     * @return Item swipe amount. Generally the range is [-1.0 .. 1.0] if the proportional amount mode is enabled, otherwise distance in pixels. Additionally these special values may also be returned;
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_TOP}
     * - {@link com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager#OUTSIDE_OF_THE_WINDOW_BOTTOM}
     */
    float getSwipeItemVerticalSlideAmount();

    /**
     * Sets the maximum item left swipe amount.
     *
     * @param amount Item swipe amount.
     *               If the item is configured as proportional amount mode, specify limit amount in range of [-1.0 .. 1.0].
     *               Otherwise, specify limit distance in pixels.
     */
    void setMaxLeftSwipeAmount(float amount);

    /**
     * Gets the maximum item left swipe amount.
     *
     * @return Item swipe amount. If the item is configured as proportional amount mode, generally the range is [-1.0 .. 1.0], otherwise the value is specified in pixels.
     */
    float getMaxLeftSwipeAmount();

    /**
     * Sets the maximum item up swipe amount.
     *
     * @param amount Item swipe amount.
     *               If the item is configured as proportional amount mode, specify limit amount in range of [-1.0 .. 1.0].
     *               Otherwise, specify limit distance in pixels.
     */
    void setMaxUpSwipeAmount(float amount);

    /**
     * Gets the maximum item up swipe amount.
     *
     * @return Item swipe amount. If the item is configured as proportional amount mode, generally the range is [-1.0 .. 1.0], otherwise the value is specified in pixels.
     */
    float getMaxUpSwipeAmount();

    /**
     * Sets the maximum item right swipe amount.
     *
     * @param amount Item swipe amount.
     *               If the item is configured as proportional amount mode, specify limit amount in range of [-1.0 .. 1.0].
     *               Otherwise, specify limit distance in pixels.
     */
    void setMaxRightSwipeAmount(float amount);

    /**
     * Gets the maximum item right swipe amount.
     *
     * @return Item swipe amount. If the item is configured as proportional amount mode, generally the range is [-1.0 .. 1.0], otherwise the value is specified in pixels.
     */
    float getMaxRightSwipeAmount();

    /**
     * Sets the maximum item down swipe amount.
     *
     * @param amount Item swipe amount.
     *               If the item is configured as proportional amount mode, specify limit amount in range of [-1.0 .. 1.0].
     *               Otherwise, specify limit distance in pixels.
     */
    void setMaxDownSwipeAmount(float amount);

    /**
     * Gets the maximum item down swipe amount.
     *
     * @return Item swipe amount. If the item is configured as proportional amount mode, generally the range is [-1.0 .. 1.0], otherwise the value is specified in pixels.
     */
    float getMaxDownSwipeAmount();

    /**
     * Gets the container view for the swipeable area.
     * <p>NOTE: Please DO NOT return <code>itemView</code> for this method.
     * An IllegalArgumentException with massage "Tmp detached view should be removed from RecyclerView before it can be recycled"
     * will be raised by RecyclerView.Recycler.</p>
     *
     * @return The container view instance.
     */
    @NonNull
    View getSwipeableContainerView();

    /**
     * Called when sets background of the swiping item.
     *
     * @param horizontalAmount Horizontal slide amount of the item view. (slide left: &lt; 0, slide right: 0 &gt;)
     * @param verticalAmount   Vertical slide amount of the item view. (slide up: &lt; 0, slide down: 0 &gt;)
     * @param isSwiping        Indicates whether the user is swiping the item or not
     */
    void onSlideAmountUpdated(float horizontalAmount, float verticalAmount, boolean isSwiping);
}
