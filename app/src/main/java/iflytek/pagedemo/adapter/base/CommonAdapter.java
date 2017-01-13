package iflytek.pagedemo.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import iflytek.pagedemo.holder.ViewHolder;


/**
 * 通用的CommonAdapter 此类中不处理业务逻辑，只提供视图的一般方法
 * 
 * @author 刘泾铭
 * 
 */
public abstract class CommonAdapter<T> extends BaseAdapter implements
		OnClickListener, OnLongClickListener {
	protected LayoutInflater mInflater;
	protected Context mContext;
	private List<T> mDatas;
	private int mLayoutId;
	private IOnItemClickListener mitemLickListener;

	/**
	 * 声明一个长按事件监听接口
	 * 
	 * @author dacui
	 */
	private IOnItemLongClickListener mItemLongClickLitener;

	public IOnItemClickListener getItemClickListener() {
		return mitemLickListener;
	}

	public void setItemClickListener(IOnItemClickListener mListener) {
		this.mitemLickListener = mListener;
	}

	/**
	 * 注册长按监听接口 </br> add by dacui
	 * 
	 * @param mListener
	 */
	public void setItemLongClickListener(IOnItemLongClickListener mListener) {
		this.mItemLongClickLitener = mListener;
	}

	/**
	 * 长按事件回调
	 * 
	 * @author dacui
	 * 
	 */
	public interface IOnItemLongClickListener {
		public void onItemLongClick(View view, Object t);
	}

	/**
	 * item长按
	 * 
	 * @author dacui
	 * @param v
	 */
	public void itemLongClick(View v) {
		if (mItemLongClickLitener != null) {
			mItemLongClickLitener.onItemLongClick(v, v.getTag());
		}

	}

	@Override
	public void onClick(View v) {
		itemClick(v);
	}

	@Override
	public boolean onLongClick(View v) {
		itemLongClick(v);
		return true;
	}

	public interface IOnItemClickListener {
		 void onItemClick(View view, Object t);
	}

	public void itemClick(View v) {
		if (mitemLickListener != null)
			mitemLickListener.onItemClick(v, v.getTag());
	}

	public CommonAdapter(Context context, int layoutId) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mDatas = new ArrayList<T>();
		this.mLayoutId = layoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder vh = ViewHolder.getViewHolder(this.mContext,
				convertView, parent, this.mLayoutId, position);
		convert(vh, getItem(position));
		return vh.getConvertView();
	}

	public List<T> getDatas() {
		return this.mDatas;
	}

	// 获取ViewHodler
	public ViewHolder getViewHodler(int position, View convertView,
			ViewGroup parent) {

		return ViewHolder.getViewHolder(this.mContext, convertView, parent,
				this.mLayoutId, position);
	}

	// 提供给外部填充实际的显示数据，以及可以一些其他的操作，如：隐藏＝＝
	public abstract void convert(ViewHolder vh, T item);

	public void addItem(T item) {
		checkListNull();
		mDatas.add(item);
		notifyDataSetChanged();
	}

	public void addItem(int location, T item) {
		checkListNull();
		mDatas.add(location, item);
		notifyDataSetChanged();
	}

	public void addItems(List<T> items) {
		checkListNull();
		mDatas.addAll(items);
		notifyDataSetChanged();
	}

	public void setItems(List<T> items) {
		checkListNull();
		mDatas.clear();
		mDatas.addAll(items);
		notifyDataSetChanged();
	}

	public void removeItem(int location) {
		if (mDatas == null && mDatas.isEmpty()) {
			return;
		}
		mDatas.remove(location);
		notifyDataSetChanged();
	}

	public void clear() {
		if (mDatas == null && mDatas.isEmpty()) {
			return;
		}
		mDatas.clear();
		notifyDataSetChanged();
	}

	public void checkListNull() {
		if (mDatas == null) {
			mDatas = new ArrayList<T>();
		}
	}

}
