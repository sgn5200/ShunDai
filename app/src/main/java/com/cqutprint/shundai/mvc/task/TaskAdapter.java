package com.cqutprint.shundai.mvc.task;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

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
    public TaskAdapter(@Nullable List<String> list, @Nullable OnItemClickListener<com.cqutprint.shundai.mvc.task.TaskHolder> listener) {
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

