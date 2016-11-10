package com.elysium.nodlebar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwipeableRecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

    private int mSlop;
    private int mMinFlingVelocity;
    private int mMaxFlingVelocity;
    private long mAnimationTime;

    private RecyclerView mRecyclerView;
    private SwipeListener mSwipeListener;
    private int mViewWidth = 1;

    private List<PendingDismissData> mPendingDismisses = new ArrayList<>();
    private int mDismissAnimationRefCount = 0;
    private float mAlpha;
    private float mDownX;
    private float mDownY;
    private boolean mSwiping;
    private int mSwipingSlop;
    private VelocityTracker mVelocityTracker;
    private int mDownPosition;
    private int mAnimatingPosition = ListView.INVALID_POSITION;
    private View mDownView;
    private boolean mPaused;
    private float mFinalDelta;

    private boolean mSwipingLeft;
    private boolean mSwipingRight;

    public SwipeableRecyclerViewTouchListener(RecyclerView recyclerView, SwipeListener listener) {
        ViewConfiguration vc = ViewConfiguration.get(recyclerView.getContext());
        mSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity() * 16;
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        mAnimationTime = recyclerView.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        mRecyclerView = recyclerView;
        mSwipeListener = listener;

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                setEnabled(newState != RecyclerView.SCROLL_STATE_DRAGGING);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    public void setEnabled(boolean enabled) {
        mPaused = !enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
        return handleTouchEvent(motionEvent);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // TODO -- nothing?
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
        handleTouchEvent(motionEvent);
    }

    private boolean handleTouchEvent(MotionEvent motionEvent) {
        if (mViewWidth < 2) {
            mViewWidth = mRecyclerView.getWidth();
        }

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                if (mPaused) {
                    break;
                }

                Rect rect = new Rect();
                int childCount = mRecyclerView.getChildCount();
                int[] listViewCoords = new int[2];
                mRecyclerView.getLocationOnScreen(listViewCoords);
                int x = (int) motionEvent.getRawX() - listViewCoords[0];
                int y = (int) motionEvent.getRawY() - listViewCoords[1];
                View child;
                for (int i = 0; i < childCount; i++) {
                    child = mRecyclerView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x, y)) {
                        mDownView = child;
                        break;
                    }
                }

                if (mDownView != null && mAnimatingPosition != mRecyclerView.getChildLayoutPosition(mDownView)) {

                    mAlpha = ViewCompat.getAlpha(mDownView);
                    mDownX = motionEvent.getRawX();
                    mDownY = motionEvent.getRawY();
                    mDownPosition = mRecyclerView.getChildLayoutPosition(mDownView);
                    mSwipingLeft = mSwipeListener.canSwipeLeft(mDownPosition);
                    mSwipingRight = mSwipeListener.canSwipeRight(mDownPosition);
                    if (mSwipingLeft||mSwipingRight) {

                        mVelocityTracker = VelocityTracker.obtain();
                        mVelocityTracker.addMovement(motionEvent);

                    } else {
                        mDownView = null;
                    }
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL: {

                if (mVelocityTracker == null) {
                    break;
                }

                if (mDownView != null && mSwiping) {

                    ViewCompat.animate(mDownView)
                            .translationX(0)
                            .alpha(mAlpha)
                            .setDuration(mAnimationTime)
                            .setListener(null);
                }

                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mDownX = 0;
                mDownY = 0;
                mDownView = null;
                mDownPosition = ListView.INVALID_POSITION;
                mSwiping = false;
                break;
            }

            case MotionEvent.ACTION_UP: {

                if (mVelocityTracker == null) {
                    break;
                }

                mFinalDelta = motionEvent.getRawX() - mDownX;
                mVelocityTracker.addMovement(motionEvent);
                mVelocityTracker.computeCurrentVelocity(1000);
                float velocityX = mVelocityTracker.getXVelocity();
                float absVelocityX = Math.abs(velocityX);
                float absVelocityY = Math.abs(mVelocityTracker.getYVelocity());
                boolean dismiss = false;
                boolean dismissRight = false;

                if (Math.abs(mFinalDelta) > mViewWidth / 2 && mSwiping) {

                    dismiss = true;
                    dismissRight = mFinalDelta > 0;

                } else if (mMinFlingVelocity <= absVelocityX && absVelocityX <= mMaxFlingVelocity
                        && absVelocityY < absVelocityX && mSwiping) {

                    dismiss = (velocityX < 0) == (mFinalDelta < 0);
                    dismissRight = mVelocityTracker.getXVelocity() > 0;
                }

                if (dismiss && mDownPosition != mAnimatingPosition && mDownPosition != ListView.INVALID_POSITION) {

                    final View downView = mDownView;
                    final int downPosition = mDownPosition;
                    ++mDismissAnimationRefCount;
                    mAnimatingPosition = mDownPosition;
                    ViewCompat.animate(mDownView)
                            .translationX(dismissRight ? mViewWidth : -mViewWidth)
                            .alpha(0)
                            .setDuration(mAnimationTime)
                            .setListener(new ViewPropertyAnimatorListener() {

                                @Override
                                public void onAnimationStart(View view) {
                                }

                                @Override
                                public void onAnimationEnd(View view) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        performDismiss(downView, downPosition);
                                    }
                                }

                                @Override
                                public void onAnimationCancel(View view) {

                                }
                            });
                } else {

                    ViewCompat.animate(mDownView)
                            .translationX(0)
                            .alpha(mAlpha)
                            .setDuration(mAnimationTime)
                            .setListener(null);
                }

                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mDownX = 0;
                mDownY = 0;
                mDownView = null;
                mDownPosition = ListView.INVALID_POSITION;
                mSwiping = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mVelocityTracker == null || mPaused) {
                    break;
                }

                mVelocityTracker.addMovement(motionEvent);
                float deltaX = motionEvent.getRawX() - mDownX;
                float deltaY = motionEvent.getRawY() - mDownY;
                if (!mSwiping && Math.abs(deltaX) > mSlop && Math.abs(deltaY) < Math.abs(deltaX) / 2) {
                    mSwiping = true;
                    mSwipingSlop = (deltaX > 0 ? mSlop : -mSlop);
                }

                if(deltaX < 0 && !mSwipingLeft)
                    mSwiping = false;
                if(deltaX > 0 && !mSwipingRight)
                    mSwiping = false;

                if (mSwiping) {

                    ViewCompat.setTranslationX(mDownView, deltaX - mSwipingSlop);
                    ViewCompat.setAlpha(mDownView, Math.max(0f, Math.min(mAlpha,
                            mAlpha * (1f - Math.abs(deltaX) / mViewWidth))));
                    return true;
                }

                break;
            }
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void performDismiss(final View dismissView, final int dismissPosition) {

        final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
        final int originalLayoutParamsHeight = lp.height;
        final int originalHeight = dismissView.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                --mDismissAnimationRefCount;
                if (mDismissAnimationRefCount == 0) {

                    Collections.sort(mPendingDismisses);

                    int[] dismissPositions = new int[mPendingDismisses.size()];
                    for (int i = mPendingDismisses.size() - 1; i >= 0; i--) {
                        dismissPositions[i] = mPendingDismisses.get(i).position;
                    }

                    if (mFinalDelta < 0) {
                        mSwipeListener.onDismissedBySwipeLeft(mRecyclerView, dismissPositions);
                    } else {
                        mSwipeListener.onDismissedBySwipeRight(mRecyclerView, dismissPositions);
                    }

                    mDownPosition = ListView.INVALID_POSITION;

                    ViewGroup.LayoutParams lp;
                    for (PendingDismissData pendingDismiss : mPendingDismisses) {

                        pendingDismiss.view.setAlpha(mAlpha);
                        pendingDismiss.view.setTranslationX(0);

                        lp = pendingDismiss.view.getLayoutParams();
                        lp.height = originalLayoutParamsHeight;

                        pendingDismiss.view.setLayoutParams(lp);
                    }

                    long time = SystemClock.uptimeMillis();
                    MotionEvent cancelEvent = MotionEvent.obtain(time, time,
                            MotionEvent.ACTION_CANCEL, 0, 0, 0);
                    mRecyclerView.dispatchTouchEvent(cancelEvent);

                    mPendingDismisses.clear();
                    mAnimatingPosition = ListView.INVALID_POSITION;
                }
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                dismissView.setLayoutParams(lp);
            }
        });

        mPendingDismisses.add(new PendingDismissData(dismissPosition, dismissView));
        animator.start();
    }

    public interface SwipeListener {

        boolean canSwipeLeft(int position);

        boolean canSwipeRight(int position);

        void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions);

        void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions);
    }

    class PendingDismissData implements Comparable<PendingDismissData> {
        public int position;
        public View view;

        public PendingDismissData(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public int compareTo(@NonNull PendingDismissData other) {

            return other.position - position;
        }
    }
}