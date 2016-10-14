package com.cqutprint.shundai.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.cqutprint.shundai.base.App;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ShareUtils {
    private static SharedPreferences sp;
    private ShareUtils(){}

    private static  String FILE_NAME="share_data";

    /**
     * sharepreference key
     */
    public static final String IS_FIRST_LUNCH="is_first_lunch";
    public static final String PHOTO_URI="photo_uri";
    public static final String PhotoBackground="PhotoBackground";


    private static SharedPreferences getSp(){
        if(sp==null)
            return App.getInstance().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        else
            return sp;
    }

    public static void setIsFirstLunch(boolean isFirstLunch){
        getSp().edit().putBoolean(IS_FIRST_LUNCH,isFirstLunch).apply();
    }

    public static boolean getIsFirstLunch() {
        return getSp().getBoolean(IS_FIRST_LUNCH,true);
    }

    public static void clear(){
        getSp().edit().clear().apply();
    }

    public static String getPhotoUri() {
        return getSp().getString(PHOTO_URI,"");
    }

    public static void setPhotoUri(String photoUri) {
        getSp().edit().putString(PHOTO_URI, photoUri).apply();
    }

    public static void setPhotoBackground(String v1) {
        getSp().edit().putString(PhotoBackground,v1).apply();
    }

    public static String getPhotoBackground(){
        return getSp().getString(PhotoBackground,"");
    }
}
