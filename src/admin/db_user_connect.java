package admin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class db_user_connect {
	static String url="jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj";
	static String n="project@projectlibprog",pass="rootrt*1";
	public static void copyObject(Object src, Object dest)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		for (Field field : src.getClass().getFields()) {
			dest.getClass().getField(field.getName()).set(dest, field.get(src));
		}
	}

	public static ObservableList<Emp> getAll() throws IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost",2003);
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

		out.writeObject("db_user_connect");
		out.flush();
		out.writeObject("getAll");
		out.flush();
		int number =(Integer)in.readObject();

		ObservableList<Emp>ol=FXCollections.observableArrayList();
		for(int i =0;i<number;i++){
			Emp e=new Emp();
			try {
				System.out.println(i);
				Object o=in.readObject();
				copyObject(o,e);
				ol.add(e);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}


		}
		out.close();
		in.close();
		socket.close();
		return ol;

	}

	//public ResultSet search(String key) {
		
	//}
}
/*
ObservableList<Emp>oo=FXCollections.observableArrayList();
		try {
			while(insert.next()) {
				String joba;
				//System.out.println(insert.getString(3));
				joba=(insert.getString(3).equals("1"))?"admin":"librarian";
				oo.add(new Emp(insert.getString(1),insert.getString(2),joba,insert.getString(4),insert.getString(5)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return oo;
 */