package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class db_user_connect {
    static String url="jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj";
    static String n="project@projectlibprog",pass="rootrt*1";

     public ObservableList<Emp>fillt(ResultSet insert){
        ObservableList<Emp>oo=FXCollections.observableArrayList();
        try {
            while(insert.next()) {
                String joba;
                //System.out.println(insert.getString(3));
                joba=(insert.getString(3).equals("1"))?"admin":"librarian";
                oo.add(new Emp(insert.getString(1),insert.getString(2),joba,insert.getString(4),insert.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return oo;
    }
    //ObservableList<Emp>
    public void getAll(ObjectOutputStream out, ObjectInputStream in) throws SQLException, IOException {
        Connection c=DriverManager.getConnection(url,n,pass);
        Statement ss=c.createStatement();
        ResultSet ans= ss.executeQuery("SELECT eid,ename,ejobid,empemail,empnumber FROM emps");
        ObservableList<Emp>oo=fillt(ans);
        c.close();
        int num=0;
        for(Server.Emp i:oo){
            num++;
        }
        out.writeObject(num);
        out.flush();
        for(Server.Emp i:oo){
            try {
                out.writeObject(i);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //public ResultSet search(String key) {

    //}
}
