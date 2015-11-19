package com.camera.simplemjpeg;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Created by peter on 11/8/15.
 */
public class SensorTest implements SensorEventListener {
    private static final String TAG = "wificamcontrol";

    private boolean ON = true;

    private final Context mContext;
    private boolean listenersSet = false;

    public SensorTest(Context mContext) {
        Log.d(TAG, "started");
        this.mContext = mContext;
    }

    public void listSensors() {
        SensorManager mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        for(Sensor s: deviceSensors) {
            Log.d(TAG, "Sensor:" + s.getName() + " - " + s);
        }
    }


    private Sensor getDefaultAccelerometer() {
        SensorManager mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        Sensor s = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (s != null) {
            Log.d(TAG, "default accelerometer:" + s);
        }
        else {
            Log.d(TAG, "NO default accelerometer");
        }

        return s;
    }

    public void setListeners() {
        if (!listenersSet) {
            Sensor s = getDefaultAccelerometer();
            if (s != null && ON) {
                SensorManager mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

                mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

                listenersSet = true;
            }
        }
    }

    public void unsetListeners() {
        if (listenersSet) {
            SensorManager mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
            mSensorManager.unregisterListener(this);
            listenersSet = false;
        }
    }

    //http://stackoverflow.com/questions/11175599/how-to-measure-the-tilt-of-the-phone-in-xy-plane-using-accelerometer-in-android
    public int computeTilt(float... values) {
        float[] g = new float[3];
        g = values.clone();

        float norm_Of_g = (float)Math.sqrt(g[0] * g[0] + g[1] * g[1] + g[2] * g[2]);

        // Normalize the accelerometer vector
        g[0] = g[0] / norm_Of_g;
        g[1] = g[1] / norm_Of_g;
        g[2] = g[2] / norm_Of_g;

        int inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));
        Log.d(TAG, "inclination:" + inclination);

        if (Util.isLaying(inclination))
        {
            // device is flat
            //   For the case of laying flat, you have to use a compass to see how much the device is
            //   rotating from the starting position.
            //Log.d(TAG, "device is lying flat");
        }
        else
        {
            // device is not flat
            int rotation = (int) Math.round(Math.toDegrees(Math.atan2(g[0], g[1])));
            Log.d(TAG, "rotation:" + rotation);
        }

        return inclination;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        int command = CameraControl.getCommand(computeTilt(event.values), 0);
        Log.d(TAG, "command:" + command);
        CameraControl.sendCommand(command);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Accuracy changed:" + accuracy);
    }
}
