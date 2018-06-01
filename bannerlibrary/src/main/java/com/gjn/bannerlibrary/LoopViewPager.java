package com.gjn.bannerlibrary;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjn on 2018/6/1.
 */

public abstract class LoopViewPager implements ViewPager.OnPageChangeListener {
    private static final String TAG = "LoopViewPager";
    private Context context;
    private ViewPager viewPager;
    private List items;
    private int itemCount = 0;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    private List<ImageView> itemViews = new ArrayList<>();
    private int itemViewCount = 0;
    //监听相关
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private onClickListener onClickListener;
    private onLongClickListener onLongClickListener;
    //循环相关
    private int delayTime = 3000;
    private boolean isLoop = true;
    private Handler loopHandler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (itemViewCount > 1 && isLoop) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                loopHandler.postDelayed(task, delayTime);
            }
        }
    };

    public LoopViewPager(Context context, ViewPager viewPager, List items) {
        this.context = context;
        this.viewPager = viewPager;
        this.items = items;
    }

    private void createViews() {
        itemViews.clear();
        //大于1需要设置头尾循环
        if (itemCount > 1) {
            for (int i = 0; i < itemCount + 2; i++) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(scaleType);
                Object img;
                if (i == 0) {
                    img = items.get(itemCount - 1);
                }else if(i == itemCount + 1){
                    img = items.get(0);
                }else{
                    img = items.get( i - 1);
                }
                ImageLoader(context,img,imageView);
                itemViews.add(imageView);
            }
        }else {
            for (int i = 0; i < itemCount; i++) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(scaleType);
                Object img = items.get(i);
                ImageLoader(context,img,imageView);
                itemViews.add(imageView);
            }
        }
        itemViewCount = itemViews.size();
    }

    public void updataView(){
        updataView(items);
    }

    public void updataView(List items) {
        updataView(items,viewPager);
    }

    public void updataView(ViewPager viewPager){
        updataView(items,viewPager);
    }

    public void updataView(List items, ViewPager viewPager){
        this.items = items;
        this.viewPager = viewPager;
        create();
    }

    public void create(){
        if (items == null) {
            Log.e(TAG, "items is null.");
            return;
        }
        if (viewPager == null) {
            Log.e(TAG, "viewPager is null.");
            return;
        }
        itemCount = items.size();
        //创建头尾view
        createViews();
        //设置适配器
        bannerAdaper adaper = new bannerAdaper(itemViews);
        viewPager.setAdapter(adaper);
        viewPager.addOnPageChangeListener(this);

        if (itemViewCount > 1) {
            viewPager.setCurrentItem(1,false);
            if (isLoop){
                startLoop();
            }
        }
    }

    public void startLoop() {
        loopHandler.removeCallbacks(task);
        loopHandler.postDelayed(task, delayTime);
    }

    public void stopLoop(){
        loopHandler.removeCallbacks(task);
    }

    public int getItemViewCount() {
        return itemViewCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public LoopViewPager setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public LoopViewPager setDelayTime(int mDelayTime) {
        this.delayTime = mDelayTime;
        return this;
    }

    public LoopViewPager setLoop(boolean loop) {
        isLoop = loop;
        return this;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public LoopViewPager setOnClickListener(LoopViewPager.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public LoopViewPager setOnLongClickListener(LoopViewPager.onLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
        return this;
    }

    public LoopViewPager setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        return this;
    }

    public abstract void ImageLoader(Context context, Object img, ImageView imageView);

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (getItemCount() != getItemViewCount()){
            //当要到头尾之前先设置指示点到相应的头尾
            if (position == 0 ) {
                position = getItemCount() - 1;
            }else if(position == (getItemViewCount() - 1)){
                position = 0;
            }else{
                position--;
            }
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
        int cur = viewPager.getCurrentItem();
        int count = getItemViewCount();
        if (state != ViewPager.SCROLL_STATE_IDLE) return;
        //当滑动到第一个的时候跳转到倒数第二个
        //当滑动到最后一个的时候跳转到第二个
        if (cur == 0) {
            viewPager.setCurrentItem(count - 2,false);
        } else if (cur == count - 1) {
            viewPager.setCurrentItem(1,false);
        }
    }

    public interface onClickListener{
        void onClick(View view, int position);
    }

    public interface onLongClickListener{
        boolean onLongClick(View view, int position);
    }

    private class bannerAdaper extends PagerAdapter{
        private List<ImageView> views;

        public bannerAdaper(List<ImageView> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views == null? 0 : views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = views.get(position);
            //点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        if (itemViewCount > 1){
                            onClickListener.onClick(v,position - 1);
                        }else{
                            onClickListener.onClick(v,position);
                        }
                    }
                }
            });
            //长按事件
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onLongClickListener != null) {
                        if (itemViewCount > 1){
                            return onLongClickListener.onLongClick(v,position - 1);
                        }else{
                            return onLongClickListener.onLongClick(v,position);
                        }
                    }
                    return false;
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }
}
