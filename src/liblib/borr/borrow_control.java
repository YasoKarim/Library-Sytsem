package liblib.borr;

import admin.book;
import admin.db_book_connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class borrow_control {
    @FXML
    DatePicker gen_dat;
    public ObservableList<borRec>shoplist;
    public String rid,eid;
    @FXML
    TableView<borRec> list;
    @FXML
    TableColumn<borRec,String>is;
    @FXML
    TableColumn<borRec,String>nam;
    @FXML
    TableColumn<borRec,String>aut;
    @FXML
    TableColumn<borRec, DatePicker>lodatp;
    @FXML
    AnchorPane exit;
    //the method takes employee id and customer number to record them and associate the borrow transaction with them
    //it takes the shopping list to display it and set return dates
    public void setneeds(String rid,String eid,ObservableList<book>blist){
        this.rid=rid;
        this.eid=eid;
        this.shoplist= FXCollections.observableArrayList();
        //converts books to borrow records in order to have a date picker
        for(book b:blist){
            shoplist.add(new borRec(b,rid,eid));
        }
        is.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        nam.setCellValueFactory(new PropertyValueFactory<>("name"));
        aut.setCellValueFactory(new PropertyValueFactory<>("author"));
        lodatp.setCellValueFactory(new PropertyValueFactory<>("rdatp"));
        list.setItems(shoplist);
    }
    //Execute traverses through the shopping list and check if user set a return date for every book
    //if user didn't choose a return date for a certain book it takes the general return date set by the user
    //if user didn't set general return date it asks him to do so
    public void execute_transaction() throws SQLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        //go through shopping list
        for(borRec rec:shoplist){
            //check if reader already has the book
            if(!db_book_connect.didborbef(rec.isbn,rec.rid)) {

                LocalDate da;
                //if he didn't choose a specific return date
                if (rec.rdatp.getValue() == null) {
                    //check the general return date field
                    if (gen_dat.getValue() != null) {
                        //take the general date
                        da = gen_dat.getValue();
                    }
                    //else no date is picked then ask the user to pick one
                    else {
                        /**both date pickers are empty**/
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("no date picked");
                        alert.showAndWait();
                        //********cancel execute*********//
                        //proceeding without cancelling will result in an error
                        return;
                        //*******************************//
                    }
                }
                //if he did choose a specific date
                else {
                    da = rec.rdatp.getValue();
                }
                //send the details to the database connect class to be stored in the database
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File("borsou.wav").getAbsoluteFile());
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.loop(0);
                db_book_connect.borrow_trans(rec.eid, rec.rid, rec.isbn, da);


                Stage here=(Stage)exit.getScene().getWindow();
                here.close();
            }
            else{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("error already borrowed please cancel and remove borrowed books");
                alert.showAndWait();
            }
        }
    }

    public void canceller(){
        Stage stage=(Stage)exit.getScene().getWindow();
        stage.close();
    }





}
