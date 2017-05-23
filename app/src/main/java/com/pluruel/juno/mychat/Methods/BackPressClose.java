package com.pluruel.juno.mychat.Methods;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.pluruel.juno.mychat.Managers.Turn_down_Manager;

/**
 * Created by Juno on 2017-01-20.
 */

public class BackPressClose {
    private long backbuttenpressedtime = 0;
    private Toast mToast;
    private Context mContext = null;
    private SizeRuler mSizeRuler = null;
    private Turn_down_Manager mTurn_down_Manager = null;
    private static BackPressClose mBackPressClose;
    public BackPressClose(Context _c){
        mContext = _c;
        mTurn_down_Manager = Turn_down_Manager.getinstance();
        mSizeRuler = SizeRuler.getInstance();
        mBackPressClose = this;
    }
    public void onBackPressed(){
        if(System.currentTimeMillis() > backbuttenpressedtime + 2000){
            backbuttenpressedtime = System.currentTimeMillis();
            showToast();
            return;
        }
        if (System.currentTimeMillis() <= backbuttenpressedtime + 2000){
            mToast.cancel();
            mTurn_down_Manager.Turn_off_this_app();
        }
    }
    private void showToast(){
        mToast = Toast.makeText(mContext, "'뒤로가기'를 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT);
        int Offset_Y = mSizeRuler.get_height_size()/5;
        mToast.setGravity(Gravity.TOP, 0, Offset_Y);
        mToast.show();
    }
}
