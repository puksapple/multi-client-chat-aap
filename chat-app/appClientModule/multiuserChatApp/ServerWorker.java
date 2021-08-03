package multiuserChatApp;

import java.io.BufferedReader; 	
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.print.attribute.HashAttributeSet;

import org.apache.commons.lang3.StringUtils;



public class ServerWorker extends Thread {

	private Socket clieSocket;
	private Server server;
	private String login;
	private OutputStream outputStream;
	private BufferedReader reader;
	private static HashSet<ServerWorker> groupChatList=new HashSet<ServerWorker>();
	

	public ServerWorker(Server server, Socket clientSocket, OutputStream os, BufferedReader reader) {

		this.clieSocket = clientSocket;
		this.server = server;
		this.outputStream = os;
		this.reader = reader;
	}

	public Socket getcli() {
		return this.clieSocket;
	}

	@Override
	public void run() {

		try {

			handle();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO Auto-generated catch block

	}

	public void handle() throws InterruptedException, IOException {

		String line = null;
		while ((line = reader.readLine()) != null) {

			String[] token = StringUtils.split(line);
					if (token != null && token.length > 0) {
				String cmd = token[0];

				if ("quit".equalsIgnoreCase(cmd)) {

					handleLogoff();

					break;
				} 
				else if ("msg".equalsIgnoreCase(cmd)) {
					String token1[]=StringUtils.split(line, " ",3);
					handleMessage(token1);
				}
				
				else if("add".equalsIgnoreCase(cmd)){
					String token1[]=StringUtils.split(line, " ",2);
					 handleGroup(token1);
					
				}
				else if ("login".equalsIgnoreCase(cmd)) {
					handleLogin(outputStream, token);
				}
					else if("group".equalsIgnoreCase(cmd)){
						
					handleGc(token);
				} else {
					String msg = cmd;
					outputStream.write(("unknown" + msg + " ").getBytes());
					
				}
			}
			String msg = "u typed::" + line;
			outputStream.write((msg + "\n").getBytes());

		}

	}
	
	
	

	private void handleGroup(String[] token) throws IOException {
		// TODO Auto-generated method stub
		
		
		if(token!=null && token.length>1) {
			String user=token[1];
			
		groupChatList.add(this);	
			
			for(ServerWorker sw:server.getWorkerList()) {
				
				if(sw.getLogin().equalsIgnoreCase(user))   {
					groupChatList.add(sw);
					
				}
				
				
			}
			System.out.println("group chat list:"+ groupChatList);

		
		}
		
		
	}

	private void handleGc(String[] token) throws IOException {
		
		String body=token[1];
	
		for( ServerWorker sw:groupChatList) {
		
			
				sw.outputStream.write((login+":"+body).getBytes());
				
			
			}
		
	}

	private void handleMessage(String[] token) throws IOException{
		// TODO Auto-generated method stub
		if(token!=null && token.length>1) {
			String sendTo=token[1];
			String body=token[2];
			System.out.println("inside handele message method");
		
			System.out.println(server+"bhbh");
			for( ServerWorker sw:server.getWorkerList()) {
				System.out.println(sw.getLogin());
				if(sw.getLogin().equalsIgnoreCase(sendTo)) {
					sw.outputStream.write((login+":"+body).getBytes());
					System.out.println(sw+"kslksdjglsdkfjglkdsjl");
				}
			}
		}
		
	}

	private void handleLogoff() throws IOException {
		
		System.out.println("quited");
		server.getWorkerList().remove(this);//removing current serverworker
		
		List<ServerWorker> workerList = server.getWorkerList();

		// send other online users current user's status
		String onlineMsg = "offline " + login + "\n";
		for (ServerWorker worker : workerList) {
			if (!login.equals(worker.getLogin())) {
				worker.outputStream.write(onlineMsg.getBytes());

			}

		}
		clieSocket.close();
	}

	public String getLogin() {
		return login;
	}

	private void handleLogin(OutputStream outputStream, String[] token) throws IOException {

		if (token.length == 3) {
			String logincurent = token[1];
			String pw = token[2];
			if ("guest".equalsIgnoreCase(logincurent) && "guest".equalsIgnoreCase(pw)
					|| "leader".equalsIgnoreCase(logincurent) && "leader".equalsIgnoreCase(pw)
					|| "pukar".equalsIgnoreCase(logincurent) && "pukar".equalsIgnoreCase(pw)|| "janak".equalsIgnoreCase(logincurent) && "janak".equalsIgnoreCase(pw)) {

				outputStream.write(("ok login" + "\n").getBytes());
				login = logincurent;

				// send current user all other online logins
				for (ServerWorker worker : server.getWorkerList()) {
					if (worker.getLogin() != null) {
						if (!logincurent.equals(worker.getLogin())) {
							String msg2 = "[online  bnbnb" + worker.getLogin() + "]" + "\n";
							outputStream.write((msg2 + "  " + "\n").getBytes());
						}
					}
				}

				// send other online users current user's status
				String onlineMsg = "[online nnnnn " + login + " ]" + "\n";

				for (ServerWorker worker : server.getWorkerList()) {
					if (!logincurent.equals(worker.getLogin())) {
						worker.outputStream.write(onlineMsg.getBytes());
					}
				}

			} else {   
				outputStream.write("invalid login".getBytes());
			}
		} else {
			outputStream.write("login properly".getBytes());
		}

	}

}
