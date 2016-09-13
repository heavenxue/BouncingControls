package com.aibei.lixue.bouncingmenu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aibei.lixue.bouncingmenu.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 模仿支付宝咻一咻控件
 *
 * Created by Administrator on 2016/9/12.
 */
public class BouceBallShoopShoop extends View{
    private Paint mPaint;//画笔
    private int smallRaduias = 30;//最里面小圆的半径

    private Bitmap xiuBitmap;
    private List<Integer> raduiss = new ArrayList<>();
    private long current = System.currentTimeMillis();
    private long mLastCreateTime;
    private long mSpeed = 100;

    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastCreateTime < mSpeed) {
                return;
            }
            //什么时候添加
            if (System.currentTimeMillis() - current > 1000){
                raduiss.add(xiuBitmap.getWidth()/2);
                current = System.currentTimeMillis();
            }

            for (int i = 0;i < raduiss.size();i ++){
                raduiss.set(i,raduiss.get(i) + 10);
            }

            //当半径大于屏幕的时候移除
            Iterator<Integer> it = raduiss.iterator();
            while (it.hasNext()){
                Integer next = it.next();
                if (next >= getWidth()/2){
                    it.remove();
                }
            }
            mLastCreateTime = currentTime;
            invalidate();
            mHandler.postDelayed(runnable,mSpeed);
        }
    };

    public BouceBallShoopShoop(Context context) {
        super(context);
        init();
    }

    public BouceBallShoopShoop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BouceBallShoopShoop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BouceBallShoopShoop(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化
     */
    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setColor(Color.parseColor("#155c7c"));
        xiuBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.xiu);
        smallRaduias = xiuBitmap.getWidth()/2;
        raduiss.add(smallRaduias);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //画圆
        int x = getWidth()/2 - xiuBitmap.getWidth()/2;
        int y = getHeight()/2 - xiuBitmap.getHeight()/2;

        int cx = getWidth()/2;
        int cy = getHeight()/2;

        for (int i =0;i < raduiss.size();i ++){
            float percent = (System.currentTimeMillis() - current) * 1.0f / 1000;
//            float percent = (raduiss.get(i) - xiuBitmap.getWidth()/2)/(getWidth()/2 - xiuBitmap.getWidth()/2);
//            int alpths = 255 - 255*((raduiss.get(i) - xiuBitmap.getWidth()/2)/(getWidth()/2-xiuBitmap.getWidth()/2));
            int alpths = 255 - (int) (255 * percent);
            Log.d("abc","alpths:" + alpths);

            mPaint.setAlpha(alpths);
            canvas.drawCircle(cx,cy,raduiss.get(i),mPaint);
        }
        //画中间的图片
        canvas.drawBitmap(xiuBitmap,x,y,null);
    }

    public void start(){
        mHandler.post(runnable);
    }
}
