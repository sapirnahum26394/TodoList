package com.sapirn_moshet.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditorActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnDatePicker, btnTimePicker, btnADD;
    private TextView txtHeadLine;
    private EditText txtDate, txtTime, txtTitle, txtDescription;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String day1, monthOfYear1;
    private String hour1, minute1;
    private String user_name;
    public static final String MY_DB_NAME = "TodosDB"; //test
    private SQLiteDatabase todos = null;
    int id = 0;
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
        txtHeadLine = (TextView)findViewById(R.id.txtheadID);
        user_name = getIntent().getExtras().getString("user_name");

        String text = (getIntent().getExtras().getString("txtheadID"));
        text+=String.valueOf(getIntent().getExtras().getInt("id"));
        text+=")";
        if(!text.equals("null0)"))
        {
            txtHeadLine.setText(text);
            btnADD.setText("UPDATE");
        }else {
            txtHeadLine.setText("ADD new Todo");
        }



        txtTitle.setText(getIntent().getExtras().getString("title"));
        txtDescription.setText(getIntent().getExtras().getString("description"));
        txtDate.setText(getIntent().getExtras().getString("date"));
        txtTime.setText(getIntent().getExtras().getString("time"));
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnADD.setOnClickListener(this);
        todos = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);
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
                        if (strDate.charAt(0) == '0')
                        {
                            if (strDate.charAt(1) < '1') {
                                return false;
                            }
                        }

                        if (strDate.charAt(0) == '3'){
                            if (strDate.charAt(1) > '1') {
                                return false;
                            }

                        }
                        if (strDate.charAt(0) > '3'){
                            return false;
                        }
                    }
                    return true;
                }

            }
            return false;
        }
        return false;

    }



    public void addToDo()    {
        String sql;
        String btn_Action;
        btn_Action = getIntent().getExtras().getString("btn");
        String title = txtTitle.getText().toString();
        String description = txtDescription.getText().toString();
        String date = txtDate.getText().toString();
        String time = txtTime.getText().toString();

        String temp = date;
        temp = temp.concat(time);

        String newTemp= temp.replace("/", "");
        newTemp =  newTemp.replace(":", "");

        long date_and_time = Long.valueOf(newTemp);


        if(btn_Action!=null && btn_Action.equals("UPDATE")){
            int update_id = getIntent().getExtras().getInt("id");
            createAlarm(date_and_time,update_id);

            sql = "UPDATE todos SET title = "+"'"+title+"' , description = "+"'"+description+"' , datetime = "+"'"+date_and_time+"' WHERE _id = "+ update_id;
            todos.execSQL(sql);
            Toast.makeText(this, "Todo was UPDATED", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            String count = "SELECT * FROM todos";
            Cursor mcursor = todos.rawQuery(count, null);
            int icount = mcursor.getCount();
            if(icount>0) {
                Cursor cursor = todos.rawQuery("SELECT max(_id) FROM todos;", null);
                if (cursor != null && cursor.moveToFirst()) {
                    if (!cursor.isNull(0)) {
                        id = cursor.getInt(0);
                    }
                }
                id++;
            }

            createAlarm(date_and_time,id);

            sql = "INSERT INTO todos (_id, username, title, description, datetime) VALUES ('" + id  + "','"+ user_name  + "','" + title  + "', '" + description + "', '" +    date_and_time+ "');";
            todos.execSQL(sql);
            Toast.makeText(this, "ADDED was Todo", Toast.LENGTH_SHORT).show();
            finish();
        }

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

                            boolean check_day = false;
                            boolean check_month = false;
                            if(dayOfMonth < 10){

                                day1  = "0" + String.valueOf(dayOfMonth) ;
                                check_day =true;

                                //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                            if(monthOfYear < 9){
                                monthOfYear++;
                                monthOfYear1 = "0" +String.valueOf(monthOfYear);
                                check_month = true;
                            }
                            if (check_day && check_month){
                                txtDate.setText(day1 + "/" + (monthOfYear1) + "/" + year);
                            }
                            else if(check_day && !check_month){
                                txtDate.setText(day1 + "/" + (monthOfYear+1) + "/" + year);
                            }
                            else if(!check_day && check_month){
                                txtDate.setText(dayOfMonth + "/" + (monthOfYear1) + "/" + year);
                            }

                            else {
                                txtDate.setText(dayOfMonth + "/" + (monthOfYear) + "/" + year);
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
                                hour1 = "0" +String.valueOf(hourOfDay);
                                check_hour = true;
                            }
                            if (minute < 10){
                                minute1 = "0" +String.valueOf(minute);
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
            if (all_fill == true) {
                addToDo();
            }
        }
    }


    private void createAlarm(long date_and_time,int id) {

        // Get the System Alarm Manager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create Intent to call the BroadcastReceiver
        Intent alarmIntent = new Intent(this, AlarmClockReceiver.class);
        alarmIntent.putExtra("alarmType", "OneTime");
        alarmIntent.putExtra("alarmId", id);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String dateString = String.valueOf(date_and_time);

        if (dateString.length() < 12) //Adding leading Zero in from of string number
            dateString = '0' + dateString;
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmm");
        String currentDateandTime = sdf.format(new Date());

        try {
            Date currentdate = sdf.parse(currentDateandTime);
            Date date = sdf.parse(dateString);
            if(!date.before(currentdate)){
                long triggerTimeMS = date.getTime();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMS, alarmPendingIntent);
                }
            }
        }
        catch (Exception e) {
            Log.d("Error: ", String.valueOf(e));
        }

    }

}
