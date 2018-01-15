package com.kethu.filetracker.user;

import java.util.List;
import java.util.Map;

/**
 * Created by satya on 07-Jan-18.
 */

public interface UserView {
    void setDataToExpandableListView(List<String> headers,Map<String, List<User>> users);
}
