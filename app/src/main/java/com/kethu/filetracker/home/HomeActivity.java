package com.kethu.filetracker.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.kethu.filetracker.R;
import com.kethu.filetracker.base.BaseActivity;
import com.kethu.filetracker.node_type.NodeTypeActivity;
import com.kethu.filetracker.office.OfficeActivity;
import com.kethu.filetracker.status_designation.StatusDesignationActivity;

import static com.kethu.filetracker.helpers.ViewHelper.removeShiftMode;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    ViewPager mViewPager;
    MenuItem mCurrentMenuItemSelected;
    private BottomNavigationView mBottomNavigationView;
    private Menu mMenu;
    private int mCurretPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setToolbar();
        navDrawerOnCreate();
        setBottomNavigation();
        setupViewPager();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add:
                break;
            case R.id.status_menu:
                Intent intent = new Intent(HomeActivity.this, StatusDesignationActivity.class);
                intent.putExtra("isStatus", true);
                startActivity(intent);
                break;
            case R.id.designation_menu:
                Intent intent1 = new Intent(HomeActivity.this, StatusDesignationActivity.class);
                intent1.putExtra("isStatus", false);
                startActivity(intent1);

                break;

            case R.id.office:
                Intent offic = new Intent(HomeActivity.this, OfficeActivity.class);
                startActivity(offic);

                break;
            case R.id.node_type:
                Intent nodeTYpe = new Intent(HomeActivity.this, NodeTypeActivity.class);
                startActivity(nodeTYpe);

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setBottomNavigation() {
        mBottomNavigationView = findViewById(R.id.navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        removeShiftMode(mBottomNavigationView);
    }

    private void setupViewPager() {
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.addOnPageChangeListener(this);
        MyViewPageAdapter adapter = new MyViewPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_user:
                mViewPager.setCurrentItem(0);
                mCurretPage=0;

               // setVisibleInvisibleMenuItems(0);
                return true;
            case R.id.navigation_node:
                mViewPager.setCurrentItem(1);
               // setVisibleInvisibleMenuItems(1);
                mCurretPage=1;

                return true;
            case R.id.navigation_group:
                mViewPager.setCurrentItem(2);
             //   setVisibleInvisibleMenuItems(2);
                mCurretPage=2;

                return true;
            case R.id.navigation_file:
                mViewPager.setCurrentItem(3);
               // .//setVisibleInvisibleMenuItems(3);
                mCurretPage=3;

                return true;
        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (mCurretPage) {
            case 0:
                mMenu.findItem(R.id.designation_menu).setVisible(true);
                mMenu.findItem(R.id.status_menu).setVisible(true);
                break;
            case 1:
                mMenu.findItem(R.id.designation_menu).setVisible(false);
                mMenu.findItem(R.id.status_menu).setVisible(false);
                mMenu.findItem(R.id.office).setVisible(true);
                mMenu.findItem(R.id.node_type).setVisible(true);
                mMenu.findItem(R.id.file_flow).setVisible(false);

                break;
            case 2:

                mMenu.findItem(R.id.designation_menu).setVisible(false);
                mMenu.findItem(R.id.status_menu).setVisible(false);
                mMenu.findItem(R.id.office).setVisible(false);
                mMenu.findItem(R.id.node_type).setVisible(false);
                mMenu.findItem(R.id.file_flow).setVisible(false);
                //mMenu.findItem(R.id.file_flow).sets(false);


                break;
            case 3:
                mMenu.findItem(R.id.designation_menu).setVisible(false);
                mMenu.findItem(R.id.status_menu).setVisible(false);
                mMenu.findItem(R.id.office).setVisible(false);
                mMenu.findItem(R.id.node_type).setVisible(false);
                mMenu.findItem(R.id.file_flow).setVisible(true);

                break;
        }

        return true;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {


        if (mCurrentMenuItemSelected != null) {
            mCurrentMenuItemSelected.setChecked(false);
        } else {
            mBottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        mCurretPage=position;
        onPrepareOptionsMenu(mMenu);
        mBottomNavigationView.getMenu().getItem(position).setChecked(true);
        mCurrentMenuItemSelected = mBottomNavigationView.getMenu().getItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
