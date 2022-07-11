package admin;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
//Initializable has a method to run automatically after the controller is created
public class addbookcontrol implements Initializable {
	//textFields for taking book details
	@FXML
	TextField isbnprom;
	@FXML
	TextField nameprom;
	@FXML
	TextField authprom;
	@FXML
	TextField qtprom;
	@FXML
	ChoiceBox <String> catprom;
	String[]choices= {"Fiction","Horror","Romance","Fantasy","Biography"};
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//just adds all categories to the choice box
		catprom.getItems().addAll(choices);
		
	}
	public void doadd(ActionEvent e) throws Exception{
		//sends all entered data to the DB connect class
		//the DB Connect class takes the data and adds it to the Database
		db_book_connect.addbook(isbnprom.getText(), nameprom.getText(), catprom.getValue(),authprom.getText(), qtprom.getText());
		//getting the running stage of add book to be able to close automatically after adding the book
		Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
		stage.close();
	}
	
	
	
}
