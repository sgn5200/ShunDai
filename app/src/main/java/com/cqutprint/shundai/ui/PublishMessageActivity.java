package com.cqutprint.shundai.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseActivity;
import com.cqutprint.shundai.utils.ImageUtils;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class PublishMessageActivity extends BaseActivity implements View.OnClickListener, GalleryFinal.OnHanlderResultCallback {
    ImageView titleLeft, titleRight,ivPreAdd;
    EditText ivMessage;
    private AlertDialog dialog;
    private View dialogView;
    private  final int CAMERA = 1;// 拍照
    private  final int GALLERY = 2;// 从相册     中选择
    private LinearLayout addContainer;
    private LinearLayout.LayoutParams layoutParams;
    @Override
    public int getRootLayoutId() {
        return R.layout.activity_publish_message;
    }

    @Override
    public void initView() {
        titleLeft = bind(R.id.titleIvLeft);
        titleLeft.setVisibility(View.VISIBLE);
        titleLeft.setOnClickListener(this);

        titleRight = bind(R.id.titleIvRight);
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setImageResource(R.mipmap.dopublish_pressed);

        ((TextView) bind(R.id.titleTvCenter)).setText("校园说说");
        ivMessage = bind(R.id.ivMessage);

        ivPreAdd= bind(R.id.ivAdd);
        addContainer=bind(R.id.picContainer);

        int h= (int)getResources().getDimension(R.dimen.px_80_0_dp);
        int margin=(int)getResources().getDimension(R.dimen.px_5_0_dp);
        layoutParams=new LinearLayout.LayoutParams(h,h);
        layoutParams.setMargins(margin,margin,margin,margin);
        initListener(this, ivPreAdd, titleLeft, titleRight);

        dialog = new AlertDialog.Builder(this, R.style.fullScreen).setCancelable(true).create();
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_image, null);
        initListener(this, dialogView.findViewById(R.id.btCamera), dialogView.findViewById(R.id.btPhoto), dialogView.findViewById(R.id.btCancel));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleIvLeft:
                finish();
                break;
            case R.id.titleIvRight:
                showToast(ivMessage.getText().toString() + "   /n 消息已经发送成功");
                finish();
                break;

            case R.id.ivAdd:
                dialog.show();
                dialog.setContentView(dialogView);
                break;
            case R.id.btCamera:    //dialog camera
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                GalleryFinal.openCamera(CAMERA, this);
                break;
            case R.id.btPhoto:      //dialog gallery
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                GalleryFinal.openGalleryMuti(GALLERY, 3, this);
                break;
            case R.id.btCancel:    //dialog cancel
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if(resultList==null)
            return;

        for(PhotoInfo info:resultList){
            ImageView view=new ImageView(this);
            view.setOnClickListener(new AddImgListener(info.getPhotoPath()));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageUtils.glideLoader(this,info.getPhotoPath(),R.mipmap.default_bg,view);
            addContainer.addView(view,addContainer.getChildCount()-1,layoutParams);
        }

        if(addContainer.getChildCount()>3){
            ivPreAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        showToast("操作失败");
    }

    class AddImgListener implements View.OnClickListener{
        String path;
        public AddImgListener(String path){
            this.path=path;
        }
        @Override
        public void onClick(View v) {
            Bundle bundle=new Bundle();
            bundle.putString("image_path",path);
            lunchActivity(UserPreviewActivity.class,bundle);
        }
    }
}
