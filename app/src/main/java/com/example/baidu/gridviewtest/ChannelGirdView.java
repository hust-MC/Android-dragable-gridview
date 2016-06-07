package com.example.baidu.gridviewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baidu on 16/6/5.
 */
public class ChannelGirdView extends FrameLayout {
    Context mContext;
    GridView mChannelWallMine, mChannelWallHot;

    List<ChannelWallItem> mChannelWallMineItems = new ArrayList<>();
    List<ChannelWallItem> mChannelWallHotItems = new ArrayList<>();

    private ChannelMineAdapter mChannelMineAdatper;
    private ChannelHotAdapter mChannelHotAdapter;
    private boolean mDelStatusFlag, mCurrentClickIsMine, mDoDelete;
    private int mItemHeight, mItemWidth;
    private int mCurrentClickIndex;

    public ChannelGirdView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ChannelGirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View inflater = LayoutInflater.from(mContext).inflate(R.layout.channel_gridview, this);

        mChannelMineAdatper = new ChannelMineAdapter(mContext, mChannelWallMineItems);
        mChannelHotAdapter = new ChannelHotAdapter(mContext, mChannelWallHotItems);

        mChannelWallMine = (GridView) inflater.findViewById(R.id.channel_wall_mine);
        mChannelWallHot = (GridView) inflater.findViewById(R.id.channel_wall_hot);
        mChannelWallMine.setAdapter(mChannelMineAdatper);
        mChannelWallHot.setAdapter(mChannelHotAdapter);


        OnChannelItemLongClick itemLongClickListener = new OnChannelItemLongClick();
        mChannelWallMine.setOnItemLongClickListener(itemLongClickListener);
        mChannelWallHot.setOnItemLongClickListener(itemLongClickListener);

        OnChannelItemClick itemClickListener = new OnChannelItemClick();
        mChannelWallMine.setOnItemClickListener(itemClickListener);
        mChannelWallHot.setOnItemClickListener(itemClickListener);
    }

    /**
     * 获取gridview中item直接的横向中心距离
     *
     * @return
     */
    private int getItemVerticalDistance() {
        return mChannelWallMine.getVerticalSpacing() + mItemWidth;
    }

    /**
     * 获取gridview中item直接的纵向中心距离
     *
     * @return
     */
    private int getItemHorizontalDistance() {
        return mChannelWallMine.getHorizontalSpacing() + mItemHeight;
    }

    /**
     * 刷新列表
     */
    private void refresh() {
        mChannelMineAdatper.notifyDataSetChanged();
        mChannelHotAdapter.notifyDataSetChanged();
    }

    /**
     * 更新数据并刷新列表
     */
    private void notifyAllData() {
        mChannelMineAdatper.setData(mChannelWallMineItems);
        mChannelHotAdapter.setData(mChannelWallHotItems);
        refresh();
    }

    public void setData(List<ChannelWallItem> channelMine, List<ChannelWallItem> channelHot) {
        mChannelWallMineItems = channelMine;
        mChannelWallHotItems = channelHot;
        notifyAllData();
    }

    class OnChannelItemLongClick implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mDelStatusFlag = !mDelStatusFlag;
            refresh();
            return true;
        }
    }

    class OnChannelItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(final AdapterView<?> parent, View view, final int position, long
                id) {
            if (!mDelStatusFlag) {
                return;
            }
            mDoDelete = true;
            mCurrentClickIndex = position;                  //设置当前点击的位置
            if (parent.getId() == R.id.channel_wall_mine) {
                mChannelWallHotItems.add(mChannelWallMineItems.get(position));
                mChannelWallMineItems.remove(position);
                mCurrentClickIsMine = true;
            } else {
                mChannelWallMineItems.add(mChannelWallHotItems.get(position));
                mChannelWallHotItems.remove(position);
                mCurrentClickIsMine = false;
            }
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_del);
            view.setAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentClickIsMine && position != mChannelWallMineItems.size()) {
                        TranslateAnimation translateAnimation = new TranslateAnimation(0,
                                0 - getItemVerticalDistance(), 0, 0);
                        translateAnimation.setDuration(1000);
                    }
                    notifyAllData();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            animation.startNow();
        }
    }


    public class ChannelMineAdapter extends ChannelBaseAdapter {

        public ChannelMineAdapter(Context context, List<ChannelWallItem> mChannelList) {
            super(context, mChannelList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_icon_mine,
                        null);
                holder.tbDelOrAdd = (ImageView) convertView.findViewById(R.id.bt_del);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            mItemHeight = convertView.getMeasuredHeight();
            mItemWidth = convertView.getMeasuredWidth();

            if (mDoDelete) {
                if (!mCurrentClickIsMine && position == mChannelList.size() - 1) {
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_add);
                    convertView.setAnimation(animation);
                    animation.startNow();
                } else if (mCurrentClickIsMine && position >= mCurrentClickIndex) {
                    Animation animation;
                    /* 判断是不是需要上移一行 */
                    if ((position + 1) % 4 != 0) {
                        animation = new TranslateAnimation(getItemVerticalDistance(), 0, 0, 0);
                    } else {
                        animation = new TranslateAnimation(0 - 4 * getItemVerticalDistance(), 0,
                                getItemHorizontalDistance(), 0);
                    }
                    animation.setDuration(300);
                    convertView.setAnimation(animation);
                    animation.startNow();
                }
            }
            holder.tvName.setText(mChannelList.get(position).getItemText());
            holder.ivIcon.setImageResource(mChannelList.get(position).getItemImageRes());
            holder.tbDelOrAdd.setVisibility(mDelStatusFlag ? View.VISIBLE : View.GONE);

            return convertView;
        }
    }

    public class ChannelHotAdapter extends ChannelBaseAdapter {

        public ChannelHotAdapter(Context context, List<ChannelWallItem> mChannelList) {
            super(context, mChannelList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_icon_hot,
                        null);
                holder.tbDelOrAdd = (ImageView) convertView.findViewById(R.id.bt_add);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.icon);
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            mItemHeight = convertView.getMeasuredHeight();
            mItemWidth = convertView.getMeasuredWidth();

            if (mDoDelete) {
                if (mCurrentClickIsMine && position == mChannelList.size() - 1) {
                    Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_add);
                    convertView.setAnimation(animation);
                    animation.startNow();
                } else if (!mCurrentClickIsMine && position >= mCurrentClickIndex) {
                    Animation animation;
                    /* 判断是不是需要上移一行 */
                    if ((position + 1) % 4 != 0) {
                        animation = new TranslateAnimation(getItemVerticalDistance(), 0, 0, 0);
                    } else {
                        animation = new TranslateAnimation(0 - 4 * getItemVerticalDistance(), 0,
                                getItemHorizontalDistance(), 0);
                    }
                    animation.setDuration(300);
                    convertView.setAnimation(animation);
                    animation.startNow();
                }
            }

            holder.tvName.setText(mChannelList.get(position).getItemText());
            holder.ivIcon.setImageResource(mChannelList.get(position).getItemImageRes());
            holder.tbDelOrAdd.setVisibility(mDelStatusFlag ? View.VISIBLE : View.GONE);

            return convertView;
        }
    }
}
