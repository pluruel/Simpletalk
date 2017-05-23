package com.pluruel.juno.mychat.Methods;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pluruel.juno.mychat.Managers.NetworkManager;
import com.pluruel.juno.mychat.Managers.ThreadManager;
import com.pluruel.juno.mychat.R;
import com.pluruel.juno.mychat.Variables.Header;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Juno on 2017-01-12.
 */

public class Popup_matching_new_partner extends Activity implements View.OnClickListener{
    OutputStream mOutputstrem = null;
    NetworkManager mNetworkManager = null;
    private RecvThd mRecvThd = null;
    private ThreadManager mThreadManager = null;
    private static Activity mActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        mLayoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(mLayoutParams);
        mActivity = this;
        setContentView(R.layout.matching_new_partner);
        mNetworkManager = NetworkManager.getInstance();
        mOutputstrem = mNetworkManager.getOutputStream();
        mThreadManager = ThreadManager.getInstance();
        mRecvThd = mThreadManager.getRecvTrd();
        findViewById(R.id.matching_new_confirm).setOnClickListener(this);
        findViewById(R.id.matching_new_cancle).setOnClickListener(this);
    }

    public static Activity return_Pmnp_Activity(){
        return mActivity;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.matching_new_confirm:
                if(mRecvThd.isSandable()) {
                    byte sendbuf[] = Functions.making_packet_sys(Header.MyStrCode.Find_New_partner);
                    try {
                        mOutputstrem.write(sendbuf, 0, sendbuf.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mActivity = null;
                finish();
                break;
            case R.id.matching_new_cancle:
                mActivity = null;
                finish();
                break;
        }
    }
}
