package com.ntduc.recyclerviewadvanced.animator.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ItemAnimationInfo {
    @Nullable
    public abstract RecyclerView.ViewHolder getAvailableViewHolder();

    public abstract void clear(@NonNull RecyclerView.ViewHolder holder);
}

