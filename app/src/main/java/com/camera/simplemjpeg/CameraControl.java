package com.camera.simplemjpeg;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

/**
 * Created by peter on 11/17/15.
 */
public class CameraControl {
    public static String TAG = "wificamcontrol";
    public static int NO_COMMAND = -1;
    public static int UP = 0;
    public static int STOP = 1;
    public static int DOWN = 2;
    public static int LEFT = 4;
    public static int RIGHT = 6;
    public static int HORIZ_PATROL = 28;
    public static int VERT_PATROL = 26;
    public static int OFF = 95;
    public static int ON = 94;
    public static String COMMAND = "decoder_control.cgi?command=";
    public static String URL = "http://192.168.1.126:81/";
    public static String USERNAME = "****";
    public static String PASSWORD = "****";

    public static int PREVCOMMAND = NO_COMMAND;

    public static int getCommand(int inclination, int rotation) {
        if (!Util.isLaying(inclination) && !Util.isStanding(inclination)) {
            if (inclination < 85) {
                return DOWN;
            }

            if (inclination > 95) {
                return UP;
            }
        }

        return STOP;
    }

    public static String getUrl(int command) {
        return URL + COMMAND + command;
    }

    public static void sendCommand(int command) {
        if (command != PREVCOMMAND) {
            PREVCOMMAND = command;
            Log.d(TAG, "" + command);
            new SendHttpRequest().execute(getUrl(command), USERNAME, PASSWORD);
        }
    }

}

class SendHttpRequest extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpParams httpParams = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);

        try {
            httpclient.execute(Util.getRequest(strings[0], strings[1], strings[2]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
