package com.avatar.newarkluxury;

import com.xm.NetSdk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chx on 2017/1/6.
 */

public class MyNetSdk extends NetSdk {
    private static MyNetSdk instance;
    private List<OnDisConnectListener> mDisConnectLsList = new ArrayList();

    public void setMyOnDisConnectListener(NetSdk.OnDisConnectListener disconnectLs) {
        if(disconnectLs != null && !this.mDisConnectLsList.contains(disconnectLs)) {
            this.mDisConnectLsList.add(disconnectLs);
        }

    }

    public static synchronized MyNetSdk getInstance() {
        if(instance == null) {
            instance = new MyNetSdk();
        }
        return instance;
    }

    @Override
    public void OnDisConnect(int id, long loginid, byte[] ip, long port, long dwUser) {
        for(OnDisConnectListener listener : mDisConnectLsList) {
            listener.onDisConnect(id, loginid, ip, port);
        }
    }
}
