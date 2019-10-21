package com.ruiyun.comm.library.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.ruiyun.comm.library.live.BaseViewModel;
import com.ruiyun.comm.library.widget.ViewPagerCompat;

import org.wcy.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 引导页
 *
 * @param <T>
 * @author wcy
 */
public class GuideBaseActivity<T extends BaseViewModel> extends BaseMActivity<T> {
    public ViewPagerCompat mViewPager;
    private int[] mImgIds;
    private List<ImageView> mImageViews = new ArrayList<ImageView>();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mViewPager = findViewById(R.id.guide_view_pager);
    }

    protected void setImages(int... images) {
        mImgIds = images;
    }

    protected void imageLoad() {
        mImageViews.clear();
        for (int imgId : mImgIds) {
            ImageView imageView = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnClickListener(v -> {
                if (mViewPager.getCurrentItem() == mImageViews.size() - 1) {
                    mViewPager.setVisibility(View.GONE);
                    EnterClick();
                }
            });
            mImageViews.add(imageView);
        }
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mImageViews.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                container.addView(mImageViews.get(position));
                return mImageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    protected void EnterClick() {

    }

    @Override
    public void showError(int state, String msg) {

        Observable<String> observable1=Observable.create(emitter -> emitter.onComplete());
        observable1.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observable1.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                toast(msg);
            }
        });
        Observable.timer(5000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong ->  finishActivity());
    }
}
