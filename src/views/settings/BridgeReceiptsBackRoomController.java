package views.settings;

import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messageBox.messageBox;
import models.club.rCeipts;
import models.settings.BridgeReceipts;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class BridgeReceiptsBackRoomController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Button vReceiptsButton;
    @FXML private Button newButton;
    @FXML private Button deleteButton;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    @FXML private TextField lField;
    @FXML private Button viewScreen;
    //@FXML private TextField tTickets;
    //@FXML private TextField tMultiplyer;
    @FXML private Stage stageV;
    @FXML private TextArea bridgeRciept;
    @FXML private TextArea cafeRciept;
    @FXML private DatePicker dStart;
    @FXML private DatePicker dEnd;
    //@FXML private TextField tRepeat;
    @FXML private TextField tQty;
    @FXML private TextField trCeipt;   
    @FXML private TableView<BridgeReceipts> cTable;
    @FXML private TableColumn<BridgeReceipts, String> C1;
    @FXML private TableColumn<BridgeReceipts, String> C2;
    //@FXML private TableColumn<BridgeReceipts, String> C3;
    @FXML private TableColumn<BridgeReceipts, String> C4;
    @FXML private TableColumn<BridgeReceipts, String> C5;
    @FXML private TableColumn<BridgeReceipts, LocalDate> C6;
    @FXML private TableColumn<BridgeReceipts, LocalDate> C7;
    @FXML private TableColumn<BridgeReceipts, String> C8;
    @FXML private TableColumn<BridgeReceipts, Integer> C9;
    @FXML private TableColumn<BridgeReceipts, Integer> C10;
    @FXML private TableColumn<BridgeReceipts, String> C11;
    @FXML private TableColumn<BridgeReceipts, String> C12;
    
    
    private BridgeReceipts itemSelected;
    public static employeeFX eFX;
    public static SceneChanger_Main sc;
    public static String cssC;
    public static dbStringPath dbsp;
    private static PrintWriter PW;
    public static FXMLLoader fxLoader = new FXMLLoader();
    private static BridgeReceipts newdata;
    public static ArrayList<rCeipts> rCeipts;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        System.out.println("1");
        root.getStylesheets().add(cssC);
        setKeyCodes();
        //C1.setCellValueFactory(new PropertyValueFactory<>("C1"));
        C1.setCellValueFactory(new PropertyValueFactory<>("Title"));
        //C2.setCellValueFactory(new PropertyValueFactory<>("Initials"));
        //C3.setCellValueFactory(new PropertyValueFactory<>("Tickets"));
        //C4.setCellValueFactory(new PropertyValueFactory<>("Multiply"));
        //C5.setCellValueFactory(new PropertyValueFactory<>("Other"));
        C6.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        C7.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        //C8.setCellValueFactory(new PropertyValueFactory<>("Repeat"));
        C9.setCellValueFactory(new PropertyValueFactory<>("Receipt"));
        C10.setCellValueFactory(new PropertyValueFactory<>("Sort"));
        C11.setCellValueFactory(new PropertyValueFactory<>("Bridge"));
        C12.setCellValueFactory(new PropertyValueFactory<>("Cafe"));
        setToUpper();
        getTableItems();
        hbox.setVisible(false);
        updateButton.setVisible(false);
        vReceiptsButton.setVisible(false);
        deleteButton.setVisible(false);
        cTable.setFixedCellSize(50.0);
        cTable.requestFocus();
        Platform.runLater(() -> getStageV());
        //cTable.addEventFilter(ScrollEvent.ANY, Event::consume);

    }

    private void getTableItems() {
        cTable.getItems().clear();
        try {
            cTable.getItems().addAll(getdata());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);
        //C2.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private BridgeReceipts getItemsSelected() {
        BridgeReceipts items = cTable.getSelectionModel().getSelectedItem();
        return items;
    }
    
    public void tableItemSelected() {
        itemSelected = cTable.getSelectionModel().getSelectedItem();
        lField.setText(itemSelected.getTitle());
        dStart.setValue(itemSelected.getStartDate());
        dEnd.setValue(itemSelected.getEndDate());
        //newButton.setVisible(false);
        tableID = itemSelected.getId();
        tQty.setText(String.valueOf(itemSelected.getSort()));
        trCeipt.setText(String.valueOf(itemSelected.getReceipt()));
        bridgeRciept.setText(itemSelected.getCafe());
        cafeRciept.setText(itemSelected.getBridge());
        updateButton.setVisible(true);
        deleteButton.setVisible(true);
        vReceiptsButton.setVisible(true);
        hbox.setVisible(true);

    }

    public ObservableList<BridgeReceipts> getdata() throws SQLException {
        ObservableList<BridgeReceipts> data = FXCollections.observableArrayList();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        newdata = null;
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT BridgeReceipts.*\n"
                    + "FROM BridgeReceipts;");

            while (rs.next()) {
                newdata = new BridgeReceipts(rs.getString("Name"), rs.getString("Initials"), rs.getInt("Tickets"), rs.getDouble("Multiply"), rs.getString("Other"), rs.getDate("StartDate").toLocalDate(), rs.getDate("EndDate").toLocalDate(), rs.getInt("Repeat"), rs.getDouble("QTY"), rs.getInt("ID"), rs.getInt("Sort"), rs.getInt("Receipt"), rs.getString("CafeR"), rs.getString("BridgeR"));
                data.add(newdata);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return data;
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F6) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F7) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F8) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F9) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
                ke.consume();
            }
        });
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                break;
            case F2:
                break;
            case F3:
                break;
            case F4:
                break;
            case F5:
                break;
            case F6:
                break;
            case F7:
                break;
            case F8:
                break;
            case F9:
                break;
            case F10:
                break;
            case F11:
                break;
            case DOWN:
                break;
            case UP:
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case ENTER:
                break;
            default:
                break;
        }
    }
    
    private void setToUpper() {
        //showErrorClear();

        lField.textProperty().addListener((ov, oldValue, newValue) -> {
            lField.setText(newValue.toUpperCase());
        });
        
    }
    
    private void getStageV() {
        stageV = (Stage) updateButton.getScene().getWindow();
        //System.out.println(new settingsFXML().getCounterSettings("stage"));
        //if (new settingsFXML().getCounterSettings("stage").equals("1")) {
        //    counterScene = "1";
        //} else {
        //    counterScene = "2";
       // }
    }
    
    
    
    
     public void GOBridgeReceipts() {
        //EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
        BridgeReceiptsController wController = (BridgeReceiptsController) fxLoader.getController();
        wController.cssC = cssC;
        wController.superLevel = 1;
        stageV.setTitle("Bridge");
        wController.WHSTAGE = stageV.getTitle();
        wController.rCeipts = rCeipts;
        wController.eFX = eFX;
        System.out.println("From Bridge: " + stageV.getTitle());
        //wController.MGR = eFX;        
        getStageV();
        try {
            sc.getpassWord(stageV, "/views/settings/BridgeReceipts.fxml", null, null, 540.0, 75.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene2("Announcements", stageV, EFX.getNameF(), depPane, boundsInScene.getMinX(), boundsInScene.getMinY()-20.0);
        return;

    }
    

          public void GOCafeReceipts() {
        //EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
        BridgeReceiptsController wController = (BridgeReceiptsController) fxLoader.getController();
        wController.cssC = cssC;
        wController.superLevel = 1;
        stageV.setTitle("Cafe");
        wController.WHSTAGE = stageV.getTitle();
        wController.rCeipts = rCeipts;
        wController.eFX = eFX;
        System.out.println("From cafe: " + stageV.getTitle());
        //wController.MGR = eFX;        
        getStageV();
        try {
            sc.getpassWord(stageV, "/views/settings/BridgeReceipts.fxml", null, null, 540.0, 75.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene2("Announcements", stageV, EFX.getNameF(), depPane, boundsInScene.getMinX(), boundsInScene.getMinY()-20.0);
        return;

    }

     
     
     

    private void resetFields() {
        lField.setText("");
        //tinitials.setText("");
        //tTickets.setText("");
        //noteT.setText("");
        hbox.setVisible(false);
        newButton.setVisible(true);
        vReceiptsButton.setVisible(false);
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
    }

    public void updateData() {
        System.out.println("here");
        itemSelected.UpdateRandReceipts(lField.getText(), "T", "0.0", "0.0", "0", dStart.getValue(), dEnd.getValue(), 0, Integer.parseInt(tQty.getText()), Integer.parseInt(trCeipt.getText()), bridgeRciept.getText(), cafeRciept.getText(), tableID);
        //try {
        //    printTextDoc();
        //} catch (FileNotFoundException ex) {
        //    System.out.println(ex);
        //}
        resetFields();
        getTableItems();
    }
    
    public void deleteData() {
        int d = new messageBox().confirmMakeThisChange(Alert.AlertType.ERROR, null, "Confirm!", "are you sure you want to DELETE this item?");
        System.out.println("deleteData " + d);
        if (d == 1) {
            itemSelected.deleteBridgeReceipt(tableID);
            //try {
            //    printTextDoc();
            //} catch (FileNotFoundException ex) {
            //    System.out.println(ex);
            //}
            resetFields();
            getTableItems();
        }
    }

    public void newButton() {
        resetFields();
        BridgeReceipts n = new BridgeReceipts("", "", 0, 0.0, "", LocalDate.now(), LocalDate.now(), 0, 0.0, 0, 0, 0, "", "");
        n.newBridgeReceipt();
        getTableItems();
        
    }
    
    public void testPrintReceipt() {

        try {
            PW = new PrintWriter(new File(dbsp.pathNameLocal + "BridgeReceipt.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        //itemSelected = getItemsSelected();

        if (itemSelected.getReceipt() == 0) {
            PW.println(""); // to test if it works.
            PW.println("======================"); // to test if it works.
            PW.println(new GetDay().getDateLocalDateNow());
            PW.println(itemSelected.getTitle());
            PW.println("Employee: " + eFX.getNameF());
            PW.println("");
            PW.println("");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(itemSelected.getCafe());
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println("Bridge Receipt"); // to test if it works.
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.close();
        }

        System.out.println("========================================== " + itemSelected.getReceipt());
        if (itemSelected.getReceipt() == 1) {
            PW.println(""); // to test if it works.
            PW.println("======================"); // to test if it works.
            PW.println("");
            PW.println(itemSelected.getTitle());
            PW.println("Employee: " + eFX.getNameF());
            PW.println("");
            PW.println("");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(itemSelected.getBridge());
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println("Cafe Receipt"); // to test if it works.
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.close();

        }

            if (itemSelected.getReceipt() == 2) {
            PW.println(""); // to test if it works.
            PW.println("======================"); // to test if it works.
            PW.println(new GetDay().getDateLocalDateNow());
            PW.println(itemSelected.getTitle());
            PW.println("Employee: " + eFX.getNameF());
            PW.println("");
            PW.println("");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(itemSelected.getBridge());
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println("Cafe Receipt"); // to test if it works.
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
        


            PW.println(""); // to test if it works.
            PW.println("======================"); // to test if it works.
            PW.println("");
            PW.println(itemSelected.getTitle());
            PW.println("Employee: " + eFX.getNameF());
            PW.println("");
            PW.println("");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println(itemSelected.getCafe());
            PW.println(" ");
            PW.println(" ");
            PW.println(" ");
            PW.println("Bridge Receipt"); // to test if it works.
            PW.println(" ");
            PW.println(" ");
            PW.close();
            }

            
            
        Desktop dsk = Desktop.getDesktop();
        try {
            dsk.open(new File(dbsp.pathNameLocal + "BridgeReceipt.txt"));
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //print pr = new print();
        //pr.printReceipt("WithdrawalReceipt.txt");
        
    }
    
    
    
    

    private void printTextDoc() throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new File(dbStringPath.pathNameClub + "VIPList.txt"))) {
            String n = null;
            LocalDate today = LocalDate.now();
            ObservableList<BridgeReceipts> topics;
            //topics = cTable.getSelectionModel().getSelectedItems();
            topics = cTable.getItems();
            topics.forEach((BridgeReceipts each) -> {
                if (each.getStartDate().isBefore(today) && each.getEndDate().isAfter(today)) {
                    System.out.println(each.getStartDate() + " " + today);
                    System.out.println(each.getEndDate() + " " + today);
                    if (each.getTitle() != null) {
                        System.out.println(each.getTitle());
                        pw.println(each.getTitle());
                    }
                }
            });
            // print pr = new print();
            //pr.printReceipt("VIPList.txt");
        }

    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        if (updateButton.isVisible()) {
            resetFields();
            cancelButton.setText(" Exit ");
        } else {
            stageV.close();
            //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX1().titleBar);
        }
    }

}
