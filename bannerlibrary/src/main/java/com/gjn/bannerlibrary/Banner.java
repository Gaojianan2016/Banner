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
    private boolean isMandatory = true;

    private List mImgItems;
    private List<String> mStringItems;
    private int mType = Indicator.TYPE_NUM;

    private int delayTime = 3000;
    private boolean isLoop = true;
    private LoopViewPager.onClickListener onClickListener;
    private LoopViewPager.onLongClickListener onLongClickListener;
    private boolean isShowIndicator = true;
    private BannerImageLoader imageLoader;
    private BannerIndicatorLoader indicatorLoader;
    private int select = 0;

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

    private void create() {
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
        if (indicatorLoader != null) {
            loadIndicatorLoader();
        }
        if (mIndicator == null) {
            mIndicator = new Indicator(getContext(), mImgItems.size(), mIndicatorLinearLayout) {
                @Override
                protected View createView(Context context, ViewGroup viewGroup) {
                    return new TextView(context);
                }

                @Override
                protected View getPointView(View view, int i) {
                    return view;
                }
            };
        }
        mIndicator.setTitles(mStringItems);
        mIndicator.setMandatory(isMandatory);
        mIndicator.changeType(mType);
    }

    private void loadIndicatorLoader() {
        if (mIndicator == null) {
            if (indicatorLoader.getType() == Indicator.TYPE_NUM) {
                mType = Indicator.TYPE_NUM;
            } else if (indicatorLoader.getType() == Indicator.TYPE_TEXT) {
                if (mStringItems.size() != mImgItems.size()) {
                    Log.e(TAG, "size is error. type change TYPE_NUM");
                    mType = Indicator.TYPE_NUM;
                } else {
                    mType = Indicator.TYPE_TEXT;
                }
            } else {
                mType = Indicator.TYPE_POINT;
            }
            mIndicator = new Indicator(getContext(), mImgItems.size(), mIndicatorLinearLayout) {
                @Override
                protected View createView(Context context, ViewGroup viewGroup) {
                    return indicatorLoader.createView(context, viewGroup);
                }

                @Override
                protected View getPointView(View view, int i) {
                    return indicatorLoader.getPointView(view, i);
                }
            };
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
        }
        mLoopViewPager.setDelayTime(delayTime)
                .setLoop(isLoop)
                .setOnPageChangeListener(this)
                .setOnClickListener(onClickListener)
                .setOnLongClickListener(onLongClickListener)
                .create();
    }

    public void start() {
        if (!isLoop) {
            setLoop(true);
        }
        mLoopViewPager.startLoop();
    }

    public void stoop() {
        if (isLoop) {
            setLoop(false);
        }
        mLoopViewPager.stopLoop();
    }

    public Banner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        if (mLoopViewPager != null) {
            mLoopViewPager.setDelayTime(delayTime);
        }
        return this;
    }

    public Banner setLoop(boolean loop) {
        isLoop = loop;
        if (mLoopViewPager != null) {
            mLoopViewPager.setLoop(loop);
        }
        return this;
    }

    public Banner setType(int type) {
        this.mType = type;
        if (mIndicator != null) {
            mIndicator.changeType(type, mStringItems);
            mIndicator.selectIndicator(select);
        }
        return this;
    }

    public Banner setIndicatorMandatory(boolean b) {
        isMandatory = b;
        if (mIndicator != null) {
            mIndicator.setMandatory(b);
            mIndicator.updataView();
        }
        return this;
    }

    public void setScaleType(ImageView.ScaleType type) {
        if (mLoopViewPager != null) {
            mLoopViewPager.setScaleType(type);
            mLoopViewPager.updataView();
        }
    }

    public Banner setImageLoader(BannerImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public void setGravity(int gravity) {
        if (mIndicator != null) {
            mIndicator.setGravity(gravity);
            mIndicator.updataView();
        }
    }

    public Banner setIndicatorLoader(BannerIndicatorLoader indicatorLoader) {
        this.indicatorLoader = indicatorLoader;
        return this;
    }

    public Banner setImgItems(List imgs) {
        mImgItems = imgs;
        if (mLoopViewPager != null) {
            mLoopViewPager.updataView(imgs);
        }
        return this;
    }

    public List getItems(){
        return mImgItems;
    }

    public Object getItem(int i){
        return mImgItems.get(i);
    }

    public Banner setStringItems(List<String> strings) {
        mStringItems = strings;
        if (mIndicator != null) {
            mIndicator.setTitles(strings);
        }
        return this;
    }

    public Banner setIndicator(Indicator indicator) {
        mIndicator = indicator;
        return this;
    }

    public Banner setLoopViewPager(LoopViewPager loopViewPager) {
        mLoopViewPager = loopViewPager;
        return this;
    }

    public boolean isShowIndicator() {
        return isShowIndicator;
    }

    public Banner setShowIndicator(boolean showIndicator) {
        isShowIndicator = showIndicator;
        if (isShowIndicator) {
            mIndicatorLinearLayout.setVisibility(VISIBLE);
        } else {
            mIndicatorLinearLayout.setVisibility(GONE);
        }
        return this;
    }

    public Banner setOnItemClickListener(LoopViewPager.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
        if (mLoopViewPager != null) {
            mLoopViewPager.setOnClickListener(onClickListener);
        }
        return this;
    }

    public Banner setOnItemLongClickListener(LoopViewPager.onLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
        if (mLoopViewPager != null) {
            mLoopViewPager.setOnLongClickListener(onLongClickListener);
        }
        return this;
    }

    public void updataView() {
        updataView(mImgItems, mStringItems);
    }

    public void updataView(List imgItems) {
        updataView(imgItems, mStringItems);
    }

    public void updataView(List imgItems, List<String> stringItems) {
        this.mImgItems = imgItems;
        this.mStringItems = stringItems;
        select = 0;
        if (mLoopViewPager == null || mIndicator == null) {
            Log.d(TAG, "new create");
            create();
        } else {
            if (mImgItems == null && mImgItems.size() <= 0) {
                Log.e(TAG, "items is null.");
                return;
            }
            mLoopViewPager.updataView(imgItems);
            mIndicator.updataView(imgItems.size(), stringItems);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mLoopViewPager != null) {
            if (mLoopViewPager.isLoop() && mImgItems.size() > 1) {
                int action = ev.getAction();
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                        || action == MotionEvent.ACTION_OUTSIDE) {
                    mLoopViewPager.startLoop();
                } else if (action == MotionEvent.ACTION_DOWN) {
                    mLoopViewPager.stopLoop();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        select = position;
        if (mIndicator != null) {
            mIndicator.selectIndicator(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface BannerImageLoader {
        void imageLoader(Context context, Object img, ImageView imageView);
    }

    public interface BannerIndicatorLoader {
        View createView(Context context, ViewGroup viewGroup);

        View getPointView(View view, int i);

        int getType();
    }
}
