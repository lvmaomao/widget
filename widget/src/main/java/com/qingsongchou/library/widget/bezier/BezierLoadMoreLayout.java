package com.qingsongchou.library.widget.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * BezierLoadMoreLayout
 *
 * 使用方法
 * BezierLoadMoreLayout mLoadmore = 。。。;
 *
 * mLoadmore.setEnable(true);
 * mLoadmore.setRefreshMode(BezierLoadMoreLayout.MODE_UNDER_FOLLOW_DRAG);
 * mLoadmore.setRefreshHeader(new RightLayout(this), BezierLoadMoreLayout.END);
 * mLoadmore.setLoadMoreCallback(this);
 *
 *
 * implements LoadMoreCallBack
 * @Override
 * public void onRightRefreshing() {
 *  ...
 *  跳转
 *
 *  完成后 mLoadmore.onRefreshComplete();
 * }
 *
 * Created by smileCloud on 16/9/23.
 */

public class BezierLoadMoreLayout extends FrameLayout {

    private View mTarget;
    private float startX;
    private float refreshStartX;
    private int dragState = -1;
    public static final int START = 0;
    public static final int END = 1;
    private int width;
    private int height;
    private float leftHeadWidth;
    private float rightHeadWidth;

    private static final int REFRESH_STATE_IDLE = 0;
    private static final int REFRESH_STATE_START = 1;
    private static final int REFRESH_STATE_DRAGGING = 2;
    private static final int REFRESH_STATE_READY_TO_RELEASE = 3;
    private static final int REFRESH_STATE_REFRESHING = 4;

    private int refreshState = REFRESH_STATE_IDLE;
    private boolean enable = true;
    private Context context;
    private View leftHead;
    private View rightHead;
    private float mTargetTranslationX = 0;

    private RefreshHeader leftRefreshHeader;
    private RefreshHeader rightRefreshHeader;
    private LoadMoreCallBack mLoadMoreCallback;
    private int commonMarginPx;

    private int commonMargin = 16;

    public static final int MODE_UNDER_FOLLOW_DRAG = 0;
    public static final int MODE_UNDER = 1;
    public static final int MODE_ABOVE = 2;

    private int refreshMode = MODE_UNDER;

    public BezierLoadMoreLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BezierLoadMoreLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BezierLoadMoreLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        commonMarginPx = dp2px(context, commonMargin);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == leftHead) {
                leftHeadWidth = view.getMeasuredWidth() + commonMarginPx;
            } else if (view == rightHead) {
                rightHeadWidth = view.getMeasuredWidth() + commonMarginPx;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        if (refreshState == REFRESH_STATE_IDLE && refreshMode != MODE_UNDER) {
            if (leftHead != null) {
                leftHead.setTranslationX(-leftHeadWidth);
            }
            if (rightHead != null) {
                rightHead.setTranslationX(rightHeadWidth);
            }
        }

        super.onLayout(changed, l, t, r, b);
    }

    private void ensureTarget() {
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(leftHead) && !child.equals(rightHead)) {
                    mTarget = child;
                    break;
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (refreshState == REFRESH_STATE_DRAGGING){
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mTarget == null || !isEnable()) {
            return super.onInterceptTouchEvent(ev);
        }
        final int action = MotionEventCompat.getActionMasked(ev);

        if (mTarget != null && (dragState == START || dragState == END)) {
            return true;
        }

        if (refreshState != REFRESH_STATE_DRAGGING){
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                if (!canChildScrollRight() && startX != 0 && startX - ev.getX() > 0
                        && refreshState != REFRESH_STATE_REFRESHING && rightRefreshHeader != null) {
                    //end drag
                    dragState = END;
                    refreshState = REFRESH_STATE_START;
                    rightRefreshHeader.onStart(END, rightHead);
                    return true;
                }
                if (!canChildScrollLeft() && startX != 0 && startX - ev.getX() < 0
                        && refreshState != REFRESH_STATE_REFRESHING && leftRefreshHeader != null) {
                    //start drag
                    dragState = START;
                    refreshState = REFRESH_STATE_START;
                    leftRefreshHeader.onStart(START, leftHead);
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startX = 0;
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mTarget == null || !isEnable()) return super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                final float targetTranslationX = mTargetTranslationX;

                if (dragState == START) {
                    if (targetTranslationX < (leftHeadWidth - commonMarginPx)) {
                        smoothRelease();
                    } else {
                        smoothLocateToRefresh(true);
                    }
                } else if (dragState == END) {
                    if ((-targetTranslationX) < (rightHeadWidth - commonMarginPx)) {
                        smoothRelease();
                    } else {
                        smoothLocateToRefresh(true);
                    }
                }
                return false;
        }

        final float targetTranslationX = mTargetTranslationX;

        if (dragState == START) {

            if (refreshStartX == 0) {
                refreshStartX = event.getX();
                return super.onTouchEvent(event);
            } else {
                float dX = event.getX() - refreshStartX;
                refreshStartX = event.getX();
                if (targetTranslationX < 0) {
                    setTargetTranslationX(0);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    dragState = -1;
                    refreshStartX = 0;
                    return false;
                }

                float dampingDX = dX * (1 - Math.abs((targetTranslationX / leftHeadWidth)));  //let drag action has resistance

                if (targetTranslationX + dampingDX < 0) {
                    setTargetTranslationX(0);
                } else if (targetTranslationX + dampingDX > leftHeadWidth){
                    setTargetTranslationX(leftHeadWidth);
                } else {
                    setTargetTranslationX(targetTranslationX + dampingDX);
//                    Log.v("AAA", mTargetTranslationX + "");
                    if (leftRefreshHeader != null) {
                        if (refreshMode == MODE_UNDER_FOLLOW_DRAG || refreshMode == MODE_ABOVE) {
                            leftHead.setTranslationX(targetTranslationX + dampingDX - (leftHeadWidth - commonMarginPx));
                        }
                        float percent = Math.abs(mTargetTranslationX / (leftHeadWidth - commonMarginPx));
                        if (percent > 0.1f) {
                            refreshState = REFRESH_STATE_DRAGGING;
                            leftRefreshHeader.onDragging(mTargetTranslationX, percent, leftHead);
                        }
                        if (mTargetTranslationX > (leftHeadWidth - commonMarginPx) && refreshState != REFRESH_STATE_READY_TO_RELEASE) {
                            refreshState = REFRESH_STATE_READY_TO_RELEASE;
                            leftRefreshHeader.onReadyToRelease(leftHead);
                        }
                    }
                }
                return true;
            }
        } else if (dragState == END) {
            if (refreshStartX == 0) {
                refreshStartX = event.getX();
                return super.onTouchEvent(event);
            } else {
                float dX = event.getX() - refreshStartX;
                refreshStartX = event.getX();
                if (targetTranslationX > 0) {
                    setTargetTranslationX(0);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    dragState = -1;
                    refreshStartX = 0;
                    return false;
                }

                float dampingDX = dX * (1 - Math.abs((targetTranslationX / rightHeadWidth)));  //let drag action has resistance

                if (targetTranslationX + dampingDX > 0) {
                    setTargetTranslationX(0);
                } else if (targetTranslationX + dampingDX < -rightHeadWidth){
                    setTargetTranslationX(-rightHeadWidth);
                } else {
                    setTargetTranslationX(targetTranslationX + dampingDX);

//                    Log.v("AAA", mTargetTranslationX + "");
                    if (rightRefreshHeader != null) {
                        if (refreshMode == MODE_UNDER_FOLLOW_DRAG || refreshMode == MODE_ABOVE) {
                            rightHead.setTranslationX(targetTranslationX + dampingDX + (rightHeadWidth - commonMarginPx));
                        }
                        float percent = Math.abs(mTargetTranslationX / (rightHeadWidth - commonMarginPx));
                        if (percent > 0.1f) {
                            refreshState = REFRESH_STATE_DRAGGING;
                            rightRefreshHeader.onDragging(-mTargetTranslationX, percent, rightHead);
                        }
                        if ((-mTargetTranslationX) > (rightHeadWidth - commonMarginPx) && refreshState != REFRESH_STATE_READY_TO_RELEASE) {
                            refreshState = REFRESH_STATE_READY_TO_RELEASE;
                            rightRefreshHeader.onReadyToRelease(rightHead);
                        }
                    }
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void setTargetTranslationX(float translation) {
        mTargetTranslationX = translation;
        if (refreshMode != MODE_ABOVE) {
            mTarget.setTranslationX(mTargetTranslationX);
        }
    }

    private void smoothRelease(){
        refreshState = REFRESH_STATE_START;
        dragState = -1;
        refreshStartX = 0;

        if (refreshMode == MODE_ABOVE) {
            setTargetTranslationX(0);
        } else {
            mTarget.animate().translationX(0).setDuration(200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            setTargetTranslationX(mTarget.getTranslationX());
                        }
                    })
                    .start();
        }
        if (leftRefreshHeader != null) {
            leftRefreshHeader.onStart(START, leftHead);
            if (refreshMode == MODE_UNDER_FOLLOW_DRAG || refreshMode == MODE_ABOVE) leftHead.animate().translationX(-leftHeadWidth).setDuration(200).start();
        }
        if (rightRefreshHeader != null) {
            rightRefreshHeader.onStart(START, rightHead);
            if (refreshMode == MODE_UNDER_FOLLOW_DRAG || refreshMode == MODE_ABOVE) rightHead.animate().translationX(rightHeadWidth).setDuration(200).start();
        }
    }

    private void smoothLocateToRefresh(boolean manualOrAuto) {
        refreshState = REFRESH_STATE_REFRESHING;
        dragState = -1;
        refreshStartX = 0;
        float translationX = mTargetTranslationX;

        if (leftRefreshHeader != null && translationX > 0) {
            leftRefreshHeader.onRelease(leftHead);
            if (refreshMode == MODE_UNDER_FOLLOW_DRAG || refreshMode == MODE_ABOVE) leftHead.animate().translationX(0).setDuration(150).start();
//            if (mLoadMoreCallback != null) mLoadMoreCallback.onLeftRefreshing();
        }
        if (rightRefreshHeader != null && translationX < 0) {
            rightRefreshHeader.onRelease(rightHead);
            if (refreshMode == MODE_UNDER_FOLLOW_DRAG || refreshMode == MODE_ABOVE) rightHead.animate().translationX(0).setDuration(150).start();
            if (mLoadMoreCallback != null) {
                mLoadMoreCallback.onRightRefreshing();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefreshComplete();
                    }
                }, 300);
            }
        }

        if (refreshMode == MODE_ABOVE) {
            setTargetTranslationX(0);
        } else {
            float dX = (translationX > 0 ? leftHeadWidth - commonMarginPx : -(rightHeadWidth - commonMarginPx)) - translationX;
            mTarget.animate().translationXBy(dX).setDuration(manualOrAuto? 150: 500)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            setTargetTranslationX(mTarget.getTranslationX());
                        }
                    })
                    .start();
        }

    }

    @MainThread
    private void setRefreshView(final View view, final int startOrEnd) {

        view.measure(width, height);
        if (startOrEnd == START) {
            ((LayoutParams) view.getLayoutParams()).gravity = Gravity.START;
        } else if (startOrEnd == END) {
            ((LayoutParams) view.getLayoutParams()).gravity = Gravity.END;
        }

        if (view.getParent() == null) {
            if (refreshMode == MODE_ABOVE) {
                addView(view);
            } else {
                addView(view, 0);
            }
        }
    }

    public boolean canChildScrollLeft() {
        return ViewCompat.canScrollHorizontally(mTarget, -1);
    }

    public boolean canChildScrollRight() {
        return ViewCompat.canScrollHorizontally(mTarget, 1);
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setRefreshMode(int refreshMode) {
        this.refreshMode = refreshMode;
    }

    public void onRefreshComplete(){
        refreshState = REFRESH_STATE_IDLE;
        smoothRelease();
        if (leftRefreshHeader != null) leftRefreshHeader.onStart(START, leftHead); //reset view style
        if (rightRefreshHeader != null) rightRefreshHeader.onStart(END, rightHead); //reset view style
    }

    public void setLeftHeadView(View view) {
        leftHead = view;
        setRefreshView(leftHead, START);
    }

    public void setRightHeadView(View view) {
        rightHead = view;
        setRefreshView(rightHead, END);
    }

    public void setRefreshHeader(RefreshHeader header, int startOrEnd) {
        if (startOrEnd == START) {
            leftRefreshHeader = header;
            setLeftHeadView(leftRefreshHeader.getView(this));
        } else if (startOrEnd == END) {
            rightRefreshHeader = header;
            setRightHeadView(rightRefreshHeader.getView(this));
        }
    }

    public void setLoadMoreCallback(LoadMoreCallBack callback) {
        mLoadMoreCallback = callback;
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }
}
