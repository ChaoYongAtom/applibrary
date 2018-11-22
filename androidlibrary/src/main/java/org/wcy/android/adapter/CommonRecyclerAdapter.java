package org.wcy.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 为所有ListView\GridView等提供通用adapter
 *
 * @author wcy
 * @date 2014年9月2日
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewRecyclerHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mDatas;
    protected int mItemLayoutId;

    public CommonRecyclerAdapter(Context context, List<T> datas, int itemLayoutId) {
        super();
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = datas;
        this.mItemLayoutId = itemLayoutId;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewRecyclerHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewRecyclerHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ViewRecyclerHolder(mFooterView);
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(mItemLayoutId, viewGroup, false);
        ViewRecyclerHolder vh = new ViewRecyclerHolder(view);
        vh.setItemCount(getItemCount());
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewRecyclerHolder viewHolder, int position) {
        if (position <getItemCount()) {
            if(getHeaderView() != null&&getFooterView()==null){
                if (position != 0) {
                    this.convert(viewHolder, this.mDatas.get(position - 1));
                }
            }else if(getHeaderView() == null&&getFooterView()!=null){
                if(position!=getItemCount()-1){
                    this.convert(viewHolder, this.mDatas.get(position));
                }
            }else if(getHeaderView() != null&&getFooterView()!=null){
                if (position > 0&&position!=getItemCount()-1) {
                    this.convert(viewHolder, this.mDatas.get(position - 1));
                }
            }else{
                this.convert(viewHolder, this.mDatas.get(position));
            }
        }
//        if (position <getItemCount()) {
//            if (getHeaderView() != null&&getFooterView()==null) {
//                if (position != 0) {
//                    this.convert(viewHolder, this.mDatas.get(position - 1));
//                }
//            } else {
//                this.convert(viewHolder, this.mDatas.get(position));
//            }
//
//        }
    }

    public void updateList(List<T> datas) {
        mDatas.clear();
        if (datas != null) {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();

    }

    /**
     * 为item设置值
     *
     * @param helper
     * @param item
     */
    public abstract void convert(ViewRecyclerHolder helper, T item);

    //获取数据的数量
    @Override
    public int getItemCount() {
        return  getHeaderView() != null && this.getFooterView() != null && this.mDatas!=null ? this.mDatas.size() + 2 : (getHeaderView() == null && this.getFooterView() == null ? this.mDatas.size() : this.mDatas.size() + 1);
    }

    protected int position;


    private static int TYPE_NORMAL = -1;
    private static int TYPE_HEADER = 0;
    private static int TYPE_FOOTER = 1;

    private View mHeaderView;
    private View mFooterView;


    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    *
     */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0 && mHeaderView != null) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }


}
