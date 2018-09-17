package com.gjn.bannerlibrary;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author gjn
 * @time 2018/9/17 16:30
 */

public class ScaleTransformer implements ViewPager.PageTransformer {
    private final static float MIN_SCALE = 0.7f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1 || position > 1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }else {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float scale;
            if(position < 0){
                scale = 1 + 0.3f * position;
            }else {
                scale = 1 - 0.3f * position;
            }
            page.setScaleX(scale);
            page.setScaleY(scale);
        }
    }
}
