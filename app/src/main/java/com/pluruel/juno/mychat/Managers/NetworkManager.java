package com.pluruel.juno.mychat.Managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Juno on 2016-12-20.
 */

public class NetworkManager {
    // 소켓 등을 네트워크에 관련 된 주요 변수, Thread를 관리하기 위한 Class
    private ThreadManager mThreadManager = null;

    private Socket mSocket = null;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private ConnectThread mConnectThread = null;
    private boolean is_Connected = false;
    private long time_start_connect = -1;
    private long time_end_connect = -1;

    private static NetworkManager networkManager = null;
    private NetworkManager() {
        mThreadManager = ThreadManager.getInstance();
    }

    public static NetworkManager getInstance()
    {
        if(networkManager == null)
            networkManager = new NetworkManager();
        return networkManager;
    }

    public void init()
    {
        // 초기화, 끌때도 요긴히 사용 가능.
        if(mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mSocket = null;
        mInputStream = null;
        mOutputStream = null;

        if(mConnectThread != null) {
            if(mConnectThread.isAlive()) {
                mConnectThread.interrupt();
            }
        }
        mConnectThread = null;
        time_start_connect = -1;
        time_end_connect = -1;
    }
    public void connectFinished(){
        mConnectThread = null;
    }
    public boolean isConnected(){
        return is_Connected;
    }
    public void setConnected()
    {
        is_Connected = true;
    }
    public void connectServer()
    {
        if(mConnectThread == null) {
            time_start_connect = System.currentTimeMillis();
            time_end_connect = -1;

            mConnectThread = new ConnectThread();
            mConnectThread.start();
        }
    }

    public long get_time_start_connect()
    {
        return time_start_connect;
    }
    public long get_time_end_connect()
    {
        return time_end_connect;
    }

    public InputStream getInputStream()
    {
        return mInputStream;
    }
    public OutputStream getOutputStream()
    {
        return mOutputStream;
    }

    class ConnectThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                if (mSocket != null) return;
                mSocket = new Socket("119.193.37.125", 15478);
                mInputStream = mSocket.getInputStream();
                mOutputStream = mSocket.getOutputStream();
                mThreadManager.runRecvThread(mInputStream);
                time_end_connect = System.currentTimeMillis();
            } catch (IOException e) {
                is_Connected = false;
                e.printStackTrace();
            }


        }
    }
}
