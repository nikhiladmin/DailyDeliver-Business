
package com.daytoday.business.dailydelivery.Network.Response;

import java.util.List;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.AuthUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthUserResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("buss_details")
    @Expose
    private List<AuthUser> users = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<AuthUser> getUsers() {
        return users;
    }

    public void setUsers(List<AuthUser> users) {
        this.users = users;
    }

}
