package views.timeclock;



import Css.cssChanger;
import JavaMail.Mail_JavaFX;
import JavaMail.Mail_JavaFX1;
import XML_Code.writeArrayListToXMLFile;
import reports.timeclock.ReportsE;
import pWordFX.employeeFX;
import commoncodes.ClubFunctions;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.GetReceipts;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.P;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import messageBox.messageBox;
import models.club.rCeipts;
import models.timeclock.EmpDocs;
import models.timeclock.EmpFileFX;
import models.timeclock.EmpFileFXDB;
import models.timeclock.EmpFileFXDetail;
import pWordFX.empFX;
import reports.timeclock.EmpActivity;
import sceneChangerFX.SceneChanger_Main;
//import static views.MemDepositFXController.FXLOADER;

public class EmpViewerController implements Initializable {
    @FXML private Pane root;
    @FXML private Pane EmployeePane;
    @FXML private Pane NotesPane;
    @FXML private Pane TrainingPane;
    @FXML private TabPane EmployeeTabPane;
    
    @FXML private VBox vbox1;
    @FXML private Label errorLabel;
    @FXML private Pane errorPane;
    
    @FXML private TextField tfEmpNumber;
    @FXML private TextField tfFName;
    @FXML private TextField tfMName;
    @FXML private TextField tfFLName;
    @FXML private TextField tfLName;
    @FXML private TextField tfAddress;
    @FXML private TextField tfCity;
    @FXML private TextField tfState;
    @FXML private TextField tfZip;
    @FXML private TextField tfPhoneHome;
    @FXML private TextField tfPhoneCell;
    @FXML private TextField tfSocSec;
    @FXML private TextField voucher;
    @FXML private TextField tEmailAddress;
    
    @FXML private TextField dropBoxLocation;
    @FXML private TextField dropBoxStatus;
    @FXML private TextField dropBoxWageStatus;
    @FXML private TextField dropBoxShirtSize;
    @FXML private TextField dropBoxTitle;
    @FXML private TextField dropBoxGender;
    @FXML private TextField tfHireDate;
    @FXML private TextField lastUpdate;
    
    @FXML private DatePicker dpBDate;
    
    @FXML private RadioButton intercardYES;
    @FXML private RadioButton intercard1;
    @FXML private RadioButton intercard2;
    @FXML private RadioButton intercard3;
    @FXML private RadioButton intercardNO;
    @FXML private RadioButton eFilesYES;
    @FXML private RadioButton eFilesNO;
    @FXML private RadioButton corpYES;
    @FXML private RadioButton corpNO;
    @FXML private RadioButton reservations1;
    @FXML private RadioButton reservations2;
    @FXML private RadioButton reservations3;
    @FXML private RadioButton cDepositsYES;
    @FXML private RadioButton cDepositsNO;
    @FXML private RadioButton bCarsYES;
    @FXML private RadioButton bCarsNO;
    @FXML private RadioButton active1;
    @FXML private RadioButton active2;
    @FXML private RadioButton active3;
    @FXML private RadioButton active4;
    @FXML private RadioButton gProblems1;
    @FXML private RadioButton gProblems2;
    @FXML private RadioButton gProblems3;
    @FXML private RadioButton tClockYES;
    @FXML private RadioButton tClockNO;    
    
    @FXML private TableView<EmpFileFXDetail> detailTable;
    @FXML private TableColumn<EmpFileFXDetail, String> cPurpose;
    @FXML private TableColumn<EmpFileFXDetail, String> cPoints;
    @FXML private TableColumn<EmpFileFXDetail, String> cDepartment;
    @FXML private TableColumn<EmpFileFXDetail, Double> cPayRate;
    @FXML private TableColumn<EmpFileFXDetail, String> cMGR;
    @FXML private TableColumn<EmpFileFXDetail, LocalDate> cDate;
    @FXML private TableColumn<EmpFileFXDetail, String> cTime;
    @FXML private TableColumn<EmpFileFXDetail, String> cNotes;
    
    
    @FXML private Button newRecButton;
    @FXML private Button moreButton;
    @FXML private Button searchButton;
    @FXML private Button editButton;
    @FXML private Button cancelButton;
    @FXML private Button DocsButton;
    @FXML private GridPane gridPane1;
    @FXML private GridPane gridPane3;
    @FXML private Pane gridPane2;
    @FXML private static String detailID;
    @FXML private static String staticMemberID;
    @FXML ImageView imageView;
    
    @FXML private ToggleGroup InterCardToggleGroup, EFilesTGroup, CorpDataTGroup, ReservationsTGroup, CorpDepositsTGroup, BCarsSalesTGroup, ActiveStatusTGroup, GProblemsTGroup, TimeClockTGroup;
    
    public static String cssPath;
    public static SceneChanger_Main sc;
    public static FXMLLoader fxmlLoader;
    public static employeeFX eFX;
    public static dbStringPath dbsp;
    public static String MGR;
    public static ArrayList<rCeipts> Receipts;

    public static Boolean editMode = false, newRec =false, runDimFile=false;
    ContextMenu contextMenu = new ContextMenu();
    private static Bounds boundsInScenememButton,boundsInScene4;
    private static Bounds boundsInScenememMoreButton;
    public static Stage stageV;
    public static int whichOne = 0, beenhereonce=0;
    private static EmpFileFX E = null, d = null;
    private static EmpFileFXDetail g = null, ht = null;
    private static EmpFileFXDB db = null;
    public static String ownerEmail;
    private static empFX newEFX = null;
    private static final NumberFormat $format = DecimalFormat.getCurrencyInstance();
    private static String tMessage = "", detailTableID;
    public static ArrayList<EmpFileFX> updateList;
    private static SingleSelectionModel<Tab> selectionModel;
    private static final java.text.SimpleDateFormat tdf = new java.text.SimpleDateFormat("hh:mm a");
    private static final java.text.SimpleDateFormat ddf = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static FileInputStream inputstream = null;
    private static String empProfilePicture = "";
    /*
    NOTE: BCARS AND INTERCARD ARE NOW FOR SOMETHING ELSE
    BCARS IS FOR MEMBER FILES FOR THE SUPERVISORS
    INTERCARD IS FOR GT10K DEOPSITS AND EMPMSG's 3/24
    */


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScenememButton = root.localToScene(root.getBoundsInLocal());
        boundsInScenememMoreButton = moreButton.localToScene(moreButton.getBoundsInLocal());
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        dbsp.setName();
        buildMenuButton();
        selectionModel = EmployeeTabPane.getSelectionModel();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        db = new EmpFileFXDB();
        makeAllEdit(1);
        Tooltip tooltip = new Tooltip("Email Address"); 
        tEmailAddress.setTooltip(tooltip);
        FillRadioButtons();
        setToUpper();
        setAllRadios(1);
        editMode = false;
        new FocusedTextFieldHighlight().setHighlightListenerBdays(EmployeePane);
        getEmployee("1001");
        cPurpose.setCellValueFactory(new PropertyValueFactory<>("DPurpose"));
        cPoints.setCellValueFactory(new PropertyValueFactory<>("DPoints"));
        cDepartment.setCellValueFactory(new PropertyValueFactory<>("DDepartment"));
        cPayRate.setCellValueFactory(new PropertyValueFactory<>("DPayRate"));
        cPayRate.setCellFactory(column -> {
            return new TableCell<EmpFileFXDetail, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(item);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText($format.format(item));
                    }
                }
            };
        }); 
        cMGR.setCellValueFactory(new PropertyValueFactory<>("DMGR"));
        cDate.setCellValueFactory(new PropertyValueFactory<>("dDate"));
        cDate.setCellFactory(column -> {
            return new TableCell<EmpFileFXDetail, LocalDate>() {
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
        //.setCellValueFactory(new PropertyValueFactory<>("dDate"));
        cTime.setCellValueFactory(new PropertyValueFactory<>("DTime"));
        cNotes.setCellValueFactory(new PropertyValueFactory<>("DNotes"));
        Platform.runLater(()->{searchButton.requestFocus(); getEmpImage();});
    }    
   
    private void getEmployee(String ID) {
        E = new EmpFileFX(ID);
        try {
            db.Connect();
            if (db.isEmpValid(ID)) {
                E = db.getEmployee(ID);
                //System.out.println("New Employee number worked");
                tfEmpNumber.setText(E.getCCNMasked());
                tfFName.setText(E.getFName());
                tfMName.setText(E.getMName());
                tfLName.setText(E.getLName());
                tfFLName.setText(E.getFName() + " " + E.getLName());
                tfAddress.setText(E.getAddress());
                tfCity.setText(E.getCity());
                tfState.setText(E.getState());
                tfZip.setText(String.valueOf(E.getZip()));
                dpBDate.setValue(E.getBirthdate());
                tfHireDate.setText(E.getHireDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                tfPhoneHome.setText(E.getPhoneH());
                tfPhoneCell.setText(E.getPhoneC());
                tfSocSec.setText(E.getSocSec());
                lastUpdate.setText(E.getLastUpdate().toString());
                dropBoxLocation.setText(E.getLoc());
                dropBoxStatus.setText(E.getStat());
                dropBoxWageStatus.setText(E.getWStat());
                dropBoxShirtSize.setText(E.getShirt());
                dropBoxTitle.setText(E.getTitle());
                dropBoxGender.setText(E.getGender());
                voucher.setText(String.valueOf(E.getVoucher()));
                tEmailAddress.setText(E.getEmail());
                setRadioButtons(E);
                
                
                staticMemberID = E.getEid();
                
                try {
                    detailTable.getItems().clear();
                    detailTable.getItems().addAll(new EmpFileFXDB().getDetail(E.getEid()));
                } catch (SQLException ex) {
                    System.out.println(ex);
                }

                db.disConnect();
            } else {
                db.disConnect();
                new messageBox().showError("No Employee Number Found " + ID, errorLabel, errorPane);
                return;
            }
            db.disConnect();
            beenhereonce = 0;
        } catch (SQLException ex) {
            Logger.getLogger(EmpViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void newRecButtonPressed() throws IOException {
        
        if (selectionModel.getSelectedIndex() == 0) {
            newRec = true;
            //System.out.println(newRec);
            selectionModel.select(0);
            makeAllEdit(2);
            setNewFields(1);
            setNewFields(2);
            return;
        }
        if (selectionModel.getSelectedIndex() == 1) {
            ht = new EmpFileFXDetail("", "Games", MGR, null, "", "0", 0.0, staticMemberID, LocalDate.now());
            //dPurpose, String dDepartment, String dMGR, String dTime, String dNotes, String dPoints, String dPayRate, String dDetailID, LocalDate dDate
            EmpDetailPopUpController wController = (EmpDetailPopUpController) fxmlLoader.getController();
            //JOptionPane.showMessageDialog(null, "the id before the screen opens " + ht.getdDetailID());
            wController.empName = E.getFName() + " " + E.getLName();
            wController.updateVar = "0";
            wController.gg = ht;
            wController.css = cssPath;
            sc.getpassWord(stageV, "/views/timeclock/EmpDetailPopUp.fxml", detailID, tMessage, 300.0, 50.0);
            //sc.goToScene("EmpDetail", stageV, eFX.getNameF(), null, boundsInScenememButton);
            runDimFile = true;
            getEmployee(E.getENumber());
            return;
        }
        if (selectionModel.getSelectedIndex() == 2) {
            new messageBox().showError("New Button Cannot be Used in This Tab", errorLabel, errorPane);
        }
    }
    
    private void setNewFields(int whichOne) {
        switch(whichOne) {
            case 1:
                tfEmpNumber.setText("");
                tfFName.setText("");
                tfMName.setText("");
                tfLName.setText("");
                tfFLName.setText("");
                tfAddress.setText("");
                tfCity.setText("BOISE");
                tfState.setText("ID");
                tfZip.setText("83704");
                dpBDate.setValue(new GetDay().NOW_LOCAL_DATE());
                tfHireDate.setText(new java.sql.Date(System.currentTimeMillis()).toString());
                tfPhoneHome.setText("");
                tfPhoneCell.setText("");
                tfSocSec.setText("");
                lastUpdate.setText(new GetDay().NOW_LOCAL_DATE().toString());
                dropBoxLocation.setText("Location - POJOS FV");
                dropBoxStatus.setText("FULL TIME");
                dropBoxWageStatus.setText("HOURLY");
                dropBoxShirtSize.setText("Shirt Size- Large");
                dropBoxTitle.setText("EMPLOYEE");
                dropBoxGender.setText("FEMALE");
                voucher.setText("5000");
                tEmailAddress.setText("");
                break;
            case 2:
                //intercardYES.setDisable(true);
                intercard1.setDisable(true);
                intercard2.setDisable(true);
                intercard3.setDisable(true);
                intercardNO.setDisable(false);
                intercardNO.setSelected(true);
                eFilesYES.setDisable(true);
                eFilesNO.setDisable(false);
                eFilesNO.setSelected(true);
                corpYES.setDisable(true);
                corpNO.setDisable(false);
                corpNO.setSelected(true);
                reservations1.setDisable(true);
                reservations2.setDisable(true);
                reservations3.setDisable(false);
                //reservations3.setSelected(true);
                cDepositsYES.setDisable(true);
                cDepositsNO.setDisable(false);
                cDepositsNO.setSelected(true);
                bCarsYES.setDisable(true);
                bCarsNO.setDisable(false);
                bCarsNO.setSelected(true);
                active1.setDisable(false);
                active1.setSelected(true);
                active2.setDisable(true);
                active3.setDisable(true);
                active4.setDisable(true);
                gProblems1.setDisable(true);
                gProblems1.setSelected(true);
                gProblems2.setDisable(true);
                gProblems3.setDisable(true);
                tClockYES.setDisable(true);
                tClockNO.setDisable(true);
                tClockNO.setSelected(true);
                break;
        }
    }
    
    
    
    public void editButtonGo() {
        if (newRec) {
            AddNewRecord();
        } else {
            try {
                checkFirstRecord(E.getENumber());
            } catch (Exception e) {
                new messageBox().showError(e.toString(), errorLabel, errorPane);
                return;
            }
            try {
                if (!editMode) {
                    if (selectionModel.getSelectedIndex() == 0) {
                        makeAllEdit(2);
                    }
                    if (selectionModel.getSelectedIndex() == 1) {
                        ht = null;
                        searchButton.setDisable(true);
                        TablePosition pos = detailTable.getSelectionModel().getSelectedCells().get(0);
                        int row = pos.getRow();
                        detailTableID = detailTable.getItems().get(row).getdDetailID();
                        ht = detailTable.getSelectionModel().getSelectedItem();
                        EmpDetailPopUpController wController = (EmpDetailPopUpController) fxmlLoader.getController();
                        wController.empName = E.getFName() + " " + E.getLName();
                        wController.css = cssPath;
                        wController.gg = ht;
                        wController.updateVar = ht.getdDetailID();
                        wController.empID = E.getEid();
                        sc.getpassWord(stageV, "/views/timeclock/EmpDetailPopUp.fxml", "Number", "Enter Employee Number:", 420.0, 200.0);
                        //sc.goToScene("EmpDetail", stageV, eFX.getNameF(), null, boundsInScenememButton);
                        searchButton.setDisable(false);
                        runDimFile = true;
                        getEmployee(E.getENumber());
                    }
                    if (selectionModel.getSelectedIndex() == 2) {
                        setAllRadios(2);
                        searchButton.setDisable(true);
                    }
                    return;
                } else {
                    if (selectionModel.getSelectedIndex() == 0) {
                        new ClubFunctions().clearApostophies(EmployeePane);
                        //public EmpFileFX(String enumber, String id, String fname,     String lname, String mname,             String address, String city, String     state,              int zip,        String phoneh,          String phonec,          String socsec,   LocalDate birthdate, LocalDate hiredate, String loc, String status, String wagestatus,                     String shirtsize,           String gender,          String title,       int icard, int efiles, int cdata, int resos, int cdeposits, int bcarssales, int active, int gproblems, int tclock ) {
                        d = new EmpFileFX(E.getENumber(), E.getEid(), tfFName.getText(), tfLName.getText(), tfMName.getText(), tfAddress.getText(), tfCity.getText(), tfState.getText(), Integer.valueOf(tfZip.getText()), tfPhoneHome.getText(), tfPhoneCell.getText(), tfSocSec.getText(), dpBDate.getValue(), E.getHireDate(), dropBoxLocation.getText(), dropBoxStatus.getText(), dropBoxWageStatus.getText(), dropBoxShirtSize.getText(), dropBoxGender.getText(), dropBoxTitle.getText(), Integer.parseInt(voucher.getText()), 0, 0, 0, 0, 0, 0, 0, 0, 0, null, tEmailAddress.getText());
                        System.out.println(tfMName.getText() + " " + d.getMName() + " " + tEmailAddress.getText() + " " + d.getEmail());
                        if (!new EmpFileFXDB().upDateEmployeeFile(d)) {
                            System.out.println("Error EMployee Update not successfull.");
                        }
                        makeAllEdit(1);
                        setAllRadios(1);

                    }
                    if (selectionModel.getSelectedIndex() == 1) {
                        searchButton.setDisable(false);
                    }
                    if (selectionModel.getSelectedIndex() == 2) {
    System.out.println("4444444444444444444444 " + getRadios(6));
                        d = new EmpFileFX(E.getENumber(), E.getEid(), getRadios(1), getRadios(2), getRadios(3), getRadios(4), getRadios(5), getRadios(6), getRadios(7), getRadios(8), getRadios(9));
                        //System.out.println(getRadios(1) + ", " + getRadios(2) + ", " +  getRadios(3) + ", " +  getRadios(4) + ", " +  getRadios(5) + ", " +  getRadios(6) + ", " +  getRadios(7) + ", " +  getRadios(8) + ", " +  getRadios(9));
                        if (!new EmpFileFXDB().upDateEmployeeFileRadios(d)) {
                            System.out.println("Error EMployee Update not successfull.");
                        }

                        setAllRadios(1);
                        searchButton.setDisable(false);
                    }
                    runDimFile = true;
                    getEmployee(d.getENumber());
                }
                // }
            } catch (NumberFormatException e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
            } catch (IOException ex) {
                Logger.getLogger(EmpViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
   public void TableClicked(MouseEvent me) throws IOException {
         if (me.getClickCount() == 2) {
            //selectButton.fire();
         }
    }
    
    public void clickDetailTable() {
        TablePosition pos = detailTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        //String st = detailTable.getItems().get(row).getDNotes();
        new messageBox().showError(detailTable.getItems().get(row).getDNotes(), errorLabel, errorPane);

    }
    
    private void AddNewRecord() {        
        if (selectionModel.getSelectedIndex() == 0) {
            new ClubFunctions().clearApostophies(EmployeePane);
            //public EmpFileFX(String enumber, String id, String fname,     String lname, String mname,             String address, String city, String     state,              int zip,        String phoneh,          String phonec,          String socsec,   LocalDate birthdate, LocalDate hiredate, String loc, String status, String wagestatus,                     String shirtsize,           String gender,          String title,       int icard, int efiles, int cdata, int resos, int cdeposits, int bcarssales, int active, int gproblems, int tclock ) {
            String newEmpNumber = new EmpFileFXDB().getNewNumber();
            d = new EmpFileFX(newEmpNumber, new EmpFileFXDB().getNewID(), tfFName.getText(), tfLName.getText(), tfMName.getText(), tfAddress.getText(), tfCity.getText(), tfState.getText(), Integer.valueOf(tfZip.getText()), tfPhoneHome.getText(), tfPhoneCell.getText(), tfSocSec.getText(), dpBDate.getValue(), E.getHireDate(), dropBoxLocation.getText(), dropBoxStatus.getText(), dropBoxWageStatus.getText(), dropBoxShirtSize.getText(), dropBoxGender.getText(), dropBoxTitle.getText(), Integer.parseInt(voucher.getText()), 0, 0, 0, 3, 0, 0, 3, 1, 0, null, tEmailAddress.getText());
            System.out.println("VOucher " + Integer.parseInt(voucher.getText()));
            if (!new EmpFileFXDB().AddRec(d)) {
                System.out.println("Error EMployee Update not successfull.");
                new Mail_JavaFX().SendMail("Error", "error in Member New Employee (See EmpViewerController) ");           
            }
            E = new EmpFileFX(d.getENumber(), d.getEid(), getRadios(1), getRadios(2), getRadios(3), getRadios(4), getRadios(5), getRadios(6), getRadios(7), getRadios(8), getRadios(9));
            //System.out.println(getRadios(1) + ", " + getRadios(2) + ", " +  getRadios(3) + ", " +  getRadios(4) + ", " +  getRadios(5) + ", " +  getRadios(6) + ", " +  getRadios(7) + ", " +  getRadios(8) + ", " +  getRadios(9));
            if (!new EmpFileFXDB().upDateEmployeeFileRadios(d)) {
                System.out.println("Error EMployee Update not successfull.");
                new Mail_JavaFX().SendMail("Error", "error in Member New Employee upDateEMployeeRadios (See EmpViewerController) ");           
            }
            java.util.Date dt = new java.util.Date();
            String currentTime = tdf.format(dt);
            g = new EmpFileFXDetail("NEW HIRE", "GAMES", MGR, currentTime, "", "0", new EmpFileFXDB().getPayRate(), d.getEid(), LocalDate.now());
    //String dPurpose, String dDepartment, String dMGR, String dTime, String dNotes, int dPoints, int dPayRate, int dDetailID, LocalDate dDate
            if (!new EmpFileFXDB().AddEmpDetail(g)) {
                System.out.println("Error EMployee Update not successfull.");
                new Mail_JavaFX1().sendEmailTo("ERROR EMP Viewer", "error in Emp New Employee upDateEMP Detail (See EmpViewerController) ", "errors");           
            }
            CreateDirectory();
            runDimFile = true;
            makeAllEdit(1);
            setAllRadios(1);
            newRec = false;
            getEmployee(d.getENumber());

        }

    }
    
    private void CreateDirectory() {
        System.out.println(new GetReceipts().getReceipts(Receipts, "EmpDocs") + "/" + E.getLName() + E.getFName() + E.getEid() + "/");
        try {
             Path path = Paths.get(new GetReceipts().getReceipts(Receipts, "EmpDocs") + "\\"+ E.getLName() + "_" + E.getFName() + "_" + E.getEid());
            if (!Files.exists(path)) {
                //Path path = Paths.get(new GetReceipts().getReceipts(Receipts, "EmpDocs") + "\\" + E.getFName() + E.getEid());
                Files.createDirectories(path);
                System.out.println("Directory is created!");
            }
        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
            new Mail_JavaFX1().sendEmailTo("ERROR EMP Viewer", "error in Emp New Employee CreateDirectory (See EmpViewerController) ", "errors");           
        }
    }
    
    
    private int getRadios(int whichOne) {
        int getRadio = 0;
        switch(whichOne) {
            case 1: //THIS IS MGR
                if (intercard1.isSelected()) {
                    getRadio = 1;
                }
                if (intercard2.isSelected()) {
                    getRadio = 2;
                }
                if (intercard3.isSelected()) {
                    getRadio = 3;
                }
                if (intercardNO.isSelected()) {
                    getRadio = 0;
                }
                break;
            case 2: //EMP FILES
                if (eFilesYES.isSelected()) {
                    getRadio = 1;
                } else {
                    getRadio = 0;
                }
                break;
            case 3: //CHNGER EDIT
                if (corpYES.isSelected()) {
                   getRadio = 1;
                } else {
                    getRadio = 0;
                }
                break;
            case 4: //THIS IS BDAY RESOS
                if (reservations1.isSelected()) {
                    getRadio = 1;    
                }
                if (reservations2.isSelected()) {
                    getRadio = 2;
                }
                if (reservations3.isSelected()) {
                    getRadio = 3;
                }
                break;
            case 5:
                if (cDepositsYES.isSelected()) {
                   getRadio = 1;
                } else {
                   getRadio = 0;
                }
                break;
            case 6: //THIS IS BPC SALES (NOT IN USE)
                System.out.println("is BcarsYES selected " + bCarsYES.isSelected());
                if (bCarsYES.isSelected()) {
                   getRadio = 1;
                System.out.println("bcars getRadio 1 = " + getRadio);
                } else {
                System.out.println("bcars getRadio 2 = " + getRadio);
                   getRadio = 0;
                }
                break;
            case 7: //THIS IS ACTIVE
                if (active1.isSelected()) {
                   getRadio = 0;
                }
                if (active2.isSelected()) {
                    getRadio = 1;
                } 
                if (active3.isSelected()) {
                    getRadio = 2;
                }
                if (active4.isSelected()) {
                    getRadio = 3;
                }
                break;
            case 8: //THIS IS GAME PROBLEMS
                if (gProblems1.isSelected()) {
                   getRadio = 1;
                }
                if (gProblems2.isSelected()) {
                    getRadio = 2;
                }
                if (gProblems3.isSelected()) {
                    getRadio = 3;
                }
                break;
            case 9: //THIS IS TIMECLOCK
                if (tClockYES.isSelected()) {
                   getRadio = 1;
                } else {
                   getRadio = 0;
                }
        }
        System.out.println("getRadio() " + whichOne + " " + getRadio);
        return getRadio;
    }
    

    public void printNumberButtonPressed() {
        //try {
        //GOPRTNumberButton(cardNumberTextfield.getText());
        //} catch (FileNotFoundException ex) {
        //Logger.getLogger(MemberViewController.class.getName()).log(Level.SEVERE, null, ex);
        //}

    }

    
    private void checkFirstRecord(String CCN) {
        if (CCN.equals("1001")) {
            throw new IllegalArgumentException("\n This account cannot be edited or Documents added to it");
        }
    }

    public void LookUpButtonPressed(ActionEvent event) throws IOException {
        new messageBox().showErrorClear(errorLabel, errorPane);
        if (dropBoxLocation.isFocused()) {
            Bounds boundsInScene = dropBoxLocation.localToScene(dropBoxLocation.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "ELocation", "Select One:", dropBoxLocation.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals("Location")) {
                dropBoxLocation.setText(sc.getGameNumber());
                dropBoxLocation.requestFocus();
            }
            return;
        }
        if (dropBoxStatus.isFocused()) {
            Bounds boundsInScene = dropBoxStatus.localToScene(dropBoxStatus.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "Status", "Select One:", dropBoxStatus.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals("Status")) {
                dropBoxStatus.setText(sc.getGameNumber());
                dropBoxStatus.requestFocus();
            }
            return;
        }
        if (dropBoxWageStatus.isFocused()) {
            Bounds boundsInScene = dropBoxWageStatus.localToScene(dropBoxWageStatus.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "WStatus", "Select One:", dropBoxWageStatus.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals("WStatus")) {
                dropBoxWageStatus.setText(sc.getGameNumber());
                dropBoxWageStatus.requestFocus();
            }
            return;
        }
        if (dropBoxShirtSize.isFocused()) {
            Bounds boundsInScene = dropBoxShirtSize.localToScene(dropBoxShirtSize.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "ShirtSize", "Select One:", dropBoxShirtSize.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals("ShirtSize")) {
                dropBoxShirtSize.setText(sc.getGameNumber());
                dropBoxShirtSize.requestFocus();
            }
            return;
        }
        if (dropBoxTitle.isFocused()) {
            getDropBox(event, dropBoxTitle, "Title");
            return;
        }
        if (dropBoxGender.isFocused()) {
            getDropBox(event, dropBoxGender, "Gender");
            return;
        }
        if (editMode) {
            new messageBox().showError("You cannot search for another employee while still editing this employee.", errorLabel, errorPane);
            return;
        } else {
            sc.changePopUp(event, "/views/timeclock/empSearchTableView.fxml", "List of Employee");
            dpBDate.requestFocus();
            if (!sc.getGameNumber().trim().equals("Number")) {
                try {
                    db.Connect();
                    //db.isInactiveMemberValid(sc.getGameNumber().trim());
                    getEmployee(sc.getGameNumber());
                    getEmpImage();
                    //GOPRTNumberButton(sc.getGameNumber());
                    db.disConnect();
                } catch (SQLException ex) {
                    System.out.println("Lookupbuttonpressed Error " + ex);
                }

            } else {
                if (!sc.getGameNumber().trim().equals("Number")) {
                    //getGame(sc.getGameNumber());
                    //GOPRTNumberButton(sc.getGameNumber());
                }
            }
        }
         Platform.runLater(()->{searchButton.requestFocus();});
    }

    private void getDropBox(ActionEvent event, TextField tx, String txtFile) {
        try {
            Bounds boundsInScene = tx.localToScene(tx.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", txtFile, "Select One:", tx.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals(txtFile)) {
                tx.setText(sc.getGameNumber());
                tx.requestFocus();
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }
    
    
    private void setToUpper() {
        for (Node node : gridPane1.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).textProperty().addListener((ov, oldValue, newValue) -> {try {((TextField) node).setText(newValue.toUpperCase());}catch(Exception e) {}});
            }
        }
        beenhereonce = 0;
        //fNameTextfield.textProperty().addListener((ov, oldValue, newValue) -> {fNameTextfield.setText(newValue.toUpperCase());});
    }
    
    public void mouseClick() {
    dropBoxLocation.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                searchButton.fire();
            }}});
    dropBoxStatus.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                searchButton.fire();
            }}});
    dropBoxWageStatus.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                searchButton.fire();
            }}});
    dropBoxShirtSize.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                searchButton.fire();
            }}});
    dropBoxGender.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                searchButton.fire();
            }}});
    dropBoxTitle.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                searchButton.fire();
            }}});
    tEmailAddress.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                openEmail();
            }}});
  
    }
    
     public void openEmail() {
        System.out.println("here we are in hostServices " + "mailto:" + tEmailAddress.getText());
        try {
            URI uri = new URI("http://" + tEmailAddress.getText());
        
        Hyperlink hp = new Hyperlink("http://" + tEmailAddress.getText()); 
        System.out.println(hp.toString());
        open(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(EmpViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void open(URI uri) {
    if (Desktop.isDesktopSupported()) {
      try {
        Desktop.getDesktop().browse(uri);
      } catch (IOException e) { /* TODO: error handling */ }
    } else { /* TODO: error handling */ }
  }
     
    
    private void enterKeyPressed() {
        new messageBox().showErrorClear(errorLabel, errorPane);
        if (tfFName.isFocused()) {
            tfMName.requestFocus();
            return;
        }
        if (tfMName.isFocused()) {
            tfLName.requestFocus();
            return;
        }
        if (tfLName.isFocused()) {
            tfAddress.requestFocus();
            return;
        }
        if (tfAddress.isFocused()) {
            tfCity.requestFocus();
            return;
        }
        if (tfCity.isFocused()) {
            tfState.requestFocus();
            return;
        }
        if (tfState.isFocused()) {
            tfZip.requestFocus();
            return;
        }
        if (tfZip.isFocused()) {
            tfPhoneHome.requestFocus();
            return;
        }
        if (tfPhoneHome.isFocused()) {
                tfPhoneHome.setText(new ClubFunctions().formatPhone(tfPhoneHome.getText()));
                tfPhoneCell.requestFocus();
                return;
        }
        if (tfPhoneCell.isFocused()) {
                tfPhoneCell.setText(new ClubFunctions().formatPhone(tfPhoneCell.getText()));
                tfSocSec.requestFocus();
                return;
        }
        if (searchButton.isFocused()) {
            searchButton.fire();
            return;
        }
        if (tfSocSec.isFocused()) {
            tfSocSec.setText(new ClubFunctions().formatSocSec(tfSocSec.getText()));
            dpBDate.requestFocus();
            return;
        }
        if (dpBDate.isFocused()) {
            dropBoxLocation.requestFocus();
            beenhereonce=0;
            return;
        }
        /*if (tfHireDate.isFocused()) {
            dropBoxLocation.requestFocus();
            return;
        }*/
        if (dropBoxLocation.isFocused()) {
            dropBoxStatus.requestFocus();
            beenhereonce=0;
            return;
        }
        if (dropBoxStatus.isFocused()) {
            dropBoxWageStatus.requestFocus();
            beenhereonce=0;
            return;
        }
        if (dropBoxWageStatus.isFocused()) {
            dropBoxShirtSize.requestFocus();
            beenhereonce=0;
            return;
        }
        if (dropBoxShirtSize.isFocused()) {
            dropBoxTitle.requestFocus();
            beenhereonce=0;
            return;
        }
        if (dropBoxTitle.isFocused()) {
            dropBoxGender.requestFocus();
            beenhereonce=0;
            return;
        }
        if (dropBoxGender.isFocused()) {
            voucher.requestFocus();
            return;
        }
        if (voucher.isFocused()) {
            System.out.println("we are in vouvher");
            tEmailAddress.requestFocus();
            return;
        }
        if (tEmailAddress.isFocused()) {
            tfFName.requestFocus();
        }
    }

    public void buildMenuButton() {
        MenuItem item1 = new MenuItem(" SHOW EMP NUMBER ");
        //MenuItem item2 = new MenuItem(" DELETE EMPLOYEE ");
        //MenuItem empDocs = new MenuItem(" EMP DOCS ");
        MenuItem item4 = new MenuItem(" NEW EMP NUMBER ");
        MenuItem copyEmail = new MenuItem(" COPY EMAIL ");
        MenuItem listofallEmails = new MenuItem(" ALL EMAILS ");

        Menu menu1 = new Menu(" Reports ");
        MenuItem csItem1 = new MenuItem(" Print File ");
        MenuItem csItem2 = new MenuItem(" Print All Active ");
        MenuItem empRday = new MenuItem(" YESTURDAY ");
        MenuItem empRweek = new MenuItem(" LAST 7 DAYS ");
        MenuItem empRmonth = new MenuItem(" LAST 30 DAYS ");
        menu1.getItems().addAll(csItem1, csItem2, empRday, empRweek, empRmonth);

        
        
        Menu menu2 = new Menu(" EDIT POPUP MENUS ");
        MenuItem cItem1 = new MenuItem("LOCATION");
        MenuItem cItem2 = new MenuItem("SHIRT SIZE");
        MenuItem cItem3 = new MenuItem("STATUS");
        MenuItem cItem4 = new MenuItem("WAGE STATUS");
        MenuItem cItem5 = new MenuItem("GENDER");
        MenuItem cItem6 = new MenuItem("TITLE");
        MenuItem cItem7 = new MenuItem("PURPOSE");
        MenuItem DocReasons = new MenuItem("DOC REASONS");
        menu2.getItems().addAll(cItem1, cItem2, cItem3, DocReasons, cItem4, cItem5, cItem6, cItem7);

        contextMenu.getItems().addAll(item1, item4, menu1, copyEmail, listofallEmails, menu2);

        item1.setOnAction((ActionEvent event) -> {
        new messageBox().showAlert(Alert.AlertType.CONFIRMATION, stageV, "Emp Number", E.ENumber);
            //whichOne = 1;
            //searchButton.fire();
        });
        
        cItem1.setOnAction((ActionEvent event) -> {
            whichdropedit(1);
        });
        cItem2.setOnAction((ActionEvent event) -> {
            whichdropedit(2);
        });
        cItem3.setOnAction((ActionEvent event) -> {
            whichdropedit(3);
        });
        cItem4.setOnAction((ActionEvent event) -> {
            whichdropedit(4);
        });
        cItem5.setOnAction((ActionEvent event) -> {
            whichdropedit(5);
        });
        cItem6.setOnAction((ActionEvent event) -> {
            whichdropedit(6);
        });
        cItem7.setOnAction((ActionEvent event) -> {
            whichdropedit(7);
        });
        DocReasons.setOnAction((ActionEvent event) -> {
            whichdropedit(8);
        });

        
        
        csItem1.setOnAction((ActionEvent event) -> {
        //new messageBox().showAlert(Alert.AlertType.CONFIRMATION, stageV, "Emp Number", E.ENumber);
        //EmpFileFX trv = partiesTable.getSelectionModel().getSelectedItem();
        new ReportsE().singlereport(E.Eid);
        
        });
        
        csItem2.setOnAction((ActionEvent event) -> {
            new ReportsE().allActive();        
        });

        empRday.setOnAction((ActionEvent event) -> {                
            new EmpActivity().buildReport("1", E, ownerEmail);
        });
        
        empRweek.setOnAction((ActionEvent event) -> {                
            new EmpActivity().buildReport("2", E, ownerEmail);
        });

        empRmonth.setOnAction((ActionEvent event) -> {                
            new EmpActivity().buildReport("3", E, ownerEmail);
        });

        copyEmail.setOnAction((ActionEvent event) -> {
            String emailAddress = E.getEmail();
            System.out.println("here is email address " + emailAddress + " " + E.getEmail());
            if (!emailAddress.equals("")){
                StringSelection stringSelection = new StringSelection(E.getEmail());
                java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                new messageBox().showAlert(Alert.AlertType.CONFIRMATION, stageV, MGR, "Email has been copied to the clipboard " + emailAddress);
            }
        });




        listofallEmails.setOnAction((ActionEvent event) ->{
            try { 
                //sc.changePopUp(event, "/views/timeclock/empEmailSearchTableView.fxml", "List of All Emaill Address to Send");
                sc.getpassWord(stageV, "/views/timeclock/empEmailSearchTableView.fxml", "List of All Emaill Address to Send", "hfhfhfhf", 300.00, 60.00);
                dpBDate.requestFocus();
            if (!sc.getGameNumber().trim().equals("Number")) {
                try {
                    db.Connect();
                    //db.isInactiveMemberValid(sc.getGameNumber().trim());
                    getEmployee(sc.getGameNumber());
                    //GOPRTNumberButton(sc.getGameNumber());
                    db.disConnect();
                } catch (SQLException ex) {
                    System.out.println("Lookupbuttonpressed Error " + ex);
                }

            } else {
                if (!sc.getGameNumber().trim().equals("Number")) {
                    //getGame(sc.getGameNumber());
                    //GOPRTNumberButton(sc.getGameNumber());
                }
            }
            } catch (IOException ex) {
                Logger.getLogger(EmpViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        item4.setOnAction((ActionEvent event) -> {
            try {
                checkFirstRecord(E.getENumber().trim());
                //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Wait!", "You are changing the employee number. you will be asked to confirm you are intending to do this by re-entering your employee number after this message box.");
                int alert = new messageBox().confirmMakeThisChange(Alert.AlertType.CONFIRMATION, stageV, "WAIT!", "CLICK (MAKE CHANGE) Button to confirm you want to change the employee number");
                if (alert == 1) {
                    java.util.Date dt = new java.util.Date();
                    String currentTime = tdf.format(dt);            
                    String newNumber = db.PutNewNumber(E, E.getEid());
                    Platform.runLater(() -> getEmployee(newNumber));

                    System.out.println("Alert " + alert + " newNumber: " + newNumber + "time: " + currentTime + " mgr: " + MGR + " getID: " + E.getEid());
                    g = new EmpFileFXDetail("CHG NUMBR", "GAMES", MGR, currentTime, "Old Number "+ newNumber, "0", 0.00, E.getEid(), LocalDate.now());
                    System.out.println("here");
                    if (!new EmpFileFXDB().AddEmpDetail(g)) {
                        System.out.println("Error EMployee Update not successfull.");
                        new Mail_JavaFX1().sendEmailTo("Error", "error in Emp New Employee upDateEMP Detail (See EmpViewerController) ", "errors");           
                    }
                    
                    //runDimFile = true;
                    Platform.runLater(() -> new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Emp Number", "The New Number is: " + newNumber));
                } else {
                    Platform.runLater(() -> new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "YOUR CHOICE", "You chose not to change the employee Number " + E.ENumber));
                }
            } catch(Exception e) {new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());}
            //Platform.runLater(() -> getEmployee(E.getENumber()));
        });


        
        //item5.setOnAction((ActionEvent event) -> {
            //db.Connect();
            //db.runImportEMPDET();
            //db.disConnect();
            //new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Email Address", r);
        //});

        moreButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(moreButton, boundsInScenememMoreButton.getMinX(), boundsInScenememMoreButton.getMinY());
            }
        });
        moreButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(moreButton, event.getScreenX(), event.getScreenY());
        });
    }

    public void menuItemsButtonMouseOver() {
        contextMenu.show(moreButton, boundsInScenememMoreButton.getMinX()+50, boundsInScenememMoreButton.getMinY() + 100);
    }

    private void makeAllEdit(int whichOne) {
        
        switch (whichOne) {
            case 1:
                for (Node node : gridPane1.getChildren()) {
                    if (node instanceof TextField) {
                        ((TextField) node).setStyle("-fx-opacity: 1.0;");
                        ((TextField) node).setDisable(true);
                    }
                }
                setButtonsOnEdit(1);
                break;
            case 2:
                for (Node node : gridPane1.getChildren()) {
                    if (node instanceof TextField) {
                        ((TextField) node).setStyle("-fx-opacity: 1.0;");
                        ((TextField) node).setDisable(false);
                    }
                }
                setButtonsOnEdit(2);
                tfFName.requestFocus();
                break;
        }
}  
    
    private void setButtonsOnEdit(int whichOne) {
        switch (whichOne) {
            case 1:
                newRecButton.setDisable(false);
                //printButton.setDisable(false);
                searchButton.setDisable(false);
                editButton.setText("Edit F9");
                tfEmpNumber.setStyle("-fx-background-color: #D3D3D3");
                tfEmpNumber.setDisable(true);
                tfHireDate.setStyle("-fx-background-color: #D3D3D3");
                tfHireDate.setDisable(true);
                editMode = false;
                break;
            case 2:
                newRecButton.setDisable(true);
                //printButton.setDisable(true);
                searchButton.setDisable(false);
                editButton.setText("Save F9");
                tfEmpNumber.setStyle("-fx-background-color: #D3D3D3");
                tfEmpNumber.setDisable(true);
                tfHireDate.setStyle("-fx-background-color: #D3D3D3");
                tfHireDate.setDisable(true);
                editMode = true;
                break;
        }
    }
    
    public void whichdropedit(int g) {
        // the other three frames rewuire this function
        String fName = "";
        switch (g) {
            case 1:
                fName = dbStringPath.pathNameTclock + "ELocation.txt";
                break;
            case 2:
                fName = dbStringPath.pathNameTclock + "ShirtSize.txt";
                break;
            case 3:
                fName = dbStringPath.pathNameTclock + "Status.txt";
                break;
            case 4:
                fName = dbStringPath.pathNameTclock + "WStatus.txt";
                break;
            case 5:
                fName = dbStringPath.pathNameTclock + "Gender.txt";
                break;
            case 6:
                fName = dbStringPath.pathNameTclock + "Title.txt";
                break;
            case 7:
                fName = dbStringPath.pathNameTclock + "Purpose.txt";
                break;
            case 8:
                fName = dbStringPath.pathNameTclock + "DocsReasons.txt";
                break;

        }
        Desktop dsk = Desktop.getDesktop();
        try {
            dsk.open(new File(fName));
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }
    
    
    public void GOEmpDocs(ActionEvent event) throws IOException {
        if (DocsButton.getText().equals("Refresh")) {
            DocsButton.setText("Docs");
            getEmpImage();
            return;
        }
        try {
            checkFirstRecord(E.getENumber());
        } catch(Exception e) {
        new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Stop!", e.toString());
        return;
        }
        EmpDocsViewerController wController = (EmpDocsViewerController) fxmlLoader.getController();
        wController.cssPath = cssPath;
        wController.MGR = eFX.nameF;
        wController.Receipts = Receipts;
        wController.Employee = E;
        wController.empDocsPath = Paths.get(new GetReceipts().getReceipts(Receipts, "EmpDocs") + "\\"+ E.getLName() + "_" + E.getFName() + "_" + E.getEid());
        getStageV();
        DocsButton.setText("Refresh");
        sc.getStagestyleUndecorated(stageV, "/views/timeclock/EmpDocsViewer.fxml", cssPath, cssPath, boundsInScenememButton.getMinX() + 230.0, boundsInScenememButton.getMinY() - 30.0);
    }
    
    
     private void getStageV() {
        this.stageV = (Stage) moreButton.getScene().getWindow();
    }
    
    
    public void showEmpNumber() {
        new messageBox().showAlert(Alert.AlertType.CONFIRMATION, stageV, "Emp Number", E.ENumber);
    }
    
    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.D && ke.isAltDown()) {
                keyListener(ke);
                ke.consume();
            }
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
                newRecButton.fire();
                break;
            case F7:
                searchButton.fire();
                break;
            case F8:
                //printButton.fire();
                break;
            case F9:
                editButton.fire();
                break;
            case F10:
                break;
            case F11:
                break;
            case F12:
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case ENTER:
                enterKeyPressed();
                break;
            case D: 
                DocsButton.fire(); 
                break;

            case DOWN:
            System.out.println(beenhereonce);
        if (beenhereonce>0) {
            System.out.println(beenhereonce);
            return;
        } else {
            beenhereonce=+1;
            System.out.println(beenhereonce);
        }
            System.out.println(beenhereonce);
                searchButton.fire();
                break;
            default:
                break;
        }
    }
    
    
    private void UpdateDimFile() {
        try {
            System.out.println("Here in this updatedimfile");
            //GET ALL ACTIVE EMPLOYEES
            updateList = new EmpFileFXDB().getAllActiveEmployees();
            System.out.println("finishedupdate list");
            //DELETE DIM FILE
            new EmpFileFXDB().emptyDimTable();
            System.out.println("empty dim table");
            //USEING A WHILE LOOP THROUGH ADD ALL ACTIVE TO DIM FILE
            new EmpFileFXDB().UpdateDateDim(updateList, E.getEmail());
            System.out.println("complete update dim file");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        
        
        
        
        
    }
    
    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        sc.setGameNumber(null);
        eFX.Flush();
        //getStageV();

        sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", boundsInScene4.getMinX() + 350.0, boundsInScene4.getMinY());
        //if (!isEMPValidInArrayList(sc.getGameNumber())) {
        //    GO = false;
        //} else {
            if (!newEFX.getEmpNumber().equals("Number") || !newEFX.getEmpNumber().isEmpty()) {
                GO = true;
            } else {
                GO = false;
            }
        //}
        return GO;
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        stageV = (Stage) cancelButton.getScene().getWindow();
        if (editMode) {
            if (confirm("End Edit?", "This will clear any changes \n are you sure you want to do this?")) {
                makeAllEdit(1);
                newRecButton.setDisable(false);
                //printButton.setDisable(false);
                searchButton.setDisable(false);
                getEmployee(E.getENumber());
                setAllRadios(1);
            } else {
                return;
            }
        } else {
            //System.out.println("hfhfhfhfhfhfhfhfhf " + runDimFile);
            try {
                if (runDimFile) {
                    Platform.runLater(() ->  new messageBox().showAlert(Alert.AlertType.CONFIRMATION, stageV, "Notice", "An Email is being generated and sent to " + ownerEmail + " so that you can see the list of active employee and confirm the full list was made"));
                    Platform.runLater(() ->  RunBuildFile());    
                }
                //new FXTimerBinding().start(stageV);
                db.disConnect();
                db = null;
                inputstream.close();
                empProfilePicture = null;
                imageView.setImage(null);
                editMode = false;
                runDimFile = false;
                stageV.close();
            } catch (Exception e) {
                stageV.close();
            }

        }
    }
    
    
    private void RunBuildFile() {
        Thread thread1 = new Thread() {
                        @Override
                        public void run() {
                            System.out.println("Running Dim File");
                            new EmpFileFXDB().UpdateDimFile(ownerEmail);

                        }
                    };
                thread1.start();
        
        
    }
    
    

    private boolean confirm(String title, String question) {
        boolean confirm = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(question + "\n");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(new cssChanger().cssPath());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            confirm = true;
        }
        return confirm;
    }


    
    
    private void FillRadioButtons() {
        InterCardToggleGroup = new ToggleGroup();
        this.intercard1.setToggleGroup(InterCardToggleGroup);
        this.intercard2.setToggleGroup(InterCardToggleGroup);
        this.intercard3.setToggleGroup(InterCardToggleGroup);
        this.intercardNO.setToggleGroup(InterCardToggleGroup);

        EFilesTGroup = new ToggleGroup();
        this.eFilesYES.setToggleGroup(EFilesTGroup);
        this.eFilesNO.setToggleGroup(EFilesTGroup);

        CorpDataTGroup = new ToggleGroup();
        this.corpYES.setToggleGroup(CorpDataTGroup);
        this.corpNO.setToggleGroup(CorpDataTGroup);

        ReservationsTGroup = new ToggleGroup();
        this.reservations1.setToggleGroup(ReservationsTGroup);
        this.reservations2.setToggleGroup(ReservationsTGroup);
        this.reservations3.setToggleGroup(ReservationsTGroup);

        CorpDepositsTGroup = new ToggleGroup();
        this.cDepositsYES.setToggleGroup(CorpDepositsTGroup);
        this.cDepositsNO.setToggleGroup(CorpDepositsTGroup);

        BCarsSalesTGroup = new ToggleGroup();
        this.bCarsYES.setToggleGroup(BCarsSalesTGroup);
        this.bCarsNO.setToggleGroup(BCarsSalesTGroup);

        ActiveStatusTGroup = new ToggleGroup();
        this.active1.setToggleGroup(ActiveStatusTGroup);
        this.active2.setToggleGroup(ActiveStatusTGroup);
        this.active3.setToggleGroup(ActiveStatusTGroup);
        this.active4.setToggleGroup(ActiveStatusTGroup);

        GProblemsTGroup = new ToggleGroup();
        this.gProblems1.setToggleGroup(GProblemsTGroup);
        this.gProblems2.setToggleGroup(GProblemsTGroup);
        this.gProblems3.setToggleGroup(GProblemsTGroup);

        TimeClockTGroup = new ToggleGroup();
        this.tClockYES.setToggleGroup(TimeClockTGroup);
        this.tClockNO.setToggleGroup(TimeClockTGroup);

    }

    private void setRadioButtons(EmpFileFX E) {
        if (E.getInterCard() == 1) {
            intercard1.setSelected(true);
        }
        if (E.getInterCard() == 2) {
            intercard2.setSelected(true);
        }
        if (E.getInterCard() == 3) {
            intercard3.setSelected(true);
        }
        if (E.getInterCard() == 0) {
            intercardNO.setSelected(true);
        }
        if (E.geteFiles() == 1) {
            eFilesYES.setSelected(true);
        } else {
            eFilesNO.setSelected(true);
        }
        if (E.getCorpData() == 1) {
            corpYES.setSelected(true);
        } else {
            corpNO.setSelected(true);
        }
        if (E.getReservations() > 0) {
            switch (E.getReservations()) {
                case 1:
                    reservations1.setSelected(true); // FULL ACCESS
                    break;
                case 2:
                    reservations2.setSelected(true); //EDIT NOTES
                    break;
                case 3:
                    reservations3.setSelected(true); //VIEWING ONLY
                    break;
            }
        } else {
            reservations3.setSelected(true); //VIEWING ONLY
        }
        if (E.getCorpDeposits() == 1) {
            cDepositsYES.setSelected(true);
        } else {
            cDepositsNO.setSelected(true);
        }
        if (E.getbCarsSales() == 1) {
            bCarsYES.setSelected(true);
        } else {
            bCarsNO.setSelected(true);
        }
        if (E.getActiveStatus() > 0 || String.valueOf(E.getActiveStatus()).isEmpty()) {
            switch (E.getActiveStatus()) {
                case 1:
                    active2.setSelected(true); // DEPS ONLY
                    break;
                case 2:
                    active3.setSelected(true); //DEPS AND WITHS
                    break;
                case 3:
                    active4.setSelected(true); // DEPS, WITHS AND NEW MEMBERS
                    break;
            }
        } else {
            active1.setSelected(true); //VIEWING ONLY
        }

        if (E.getgProblems() > 0 || String.valueOf(E.getgProblems()).isEmpty()) {
            switch (E.getgProblems()) {
                case 3:
                    gProblems3.setSelected(true); // ADD, EDIT, DELETE
                    break;
                case 2:
                    gProblems2.setSelected(true); // ADD, EDIT, FIXED
                    break;
                case 1:
                    gProblems1.setSelected(true); // ADD NEW RECORD ONLY
                    break;
            }
        } else {
            gProblems1.setSelected(true); //VIEWING ONLY
        }
        if (E.gettClock() == 1) {
            tClockYES.setSelected(true);
        } else {
            tClockNO.setSelected(true);
        }

    }

    private void setAllRadios(int whichOne) {
        switch (whichOne) {
            case 1:
                intercard1.setDisable(true);
                intercard2.setDisable(true);
                intercard3.setDisable(true);
                intercardNO.setDisable(true);
                eFilesYES.setDisable(true);
                eFilesNO.setDisable(true);
                corpYES.setDisable(true);
                corpNO.setDisable(true);
                reservations1.setDisable(true);
                reservations2.setDisable(true);
                reservations3.setDisable(true);
                cDepositsYES.setDisable(true);
                cDepositsNO.setDisable(true);
                bCarsYES.setDisable(true);
                bCarsNO.setDisable(true);
                active1.setDisable(true);
                active2.setDisable(true);
                active3.setDisable(true);
                active4.setDisable(true);
                gProblems1.setDisable(true);
                gProblems2.setDisable(true);
                gProblems3.setDisable(true);
                tClockYES.setDisable(true);
                tClockNO.setDisable(true);
                setButtonsOnEdit(1);
                break;
            case 2:
                intercard1.setDisable(false);
                intercard2.setDisable(false);
                intercard3.setDisable(false);
                intercardNO.setDisable(false);
                eFilesYES.setDisable(false);
                eFilesNO.setDisable(false);
                corpYES.setDisable(false);
                corpNO.setDisable(false);
                reservations1.setDisable(false);
                reservations2.setDisable(false);
                reservations3.setDisable(false);
                cDepositsYES.setDisable(false);
                cDepositsNO.setDisable(false);
                bCarsYES.setDisable(false);
                bCarsNO.setDisable(false);
                active1.setDisable(false);
                active2.setDisable(false);
                active3.setDisable(false);
                active4.setDisable(false);
                gProblems1.setDisable(false);
                gProblems2.setDisable(false);
                gProblems3.setDisable(false);
                tClockYES.setDisable(false);
                tClockNO.setDisable(false);
                setButtonsOnEdit(2);
                break;
        }

    }

    


    private void getEmpImage() {
        Connection conn = null;
        Path path = null;
        //String empProfilePicture = "";
        EmpDocs empProfilePic = new EmpDocs("","","",new GetDay().asSQLDate(LocalDate.now()),"","", conn,path, false);
        //FileInputStream 0inputstream = null;
        try {
            empProfilePicture = empProfilePic.getPrifilePic(E.getEid());
            if (empProfilePicture.equals("NONE")) {
                inputstream = new FileInputStream("L:\\Tclock\\Noempphoto.png");
            } else {
                inputstream = new FileInputStream(empProfilePicture);
            }
        } catch (FileNotFoundException | SQLException ex) {
            System.out.println(ex);
        }
        System.out.println(empProfilePicture);
        Image image = new Image(inputstream);
        imageView.setImage(image);
        empProfilePic = null;
    }
    
    
    
    
    
    
    
    
}
