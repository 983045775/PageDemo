package iflytek.pagedemo.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iflytek.pagedemo.R;
import iflytek.pagedemo.adapter.UserAdapter;
import iflytek.pagedemo.adapter.base.CommonAdapter;
import iflytek.pagedemo.controller.UserController;
import iflytek.pagedemo.model.UserInfo;

/**
 * view层  不应当在此编写获取数据处理数据的方法,应当调用controller的方法,
 * ACTIVITY专注处理页面跳转 组合View  管理生命周期的功能
 * 如果一个Activity的行数超过1000行就需要拆分出去,将业务逻辑分散到其他地方
 */
public class UserActivity extends Activity implements CommonAdapter.IOnItemClickListener {

    @BindView(R.id.listview)
    public ListView mLvListView;

    private List<UserInfo> mDatas;//数据
    private UserAdapter mAdapter;

    /***
     * 不要将所有的变量初始化都放在onCreate函数中,需要按照模块或者业务逻辑拆分出init方法,如
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注入
        ButterKnife.bind(this);
        //初始化数据
        initDatas();
        //绑定事件
        bindEvent();
    }

    private void initDatas() {
        UserController.getInstance().getContentDatas(new UserController.Responseback() {
            @Override
            public void onSuccess(List<UserInfo> result) {
                //获取到了成功数据
                mDatas = result;
                //初始化页面
                initView();
            }
        });
    }


    private void initView() {
        //创建listview的adapter
        mAdapter = new UserAdapter(this, R.layout.item_listview);
        //adapter设置数据
        mAdapter.setItems(mDatas);
        //listview添加adapter
        mLvListView.setAdapter(mAdapter);
    }

    private void bindEvent() {
        mAdapter.setItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, Object t) {
        //处理点击的item !!注意t就是adapter中你传入的model模型
        Toast.makeText(this, "点击了" + ((UserInfo) t).name, Toast.LENGTH_SHORT).show();
    }
}
