package test.gameapp.CONTROLLER.IO;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import test.gameapp.MODEL.DATA.PlayerInfo;

/**
 * Created by Marcus on 2016-12-27.
 */


/**
 *  Stegräknar service som lägger till 50 guld till spelare när mobilen registrerat 50 steg
 *  Använder standardlösningar med kod tagen från internet.
 * */
public class StepCounter implements SensorEventListener {

    private Context context;
    private SensorManager sensorManager;
    private long firstStepValue;
    private IOManager ioManager;

    public StepCounter(Context context){
        ioManager = new IOManager(context);
        if(ioManager.load() == null){
            ioManager.save(new PlayerInfo());
        }

        firstStepValue = -1;
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

    }

    public void resume(){
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    private static long walkedSteps;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(firstStepValue == -1f){
            firstStepValue = (long) event.values[0];
        }
        walkedSteps = (long) (event.values[0] - firstStepValue);
        if(walkedSteps % 50 == 0 && walkedSteps != 0){
            ioManager.addSteps(50); //lägg till 50 guld
        }
      //  Log.i("STEPTEST1", "test:" + String.valueOf(walkedSteps) + "  bank: " + ioManager.getGold());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
