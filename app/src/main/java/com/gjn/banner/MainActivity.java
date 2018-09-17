package com.gjn.banner;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gjn.bannerlibrary.Banner;
import com.gjn.bannerlibrary.LoopViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int type = 0;
    private Banner banner;
    private List<String> list;
    private List<String> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner = findViewById(R.id.banner);

        list = new ArrayList<>();
        list.add("http://ww1.sinaimg.cn/large/0065oQSqly1fryyn63fm1j30sg0yagt2.jpg");
        list.add("http://ww1.sinaimg.cn/large/0065oQSqly1frv03m8ky5j30iz0rltfp.jpg");
        list.add("http://ww1.sinaimg.cn/large/0065oQSqly1frslibvijrj30k80q678q.jpg");
        list.add("http://ww1.sinaimg.cn/large/0065oQSqly1frsllc19gfj30k80tfah5.jpg");
        list.add("http://ww1.sinaimg.cn/large/0065oQSqly1frrifts8l5j30j60ojq6u.jpg");
        list.add("http://ww1.sinaimg.cn/large/0065oQSqly1frjd77dt8zj30k80q2aga.jpg");

        list2 = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            list2.add("页面" + i);
        }

        banner.setImageLoader(new Banner.BannerImageLoader() {
            @Override
            public void imageLoader(Context context, Object img, ImageView imageView) {
                Glide.with(context).load(img).into(imageView);
            }
        }).setOnItemClickListener(new LoopViewPager.onClickListener() {
            @Override
            public void onClick(View view, int position, Object item) {
                Toast.makeText(MainActivity.this, "点击 " + position, Toast.LENGTH_SHORT).show();
            }
        }).setIndicatorMandatory(false)
                .updataView(list, list2);

        //修改imageview ScaleType
        banner.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //停止自动轮播
        banner.stop();
        //设置PageClip
        banner.setPageClip(true);
        //修改item
        banner.changeItemView(R.layout.itemview, new LoopViewPager.ChangeItemView() {
            @Override
            public void bindItemView(Context context, View view, Object img) {
                ImageView imageView = view.findViewById(R.id.iv_itemview);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(context).load(img).into(imageView);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type++;
                if (type > 3) {
                    type = 0;
                }
                switch (type) {
                    case 0:
                        banner.setIndicatorLoader(Banner.defaultImgIndicator(R.drawable.select));
                        banner.setIndicatorType(0);
                        break;
                    case 1:
                        banner.setIndicatorLoader(Banner.defaultNumIndicator(0, Color.WHITE));
                        banner.setIndicatorType(1);
                        break;
                    case 2:
                        banner.setIndicatorLoader(Banner.defaultTextIndicator(R.drawable.bg, Color.WHITE));
                        banner.setIndicatorType(2);
                        break;
                    case 3:
                        banner.setIndicatorLoader(Banner.defaultImgIndicator(R.mipmap.homepage_banner_point02, R.mipmap.homepage_banner_point01));
                        banner.setIndicatorType(0);
                        break;
                    default:
                }
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() > 1) {
                    list.remove(list.size() - 1);
                    list2.remove(list2.size() - 1);
                    banner.updataView(list, list2);
                }
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.add("http://ww1.sinaimg.cn/large/0065oQSqly1frjd77dt8zj30k80q2aga.jpg");
                list2.add("页面" + list2.size());
                banner.updataView(list, list2);
            }
        });
    }
}
