package com.kethu.filetracker.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kethu.filetracker.R;
import com.kethu.filetracker.helpers.NetworkHelper;
import com.kethu.filetracker.helpers.ToastHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private static final String NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    int mPosition = -1;
    String mTitle = "";
    Toolbar toolbar;
    TextView toolbartitle;
    private String[] mCountries;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FrameLayout frameLayout;

    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawer, mDrawerListLayout;
    private List<HashMap<String, String>> mList;
    private SimpleAdapter mAdapter;
    final private String COUNTRY = "country";
    final private String FLAG = "flag";
    final private String COUNT = "count";
    int[] mFlags = new int[7];/*{
         *//*   R.drawable.truck_icon,
            R.drawable.ic_nav_my_rides,
            R.drawable.ic_nav_rate_card,
            R.drawable.ic_nav_emergency_contact,
            R.drawable.ic_nav_support,
            R.drawable.about,
            R.drawable.logout*//*
    };*/
    boolean isNavDrawerOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (!NetworkHelper.hasNetworkConnection(BaseActivity.this)) {
                    ToastHelper.noInternet(BaseActivity.this);
                }
            }
        };

    }


    @Override
    public void setContentView(int layoutResID) {
        LinearLayout fullLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.base_drawer_layout, null);
        frameLayout = fullLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(fullLayout);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initialize();
    }


    private void initialize() {
        TextView userMobileNOTV = (TextView) findViewById(R.id.user_role);
        TextView userNameTV = (TextView) findViewById(R.id.user_name);

    }


    protected void navDrawerOnCreate() {

        mCountries = new String[]{
                "Admin",
                "Manage File",
                "Search",
                "Reports",
        };


        mTitle = (String) getTitle();

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawer = (LinearLayout) findViewById(R.id.main_parent_view);
        mDrawerListLayout = (LinearLayout) findViewById(R.id.drawer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbartitle = (TextView) findViewById(R.id.titletool);

        // Each row in the list stores country name, count and flag
        mList = new ArrayList<>();


        for (int i = 0; i < mCountries.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put(COUNTRY, mCountries[i]);
            mList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {COUNTRY};

        int[] to = {R.id.country};

        // Instantiating an adapter to store each items
        // R.layout.nav_drawer_layout defines the layout of each item
        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_layout_list_item, from, to);

        // Getting reference to DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Creating a ToggleButton for NavigationDrawer with drawer event listener
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                highlightSelectedCountry();
                supportInvalidateOptionsMenu();
                isNavDrawerOpened = false;
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle("More Apps From Us");
                supportInvalidateOptionsMenu();
                initialize();
                isNavDrawerOpened = true;
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, final View arg1, int position, long arg3) {
                mDrawerLayout.closeDrawer(mDrawerListLayout);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerList.setAdapter(mAdapter);
        initialize();
    }

    public void highlightSelectedCountry() {
        int selectedItem = mDrawerList.getCheckedItemPosition();

        if (selectedItem > 4)
            mDrawerList.setItemChecked(mPosition, true);
        else
            mPosition = selectedItem;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(NETWORK_CHANGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
