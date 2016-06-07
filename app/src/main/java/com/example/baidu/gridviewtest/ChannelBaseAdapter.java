package com.example.baidu.gridviewtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by baidu on 16/6/7.
 */
public class ChannelBaseAdapter extends BaseAdapter {

    List<ChannelWallItem> mChannelList;
    Context mContext;

    public ChannelBaseAdapter(Context context, List<ChannelWallItem> mChannelList) {
        this.mChannelList = mChannelList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mChannelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mChannelList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    class ViewHolder {
        TextView tvName;
        ImageView tbDelOrAdd;
        ImageView ivIcon;
    }

    public void setData(List<ChannelWallItem> channelList) {
        mChannelList = channelList;
    }
}
