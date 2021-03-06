package com.sample.realmpractices.model;

import io.realm.RealmObject;

/**
 * Created by app on 2/16/17.
 */

public class Email extends RealmObject {
    private String email;

    private int active;

    public Email() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
