package com.kethu.filetracker.login;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.kethu.filetracker.R;
import com.kethu.filetracker.helpers.ViewHelper;
import com.kethu.filetracker.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView, TextWatcher {
    @BindView(R.id.user_id)
    TextInputLayout mUserId;
    @BindView(R.id.password)
    TextInputLayout mPassword;
    LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (mUserId.getEditText() != null)
            mUserId.getEditText().addTextChangedListener(this);

        if (mPassword.getEditText() != null)
            mPassword.getEditText().addTextChangedListener(this);

        mLoginPresenter = new LoginPresenterImpl(LoginActivity.this, this);
    }

    public void onClick(View view) {
        mLoginPresenter.doLogin(ViewHelper.getString(mUserId), ViewHelper.getString(mPassword));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String userid = ViewHelper.getString(mUserId);
        String password = ViewHelper.getString(mPassword);

        if (userid.length() > 0 && mLoginPresenter.validateUserId(userid))
            mUserId.setErrorEnabled(false);

        if (password.length() > 0 && mLoginPresenter.validatePassword(password))
            mPassword.setErrorEnabled(false);


    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void enterUserId() {
        mUserId.setError("Enter user id");
    }

    @Override
    public void enterPassword() {
        mPassword.setError("Enter password");
    }

    @Override
    public void navigateToHomePage() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();

    }
}
