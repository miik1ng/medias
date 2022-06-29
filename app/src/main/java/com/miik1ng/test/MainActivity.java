package com.miik1ng.test;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.miik1ng.medias.MediasLayout;
import com.miik1ng.medias.NewMedia;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MediasLayout mediasLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediasLayout = findViewById(R.id.ml_camera);

        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediasLayout.setType(MediasLayout.TYPE_ADD);

                List<NewMedia> list = new ArrayList<>();
                list.add(new NewMedia("https://zhjs.ysb.qz.gov.cn/iResources/img/jpg/home_banner1.png", "image/jpeg"));
                //list.add(new NewMedia("https://zhjs.ysb.qz.gov.cn/iResources/img/jpg/home_banner1.png", "video/mp4"));
                mediasLayout.setData(list);
                mediasLayout.setVideoMaxSecond(5);
                mediasLayout.addWaterMaskListener(new MediasLayout.WaterMaskListener() {
                    @Override
                    public String getWaterMask() {
                        return "水印";
                    }
                });
            }
        });
    }
}