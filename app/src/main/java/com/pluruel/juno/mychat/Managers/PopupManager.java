package com.pluruel.juno.mychat.Managers;

import android.content.Context;


/**
 * Created by Juno on 2016-12-30.
 */

public class PopupManager {
    private static PopupManager mPopupManager = null;
    private boolean Possibility_Popup = false;
    private static Context mContext;
    private PopupManager(){}

    public static PopupManager getInstance(){
        if(mPopupManager == null){
            mPopupManager = new PopupManager();
        }
        return mPopupManager;
    }

    public void init(){
        mContext = null;
        mPopupManager = null;
        Possibility_Popup = false;
    }

    public void setContext(Context _c){
        mContext = _c;
    }
    public void Confirm_Popup(){
        Possibility_Popup = true;
    }
    public void Reject_Popup(){
        Possibility_Popup = false;
    }

    public boolean Possibility_Popup(){
        return Possibility_Popup;
    }


}
