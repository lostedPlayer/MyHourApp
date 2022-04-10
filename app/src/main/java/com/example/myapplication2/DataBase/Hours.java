package com.example.myapplication2.DataBase;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "HOURS")
public class Hours {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id=0;

    @ColumnInfo(name = "YEAR")
    private Integer year;

    @ColumnInfo(name = "MONTH")
    private Integer month;

    @ColumnInfo(name = "DAY")
    private Integer day;

    @ColumnInfo(name = "HOURS")
    private double hours;

    @ColumnInfo(name = "WORK_PLACE")
    private String workPlace;


    public Hours(int year, int month, int day, double hours, String workPlace) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.workPlace = workPlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public double getHours() {
        return hours;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
}
