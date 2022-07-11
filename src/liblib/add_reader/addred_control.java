package liblib.add_reader;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import liblib.DB_liblib_connect;
import liblib.libmain_control;

import java.io.IOException;
import java.sql.SQLException;

public class addred_control {
    @FXML
    AnchorPane pp;
    libmain_control main;
    @FXML
    TextField pnum;
    @FXML
    TextField name;
    @FXML
    TextField email;
    @FXML
    TextField addr;
    //takeP is called in libmain_control to send those necessary information
    //1-pnum:it is the customer number
    //we send pnum to eliminate the need to rewrite the customer number in the new stage
    //2-mm:it is an object of libmain controller
    //we send it to confirm the reader after recording his information in the database
    public void takeP(String pnum,libmain_control mm){
        this.pnum.setText(pnum);
        main=mm;
    }
    public void save(){
        try {
            //send reader details
            DB_liblib_connect.regred(pnum.getText(),name.getText(),email.getText(),addr.getText());
            //confirm reader in libmain controller
            main.regred();
            //close the add reader stage after adding the reader
            Stage sma=(Stage)pp.getScene().getWindow();
            sma.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("DID NOT SAVE");
            alert.showAndWait();
        }
    }

}
