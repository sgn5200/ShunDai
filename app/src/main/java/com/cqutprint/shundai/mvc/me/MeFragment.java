package com.cqutprint.shundai.mvc.me;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cqutprint.shundai.R;
import com.cqutprint.shundai.base.BaseFragment;
import com.cqutprint.shundai.utils.ImageUtils;
import com.cqutprint.shundai.utils.ShareUtils;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;


public class MeFragment extends BaseFragment implements View.OnClickListener,GalleryFinal.OnHanlderResultCallback {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView ivPhoto,ivBackground;

    private static final int CAMERA = 1;// 拍照
    private static final int GALLERY = 2;// 从相册     中选择

    private AlertDialog dialog;
    private View dialogView;
    private boolean isPressBackground;

    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        ivPhoto = bind(R.id.ivPhoto);
        ivBackground=bind(R.id.ivBackground);
        dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_image, null);
        dialog = new AlertDialog.Builder(getActivity(), R.style.fullScreen).setCancelable(true).create();
        String pathPhoto = ShareUtils.getPhotoUri();
        String pathBackground = ShareUtils.getPhotoBackground();

        if (!TextUtils.isEmpty(pathPhoto)) {
            ImageUtils.glideLoaderCircle(getActivity(), pathPhoto, R.mipmap.default_bg, ivPhoto);
        }

        if (!TextUtils.isEmpty(pathBackground)) {
            ImageUtils.glideLoader(getActivity(), pathBackground, R.mipmap.default_bg, ivBackground);
        }

        initListener(this, R.id.ivPhoto, R.id.photoParent);
        initListener(this, dialogView.findViewById(R.id.btCamera), dialogView.findViewById(R.id.btPhoto), dialogView.findViewById(R.id.btCancel));
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.ivPhoto:
                isPressBackground=false;
                dialog.show();
                dialog.setContentView(dialogView);

                break;
            case R.id.photoParent:
                isPressBackground=true;
                dialog.show();
                dialog.setContentView(dialogView);
                break;
            case R.id.btCancel:
                dialog.dismiss();
                break;
        }
    }


    //调用系统相册 回调接口
    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        String path = resultList.get(0).getPhotoPath();
        if(isPressBackground){
            ShareUtils.setPhotoBackground(path);
            ImageUtils.glideLoader(getActivity(), path, R.mipmap.default_bg, ivBackground);
        }else{
            ShareUtils.setPhotoUri(path);
            ImageUtils.glideLoaderCircle(getActivity(), path, R.mipmap.default_bg, ivPhoto);
        }
    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
    }
}
