package com.qingsongchou.library.widget.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingsongchou.library.widget.R;


/**
 * Created by 俊晓 on 2016/01/13.
 */
public class CommonToast {

    public CommonToast(Context context,String msg,boolean flag) {
        Context mContext = context.getApplicationContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.layout_common_toast, null);
        ImageView commonPromptIconView = (ImageView) layout.findViewById(R.id.prompt_dialog_icon);
        TextView commonPromptTextView = (TextView) layout.findViewById(R.id.prompt_dialog_content);
        Toast toast = new Toast(mContext);
        commonPromptTextView.setText(msg);
        if(flag){
            commonPromptIconView.setImageResource(R.mipmap.ic_prompt_success);
        }else{
            commonPromptIconView.setImageResource(R.mipmap.ic_prompt_error);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static CommonToast makeSuccessText(Context context, String msg) {
        return new CommonToast(context,msg,true);
    }

    public static CommonToast makeSuccessText(Context context, int resourceId) {
        String msg = context.getResources().getString(resourceId);
        return new CommonToast(context,msg,true);
    }

    public static CommonToast makeErrorText(Context context, String msg) {
        return new CommonToast(context,msg,false);
    }

    public static CommonToast makeErrorText(Context context, int resourceId) {
        String msg = context.getResources().getString(resourceId);
        return new CommonToast(context,msg,false);
    }
}
