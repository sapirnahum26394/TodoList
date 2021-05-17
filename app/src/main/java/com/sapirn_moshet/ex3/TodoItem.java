package com.sapirn_moshet.ex3;

public class TodoItem {
    private String title;

    // Android version API
    private String description;

    // Drawable resource ID
  //  private String date;

    // Drawable resource ID
  //  private String time;
    private long datetime;
  //  private int datetime;
    private int id;


    /*
     * Create a new AndroidFlavor object.
     *
     * @param name is the name of the Android version (e.g. Gingerbread)
     * @param number is the corresponding Android version API level
     * @param image is drawable reference ID that corresponds to the Android version
     * */
   /*
    public TodoItem(int id, String title, String desc, String date, String time)
    {
        this.id = id;
        this.title = title;
        this.description = desc;
      //  this.date = date;
       // this.time = time;

    }
    */
    public TodoItem(int id, String title, String desc, long datetime)
  //  public TodoItem(int id, String title, String desc, int datetime)
    {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.datetime = datetime;
        // this.date = date;
        // this.time = time;
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
    public long getDateTime(){ return datetime; }
  //  public int getDateTime(){ return datetime; }
    /**
     * Get the image resource ID
     */
    /*
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }

     */
    public int getID(){ return id; }
}
