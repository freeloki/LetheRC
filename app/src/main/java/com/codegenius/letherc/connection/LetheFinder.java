package com.codegenius.letherc.connection;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.codegenius.letherc.listeners.LetheFinderListener;

/**
 * Created by Yavuz on 18.05.2016.
 */
public class LetheFinder implements NsdManager.DiscoveryListener {

    private static final String TAG =LetheFinder.class.getSimpleName() ;
    private Context mContext;
    private NsdManager mNsdManager;
    private static final String LETHE_RC_CAR = "_socket._tcp";
    private LetheFinderListener mLetheListener;
    private String ip;
    private int port;


    public LetheFinder(Context mContext,LetheFinderListener mLetheListener){
        this.mContext = mContext;
        this.mLetheListener = mLetheListener;
    }


    public void start(){
        mNsdManager = (NsdManager) mContext.getSystemService(Context.NSD_SERVICE);

        mNsdManager.discoverServices(LETHE_RC_CAR,NsdManager.PROTOCOL_DNS_SD,this);
        Log.i(TAG,"Starting letheRC founder...");
    }

    @Override
    public void onStartDiscoveryFailed(String serviceType, int errorCode) {
        Log.i(TAG,"Starting letheRC founder failed.\n Error Code : " + errorCode);
    }

    @Override
    public void onStopDiscoveryFailed(String serviceType, int errorCode) {
        Log.i(TAG,"Stopping letheRC founder failed.\n Error Code : " + errorCode);
    }

    @Override
    public void onDiscoveryStarted(String serviceType) {
        Log.i(TAG,"Discover started for : " + serviceType);
    }

    @Override
    public void onDiscoveryStopped(String serviceType) {
        Log.i(TAG,"Discover stopped for : " + serviceType);
    }

    @Override
    public void onServiceFound(NsdServiceInfo serviceInfo) {
        Log.i(TAG,"Service found! : " + serviceInfo.getServiceName());

        if(serviceInfo.getServiceName().equals("LetheRC")) {
            mNsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    Log.i(TAG,"Service resolve failed ! : " + serviceInfo.getServiceName());

                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {

                    Log.i(TAG,"Service name! : " + serviceInfo.getServiceName());
                    Log.i(TAG,"Service host! : " + serviceInfo.getHost());
                    Log.i(TAG,"Service port! : " + serviceInfo.getPort());
                    Log.i(TAG,"Service type! : " + serviceInfo.getServiceType());

                    ip = serviceInfo.getHost().getHostAddress();
                    port = serviceInfo.getPort();
                    Log.i(TAG,"ip : " + ip + " port : " + port);
                    mLetheListener.onLetheFound(ip,port);
                }
            });
        }
    }

    @Override
    public void onServiceLost(NsdServiceInfo serviceInfo) {
        Log.i(TAG,"Service lost! : " + serviceInfo.getServiceName());
    }

    public void stop(){
        mNsdManager.stopServiceDiscovery(this);
    }
}
