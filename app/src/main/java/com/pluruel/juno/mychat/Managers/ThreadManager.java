package com.pluruel.juno.mychat.Managers;

import android.content.Context;
import com.pluruel.juno.mychat.Methods.RecvThd;

import java.io.InputStream;

/**
 * Created by Juno on 2016-12-20.
 */

public class ThreadManager {

    private RecvThd mrecvTrd = null;
    private static ThreadManager threadManager = null;
    private Context mContext;
    private boolean recv_flag = false;
    private ThreadManager() {
        init();
    }

    public static ThreadManager getInstance() {
        if(threadManager == null)
            threadManager = new ThreadManager();
        return threadManager;
    }


    public void init()
    {
        if (recv_flag)
            mrecvTrd.interrupt();
        recv_flag = false;
        mrecvTrd = null;
        threadManager = null;
    }

    public RecvThd getRecvTrd()
    {
        if(mrecvTrd != null)
            return this.mrecvTrd;
        return null;
    }

    public void runRecvThread(InputStream _is)
    {
        if(mrecvTrd == null && recv_flag == false) {
            recv_flag = true;
            mrecvTrd = new RecvThd(_is);
            mrecvTrd.start();
        }
    }
}
