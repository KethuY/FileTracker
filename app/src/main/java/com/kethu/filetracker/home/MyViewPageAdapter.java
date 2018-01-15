package com.kethu.filetracker.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kethu.filetracker.file.FileFragment;
import com.kethu.filetracker.node.NodeFragment;
import com.kethu.filetracker.target_group.TargetGroupFragment;
import com.kethu.filetracker.user.UserFragment;

/**
 * Created by satya on 07-Jan-18.
 */

public class MyViewPageAdapter extends FragmentPagerAdapter {

    MyViewPageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new UserFragment();
            case 1:
                return new NodeFragment();
            case 2:
                return new TargetGroupFragment();
            case 3:
                return new FileFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }


}