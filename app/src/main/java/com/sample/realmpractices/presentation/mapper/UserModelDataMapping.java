package com.sample.realmpractices.presentation.mapper;

import com.annimon.stream.Stream;
import com.sample.realmpractices.domain.User;
import com.sample.realmpractices.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by app on 3/9/17.
 */

public class UserModelDataMapping {

    private EmailModelDataMapping emailModelDataMapping;

    public UserModelDataMapping() {
        emailModelDataMapping = new EmailModelDataMapping();
    }

    public UserModel parse(User user) {
        if (user == null) {
            throw new NullPointerException("user not null");
        }

        final UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setName(user.getName());
        userModel.setAge(user.getAge());
        userModel.setEmailModels(emailModelDataMapping.parseList(user.getEmails()));

        return userModel;
    }

    public List<UserModel> parseList(Collection<User> users) {
        List<UserModel> userModels = new ArrayList<>();
        Stream.of(users)
            .forEach(user -> userModels.add(parse(user)));
        return userModels;
    }
}
