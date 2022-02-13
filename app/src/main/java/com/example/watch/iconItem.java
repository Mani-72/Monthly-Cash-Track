package com.example.watch;

public class iconItem {

    private int mFlagImage;
    private String mName;

    public iconItem( String name, int flagImage) {
        mName=name;
        mFlagImage = flagImage;
    }

    public String getName(){
        return mName;
    }
    public int getFlagImage() {
        return mFlagImage;
    }
}
