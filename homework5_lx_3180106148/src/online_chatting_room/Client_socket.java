package online_chatting_room;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

/* This class implements the socket of client and related functions*/
public class Client_socket {
	private Socket cliSocket;				// Client socket
	private ObjectInputStream socketIn;		// InputStream of socket
	private ObjectOutputStream socketOut;	// OutputStream of socket
	private String serverIP;				// IP address of the server
	private int port;						// Port of the server
	private String username;				// Username of the client
	private ChattingRoom_GUI chatRoom;		// existing GUI of chatRoom
	
	/* Construct function */
	public Client_socket(String serverIP, int port, String username, ChattingRoom_GUI chatRoom) {
		this.serverIP = serverIP;
		this.port = port;
		this.username = username;
		this.chatRoom = chatRoom;
	}
	
	/* Function to connect to server */
	public boolean connectServer() {
		try {
			// Create a socket 
			cliSocket = new Socket(serverIP,port);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "连接失败！服务器可能关闭，或者输入的服务器IP地址或端口有误", "Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		JOptionPane.showMessageDialog(null, "连接服务器成功！");
		String errorMsg;
		
		// Initialize two stream
		try {
			socketIn = new ObjectInputStream(cliSocket.getInputStream());
			socketOut = new ObjectOutputStream(cliSocket.getOutputStream());
		}catch (Exception e) {
			errorMsg = "创建套接字的I/O流时出错！ " + e;
			JOptionPane.showMessageDialog(null, errorMsg, "Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		// Try to send message to server
		try {
			socketOut.writeObject(username);
		}catch (IOException e) {
			errorMsg = "向服务器发送消息时出错！ " + e;
			JOptionPane.showMessageDialog(null, errorMsg, "Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}	

		// Create a thread to listen the message from Server
		new InteractWithServer().start();
		return true;
	}
	
	// Thread: Print message sent by server
	class InteractWithServer extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					// Receive message
					Message servermsg = (Message) socketIn.readObject();
					String content = servermsg.getContent();
	                switch(servermsg.getType()) {
	                // Type of message is MESSAGE, then display it on ChattingTa
                    case Message.MESSAGE:
                    	chatRoom.ChattingTaDisplay(content);
                        break;
                     // Type of message is MESSAGE, then display it on UserlistTa
                    case Message.USERLIST:
                    	chatRoom.UserlistTaClear();
                    	chatRoom.UserlistTaDisplay(content);
                        break;
                }
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "与服务器的连接已断开！");
					chatRoom.setVisible(false);
					break;
				}
			}
		}
	}
	
	/* Function to send message */
	public void sendMessage(Message msg) {
		try {
		socketOut.writeObject(msg);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "消息发送过程出错！", "Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
}