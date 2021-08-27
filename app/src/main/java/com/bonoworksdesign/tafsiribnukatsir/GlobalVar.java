package com.bonoworksdesign.tafsiribnukatsir;

import android.app.Application;

public class GlobalVar extends Application {
    private int Pos;

    public int getPos() {
        return Pos;
    }

    public void setPos(int pos) {
        Pos = pos;
    }
}
