package com.ntduc.recyclerviewadvanced.draggable.annotation;

import androidx.annotation.IntDef;

import com.ntduc.recyclerviewadvanced.draggable.DraggableItemConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(flag = true, value = {
        DraggableItemConstants.STATE_FLAG_DRAGGING,
        DraggableItemConstants.STATE_FLAG_IS_ACTIVE,
        DraggableItemConstants.STATE_FLAG_IS_IN_RANGE,
        DraggableItemConstants.STATE_FLAG_IS_UPDATED,
})
@Retention(RetentionPolicy.SOURCE)
public @interface DraggableItemStateFlags {
}
