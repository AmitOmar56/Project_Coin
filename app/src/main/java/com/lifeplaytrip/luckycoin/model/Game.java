package com.lifeplaytrip.luckycoin.model;

/**
 * Created by LifePlayTrip on 2/26/2018.
 */

public class Game {
    private String win_status;
    private int win_status_image;
    private String win_points;
    private String win_date;
    private String win_time;

    public Game(String win_status, int win_status_image, String win_points, String win_date, String win_time) {
        this.win_status = win_status;
        this.win_status_image = win_status_image;
        this.win_points = win_points;
        this.win_date = win_date;
        this.win_time = win_time;
    }

    public String getWin_status() {
        return win_status;
    }

    public void setWin_status(String win_status) {
        this.win_status = win_status;
    }

    public int getWin_status_image() {
        return win_status_image;
    }

    public void setWin_status_image(int win_status_image) {
        this.win_status_image = win_status_image;
    }

    public String getWin_points() {
        return win_points;
    }

    public void setWin_points(String win_points) {
        this.win_points = win_points;
    }

    public String getWin_date() {
        return win_date;
    }

    public void setWin_date(String win_date) {
        this.win_date = win_date;
    }

    public String getWin_time() {
        return win_time;
    }

    public void setWin_time(String win_time) {
        this.win_time = win_time;
    }
}
