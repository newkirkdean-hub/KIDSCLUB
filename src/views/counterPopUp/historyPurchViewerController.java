
package views.counterPopUp;

import JavaMail.Mail_JavaFX1;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import models.club.Member;
import models.toys.ToysDB;
import models.toys.ToysDetail;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class historyPurchViewerController implements Initializable {
    @FXML private TableView<ToysDetail> memPurchTable;
    @FXML private TableColumn<ToysDetail, LocalDate> memPurchDateColumn;
    @FXML private TableColumn<ToysDetail, String> memPurchEmpNumberColumn;
    @FXML private TableColumn<ToysDetail, String> memPurchitemNameColumn;
    @FXML private TableColumn<ToysDetail, String> memPurchTimeColumn;
    @FXML private TableColumn<ToysDetail, Integer> memPurchQTYColumn;
    @FXML private TableColumn<ToysDetail, Integer> memPurchTicketsColumn;
    @FXML private TableColumn<ToysDetail, String> memPurchIDColumn;
    @FXML private TextField searchField;
    @FXML private Button cancelButton;
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
       memPurchDateColumn.setCellValueFactory(new PropertyValueFactory<>("purchDate"));
       memPurchDateColumn.setCellFactory(column -> {
            return new TableCell<ToysDetail, LocalDate>() {
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
        memPurchEmpNumberColumn.setCellValueFactory(new PropertyValueFactory<>("empID"));
        memPurchitemNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        memPurchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("ITranNumb"));
        memPurchQTYColumn.setCellValueFactory(new PropertyValueFactory<>("QTY"));
        memPurchTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("Tickets"));
        //memPurchIDColumn.setCellValueFactory(new PropertyValueFactory<>("TranID"));
        
        memPurchTable.getItems().clear();
        System.out.println(memID);
        memPurchTable.getItems().addAll(new ToysDB().getPurchHistory(memID));
        searchField.setText(cName);
        //searchField.requestFocus();
        
    }    
    
   

    public void selectButtonPushed(ActionEvent event) throws IOException {
       sc.changePopUp1(event, "");
    }
    
   
    
    /*
    public ObservableList<Member> getGames() throws SQLException {
    ObservableList<Member> games = FXCollections.observableArrayList();

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "InactiveMembers.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Inactive.[Customer Card Number], Inactive.[First Name], Inactive.[Last Name], Inactive.Address, Inactive.[Customer ID] FROM Inactive ORDER BY Inactive.[Customer ID];");
           //String ccn, String cid, String namef, String namel, LocalDate bdate, String areacode, String phone1, String phone2
            while (rs.next()) {
                    //public ToysDetail(String Name, String iNumber, String itemID, String unitType, String iTranNumb, String invoiceNumb, String empID, int Tickets, int QTY, Double PPU, Double ATP, LocalDate purchDate) {
                Member newGame = new Member( rs.getString("Customer Card Number"), rs.getString("First Name"), rs.getString("Last Name"), rs.getString("Address"), rs.getString("Customer ID"));
                games.add(newGame);
            }
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
    }
    finally {
        if (conn != null){
        conn.close();}
    if (st != null){
        st.close();}
    if (rs != null){
        rs.close();}
}

    return games;
}   
    */
    
    public void getTableRow() {
    
    ToysDetail tableRowValue = memPurchTable.getSelectionModel().getSelectedItem();
    if (!efx.isemployeeHistoric(tableRowValue.getEmpID())) {
        //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "No Number", "No Employee number found");
    } else {
        mbox.showAlert(Alert.AlertType.ERROR, stageV, "EmpNumber", efx.getNameF() + " " + efx.getNameL() + " \n Time: " + tableRowValue.getITranNumb());
    }
    efx.Flush();
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
    
}
