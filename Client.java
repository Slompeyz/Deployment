package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application{
	
	static TextField textField = new TextField();
	static TextArea textArea = new TextArea();
	static DataInputStream fromServer = null;
	static DataOutputStream toServer = null;
	
	public static void main(String[] args) {
		
		Thread clientThread = new Thread(new Runnable() {
			public void run() {

				//attempts to connect to Server
				try {
					Socket socket = new Socket("localhost", 2000);
					
					//variables for sending Client and receiving Server data
					fromServer = new DataInputStream(socket.getInputStream());
					toServer = new DataOutputStream(socket.getOutputStream());
				}catch(IOException e) {
					System.out.println(e);
				}
				
				//receives data from Server
				try {
					while(true) {
						boolean isPrime = fromServer.readBoolean();
						
						if(isPrime) {
							textArea.appendText("That number is prime\n");
						}else {
							textArea.appendText("That number was not prime\n");
						}
					}
				}catch(IOException e) {
					System.out.println(e);
				}
				
			}
		});
		
		clientThread.start();
		
		//starts GUI
		launch(args);
	}
	
	//creating the Server GUI
	public void start(Stage primaryStage) {
		
		//main pane
		BorderPane pane = new BorderPane();
		
		//displays Server information
		ScrollPane scrollPane = new ScrollPane(textArea);
		
		//adding components to main pane
		pane.setTop(textField);
		pane.setCenter(scrollPane);
		
		//adds scene to stage
		Scene scene = new Scene(pane,485,185);	
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		primaryStage.show();
			   
		
		//handles textField user input
		textField.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
				try {
					int userNumber = Integer.parseInt(textField.getText());
					textArea.appendText("User entered: " + userNumber + "\n");
					toServer.writeInt(userNumber);
				}catch(IOException e) {
					System.out.println(e);
				}
			}
		});
	}

}
