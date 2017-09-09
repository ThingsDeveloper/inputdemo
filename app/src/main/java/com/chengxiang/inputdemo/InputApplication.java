package com.chengxiang.inputdemo;

import android.app.Application;
import android.content.Intent;

/**
 * Created by chengxiang.peng on 2017/3/9.
 */
public class InputApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //启动输入驱动服务
        Intent intent = new Intent(this, KeyDriverService.class);
        startService(intent);
    }
}
