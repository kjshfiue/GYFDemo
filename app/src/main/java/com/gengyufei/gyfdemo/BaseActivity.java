package com.gengyufei.gyfdemo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by FineFan on 2017/6/12.
 */

public class BaseActivity extends AppCompatActivity {
    public Toast mToast;

    protected AppCompatActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        mActivity = this;
    }

    /**
     * 弹出toast提示框
     *
     * @param message 要弹出提示的文字
     */
    public void toast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    public void onBack(View view){
        finish();
    }
}
