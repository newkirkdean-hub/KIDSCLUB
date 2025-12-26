package views;

import views.settings.HelpVideosViewerController;
import views.timeclock.EmpViewerController;
import views.games.GameMainController;
import views.toys.ToysMainController;
import views.parties.BDayFXController;
import pWordFX.employeeFX;
import views.corp.CorpForm_MainFXController;
import views.gameproblems.GamesProblemsController;
import views.clubmail.BdayMailReturnsController;
import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import clockoutatnight.clockOutAtNightFX;
import dbpathnames.dbStringPath;
import static dbpathnames.dbStringPath.pathNameLocal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import messageBox.messageBox;
import models.club.DB;
import models.club.rCeipts;
import commoncodes.MemoryTest;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import messageBox.HTMLViewerController_Page;
import models.club.CheckBalanceDB;
import models.club.Member;
import models.club.Memtick;
import models.timeclock.EmpFileFX;
import models.timeclock.EmpFileFXDB;
import pWordFX.*;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;
import views.cafe.CToysMainController;
import views.clubmail.BdaysNextMonthController;
import views.clubmail.ClosedLockedController;
import views.clubmail.EmailsController;
import views.clubmail.NewMemberMailController;
import views.counterPopUp.QTYEMPTouchScreenLoginController;
import views.counterPopUp.QTYTouchScreenController;
import static views.gameproblems.GamesProblemsController.dbsp;
import views.settings.BridgeReceiptsBackRoomController;
import views.settings.DayReportsViewerController;
import views.settings.GreetingsControllerClub1;
import views.settings.TicketEaterCodesController;
import views.settings.notificationEmailsController;
import views.timeclock.EmpDetailPopUpCounterController;
import views.timeclock.TClockViewerController;
import static views.toys.PurchaseFormController.stageV;
import views.vouchers.FXVoucherViewerController;


public class MainController implements Initializable {
    @FXML private Button exitButton;
    @FXML private Button gamesButton;
    @FXML private Button corpButton;
    @FXML private Button empButton;
    @FXML private Button clockButton;
    @FXML private Button menuItemsButton;
    @FXML private Button colorsButton;
    @FXML private Button reportsButton;
    //@FXML private Button notesButton;
    //@FXML private Button printButton;
    @FXML private Button loginButton;
    @FXML private Pane root;
    @FXML private Pane depPane;
    @FXML private VBox menuPane;
    @FXML private Stage MainStageV;

    private static final SceneChanger_Main SC = new SceneChanger_Main();
    ContextMenu CONTEXTMENU = new ContextMenu();
    private static final settingsFXML SG = new settingsFXML();
    private static final dbStringPath DBSP = new dbStringPath();
    private static final cssChanger CSSC = new cssChanger();
    private static final employeeFX EFX = new employeeFX();
    private static final String CSSPATH = CSSC.cssPath();
    private static final DB DB = new DB();
    private static final EmpFileFXDB EMPDB = new EmpFileFXDB();
    private static final clockOutAtNightFX CLOCKOUT = new clockOutAtNightFX();
    private static final FXMLLoader FXLOADER = new FXMLLoader();
    private static final messageBox MBOX = new messageBox();
    private static final CheckBalanceDB CHKBALANCE = new CheckBalanceDB();
    private static final Mail_JavaFX1 JMAIL = new Mail_JavaFX1();
    private static final MemoryTest MEMORYTEST = new MemoryTest();
    private static final Member MEMBER = null;
    private static final Memtick MEMTICK = null;
    private static final String PATHCORP2 = "//POJOS-NET-DRIVE\\clubdb\\Corp\\Corp2";

    


    
    private static String thisone="";
    private static EmpFileFX E = null;
    private static ArrayList<empFX> EE;
    private static empFX newEFX;
    private static ArrayList<String> V;
    private static ArrayList<String> VV;
    private static ArrayList<rCeipts> Receipts;
    private Bounds boundsInSceneGamesButton, boundsInSceneMenuButton;
    private static final DecimalFormat DF = new DecimalFormat("#");
    private static boolean locked = false;



    
    @Override   
    public void initialize(URL url, ResourceBundle rb) {
        boundsInSceneMenuButton = menuItemsButton.localToScene(menuItemsButton.getBoundsInLocal());
        boundsInSceneGamesButton = gamesButton.localToScene(gamesButton.getBoundsInLocal());
        SC.resetPanes(depPane);
        buildMenuButton();
        setKeyCodes();
        GOlockScreen(0);
        //checkActiveStatus();
        setTimer();
        DBSP.setName();
        menuPane.prefHeightProperty().bind(root.heightProperty());
        File file = new File(pathNameLocal + "configFXML.properties");
        if (file.exists() && file.canRead()) {
            root.getStylesheets().add(CSSPATH);
            colorsButton.setText("Theme: " + CSSC.GetCssName(2));
        } else {
            SG.setSettings(2);
            root.getStylesheets().add(CSSPATH);
            colorsButton.setText("Theme: " + CSSC.GetCssName(2));
        }
        Platform.runLater(()->getStage());
        Platform.runLater(()->setCloseCatch());
        Platform.runLater(()->checkTime());
        Platform.runLater(() -> GetLists());

        
    }
    
    private void setCloseCatch() {
        getStageV();
        MainStageV.setOnCloseRequest((WindowEvent we) -> {
            System.out.println("Closing");
            EFX.Flush();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
            Platform.exit();
            System.exit(1);
        }); 
    }

    private void setTimer() {
        PauseTransition wait = new PauseTransition(Duration.minutes(5.0));
        wait.setOnFinished((e) -> {
            checkTime();
            wait.play();
        });
        wait.play();

    }
    
    private void GetLists() {
        //Platform.runLater(() -> EE = new empDB().getList());
        Platform.runLater(() -> EE = new EmpFileFXDB().getDimDataXML());
        //setEMPList(EE);
        //Platform.runLater(() -> V = new DB().getAllPromos());
        //setVIPList(V);
        //Platform.runLater(() -> VIPMultiplier = new DB().getVIPMultipier());
        //setVIPMultiplier(VIPMultiplier);
        Platform.runLater(() -> Receipts = DB.getReceipts());
        setReceipts(Receipts);
        Platform.runLater(() -> DB.disConnect());

    }
    
    private void setReceipts(ArrayList Receipts) {
        this.Receipts = Receipts;
    }
    
    public void checkActiveStatus() {
        if (newEFX.getEmpNumber() != null) {
            loginButton.setText("Active");
        }
    }
    
    public void checkTime(){
        CLOCKOUT.closeCafeScreen(DBSP.localMachine.toString());
        //System.out.println(String.valueOf(DF.format(MEMORYTEST.runGC()) + "%"));
        //percentage.setText(String.valueOf(DF.format(MEMORYTEST.runGC()) + "%"));
        if (!isLoggedIn()) {
            MainStageV.setTitle("Pojos Main " + String.valueOf(DF.format(MEMORYTEST.runGC()) + "%"));
        } else {
            MainStageV.setTitle("Pojos Main " + String.valueOf(DF.format(MEMORYTEST.runGC()) + "%") + " - Active Employee: " + newEFX.getNameF());
        }


    }
    
    public void buildMenuButton() {
        MenuItem item2 = new MenuItem(" Voucher Viewer ");
        MenuItem item3 = new MenuItem(" Employee Message ");
        MenuItem itemCorp = new MenuItem(" New Corp Data ");
        MenuItem DailyReports = new MenuItem(" Daily Reports Viewer ");
        MenuItem empNotes = new MenuItem(" Emp - Notes ");
        MenuItem item7 = new MenuItem(" Help Videos");
       // MenuItem test = new MenuItem(" Test");
        MenuItem chgScrn = new MenuItem(" Change Screen ");
        //MenuItem lockScreen = new MenuItem(" Lock Screen ");
        MenuItem cafeScreen = new MenuItem("Cafe");
        MenuItem item6 = new MenuItem(" Exit ");
        

        Menu menu6 = new Menu(" Mail Outs ");
        MenuItem rItem1 = new MenuItem(" New Members Last Month ");
        MenuItem rItem2 = new MenuItem(" Birthdays Next Month ");
        MenuItem emails = new MenuItem(" Email Addresses ");
        MenuItem rItem3 = new MenuItem(" Returns ");
        menu6.getItems().addAll(rItem1, rItem2, emails, rItem3);


        Menu menu7 = new Menu(" Club Settings ");
        MenuItem bridgeReceipts = new MenuItem(" Bridge Receipts ");
        MenuItem m7item2 = new MenuItem(" Receipts ");
        MenuItem closelock = new MenuItem(" Closed / Locked ");
        MenuItem m7item21 = new MenuItem(" New Member Greeting ");
        MenuItem m7item3 = new MenuItem(" Random Winner ");
        MenuItem m7item4 = new MenuItem(" VIP Promos ");
        MenuItem weekDay = new MenuItem(" Week Day ");
        MenuItem m7item41 = new MenuItem(" Notification Emails ");
        MenuItem eaterCodes = new MenuItem(" Ticket Eater Codes ");
        MenuItem m7item5 = new MenuItem(" Counter Screen Colors ");
        MenuItem m7item6 = new MenuItem(" Test Screen ");
        menu7.getItems().addAll(m7item2, m7item21, m7item3, m7item4, weekDay, bridgeReceipts, closelock, eaterCodes, m7item41, m7item5, m7item6);
        
        
        
        /* test.setOnAction((ActionEvent event) -> {
            try {
        
            HTMLViewerController_Page wController = (HTMLViewerController_Page) FXLOADER.getController();
            wController.COUNTERSTAGE = "1";
            wController.Receipts = Receipts;
            SC.loadSceneRemoveDepPane(MainStageV, "/messageBox/HTMLViewer_Page.fxml", depPane, 20.0, 20.0);

        
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );*/
        
        
        
        item2.setOnAction((ActionEvent event) -> {
            try {
                GOvoucherViewer(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );

        itemCorp.setOnAction((ActionEvent event) -> {
            try {
                GOCorpNew2(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );

        DailyReports.setOnAction((ActionEvent event) -> {
            try {
                GODailyReportsViewer(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );
        
        
        chgScrn.setOnAction((ActionEvent event) -> {
            try {
            //    if (!loginButtonPushed()) {
            //        return;
            //    }
            //if (!newEFX.employeeLevel("Corporate")) {
            //    return;
            //}

            SG.setCounterSettings();
            SG.setSettings(2);
            SG.setProp("Css", "99", 6);
            SG.setCounterProp("stage", "6");
            new SceneChanger_Main().MainStage(getStage(), "/views/CounterChooser.fxml", "Counter Chooser");
            } catch (IOException ex) {
            }
            //exitButton.fire();
        }
        );
        
        
        
        

        item7.setOnAction((ActionEvent event) -> {
            try {
                GOHelpVideos(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );
        closelock.setOnAction((ActionEvent event) -> {
            try {
                GOnewMemberMail(event, 5);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );

        eaterCodes.setOnAction((ActionEvent event) -> {
            try {
                GOEaterCodes(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );
        
        /*
        m7item1.setOnAction((ActionEvent event) -> {
            try {
                GOBonusTable(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );
        */

        m7item2.setOnAction((ActionEvent event) -> {
            try {
                GOGreetingsTable(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        m7item21.setOnAction((ActionEvent event) -> {
            try {
                GONewMemberGreet(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );
        
        m7item3.setOnAction((ActionEvent event) -> {
            try {
                GORandomWinner(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        );
        
        m7item4.setOnAction((ActionEvent event) -> {
            try {
                GOVIPPromos(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        bridgeReceipts.setOnAction((ActionEvent event) -> {
            try {
                GOBridgeReceipts(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );



        weekDay.setOnAction((ActionEvent event) -> {
            try {
                GOWeekDay(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );
        m7item41.setOnAction((ActionEvent event) -> {
            try {
                GOnotificationEmails(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        m7item5.setOnAction((ActionEvent event) -> {
            try {
                GOotherSettings(event, 1);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );
        m7item6.setOnAction((ActionEvent event) -> {
            try {
                GOotherSettings(event, 2);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        rItem1.setOnAction((ActionEvent event) -> {
            try {
                GOnewMemberMail(event, 1);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        rItem2.setOnAction((ActionEvent event) -> {
            try {
                GOnewMemberMail(event, 2);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        /*lockScreen.setOnAction((ActionEvent event) -> {
            GOlockScreen(1);
        } //bonus table
        );*/
        
        cafeScreen.setOnAction((ActionEvent event) -> {
            try {
                GOcafeScreen(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        
        empNotes.setOnAction((ActionEvent event) -> {
            GOEmployeeNotes(event);
        } //counter screens
        );
        
        emails.setOnAction((ActionEvent event) -> {
            try {
                GOnewMemberMail(event, 4);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );

        rItem3.setOnAction((ActionEvent event) -> {
            try {
                GOnewMemberMail(event, 3);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );
        
        item3.setOnAction((ActionEvent event) -> {
            try {
                GOtClockMessageScene(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //bonus table
        );
        
        
        
        item6.setOnAction((ActionEvent event) -> {
            exitButton.fire();
        } //exit
        );
        CONTEXTMENU.getItems().clear();
        CONTEXTMENU.getItems().addAll(item2, menu7, chgScrn, menu6, item3, item7, item6, cafeScreen, DailyReports, empNotes, itemCorp);

        menuItemsButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                CONTEXTMENU.show(menuItemsButton, boundsInSceneMenuButton.getMaxX() + 175.0, boundsInSceneMenuButton.getMaxY() + 12.0);
            }
        });
        menuItemsButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            CONTEXTMENU.show(menuItemsButton, event.getScreenX(), event.getScreenY());
        });
   }

    public void menuItemsButtonMouseOver() {
        CONTEXTMENU.show(menuItemsButton, boundsInSceneMenuButton.getMaxX() + 175.0, boundsInSceneMenuButton.getMaxY() + 12.0);
    }

    private void getStageV() {
        this.MainStageV = (Stage) gamesButton.getScene().getWindow();
    }
    
    private Stage getStage() {
        return MainStageV;
    }

//=================
 
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: break;
                    case F7: break;
                    case F8: break;
                    case F9: 
    {
        try {
            exitButtonPushed();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }
                        break;
                    case F10: break;
                    case F11: break;
                    case F12: break;
                    case ESCAPE: SC.resetPanes(depPane); break;
                default:
                    break;
                }
    }
    
    
    private void GOlockScreen(int i) {
        if (i == 1) {
            locked = true;
            corpButton.setDisable(true);
            empButton.setDisable(true);
            clockButton.setDisable(true);
            menuItemsButton.setDisable(true);
        } else {
            locked = false;
            corpButton.setDisable(false);
            empButton.setDisable(false);
            clockButton.setDisable(false);
            menuItemsButton.setDisable(false);
        }
    }
    
    
   
    public void setColorsButton() {
        if (colorsButton.getText().equals("Theme: null")) {
            SG.setSettings(2);
            System.out.println("Changing to backOffice Theme");
        } else {
        root.getStylesheets().clear();
        root.getStylesheets().add(CSSC.GetCssNextStyle(2));
        root.applyCss();
        try {
            CSSC.CssChange(2);
        } catch (IOException ex) {
            System.out.println("Erro with Css: " + ex);
        }
        }
        colorsButton.setText("Theme: " + CSSC.GetCssName(2));
        CSSC.getFiles();
}
 
     private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        for (int y = 0; y < EE.size(); y++) {
            if (n.trim().equals(EE.get(y).getEmpNumber())) {
                empValid = true;
                newEFX = new empFX(EE.get(y).getNameF(), EE.get(y).getNameL(), EE.get(y).getEmpNumber(), EE.get(y).getVAmt(), EE.get(y).getBdayresos(), EE.get(y).getChangerEdit(), EE.get(y).getEmpID(), EE.get(y).getGProb(), EE.get(y).getActive(), EE.get(y).getArcade(), EE.get(y).getBcarsales());
            }
        }
        return empValid;
    }

 
    public boolean isLoggedIn() {
        boolean t = false;
        if (loginButton.getText().equals("Active")) {
            t = true;
        } else{
            loginButton.setText("Login");
            newEFX = null;
            t = false;
        }
        return t;
    }

    public void loginButtonPushed(ActionEvent event) throws IOException { 
        loginButton.setText("Login");
        SC.setGameNumber(null);
        EFX.Flush();
        //Stage stage = (Stage) gamesButton.getScene().getWindow();
        MainStageV.setTitle("Pojos Main - Active Employee: NULL");
        
        
            FXMLLoader Loader = new FXMLLoader();
            QTYEMPTouchScreenLoginController controller = Loader.<QTYEMPTouchScreenLoginController>getController();
            controller.tString = "Enter Employee Number";
            controller.ctString = "Enter Employee Number";
            controller.removeButtonTF = false;
            SC.getpassWord(stageV, "/views/counterPopUp/QTYEMPTouchScreenLogin.fxml", "How Many Days", "How Many Days:", boundsInSceneGamesButton.getMinX() + 350.0, boundsInSceneGamesButton.getMinY()+300.0);
        
        
        
        
        
        
        
        //boundsInScene = gamesButton.localToScene(gamesButton.getBoundsInLocal());
        //SC.getpassWord(MainStageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", boundsInSceneGamesButton.getMinX() + 350.0, boundsInSceneGamesButton.getMinY()+300.0);
        if (!isEMPValidInArrayList(SC.getGameNumber())) {
        //if (!EFX.isemployeevalid(SC.getGameNumber())) {
        System.out.println("Here we are in the place 1 " + newEFX.getEmpNumber());
           return;
        } else {
        if (!newEFX.getEmpNumber().equals("Number") && !newEFX.getEmpNumber().isEmpty()) {
        //if (!EFX.getEmpNumber().equals("Number") && !EFX.getEmpNumber().isEmpty()) {
            try {
                //here is where we need to get the email of the owner
                EMPDB.Connect();
                E = EMPDB.getEmployee(SC.getGameNumber());
                //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, thisone, E.getEmail());
                EMPDB.disConnect();
            } catch (SQLException ex) {
               System.out.println(ex);
            }
            
            MainStageV.setTitle("Pojos Main " + String.valueOf(DF.format(MEMORYTEST.runGC()) + "%") + " - Active Employee: " + newEFX.getNameF());
            loginButton.setText("Active");
        } else {
            loginButton.setText("Login");
            MainStageV.setTitle("Pojos Main " + String.valueOf(DF.format(MEMORYTEST.runGC()) + "%"));

        } 
        }
    }

//=========================

    //DONE
    public void GOFXBdays(ActionEvent event) throws IOException {
        
        GOHelpVideos(event);    
        /*
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Birthdays")) {
            return;
        }
        EFX.SetEmployeeFX(EFX.getNameF(), EFX.getNameL(), EFX.getEmpNumber(), EFX.getVAmt(), EFX.getGProb(), EFX.getBdayresos());
        BDayFXController wController = (BDayFXController) FXLOADER.getController();
        wController.boundsPlusX = 125;
        wController.boundsPlusY = -70;
        wController.sc = SC;
        wController.dbsp = DBSP;
        wController.eFXX = EFX;
        wController.fxloader = FXLOADER;
        wController.mBox = MBOX;
        wController.jmail = JMAIL;
        wController.cssPath = CSSPATH;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/parties/BDayFX.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        */
    }
    //DONE
    public void GOEmpViewer(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Employee")) {
            return;
        }
        EmpViewerController wController = (EmpViewerController) FXLOADER.getController();
        wController.MGR = newEFX.nameF;
        wController.cssPath = CSSPATH;
        wController.sc = SC;
        wController.fxmlLoader = FXLOADER;
        wController.eFX = EFX;
        wController.dbsp = DBSP;
        wController.ownerEmail = E.getEmail();
        wController.Receipts = Receipts;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/timeclock/EmpViewer.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
    }
    
    
    public void GOEmployeeNotes(ActionEvent event) {
        try {
            if (!isLoggedIn()) {
                loginButtonPushed(event);
            }
            if (!newEFX.employeeLevel("Corporate") ) {
                return;
            }
            EFX.SetEmployeeFX(EFX.getNameF(), EFX.getNameL(), EFX.getEmpNumber(), EFX.getVAmt(), EFX.getGProb(), EFX.getBdayresos());
            EmpDetailPopUpCounterController wController = (EmpDetailPopUpCounterController) FXLOADER.getController();
            wController.cssPath = CSSPATH;
            wController.screen = "CORP NOTE";
            wController.dbsp = DBSP;
            wController.eFXX = EFX;
            wController.sc = SC;
            wController.EE = this.EE;
            wController.empName = newEFX.getNameF();
            wController.updateVar = "0";
            getStageV();
            SC.getpassWord(MainStageV, "/views/timeclock/EmpDetailPopUpCounter.fxml", null, null, 400.00, 175.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        //sc.goToScene("EmpDetailCounter", stageV, eFX.getNameF(), null, boundsInScene);
        //runDimFile = true; THIS IS TO TELL THE EMPLOYEE VIEWER TO RUN THE DIM FILE ON EXIT.
        return;

    }
    
    
    //DONE
    public void GOCorpNew2(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }
        CorpForm_MainFXController wController = (CorpForm_MainFXController) FXLOADER.getController();
        wController.cssPath = CSSPATH;
        wController.MGR = newEFX.nameF;
        wController.dbsp = DBSP;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/corp/CorpForm_Main.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
    }


    public void GOHelpVideos(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }
        HelpVideosViewerController wController = (HelpVideosViewerController) FXLOADER.getController();
        wController.cssPath = CSSPATH;
        wController.MGR = newEFX.nameF;
        wController.Receipts = Receipts;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/HelpVideosViewer.fxml", CSSPATH, CSSPATH, boundsInSceneMenuButton.getMinX() + 230.0, boundsInSceneMenuButton.getMinY() - 30.0);
    }

    public void GOEaterCodes(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }
        TicketEaterCodesController wController = (TicketEaterCodesController) FXLOADER.getController();
        wController.cssPath = CSSPATH;
        wController.MGR = newEFX.nameF;
        wController.Receipts = Receipts;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/TicketEaterCodes.fxml", CSSPATH, CSSPATH, boundsInSceneMenuButton.getMinX() + 230.0, boundsInSceneMenuButton.getMinY() - 30.0);
    }



    public void GOotherSettings(ActionEvent event, int whichOne) throws IOException {

        switch (whichOne) {
            case 1:
                thisone = "otherSettings";
                if (!isLoggedIn()) {
                    loginButtonPushed(event);
                }
                if (!newEFX.employeeLevel(thisone)) {
                    return;
                }
                getStageV();
                SC.getStagestyleUndecorated(MainStageV, "/views/settings/CSSView.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
                //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
                break;
            case 2:
                thisone = "CSSEditor";
                getStageV();
                SC.getStagestyleUndecorated(MainStageV, "/views/settings/CSSEditR.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
                break;
        }
    }

    public void GORandomWinner(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }       
        System.out.println("Here in Random WInner");
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/RandReciepts.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
    }
   
    public void GODailyReportsViewer(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }       
        getStageV();
        DayReportsViewerController wController = (DayReportsViewerController) FXLOADER.getController();
        //wController.cssPath = CSSPATH;
        //wController.dbsp = DBSP;
        wController.emailAddress = E.getEmail();
        //wController.sc = SC;
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/DayReportsViewer.fxml", CSSPATH, CSSPATH, 200.0, 60.0);
    }

    public void GOVIPPromos(ActionEvent event) throws IOException {
        thisone = "VIPPromos";
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel(thisone)) {
            return;
        }       
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/VIPPromos.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
       // goToScene(thisone, stageV);
    }
    
    public void GOBridgeReceipts(ActionEvent event) throws IOException {
        thisone = "VIPPromos";
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel(thisone)) {
            return;
        }       
        getStageV();
        EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
        BridgeReceiptsBackRoomController wController = (BridgeReceiptsBackRoomController) FXLOADER.getController();
        wController.cssC = CSSPATH;
        wController.dbsp = DBSP;
        wController.eFX = EFX;
        wController.sc = SC;
        wController.rCeipts = Receipts;

        SC.getStagestyleUndecorated(MainStageV, "/views/settings/BridgeReceiptsBackRoom.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
       // goToScene(thisone, stageV);
    }


    public void GOWeekDay(ActionEvent event) throws IOException {
        thisone = "WeekDay";
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("VIPPromos")) {
            return;
        }       
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/WeekDays.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
       // goToScene(thisone, stageV);
    }

    public void GOBonusTable(ActionEvent event) throws IOException {
        thisone = "Bonus Table";
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel(thisone)) {
            return;
        }       
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/BonusTable.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
       // goToScene(thisone, stageV);
    }
    
    //DONE
    public void GOnotificationEmails(ActionEvent event) throws IOException {
        thisone = "GreetingsClub";
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel(thisone)) {
            return;
        }       
        getStageV();
        notificationEmailsController wController = (notificationEmailsController) FXLOADER.getController();
        wController.csspath = CSSPATH;
        wController.dbsp = DBSP;
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/nEmails.fxml", "Number", "Enter Employee Number:", 220.00, 50.00);
        //SC.getpassWord(stageV, "/views/settings/nEmails.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
       // goToScene(thisone, stageV);
    }

    public void GOGreetingsTable(ActionEvent event) throws IOException {
        thisone = "GreetingsClub";
        System.out.println("Starting " + thisone);
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel(thisone)) {
            System.out.println("GreetingsClub");
            return;
        }       
        getStageV();
        System.out.println("Starting");
        //GreetingsControllerClub wController = (GreetingsControllerClub) FXLOADER.getController();
        GreetingsControllerClub1 wControll = (GreetingsControllerClub1) FXLOADER.getController();
        wControll.OwnerEmail = E.getEmail();
        SC.getStagestyleUndecorated(MainStageV, "/views/settings/GreetingsClub.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
        //goToScene(thisone, stageV);
    }
    
    
    //(DONE)
    public void GOmembers(ActionEvent event) throws IOException {
   if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Members")) {
            return;
        }
        MemberViewController wController = (MemberViewController) FXLOADER.getController();
        wController.SC = SC;
        wController.dbsp = DBSP;
        wController.cssPath = CSSPATH;
        wController.DB = DB;
        wController.FXLOADER = FXLOADER;
        wController.EFX = EFX;
        wController.rCeipts = Receipts;
        wController.jmail = JMAIL;
        wController.mbox = MBOX;
        wController.chkbalance = CHKBALANCE;
        wController.eID = newEFX.empNumber;
        wController.member = MEMBER;
        wController.d = MEMBER;
        wController.mt = MEMTICK;
        wController.newEFX = newEFX;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/MemberView.fxml", CSSPATH, CSSPATH, 275.0, 10.0);
        //SC.goToScene2("Members", stageV, EFX.getNameF(), depPane,  boundsInSceneMenuButton.getMinX() + 250.0, boundsInSceneMenuButton.getMinY());
       // goToScene("Members", stageV);
        return;

    }

    //(DONE)
    public void GOinventory(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Inventory")) {
            return;
        }
        //FXMLLoader fxmlLoader = new FXMLLoader();
        ToysMainController wController = (ToysMainController) FXLOADER.getController();
        wController.cssPath = CSSPATH;
        wController.MGR = newEFX.nameF;
        wController.sc = SC;
        wController.mBox = MBOX;
        wController.fxmlLoader = FXLOADER;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/toys/ToysMain.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
    }


    //(DONE)
    public void GOcafeScreen(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Inventory")) {
            return;
        }
        //FXMLLoader fxmlLoader = new FXMLLoader();
        CToysMainController wController = (CToysMainController) FXLOADER.getController();
        wController.cssPath = CSSPATH;
        wController.MGR = newEFX.nameF;
        wController.sc = SC;
        wController.mBox = MBOX;
        wController.fxmlLoader = FXLOADER;
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/cafe/CToysMain.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
    }




    //DONE
    public void GOnewMemberMail(ActionEvent event, int whichOne) throws IOException {
        String thisone = null;
        //FXMLLoader fxmlLoader = new FXMLLoader();
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("newMemberMail")) {
            return;
        }
        switch (whichOne) {
            //DONE
            case 1:
                thisone = "newMemberMail";
                NewMemberMailController wControllerNewMemberMail = (NewMemberMailController) FXLOADER.getController();
                wControllerNewMemberMail.cssPath = CSSPATH;
                wControllerNewMemberMail.mBox = MBOX;
                wControllerNewMemberMail.dbsp = DBSP;
                wControllerNewMemberMail.jmail = JMAIL;
                wControllerNewMemberMail.sc = SC;
                wControllerNewMemberMail.db = DB;
                wControllerNewMemberMail.fxloader = FXLOADER;
                wControllerNewMemberMail.EFX = EFX;
                
                SC.getStagestyleUndecorated(MainStageV, "/views/clubmail/NewMemberMail.fxml", "Number", "New Member Mail", 220.0, 50.0);
                break;
            case 2:
                //DONE
                thisone = "bDaysNextMonth";
                BdaysNextMonthController wControllerBdaysNextMonth = (BdaysNextMonthController) FXLOADER.getController();
                wControllerBdaysNextMonth.cssPath = CSSPATH;
                wControllerBdaysNextMonth.mBox = MBOX;
                wControllerBdaysNextMonth.dbsp = DBSP;
                wControllerBdaysNextMonth.jmail = JMAIL;
                SC.getStagestyleUndecorated(MainStageV, "/views/clubmail/BdaysNextMonth.fxml", "Number", "Birthdays Next Month", 220.0, 50.0);
                break;
            case 3:
                //DONE
                BdayMailReturnsController wController = (BdayMailReturnsController) FXLOADER.getController();
                wController.en = newEFX.getEmpNumber();
                wController.db = DB;
                wController.cssPath = CSSPATH;
                wController.sc = SC;
                wController.dbsp = DBSP;
                SC.getStagestyleUndecorated(MainStageV, "/views/clubmail/BdayMailReturns.fxml", "Number", "Enter Employee Number:", 220.00, 50.00);
                break;
            case 4:
                //DONE
                EmailsController wControllerEmails = (EmailsController) FXLOADER.getController();
                wControllerEmails.Receipts = Receipts;
                //wControllerEmails.contextMenu = CONTEXTMENU;
                wControllerEmails.db = DB;
                wControllerEmails.cssPath = CSSPATH;
                wControllerEmails.eFX = EFX;
                wControllerEmails.mBox = MBOX;
                wControllerEmails.sc = SC;
                wControllerEmails.dbsp = DBSP;
                SC.getStagestyleUndecorated(MainStageV, "/views/clubmail/Emails.fxml", "Number", "Enter Employee Number:", 220.00, 50.00);
                break;
            case 5:
                thisone = "closelock";
                ClosedLockedController wControllerCloseLock = (ClosedLockedController) FXLOADER.getController();
                wControllerCloseLock.cssPath = CSSPATH;
                wControllerCloseLock.mBox = MBOX;
                wControllerCloseLock.dbsp = DBSP;
                wControllerCloseLock.jmail = JMAIL;
                wControllerCloseLock.sc = SC;
                wControllerCloseLock.db = DB;
                wControllerCloseLock.fxloader = FXLOADER;
                wControllerCloseLock.EFX = EFX;
                
                SC.getStagestyleUndecorated(MainStageV, "/views/clubmail/ClosedLocked.fxml", "Number", "New Member Mail", 220.0, 50.0);
                break;
        }
        //getStageV();
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
        // goToScene(thisone, stageV);
    }

    public void GOcorp(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }
        newEFX.createPdoxTxtFile(newEFX.getEmpNumber());
        //Runtime.getRuntime().exec("Java -jar J:\\Kidsclub\\Corp2\\Corp2.jar");
        System.out.println("HERE IT IS . . . " + "Java -jar" + PATHCORP2 + "\\Corp2.jar");
        Runtime.getRuntime().exec("Java -jar " + PATHCORP2 + "\\Corp2.jar");
        return;
    }

    //DONE
    public void GOgameProbs(ActionEvent event){
        try {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Game Prob's")) {
            return;
        }
        getStageV();
        //FXMLLoader fxmlLoader = new FXMLLoader();
        GamesProblemsController wController = (GamesProblemsController) FXLOADER.getController();
        wController.cssPath = CSSPATH;        
        wController.eFXX = EFX; 
        wController.newEFX = newEFX;
        wController.dbsp = DBSP;
        SC.getStagestyleUndecorated(MainStageV, "/views/gameproblems/GamesProblems.fxml", null, null, boundsInSceneMenuButton.getMinX()+250.0, boundsInSceneMenuButton.getMinY());
        } catch(Exception e) {
            new Mail_JavaFX1().sendEmailTo("Game Problem Error", "-1-  " +  e  + dbsp.pathNameXML, "errors");
        }
        //SC.goToScene2("Game Prob's", stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton.getMinX()+250.0, boundsInSceneMenuButton.getMinY());
        return;
    }
    
    //DONE
    public void GOtimeClock(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Time Clock")) {
            return;
        }
        getStageV();
        TClockViewerController wController = (TClockViewerController) FXLOADER.getController();
        wController.cssPath = CSSPATH;        
        wController.eFX = EFX; 
        wController.dbsp = DBSP;
        wController.sc = SC;
        SC.getStagestyleUndecorated(MainStageV, "/views/timeclock/TClockViewer.fxml", CSSPATH, CSSPATH, 220.0, 50.0);
        //SC.goToScene("Time Clock", stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
       // goToScene("Time Clock", stageV);
         return;
    }
    
    public void GOgames(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Games")) {
            return;
        }        
        //EFX.SetEmployeEFX(EFX.getNameF(), EFX.getNameL(), EFX.getEmpNumber(), EFX.getVAmt(), EFX.getGProb(), EFX.getBdayresos());
        getStageV();
        //FXMLLoader fxmlLoader = new FXMLLoader();
        GameMainController wController = (GameMainController) FXLOADER.getController();
        wController.cssPath = CSSPATH;        
        wController.eFXX = EFX; 
        wController.dbsp = DBSP;
        wController.sc = SC;
        SC.getStagestyleUndecorated(MainStageV, "/views/games/GameMain.fxml", null, null, 260.0, 50.0);
        //SC.goToScene("Games", stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
        return;

    }
    
    /*public void GOcafeScene(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!EFX.employeeLevel("Time Clock")) {
            return;
        }
        getStageV();
        SC.getStagestyleUndecorated(stageV, "/views/timeclock/TClockViewer.fxml", CSSPATH, CSSPATH, 200.0, 50.0);
        //SC.goToScene("newTclock", stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
         return;

    }*/

    public void GOtClockMessageScene(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Time Clock")) {
            return;
        }
        getStageV();
        SC.getStagestyleUndecorated(MainStageV, "/views/timeclock/MessageEditor.fxml", CSSPATH, CSSPATH, 200.0, 50.0);
        //SC.goToScene("Message Editor", stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
         return;

    }

    //BELOW ARE ALL SEITCHED TO THE SCENECHANGER CODE TO OPEN THE SCENES AND NOT THE PASS THRU CODE.
    
    public void GONewMemberGreet(ActionEvent event) throws IOException {
        thisone = "NewMemberGreet";
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel(thisone)) {
            return;
        }       
        //getStageV();
        SC.getStagestyleUndecorated(getStage(), "/views/settings/MemberGreet.fxml", "Number", "Enter Employee Number:", 220.00, 50.00);
        //SC.goToScene(thisone, stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
        //goToScene(thisone, stageV);
    }
    
    //DONE
    public void GOvoucherViewer(ActionEvent event) throws IOException {
        if (!isLoggedIn()) {
            loginButtonPushed(event);
        }
        if (!newEFX.employeeLevel("Games")) {
            return;
        }
        getStageV();
        FXVoucherViewerController wController = (FXVoucherViewerController) FXLOADER.getController();
        wController.cssPath = CSSPATH;        
        wController.dbsp = DBSP;
        SC.getStagestyleUndecorated(MainStageV, "/views/vouchers/FXVoucherViewer.fxml", "Number", "Birthdays Next Month", 220.0, 50.0);
        //SC.getpassWord(getStage(), "/views/vouchers/FXVoucherViewer.fxml", "Number", "Enter Employee Number:", boundsInSceneMenuButton.getMaxX() + 200.00, boundsInSceneMenuButton.getMaxY());
        //SC.goToScene("VoucherViewer", stageV, EFX.getNameF(), depPane, boundsInSceneMenuButton);
         return;

    }   
        
    public void exitButtonPushed() throws FileNotFoundException {   
        if (locked) {
            GOlockScreen(0);
            isLoggedIn();
            return;
        }
        if (depPane.isVisible()) {
            SC.resetPanes(depPane);     
        } else {
        EFX.Flush();
            new clockOutAtNightFX().deleteTXTFilesFromToday();
            getStage().close();
            System.exit(1);
        }
    }

    
}
