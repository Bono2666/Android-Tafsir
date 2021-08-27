package com.bonoworksdesign.tafsiribnukatsir.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelDataSettings {
    @SerializedName("screen_on")
    @Expose
    private int screen_on;

    public ModelDataSettings(int screen_on) {
        this.screen_on = screen_on;
    }

    public int getScreen_on() {
        return screen_on;
    }

    public void setScreen_on(int screen_on) {
        this.screen_on = screen_on;
    }
}
