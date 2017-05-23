package com.pluruel.juno.mychat.Methods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pluruel.juno.mychat.R;

/**
 * Created by Juno on 2017-01-19.
 */

public class Titlebar_info extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        mLayoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(mLayoutParams);
        setContentView(R.layout.info);
        findViewById(R.id.activity_info).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_info:
                this.finish();
                break;
        }
    }
}
