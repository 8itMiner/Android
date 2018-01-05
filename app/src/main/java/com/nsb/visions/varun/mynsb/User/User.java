package com.nsb.visions.varun.mynsb.User;

/**
 * Created by varun on 30/12/2017.
 */


// User class passed into every activity
// Holds the current state for the user

public class User {

    public Integer studentID;
    public String fname;
    public String lname;
    public Integer year;

    public User(Integer StudentID, String Fname, String Lname, Integer Year) {
        this.studentID = StudentID;
        this.fname = Fname;
        this.lname = Lname;
        this.year = Year;
    }


    public String toString() {
        return studentID.toString() + " " + fname + " " + lname + " " + year.toString();
    }

    public void parseString(String data) throws Exception {
        // Split the string
        String dataArr[] = data.split(" ");

        try {
            this.studentID = Integer.parseInt(dataArr[0]);
            this.fname = dataArr[1];
            this.lname = dataArr[2];
            this.year = Integer.parseInt(dataArr[3]);
        } catch (NullPointerException e) {
            throw new Exception("Data is not valid");
        }
    }
}
