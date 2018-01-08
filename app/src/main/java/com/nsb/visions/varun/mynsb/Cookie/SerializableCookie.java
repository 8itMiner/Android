package com.nsb.visions.varun.mynsb.Cookie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.*;
import okhttp3.Cookie;

/**
 * Created by varun on 8/01/2018. Coz varun is awesome as hell :)
 */

// Custom cookie wrapper that
public class SerializableCookie implements Serializable {

    private Cookie cookie;

    private static final long serialVersionUID = 2532101328282342578L;

    public SerializableCookie(okhttp3.Cookie cookie) {
        this.cookie = cookie;
    }

    public Cookie getCookie() {
        return this.cookie;
    }


    // Serializable cookie functions
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.name());
        out.writeObject(cookie.value());
        out.writeLong(cookie.expiresAt());
        out.writeObject(cookie.domain());
        out.writeObject(cookie.path());
        out.writeBoolean(cookie.secure());
        out.writeBoolean(cookie.httpOnly());
        out.writeBoolean(cookie.hostOnly());
        out.writeBoolean(cookie.persistent());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Cookie.Builder builder = new Cookie.Builder()
                .name((String) in.readObject())
                .value((String) in.readObject())
                .expiresAt(in.readLong())
                .domain((String) in.readObject())
                .path((String) in.readObject())
                .httpOnly();

        // Set up cookie
        Cookie cookie = builder.build();
        this.cookie = cookie;
    }


}
