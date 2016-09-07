package com.qingsongchou.library.widget.groupview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TriangleTextView
 * Created by smileCloud on 16/7/5.
 */
public class TriangleTextView extends TextView {
    public TriangleTextView(Context context) {
        super(context);
    }

    public TriangleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TriangleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#E9E9E9"));
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(getMeasuredWidth(), getMeasuredHeight());
        path.lineTo(getMeasuredWidth(), 0);
        path.close();
        canvas.drawPath(path, paint);
        canvas.rotate(37, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        super.onDraw(canvas);
    }
}
