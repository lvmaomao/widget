package com.qingsongchou.library.widget.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.qingsongchou.library.widget.R;
import com.qingsongchou.library.widget.util.PixUtils;


/**
 * Created by duanjunxiao on 2017/1/17.
 * 宽度固定的(与屏幕有关系的)LinearLayout
 */

public class FixedWidthLinearLayout extends LinearLayout {

    private float widthWeight;
    private float remain;
    private Context mContext;

    public FixedWidthLinearLayout(Context context) {
        this(context, null);
    }

    public FixedWidthLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FixedWidthLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FixedWidthLinearLayout);
        widthWeight = a.getFloat(R.styleable.FixedWidthLinearLayout_fixed_width_layout_width_weight, 0);
        remain = a.getDimension(R.styleable.FixedWidthLinearLayout_fixed_width_layout_remain, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenWidthPixels = PixUtils.getScreenWidthPixels(mContext);
        int expectLeft = screenWidthPixels - (int) remain;
        int childWidthSize = (int) (expectLeft / widthWeight);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                childWidthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
