package com.ntduc.recyclerviewadvanced.expandable;

import androidx.annotation.NonNull;

import com.ntduc.recyclerviewadvanced.draggable.ItemDraggableRange;

public class ChildPositionItemDraggableRange extends ItemDraggableRange {
    public ChildPositionItemDraggableRange(int start, int end) {
        super(start, end);
    }

    @NonNull
    protected String getClassName() {
        return "ChildPositionItemDraggableRange";
    }
}
