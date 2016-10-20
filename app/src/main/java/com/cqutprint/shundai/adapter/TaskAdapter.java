package com.cqutprint.shundai.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.utils.Log;

import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */
public class TaskAdapter extends RecyclerAdapter<String,TaskHolder>{

    /**
     * 设置数据,并设置点击回调接口
     * @param list     数据集合
     * @param listener 回调接口
     */
    public TaskAdapter(@Nullable List<String> list, @Nullable OnItemClickListener<TaskHolder> listener) {
        super(list, listener);
    }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TASKHOLDER","TYPE  "+viewType);
        if(viewType==TYPE_HEADER && getHeaderView() !=null){
            return new TaskHolder(getHeaderView());
        }else if(viewType==TYPE_FOOTER  && getFooterView() !=null){
            return new TaskHolder(getFooterView());
        }else
        return new TaskHolder(parent, R.layout.item_publish);
    }

}

class TaskHolder extends RecyclerAdapter.BaseHolder<String> {

    public TaskHolder(ViewGroup parent, @LayoutRes int resId) {
        super(parent, resId);
    }

    int type;
    public TaskHolder (View view){
        super(view);
    }

    @Override
    public void setData(String data) {

        TextView tvName=getView(R.id.tvName);
        TextView tvMessage=getView(R.id.tvMessage);
        tvName.setText(data);
        tvMessage.setText(data+" message type   "+ type);
    }
}

