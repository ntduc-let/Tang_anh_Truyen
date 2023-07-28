package com.ntduc.recyclerviewadvanced.animator.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MoveAnimationInfo extends ItemAnimationInfo {
    public RecyclerView.ViewHolder holder;
    public final int fromX;
    public final int fromY;
    public final int toX;
    public final int toY;

    public MoveAnimationInfo(@NonNull RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        this.holder = holder;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    @Override
    @Nullable
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
        return "MoveAnimationInfo{" +
                "holder=" + holder +
                ", fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                '}';
    }
}