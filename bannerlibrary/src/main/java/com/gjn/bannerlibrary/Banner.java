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

import java.util.ArrayList;
import java.util.List;

/**
 * @author gjn
 * @time 2018/6/1 11:39
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final String TAG = "Banner";
    private static final int MARGIN = 50;
    private static final int PAGE_MARGIN = 20;

    private FrameLayout mFrameLayout;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLinearLayout;
    private LoopViewPager mLoopViewPager;
    private Indicator mIndicator;
    private boolean isMandatory = true;
    private List mImgItems;
    private List<String> mStringItems;
    private int mType = Indicator.TYPE_NUM;
    private int mLimit = 3;

    private int delayTime = 3000;
    private boolean isLoop = true;
    private LoopViewPager.onClickListener onClickListener;
    private LoopViewPager.onLongClickListener onLongClickListener;
    private boolean isShowIndicator = true;
    private BannerImageLoader imageLoader;
    private BannerPointIndicatorLoader indicatorLoader;
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
        mFrameLayout = (FrameLayout) mViewPager.getParent();
    }

    public static BannerPointIndicatorLoader defaultImgIndicator(final int bgResid) {
        return new BannerPointIndicatorLoader() {
            @Override
            public int getNormol() {
                return 0;
            }

            @Override
            public int getSelect() {
                return 0;
            }

            @Override
            public View createView(Context context, ViewGroup viewGroup) {
                ImageView imageView = new ImageView(context);
                if (bgResid != 0) {
                    imageView.setBackgroundResource(bgResid);
                }
                return imageView;
            }

            @Override
            public View getPointView(View view, int i) {
                return view;
            }

            @Override
            public int getType() {
                return Indicator.TYPE_POINT;
            }
        };
    }

    public static BannerPointIndicatorLoader defaultImgIndicator(final int bgNormalResid, final int bgSelectResid) {
        return new BannerPointIndicatorLoader() {
            @Override
            public int getNormol() {
                return bgNormalResid;
            }

            @Override
            public int getSelect() {
                return bgSelectResid;
            }

            @Override
            public View createView(Context context, ViewGroup viewGroup) {
                ImageView imageView = new ImageView(context);
                if (bgNormalResid != 0) {
                    imageView.setBackgroundResource(bgNormalResid);
                }
                return imageView;
            }

            @Override
            public View getPointView(View view, int i) {
                return view;
            }

            @Override
            public int getType() {
                return Indicator.TYPE_POINT;
            }
        };
    }

    public static BannerPointIndicatorLoader defaultNumIndicator(final int bgResid, final int colorResid) {
        return new BannerPointIndicatorLoader() {
            @Override
            public int getNormol() {
                return 0;
            }

            @Override
            public int getSelect() {
                return 0;
            }

            @Override
            public View createView(Context context, ViewGroup viewGroup) {
                TextView textView = new TextView(context);
                if (colorResid != 0) {
                    textView.setTextColor(colorResid);
                }
                if (bgResid != 0) {
                    textView.setBackgroundResource(bgResid);
                }
                return textView;
            }

            @Override
            public View getPointView(View view, int i) {
                return view;
            }

            @Override
            public int getType() {
                return Indicator.TYPE_NUM;
            }
        };
    }

    public static BannerPointIndicatorLoader defaultTextIndicator(final int bgResid, final int colorResid) {
        return new BannerPointIndicatorLoader() {
            @Override
            public int getNormol() {
                return 0;
            }

            @Override
            public int getSelect() {
                return 0;
            }

            @Override
            public View createView(Context context, ViewGroup viewGroup) {
                TextView textView = new TextView(context);
                if (colorResid != 0) {
                    textView.setTextColor(colorResid);
                }
                if (bgResid != 0) {
                    textView.setBackgroundResource(bgResid);
                }
                return textView;
            }

            @Override
            public View getPointView(View view, int i) {
                return view;
            }

            @Override
            public int getType() {
                return Indicator.TYPE_TEXT;
            }
        };
    }

    private void create() {
        if (mImgItems == null || mImgItems.size() <= 0) {
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
        if (indicatorLoader == null) {
            indicatorLoader = defaultNumIndicator(0, 0);
        }
        if (mIndicator == null) {
            loadIndicatorLoader();
        }
        mIndicator.setTitles(mStringItems);
        mIndicator.setMandatory(isMandatory);
        mIndicator.changeType(mType);
    }

    private void loadIndicatorLoader() {
        if (indicatorLoader.getType() == Indicator.TYPE_NUM) {
            mType = Indicator.TYPE_NUM;
        } else if (indicatorLoader.getType() == Indicator.TYPE_TEXT) {
            if (mStringItems.size() != mImgItems.size()) {
                Log.w(TAG, "size is error. type change TYPE_NUM");
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
        mIndicator.setImgState(indicatorLoader.getNormol(), indicatorLoader.getSelect());
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
                .setOffscreenPageLimit(mLimit)
                .setLoop(isLoop)
                .setOnPageChangeListener(this)
                .setOnClickListener(onClickListener)
                .setOnLongClickListener(onLongClickListener)
                .create();
    }

    public Banner setIndicatorType(int type) {
        this.mType = type;
        if (mIndicator != null) {
            mIndicator.setImgState(indicatorLoader.getNormol(), indicatorLoader.getSelect());
            mIndicator.changeType(type, mStringItems);
            mIndicator.selectIndicator(select);
        }
        return this;
    }

    public Banner setIndicatorMandatory(boolean mandatory) {
        isMandatory = mandatory;
        if (mIndicator != null) {
            mIndicator.setMandatory(mandatory);
            mIndicator.updataView();
        }
        return this;
    }

    public void setIndicatorImgState(int normalImg, int selectImg) {
        if (mIndicator != null) {
            mIndicator.setImgState(normalImg, selectImg);
            mIndicator.updataView();
        }
    }

    public void setIndicatorGravity(int gravity) {
        if (mIndicator != null) {
            mIndicator.setGravity(gravity);
            mIndicator.updataView();
        }
    }

    public Banner setIndicatorStringItems(List<String> strings) {
        mStringItems = strings == null ? new ArrayList<String>() : strings;
        if (mIndicator != null) {
            mIndicator.setTitles(strings);
        }
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

    public List getItems() {
        return mImgItems;
    }

    public int getItemCount() {
        return mImgItems.size();
    }

    public Object getItem(int i) {
        return mImgItems.get(i);
    }

    public LinearLayout getIndicatorLinearLayout() {
        return mIndicatorLinearLayout;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public LoopViewPager getLoopViewPager() {
        return mLoopViewPager;
    }

    public FrameLayout getFrameLayout() {
        return mFrameLayout;
    }

    public Banner setImageLoader(BannerImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public Banner setIndicatorLoader(BannerPointIndicatorLoader indicatorLoader) {
        this.indicatorLoader = indicatorLoader;
        return this;
    }

    public void start() {
        if (!isLoop) {
            setLoop(true);
        }
        if (mLoopViewPager != null) {
            mLoopViewPager.startLoop();
        }
    }

    public void stop() {
        if (isLoop) {
            setLoop(false);
        }
        if (mLoopViewPager != null) {
            mLoopViewPager.stopLoop();
        }
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

    public Banner setImgItems(List imgs) {
        mImgItems = imgs == null ? new ArrayList() : imgs;
        if (mLoopViewPager != null) {
            mLoopViewPager.updataView(imgs);
        }
        return this;
    }

    public Banner setViewPagerLimit(int limit) {
        this.mLimit = limit;
        if (mLoopViewPager != null) {
            mLoopViewPager.setOffscreenPageLimit(limit);
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

    public void setScaleType(ImageView.ScaleType type) {
        if (mLoopViewPager != null) {
            mLoopViewPager.setScaleType(type);
            mLoopViewPager.updataView();
        }
    }

    public void setPageClip(boolean clip) {
        setPageClip(clip, PAGE_MARGIN, MARGIN);
    }

    public void setPageClip(boolean clip, int pageMargin, int margin) {
        mFrameLayout.setClipChildren(!clip);
        mViewPager.setClipChildren(!clip);
        int width = (int) (getResources().getDisplayMetrics().density * margin);
        FrameLayout.LayoutParams params = (LayoutParams) mViewPager.getLayoutParams();
        if (clip) {
            mViewPager.setPageMargin(pageMargin);
            params.setMargins(width, 0, width, 0);
        } else {
            mViewPager.setPageMargin(0);
            params.setMargins(0, 0, 0, 0);
        }
    }

    public void changeItemView(int itemViewId, LoopViewPager.ChangeItemView changeItemView) {
        if (mLoopViewPager != null) {
            mLoopViewPager.setChangeItemView(itemViewId, changeItemView);
            mLoopViewPager.updataView();
        }
    }

    public void updataView() {
        updataView(mImgItems, mStringItems);
    }

    public void updataView(List imgItems) {
        updataView(imgItems, mStringItems);
    }

    public void updataView(List imgItems, List<String> stringItems) {
        setImgItems(imgItems);
        setIndicatorStringItems(stringItems);
        select = 0;
        if (mLoopViewPager == null || mIndicator == null) {
            Log.d(TAG, "new create");
            create();
        } else {
            if (mImgItems == null || mImgItems.size() == 0) {
                Log.e(TAG, "items is null.");
                return;
            }
            mLoopViewPager.updataView(imgItems);
            mIndicator.setImgState(indicatorLoader.getNormol(), indicatorLoader.getSelect());
            mIndicator.updataView(imgItems.size(), stringItems);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mLoopViewPager != null && mLoopViewPager.isLoop() && mImgItems.size() > 1) {
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

    public interface BannerPointIndicatorLoader {
        View createView(Context context, ViewGroup viewGroup);

        View getPointView(View view, int i);

        int getType();

        int getNormol();

        int getSelect();
    }
}
