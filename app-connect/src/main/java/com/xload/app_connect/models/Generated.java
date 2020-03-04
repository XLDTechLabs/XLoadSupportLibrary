package com.xload.app_connect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Generated {
    @SerializedName("generated")
    @Expose
    private String generated;

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }
}
