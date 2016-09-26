package com.qingsongchou.library.widget.bezier;

import android.content.Context;
import android.support.annotation.NonNull;
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

    public RightLayout(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(ViewGroup container) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_load_more_layout, container, false);
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
}
