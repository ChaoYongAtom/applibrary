package org.wcy.android.utils.GlideUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by wcy on 2017/3/28.
 */

public class GlideImgManager {
    private static int placeholderImage = 0;

    public static void loadImage(Context context, String url, int erroImg, int emptyImg, final ImageView iv) {
        //原生 API
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(emptyImg)
                .error(erroImg)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        iv.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        iv.setImageDrawable(errorDrawable);
                    }
                });

    }

    public static void loadImage(Context context, String url, final ImageView iv) {
        //原生 API
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeholderImage)
                .error(placeholderImage)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        iv.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        iv.setImageDrawable(errorDrawable);
                    }
                });

    }


    public static void loadGifImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(placeholderImage)
                .error(placeholderImage).dontAnimate().into(iv);
    }


    /**
     * 圆形显示
     **/
    public static void loadCircleImage(Context context, String url, final ImageView iv) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeholderImage)
                .error(placeholderImage)
                .transform(new GlideCircleTransform(context))
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        iv.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        iv.setImageDrawable(errorDrawable);
                    }
                });

    }


    /**
     * 圆角显示
     **/
    public static void loadRoundCornerImage(Context context, String url, final ImageView iv) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(placeholderImage)
                .error(placeholderImage)
                .transform(new GlideRoundTransform(context, 10))
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap>
                            glideAnimation) {
                        iv.setImageBitmap(resource);
                    }
                });
    }


    /**
     * 加载本地图片
     **/
    public static void loadLockImage(Context context, String imagePath, final ImageView imageView) {
        Glide.with(context)
                .load("file://" + imagePath)
                .into(imageView);


    }

    public static void loadLockImage(Context context, Uri imagePath, final ImageView imageView, int width,
                                     int height) {
        Glide.with(context)
                .load("file://" + imagePath)
                .override(width, height)
                .dontAnimate()
                .into(imageView);


    }

    public static void loadLockImage(Context context, Uri imagePath, final ImageView imageView) {
        Glide.with(context)
                .load(imagePath)
                .dontAnimate()
                .into(imageView);


    }

    /**
     * 加载app资源图片
     **/
    public static void loadImage(Context context, final int resourceId, final ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .dontAnimate()
                .into(imageView);
    }

    /**
     * 加载git图片
     *
     * @param context
     * @param resourceId
     * @param imageView
     */
    public static void loadGif(Context context, int resourceId, final ImageView imageView) {
        Glide.with(context).load(resourceId).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into
                (imageView);
    }

    public static void setPlaceholderImage(int placeholderImage) {
        GlideImgManager.placeholderImage = placeholderImage;
    }
}
