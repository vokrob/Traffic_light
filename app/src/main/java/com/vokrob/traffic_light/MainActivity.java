package com.vokrob.traffic_light;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout b_1, b_2, b_3;
    private Button button_1;
    private boolean start_stop = false;
    private int counter = 0;
    private Thread trafficLightThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b_1 = findViewById(R.id.bulb_1);
        b_2 = findViewById(R.id.bulb_2);
        b_3 = findViewById(R.id.bulb_3);
        button_1 = findViewById(R.id.button1);

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTrafficLight();
            }
        });
    }

    private void toggleTrafficLight() {
        if (!start_stop) {
            startTrafficLight();
        } else {
            stopTrafficLight();
        }
    }

    private void startTrafficLight() {
        button_1.setText("Stop");
        start_stop = true;
        trafficLightThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (start_stop) {
                    counter++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTrafficLight();
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        trafficLightThread.start();
    }

    private void stopTrafficLight() {
        start_stop = false;
        button_1.setText("Start");
        if (trafficLightThread != null) {
            trafficLightThread.interrupt();
        }
    }

    private void updateTrafficLight() {
        switch (counter % 3) {
            case 1:
                b_1.setBackgroundColor(getResources().getColor(R.color.red));
                b_2.setBackgroundColor(getResources().getColor(R.color.grey));
                b_3.setBackgroundColor(getResources().getColor(R.color.grey));
                break;
            case 2:
                b_1.setBackgroundColor(getResources().getColor(R.color.grey));
                b_2.setBackgroundColor(getResources().getColor(R.color.yellow));
                b_3.setBackgroundColor(getResources().getColor(R.color.grey));
                break;
            case 0:
                b_1.setBackgroundColor(getResources().getColor(R.color.grey));
                b_2.setBackgroundColor(getResources().getColor(R.color.grey));
                b_3.setBackgroundColor(getResources().getColor(R.color.green));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTrafficLight();
    }
}