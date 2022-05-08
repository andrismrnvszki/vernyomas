package com.example.venyomasnaplo;

import java.time.LocalDate;


public class MeasurementItem {
    private String id;
    private int systole;
    private int diastole;
    private int pulse;
    private String date;
    private String user;

    public MeasurementItem(int systole, int diastole, int pulse, String date, String user) {
        this.systole = systole;
        this.diastole = diastole;
        this.pulse = pulse;
        this.date = date;
        this.user=user;
    }

    public MeasurementItem() {

    }

    public void setSystole(int systole) {
        this.systole = systole;
    }

    public void setDiastole(int diastole) {
        this.diastole = diastole;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public int getSystole() {
        return systole;
    }

    public int getDiastole() {
        return diastole;
    }

    public int getPulse() {
        return pulse;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }
}
