package com.gjn.banner;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gjn.bannerlibrary.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int type = 1;
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
        }).setIndicatorLoader(new Banner.BannerIndicatorLoader() {
            @Override
            public View createView(Context context, ViewGroup viewGroup) {
                if (type == 0) {
                    return LayoutInflater.from(context).inflate(R.layout.img, viewGroup, false);
                } else if (type == 1) {
                    return LayoutInflater.from(context).inflate(R.layout.text, viewGroup, false);
                } else {
                    return LayoutInflater.from(context).inflate(R.layout.layout, viewGroup, false);
                }
            }

            @Override
            public View getPointView(View view, int i) {
                if (type == 2) {
                    return view.findViewById(R.id.tv);
                }
                return view;
            }

            @Override
            public int getType() {
                return type;
            }
        }).updataView(list, list2);

        banner.setScaleType(ImageView.ScaleType.FIT_CENTER);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type++;
                if (type > 2) {
                    type = 0;
                }
                banner.setType(type);
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
