package com.example.myapplication2.DataBase;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class HoursViewModel extends AndroidViewModel {

    private HoursRepository mRepository;
    private LiveData<List<Hours>> mData;

    public HoursViewModel(Application application) {
        super(application);
        mRepository = new HoursRepository(application);
    }

    //get all data from database
    public LiveData<List<Hours>> getAllData() {
        return mRepository.getAllData();
    }

    //insert data to database
    public void insertData(Hours data) {
        mRepository.insertData(data);
    }

    //delete data with specified parameters
    public void deleteData(int day, int month, int year) {
        mRepository.deleteData(day, month, year);
    }


}
