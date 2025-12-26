package views.settings;

import Css.cssChanger;
import dbpathnames.dbStringPath;
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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.settings.VIPPromos;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class VIPPromosController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private Button vReceiptsButton;
    @FXML private Button newButton;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    @FXML private TextField lField;
    @FXML private TextField tinitials;
    @FXML private TextField tTickets;
    @FXML private TextField tMultiplyer;
    @FXML private TextArea noteT;
    @FXML private TextArea bridgeRciept;
    @FXML private TextArea cafeRciept;
    @FXML private DatePicker dStart;
    @FXML private DatePicker dEnd;
    @FXML private TextField tRepeat;
    @FXML private TextField tQty;
    @FXML private TextField trCeipt;    
    @FXML private TableView<VIPPromos> cTable;
    @FXML private TableColumn<VIPPromos, String> C1;
    @FXML private TableColumn<VIPPromos, String> C2;
    @FXML private TableColumn<VIPPromos, String> C3;
    @FXML private TableColumn<VIPPromos, String> C4;
    @FXML private TableColumn<VIPPromos, String> C5;
    @FXML private TableColumn<VIPPromos, LocalDate> C6;
    @FXML private TableColumn<VIPPromos, LocalDate> C7;
    @FXML private TableColumn<VIPPromos, String> C8;
    @FXML private TableColumn<VIPPromos, Integer> C9;
    @FXML private TableColumn<VIPPromos, Integer> C10;
    @FXML private TableColumn<VIPPromos, String> C11;
    @FXML private TableColumn<VIPPromos, String> C12;
    
    
    
    employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        setKeyCodes();
        //C1.setCellValueFactory(new PropertyValueFactory<>("C1"));
        C1.setCellValueFactory(new PropertyValueFactory<>("VName"));
        
        C2.setCellValueFactory(new PropertyValueFactory<>("VInitials"));
        C3.setCellValueFactory(new PropertyValueFactory<>("VTickets"));
        C4.setCellValueFactory(new PropertyValueFactory<>("VMultiply"));
        C5.setCellValueFactory(new PropertyValueFactory<>("VOther"));
        C6.setCellValueFactory(new PropertyValueFactory<>("VsDate"));
        C7.setCellValueFactory(new PropertyValueFactory<>("VeDate"));
        C8.setCellValueFactory(new PropertyValueFactory<>("VRepeat"));
        C9.setCellValueFactory(new PropertyValueFactory<>("rCeipt"));
        C10.setCellValueFactory(new PropertyValueFactory<>("Sort"));
        C11.setCellValueFactory(new PropertyValueFactory<>("BridgeReceipt"));
        C12.setCellValueFactory(new PropertyValueFactory<>("CafeReceipt"));
        setToUpper();
        getTableItems();
        hbox.setVisible(false);
        updateButton.setVisible(false);
        vReceiptsButton.setVisible(false);
        cTable.setFixedCellSize(50.0);
        cTable.requestFocus();
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
        C2.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void tableItemSelected() {
        VIPPromos itemSelected = cTable.getSelectionModel().getSelectedItem();
        //lField.setText(itemSelected.getC1());
        lField.setText(itemSelected.getvName());
        //tinitials.setText(itemSelected.getC2());
        tinitials.setText(itemSelected.getvInitials());
        //tTickets.setText(itemSelected.getC3());
        tTickets.setText(String.valueOf(itemSelected.getvTickets()));
        //tMultiplyer.setText(itemSelected.getC4());
        tMultiplyer.setText(String.valueOf(itemSelected.getvMultiply()));
        //noteT.setText(itemSelected.getC5());
        noteT.setText(itemSelected.getvOther());
        //dStart.setValue(itemSelected.getC6());
        dStart.setValue(itemSelected.getVsDate());
        //dEnd.setValue(itemSelected.getC7());
        dEnd.setValue(itemSelected.getVeDate());
        //tRepeat.setText(itemSelected.getC8());
        tRepeat.setText(String.valueOf(itemSelected.getvRepeat()));
        //tQty.setText(String.valueOf(itemSelected.getSort()));
        newButton.setVisible(false);
        //tableID = itemSelected.getC10();
        tableID = itemSelected.getvID();
        //trCeipt.setText(String.valueOf(itemSelected.getC9()));
        tQty.setText(String.valueOf(itemSelected.getSort()));
        trCeipt.setText(String.valueOf(itemSelected.getrCeipt()));
        //trCeipt.setText(String.valueOf(itemSelected.getrCeipt()));
        //bridgeRciept.setText(itemSelected.getCafe());
        bridgeRciept.setText(itemSelected.getBridgeReceipt());
        //cafeRciept.setText(itemSelected.getBridge());
        cafeRciept.setText(itemSelected.getCafeReceipt());
        updateButton.setVisible(true);
        //vReceiptsButton.setVisible(true);
        hbox.setVisible(true);

    }

    public ObservableList<VIPPromos> getdata() throws SQLException {
        ObservableList<VIPPromos> data = FXCollections.observableArrayList();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            //rs = st.executeQuery("SELECT RandReciepts.Item, RandReciepts.Needed, RandReciepts.Current, RandReciepts.Note, RandReciepts.ID FROM RandReciepts;");
            //rs = st.executeQuery("SELECT VIPPromos.Name, VIPPromos.Initials, VIPPromos.Tickets, VIPPromos.Multiply, VIPPromos.Other, VIPPromos.StartDate, VIPPromos.EndDate, VIPPromos.Repeat, VIPPromos.QTY, VIPPromos.Sort, VIPPromos.ID, VIPPromos.Receipt\n" +
            rs = st.executeQuery("SELECT VIPPromos.*\n"
                    + "FROM VIPPromos;");
            while (rs.next()) {
                                                       //String vName,        String vInitials,       String vOther,         int vTickets,         int vMultiply,           int vRepeat,          int vQTY,           int vID,         LocalDate vsDate,                    LocalDate veDate,                 String rBridge,                 String rCafe,     int rCeipt
                VIPPromos newdata = new VIPPromos(rs.getString("Name"), rs.getString("Initials"), rs.getString("Other"), rs.getInt("Tickets"), rs.getInt("Multiply"), rs.getInt("Repeat"), rs.getInt("QTY"), rs.getInt("ID"), rs.getDate("StartDate").toLocalDate(), rs.getDate("EndDate").toLocalDate(), rs.getString("CafeR"), rs.getString("BridgeR"), rs.getInt("Receipt"), rs.getInt("sort"));
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
        // new Mail_JavaFX1().sendEmailTo("TEST", "TEST", "errors");
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
                JOptionPane.showMessageDialog(null, "F1");
                break;
            case F2:
                JOptionPane.showMessageDialog(null, "F2");
                break;
            case F3:
                JOptionPane.showMessageDialog(null, "F3");
                break;
            case F4:
                break;
            case F5:
                JOptionPane.showMessageDialog(null, "F5");
                break;
            case F6:
                JOptionPane.showMessageDialog(null, "F6");
                break;
            case F7:
                JOptionPane.showMessageDialog(null, "F7");
                break;
            case F8:
                JOptionPane.showMessageDialog(null, "F8");
                break;
            case F9:
                break;
            case F10:
                JOptionPane.showMessageDialog(null, "F10");
                break;
            case F11:
                JOptionPane.showMessageDialog(null, "F11");
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
        tinitials.textProperty().addListener((ov, oldValue, newValue) -> {
            tinitials.setText(newValue.toUpperCase());
        });
        
    }

    private void resetFields() {
        lField.setText("");
        tinitials.setText("");
        tTickets.setText("");
        noteT.setText("");
        hbox.setVisible(false);
        newButton.setVisible(true);
        vReceiptsButton.setVisible(false);
        updateButton.setVisible(false);
    }

    public void updateData() {
        VIPPromos itemSelected = cTable.getSelectionModel().getSelectedItem();
        itemSelected.UpdateRandReceipts(lField.getText(), tinitials.getText(), tTickets.getText(), tMultiplyer.getText(), noteT.getText(), dStart.getValue(), dEnd.getValue(), tRepeat.getText(), tQty.getText(), cafeRciept.getText(), bridgeRciept.getText(), tableID, Integer.parseInt(trCeipt.getText()));
        try {
            printTextDoc();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        resetFields();
        getTableItems();
    }

    public void newButton() {
        VIPPromos n = new VIPPromos("", "", "0", 0, 0, 0, 0, 0, LocalDate.now(), LocalDate.now(), 0);

        n.newVIPPromo();
        getTableItems();
        
    }

    private void printTextDoc() throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new File(dbStringPath.pathNameClub + "VIPList.txt"))) {
            String n = null;
            LocalDate today = LocalDate.now();
            ObservableList<VIPPromos> topics;
            //topics = cTable.getSelectionModel().getSelectedItems();
            topics = cTable.getItems();
            topics.forEach((VIPPromos each) -> {
                if (each.getVsDate().isBefore(today) && each.getVeDate().isAfter(today)) {
                    System.out.println(each.getVsDate() + " " + today);
                    System.out.println(each.getVeDate() + " " + today);
                    if (each.getvName() != null) {
                        System.out.println(each.getvName());
                        pw.println(each.getvName());
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
