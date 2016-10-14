package com.cqutprint.shundai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cqutprint.shundai.base.RecyclerAdapter;
import com.cqutprint.shundai.widget.PullRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment {

    public PullRefreshLayout mPullToRefreshView;
    private ProgressBar progressBar;
    private TextView textView;
    private ImageView imageView;
    private ImageView imgDone;
    private List<String> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test, container, false);

        mList=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mList.add("------------>"+String.valueOf(i));
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SampleAdapter(mList,null));
        mPullToRefreshView = (PullRefreshLayout) rootView.findViewById(R.id.pull_to_refresh);
        createRefreshView();
        mPullToRefreshView.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                textView.setText("正在刷新");
                Log.d("TAG", "onRefresh() called with: " + "");
                imgDone.setVisibility(View.GONE);
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }

            @Override
            public void onDragDistanceChange(float distance, float percent, float offset) {
                if (percent >= 1.0f) {
                    textView.setText("松开刷新");
                    imgDone.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setRotation(180);
                } else {
                    textView.setText("下拉刷新");
                    imgDone.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setRotation(0);
                }
            }

            @Override
            public void onFinish() {
                textView.setText("刷新成功");
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                imgDone.setVisibility(View.VISIBLE);
                Log.d("TAG", "textView.Text: " + textView.getText());
            }
        });
        mPullToRefreshView.setFinishRefreshToPauseDuration(800);
        return rootView;
    }

    private View createRefreshView() {
        View headerView=mPullToRefreshView.setRefreshView(R.layout.layout_pull_refresh);
        progressBar = (ProgressBar) headerView.findViewById(R.id.pb_view);
        textView = (TextView) headerView.findViewById(R.id.text_view);
        textView.setText("下拉刷新");
        imageView = (ImageView) headerView.findViewById(R.id.image_view);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.mipmap.v2female);
        imgDone=(ImageView)headerView.findViewById(R.id.img_done);
        imgDone.setImageResource(R.mipmap.v2type1);
        imgDone.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        return headerView;
    }

    private class SampleAdapter extends RecyclerAdapter<String,SampleHolder> {

        /**
         * 设置数据,并设置点击回调接口
         * @param list     数据集合  Model 泛型
         * @param listener 回调接口  Holder 泛型
         */
        public SampleAdapter(@Nullable List<String> list, @Nullable OnItemClickListener<SampleHolder> listener) {
            super(list, listener);
        }

        @Override
        public SampleHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_publish, parent, false);
            return new SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(SampleHolder holder, int pos) {
            String data = mList.get(pos);
            holder.setData(data);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}

 class SampleHolder extends RecyclerAdapter.BaseHolder<String> {

    private TextView tvNum;

    public SampleHolder(View itemView) {
        super(itemView);
        tvNum = (TextView) itemView.findViewById(R.id.tvName);
    }

     @Override
     public void setData(String data) {
         tvNum.setText(data);
     }
}
