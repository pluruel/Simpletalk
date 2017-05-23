package com.pluruel.juno.mychat.Methods;

import android.content.Context;
import android.util.Log;

import com.pluruel.juno.mychat.Managers.PopupManager;
import com.pluruel.juno.mychat.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Juno on 2017-01-13.
 */



public class Ad_class {

    private InterstitialAd mInterstitialad;
    private Context mContext;
    private static Ad_class mAd_class = null;
    private PopupManager mPopupManager = null;
    private Progress_Dialogs mProgress_Dialogs = null;

    private Ad_class(){}

    public static Ad_class getInstance() {
        if( mAd_class == null){
            mAd_class = new Ad_class();
        }
        return mAd_class;
    }

    public void set_mContext(Context _c){
        mPopupManager = PopupManager.getInstance();
        mProgress_Dialogs = Progress_Dialogs.returner();
        mContext = _c;
    }

    public void setFullAd(){
        mInterstitialad = new InterstitialAd(mContext);
        mInterstitialad.setAdUnitId(mContext.getResources().getString(R.string.adID));
        AdRequest mAdRequest = new AdRequest.Builder().build();
        mInterstitialad.loadAd(mAdRequest);

        mInterstitialad.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AdRequest mAdRequest = new AdRequest.Builder().build();
                mInterstitialad.loadAd(mAdRequest);
                mPopupManager.Confirm_Popup();
                if(mProgress_Dialogs.shouldRunning() && !mProgress_Dialogs.isRunning())
                    mProgress_Dialogs.showProgressDialog();
                if(mProgress_Dialogs.shouldToast())
                    mProgress_Dialogs.showToast();
                super.onAdClosed();
            }
        });
    }
    public void showFullAd(){
        if(mInterstitialad.isLoaded())
            mInterstitialad.show();
    }


}
