package liblib.retrun;

import admin.book;
import admin.db_book_connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

public class return_control implements Initializable {
    String rednum;
    @FXML
    TableView<book>returned;
    @FXML
    TableColumn<book,String>isbn;
    @FXML
    TableColumn<book,String>name;
    @FXML
    TableColumn<book,String>author;
    @FXML
    TableColumn<book,Long>dlate;
    @FXML
    TableColumn<book,Long>latfine;
    @FXML
    Button confirm;
    @FXML
    Button cancel;
    ObservableList<book>return_list;

    @FXML
    Label stot;
    @FXML
    Label tax;
    @FXML
    Label tot;

    public void addbook(ObservableList<book>relist,String rednum){
        this.rednum=rednum;
        return_list=relist;
        double total=0,sum=0;
        returned.setItems(return_list);
        for(book trav:return_list){
            sum+=trav.getFine();
        }

        stot.setText(String.valueOf(sum));
        total=sum*14/100;
        tax.setText(String.valueOf(total));
        total=total+sum;
        tot.setText(String.valueOf(total));
    }
    public void addbook(book rb,String rednum) {
        this.rednum = rednum;
        return_list.add(rb);
        returned.setItems(return_list);

        stot.setText(String.valueOf(rb.getFine()));

        tax.setText(String.valueOf((rb.getFine()*14/100)));
        long total= rb.getFine()+(rb.getFine()*14/100);
        tot.setText(String.valueOf(total));
        //TODO set labels for a one book setter

    }
    //It is used to print data to a new file (fine and returned book)
    public void output(){
        try {
            for(book i:return_list) {
                db_book_connect.returner(rednum,i.getIsbn());
            }
            PrintWriter printWriter = new PrintWriter(rednum+"Receipt.txt");

            printWriter.println("Reader number: " + rednum);
            printWriter.println("-------------------------------------------------------");
            printWriter.println(String.format("%-20s %-20s" , "Book name" , "Fine"));

            //iterate over the return list
            for (book i : return_list) {

                printWriter.println(String.format("%-20s %-20s", i.getNam() ,  i.getFine() ));
            }
            printWriter.println("SubTotal: " + stot.getText());
            printWriter.println("Tax:      " + tax.getText());
            printWriter.println("------------------------------------------------------");
            printWriter.println("Total   : " + tot.getText());
            printWriter.close();
            Alert alert;
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save Return");
            alert.setHeaderText((String)null);
            alert.setContentText("Saved successfully");
            alert.showAndWait();
            Stage stage=(Stage)stagepane.getScene().getWindow();
            stage.close();
            //TODO email the receipt

        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
    @FXML
    AnchorPane stagepane;
    public void canceller(){
        Stage stage=(Stage)stagepane.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        return_list= FXCollections.observableArrayList();
        isbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        name.setCellValueFactory(new PropertyValueFactory<>("nam"));
        author.setCellValueFactory(new PropertyValueFactory<>("auth"));
        dlate.setCellValueFactory(new PropertyValueFactory<>("lateness"));
        latfine.setCellValueFactory(new PropertyValueFactory<>("fine"));
    }
}
