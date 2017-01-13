package iflytek.pagedemo.adapter;

import android.content.Context;
import android.view.View;

import iflytek.pagedemo.R;
import iflytek.pagedemo.adapter.base.CommonAdapter;
import iflytek.pagedemo.holder.ViewHolder;
import iflytek.pagedemo.model.UserInfo;

/**
 * adapter应当只包含和view,model转换相关的代码,不承担业务逻辑,一个adapter越简单就越能够被重用
 * 具体的业务逻辑应当抛出给adapter的使用者
 */

public class UserAdapter extends CommonAdapter<UserInfo> {

    public UserAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void convert(ViewHolder vh, final UserInfo item) {
        vh.setText(R.id.item_tv_name, item.name);
        vh.setText(R.id.item_tv_id, item.id);
        vh.setItemOnClickListener(R.id.item_tv_name, item, this);
    }

    @Override
    public void onClick(View v) {
        super.itemClick(v);
    }
}
