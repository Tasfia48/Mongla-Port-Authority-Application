package com.example.nur.monglaport.Class;

/**
 * Created by nur on 11/30/16.
 */

public class Tide {
    private String date;
    private String river;
    private String time;
    private String height;


    public Tide() {
        /**
         * Blank default constructor essential for Firebase
         * */
    }

    //Getters and setters



    public String getDate() {

        return date;
    }

    public void setDate(String date)
    {

        this.date=date;
    }

    public String getRiver() {

        return river;
    }

    public void setRiver(String river)
    {

        this.river=river;
    }


    public String getTime() {

        return time;
    }

    public void setTime(String time)
    {

        this.time=time;
    }
    public String getHeight() {

        return height;
    }

    public void setHeight(String height)
    {

        this.height=height;
    }

}
