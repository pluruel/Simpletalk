package com.pluruel.juno.mychat.Methods;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pluruel.juno.mychat.R;

/**
 * Created by Juno on 2016-12-02.
 */

public class Length_Error_Dialog extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        mLayoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(mLayoutParams);
        setContentView(R.layout.length_error_dialog);
        findViewById(R.id.Length_dialog_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Length_dialog_btn:
                this.finish();
                break;
        }
    }

}
