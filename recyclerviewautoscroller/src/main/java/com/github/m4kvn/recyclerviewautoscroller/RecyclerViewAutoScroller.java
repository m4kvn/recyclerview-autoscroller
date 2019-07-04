package com.github.m4kvn.recyclerviewautoscroller;

import android.os.Handler;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class RecyclerViewAutoScroller extends RecyclerView.OnScrollListener implements Runnable {
    private long delayMillis;
    private int initialPosition;
    private AtomicBoolean isInitialized;
    private AtomicInteger lastPosition;
    private AtomicMarkableReference<RecyclerView> running;
    private Handler handler;

    public RecyclerViewAutoScroller(
            long delayMillis,
            int initialPosition
    ) {
        this.delayMillis = delayMillis;
        this.initialPosition = initialPosition;
        this.isInitialized = new AtomicBoolean(false);
        this.lastPosition = new AtomicInteger(0);
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
        int suggestPos = getLastVisiblePosition(recycler) + 1;
        recycler.smoothScrollToPosition((itemCount > suggestPos) ? suggestPos : 0);
        recycler.setFocusable(false);
        handler.postDelayed(this, delayMillis);
    }

    @CallSuper
    public void start(@NonNull RecyclerView recycler) {
        if (running.getReference() == null) {
            if (isInitialized.get()) {
                recycler.scrollToPosition(lastPosition.get());
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
            lastPosition.set(getLastVisiblePosition(recycler));
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

    private int getLastVisiblePosition(@NonNull RecyclerView recycler) {
        RecyclerView.LayoutManager layoutManager = recycler.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        } else {
            return -1;
        }
    }
}
