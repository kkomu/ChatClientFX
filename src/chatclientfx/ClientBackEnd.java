/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclientfx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ClientBackEnd implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private FXMLDocumentController controller;
    
    public ClientBackEnd(FXMLDocumentController controller) {
        try {
            clientSocket = new Socket("localhost",3010);
            this.controller = controller;
        } catch (IOException ex) {
            System.out.println("1");
            ex.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            // Output-stream pitää luoda aina ensin
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            System.out.println("2");
            ex.printStackTrace();
        }
        
        // read and write from socket until user closes the app
        while(true) {
            try {
                final ChatMessage m = (ChatMessage)input.readObject();
                //System.out.println(m.getChatMessage());
                // Runs in Event Dispatcher Thread
                if(m.isUserListUpdate()) {
                    Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.updateUserList(m.getChatMessage());
                    }
                });
                }
                else {
                    Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //controller.updateTextArea(m.getUserName() + ": " + m.getChatMessage());
                        controller.updateTextArea(m);
                    }
                });
                }
                
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("3");
                ex.printStackTrace();
            }
        }
    }
    
    public void sendMessage(ChatMessage cm) {
        try {
            output.writeObject(cm);
            output.flush();
        } catch (IOException ex) {
            System.out.println("4");
            ex.printStackTrace();
        }
    }

    
}
