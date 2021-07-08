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
/* This class is the graphic user interface of Server monitor */
public class Server_GUI extends JFrame {
	
	/* Panel definition */
	private JPanel contentPanel = new JPanel();
	
	/* TextArea definition */
	private JTextArea monitorTa;
	
	/* JScrollPane definition */
	private JScrollPane monitorSp;
	
	/* Construction function */
	public Server_GUI() {
		this.init();
	}

	@SuppressWarnings("deprecation")
	/* Initialize function */
	private void init() {
		String port;
		int portnum = 0;
		boolean port_valid = false;
		
		// Loop until the port is valid
		do {
			port_valid = true;
			port = JOptionPane.showInputDialog(this,"请输入准备监听的端口号","服务器对话框",JOptionPane.PLAIN_MESSAGE);
	        try {
	        	portnum = Integer.parseInt(port);
	        }
	        catch(Exception e) {
	        	JOptionPane.showMessageDialog(null, "端口号需要为1-65535的整数！", "Error",JOptionPane.ERROR_MESSAGE);
	        	port_valid = false;
	        }
		} while(!port_valid);

		/* window setting */
		this.setTitle("服务器界面");  					// Set title of the window
		this.setSize(610, 570);  					// Set size of the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);	// Enable close button 
        setLocationRelativeTo(null);  				// Set the window placed in the center of the screen
        setVisible(true);  							// Set Visibility of the window
        Toolkit kit =Toolkit.getDefaultToolkit();
        Image image2 =kit.createImage("images/logo.jpg");
        setIconImage(image2);						// Change the image of the window

		
		// Monitor TextArea
		monitorTa = new JTextArea("", 500, 80);
        Color color = new Color(255,255,245);
        monitorTa.setBackground(color);
        monitorTa.setEditable(false);
       
        // Monitor JScrollPane
        monitorSp = new JScrollPane(monitorTa);

        
		/* add component to contentPanel */
		contentPanel.setLayout(null);
		add(monitorSp);
	
		/* position setting */	
		monitorSp.setBounds(0, 0, 597, 535);

		// Set contentPanel to transparent and you can see the background.
		contentPanel.setOpaque(false);
		getContentPane().add(contentPanel);
		
		// Create an instance of Server_socket and listening the specified port
		Server_socket sersoc = new Server_socket(portnum, this);
		sersoc.listen();

	}

    // Chat window to show message
    public void MonitorTaDisplay(String msg) {
    	monitorTa.append(msg);
    	//Pulled the scroll bar to the bottom end of the JTextArea.
    	monitorTa.setCaretPosition(monitorTa.getText().length() - 1);
    }
	
}


