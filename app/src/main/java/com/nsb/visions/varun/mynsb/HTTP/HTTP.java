package com.nsb.visions.varun.mynsb.HTTP;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by varun on 5/01/2018.
 */

// HTTP class that uses stuff such as cookie managers and stuff and makes http requests easier
public class HTTP {

    // Key information
    public final static String API_URL = "https://mynsb.nsbvisions.com/api/v1";
    private OkHttpClient client;
    public ClearableCookieJar cookieJar;
    private Context context;
    private File httpCacheDirectory;

    // Interceptor
    private final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (HTTP.validConnection(context)) {
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
    // Perms
    public final static int GET = 0;
    public final static int POST = 1;



    // Constructor
    public HTTP(Context context) {
        this.context = context;
        this.httpCacheDirectory = new File(context.getCacheDir(), "mynsb-http-data");
        this.cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this.context));
        // Build the cache
        int cacheSize = 30 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        // Build client
        this.client = new OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .cache(cache)
            .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .build();

    }




    // validConnection tests weather the student's current connection is actually usable and can connect to the API
    public static boolean validConnection(Context context) {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean activeConnection = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        // Determine if the API is down or if we can even get to it
        HTTP requestHander = new HTTP(context);
        int respCode = 0;
        try {
            respCode = requestHander.performRequest(
                requestHander.buildRequest(HTTP.GET, "/", null), false).code();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activeConnection && respCode == 200;
    }




    // buildRequest builds a request object given a set of requirements, requestType is the type of request you want (see perm variables), endpoint is the API endpoint and params are the parameters
    // to attach to the request
    // Function returns a builder to allow us to add additional custom headers if we please
    public Request.Builder buildRequest(int requestType, String endpoint, String[] params) {

        Request.Builder request = new Request.Builder()
            .header("Connection", "close");

        // Deal with the request
        switch (requestType) {
            case HTTP.GET:
                // Convert param array to string to tack onto http request
                String str = "";
                // If the params array is null then ignore this step and don't parse the parameters
                if (params != null) {
                    // Iterate over params, attach that index to the param string, index looks like: var=x
                    StringBuilder builder = new StringBuilder();
                    for (String s : params) {
                        builder.append("?").append(s);
                    }
                    str = builder.toString();
                }

                // Build request
                request = request
                    .url(HTTP.API_URL + endpoint + "&" + str)
                    .get();
                break;
            case HTTP.POST:
                // Build request body to tack onto request
                RequestBody requestBody = RequestBody.create(null, new byte[0]);

                // If the param array is null then skip this and send an empty body to the API
                if (params != null) {
                    FormBody.Builder requestParams = new FormBody.Builder();
                    // Iterate over params and break them by the first instance of the ='s sign e.g x=321321=dsadas turns into x, 321321=dsadas where token[0] is x and token[1] is 321321=dsadas
                    for (String s : params) {
                        try {
                            // Split the string
                            String tokens[] = s.split("=", 2);
                            // Token 0 is the var name, token 1 is the value
                            requestParams.addEncoded(tokens[0], tokens[1]);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    requestBody = requestParams.build();
                }

                // Build post request
                request = request
                    .url(HTTP.API_URL + endpoint)
                    .post(requestBody);
                break;
            default:
                return null;
        }


        // Return the final request builder
        return request;
    }




    // performRequest builds and send the given HTTP request
    // The method also allows for asynchronous requests with the async flag.
    public Response performRequest(Request.Builder requestBuilder, boolean async) throws IOException, InterruptedException {

        // Build the request
        Request request = requestBuilder.build();
        final Response[] response = new Response[1];
        final IOException[] exception = new IOException[1];

        // Perform the request on a new thread
        Thread runner = new Thread(() -> {
            try {
                response[0] = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                exception[0] = e;
            }
        });
        runner.start();

        // If async is off then wait for thread to finish if on then just leave it
        if (!async) {
            runner.join();
            if (exception[0] != null) {
                throw exception[0];
            }
        }

        // Return the response
        return response[0];
    }




    // Exceptions
    public static class HTTPError extends Exception {
        public HTTPError(String message) {
            super(message);
        }
    }
    public static class HTTPUserError extends Exception {
        public HTTPUserError(String message) {
            super(message);
        }
    }
}
