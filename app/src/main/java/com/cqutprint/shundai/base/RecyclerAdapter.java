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

    protected List<M> dataList;                         //泛型数据定义
    protected OnItemClickListener<H> listener; //item点击监听器
    public static final int TYPE_HEADER = 0;   //说明是带有Header的
    public static final int TYPE_FOOTER = 1;   //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    private View mHeaderView;                        //recyclerView 头
    private View mFooterView;                         //recyclerView 脚

    protected interface OnItemClickListener<H> {
        void onItemClick(H holder);
    }

    /**
     * 设置数据,并设置点击回调接口
     * @param list 数据集合  Model 泛型
     * @param listener 回调接口  Holder 泛型
     */
    public RecyclerAdapter(@Nullable List<M> list, @Nullable OnItemClickListener<H> listener) {
        this.dataList = list;
        if (this.dataList == null) {
            this.dataList = new ArrayList<>();
        }
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(final H holder, int position) {
        //根据item type 绑定header footer or normal
        //这里加载数据是从position-1开始，因为position==0已经被header占用了
        switch (getItemViewType(position)){
            case TYPE_NORMAL:
                position= mHeaderView==null?position:position-1;
                holder.setData(dataList.get(position));
                if (listener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(holder);
                        }
                    });
                }
                break;
            case TYPE_HEADER:

                break;

            case TYPE_FOOTER:

                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return dataList.size();
        } else if (mHeaderView == null || mFooterView == null) {
            return dataList.size() + 1;
        } else {
            return dataList.size() + 2;
        }
    }

    /**
     * Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view
     * @return int type    TYPE_NORMAL & TYPE_HEADER & TYPE_FOOTER
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    /**
     * 给recyclerView 设置头
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    /**
     * 给recyclerView 设置脚
     * @param footerView
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public View getHeaderView() {
        return mHeaderView;
    }
    public View getFooterView() {
        return mFooterView;
    }

    /**------------------数据操纵------------------**/
    /**
     * 更新数据
     *
     * @param holder item对应的holder
     * @param data   item的数据
     */
    public void updateItem(H holder, M data) {
        dataList.set(holder.getLayoutPosition(), data);
    }

    /**
     * 获取一条数据
     *
     * @param holder item对应的holder
     * @return 该item对应的数据
     */
    public M getItem(H holder) {
        return dataList.get(holder.getLayoutPosition());
    }

    /**
     * 获取一条数据
     *
     * @param position item的位置
     * @return item对应的数据
     */
    public M getItem(int position) {
        return dataList.get(position);
    }

    /**
     * 填充数据,此方法会清空以前的数据
     * @param list 需要显示的数据
     */
    public void dataFill(List<M> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 追加一个集合数据
     * @param list 要追加的数据集合
     */
    public void dataAppend(List<M> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 在最顶部前置数据
     * @param data 要前置的数据
     */
    public void dataPrepose(M data) {
        dataList.add(0, data);
        notifyDataSetChanged();
    }

    /**
     * 在顶部前置数据集合
     * @param list 要前置的数据集合
     */
    public void dataPrepose(List<M> list) {
        dataList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void dataClear(){
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
         * @param viewId view的Id
         * @param <T> View的类型
         * @return view
         */
        protected <T extends View>T getView(@IdRes int viewId){
            return (T) (itemView.findViewById(viewId));
        }

        /**
         * 获取Context实例
         * @return context
         */
        protected Context getContext() {
            return itemView.getContext();
        }

        /**
         * 设置数据
         * @param data 要显示的数据对象
         */
        public abstract void setData(M data);
    }
}
