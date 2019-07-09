package com.m4kvn.recyclerviewautoscroller;

import android.os.Handler;
import android.os.Parcelable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAutoScroller extends RecyclerView.OnScrollListener implements Runnable {
    private long delayMillis;
    private int initialPosition;
    private AtomicBoolean isInitialized;
    private AtomicReference<Parcelable> lastState;
    private AtomicMarkableReference<RecyclerView> running;
    private Handler handler;

    public RecyclerViewAutoScroller(
            long delayMillis,
            int initialPosition
    ) {
        this.delayMillis = delayMillis;
        this.initialPosition = initialPosition;
        this.isInitialized = new AtomicBoolean(false);
        this.lastState = new AtomicReference<>(null);
        this.running = new AtomicMarkableReference<>(null, false);
        this.handler = new Handler();
    }

    @Override
    public void run() {
        RecyclerView recycler = running.getReference();
        if (recycler == null) return;
        RecyclerView.Adapter adapter = recycler.getAdapter();
        if (adapter == null) return;
        int itemCount = adapter.getItemCount();
        int suggestPos = getSuggestedNextPosition(recycler);
        recycler.smoothScrollToPosition((itemCount > suggestPos) ? suggestPos : 0);
        recycler.setFocusable(false);
        handler.postDelayed(this, delayMillis);
    }

    @CallSuper
    public void start(@NonNull RecyclerView recycler) {
        if (running.getReference() == null) {
            if (isInitialized.get()) {
                restoreState(recycler);
            } else {
                recycler.scrollToPosition(initialPosition);
                isInitialized.set(true);
            }
            recycler.setFocusable(false);
            recycler.addOnScrollListener(this);
            running.set(recycler, false);
            resume();
        }
    }

    private void resume() {
        if (!running.isMarked()) {
            running.set(running.getReference(), true);
            handler.postDelayed(this, delayMillis);
        }
    }

    private void pause() {
        if (running.isMarked()) {
            running.set(running.getReference(), false);
            handler.removeCallbacksAndMessages(null);
        }
    }

    @CallSuper
    public void stop() {
        RecyclerView recycler = running.getReference();
        if (recycler != null) {
            pause();
            saveState(recycler);
            running.set(null, false);
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (recyclerView != running.getReference()) return;
        switch (newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING:
                pause();
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                resume();
                break;
        }
    }

    private int getSuggestedNextPosition(@NonNull RecyclerView recycler) {
        RecyclerView.LayoutManager layoutManager = recycler.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager llm = ((LinearLayoutManager) layoutManager);
            int completeVisiblePos = llm.findLastCompletelyVisibleItemPosition();
            return (completeVisiblePos >= 0) ? completeVisiblePos + 1 : llm.findLastVisibleItemPosition();
        } else {
            return -1;
        }
    }

    private void saveState(@NonNull RecyclerView recycler) {
        RecyclerView.LayoutManager layoutManager = recycler.getLayoutManager();
        if (layoutManager != null) {
            lastState.set(layoutManager.onSaveInstanceState());
        }
    }

    private void restoreState(@NonNull RecyclerView recycler) {
        RecyclerView.LayoutManager layoutManager = recycler.getLayoutManager();
        Parcelable state = lastState.get();
        if (layoutManager != null && state != null) {
            layoutManager.onRestoreInstanceState(state);
        }
    }
}