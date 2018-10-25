package com.minxing.client.widget;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MultiImageGradientsLayout extends RelativeLayout {
    private final int ANIMRESULT = 1;
    private final int ANIMOUT = 2;
    private final int ANIMIN = 3;
    private final int IMAGESET = 4;
    private Context context;
    private ImageView img1;
    private ImageView img2;
    private ArrayList<Bitmap> bitmaps;
    private long time = 6000;  //值是加上渐变的时间效果好
    private int index = 0; //计算切换状态
    private boolean outOrIn = true;
    private Timer timer = new Timer();
    private ObjectAnimator outAnimation;
    private ObjectAnimator inAnimation;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ANIMRESULT:
                    index = 0;
                    break;
                case ANIMOUT:
                    outAnimation = createOutAnim(img1,img2);
                    break;
                case ANIMIN:
                    outAnimation = createOutAnim(img2,img1);
                    break;
            }
        }
    };

    public MultiImageGradientsLayout(Context context) {
        this(context, null);
    }

    public MultiImageGradientsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createContentView();

    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setBitmaps(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
        //如果没有图片，那么直接return
        if (bitmaps == null || bitmaps.size() == 0) {
            return;
        }
        //如果就是一张图，那么img1控件显示一张图
        if (bitmaps.size() == 1) {
            img1.setImageBitmap(bitmaps.get(0));
            return;
        }
        if (bitmaps.size() >= 2) {
            img1.setImageBitmap(bitmaps.get(0));
            img2.setAlpha(0.0f);
        }

    }

    //创建两个imageview
    private void createContentView() {
        img1 = new ImageView(context);
        img2 = new ImageView(context);
        img1.setScaleType(ImageView.ScaleType.FIT_XY);
        img2.setScaleType(ImageView.ScaleType.FIT_XY);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        img1.setLayoutParams(params);
        img2.setLayoutParams(params);
        addView(img2); //先添加在最底下被覆盖
        addView(img1);

    }

    //创建淡出的动画
    private ObjectAnimator createOutAnim(final ImageView imageView1, final ImageView imageView2) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(imageView1, "alpha", 1.0f, 0.0f);
        animator.setDuration(3000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(index == bitmaps.size()) {
                    imageView2.setImageBitmap(bitmaps.get(0));
                    handler.sendEmptyMessage(ANIMRESULT);
                }else{
                    if(index == bitmaps.size()-1){
                        imageView2.setImageBitmap(bitmaps.get(0));
                    }else {
                        imageView2.setImageBitmap(bitmaps.get(index + 1));
                    }
                }
                //img2开始展示
                inAnimation = createInAnim(imageView2);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // img1.setImageBitmap(bitmaps.get(index));
                index++;
                if(index == bitmaps.size()){
                    index = 0;
                }
                if(outOrIn){
                    outOrIn = false;
                }else{
                    outOrIn = true;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        return animator;
    }

    //创建淡入的动画
    private ObjectAnimator createInAnim(ImageView imageView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 0.0f, 1.0f);
        animator.setDuration(3000);
        animator.start();
        return animator;
    }

    public void startAnim() {
        //如果就是两张图，那么进行直接淡入淡出
        if (bitmaps.size() >= 2) {
            timer.schedule(timerTask, 3000, time);
        }
    }

    public void destroy() {
        if(outAnimation!=null){
            outAnimation.cancel();
        }
        if (inAnimation != null){
            inAnimation.cancel();
        }
        if(timer!=null){
            timer.cancel();
        }
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (outOrIn) {
                handler.sendEmptyMessage(ANIMOUT);
            } else {
                handler.sendEmptyMessage(ANIMIN);
            }
        }
    };
}
