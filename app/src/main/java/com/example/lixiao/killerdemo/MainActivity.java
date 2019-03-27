package com.example.lixiao.killerdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NestedScrollView.OnScrollChangeListener, View.OnTouchListener {
    private RelativeLayout searchBar;
    private TextView city;
    private ImageView searchIv;//搜索用的图片

    private RelativeLayout titleBar;
    private TabLayout tabLayout;
    private ImageView classIV;//分类的图片
    private NestedScrollView scrollView;

    private SinWaveView wave;
    private EditText input;

    /**
     * 定义一个最大滑动距离
     */
    private float maxScroll;
    private float scroll110;

    private float searchIVNomalX, searchIVNomalY = 0;
    private float offsetIVEndX, offsetIVEndY = 0;
    /**
     * tabbar栏左移的距离
     */
    private float leftOffset = 0;

    private float barMargginRight = 0;
    private float classMargginRight = 0;

    private int currentScrollY;

    private boolean isAuto;
    private boolean isAnime;

    private int direction;//0向下，1向上
    private boolean idDown;
    private boolean isEidtForuse = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBar = findViewById(R.id.search_bar);
        city = findViewById(R.id.city);
        searchIv = findViewById(R.id.iv_search);

        titleBar = findViewById(R.id.title_bar);
        tabLayout = findViewById(R.id.taglayout);
        classIV = findViewById(R.id.classiv);
        input = findViewById(R.id.input);

        scrollView = findViewById(R.id.scrollview);
        scrollView.setOnScrollChangeListener(this);
        scrollView.setOnTouchListener(this);

        wave = findViewById(R.id.wave);

        //50dp 指的是 searchbar的高度 为50个dp
        maxScroll = dip2px(this, 50);//searchbar的高度
        scroll110 = dip2px(this, 110);//波浪线和scrollview的marggintop
        scrollView.setNestedScrollingEnabled(true);
        leftOffset = dip2px(this, 37);//titlebar的缩进
        barMargginRight = dip2px(this, 50);//tablayout的marginright
        classMargginRight = dip2px(this, 12);//classimageview的marginright
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isEidtForuse = true;
                }else{
                    isEidtForuse = false;
                }
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                closeInputMethod(tabLayout);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //计算searchImageview的偏移总量
        searchIVNomalX = dip2px(this, 65);
        searchIVNomalY = searchIv.getY();
        float marggin12 = dip2px(this, 12);
        float marggin25 = dip2px(this, 25);
        int screenw = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        offsetIVEndX = screenw - marggin12 - marggin25 - searchIVNomalX;
        offsetIVEndY = dip2px(this, 4.5f);
    }



    @Override
    public void onScrollChange(NestedScrollView nestedScrollView, int newx, int newy, int oldx, int oldy) {
        currentScrollY = newy;
        if (newy > oldy) {
            direction = 1;
        } else if (newy < oldy) {
            direction = 0;
        }

        closeInputMethod(scrollView);

        if (!isAuto){
           startAnime(currentScrollY);
           if (!idDown) {
               if ( currentScrollY < maxScroll ) {
                   if (direction == 0) {
                       autoCloseAnime();
                   }else{
                       autoOpenAnime();
                   }
               }
           }
       }
    }

    private void closeInputMethod(View view){
        if (isEidtForuse) {
            isEidtForuse = false;
            input.clearFocus();
            view.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isAuto) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                idDown = true;
                break;
            case MotionEvent.ACTION_UP:{
                idDown = false;
                if (currentScrollY>maxScroll) return false;
                if (currentScrollY <= 5) {
                    autoCloseAnime();
                } else if (currentScrollY >= maxScroll - 5 && currentScrollY < maxScroll) {
                    autoOpenAnime();
                } else if (currentScrollY >= 5 && currentScrollY <= maxScroll - 5) {
                    if (direction == 0) {
                        autoCloseAnime();
                    }else{
                        autoOpenAnime();
                    }
                }
            }
                break;
        }
        return false;
    }

    private void autoOpenAnime(){
        isAuto = true;
        isAnime = true;
        ValueAnimator animator = ValueAnimator.ofInt(currentScrollY, (int) maxScroll);
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnime = false;
                isAuto = false;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                startAnime(value);
                scrollView.smoothScrollTo(0,value);
            }
        });
        animator.start();
    }

    private void autoCloseAnime(){
        isAuto = true;
        isAnime = true;
        ValueAnimator animator = ValueAnimator.ofInt( -currentScrollY , 0 );
        animator.setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAuto = false;
                isAnime = false;
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                println("autoCloseAnime ：" + value);
                startAnime( -value);
                scrollView.smoothScrollTo(0, -value);
            }
        });
        animator.start();
    }




    private void startAnime(int newy){
        //只计算newy
        //第一步渐隐searchbar
        changeSearchBarAlpha(newy);
        //第二部吧bar往上提一些
        changeBarMargginTop(newy);
        //第三部吧搜索图片放到右侧去
//        changeSearchIv(newy);
        //第四步吧分类图标和tabbar左移
        changeTabbar(newy);
        //重置波浪线
        wave.setAnime(newy / maxScroll);
    }

    private void changeTabbar(int newy) {
        float ratio =  newy / maxScroll;
        if (ratio <= 0) {
            ratio = 0;
        } else if (ratio >= 1) {
            ratio = 1;
        }
        float x = leftOffset * ratio;
        float marggin = barMargginRight + x;
        float marggin1 = classMargginRight + x;


        float currentX = ratio * offsetIVEndX;
        float currentY = ratio * offsetIVEndY;
        searchIv.setTranslationX(currentX);
        searchIv.setTranslationY(currentY);


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tabLayout.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) classIV.getLayoutParams();

        params.rightMargin = (int) marggin;
        params2.rightMargin = (int) marggin1;

        tabLayout.setLayoutParams(params);
        classIV.setLayoutParams(params2);

    }

    /*private void changeSearchIv(int newy) {
        float ratio =  newy / maxScroll;
        if (ratio <= 0) {
            ratio = 0;
        } else if (ratio >= 1) {
            ratio = 1;
        }

    }*/

    private void changeSearchBarAlpha(float y){
        if (y <= 0) {
            //全显示
            searchBar.setAlpha(1);
        }else if (y>=maxScroll){
            //消失
            searchBar.setAlpha(0);
        }else{
            //渐变
            float alpha = y / maxScroll;
            if (alpha >= 1) {
                alpha = 1;
            }
            searchBar.setAlpha(1 - alpha - 0.1f);
        }
    }


    void changeBarMargginTop(float y){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) titleBar.getLayoutParams();
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) wave.getLayoutParams();
        if (y <= 0) {
            //50dp
            params.topMargin = (int) maxScroll;
            params2.topMargin = (int) scroll110;
        }else if (y>=maxScroll){
            //0dp
            params.topMargin = 0;
            params2.topMargin = titleBar.getHeight();
        }else{
            //渐变
            params.topMargin = (int) (maxScroll - y);
            params2.topMargin = (int) (scroll110 - y);
        }
        titleBar.setLayoutParams(params);
        wave.setLayoutParams(params2);
    }



    public static void println(String str){
        Log.e("MAIN", str);
    }



    /**
     * dp转px
     * @param dp
     * @return
     */
    public static int dip2px(Context context , float dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5);
    }

    /** px转换dip */
    public static int px2dip(Context  context  , float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }



}
