package com.ntduc.recyclerviewadvanced.swipeable.action;

import com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager;

public class SwipeResultActionDoNothing extends SwipeResultAction {
    public SwipeResultActionDoNothing() {
        super(RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_DO_NOTHING);
    }
}
