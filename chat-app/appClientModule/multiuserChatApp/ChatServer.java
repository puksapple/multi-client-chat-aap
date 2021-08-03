package multiuserChatApp;

import java.io.IOException;	
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	
	
	public static void main(String[] args) {
		
	Server server=new Server(9098);
	server.start();
	
	}
		

}
