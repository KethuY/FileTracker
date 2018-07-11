package com.chs.secure_transport.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.chs.secure_transport.R;
import com.chs.secure_transport.changepwd.ChangePasswordActivity;
import com.chs.secure_transport.helpers.NetworkHelper;
import com.chs.secure_transport.helpers.SharedPrefHelper;
import com.chs.secure_transport.helpers.ToastHelper;
import com.chs.secure_transport.menu.MenuActivity;
import com.chs.secure_transport.login.LoginActivity;
import com.chs.secure_transport.profile.ProfileActivity;


public abstract class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private static final String NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static boolean WANT_TO_SHOW_HOME_ICON;


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
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(broadcastReceiver, new IntentFilter(NETWORK_CHANGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        for(int i = 0; i < menu.size(); i++){
            //menu.getItem(i).setVisible(true);

            if(menu.getItem(i).getItemId()==R.id.home_menu){

                if(WANT_TO_SHOW_HOME_ICON){
                    menu.getItem(i).setVisible(true);
                }else{
                    menu.getItem(i).setVisible(false);
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.home_menu:
                startActivity(new Intent(BaseActivity.this, MenuActivity.class));
                return true;

            case R.id.signout_menu:
                signOutConfirmation();
                return true;

            case R.id.profile_menu:
                startActivity(new Intent(BaseActivity.this, ProfileActivity.class));
                return true;



            case R.id.change_pwd_menu:
                startActivity(new Intent(BaseActivity.this, ChangePasswordActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void signOutConfirmation() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BaseActivity.this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Signout confirmation");
        builder.setMessage("Do you want to signout from the Parent app?");
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

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                SharedPrefHelper.clearAll(BaseActivity.this);
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

}
