package views.clubmail;

import Css.cssChanger;
import JavaMail.Mail_JavaFX;
import JavaMail.Mail_JavaFX1;
import commoncodes.IsItANumber;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
import messageBox.messageBox;
import models.clubmail.BdaysNextMonth;
import pWordFX.employeeFX;
import dbpathnames.GetDay;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.scene.control.TableCell;
import reports.clubmail.XLSBirthdaysNextMonth;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class BdaysNextMonthController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Button countButton;
    @FXML private Button printLabelsButton;
    @FXML private Button twoCleanButton;
    @FXML private Button updateButton;
    @FXML private Button fillLastUsedButton;
    @FXML private DatePicker Date1;
    @FXML private DatePicker Date2;
    @FXML private TextField c1L;
    @FXML private TextField c2T;
    @FXML private TextField c3T;
    @FXML private TextField c4T;
    @FXML private TextField c5T;
    @FXML private TextArea noteT;
    @FXML private Pane errorBar;
    @FXML private Label errorLabel;
    @FXML public static int tableID;
    @FXML private HBox hbox;
    
    @FXML private TableView<BdaysNextMonth> cTable;
    @FXML private TableColumn<BdaysNextMonth, String> C1;
    @FXML private TableColumn<BdaysNextMonth, String> C2;
    @FXML private TableColumn<BdaysNextMonth, String> C3;
    @FXML private TableColumn<BdaysNextMonth, String> C4;
    @FXML private TableColumn<BdaysNextMonth, String> C5;
    @FXML private TableColumn<BdaysNextMonth, String> C55;
    @FXML private TableColumn<BdaysNextMonth, String> C6;
    @FXML private TableColumn<BdaysNextMonth, String> C7;
    @FXML private TableColumn<BdaysNextMonth, String> C8;
    @FXML private TableColumn<BdaysNextMonth, LocalDate> C9;
    @FXML private TableColumn<BdaysNextMonth, Integer> C10;
    
    
    public static dbStringPath dbsp;
    public static String cssPath;
    public static messageBox mBox;
    public static Mail_JavaFX1 jmail;
    
    private static Connection conn = null;
    private static PreparedStatement pst = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static final String EMAIL_SUBJECT = "Error in CLUBMAIL Bdays Next Month Controller";
    private static final DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static Bounds boundsInScene;
    private static ContextMenu contextMenu = new ContextMenu();
    private static int del=0, size=0;
    private static final LocalDate DATEFIRSTDAY = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
 
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        Date1.setValue(DATEFIRSTDAY);
        buildMenuButton();
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setKeyCodes1();
        C1.setCellValueFactory(new PropertyValueFactory<>("CCN"));
        C2.setCellValueFactory(new PropertyValueFactory<>("Ncombined"));
        C3.setCellValueFactory(new PropertyValueFactory<>("Address"));
        C4.setCellValueFactory(new PropertyValueFactory<>("City"));
        C5.setCellValueFactory(new PropertyValueFactory<>("State"));
        C55.setCellValueFactory(new PropertyValueFactory<>("Zip"));
        C6.setCellValueFactory(new PropertyValueFactory<>("Bdate"));
        C7.setCellValueFactory(new PropertyValueFactory<>("Balance"));
        C8.setCellValueFactory(new PropertyValueFactory<>("Odate"));
        C9.setCellValueFactory(new PropertyValueFactory<>("AreaCode"));
        C9.setCellFactory(column -> {
            return new TableCell<BdaysNextMonth, LocalDate>() {
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
        C10.setCellValueFactory(new PropertyValueFactory<>("TID"));
        getTableItems();
        size = cTable.getItems().size();
        cTable.setFixedCellSize(30.0);
        if (size > 0) {
            System.out.println("size is greater then 0");
            Platform.runLater(() -> {
                cTable.requestFocus();
                cTable.getSelectionModel().select(0);
                cTable.getFocusModel().focus(0);
            });
        }
        //mb.showError("Birthdays Next Month Labels", errorLabel, errorBar);
    }    
    
    private void getTableItems() {
        cTable.getItems().clear();
        try {
            cTable.getItems().addAll(getdataB());
        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
        cTable.setEditable(true);
        new messageBox().showError("Birthdays Next Month " + cTable.getItems().size(), errorLabel, errorBar);
        //C2.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void countButtonPushed() {
        ObservableList selectedItems = cTable.getSelectionModel().getSelectedItems();
        mBox.showError("The items selected in the table total: " + selectedItems.size(), errorLabel, errorBar);

    }

    public void buildMenuButton() {
        MenuItem address = new MenuItem(" Address Labels");
        MenuItem addressCCN = new MenuItem(" Address W/ CCN's ");
        //MenuItem add = new MenuItem("  ");

        //Menu dropEdits = new Menu(" Drop Edits ");
        MenuItem csItem1 = new MenuItem(" Parties ");
        MenuItem csItem2 = new MenuItem(" Introduced ");
        MenuItem csItem3 = new MenuItem(" Party Times ");
        MenuItem csItem4 = new MenuItem(" Room Number ");
        //dropEdits.getItems().addAll(csItem1, csItem2, csItem3, csItem4);
        ObservableList<BdaysNextMonth> list = cTable.getItems();
        address.setOnAction((ActionEvent event) -> {
            DumpData(list);
        });
        addressCCN.setOnAction((ActionEvent event) -> {
            DumpDataCCN(list);
        });

        contextMenu.getItems().addAll(address, addressCCN);

        // When user right-click on Circle
        printLabelsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boundsInScene = printLabelsButton.localToScene(printLabelsButton.getBoundsInLocal());
                contextMenu.show(printLabelsButton, boundsInScene.getMaxX() + 5.0, boundsInScene.getMaxY() - 42.0);
            }
        });

        printLabelsButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(printLabelsButton, event.getScreenX(), event.getScreenY());
        });
    }

    public void menuItemsButtonMouseOver() {
        boundsInScene = printLabelsButton.localToScene(printLabelsButton.getBoundsInLocal());
        contextMenu.show(printLabelsButton, boundsInScene.getMaxX() + 5.0, boundsInScene.getMaxY() - 42.0);
    }






    public void DumpData(ObservableList<BdaysNextMonth> list) {
        XLSBirthdaysNextMonth XRW = new XLSBirthdaysNextMonth();
        try {
            //errorLabel.setText("");
            XRW.collectionListReport(list);
        } catch (IOException | SQLException ex) {
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
    }

    public void DumpDataCCN(ObservableList<BdaysNextMonth> list) {
        XLSBirthdaysNextMonth XRW = new XLSBirthdaysNextMonth();
        try {
            //errorLabel.setText("");
            XRW.collectionListReportCNN(list);
        } catch (IOException | SQLException ex) {
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
    }


 


    public void RunSearch() {
        mBox.showErrorClear(errorLabel, errorBar);
        new IsItANumber().validDate(Date1, "The first Date field is empty");
        cTable.getItems().clear();
        try {
            //conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE * FROM BdaysNextMonth";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        BdaysNextMonth m = new BdaysNextMonth(Date1.getValue(), Date1.getValue());
        int bday = Date1.getValue().getMonthValue();
        //System.out.println(bday);
        try {
            m.RunSearchBdays(bday);
        } catch (SQLException ex) {
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
        cTable.getItems().clear();
        getTableItems();
        mBox.showError("Birthday Search is Complete " + cTable.getItems().size() + " " + "USE (Quick Clean) to delete (CLOSED, LOCKED or WRONG ADDRESS", errorLabel, errorBar);
    }

    public void deleteMailLabel() {
        mBox.showErrorClear(errorLabel, errorBar);

        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE *\n"
                    + "FROM BdaysNextMonth\n"
                    + "WHERE (((BdaysNextMonth.ID)='" + tableRowSelected() + "'));";
            // JOptionPane.showMessageDialog(null, sql);

            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        getTableItems();

    }

    public void del() {
        ObservableList<BdaysNextMonth> selectedItems = cTable.getSelectionModel().getSelectedItems();
        //System.out.println(selectedItems.size());
        ArrayList<String> selectedIDs = new ArrayList<>();
        for (BdaysNextMonth row : selectedItems) {
            selectedIDs.add(row.getTID());
        }

        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        ListIterator<String> iterator = selectedIDs.listIterator();
        while (iterator.hasNext()) {
            try {
                String sql = "DELETE *\n"
                        + "FROM BdaysNextMonth\n"
                        + "WHERE (((BdaysNextMonth.ID)='" + iterator.next() + "'));";

                pst = conn.prepareStatement(sql);
                pst.executeUpdate();

            } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
            }
        }
        getTableItems();

    }

    public void QuickClean() {
        mBox.showErrorClear(errorLabel, errorBar);
        mBox.showError("Cleaning WRONG ADDRESS & CLOSE", errorLabel, errorBar);
        try {
            System.out.println("1");
            String sql = "DELETE * \n"
                    + "FROM BdaysNextMonth\n"
                    + "WHERE (((BdaysNextMonth.Address)=\"WRONG ADDRESS\")) OR (((BdaysNextMonth.City)=\"CLOSE\")) OR (((BdaysNextMonth.City)=\"LOCK\"));";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        //getTableItems();
        try {
            //conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            System.out.println("2");
            String sql = "DELETE * \n"
                    + "FROM BdaysNextMonth\n"
                    + "WHERE (((BdaysNextMonth.Address) Is Null)) OR (((BdaysNextMonth.City) Is Null)) OR (((BdaysNextMonth.State) Is Null)) OR (((BdaysNextMonth.[Zip Code]) Is Null));";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
        System.out.println("3");

        //Platform.runLater(()->mb.showError("Quick Clean Complete, Wrong Address - Close - Empty Address, City State or Zip Code", errorLabel, errorBar));
        cTable.getItems().clear();
        getTableItems();

    }

    public void twoCleanButtonPushed() {
        checkTwoYearDetail();
        cTable.getItems().clear();
        getTableItems();
    }
    
    
    public void FillLastUsed() {
        BdaysNextMonth m = new BdaysNextMonth(Date1.getValue(), Date1.getValue());
        ObservableList<BdaysNextMonth> list = cTable.getItems();

        if (list.size() > 0) {

            m.dbconnect();
            list.forEach((BdaysNextMonth each) -> {
            try {    
                if (m.checkDetail(each.getCCN())) {
                    System.out.println("Customer " + each.getCCN() + " Date " + m.getDate() + "CheckDetail TRUE");
                    System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                    insertDate(each.getCCN(), m.getDate());
                    m.setDate(new GetDay().asSQLDate(LocalDate.of(12, Month.MARCH, 3)));
                } else {
                    if (m.checkDetailInactive(each.getCID())) {
                        System.out.println("Customer " + each.getCCN() + " Date " + m.getDate() + "CheckInactiveDetail TRUE");
                        System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                        insertDate(each.getCCN(), m.getDate());
                        m.setDate(new GetDay().asSQLDate(LocalDate.of(12, Month.MARCH, 3)));
                    }
                }
            } catch (SQLException ex) {
                    System.out.println(ex);
            }
                //System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                //insertDate(each.getCCN(), m.getDate());
                //m.setDate(new GetDay().asSQLDate(LocalDate.of(12, Month.MARCH, 3)));
            });
        }
        getTableItems();
        m.disconnect();

    }

    private void checkTwoYearDetail() {
        BdaysNextMonth m = new BdaysNextMonth(Date1.getValue(), Date1.getValue());
        ObservableList<BdaysNextMonth> list = cTable.getItems();
        
        if (list.size() > 0) {

            m.dbconnect();
            list.forEach((BdaysNextMonth each) -> {
                try {
                    if (!m.checkTwoYearDetail(each.getCCN())) {
                        del++;
                    } else {
                        del--;
                    }
                    if (!m.checkTwoYearDetailInactive(each.getCID())) {
                        //System.out.println(each.getCID());
                        del++;
                    } else {
                        del--;
                    }

                    if (del > 0) {
                        System.out.println("yyyyyyyyyyyyyyyyyyyyyy " + each.getCCN() + " " + del);
                        delNotTwoYearsActivie(each.getCCN());
                    } else {
                        //System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                        //THIS IS WHERE WE RUN A NEW COMMAND
                        //insertDate(each.getCCN(), m.getDate());
                        //m.setDate(new GetDay().asSQLDate(LocalDate.now()));
                    }

                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                del = 0;
            });
        }
        getTableItems();
        m.disconnect();
    }

    private void insertDate(String CCN, LocalDate ldate) {
        
        try {
      
            String sql = "UPDATE BdaysNextMonth SET BdaysNextMonth.Area_Code = ? WHERE (((BdaysNextMonth.[Customer Card Number])='" + CCN + "'));";
            pst = conn.prepareStatement(sql);
            pst.setDate(1, new GetDay().asSQLDate(ldate));
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("error in New Member Mail putNewMember " + ex);
        }
    
    }
    
    
    public void delNotTwoYearsActivie(String ID) {
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE *\n"
                    + "FROM BdaysNextMonth\n"
                    + "WHERE (((BdaysNextMonth.[Customer Card Number])='" + ID + "'));";

            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

        } catch (SQLException ex) {
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
}
    
    
    
    
    
    
    
   

    public String tableRowSelected() {
        String stt = null;
        TablePosition pos = cTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        stt = cTable.getItems().get(row).getCID();
        return stt;
    }

    public ObservableList<BdaysNextMonth> getdataB() throws SQLException {
        ObservableList<BdaysNextMonth> data = FXCollections.observableArrayList();

        try {
            //conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT BdaysNextMonth.[Customer Card Number], BdaysNextMonth.[Customer ID], BdaysNextMonth.[First Name], BdaysNextMonth.[Last Name], BdaysNextMonth.[Address], BdaysNextMonth.[City], BdaysNextMonth.[State], BdaysNextMonth.[Zip Code], BdaysNextMonth.[Birth Date], BdaysNextMonth.[Orig Date], BdaysNextMonth.[Ticket Balance], BdaysNextMonth.[Area_code], BdaysNextMonth.[ID] FROM BdaysNextMonth");
            while (rs.next()) {
                //System.out.println(rs.getDate("Birth Date")); 

                BdaysNextMonth newdata = new BdaysNextMonth(rs.getString("Customer Card Number"), rs.getString("Customer ID"), rs.getString("First Name"), rs.getString("last Name"), rs.getString("Address"), rs.getString("City"), rs.getString("State"), rs.getString("Zip Code"), rs.getDate("Birth Date").toLocalDate(), rs.getDate("Orig Date").toLocalDate(), rs.getString("Ticket Balance"), rs.getDate("Area_code").toLocalDate(), rs.getString("ID"));
                data.add(newdata);

            }
        } catch (SQLException e) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, e.toString(), "errors");
        } finally {

        }

        return data;
    }



    
    private void resetFields() {
        c2T.setText("");
        c3T.setText("");
        noteT.setText("");
        hbox.setVisible(false);
        updateButton.setVisible(false);
    }
    
    private void enterKeyPressed(int whichOne) {
            final int cRow = cTable.getSelectionModel().getSelectedIndex();
            if (cRow + 1 == size) {
                cTable.getSelectionModel().select(0);
            } else {
            if (whichOne==1){
                cTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                cTable.getSelectionModel().selectNext();
                cTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            } else {
                cTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                cTable.getSelectionModel().selectPrevious();
                cTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            }
            }
            
    }
    
    
    private void setKeyCodes1() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         //if (ke.getCode() == KeyCode.F3) {keyListener(ke);ke.consume();}
         //if (ke.getCode() == KeyCode.F6) {keyListener(ke);ke.consume();}
         //if (ke.getCode() == KeyCode.DOWN) {keyListener(ke);ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke);ke.consume();}
         if (ke.getCode() == KeyCode.ENTER) {keyListener(ke);ke.consume();}
         //if (ke.getCode() == KeyCode.UP) {keyListener(ke);ke.consume();}
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
                    case DOWN: enterKeyPressed(1);break;
                    case UP: enterKeyPressed(2);break;
                default:
                    break;
                }
}
     
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
        try {
            if (conn != null)
                conn.close();
            if (st != null)
                st.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(BdaysNextMonthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX().titleBar);
        stageV.close();
    }



}
