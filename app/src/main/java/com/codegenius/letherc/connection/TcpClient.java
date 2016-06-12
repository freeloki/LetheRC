package com.codegenius.letherc.connection;

import android.content.Context;
import android.util.Log;

import com.codegenius.letherc.listeners.LetheFinderListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Yavuz on 16.05.2016.
 */
public class TcpClient extends Thread implements LetheFinderListener {

    private int PORT = 0;
    private String IP =null;
    private static final String TAG =TcpClient.class.getSimpleName() ;
    private Socket clientSocket;
    private OutputStreamWriter writer;
    private String msg;
    private LetheFinder mLetheFinder;
    private Context mContext;


    public TcpClient(Context appContext){
        this.mContext = appContext;
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

        if(clientSocket.isConnected()){

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
        return clientSocket.isConnected();
    }

    public Socket getSocket(){
        return this.clientSocket;
    }

    @Override
    public void onLetheFound(String ip, int port) {

        Log.i(TAG,"LetheRC found on lan! \nIP :  " + ip + "\n PORT : " + port);
        this.IP = ip;
        this.PORT = port;

        mLetheFinder.stop();
        connect();
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

    public String getIP(){
        return this.IP;
    }
}
