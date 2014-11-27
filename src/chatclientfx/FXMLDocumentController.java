/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientfx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    private String user;
    
    @FXML
    Button btnJoin;
    
    @FXML
    Button btnSend;
    
    @FXML
    TextField userName;
    
    @FXML
    TextField chatMessage;
    
    @FXML
    TextArea chatMessageArea;
    
    @FXML
    ListView userListView;
    
    @FXML
    private void quitChat(ActionEvent e) {
        Platform.exit();
    }
    
    @FXML
    private void joinToChat(ActionEvent e) {
        if (userName.getText().isEmpty()) {
            System.out.println("Pls give username");
        }
        else {
            this.user = userName.getText();
            
            ChatMessage cm = new ChatMessage();
            cm.setUserName(this.user);
            cm.setNameUpdate(true);
            cm.setChatMessage("testi");
            userName.setDisable(true);
            btnJoin.setDisable(true);
            btnSend.setDisable(false);
            chatMessage.setDisable(false);
            backEnd.sendMessage(cm);
            chatMessage.requestFocus();
            
        }
    }
    
    @FXML
    private void sendChatMessage(ActionEvent e) {
        ChatMessage cm = new ChatMessage();
        cm.setChatMessage(chatMessage.getText());
        String privateName = "";
        if (userListView.getSelectionModel().getSelectedItem() != null) {
            privateName = (String) userListView.getSelectionModel().getSelectedItem();
        }

        //System.out.printf("private name: %s\n",privateName);
        if( !privateName.isEmpty() && !privateName.matches("<public>" ) ) {
            System.out.println("ollan private-haarassa");
            cm.setIsPrivate(true);
            cm.setPrivateName(privateName);
        }
        cm.setUserName(this.user);
        backEnd.sendMessage(cm);
        chatMessage.clear();
    }
    
    public void updateTextArea(String message) {
        chatMessageArea.appendText(message +"\n");
    }

    public void updateUserList(String users) {
        ObservableList<String> userList = FXCollections.observableArrayList();
        String[] test = users.split(",");
        userList.add("<public>");
        for(String i: test) {
            userList.add(i);
            //System.out.printf("User: %s\n",i);
        }
        userListView.setItems(userList);
    }
    
    public String getUser() {
        return user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set focus to username-field in EDT (Event Dispatcher Thread)
        // (can't be done during the initialize method)
        Platform.runLater(new Runnable() {
             @Override
            public void run() {
                userName.requestFocus();
            }
        });
        userName.setDisable(false);
        btnJoin.setDisable(false);
        btnSend.setDisable(true);
        chatMessage.setDisable(true);
        
        backEnd = new ClientBackEnd(this);
        backThread = new Thread(backEnd);
        backThread.setDaemon(true);
        backThread.start();
        
    }    
    
}
