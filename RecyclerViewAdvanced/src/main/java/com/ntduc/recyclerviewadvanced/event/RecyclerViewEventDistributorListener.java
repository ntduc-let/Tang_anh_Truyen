package com.ntduc.recyclerviewadvanced.event;

import androidx.annotation.NonNull;

public interface RecyclerViewEventDistributorListener {
    void onAddedToEventDistributor(@NonNull BaseRecyclerViewEventDistributor distributor);

    void onRemovedFromEventDistributor(@NonNull BaseRecyclerViewEventDistributor distributor);
}
