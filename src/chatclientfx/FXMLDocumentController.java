/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class FXMLDocumentController implements Initializable {
    
    private ClientBackEnd backEnd;
    private Thread backThread;
    private String user = "Anonymous";
    
    @FXML
    TextField userName;
    
    @FXML
    TextField chatMessage;
    
    @FXML
    TextArea chatMessageArea;
    
    @FXML
    private void sendChatMessage(ActionEvent e) {
        ChatMessage cm = new ChatMessage();
        cm.setChatMessage(chatMessage.getText());
        this.user = userName.getText();
        cm.setUserName(this.user);
        backEnd.sendMessage(cm);
        chatMessage.clear();
    }
    
    public void updateTextArea(String message) {
        chatMessageArea.appendText(message +"\n");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backEnd = new ClientBackEnd(this);
        backThread = new Thread(backEnd);
        backThread.setDaemon(true);
        backThread.start();
        
        
    }    
    
}
