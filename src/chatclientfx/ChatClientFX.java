/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ChatClientFX extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        // Create thread for client back end
        ClientBackEnd backEnd = new ClientBackEnd();
        Thread backThread = new Thread(backEnd);
        backThread.start();
        
        ChatMessage chatM = new ChatMessage();
        chatM.setChatMessage("Hello yello!");
        backEnd.sendMessage(chatM);
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
