package com.ntduc.recyclerviewadvanced.composedadapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntduc.recyclerviewadvanced.adapter.BridgeAdapterDataObserver;

import java.util.ArrayList;
import java.util.List;

class ComposedChildAdapterDataObserver extends BridgeAdapterDataObserver {
    public ComposedChildAdapterDataObserver(@NonNull Subscriber subscriber, @NonNull RecyclerView.Adapter sourceAdapter) {
        super(subscriber, sourceAdapter, new ArrayList<ComposedChildAdapterTag>());
    }

    @SuppressWarnings("unchecked")
    @NonNull
    private List<ComposedChildAdapterTag> getChildAdapterTags() {
        return (List<ComposedChildAdapterTag>) getTag();
    }

    public void registerChildAdapterTag(@NonNull ComposedChildAdapterTag tag) {
        getChildAdapterTags().add(tag);
    }

    public void unregisterChildAdapterTag(@NonNull ComposedChildAdapterTag tag) {
        getChildAdapterTags().remove(tag);
    }

    public boolean hasChildAdapters() {
        return !getChildAdapterTags().isEmpty();
    }

    public void release() {
        getChildAdapterTags().clear();
    }
}
