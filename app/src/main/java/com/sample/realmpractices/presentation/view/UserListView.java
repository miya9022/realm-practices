package com.sample.realmpractices.presentation.view;

import com.sample.realmpractices.presentation.model.UserModel;

import java.util.Collection;

/**
 * Created by app on 3/9/17.
 */

public interface UserListView extends LoadDataView {
    void renderUserList(Collection<UserModel> userModelCollection);

    void viewUser(UserModel userModel);
}
