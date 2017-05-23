package com.pluruel.juno.mychat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.pluruel.juno.mychat.Managers.PopupManager;
import com.pluruel.juno.mychat.Managers.Turn_down_Manager;
import com.pluruel.juno.mychat.Methods.Ad_class;
import com.pluruel.juno.mychat.Methods.BackPressClose;
import com.pluruel.juno.mychat.Methods.Functions;
import com.pluruel.juno.mychat.Methods.Chat_Handler;
import com.pluruel.juno.mychat.Managers.NetworkManager;
import com.pluruel.juno.mychat.Methods.Popup_Connection_Error;
import com.pluruel.juno.mychat.Methods.Popup_matching_new_partner;
import com.pluruel.juno.mychat.Methods.Progress_Dialogs;
import com.pluruel.juno.mychat.Methods.RecvThd;
import com.pluruel.juno.mychat.Managers.ThreadManager;
import com.pluruel.juno.mychat.Methods.Titlebar_info;
import com.pluruel.juno.mychat.Variables.Header;


import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity {
    private EditText mEditText;
    private Button mButton;
    private ImageView mImageView_refresh;
    private ImageView mImageView_info;
    private LinearLayout mLinearLayout;
    private LinearLayout mainLinearLayout;
    private ThreadManager mThreadmanager;
    private NetworkManager mNetworkManager;
    private OutputStream OS = null;
    private Context mContext = null;
    private ScrollView mScrollView;
    private RecvThd mRecvThd;
    private Chat_Handler mChat_Handler;
    private Progress_Dialogs mProgress_Dialog;
    private ThreadManager mThreadManager;
    private PopupManager mPopupManager = null;
    private Popup_Connection_Error mPopup_Connection_Error;
    private Turn_down_Manager mTurn_down_app;
    private Ad_class mAd_Class = null;
    private BackPressClose mBackPressClose = null;
    private MainActivity mMainActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        mContext = this;
        mMainActivity = this;
        mEditText = (EditText) findViewById(R.id.editText);
        mScrollView = (ScrollView) findViewById(R.id.ScrollView);
        mLinearLayout = (LinearLayout) findViewById(R.id.Linear);
        mainLinearLayout = (LinearLayout)findViewById(R.id.ll_main);
        mButton = (Button) findViewById(R.id.send_button);
        mImageView_refresh = (ImageView) findViewById(R.id.refresh);
        mImageView_info = (ImageView) findViewById(R.id.developer);
        mTurn_down_app = Turn_down_Manager.getinstance();
        mTurn_down_app.SetContext(this);
        mLinearLayout.setOnClickListener(Listner_);
        mScrollView.setOnClickListener(Listner_);
        mButton.setOnClickListener(listener);
        mImageView_refresh.setOnClickListener(Listner_find_new);
        mImageView_info.setOnClickListener(Listner_info);
        mNetworkManager = NetworkManager.getInstance();
        mNetworkManager.connectFinished();
        mThreadmanager = ThreadManager.getInstance();
        mRecvThd = mThreadmanager.getRecvTrd();
        mRecvThd.setContext(mContext);
        mRecvThd.setLinearLayout(mLinearLayout);
        mPopupManager = PopupManager.getInstance();
        mPopupManager.setContext(mContext);
        mPopupManager.Confirm_Popup();
        mChat_Handler = Chat_Handler.getInstance();
        mChat_Handler.setVarious(mContext, mLinearLayout, mScrollView);
        OS = mNetworkManager.getOutputStream();
        mProgress_Dialog = Progress_Dialogs.getInstance(mContext,2);
        mAd_Class = Ad_class.getInstance();
        mAd_Class.set_mContext(mContext);
        mBackPressClose = new BackPressClose(mMainActivity);

        if (savedInstanceState == null) {
            byte sendbuf[];
            mProgress_Dialog.showProgressDialog();
            sendbuf = Functions.making_packet_sys(Header.MyStrCode.Connection_confirm);
            try {
                OS.write(sendbuf, 0, sendbuf.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private View.OnClickListener Listner_ = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager ipm = (InputMethodManager)getSystemService(mContext.INPUT_METHOD_SERVICE);
            ipm.hideSoftInputFromWindow(mEditText.getWindowToken(),0);
        }
    };

    private View.OnClickListener Listner_find_new = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mRecvThd.isSandable()) {
                Intent intent = new Intent(mContext, Popup_matching_new_partner.class);
                startActivity(intent);
            }
        }
    };

    private View.OnClickListener Listner_info = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(mRecvThd.isSandable()) {
                Intent intent = new Intent(mContext, Titlebar_info.class);
                startActivity(intent);
            }
        }
    };

    //한글도 정상적으로 동작
    //서버에서의 제한 때문에 최대 길이는 500으로 고정
   private View.OnClickListener listener = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            if (OS != null) {
                if (mRecvThd.isSandable()) {
                    String s = mEditText.getText().toString();
                    if (s.getBytes().length < 499 && s.length() > 0) {
                        byte sendbuf[];
                        sendbuf = Functions.making_packet_chat(s);
                        try {
                            OS.write(sendbuf, 0, s.getBytes().length + 13);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = s;
                        mChat_Handler.mHandler.sendMessage(msg);
                    }
                    else if (s.getBytes().length >= 499){
                        Intent intent = new Intent(mContext, com.pluruel.juno.mychat.Methods.Length_Error_Dialog.class);
                        startActivity(intent);
                    }
                }
                else {
                    // 그다지 필요 없는 구간, waiting 시 dialog가 override 되기때문에 별로 필요는 없다.
                }
                mEditText.setText(null);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPopupManager.Confirm_Popup();
        if(mProgress_Dialog.shouldRunning() && !mProgress_Dialog.isRunning()) {
            mProgress_Dialog.showProgressDialog();
        }
        if(mProgress_Dialog.shouldToast())
            mProgress_Dialog.showToast();
        mTurn_down_app.when_onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPopupManager.Reject_Popup();
        mProgress_Dialog.Stop_ProgressDialog();
        mTurn_down_app.when_onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mThreadManager.init();
        }catch (NullPointerException c){
        }
        mNetworkManager.init();
        mProgress_Dialog.init();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mBackPressClose.onBackPressed();
    }
}
