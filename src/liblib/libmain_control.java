package liblib;

import admin.book;
import admin.db_book_connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import liblib.QR.qr_handel;
import liblib.add_reader.addred_control;
import liblib.borr.borrow_control;
import liblib.retrun.return_control;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class libmain_control implements Initializable {
    public ObservableList<book>bbs;
    public ObservableList<book>search_res;
    public String empid;
    public void setEmpid(String empid){
        this.empid=empid;
    }


    //-----------------------------------------------------------
    //readers hub
    @FXML
    TextField renum;
    public void regred() throws IOException, ClassNotFoundException {
        String rednum=renum.getText();
        if(DB_liblib_connect.IsRegRed(rednum)){
            sercher.setDisable(false);
            bbs= FXCollections.observableArrayList();
            search_res=FXCollections.observableArrayList();
            bbs.removeAll();
            search_res.removeAll();
            basket_tabl.setItems(bbs);
            bees.setItems(search_res);

            //setting the return table
            returnname.setCellValueFactory(new PropertyValueFactory<>("nam"));
            author.setCellValueFactory(new PropertyValueFactory<>("auth"));
            returnButton.setCellValueFactory(new PropertyValueFactory<>("retbok"));
            try {
                returntable.setItems(db_book_connect.getReturnList(renum.getText()));
            } catch (SQLException e) {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("check connection");
                alert.showAndWait();
            }


        }
        else{
            Stage adred=new Stage();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("add_reader/addred.fxml"));
            Parent root= loader.load();
            addred_control cc1=loader.getController();
            cc1.takeP(renum.getText(),this);
            Scene adre=new Scene(root);
            adred.setScene(adre);
            adred.show();
        }


    }
    //-----------------------------------------------------------
    //return hub
    @FXML
    TableView<book> returntable;
    @FXML
    TableColumn<book,String>returnname;
    @FXML
    TableColumn<book, String>author;
    @FXML
    TableColumn<book, Button>returnButton;

    public void retAll(){
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/liblib/retrun/return_ui.fxml"));
            Parent root=loader.load();
            return_control controller = loader.getController();
            controller.addbook(returntable.getItems(),renum.getText());
            Scene ret=new Scene(root);
            Stage st=new Stage();
            st.setScene(ret);
            st.show();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    //-----------------------------------------------------------
    //borrow hub
    public void check_out(){
        try {
            Stage chout=new Stage();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("borr/borrow_ui.fxml"));
            Parent root=loader.load();
            borrow_control borcon = loader.getController();
            borcon.setneeds(renum.getText(),empid,bbs);
            Scene ss=new Scene(root);
            chout.setScene(ss);
            chout.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Button activate_qr,deactiv_qr;
    Thread qr_thread;
    qr_handel cam;
    public void act_qr()
    {
        cam=new qr_handel(this);
        qr_thread = new Thread(cam);
        qr_thread.start();
        deactiv_qr.setDisable(false);
        activate_qr.setDisable(true);

    }
    public void kill_cam(){
        cam.flag=0;
        activate_qr.setDisable(false);
        deactiv_qr.setDisable(true);
    }
    public void add_qr_read(String isbn){
        try {
            if(!db_book_connect.isAvailable(isbn)) {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("out of stock");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("check connection");
            alert.showAndWait();
        }
        int flag=0;
        try {
            for(book travers:bbs){
                if(travers.getIsbn().equals(isbn))flag=1;
            }
            if(flag!=1) {
                bbs.add(db_book_connect.getBook(isbn, this));
                basket_tabl.setItems(bbs);
            }
            else{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("book already in basket");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("check connection");
            alert.showAndWait();
        }
    }

    @FXML
    TableView<book>basket_tabl;
    @FXML
    TableColumn<book,String>bas_name;
    @FXML
    TableColumn<book,String>bas_auth;
    @FXML
    TableColumn<book,Button>bas_rem;

    public void basket_refresh(){
        basket_tabl.setItems(bbs);
    }
    @FXML
    public Button sercher;
    @FXML
    public Button addbas;
    @FXML
    TextField sebox;
    @FXML
    TableView<book>bees;
    @FXML
    TableColumn<book,String>bis;
    @FXML
    TableColumn<book,String>bnam;
    @FXML
    TableColumn<book,Button>bbv;
    @FXML
    TableColumn<book, Button>bba;
    @FXML
    TextField is;
    @FXML
    TextField na;
    @FXML
    TextField ca;
    @FXML
    TextField au;
    @FXML
    ImageView vco;
    public void seda(){
        bis.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bnam.setCellValueFactory(new PropertyValueFactory<>("nam"));
        bbv.setCellValueFactory(new PropertyValueFactory<>("bbv"));
        bba.setCellValueFactory(new PropertyValueFactory<>("bba"));

        try {
            search_res=db_book_connect.search(sebox.getText(),is,na,ca,au,vco,this);
            bees.setItems(search_res);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        //-----------------------------------------------------------
    @FXML
    AnchorPane stagepane;
    public void logout(){
        try {
            //login is in another package
            Parent root=FXMLLoader.load(getClass().getResource("/login/LoginGui.fxml"));
            Scene scene=new Scene(root);
            Stage stage=(Stage)stagepane.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("files corrupted");
            alert.showAndWait();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deactiv_qr.setDisable(true);
        addbas.setDisable(true);
        sercher.setDisable(true);
        bas_name.setCellValueFactory(new PropertyValueFactory<>("nam"));
        bas_auth.setCellValueFactory(new PropertyValueFactory<>("auth"));
        bas_rem.setCellValueFactory(new PropertyValueFactory<>("but_rem"));
    }
}
