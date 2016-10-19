package com.cqutprint.shundai.base;

/**
 * Created by Administrator on 2016/10/12.
 */

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的Adapter
 * M -->Model
 * H-->Holder
 * Created by guangneng on 2016/10/10.
 */
public abstract class RecyclerAdapter<M, H extends RecyclerAdapter.BaseHolder<M>> extends RecyclerView.Adapter<H> {

    private List<M> dataList;                         //泛型数据定义
    private OnItemClickListener<H> listener; //item点击监听器

    protected static final int TYPE_HEADER = 0;   //item类型
    protected static final int TYPE_FOOTER = 1;
    private static final int ITEM_TYPE_CONTENT = 2;

    private View mHeaderView;                        //recyclerView 头
    private View mFooterView;                         //recyclerView 脚

    private int mHeaderCount = 0;//头部View个数
    private int mBottomCount = 0;//底部View个数

    public interface OnItemClickListener<H> {
        void onItemClick(H holder);
    }

    /**
     * 设置数据,并设置点击回调接口
     *
     * @param list     数据集合  Model 泛型
     * @param listener 回调接口  Holder 泛型
     */
    public RecyclerAdapter(@Nullable List<M> list, @Nullable OnItemClickListener<H> listener) {
        this.dataList = list;
        if (this.dataList == null) {
            this.dataList = new ArrayList<>();
        }
        this.listener = listener;
    }


    public void setDataList(List<M> list) {
        this.dataList = list;
    }

    //内容长度
    public int getContentItemCount() {
        return dataList.size();
    }


    public void setOnItemClickListener(@Nullable OnItemClickListener<H> listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final H holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_CONTENT) {
            holder.setData(dataList.get(position - mHeaderCount));

            if (listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(holder);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return mHeaderCount + getContentItemCount() + mBottomCount;
    }

    /**
     * Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view
     *
     * @return int type    TYPE_NORMAL & TYPE_HEADER & TYPE_FOOTER
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount != 0 && position < mHeaderCount) {
            return TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + getContentItemCount())) {
            return TYPE_FOOTER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    /**
     * 给recyclerView 设置头
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        mHeaderCount += 1;
    }

    /**
     * 给recyclerView 设置脚
     *
     * @param footerView
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        mBottomCount += 1;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getFooterView() {
        return mFooterView;
    }


    /**------------------数据操纵------------------**/
    /**
     * 获取一条数据
     *
     * @param holder item对应的holder
     * @return 该item对应的数据
     */
    public M getItem(H holder) {
        return dataList.get(holder.getLayoutPosition() - mHeaderCount);
    }

    /**
     * 填充数据,此方法会清空以前的数据
     *
     * @param list 需要显示的数据
     */
    public void dataFill(List<M> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 追加一个集合数据
     *
     * @param list 要追加的数据集合
     */
    public void dataAppend(List<M> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 在最顶部前置数据
     *
     * @param data 要前置的数据
     */
    public void dataPrepose(M data) {
        dataList.add(0, data);
        notifyDataSetChanged();
    }

    /**
     * 在顶部前置数据集合
     *
     * @param list 要前置的数据集合
     */
    public void dataPrepose(List<M> list) {
        dataList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void dataClear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public abstract static class BaseHolder<M> extends RecyclerView.ViewHolder {

        public BaseHolder(View itemView) {
            super(itemView);
        }

        public BaseHolder(ViewGroup parent, @LayoutRes int resId) {
            super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        }

        /**
         * 获取布局中的View
         *
         * @param viewId view的Id
         * @param <T>    View的类型
         * @return view
         */
        protected <T extends View> T getView(@IdRes int viewId) {
            return (T) (itemView.findViewById(viewId));
        }

        /**
         * 获取Context实例
         *
         * @return context
         */
        protected Context getContext() {
            return itemView.getContext();
        }

        /**
         * 设置数据
         *
         * @param data 要显示的数据对象
         */
        public abstract void setData(M data);
    }
}
