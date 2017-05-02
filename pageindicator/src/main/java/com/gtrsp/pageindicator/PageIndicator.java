package com.gtrsp.pageindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * viewpage的指示器
 * Created by raoxuting on 2017/3/24.
 */

public class PageIndicator extends FrameLayout {

    private LinearLayout llIndicators;
    private ImageView indicator;
    private Context context;
    private int spacePoints;
    private int normalIndicator;//未选中指示器资源
    private int selectedIndicator;//选中指示器资源

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator);
        normalIndicator = a.getResourceId(R.styleable.PageIndicator_normal_indicator,
                R.drawable.shape_ring_white);
        selectedIndicator = a.getResourceId(R.styleable.PageIndicator_selected_indicator,
                R.drawable.shape_point_white);
        a.recycle();

        LayoutParams params = new LayoutParams
                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        llIndicators = new LinearLayout(context);
        llIndicators.setLayoutParams(params);
        llIndicators.setOrientation(LinearLayout.HORIZONTAL);

        indicator = new ImageView(context);
        LayoutParams params1 = new LayoutParams
                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        indicator.setLayoutParams(params1);
        indicator.setImageResource(selectedIndicator);//默认使用白色实心点

        this.addView(llIndicators);
        this.addView(indicator);


    }

    public void setIndicatorToViewpager(ViewPager viewPager) {
        setIndicatorToViewpager(viewPager,0);
    }

    public void setIndicatorToViewpager(ViewPager viewPager, int realPageCount) {
        int pagecount;
        if (realPageCount == 0) {
            pagecount = viewPager.getAdapter().getCount();
        }else {
            pagecount = realPageCount;
        }
        for (int i = 0; i < pagecount; i++) {
            ImageView backpoint = new ImageView(context);
            backpoint.setImageResource(normalIndicator);//默认使用白色圆环

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                layoutParams.leftMargin = dip2px(10);
            }
            backpoint.setLayoutParams(layoutParams);

            llIndicators.addView(backpoint);
        }

        indicator.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                indicator.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                spacePoints = llIndicators.getChildAt(1).getLeft() - llIndicators.getChildAt(0).getLeft();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                int leftMargin = (int) (spacePoints * positionOffset + spacePoints * position);
                LayoutParams layoutParams = (LayoutParams)
                        indicator.getLayoutParams();
                layoutParams.leftMargin = leftMargin;

                indicator.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 將 dip 或 dp 转换为 px, 保证尺寸大小不变
     * @param dipValue
     * @return
     */
    public int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


}