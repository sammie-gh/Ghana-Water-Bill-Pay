package com.gh.sammie.ghanawater.model;

import android.graphics.drawable.GradientDrawable;

public class Extra_categories {

    int image;
    GradientDrawable gradient;
    String title;

    public Extra_categories(GradientDrawable gradient, int image, String title) {
        this.image = image;
        this.gradient = gradient;
        this.title = title;
    }


    public Extra_categories() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public GradientDrawable getGradient() {
        return gradient;
    }

    public void setGradient(GradientDrawable gradient) {
        this.gradient = gradient;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
