package com.pluruel.juno.mychat.Methods;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pluruel.juno.mychat.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Juno on 2016-11-29.
 */


public class Chat_Handler {
    private Context mContext = null;
    private LinearLayout mLinearLayout = null;
    private ScrollView mScrollView = null;
    private static Chat_Handler mChat_Handler = null;
    private long Current_time = -1;
    private Date mDate = null;
    private Chat_Handler(){

    }

    public static Chat_Handler getInstance(){
        if (mChat_Handler == null){
            mChat_Handler = new Chat_Handler();
        }
        return mChat_Handler;
    }

    public void setVarious(Context _c, LinearLayout _l, ScrollView _s){
        mContext = _c;
        mLinearLayout = _l;
        mScrollView = _s;

    }

    public void init(){
        mContext = null;
        mLinearLayout = null;
        mScrollView = null;
        mChat_Handler = null;
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Current_time = System.currentTimeMillis();
                    mDate = new Date(Current_time);
                    SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                    String now_string = CurTimeFormat.format(mDate);
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout LLL = (LinearLayout) inflater.inflate(R.layout.chatbox, null);
                    TextView time_tv = (TextView) LLL.findViewById(R.id.chatbox_left_time);
                    TextView tv = (TextView) LLL.findViewById(R.id.chatbox_tv);
                    View view_gap_time_right = LLL.findViewById(R.id.right_gap_with_time);
                    View view_right = LLL.findViewById(R.id.chatbox_right);
                    String sendText = msg.obj.toString();
                    tv.setText(sendText);
                    Functions.apply_new_line(tv);
                    view_gap_time_right.setVisibility(View.GONE);
                    view_right.setVisibility(View.GONE);
                    tv.setBackgroundResource(R.drawable.sender_fin);
                    time_tv.setText(now_string);
                    mLinearLayout.addView(LLL);
                    LinearLayout add_blank = new LinearLayout(mContext);
                    LinearLayout.LayoutParams add_param = new LinearLayout.LayoutParams(1, 15);
                    add_blank.setLayoutParams(add_param);
                    mLinearLayout.addView(add_blank);
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.scrollTo(0, mLinearLayout.getHeight());
                        }
                    });
                    break;
                case 2:
                    // 받는 사람이 쓴 문장, 왼쪽에 표현됨.
                    Current_time = System.currentTimeMillis();
                    mDate = new Date(Current_time);
                    CurTimeFormat = new SimpleDateFormat("HH:mm", Locale.KOREA);
                    now_string = CurTimeFormat.format(mDate);
                    LayoutInflater inflater2 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout RLL = (LinearLayout)inflater2.inflate(R.layout.chatbox, null);
                    View view_gap_time_left = RLL.findViewById(R.id.left_gap_with_time);
                    view_gap_time_left.setVisibility(View.GONE);
                    TextView time_tv2 = (TextView) RLL.findViewById(R.id.chatbox_right_time);
                    time_tv2.setText(now_string);
                    time_tv2.setGravity(Gravity.BOTTOM);
                    TextView tv2 = (TextView)RLL.findViewById(R.id.chatbox_tv);
                    View view_left = RLL.findViewById(R.id.chatbox_left);
                    String recvText = msg.obj.toString();
                    tv2.setText(recvText);
                    Functions.apply_new_line(tv2);
                    view_left.setVisibility(View.GONE);
                    tv2.setBackgroundResource(R.drawable.receiver_fin);
                    mLinearLayout.addView(RLL);
                    LinearLayout add_blank_recv = new LinearLayout(mContext);
                    LinearLayout.LayoutParams add_param_recv = new LinearLayout.LayoutParams(1, 15);
                    add_blank_recv.setLayoutParams(add_param_recv);
                    mLinearLayout.addView(add_blank_recv);
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.scrollTo(0, mLinearLayout.getHeight());
                        }
                    });
                    break;
                default:
                    break;
            }
        }


    };
}
