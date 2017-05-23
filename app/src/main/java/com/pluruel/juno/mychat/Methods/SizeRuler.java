package com.pluruel.juno.mychat.Methods;

import android.widget.ImageView;

/**
 * Created by Juno on 2016-12-20.
 */

public class SizeRuler {


    private int height_main = -1;
    private int width_main = -1;
    private static SizeRuler mSizeRuler = null;
    private SizeRuler(){}

    public static SizeRuler getInstance(){
        if(mSizeRuler == null)
            mSizeRuler = new SizeRuler();
        return mSizeRuler;
    }


    public void init(){
        height_main = -1;
        width_main = -1;
        mSizeRuler = null;
    }

    public void get_size_of_display (final ImageView mImageView) {
        // 길이를 재서 Class에 저장시킴, 높이나 너비가 필요 시 get_*_size를 이용하여 가져간다.
        if (height_main == -1 && width_main == -1) {
            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    height_main = mImageView.getHeight();
                    width_main = mImageView.getWidth();
                }
            });
        }
    }

    public int get_width_size() {
        if (width_main != -1) return width_main;
        else return -1;
    }

    public int get_height_size(){
        if (width_main != -1) return width_main;
        else return -1;
    }

}
