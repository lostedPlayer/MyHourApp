package com.example.myapplication2.DataBase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class HoursRepository {


    private HoursDao hoursDao;
    private LiveData<List<Hours>> mData;
    private ExecutorService dataBaseExicutor;

    HoursRepository(Application application) {
        HoursDB db = HoursDB.getDataBaseInstance(application);
        dataBaseExicutor = HoursDB.dataBaseWriteExecutor;
        hoursDao = db.HoursDao();
        mData = hoursDao.getAllData();


    }


    //get all data from db
    public LiveData<List<Hours>> getAllData() {
        return hoursDao.getAllData();
    }


    //insert Data to Db
    public void insertData(Hours data) {
        dataBaseExicutor.execute(() -> {
            hoursDao.insertData(data);

        });
    }


    //delete Specific data from db
    public void deleteData(int day, int month, int year) {
        dataBaseExicutor.execute(() -> {
            hoursDao.deleteData(day, month, year);

        });
    }

}