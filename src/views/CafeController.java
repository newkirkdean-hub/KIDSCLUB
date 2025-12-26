package views;




import views.timeclock.EmpDetailPopUpCounterController;
import views.vouchers.FXVoucherDialogController;
import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import XML_Code.readXMLToArrayList;
import XML_Code.writeArrayListToXMLFile;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import clockoutatnight.clockOutAtNightFX;
import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import messageBox.messageBox;
import models.club.DB;
import models.club.rCeipts;
import commoncodes.GetReceipts;
import commoncodes.MemoryTest;
import java.awt.Desktop;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messageBox.HTMLViewerController_Page;
import models.club.LastMemberTransaction;
import models.timeclock.EmpFileFXDB;
import models.voucher.EaterCodes;
import pReceipts.prtReceiptsFX;
import pWordFX.empDB;
import pWordFX.empFX;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;
import views.settings.BridgeReceiptsController;
import views.shiftNotes.ShiftNotesController;
import views.timeclock.TClockDialogFXController;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class CafeController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private Button voucherButton;    
    @FXML private Button timeClockButton;    
    @FXML private Button birthdayButton;    
    @FXML private Button VIPButton;    
    @FXML private Button refill;
    @FXML private Button menuItemsButton;
    @FXML private Label menuItemsLabel;
    @FXML private Pane depPane;
    @FXML private Stage stageV;

    
    ContextMenu CONTEXTMENU = new ContextMenu();
    private static final dbStringPath DBSP = new dbStringPath();
    private static final FXMLLoader FXLOADER = new FXMLLoader();
    private static final employeeFX EFX = new employeeFX();
    private static final DB DB = new DB();
    private static final SceneChanger_Main SC = new SceneChanger_Main();
    private static final messageBox MBOX = new messageBox();
    private static final Mail_JavaFX1 JMAIL = new Mail_JavaFX1();
    private static final MemoryTest MEMORYTEST = new MemoryTest();
    private static final clockOutAtNightFX CLOCKOUTATNIGHT = new clockOutAtNightFX();
    private static ArrayList<LastMemberTransaction> lastMember = new ArrayList();
    private static LastMemberTransaction LMT = new LastMemberTransaction("2007433", "4793", "DayStart", "Bridge", "DayStart");
    private static int iLastTran = 0;

    private static int VIPMultiplier;
    private static ArrayList<rCeipts> Receipts;
    private static ArrayList<empFX> EE;
    private static ArrayList<String> V;
    private static ArrayList<EaterCodes> TEC; //TicketEaterCodes


    private static final cssChanger CSS = new cssChanger();
    private static final prtReceiptsFX PRTSODA = new prtReceiptsFX();
    private static empFX newEFX = null;
    private static String cssPath = null, counterScene = null;
    private static Bounds boundsInScene;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = voucherButton.localToScene(voucherButton.getBoundsInLocal());
        DBSP.setName();
        setKeyCodes();
        setTimer();
        buildMenuButton();
        cssPath = CSS.cssPath();
        root.getStylesheets().add(cssPath); 
        Platform.runLater(() -> getStageV());
        Platform.runLater(() -> startLastMemberTransaction());
        Platform.runLater(()->setCloseCatch());
        Platform.runLater(() -> GetLists());
        Platform.runLater(() -> checkTime());
    }    
 
    
    
    
    public void GOBirthdayButton(ActionEvent evt) { 
        try {
            if (!loginButtonPushed()) {
            return;
            }
            if (!newEFX.employeeLevel("Birthdays")) {
            return;
            }
            //HTMLViewerController_Page wController = (HTMLViewerController_Page) FXLOADER.getController();
            //wController.COUNTERSTAGE = counterScene;
            //wController.Receipts = Receipts;
            //SC.loadSceneRemoveDepPane(stageV, "/messageBox/HTMLViewer_Page.fxml", depPane, 20.0, 20.0);
            Desktop.getDesktop().browse(new URL("https://pojosadmin.venuesumo.app/bookings/calendar/bookings/").toURI());
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CafeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;

    }     
    
    
    public void GOBirthdayButtonTest() { 
        try {
            if (!loginButtonPushed()) {
            return;
            }
            if (!newEFX.employeeLevel("Birthdays")) {
            return;
            }
            HTMLViewerController_Page wController = (HTMLViewerController_Page) FXLOADER.getController();
            wController.COUNTERSTAGE = counterScene;
            wController.Receipts = Receipts;
            SC.loadSceneRemoveDepPane(stageV, "/messageBox/HTMLViewer_Page.fxml", depPane, 20.0, 20.0);
            //Desktop.getDesktop().browse(new URL("https://pojosadmin.venuesumo.app/bookings/calendar/bookings/").toURI());
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return;


    }     
    
    
    
    public void GOShiftNotes() {
        try {
            if (!loginButtonPushed()) {
                return;
            }

            if (!newEFX.employeeLevel("Game Prob's")) {
                return;
            }
            //EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
            ShiftNotesController wController = (ShiftNotesController) FXLOADER.getController();
            wController.cssPath = cssPath;
            wController.eFXX = EFX;
            wController.newEFX = newEFX;
            wController.dbsp = DBSP;
            wController.screenName = "CAFE";
            getStageV();
            SC.loadSceneRemoveDepPane(stageV, "/views/shiftNotes/ShiftNotes.fxml", depPane, boundsInScene.getMinX() + 170.0, boundsInScene.getMinY() + 50.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        return;

    }
    
    
    
    
            
    public void GOVoucherButton(ActionEvent evt) {  
        try {
        if (!loginButtonPushed()) {return;}
        if (!newEFX.employeeLevel("Vouchers")) {return;}
        EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getEmpID());
        FXVoucherDialogController wController = (FXVoucherDialogController) FXLOADER.getController();
        String t = new GetReceipts().getReceiptsNumber(Receipts, "Voucher_Reason");
        wController.dbsp = DBSP;
        wController.css = cssPath;
        wController.mBox = MBOX;
        wController.receipts = Receipts;
        wController.eFXX = EFX;
        wController.TEC = TEC;
        wController.jmail = JMAIL;
        wController.vAmt = EFX.getVAmt();
        wController.rCeiptvMount = Double.valueOf(t);
        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/vouchers/FXVoucherDialog.fxml", depPane, boundsInScene.getMaxX()+300.0, boundsInScene.getMaxY()+100.0);
        } catch (IOException ex) {System.out.println("Login Button Pushed Error: " + ex);}
        return;
    }          
    
    public void GOVIPButton(ActionEvent evt) throws IOException {                                          
        MemVIPController wController = (MemVIPController) FXLOADER.getController();
        wController.sc = SC;
        wController.db = DB;
        wController.dbsp = DBSP;
        wController.css = cssPath;
        wController.E = this.EE;
        wController.V = this.V;
        wController.rCeipts = Receipts;
        wController.eFX = EFX;
        wController.mBox = MBOX;
        wController.jmail = JMAIL;
        wController.VIPMultiplier = this.VIPMultiplier;
        wController.lastmember = lastMember;
        wController.iLastTran = this.iLastTran;
        SC.loadSceneRemoveDepPane(stageV, "/views/MemVIP.fxml", depPane, 200.0, 100.0);
    }                
    
    public void GOTimeClockButton() {
        try {
        TClockDialogFXController wController = (TClockDialogFXController) FXLOADER.getController();
        wController.cssPath = cssPath;
        wController.dbsp = DBSP;
        wController.mbox = MBOX;
        wController.eFX = EFX;
        wController.jmail = JMAIL;
        wController.EE = EE;
            SC.getpassWord(stageV, "/views/timeclock/TClockDialogFX.fxml", "Number", "Enter Employee Number:", boundsInScene.getMinX() + 350.0, boundsInScene.getMinY());
        } catch (IOException ex) {
            System.out.println(ex);
        }
     }
    
    public void GOEmployeeNotes() {
    try {
            if (!loginButtonPushed()) {
                return;
            }
            if (newEFX.getArcade() != 2 && newEFX.getArcade() != 3 ) {
                return;
            }
        EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
        EmpDetailPopUpCounterController wController = (EmpDetailPopUpCounterController) FXLOADER.getController();
        wController.cssPath = cssPath;
        wController.dbsp = DBSP;
        wController.eFXX = EFX;
        wController.screen = "CAFE NOTE";
        wController.sc = SC;
        wController.EE = this.EE;
        wController.empName = EFX.getNameF();
        wController.updateVar = "0";
        getStageV();
        SC.getpassWord(stageV, "/views/timeclock/EmpDetailPopUpCounter.fxml", null, null, 450.0, 175.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        return;
        
    }
    
    public void GOBridgeReceipts() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        //if (!newEFX.employeeLevel("Super")) {
        //    return;
        //}
        getStageV();
        EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
        BridgeReceiptsController wControllerC = (BridgeReceiptsController) FXLOADER.getController();
        wControllerC.WHSTAGE = "";
        stageV.setTitle("Cafe");
        wControllerC.WHSTAGE = stageV.getTitle();
        wControllerC.cssC = cssPath;
        wControllerC.superLevel = newEFX.getGProb();
        wControllerC.eFX = EFX;
        System.out.println("From Cafe: " + stageV.getTitle());

        //wController.MGR = eFX;        
        try {
            SC.getpassWord(stageV, "/views/settings/BridgeReceipts.fxml", null, null, 450.0, 100.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene2("Announcements", stageV, EFX.getNameF(), depPane, boundsInScene.getMinX(), boundsInScene.getMinY()-20.0);
        return;

    }
    
    public void GOCorpEmail() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (newEFX.getArcade() != 2 && newEFX.getArcade() != 3) {
                System.out.println(newEFX.getArcade());
                return;
            }
            EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
            EmpDetailPopUpCounterController wController = (EmpDetailPopUpCounterController) FXLOADER.getController();
            wController.cssPath = cssPath;
            wController.dbsp = DBSP;
            wController.eFXX = EFX;
            wController.screen = "CORP EMAIL";
            wController.rCeipts = Receipts;
            wController.sc = SC;
            wController.EE = this.EE;
            wController.empName = EFX.getNameF();
            wController.updateVar = "1";
            getStageV();
            SC.getpassWord(stageV, "/views/timeclock/EmpDetailPopUpCounter.fxml", null, null, 400.00, 175.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        //sc.goToScene("EmpDetailCounter", stageV, eFX.getNameF(), null, boundsInScene);
        //runDimFile = true; THIS IS TO TELL THE EMPLOYEE VIEWER TO RUN THE DIM FILE ON EXIT.
        return;

    }


    
    
    
    ///LASTMEMBER --------------------------------------------------------------
    ///

    private void startLastMemberTransaction() {
        try {
        //lastMember.add(iLastTran, LMT);
        lastMember = new readXMLToArrayList().getLastMemberTranList("xLM_" + DBSP.localMachine.getHostName());
        iLastTran = lastMember.size()-1;
        } catch(Exception e ) {
            System.out.println("NO FILE FOUND");
            lastMember.add(iLastTran, LMT);
            new writeArrayListToXMLFile().writeLastMemberTransaction(lastMember, DBSP.localMachine.getHostName());
            lastMember = new readXMLToArrayList().getLastMemberTranList(DBSP.localMachine.getHostName());
        }

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
    
    public void prtPitcher(){
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Family Focus Button Pushed Error: " + ex);
        }
        PRTSODA.printPitcherSoda(newEFX.getNameF());
    }
    

    public boolean loginButtonPushed() throws IOException { 
        Boolean GO = false;
        SC.setGameNumber(null);
        EFX.Flush();
        getStageV();

        SC.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", boundsInScene.getMinX() + 350.0, boundsInScene.getMinY());
        if (!isEMPValidInArrayList(SC.getGameNumber())) {
            GO = false;
        } else {
            if (!newEFX.getEmpNumber().equals("Number") || !newEFX.getEmpNumber().isEmpty()) {
                GO = true;
            } else {
                GO = false;
            }
        }
        return GO;

    }

    private void setTimer() {
        PauseTransition pTransition = new PauseTransition(Duration.minutes(2.0));
        pTransition.setOnFinished((e) -> {
            checkTime();
            pTransition.play();
        });
        pTransition.play();

    }
    
    public void checkTime(){
        CLOCKOUTATNIGHT.closeCafeScreen(DBSP.localMachine.toString());
    }
    
    
    private void GetLists() {
        //Platform.runLater(() -> EE = new empDB().getList());
        //setEMPList(EE);
        Platform.runLater(() -> EE = new EmpFileFXDB().getDimDataXML());
        setEMPList(EE);
        Platform.runLater(() -> V = DB.getAllPromos());
        setVIPList(V);
        Platform.runLater(() -> VIPMultiplier = DB.getVIPMultipier());
        setVIPMultiplier(VIPMultiplier);
        Platform.runLater(() -> Receipts = DB.getReceipts());
        setReceipts(Receipts);
        Platform.runLater(() ->TEC = new readXMLToArrayList().getTicketEaterCodesXML());
        //setReceipts2(Receipts2);
        Platform.runLater(() -> DB.disConnect());

    }
    
    
    
    
    public void actionPerformed(ActionEvent e){
     checkTime();
  }

    
    private void setVIPList(ArrayList V) {
        this.V = V;
    }

    private void setEMPList(ArrayList E) {
        this.EE = E;
    }

    
    private void setReceipts(ArrayList Receipts) {
        this.Receipts = Receipts;
    }
    
    //private void setReceipts2(ArrayList Receipts2) {
    //    this.Receipts2 = Receipts2;
    //}

    
    private void setVIPMultiplier(int m){
        this.VIPMultiplier = m;
    }
    
    
    private void getStageV() {
        stageV = (Stage) voucherButton.getScene().getWindow();    
        System.out.println(stageV.getTitle());
        System.out.println(new settingsFXML().getCounterSettings("stage"));
        if (new settingsFXML().getCounterSettings("stage").equals("1")) {
            counterScene = "1";
        } else {
            counterScene = "2";
        }
    }

    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F1) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F6) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F8) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F9) {keyListener(ke);}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke);}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: GOBridgeReceipts();break;
                    case F2: VIPButton.fire(); break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: birthdayButton.fire(); break;
                    case F7: voucherButton.fire(); break;
                    case F8: timeClockButton.fire(); break;
                    case F9: GOBridgeReceipts(); break;
                    case F10: break;
                    case F11: break;
                    case F12: break;
                    case ENTER: break;
                    case ESCAPE: break;
                default:
                    break;
                }
}
    
    public void buildMenuButton() {
        MenuItem refreshList = new MenuItem(" Refresh List's ");
        MenuItem empNotes = new MenuItem(" Emp - Notes ");
        MenuItem corpEmail = new MenuItem(" Corp - Email ");
        MenuItem chgScrn = new MenuItem(" Change Screen ");
        MenuItem receipts = new MenuItem(" Receipts F9");
        MenuItem bday = new MenuItem(" Bday");
        MenuItem exit = new MenuItem("      Exit     ");


        
        refreshList.setOnAction((ActionEvent event) -> {
            EE.removeAll(EE);
            V.removeAll(V);
            VIPMultiplier = 0;
            GetLists();
        } //clock in out
        );

        exit.setOnAction((ActionEvent event) -> {
            exitProcessDO();
        } //clock in out
        );
        
        empNotes.setOnAction((ActionEvent event) -> {
            GOEmployeeNotes();
        } //counter screens
        );

        bday.setOnAction((ActionEvent event) -> {
            GOBirthdayButtonTest();
        } //counter screens
        );

        corpEmail.setOnAction((ActionEvent event) -> {
            GOCorpEmail();
        } //counter screens
        );

        
        receipts.setOnAction((ActionEvent event) -> {
            GOBridgeReceipts();
        } //counter screens
        );
        
        chgScrn.setOnAction((ActionEvent event) -> {
            try {
                if (!loginButtonPushed()) {
                    return;
                }
            if (!newEFX.employeeLevel("Corporate")) {
                return;
            }

            new settingsFXML().setCounterSettings();
            new settingsFXML().setSettings(2);
            new settingsFXML().setProp("Css", "99", 6);
            new settingsFXML().setCounterProp("stage", "6");
            new SceneChanger_Main().MainStage(stageV, "/views/CounterChooser.fxml", "Counter Chooser");
            } catch (IOException ex) {
            }

            //exitProcessDO();
        }
        );

 
        CONTEXTMENU.getItems().clear();
        CONTEXTMENU.getItems().addAll(refreshList, empNotes, corpEmail, chgScrn, receipts, bday, exit);
 
        menuItemsLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boundsInScene = menuItemsLabel.localToScene(menuItemsButton.getBoundsInLocal());
                CONTEXTMENU.show(menuItemsLabel, boundsInScene.getMaxX()+ 5.0, boundsInScene.getMaxY() + 10.0);
            }
        });
        menuItemsLabel.setOnContextMenuRequested((ContextMenuEvent event) -> {
            CONTEXTMENU.show(menuItemsLabel, event.getScreenX(), event.getScreenY());
        });
   }
    
    public void menuItemsButtonMouseOver() {
        Bounds boundsInScene2 = menuItemsLabel.localToScene(menuItemsLabel.getBoundsInLocal());
        CONTEXTMENU.show(menuItemsLabel, boundsInScene2.getMaxX() -110.0, boundsInScene2.getMaxY() +20.0);
        
    }
    
    private void setCloseCatch() {
        getStageV();
        stageV.setOnCloseRequest((WindowEvent we) -> {
            Platform.exit();
            System.exit(0);
        }); 
    }
    
    private void exitProcessDO() {
        new writeArrayListToXMLFile().writeLastMemberTransaction(lastMember, "xLM_" + DBSP.localMachine.getHostName());
        Stage stage = (Stage) voucherButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
  }

    
}
