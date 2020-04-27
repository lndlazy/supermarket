//package com.supermarket.haidilao.custom;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.jessewu.library.SuperAdapter;
//
//public class LoadMoreRecyclerView extends RecyclerView {
//
//    private final int STATE_NONE = 0x00;
//    private final int STATE_LOADING = 0x01;
//    private final int STATE_FAILURE = 0x02;
//    private final int STATE_COMPLETE = 0x03;
//
//    // 滑到底部里最后一个个数的阀值
//    private static final int VISIBLE_THRESHOLD = 1;
//
//    private int state;
//    private boolean loadMoreEnabled = true;
//    private OnLoadMoreListener listener;
//    private SuperAdapter superAdapter;
//    private LoadMoreView loadMoreView;
//    private LinearLayoutManager layoutManager;
//
//    public LoadMoreRecyclerView(Context context) {
//        this(context, null);
//    }
//
//    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    public void initLoadMore(@NonNull OnLoadMoreListener listener) {
//        this.listener = listener;
//
//        if (getAdapter() instanceof SuperAdapter) {
//            superAdapter = (SuperAdapter) getAdapter();
//        }
//
//        loadMoreView = new LoadMoreView(getContext());
//        layoutManager = (LinearLayoutManager) getLayoutManager();
//
//        addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                if (canLoadMore() && dy >= 0) {
//                    int totalItemCount = layoutManager.getItemCount();
//                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//                    if (lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
//                        onLoadMore(recyclerView);
//                    }
//                }
//            }
//        });
//    }
//
//    public void setLoadMoreEnabled(boolean enabled) {
//        this.loadMoreEnabled = enabled;
//    }
//
//    private boolean canLoadMore() {
//        return state != STATE_LOADING && loadMoreEnabled;
//    }
//
//    private void onLoadMore(RecyclerView recyclerView) {
//        state = STATE_LOADING;
//
//        if (loadMoreView != null) {
//            loadMoreView.loading();
//        }
//
//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                if (state == STATE_LOADING) {
//                    if (superAdapter != null && loadMoreView != null) {
//                        superAdapter.addFooterView(loadMoreView);
//                    }
//                }
//            }
//        });
//
//        if (listener != null) {
//            listener.onLoadMore();
//        }
//    }
//
//    public void loadMoreFailed() {
//        state = STATE_FAILURE;
//
//        if (loadMoreView != null) {
//            loadMoreView.failure();
//        }
//
//        if (superAdapter != null) {
//            superAdapter.removeFooterView();
//        }
//    }
//
//    public void loadMoreComplete() {
//        state = STATE_COMPLETE;
//
//        if (loadMoreView != null) {
//            loadMoreView.complete();
//        }
//
//        if (superAdapter != null) {
//            superAdapter.removeFooterView();
//        }
//    }
//
//}
