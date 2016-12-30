package com.android.tigerhelp.entity;

import java.io.Serializable;

/**
 * Created by Charles on 2016/12/23.
 */

public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private int userId;
    private String token;
    private boolean turnUpdateUser;

    public int getUserid() {
        return userId;
    }

    public void setUserid(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isTurnUpdateUser() {
        return turnUpdateUser;
    }

    public void setTurnUpdateUser(boolean turnUpdateUser) {
        this.turnUpdateUser = turnUpdateUser;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                ", turnUpdateUser=" + turnUpdateUser +
                '}';
    }
}
