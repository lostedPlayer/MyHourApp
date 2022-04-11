package com.example.myapplication2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.DataBase.Hours;
import com.example.myapplication2.DataBase.HoursViewModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class MainActivity extends AppCompatActivity {

    //views
    private CompactCalendarView calendarView;
    private TextView textView_HoursTogether, textView_Date, textView_HoursForThatDay, textView_WorkPlaceName, textView_monthYear, textView_PayCheck;
    private ImageButton button_addEditData, button_deleteData;

    private HoursViewModel hoursViewModel;
    private List<Hours> mData;


    private String TAG = "test";
    private String holder_empty;
    private String mSelectedDate;
    int sYear;
    int sMonth;
    int sDay;

    int curentMonth;
    int curentYear;


    //Dialog fields for EditTextData
    String mWorkPlace;
    Double mWorkHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initialize Views
        initViews();


        // prepare calendar
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        calendarView.displayOtherMonthDays(false);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(calendarView.getFirstDayOfCurrentMonth());
        curentMonth = cal.get(Calendar.MONTH);
        curentYear = cal.get(Calendar.YEAR);


        //hide  buttons for adding and deleting data inside calendar on start
        button_deleteData.setVisibility(View.INVISIBLE);
        button_addEditData.setVisibility(View.INVISIBLE);

        //get View Model class
        mData = new ArrayList<>();
        hoursViewModel = new ViewModelProvider(this).get(HoursViewModel.class);
        hoursViewModel.getAllData().observe(this, new Observer<List<Hours>>() {
            @Override
            public void onChanged(List<Hours> mHours) {

                //observe data and add to ArrayList
                mData = mHours;
                Log.d(TAG, "mData Size: " + mData.size());
                double mHoursTogether = 0.0;
                double mPayCheck = 0.0;
                calendarView.removeAllEvents();

                for (int i = 0; i < mData.size(); i++) {
                    Hours hours = mData.get(i);


                    //set event
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(hours.getYear(), hours.getMonth() - 1, hours.getDay());
                    long date = calendar.getTimeInMillis();
                    Event event = new Event(Color.BLUE, date, hours.getHours());
                    calendarView.addEvent(event);


                    //get Hours together or specific month and paycheck
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
                    cal.setTime(calendarView.getFirstDayOfCurrentMonth());
                    int mSelectedMonth = cal.get(Calendar.MONTH);
                    int month = hours.getMonth();

                    if (month == mSelectedMonth + 1) {
                        mHoursTogether = hours.getHours() + mHoursTogether;
                    }


                }


                //set hours Together to textView
                textView_HoursTogether.setText(Double.toString(mHoursTogether));
            }
        });


        //Calendar View listener here
        String[] mMonths = {"JANUAR", "FEBRUAR", "MART", "APRIL", "MAJ", "JUNI", "JULI", "AUGUST", "SEPTEMBAR", "OKTOBAR", "NOVEMBAR", "DECEMBAR"}; //lIST OF MONTHS TO SHOW ON TOP OF CALENDAR VIEW
        textView_monthYear.setText(curentYear + "-" + mMonths[curentMonth]);
        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                //get Data for selectd day
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
                cal.setTime(dateClicked);
                sYear = cal.get(Calendar.YEAR);
                sMonth = cal.get(Calendar.MONTH) + 1;
                sDay = cal.get(Calendar.DAY_OF_MONTH);
                getDataFromDBForSpecificDate(sYear, sMonth, sDay);


            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                Log.d(TAG, "onMonthScroll: ");
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
                cal.setTime(firstDayOfNewMonth);
                int y = cal.get(Calendar.YEAR);
                int m = cal.get(Calendar.MONTH);


                textView_monthYear.setText(y + "-" + mMonths[m]);

                //get all hours for that month
                double mPayCheck = 0.0;
                double mHoursTogether = 0.0;
                for (int i = 0; i < mData.size(); i++) {
                    Hours hours = mData.get(i);
                    int month = hours.getMonth();

                    if (month == m + 1) {
                        mHoursTogether = hours.getHours() + mHoursTogether;
                    }


                    //show hours together for that month
                    textView_HoursTogether.setText(Double.toString(mHoursTogether));


                }

            }
        });

        //Button for adding new data to Database
        button_addEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //TODO: MAKE ALERT DIALOG HERE TO ADD NEW DATA TO APP
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.alert_dialog_customTheme));
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_data_dialog, null);
                mBuilder.setView(view1);

                //views inside alertDialog
                TextView dialogTextView_date = view1.findViewById(R.id.textView_selectedDate_dialog);
                TextFieldBoxes dialogWorkPlace_EditText = view1.findViewById(R.id.dialogWorkPlace);
                TextFieldBoxes dialogWorkHours = view1.findViewById(R.id.dialogWorkHoursThatDay);


                //set date for wich user is entering data
                dialogTextView_date.setText(sDay + "." + sMonth + "." + sYear);


                //get text From editText fields
                dialogWorkPlace_EditText.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
                    @Override
                    public void onTextChanged(String theNewText, boolean isError) {
                        mWorkPlace = theNewText;
                    }
                });
                dialogWorkHours.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
                    @Override
                    public void onTextChanged(String theNewText, boolean isError) {

                        if (theNewText.isEmpty()) {

                        } else {
                            mWorkHours = Double.parseDouble(theNewText);
                        }
                    }
                });


                //Add button listener
                mBuilder.setPositiveButton(

                        getResources().

                                getString(R.string.dialog_addDataButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (mWorkHours != 0) {
                                    //save data to data base

                                    Hours newData = new Hours(sYear, sMonth, sDay, mWorkHours, mWorkPlace);
                                    hoursViewModel.insertData(newData);
                                    dialogInterface.dismiss();

                                } else {
                                    Toast.makeText(MainActivity.this, "Pick working hours!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                //cancel button listener
                mBuilder.setNegativeButton(

                        getResources().

                                getString(R.string.dialog_dismissButton), null);

                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();


            }
        });

        //button for deleting data
        button_deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete From db
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Are you sure that you want to delete this entry?")
                        .setPositiveButton("Yes Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                hoursViewModel.deleteData(sDay, sMonth, sYear);
                                Toast.makeText(MainActivity.this, "DELETED!", Toast.LENGTH_SHORT).show();
                                button_deleteData.setVisibility(View.INVISIBLE);

                            }
                        })

                        .setNegativeButton("Back", null)
                        .create()
                        .show();
                ;

            }
        });
    }


    //init Views here
    void initViews() {
        calendarView = findViewById(R.id.CalendarView_main);
        textView_HoursTogether = findViewById(R.id.textView_hoursTogether);
        textView_Date = findViewById(R.id.textView_selectedDate);
        textView_HoursForThatDay = findViewById(R.id.textView_hoursForThatDay);
        textView_WorkPlaceName = findViewById(R.id.textView_workPlaceName);
        button_addEditData = findViewById(R.id.button_addEditData);
        button_deleteData = findViewById(R.id.button_DeleteData);
        textView_monthYear = findViewById(R.id.textView_monthYear);
    }


    //set data to text fields
    void getDataFromDBForSpecificDate(int yearRaw, int monthRaw, int dayRaw) {

        //check if there is any data in DataBase
        //if no data show add button to user

        if (mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                Hours curentHour = mData.get(i);
                Integer y = curentHour.getYear();
                Integer m = curentHour.getMonth();
                Integer d = curentHour.getDay();


                Integer year = new Integer(yearRaw);
                Integer month = new Integer(monthRaw);
                Integer day = new Integer(dayRaw);


                if (y.equals(yearRaw) & m.equals(monthRaw) & d.equals(dayRaw)) {
                    Log.d(TAG, "setDataToTextFields: " + y + "/" + m + "/" + d);
                    double hourForThatDate = curentHour.getHours();
                    String workPlaceName = curentHour.getWorkPlace();
                    String date = day + "." + month + "." + year;

                    setDataToTextFields(date, workPlaceName, Double.toString(hourForThatDate));
                    button_addEditData.setVisibility(View.INVISIBLE);
                    button_deleteData.setVisibility(View.VISIBLE);


                    break;

                } else {
                    Log.d(TAG, "err");
                    button_addEditData.setVisibility(View.VISIBLE);
                    button_deleteData.setVisibility(View.INVISIBLE);
                    setDataToTextFields(Integer.toString(dayRaw) + "." + Integer.toString(monthRaw) + "." + Integer.toString(yearRaw), holder_empty, holder_empty);
                }

            }
        } else {

            //If no data for selected date show add button to user
            button_addEditData.setVisibility(View.VISIBLE);
            button_deleteData.setVisibility(View.INVISIBLE);
            setDataToTextFields(Integer.toString(dayRaw) + "." + Integer.toString(monthRaw) + "." + Integer.toString(yearRaw), holder_empty, holder_empty);
        }

    }

    //set default data to text Views
    void setDataToTextFields(String date, String workPlaceName, String hoursForThatDay) {
        textView_Date.setText(date);
        textView_WorkPlaceName.setText(workPlaceName);
        textView_HoursForThatDay.setText(hoursForThatDay);
    }

}