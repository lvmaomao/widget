package com.qingsongchou.library.widget.bezier;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingsongchou.library.widget.R;


/**
 * Created by smileCloud on 16/9/23.
 */

public class RightLayout implements RefreshHeader {

    private Context mContext;
    private TextView textView;
    private PointerView mPointView;

    private int mHeight;
    private int mMarginTop;
    private int mMarginBottom;

    public RightLayout(Context context) {
        this(context, 0);
    }

    public RightLayout(Context context, int height) {
        this(context, height, 0);
    }

    public RightLayout(Context context, int height, int marginTop) {
        this(context, height, marginTop, 0);
    }

    public RightLayout(Context context, int height, int marginTop, int marginBottom) {
        mContext = context;
        mHeight = height;
        mMarginTop = marginTop;
        mMarginBottom = marginBottom;
    }

    @NonNull
    @Override
    public View getView(ViewGroup container) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.widget_load_more_layout, container, false);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (mHeight != 0) {
            params.width = (int) dp2px(92, view);
            params.height = (int) dp2px(mHeight, view);
        }
        if (mMarginTop != 0) {
            params.topMargin = (int) dp2px(mMarginTop, view);
        }
        if (mMarginBottom != 0) {
            params.bottomMargin = (int) dp2px(mMarginBottom, view);
        }
        view.setLayoutParams(params);
        textView = (TextView) view.findViewById(R.id.text);
        textView.bringToFront();
        mPointView = (PointerView) view.findViewById(R.id.pointView);
        return view;
    }

    @Override
    public void onStart(int dragPosition, View refreshHead) {
        textView.setText("左拉查看");
    }

    @Override
    public void onDragging(float distance, float percent, View refreshHead) {
        mPointView.run(percent);
    }

    @Override
    public void onReadyToRelease(View refreshHead) {
        textView.setText("查看更多");
    }

    @Override
    public void onRelease(View refreshHead) {
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    private float dp2px(int dpVal, View view) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, view.getResources().getDisplayMetrics());
    }
}
