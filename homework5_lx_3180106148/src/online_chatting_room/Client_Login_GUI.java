package online_chatting_room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;


@SuppressWarnings("serial")
/* This class is the graphic user interface of Client Login */
public class Client_Login_GUI extends JFrame {
	
	/* Panel definition */
	private JPanel contentPanel = new JPanel();
	
	/* Label definition */
	private JLabel bgLabel;
	private JLabel serverLabel = new JLabel("服务器IP地址:");
	private JLabel portLabel = new JLabel("端口号:");
	private JLabel userLabel = new JLabel("用户名:");
	private JLabel titleLabel = new JLabel("登录界面");
	
	/* Button definition */
	private JButton loginBtn = new JButton("登录");


    /* TextField definition */
	private JTextField serverTf = new JTextField("127.0.0.1");
	private JTextField portTf = new JTextField("6148");
	private JTextField userTf = new JTextField();
	
	/* Create an instance of ChattingRoom_GUI and Client_socket */
	public static ChattingRoom_GUI chatRoom = new ChattingRoom_GUI();
	public static Client_socket Client;
	
	/* Construction function */
	public Client_Login_GUI() {
		this.init();
		this.addListener();
	}

	@SuppressWarnings("deprecation")
	/* Initialize function */
	private void init() {
		
		/* Window setting */
		this.setTitle("客户端登陆界面");  				// Set title of the window
		this.setSize(500, 315);  					// Set size of the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);	// Enable close button 
        setLocationRelativeTo(null);  				// Set the window placed in the center of the screen
        setVisible(true);  							// Set Visibility of the window
        Toolkit kit =Toolkit.getDefaultToolkit();
        Image image1 =kit.createImage("images/logo.jpg");
        setIconImage(image1);						// Change the image of the window
      
		ImageIcon image2 = new ImageIcon("images/background.gif");  // Background image of the interface
		bgLabel = new JLabel(image2);
		bgLabel.setBounds(0, 0, 500, 280);
		
		// Add bgLabel to the LayeredPane and place it at the bottom
		this.getLayeredPane().add(bgLabel, new Integer(Integer.MIN_VALUE));
		// Set the ContentPane to transparent and you can see the background added on the LayeredPane.
		((JPanel) this.getContentPane()).setOpaque(false);

		/* Add component to contentPanel */
		contentPanel.setLayout(null);
		add(serverTf);
		add(portTf);
		add(userTf);
		add(loginBtn);
		add(serverLabel);
		add(portLabel);
		add(titleLabel);
		add(userLabel);

		/* Position setting */	
		serverTf.setBounds(140, 110, 240, 20);
		portTf.setBounds(140, 140, 240, 20);
		userTf.setBounds(140, 170, 240, 20);
		
		Font f1 = new Font("黑体", Font.BOLD + Font.ITALIC, 12);
		serverLabel.setBounds(50, 107, 90, 25);
		serverLabel.setFont(f1);
		serverLabel.setForeground(Color.RED);	
		
		portLabel.setBounds(85, 137, 90, 25);
		portLabel.setFont(f1);
		portLabel.setForeground(Color.RED);

		userLabel.setBounds(85, 167, 90, 25);
		userLabel.setFont(f1);
		userLabel.setForeground(Color.RED);

		titleLabel.setBounds(165, 50, 200, 50);
		Font f2 = new Font("隶书", Font.BOLD, 38);
		titleLabel.setFont(f2);
		titleLabel.setForeground(Color.BLACK);
		
		loginBtn.setBounds(200, 225, 100, 30);

		// Set contentPanel to transparent and you can see the background.
		contentPanel.setOpaque(false);
		getContentPane().add(contentPanel);

	}


	/* Event monitoring */
	private void addListener() {
		/* Action for loginBtn */
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				forloginBtn(serverTf.getText().trim(), portTf.getText().trim(), userTf.getText().trim());
			}

		});
	}

	/* Function for login */
	public void forloginBtn(String serverIP, String port, String username) {
		/* Input legality check */
		int portnum;
        if(serverIP.length() == 0){
        	JOptionPane.showMessageDialog(null, "服务器IP地址不能为空！", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(port.length() == 0){
        	JOptionPane.showMessageDialog(null, "端口号不能为空！", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(username.length() == 0){
        	JOptionPane.showMessageDialog(null, "用户名不能为空！", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
        	portnum = Integer.parseInt(port);
        }
        catch(Exception e) {
        	JOptionPane.showMessageDialog(null, "端口号需要为1-65535的整数！", "Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
		// Create a Client_socket class to connect to server
		Client = new Client_socket(serverIP, portnum, username, chatRoom);
		// If connect failed, return
		if(!Client.connectServer())
			return;
		// Set login window invisible	
		setVisible(false);  	
		// Set ChattingRoom window visible	
	    chatRoom.setVisible(true);				
	}
}


