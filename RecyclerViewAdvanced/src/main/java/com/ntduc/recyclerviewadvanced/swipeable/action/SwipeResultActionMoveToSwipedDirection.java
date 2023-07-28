package com.ntduc.recyclerviewadvanced.swipeable.action;

import com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager;

public abstract class SwipeResultActionMoveToSwipedDirection extends SwipeResultAction {
    public SwipeResultActionMoveToSwipedDirection() {
        super(RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_MOVE_TO_SWIPED_DIRECTION);
    }
}
