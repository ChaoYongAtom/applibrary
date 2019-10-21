package com.wcy.app.lib.imageloader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

/**
 * Created by wcy on 2017/3/28.
 */

public class ImageLoaderManager {
    private static int placeholderImage = 0;

    /**
     * @param url      图片地址
     * @param erroImg  加载错误之后的错误图
     * @param emptyImg 加载成功之前占位图
     * @param iv
     * @param width    加载成功之前占位图
     * @param height
     */
    public static void loadImage(Object url, int erroImg, int emptyImg, final ImageView iv, int width, int height) {
        RequestOptions options = new RequestOptions().override(width, height).placeholder(emptyImg)  //加载成功之前占位图
                .error(erroImg);//加载错误之后的错误图
        Glide.with(iv.getContext()).asBitmap().load(url).apply(options).into(iv);
    }

    public static void loadImage(Object url, ImageView iv, RequestOptions options) {
        Glide.with(iv.getContext()).asBitmap().load(url).apply(options).into(iv);
    }

    public static void loadImage(Object url, ImageView iv) {
        RequestOptions options = new RequestOptions().placeholder(placeholderImage)  //加载成功之前占位图
                .error(placeholderImage);//加载错误之后的错误图
        Glide.with(iv.getContext()).asBitmap().load(url).apply(options).into(iv);
    }

    public static void loadImage(Object url, final ImageView iv, int width, int height) {
        RequestOptions options = new RequestOptions().override(width, height).placeholder(placeholderImage)  //加载成功之前占位图
                .error(placeholderImage);//加载错误之后的错误图
        Glide.with(iv.getContext()).asBitmap().load(url).apply(options).into(iv);
    }

    /**
     * @param url      图片地址
     * @param erroImg  加载错误之后的错误图
     * @param emptyImg 加载成功之前占位图
     * @param iv
     */
    public static void loadImage(Object url, int erroImg, int emptyImg, final ImageView iv) {
        RequestOptions options = new RequestOptions().placeholder(emptyImg)  //加载成功之前占位图
                .error(erroImg);//加载错误之后的错误图
        Glide.with(iv.getContext()).asBitmap().load(url).apply(options).into(iv);
    }


    /**
     * 圆形显示
     **/
    public static void loadCircleImage(Object url, final ImageView iv) {
        RequestOptions options = new RequestOptions().placeholder(placeholderImage)  //加载成功之前占位图
                .centerCrop().transform(new CircleCrop()).error(placeholderImage);//加载错误之后的错误图
        Glide.with(iv.getContext()).load(url).apply(options).into(iv);
    }


    /**
     * 圆角显示
     **/
    public static void loadRoundCornerImage(Object url, final ImageView iv) {
        loadRoundCornerImage(url, 20, iv);
    }

    /**
     * @param url    图片加载地址
     * @param radius 圆角图片大小
     * @param iv
     */
    public static void loadRoundCornerImage(Object url, int radius, final ImageView iv) {
        RequestOptions options = new RequestOptions().placeholder(placeholderImage)  //加载成功之前占位图
                .transform(new RoundedCorners(radius)).error(placeholderImage);//加载错误之后的错误图
        Glide.with(iv.getContext()).load(url).apply(options).into(iv);
    }

    /**
     * 为非view加载图片
     */
    private void displayImageForTarget(Context context, Target target, String url) {
        Glide.with(context).asBitmap().load(url).transition(withCrossFade()).fitCenter().into(target);
    }

    /**
     * 加载本地图片
     **/
    public static void loadLockImage(String imagePath, final ImageView imageView) {
        Glide.with(imageView.getContext()).load("file://" + imagePath).into(imageView);


    }

    public static void loadLockImage(Uri imagePath, final ImageView imageView) {
        Glide.with(imageView.getContext()).load("file://" + imagePath).into(imageView);


    }

    public static void setPlaceholderImage(int placeholderImage) {
        ImageLoaderManager.placeholderImage = placeholderImage;
    }

}
