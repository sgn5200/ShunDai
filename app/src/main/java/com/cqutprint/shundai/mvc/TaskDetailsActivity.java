package com.cqutprint.shundai.mvc;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseActivity;

public class TaskDetailsActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_task_details;
    }

    @Override
    public void initView() {
        TextView tvTitle=bind(R.id.titleTvCenter);
        tvTitle.setText("详情");
        ImageView ivTitleLeft=bind(R.id.titleIvLeft);
        ivTitleLeft.setVisibility(View.VISIBLE);

        Bundle bundle=getIntent().getExtras();

        if(null!= bundle){
            String rs=bundle.getString("key");
            TextView tvName=bind(R.id.tvName);
            tvName.setText("name :"+rs);
        }

        initListener(this,R.id.titleIvLeft);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleIvLeft:
                finish();
                break;
        }

    }
}
