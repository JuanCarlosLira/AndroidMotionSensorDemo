package motion.example.com.morionsensors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.Math.abs;

public class LeftActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private SensorEventListener gyroEventListener;

    private Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_left);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (gyroSensor == null){
            Toast.makeText(this, "This Device Has No Gyroscope !", Toast.LENGTH_SHORT).show();
            finish();
        }

        gyroEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (abs(event.values[2]) <= 2.0f) {
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                }

                if (event.values[2] < -2.0f) {
                    getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
                    goRight();
                }

                if (event.values[2] > 2.0f) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    //goLeft();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void vib() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(100);
        }
    }

    private void goRight() {
        Intent myIntent = new Intent(this, MainActivity.class);
        vib();
        startActivity(myIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroEventListener, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroEventListener);
    }
}
