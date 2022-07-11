package admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class editBcontrol {
    @FXML
    AnchorPane st;
    public String isbnsearch;
    @FXML
    TextField isbn;
    @FXML
    TextField nam;
    @FXML
    TextField Cat;
    @FXML
    TextField auth;
    @FXML
    TextField quant;
    @FXML
    ImageView im;


    //not automatically called
    public void initialize(String isbnn) {
        isbnsearch=isbnn;
        try {
            //System.out.println("---------22"+isbnsearch);
            book boo=db_book_connect.getBook(isbnsearch);
            isbn.setText(boo.getIsbn());
            nam.setText(boo.getNam());
            Cat.setText(boo.getCat());
            auth.setText(boo.getAuth());
            quant.setText(boo.getTotq());
            im.setImage(boo.getCov());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void save() throws SQLException, IOException {

        db_book_connect.upbook(isbn.getText(),quant.getText(),im.getImage());
        Stage ss=(Stage)st.getScene().getWindow();
        ss.close();
    }
    //chan method opens a file chooser to get the directory of the new image
    public void chan() throws FileNotFoundException {
        FileChooser dd=new FileChooser();
        File mam=dd.showOpenDialog(new Stage());
        Image imm=new Image(new FileInputStream(mam));
        im.setImage(imm);
    }
    public void cancel(){
        Stage ss=(Stage)st.getScene().getWindow();
        ss.close();
    }
}
    /*public void Close(ActionEvent event){
        Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close window");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to close the window");
        alert.showAndWait();
        stage.close();
    }*/