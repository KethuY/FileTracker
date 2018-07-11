package com.chs.secure_transport.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chs.secure_transport.R;
import com.chs.secure_transport.base.BaseActivity;
import com.chs.secure_transport.helpers.CommonConstants;
import com.chs.secure_transport.helpers.CommonUtils;
import com.chs.secure_transport.helpers.OnItemClickListener;
import com.chs.secure_transport.restapi.ProcessAsyncRequest;
import com.chs.secure_transport.restapi.RequestObject;
import com.chs.secure_transport.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.chs.secure_transport.utils.AppUtils.SESSION_ID;


public class StudentsTransportActivity extends BaseActivity implements OnItemClickListener, ProcessAsyncRequest.onResponseListener {

    RecyclerView mRecylerView;
    List<Student> mAlerts;
    private LinearLayout mNoNotificationLL;
    private StudentAdapter mNotificationAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onStart() {
        WANT_TO_SHOW_HOME_ICON=true;
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Students Transport");
        toolbar.setNavigationIcon(ContextCompat.getDrawable(StudentsTransportActivity.this,R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mNoNotificationLL = findViewById(R.id.no_data_ll);
        mNoNotificationLL.setVisibility(View.GONE);
        mRecylerView = findViewById(R.id.recylerView);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        // getAllStudnets();


        mAlerts = new ArrayList<>();

        for (int i = 0; i < 5; i++) {

            Student stu = new Student();
            stu.setRouteName("RouteName");
            stu.setHeader(true);
            mAlerts.add(stu);

            for (int j = 0; j < 5; j++) {
                Student student = new Student();
                student.setRouteName("RouteName");
                student.setName("Satya");
                student.setClassName("Class 3-Sec c");
                mAlerts.add(student);
            }

        }

        mNotificationAdapter = new StudentAdapter(mAlerts);
        mRecylerView.setAdapter(mNotificationAdapter);
        mRecylerView.setLayoutManager(new LinearLayoutManager(StudentsTransportActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecylerView.setHasFixedSize(true);


    }

    private void getAllStudnets() {


        try {
            RequestObject requestObject = new RequestObject();
            requestObject.setRequestId("");
            requestObject.setServiceId("CS_GET_ROUTE_WISE_STUDENTS");
            requestObject.setDomain("CS_DAO");
            requestObject.setUserType("student_transport");
            requestObject.setGetOrPost(CommonConstants.POST);

            JSONObject authentication_obj = new JSONObject();
            authentication_obj.accumulate("user_id", "cs_admin");
            authentication_obj.accumulate("session_id", SESSION_ID);
            requestObject.setLogin(authentication_obj.toString());

            JSONObject crt_obj = new JSONObject();
            crt_obj.accumulate("admission_no_", AppUtils.ADM_NO);
            crt_obj.accumulate("route_no_", AppUtils.ADM_NO);
            crt_obj.accumulate("ay_code_", AppUtils.ADM_NO);
            crt_obj.accumulate("branch_code_", AppUtils.ADM_NO);
            crt_obj.accumulate("vehicle_no_", AppUtils.ADM_NO);


/*Response:
	student_transport_status_ref_id_
	admission_no_
	student_name_
	father_name_
	mobile_no_
	ay_code_
	branch_code_
	class_code_
	section_code_
	route_point_
	route_point_name_
	pickup_time_
	pickup_status_
	drop_off_time_
	drop_off_status_
	vehicle_no_
	driver_name_
	cleaner_name_*/

            requestObject.setCriteria(crt_obj.toString());
            new ProcessAsyncRequest(requestObject, this).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onItemClick(int position, View view) {

        switch (view.getId()) {
            //// case R.id.delete_iv:
            //      mAlerts.remove(position);
            //   break;
            case R.id.checkbox:
                boolean isChecked = mAlerts.get(position).isChecked();
                mAlerts.get(position).setChecked(!isChecked);
                int count = 0;
                for (int i = 0; i < mAlerts.size(); i++) {

                    if (mAlerts.get(i).isChecked()) {
                        count++;
                    }
                }

                break;
        }

        try {
            mNotificationAdapter.notifyItemChanged(position);
        } catch (Exception e) {
            Log.e("TAG", "Exception" + e.getMessage());
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        Log.e("NA", "jsonObject:" + jsonObject);
        mNoNotificationLL.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(false);
        try {

            {
                JSONObject jsonObject1 = jsonObject.getJSONObject("CS_SEARCH_SYSTEM_NOTIFICATION");

                if (jsonObject1 != null) {
                    try {

                        JSONObject tmp = jsonObject.getJSONObject("collection_");

                        Iterator<String> keys = tmp.keys();

                        boolean isSingleObj = false;
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONObject child_obj = null;
                            try {
                                child_obj = tmp.getJSONObject(key);
                            } catch (JSONException e) {

                                child_obj = tmp;
                                isSingleObj = true;
                            }

                            if (child_obj != null) {
                                String app_id_ = CommonUtils.get_json_string(child_obj, "app_id_");
                                String date_ = CommonUtils.get_json_string(child_obj, "date_");
                                String id_ = CommonUtils.get_json_string(child_obj, "id_");
                                String message_ = CommonUtils.get_json_string(child_obj, "message_");
                                String sender_ = CommonUtils.get_json_string(child_obj, "sender_");
                                String time_ = CommonUtils.get_json_string(child_obj, "time_");

                                Student alerts = new Student();
                                alerts.setId(app_id_);
                                alerts.setName(date_);
                                alerts.setId(id_);
                                //   alerts.setMessage(message_);
                                // alerts.setSender(sender_);
                                alerts.setTime(time_);
                                mAlerts.add(alerts);
                            }

                            if (isSingleObj)
                                break;

                        }


                        if (mAlerts == null || mAlerts.size() == 0) {
                            mNoNotificationLL.setVisibility(View.VISIBLE);
                            // mSelectAll.setVisibility(View.GONE);
                        }


                        mNotificationAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        JSONObject jsonObject2 = jsonObject.getJSONObject("CS_DEL_SYSTEM_NOTIFICATION");

                        if (jsonObject2 != null)
                            getAllStudnets();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(JSONObject jsonObject) {
        Log.e("NA", "onFailure jsonObject:" + jsonObject);

        mSwipeRefreshLayout.setRefreshing(false);
        mNoNotificationLL.setVisibility(View.VISIBLE);
    }
}
