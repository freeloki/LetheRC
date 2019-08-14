package com.codegenius.letherc;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.codegenius.letherc.connection.CameraControllerClient;
import com.codegenius.letherc.connection.TcpClient;


public class DashboardActivity extends AppCompatActivity implements OnTouchListener, LifecycleOwner {

    private static final String TAG = "LetheRC";
    private TcpClient client;
    private CameraControllerClient cameraClient;
    private Button upBtn, downBtn, rightBtn, leftBtn, cameraUpBtn, cameraDownBtn, cameraRightBtn, cameraLeftBtn, cameraDefaultBtn;
    private TextureView viewFinder;


    private OnTouchListener cameraOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (cameraClient != null && cameraClient.isConnected()) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        if (v.equals(cameraUpBtn)) {
                            Log.i(TAG, "Up button pressed.Moving camera.");
                            moveCamera("h");
                        } else if (v.equals(cameraDownBtn)) {
                            Log.i(TAG, "Down button pressed.Moving camera.");
                            moveCamera("k");
                        } else if (v.equals(cameraLeftBtn)) {
                            Log.i(TAG, "Left button pressed.Moving camera.");
                            moveCamera("u");
                        } else if (v.equals(cameraRightBtn)) {
                            Log.i(TAG, "Right button pressed.Moving camera.");
                            moveCamera("j");
                        } else if (v.equals(cameraDefaultBtn)) {
                            Log.i(TAG, "Default button pressed.Moving camera.");
                            moveCamera("b");
                            moveCamera("n");
                        }

                        return true;
                }
            }
            Log.e(TAG, "Camera is not connected.");
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUIComponents();

        cameraClient = new CameraControllerClient(getApplicationContext());
        cameraClient.start();


       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                client = new TcpClient(getApplicationContext());
                client.start();
            }
        },5000);*/


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (client != null) {
            client.close();
        }
        if (cameraClient != null) {
            cameraClient.close();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation/keyboard change
        super.onConfigurationChanged(newConfig);
    }

    private void initUIComponents() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature(Window.)
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        // wheel controllers
        upBtn = (Button) findViewById(R.id.forward_Btn);
        downBtn = (Button) findViewById(R.id.back_Btn);
        rightBtn = (Button) findViewById(R.id.right_Btn);
        leftBtn = (Button) findViewById(R.id.left_Btn);

        // camera controllers
        cameraDefaultBtn = (Button) findViewById(R.id.camera_default_btn);
        cameraDownBtn = (Button) findViewById(R.id.camera_down_btn);
        cameraUpBtn = (Button) findViewById(R.id.camera_up_btn);
        cameraLeftBtn = (Button) findViewById(R.id.camera_left_btn);
        cameraRightBtn = (Button) findViewById(R.id.camera_right_btn);

        // wheel controller listeners
        upBtn.setOnTouchListener(this);
        downBtn.setOnTouchListener(this);
        rightBtn.setOnTouchListener(this);
        leftBtn.setOnTouchListener(this);

        // camera controller listeners

        cameraDefaultBtn.setOnTouchListener(cameraOnTouchListener);
        cameraUpBtn.setOnTouchListener(cameraOnTouchListener);
        cameraDownBtn.setOnTouchListener(cameraOnTouchListener);
        cameraRightBtn.setOnTouchListener(cameraOnTouchListener);
        cameraLeftBtn.setOnTouchListener(cameraOnTouchListener);

        viewFinder = findViewById(R.id.view_finder);

        viewFinder.post(new Runnable() {
            @Override
            public void run() {
                startCamera();
            }
        });

        viewFinder.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                updateTransform();
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (client != null && client.isConnected()) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (v.equals(upBtn)) {
                        Log.i(TAG, "Up button pressed.Moving car");
                        moveCar("w");
                    } else if (v.equals(downBtn)) {
                        Log.i(TAG, "Down button pressed.Moving car.");
                        moveCar("s");
                    } else if (v.equals(leftBtn)) {
                        Log.i(TAG, "Left button pressed.Moving car.");
                        moveCar("a");
                    } else if (v.equals(rightBtn)) {
                        Log.i(TAG, "Right button pressed.Moving car.");
                        moveCar("d");
                    }

                    return true;
                case MotionEvent.ACTION_UP:

                    Log.i(TAG, "Button released.Stop car.");
                    moveCar("q");
                    return true;
            }

        }
        Log.e(TAG, "Car is not connected.");

        return false;
    }

    private void moveCar(String direction) {

        if (client != null && client.isConnected()) {
            client.writeMsg(direction);
        } else {
            Log.e(TAG, "moveCar: Client is not connected.");
        }

    }


    private void moveCamera(String direction) {

        if (cameraClient != null && cameraClient.isConnected()) {
            cameraClient.writeMsg(direction);
        } else {
            Log.e(TAG, "moveCamera: Camera is not connected.");
        }
    }

    private void startCamera() {
        // TODO: Implement CameraX operations
    }

    private void updateTransform() {
        // TODO: Implement camera viewfinder transformations
    }
}
