package com.ntduc.recyclerviewadvanced.animator.impl;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RemoveAnimationInfo extends ItemAnimationInfo {
    public RecyclerView.ViewHolder holder;

    public RemoveAnimationInfo(RecyclerView.ViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public RecyclerView.ViewHolder getAvailableViewHolder() {
        return holder;
    }

    @Override
    public void clear(@NonNull RecyclerView.ViewHolder item) {
        if (holder == item) {
            holder = null;
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "RemoveAnimationInfo{" +
                "holder=" + holder +
                '}';
    }
}
