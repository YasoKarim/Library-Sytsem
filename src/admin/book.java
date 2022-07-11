package admin;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import liblib.libmain_control;
import liblib.retrun.return_control;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import static java.time.temporal.ChronoUnit.DAYS;


public class book {
	//all attributes required by all classes are declared
	private int flag=0;
	private String isbn="N/A",totq="N/A",tak="N/A";
	private String nam="N/A",cat="N/A",auth="N/A";
	private Image cov;
	private Button bb,bba,bbv,but_rem,retbok;
	private Date return_date;
	private TextField i,n,c,a;

	long fine,lateness;
	private ImageView co;
	//we need an object of the controller to access methods in it
	private libmain_control controller;
	//constructor for return
	/**reader number "rednum" is not an attribute but it needs to be passed to a controller**/
	book(String isbn,String nam,String auth,Date rdat,String rednum){
		this.isbn=isbn;
		this.nam=nam;
		this.auth=auth;
		//date we got is sql.date we need to covert it to lacal date to be able to make operations on dates
		LocalDate rld=rdat.toLocalDate();
		//gets system date
		LocalDate nowdate =(new java.sql.Date(Calendar.getInstance().getTime().getTime())).toLocalDate();
		if(nowdate.isAfter(rld)){
			//count days between now and "supposedly"
			lateness= ChronoUnit.DAYS.between(rld,nowdate);
			fine=lateness*10;
		}
		else{
			lateness=fine=0;
		}
		retbok=new Button("return");
		retbok.setOnAction(e->{
			try {
				FXMLLoader loader=new FXMLLoader(getClass().getResource("/liblib/retrun/return_ui.fxml"));
				Parent root=loader.load();
				return_control controller = loader.getController();
				controller.addbook(this,rednum);
				Scene ret=new Scene(root);
				Stage st=new Stage();
				st.setScene(ret);
				st.show();

			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}

		});
	}
	//this  constructor is used for book objects declared in basket
	book(String isbn, String nam, String cat, String auth, String totq, String tak, Image cov,libmain_control controller){
		this.isbn=isbn;
		this.nam=nam;
		this.cat=cat;
		this.auth=auth;
		this.totq=totq;
		this.tak=tak;
		this.cov=cov;
		this.controller=controller;
		but_rem=new Button("remove");
		but_rem.setOnAction(e->{
			flag=0;
			controller.bbs.remove(this);
			controller.basket_refresh();
		});
		//---------------------------------------------------
		//TODO check if sending "Image cov" is really necessary
		//----------------------------------------------------
	}
	//this constructor is used by object of book created in the admin controller
	//it
	book(String isbn, String nam, String cat, String auth, String totq, String tak, Image cov){
		this.isbn=isbn;
		this.nam=nam;
		this.cat=cat;
		this.auth=auth;
		this.totq=totq;
		this.tak=tak;
		this.cov=cov;
		//TODO check if but_rem is necessary here
		but_rem=new Button("remove");
		but_rem.setOnAction(e->{
			flag=0;
			controller.bbs.remove(this);
			controller.basket_refresh();
		});
		//---------------------------------------------------
		//edit button is used in admin interface to edit basic information about books
		bb=new Button("edit");
		bb.setOnAction((event)->{
			Stage edito=new Stage();
			try {
				FXMLLoader loader=new FXMLLoader(getClass().getResource("edito_UI.fxml"));
				Parent root=loader.load();
				//gets controller of the add book interface
				//then it gives it the isbn for the book we are trying to edit
				editBcontrol editc=loader.getController();
				editc.initialize(isbn);
				Scene ss=new Scene(root);
				edito.setScene(ss);
				edito.show();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});
	}
	//this constructor is used for making objects of book for the librarian controller
	//specifically the search table
	//it takes TextFields and ImageView to be used by view button
	//it takes object of controller to be used by the add button
	book(String isbn, String nam, String cat, String auth, String totq, String tak, Image cov, TextField i, TextField n, TextField c, TextField a, ImageView v,libmain_control controller){
		this.isbn=isbn;
		this.nam=nam;
		this.cat=cat;
		this.auth=auth;
		this.totq=totq;
		this.tak=tak;
		this.cov=cov;
		this.i=i;
		this.n=n;
		this.c=c;
		this.a=a;
		this.co=v;
		this.controller=controller;
		//TODO check if commented block is useful
		/*controller.addbas.setOnAction(e->{
			controller.bbs.add(this);
			controller.basket_refresh();
		});*/
		//---------------------------------------------------
		//TODO check if but_rem is necessary
		but_rem=new Button("remove");
		but_rem.setOnAction(e->{
			flag=0;
			controller.bbs.remove(this);
			controller.basket_refresh();
		});

		//----------------------------------------------------
		//View button
		bbv=new Button();
		bbv.setText("View");
		bbv.setOnAction((event)->{
			//setting textFields with book details
			i.setText(isbn);
			n.setText(nam);
			c.setText(cat);
			a.setText(auth);
			v.setImage(cov);
			//if we are currently viewing a book we need to make sure we can add it to the basket using the big add basket button
			//this is why we passed the controller
			controller.addbas.setDisable(false);
			controller.addbas.setOnAction(e->{
				try {
					//if book not available show error message
					if(!db_book_connect.isAvailable(isbn)) {
						Alert alert=new Alert(Alert.AlertType.ERROR);
						alert.setContentText("out of stock");
						alert.showAndWait();

					}
					else{
						//else if it is available travese through the basket to check that it is not  already there
						for(book travers:controller.bbs){
							if(travers.isbn.equals(isbn))flag=1;
						}
						//if it is not in the basket add it to basket
						if(flag!=1) {

							controller.bbs.add(this);
							controller.basket_refresh();
							//TODO check if this assign statment is useful
							flag=1;
						}
						//else show error
						else{
							Alert alert=new Alert(Alert.AlertType.ERROR);
							alert.setContentText("book already in basket");
							alert.showAndWait();
						}
					}
				} catch (SQLException ex) {
					//Execption here means connection problems

					Alert alert=new Alert(Alert.AlertType.ERROR);
					alert.setContentText("check connection");
					alert.showAndWait();
					throw new RuntimeException(ex);
				}


			});
		});
		//the add without viewing button
		//the small add button next to each book
		bba=new Button();
		bba.setText("add");
		bba.setOnAction(e->{
			//it does same adding procedure like addbas see line 132
			try {
				if(!db_book_connect.isAvailable(isbn)) {
					Alert alert=new Alert(Alert.AlertType.ERROR);
					alert.setContentText("out of stock");
					alert.showAndWait();

				}
				else{
					for(book travers:controller.bbs){
						if(travers.isbn.equals(isbn))flag=1;
					}
					if(flag!=1) {
						flag=1;
						controller.bbs.add(this);
						controller.basket_refresh();
					}
					else{
						Alert alert=new Alert(Alert.AlertType.ERROR);
						alert.setContentText("book already in basket");
						alert.showAndWait();
					}
				}
			} catch (SQLException ex) {
				Alert alert=new Alert(Alert.AlertType.ERROR);
				alert.setContentText("check connection");
				alert.showAndWait();
				throw new RuntimeException(ex);

			}

		});
		//next block disables the add button if the book is out of stock to prevent the user from clicking it in the first place
		try {
			if(!db_book_connect.isAvailable(isbn)){
				bba.setDisable(true);
				controller.addbas.setDisable(true);
			}
		} catch (SQLException e) {
			//Connection Error
			throw new RuntimeException(e);
		}

		//----------------------------------------------------
	}
	//GETTERS for setCellValueFactory method
	public String getIsbn(){return isbn;}
	public String getNam(){return nam;}
	public String getCat(){return cat;}
	public String getAuth(){return auth;}
	public String getTotq(){return totq;}
	public String getTak(){return tak;}
	public Button getBb() {return bb;}
	public Image getCov() {return cov;}
	public Button getBba() {return bba;}
	public Button getBbv() {return bbv;}
	public Button getBut_rem() {return but_rem;}
	public Button getRetbok() {return retbok;}
	public long getFine() {return fine;}
	public long getLateness() {return lateness;}
}
