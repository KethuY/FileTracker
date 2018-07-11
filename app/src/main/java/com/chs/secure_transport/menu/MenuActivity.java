package com.chs.secure_transport.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;

import com.chs.secure_transport.R;
import com.chs.secure_transport.base.BaseActivity;
import com.chs.secure_transport.drop.StudentDropActivity;
import com.chs.secure_transport.pickup.PickupActivity;
import com.chs.secure_transport.route.RoutesActivity;
import com.chs.secure_transport.route_map.RouteMapActivity;
import com.chs.secure_transport.route_point.AddRoutePointsActivity;
import com.chs.secure_transport.student.StudentsTransportActivity;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends BaseActivity
{
    List<MenuIcon> mHomeIcons = null;

    @Override
    protected void onStart()
    {
        WANT_TO_SHOW_HOME_ICON = false;
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Secure Transport");
        setSupportActionBar(toolbar);
/*
        String studentObj = SharedPrefHelper.getStudentObject(MenuActivity.this);
        if (studentObj != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(studentObj);
                AppUtils.ACCEDMIC_YEAR = CommonUtils.get_json_string(jsonObject, "ay_code_");
                AppUtils.BRANCH_CODE = CommonUtils.get_json_string(jsonObject, "branch_code_");
                AppUtils.INQUIRY_NO = CommonUtils.get_json_string(jsonObject, "inquiry_no_");
                AppUtils.GROUP = CommonUtils.get_json_string(jsonObject, "class_code_");
                AppUtils.NAME = CommonUtils.get_json_string(jsonObject, "first_name_") + " " + CommonUtils.get_json_string(jsonObject, "last_name_");
                //AppUtils.ID = CommonUtils.get_json_string(jsonObject, "id_");
                AppUtils.ADM_NO = CommonUtils.get_json_string(jsonObject, "admission_no_");


            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
*/

        String[] homeTitles =
                {
                    "Pickup",
                    "Drop off",
                    "Transport",
                    "Route Map",
                    "Routes",
                    "Route Points",
                };


        int[] imageId =
                {
                    R.drawable.transport_icon,
                    R.drawable.fee_pay_icon,
                    R.drawable.notification_icon,
                    R.drawable.holidays_icon,
                    R.drawable.time_table_icon,
                    R.drawable.examinatio_icon
                };

        mHomeIcons = new ArrayList<>();

        for (int i = 0; i < homeTitles.length; i++)
        {
            MenuIcon stu = new MenuIcon();
            stu.setName(homeTitles[i]);
            stu.setImageId(imageId[i]);
            mHomeIcons.add(stu);
        }

        final Intent[] intents =
                {
                    new Intent(MenuActivity.this, PickupActivity.class),
                    new Intent(MenuActivity.this, StudentDropActivity.class),
                    new Intent(MenuActivity.this, StudentsTransportActivity.class),//done
                    new Intent(MenuActivity.this, RouteMapActivity.class),//done
                    new Intent(MenuActivity.this, RoutesActivity.class),
                    new Intent(MenuActivity.this, AddRoutePointsActivity.class),
                };

        GridView gridView = findViewById(R.id.grid_view);
        final GridViewAdapter gridViewAdapter = new GridViewAdapter(MenuActivity.this, mHomeIcons);
        gridView.setAdapter(gridViewAdapter);

        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        gridView.setAnimation(anim);
        anim.start();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                boolean isClicked = mHomeIcons.get(position).isClicked();
                mHomeIcons.get(position).setClicked(!isClicked);

                try
                {
                    gridViewAdapter.notifyDataSetChanged();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            startActivity(intents[position]);
                        }

                    }, 1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}
