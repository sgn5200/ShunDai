package com.cqutprint.shundai.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqutprint.shundai.R;


/**
 * Created by Administrator on 2016/10/9.
 */
public class CustomTitle extends RelativeLayout{

    TextView tvLeft,tvRight;
    private TitleChangeListener listener;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public CustomTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
         tvLeft=new TextView(context);
         tvRight=new TextView(context);

        tvLeft.setText("消息");
        tvLeft.setBackground(getResources().getDrawable(R.drawable.left_corners));
        tvRight.setText("评价");


        tvLeft.setGravity(Gravity.CENTER);
        tvRight.setGravity(Gravity.CENTER);


        LinearLayout layout=new LinearLayout(context,attrs);
        RelativeLayout.LayoutParams pl=new RelativeLayout.LayoutParams(
                (int)getResources().getDimension(R.dimen.px_200_0_dp) , (int)getResources().getDimension(R.dimen.px_40_0_dp));

        pl.addRule(RelativeLayout.CENTER_IN_PARENT);


        layout.setLayoutParams(pl);
        layout.setBackground(getResources().getDrawable(R.drawable.board_solid));

        layout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(context,attrs);
        params.weight=1;
        params.height=LayoutParams.MATCH_PARENT;
        params.width=0;

        layout.addView(tvLeft,params);
        layout.addView(tvRight,params);

        setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        addView(layout);

        tvLeft.setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                tvLeft.setBackground(getResources().getDrawable(R.drawable.left_corners));
                tvRight.setBackground(null);
                if(listener!=null){
                    listener.changed(true);
                }
                tvLeft.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tvRight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.changed(false);
                }
                tvRight.setBackground(getResources().getDrawable(R.drawable.right_corners));
                tvRight.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tvLeft.setTextColor(getResources().getColor(R.color.white));
                tvLeft.setBackground(null);
            }
        });
    }

    public void addChangeListener(TitleChangeListener listener){
        this.listener=listener;
    }

    public interface TitleChangeListener{
        void changed(boolean isLeftSelected);
    }

    public void setText(String left,String right){
        tvLeft.setText(left);
        tvRight.setText(right);
    }
}
