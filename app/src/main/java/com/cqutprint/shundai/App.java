package com.cqutprint.shundai;

import android.app.Application;

import com.cqutprint.shundai.utils.GlideLoader;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by Administrator on 2016/9/27.
 */
public class App extends Application {
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

        initGalleryFinal();
    }

    public static App getInstance(){
        return instance;
    }

    /**
     * 初始化调用相册的第三方开源
     *
     * GlideLoader 需要重写
     * ids <item name="adapter_item_tag_key" type="id"/>
     */
    private void initGalleryFinal() {
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new GlideLoader(), theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }
}
