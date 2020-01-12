package com.msa.timetracker;

import android.annotation.SuppressLint;

public class DataModel {

    private String name;
    private String duration;

    DataModel(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }


    @SuppressLint("DefaultLocale")
    String getDuration() {
        long milliseconds = Long.parseLong(duration);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        milliseconds -= hours * (1000 * 60 * 60);
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        milliseconds -= minutes * (1000 * 60);
        int seconds = (int) (milliseconds / 1000) % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}