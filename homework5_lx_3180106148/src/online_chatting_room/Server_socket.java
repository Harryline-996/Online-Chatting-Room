package online_chatting_room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;

/* This class implements the socket of server and related functions*/
public class Server_socket {
    private int port;									// Port to listen
    private ArrayList<InteractWithClient> clientlist;	// ArrayList of all connected client
    private int clientid;								// Unique id for each client
    private SimpleDateFormat curtime;					// current time
    private Server_GUI server;							// The instance of Server_GUI
    
    /* Construction function */
    public Server_socket(int port, Server_GUI server) {
        this.port = port;
        clientlist = new ArrayList<InteractWithClient>();
        this.clientid = 0;
        this.curtime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.server = server;
    }
    
    /* Function to listen and wait connect */
    public void listen() {
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            display("正在监听端口" + port + " ...\n");
            // Keep listening
            while(true)
            {
                Socket socket = serverSocket.accept();      
                InteractWithClient tempclient = new InteractWithClient(socket);  
                // Add connected client to clientlist
                clientlist.add(tempclient);
                tempclient.start();
                notifyClients(tempclient.userName+" 上线啦！大家快去找他/她聊天吧！");
                display(tempclient.userName+" 连接到服务器，当前在线人数为：" + clientlist.size() + "\n");
            }
        }
        catch (IOException e) {
            String msg = curtime.format(new Date()) + " 创建ServerSocket过程出错！: " + e + "\n";
            display(msg);
        }
    }

    /* Function to notify all the connected clients */
    private synchronized void notifyClients(String message) {
        
        String notice = curtime.format(new Date()) + "\n" + message + "\n\n";
        Message chatmsg = new Message(Message.MESSAGE, notice);
        /* Construct the chatmessage and notify this to all clients*/
        for(int i = 0; i < clientlist.size(); ++i) {
            InteractWithClient nowclient = clientlist.get(i);
            nowclient.sendMessage(chatmsg);
        }
        /* Construct the message of online client list and notify this to all clients*/
    	String tempstr = "当前在线用户数: " + clientlist.size() + "\n";
    	tempstr += "在线用户列表:\n";
    	tempstr += "编号  用户名    连接时间\n";
        for(int i = 0; i < clientlist.size(); ++i) {
            InteractWithClient nowclient = clientlist.get(i);
            tempstr += " ["+(i+1) + "]     " + nowclient.userName + "      " + nowclient.linktime +  "\n";
        }
        Message listmsg = new Message(Message.USERLIST, tempstr);      
        
        for(int i = 0; i < clientlist.size(); ++i) {
            InteractWithClient nowclient = clientlist.get(i);
            nowclient.sendMessage(listmsg);
        }
    }

    /* Function to remove client in the clientlist */
    synchronized void remove(int id) {
        // scan the arraylist to find the client to remove
        for(int i = 0; i < clientlist.size(); ++i) {
            InteractWithClient nowclient = clientlist.get(i);
            if(nowclient.id == id) {
                clientlist.remove(i);
                return;
            }
        }
    }

    /* Function to display message on server_GUI */
    private void display(String msg) {
        msg = curtime.format(new Date()) + " " + msg;
        server.MonitorTaDisplay(msg);
    }

    class InteractWithClient extends Thread {
    	int id;								// Unique id for each thread
        Socket socket;						// Server socket
        ObjectInputStream socketInput;		// InputStream of socket
        ObjectOutputStream socketOutput;	// OutputStream of socket
        String userName;					// The username of connected client
        String linktime;					// Link time of client
        Message clientmessage;				// Message from client
        
        /* Construction function */
        InteractWithClient(Socket socket) {
            id = ++clientid;
            this.socket = socket;
            String errorMsg;
            try
            {
            	socketOutput = new ObjectOutputStream(socket.getOutputStream());
                socketInput  = new ObjectInputStream(socket.getInputStream());
                // get the user name
            	userName = (String) socketInput.readObject();

            }
            catch (IOException e1) {
            	errorMsg = "创建套接字的I/O流时出错！ " + e1;
            	JOptionPane.showMessageDialog(null, errorMsg, "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            catch (ClassNotFoundException e2) {
            }
            linktime = curtime.format(new Date());
        }

        public void run() {
            // Loop until disconnect
            while(true) {
                // Read message from client
                try {
                    clientmessage = (Message) socketInput.readObject();
                }
                catch (IOException e1) {
                    break;
                }
                catch(ClassNotFoundException e2) {
                    break;
                }
                notifyClients(userName + " 说: \n" + clientmessage.getContent());
            }
            // If client is shutdown, then remove it from the clientlist
            remove(id);
            notifyClients(userName+" 下线了，下次再找他/她聊天吧~\n");
            display(userName+" 断开了连接，当前在线人数为：" + clientlist.size() + "\n");
        }

        /* Function to sendMessage to clinet */
        private boolean sendMessage(Message msg) {
        	// If the client is not connected, return false
            if(!socket.isConnected()) {
                return false;
            }
            try {
                socketOutput.writeObject(msg);
            }
            catch(IOException e) {
            	display("发送消息给" + userName + "时产生异常" + e.toString() + "\n");
            	return false;
            }
            return true;
        }
        
    }
}
