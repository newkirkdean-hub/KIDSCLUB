package views.clubmail;

import JavaMail.Mail_JavaFX1;
import XML_Code.readXMLToArrayList;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import models.clubmail.NewMemberMail;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import models.club.DB;
import pWordFX.employeeFX;
import reports.clubmail.XLSNewMembersLastMonth;
import reports.clubmail.XLSNewMembersLastMonthCCN;
import sceneChangerFX.SceneChanger_Main;
import views.counterPopUp.historyViewerController;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class NewMemberMailController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    //@FXML private Button colorsButton;
    @FXML private Button updateButton;
    @FXML private Button printLabelsButton;
    @FXML private Button closedLockedButton;
    @FXML private Button detailCloedLockedButton;
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
    
    @FXML private TableView<NewMemberMail> cTable;
    @FXML private TableColumn<NewMemberMail, String> C1;
    @FXML private TableColumn<NewMemberMail, String> C2;
    @FXML private TableColumn<NewMemberMail, String> C3;
    @FXML private TableColumn<NewMemberMail, String> C4;
    @FXML private TableColumn<NewMemberMail, String> C5;
    @FXML private TableColumn<NewMemberMail, String> C55;
    @FXML private TableColumn<NewMemberMail, String> C6;
    @FXML private TableColumn<NewMemberMail, String> C7;
    @FXML private TableColumn<NewMemberMail, Integer> C8;
    
    //public static employeeFX eFX = new employeeFX();
    //public static SceneChanger_Main sc;
    //public static settingsFXML sg; 
    public static dbStringPath dbsp;
    public static String cssPath;
    public static messageBox mBox;
    public static Mail_JavaFX1 jmail;
    public static SceneChanger_Main sc;
    public static FXMLLoader fxloader;
    public static DB db;
    public static employeeFX EFX;
    
    private static boolean closedLocked = false;
    private static Connection conn = null;
    private static PreparedStatement pst = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static Bounds boundsInScene;
    private static final String EMAIL_SUBJECT = "Error in CLUBMAIL New Member Mail Controller";
    //private static LocalDate dt;
    ContextMenu contextMenu = new ContextMenu();
    private static NewMemberMail newMMail;
    private static int size=0;
    private static final LocalDate DATEFIRSTDAY = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    private static final LocalDate DATELASTDAY = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = printLabelsButton.localToScene(printLabelsButton.getBoundsInLocal());
        dbsp.setName();
        buildMenuButton();
        root.getStylesheets().add(cssPath);
        Date1.setValue(DATEFIRSTDAY);
        Date2.setValue(DATELASTDAY);
        cTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setKeyCodes();
        C1.setCellValueFactory(new PropertyValueFactory<>("CCN"));
        C2.setCellValueFactory(new PropertyValueFactory<>("Ncombined"));
        C3.setCellValueFactory(new PropertyValueFactory<>("Address"));
        C4.setCellValueFactory(new PropertyValueFactory<>("City"));
        C55.setCellValueFactory(new PropertyValueFactory<>("State"));
        C5.setCellValueFactory(new PropertyValueFactory<>("Zip"));
        C6.setCellValueFactory(new PropertyValueFactory<>("Odate"));
        C7.setCellValueFactory(new PropertyValueFactory<>("Balance"));
        C8.setCellValueFactory(new PropertyValueFactory<>("CID"));
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
        mBox.showError("New Members Last Month " + cTable.getItems().size(), errorLabel, errorBar);

    }

    private void getTableItems() {
        cTable.getItems().clear();
        try {
            cTable.getItems().addAll(getdataB());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        cTable.setEditable(true);
        mBox.showError("New Members Last Month " + cTable.getItems().size(), errorLabel, errorBar);
        //C2.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void RunSearch() {
        //need to empty table here.
        new IsItANumber().validDate(Date1, "The first Date field is empty");
        new IsItANumber().validDate(Date2, "The second Date field is empty");
        cTable.getItems().clear();
        closedLocked = false;
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE * FROM NewMember";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            System.out.println(ex);
        }
        cTable.getItems().clear();
        getTableItems();
        cTable.refresh();
        cTable.setVisible(false);
        NewMemberMail m = new NewMemberMail(Date1.getValue(), Date2.getValue());
        try {
            m.RunSearch();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        getTableItems();
        cTable.setVisible(true);
    }

    public String tableRowSelected() {
        String tr = null;
        TablePosition pos = cTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        tr = cTable.getItems().get(row).getCID();
        //JOptionPane.showMessageDialog(null, st);
        return tr;
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
        ObservableList<NewMemberMail> list = cTable.getItems();
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
    
    
    
    
    
    
    
    
    public void deleteMailLabel() {

        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE *\n"
                    + "FROM NewMember\n"
                    + "WHERE (((NewMember.ID)='" + tableRowSelected() + "'));";
            // JOptionPane.showMessageDialog(null, sql);

            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        getTableItems();

    }

    public void del() {
        ObservableList<NewMemberMail> selectedItems = cTable.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems.size());
        ArrayList<String> selectedIDs = new ArrayList<>();
        for (NewMemberMail row : selectedItems) {
            selectedIDs.add(row.getCID());
        }

        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        ListIterator<String> iterator = selectedIDs.listIterator();
        while (iterator.hasNext()) {
            try {
                String sql = "DELETE *\n"
                        + "FROM NewMember\n"
                        + "WHERE (((NewMember.ID)='" + iterator.next() + "'));";

                pst = conn.prepareStatement(sql);
                pst.executeUpdate();

            } catch (SQLException ex) {
                System.out.println(ex);
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
            }
        }
        getTableItems();

    }

    public void tableItemSelected() {
    newMMail  = cTable.getSelectionModel().getSelectedItem();
    //c1L.setText(itemSelected.getC1());
    //c2T.setText(itemSelected.getC2());
    //c3T.setText(itemSelected.getC3());
    //c4T.setText(itemSelected.getC4());
    //c5T.setText(itemSelected.getC5());
    //noteT.setText(itemSelected.getC6());
    //tableID = itemSelected.getC7();
    //updateButton.setVisible(true);
    //hbox.setVisible(true);  */

    }















    
    public void getTableItemsForClosedLocked() {
        cTable.getItems().clear();
        cTable.getItems().addAll(getClosedLocked());
        cTable.setEditable(true);
        mBox.showError("New Members Last Month " + cTable.getItems().size(), errorLabel, errorBar);
        

    }
    
    
    
    private ObservableList<NewMemberMail> getClosedLocked() {
        ObservableList<NewMemberMail> closedLockedData = FXCollections.observableArrayList();
        
        closedLockedData = new readXMLToArrayList().getClosedLockedCardDataXML();
        

        return closedLockedData;
    }
    
    
    

        public void getFullDetailsLockedClosed() {
            historyViewerController wController = (historyViewerController) fxloader.getController();
            wController.sc = sc;
            wController.dbsp = dbsp;
            wController.db = db;
            wController.mbox = mBox;
            wController.efx = EFX;
            wController.jmail = jmail;
            wController.memID = newMMail.getCID();
            wController.cName = newMMail.getNameF() + " " + newMMail.getNameL();
            wController.csspath = cssPath;
            try {
                sc.getpassWord(null, "/views/counterPopUp/historyViewer.fxml", newMMail.getCID(), cssPath, 300.0, 50.0);
            } catch (IOException ex) {
                System.out.println(ex);
            }
    }
    


















    

    public ObservableList<NewMemberMail> getdataB() throws SQLException {
        ObservableList<NewMemberMail> data = FXCollections.observableArrayList();
        int i = 0;
        
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            String sql = "SELECT NewMember.[Customer Card Number], NewMember.[First Name], NewMember.[Last Name], NewMember.[Address], NewMember.[City], NewMember.[State], NewMember.[Zip Code], NewMember.[Orig Date], NewMember.[Ticket Balance], NewMember.[Customer ID] FROM NewMember";
            rs = st.executeQuery("SELECT NewMember.[Customer Card Number], NewMember.[First Name], NewMember.[Last Name], NewMember.[Address], NewMember.[City], NewMember.[State], NewMember.[Zip Code], NewMember.[Orig Date], NewMember.[Ticket Balance], NewMember.[ID] FROM NewMember");
            while (rs.next()) {
                i++;

                NewMemberMail newdata = new NewMemberMail(rs.getString("Customer Card Number"), rs.getString("First Name"), rs.getString("last Name"), rs.getString("Address"), rs.getString("City"), rs.getString("State"), rs.getString("Zip Code"), rs.getDate("Orig Date").toLocalDate(), rs.getString("Ticket Balance"), rs.getString("ID"));
                data.add(newdata);

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            jmail.sendEmailTo(EMAIL_SUBJECT, e.toString(), "errors");
        } finally {
            if (st != null){
            st.close();}
            if (rs != null){
            rs.close();}
            if (conn != null){
            conn.close();}
        }

        return data;
    }
    
    
    


    public void DumpData(ObservableList<NewMemberMail> list) {
        XLSNewMembersLastMonth XRW = new XLSNewMembersLastMonth();
        try {
            //errorLabel.setText("");
            XRW.collectionListReport(list);
        } catch (IOException | SQLException ex) {
            System.out.println(ex);
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
    }
    
    public void DumpDataCCN(ObservableList<NewMemberMail> list) {
        XLSNewMembersLastMonthCCN XRW = new XLSNewMembersLastMonthCCN();
        try {
            //errorLabel.setText("");
            XRW.collectionListReportCCN(list);
        } catch (IOException | SQLException ex) {
            System.out.println(ex);
            jmail.sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
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
    

    private void resetFields() {
        c2T.setText("");
        c3T.setText("");
        noteT.setText("");
        hbox.setVisible(false);
        updateButton.setVisible(false);
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F3) {keyListener(ke);ke.consume();}
            if (ke.getCode() == KeyCode.F6) {keyListener(ke);ke.consume();}
            if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke);ke.consume();}
            //if (ke.getCode() == KeyCode.DOWN) {keyListener(ke);ke.consume();}
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
            case F4: ;
                break;
            case F5: ;
                break;
            case F6:
                break;
            case F7:
                break;
            case F8:
                break;
            case F9: ;
                break;
            case F10: ;
                break;
            case F11: ;
                break;
            case F12: ;
                break;
            case ENTER:
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case UP:
                enterKeyPressed(1);
                break;
            case DOWN:
                enterKeyPressed(2);
                break;
            default:
                break;
        }
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX().titleBar);
        stageV.close();
    }

}
