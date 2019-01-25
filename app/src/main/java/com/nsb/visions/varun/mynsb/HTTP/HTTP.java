package com.nsb.visions.varun.mynsb.HTTP;

import android.content.Context;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.nsb.visions.varun.mynsb.Common.Util;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by varun on 5/01/2018. Coz varun is awesome as hell :)
 */

// HTTP class that uses stuff such as cookie managers and stuff and makes http requests easier
public class HTTP {

    // Key information
    public final static String API_URL = "https://mynsb.nsbvisions.com/api/v1";

    // Cookie manager
    private OkHttpClient client;
    private Context context;
    // Cache
    File httpCacheDirectory;


    // Interceptor
    private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (Util.isNetworkAvailable(context)) {
                int maxAge = 120; // read from cache for 2 minutes
                return originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
            }
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            return originalResponse.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                .build();
        }
    };


    public HTTP(Context context) {
        this.context = context;
        this.httpCacheDirectory = new File(context.getCacheDir(), "mynsb-http-data");

        // Setup the cookie jar
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this.context));

        //setup cache
        int cacheSize = 30 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        // Set up the okhttp client
        this.client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .cache(cache)
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .build();

    }





    public Response performRequest(Request request) throws Exception {
        // Perform the request
        Response response = client.newCall(request).execute();
        // Save the cookies
        return response;
    }
}
