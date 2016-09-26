package com.qingsongchou.library.widget.bezier;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingsongchou.library.widget.R;


/**
 * Created by smileCloud on 16/9/23.
 */

public class RightLayout implements RefreshHeader {

    private final Context context;
    private TextView textView;
    private PointerView mPointView;

    private int mHeight;

    public RightLayout(Context context,int height) {
        this.context = context;
        mHeight = height;
    }

    @NonNull
    @Override
    public View getView(ViewGroup container) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_load_more_layout, container, false);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) dp2px(92,view);
        params.height = (int) dp2px(mHeight,view);
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
    private float dp2px(int dpVal,View view) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, view.getResources().getDisplayMetrics());
    }
}
