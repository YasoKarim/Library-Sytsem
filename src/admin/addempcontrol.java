package admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert.AlertType;

public class addempcontrol {

    @FXML
    private TextField name;
    @FXML
    private TextField pass;
    @FXML
    private TextField email;
    @FXML
    private TextField number;
    @FXML
    private TextField address;
    @FXML
    private TextField ejobid;
    @FXML
    private Button save;
    @FXML
    private Button close;

    public addempcontrol() {
    }

    @FXML
    public void AddMember(ActionEvent event) throws SQLException {
        String ename = this.name.getText();
        String emppass = this.pass.getText();
        String empnumber = this.number.getText();
        String ejboid = this.ejobid.getText();
        String empemail = this.email.getText();
        String empaddress = this.address.getText();
        Boolean check = ename.isEmpty() || emppass.isEmpty() || empemail.isEmpty() || ejboid.isEmpty() || empnumber.isEmpty() || empaddress.isEmpty();
        if (check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText((String)null);
            alert.setContentText("Please enter in all fields");
            alert.showAndWait();
        } else {
            Connection connection = DriverManager.getConnection("jdbc:mysql:// projectlibprog.mysql.database.azure.com", "project@projectlibprog", "rootrt*1");
            Statement statement = connection.createStatement();
            String add = "INSERT INTO `proj`.`emps` (`ename`,`epass`,`ejobid`,`empemail`,`empnumber`, `empaddress`) VALUES ('" + ename + "','" + emppass + "'," + ejboid + ",'" + empemail + "','" + empnumber + "','" + empaddress + "');";
            statement.execute(add);
            ResultSet added = statement.executeQuery("SELECT * FROM `proj`.`emps`;");
            Alert alert;
            if (added.next()) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Save Reader");
                alert.setHeaderText((String)null);
                alert.setContentText("Saved");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText((String)null);
                alert.setContentText("Error Occured");
                alert.showAndWait();
            }

            connection.close();
        }
    }

    /*public void Close(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close window");
        alert.setHeaderText((String)null);
        alert.setContentText("Do you want to close the window");
        alert.showAndWait();
        stage.close();
    }*/
}

