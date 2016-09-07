package com.qingsongchou.library.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wsl on 15-9-2.
 */
public class TimeLineView extends View {

    private static final int COLOR_DEFAULT = 0xfff0f0f0;
    private static final int COLOR_GREEN = 0xff19b458;

    private Paint borderPaint;
    private RectF borderRect;
    private float borderWidth;

    private Paint innerRimPaint;
    private RectF innerRimRect;
    private float innerRimWidth;

    private Paint innerPaint;
    private RectF innerRect;
    private float innerRadius;

    private RectF topRimRect;
    private RectF bottomRimRect;
    private Paint outRimPaint;
    private float outRimWidth;

    private boolean done;
    private boolean last;

    private Paint testPaint;
    private Paint testPaint2;

    public TimeLineView(Context context) {
        this(context, null);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        innerRimWidth = resources.getDimension(R.dimen.timeline_inner_rim_width);
        innerRadius = resources.getDimension(R.dimen.timeline_inner_radius);
        outRimWidth = resources.getDimension(R.dimen.timeline_out_rim_width);
        borderWidth = resources.getDimension(R.dimen.timeline_border_width);

        innerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerPaint.setColor(COLOR_GREEN);
        innerPaint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(COLOR_DEFAULT);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);

        innerRimPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerRimPaint.setColor(Color.WHITE);
        innerRimPaint.setStyle(Paint.Style.STROKE);
        innerRimPaint.setStrokeWidth(borderWidth);

        outRimPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outRimPaint.setColor(COLOR_DEFAULT);
        outRimPaint.setStyle(Paint.Style.FILL);

        testPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        testPaint.setColor(Color.RED);
        testPaint.setStyle(Paint.Style.FILL);

        testPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        testPaint2.setColor(Color.BLUE);
        testPaint2.setStyle(Paint.Style.FILL);

        borderRect = new RectF();
        innerRimRect = new RectF();
        innerRect = new RectF();

        topRimRect = new RectF();
        bottomRimRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    private void setup() {
        int width = getWidth();
        int height = getHeight();

        borderRect.set(width / 2 - innerRadius - innerRimWidth,
                height / 2 - innerRadius - innerRimWidth,
                width / 2 + innerRadius + innerRimWidth,
                height / 2 + innerRadius + innerRimWidth);

        innerRimRect.set(borderRect.left + innerRimWidth, borderRect.top + innerRimWidth,
                borderRect.right - innerRimWidth, borderRect.bottom - innerRimWidth);

        innerRect.set(innerRimRect);
        innerRadius = Math.min(innerRect.width() / 2, innerRect.height() / 2);

        topRimRect.set((width - outRimWidth) / 2, 0, (width + outRimWidth) / 2,
                (height - borderRect.height()) / 2);
        bottomRimRect.set((width - outRimWidth) / 2,
                (height + borderRect.height()) / 2,
                (width + outRimWidth) / 2, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw border
        canvas.drawArc(borderRect, 0, 360, false, borderPaint);
        //draw rim
        canvas.drawArc(innerRimRect, 0, 360, false, innerRimPaint);

        //draw inner
        canvas.save();
        innerPaint.setColor(isDone() ? COLOR_GREEN : COLOR_DEFAULT);
        canvas.translate(innerRect.left, innerRect.top);
        canvas.drawCircle(innerRect.width() / 2, innerRect.height() / 2, innerRadius, innerPaint);
        canvas.restore();

        //draw top and bottom
        outRimPaint.setAlpha(255);
        canvas.drawRect(topRimRect, outRimPaint);
        outRimPaint.setAlpha(isLast() ? 0 : 255);
        canvas.drawRect(bottomRimRect, outRimPaint);
    }

    public void setDone(boolean done) {
        this.done = done;
        invalidate();
    }

    public boolean isDone() {
        return done;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
        invalidate();
    }
}