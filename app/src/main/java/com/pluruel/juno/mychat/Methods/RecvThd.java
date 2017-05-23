package com.pluruel.juno.mychat.Methods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

import com.pluruel.juno.mychat.Intro;
import com.pluruel.juno.mychat.Managers.NetworkManager;
import com.pluruel.juno.mychat.Managers.PopupManager;
import com.pluruel.juno.mychat.Managers.ThreadManager;
import com.pluruel.juno.mychat.Managers.Turn_down_Manager;
import com.pluruel.juno.mychat.Variables.Header;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.Arrays;


/**
 * Created by Juno on 2016-12-02.
 */

public class RecvThd extends Thread {
    private InputStream in;
    private int temp;
    private boolean sandable = false;
    private boolean waiting = true;
    private Context mContext = null;
    private Activity matching_new_activity = null;
    private Chat_Handler chatHandler = null;
    private Progress_Dialogs mProgress_Dialog = null;
    private LinearLayout mLinearLayout = null;
    private ThreadManager mThreadManager = null;
    private PopupManager mPopupManager = null;
    private NetworkManager mNetworkManager = null;
    private Encrypt_class mEncrypt_class = null;
    private byte Encryption_code[] = new byte[512];
    private Turn_down_Manager mTurn_down_app;
    private static int newest_ver = -1;


    public RecvThd(InputStream is) {
        this.in = is;
        mNetworkManager = NetworkManager.getInstance();
    }

    public boolean getwaiting(){
        return waiting;
    }
    public void setContext(Context _c){
        this.mContext = _c;
        Log.d("갖고감", String.valueOf(_c));
        mProgress_Dialog = Progress_Dialogs.getInstance(mContext,2);
        chatHandler = Chat_Handler.getInstance();
        mThreadManager = ThreadManager.getInstance();
        mPopupManager = PopupManager.getInstance();
        mEncrypt_class = Encrypt_class.getInstance();
    }
    public void setLinearLayout ( LinearLayout _l){
        this.mLinearLayout = _l;
    }

    public boolean isSandable(){
        return sandable;
    }

    public int getNewest_ver() {
        return newest_ver;
    }


    public void run() {
        Log.d("시작됨", "시작됨");
        byte define[] = new byte[3];
        byte lengths[] = new byte[5];
        byte inb[] = new byte[512];
        byte version[] = new byte[6];
        Arrays.fill(inb, (byte) 0);

        mContext = Intro.getintroContext();
        try {
            int i = in.read(version,0,4);
            if(i == 4){
                newest_ver = Functions.byteToint(version);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (mContext != null && mPopupManager.Possibility_Popup()) {
                Intent intent = new Intent(mContext, Popup_Connection_Error.class);
                intent.putExtra("ERROR_CODE", 2);
                mContext.startActivity(intent);
                return;
            }
        }

        try {
             int a = in.read(inb, 0, 4);
             if (a == 4) {
                    temp = Functions.byteToint(inb);
                 if (temp != 0) {
                     Header.MyIntCode.set_codes(temp);
                 }
                 else if (temp == 0){
                     Intent intent = new Intent(mContext, Popup_Connection_Error.class);
                     intent.putExtra("ERROR_CODE", 3);
                     mContext.startActivity(intent);
                     return;
                 }
        }
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (IOException ie) {
            if (mContext != null && mPopupManager.Possibility_Popup()) {
                Intent intent = new Intent(mContext, Popup_Connection_Error.class);
                intent.putExtra("ERROR_CODE", 2);
                mContext.startActivity(intent);
            }
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } // try - catch

        mNetworkManager.setConnected();

        try {
            while (true) {
                int a = in.read(define, 0, 1);
                if (a == 1){
                    if (define[0] == 23) {
                        a = in.read(lengths, 0, 4);
                        if (a == 4){
                            temp = Functions.byteToint(lengths);
                            byte inc[] = new byte[temp];
                            a = in.read(inc, 0, temp);
                            if (a > 0) {
                                Message msg = new Message();
                                msg.what = 2;
                                //msg.obj = new String(inc);
                                msg.obj = new String(mEncrypt_class.Decrypt_data(inc, inc.length));
                                chatHandler.mHandler.sendMessage(msg);
                            }
                        }
                    }
                    if (define[0] == 34) {
                        a = in.read(lengths, 0, 4);
                        if (a == 4){
                            temp = Functions.byteToint(lengths);
                            byte ins[] = new byte[temp];
                            a = in.read(ins, 0, temp);
                            if (a > 0) {
                                String str = new String(ins);
                                if (str.equals(Header.MyStrCode.turn_off_chat)){
                                    sandable = false;
                                    waiting = true;
                                }
                                else if (str.equals(Header.MyStrCode.turn_on_chat)){
                                    sandable = true;
                                    waiting = false;

                                    mProgress_Dialog.dismissProgressDialog();
                                    mProgress_Dialog.showToast();
                                    byte ine[] = new byte[100];
                                    a = in.read(ine, 0, 100);
                                    if (a == 100){
                                        mEncrypt_class.make_new_encryptcode(ine);
                                    }
                                }
                                else if (str.equals(Header.MyStrCode.discon_partner)){
                                    sandable = false;
                                    waiting = true;
                                    mLinearLayout.removeAllViewsInLayout();
                                    matching_new_activity = Popup_matching_new_partner.return_Pmnp_Activity();
                                    if (matching_new_activity != null)
                                        matching_new_activity.finish();
                                    mProgress_Dialog.showProgressDialog();
                                    //mPopupManager.turn_on_adv();

                                }
                                else {

                                }

                            }
                        }
                    }
                }
                else
                    break;
            }
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (IOException ie) {
            if (mContext != null && mPopupManager.Possibility_Popup()) {
                Intent intent = new Intent(mContext, Popup_Connection_Error.class);
                intent.putExtra("ERROR_CODE", 2);
                mContext.startActivity(intent);
                }
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } // try - catch
    }

}

