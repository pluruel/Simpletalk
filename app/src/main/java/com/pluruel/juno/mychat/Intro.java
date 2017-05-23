package com.pluruel.juno.mychat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pluruel.juno.mychat.Managers.PopupManager;
import com.pluruel.juno.mychat.Managers.Turn_down_Manager;
import com.pluruel.juno.mychat.Methods.Ad_class;
import com.pluruel.juno.mychat.Methods.IntroText_Returner;
import com.pluruel.juno.mychat.Methods.Popup_Connection_Error;
import com.pluruel.juno.mychat.Managers.NetworkManager;
import com.pluruel.juno.mychat.Methods.Progress_Dialogs;
import com.pluruel.juno.mychat.Methods.RecvThd;
import com.pluruel.juno.mychat.Methods.SizeRuler;
import com.pluruel.juno.mychat.Managers.ThreadManager;
import com.pluruel.juno.mychat.Variables.Header;


public class Intro extends Activity {
    static final int ACTIVITY_FAILED_RESULT = 12343;

    private Progress_Dialogs mProgressDialog;
    private static Context mContext;
    private SizeRuler mSizeRuler;
    private ImageView mImageView;
    private TimerThread mTimerThread;
    private NetworkManager mNetworkManager; 
    private ThreadManager mThreadManager;
    private PopupManager mPopupManager;
    private boolean Should_Popup = false;
    private boolean show_mainActivity = true;
    private boolean timer_flag = false;
    private TextView mTextview = null;
    private IntroText_Returner mIntro_Text_Returner;
    private Turn_down_Manager mTurn_down_app = null;
    private RecvThd mRecvThd = null;
    private Ad_class mAd_class = null;
    private LinearLayout mLinearLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mIntro_Text_Returner = IntroText_Returner.getInstance();
        mImageView = (ImageView)findViewById(R.id.imageView);
        mContext = this;
        mSizeRuler = SizeRuler.getInstance();
        mNetworkManager = NetworkManager.getInstance();
        mThreadManager = ThreadManager.getInstance();
        mPopupManager = PopupManager.getInstance();
        mPopupManager.setContext(mContext);
        mTextview = (TextView) findViewById(R.id.textView_intro);
        mAd_class = Ad_class.getInstance();
        mAd_class.set_mContext(mContext);
        mPopupManager.Confirm_Popup();
        mTimerThread = new TimerThread();
        mTimerThread.start();
        mNetworkManager.connectServer();

        mProgressDialog = Progress_Dialogs.getInstance(mContext, 1);

        mTurn_down_app = Turn_down_Manager.getinstance();
        mTurn_down_app.SetContext(this);
        mSizeRuler.get_size_of_display(mImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_FAILED_RESULT)
        {
            if(resultCode == RESULT_OK)
            {
                if(mTimerThread.isAlive())
                    mTimerThread.interrupt();
                mThreadManager.init();
                mNetworkManager.init();
                mPopupManager.Reject_Popup();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }


    public static Context getintroContext(){
        return mContext;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPopupManager.Confirm_Popup();
        if(Should_Popup){
            Intent intent = new Intent(mContext, Popup_Connection_Error.class);
            intent.putExtra("ERROR_CODE", 1);
            startActivityForResult(intent, ACTIVITY_FAILED_RESULT);
            Should_Popup = false;
        }
        if (timer_flag) {
            mTimerThread = new TimerThread();
            mTimerThread.start();
        }
        show_mainActivity = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPopupManager.Reject_Popup();
        if(mTimerThread.isAlive())
            mTimerThread.interrupt();
        mTimerThread = null;
        show_mainActivity = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class TimerThread extends Thread {
        private long start_time = -1;
        private long end_time = -1;
        private int counter;
        @Override
        public void run() {
            super.run();
            counter = 0;
            while(end_time == mNetworkManager.get_time_end_connect())
            {
                if(counter++ >= 50)
                {
                    if (mPopupManager.Possibility_Popup()) {
                        //mProgressDialog.dismissProgressDialog();
                        Intent intent = new Intent(mContext, Popup_Connection_Error.class);
                        intent.putExtra("ERROR_CODE", 1);
                        startActivityForResult(intent, ACTIVITY_FAILED_RESULT);
                        mHandeler.sendEmptyMessage(-1);
                        return;
                    }
                    else {
                        Should_Popup = true;
                        return;
                    }
                }
                try {
                    sleep(100);
                }
                catch (InterruptedException e){
                    timer_flag = true;
                    return;
                }
            }
            start_time = mNetworkManager.get_time_start_connect();
            end_time = mNetworkManager.get_time_end_connect();
            mRecvThd = mThreadManager.getRecvTrd();
            int countdeb = 0;
            while(!mNetworkManager.isConnected())
            {
                if(countdeb > 50)
                {
                    Intent intent = new Intent(mContext, Popup_Connection_Error.class);
                    intent.putExtra("ERROR_CODE", 1);
                    startActivityForResult(intent, ACTIVITY_FAILED_RESULT);
                    mHandeler.sendEmptyMessage(-1);
                    return;
                }
                try{
                    sleep(100);
                    countdeb++;
                } catch (InterruptedException e) {
                    timer_flag = true;
                    e.printStackTrace();
                    return;
                }
            }
            if(end_time - start_time <= 3000) {

                long waiting_time = 3000 - (end_time - start_time);
                long fixed_time = waiting_time /= 100;
                fixed_time *= 100;
                if (mRecvThd.getNewest_ver() != Header.return_currentver()){
                    Intent intent = new Intent(mContext, Popup_Connection_Error.class);
                    intent.putExtra("ERROR_CODE", 4);
                    startActivityForResult(intent, ACTIVITY_FAILED_RESULT);
                    return;
                }
                while(fixed_time >= 0) {
                    fixed_time -= 100;
                    if (fixed_time % 650 == 0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String temp = mIntro_Text_Returner.Return_text();
                                mTextview.setText(temp);
                                return;
                            }
                        });

                    }
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        timer_flag = true;
                        return;
                    }
                }
            }
            if (show_mainActivity)
                mHandeler.sendEmptyMessage(0);

        }
    }
    private Handler mHandeler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what)
            {
                case 0:
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
