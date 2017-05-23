package com.pluruel.juno.mychat.Methods;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pluruel.juno.mychat.Managers.Turn_down_Manager;
import com.pluruel.juno.mychat.R;

/**
 * Created by Juno on 2016-12-15.
 */

public class Popup_Connection_Error extends Activity implements View.OnClickListener {
    public boolean failed_connecting = false;
    private static Popup_Connection_Error mPopup_Connection_Error = null;
    private Context mContext= null;
    private LinearLayout mLinearLayout = null;
    private Turn_down_Manager mTurn_down_app = null;
    private String tvstring[] = {"버그찾아라",
            "서버와 연결할 수 없습니다" ,
            "인터넷 연결이 끊어졌습니다.",
            "이용자가 너무 많아요!\n잠시 후 다시 접속 해 주세요!",
            "최신버전 업데이트 후 이용해 주세요!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTurn_down_app = Turn_down_Manager.getinstance();
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.pop_up);
        TextView tv = (TextView)dialog.findViewById(R.id.connection_error_msg);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        mLayoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(mLayoutParams);
        dialog.setCancelable(false);
        int WhatshouldIsay = getIntent().getIntExtra("ERROR_CODE", 0);
        if(WhatshouldIsay == 1) {
            tv.setText(tvstring[1]);
        }
        else if(WhatshouldIsay == 2)
            tv.setText(tvstring[2]);
        else if(WhatshouldIsay == 3)
            tv.setText(tvstring[3]);

        else if(WhatshouldIsay == 4)
            tv.setText(tvstring[4]);

        Button btn = (Button)dialog.findViewById(R.id.check_afk_btn);
        btn.setOnClickListener(this);
        dialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_afk_btn:
                setResult(RESULT_OK);
                mTurn_down_app.Turn_off_this_app();
                this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
