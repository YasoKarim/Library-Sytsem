package admin;

import java.awt.image.BufferedImage;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import liblib.libmain_control;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

public abstract class db_book_connect {
	//database login credentials
	static String url="jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj";
	static String n="project@projectlibprog",pass="rootrt*1";

	//defim takes an array of bytes representing an image loaded from a file
	//then defim converts it to a javafx image that will later be displayed in an ImageView

	static private Image defim(byte[]imam){
		Image im;
		//if image is null we put a default image representing that there is no cover
		if(imam!=null){
			//we get the array through an input stream to be able to convert to to an image
			im=new Image(new ByteArrayInputStream(imam));
		}
		else{
			//the default image is taken from the internet
			im=new Image("https://images.assetsdelivery.com/compings_v2/yehorlisnyi/yehorlisnyi2104/yehorlisnyi210400016.jpg");
		}
		return im;
	}
	//---fillt methods take resultsets from database and use to make objects that will be stored in-
	// an observable list
	//we need an observablelist to be able to display it in a tableView
	static public ObservableList<book>fillt(ResultSet insert){
		//new observablelist
		ObservableList<book>oo=FXCollections.observableArrayList();
		try {
			//if there is a next row in the resultset make an object with it
			while(insert.next()) {
				//make the object and store it in the observable list
				oo.add(new book(insert.getString(1),insert.getString(2),insert.getString(3),insert.getString(4),insert.getString(5),insert.getString(6),defim(insert.getBytes(7))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return oo;
	}

	//this fillt is no different from the first except that it is used as a bridge to pass the book imageView and details textFields
	//we pass those objects because in class book the view button edits those fields
	//the controller is used to access the big add to basket button and make it add that specefic object of book
	static public ObservableList<book>fillt(ResultSet insert, TextField i, TextField n, TextField c, TextField a, ImageView v, libmain_control controller){
		//the method body is the same except that it calls a different book constructor
		ObservableList<book>oo=FXCollections.observableArrayList();
		try {
			while(insert.next()) {
				//System.out.println(insert.getString(1));
				oo.add(new book(insert.getString(1),insert.getString(2),insert.getString(3),insert.getString(4),insert.getString(5),insert.getString(6),defim(insert.getBytes(7)),i,n,c,a,v,controller));
			}
		} catch (SQLException e) {
			    //TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return oo;
	}

	static public ObservableList<book>filltreturn(ResultSet insert,String rednum){
		//the method body is the same except that it calls a different book constructor
		ObservableList<book>oo=FXCollections.observableArrayList();
		try {
			while(insert.next()) {
				//System.out.println(insert.getString(1));
				oo.add(new book(insert.getString(1),insert.getString(2),insert.getString(3),insert.getDate(4),rednum));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return oo;
	}
	//the getall method is used only for the admin
	//when the admin interface is opened getAll is called to display all books
	public static ObservableList<book> getAll() throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ResultSet ans= ss.executeQuery("SELECT * FROM books");
		//fillt converts result set to observable list as mentioned in line 62
		ObservableList<book>oo=fillt(ans);
		c.close();
		return oo;
	}

	//addbook adds a new book to the database
	public static void addbook(String isbn,String nm,String categ,String auth,String qt) throws Exception{
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ss.execute("INSERT INTO `proj`.`books`\r\n"
				+ "(`ISBN`,\r\n"
				+ "`Bname`,\r\n"
				+ "`Category`,\r\n"
				+ "`Author`,\r\n"
				+ "`TotQT`)\r\n"
				+ "VALUES\r\n"
				+ "("+isbn+",'"+nm+"','"+categ+"','"+auth+"',"+qt+");");
		ResultSet isadd=ss.executeQuery("select * from books where isbn="+isbn);
		Alert alert;
		if(isadd.next()){
			alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Saved");
		}
		else{
			alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Error Occured");
		}
		alert.showAndWait();
		c.close();
	}
	//
	//search takes a key word from admin and searches for it in the book database then returns the results in an observable list
	public static ObservableList<book> search(String key) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ResultSet ans=ss.executeQuery("select * from books where (Bname like '%"+key+"%') or (Category like '%"+key+"%') or (Author like'%"+key+"%')");
		ObservableList<book>oo=fillt(ans);
		c.close();
		return oo;
	}

	//this search does the same thing but its also used as a bridge to pass information to the constructor look line 62
	//!!!Not passing those information will result in buttons add/view not showing !!!!
	public static ObservableList<book> search(String key, TextField i, TextField na, TextField ca, TextField a,ImageView v,libmain_control controller) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ResultSet ans=ss.executeQuery("select * from books where (Bname like '%"+key+"%') or (Category like '%"+key+"%') or (Author like'%"+key+"%')");
		ObservableList<book>oo=fillt(ans,i,na,ca,a,v,controller);
		c.close();
		return oo;
	}

	//takes isbn and returns details from database
	public static book getBook(String isbn) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ResultSet ans=ss.executeQuery("select * from books where isbn ="+isbn);
		ans.next();
		Image im=defim(ans.getBytes(7));
		book ret=new book(ans.getString(1),ans.getString(2),ans.getString(3),ans.getString(4),ans.getString(5),ans.getString(6),im);
		return ret;
	}

	/**takes controller because it needs to create a book that has remove button that edits basket inside controller**/
	public static book getBook(String isbn,libmain_control controller) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ResultSet ans=ss.executeQuery("select * from books where isbn ="+isbn);
		ans.next();
		Image im=defim(ans.getBytes(7));
		book ret=new book(ans.getString(1),ans.getString(2),ans.getString(3),ans.getString(4),ans.getString(5),ans.getString(6),im,controller);
		return ret;
	}
	//upbook takes information entered by the admin and sent by the admin controller
	//then it updates these information for that certain book with the same isbn
	public static void upbook(String isb, String quant, Image im) throws SQLException, IOException {
		//creates new file
		File fileim=new File("imbuf");
		//writes imge to file
		ImageIO.write(SwingFXUtils.fromFXImage(im,null),"png",fileim);

		FileInputStream fin=new FileInputStream(fileim);
		Connection c=DriverManager.getConnection(url,n,pass);
		PreparedStatement ss=c.prepareStatement("UPDATE `proj`.`books` SET `TotQT` = ? , cover = ? WHERE `ISBN` = ? ;");
		ss.setNString(1,quant);
		ss.setBinaryStream(2,fin,(int)fileim.length());
		ss.setNString(3,isb);
		ss.executeUpdate();
		c.close();
	}
	//TODO complete the delete
	public static void delete(String isb) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		//ss.execute("UPDATE `proj`.`books` " + "SET" + " `TotQT` = "+quant+ " WHERE `ISBN` = "+isb+";");
		c.close();
	}
	//isAvailable check if book is available in stock
	//we check to prevent the user from adding the book to the basket if the book is out of stock
	public static boolean isAvailable(String isbn) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		//TotQT is the total number of copies of that book in/out of the library
		//taken is number of borrowed books
		ResultSet ans=ss.executeQuery("select TotQT,taken from books where isbn ="+isbn);
		ans.next();
		//the subtraction of total and taken will result in the total of availa
		int av=ans.getInt(1)-ans.getInt(2);
		c.close();
		if(av>0)return true;
		return false;
	}
	//borrow_trans adds one to the taken num in taken column in table book
	public static boolean borrow_trans(String eid, String rid, String isbn, LocalDate da) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		//increments taken value in database by one
		//one: get the number rom db
		ResultSet inc=ss.executeQuery("select taken from books where isbn="+isbn);
		inc.next();
		//two: convert it to integer then add one
		int tak=inc.getInt(1);
		tak++;
		//three update the database
		ss.execute("UPDATE books SET taken ="+tak+" WHERE `ISBN` ="+isbn+";");
		//checks if borrowed
		//TODO check if useful
		ResultSet isborrowed=ss.executeQuery("select * from borrow where readerid='"+rid+"' and ISBN="+isbn+" and bdate='"+Date.valueOf(da)+"'");
		if(isborrowed.next()) {
			c.close();
			return false;
		}
		else{
			//gets system date and puts it in a date datatype to be sent as the borrow date
			Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			PreparedStatement ps=c.prepareStatement("INSERT INTO borrow VALUES(?,?,?,?,?);");
			ps.setInt(1,Integer.parseInt(isbn));
			ps.setDate(2,date);
			ps.setNString(3,rid);
			ps.setInt(4,Integer.parseInt(eid));
			ps.setDate(5,Date.valueOf(da));
			ps.execute();
			c.close();
			return true;
		}
	}
	public static boolean didborbef(String isbn,String rednum) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ResultSet response=ss.executeQuery("select ISBN from borrow where ISBN="+isbn+" and readerid='"+rednum+"';");
		if(response.next()){
			c.close();
			return true;
		}
		c.close();
		return false;

	}
	/**gets the currently borrowed books by a reader**/
	public static ObservableList<book> getReturnList(String rednum) throws SQLException {
		Connection c=DriverManager.getConnection(url,n,pass);
		Statement ss=c.createStatement();
		ResultSet ans=ss.executeQuery("select books.isbn,books.bname,books.author,borrow.rdate from books,borrow where books.isbn=borrow.isbn and borrow.readerid="+rednum+";");
		ObservableList<book> oo=filltreturn(ans,rednum);
		c.close();
		return oo;
	}

	public static void returner(String rednum,String isbn){
		try {
			Connection c = DriverManager.getConnection(url, n, pass);
			PreparedStatement ps = c.prepareStatement("DELETE FROM `proj`.`borrow` WHERE readerid = ? and isbn = ?;");
			ps.setNString(1,rednum);
			int is=Integer.parseInt(isbn);
			ps.setInt(2,is);
			ps.execute();
			c.close();
			return;
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

}



