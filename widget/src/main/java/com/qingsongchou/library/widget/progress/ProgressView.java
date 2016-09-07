package com.qingsongchou.library.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.qingsongchou.library.widget.R;
import com.qingsongchou.library.widget.utils.DensityUtils;

/**
 * Created by yangyu on 16/8/12.
 */
public class ProgressView extends View {

    /**
     * 画笔
     */
    private Paint backPaint;
    private Paint forePaint;
    /**
     * 矩形
     */
    private RectF rectF;
    /**
     * params
     */
    private int circleWidth;
    private int radius = 0;
    private int viewWidth;
    private int viewHeight;

    private int backColor;
    private int foreColor;

    /**
     * 变化的角度
     */
    private float angle;


    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(attrs); //获取自定义的属性
        init();
    }

    private void obtainStyledAttrs(AttributeSet attrs) {
        TypedArray array = null;
        try {
            array = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
            circleWidth = (int) array.getDimension(R.styleable.ProgressView_progress_cricle_width, DensityUtils.dp2px(4));
            radius = (int) array.getDimension(R.styleable.ProgressView_progress_radius, 0);
            backColor = array.getColor(R.styleable.ProgressView_progress_back_color, Color.WHITE);
            foreColor = array.getColor(R.styleable.ProgressView_progress_fore_color, Color.GREEN);
        } catch (Exception e) {

        } finally {
            if (array != null) {
                array.recycle();
            }
        }

    }


    /**
     * 初始化
     */
    private void init() {

        backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setColor(backColor);
        backPaint.setStyle(Paint.Style.STROKE);
        backPaint.setStrokeWidth(circleWidth);

        forePaint = new Paint();
        forePaint.setAntiAlias(true);
        forePaint.setColor(foreColor);
        forePaint.setStyle(Paint.Style.STROKE);
        forePaint.setStrokeWidth(circleWidth);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (radius == 0) {
            radius = Math.min(viewHeight, viewWidth) / 6; //获取半径
        }
        rectF = new RectF(-radius, -radius, radius, radius);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动到中间
        canvas.translate(viewWidth / 2, viewHeight / 2);
        canvas.drawCircle(0, 0, radius, backPaint);
        canvas.drawArc(rectF, 270, angle, false, forePaint);
    }


    /**
     * 更新当前的进度
     *
     * @param scale
     */
    public void setProgress(float scale) {

        angle = 360 * scale;
        invalidate();

    }

}
