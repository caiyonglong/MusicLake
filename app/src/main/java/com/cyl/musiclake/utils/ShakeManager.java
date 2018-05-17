package com.cyl.musiclake.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 15/9/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ShakeManager {

    private static ShakeManager sShakeManager;
    private Context mContext;
    private long mLastTime;
    private float lastX;
    private float lastY;
    private float lastZ;

    private ShakeManager(Context ctx) {
        mContext = ctx;
    }

    public static ShakeManager with(Context ctx) {
        if (sShakeManager == null
                || sShakeManager.mContext == null) {
            sShakeManager = new ShakeManager(ctx);
        }
        return sShakeManager;
    }


    private MySensor mySensor;

    public void startShakeListener(final ISensor iSensor) {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensorList.size() == 0) return;

        Sensor sensor = sensorList.get(0);

        mySensor = new MySensor(iSensor);
        sensorManager.registerListener(mySensor, sensor, SensorManager.SENSOR_DELAY_GAME);

    }

    private class MySensor implements SensorEventListener {

        private ISensor iSensor;

        public MySensor(ISensor iSensor) {
            this.iSensor = iSensor;
        }

        //传感器的数据发生变化会执行
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mLastTime == 0l) {
                //timestamp时间戳，就是很长的表示毫秒的一串数字
                mLastTime = event.timestamp;
                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            } else {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float force = Math.abs(x + y + z - lastX - lastY - lastZ);
                if (iSensor != null) {
                    iSensor.onSensorChange(force);
                }
                mLastTime = 0l;
            }
        }

        //当传感器的精确度改变会执行
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    public void cancel() {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        //取消传感器监听
        sensorManager.unregisterListener(mySensor);

    }

    public interface ISensor {
        void onSensorChange(float force);
    }

}
