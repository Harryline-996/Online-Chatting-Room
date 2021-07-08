package online_chatting_room;

import java.io.Serializable;

/* This class is the abstract of message transfered between server and client */
public class Message implements Serializable{
	// Use my student ID as the serialVersionUID
	protected static final long serialVersionUID = 3180106148L;	
	// Define two type constants of the Message 
    static final int MESSAGE = 0, USERLIST = 1;
    // Type of the Message 
    private int type;
    // Actual content of the Message
    private String content;

    /* Construct funtion */
    Message(int type, String content) {
        this.type = type;
        this.content = content;
    }

    /* Funtion to get type of the Message*/
    int getType() {
        return type;
    }
    
    /* Funtion to get content of the Message*/
    String getContent() {
        return content;
    }
}
