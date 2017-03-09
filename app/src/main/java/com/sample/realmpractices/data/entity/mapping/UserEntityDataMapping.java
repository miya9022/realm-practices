package com.sample.realmpractices.data.entity.mapping;

import com.annimon.stream.Stream;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.domain.Email;
import com.sample.realmpractices.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by app on 3/9/17.
 */

public class UserEntityDataMapping {

    private EmailEntityDataMapping emailEntityDataMapping;

    public UserEntityDataMapping() {
        emailEntityDataMapping = new EmailEntityDataMapping();
    }

    public User replicateUserFromRealm(UserEntity userEntity) {
        User user = null;
        if (userEntity.isLoaded() && userEntity.isValid()) {
            user = new User();
            user.setId(userEntity.getId());
            user.setAge(userEntity.getAge());
            user.setName(userEntity.getName());


            List<Email> emails = new ArrayList<>();
            Stream.of(userEntity.getEmails())
                .forEach(
                    emailEntity -> emails.add(emailEntityDataMapping.replicateEmailFromRealm(emailEntity)
                ));
            user.setEmails(emails);
        }
        return user;
    }

    public List<User> replicateUserListFromRealm(List<UserEntity> userEntities) {
        List<User> users = new ArrayList<>();
        Stream.of(userEntities)
                .forEach(userEntity -> {
                    users.add(this.replicateUserFromRealm(userEntity));
                });
        return users;
    }
}
