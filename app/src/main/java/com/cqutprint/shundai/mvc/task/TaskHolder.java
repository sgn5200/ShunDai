package com.cqutprint.shundai.mvc.task;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.RecyclerAdapter;

/**
 * Created by Administrator on 2016/10/13.
 */
public class TaskHolder extends RecyclerAdapter.BaseHolder<String> {

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
