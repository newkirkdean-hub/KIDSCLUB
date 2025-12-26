package views.clubmail;

import JavaMail.Mail_JavaFX;
import JavaMail.Mail_JavaFX1;
import commoncodes.EmailExtractor;
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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Writer;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import models.club.DB;
import models.clubmail.ClubEmails;
import models.club.rCeipts;
import reports.clubmail.XLSEmailReports;
import sceneChangerFX.SceneChanger_Main;
import views.counterPopUp.EmailEditorPopUpController;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class EmailsController implements Initializable {
    @FXML private Pane root;
    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Button countButton;
    @FXML private Button printLabelsButton;
    @FXML private Button twoCleanButton;
    @FXML private Button updateButton;
    @FXML private Button fillLastUsedButton;
    @FXML private Button fillEmails;
    @FXML private Button badEmails;
    @FXML private TextField searchField;
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
    
    @FXML private TableView<ClubEmails> cTable;
    @FXML private TableColumn<ClubEmails, String> C1;
    @FXML private TableColumn<ClubEmails, String> C2;
    @FXML private TableColumn<ClubEmails, String> C3;
    @FXML private TableColumn<ClubEmails, String> C4;
    @FXML private TableColumn<ClubEmails, String> C5;
    @FXML private TableColumn<ClubEmails, String> C55;
    @FXML private TableColumn<ClubEmails, String> C6;
    @FXML private TableColumn<ClubEmails, String> C7;
    @FXML private TableColumn<ClubEmails, String> C8;
    @FXML private TableColumn<ClubEmails, LocalDate> C9;
    @FXML private TableColumn<ClubEmails, Integer> C10;
    
    
    
    private static final String EMAIL_SUBJECT = "Error in CLUBMAIL Bdays Next Month Controller";
    private static final DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static employeeFX eFX;
    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    public static messageBox mBox;
    public static DB db;
    public static String cssPath;
    public static ArrayList<rCeipts> Receipts;
    ArrayList<String> nextEmail;
    
    private static ContextMenu contextMenu = new ContextMenu();
    private static Connection conn = null, connEmailDB = null;
    private static PreparedStatement pst = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    private static Bounds boundsInScene;
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
            Logger.getLogger(EmailsController.class.getName()).log(Level.SEVERE, null, ex);
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
            return new TableCell<ClubEmails, LocalDate>() {
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
                searchField.requestFocus();
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
        new messageBox().showError("Email Count " + cTable.getItems().size(), errorLabel, errorBar);
        //C2.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    public void countButtonPushed() {
        ObservableList selectedItems = cTable.getSelectionModel().getSelectedItems();
        new messageBox().showError("The items selected in the table total: " + selectedItems.size(), errorLabel, errorBar);

    }

    public void buildMenuButton() {
        MenuItem address = new MenuItem(" Create CSV File");
        MenuItem addressCCN = new MenuItem(" Address W/ CCN's ");
        //MenuItem add = new MenuItem("  ");

        //Menu dropEdits = new Menu(" Drop Edits ");
        MenuItem csItem1 = new MenuItem(" Parties ");
        MenuItem csItem2 = new MenuItem(" Introduced ");
        MenuItem csItem3 = new MenuItem(" Party Times ");
        MenuItem csItem4 = new MenuItem(" Room Number ");
        //dropEdits.getItems().addAll(csItem1, csItem2, csItem3, csItem4);
        ObservableList<ClubEmails> list = cTable.getItems();
        address.setOnAction((ActionEvent event) -> {
            DumpData(list);
        });
        addressCCN.setOnAction((ActionEvent event) -> {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "WAIT", "This menu item has no code");
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

    public void DumpData(ObservableList<ClubEmails> list) {

    Writer writer = null;
    try {
        File file = new File(new dbStringPath().pathNameLocal + "MassEmailList.csv");
        writer = new BufferedWriter(new FileWriter(file));
        for (ClubEmails person : list) {

            String text = person.getNameF() + "," + person.getNameL() + "," + person.getAddress() + "\n";



            writer.write(text);
        }
    } catch (IOException ex) {
        new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, EmailsController.class.getName() + "Error" + ex, "errors");
    }
    finally {

             try {
                 writer.flush();
                 writer.close();
             } catch (IOException ex) {
                 new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, EmailsController.class.getName() + "Error" + ex, "errors");
             }
    } 



    }

    public void DumpDataCCN(ObservableList<ClubEmails> list) {
        XLSEmailReports XRW = new XLSEmailReports();
        try {
            //errorLabel.setText("");
            XRW.collectionListReportCNN(list);
        } catch (IOException | SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
    }

    public void RunSearchNewMembers() {
        //need to empty table here.
        //new IsItANumber().validDate(Date1, "The first Date field is empty");
        //new IsItANumber().validDate(Date2, "The second Date field is empty");
        cTable.getItems().clear();
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE * FROM Emails";
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
        int bday = Date1.getValue().getMonthValue();
        int bdayY = Date1.getValue().getYear();
        System.out.println("333333333333333333333 " + bday + "  " + bdayY);
        ClubEmails m = new ClubEmails(Date1.getValue(), Date1.getValue());
        try {
            System.out.println("4444444444");
            m.RunSearchNewMembers(bday, bdayY);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        getTableItems();
        cTable.setVisible(true);
    }

    public void RunSearchAllEmails() {
        //need to empty table here.
        //new IsItANumber().validDate(Date1, "The first Date field is empty");
        //new IsItANumber().validDate(Date2, "The second Date field is empty");
        cTable.getItems().clear();
        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE * FROM Emails";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            System.out.println(ex);
        }
        cTable.getItems().clear();
        //getTableItems();
        cTable.refresh();
        //cTable.setVisible(false);
        int bday = Date1.getValue().getMonthValue();
        int bdayY = Date1.getValue().getYear();
        System.out.println("333333333333333333333 ");
        ClubEmails m = new ClubEmails(Date1.getValue(), Date1.getValue());
        System.out.println("4444444444");
        Platform.runLater(()->{
            try {
                m.getAllEmails();
        getTableItems();
        cTable.refresh();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });
        
        
        
        //cTable.setVisible(true);
    }
    
    
    public void RunSearch() {
        mBox.showErrorClear(errorLabel, errorBar);
        new IsItANumber().validDate(Date1, "The first Date field is empty");
        cTable.getItems().clear();
        try {
            //conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE * FROM Emails";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        ClubEmails m = new ClubEmails(Date1.getValue(), Date1.getValue());
        int bday = Date1.getValue().getMonthValue();
        //System.out.println(bday);
        try {
            m.RunSearchBdays(bday);
        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
        cTable.getItems().clear();
        getTableItems();
        mBox.showError("Email Search is Complete " + cTable.getItems().size(), errorLabel, errorBar);
    }

    public void deleteMailLabel() {
        mBox.showErrorClear(errorLabel, errorBar);

        try {
            conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            String sql = "DELETE *\n"
                    + "FROM Emails\n"
                    + "WHERE (((Emails.ID)='" + tableRowSelected() + "'));";
            // JOptionPane.showMessageDialog(null, sql);

            pst = conn.prepareStatement(sql);
            pst.executeUpdate();

        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        getTableItems();

    }

    public void del() {
        ObservableList<ClubEmails> selectedItems = cTable.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems.size());
        ArrayList<String> selectedIDs = new ArrayList<>();
        for (ClubEmails row : selectedItems) {
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
                        + "FROM Emails\n"
                        + "WHERE (((Emails.ID)='" + iterator.next() + "'));";

                pst = conn.prepareStatement(sql);
                pst.executeUpdate();

            } catch (SQLException ex) {
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
            }
        }
        if (searchField.getText().length() > 0) {
            searchField.requestFocus(); 
            searchField.clear();
            searchField.requestFocus();
        } else {
            getTableItems();
        }

    }

    public void delFromDbase() {
        ObservableList<ClubEmails> selectedItems = cTable.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems.size());
        ArrayList<String> selectedIDs = new ArrayList<>();
        for (ClubEmails row : selectedItems) {
            selectedIDs.add(row.getCID());
        }

        try {
            connEmailDB = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberEmails.accdb;immediatelyReleaseResources=true");
        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        ListIterator<String> iterator = selectedIDs.listIterator();
        while (iterator.hasNext()) {
            try {
                //String sqlBadEmail = "UPDATE Email SET Email.[First Name] = ?, Email.[Last Name] = ? WHERE (((Email.[Customer ID])='" + iterator.next() + "'));";
                String sql = "DELETE * FROM Email WHERE (((Email.[Customer ID])='" + iterator.next() + "'));";
                System.out.println(sql);
                pst = connEmailDB.prepareStatement(sql);
                pst.executeUpdate();

            } catch (SQLException ex) {
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
            }
        }
        del();

    }
    
    
    
    public void TableClicked(MouseEvent me) throws IOException {
        if (me.getClickCount() == 2) {
        ObservableList<ClubEmails> selectedItems = cTable.getSelectionModel().getSelectedItems();
        TablePosition pos = cTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        System.out.println(selectedItems.get(0).getCCN());
        FXMLLoader fxmlLoader = new FXMLLoader();
        EmailEditorPopUpController wController = (EmailEditorPopUpController) fxmlLoader.getController();
        wController.typeVar = "3";
        wController.empID = String.valueOf(eFX.getEmpID());
        wController.CCN = selectedItems.get(0).getCCN();
        wController.db = db;
        wController.dbsp = dbsp;
        wController.sc = sc;
        wController.mBox = mBox;
        wController.cssPath = cssPath;
        wController.rCeipts = Receipts;
        sc.getStagestyleUndecorated(null, "/views/counterPopUp/EmailEditor.fxml", null, null, 220.00, 220.0);
          ;
        }
    }
    
       
    
    

    public void BadEmail() {
        //int whichone = new messageBox().confirmMakeThisChange(Alert.AlertType.ERROR, null, "Question", "Are you useing the badEmailTextFile?");
        int whichone = new messageBox().confirmMakeThisChangeButtonTitles(Alert.AlertType.ERROR, null, "Question", "Are you using the badEmailTextFile?", "YES", "NO");
        if (whichone == 1) {
        try(
            FileInputStream inputStream = new FileInputStream(dbsp.pathNameExcel + "badEmailsTextFile.txt")) {  
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream)); 
            String badEmailsfromTextFile = br.readLine();
        
            
            nextEmail = new EmailExtractor().findEmails(badEmailsfromTextFile);
            //System.out.println("2 " + nextEmail.getFirst());
            //System.out.println("3 " + nextEmail.getLast());
            //System.out.println("3 " + nextEmail.size());
        //new messageBox().showAlert(Alert.AlertType.ERROR, null, "Here", "We are this far 1 " + dbsp.pathNameExcel + "badEmailsTextFile.txt");

        }catch (FileNotFoundException ex) {
            System.out.println(ex);
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        } catch (IOException ex) {
            System.out.println(ex);
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        
        try {
            connEmailDB = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberEmails.accdb;immediatelyReleaseResources=true");
        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
        String nextEmailListfortheEmail = null;
        ListIterator<String> iterator = nextEmail.listIterator();
        //new messageBox().showAlert(Alert.AlertType.ERROR, null, "Here", "We are this far 1 " + dbsp.pathNameExcel + "badEmailsTextFile.txt");
        while (iterator.hasNext()) {
            String thisone =  iterator.next();
            try {
                String sqlBadEmail = "UPDATE Email SET Email.[First Name] = ?, Email.[Last Name] = ? WHERE (((Email.E_Mail)='" + thisone + "'));";
                //String sql = "DELETE * FROM Email WHERE (((Email.[Customer ID])='" + iterator.next() + "'));";
                System.out.println(sqlBadEmail);
                pst = connEmailDB.prepareStatement(sqlBadEmail);
                pst.setString(1, "BAD");
                pst.setString(2, "EMAIL");

                pst.executeUpdate();
                nextEmailListfortheEmail += "\n" + thisone;
            } catch (SQLException ex) {
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
            }
        }
        new Mail_JavaFX().SendMail2("Bad Email List", nextEmailListfortheEmail, "newkirkdean@gmail.com");
        new messageBox().showAlert(Alert.AlertType.ERROR, null, "Bad Email List Complete", nextEmailListfortheEmail);
        
        } else {

        
        
        
        
        
        
        
       
        //FROM HERE DOWN IS WHET HAPPENS TO MAKE IT A BAD EMAIL.
        ObservableList<ClubEmails> selectedItems = cTable.getSelectionModel().getSelectedItems();
        System.out.println(selectedItems.size());
        ArrayList<String> selectedIDs = new ArrayList<>();
        for (ClubEmails row : selectedItems) {
            selectedIDs.add(row.getCID());
        }

        try {
            connEmailDB = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberEmails.accdb;immediatelyReleaseResources=true");
        } catch (SQLException ex) {
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        ListIterator<String> iterator = selectedIDs.listIterator();
        while (iterator.hasNext()) {
            try {
                String sqlBadEmail = "UPDATE Email SET Email.[First Name] = ?, Email.[Last Name] = ? WHERE (((Email.[Customer ID])='" + iterator.next() + "'));";
                //String sql = "DELETE * FROM Email WHERE (((Email.[Customer ID])='" + iterator.next() + "'));";
                System.out.println(sqlBadEmail);
                pst = connEmailDB.prepareStatement(sqlBadEmail);
                pst.setString(1, "BAD");
                pst.setString(2, "EMAIL");

                pst.executeUpdate();

            } catch (SQLException ex) {
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
            }
        }
        del();
            }
    }

    public void searchRecord(KeyEvent ke) throws SQLException {

        FilteredList<ClubEmails> filterData = new FilteredList<>(getdataB(), p -> true);
        searchField.textProperty().addListener((obsevable, oldvalue, newvalue) -> {
            filterData.setPredicate(pers -> {
                if (newvalue == null || newvalue.isEmpty()) {
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if (pers.getAddress().toLowerCase().contains(typedText)) {

                    return true;
                }
                if (pers.getCCN().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getNameF().toLowerCase().contains(typedText)) {
                    return true;
                }
                if (pers.getNameL().toLowerCase().contains(typedText)) {

                    return true;
                }

                return false;
            });
            SortedList<ClubEmails> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(cTable.comparatorProperty());
            cTable.setItems(sortedList);

        });

    }

    public void QuickClean() {
        //Platform.runLater(()->mb.showErrorClear(errorLabel, errorBar));
        Platform.runLater(() -> mBox.showError("Cleaning BAD EMAIL", errorLabel, errorBar));
        try {
            String sql = "DELETE * \n"
                    + "FROM Emails\n"
                    + "WHERE (((Emails.[First Name])=\"BAD\")) AND (((Emails.[Last Name])=\"EMAIL\"));";
            System.out.println("1 " + sql);
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }

        //getTableItems();
        /*
        try {
            //conn = (Connection) DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            System.out.println("2");
            String sql = "DELETE * \n"
                    + "FROM Emails\n"
                    + "WHERE (((Emails.Address) Is Null)) OR (((Emails.Address)=\"\"))  OR (((Emails.Address)=\"n/a\")) OR (((Emails.State) Is Null)) OR (((Emails.[Zip Code]) Is Null));";
            pst = conn.prepareStatement(sql);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
        System.out.println("3");
        */
        Platform.runLater(()->mBox.showError("Quick Clean Complete, Wrong Address - Close - Empty Address, City State or Zip Code", errorLabel, errorBar));
        Platform.runLater(()->cTable.getItems().clear());
        Platform.runLater(()->getTableItems());

    }

    public void twoCleanButtonPushed() {
        checkTwoYearDetail();
        cTable.getItems().clear();
        getTableItems();
    }

    public void FillLastUsed() {
        BdaysNextMonth m = new BdaysNextMonth(Date1.getValue(), Date1.getValue());
        ObservableList<ClubEmails> list = cTable.getItems();
        //DBV d = new DBV();
        if (list.size() > 0) {

            m.dbconnect();
            list.forEach((ClubEmails each) -> {
                try {
                    if (new DB().isMemberValid(each.getCCN())) {
                        System.out.println("Customer " + each.getCCN() + " Date " + m.getDate() + "CheckDetail TRUE");
                        System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                        BdaysNextMonth mm = new DB().getMemberEmails(each.getCCN());
                        insertDate(each.getCCN(), mm.getBdate(), mm.getCity(), mm.getState(), mm.getZip(), String.valueOf(mm.getBalance()));
                        m.setDate(new GetDay().asSQLDate(LocalDate.of(12, Month.MARCH, 3)));
                    /*} else {
                        if (m.checkDetailInactive(each.getCID())) {
                            System.out.println("Customer " + each.getCCN() + " Date " + m.getDate() + "CheckInactiveDetail TRUE");
                            System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                            insertDate(each.getCCN(), m.getDate());
                            m.setDate(new GetDay().asSQLDate(LocalDate.of(12, Month.MARCH, 3)));
                        }*/
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

    public void FillEmails() {
        ClubEmails m = new ClubEmails(Date1.getValue(), Date1.getValue());
        ObservableList<ClubEmails> list = cTable.getItems();

        if (list.size() > 0) {

            m.dbconnect();
            list.forEach((ClubEmails each) -> {
                try {
                    if (m.checkEmails(each.getCID())) {
                        System.out.println("Customer " + each.getCCN() + " Email: " + m.getAddress() + "CheckDetail TRUE");
                        //System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                        insertEmails(each.getCCN(), m.getAddress());
                        m.setAddress("");
                    } else {
                        if (m.checkDetailInactive(each.getCID())) {
                            System.out.println("Customer " + each.getCCN() + " Date " + m.getDate() + "CheckInactiveDetail TRUE");
                            //System.out.println("Customer " + each.getCCN() + " Date " + m.getDate());
                            insertEmails(each.getCCN(), m.getAddress());
                            m.setAddress("");
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
        ObservableList<ClubEmails> list = cTable.getItems();

        if (list.size() > 0) {

            m.dbconnect();
            list.forEach((ClubEmails each) -> {
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

    private void insertDate(String CCN, LocalDate ldate, String c, String s, String z, String t) {

        try {

            String sql = "UPDATE Emails SET Emails.Area_Code = ?, Emails.City = ?, Emails.State = ?, Emails.[Zip Code] = ?, Emails.[Ticket Balance] = ? WHERE (((Emails.[Customer Card Number])='" + CCN + "'));";
            pst = conn.prepareStatement(sql);
            pst.setDate(1, new GetDay().asSQLDate(ldate));
            pst.setString(2, c);
            pst.setString(3, s);
            pst.setString(4, z);
            pst.setString(5, t);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("error in New Member Mail putNewMember " + ex);
        }

    }

    private void insertEmails(String CCN, String eAddress) {

        try {

            String sql = "UPDATE Emails SET Emails.Address = ? WHERE (((Emails.[Customer Card Number])='" + CCN + "'));";
            pst = conn.prepareStatement(sql);
            pst.setString(1, eAddress);
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
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, ex.toString(), "errors");
        }
    }

    public String tableRowSelected() {
        String stt = null;
        TablePosition pos = cTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        stt = cTable.getItems().get(row).getCID();
        return stt;
    }

    public ObservableList<ClubEmails> getdataB() throws SQLException {
        ObservableList<ClubEmails> data = FXCollections.observableArrayList();

        try {
            //conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbsp.pathNameClubDBs + "MemberMail.accdb;immediatelyReleaseResources=true");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Emails.[Customer Card Number], Emails.[Customer ID], Emails.[First Name], Emails.[Last Name], Emails.[Address], Emails.[City], Emails.[State], Emails.[Zip Code], Emails.[Birth Date], Emails.[Orig Date], Emails.[Ticket Balance], Emails.[Area_code], Emails.[ID] FROM Emails");
            while (rs.next()) {
                //System.out.println(rs.getDate("Birth Date")); 

                ClubEmails newdata = new ClubEmails(rs.getString("Customer Card Number"), rs.getString("Customer ID"), rs.getString("First Name"), rs.getString("last Name"), rs.getString("Address"), rs.getString("City"), rs.getString("State"), rs.getString("Zip Code"), rs.getDate("Birth Date").toLocalDate(), rs.getDate("Orig Date").toLocalDate(), rs.getString("Ticket Balance"), rs.getDate("Area_code").toLocalDate(), rs.getString("ID"));
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
            if (whichOne == 1) {
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
            if (ke.getCode() == KeyCode.F3) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.TAB) {
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
            //if (ke.getCode() == KeyCode.UP) {keyListener(ke);ke.consume();}
        });
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                break;
            case F2:
                break;
            case F3:
                if (searchField.getText().length() > 1) {
                    System.out.println("here here");
                    badEmails.fire();
                }
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
            case TAB: 
                if (searchField.getText().length() > 1){
                    cTable.requestFocus();
                    cTable.scrollTo(0);
                };
                break;
            case ENTER:
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case DOWN:
                enterKeyPressed(1);
                break;
            case UP:
                enterKeyPressed(2);
                break;
            default:
                break;
        }
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        try {
            if (conn != null) {
                conn.close();
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX().titleBar);
        stageV.close();
    }

}
