package com.example.achuan.iflytek_test;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.achuan.iflytek_test.base.SimpleActivity;
import com.example.achuan.iflytek_test.ui.main.fragment.SpeechFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SimpleActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActionBarDrawerToggle mDrawerToggle;//左侧部分打开按钮

    //fragment的引用变量
    SpeechFragment mSpeechFragment;


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        //初始化toolbar
        setToolBar(mToolbar, "语音助手");
        /***因为该为主界面,需要额外设置home按钮***/
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(
                this,            /* host Activity */
                mDrawerLayout,  /* DrawerLayout object */
                mToolbar,
                R.string.navigation_drawer_open,/* "open drawer" description for accessibility */
                R.string.navigation_drawer_close);/* "close drawer" description for accessibility */
        mDrawerLayout.addDrawerListener(mDrawerToggle);//添加点击监听事件
        //将DrawerToggle中的drawer图标,设置为ActionBar中的Home-Button的Icon
        mDrawerToggle.syncState();

        //初始化创建fragment实例对象
        mSpeechFragment = new SpeechFragment();
        /***将需要显示的fragment全部装载到当前activity中***/
        //越靠前添加的fragment越在上面显示
        loadMultipleRootFragment(R.id.fl_main_content, 0,//容器id和目标位置
                mSpeechFragment);//添加进去的fragment实例对象
        //设置默认选中的item项
        mNavView.setCheckedItem(R.id.backup);
        mNavView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        /***当系统版本小于5.0时,避免NavView不延伸到状态栏,需进行如下设置***/
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            mDrawerLayout.setClipToPadding(false);
        }
    }


    //HomeAsUp按钮的id永远都是android.R.id.home
    @Override//Toolbar上按钮的选择监听事件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case android.R.id.home:
                //点击按钮打开显示左边的菜单,这里设置其显示的行为和XML中定义的一致
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;*/
            default:
                break;
        }
        return true;
    }

    /***
     * navigation的item点击事件监听方法实现
     ***/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                //针对不同的item实现不同的逻辑处理
                break;
            default:
                break;
        }
        //点击左侧菜单栏中的item后将滑动菜单关闭
        mDrawerLayout.closeDrawers();
        return true;
    }

}
