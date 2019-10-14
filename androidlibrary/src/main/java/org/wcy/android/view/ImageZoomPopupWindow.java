package org.wcy.android.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import org.wcy.android.R;
import org.wcy.android.utils.GlideUtil.GlideImgManager;

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
        viewPager = popupView.findViewById(R.id.view_pager);
        tv_position = popupView.findViewById(R.id.tv_position);
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
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    dismiss();
                }
            });
            GlideImgManager.loadImage(datas.get(position), R.mipmap.bg_estate_palceholder, R.mipmap.bg_estate_palceholder, photoView);
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

    public void show() {
        viewPager.setCurrentItem(position);
        tv_position.setText((position + 1) + "/" + datas.size());
        setAnimation(R.style.Animations_ZoomPush);
        show(bActivity);
    }

}
