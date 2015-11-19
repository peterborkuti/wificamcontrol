package com.camera.simplemjpeg;

import android.util.Base64;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by peter on 11/17/15.
 */
public class Util {
    public static String TAG = "CAMCTRL";

    public static double[] toDegrees(float... values) {
        double[] degrees = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            degrees[i] = Math.toDegrees(values[i]);
        }

        return degrees;
    }

    public static HttpUriRequest getRequest(String url, String userName, String password) {

        HttpUriRequest request = new HttpGet(url); // Or HttpPost(), depends on your needs

        if (userName != null && password != null && !"".equals(userName)) {
            String credentials = userName + ":" + password;
            Log.d(TAG, "credentials:" + credentials);
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        }

        return request;
    }

    public static boolean isLaying(int inclination) {
        return (inclination < 25) || (inclination > 155);
    }

    public static boolean isStanding(int inclination) {
        return (inclination > 75) && (inclination < 120);
    }
}
