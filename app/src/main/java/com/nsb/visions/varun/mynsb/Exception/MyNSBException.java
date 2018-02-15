package com.nsb.visions.varun.mynsb.Exception;

public class MyNSBException extends Exception {
    private MyNSBExceptionType type;

    public MyNSBException(MyNSBExceptionType type) {
        this.type = type;
    }

    public String toString() {
        return this.type.toString();
    }
}
