package com.sample.realmpractices.data.entity;

import io.realm.RealmObject;

/**
 * Created by app on 3/8/17.
 */

public class EmailEntity extends RealmObject {
    private String email;

    private int active;

    public EmailEntity() {
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
