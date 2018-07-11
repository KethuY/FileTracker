package com.chs.secure_transport.drop;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.chs.secure_transport.R;
import com.chs.secure_transport.base.BaseActivity;
import com.chs.secure_transport.fcm.ObservableObject;
import com.chs.secure_transport.helpers.OnItemClickListener;
import com.chs.secure_transport.helpers.PermissionChecker;
import com.chs.secure_transport.helpers.Utility;
import com.chs.secure_transport.restapi.ProcessAsyncRequest;
import com.chs.secure_transport.student.Student;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;


public class StudentDropActivity extends BaseActivity implements OnMapReadyCallback, DropAttendceFragment.OnStudentDropStatusSavedListener, OnItemClickListener, ProcessAsyncRequest.onResponseListener, GoogleMap.OnMyLocationButtonClickListener, Observer {
    private static final String[] mLocationPermissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap map_;
    private BitmapDescriptor mCarIcon;
    private Marker mMarker;
    private boolean mIsDriverStared;
    // private TextView mStartReachBtn;
    private Location mPreviousLocation;
    private float mBearing;
    private List<Student> mStudents;
    LinearLayout mMainLl;
    private DropAttendceFragment mDropAtt;
    private MenuItem mStartOrStopMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_drop);
        mMainLl = findViewById(R.id.main_ll);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Drop Off's");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(StudentDropActivity.this, R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ObservableObject.getInstance().addObserver(this);

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!PermissionChecker.checkPermission(StudentDropActivity.this, mLocationPermissions))
            PermissionChecker.reqPermissions(StudentDropActivity.this, mLocationPermissions);
        mStudents = new ArrayList<>();

        SupportMapFragment map_fragment_ = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map_fragment_.getMapAsync(this);

        //  mStartReachBtn = findViewById(R.id.start_reached);

        try {
            mCarIcon = Utility.getDrawableIcon(StudentDropActivity.this, R.drawable.truck);
        } catch (Exception e) {
            Log.e("StudentPickUpActivity", "IBitmapDescriptorFactory is not initialized");
        }


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
                        Utility.buildAlertMessageNoGps(StudentDropActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (PermissionChecker.checkPermission(StudentDropActivity.this, mLocationPermissions)) {

                    Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    LatLng latLng = null;
                    if (lastKnownLocation != null) {
                        latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    }

                    if (latLng == null && hasGps) {
                        lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (lastKnownLocation != null) {
                            latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }
                    }

                    if (latLng != null) {
                        showMarker(latLng);

                    } else {
                        onMyLocationButtonClick();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMarker(LatLng latLng) {

        if (mMarker == null) {
            mMarker = map_.addMarker(new MarkerOptions().position(latLng).icon(mCarIcon).title("Current Location"));

        } else {
            mMarker.setPosition(latLng);
        }

        map_.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

    }


    @Override
    public boolean onMyLocationButtonClick() {
        if (map_ != null) {
            Location location = map_.getMyLocation();

            showMarker(new LatLng(location.getLatitude(), location.getLongitude()));

        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            map_ = googleMap;
            map_.getUiSettings().setAllGesturesEnabled(true);
            map_.getUiSettings().setScrollGesturesEnabled(true);
            map_.getUiSettings().setZoomGesturesEnabled(true);
            checkGPSAndLocationEnabledOrNot();

            map_.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int i) {

                }
            });
            map_.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {

                }
            });

            map_.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {

                }
            });

            map_.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener() {
                @Override
                public void onCameraMoveCanceled() {

                }
            });

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        WANT_TO_SHOW_HOME_ICON = true;
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllowPermission = true;

        for (int granted : grantResults) {

            if (PERMISSION_GRANTED != granted) {
                hasAllowPermission = false;
            }
        }

        if (hasAllowPermission) {
            checkGPSAndLocationEnabledOrNot();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.common_menu, menu);
        mStartOrStopMenu = menu.findItem(R.id.start_menu);
        mStartOrStopMenu.setVisible(true);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.start_menu:

                if (!mIsDriverStared) {
                    item.setTitle("STOP");
                    //  mStartReachBtn.setText("Reached Drop Location");
                    mIsDriverStared = true;
                } else {

                 /*   if(mMainLl.getVisibility()==View.VISIBLE){
                        mMainLl.setVisibility(View.GONE);
                    }*/

                    if (map_ != null) {
                        map_.clear();
                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        onMapReady(map_);
                    }
                    item.setTitle("START");
                    mIsDriverStared = false;
                    // mStartReachBtn.setText("Start");
                }

                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void update(Observable o, Object arg) {

        if (mIsDriverStared) {
            Bundle bundle = (Bundle) arg;
            Double lat = bundle.getDouble("lat");
            Double lng = bundle.getDouble("lng");
            float distance = bundle.getFloat("dist") / 1000;
            LatLng latLng = new LatLng(lat, lng);
            Location currentLoc = new Location("");
            currentLoc.setLongitude(lng);
            currentLoc.setLatitude(lat);

            if (mPreviousLocation != null && mPreviousLocation.getLatitude() == currentLoc.getLatitude() && mPreviousLocation.getLongitude() == currentLoc.getLongitude())
                return;

            if (mPreviousLocation == null)
                mPreviousLocation = currentLoc;
            else {
                mBearing = mPreviousLocation.bearingTo(currentLoc);
                mPreviousLocation = currentLoc;
            }

            map_.addPolyline(new PolylineOptions().add(latLng));
            mMarker.setRotation(mBearing);
            mMarker.setPosition(latLng);

            if (distance <= 1) {
                showStudents();
            }
        }
    }

    private void showStudents() {

        if (mDropAtt != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mDropAtt);
            fm.beginTransaction().commitAllowingStateLoss();
            mDropAtt = null;
        }

        if (mStartOrStopMenu != null)
            mStartOrStopMenu.setVisible(false);

        mDropAtt = new DropAttendceFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.student_frame, mDropAtt);
        ft.commit();

    }


    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onFailure(JSONObject jsonObject) {

    }

    @Override
    public void onItemClick(final int position, View view) {

    }


    @Override
    public void savedSuccessfully() {

        if (mDropAtt != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mDropAtt);
            fm.beginTransaction().commitAllowingStateLoss();
            mDropAtt = null;
        }

        if (mStartOrStopMenu != null)
            mStartOrStopMenu.setVisible(true);

    }
}



















