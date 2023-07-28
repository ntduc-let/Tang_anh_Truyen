package com.ntduc.recyclerviewadvanced.utils;

import android.view.View;

import androidx.annotation.NonNull;

import com.ntduc.recyclerviewadvanced.draggable.DraggableItemState;
import com.ntduc.recyclerviewadvanced.draggable.DraggableItemViewHolder;
import com.ntduc.recyclerviewadvanced.draggable.annotation.DraggableItemStateFlags;

public abstract class AbstractDraggableSwipeableItemViewHolder extends AbstractSwipeableItemViewHolder implements DraggableItemViewHolder {
    private final DraggableItemState mDragState = new DraggableItemState();

    public AbstractDraggableSwipeableItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDragStateFlags(@DraggableItemStateFlags int flags) {
        mDragState.setFlags(flags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @DraggableItemStateFlags
    public int getDragStateFlags() {
        return mDragState.getFlags();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public DraggableItemState getDragState() {
        return mDragState;
    }
}
