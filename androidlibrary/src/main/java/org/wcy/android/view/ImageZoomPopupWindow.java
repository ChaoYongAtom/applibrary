package org.wcy.android.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.chrisbanes.photoview.PhotoView;

import org.wcy.android.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片放大缩小
 * Created by wcy on 2017/7/24.
 */

public class ImageZoomPopupWindow extends BottomPushPopupWindow {
    List<String> datas;
    HackyViewPager viewPager;
    TextView tv_position;
    private int position = 0;
    public ImageZoomPopupWindow(Activity activity, List<String> str, int position) {
        super(activity, str, true);
        this.position = position;
    }

    @Override
    protected View generateCustomView(Object o) {
        datas = (ArrayList) o;
        View popupView = View.inflate(bActivity, R.layout.activity_image_zoom, null);
        viewPager=popupView.findViewById(R.id.view_pager);
        tv_position=popupView.findViewById(R.id.tv_position);
        viewPager.setAdapter(new SamplePagerAdapter(bActivity, datas));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_position.setText((position + 1) + "/" + datas.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return popupView;
    }

    class SamplePagerAdapter extends PagerAdapter {
        private List<String> datas;
        private Context context;

        public SamplePagerAdapter(Context context, List<String> datas) {
            this.datas = datas;
            this.context = context;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            loadImage(context, datas.get(position), R.mipmap.bg_estate_palceholder, R.mipmap.bg_estate_palceholder, photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
    private void loadImage(Context context, String url, int erroImg, int emptyImg, final ImageView iv) {
        //原生 API
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(emptyImg)
                .error(erroImg)
                .dontAnimate()
                .into(new SimpleTarget<Bitmap>(){

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        iv.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        iv.setImageDrawable(errorDrawable);
                    }
                });

    }
    public void show() {
        viewPager.setCurrentItem(position);
        tv_position.setText((position + 1) + "/" + datas.size());
        setAnimation(R.style.Animations_ZoomPush);
        show(bActivity);
    }

}
