package com.pluruel.juno.mychat.Methods;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pluruel.juno.mychat.Managers.PopupManager;


/**
 * Created by Juno on 2016-12-20.
 */

public class Progress_Dialogs {
    private static Progress_Dialogs mProgress_Dialogs = null;
    private static ProgressDialog mProgressDialog = null;
    private static int selector = 0;
    private static boolean Run_adv = false;
    private static boolean shouldRunning = false;
    private static boolean isRunning = false;
    private static boolean show_Toast = false;
    private static SizeRuler mSizeRuler = null;
    private static PopupManager mPopupManager;
    private int searching_amount = 1;
    private static Ad_class mAd_class = null;
    private static BackPressClose mBackPressClose = null;
    private String Progress_Message[] = {"버그 엉엉",
                                         "서버에 접속 중이에요!\n잠시만 기다려 주세요!",
                                         "새로운 파트너 기다리는 중!",
                                         "재접속 시도 중!"};
    private static Context mContext = null;
    private LinearLayout mLinearLayout = null;


    private Progress_Dialogs(){

    }

    public boolean isRunning(){return isRunning;}
    public boolean shouldRunning(){
        return shouldRunning;
    }
    public boolean shouldToast(){
        return show_Toast;
    }

    public void init(){
        if(shouldRunning)
            dismissProgressDialog();
        selector = 0;
        mContext = null;
        mProgress_Dialogs = null;
    }
    public static Progress_Dialogs getInstance(Context _c, int _s){
        if (mContext != _c || selector != _s) {
            mProgress_Dialogs = new Progress_Dialogs();
            mContext = _c;
            selector = _s;
            mSizeRuler = SizeRuler.getInstance();
            if (selector == 2) Run_adv = true;
            mProgressDialog = new ProgressDialog(mContext);
            mPopupManager = PopupManager.getInstance();
            mAd_class = Ad_class.getInstance();
            mAd_class.setFullAd();
        }
        return mProgress_Dialogs;
    }
    public static Progress_Dialogs returner(){
        return mProgress_Dialogs;
    }
    public void showProgressDialog() {
        shouldRunning = true;
        if (mPopupManager.Possibility_Popup()) {
            if (searching_amount % 2 == 0) {
                ProgressDialog_runner.sendEmptyMessage(3);
                mPopupManager.Reject_Popup();
                searching_amount++;
                return;
            }
            else{
                isRunning = true;
                ProgressDialog_runner.sendEmptyMessage(0);
                Log.d("몇이냐", String.valueOf(searching_amount));
                searching_amount++;
                return;
            }
        }
    }

    public void showToast() {
        show_Toast = true;
        if (mPopupManager.Possibility_Popup()) {
            show_Toast = false;
            ProgressDialog_runner.sendEmptyMessage(2);
        }
    }

    public void dismissProgressDialog()
    {
        isRunning = false;
        shouldRunning = false;
        mProgressDialog.dismiss();
    }

    public void Stop_ProgressDialog(){
        isRunning = false;
        mProgressDialog.dismiss();
    }


    private Handler ProgressDialog_runner = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setMessage(Progress_Message[selector]);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mBackPressClose = new BackPressClose(mProgressDialog.getContext());
                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mProgressDialog.show();
                            mBackPressClose.onBackPressed();
                        }
                    });
                    mProgressDialog.show();
                    break;
                case 2:
                    Toast toast = Toast.makeText(mContext, "새로운 파트너와 연결되었습니다.", Toast.LENGTH_SHORT);
                    int Offset_Y = mSizeRuler.get_height_size()/5;
                    toast.setGravity(Gravity.TOP, 0, Offset_Y);
                    toast.show();
                    break;
                case 3:
                    mAd_class.showFullAd();
                default:
                    break;
            }
        }
    };
}
