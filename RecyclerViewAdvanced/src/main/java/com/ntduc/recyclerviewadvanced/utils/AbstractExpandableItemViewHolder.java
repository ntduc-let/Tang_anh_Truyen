package com.ntduc.recyclerviewadvanced.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntduc.recyclerviewadvanced.expandable.ExpandableItemState;
import com.ntduc.recyclerviewadvanced.expandable.ExpandableItemViewHolder;
import com.ntduc.recyclerviewadvanced.expandable.annotation.ExpandableItemStateFlags;

public abstract class AbstractExpandableItemViewHolder extends RecyclerView.ViewHolder implements ExpandableItemViewHolder {
    private final ExpandableItemState mExpandState = new ExpandableItemState();

    public AbstractExpandableItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setExpandStateFlags(@ExpandableItemStateFlags int flags) {
        mExpandState.setFlags(flags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ExpandableItemStateFlags
    public int getExpandStateFlags() {
        return mExpandState.getFlags();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public ExpandableItemState getExpandState() {
        return mExpandState;
    }
}
