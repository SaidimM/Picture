package com.baidu.aip.asrwakeup3.core.mini;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.aip.asrwakeup3.core.R;
import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream;
import com.baidu.aip.asrwakeup3.core.wakeup.MyWakeup;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.IWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.RecogWakeupListener;
import com.baidu.aip.asrwakeup3.core.wakeup.listener.SimpleWakeupListener;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by fujiayi on 2017/8/15.
 */

public class ActivityMiniWakeUp extends AppCompatActivity implements EventListener {
    private static String DESC_TEXT = "精简版唤醒，带有SDK唤醒运行的最少代码，仅仅展示如何调用，\n" +
            "也可以用来反馈测试SDK输入参数及输出回调。\n" +
            "本示例需要自行根据文档填写参数，可以使用之前唤醒示例中的日志中的参数。\n" +
            "需要完整版请参见之前的唤醒示例。\n\n" +
            "唤醒词是纯离线功能，需要获取正式授权文件（与离线命令词的正式授权文件是同一个）。 第一次联网使用唤醒词功能自动获取正式授权文件。之后可以断网测试\n" +
            "请说“小度你好”或者 “百度一下”\n\n";

    private MyWakeup myWakeup;

    private boolean logTime = true;

    // 点击“开始识别”按钮
    // 基于DEMO唤醒词集成第2.1, 2.2 发送开始事件开始唤醒
    protected void start(IWakeupListener listener) {
        myWakeup = new MyWakeup(this, listener);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        params.put(SpeechConstant.APP_ID,18083036);
        params.put(SpeechConstant.APP_KEY,"myyVA6VsAbjCk5gx8dQIOPqU");
        params.put(SpeechConstant.SECRET,"fYgYRnQZLW1e6z5XwkaIDmKp6hmi5qRt");

//         params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_mini);
        initPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myWakeup.release();
    }

    //  基于SDK唤醒词集成1.2 自定义输出事件类 EventListener  回调方法
    // 基于SDK唤醒3.1 开始回调事件
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;
        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        printLog(logTxt);
    }

    private void printLog(String text) {
        if (logTime) {
            text += "  ;time=" + System.currentTimeMillis();
        }
        text += "\n";
        Log.i(getClass().getName(), text);
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
