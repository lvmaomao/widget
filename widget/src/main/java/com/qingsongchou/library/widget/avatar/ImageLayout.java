package com.qingsongchou.library.widget.avatar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingsongchou.library.widget.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsl on 16-1-28.
 */
public class ImageLayout extends ViewGroup {

    private List<String> urls;
    private int unitDimen;
    private int coverDimen;
    private int size;

    public ImageLayout(Context context) {
        this(context, null);
    }

    public ImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        urls = new ArrayList<>();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarLayout);
        coverDimen = a.getDimensionPixelSize(R.styleable.AvatarLayout_cover_dimen, (int) dp2px(4));
        unitDimen = a.getDimensionPixelSize(R.styleable.AvatarLayout_unit_dimen, (int) dp2px(24));
        size = a.getDimensionPixelSize(R.styleable.AvatarLayout_unit_dimen, 4);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        int count = getChildCount();
        if (count > size) {
            count = size;
        }
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams layoutParams = child.getLayoutParams();
            width = width + layoutParams.width;
            height = layoutParams.height;
        }
        if (width > 0) {
            width = width - (count - 1) * coverDimen;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int left;
        int right;
        int bottom = getMeasuredHeight();
        int top = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            left = width * i;
            if (i != 0) {
                left = left - coverDimen * i;
            }
            right = left + width;
            child.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        load();
    }

    private void load() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {

        }
    }

    public void update(List<String> urls) {
        if (urls == null) {
            return;
        }
        this.urls.clear();
        removeAllViews();
        LayoutParams childLayoutParams = new LayoutParams(unitDimen, unitDimen);
        for (String url : urls) {
            this.urls.add(url);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.mipmap.ic_avatar_default);
            Picasso.with(getContext())
                    .load(url)
                    .error(R.mipmap.ic_avatar_default)
                    .placeholder(R.mipmap.ic_avatar_default)
                    .into(imageView);
            addView(imageView, childLayoutParams);
        }
        invalidate();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected float dp2px(float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}