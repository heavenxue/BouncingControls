package com.aibei.lixue.bouncingmenu.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 自定义控件--弹跳菜单
 * Created by Administrator on 2016/8/22.
 */
public class BoucingMenu extends View{
    private static final String TAG = BoucingMenu.class.getSimpleName();
    private View view;
    private int resId;
    private ViewGroup mParentVG;//DecorView根布局
    private RelativeLayout rootView;
    private Path mPath = new Path();//路径类
    private Paint mPaint = new Paint();//画笔
    private int arcHeight;
    private int maxArcHeight = 320;
    private Status mStatus = Status.NONE;

    /**
     * 构造函数
     * @param view  a parent from
     * @param resId 加载的菜单布局，里面含有recyclerView
     */
    public BoucingMenu(Context context, View view, int resId) {
        super(context);
        this.view = view;
        this.resId = resId;
        initView();
    }

    public BoucingMenu(Context context, AttributeSet attrs, View view, int resId) {
        super(context, attrs);
        this.view = view;
        this.resId = resId;
        initView();
    }

    public BoucingMenu(Context context, AttributeSet attrs, int defStyleAttr, View view, int resId) {
        super(context, attrs, defStyleAttr);
        this.view = view;
        this.resId = resId;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoucingMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, View view, int resId) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.view = view;
        this.resId = resId;
        initView();
    }

    private void initView(){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        //找到DecorView的跟布局
        mParentVG = findSuitableParent(view);
        rootView = (RelativeLayout)LayoutInflater.from(getContext()).inflate(resId,null);
        Log.i(TAG,"视图的根布局是:" + rootView);
        mParentVG.addView(rootView,lp);
    }

    public static BoucingMenu make(Context mContext,View view, int resId){
        return new BoucingMenu(mContext,view,resId);
    }

    public void show(){
        rootView.addView(this);
    }

    /**
     *找到DecorView的根布局
     * @param view
     */
    private ViewGroup findSuitableParent(View view){
        ViewGroup fallBack = null;
        do{
            if (view instanceof FrameLayout){
                if (view.getId() == android.R.id.content){
                    return (ViewGroup)view;
                }else{
                    fallBack = (ViewGroup) view;
                }
            }
            if (view != null){
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        }while (view != null);
        return fallBack;
    }

    private enum Status{
        NONE,
        STATUS_SMOOTH_UP,
        STATUS_DOWN
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        int currentPointY = 0;
        switch (mStatus){
            case NONE:
                currentPointY = maxArcHeight;
                break;
            case STATUS_SMOOTH_UP:
                currentPointY = getHeight() -(getHeight() - maxArcHeight * Math.min(1,(arcHeight - maxArcHeight/4)));
                break;
            case STATUS_DOWN:
                currentPointY = maxArcHeight;
                break;
        }
        mPath.moveTo(0,(getHeight()-rootView.getChildAt(0).getHeight()));//p0
        //x1,y1为控制点，x2,y2为结束点
        mPath.quadTo(getWidth()/2,getHeight()-rootView.getChildAt(0).getHeight() - currentPointY,getWidth(),getHeight()-rootView.getChildAt(0).getHeight());
        mPath.lineTo(getWidth(),getHeight());
        mPath.lineTo(0,getHeight());
        mPath.close();
        Log.i(TAG,"rootView的高度:" + rootView.getHeight());
        Log.i(TAG,"currentPointY:" + currentPointY);
        Log.i(TAG,"第一个点:0," + (getHeight()-rootView.getChildAt(0).getHeight()));
        Log.i(TAG,"第二个点:" + (getWidth()/2) +"," + (getHeight()-rootView.getChildAt(0).getHeight() + currentPointY)+"," +getWidth() + "," + getWidth() +"," +(getHeight()-rootView.getChildAt(0).getHeight()));
        Log.i(TAG,"第三个点:" + getWidth()+"," + getHeight());
        Log.i(TAG,"第四个点:0," + getHeight());

        canvas.drawPath(mPath,mPaint);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,maxArcHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                arcHeight = (int) valueAnimator.getAnimatedValue();
                mStatus = Status.STATUS_SMOOTH_UP;
//                if (arcHeight == maxArcHeight){
//                    //往下弹
//                    bouce();
//                }
                invalidate();
            }
        });
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new OvershootInterpolator(4f));
        valueAnimator.start();
    }

    private void bouce(){
        mParentVG.removeView(rootView);
    }
}
