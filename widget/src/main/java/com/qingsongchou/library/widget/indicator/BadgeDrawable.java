package com.qingsongchou.library.widget.indicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.TextUtils;

/**
 * Created by wsl on 15-8-11.
 */
public class BadgeDrawable extends ShapeDrawable {

    private final Paint textPaint;

    //default must -1, draw will not invoke if not
    private int width;
    private int height;

    private String text;
    private int textSize;
    private int textColor;

    public BadgeDrawable(Builder builder) {
        super(builder.shape);

        this.width = builder.width;
        this.height = builder.height;

        //text
        text = builder.text;
        textSize = builder.textSize;
        textColor = builder.textColor;

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(false);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        //background
        Paint paint = getPaint();
        paint.setColor(builder.backgroundColor);

        //init
        if(TextUtils.isEmpty(text)) {
            text = "";
            setAlpha(0);
        } else {
            setAlpha(255);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        width = bounds.width();
        height = bounds.height();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = getBounds();

        int count = canvas.save();
        canvas.translate(r.left, r.top);

        // draw text
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int textSize = this.textSize < 0 ? (Math.min(width, height) / 2) : this.textSize;
        textPaint.setTextSize(textSize);
        canvas.drawText(text, width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        canvas.restoreToCount(count);
    }

    @Override
    public void setAlpha(int alpha) {
        super.setAlpha(alpha);
        textPaint.setAlpha(alpha);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public void setText(String text) {
        if(TextUtils.isEmpty(text)) {
            setAlpha(0);
            this.text = "";
        } else {
            setAlpha(255);
            this.text = text;
        }
        invalidateSelf();
    }

    public static Builder builder(String text, float radius) {
        return new Builder(text, radius);
    }

    public static class Builder implements IBuilder {
        private RectShape shape;

        private String text;
        private int textColor;
        private int textSize;

        private int backgroundColor;

        private int width;
        private int height;
        private float radius;

        private Builder(String text, float radius) {
            width = -1;
            height = -1;

            this.text = text;
            this.textColor = Color.WHITE;
            this.textSize = -1;

            backgroundColor = 0xffd3321b;

            this.radius = radius;

            float[] radii = {radius, radius, radius, radius, radius, radius, radius, radius};
            this.shape = new RoundRectShape(radii, null, null);
        }

        public Builder textColor(int color) {
            this.textColor = color;
            return this;
        }

        public Builder textSize(int size) {
            this.textSize = size;
            return this;
        }

        public Builder backgroundColor(int color) {
            this.backgroundColor = color;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        @Override
        public BadgeDrawable build() {
            return new BadgeDrawable(this);
        }
    }

    public interface IBuilder {
        BadgeDrawable build();
    }
}
