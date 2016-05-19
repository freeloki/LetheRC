package com.codegenius.projectlethe;

public class App 	
{	
    public static void main( String[] args )
    {
	// Main controller code
	try {
	TcpServer server = new TcpServer();
	
	server.start();
	}catch(Exception e){
	}
    }
}
