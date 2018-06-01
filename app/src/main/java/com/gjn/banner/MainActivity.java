package com.gjn.banner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.gjn.bannerlibrary.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Banner banner = findViewById(R.id.banner);

        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher_background);
        list.add(R.drawable.ic_launcher_foreground);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.ic_launcher_background);
        list.add(R.drawable.ic_launcher_foreground);

        List<String> list2 = new ArrayList<>();
        list2.add("页面1");
        list2.add("页面2");
        list2.add("页面3");
        list2.add("页面4");
        list2.add("页面5");
        list2.add("页面6");

        banner.setImageLoader(new Banner.BannerImageLoader() {
            @Override
            public void imageLoader(Context context, Object img, ImageView imageView) {
                imageView.setImageResource((Integer) img);
            }
        }).updataView(list, list2);


        list.remove(3);
        banner.updataView(list);
    }
}
