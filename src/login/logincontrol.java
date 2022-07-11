package login;

import java.io.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import liblib.libmain_control;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
public class logincontrol {
	@FXML
	Button login;
	@FXML
	TextField id;
	@FXML
	PasswordField pass;
	@FXML
	private Label nameid;

	@FXML
	private Label pass2;

	@FXML
	Label outname;//shows only if username is wrong
	@FXML
	Label outpass;//displayed only if password is wrong
	@FXML
	Label outunknown;//displayed after a sql exception caused by loss of connection
	@FXML
	AnchorPane p;
	
	//method asks the database connect class to check the entered credentials
	//method sends the user to the UI meant for him based on the job id returned from database connect class
	public void login(ActionEvent e) throws Exception {
		login.setDisable(true);
		AudioInputStream ais = AudioSystem.getAudioInputStream(new File("Warda.wav").getAbsoluteFile());
		Clip clip = AudioSystem.getClip();
		clip.open(ais);
		clip.loop(0);


		//asking db_login_connect to check credentials entered in the TextFields
		String serverresponse=db_login_connect.checkpass(id.getText(), pass.getText());
		//job id 1 means he is an admin so we send him to admin page
		if(serverresponse.equals("1")) {
			//System.out.println("it worked :)");
			Parent root = FXMLLoader.load(getClass().getResource("/admin/adminUI.fxml"));
			//next line gets the stage that the login click happened in to switch the scene for that stage
			Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
			Scene scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
			
		}
		//job id 2 means user is a librarian
		else if(serverresponse.equals("2")) {
			FXMLLoader loader=new FXMLLoader(getClass().getResource("/liblib/libmain.fxml"));
			Parent root = loader.load();
			//fxml loader gets the controller for us to be able to send emp id to the controller after converting the username to emp id
			libmain_control controller=loader.getController();
			//librarian controller needs the employee id to be able to print receipts with employee name
			//next line
			controller.setEmpid(db_login_connect.getid(id.getText()));
			Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
			Scene scene=new Scene(root);
			stage.setScene(scene);
			stage.show();

		}
		//if server didn't respond with 1 or 2 it means it responded with an error message
		//the next block displays each error in the block meant for it

		else {
			login.setDisable(false);
			System.out.println(serverresponse);
			if(serverresponse.equals("password wrong")) {
				outpass.setText(serverresponse);
				//we remove the text from previous errors
				outname.setText("");
				outunknown.setText("");
			}
			else if(serverresponse.equals("wrong id")) {
				outpass.setText("");
				outname.setText(serverresponse);
				outunknown.setText("");
			}
			else {
				outpass.setText("");
				outname.setText("");
				outunknown.setText(serverresponse);
			}
		}
		
		
	}
	public void forgotten(){
		try {
			db_login_connect.passforgot(id.getText());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
