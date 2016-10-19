package com.cqutprint.shundai.mvc;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseActivity;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class PublishMessageActivity extends BaseActivity implements View.OnClickListener,GalleryFinal.OnHanlderResultCallback {
    ImageView titleLeft,titleRight,ivAddpic;
    EditText ivMessage;
    private AlertDialog dialog;
    private View dialogView;
    private static final int CAMERA = 1;// 拍照
    private static final int GALLERY = 2;// 从相册     中选择

    @Override
    public int getRootLayoutId() {
       return R.layout.activity_publish_message;
    }

    @Override
    public void initView() {
        titleLeft=bind(R.id.titleIvLeft);
        titleLeft.setVisibility(View.VISIBLE);
        titleLeft.setOnClickListener(this);

        titleRight=bind(R.id.titleIvRight);
        titleRight.setVisibility(View.VISIBLE);
        titleRight.setImageResource(R.mipmap.dopublish_pressed);

        ((TextView)bind(R.id.titleTvCenter)).setText("校园说说");
        ivMessage=bind(R.id.ivMessage);
        ivAddpic=bind(R.id.ivAddPicture);

        initListener(this,ivAddpic,titleLeft,titleRight);

        dialog = new AlertDialog.Builder(this, R.style.fullScreen).setCancelable(true).create();
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_select_image, null);
        initListener(this, dialogView.findViewById(R.id.btCamera), dialogView.findViewById(R.id.btPhoto), dialogView.findViewById(R.id.btCancel));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleIvLeft:
                finish();
                break;
            case R.id.titleIvRight:
                showToast(ivMessage.getText().toString()+"   /n 消息已经发送成功");

                finish();
                break;
            case R.id.ivAddPicture:
                dialog.show();
                dialog.setContentView(dialogView);
                break;
            case R.id.btCamera:

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                GalleryFinal.openCamera(CAMERA, this);
                break;
            case R.id.btPhoto:
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                GalleryFinal.openGallerySingle(GALLERY, this);
                break;
            case R.id.btCancel:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        showToast("操作成功");
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        showToast("操作失败");
    }
}
