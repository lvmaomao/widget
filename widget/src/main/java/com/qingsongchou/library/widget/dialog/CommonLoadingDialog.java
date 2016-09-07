package com.qingsongchou.library.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.qingsongchou.library.widget.R;

/**
 * Created by 俊晓 on 2016/1/6.
 */
public class CommonLoadingDialog extends Dialog {
    private Animation animation;
    private ImageView border;
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
        border = (ImageView) view.findViewById(R.id.border);
        initAnimation();
    }

    private void initAnimation() {
        animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1200);
        animation.setRepeatCount(-1);
        animation.getFillAfter();
        animation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void show() {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (!activity.isFinishing()) {
                super.show();
                if (border != null) {
                    if (border.getAnimation() != null) {
                        border.clearAnimation();
                    }
                    border.setAnimation(animation);
                    animation.start();
                }
            }
        }
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            super.dismiss();
        }
    }

}