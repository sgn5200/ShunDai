package com.cqutprint.shundai.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Administrator on 2016/10/9.
 * Created by qly on 2016/6/22.
 * 将图片转化为圆形
 */
public class ImageUtils extends BitmapTransformation {

    public ImageUtils(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }


    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(erroImg).error(erroImg).into(iv);
    }


    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param iv
     */
    public static void glideLoaderCircle(Context context, String url, int erroImg, ImageView iv) {
        BitmapTransformation tm= new ImageUtils(context);
        Glide.with(context).load(url).error(erroImg).transform(tm).into(iv);
    }
}
