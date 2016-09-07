package com.qingsongchou.library.widget.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by wsl on 15-8-11.
 */
public class BadgeIndicatorView extends IndicatorView {

    private float mBadgeRadius;
    private int mBadgeTopMargin;
    private String mBadgeText;
    private BadgeDrawable mBadgeDrawable;

    public BadgeIndicatorView(Context context) {
        this(context, null);
    }

    public BadgeIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initParams();
    }

    private void initParams() {
        mBadgeRadius = dp2px(8f);
        int badgeTextSize = (int) sp2px(11f);
        mBadgeDrawable = BadgeDrawable.builder(null, mBadgeRadius)
                .textSize(badgeTextSize).build();
        mBadgeTopMargin = (int) dp2px(1f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBadgeView(canvas);
    }

    private void drawBadgeView(Canvas canvas) {
        int radius = (int) mBadgeRadius;
        int left = getDrawableRect().right - radius;
        int top = mBadgeTopMargin;
        int right = getDrawableRect().right + radius;
        int bottom = radius * 2 + top;

        int textPadding = getBadgeTextPadding();
        mBadgeDrawable.setText(mBadgeText);
        mBadgeDrawable.setBounds(left - textPadding, top, right + textPadding, bottom);
        mBadgeDrawable.draw(canvas);
    }

    private int getBadgeTextPadding() {
        if (TextUtils.isEmpty(mBadgeText)) {
            return 0;
        } else {
            int size = mBadgeText.length();
            if (size == 1) {
                return 0;
            } else if (size > 1) {
                return 5;
            } else {
                return 0;
            }
        }
    }

    public void setBadgeCount(int count) {
        if (count < 99) {
            mBadgeText = count <= 0 ? null : Integer.toString(count);
        } else {
            mBadgeText = "···";
            mBadgeDrawable = BadgeDrawable.builder(null, mBadgeRadius)
                    .textSize((int) sp2px(17f)).build();
        }
        invalidate();
    }

    private static final String SUPER_STATUS = "badge_super_status";
    private static final String STATUS_BADGE_TEXT = "status_badge_text";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUPER_STATUS, super.onSaveInstanceState());
        bundle.putString(STATUS_BADGE_TEXT, mBadgeText);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mBadgeText = bundle.getString(STATUS_BADGE_TEXT);
            super.onRestoreInstanceState(bundle.getParcelable(SUPER_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
