package com.ntduc.recyclerviewadvanced.event;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;

public class RecyclerViewRecyclerEventDistributor extends BaseRecyclerViewEventDistributor<RecyclerView.RecyclerListener> {

    private InternalRecyclerListener mInternalRecyclerListener;

    public RecyclerViewRecyclerEventDistributor() {
        super();

        mInternalRecyclerListener = new InternalRecyclerListener(this);
    }

    @Override
    protected void onRecyclerViewAttached(@NonNull RecyclerView rv) {
        super.onRecyclerViewAttached(rv);

        rv.setRecyclerListener(mInternalRecyclerListener);
    }

    @Override
    protected void onRelease() {
        super.onRelease();

        if (mInternalRecyclerListener != null) {
            mInternalRecyclerListener.release();
            mInternalRecyclerListener = null;
        }
    }

    /*package*/ void handleOnViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (mListeners == null) {
            return;
        }

        for (RecyclerView.RecyclerListener listener : mListeners) {
            listener.onViewRecycled(holder);
        }
    }

    private static class InternalRecyclerListener implements RecyclerView.RecyclerListener {
        private final WeakReference<RecyclerViewRecyclerEventDistributor> mRefDistributor;

        public InternalRecyclerListener(@NonNull RecyclerViewRecyclerEventDistributor distributor) {
            super();
            mRefDistributor = new WeakReference<>(distributor);
        }

        @Override
        public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
            final RecyclerViewRecyclerEventDistributor distributor = mRefDistributor.get();

            if (distributor != null) {
                distributor.handleOnViewRecycled(holder);
            }
        }

        public void release() {
            mRefDistributor.clear();
        }
    }
}
