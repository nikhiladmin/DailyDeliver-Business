package com.daytoday.business.dailydelivery.Network.Response;

import com.daytoday.business.dailydelivery.MainHomeScreen.Model.Dates;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DayWiseResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("pending")
    @Expose
    private List<Dates> pending = null;
    @SerializedName("accepted")
    @Expose
    private List<Dates> accepted = null;
    @SerializedName("rejected")
    @Expose
    private List<Dates> rejected = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Dates> getPending() {
        return pending;
    }

    public void setPending(List<Dates> pending) {
        this.pending = pending;
    }

    public List<Dates> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<Dates> accepted) {
        this.accepted = accepted;
    }

    public List<Dates> getRejected() {
        return rejected;
    }

    public void setRejected(List<Dates> rejected) {
        this.rejected = rejected;
    }
}
