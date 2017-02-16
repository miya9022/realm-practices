package com.sample.realmpractices.model;

import com.sample.realmpractices.helper.RealmProvider;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by app on 2/13/17.
 */

public class User extends RealmObject {
    @PrimaryKey
    private int id;

    private String name;
    private int age;
    private RealmList<Email> emails;

    public User() {
    }

    public User(String name, int age) {
        this.id = RealmProvider.getLastId(User.class, "id") + 1;
        this.name = name;
        this.age = age;
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

    public RealmList<Email> getEmails() {
        return emails;
    }

    public void setEmails(RealmList<Email> emails) {
        this.emails = emails;
    }
}
