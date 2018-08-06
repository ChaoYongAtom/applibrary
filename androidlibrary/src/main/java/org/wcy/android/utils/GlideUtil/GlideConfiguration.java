package org.wcy.android.utils.GlideUtil;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by YaphetZhao
 * on 2016/12/19.
 * <p>
 * QQ:11613371
 * GitHub:https://github.com/YaphetZhao
 * Email:yaphetzhao@foxmail.com
 * Email_EN:yaphetzhao@gmail.com
 * <p>
 * GlideConfiguration
 */

public class GlideConfiguration implements GlideModule {

    // 需要在AndroidManifest.xml中声明
    // <meta-data
    //    android:name="com.yaphetzhao.glidecatchsimple.glide.GlideConfiguration"
    //    android:value="GlideModule" />
    // 图片缓存最大容量，150M，根据自己的需求进行修改
   public static final int GLIDE_CATCH_SIZE = 300 * 1000 * 1000;

    // 图片缓存子目录
    public static final String GLIDE_CARCH_DIR = "image_catch";
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,GLIDE_CARCH_DIR,GLIDE_CATCH_SIZE));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
