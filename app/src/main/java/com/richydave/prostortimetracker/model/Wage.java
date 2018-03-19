package com.richydave.prostortimetracker.model;



import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class Wage {
    //instance variable declaration
    private int wageId;
    private double wage;
    private double hours;
    private String saveTime;

    public int getWageId() {
        return this.wageId;
    }

    public void setWageId(int wageId) {
        this.wageId = wageId;
    }

    public double getWage() {
        return this.wage;
    }

    public void setWage(double wage) {
        this.wage = wage;
    }

    public String getSaveTime() {
        DateTime dataTime = new DateTime();
        DateTimeFormatter dateTimeFormatter =DateTimeFormat.forPattern("dd MMM yyyy hh:mm:ss a");
        this.saveTime = dateTimeFormatter.print(dataTime);
        return this.saveTime;
    }

    public void setSaveTime(String saveTime) {
        this.saveTime = saveTime;
    }

    public double getHours() {
        return this.hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

}
