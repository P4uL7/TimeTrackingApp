package com.msa.timetracker;

public class DataModel {

    private String name;
    private String duration;

    public DataModel(String name, String duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }


    public String getDuration() {
        long milliseconds = Long.parseLong(duration);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        milliseconds -= hours * (1000 * 60 * 60);
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        milliseconds -= minutes * (1000 * 60);
        int seconds = (int) (milliseconds / 1000) % 60;

        String _duration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return _duration;
    }

}