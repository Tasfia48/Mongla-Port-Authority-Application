package com.example.nur.monglaport.Class;

/**
 * Created by nur on 11/28/16.
 */

public class Teleph {
    private String designation;
    private String tel;


    public Teleph() {
        /**
         * Blank default constructor essential for Firebase
         * */
    }

    //Getters and setters



    public String getDesignation() {

        return designation;
    }

    public void setDesignation(String designation)
    {

        this.designation=designation;
    }

    public String getTel() {

        return tel;
    }

    public void setTel(String tel)
    {

        this.tel=tel;
    }
}
