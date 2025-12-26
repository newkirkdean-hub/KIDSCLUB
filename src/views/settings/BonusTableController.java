package views.settings;

import Css.cssChanger;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.settings.Bonus;
import models.settings.RandReceipts;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class BonusTableController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button colorsButton;
    @FXML private Button updateButton;
    @FXML private TextField c1L;
    @FXML private TextField c2T;
    @FXML private TextField c3T;
    @FXML private TextField c4T;
    @FXML private TextField c5T;
    @FXML private TextArea noteT;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    @FXML private Pane errorPane;
    @FXML private Label errorLabel;
    
    @FXML private TableView<Bonus> cTable;
    @FXML private TableColumn<Bonus, String> C1;
    @FXML private TableColumn<Bonus, String> C2;
    @FXML private TableColumn<Bonus, String> C3;
    @FXML private TableColumn<Bonus, String> C4;
    //@FXML private TableColumn<Bonus, String> C5;
    //@FXML private TableColumn<Bonus, String> C6;
    //@FXML private TableColumn<Bonus, Integer> C7;
    
    
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    settingsFXML sg = new settingsFXML();    
    dbStringPath dbsp = new dbStringPath();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        
        setKeyCodes();
        C1.setCellValueFactory(new PropertyValueFactory<>("C1"));
        C2.setCellValueFactory(new PropertyValueFactory<>("C2"));
        C3.setCellValueFactory(new PropertyValueFactory<>("C3"));
        C4.setCellValueFactory(new PropertyValueFactory<>("C4"));
        //C5.setCellValueFactory(new PropertyValueFactory<>("C5"));
        //C6.setCellValueFactory(new PropertyValueFactory<>("C6"));
        //C7.setCellValueFactory(new PropertyValueFactory<>("C7"));
        getTableItems();
        hbox.setVisible(false);
        errorPane.setVisible(false);
        updateButton.setVisible(false);
        cTable.setFixedCellSize(30.0);
        cTable.addEventFilter(ScrollEvent.ANY, Event::consume);


    }    
    
private void getTableItems() {
    cTable.getItems().clear();
        try {
            cTable.getItems().addAll(getdataB());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);
        //C2.setCellFactory(TextFieldTableCell.forTableColumn());
    
}
   

 public void tableItemSelected() {
    Bonus itemSelected  = cTable.getSelectionModel().getSelectedItem();
    c1L.setText(itemSelected.getC1());
    c2T.setText(itemSelected.getC2());
    //c3T.setText(itemSelected.getC3());
    c3T.setText(itemSelected.getC3());
    c4T.setText(itemSelected.getC4());
    //noteT.setText(itemSelected.getC6());
    tableID = itemSelected.getC7();
    updateButton.setVisible(true);
    hbox.setVisible(true);  
    
    
 }


public ObservableList<Bonus> getdataB() throws SQLException {
    ObservableList<Bonus> data = FXCollections.observableArrayList();

    
  try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Bonus.AtLeast, Bonus.LessThan, Bonus.Tickets, Bonus.Percent, Bonus.Tuesday, Bonus.[Bonus Special], Bonus.ID FROM Bonus ORDER BY Bonus.ID;");
            while (rs.next()) {

                Bonus newdata = new Bonus(rs.getString("AtLeast"), rs.getString("LessThan"), rs.getString("Percent"), rs.getString("Tuesday"), rs.getInt("ID"));
                data.add(newdata);
            }
    }
    catch (SQLException e) {
        System.err.println(e.getMessage());
    }
    finally {
    //if (rs != null);
    //    rs.close();
    if (st != null);
        st.close();
    if (conn != null);
        conn.close();
}

    return data;
}   


public ObservableList<RandReceipts> getdata() throws SQLException {
    ObservableList<RandReceipts> data = FXCollections.observableArrayList();

    
  try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "Member.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Bonus.AtLeast, Bonus.LessThan, Bonus.Tickets, Bonus.Percent, Bonus.ID FROM Bonus ORDER BY Bonus.ID;");
            while (rs.next()) {
          //System.out.println("here");
                RandReceipts newdata = new RandReceipts(rs.getString("AtLeast"), rs.getString("LessThan"),rs.getString("Tickets"), rs.getString("Percent"), rs.getInt("ID"));
                data.add(newdata);
            }
    }
    catch (Exception e) {
        System.err.println(e.getMessage());
    }
    finally {
        if (conn != null);
        conn.close();
    if (st != null);
        st.close();
    if (rs != null);
        rs.close();
}

    return data;
}   
    
    private void resetFields() {
        c2T.setText("");
        c3T.setText("");
        //noteT.setText("");
        hbox.setVisible(false);
        updateButton.setVisible(false);
    }
    
    public void updateData() {
        if (c1L.getText().isEmpty() || c2T.getText().isEmpty() || c3T.getText().isEmpty() || c4T.getText().isEmpty()) {
            //new messageBox().showAlert(Alert.AlertType.ERROR, null, "Stop", "You have left something empty or blank. \nThere must be a Zero in all empty spaces except for the Special");
            new messageBox().showError("You have left something empty or blank. \nThere must be a Zero in all empty spaces except for the Special", errorLabel, errorPane);
        } else {
            Bonus itemSelected = cTable.getSelectionModel().getSelectedItem();
            Bonus nBonus = new Bonus(c1L.getText(), c2T.getText(), c3T.getText(), c4T.getText(), itemSelected.getC7());
//    nBonus.UpdateBonusTable(nBonus.getC1(), nBonus.getC1(), nBonus.getC3(), nBonus.getC4(), nBonus.getC5(), nBonus.getC6(), tableID);
            nBonus.UpdateBonusTable();
            //itemSelected.UpdateBonusTable(c1L.getText(), c2T.getText(), c3T.getText(), c4T.getText(), c5T.getText(), noteT.getText(), itemSelected.getC7());
            resetFields();
            getTableItems();
            new messageBox().showErrorClear(errorLabel, errorPane);
        }
    }
    
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F3) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F6) {keyListener(ke);}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke);}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1:  break;
                    case F2:  break;
                    case F3: break;
                    case F4: ; break;
                    case F5: ; break;
                    case F6:  break;
                    case F7:  break;
                    case F8:  break;
                    case F9: ; break;
                    case F10: ; break;
                    case F11: ; break;
                    case F12: ; break;
                    case ENTER: break;
                    case ESCAPE: cancelButton.fire();break;
                default:
                    break;
                }
}
     
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        if (updateButton.isVisible()) {
            resetFields();
            cancelButton.setText(" Exit ");
        }else {
            stageV.close();
            //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX1().titleBar);
        }
    }



}
