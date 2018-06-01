package com.gjn.bannerlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gjn.indicatorlibrary.Indicator;

import java.util.List;

/**
 * Created by gjn on 2018/6/1.
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Banner";
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLinearLayout;
    private LoopViewPager mLoopViewPager;
    private Indicator mIndicator;

    private List mImgItems;
    private List<String> mStringItems;
    private int delayTime = 3000;
    private boolean isLoop = true;
    private LoopViewPager.onClickListener onClickListener;
    private LoopViewPager.onLongClickListener onLongClickListener;
    private boolean isShowIndicator = true;
    private BannerImageLoader imageLoader;

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.banner, this, true);
        mViewPager = view.findViewById(R.id.vp_banner);
        mIndicatorLinearLayout = view.findViewById(R.id.ll_indicator_banner);
    }

    private void create(){
        if (mStringItems != null && mImgItems != null) {
            if (mStringItems.size() != mImgItems.size()) {
                Log.e(TAG, "size is error. StringItems size = " + mStringItems.size() + ", ImgItems size = " + mImgItems.size());
                return;
            }
        }
        if (mImgItems == null && mImgItems.size() <= 0) {
            Log.e(TAG, "items is null.");
            return;
        }
        if (imageLoader == null) {
            Log.e(TAG, "imageLoader is null.");
            return;
        }
        createIndicator();
        createView();
        setShowIndicator(isShowIndicator);
    }

    private void createIndicator() {
        if (mStringItems == null) {
            mIndicator = null;
        }
        if (mIndicator == null) {
            if (mStringItems != null) {
                mIndicator = new Indicator(getContext(), mStringItems, mIndicatorLinearLayout) {

                    @Override
                    protected View createView(Context context, ViewGroup viewGroup) {
                        return new TextView(context);
                    }

                    @Override
                    protected View getPointView(View view) {
                        return view;
                    }
                };
            }else {
                mIndicator = new Indicator(getContext(), mImgItems.size(), mIndicatorLinearLayout) {
                    @Override
                    protected View createView(Context context, ViewGroup viewGroup) {
                        return new TextView(context);
                    }

                    @Override
                    protected View getPointView(View view) {
                        return view;
                    }
                };
                mIndicator.setType(Indicator.TYPE_NUM);
            }
            mIndicator.create();
        }else {
            mIndicator.updataView(mImgItems.size());
        }
    }

    private void createView() {
        if (mLoopViewPager == null) {
            mLoopViewPager = new LoopViewPager(getContext(), mViewPager, mImgItems) {
                @Override
                public void ImageLoader(Context context, Object img, ImageView imageView) {
                    imageLoader.imageLoader(context, img, imageView);
                }
            };
            mLoopViewPager.setDelayTime(delayTime)
                    .setLoop(isLoop)
                    .setOnPageChangeListener(this)
                    .setOnClickListener(onClickListener)
                    .setOnLongClickListener(onLongClickListener)
                    .create();
        }else {
            mLoopViewPager.updataView(mImgItems);
        }
    }

    public Banner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public Banner setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    public Banner setShowIndicator(boolean showIndicator) {
        isShowIndicator = showIndicator;
        if (isShowIndicator) {
            mIndicatorLinearLayout.setVisibility(VISIBLE);
        }else {
            mIndicatorLinearLayout.setVisibility(GONE);
        }
        return this;
    }

    public Banner setImageLoader(BannerImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public boolean isShowIndicator() {
        return isShowIndicator;
    }

    public Banner setOnClickListener(LoopViewPager.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public Banner setOnLongClickListener(LoopViewPager.onLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
        return this;
    }

    public void updataView(){
        updataView(mImgItems, mStringItems);
    }

    public void updataView(List imgItems){
        if (imgItems.size() != mStringItems.size()) {
            updataView(imgItems, null);
        }else {
            updataView(imgItems, mStringItems);
        }
    }

    public void updataView(List imgItems, List<String> stringItems){
        this.mImgItems = imgItems;
        this.mStringItems = stringItems;
        create();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mLoopViewPager.isLoop() && mImgItems.size() > 1) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                mLoopViewPager.startLoop();
            } else if (action == MotionEvent.ACTION_DOWN) {
                mLoopViewPager.stopLoop();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mIndicator != null) {
            mIndicator.selectIndicator(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface BannerImageLoader{
        void imageLoader(Context context, Object img, ImageView imageView);
    }
}
