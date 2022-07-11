/*package nw;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class neww implements Initializable {
    @FXML
    ImageView vv;
    public void mm() throws IOException, SQLException {
        Connection c= DriverManager.getConnection("jdbc:mysql://projectlibprog.mysql.database.azure.com:3306/proj",
                                                  "project@projectlibprog",
                                                  "rootrt*1");
        Statement ss=c.createStatement();
        ResultSet ans=ss.executeQuery("select photo from emps where eid=1");
        ans.next();
        byte[] imag= ans.getBytes(1);
        Image im=new Image(new ByteArrayInputStream(imag));

        vv.setImage(im);
        vv.setRotate(90);
        vv.setFitHeight(300);
        vv.setFitWidth(200);


        File fil=new File("C:\\Users\\THE KING IS HERE\\Downloads\\pp1.jpg");
        FileInputStream mm =new FileInputStream(fil);

        PreparedStatement pp=c.prepareStatement("UPDATE `proj`.`emps`\n" +
                "SET\n" +
                "\n" +
                "`photo` = ?\n" +
                "WHERE `eid` = 1;\n");
        pp.setBinaryStream(1,mm,(int)fil.length() );
        pp.executeUpdate();



        File file = new File("outputtt.png");
        FileOutputStream output = new FileOutputStream(file);

        ans.next();
        InputStream inner=ans.getBinaryStream(1);
        byte[] buffer=new byte[1024];
        while(inner.read(buffer)>0){
            output.write(buffer);
        }

        c.close();



    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image im=new Image("D:\\WorkSpace\\lib_excellency\\book.jpg");
        vv.setImage(im);
    }
}*/
