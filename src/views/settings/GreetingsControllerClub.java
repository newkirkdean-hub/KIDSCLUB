package views.settings;

import Css.cssChanger;
import bdayResosEmailUtil.GetEmailsBdayResosUtil;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.club.CheckBalanceDB;
import models.club.DB;
import models.settings.GreetingsClub;
import models.club.Member;
import models.club.Memtick;
import models.club.rCeipts;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class GreetingsControllerClub implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Button BdayEmailButton;
    @FXML private Button ClubEmailButton;
    @FXML private ColorPicker ColorPicker;
    @FXML private Label c1L;
    @FXML private TextField c2T;
    @FXML private TextField c3T;
    @FXML private TextField c4T;
    @FXML private TextField sort;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    
    @FXML private TableView<GreetingsClub> cTable;
    @FXML private TableColumn<GreetingsClub, String> C1;
    @FXML private TableColumn<GreetingsClub, String> C2;
    @FXML private TableColumn<GreetingsClub, String> C3;
    @FXML private TableColumn<GreetingsClub, String> C4;
    @FXML private TableColumn<GreetingsClub, Integer> C6;
    
    
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    public static ArrayList<rCeipts> Receipts;
    public static ArrayList<rCeipts> ReceiptsClub;
    public static String OwnerEmail;  
    Stage stageV;
    Bounds boundsInScene, boundsInScene2, boundsInScene3;
    private static String emailAddressSave = "Enter Email";



    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene2 = root.localToScene(root.getBoundsInLocal());
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        setKeyCodes();
        C1.setCellValueFactory(new PropertyValueFactory<>("C1"));
        C2.setCellValueFactory(new PropertyValueFactory<>("C2"));
        C3.setCellValueFactory(new PropertyValueFactory<>("C3"));
        C4.setCellValueFactory(new PropertyValueFactory<>("C4"));
        C6.setCellValueFactory(new PropertyValueFactory<>("C8"));
        getTableItems();
        hbox.setVisible(false);
        updateButton.setVisible(false);
        ColorPicker.setVisible(false);
        //BdayEmailButton.setVisible(false);
        //ClubEmailButton.setVisible(false);
        cTable.setFixedCellSize(30.0);
        //cTable.addEventFilter(ScrollEvent.ANY, Event::consume);
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
   

private void setEmailAddressSave(String emailAddress) {
    this.emailAddressSave = emailAddress;
}

private String getEmailAddressSave() {
    return emailAddressSave;
}

    public void BdayEmailButtonGo() {
        try {
            //need to enter account number here
            sc.getpassWord(stageV, "/pWordFX/NewItemNumber.fxml", emailAddressSave, emailAddressSave, boundsInScene2.getMinX() + 550.0, boundsInScene2.getMinY() + 290.00);
            setEmailAddressSave(sc.getGameNumber());
            if (sc.getGameNumber().equals("Number")){
                System.out.println(sc.getGameNumber());
                return;
            }
            Receipts = new DB().getReceipts();
            new GetEmailsBdayResosUtil().ViewCopy(Receipts, sc.getGameNumber());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }


    public void ClubEmailButtonGo() {
        try {
            //sc.getpassWord(stageV, "/pWordFX/scanCardAgain.fxml", "Number", "Scan Card Again:", boundsInScene2.getMinX() + 250.0, boundsInScene2.getMinY() + 90.00);
            sc.getpassWord(stageV, "/pWordFX/NewItemNumber.fxml", emailAddressSave, emailAddressSave, boundsInScene2.getMinX() + 550.0, boundsInScene2.getMinY() + 290.00);
            if (sc.getGameNumber().equals("Number")){
                System.out.println(sc.getGameNumber());
                return;
            }
            Member m = new DB().getMember("100001");

            if (!m.getNameF().equals("inValid")) {
                Memtick mt = new Memtick(m.getCID(), "Dean", new GetDay().getCurrentTimeStamp(), LocalDate.now(), 400.0, 40.0, 0.0, "", 0);
                ReceiptsClub = new DB().getReceipts();
                String EmailAdd1 = getReceipts(ReceiptsClub, "EmailAdd1");
                //String isEMail = new DB().getEmail(m.getCID());
                new CheckBalanceDB().sendMemberEmail(m, mt, 4440.0, 4000.0, m.getNameF(), EmailAdd1, "D", null, null, sc.getGameNumber(), ReceiptsClub);
            } else {
                new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Error", "That Number does not Exist");
            }
        } catch (IOException | SQLException ex) {
            System.out.println(ex);
        }
 
    }


        private String getReceipts(ArrayList<rCeipts> Receipts, String n) {
        String StringItem = null;
        for (int y = 0; y < Receipts.size(); y++) {
            if (n.trim().equals(Receipts.get(y).getrItem())) {
                //System.out.println(rCeipts.get(y).getrItem() + " " + rCeipts.get(y).getrString() + " " + rCeipts.get(y).getrNumber());
                StringItem = Receipts.get(y).getrString();
            }
        }
        return StringItem;
    }
    
    
    
    private void getStageV() {
        stageV = (Stage) updateButton.getScene().getWindow();
    }


 public void tableItemSelected() {
    GreetingsClub itemSelected  = cTable.getSelectionModel().getSelectedItem();
    c1L.setText(itemSelected.getC1());
    c2T.setText(itemSelected.getC2());
    c3T.setText(itemSelected.getC3());
    c4T.setText(itemSelected.getC4());
    sort.setText(String.valueOf(itemSelected.getC8()));
    tableID = itemSelected.getC6();
    updateButton.setVisible(true);
    ColorPicker.setVisible(true);
    BdayEmailButton.setVisible(true);
    ClubEmailButton.setVisible(true);
    
    hbox.setVisible(true);  
    
 }


public ObservableList<GreetingsClub> getdataB() throws SQLException {
    ObservableList<GreetingsClub> data = FXCollections.observableArrayList();

    try {
        conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
        st = conn.createStatement();
        rs = st.executeQuery("SELECT Greetings.Item, Greetings.String, Greetings.Number, Greetings.Formatted, Greetings.Date, Greetings.ID, Greetings.ID2 FROM Greetings ORDER BY Greetings.ID2;");
        while (rs.next()) {

            GreetingsClub newdata = new GreetingsClub(rs.getString("Item"), rs.getString("String"), rs.getString("Number"), rs.getString("Formatted"), rs.getString("Date"), rs.getInt("ID"), rs.getInt("ID2"));
            data.add(newdata);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    } finally {
        if (rs != null);
        rs.close();
        if (st != null);
        st.close();
        if (conn != null);
        conn.close();
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
        sort.setText("");
        hbox.setVisible(false);
        updateButton.setVisible(false);
        ColorPicker.setVisible(false);
        //BdayEmailButton.setVisible(false);
        //ClubEmailButton.setVisible(false);
    }
    
    public void updateData() {
    GreetingsClub itemSelected  = cTable.getSelectionModel().getSelectedItem();
    GreetingsClub greeting = new GreetingsClub(c2T.getText(), c3T.getText(), c4T.getText(), null, itemSelected.getC6(), Integer.parseInt(sort.getText()));
    greeting.UpdateGreetingsTable();
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
            //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX1().titleBar);
        }
    }



}
