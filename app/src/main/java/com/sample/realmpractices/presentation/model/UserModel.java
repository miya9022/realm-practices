package com.sample.realmpractices.presentation.model;

import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * Created by app on 2/13/17.
 */

public class UserModel {
    @PrimaryKey
    private int id;

    private String name;
    private int age;
    private List<EmailModel> emails;

    public UserModel() {
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

    public List<EmailModel> getEmailModels() {
        return emails;
    }

    public void setEmailModels(List<EmailModel> emailModels) {
        this.emails = emailModels;
    }

    public void addEmail(EmailModel emailModel) {
        emails.add(emailModel);
    }
}
