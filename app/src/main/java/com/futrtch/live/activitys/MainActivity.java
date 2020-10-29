package com.futrtch.live.activitys;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.futrtch.live.R;
import com.futrtch.live.activitys.fragments.LiveListFragment;
import com.futrtch.live.activitys.fragments.MessageFragment;
import com.futrtch.live.activitys.fragments.MineFragment;
import com.futrtch.live.activitys.fragments.VodListFragment;
import com.futrtch.live.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mDataBinding;
    List<Fragment> mFragments = new ArrayList<>();
    private int lastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setBottom();
        mFragments.add(new LiveListFragment());
        mFragments.add(new VodListFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new MineFragment());
        setFragmentPosition(0);
    }

    private void setBottom() {
        // 解决当item大于三个时，非平均布局问题
//        BottomNavigationViewHelper.disableShiftMode(mDataBinding.bvBottomNavigation);
        mDataBinding.bvBottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_live:
                    setFragmentPosition(0);
                    break;
                case R.id.menu_replay:
                    setFragmentPosition(1);
                    break;
                case R.id.menu_message:
                    setFragmentPosition(2);
                    break;
                case R.id.menu_mine:
                    setFragmentPosition(3);
                    break;
                default:
                    break;
            }
            return true;
        });

    }

    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.contentPanel, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {


            //不执行父类点击事件
            return true;
        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }
}