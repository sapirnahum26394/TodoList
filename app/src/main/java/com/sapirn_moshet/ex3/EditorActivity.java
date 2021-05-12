package com.sapirn_moshet.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnDatePicker, btnTimePicker, btnADD;
    private EditText txtDate, txtTime, txtTitle, txtDescription;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String day1, monthOfYear1;
    private String hour1, minute1;
    private String user_name;

    public static final String MY_DB_NAME = "TodosDB"; //test
    private SQLiteDatabase todos = null; //test
    int id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        btnDatePicker = (Button) findViewById(R.id.btnDatePickerID);
        btnTimePicker = (Button) findViewById(R.id.btnTimePickerID);
        btnADD = (Button) findViewById(R.id.btnAddID);
        txtDate = (EditText) findViewById(R.id.dateID);
        txtTime = (EditText) findViewById(R.id.timeID);
        txtTitle = (EditText) findViewById(R.id.titleID);
        txtDescription = (EditText) findViewById(R.id.descID);
        user_name = getIntent().getExtras().getString("user_name");
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnADD.setOnClickListener(this);
        createDB();
    }


    public static boolean isDigit(Character c) {
        if (c == null)
            return false;
        if (c < '0' || c > '9')
            return false;
        return true;
    }

    public static boolean Check_validTime(String str) {
        //check if its valid time

        if (str.length() == 5) {
            if (str.charAt(2) == ':' && isDigit(str.charAt(0)) && isDigit(str.charAt(1)) && isDigit(str.charAt(3)) && isDigit(str.charAt(4))) {
                if (str.charAt(0) <= '1' && str.charAt(3) <= '5' && str.charAt(4) <= '9')
                    return true;
                if (str.charAt(0) == '2' && str.charAt(1) <= '3' && str.charAt(3) <= '5' && str.charAt(4) <= '9') //23:59
                    return true;
            }
        }
        return false;

    }

    public static boolean Check_validDate(String strDate) {

        if (strDate.length() == 10) {
            if (strDate.charAt(2) == '/' &&  strDate.charAt(5) == '/')
            {
                if(isDigit(strDate.charAt(6)) && isDigit(strDate.charAt(7)) && isDigit(strDate.charAt(8))&& isDigit(strDate.charAt(9))){ //chek year
                    if(isDigit(strDate.charAt(3)) && isDigit(strDate.charAt(4))){//check month{
                        if(strDate.charAt(3) == '0'){
                            if(strDate.charAt(4) > '9'){
                                return false;
                            }
                        }

                        else if(strDate.charAt(3) == '1')
                        {
                            if(strDate.charAt(4) > '2'){
                                return false;
                            }

                        }
                        if(strDate.charAt(3) > '1')
                            return false;

                    }
                    if(isDigit(strDate.charAt(0)) && isDigit(strDate.charAt(1))){//check day
                        Log.d("mylog", "check DAY of date! 0");
                        if (strDate.charAt(0) == '0')
                        {
                            if (strDate.charAt(1) < '1') {
                                Log.d("mylog", "check DAY of date! 1");
                                return false;
                            }
                        }

                        if (strDate.charAt(0) == '3'){
                            if (strDate.charAt(1) > '1') {
                                Log.d("mylog", "check DAY of date! 2");
                                return false;
                            }

                        }
                        if (strDate.charAt(0) > '3'){
                            return false;
                        }
                        Log.d("mylog", "ALL CLEAR!");
                    }
                    return true;
                }

            }
            return false;
        }
        return false;

    }

    public void createDB() {
        try
        {
            todos = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
            String sql = "CREATE TABLE IF NOT EXISTS todos (_id INTEGER primary key, username VARCHAR, title VARCHAR,description VARCHAR, date VARCHAR, time VARCHAR);";
            todos.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }

        // Make buttons clickable since the database was created
        btnADD.setEnabled(true);
    }

    public void addToDo()
    {
        Cursor cursor = todos.rawQuery("SELECT max(_id) FROM todos;", null);

        // Move to the first row of results & Verify that we have results
        if (cursor.moveToFirst()) {
            if(!cursor.isNull(0)) {
                id = cursor.getInt(0);
                Log.d("debug", "------>id: " + id);
            }
        }
        id++;

        // Get the contact name and email entered
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

//        // Execute SQL statement to insert new data
        String sql = "INSERT INTO todos (_id, username, title, description, date, time) VALUES ('" + id  + "','"+ user_name  + "','" + title  + "', '" + description + "', '" + date + "', '" + time + "');";
        todos.execSQL(sql);
        Log.d("mylog"," ---> "+title + " was insert!");
//        Intent intent = new Intent(EditorActivity.this, ToDoListActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Log.d("mylog"," ---> "+ String.valueOf(dayOfMonth) );


                            boolean check_day = false;
                            boolean check_month = false;
                            if(dayOfMonth < 10){

                                day1  = "0" + String.valueOf(dayOfMonth) ;
                                check_day =true;

                                //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                            if(monthOfYear < 9){
                                Log.d("mylog", String.valueOf(monthOfYear));
                                monthOfYear++;
                                monthOfYear1 = "0" +String.valueOf(monthOfYear);
                                check_month = true;
                                Log.d("mylog", "i was here");
                            }
                            if (check_day && check_month){
                                txtDate.setText(day1 + "/" + (monthOfYear1) + "/" + year);
                                Log.d("mylog", "1");
                            }
                            else if(check_day && !check_month){
                                txtDate.setText(day1 + "/" + (monthOfYear+1) + "/" + year);
                                Log.d("mylog", "2");
                            }
                            else if(!check_day && check_month){
                                txtDate.setText(dayOfMonth + "/" + (monthOfYear1) + "/" + year);
                                Log.d("mylog", "2");
                            }

                            else {
                                txtDate.setText(dayOfMonth + "/" + (monthOfYear) + "/" + year);
                                Log.d("mylog", "3");
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            boolean check_hour = false;
                            boolean check_minute = false;
                            if(hourOfDay < 10){
                                Log.d("mylog", "1");
                                Log.d("mylog", String.valueOf(hourOfDay));
                                hour1 = "0" +String.valueOf(hourOfDay);
                                check_hour = true;
                            }
                            if (minute < 10){
                                Log.d("mylog", "2");
                                Log.d("mylog", String.valueOf(minute));
                                minute1 = "0" +String.valueOf(minute);
                                Log.d("mylog","change to"+ minute1);
                                check_minute = true;
                            }
                            if (check_hour && check_minute){
                                txtTime.setText(hour1 + ":" + minute1);
                            }
                            else if(check_hour && !check_minute){
                                txtTime.setText(hour1 +  ":"+ minute);
                            }
                            else if(!check_hour && check_minute){
                                txtTime.setText(hourOfDay +  ":"+ minute1);
                            }
                            else
                                txtTime.setText(hourOfDay + ":" + minute);



                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        if (v == btnADD) {
            boolean all_fill = true;

            if (txtTitle.getText().toString().equals("")) {
                Toast.makeText(this, "please add title text to Todo", Toast.LENGTH_SHORT).show();
                all_fill = false;
            }
            if (txtDescription.getText().toString().equals("")) {
                Toast.makeText(this, "please add description text to Todo", Toast.LENGTH_SHORT).show();
                all_fill = false;
            }
            if (!txtDate.getText().toString().equals("")) {
                if (!Check_validDate(txtDate.getText().toString())) {
                    Toast.makeText(this, "Not valid Date manually!!!", Toast.LENGTH_SHORT).show();
                    all_fill = false;
                } else
                    Toast.makeText(this, "valid Date was enterd !!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "please add date to Todo", Toast.LENGTH_SHORT).show();
                all_fill = false;
            }
            if (!txtTime.getText().toString().equals("")) {
                if (!Check_validTime(txtTime.getText().toString())) {
                    Toast.makeText(this, "Not valid time manually!!!", Toast.LENGTH_SHORT).show();
                    all_fill = false;
                } else
                    Toast.makeText(this, "  time was enterd !!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "please add time to Todo", Toast.LENGTH_SHORT).show();
                all_fill = false;
            }

            Log.d("mylog", "allFill is: "+String.valueOf(all_fill));
            if (all_fill == true) {
//                Toast.makeText(this, "All fields was entered correctly", Toast.LENGTH_SHORT).show(); //test
                addToDo();
                Toast.makeText(this, "Todo was ADDED", Toast.LENGTH_SHORT).show();

            }
        }
    }




}
