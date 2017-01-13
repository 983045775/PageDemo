package iflytek.pagedemo.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;

/**
 * 通用性极高的ViewHolder
 */
public class ViewHolder {
    private final static int CONVER_VIEW_TAG_KEY = 0x12345678;
    // 用于存储listView item的容器
    private SparseArray<View> mViews;
    // item根view
    private View mConvertView;

    protected Context mContext;

    private int position;
    private ImageView iv;

    public ViewHolder(Context context, ViewGroup parent, int layoutId,
                      int position) {
        Log.d("Layout_Id", "Layout_Id" + String.valueOf(layoutId));
        this.mViews = new SparseArray<View>();
        this.mConvertView = LayoutInflater.from(context).inflate(layoutId,
                parent, false);
        // this.mConvertView.setTag(this);
        this.mConvertView.setTag(CONVER_VIEW_TAG_KEY, this);
        this.mContext = context;
        this.position = position;
    }

    /**
     * 获取一个viewHolder
     *
     * @param context     context
     * @param convertView view
     * @param parent      parent view
     * @param layoutId    布局资源id
     * @param position    索引
     * @return
     */
    public static ViewHolder getViewHolder(Context context, View convertView,
                                           ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }

        return (ViewHolder) convertView.getTag(CONVER_VIEW_TAG_KEY);
    }

    public int getPosition() {
        return this.position;
    }

    /**
     * 将某个控件的点击事件捕获，通过adapter统一将事件传递出去 adapter并不处理业务逻辑，view和controller处理
     *
     */
    public void setItemOnClickListener(int viewId, Object tagobj,
                                       OnClickListener l) {
        getView(viewId).setTag(tagobj);
        getView(viewId).setOnClickListener(l);
    }

    public static final int ITEM_TAG = 101;

    /**
     * 将某个控件的点击事件捕获，通过adapter统一将事件传递出去，通过IOnItemClickListener回调进行业务逻辑处理
     *
     * @param tagobj            模型
     * @param longClickListener 长按事件监听接口
     */
    public void setItemOnLongClickListener(Object tagobj,
                                           OnLongClickListener longClickListener, int...
                                                   idAndTags) {
        getView(idAndTags[0]).setTag(tagobj);

        if (idAndTags.length > 1) {
            getView(idAndTags[0]).setTag(idAndTags[1], this);
        }
        getView(idAndTags[0]).setOnLongClickListener(longClickListener);
    }

    // 通过一个viewId来获取一个view
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    // 返回viewHolder的容器类
    public View getConvertView() {
        return this.mConvertView;
    }

    // 给TextView设置文字
    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
    }

    public void setTag(int viewId, String tag) {
        View v = getView(viewId);
        v.setTag(tag);
    }

    // 给TextView设置文字
    public void setText(int viewId, SpannableString text) {
        TextView tv = getView(viewId);
        tv.setText(text);
    }

    // 给TextView设置文字
    public void setText(int viewId, int textRes) {
        TextView tv = getView(viewId);
        tv.setText(textRes);
    }

    public void setProgressBar(int viewId, int textRes) {
        ProgressBar bar = getView(viewId);
        bar.setProgress(textRes);
    }

    // 给TextView 设置字体颜色
    public void setTextColor(int viewId, int textColor) {
        TextView tv = getView(viewId);
        tv.setTextColor(textColor);
    }

    // 给TextView设置背景色
    public void setTextBackgroundColor(int viewId, int textColor) {
        TextView tv = getView(viewId);
        tv.setBackgroundColor(textColor);
    }

    public void setWidthOrHeight(int viewId, int width, int height) {
        LinearLayout lin = getView(viewId);
        LayoutParams linearParams = (LayoutParams) lin
                .getLayoutParams();
        //linearParams.width = width;
        linearParams.height = height;
        lin.setLayoutParams(linearParams);
    }

    /**
     * 设置背景图片
     *
     * @param viewId
     * @param drawableId
     */
    @SuppressLint("NewApi")
    public void setTextBackground(int viewId, int drawableId) {
        TextView tv = getView(viewId);
        tv.setBackground(mContext.getResources().getDrawable(drawableId));
    }

    // 给ImageView设置背景色
    public void setImageViewBackgroundColor(int viewId, int imageColor) {
        ImageView tv = getView(viewId);
        tv.setBackgroundResource(imageColor);
    }

    // 给TextView 上下左右的图片
    public void setCompoundDrawables(int viewId, Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        TextView iv = getView(viewId);
        iv.setCompoundDrawables(left, top, right, bottom);
    }

    public void setText(int viewId, String text, int emptyRes) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            tv.setText(emptyRes);
        } else {
            tv.setText(text);
        }
    }

    public void setText(int viewId, String text, String emptyText) {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)) {
            tv.setText(emptyText);
        } else {
            tv.setText(text);
        }
    }

    // 丁伟峰
    public void setVisibility(int viewId, int visibility) {
        View iv = getView(viewId);
        iv.setVisibility(visibility);
    }

    // 给ImageView设置图片资源
    public void setImageResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);
    }

    // 给ImageView设置图片资源
    public void setImageResource(Context context, int viewId, int resId) {
        ImageView iv = getView(viewId);
        Bitmap bitmap = readBitMap(context, resId);
        iv.setImageBitmap(bitmap);
		/*if (bitmap != null && (!bitmap.isRecycled()))  
		{  
			bitmap.recycle();
			bitmap = null; 
		}*/
        System.gc();
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
/*

    /
     * 使用imageloader加载一个图片

    public void setImageForUrl(int viewId, String imgUrl) {
        iv = getView(viewId);
        if (iv.getTag() == null || !iv.getTag().equals(imgUrl)) {
            iv.setImageResource(ImgLoaderConfig.getLoadFileImageId());
        }

        // 在这里做一个判断，如果Tag存在则不加载
        if (iv.getTag() == null || !iv.getTag().equals(imgUrl)) {
            ImageLoader.getInstance().displayImage(imgUrl, iv,
                    ImgLoaderConfig.getLoaderOption(), null);
            iv.setTag(imgUrl);
        }

    }

    // 显示图片和视频缩略图
    public void setImageOrVideoForUrl(int viewId, String imgUrl) {
        iv = getView(viewId);
        if (iv.getTag() == null || !iv.getTag().equals(imgUrl)) {
            iv.setImageResource(ImgLoaderConfig.getLoadFileImageId());
        }
        // 在这里做一个判断，如果Tag存在则不加载
        if (iv.getTag() == null || !iv.getTag().equals(imgUrl)) {
            ImageLoader.getInstance().displayImage(imgUrl, iv,
                    ImgLoaderConfig.getMaterialLoaderOption(), null);
            iv.setTag(imgUrl);
        }
        System.gc();
    }

    // 显示图片和视频缩略图
    public void setMeetImageUrl(int viewId, String imgUrl) {
        iv = getView(viewId);
        if (iv.getTag() == null || !iv.getTag().equals(imgUrl)) {
            iv.setImageResource(ImgLoaderConfig.getLoadFileImageId());
        }
        // 在这里做一个判断，如果Tag存在则不加载
        if (iv.getTag() == null || !iv.getTag().equals(imgUrl)) {
            ImageLoader.getInstance().displayImage(imgUrl, iv,
                    ImgLoaderConfig.getDrawIdLoaderOption(R.drawable.hp_ic_pic_default), null);
            iv.setTag(imgUrl);
        }
    }

*/
    // 给TextView 设置字体颜色
    public void setCheckbox(int viewId, boolean isCheck) {
        CheckBox tv = getView(viewId);
        tv.setChecked(isCheck);
    }


}
