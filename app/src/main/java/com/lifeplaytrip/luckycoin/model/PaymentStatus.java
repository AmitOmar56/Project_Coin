package com.lifeplaytrip.luckycoin.model;

/**
 * Created by Priyanka on 2/27/2018.
 */

public class PaymentStatus
{
    private int payment_status_image;
    private String payment_status;
    private String cash_remaining;
    private String payment_date;
    private String payment_time;

    public PaymentStatus(String payment_status,int payment_status_image,String cash_remaining,String payment_date,String payment_time)
    {
        this.cash_remaining=cash_remaining;
        this.payment_status=payment_status;
        this.payment_status_image=payment_status_image;
        this.payment_date=payment_date;
        this.payment_time=payment_time;
    }

    public int getPayment_status_image() {
        return payment_status_image;
    }

    public void setPayment_status_image(int payment_status_image) {
        this.payment_status_image = payment_status_image;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getCash_remaining() {
        return cash_remaining;
    }

    public void setCash_remaining(String cash_remaining) {
        this.cash_remaining = cash_remaining;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

}
