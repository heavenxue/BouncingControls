package com.aibei.lixue.bouncingmenu.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.DecelerateInterpolator;

import com.aibei.lixue.bouncingmenu.R;

/**
 * 自定义一个view
 *
 * 两条绳子带动着小球弹跳
 *
 * 利用surfaceView性能更好(高效绘制，具有缓冲性能)
 *
 * Created by Administrator on 2016/9/5.
 */
public class BouceProgressBar extends SurfaceView implements SurfaceHolder.Callback{
    private static final int STATE_DOWN = 1;//向下的状态
    private static final int STATE_UP = 2;//向上的状态
    private Canvas canvas;
    private Paint mPaint;//画笔
    private Path mPath;//路径

    private int mLineLength = 200;//线的长度
    private int mLineWidth = 5;//线宽
    private int mLineColor ;//线的颜色
    private int mPointColor;//点的颜色

    private float mDownDistance;//绳子最底端到中间水平的高度 （中间到达的某一点）
    private float mUpDistance;//绳子最高端到中间水平的高度 （中间到达的某一点）
    private float freeBallDistance;//自由落体的距离

    //初始化各个阶段动画
    private ValueAnimator downController;//向下运动
    private ValueAnimator upController;//向上运动
    private ValueAnimator freeDownController;//自由落体

    private AnimatorSet animatorSet;
    private  int state;//状态
    private boolean isBounced = false;//是否弹起来
    private boolean isBallFreeUp = false;//是否自由上抛
    private boolean isUpControllerDied = false;//是否开始下抛
    private boolean isAnimationShowing = false;//动画是否显示




    public BouceProgressBar(Context context) {
        super(context);
        init();
    }

    public BouceProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BouceProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BouceProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);//线帽，是圆头的
        mPath = new Path();
        mLineColor = getResources().getColor(R.color.colorLine);
        mPointColor = getResources().getColor(R.color.colorPoint);
        getHolder().addCallback(this);//会调用surfaceCreated方法
        initController();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        canvas = surfaceHolder.lockCanvas();//锁定画布
        //开始画
        draw(canvas);
        //解除锁定
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        surfaceHolder.removeCallback(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1、绳子，一条绳子用部分二阶贝塞尔曲线画
        mPaint.setColor(mLineColor);
        mPath.reset();
        mPath.moveTo(getWidth()/2-mLineLength/2,getHeight()/2);//线先移到起始点
        if (state == STATE_DOWN){
            //下坠
            //左边的贝塞尔曲线
            mPath.quadTo((float) (getWidth()/2-mLineLength/2 + mLineLength*0.375),getHeight()/2+mDownDistance,getWidth()/2,getHeight()/2+mDownDistance);//控制点，结束点
            //右边的贝塞尔曲线
            mPath.quadTo((float) (getWidth()/2 + mLineLength/2 - mLineLength*0.375),getHeight()/2+mDownDistance,getWidth()/2+mLineLength/2,getHeight()/2);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mPath,mPaint);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mPointColor);
            canvas.drawCircle(getWidth()/2,getHeight()/2+mDownDistance-10,10,mPaint);//小球的半径是10

        }else if(state == STATE_UP){
            //向上弹，小绳子照样画
            mPath.quadTo((float)(getWidth()/2-mLineLength/2 + mLineLength * 0.375),getHeight()/2 +(50 - mUpDistance),getWidth()/2,getHeight()/2 +(50-mDownDistance));
            mPath.quadTo((float)(getWidth()/2+mLineLength/2 - mLineLength * 0.375),getHeight()/2 +(50 - mUpDistance),getWidth()/2+mLineLength/2,getHeight()/2);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mPath,mPaint);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mPointColor);
            //弹性小球
            //第三种状态--自由落体小球
            //********假设下坠的最大高度是50*****************
            if (!isBounced){
                //正常上升
                canvas.drawCircle(getWidth()/2,getHeight()/2+(50 - mUpDistance) -10,10,mPaint);
            }else{
                //自由落体
                canvas.drawCircle(getWidth()/2,(getHeight()/2-freeBallDistance-10),10,mPaint);
            }
        }


        //先画两头的点
        mPaint.setColor(mPointColor);
        mPath.reset();
        canvas.drawCircle((getWidth()/2-mLineLength/2),getHeight()/2,10,mPaint);
        canvas.drawCircle((getWidth()/2+mLineLength/2),getHeight()/2,10,mPaint);
        //再画由两个二阶贝塞尔曲线组成的小绳

        //2、弹性小球
        //3、两边的小球
    }

    private void initController(){
        downController = ValueAnimator.ofFloat(0,1);
        downController.setDuration(500);
        downController.setInterpolator(new DecelerateInterpolator());
        downController.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mDownDistance = 50 * (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        downController.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                state = STATE_DOWN;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        upController = ValueAnimator.ofFloat(0,1);
        upController.setDuration(900);
        upController.setInterpolator(new DampingInterpolator());
        upController.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mUpDistance = 50 * (float) valueAnimator.getAnimatedValue();
                if (mUpDistance >= 50){
                    //进入自由落体状态
                    isBounced = true;
                    if (!freeDownController.isRunning() && !freeDownController.isStarted() && !isBallFreeUp){
                        freeDownController.start();
                    }
                }
                postInvalidate();

            }
        });
        upController.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                state = STATE_UP;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        freeDownController = ValueAnimator.ofFloat(0,1);
        freeDownController.setDuration(900);
        freeDownController.setInterpolator(new DecelerateInterpolator());
        freeDownController.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //一个公式解决上升减速和下降加速
                float t = (float)valueAnimator.getAnimatedValue();
                freeBallDistance = 34 * t -5 *t*t;//加速度S = v*t*t
                if (isUpControllerDied){
                    postInvalidate();
                }
            }
        });
        freeDownController.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isBallFreeUp = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimationShowing = false;//自由落体下降结束后，动画结束
                startTotalAnimations();//循环第二次动画
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.play(downController).before(upController);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isAnimationShowing = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    private void startTotalAnimations(){
        if (isAnimationShowing){
            return;
        }
        if (animatorSet.isRunning()){
            animatorSet.end();
            animatorSet.cancel();
        }
        isBounced = false;
        isBallFreeUp = false;
        isUpControllerDied = false;
        animatorSet.start();


    }
}
