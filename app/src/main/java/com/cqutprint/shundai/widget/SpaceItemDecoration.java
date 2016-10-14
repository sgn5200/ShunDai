package com.cqutprint.shundai.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cqutprint.shundai.utils.ScreenUtil;
import com.cqutprint.shundai.utils.Log;

/**
 * recycler view 分割线控制
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace ;

    /**
     * @param space 传入的值，其单位视为dp
     */
    public SpaceItemDecoration(int space) {
        this.mSpace = ScreenUtil.dip2px(space);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        Log.d("TAG", "itemCount>>"  + ";Position>>" + pos);
        outRect.left = 0;
        outRect.top = mSpace;
        outRect.bottom = 0;
    }
}