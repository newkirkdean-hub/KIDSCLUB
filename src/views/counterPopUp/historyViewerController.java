
package views.counterPopUp;

import JavaMail.Mail_JavaFX1;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messageBox.messageBox;
import models.club.DB;
import models.club.Memtick;
import models.toys.ToysDB;
import models.toys.ToysDetail;
import pWordFX.employeeFX;
//import pWordFX.employeeFX11;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class historyViewerController implements Initializable {
    @FXML private TableView<Memtick> memtickDetailTable;
    @FXML private TableColumn<Memtick, LocalDate> memtickDateColumn;
    @FXML private TableColumn<Memtick, String> memtickEmpNumberColumn;
    @FXML private TableColumn<Memtick, String> memtickAddedColumn;
    @FXML private TableColumn<Memtick, String> memtickBonusColumn;
    @FXML private TableColumn<Memtick, String> memtickWithColumn;
    @FXML private TableColumn<Memtick, String> LocationColumn;
    @FXML private TableColumn<Memtick, String> memtickIDColumn;
    @FXML private TextField searchField;
    //@FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Pane root;


    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    public static String csspath;
    public static messageBox mbox;
    public static employeeFX efx;
    public static DB db;
    public static Mail_JavaFX1 jmail;
    public static String memID, cName;

    private static Stage stageV;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(csspath);
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        memtickDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        memtickDateColumn.setCellFactory(column -> {
            return new TableCell<Memtick, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
        memtickEmpNumberColumn.setCellValueFactory(new PropertyValueFactory<>("MaskedEmpNumber"));
        memtickAddedColumn.setCellValueFactory(new PropertyValueFactory<>("Added"));
        memtickBonusColumn.setCellValueFactory(new PropertyValueFactory<>("Bonus"));
        memtickWithColumn.setCellValueFactory(new PropertyValueFactory<>("Subtracted"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        memtickIDColumn.setCellValueFactory(new PropertyValueFactory<>("TranID"));
        
        memtickDetailTable.getItems().clear();
        try {
            memtickDetailTable.getItems().addAll(db.getHistory(memID));
        } catch (SQLException ex) {
            System.out.println("///////////////////////// " + ex);
        }
        searchField.setText(cName);
        //searchField.requestFocus();
        
    }    
    
   

    public void selectButtonPushed(ActionEvent event) throws IOException {
       sc.changePopUp1(event, "");
    }
    
    
 
    
    public void getTableRow3() {
    
    Memtick tableRowValue = memtickDetailTable.getSelectionModel().getSelectedItem();
    if (!efx.isemployeeHistoric(tableRowValue.getEmpNumber())) {
        //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "No Number", "No Employee number found");
    } else {
        String t = db.GetAccountChangeMessageForHistory(tableRowValue.getTranID());
        if (t!=null) {
            mbox.showAlert(Alert.AlertType.ERROR, stageV, "EmpNumber", new employeeFX().getNameF() + " " + new employeeFX().getNameL() + " \n Time: " + tableRowValue.getTime() + "\n\n" + t);
        } else {
            mbox.showAlert(Alert.AlertType.ERROR, stageV, "EmpNumber", new employeeFX().getNameF() + " " + new employeeFX().getNameL() + " \n Time: " + tableRowValue.getTime());
        }
    }
    efx.Flush();
    }
    
        public void getTableRow() {
    
    Memtick tableRowValue = memtickDetailTable.getSelectionModel().getSelectedItem();
    if (!efx.isemployeeHistoric(tableRowValue.getEmpNumber())) {
        Platform.runLater(()->jmail.sendEmailTo("No Number Found", "This Number Could Not be Found in THe Employees Data " + tableRowValue.getEmpNumber(), "errors"));
    } else {
        //get items this purchase
        String t = db.GetAccountChangeMessageForHistory(tableRowValue.getTranID());
        ArrayList<ToysDetail> toylist = new ToysDB().getThisPurchHistory2(tableRowValue.getMemID(), tableRowValue.getTime());
        Iterator<ToysDetail> toylistIterator = toylist.iterator();
        String newToylist = "";
        while (toylistIterator.hasNext()) {
            ToysDetail tvShow = toylistIterator.next();
            newToylist += "(" + tvShow.getQTY() + ") " + tvShow.getName() +  " " + tvShow.getTickets() + " Tickets\n";
        }
        mbox.showAlertMemDetail(Alert.AlertType.ERROR, stageV, "EmpNumber", new employeeFX().getNameF() + " " + new employeeFX().getNameL() + " \n Time: " + tableRowValue.getTime() + " \n\n" + newToylist + " \n\n " + t);
    }
    }

    
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        sc.changePopUp1(event, "");
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: break;
                    case F7: break;
                    case F8: break;
                    case F9: break;
                    case F10: break;
                    case F11: break;
                    case F12: break;
                    case ESCAPE: selectButton.fire(); break;
                    case ENTER: selectButton.fire(); break;
                default:
                    break;
                }
    }
    
    
    
    
    
    
       /*
    public ObservableList<Member> getFullTransHistory() throws SQLException {
    ObservableList<Member> memberList = FXCollections.observableArrayList();
    try {
            fullTransConn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "InactiveMembers.accdb;immediatelyReleaseResources=true");
            fullTransStatement = fullTransConn.createStatement();
            fullTransRestultSet = fullTransStatement.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[First Name], Inactive.[Last Name], Inactive.Address, Inactive.[Customer ID] FROM Inactive ORDER BY Inactive.[Customer ID];");
           //String ccn, String cid, String namef, String namel, LocalDate bdate, String areacode, String phone1, String phone2
            while (fullTransRestultSet.next()) {
                Member member = new Member( fullTransRestultSet.getString("Customer Card Number"), fullTransRestultSet.getString("First Name"), fullTransRestultSet.getString("Last Name"), fullTransRestultSet.getString("Address"), fullTransRestultSet.getString("Customer ID"));
                memberList.add(member);
            }
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
    }
    finally {
    if (fullTransStatement != null){
        fullTransStatement.close();}
    if (fullTransRestultSet != null){
        fullTransRestultSet.close();
        fullTransRestultSet = null;}
    if (fullTransConn != null){
        fullTransConn.close();
        fullTransConn = null;}
}

    return memberList;
}   
    */
}
