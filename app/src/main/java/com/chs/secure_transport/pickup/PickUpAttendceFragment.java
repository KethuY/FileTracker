package com.chs.secure_transport.pickup;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chs.secure_transport.R;
import com.chs.secure_transport.helpers.CommonConstants;
import com.chs.secure_transport.helpers.DateHelper;
import com.chs.secure_transport.helpers.OnItemClickListener;
import com.chs.secure_transport.helpers.SharedPrefHelper;
import com.chs.secure_transport.restapi.ProcessAsyncRequest;
import com.chs.secure_transport.restapi.RequestObject;
import com.chs.secure_transport.student.Student;
import com.chs.secure_transport.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.chs.secure_transport.utils.AppUtils.SESSION_ID;


public class PickUpAttendceFragment extends Fragment implements OnItemClickListener ,ProcessAsyncRequest.onResponseListener{

    /* private OnFragmentInteractionListener mListener;
     */
    List<Student> mStudnets;
    PickUpAttendanceAdapter adapter;
    private MenuItem mSaveMenu;
    private OnStudentPickUpStatusSavedListener mListner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_attendce, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recylerView);
        TextView routePointName = view.findViewById(R.id.route_name_tv);
        TextView pickTime = view.findViewById(R.id.starttime);
        pickTime.setText(String.format("PickUp: %s", DateHelper.getCurrentTime()));
        TextView dropTime = view.findViewById(R.id.endtime);
        dropTime.setText(String.format("Drop: %s", DateHelper.getCurrentTime()));
        setHasOptionsMenu(true);
        mStudnets = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setName("Satya " + (i + 1));
            student.setClassName("Class 4 - Sec" + (i + 1));
            student.setStatus("PRESENT");
            student.setId("GCMR180002");
            mStudnets.add(student);
        }

        adapter = new PickUpAttendanceAdapter(mStudnets, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
    }


    @Override
    public void onItemClick( final int position, View view) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getActivity(), view);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.status, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                mStudnets.get(position).setStatus(item.getTitle().toString());
                adapter.notifyItemChanged(position);
                return true;
            }
        });

        popup.show();
        
        /* try {
            RequestObject requestObject = new RequestObject();
            requestObject.setRequestId("");
            requestObject.setServiceId("CS_GET_ROUTE_WISE_STUDENTS");
            requestObject.setDomain("CS_DAO");
            requestObject.setUserType("student_transport");
            requestObject.setGetOrPost(CommonConstants.POST);

            JSONObject authentication_obj = new JSONObject();
            authentication_obj.accumulate("user_id", SharedPrefHelper.getUserId(getActivity()));
            authentication_obj.accumulate("session_id", SESSION_ID);
            requestObject.setLogin(authentication_obj.toString());

            JSONObject crt_obj = new JSONObject();
            crt_obj.accumulate("inquiry_no_", AppUtils.INQUIRY_NO);
            crt_obj.accumulate("admission_no_", SharedPrefHelper.getUserId(getActivity()));
            crt_obj.accumulate("user_id_", SharedPrefHelper.getUserId(getActivity()));
            crt_obj.accumulate("ay_code_", AppUtils.ACCEDMIC_YEAR);
            crt_obj.accumulate("branch_code_", AppUtils.BRANCH_CODE);
            crt_obj.accumulate("class_code_", AppUtils.GROUP);
            crt_obj.accumulate("route_no", "");
            crt_obj.accumulate("vehicle_no", "");
            requestObject.setCriteria(crt_obj.toString());
            new ProcessAsyncRequest(requestObject, this).execute();

            /*admission_no_
student_name_
father_name_
mobile_no_
ay_code_
vehicle_no_
branch_code_
class_code_
section_code_
route_point_
pickup_time_
drop_off_time_
route_point_
route_point_name_*/

    }

    private void saveStudentPickUp() {
        try {
            RequestObject requestObject = new RequestObject();
            requestObject.setRequestId("");
            requestObject.setServiceId("CS_REQ_SAVE_STUDENT_TRANSPORT_STATUS");
            requestObject.setDomain("CS_DAO");
            requestObject.setUserType("student_transport_status ");
            requestObject.setGetOrPost(CommonConstants.SAVE);

            JSONObject authentication_obj = new JSONObject();
            authentication_obj.accumulate("user_id", SharedPrefHelper.getUserId(getActivity()));
            authentication_obj.accumulate("session_id", SESSION_ID);
            requestObject.setLogin(authentication_obj.toString());

            JSONObject crt_obj = new JSONObject();
            crt_obj.accumulate("monitor_id_", SharedPrefHelper.getUserId(getActivity()));
            crt_obj.accumulate("inquiry_no_", AppUtils.INQUIRY_NO);
            crt_obj.accumulate("admission_no_", SharedPrefHelper.getUserId(getActivity()));
            crt_obj.accumulate("user_id_", SharedPrefHelper.getUserId(getActivity()));
            crt_obj.accumulate("ay_code_", AppUtils.ACCEDMIC_YEAR);
            crt_obj.accumulate("branch_code_", AppUtils.BRANCH_CODE);
            crt_obj.accumulate("class_code_", AppUtils.GROUP);
            crt_obj.accumulate("route_no_", AppUtils.GROUP);
            crt_obj.accumulate("route_point_", AppUtils.GROUP);
            crt_obj.accumulate("date_", AppUtils.GROUP);
            crt_obj.accumulate("pickup_time_", AppUtils.GROUP);
            crt_obj.accumulate("remarks_", AppUtils.GROUP);
            crt_obj.accumulate("pickup_status_", AppUtils.GROUP);
            requestObject.setCriteria(crt_obj.toString());
            new ProcessAsyncRequest(requestObject, this).execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.common_menu,menu);
         mSaveMenu =menu.findItem(R.id.save_menu);
       // mSaveMenu.setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.save_menu){
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnStudentPickUpStatusSavedListener){
            mListner= (OnStudentPickUpStatusSavedListener) context;
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        mListner.savedSuccessfully();

        if (mSaveMenu!=null) {
            mSaveMenu.setVisible(false);
        }
    }

    @Override
    public void onFailure(JSONObject jsonObject) {

    }

    public interface OnStudentPickUpStatusSavedListener {
        void savedSuccessfully();
    }

}
