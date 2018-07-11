package com.chs.secure_transport.route_point;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chs.secure_transport.R;
import com.chs.secure_transport.base.BaseActivity;
import com.chs.secure_transport.helpers.CommonConstants;
import com.chs.secure_transport.helpers.PermissionChecker;
import com.chs.secure_transport.helpers.SharedPrefHelper;
import com.chs.secure_transport.helpers.Utility;
import com.chs.secure_transport.restapi.ProcessAsyncRequest;
import com.chs.secure_transport.restapi.RequestObject;
import com.chs.secure_transport.utils.AppUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.chs.secure_transport.utils.AppUtils.SESSION_ID;

public class AddRoutePointsActivity extends BaseActivity implements OnMapReadyCallback, ProcessAsyncRequest.onResponseListener, GoogleMap.OnMyLocationButtonClickListener {
    private GoogleMap mMap;

    ProcessAsyncRequest.onResponseListener mListenr;
    private Marker mCurLocMarker;

    @Override
    protected void onStart() {
        WANT_TO_SHOW_HOME_ICON = true;
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_route_points);

        if (!PermissionChecker.checkPermission(AddRoutePointsActivity.this, PermissionChecker.LOCATION_PERMISSIONS)) {
            PermissionChecker.reqPermissions(AddRoutePointsActivity.this, PermissionChecker.LOCATION_PERMISSIONS);
        }


        mListenr = this;
        setUpToolbarDrawerNaviagtion();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void checkGPSAndLocationEnabledOrNot() {
        try {
            LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (null != mLocationManager) {
                PackageManager pm = getPackageManager();
                boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
                boolean isGPSEnabled = false;
                boolean isNetworkEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (hasGps) {
                    isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                }

                try {
                    if (!isNetworkEnable && !isGPSEnabled) {
                        Utility.buildAlertMessageNoGps(AddRoutePointsActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!PermissionChecker.checkPermission(AddRoutePointsActivity.this, PermissionChecker.LOCATION_PERMISSIONS)) {
                    PermissionChecker.reqPermissions(AddRoutePointsActivity.this, PermissionChecker.LOCATION_PERMISSIONS);
                    return;
                }


                Location lastKnownLocation = null;
                LatLng latLng = null;

                if (hasGps) {

                    lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (lastKnownLocation != null) {
                        latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    }
                }

                if (lastKnownLocation == null) {
                    lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (lastKnownLocation != null) {
                        latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                    }
                }

                if (latLng != null) {
                    mCurLocMarker= mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int count = 0;

        for (int grantResult : grantResults) {

            if (grantResult == PERMISSION_GRANTED) {
                count++;
            }
        }

        if (grantResults.length != count) {
            PermissionChecker.reqPermissions(AddRoutePointsActivity.this, PermissionChecker.LOCATION_PERMISSIONS);
            //ToastHelper.showToastLenShort(AddRoutePointsActivity.this, "Please allow the location permissions");
        } else {
            if (mMap == null)
                return;

            onMapReady(mMap);

        }
    }

    private void setUpToolbarDrawerNaviagtion() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Route Point");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(AddRoutePointsActivity.this, R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (!PermissionChecker.checkPermission(AddRoutePointsActivity.this, PermissionChecker.LOCATION_PERMISSIONS) || mMap == null)
            return;

        mMap.setMyLocationEnabled(true);

        checkGPSAndLocationEnabledOrNot();

    }

    @Override
    public boolean onMyLocationButtonClick() {

        if (mMap != null) {
            Location location = mMap.getMyLocation();

            if (location != null)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));

        }
        return false;
    }


    public void addRoutePoint(View view) {

        showConfirmationDialog();


    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRoutePointsActivity.this);
        builder.setTitle("Route Point Confirmation");
        builder.setMessage("Are you sure to add this location as Route Point?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

       final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ;

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    alertDialog.cancel();

                    RequestObject requestObject = new RequestObject();
                    requestObject.setRequestId("");
                    requestObject.setServiceId("CS_SAVE_ROUTE_POINT");
                    requestObject.setDomain("CS_DAO");
                    requestObject.setUserType("route_point");
                    requestObject.setGetOrPost(CommonConstants.POST);

                    JSONObject authentication_obj = new JSONObject();
                    authentication_obj.accumulate("user_id", SharedPrefHelper.getUserId(AddRoutePointsActivity.this));
                    authentication_obj.accumulate("session_id", SESSION_ID);
                    requestObject.setLogin(authentication_obj.toString());

                    JSONObject crt_obj = new JSONObject();
                    crt_obj.accumulate("id_", SharedPrefHelper.getUserId(AddRoutePointsActivity.this));
                    crt_obj.accumulate("inquiry_no_", AppUtils.INQUIRY_NO);
                    crt_obj.accumulate("admission_no_", SharedPrefHelper.getUserId(AddRoutePointsActivity.this));
                    crt_obj.accumulate("user_id_", SharedPrefHelper.getUserId(AddRoutePointsActivity.this));
                    requestObject.setCriteria(crt_obj.toString());
                    new ProcessAsyncRequest(requestObject, mListenr).execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onFailure(JSONObject jsonObject) {

    }
}
