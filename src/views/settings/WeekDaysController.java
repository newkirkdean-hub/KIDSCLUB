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
import models.settings.WeekDay;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class WeekDaysController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button updateButton;
    @FXML private TextField tStTime;
    @FXML private TextField tEndTime;
    //@FXML private Button newButton;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    @FXML private TextField lField;
    @FXML private TextField tinitials;
    @FXML private TextField tTickets;
    @FXML private TextField tMultiplyer;
    @FXML private TextField tOther;
    @FXML private TextArea noteT;
    @FXML private TextField screenPrompt;
    @FXML private TextField tMax;
    @FXML private DatePicker dStart;
    @FXML private DatePicker dEnd;
    @FXML private TextField tRepeat;
    @FXML private TextField tQty;
    //@FXML private TextField trCeipt;    
    @FXML private TableView<WeekDay> cTable;
    @FXML private TableColumn<WeekDay, String> Day;
    @FXML private TableColumn<WeekDay, String> ScreenPrompt;
    @FXML private TableColumn<WeekDay, String> Tickets;
    @FXML private TableColumn<WeekDay, Double> Multiply;
    @FXML private TableColumn<WeekDay, String> Initials;
    @FXML private TableColumn<WeekDay, LocalDate> StartDate;
    @FXML private TableColumn<WeekDay, LocalDate> EndDate;
    @FXML private TableColumn<WeekDay, String> Repeat;
    @FXML private TableColumn<WeekDay, Integer> Max;
    @FXML private TableColumn<WeekDay, Double> Bonus;
    @FXML private TableColumn<WeekDay, String> stTime;
    @FXML private TableColumn<WeekDay, String> endTime;
    
    
    
    employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssC.cssPath());
        setKeyCodes();
        Day.setCellValueFactory(new PropertyValueFactory<>("Day"));
        ScreenPrompt.setCellValueFactory(new PropertyValueFactory<>("ScreenPrompt"));
        Initials.setCellValueFactory(new PropertyValueFactory<>("Initials"));
        Tickets.setCellValueFactory(new PropertyValueFactory<>("Tickets"));
        Multiply.setCellValueFactory(new PropertyValueFactory<>("Multiply"));
        Bonus.setCellValueFactory(new PropertyValueFactory<>("Bonus"));
        StartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        EndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        Repeat.setCellValueFactory(new PropertyValueFactory<>("Repeat"));
        Max.setCellValueFactory(new PropertyValueFactory<>("Max"));
        stTime.setCellValueFactory(new PropertyValueFactory<>("STime"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("ETime"));
        setToUpper();
        getTableItems();
        updateButton.setVisible(false);
        cTable.setFixedCellSize(50.0);
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
        ScreenPrompt.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void tableItemSelected() {
        WeekDay itemSelected = cTable.getSelectionModel().getSelectedItem();
        lField.setText(itemSelected.getDay());
        screenPrompt.setText(itemSelected.getScreenPrompt());
        tinitials.setText(itemSelected.getInitials());
        tTickets.setText(String.valueOf(itemSelected.getTickets()));
        tMultiplyer.setText(itemSelected.getMultiply().toString());
        tOther.setText(itemSelected.getBonus().toString());
        dStart.setValue(itemSelected.getStartDate());
        dEnd.setValue(itemSelected.getEndDate());
        tRepeat.setText(String.valueOf(itemSelected.getRepeat()));
        //tQty.setText(String.valueOf(itemSelected.getSort()));
        //newButton.setVisible(false);
        tableID = itemSelected.getC10();
        tMax.setText(String.valueOf(itemSelected.getMax()));
        tStTime.setText(String.valueOf(itemSelected.getSTime()));
        tEndTime.setText(String.valueOf(itemSelected.getETime()));
        updateButton.setVisible(true);
        //vReceiptsButton.setVisible(true);
        hbox.setVisible(true);

    }

    public ObservableList<WeekDay> getdata() throws SQLException {
        ObservableList<WeekDay> data = FXCollections.observableArrayList();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "Other.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            //rs = st.executeQuery("SELECT RandReciepts.Item, RandReciepts.Needed, RandReciepts.Current, RandReciepts.Note, RandReciepts.ID FROM RandReciepts;");
            //rs = st.executeQuery("SELECT VIPPromos.Name, VIPPromos.Initials, VIPPromos.Tickets, VIPPromos.Multiply, VIPPromos.Other, VIPPromos.StartDate, VIPPromos.EndDate, VIPPromos.Repeat, VIPPromos.QTY, VIPPromos.Sort, VIPPromos.ID, VIPPromos.Receipt\n" +
            rs = st.executeQuery("SELECT Daily.*\n"
                    + "FROM Daily;");
            while (rs.next()) {
                WeekDay newdata = new WeekDay(rs.getString("Name"), rs.getString("Group"), rs.getString("Initials"), rs.getInt("Tickets"), rs.getDouble("Multiply"), rs.getDouble("Other"), rs.getDate("StartDate").toLocalDate(), rs.getDate("EndDate").toLocalDate(), rs.getInt("Repeat"), rs.getInt("Max Tickets"), rs.getInt("STime"), rs.getInt("ETime"), rs.getInt("ID"));
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
       // noteT.setText("");
        hbox.setVisible(false);
        //newButton.setVisible(true);
        //vReceiptsButton.setVisible(false);
        updateButton.setVisible(false);
    }

    
    // NEED TO FIX UPDATE BUTTON HERE BELOW. LAST WROKED ON 1/25/2021
    public void updateData() {
        WeekDay itemSelected = cTable.getSelectionModel().getSelectedItem();
        itemSelected.UpdateRandReceipts(lField.getText(), screenPrompt.getText(), tinitials.getText(), tTickets.getText(), tMultiplyer.getText(), tOther.getText(), dStart.getValue(), dEnd.getValue(), Integer.valueOf(tRepeat.getText()), Integer.valueOf(tMax.getText()), tStTime.getText(), tEndTime.getText(), tableID);
        try {
            printTextDoc();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        resetFields();
        getTableItems();

    }

    public void newButton() {
        //VIPPromos n = new VIPPromos("", "", "0", "", LocalDate.now(), LocalDate.now(), "", 0, 0);
        WeekDay wd = new WeekDay("", "", "0", "", LocalDate.now(), LocalDate.now(), "", 0, 0);
        wd.newVIPPromo();
        getTableItems();
    }

    private void printTextDoc() throws FileNotFoundException {
        try (PrintWriter pw = new PrintWriter(new File(dbStringPath.pathNameClub + "VIPList.txt"))) {
            String n = null;
            LocalDate today = LocalDate.now();
            ObservableList<WeekDay> topics;
            //topics = cTable.getSelectionModel().getSelectedItems();
            topics = cTable.getItems();
            topics.forEach((WeekDay each) -> {
                if (each.getStartDate().isBefore(today) && each.getEndDate().isAfter(today)) {
                    System.out.println(each.getStartDate() + " " + today);
                    System.out.println(each.getEndDate() + " " + today);
                    if (each.getDay() != null) {
                        System.out.println(each.getDay());
                        pw.println(each.getDay());
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
