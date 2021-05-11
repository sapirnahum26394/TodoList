package com.sapirn_moshet.ex3;

public class AndroidFlavor {
    private String title;

    // Android version API
    private String description;

    // Drawable resource ID
    private String date;

    // Drawable resource ID
    private String time;


    /*
     * Create a new AndroidFlavor object.
     *
     * @param name is the name of the Android version (e.g. Gingerbread)
     * @param number is the corresponding Android version API level
     * @param image is drawable reference ID that corresponds to the Android version
     * */
    public AndroidFlavor(String title, String desc, String date, String time)
    {
        this.title = title;
        this.description = desc;
        this.date = date;
        this.time = time;
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

    /**
     * Get the image resource ID
     */
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }

}
