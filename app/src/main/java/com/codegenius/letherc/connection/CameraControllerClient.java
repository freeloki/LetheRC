package com.codegenius.letherc.connection;

import android.content.Context;
import android.util.Log;

import com.codegenius.letherc.listeners.LetheFinderListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Yavuz on 8.06.2016.
 */
public class CameraControllerClient extends Thread implements LetheFinderListener {

        private int PORT = 1904;
        private String IP =null;
        private static final String TAG =CameraControllerClient.class.getSimpleName() ;
        private Socket clientSocket;
        private LetheFinder mLetheFinder;
        private OutputStreamWriter writer;
        private String msg;
        private Context mContext;


        public CameraControllerClient(Context mContext){
            this.mContext = mContext;
        }


        public void connect(){
            try {
                clientSocket = new Socket(IP,PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            mLetheFinder = new LetheFinder(mContext,this);
            mLetheFinder.start();
            super.run();
        }

        public void writeMsg(String msg){

            if(clientSocket != null && clientSocket.isConnected()){

                if(!clientSocket.isClosed()) {

                    try {
                        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),true);
                        writer.println(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }else{
                Log.i(TAG,"Socket offline ");
            }
        }

        public boolean isConnected(){
            return clientSocket != null && clientSocket.isConnected();
        }

        public Socket getSocket(){
            return this.clientSocket;
        }


        public void close(){

            if(clientSocket!=null) {
                try {
                    if (clientSocket.getInputStream() != null) {
                        clientSocket.getInputStream().close();
                    }

                    if (clientSocket.getOutputStream() != null) {
                        clientSocket.getOutputStream().close();

                    }


                    clientSocket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    @Override
    public void onLetheFound(String ip, int port) {

        Log.i(TAG,"LetheRC found on lan! CAMERA!  \nIP :  " + ip);
        this.IP = ip;
        mLetheFinder.stop();
        connect();
    }
}
