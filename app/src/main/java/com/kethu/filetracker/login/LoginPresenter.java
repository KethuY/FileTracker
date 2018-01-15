package com.kethu.filetracker.login;

/**
 * Created by satya on 07-Jan-18.
 */

public interface LoginPresenter {
   boolean validateUserId(String userid);
   boolean validatePassword(String password);

    void doLogin(String string, String string1);
}
