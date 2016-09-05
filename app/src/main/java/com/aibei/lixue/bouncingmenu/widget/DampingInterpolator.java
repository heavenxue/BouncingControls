package com.aibei.lixue.bouncingmenu.widget;

import android.view.animation.Interpolator;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DampingInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float v) {
        return (float)(1 - Math.exp(-20 * v) * Math.cos(20 * v));
    }
}
