package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application{
	
	static TextArea textArea = new TextArea();
	
	public static void main(String[] args) {
		
		
	    //thread that runs the server-client connection and calculates whether the client's
	    //number is prime
	    Thread serverThread = new Thread(new Runnable() {
			public void run() {
				try {
					textArea.setText("Server created\n");
					
					//checks for client connection
					ServerSocket serverSocket = new ServerSocket(2000);
					Socket socket = serverSocket.accept();
					textArea.appendText("Client connected\n");
					
					//variables for receiving data from client
					DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
					DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
					
					//receives an int from the client and determines if it's prime
					//before sending that information back to the client
					while(true) {
						int clientNumber = inputFromClient.readInt();
						textArea.appendText("The client's number is: " + clientNumber + "\n");
						
						boolean isPrime = true;
						for(int i = 2; i < clientNumber; i++) {
							if(clientNumber%i == 0) {
								isPrime = false;
							}
						}
						textArea.appendText("Number is prime: " + isPrime + "\n");
						
						//sends information to Client
						outputToClient.writeBoolean(isPrime);
					}
					
				}catch(IOException e) {
					System.out.println(e);
				}
			}
		});
	    
	    serverThread.start();
	    
	  //starts the Server GUI
	  launch(args);
	}

	public void start(Stage primaryStage) {
		
		//creating the Server GUI
		Scene scene = new Scene(new ScrollPane(textArea),485,185);
		
		primaryStage.setTitle("Server");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
}
