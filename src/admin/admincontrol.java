package admin;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
//admin has two tabs
//books tab to edit books
//employees tab to edit employees

//Initializable has a method to run automatically after the controller is created
public class admincontrol implements Initializable  {
	//Book Tab
	//----------------------------------
	//searchable is a textfield for searching
	@FXML
	TextField searchable;

	//table view and columns display objects of book
	@FXML
	TableView<book>tb;
	@FXML
	TableColumn<book,String>isbn;
	@FXML
	TableColumn<book,String>nam;
	@FXML
	TableColumn<book,String>cat;
	@FXML
	TableColumn<book,String>auth;
	@FXML
	TableColumn<book,String>qt;
	@FXML
	TableColumn<book,String>out;
	@FXML
	TableColumn<book, Button>edi;
	
	//Employee Table
	//----------------------------------
	@FXML
	TableView <Emp> tabl;
	@FXML
	TableColumn <Emp,String> id;
	@FXML
	TableColumn <Emp,String> name;
	@FXML
	TableColumn <Emp,String> job;
	@FXML
	TableColumn <Emp,String> email;
	@FXML
	TableColumn <Emp,String> phone;
	//----------------------------------
	//----------------------------------
	//add book opens another stage to add the book to database separately
	public void addbook(ActionEvent e) throws Exception{
		Parent rt=FXMLLoader.load(getClass().getResource("addbook.fxml"));
		Stage ss=new Stage();
		Scene sc=new Scene(rt);
		ss.setScene(sc);
		ss.show();
		
	}
	//add employee opens another stage to add the book to database separately
	public void addemp(ActionEvent e) throws Exception{
		Parent mt=FXMLLoader.load(getClass().getResource("addemp.fxml"));
		Stage s=new Stage();
		Scene cc=new Scene(mt);
		s.setScene(cc);
		s.show();

	}
	
	
	
	//Refresh Employee
	//tableView displays an observable list
	//when we change in the observable list we need to tell the table to use the new edited observable list
	// AKA table.setItems(ObservableList)
	public void refresh(ActionEvent e) {

		try {
			//getAll adds all employees to an observable list and returns the ObservableList
			tabl.setItems(db_user_connect.getAll());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	//initialize runs immediatly after an object of the controller is created
	//in other words it is the first method to run
	public void initialize(URL url,ResourceBundle resourceBundle) {
		//each column in a table needs to know which attribute to display from the object in the observable list
		//here we tell the id column that it will display the id attribute in class Emp
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		name.setCellValueFactory(new PropertyValueFactory<>("name"));
		job.setCellValueFactory(new PropertyValueFactory<>("job"));
		email.setCellValueFactory(new PropertyValueFactory<>("email"));
		phone.setCellValueFactory(new PropertyValueFactory<>("pnum"));
		//---------------------------------------------
		//we refresh to display all the employees automatically
		refresh(new ActionEvent());
		//-----------------------------------------------
		//setting the books table columns
		isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		nam.setCellValueFactory(new PropertyValueFactory<>("nam"));
		cat.setCellValueFactory(new PropertyValueFactory<>("cat"));
		auth.setCellValueFactory(new PropertyValueFactory<>("auth"));
		qt.setCellValueFactory(new PropertyValueFactory<>("totq"));
		out.setCellValueFactory(new PropertyValueFactory<>("tak"));
		edi.setCellValueFactory(new PropertyValueFactory<>("bb"));
		//-----------------------------------------------
		//another refresh
		refbook();
		
		
	}
	//refbook is the same as refresh but for books
	public void refbook() {
		try {
			tb.setItems(db_book_connect.getAll());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	//seabook searches for the key word written in searchable textfield
	public void seabook(ActionEvent e) throws SQLException {
		//db_book_connect.search returns an observable list of books
		//tb.setItems asks the table to display the returned observable list
		tb.setItems(db_book_connect.search(searchable.getText()));
	}
}
