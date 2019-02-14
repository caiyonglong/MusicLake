package com.cyl.musiclake.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 15/9/23
 * * * * * * * * * * * * * * * * * * * * * * *
 */
class ShakeManager(private val mContext: Context?) {
    private var mLastTime: Long = 0
    private var lastX: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    private var lastZ: Float = 0.toFloat()


    private var mySensor: MySensor? = null

    fun startShakeListener(iSensor: ISensor) {
        val sensorManager = mContext!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)
        if (sensorList.size == 0) return

        val sensor = sensorList[0]

        mySensor = MySensor(iSensor)
        sensorManager.registerListener(mySensor, sensor, SensorManager.SENSOR_DELAY_GAME)

    }

    private inner class MySensor(private val iSensor: ISensor?) : SensorEventListener {

        //传感器的数据发生变化会执行
        override fun onSensorChanged(event: SensorEvent) {
            if (mLastTime == 0L) {
                //timestamp时间戳，就是很长的表示毫秒的一串数字
                mLastTime = event.timestamp
                lastX = event.values[0]
                lastY = event.values[1]
                lastZ = event.values[2]
            } else {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val force = Math.abs(x + y + z - lastX - lastY - lastZ)
                iSensor?.onSensorChange(force)
                mLastTime = 0L
            }
        }

        //当传感器的精确度改变会执行
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }
    }


    fun cancel() {
        val sensorManager = mContext!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //取消传感器监听
        sensorManager.unregisterListener(mySensor)

    }

    interface ISensor {
        fun onSensorChange(force: Float)
    }

    companion object {

        private var sShakeManager: ShakeManager? = null

        fun with(ctx: Context): ShakeManager {
            if (sShakeManager == null || sShakeManager!!.mContext == null) {
                sShakeManager = ShakeManager(ctx)
            }
            return sShakeManager as ShakeManager
        }
    }

}
