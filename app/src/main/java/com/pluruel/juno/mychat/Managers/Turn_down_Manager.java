package com.pluruel.juno.mychat.Managers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.pluruel.juno.mychat.Methods.Encrypt_class;
import com.pluruel.juno.mychat.Methods.Progress_Dialogs;
import com.pluruel.juno.mychat.Methods.SizeRuler;

import static java.lang.Thread.sleep;

/**
 * Created by Juno on 2017-01-12.
 */

public class Turn_down_Manager {
    private static Turn_down_Manager mTurn_down_app = null;
    private Context mContext;
    private PopupManager mPopupManager = null;
    private NetworkManager mNetworkManager = null;
    private ThreadManager mThreadManager = null;
    private Encrypt_class mEncrypt_class = null;
    private Progress_Dialogs mProgress_Dialogs = null;
    private SizeRuler mSizeRuler = null;
    private Activity mMainActivity;
    private timer_to_destroy mtimer_to_destroy = null;
    private boolean TurndownCalled = false;

    private Turn_down_Manager(){

    }
    public static Turn_down_Manager getinstance(){
        if (mTurn_down_app == null){
            mTurn_down_app = new Turn_down_Manager();
        }
        return mTurn_down_app;

    }
    public void SetContext(Activity _m){
        mPopupManager = PopupManager.getInstance();
        mNetworkManager = NetworkManager.getInstance();
        mThreadManager = ThreadManager.getInstance();
        mEncrypt_class = Encrypt_class.getInstance();
        mProgress_Dialogs = Progress_Dialogs.returner();
        mSizeRuler = SizeRuler.getInstance();
        mMainActivity = _m;
    }

    public void Turn_off_this_app(){
        TurndownCalled = true;
        mThreadManager.init();
        mEncrypt_class.init();
        mProgress_Dialogs.init();
        mPopupManager.init();
        mSizeRuler.init();
        mMainActivity.finish();
        mNetworkManager.init();

    }

    public void when_onPause(){
        if(mtimer_to_destroy == null) {
            mtimer_to_destroy = new timer_to_destroy();
            mtimer_to_destroy.start();
        }
    }

    public void when_onResume(){
        if(mtimer_to_destroy != null) {
            mtimer_to_destroy.interrupt();
            mtimer_to_destroy = null;
        }
    }


    private class timer_to_destroy extends Thread{
        @Override
        public void run() {
            super.run();
            int paused_time = 0;
            while (paused_time++ < 3000){
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
            Turn_off_this_app();
            return;
        }
    };



}
