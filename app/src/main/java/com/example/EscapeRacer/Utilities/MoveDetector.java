package com.example.EscapeRacer.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.EscapeRacer.Interfaces.MoveCallback;


public class MoveDetector {
    private final SensorManager sensorManager;
    private final Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long timestamp = 0;

    private final MoveCallback moveCallback;

    public MoveDetector(Context context, MoveCallback moveCallback) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                calculateMove(x);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //pass
            }
        };
    }

    private void calculateMove(float x) {
        if (System.currentTimeMillis() - timestamp > 100) {
            timestamp = System.currentTimeMillis();

            float threshold = 1.5f;

            if (x < -threshold) {
                if (moveCallback != null) {
                    moveCallback.moveRight();
                }
            } else if (x > threshold) {
                if (moveCallback != null) {
                    moveCallback.moveLeft();
                }
            }
        }
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );

    }
}
