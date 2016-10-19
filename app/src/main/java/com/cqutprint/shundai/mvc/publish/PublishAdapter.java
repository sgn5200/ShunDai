package com.cqutprint.shundai.mvc.publish;


import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.RecyclerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/10/12.
 */
public class PublishAdapter extends RecyclerAdapter<String,MyHolder> {

    /**
     * 设置数据,并设置点击回调接口
     *
     * @param list     数据集合
     * @param listener 回调接口
     */
    public PublishAdapter(@Nullable List<String> list, @Nullable OnItemClickListener<MyHolder> listener) {
        super(list, listener);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==TYPE_HEADER && getHeaderView() !=null){
            return new MyHolder(getHeaderView(),TYPE_HEADER);
        }else if(viewType==TYPE_FOOTER  && getFooterView() !=null){
            return new MyHolder(getFooterView(),TYPE_HEADER);
        }else
            return new MyHolder(parent, R.layout.item_publish);
    }

}

class MyHolder extends RecyclerAdapter.BaseHolder<String> {

    TextView textView, textView2;
    int type;

    public MyHolder(View view,int type){
        super(view);
        this.type=type;
    }

    public MyHolder(ViewGroup parent, @LayoutRes int resId) {
        super(parent, resId);
        textView = getView(R.id.tvName);
        textView2 = getView(R.id.tvMessage);
    }

    @Override
    public void setData(String data) {
        textView.setText(data);
        if(type==0)
        textView2.setText("hello  ");
    }
}


