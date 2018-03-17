package com.example.nur.monglaport.Class;

/**
 * Created by nur on 11/30/16.
 */

public class Fpofficer {
    private String service;
    private String officer;


    public Fpofficer() {
        /**
         * Blank default constructor essential for Firebase
         * */
    }

    //Getters and setters



    public String getService() {

        return service;
    }

    public void setService(String service)
    {

        this.service=service;
    }

    public String getOfficer() {

        return officer;
    }

    public void setOfficer(String officer)
    {

        this.officer=officer;
    }
}
