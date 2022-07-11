package liblib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.net.Socket;
import java.sql.*;

public abstract class DB_liblib_connect {
    static String url="jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj";
    static String n="project@projectlibprog",pass="rootrt*1";
    //
    public static void copyObject(Object src, Object dest)
            throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SecurityException {
        for (Field field : src.getClass().getFields()) {
            dest.getClass().getField(field.getName()).set(dest, field.get(src));
        }
    }

    public static boolean IsRegRed(String rednum) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost",2003);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        out.writeObject("DB_liblib_connect");
        out.flush();
        out.writeObject("IsRegRed");
        out.flush();
        out.writeObject(rednum);
        out.flush();
        //to read from the server
        Boolean response = (Boolean) in.readObject();
        out.close();
        in.close();
        socket.close();
        return response;

    }
    //regred adds new reader to the database
    public static void regred(String pnum,String nam,String email,String addr) throws IOException {
        Socket socket = new Socket("localhost",2003);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        out.writeObject("DB_liblib_connect");
        out.flush();
        out.writeObject("regred");
        out.flush();
        out.writeObject(pnum);
        out.flush();
        out.writeObject(nam);
        out.flush();
        out.writeObject(email);
        out.flush();
        out.writeObject(addr);
        out.flush();
        //to read from the server
        out.close();
        in.close();
        socket.close();

    }
}
/*
Connection c= DriverManager.getConnection(url,n,pass);
        PreparedStatement ss= c.prepareStatement("INSERT INTO `proj`.`reader`(`Rnumber`,`Rname`,`Remail`,`Raddress`)VALUES(?,?,?,?);");
        ss.setNString(1,pnum);
        ss.setNString(2,nam);
        ss.setNString(3,email);
        ss.setNString(4,addr);
        ss.executeUpdate();
        c.close();
 */