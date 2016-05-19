package com.codegenius.projectlethe;

import java.io.*;
import java.net.*;


public class TcpServer{

	private static final int PORT = 1903;
	private ServerSocket serverSocket;
	private Socket connectionSocket;
	private CarController mCarController;
	public TcpServer(){
		try{
		serverSocket = new ServerSocket(PORT);
		}catch(IOException e){
		}
		mCarController = new CarController();
	}


	public void start() throws Exception {
		
		while(true){
			
			if(connectionSocket == null){
			connectionSocket = serverSocket.accept();
			

			System.out.println("Client connected!");
			}else if(connectionSocket.isClosed()){
			
			System.out.println("Socket closed waiting for new connection...");
			connectionSocket = null;
			}else if(connectionSocket.getInputStream() == null){
				System.out.println("Input stream null resetting socket...");
				connectionSocket = null;
			}else if(!connectionSocket.isInputShutdown()){

				
	
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			
			String msg = inFromClient.readLine();
			if(msg!=null){
			
			
			try{
			Thread.sleep(50);
				}catch(Exception e){
					System.out.println(e);
				}

			System.out.println("Received : " + msg);
			
			controlCar(msg);
			}else{
			connectionSocket.close();
			connectionSocket=null;
			}
			}else if(connectionSocket.isInputShutdown()){
			System.out.println("Ïnput shutdown closing connection...");
						
			connectionSocket.close();

			connectionSocket = null;		
			}
			
		}
	}

	public void stop() throws Exception {	
	 	serverSocket.close();
	}

	private void controlCar(String msg){
	
		if("w".equals(msg)){
		
			mCarController.moveForward();
			
		}else if("s".equals(msg)){

			mCarController.moveBackward();

		}else if("a".equals(msg)){

			mCarController.turnLeft();

		}else if("d".equals(msg)){

			mCarController.turnRight();

		}else if("q".equals(msg)){
			mCarController.stop();
		}

	}
}
