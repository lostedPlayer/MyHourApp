package com.example.myapplication2.DataBase;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Hours.class}, version = 5, exportSchema = false)
public abstract class HoursDB extends RoomDatabase {

    public abstract HoursDao HoursDao();

    private static HoursDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService dataBaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //create database instance
    static HoursDB getDataBaseInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (HoursDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, HoursDB.class, "DB").fallbackToDestructiveMigration().addCallback(mPopulateDataBase).build();
                }
            }
        }
        return INSTANCE;
    }

//populate database
    private static RoomDatabase.Callback mPopulateDataBase = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            dataBaseWriteExecutor.execute(() -> {
                HoursDao mHoursDao = INSTANCE.HoursDao();

 /*
                mHoursDao.deleteAll();

                Hours hours = new Hours(2022 , 3, 15, 10.5 , "Berlin");
                Hours hours2 = new Hours(2022 , 3, 12, 8.5 , "Studgart");
                mHoursDao.insertData(hours);
                mHoursDao.insertData(hours2);

                 */


            });

        }


    };


}
