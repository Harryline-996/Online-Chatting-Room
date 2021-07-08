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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

@SuppressWarnings("serial")
/* This class is the graphic user interface of ChattingRoom */
public class ChattingRoom_GUI extends JFrame {
	
	/* Panel definition */
	private JPanel contentPanel = new JPanel();
	
	/* Label definition */
	private JLabel bgLabel;

	
	/* Button definition */
	private JButton sendBtn = new JButton("发送");
	private JButton clearBtn = new JButton("清屏");


    /* TextField definition */
	private JTextField chattingTf = new JTextField();
	
	/* TextArea definition */
	private JTextArea chattingTa;
	private JTextArea userlistTa;
	
	/* JScrollPane definition */
	private JScrollPane chattingSp;
	private JScrollPane userlistSp;
	
	/* Construction function */
	public ChattingRoom_GUI() {
		this.init();
		this.addListener();
	}

	/* Initialize function */
	private void init() {
		
		/* Window setting */
		this.setTitle("聊天窗口");  					// Set title of the window
		this.setSize(610, 570);  					// Set size of the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);	// Enable close button 
        setLocationRelativeTo(null);  				// Set the window placed in the center of the screen
        setVisible(false);  						// Set Visibility of the window
        Toolkit kit =Toolkit.getDefaultToolkit();
        Image image2 =kit.createImage("images/logo.jpg");
        setIconImage(image2);						// Change the image of the window

        /* Create background Label */
		ImageIcon image1 = new ImageIcon("images/chatboy.gif");  // Background image of the interface
		bgLabel = new JLabel(image1);
		
		/* Create chatting TextArea */
		chattingTa = new JTextArea("", 100, 80);
        Color color = new Color(245,255,255);
        chattingTa.setBackground(color);
        chattingTa.setEditable(false);
        
        /* Create userlist TextArea */
        userlistTa = new JTextArea("", 50, 80);
        color = new Color(255,245,255);
        userlistTa.setBackground(color);
        userlistTa.setEditable(false);
        
        /* Create chatting JScrollPane */
        chattingSp = new JScrollPane(chattingTa);
        userlistSp = new JScrollPane(userlistTa);
        
		/* Add component to contentPanel */
		contentPanel.setLayout(null);
		add(bgLabel);
		add(chattingSp);
		add(userlistSp);
		add(chattingTf);
		add(sendBtn);
		add(clearBtn);

		/* Position setting */	
		chattingTf.setBounds(0, 400, 400, 100);
		sendBtn.setBounds(298, 500, 100, 30);
		clearBtn.setBounds(188, 500, 100, 30);
		chattingSp.setBounds(0, 0, 400, 400);
		userlistSp.setBounds(400, 253, 195, 280);
		bgLabel.setBounds(400, 0, 205, 253);
		
		// Set contentPanel to transparent and you can see the background.
		contentPanel.setOpaque(false);			
		getContentPane().add(contentPanel);

	}


	/* Event monitoring */
	private void addListener() {
		/* Action for sendBtn */
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	            String tempmsg = chattingTf.getText().trim();
	            // Test the validity of the message to send
	            if(tempmsg.length() == 0){		
	            	JOptionPane.showMessageDialog(null, "要发送的消息不能为空！", "Error",JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            Message msg = new Message(Message.MESSAGE, tempmsg);
	            // Send the message to server
	            Client_Login_GUI.Client.sendMessage(msg);
	            // Clear the chattingTf
	            chattingTf.setText("");
			}

		});
		
		/* Action for clearBtn */
		clearBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Clear the chattingTa
				chattingTa.setText("");
			}
 
		});
		
	}

	/* Function to show message on the chattingTa */
    public void ChattingTaDisplay(String msg) {
    	chattingTa.append(msg);
    	chattingTa.setCaretPosition(chattingTa.getText().length() - 1);
    }

    /* Function to show message on the userlistTa */ 
    public void UserlistTaDisplay(String msg) {
    	userlistTa.append(msg);
    	userlistTa.setCaretPosition(userlistTa.getText().length() - 1);
    }

    /* Function to Clear userlistTa */
    public void UserlistTaClear() {
    	userlistTa.setText("");
    }
	
}


