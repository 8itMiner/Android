package com.nsb.visions.varun.mynsb.User;

/**
 * Created by varun on 30/12/2017.
 */


// User class passed into every activity
// Holds the current state for the user

public class User {

    private Integer studentID;
    private String fname;
    private String lname;
    private Integer year;



    // Constructor
    public User(Integer StudentID, String Fname, String Lname, Integer Year) {
        this.studentID = StudentID;
        this.fname = Fname;
        this.lname = Lname;
        this.year = Year;
    }




    // toString converts a student's data into a string
    public String toString() {
        return studentID.toString() + " " + fname + " " + lname + " " + year.toString();
    }
}
