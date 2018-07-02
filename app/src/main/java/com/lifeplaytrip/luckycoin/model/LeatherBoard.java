package com.lifeplaytrip.luckycoin.model;

/**
 * Created by LifePlayTrip on 2/9/2018.
 */

public class LeatherBoard {
    private String winner_name;
    private String winner_fname;
    private int winner_id;
    private String winner_price;
    private int winner_rank;

    public LeatherBoard(String winner_name, String winner_fname, String winner_price, int winner_id, int winner_rank) {
        this.winner_name = winner_name;
        this.winner_fname = winner_fname;
        this.winner_price = winner_price;
        this.winner_id = winner_id;
        this.winner_rank = winner_rank;
    }

    public String getWinner_name() {
        return winner_name;
    }

    public void setWinner_name(String winner_name) {
        this.winner_name = winner_name;
    }

    public String getWinner_fname() {
        return winner_fname;
    }

    public void setWinner_fname(String winner_fname) {
        this.winner_fname = winner_fname;
    }

    public int getWinner_id() {
        return winner_id;
    }

    public void setWinner_id(int winner_id) {
        this.winner_id = winner_id;
    }

    public String getWinner_price() {
        return winner_price;
    }

    public void setWinner_price(String winner_price) {
        this.winner_price = winner_price;
    }

    public int getWinner_rank() {
        return winner_rank;
    }

    public void setWinner_rank(int winner_rank) {
        this.winner_rank = winner_rank;
    }
}
