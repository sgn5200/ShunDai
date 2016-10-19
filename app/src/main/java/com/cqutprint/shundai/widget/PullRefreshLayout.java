package com.cqutprint.shundai.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.utils.Log;
import com.cqutprint.shundai.utils.ScreenUtil;

public class PullRefreshLayout extends ViewGroup {

    private static final int LIMIT_DRAG_HEIGHT = 1;
    private static final int DRAG_MAX_DISTANCE = 80;
    private int mDragMode = LIMIT_DRAG_HEIGHT;
    /**
     * 下拉拖拽阻尼
     */
    private static final float DRAG_RATE = .4f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    public static final int MAX_OFFSET_ANIMATION_DURATION = 700;

    private static final int INVALID_POINTER = -1;
    private String TAG = getClass().getSimpleName();
    private Context context;
    private View mTarget;
    private RelativeLayout mRefreshView;
    private Interpolator mDecelerateInterpolator;
    private int mTouchSlop;
    /**
     * 触发刷新的下拉距离，也是mRefreshView刷新时停留的高度
     */
    private int mTotalDragDistance;
    /**
     * 下拉进度
     */
    private float mCurrentDragPercent;
    /**
     * mCurrentOffsetTop=mTarget.getTop();内容view的top值
     */
    private int mCurrentOffsetTop;

    private boolean isRefreshing;

    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;
    private int mFrom;
    private float mFromDragPercent;
    private boolean mNotify;
    private OnRefreshListener mListener;
    private int finishRefreshToPauseDuration = 0;

    public PullRefreshLayout(Context context) {
        this(context, null);
    }

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mTotalDragDistance = ScreenUtil.dip2px(DRAG_MAX_DISTANCE);
        mRefreshView = new RelativeLayout(context);
        addView(mRefreshView);
        bindRecyclerView();
        //在构造函数上加上这句，防止自定义View的onDraw方法不执行的问题
        setWillNotDraw(false);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    private void setRefreshView() {
        if (mRefreshView == null) {
            return;
        }
        mRefreshView.removeAllViews();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mRefreshView.setLayoutParams(layoutParams);
        refreshHeader = LayoutInflater.from(getContext()).inflate(R.layout.layout_pull_refresh, mRefreshView, true);
        initRefresh();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ensureTarget();
        if (mTarget == null)
            return;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);
        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mTotalDragDistance = (int) (mRefreshView.getMeasuredHeight() * 0.8f);
    }

    private View mTarget2;

    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mRefreshView) {
                    mTarget = child;
                }
            }
            mTarget2 = getChildAt(getChildCount() - 1);
        }
    }

    /**
     * 该函数只干两件事
     * 1.记录手指按下的坐标
     * 2.判断是否拦截处理事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isEnabled() || isRefreshing || canChildScrollUp()) {
            return false;
        }

        switch (MotionEventCompat.getActionMasked(ev)) {
            //手指按下，记录点击坐标
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(ev, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialMotionY;
                //如果是滑动动作，将标志mIsBeingDragged置为true
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                }
                break;
            //手指松开，标志复位
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }

        //如果是正在被下拉拖动，拦截该事件，不往下传递；反之，你懂的
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        //如果不是在被下拉拖动，不处理，直接返回
        if (!mIsBeingDragged) {
            return super.onTouchEvent(ev);
        }
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    return false;
                }

                //将要移到的位置对应的Y坐标，也是在未松手前，target下拉的总高度 int
                int targetY;
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float yDiff = y - mInitialMotionY;
                //未松手前,总共下拉的距离 float
                final float scrollTop = yDiff * DRAG_RATE;
                if (mCurrentDragPercent < 0) {
                    return false;
                }
                if (mDragMode == LIMIT_DRAG_HEIGHT) {
                    //控制下拉的最大距离
                    mCurrentDragPercent = scrollTop / mTotalDragDistance;
                    float boundedDragPercent = Math.min(1f, Math.abs(mCurrentDragPercent));
                    float extraOS = Math.abs(scrollTop) - mTotalDragDistance;
                    float slingshotDist = mTotalDragDistance;
                    float tensionSlingshotPercent = Math.max(0,
                            Math.min(extraOS, slingshotDist * 2) / slingshotDist);
                    float tensionPercent = (float) ((tensionSlingshotPercent / 4) - Math.pow(
                            (tensionSlingshotPercent / 4), 2)) * 2f;
                    float extraMove = (slingshotDist) * tensionPercent / 2;
                    targetY = (int) ((slingshotDist * boundedDragPercent) + extraMove);
                } else {
                    targetY = (int) scrollTop; //效果一样,但可以无限下拉
                }
                if (!isRefreshing) {
                    onDragChanged(mCurrentDragPercent);
                }

                mRefreshView.setVisibility(VISIBLE);
                //调整更新位置，传过去的值是每次的偏移量
                setTargetOffsetTop(targetY - mCurrentOffsetTop, true);
                break;
            }
            //做多指触控处理
            case MotionEventCompat.ACTION_POINTER_DOWN:
                //将最后一只按下的手指作为ActivePointer
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            //手指松开！
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                //排除是无关手指
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                //计算松开瞬间下拉的距离
                final float overScrollTop = (y - mInitialMotionY) * DRAG_RATE;
                //标志复位
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;

                if (overScrollTop > mTotalDragDistance) {//触发刷新
                    setRefreshing(true, true);
                } else {//回滚
                    isRefreshing = false;
                    animateOffsetToStartPosition();
                }
                return false;//系列点击事件已经处理完，将处理权交还mTarget
            }
        }

        return true;//该系列点击事件未处理完，消耗此系列事件
    }

    /**
     * 回滚动画
     */
    private void animateOffsetToStartPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;
        long animationDuration = Math.abs((long) (MAX_OFFSET_ANIMATION_DURATION * mFromDragPercent));

        //当动画开始，通过applyTransformation(float interpolatedTime, Transformation t)方法的interpolatedTime参数（0.0~1.0）
        //内部调用setTargetOffsetTop(offset, false);两者更新位置和RefreshView的加载动画
        //当动画结束时，更新mCurrentOffsetTop=mTarget.getTop();
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(animationDuration);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(mToStartListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToStartPosition);
    }

    private void animateOffsetToCorrectPosition() {
        mFrom = mCurrentOffsetTop;
        mFromDragPercent = mCurrentDragPercent;

        //动画上调至mTotalDragDistance这个高度
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(MAX_OFFSET_ANIMATION_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        //回调onRefresh()
        mAnimateToCorrectPosition.setAnimationListener(mReadyToRefreshListener);
        mRefreshView.clearAnimation();
        mRefreshView.startAnimation(mAnimateToCorrectPosition);
    }

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop;
            int endTarget = mTotalDragDistance;
            targetTop = (mFrom + (int) ((endTarget - mFrom) * interpolatedTime));
            int offset = targetTop - mTarget.getTop();

            mCurrentDragPercent = mFromDragPercent - (mFromDragPercent - 1.0f) * interpolatedTime;
            if (!isRefreshing) {
                onDragChanged(mCurrentDragPercent);
            }
            setTargetOffsetTop(offset, false /* requires update */);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int targetTop = mFrom - (int) (mFrom * interpolatedTime);
        float targetPercent = mFromDragPercent * (1.0f - interpolatedTime);
        //计算偏移量
        int offset = targetTop - mTarget.getTop();

        //更新RefreshView加载动画
        mCurrentDragPercent = targetPercent;
        if (!isRefreshing) {
            onDragChanged(mCurrentDragPercent);
        }
        setTargetOffsetTop(offset, false);
    }

    public void setRefreshing(boolean refreshing) {
        if (isRefreshing != refreshing) {
            setRefreshing(refreshing, true /* notify */);
        }
    }

    /**
     * @param refreshing 是否触发刷新
     * @param notify     是否回调onRefresh()方法
     */
    public void setRefreshing(boolean refreshing, final boolean notify) {
        mNotify = notify;
        ensureTarget();
        isRefreshing = refreshing;
        if (isRefreshing) {
            //开始刷新
            animateOffsetToCorrectPosition();//位置上调到合适的位置
        } else {
            //刷新完成
            onFinish();


            //让mRefreshView停留几秒钟
            mRefreshView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animateOffsetToStartPosition();
                }
            }, finishRefreshToPauseDuration);
        }
    }

    private Animation.AnimationListener mToStartListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mCurrentOffsetTop = mTarget.getTop();//更新mCurrentOffsetTop
        }
    };

    private Animation.AnimationListener mReadyToRefreshListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (isRefreshing) {//开始刷新
                if (mNotify) {
                    onRefresh();
                }
            } else {//停止刷新
                animateOffsetToStartPosition();
            }
            //更新mCurrentOffsetTop
            mCurrentOffsetTop = mTarget.getTop();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 处理多指触控的点击事件
     */
    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    /**
     * 通过调用offsetTopAndBottom方法
     * 更新mTarget和mBaseRefreshView的位置
     * 更新target下拉高度--mCurrentOffsetTop
     *
     * @param offset         偏移位移
     * @param requiresUpdate 时候invalidate()
     */
    private void setTargetOffsetTop(int offset, boolean requiresUpdate) {
        mTarget.offsetTopAndBottom(offset);
        mRefreshView.offsetTopAndBottom(offset);
        mCurrentOffsetTop = mTarget.getTop();
        if (requiresUpdate /*&& android.os.Build.VERSION.SDK_INT < 11*/) {
            invalidate();
        }
    }


    private boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(mTarget, -1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        ensureTarget();
        if (mTarget == null)
            return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        //mTarget MATCH_PARENT
        mTarget.layout(left, top + mCurrentOffsetTop, left + width - right, top + height - bottom + mCurrentOffsetTop);
        //mRefreshView隐藏在mTarget的上面
        mRefreshView.layout(left, top - mRefreshView.getMeasuredHeight() + mCurrentOffsetTop, left + width - right, top + mCurrentOffsetTop - bottom);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public void setFinishRefreshToPauseDuration(int finishRefreshToPauseDuration) {
        this.finishRefreshToPauseDuration = finishRefreshToPauseDuration;
    }

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * 设置默认刷新 view
     */

    private RecyclerView recyclerView;

    private RecyclerView bindRecyclerView() {
        recyclerView = new RecyclerView(context);
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setRefreshView();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return recyclerView;
    }

    private RecyclerAdapter adapter;

    public void setAdapter(RecyclerAdapter adapter) {
        recyclerView.setAdapter(adapter);
        this.adapter = adapter;

        View footer=adapter.getFooterView();
         tvLoadMore= (TextView) footer.findViewById(R.id.tvLoadMore);
         bar= (ProgressBar) footer.findViewById(R.id.progressLoadMore);
        tvLoadMore.setText("加载更多");
        tvLoadMore.setVisibility(VISIBLE);
        bar.setVisibility(GONE);

        footer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              tvLoadMore.setVisibility(INVISIBLE);
                bar.setVisibility(VISIBLE);
                mListener.onLoadMore();
            }
        });

    }

    public void onLoadFinish(){
        tvLoadMore.setVisibility(VISIBLE);
        bar.setVisibility(GONE);
    }

    public void setItemDecoration(int space) {
        recyclerView.addItemDecoration(new SpaceItemDecoration(space));
    }

    View refreshHeader;
    private ProgressBar progressBar;
    private TextView tvLoad;
    private ImageView ivLoad;
    private TextView tvLoadMore;
    ProgressBar bar;

    private void initRefresh() {
        progressBar = (ProgressBar) refreshHeader.findViewById(R.id.pb_view);
        tvLoad = (TextView) refreshHeader.findViewById(R.id.text_view);
        tvLoad.setText("下拉刷新");
        ivLoad = (ImageView) refreshHeader.findViewById(R.id.image_view);
        ivLoad.setVisibility(View.VISIBLE);
        ivLoad.setImageResource(R.mipmap.load);
        progressBar.setVisibility(View.GONE);
    }

    private void onRefresh() {

        mListener.onRefresh();
        tvLoad.setText("正在刷新");
        ivLoad.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshing(false);
            }
        }, 2000);
    }

    private void onDragChanged(float percent) {
        if (percent >= 1.0f) {
            tvLoad.setText("松开刷新");
            ivLoad.setVisibility(View.VISIBLE);
            ivLoad.setRotation(180);
        } else {
            tvLoad.setText("下拉刷新");
            ivLoad.setVisibility(View.VISIBLE);
            ivLoad.setRotation(0);
        }
    }

    private void onFinish() {
        Log.i(TAG);
        tvLoad.setText("刷新成功");
        ivLoad.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    //// is use SwipeRefreshLayout
    //    private void setRefresh(final SwipeRefreshLayout refreshLayout) {
//        //下拉刷新
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                clearData();
//                adapter.dataAppend(initData());
//                refreshLayout.setRefreshing(false);
//            }
//        });
//
//        //上拉加载
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                                             @Override
//                                             public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                                                 super.onScrolled(recyclerView, dx, dy);
//                                                 lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//                                             }
//
//                                             @Override
//                                             public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                                                 super.onScrollStateChanged(recyclerView, newState);
//
//
//                                                 Log.i(TAG,"  lastVisibleItem  "+lastVisibleItem+"   adapter.getItemCount   "+adapter.getItemCount());
//
//                                                 if (newState == RecyclerView.SCROLL_STATE_IDLE
//                                                         && lastVisibleItem + 1 == adapter.getItemCount()) {
//                                                     refreshLayout.setRefreshing(true);
//
//                                                     //分页获取数据
//                                                     clearData();
//                                                     adapter.dataAppend(setList());
//
//                                                     refreshLayout.setRefreshing(false);
//                                                 }
//                                             }
//                                         }
//        );
//    }
}