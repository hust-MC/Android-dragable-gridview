package com.example.baidu.gridviewtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by baidu on 16/6/7.
 */
public abstract class ChannelBaseAdapter extends BaseAdapter {

    protected final int CHANNEL_MINE = 0;
    protected final int CHANNEL_HOT = 1;

    protected ViewHolder mHolder;

    protected List<ChannelWallItem> mChannelList;
    private Context mContext;
    private View mConvertView;

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
    abstract public View getView(int position, View convertView, ViewGroup parent);

    abstract protected int getVerticalDistance();

    abstract protected int getHorizontalDistance();

    protected View initView(View convertView) {
        if (convertView == null) {
            mHolder = new ViewHolder();
            if (this instanceof ChannelGirdView.ChannelMineAdapter) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_icon_mine, null);
                mHolder.tbDelOrAdd = (ImageView) convertView.findViewById(R.id.bt_del);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_icon_hot, null);
                mHolder.tbDelOrAdd = (ImageView) convertView.findViewById(R.id.bt_add);
            }

            mHolder.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        return mConvertView = convertView;
    }

    protected void playAnimation(boolean currentClickIsMe, int position, int currentClickIndex) {
        if (ChannelGirdView.isDoDelete()) {
            if (!currentClickIsMe && position == mChannelList.size() - 1) {
                Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_add);
                mConvertView.setAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ChannelGirdView.setDoDelete(false);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                animation.startNow();
            } else if (currentClickIsMe && position >= currentClickIndex) {
                Animation animation;
                    /* 判断是不是需要上移一行 */
                if ((position + 1) % 4 != 0) {
                    animation = new TranslateAnimation(getVerticalDistance(), 0, 0, 0);
                } else {
                    animation = new TranslateAnimation(0 - 4 * getVerticalDistance(), 0,
                            getHorizontalDistance(), 0);
                }
                animation.setDuration(300);
                mConvertView.setAnimation(animation);
                animation.startNow();
            }
        }
    }

    protected void setItemContent(boolean delStatusFlag, int position) {
        mHolder.tvName.setText(mChannelList.get(position).getItemText());
        mHolder.ivIcon.setImageResource(mChannelList.get(position).getItemImageRes());
        mHolder.tbDelOrAdd.setVisibility(delStatusFlag ? View.VISIBLE : View.GONE);
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
