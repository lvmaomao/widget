package com.qingsongchou.library.widget.radar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.qingsongchou.library.widget.R;

/**
 * Created by wsl on 15-8-14.
 */
public class RadarView extends View {

    //bound path
    private Path fivePentagonOutPath;
    private Path fourPentagonOutPath;
    private Path thirdPentagonOutPath;
    private Path secondPentagonOutPath;
    private Path firstPentagonOutPath;

    private Path firstInnerLinePath;
    private Path secondInnerLinePath;
    private Path thirdInnerLinePath;
    private Path fourInnerLinePath;
    private Path fiveInnerLinePath;

    //inner content path
    private Path contentPath;

    //paint
    private Paint pentagonLinePathPaint;
    private Paint pentagonFillPathPaint;
    private Paint innerLinePaint;
    private Paint contentPathPaint;
    private Paint contentTextPaint;
    private Paint descriptionTextPaint;

    private int firstPointNumber;
    private int secondPointNumber;
    private int thirdPointNumber;
    private int fourPointNumber;
    private int fivePointNumber;

    //rect
    private RectF pentagonRect;
    private RectF firstDescriptionRect;
    private RectF secondDescriptionRect;
    private RectF thirdDescriptionRect;
    private RectF fourDescriptionRect;
    private RectF fiveDescriptionRect;

    private Drawable firstDescriptionDrawable;
    private Drawable secondDescriptionDrawable;
    private Drawable thirdDescriptionDrawable;
    private Drawable fourDescriptionDrawable;
    private Drawable fiveDescriptionDrawable;

    private String firstDescriptionText;
    private String secondDescriptionText;
    private String thirdDescriptionText;
    private String fourDescriptionText;
    private String fiveDescriptionText;

    private int defaultWidth;
    private int defaultHeight;
    private float descriptionPadding;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        defaultWidth = context.getResources().getDimensionPixelSize(R.dimen.radar_view_width);
        defaultHeight = context.getResources().getDimensionPixelSize(R.dimen.radar_view_height);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
        firstDescriptionText = a.getString(R.styleable.RadarView_firstRadarText);
        secondDescriptionText = a.getString(R.styleable.RadarView_secondRadarText);
        thirdDescriptionText = a.getString(R.styleable.RadarView_thirdRadarText);
        fourDescriptionText = a.getString(R.styleable.RadarView_fourRadarText);
        fiveDescriptionText = a.getString(R.styleable.RadarView_fiveRadarText);

        firstDescriptionDrawable = a.getDrawable(R.styleable.RadarView_firstRadarDrawable);
        secondDescriptionDrawable = a.getDrawable(R.styleable.RadarView_secondRadarDrawable);
        thirdDescriptionDrawable = a.getDrawable(R.styleable.RadarView_thirdRadarDrawable);
        fourDescriptionDrawable = a.getDrawable(R.styleable.RadarView_fourRadarDrawable);
        fiveDescriptionDrawable = a.getDrawable(R.styleable.RadarView_fiveRadarDrawable);
        a.recycle();

        fivePentagonOutPath = new Path();
        fourPentagonOutPath = new Path();
        thirdPentagonOutPath = new Path();
        secondPentagonOutPath = new Path();
        firstPentagonOutPath = new Path();

        fiveInnerLinePath = new Path();
        fourInnerLinePath = new Path();
        thirdInnerLinePath = new Path();
        secondInnerLinePath = new Path();
        firstInnerLinePath = new Path();

        contentPath = new Path();

        pentagonLinePathPaint = new Paint();
        pentagonLinePathPaint.setAntiAlias(true);
        pentagonLinePathPaint.setColor(0x66c2ebd3);
        pentagonLinePathPaint.setStyle(Paint.Style.STROKE);
        pentagonLinePathPaint.setStrokeWidth(1);

        innerLinePaint = new Paint();
        innerLinePaint.setAntiAlias(true);
        innerLinePaint.setColor(0x6650cb8a);
        innerLinePaint.setStyle(Paint.Style.STROKE);
        innerLinePaint.setStrokeWidth(1);

        pentagonFillPathPaint = new Paint();
        pentagonFillPathPaint.setAntiAlias(true);
        pentagonFillPathPaint.setColor(0xffedf9f2);
        pentagonFillPathPaint.setStyle(Paint.Style.FILL);

        contentPathPaint = new Paint();
        contentPathPaint.setAntiAlias(true);
        contentPathPaint.setColor(0xff19b458);
        contentPathPaint.setStyle(Paint.Style.FILL);

        contentTextPaint = new Paint();
        contentTextPaint.setAntiAlias(true);
        contentTextPaint.setColor(Color.WHITE);
        contentTextPaint.setTextAlign(Paint.Align.CENTER);
        contentTextPaint.setTextSize(sp2px(33));

        descriptionTextPaint = new Paint();
        descriptionTextPaint.setAntiAlias(true);
        descriptionTextPaint.setColor(0xff666666);
        descriptionTextPaint.setTextAlign(Paint.Align.CENTER);
        descriptionTextPaint.setTextSize(sp2px(12));

        pentagonRect = new RectF();
        firstDescriptionRect = new RectF();
        secondDescriptionRect = new RectF();
        thirdDescriptionRect = new RectF();
        fourDescriptionRect = new RectF();
        fiveDescriptionRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setupBounds();
        setupPaint();
    }

    private void setupPaint() {
        int width = getWidth();
        contentTextPaint.setTextSize(width * 0.105f);
        descriptionTextPaint.setTextSize(width * 0.038f);

    }

    private void setupBounds() {
        int width = getWidth();
        int height = getHeight();

        //number two or five 146px
        float outTextAndImageWidth = dp2px(48.6f);
        //number first or third or four
        float outTextAndImageHeight = outTextAndImageWidth;

        //number two or five 186px
        float translateX = dp2px(62f);
        //number first or third or four
        float translateY = translateX;

        //description text and image padding 24px
        descriptionPadding = dp2px(8f);
        //description margin to pentagon 40 px, number two and five margin to pentagon
        float descriptionMargin = dp2px(13.3f);
        //description image height 80px
        float descriptionImageHeight = dp2px(26.6f);
        //description text height 42px
        float descriptionTextHeight = dp2px(14f);
        //number third and four horizontal margin to third or four point 10px
        float descriptionRectHorizontalOffset = dp2px(3.3f);
        //number third and four vertical margin to third or four point 20px
        float descriptionRectVerticalOffset = dp2px(6.6f);

        pentagonRect.set(translateX, translateY,
                width - translateX,
                height - translateY);

        float pentagonWidth = pentagonRect.width();
        float radius = pentagonWidth / 2;

        double sin18 = Math.sin(Math.toRadians(18));
        double sin54 = Math.sin(Math.toRadians(54));
        double tan9 = Math.tan(Math.toRadians(9));
        double sin36 = Math.sin(Math.toRadians(36));

        float firstPointX = pentagonWidth / 2;
        float firstPointY = 0;

        float fivePointY = (float) (radius * (1 - sin18));
        float fivePointX = (float) (radius * sin18 * tan9);

        float secondPointY = fivePointY;
        float secondPointX = pentagonWidth - fivePointX;

        float fourPointX = (float) (radius * (1 - sin36));
        float fourPointY = (float) (radius * (1 + sin54));

        float thirdPointX = pentagonWidth - fourPointX;
        float thirdPointY = fourPointY;

        firstDescriptionRect.set(0, 0, width, outTextAndImageHeight);
        secondDescriptionRect.set(secondPointX + descriptionMargin + pentagonRect.left,
                secondPointY - descriptionPadding - descriptionImageHeight + pentagonRect.top,
                width,
                secondPointY + descriptionTextHeight + pentagonRect.top);

        thirdDescriptionRect.set(thirdPointX - descriptionRectHorizontalOffset + pentagonRect.left,
                thirdPointY + descriptionRectVerticalOffset + pentagonRect.top,
                thirdPointX - descriptionRectHorizontalOffset + pentagonRect.left + outTextAndImageWidth,
                thirdPointY + descriptionRectVerticalOffset + pentagonRect.top + outTextAndImageHeight);
        fourDescriptionRect.set(fourPointX + descriptionRectHorizontalOffset + pentagonRect.left - outTextAndImageWidth,
                fourPointY + descriptionRectVerticalOffset + pentagonRect.top,
                fourPointX + descriptionRectHorizontalOffset + pentagonRect.left,
                fourPointY + descriptionRectVerticalOffset + pentagonRect.top + outTextAndImageHeight);
        fiveDescriptionRect.set(0,
                fivePointY - descriptionPadding - descriptionImageHeight + pentagonRect.top,
                pentagonRect.left - (descriptionMargin - fivePointX),
                fivePointY + descriptionTextHeight + pentagonRect.top);

        fivePentagonOutPath.moveTo(firstPointX, firstPointY);
        fivePentagonOutPath.lineTo(secondPointX, secondPointY);
        fivePentagonOutPath.lineTo(thirdPointX, thirdPointY);
        fivePentagonOutPath.lineTo(fourPointX, fourPointY);
        fivePentagonOutPath.lineTo(fivePointX, fivePointY);
        fivePentagonOutPath.lineTo(firstPointX, firstPointY);

        firstInnerLinePath.moveTo(firstPointX, firstPointY);
        firstInnerLinePath.lineTo(radius, radius);
        secondInnerLinePath.moveTo(secondPointX, secondPointY);
        secondInnerLinePath.lineTo(radius, radius);
        thirdInnerLinePath.moveTo(thirdPointX, thirdPointY);
        thirdInnerLinePath.lineTo(radius, radius);
        fourInnerLinePath.moveTo(fourPointX, fourPointY);
        fourInnerLinePath.lineTo(radius, radius);
        fiveInnerLinePath.moveTo(fivePointX, fivePointY);
        fiveInnerLinePath.lineTo(radius, radius);

        moveBoundPath(firstInnerLinePath, false);
        moveBoundPath(secondInnerLinePath, true);
        moveBoundPath(thirdInnerLinePath, true);
        moveBoundPath(fourInnerLinePath, true);
        moveBoundPath(fiveInnerLinePath, true);
        moveBoundPath(firstInnerLinePath, true);

        setupContentBounds();
    }

    private void setupContentBounds() {
        contentPath.reset();
        moveContentPath(firstPointNumber, firstInnerLinePath, false);
        moveContentPath(secondPointNumber, secondInnerLinePath, true);
        moveContentPath(thirdPointNumber, thirdInnerLinePath, true);
        moveContentPath(fourPointNumber, fourInnerLinePath, true);
        moveContentPath(fivePointNumber, fiveInnerLinePath, true);
        moveContentPath(firstPointNumber, firstInnerLinePath, true);
    }

    private void moveContentPath(int number, Path innerLinePath, boolean lineTo) {
        float[] pos = new float[2];
        float[] tan = new float[2];
        PathMeasure pathMeasure = new PathMeasure(innerLinePath, false);
        float length = pathMeasure.getLength();
        float moveDistance = (100 - number) * length / 100;
        pathMeasure.getPosTan(moveDistance, pos, tan);
        if (lineTo) {
            contentPath.lineTo(pos[0], pos[1]);
            return;
        }
        contentPath.moveTo(pos[0], pos[1]);
    }

    /**
     * @param path   path
     * @param lineTo true means lineTo or moveTo
     */
    private void moveBoundPath(Path path, boolean lineTo) {
        float[] pos = new float[2];
        float[] tan = new float[2];
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float length = pathMeasure.getLength();
        float moveDistance = length / 5;
        for (int i = 0; i < 4; i++) {
            pathMeasure.getPosTan(moveDistance * (i + 1), pos, tan);
            switch (i) {
                case 0:
                    if (lineTo) {
                        fourPentagonOutPath.lineTo(pos[0], pos[1]);
                        continue;
                    }
                    fourPentagonOutPath.moveTo(pos[0], pos[1]);
                    break;
                case 1:
                    if (lineTo) {
                        thirdPentagonOutPath.lineTo(pos[0], pos[1]);
                        continue;
                    }
                    thirdPentagonOutPath.moveTo(pos[0], pos[1]);
                    break;
                case 2:
                    if (lineTo) {
                        secondPentagonOutPath.lineTo(pos[0], pos[1]);
                        continue;
                    }
                    secondPentagonOutPath.moveTo(pos[0], pos[1]);
                    break;
                case 3:
                    if (lineTo) {
                        firstPentagonOutPath.lineTo(pos[0], pos[1]);
                        continue;
                    }
                    firstPentagonOutPath.moveTo(pos[0], pos[1]);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && widthSize > defaultWidth) {
            width = widthSize;
        } else {
            width = defaultWidth;
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY && heightSize > defaultHeight) {
            height = heightSize;
        } else {
            height = defaultHeight;
        }

        int scale = Math.min(width, height);
        setMeasuredDimension(scale, scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(pentagonRect.left, pentagonRect.top);

        //draw bound pentagon path, number five that is outside than other
        canvas.drawPath(fivePentagonOutPath, pentagonLinePathPaint);

        //fill pentagon
        fivePentagonOutPath.setFillType(Path.FillType.EVEN_ODD);
        canvas.drawPath(fivePentagonOutPath, pentagonFillPathPaint);

        //draw content path
        canvas.drawPath(contentPath, contentPathPaint);

        //draw other bound pentagon path
        canvas.drawPath(fourPentagonOutPath, pentagonLinePathPaint);
        canvas.drawPath(thirdPentagonOutPath, pentagonLinePathPaint);
        canvas.drawPath(secondPentagonOutPath, pentagonLinePathPaint);
        canvas.drawPath(firstPentagonOutPath, pentagonLinePathPaint);

        //draw inner line base on center
        canvas.drawPath(firstInnerLinePath, innerLinePaint);
        canvas.drawPath(secondInnerLinePath, innerLinePaint);
        canvas.drawPath(thirdInnerLinePath, innerLinePaint);
        canvas.drawPath(fourInnerLinePath, innerLinePaint);
        canvas.drawPath(fiveInnerLinePath, innerLinePaint);

        canvas.restore();

        //draw outside text and image
        drawDescription(canvas, firstDescriptionRect, firstDescriptionText, descriptionTextPaint, firstDescriptionDrawable);
        drawDescription(canvas, secondDescriptionRect, secondDescriptionText, descriptionTextPaint, secondDescriptionDrawable);
        drawDescription(canvas, thirdDescriptionRect, thirdDescriptionText, descriptionTextPaint, thirdDescriptionDrawable);
        drawDescription(canvas, fourDescriptionRect, fourDescriptionText, descriptionTextPaint, fourDescriptionDrawable);
        drawDescription(canvas, fiveDescriptionRect, fiveDescriptionText, descriptionTextPaint, fiveDescriptionDrawable);


        //draw content text
        float textHeight = contentTextPaint.descent() - contentTextPaint.ascent();
        float verticalOffset = (textHeight / 2) - contentTextPaint.descent();
        String text = Integer.toString((firstPointNumber + secondPointNumber + thirdPointNumber +
                fourPointNumber + fivePointNumber) / 5);
        canvas.drawText(text, getWidth() / 2, getHeight() / 2 + verticalOffset, contentTextPaint);
    }

    private void drawDescription(Canvas canvas, RectF rectF, String text, Paint textPaint, Drawable drawable) {
        canvas.save();
        canvas.translate(rectF.left, rectF.top);

        float textHeight = textPaint.descent() - textPaint.ascent();
        canvas.drawText(text, rectF.width() / 2, rectF.height(), textPaint);

        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        int drawableLeft = (int) ((rectF.width() - drawableWidth) / 2);
        int drawableTop = (int) (rectF.height() - textHeight - descriptionPadding - drawableHeight);
        drawable.setBounds(drawableLeft, drawableTop, drawableLeft + drawableWidth,
                drawableTop + drawableHeight);
        drawable.draw(canvas);

        canvas.restore();
    }

    public void setValues(int firstPointNumber, int secondPointNumber, int thirdPointNumber,
                          int fourPointNumber, int fivePointNumber) {
        this.firstPointNumber = firstPointNumber;
        this.secondPointNumber = secondPointNumber;
        this.thirdPointNumber = thirdPointNumber;
        this.fourPointNumber = fourPointNumber;
        this.fivePointNumber = fivePointNumber;
        setupContentBounds();
        invalidate();
    }

    private static final String STATUS_KEY_SUPER = "status_radar_super";
    private static final String STATUS_KEY_NUMBER_FIRST = "status_radar_number_first";
    private static final String STATUS_KEY_NUMBER_SECOND = "status_radar_number_second";
    private static final String STATUS_KEY_NUMBER_THIRD = "status_radar_number_third";
    private static final String STATUS_KEY_NUMBER_FOUR = "status_radar_number_four";
    private static final String STATUS_KEY_NUMBER_FIVE = "status_radar_number_five";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATUS_KEY_SUPER, super.onSaveInstanceState());
        bundle.putInt(STATUS_KEY_NUMBER_FIRST, firstPointNumber);
        bundle.putInt(STATUS_KEY_NUMBER_SECOND, secondPointNumber);
        bundle.putInt(STATUS_KEY_NUMBER_THIRD, thirdPointNumber);
        bundle.putInt(STATUS_KEY_NUMBER_FOUR, fourPointNumber);
        bundle.putInt(STATUS_KEY_NUMBER_FIVE, fivePointNumber);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            firstPointNumber = bundle.getInt(STATUS_KEY_NUMBER_FIRST, 0);
            secondPointNumber = bundle.getInt(STATUS_KEY_NUMBER_SECOND, 0);
            thirdPointNumber = bundle.getInt(STATUS_KEY_NUMBER_THIRD, 0);
            fourPointNumber = bundle.getInt(STATUS_KEY_NUMBER_FOUR, 0);
            fivePointNumber = bundle.getInt(STATUS_KEY_NUMBER_FIVE, 0);
            super.onRestoreInstanceState(bundle.getParcelable(STATUS_KEY_SUPER));
            return;
        }
        super.onRestoreInstanceState(state);
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

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected float sp2px(int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }
}