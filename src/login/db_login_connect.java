package login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class db_login_connect {
	//connection url//
	static String url="jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj";

	//name and password//
	static String n="project@projectlibprog",pass="rootrt*1";


	//checks if password is right and returns the jobid to the controller or returns the error statement//
	static String checkpass(String name,String p) throws IOException, ClassNotFoundException {

		Socket socket = new Socket("localhost", 2003);
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

		out.writeObject("db_login_connect");
		out.flush();
		out.writeObject("checkpass");
		out.flush();
		out.writeObject(name);
		out.flush();
		out.writeObject(p);
		out.flush();
		String ans = (String) in.readObject();
		out.close();
		in.close();
		socket.close();
		return ans;
	}

	//we usually search in the database using the primary key
	//this method converts the username of the employee to his id in order to be sent to a controller to print it in a receipt
	static String getid(String name) throws IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost", 2003);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		out.writeObject("db_login_connect");
		out.flush();
		out.writeObject("getid");
		out.flush();
		out.writeObject(name);
		out.flush();
		//to read from the server
		String response = (String) in.readObject();
		out.close();
		in.close();
		socket.close();
		return response;
	}
	static void passforgot(String key) throws IOException {
		Socket socket = new Socket("localhost", 2003);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		out.writeObject("db_login_connect");
		out.flush();
		out.writeObject("passforgot");
		out.flush();
		out.writeObject(key);
		out.flush();

		out.close();
		in.close();
		socket.close();
		return;
	}

	}
/*String q="select epass,ejobid from emps where eid='"+name+"' or ename=\'"+name+"\'";
		Connection c = null;
		String response;
		try {
			c = DriverManager.getConnection(url,n,pass);

			Statement st=c.createStatement();

			ResultSet ans=st.executeQuery(q);
			//if there is an answer it means the name or id was found and it is an employee
			// now we check the password using the data in the resultset
			if(ans.next()) {
				if(ans.getString(1).equals(p)) {
					//if the password is also right return the job id
					// 1 for admin 2 for librarian
					response = ans.getString(2);
					c.close();
					return response;
				}
				else {
					//password turned out to be wrong
					c.close();
					return "password wrong";
				}
			}
			else {
				//having no result set means the id was not found in database
				//in other words employee doesn't exist
				c.close();
				return "wrong id";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(c!=null)c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//reaching this point means an error happened in the connection process
		//the error was probably caused by network issues
		return "unknown error check terminal";

		try {
			//the user might enter an id or a username
			//we check here if the value entered by user isn't already an id
			Integer.parseInt(name);
			return name;
		}
		catch (Exception e){
			//trying to convert a username to an integer will cause an exception
			//we catch it and get the corresponding id from the database
			Connection c = DriverManager.getConnection(url,n,pass);
			Statement st=c.createStatement();
			ResultSet ans=st.executeQuery("select eid from emps where ename='"+name+"'");
			ans.next();
			String res= ans.getString(1);
			c.close();
			return res;











		*/