package com.ntduc.recyclerviewadvanced.swipeable.action;

import com.ntduc.recyclerviewadvanced.swipeable.RecyclerViewSwipeManager;

public abstract class SwipeResultActionRemoveItem extends SwipeResultAction {
    public SwipeResultActionRemoveItem() {
        super(RecyclerViewSwipeManager.AFTER_SWIPE_REACTION_REMOVE_ITEM);
    }
}
