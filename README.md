# Banner
自定义Banner

- 依赖使用
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}


dependencies {
    implementation 'com.github.Gaojianan2016:Banner:1.0.5'
    implementation 'com.github.Gaojianan2016:IndicatorLayout:1.0.7'
}
```

如果只要循环单独使用的话，就不需要IndicatorLayout

```
dependencies {
    implementation 'com.github.Gaojianan2016:Banner:1.0.0'
}
```

调用内部的LoopViewPager即可
```
//例如如下
mLoopViewPager = new LoopViewPager(getContext(), mViewPager, mImgItems) {
    @Override
    public void ImageLoader(Context context, Object img, ImageView imageView) {
        Glide.with(context).load(img).into(imageView);
    }
};

mLoopViewPager.setDelayTime(delayTime)
                .setLoop(isLoop)
                .setOnPageChangeListener(this)
                .setOnClickListener(onClickListener)
                .setOnLongClickListener(onLongClickListener)
                .create();
```
