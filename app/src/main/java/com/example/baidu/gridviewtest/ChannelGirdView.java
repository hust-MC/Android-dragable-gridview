package com.example.baidu.gridviewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
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

    /**
     * 状态标志
     */
    private boolean mEditStatusFlag, mCurrentClickIsMine, mIsDraging;
    private static boolean mDoDelete;

    /**
     * 条目的尺寸
     */
    private int mItemHeight, mItemWidth;

    /**
     * 当前点击条目的序号
     */
    private int mCurrentClickIndex;

    /**
     * 拖拽坐标
     */
    private float mDown_x, mDown_y, mMove_x, mMove_y, mUp_x, mUp_y;

    /**
     * 当前正在拖拽的控件
     */
    private View mCurrentDragingView;


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

    public static boolean isDoDelete() {
        return mDoDelete;
    }

    public static void setDoDelete(boolean doDelete) {
        mDoDelete = doDelete;
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

        mChannelWallMine.setOnTouchListener(new OnChannelItemTouch());
    }

    /**
     * 获取gridview中item直接的横向中心距离
     *
     * @return
     */
    private int getItemVerticalDistance() {
        return mChannelWallMine.getVerticalSpacing() + mItemHeight;
    }

    /**
     * 获取gridview中item直接的纵向中心距离
     *
     * @return
     */
    private int getItemHorizontalDistance() {
        return mChannelWallMine.getHorizontalSpacing() + mItemWidth;
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

    private void setGridViewClickable(boolean clickable) {
        mChannelWallMine.setEnabled(clickable);
        mChannelWallHot.setEnabled(clickable);
    }

    public boolean isEditable() {
        return mEditStatusFlag;
    }

    public void setEditable(boolean editable) {
        mEditStatusFlag = editable;
        refresh();
    }

    class OnChannelItemTouch implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    mMove_x = mDown_x = event.getX();
                    mMove_y = mDown_y = event.getY();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (mIsDraging) {

                        Animation animation = new TranslateAnimation(mMove_x - mDown_x, event
                                .getX() - mDown_x,
                                mMove_y - mDown_y, event.getY() - mDown_y);
                        mCurrentDragingView.setAnimation(animation);
                        animation.startNow();
                        mMove_x = event.getX();
                        mMove_y = event.getY();
                    }

                    break;
                }
                case MotionEvent.ACTION_UP: {
                    mIsDraging = false;
                    break;
                }
            }
            return false;
        }
    }

    class OnChannelItemLongClick implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (mEditStatusFlag) {
                mIsDraging = true;
                mCurrentDragingView = view;
            } else {
                mEditStatusFlag = true;
                refresh();
            }
            return true;
        }
    }

    class OnChannelItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(final AdapterView<?> parent, View view, final int position, long
                id) {
            if (!mEditStatusFlag) {
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
                    setGridViewClickable(false);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setGridViewClickable(true);
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
            convertView = initView(convertView);

            mItemHeight = convertView.getMeasuredHeight();
            mItemWidth = convertView.getMeasuredWidth();

            playAnimation(mCurrentClickIsMine, position, mCurrentClickIndex);
            /* 设置gridview中item内容 */
            setItemContent(mEditStatusFlag, position);

            return convertView;
        }

        @Override
        protected int getVerticalDistance() {
            return getItemVerticalDistance();
        }

        @Override
        protected int getHorizontalDistance() {
            return getItemHorizontalDistance();
        }
    }

    public class ChannelHotAdapter extends ChannelBaseAdapter {

        public ChannelHotAdapter(Context context, List<ChannelWallItem> mChannelList) {
            super(context, mChannelList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = initView(convertView);

            /* 在converView创建完成之后测量大小 */
            mItemHeight = convertView.getMeasuredHeight();
            mItemWidth = convertView.getMeasuredWidth();

            /* 播放动画 */
            playAnimation(!mCurrentClickIsMine, position, mCurrentClickIndex);

            /* 设置gridview中item内容 */
            setItemContent(mEditStatusFlag, position);

            return convertView;
        }

        @Override
        protected int getVerticalDistance() {
            return getItemVerticalDistance();
        }

        @Override
        protected int getHorizontalDistance() {
            return getItemHorizontalDistance();
        }
    }
}
