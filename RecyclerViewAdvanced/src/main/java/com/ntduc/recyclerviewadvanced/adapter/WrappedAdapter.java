package com.ntduc.recyclerviewadvanced.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An interface provides better methods for wrapped adapters.
 */
public interface WrappedAdapter<VH extends RecyclerView.ViewHolder> {
    /**
     * onViewAttachedToWindow() with unwrapped item view type parameter.
     *
     * @param holder   Holder of the view being attached
     * @param viewType Unwrapped view type. Use this instead of #{{@link RecyclerView.ViewHolder#getItemViewType()}}.
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onViewAttachedToWindow(RecyclerView.ViewHolder)
     */
    void onViewAttachedToWindow(@NonNull VH holder, int viewType);

    /**
     * onViewDetachedFromWindow() with unwrapped item view type parameter.
     *
     * @param holder   Holder of the view being detached
     * @param viewType Unwrapped view type. Use this instead of #{{@link RecyclerView.ViewHolder#getItemViewType()}}.
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onViewDetachedFromWindow(RecyclerView.ViewHolder)
     */
    void onViewDetachedFromWindow(@NonNull VH holder, int viewType);

    /**
     * onViewRecycled() with unwrapped item view type parameter.
     *
     * @param holder   The ViewHolder for the view being recycled
     * @param viewType Unwrapped view type. Use this instead of #{{@link RecyclerView.ViewHolder#getItemViewType()}}.
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onViewRecycled(RecyclerView.ViewHolder)
     */
    void onViewRecycled(@NonNull VH holder, int viewType);


    /**
     * onFailedToRecycleView() with unwrapped item view type parameter.
     *
     * @param holder   The ViewHolder containing the View that could not be recycled due to its
     *                 transient state.
     * @param viewType Unwrapped view type. Use this instead of #{{@link RecyclerView.ViewHolder#getItemViewType()}}.
     * @return True if the View should be recycled, false otherwise. Note that if this method
     * returns <code>true</code>, RecyclerView <em>will ignore</em> the transient state of
     * the View and recycle it regardless. If this method returns <code>false</code>,
     * RecyclerView will check the View's transient state again before giving a final decision.
     * Default implementation returns false.
     * @see androidx.recyclerview.widget.RecyclerView.Adapter#onFailedToRecycleView(RecyclerView.ViewHolder)
     */
    boolean onFailedToRecycleView(@NonNull VH holder, int viewType);
}
