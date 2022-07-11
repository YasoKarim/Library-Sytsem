package Server;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Message;
import com.sun.mail.smtp.SMTPMessage;
import com.sun.mail.smtp.SMTPSaslAuthenticator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
public class db_login_connect{
	//connection url//
	static String url="jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj";

	//name and password//
	static String n="project@projectlibprog",pass="rootrt*1";


	//checks if password is right and returns the jobid to the controller or returns the error statement//
	/*String name,String p*/
	 void checkpass(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
		String name= (String)in.readObject();
		String p=(String)in.readObject();

		String q="select epass,ejobid from emps where eid='"+name+"' or ename=\'"+name+"\'";
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
					//return
					out.writeObject(response);
				}
				else {
					//password turned out to be wrong
					c.close();
					//return
					out.writeObject("password wrong");
				}
			}
			else {
				//having no result set means the id was not found in database
				//in other words employee doesn't exist
				c.close();
				//return
				out.writeObject("wrong id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(c!=null)c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//reaching this point means an error happened in the connection process
		//the error was probably caused by network issues
		out.writeObject("unknown error check terminal");
	}

	//we usually search in the database using the primary key
	//this method converts the username of the employee to his id in order to be sent to a controller to print it in a receipt
	/*String name*/
	void getid(ObjectOutputStream out, ObjectInputStream in) throws SQLException, IOException, ClassNotFoundException {
		String name = (String)in.readObject();
		try {
			//the user might enter an id or a username
			//we check here if the value entered by user isn't already an id
			Integer.parseInt(name);
			out.writeObject(name);
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
			out.writeObject(res);
		}

	}
	//passforgot needs to find the email to initiate an smtp"email" sequence
	void passforgot(ObjectOutputStream out, ObjectInputStream in) {
		try {
			//reads name or id from client
			String key=(String)in.readObject();
			//connects to database
			Connection c = DriverManager.getConnection(url,n,pass);
			Statement st=c.createStatement();
			/**we search by ename first because if the key if characters using it for eid will give an error**/
			ResultSet ans=st.executeQuery("select empemail from emps where ename='"+key+"';");
			if(ans.next()){
				email(ans.getString(1));
			}
			//if first query doesn't find a result then the key is propably an id
			//then we search for it as a number
			else{
				ans=st.executeQuery("select empemail from emps where eid="+key+";");
				if(ans.next())email(ans.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	void email(String to) throws MessagingException {
		//to="dwidar.mohamed@icloud.com";
		String from="lib.sys.proj@gmail.com";
		String ms="test email sent";
		String sub="library password change";
		String smtp="smtp.google.com";
		//System.setProperty("java.net.preferIPv4Stack", "true");
		Properties prop=new Properties();

		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "465");

		Session session=Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from,"rootrt*1");
			}
		});
		Message mess=new MimeMessage(session);
		mess.setFrom(new InternetAddress(from));
		mess.setRecipient(Message.RecipientType.TO,new InternetAddress(to,false));
		mess.setSubject(sub);
		mess.setText(ms);

		mess.setSentDate(new Date());
		Transport.send(mess);
		System.out.println("email sent");

	}
	/**this method doesn't need in and out streams because it is only called by the server itself and never by the client**/
	public static void uppass(String email,String pass) throws SQLException {
		Connection c = DriverManager.getConnection(url,n,pass);
		PreparedStatement ps=c.prepareStatement("UPDATE `proj`.`emps` SET `epass` =?, WHERE `empemail` = ?;");
		ps.setNString(1,pass);
		ps.setNString(2,email);
		ps.execute();
		c.close();
	}
}
