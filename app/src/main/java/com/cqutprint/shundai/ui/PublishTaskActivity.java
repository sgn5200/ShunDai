package com.cqutprint.shundai.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseActivity;

public class PublishTaskActivity extends BaseActivity implements View.OnClickListener {

    ImageView ivTitleLeft,ivTitleRight;
    TextView tvTitleCenter;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_publish_task;
    }

    @Override
    public void initView() {
        ivTitleLeft=bind(R.id.titleIvLeft);
        ivTitleLeft.setVisibility(View.VISIBLE);
        ivTitleRight=bind(R.id.titleIvRight);
        ivTitleRight.setVisibility(View.VISIBLE);
        ivTitleRight.setImageResource(R.mipmap.dopublish_pressed);
        tvTitleCenter=bind(R.id.titleTvCenter);
        tvTitleCenter.setText(R.string.publish_task);

        initListener(this,ivTitleLeft,ivTitleRight);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleIvLeft:
                finish();
                break;
            case R.id.titleIvRight:
                finish();
                break;
        }
    }
}
