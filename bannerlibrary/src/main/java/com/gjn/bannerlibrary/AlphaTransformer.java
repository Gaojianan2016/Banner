package com.gjn.bannerlibrary;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author gjn
 * @time 2018/9/17 16:30
 */

public class AlphaTransformer implements ViewPager.PageTransformer {
    private final static float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1 || position > 1) {
            page.setAlpha(MIN_ALPHA);
        }else {
            if(position < 0){
                page.setAlpha(MIN_ALPHA + (1 + position) * (1 - MIN_ALPHA));
            }else {
                page.setAlpha(MIN_ALPHA + (1 - position) * (1 - MIN_ALPHA));
            }
        }
    }
}
