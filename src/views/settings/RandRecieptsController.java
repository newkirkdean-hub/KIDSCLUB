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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.settings.RandReceipts;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class RandRecieptsController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Label c1L;
    @FXML private TextField c2T;
    @FXML private TextField c3T;
    @FXML private TextArea noteT;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    
    @FXML private TableView<RandReceipts> cTable;
    @FXML private TableColumn<RandReceipts, String> C1;
    @FXML private TableColumn<RandReceipts, String> C2;
    @FXML private TableColumn<RandReceipts, String> C3;
    @FXML private TableColumn<RandReceipts, String> C4;
    @FXML private TableColumn<RandReceipts, Integer> C5;
    
    
    
    employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
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
        C5.setCellValueFactory(new PropertyValueFactory<>("C5"));
        getTableItems();
        hbox.setVisible(false);
        updateButton.setVisible(false);
        cTable.setFixedCellSize(54.0);
        cTable.addEventFilter(ScrollEvent.ANY, Event::consume);


    }    
    
private void getTableItems() {
    cTable.getItems().clear();
        try {
            cTable.getItems().addAll(getdata());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);
        C2.setCellFactory(TextFieldTableCell.forTableColumn());
    
}
   

 public void tableItemSelected() {
    RandReceipts itemSelected  = cTable.getSelectionModel().getSelectedItem();
    c1L.setText(itemSelected.getC1());
    c2T.setText(itemSelected.getC2());
    c3T.setText(itemSelected.getC3());
    noteT.setText(itemSelected.getC4());
    tableID = itemSelected.getC5();
    updateButton.setVisible(true);
    hbox.setVisible(true);
    
 }


public ObservableList<RandReceipts> getdata() throws SQLException {
    ObservableList<RandReceipts> data = FXCollections.observableArrayList();

    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
  try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT RandReciepts.Item, RandReciepts.Needed, RandReciepts.Current, RandReciepts.Note, RandReciepts.ID FROM RandReciepts;");
            while (rs.next()) {
          //System.out.println("here");
                RandReceipts newdata = new RandReceipts(rs.getString("Item"), rs.getString("Needed"), rs.getString("Current"), rs.getString("Note"), rs.getInt("ID"));
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





private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.DOWN) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: JOptionPane.showMessageDialog(null, "F1"); break;
                    case F2: JOptionPane.showMessageDialog(null, "F2"); break;
                    case F3: JOptionPane.showMessageDialog(null, "F3"); break;
                    case F4: break;
                    case F5: JOptionPane.showMessageDialog(null, "F5"); break;
                    case F6: JOptionPane.showMessageDialog(null, "F6"); break;
                    case F7: JOptionPane.showMessageDialog(null, "F7"); break;
                    case F8: JOptionPane.showMessageDialog(null, "F8"); break;
                    case F9: break;
                    case F10: JOptionPane.showMessageDialog(null, "F10"); break;
                    case F11: JOptionPane.showMessageDialog(null, "F11"); break;
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
        noteT.setText("");
        hbox.setVisible(false);
        updateButton.setVisible(false);
    }
    
    public void updateData() {
    RandReceipts itemSelected  = cTable.getSelectionModel().getSelectedItem();
    itemSelected.UpdateRandReceipts(c2T.getText(), c3T.getText(), noteT.getText(), itemSelected.getC5());
    resetFields();
    getTableItems();
    
    }
    
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        if (updateButton.isVisible()) {
            resetFields();
            cancelButton.setText(" Exit ");
        }else {
            stageV.close();
        }
    }



}
