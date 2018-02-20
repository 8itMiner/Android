package com.nsb.visions.varun.mynsb.Exception;

public enum MyNSBExceptionType {
    SETTINGS_MISSING_FIELDS(0, "Some fields were empty, please fill in missing fields.");

    final int code;
    final String message;

    MyNSBExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
