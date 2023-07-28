package com.ntduc.recyclerviewadvanced.expandable;

import androidx.annotation.NonNull;

import com.ntduc.recyclerviewadvanced.draggable.ItemDraggableRange;

public class GroupPositionItemDraggableRange extends ItemDraggableRange {
    public GroupPositionItemDraggableRange(int start, int end) {
        super(start, end);
    }

    @NonNull
    protected String getClassName() {
        return "GroupPositionItemDraggableRange";
    }
}
