package multiuserChatApp;

import java.io.BufferedReader;	
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Server extends Thread {

	int port;
	private List<ServerWorker> workerList = new ArrayList<ServerWorker>();

	
	public Server(int i) {
	port=i;
	}
	public Server() {
		// TODO Auto-generated constructor stub
	}
	public List<ServerWorker> getWorkerList() {
		return workerList;
	}
	

//                                              sw=cs1
	@Override
	public void run() {

		try {

			System.out.println("waiting for clients");
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
			Socket clientSocket =serverSocket.accept(); //gives  client a clientsocket object
			BufferedReader is=new BufferedReader(new java.io.InputStreamReader(clientSocket.getInputStream()));
			OutputStream os=clientSocket.getOutputStream();
		
			ServerWorker serverWorker1=new ServerWorker(this,clientSocket,os,is);
			workerList.add(serverWorker1);
			
			
				serverWorker1.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
