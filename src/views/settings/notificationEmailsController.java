package views.settings;

import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.settings.NotificationEmails;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class notificationEmailsController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Label c1L;
    @FXML private TextField c2T;
    @FXML private TextField c3T;
    //@FXML private TextField c4T;
    //@FXML private TextField sort;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    
    @FXML private TableView<NotificationEmails> cTable;
    @FXML private TableColumn<NotificationEmails, String> C1;
    @FXML private TableColumn<NotificationEmails, String> C2;
    @FXML private TableColumn<NotificationEmails, String> C3;
    
    
    private static Connection conn = null;
    private static Statement st = null;
    private static ResultSet rs = null;

    //private static final cssChanger cssC = new cssChanger();
    public static dbStringPath dbsp;
    public static String csspath;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(csspath);
        setKeyCodes();
        C1.setCellValueFactory(new PropertyValueFactory<>("C1"));
        C2.setCellValueFactory(new PropertyValueFactory<>("C2"));
        C3.setCellValueFactory(new PropertyValueFactory<>("C3"));
        hbox.setVisible(false);
        updateButton.setVisible(false);
        cTable.setFixedCellSize(30.0);
        cTable.addEventFilter(ScrollEvent.ANY, Event::consume);
        Platform.runLater(()->getTableItems());
    }    
    
private void getTableItems() {
        cTable.getItems().clear();
        try {
            cTable.getItems().addAll(getdataB());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);
    }
   

 public void tableItemSelected() {
    NotificationEmails itemSelected  = cTable.getSelectionModel().getSelectedItem();
    c1L.setText(itemSelected.getC1());
    c2T.setText(itemSelected.getC2());
    c3T.setText(itemSelected.getC3());
    tableID = itemSelected.getC7();
    updateButton.setVisible(true);
    hbox.setVisible(true);  
    
 }


public ObservableList<NotificationEmails> getdataB() throws SQLException {
    ObservableList<NotificationEmails> data = FXCollections.observableArrayList();

    try {
        conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
        st = conn.createStatement();
        rs = st.executeQuery("SELECT WhoToTable.* FROM WhoToTable;");
        while (rs.next()) {

            NotificationEmails newdata = new NotificationEmails(rs.getString("sendingwhat"), rs.getString("whoTo"), rs.getString("details"), rs.getInt("ID"));
            data.add(newdata);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } finally {
        if (rs != null){
        rs.close();}
        if (st != null){
        st.close();}
        if (conn != null){
        conn.close();}
    }

    return data;
}   



private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
     });   
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
                    case DOWN: break;
                    case UP: break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: break;
                default:
                    break;
                }
    } 
    
    private void resetFields() {
        c2T.setText("");
        c3T.setText("");
        //sort.setText("");
        hbox.setVisible(false);
        updateButton.setVisible(false);
    }
    
    public void updateData() {
    NotificationEmails whoto = null;
    NotificationEmails itemSelected  = cTable.getSelectionModel().getSelectedItem();
    if (!"".equals(c3T.getText())) {
        System.out.println(c2T.getText() + " " +  c3T.getText() + " " + itemSelected.getC7());
        whoto = new NotificationEmails(c2T.getText(), c3T.getText(), "", itemSelected.getC7());        
    } else {
        System.out.println(c2T.getText() + " " +  c3T.getText() + " " + itemSelected.getC7());
        whoto = new NotificationEmails(c2T.getText(), "", "", itemSelected.getC7());        
    }
    NotificationEmails.UpdateWhoToTable(whoto);
    resetFields();
    getTableItems();
    
    }
    
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        System.out.println("we are in exitbuttonpushed");
        if (updateButton.isVisible()) {
            resetFields();
            cancelButton.setText(" Exit ");
        }else {
            stageV.close();
            //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX1().titleBar);
        }
    }



}
