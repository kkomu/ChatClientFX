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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JOptionPane;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class FXMLDocumentController implements Initializable {
    
    private ClientBackEnd backEnd;
    private Thread backThread;
    private String user;
    //private WebEngine engine;
    //private StringBuilder chatHistory = new StringBuilder();
    
    @FXML
    Button btnJoin;
    
    @FXML
    Button btnSend;
    
    @FXML
    TextField userName;
    
    @FXML
    TextField chatMessage;
    
    @FXML
    TextFlow chatMessageArea;
    
    @FXML
    ListView userListView;
    
    @FXML
    ColorPicker clrPicker;
    
    @FXML
    ChoiceBox cbxFont;
        
    @FXML
    private void quitChat(ActionEvent e) {
        Platform.exit();
    }
    
    @FXML
    private void joinToChat(ActionEvent e) {
        if (userName.getText().isEmpty()) {
             JOptionPane.showMessageDialog(null, "Please give user name.");
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
    private void handleSendButton(ActionEvent e) {
        sendChatMessage();
    }
    
    @FXML
    private void handleTextFieldEnterKey(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            sendChatMessage();
        }
    }
    
    private void sendChatMessage() {
        ChatMessage cm = new ChatMessage();
        cm.setChatMessage(chatMessage.getText());
        
        // Send private message if username is selected from list view
        String privateName = "";
        if (userListView.getSelectionModel().getSelectedItem() != null) {
            privateName = (String) userListView.getSelectionModel().getSelectedItem();
        }
        if( !privateName.isEmpty() && !privateName.matches("<public>" ) ) {
            cm.setPrivateMessage(true);
            cm.setPrivateName(privateName);
        }
        
        // Set message color
        cm.setUserName(this.user);
        cm.setMessageColor(clrPicker.getValue().toString());
       
        // Set message font
        String tempFont = (String)cbxFont.getValue();
        cm.setFontType(tempFont);
        
        backEnd.sendMessage(cm);
        chatMessage.clear();
    }
    
    //public void updateTextArea(String message) {
    public void updateTextArea(ChatMessage cm) {
        String message = cm.getUserName()+": "+cm.getChatMessage()+"\n";
        Text text = new Text(message);
        
        // Get message font
        String tempFont = cm.getFontType();
        int fontSize = 12;
        if(cm.isPrivateMessage()) {
            text.setFont(Font.font(tempFont,FontWeight.BOLD,fontSize));
        }
        else {
            text.setFont(Font.font(tempFont,FontWeight.NORMAL,fontSize));
        }
        
        // Get message color
        String color = cm.getMessageColor();
        text.setFill(Color.web(color));

        chatMessageArea.getChildren().addAll(text);
    }

    public void updateUserList(String users) {
        ObservableList<String> userList = FXCollections.observableArrayList();
        String[] test = users.split(",");
        userList.add("<public>");
        for(String i: test) {
            userList.add(i);
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
        
        clrPicker.setValue(Color.BLACK);
        
        ObservableList fonts = FXCollections.observableArrayList("Arial", "Tahoma", "Serif");
        cbxFont.setItems(fonts);
        
        backEnd = new ClientBackEnd(this);
        backThread = new Thread(backEnd);
        backThread.setDaemon(true);
        backThread.start();
    }    
    
}
