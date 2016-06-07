package com.example.baidu.gridviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ChannelWallItem> mChannelWallMineItem = new ArrayList<>();
    List<ChannelWallItem> mChannelWallHotItem = new ArrayList<>();

    String mTitlesMine[] = {"电视剧", "电影", "综艺", "动漫", "搞笑", "娱乐", "美女", "专题", "电视剧", "电影", "综艺",
            "动漫", "搞笑", "娱乐", "美女", "专题"
            , "电视剧", "电影", "综艺", "动漫", "搞笑", "娱乐", "美女", "专题"
    };
    String mTitlesHot[] = {"自拍秀", "新闻", "体育", "卫视直播"};

    ChannelGirdView mChannelGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        initData();
    }

    private void initWidget() {
        mChannelGridView = (ChannelGirdView) findViewById(R.id.channel_girdview);
    }

    private void initData() {
        for (String aMTitlesMine : mTitlesMine) {
            ChannelWallItem channelWallItem = new ChannelWallItem();
            channelWallItem.setItemText(aMTitlesMine);
            channelWallItem.setItemImageRes(R.mipmap.ic_launcher);
            mChannelWallMineItem.add(channelWallItem);
        }
        for (String aMTitlesHot : mTitlesHot) {
            ChannelWallItem channelWallItem = new ChannelWallItem();
            channelWallItem.setItemText(aMTitlesHot);
            channelWallItem.setItemImageRes(R.mipmap.ic_launcher);
            mChannelWallHotItem.add(channelWallItem);
        }
        mChannelGridView.setData(mChannelWallMineItem, mChannelWallHotItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit: {
                if (mChannelGridView.isEditable()) {
                    mChannelGridView.setEditable(false);
                    item.setTitle("编辑");
                } else {
                    mChannelGridView.setEditable(true);
                    item.setTitle("完成");
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
