package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.sql.*;

public  class DB_liblib_connect {
    static String url="jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj";
    static String n="project@projectlibprog",pass="rootrt*1";
    //IsRegred looks for reader number in the database
    //it returns true if found false if not
    //tn is counter for the number of tries
    //String rednum,int tn
    public void IsRegRed(ObjectOutputStream out, ObjectInputStream in){
        try {
            String rednum=(String)in.readObject();

            Connection c= DriverManager.getConnection(url,n,pass);
            Statement ss=c.createStatement();
            ResultSet ans= ss.executeQuery("SELECT * FROM reader where Rnumber='"+rednum+"';");
            if(ans.next()) {
                    out.writeObject(true);
                    out.flush();
            }
            else {
                out.writeObject(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //******IMPORTANT*******//
            //if we try to confirm a reader and there was a network error the program might ask the for the reader to be added again
            //trying to add the same reader again will result in an error
            //this block tries to confirm the reader again in network error cases
            try {
                out.writeObject(false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //**********************//
        }
    }
    //regred adds new reader to the database
    //String pnum,String nam,String email,String addr
    public void regred(ObjectOutputStream out, ObjectInputStream in) throws SQLException {
        try {
            String pnum=(String)in.readObject();
            String nam = (String)in.readObject();
            String email = (String)in.readObject();
            String addr = (String)in.readObject();

            Connection c= DriverManager.getConnection(url,n,pass);
            PreparedStatement ss= c.prepareStatement("INSERT INTO `proj`.`reader`(`Rnumber`,`Rname`,`Remail`,`Raddress`)VALUES(?,?,?,?);");
            ss.setNString(1,pnum);
            ss.setNString(2,nam);
            ss.setNString(3,email);
            ss.setNString(4,addr);
            ss.executeUpdate();
            c.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
