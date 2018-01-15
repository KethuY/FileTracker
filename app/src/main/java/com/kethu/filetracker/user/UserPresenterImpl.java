package com.kethu.filetracker.user;

import android.content.Context;

import com.kethu.filetracker.db.SqliteAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by satya on 07-Jan-18.
 */

public class UserPresenterImpl implements UserPresnter {
    private final SqliteAdapter mSqliteAdapter;
    private Context mContext;
    private UserView mUserView;

    public UserPresenterImpl(Context context, UserView userFragment) {
        mContext = context;
        mUserView = userFragment;

        mSqliteAdapter=new SqliteAdapter(mContext);
    }

    @Override
    public void getUserData() {
        Map<String, List<User>> users = new HashMap<>();
        List<String> headers = new ArrayList<>();

        for (int i = 0; i < 10 ; i++) {
            headers.add("User Type "+(i+1));

            ArrayList<User> users1=new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                User user=new User();
                user.setName("Name "+(j+1));
                user.setMobile("999999999"+(j+1));
                user.setDesignation("Designation"+(j+1));
                user.setUserStatus("Status"+(j+1));
                user.setUserType(headers.get(i));
                users1.add(user);
            }

            users.put(headers.get(i),users1);
        }

        mUserView.setDataToExpandableListView(headers,users);
    }
}
