package com.sample.realmpractices.data.entity;


import com.sample.realmpractices.data.database.RealmProvider;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by app on 3/8/17.
 */

public class UserEntity extends RealmObject {
    @PrimaryKey
    private int id;

    private String name;
    private int age;
    private RealmList<EmailEntity> emails;

    public UserEntity() {
    }

    public UserEntity(String name, int age) {
        this.id = RealmProvider.getLastId(UserEntity.class, "id") + 1;
        this.name = name;
        this.age = age;
        this.emails = new RealmList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public RealmList<EmailEntity> getEmails() {
        return emails;
    }

    public void setEmails(RealmList<EmailEntity> emails) {
        this.emails = emails;
    }

    public void addEmail(EmailEntity email) {
        emails.add(email);
    }
}
