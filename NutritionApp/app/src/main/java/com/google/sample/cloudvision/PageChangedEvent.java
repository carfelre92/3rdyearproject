package com.google.sample.cloudvision;

/**
 * Created by SuKim on 15/08/2017.
 */

public class PageChangedEvent {
    private boolean mHasVerticalNeighbors = true;

    public PageChangedEvent(boolean hasVerticalNeighbors) {
        mHasVerticalNeighbors = hasVerticalNeighbors;
    }

    public boolean hasVerticalNeighbors() {

        return mHasVerticalNeighbors;
    }


}
