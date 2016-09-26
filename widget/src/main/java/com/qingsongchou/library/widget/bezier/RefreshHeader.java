package com.qingsongchou.library.widget.bezier;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * RefreshHeader
 * Created by smileCloud on 16/9/23.
 */
public interface RefreshHeader {

    void onStart(int dragPosition, View refreshHead);
    void onDragging(float distance, float percent, View refreshHead);
    void onReadyToRelease(View refreshHead);

    @NonNull
    View getView(ViewGroup container);
    void onRelease(View rightLayout);

}
