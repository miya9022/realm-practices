package com.sample.realmpractices.domain;

import java.util.List;

/**
 * Created by app on 2/13/17.
 */

public class User {
    private int id;

    private String name;
    private int age;
    private List<Email> emails;

    public User() {
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

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    public void addEmail(Email email) {
        emails.add(email);
    }
}
