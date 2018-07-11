package com.chs.secure_transport.fcm;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chs.secure_transport.helpers.CommonConstants;
import com.chs.secure_transport.helpers.CommonUtils;
import com.chs.secure_transport.helpers.DateHelper;
import com.chs.secure_transport.helpers.DateUtils;
import com.chs.secure_transport.helpers.LocationHelper;
import com.chs.secure_transport.helpers.NetworkHelper;
import com.chs.secure_transport.helpers.PermissionChecker;
import com.chs.secure_transport.helpers.SharedPrefHelper;
import com.chs.secure_transport.helpers.ToastHelper;
import com.chs.secure_transport.restapi.ProcessAsyncRequest;
import com.chs.secure_transport.restapi.RequestObject;
import com.chs.secure_transport.utils.AppUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static com.chs.secure_transport.helpers.PermissionChecker.LOCATION_PERMISSIONS;
import static com.chs.secure_transport.utils.AppUtils.SESSION_ID;

public class LocationUpdateService extends Service implements LocationListener, ProcessAsyncRequest.onResponseListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    public LocationRequest location_request_;
    private GoogleApiClient google_api_client_;
    private Location current_location_;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        if (googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS)
        {
            google_api_client_ = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }

        createLocationRequest();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        try
        {
            if (location == null)
            {
                return;
            }

            float distance = current_location_.distanceTo(location);
            long time = current_location_.getTime();

            current_location_ = location;

            Log.e("onLocationChanged", "current_location_" + current_location_.toString());

            if (!LocationHelper.isAppIsInBackground(LocationUpdateService.this))
            {
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", location.getLatitude());
                bundle.putDouble("lng", location.getLongitude());
                bundle.putDouble("dist", distance);
                ObservableObject.getInstance().updateValue(bundle);
            }

            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {

                    if (!NetworkHelper.hasNetworkConnection(LocationUpdateService.this))
                    {
                        //SnackbarHelper.noInternet(LocationUpdateService.this);
                        ToastHelper.noInternet(LocationUpdateService.this);
                        return;
                    }

                    updateData();
                }
            });

            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.e("TAG", "onConnect called");
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.e("TAG", "Suspended");
        google_api_client_.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.e("TAG", "Connection Failed");
    }

    @SuppressLint("RestrictedApi")
    protected void createLocationRequest()
    {
        try
        {
            if (location_request_ == null)
            {
                location_request_ = new LocationRequest();
                location_request_.setFastestInterval(10000);//10ms
                location_request_.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                location_request_.setInterval(60000);//60ms
                location_request_.setSmallestDisplacement(30);
            }

            if (google_api_client_ != null)
            {
                google_api_client_.connect();
            }

            try
            {
                if ((google_api_client_ != null) && google_api_client_.isConnected())
                {
                    if (PermissionChecker.checkPermission(LocationUpdateService.this, LOCATION_PERMISSIONS))
                    {
                        LocationServices.FusedLocationApi.requestLocationUpdates(google_api_client_, location_request_, this);
                    }
                }
            }
            catch (Exception e)
            {
                Log.e("TAG", "Exception Failed1" + e.getMessage());
            }
        }
        catch (Exception e)
        {
            Log.e("TAG", "Exception Failed" + e.getMessage());
        }
    }

    private void updateData()
    {
        try
        {
            RequestObject requestObject = new RequestObject();
            requestObject.setRequestId("");
            requestObject.setServiceId("CS_SAVE_LOCATION_TRACK ");
            requestObject.setDomain("CS_DAO");
            requestObject.setUserType("location_track");
            requestObject.setGetOrPost(CommonConstants.SAVE);

            JSONObject authentication_obj = new JSONObject();
            authentication_obj.accumulate("user_id", SharedPrefHelper.getUserId(LocationUpdateService.this));
            authentication_obj.accumulate("session_id", SESSION_ID);
            requestObject.setLogin(authentication_obj.toString());

            JSONObject crt_obj = new JSONObject();
            crt_obj.accumulate("inquiry_no_", AppUtils.INQUIRY_NO);
            crt_obj.accumulate("admission_no_", SharedPrefHelper.getUserId(LocationUpdateService.this));
            crt_obj.accumulate("user_id_", SharedPrefHelper.getUserId(LocationUpdateService.this));
            crt_obj.accumulate("ay_code_", AppUtils.ACCEDMIC_YEAR);
            crt_obj.accumulate("branch_code_", AppUtils.BRANCH_CODE);
            crt_obj.accumulate("date_", DateUtils.get_date_string_save(new Date()));
            crt_obj.accumulate("time_", DateHelper.getCurrentTime());
            crt_obj.accumulate("timestamp_", CommonUtils.get_current_timestamp());
            String lat = String.valueOf(current_location_.getLatitude());
            String lng = String.valueOf(current_location_.getLongitude());
            crt_obj.accumulate("latitude_", lat);
            crt_obj.accumulate("longitude_", lng);
            requestObject.setCriteria(crt_obj.toString());
            new ProcessAsyncRequest(requestObject, this).execute();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {
        if (google_api_client_ != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(google_api_client_, this);
        }

        super.onDestroy();
    }

    @Override
    public void onSuccess(JSONObject jsonObject)
    {
    }

    @Override
    public void onFailure(JSONObject jsonObject)
    {
    }
}
