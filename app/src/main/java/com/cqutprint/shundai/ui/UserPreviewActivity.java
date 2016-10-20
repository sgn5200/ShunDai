package com.cqutprint.shundai.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseActivity;
import com.cqutprint.shundai.utils.ImageUtils;

public class UserPreviewActivity extends BaseActivity {

    FrameLayout root;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_user_preview;
    }

    @Override
    public void initView() {
        root=bind(R.id.activity_user_priview);




        initListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        },R.id.activity_user_priview);

        setImage();
    }

    private void setImage() {
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String path=bundle.getString("image_path");
            if(TextUtils.isEmpty(path))
                return;
            ImageView view=new ImageView(this);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageUtils.glideLoader(this,path, R.mipmap.default_bg,view);
            root.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0,0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
