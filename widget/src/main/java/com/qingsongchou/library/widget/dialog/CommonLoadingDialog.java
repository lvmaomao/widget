package com.qingsongchou.library.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingsongchou.library.widget.R;

/**
 * Created by 俊晓 on 2016/1/6.
 */
public class CommonLoadingDialog extends Dialog {

    private AnimationDrawable animation;
    private ImageView loading;
    private TextView loadingText;
    private Context mContext;

    public CommonLoadingDialog(Context context) {
        super(context, R.style.CommonDialog);
        mContext = context;
    }

    private void initWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        View view = getLayoutInflater().inflate(R.layout.layout_common_dialog, null);
        initView(view);
        setContentView(view);
    }

    private void initView(View view) {
        loading = (ImageView) view.findViewById(R.id.loading);
        loadingText = (TextView) view.findViewById(R.id.loadingText);
        animation = (AnimationDrawable) loading.getDrawable();
    }

    @Override
    public void show() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (!activity.isFinishing()) {
                super.show();
                if (loading != null && animation != null) {
                    loading.clearAnimation();
                    animation.start();
                }
            }
        }
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            if (animation != null) {
                animation.stop();
            }
            if (loading.getAnimation() != null) {
                loading.clearAnimation();
            }
            super.dismiss();
        }
    }

    public void show(String text) {
        this.show();
        loadingText.setText(text);
    }
}