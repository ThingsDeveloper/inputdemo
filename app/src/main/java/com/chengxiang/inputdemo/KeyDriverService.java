package com.chengxiang.inputdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.userdriver.InputDriver;
import com.google.android.things.userdriver.UserDriverManager;

import java.io.IOException;

/**
 * 输入驱动服务，用户监听外部开关按钮A和B的开关，转换成Android中A和B字母的输入
 */
public class KeyDriverService extends Service {
    private static final String TAG = KeyDriverService.class.getSimpleName();

    private static final String A_DRIVER_NAME = "Akey";
    private static final int A_DRIVER_VERSION = 1;
    private static final int A_KEY_CODE = KeyEvent.KEYCODE_A;

    private static final String B_DRIVER_NAME = "Bkey";
    private static final int B_DRIVER_VERSION = 1;
    private static final int B_KEY_CODE = KeyEvent.KEYCODE_B;

    private static final String A_GPIO_NAME = "BCM5";
    private static final String B_GPIO_NAME = "BCM6";

    private UserDriverManager mUserDriverManager;
    private InputDriver mADriver;
    private InputDriver mBDriver;

    private Gpio mAGpio;
    private Gpio mBGpio;

    private GpioCallback mGpioCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            Log.d(TAG, "onGpioEdge");
            try {
                if (gpio == mAGpio) {
                    triggerEvent(mADriver, gpio.getValue(), A_KEY_CODE);
                } else if (gpio == mBGpio) {
                    triggerEvent(mBDriver, gpio.getValue(), B_KEY_CODE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            Log.w(TAG, gpio + ": Error event " + error);
        }
    };

    public KeyDriverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PeripheralManagerService manager = new PeripheralManagerService();
        try {
            mAGpio = manager.openGpio(A_GPIO_NAME);
            mAGpio.setDirection(Gpio.DIRECTION_IN);
            mAGpio.setActiveType(Gpio.ACTIVE_LOW);
            mAGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
            mAGpio.registerGpioCallback(mGpioCallback);

            mBGpio = manager.openGpio(B_GPIO_NAME);
            mBGpio.setDirection(Gpio.DIRECTION_IN);
            mBGpio.setActiveType(Gpio.ACTIVE_LOW);
            mBGpio.setEdgeTriggerType(Gpio.EDGE_BOTH);
            mBGpio.registerGpioCallback(mGpioCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mADriver = InputDriver.builder(InputDevice.SOURCE_CLASS_BUTTON).setName(A_DRIVER_NAME)
                .setVersion(A_DRIVER_VERSION).setKeys(new int[]{A_KEY_CODE}).build();
        mBDriver = InputDriver.builder(InputDevice.SOURCE_CLASS_BUTTON).setName(B_DRIVER_NAME)
                .setVersion(B_DRIVER_VERSION).setKeys(new int[]{B_KEY_CODE}).build();

        mUserDriverManager = UserDriverManager.getManager();
        mUserDriverManager.registerInputDriver(mADriver);
        mUserDriverManager.registerInputDriver(mBDriver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAGpio != null) {
            try {
                mAGpio.unregisterGpioCallback(mGpioCallback);
                mAGpio.close();
                mAGpio = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPIO", e);
            }
        }

        if (mBGpio != null) {
            try {
                mBGpio.unregisterGpioCallback(mGpioCallback);
                mBGpio.close();
                mBGpio = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPIO", e);
            }
        }

        mUserDriverManager.unregisterInputDriver(mADriver);
        mUserDriverManager.unregisterInputDriver(mBDriver);
    }

    private void triggerEvent(InputDriver inputDriver, boolean pressed, int keyCode) {
        Log.d(TAG, "triggerEvent");
        int action = pressed ? KeyEvent.ACTION_DOWN : KeyEvent.ACTION_UP;
        KeyEvent[] events = new KeyEvent[]{new KeyEvent(action, keyCode)};
        if (!inputDriver.emit(events)) {
            Log.w(TAG, "Unable to emit key event");
        }
    }
}
