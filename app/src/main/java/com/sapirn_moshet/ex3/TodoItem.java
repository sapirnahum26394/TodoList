package com.sapirn_moshet.ex3;


import android.util.Log;

public class TodoItem {
    private String title;

    private String description;

    private long datetime;

    private int id;


    /*
     * Create a new AndroidFlavor object.
     *
     * @param name is the name of the Android version (e.g. Gingerbread)
     * @param number is the corresponding Android version API level
     * @param image is drawable reference ID that corresponds to the Android version
     * */

    public TodoItem(int id, String title, String desc, long datetime)
  //  public TodoItem(int id, String title, String desc, int datetime)
    {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.datetime = datetime;
    }
    /**
     * Get the name of the Android version
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the Android version API Level
     */
    public String getDescription() {
        return description;
    }

    public String getTime(long l) {
        if(l!=0) {

            String s = String.valueOf(l);
            if (s.length() < 12) //Adding leading Zero in from of string number
                s = '0' + s;

            return s.substring(8, 10) + ":" + s.substring(10, s.length());
        }
        return "";
    }

    public long getDateTime(){ return datetime; }
    public String getDate(long l) {
        if(l!=0) {
            String s = String.valueOf(l);
            if (s.length() < 12) //Adding leading Zero in from of string number
                s = '0' + s;
            return s.substring(0, 2) + "/" + s.substring(2, 4) + "/" + s.substring(4, 8);
        }
        return "";
    }
    public int getID(){ return id; }
}
