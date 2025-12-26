package views.timeclock;



import Css.cssChanger;
import reports.timeclock.tClockREport;
import commoncodes.ClubFunctions1;
import commoncodes.FocusedTextFieldHighlight;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.timeclock.tClock;
import models.timeclock.tClockDB;
import pWordFX.employeeFX;
import java.awt.Desktop;
import java.io.File;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import sceneChangerFX.SceneChanger_Main;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class TClockViewerController implements Initializable {
    @FXML private Pane root;
    @FXML private Button bCancelButton;
    @FXML private Button cUpdateButton;
    @FXML private Button bDeleteButton;
    @FXML private Button bAddNewButton;
    @FXML private Button bCalcButton;
    @FXML private Button bReportsButton;
    @FXML private Button bDropMenuButton;
    @FXML private Button lButton;
    @FXML private Label c1L;
    @FXML private TextField nameTF;
    @FXML private TextField totalTime;
    @FXML private ComboBox departmentTF;
    @FXML private TextField timeInTF;
    @FXML private TextField timeOutTF;
    @FXML public static int rowID;
    @FXML private HBox hbox;
    @FXML private DatePicker dateTF;

    @FXML private TableView<tClock> timeTable;
    @FXML private TableColumn<tClock, String> empIDC1;
    @FXML private TableColumn<tClock, LocalDate> dateC2;
    @FXML private TableColumn<tClock, String> dayC3;
    @FXML private TableColumn<tClock, String> nameC4;
    @FXML private TableColumn<tClock, Integer> timeINC5;
    @FXML private TableColumn<tClock, String> timeOUTC6;
    @FXML private TableColumn<tClock, Double> totalTimeC7;
    @FXML private TableColumn<tClock, Integer> departC8;
    
    
    
    public static String cssPath; 
    public static employeeFX eFX;
    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    //tClockDB db = new tClockDB();
    private static final DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
    private static final tClockREport g = new tClockREport();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");


    private static String timeTableID = "", typedText = "";
    private static tClock t = null;
    //private Stage stageV;
    private static ObservableList<tClock> r = null;
    private static SortedList<tClock> sortedList = null;
    private static FilteredList<tClock> filterData = null;
    private static ContextMenu contextMenu = new ContextMenu();
    private static Bounds boundsInScene, boundsInSceneCancelButton;
    private static String tOut;
    private static TablePosition pos = null;
    private static int row = 0;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        addTextfieldListeners();
        buildMenuButton();
        dateTF.setValue(LocalDate.now());
        empIDC1.setCellValueFactory(new PropertyValueFactory<>("EIDC1"));
        dateC2.setCellValueFactory(new PropertyValueFactory<>("DATEC2"));
        
        dateC2.setCellFactory(column -> {
            return new TableCell<tClock, LocalDate>() {
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
        dayC3.setCellValueFactory(new PropertyValueFactory<>("DayC3"));
        nameC4.setCellValueFactory(new PropertyValueFactory<>("NameC4"));
        timeINC5.setCellValueFactory(new PropertyValueFactory<>("INC5"));
        timeOUTC6.setCellValueFactory(new PropertyValueFactory<>("OUTC6"));
        totalTimeC7.setCellValueFactory(new PropertyValueFactory<>("TTC7"));
        totalTimeC7.setCellFactory(column -> {
            return new TableCell<tClock, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(item);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText(df2.format(item));
                    }
                }
            };
        });

        departC8.setCellValueFactory(new PropertyValueFactory<>("DPTC8"));
        new FocusedTextFieldHighlight().setHighlightListener(root);
        getTableItems(1);
        Platform.runLater(() -> getStageV());
        Platform.runLater(() -> fillComboBox());
        Platform.runLater(() -> nameTF.requestFocus());

    }

    
    
    public void buildMenuButton() {
        MenuItem r1Day = new MenuItem(" Single Day ");
        MenuItem r7Day = new MenuItem(" 7 Day ");
        MenuItem r30Day = new MenuItem(" 30 Day ");
        MenuItem r31Day = new MenuItem(" 31 Day ");
        
        r1Day.setOnAction((ActionEvent event) -> {
            LocalDate lDate = dateTF.getValue();
            g.tClockReport(new ClubFunctions1().asSQLDate(lDate), 1);
        });
        r7Day.setOnAction((ActionEvent event) -> {
            LocalDate lDate = dateTF.getValue();
            g.tClockReport(new ClubFunctions1().asSQLDate(lDate), 2);
        });
        r30Day.setOnAction((ActionEvent event) -> {
            LocalDate lDate = dateTF.getValue();
            g.tClockReport(new ClubFunctions1().asSQLDate(lDate), 3);
        });
        r31Day.setOnAction((ActionEvent event) -> {
            LocalDate lDate = dateTF.getValue();
            g.tClockReport(new ClubFunctions1().asSQLDate(lDate), 4);
        });

        contextMenu.getItems().addAll(r1Day, r7Day, r30Day, r31Day);

        // When user right-click on Circle
        bReportsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boundsInScene = bReportsButton.localToScene(bReportsButton.getBoundsInLocal());
                contextMenu.show(bReportsButton, boundsInScene.getMinX(), boundsInScene.getMaxY()+20.0);
            }
        });

        bReportsButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(bReportsButton, event.getScreenX(), event.getScreenY());
        });
    }
    
    public void menuItemsButtonMouseOver() {
        System.out.println("help");
        boundsInScene = bReportsButton.localToScene(bReportsButton.getBoundsInLocal());
        contextMenu.show(bReportsButton, boundsInScene.getMinX(), boundsInScene.getMaxY()+20.0);
    }
    
    
    
    
    
    public void MSGButtonPushed() {
        boundsInSceneCancelButton = bCancelButton.localToScene(bCancelButton.getBoundsInLocal());
        String thisone = "Message Editor";
        //getStageV();
        sc.goToScene(thisone, getStageV(), eFX.getNameF(), null, boundsInSceneCancelButton);
       // goToScene(thisone, stageV);
        
        
        
        
    }
    
    
     private Stage getStageV() {
        Stage stageV = (Stage) nameTF.getScene().getWindow();
        return stageV;
    }

    
    
    
    
    private void fillComboBox() {
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameTclock + "Depart.txt")).collect(Collectors.toList());
            departmentTF.setItems(FXCollections.observableArrayList(myList));
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
    }

    public void addTextfieldListeners() {
        nameTF.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.length() > 0) {
                        bCancelButton.setText("Clear");
                    } else {
                        bCancelButton.setText("Exit");
                    }
                }
        );

    }

//RIGHT HERE IS THE CODE THAT IS BLOCKED BECAUSE IT CANNOT SEE THE SCENECHANGER
    public void bDropMenuButton() {
        whichdropedit(1);
    }

     public void whichdropedit(int g) {
        // the other three frames rewuire this function
        String fName = "";
        switch (g) {
            case 1:
                fName = dbStringPath.pathNameTclock + "Depart.txt";
                break;
        }
        Desktop dsk = Desktop.getDesktop();
        try {
            dsk.open(new File(fName));
        } catch (IOException ex) {
            Logger.getLogger(TClockViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
    
    
    private void getTableItems(int f) {
        switch (f) {
            case 1:
                try {
                    r = null;
                    timeTable.getItems().removeAll();
                    sortedList = null;
                } catch (Exception e) {
                    System.out.println("Error " + e);
                }
                try {
                    fillList(1);
                } catch (SQLException ex) {
                    System.out.println("THis si timetable getall items " + ex.toString());
                }

                break;

            case 2:
                try {
                    r = null;
                    timeTable.getItems().removeAll();
                    sortedList = null;
                } catch (Exception e) {
                    System.out.println("Error " + e);
                }
                try {
                    fillList(2);
                } catch (SQLException ex) {
                    System.out.println("THis si timetable getall items " + ex.toString());
                }
                break;
        }
        timeTable.setEditable(false);
        getToRowID();
    }

    public void addNewRecord() {
        TablePosition pos = null;
        try {
            pos = timeTable.getSelectionModel().getSelectedCells().get(0);
        } catch (Exception e) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "No Row", "Please Select a row");
        }
        int row = pos.getRow();
        if (row < 1) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "Select Row", "Please select a row");
        }
        String q = null;
        Date dt3 = t.getSQLDate();
        Date dt2 = java.sql.Date.valueOf(dateTF.getValue());
        java.sql.Date f = null;
        String theday = new GetDay().getIntDate(dt2);
        if (!timeInTF.getText().isEmpty()) {
            calcTime();
            q = "INSERT INTO Memtime (Employee_ID, [Date], Day, Emp_Name, Time_IN, Time_Out, Total_Time, Location, Depart) VALUES (" + t.getEIDC1() + ", #" + dt2 + "#, '" + theday + "', '" + nameTF.getText() + "', '" + timeInTF.getText() + "', '" + timeOutTF.getText() + "', '" + totalTime.getText() + "', 'Pojos Fv', '" + "NewRecordTOEdit" + "')";
            System.out.println(q);
            new tClockDB().insertData(q);
            try {
                timeTable.itemsProperty().unbind();
                timeTable.setItems(null);
                //timeTable.getItems().clear();
            } catch (Exception e) {
                System.out.println("Error " + e);
            }

            resetfields();
            getTableItems(1);
            getToRowID();
            /*timeTable.requestFocus();
            timeTable.getItems().stream()
                    .filter(item -> (item.getC9() == null ? timeTableID == null : item.getC9().equals(timeTableID)))
                    .findAny()
                    .ifPresent(item -> {
                        timeTable.getSelectionModel().select(item);
                        timeTable.scrollTo(item);
                    });*/
        } else {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "No Time", "Time In Field cannot be blank.");
        }
    }

    private void getToRowID() {
        timeTable.requestFocus();
        timeTable.getItems().stream()
                .filter(item -> (item.getC9() == null ? timeTableID == null : item.getC9().equals(timeTableID)))
                .findAny()
                .ifPresent(item -> {
                    timeTable.getSelectionModel().select(item);
                    timeTable.scrollTo(item);
                });
    }

    
    private String getTimeOut() {
        tOut = "";
        try {
        tOut = timeOutTF.getText().trim();
        } catch (Exception e) {
            tOut = "";
        }
        return tOut;
    }
    
    
    public void upDateTime() {
        //TablePosition pos = timeTable.getSelectionModel().getSelectedCells().get(0);
        //int row = pos.getRow();
        //if (row < 1) {
        //    new messageBox().showAlert(Alert.AlertType.ERROR, null, "Select Row", "Please select a row");
        //}
        String q = null;
        Date dt3 = t.getSQLDate();
        Date dt2 = java.sql.Date.valueOf(dateTF.getValue());
        java.sql.Date f = null;
        String theday = new GetDay().getIntDate(dt2);
        if (!timeInTF.getText().isEmpty()) {
            if (!getTimeOut().isEmpty()) {
                calcTime();
                q = "UPDATE Memtime SET Memtime.[Date] = #" + dt2 + "#, Memtime.[Day] = '" + theday + "', Memtime.Time_IN = '" + timeInTF.getText() + "', Memtime.Time_OUT = '" + timeOutTF.getText() + "', Memtime.Total_Time = '" + totalTime.getText() + "', Memtime.Depart = '" + departmentTF.getValue() + "' WHERE (((Memtime.Tclock_ID)='" + timeTableID + "'))";
                System.out.println(q);
            } else {
                //NO OUT TIME
                q = "UPDATE Memtime SET Memtime.[Date] = #" + dt2 + "#, Memtime.[Day] = '" + theday + "', Memtime.Time_IN = '" + timeInTF.getText() + "', Memtime.Depart = '" + departmentTF.getValue() + "' WHERE (((Memtime.Tclock_ID)=" + timeTableID + "))";
            }
            new tClockDB().updateTimeClock(q);
        } else {
            JOptionPane.showMessageDialog(null, "Time In Field cannot be blank.");
        }
        getTableItems(1);
        //getToRowID();
        nameTF.requestFocus();
        //Platform.runLater(() -> nameTF.setText(getTypedText()));
        resetfields();
    }

    public void DeleteButtonPushed() {
        if (nameTF.getText().isEmpty()) {
            new messageBox().showAlert(AlertType.ERROR, getStageV(), "No Record", "Please Select a Record");
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Record?");
            alert.setHeaderText("");
            alert.setContentText("Are you Sure you want to delete this Record?\n " + nameTF.getText() + " " + myDateFormatter.format(dateTF.getValue()));
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(new cssChanger().cssPath());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                new tClockDB().deleteTimeRecord(timeTableID);
                getTableItems(1);
                nameTF.requestFocus();
            } else {
            }
        }
    }

    public void calcTime() {
        if (timeInTF.getText().trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "Blank Field", "The Time In Field cannot be blank.");
            timeInTF.requestFocus();
            return;
        }
        if (getTimeOut().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "Blank Field", "The Time Out Field cannot be blank.");
            timeOutTF.requestFocus();
            return;
        }
        String inTime = timeInTF.getText();
        String outTime = timeOutTF.getText();
        Date sdtInTime = null;
        Date sdtOutTime = null;
        sdtInTime = t.StringToDate(inTime);
        sdtOutTime = t.StringToDate(outTime);
        Double nTotalTime = t.timeTotal(sdtInTime, sdtOutTime);
        String s = String.valueOf(df2.format(nTotalTime));
        Date dt2 = t.asDate(dateTF.getValue());
        Date f = new Date(dt2.getTime());
        totalTime.setText(s);
    }

    public void resetfields() {
        timeInTF.clear();
        timeOutTF.clear();
        totalTime.clear();
        bCancelButton.setText("Exit");
        nameTF.clear();
        setTypedText("");
        pos = null;
        row = 0;
        t = null;
        nameTF.requestFocus();
        //timeTable.getSortOrder().removeAll(r);
        //empIDC1.setSortType(TableColumn.SortType.ASCENDING);
        //timeTable.sort();
        //timeTable.getSortOrder().add(empIDC1);
        //empIDC1.setSortable(true);
        //timeTable.sort();
        timeTable.getSelectionModel().select(0);
        timeTable.getSelectionModel().clearSelection();
        
    }

    public void getTableRow() throws IOException {
        try {
        pos = timeTable.getSelectionModel().getSelectedCells().get(0);
        row = pos.getRow();
        timeTableID = timeTable.getItems().get(row).getC9();
        t = timeTable.getSelectionModel().getSelectedItem();
        dateTF.setValue(t.getDATEC2());
        nameTF.setText(t.getNameC4());
        setTypedText(t.getNameC4());
        //nameTF.clear();
        totalTime.setText(String.valueOf(t.getTTC7()));
        departmentTF.setValue(t.getDPTC8());
        timeInTF.setText(t.getINC5());
        timeOutTF.setText(t.getOUTC6());
        bCancelButton.setText("Clear");
        getTableItems(2);

        } catch (Exception e) {
            //System.out.println("ffffffffffffffffffff " + e);
        }
        //timeTable.getSelectionModel().clearSelection();
    }

    private void enterKeyPressed() {
        if (nameTF.isFocused()) {
            departmentTF.requestFocus();
            return;
        }
        if (departmentTF.isFocused()) {
            timeInTF.requestFocus();
            return;
        }
        if (timeInTF.isFocused()) {
            timeOutTF.requestFocus();
            return;
        }
        if (timeOutTF.isFocused()) {
            nameTF.requestFocus();
            return;
        }
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F1) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F2) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F3) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F4) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F5) {
                keyListener(ke);
                ke.consume();
            }
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
            if (ke.getCode() == KeyCode.F10) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F11) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F12) {
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
            if (ke.getCode() == KeyCode.ENTER) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.TAB) {
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
            case TAB:
                enterKeyPressed();
                break;
            case UP:
                break;
            case ESCAPE:
                //resetExit();
                bCancelButton.fire();
                break;
            case ENTER:
                enterKeyPressed();
                break;
            default:
                break;
        }
    }

    private void setTypedText(String s) {
        this.typedText = s;
    }

    public String getTypedText() {
        return this.typedText;
    }

    public void fillList(int a) throws SQLException {

        switch (a) {
            case 1:
                filterData = new FilteredList<>(new tClockDB().getdataB(), p -> true);
                break;
            case 2:
                filterData = new FilteredList<>(new tClockDB().getFilteredData(nameTF.getText()), p -> true);
                break;
            case 3:
                filterData = new FilteredList<>(timeTable.getItems(), p -> true);
                nameTF.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
                    filterData.setPredicate(pers -> {
                        if (newvalue == null || newvalue.isEmpty()) {
                            return false;
                        }
                        typedText = newvalue.toLowerCase();
                        setTypedText(typedText);
                        if (pers.getNameC4().toLowerCase().contains(typedText)) {
                            return true;
                        }
                        
                        try {
                        if (pers.getOUTC6().toLowerCase().contains(typedText)) {
                            return true;
                        }
                        } catch (Exception e) {System.out.println(e); return false;}
                        
                        if (String.valueOf(pers.getDPTC8()).toLowerCase().contains(typedText)) {
                            return true;
                        }
                        return false;
                    });
                });
                break;

        }
        sortedList = new SortedList<>(filterData);
        sortedList.comparatorProperty().bind(timeTable.comparatorProperty());
        timeTable.setItems(sortedList);
    }

    public void searchRecord(KeyEvent ke) throws SQLException {
        //System.out.println(ke);
        fillList(3);
        //FilteredList<tClock> filterData = new FilteredList<>(new tClockDB().getdataB(), p -> true);
        /*FilteredList<tClock> filterData = new FilteredList<>(timeTable.getItems(), p -> true);
        nameTF.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                typedText = newvalue.toLowerCase();
                System.out.println(typedText);
                setTypedText(typedText);
                if (pers.getNameC4().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (String.valueOf(pers.getDPTC8()).toLowerCase().contains(typedText)) {
                    return true;
                }

                return false;
            });
            sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(timeTable.comparatorProperty());
            timeTable.setItems(sortedList);
        });*/
    }

    public void resetExit() {
        if (!nameTF.getText().trim().isEmpty()) {
            getTableItems(1);
            resetfields();
            //nameTF.requestFocus();
        } else {
            try {
                exitButtonPushed();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void exitButtonPushed() throws IOException {
        Platform.runLater(() -> new tClockDB().keepTimeClockClean());
        //stageV = (Stage) bCancelButton.getScene().getWindow();
        //sc.changeScenes(getStageV(), "/views/Main.fxml", "Pojo Main " + new employeeFX().titleBar);
        getStageV().close();
    }

    private static class ChangeListenerImpl implements ChangeListener<Boolean> {

        public ChangeListenerImpl() {
        }

        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
            if (newPropertyValue) {
                System.out.println("Textfield on focus");
            } else {
                System.out.println("Textfield out focus");
            }
        }
    }


}
