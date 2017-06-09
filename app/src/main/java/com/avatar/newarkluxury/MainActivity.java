package com.avatar.newarkluxury;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private ViewPager mViewPager;
    public static final String ACTION_SLIDE_START = "com.avatar.newark.slide_start";
    public static final String ACTION_SLIDE_END = "com.avatar.newark.slide_end";

    private BaseFragment[] fragments = new BaseFragment[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getWindow().setAttributes(lp);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return getFragment(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        sendBroadcast(new Intent(ACTION_SLIDE_END));
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        sendBroadcast(new Intent(ACTION_SLIDE_START));
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    default:
                }
            }
        });
    }

    private Fragment getFragment(int position) {
        if (position == 0) {
            if (fragments[0] == null) {
                fragments[0] = new ControllerFragment();
            }
            return fragments[0];
        } else if (position == 1) {
            if (fragments[1] == null) {
                fragments[1] = new CameraFragment();
            }
            return fragments[1];
        } else {
            Log.e(TAG, "the position of fragment is ERROR!!!!");
            return null;
        }
    }

    public void setPageAt(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //点击返回按键时activity会销毁再重启。所以屏蔽掉返回按键。
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void updateLanguage(boolean isChinese) {
        for (BaseFragment fragment : fragments) {
            if (isChinese) {
                fragment.setTextToChinese();
            } else {
                fragment.setTextToEnglish();
            }
        }
    }
}
