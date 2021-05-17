package com.sapirn_moshet.ex3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {
    private String title;
    private String description;
    private String dateTime;
    private int id;
    private SimpleDateFormat dateTimeFormatter,dateFormatter,hourFormatter;
    private Date d;

    public TodoItem(int id, String title, String desc, String dateTime)
    {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.dateTime = dateTime;
        dateTimeFormatter = new SimpleDateFormat("yyyyMMddHHmm");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        hourFormatter = new SimpleDateFormat("HH:mm");
        try {
            d = dateTimeFormatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return dateFormatter.format(d);
    }
    public String getTime() {
        return hourFormatter.format(d);
    }
    public int getID(){ return id; }
}
