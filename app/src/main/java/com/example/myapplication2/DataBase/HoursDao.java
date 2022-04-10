package com.example.myapplication2.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HoursDao {

    //insert data to database
    @Insert
    void insertData(Hours data);

    //get all data from database
    @Query("SELECT * FROM HOURS")
    LiveData<List<Hours>> getAllData();

    //delete all data from database
    @Query("DELETE FROM HOURS")
    void deleteAll();

    //delete data from database with selected parameters
    @Query("DELETE FROM HOURS WHERE DAY = :day AND MONTH =:month AND YEAR = :year")
    void deleteData(int day, int month, int year);
}
