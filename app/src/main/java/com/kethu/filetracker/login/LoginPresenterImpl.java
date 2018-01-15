package com.kethu.filetracker.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.kethu.filetracker.helpers.SharedPrefHelper;

/**
 * Created by satya on 07-Jan-18.
 */

public class LoginPresenterImpl implements LoginPresenter{
    private Context mContext;
    private LoginView mLoginView;

    LoginPresenterImpl(Context loginActivity, LoginView loginActivity1) {
        mContext=loginActivity;
        mLoginView=loginActivity1;
    }

    @Override
    public boolean validateUserId(String userid) {

        if(TextUtils.isEmpty(userid)){
            mLoginView.enterUserId();
            return false;
        }

        return true;
    }

    @Override
    public boolean validatePassword(String password) {
        if(TextUtils.isEmpty(password)){
            mLoginView.enterPassword();
            return false;
        }

        return true;
    }

    @Override
    public void doLogin(String userId, String password) {

        if(!validateUserId(userId)&&!validatePassword(password)){
            return;
        }

        SharedPrefHelper.seFirstTimeLogin(mContext,true);
        mLoginView.navigateToHomePage();
    }
}
