package views;




import views.counterPopUp.SearchMemberViewController;
import views.counterPopUp.EmailEditorPopUpController;
import views.counterPopUp.PinNumberPopUpController;
import views.settings.AnnouncementsController;
import views.timeclock.EmpDetailPopUpCounterController;
import views.gameproblems.GamesProblemsController;
//import views.vouchers.FXVoucherDialogController;
import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import XML_Code.readXMLToArrayList;
import XML_Code.writeArrayListToXMLFile;
import animations.AllStartAnimations;
import models.club.BridgeTimeTable;
import clockoutatnight.clockOutAtNightFX;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import commoncodes.GetReceipts;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import messageBox.messageBox;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;
import models.club.CheckBalanceDB;
import models.toys.ToysDB;
import models.toys.Toys;
import models.club.rCeipts;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.control.TablePosition;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import models.club.LastMemberTransaction;
import models.settings.DailyBonus;
import models.timeclock.EmpFileFXDB;
import models.timeclock.message;
import models.timeclock.tClockDB;
import models.voucher.EaterCodes;
import models.voucher.FXVoucherViewer;
import pReceipts.JavaPrinterOptions;
import pReceipts.prtReceiptsFX;
import pWordFX.empFX;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;
import views.counterPopUp.scanCardAgainController;
import views.settings.BridgeReceiptsController;
import views.shiftNotes.ShiftNotesController;
import views.timeclock.TClockDialogFXController;
import views.vouchers.FXVoucherDialogController;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class BridgeController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private Button voucherButton;    
    @FXML private Button timeClockButton;    
    @FXML private Button birthdayButton; 
    @FXML private Button VIPButton;    
    @FXML private Button fFocusButton;
    @FXML private Button shiftNotesButton;
    @FXML private Button gProblems;
    @FXML private Button pdOutButton;
    @FXML private Button prtNewMemberButton;
    @FXML private Button memSearchButton;
    @FXML private Button dropButton;
    @FXML private Label menuItemsLabel;
    @FXML private Label percentage;
    @FXML private Pane depPane;
    @FXML private Pane timeClockPane;
    @FXML private Stage stageV;
    
    @FXML private TableView<BridgeTimeTable> cTable;
    @FXML private TableColumn<BridgeTimeTable, String> C1;
    @FXML private TableColumn<BridgeTimeTable, String> C2;
    @FXML private TableColumn<BridgeTimeTable, String> empID;
    

    
    ContextMenu CONTEXTMENU = new ContextMenu();
    ContextMenu CONTEXTMENUError = new ContextMenu();
    
    private static final dbStringPath DBSP = new dbStringPath();
    private static final cssChanger CSS = new cssChanger();
    private static final employeeFX EFX = new employeeFX();
    private static final SceneChanger_Main SC = new SceneChanger_Main();
    private static final DB DB = new DB();
    private static final FXMLLoader FXLOADER = new FXMLLoader();
    private static final messageBox MBOX = new messageBox();
    private static final CheckBalanceDB CHKBALANCE = new CheckBalanceDB();
    private static final Mail_JavaFX1 JMAIL = new Mail_JavaFX1();
    private static final clockOutAtNightFX CLOCKOUTATNIGHT = new clockOutAtNightFX();
    private static final prtReceiptsFX printReceitps = new prtReceiptsFX();
    private static final GetReceipts getR = new GetReceipts();
    private static final JavaPrinterOptions p = new JavaPrinterOptions();
    private static Member member;
    private static ArrayList<DailyBonus> Daily;
    private static Memtick memtick;
    private static PrintWriter pw;
    private static ArrayList<FXVoucherViewer> vList= null;
    private static ArrayList<LastMemberTransaction> lastMember = new ArrayList();
    private static LastMemberTransaction LMT = new LastMemberTransaction("2007433", "4793", "DayStart", "Bridge", "DayStart");
    private static int iLastTran = 0;
    private static String tMessage = "";
    
    private static int VIPMultiplier;
    private static ArrayList<empFX> EE;
    private static ArrayList<String> V;
    private static ArrayList<Toys> l;
    private static ArrayList<EaterCodes> TEC; //TicketEaterCodes
    private static String counterScene = "";
    private static ArrayList<rCeipts> Receipts;
    private static String newMemberReceipt = null;
    //private static final MemoryTest MEMORYTEST = new MemoryTest();
    //private static final DecimalFormat DF = new DecimalFormat("#");

    
    private static Connection connectionBridgeTimeTable; 
    private static Statement statementTimeTable;
    private static ResultSet resultsetTimeTable;
    private static String cssPath;
    private static empFX newEFX;
    private static Bounds boundsInScene, boundsInScene2, boundsInScene3;
    private static Style Format = new Style();

   
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = depPane.localToScene(depPane.getBoundsInLocal());
        boundsInScene2 = root.localToScene(root.getBoundsInLocal());
        boundsInScene3 = menuItemsLabel.localToScene(menuItemsLabel.getBoundsInLocal());
        DBSP.setName();
        setKeyCodes();
        setTimer();
        dropButton.setVisible(false);
        pdOutButton.setVisible(false);
        timeClockPane.setVisible(false);
        C1.setCellValueFactory(new PropertyValueFactory<>("C1"));
        C2.setCellValueFactory(new PropertyValueFactory<>("C2"));
        buildMenuButton();
        cssPath = CSS.cssPath();
        connectionBridgeTimeTable = null;
        root.getStylesheets().add(cssPath);
        Platform.runLater(() -> startLastMemberTransaction());
        Platform.runLater(() -> setCloseCatch());
        Platform.runLater(() -> GetLists());
        Platform.runLater(() -> checkTime());

    }

    public void GObirthdayButton() {

        //new GetReceipts().getReceipts(Receipts, "")
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
        } catch (IOException e) {

        } catch (URISyntaxException ex) {
            System.out.println(ex);
        }

        /*    
            try {
            if (!loginButtonPushed()) {
            return;
            }
            if (!newEFX.employeeLevel("Birthdays")) {
            return;
            }
            getStageV();
            new employeeFX().SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
            //BDayFXController wController = (BDayFXController) FXLOADER.getController();
            //wController.sc = SC;
            //wController.dbsp = DBSP;
            //wController.cssPath = cssPath;
            //wController.eFXX = EFX;
            //wController.fxloader = FXLOADER;
            //wController.mBox = MBOX;
            //wController.jmail = JMAIL;            
                SC.loadSceneRemoveDepPane(stageV, "/messageBox/HTMLViewer_Page.fxml", depPane, 20.0, 20.0);
            } catch (IOException ex) {
                System.out.println(ex);
            }
         */
        return;

    }

    public void GOnewMemberButton() {
        int GO = 0;
        try {
            scanCardAgainController wController = new scanCardAgainController();
            wController.lastmember = lastMember;
            wController.newEFX = newEFX;
            wController.sc = SC;
            wController.cssPath = cssPath;
            wController.FXLOADER = FXLOADER;
            wController.dbsp = DBSP;
            SC.getpassWord(stageV, "/views/counterPopUp/scanCardAgain.fxml", "Number", "Scan Card:", boundsInScene2.getMinX() + 550.0, boundsInScene2.getMinY() + 90.00);
            if (SC.getGameNumber().equals("Number")) {
                GO = 0;
                return;
            }
            if (DB.isMemberValid(SC.getGameNumber())) {
                MBOX.showAlert(Alert.AlertType.ERROR, stageV, "Error", "This Card is already an Account");
                GO = 0;
            } else {
                GO++;
            }
            if (DB.isInactiveMemberValid(SC.getGameNumber())) {
                MBOX.showAlert(Alert.AlertType.ERROR, stageV, "Error", "This Card is already an Account");
                GO = 0;
            } else {
                GO++;
            }
            if (GO > 0) {
                GOnewMemberScreen(SC.getGameNumber());
            }
        } catch (IOException | SQLException ex) {
            JMAIL.sendEmailTo("Error Bridge 2", "Error getting to New Member Screen" + ex, "errors");
        }
    }

    public void GOnewMemberScreen(String n) throws IOException {
        NewMemberController wController = (NewMemberController) FXLOADER.getController();
        wController.sc = SC;
        wController.db = DB;
        wController.dbsp = DBSP;
        wController.csspath = cssPath;
        wController.mbox = MBOX;
        wController.efx = EFX;
        wController.E = this.EE;
        wController.fxloader = FXLOADER;
        wController.cn = n;
        wController.rCeipts = Receipts;
        wController.pw = pw;
        wController.VIPPromosDailyBonus = Daily;
        wController.lastmember = lastMember;
        wController.NEWMEMRECEIPT = newMemberReceipt;
        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/NewMember.fxml", depPane, 200.0, 50.0);
        updateLastMember();
    }

    public void GOdepositButton() throws IOException {
        MemDepositFXController wController = (MemDepositFXController) FXLOADER.getController();
        wController.sc = SC;
        wController.db = DB;
        wController.dbsp = DBSP;
        wController.member = member;
        wController.memtick = memtick;
        wController.mBox = MBOX;
        wController.E = EE;
        wController.l = l;
        wController.cssPath = cssPath;
        wController.rCeipts = Receipts;
        wController.jmail = JMAIL;
        wController.chkBalance = CHKBALANCE;
        wController.FXLOADER = FXLOADER;        
        wController.pw = pw;
        wController.VIPPromosDailyBonus = Daily;
        wController.tec = TEC;
        wController.lastmember = lastMember;
        wController.iLastTran = this.iLastTran;
        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/MemDepositFX.fxml", depPane, 300.0, 50.0);
        updateLastMember();
        newEFX = null;
    }

    public void GOvoucherButton() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (!newEFX.employeeLevel("Vouchers")) {
                return;
            }
            new employeeFX().SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getEmpID());
            FXVoucherDialogController wController = (FXVoucherDialogController) FXLOADER.getController();
            String t = getR.getReceiptsNumber(Receipts, "Voucher_Reason");
            wController.dbsp = DBSP;
            wController.css = cssPath;
            wController.eFXX = EFX;
            wController.css = cssPath;
            wController.receipts = Receipts;
            wController.eFXX = EFX;
            wController.TEC = TEC;
            wController.mBox = MBOX;
            wController.jmail = JMAIL;
            wController.vAmt = EFX.getVAmt();
            wController.rCeiptvMount = Double.valueOf(t);
            getStageV();
            SC.loadSceneRemoveDepPane(stageV, "/views/vouchers/FXVoucherDialog.fxml", depPane, boundsInScene2.getMinX() + 300.0, boundsInScene.getMinY() + 50.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        return;
    }

    public void GOVIPButton() throws IOException {
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
        wController.FXLOADER = FXLOADER;
        wController.chkBalance = CHKBALANCE;
        wController.VIPMultiplier = this.VIPMultiplier;
        wController.pw = pw;
        wController.lastmember = lastMember;
        wController.iLastTran = this.iLastTran;
        System.out.println("HERE IS THE LAST TRAN NUMBER" + this.iLastTran);
        SC.loadSceneRemoveDepPane(stageV, "/views/MemVIP.fxml", depPane, 200.0, 100.0);
        updateLastMember();
    }
    
    
    ///LAST MEMBER -----------------------------------------------------------------
    ///THIS GOES IN THE DECLARATION AREA CAFE, BRIDGE COUNTER ONLY
    ///private static LastMemberTransaction LMT = new LastMemberTransaction("2007433", "4793", "DayStart", "Bridge", "DayStart");
    ///private static int iLastTran = 0;
    ///private static ArrayList<LastMemberTransaction> lastMember = new ArrayList();

    ///THIS GOES IN INITIALIZE CAFE, BRIDGE, COUNT ONLY
    ///Platform.runLater(() -> startLastMemberTransaction());

    ///=========================================================================

    ///THIS GOES IN THE SCREEN LIKE MEMVIP, MEMdEPOSIT
    ///public static ArrayList<LastMemberTransaction> lastmember;
    ///public static int iLastTran;
    ///if (!SCLMT) {
    ///sc.setActivity("1");
    ///}
    ///SCLMT = false; NOT CAFE, BRIDGE OR COUNTER

    
    ///THIS GOES IN THE GOTOSCREEN
    ///wController.lastmember = lastMember;
    ///wController.iLastTran = this.iLastTran;
    ///updateLastMember();
    


    ///THIS GOES IN EXITDO() FOR BRIDGE, CAFE, COUNTER ONLY
    ///new writeArrayListToXMLFile().writeLastMemberTransaction(lastMember, "xLM_" + DBSP.localMachine.getHostName());
    ///
    ///THIS GOES IN THE DO BUTTON
    ///setLastMemberVariables();



    
    
    
    
    
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
    
    
    private void updateLastMember() {
            LMT = new LastMemberTransaction(SC.getCCN(), SC.getEmployee(), SC.getEmpName(), SC.getLastName(), SC.getActivity());
            System.out.println("HERE IS WHERE WE COMEBACK FROM A BLACNK TRANSACTION " + LMT.getLastActivity());
            if (!LMT.getLastActivity().equals("1")) {
                lastMember.add(getIntLastMemberTransaction(), LMT);
            }
            LMT = null;
            SC.setCCN("");
            SC.setActivity("");
            SC.setEmployee("");
            SC.setLastName("");
    }
    
    
    private int getIntLastMemberTransaction() {
        iLastTran++;
        return iLastTran;
    }
    

    
    
    public void getLastMemberTransaction() {
        String lastEmpNumber, lastCCN;
            try {
                //lastEmpNumber = lastMember.get(iLastTran).getlastEmpNumber();
                lastEmpNumber = lastMember.get(iLastTran).getlastEmpNumber();
                System.out.println(lastEmpNumber);
                lastCCN = lastMember.get(iLastTran).getLastMember()+ " " + lastMember.get(iLastTran).getLastEmpName() + " " + lastMember.get(iLastTran).getLastLastName() + " " + lastMember.get(iLastTran).getLastActivity();
                loginButtonPushed();
                if (newEFX.getEmpNumber().equals(lastEmpNumber)) {
                    new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Approved!", "Here is the last member number " + lastEmpNumber + " \n" + lastCCN);
                }
            } //counter screens
            catch (IOException ex) {
               System.out.println(ex);
               JMAIL.sendEmailTo("ERROR getlastMemberTransaction()", "there is an error in getLastTransaction" + ex, "errors");
            }
                 lastEmpNumber = "";
                 lastCCN = "";
    }
    
    
///END LAST MEMBER ------------------------------------------------------------- 
    


    public void GOmemberSearch() throws IOException {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        SearchMemberViewController wController = (SearchMemberViewController) FXLOADER.getController();
        wController.en = newEFX.empNumber;
        wController.fn = newEFX.nameF;
        wController.csspath = cssPath;
        wController.db = DB;
        wController.dbsp = DBSP;
        wController.receipts = Receipts;
        wController.pw = pw;
        SC.loadSceneRemoveDepPane(stageV, "/views/counterPopUp/SearchMemberView.fxml", depPane, 300.0, 100.0);
    }

    public void GOgameProbs() {
        try {
            if (!loginButtonPushed()) {
                return;
            }

            if (!newEFX.employeeLevel("Game Prob's")) {
                return;
            }
            //EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
            GamesProblemsController wController = (GamesProblemsController) FXLOADER.getController();
            wController.cssPath = cssPath;
            wController.eFXX = EFX;
            wController.newEFX = newEFX;
            wController.dbsp = DBSP;
            getStageV();
            SC.loadSceneRemoveDepPane(stageV, "/views/gameproblems/GamesProblems.fxml", depPane, boundsInScene2.getMinX() + 170.0, boundsInScene2.getMinY() + 50.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
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
            wController.screenName = "BRIDGE";
            getStageV();
            SC.loadSceneRemoveDepPane(stageV, "/views/shiftNotes/ShiftNotes.fxml", depPane, boundsInScene2.getMinX() + 170.0, boundsInScene2.getMinY() + 50.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        return;

    }

    public void GOEmployeeNotes() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (newEFX.getArcade() != 2 && newEFX.getArcade() != 3 ) {
                System.out.println(newEFX.getArcade());
                return;
            }
            EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
            EmpDetailPopUpCounterController wController = (EmpDetailPopUpCounterController) FXLOADER.getController();
            wController.cssPath = cssPath;
            wController.dbsp = DBSP;
            wController.eFXX = EFX;
            wController.screen = "BRIDGE NOTE";            
            wController.rCeipts = Receipts;
            wController.sc = SC;
            wController.EE = this.EE;
            wController.empName = EFX.getNameF();
            wController.updateVar = "0";
            getStageV();
            SC.getpassWord(stageV, "/views/timeclock/EmpDetailPopUpCounter.fxml", null, null, 400.00, 175.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        //sc.goToScene("EmpDetailCounter", stageV, eFX.getNameF(), null, boundsInScene);
        //runDimFile = true; THIS IS TO TELL THE EMPLOYEE VIEWER TO RUN THE DIM FILE ON EXIT.
        return;

    }


    public void GOCorpEmail() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (newEFX.getArcade() != 2 && newEFX.getArcade() != 3 ) {
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


    public void GOTimeClock() {
        try {
            depPane.setVisible(false);
            TClockDialogFXController wController = (TClockDialogFXController) FXLOADER.getController();
            wController.cssPath = cssPath;
            wController.dbsp = DBSP;
            wController.mbox = MBOX;
            wController.eFX = EFX;
            wController.jmail = JMAIL;
            wController.EE = EE;
            wController.tMessage = tMessage;
            //MBOX.showAlert(Alert.AlertType.ERROR, null, "HEY", tMessage);
            SC.getpassWord(stageV, "/views/timeclock/TClockDialogFX.fxml", "Number", "Enter Employee Number:", boundsInScene.getMinX() + 350.00, boundsInScene.getMinY());
            depPane.setVisible(true);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        Thread threadTimeClock = new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
                Platform.runLater(() -> getTableItems());
            }
        };
        threadTimeClock.start();
    }

    private void GOEmailEditor() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        if (!newEFX.employeeLevel("Super")) {
            return;
        }
        EmailEditorPopUpController wController = (EmailEditorPopUpController) FXLOADER.getController();
        wController.typeVar = "3";
        wController.rCeipts = Receipts;
        wController.dbsp = DBSP;
        wController.chkbalance = CHKBALANCE;
        wController.cssPath = cssPath;
        wController.db = DB;
        wController.mBox = MBOX;
        wController.sc = SC;
        wController.empID = String.valueOf(newEFX.getEmpID());
        try {
            SC.getpassWord(stageV, "/views/counterPopUp/EmailEditor.fxml", null, null, 420.0, 200.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void GOAnnounce() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        if (!newEFX.employeeLevel("Super")) {
            return;
        }
        EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
        AnnouncementsController wController = (AnnouncementsController) FXLOADER.getController();
        wController.cssPath = cssPath;
        wController.superLevel = newEFX.getGProb();
        wController.Receipts = Receipts;
        //wController.MGR = eFX;        
        getStageV();
        try {
            SC.getpassWord(stageV, "/views/settings/Announcements.fxml", null, null, 100.0, 50.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene2("Announcements", stageV, EFX.getNameF(), depPane, boundsInScene.getMinX(), boundsInScene.getMinY()-20.0);
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
        EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getBdayresos());
        BridgeReceiptsController wController = (BridgeReceiptsController) FXLOADER.getController();
        wController.cssC = cssPath;
        wController.rCeipts = Receipts;
        wController.superLevel = newEFX.getGProb();
        stageV.setTitle("Bridge");
        wController.eFX = EFX;
        wController.WHSTAGE = stageV.getTitle();
        System.out.println("From Bridge: " + stageV.getTitle());
        //wController.MGR = eFX;        
        getStageV();
        try {
            SC.getpassWord(stageV, "/views/settings/BridgeReceipts.fxml", null, null, 540.0, 75.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene2("Announcements", stageV, EFX.getNameF(), depPane, boundsInScene.getMinX(), boundsInScene.getMinY()-20.0);
        return;

    }
    

    public void prtChangers(){
        /*try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }*/
        GOBridgeReceipts();
        //printReceitps.prtChangers(newEFX.getNameF());
    }
   
    public void prtFamFocus(){
        /*try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Family Focus Button Pushed Error: " + ex);
        }*/
        GOBridgeReceipts();
        //printReceitps.prtFamFocus(newEFX.getNameF());
    }
    
    public void prtDropSheet(){
        /*try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("DropSheet Button Pushed Error: " + ex);
        }*/
        GOBridgeReceipts();
        //printReceitps.printDropSheet(newEFX.getNameF());
    }
    
    public void prtNewMember(){
        /*try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("New Member Button Pushed Error: " + ex);
        }*/
        GOBridgeReceipts();
        //printReceitps.prtNewMember(newEFX.getNameF(), pw);
    }
    
    public void prtPaidOut(){
        /*try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("New Member Button Pushed Error: " + ex);
        }*/
        GOBridgeReceipts();
        //printReceitps.prtPaidOut(newEFX.getNameF());
    }
    
    public void prtReceipt(){
        /*try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("New Member Button Pushed Error: " + ex);
        }*/
        GOBridgeReceipts();
        //printReceitps.prtReceipt(newEFX.getNameF());
    }
 
    public void prtTTuesday(){
        /*try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("New Member Button Pushed Error: " + ex);
        }*/
        GOBridgeReceipts();
        //printReceitps.prtTTuesday(newEFX.getNameF());
    }
 
    
   


    
    private void GetLists() {
        //Platform.runLater(() -> EE = new empDB().getList());
        //setEMPList(EE);
        Platform.runLater(() -> EE = new EmpFileFXDB().getDimDataXML());
        setEMPList(EE);
        Platform.runLater(() -> l = new ToysDB().getList());
        setToyList(l);
        Platform.runLater(() -> V = DB.getAllPromos());
        setVIPList(V);
        Platform.runLater(() -> VIPMultiplier = DB.getVIPMultipier());
        setVIPMultiplier(VIPMultiplier);
        System.out.println("here we are in GetList()");
        Platform.runLater(() -> Receipts = DB.getReceipts());
        setReceipts(Receipts);
        Platform.runLater(() -> Daily = DB.getAllDailyArrayList());
        setDailyList(Daily);
        Platform.runLater(() ->TEC = new readXMLToArrayList().getTicketEaterCodesXML());
        Platform.runLater(() -> newMemberReceipt = DB.getNewMemberReceipt());
        //setReceipts1(Receipts1);
        //Platform.runLater(()->histEmpListRS = EFXDB.HistoricEmployeeList());
        new Thread(taskGetMessageXML).start();
        Platform.runLater(() -> DB.disConnect());

    }
    
    
    
     Task<Void> taskGetMessageXML = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            ArrayList<message> messageList = new readXMLToArrayList().getempMessageDataXML();
            System.out.println("This would be the date to search for; " + LocalDate.now());
            System.out.println(" BEFORE: " + messageList.size());
            for (int i = 0; i < messageList.size(); i++) {
                String startDatefirstTenChars = messageList.get(i).getDateStart().toString().substring(0, 10);
                String endDatefirstTenChars = messageList.get(i).getDateEnd().toString().substring(0, 10);
                if (new GetDay().setToLocalDateforXML(startDatefirstTenChars).isEqual(LocalDate.now()) || new GetDay().setToLocalDateforXML(startDatefirstTenChars).isBefore(LocalDate.now())) {
                    if (new GetDay().setToLocalDateforXML(endDatefirstTenChars).isEqual(LocalDate.now()) || new GetDay().setToLocalDateforXML(endDatefirstTenChars).isAfter(LocalDate.now())) {
                        System.out.println("======================================== enddate" + endDatefirstTenChars);
                        System.out.println(" Winner: startDate = to LocalDate.now()" + messageList.size());
                        tMessage = messageList.get(i).getHTML();
                    }
                } //END OF IF
            } //END OF FOR
            return null;
        }
    };

    
    private void setDailyList(ArrayList Daily) {
        this.Daily = Daily;
    }
    
    public void actionPerformed() {
        checkTime();
    }

    private void setToyList(ArrayList l) {
        this.l = l;
    }

    private void setVIPList(ArrayList V) {
        this.V = V;
    }

    private void setEMPList(ArrayList E) {
        this.EE = E;
    }

    public ArrayList getEMPList() {
        return EE;
    }

    private void setVIPMultiplier(int m) {
        this.VIPMultiplier = m;
    }

    private void setReceipts(ArrayList Receipts) {
        this.Receipts = Receipts;
    }

    //private void setReceipts1(ArrayList Receipts1) {
    //    this.Receipts1 = Receipts1;
    //}


    public void clockOut() {
        TablePosition pos = cTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        String empIDforClockOut = cTable.getItems().get(row).getEMPID();
        String nameForClockout = cTable.getItems().get(row).getC1();
        System.out.println("This si the Employee ID :" + empIDforClockOut);
        superClockOut(empIDforClockOut, nameForClockout);
        JMAIL.sendEmailTo(nameForClockout + " did not clock out!!", nameForClockout + " did not Clock out! \n" + newEFX.nameF + " Clocked " + nameForClockout +" out @ " + new GetDay().Local_Date_Time_AMPM(), "notClockedOut");
        //then send email to lexi or clock out I put in whoTo table.
        newEFX = null;
        
        checkTime();
     }
     
     
    private void setTimer() {
        PauseTransition pTransition = new PauseTransition(Duration.minutes(2.0));
        pTransition.setOnFinished((e) -> {
            checkTime();
            pTransition.play();
        });
        pTransition.play();
    }

    public void checkTime() {
        CLOCKOUTATNIGHT.clockout("xLM_" + DBSP.localMachine.getHostName());
        //percentage.setText(String.valueOf(DF.format(MEMORYTEST.runGC()) + "%"));
        Platform.runLater(() -> getTableItems()); //refresh the timeclock
    }

    private void getTableItems() {
        try {
            cTable.getItems().clear();
            cTable.getItems().addAll(getdataB());
            if (cTable.getItems().size() > 0) {
                timeClockPane.setVisible(true);
            } else {
                timeClockPane.setVisible(false);
            }
            cTable.setEditable(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public ObservableList<BridgeTimeTable> getdataB() throws SQLException {
        ObservableList<BridgeTimeTable> timeTableData = FXCollections.observableArrayList();
        try {
            connectionBridgeTimeTable = DriverManager.getConnection("jdbc:ucanaccess://" + DBSP.pathNameClubDBs + "Tclock.accdb;immediatelyReleaseResources=true");
            statementTimeTable = connectionBridgeTimeTable.createStatement();            
            resultsetTimeTable = statementTimeTable.executeQuery("SELECT Memtime.Employee_ID, Memtime.Emp_Name, Memtime.Day FROM Memtime WHERE (((Memtime.Time_OUT) Is Null));");
            while (resultsetTimeTable.next()) {

                BridgeTimeTable bridgeTableData = new BridgeTimeTable( resultsetTimeTable.getString("Emp_Name"), resultsetTimeTable.getString("Day"),resultsetTimeTable.getString("Employee_ID"));
                timeTableData.add(bridgeTableData);
            }
        } catch (SQLException e) {
            JMAIL.sendEmailTo("error", e.getMessage(), "errors");
        } finally {
            if (resultsetTimeTable != null){
                resultsetTimeTable.close();
                resultsetTimeTable = null;
            }
            if (statementTimeTable != null){
                statementTimeTable.close();
                statementTimeTable = null;
            }
            if (connectionBridgeTimeTable != null){
            connectionBridgeTimeTable.close();
            connectionBridgeTimeTable = null;}
        }

        return timeTableData;
    }


    
    
    
    
    
    
    
    
    
    
    
    private void setCloseCatch() {
        getStageV();
        stageV.setOnCloseRequest((WindowEvent we) -> {
        try {
            System.out.println(connectionBridgeTimeTable.isValid(0));
        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }
            Platform.exit();
            System.exit(0);
        });
    }

    public void buildMenuButton() {
        MenuItem item2 = new MenuItem(" Refresh List's ");
        MenuItem item4 = new MenuItem(" Print Changers ");
        MenuItem item7 = new MenuItem(" Unlock Account ");
        MenuItem item5 = new MenuItem(" Supervisor ");
        MenuItem empNotes = new MenuItem(" Emp - Notes ");
        MenuItem corpEmail = new MenuItem(" Corp - Email ");
        MenuItem chgScrn = new MenuItem(" Change Screen ");
        MenuItem announce = new MenuItem(" Announcments ");
        MenuItem emailEditor = new MenuItem(" Email Editor ");
        MenuItem memUpdate = new MenuItem(" Update Return");
        //MenuItem receitps = new MenuItem(" Receipts F1 ");
        MenuItem lastTran = new MenuItem(" Last Member ");
        MenuItem allAnimations = new MenuItem( " All Animations ");
        
        // A SEPARATOR MENU ITEM IS HERE IN THE ADDMENUITEMS COMMAND BELOW
        MenuItem exit = new MenuItem("      Exit     ");

        Menu pinMenu1 = new Menu(" Pin Number ");
        //MenuItem pinItem1 = new MenuItem(" Use Pin Number ");
        MenuItem pinItem2 = new MenuItem(" Pin Number Editor ");
        //MenuItem pinItem3 = new MenuItem(" Change a Pin Number ");
        //pinMenu1.getItems().addAll(pinItem2, pinItem3 );

        
        //THESE ITEMS ARE PIN NUMBER ACTIVITIES


        /*pinItem1.setOnAction((ActionEvent event) -> {
            //new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Coming Soon", "This menu item is not active yet.");
            SetAPinNumber();
        } //USE A PIN NUMBER
        );*/
        
        pinItem2.setOnAction((ActionEvent event) -> {
            AddAPinNumber();
            //new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Coming Soon", "This menu item is not active yet.");
        } // ADD A PIN NUMBER
        );
        /*pinItem3.setOnAction((ActionEvent event) -> {
            //new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Coming Soon", "This menu item is not active yet.");
            ChgAPinNumber();
        } //CHANGE A PIN NUMBER
        );*/
        
        emailEditor.setOnAction((ActionEvent event) -> {
            //new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Coming Soon", "This menu item is not active yet.");
            GOEmailEditor();
        } //CHANGE A PIN NUMBER
        );
        
        
        lastTran.setOnAction((ActionEvent event) -> {
            
            try {
                printEscPosWITHDRAWAL();
            } catch (PrintException | IOException ex) {
               System.out.println(ex);
            }
        }        
        );
        
        
        
        // THESE ITEMS ARE THE MAIN MENU ITEMS
        item2.setOnAction((ActionEvent event) -> {
            EE.removeAll(EE);
            V.removeAll(V);
            VIPMultiplier = 0;
            Receipts.removeAll(Receipts);
            GetLists();
        } //counter screens
        );


        item4.setOnAction((ActionEvent event) -> {
            GOBridgeReceipts();
            //prtChangers();
        } //counter screens
        );


        announce.setOnAction((ActionEvent event) -> {
            GOAnnounce();
        } //counter screens
        );


        //receitps.setOnAction((ActionEvent event) -> {
        //    GOBridgeReceipts();
        //} //counter screens
        //);


        empNotes.setOnAction((ActionEvent event) -> {
            GOEmployeeNotes();
        } //counter screens
        );

        corpEmail.setOnAction((ActionEvent event) -> {
            GOCorpEmail();
        } //counter screens
        );

        
        item5.setOnAction((ActionEvent event) -> {
            superIn();
        } //counter screens
        );

        
        exit.setOnAction((ActionEvent event) -> {
            exitProcessDO();
        } //clock in out
        );
        
        item7.setOnAction((ActionEvent event) -> {
            try {
                if (!loginButtonPushed()) {
                    return;
                }
            } catch (IOException ex) {
            }
            if (!newEFX.employeeLevel("Super")) {
                return;
            }
            try {
            scanCardAgainController wController = new scanCardAgainController();
            wController.lastmember = lastMember;
            wController.newEFX = newEFX;
            wController.sc = SC;
            wController.cssPath = cssPath;
            wController.FXLOADER = FXLOADER;
            wController.dbsp = DBSP;

                SC.getpassWord(stageV, "/views/counterPopUp/scanCardAgain.fxml", "Number", "Scan Card:", boundsInScene2.getMinX() + 250.0, boundsInScene2.getMinY() + 90.00);
                if (!DB.IsUnlockMemberValid(SC.getGameNumber(), newEFX.getEmpNumber())) {
                    DB.disConnect();
                } else {
                    MBOX.showAlert(Alert.AlertType.INFORMATION, stageV, "Inacvtive Member Found", "This member has been Unlocked, please SCAN card again and have them update the receipt with current information.");
                }
            } //unock account
            catch (IOException | SQLException ex) {
                System.out.println(ex);
            }
        }
        );
        

        
        memUpdate.setOnAction((ActionEvent event) -> {
            try {
                if (!loginButtonPushed()) {
                    return;
                }
            } catch (IOException ex) {
            }
            if (!newEFX.employeeLevel("Vouchers")) {
                System.out.println("somethingWRONG");
                return;
            }
                System.out.println("MOVEING AHEAD");
            try {
            scanCardAgainController wController = new scanCardAgainController();
            wController.lastmember = lastMember;
            wController.newEFX = newEFX;
            wController.sc = SC;
            wController.cssPath = cssPath;
            wController.FXLOADER = FXLOADER;
            wController.dbsp = DBSP;

                SC.getpassWord(stageV, "/views/counterPopUp/scanCardAgain.fxml", "Number", "Scan Card:", boundsInScene2.getMinX() + 250.0, boundsInScene2.getMinY() + 90.00);
                System.out.println(SC.getGameNumber());
                if (!SC.getGameNumber().equals("Number")) {
                String MemberID = DB.GetMarkerForUpdate(SC.getGameNumber());
                String tranTime = new GetDay().getCurrentTimeStamp();
                if  (!MemberID.equals("InValid")){
                    memtick = new Memtick(MemberID, newEFX.getEmpNumber(), tranTime, LocalDate.now(), 0.0, 0.0, 0.0, "WUPDate", 0);
                    DB.WaitingUpdate(MemberID, memtick);
                    MBOX.showAlert(Alert.AlertType.INFORMATION, stageV, "Waiting Update Complete", "Marker ID: " + MemberID);
                } else {
                    MBOX.showAlert(Alert.AlertType.INFORMATION, stageV, "Posting Receipt ID Failed", "MarkerID: " + MemberID);
                }
                }
            
            }catch (IOException | SQLException ex) {
                System.out.println(ex);
                new Mail_JavaFX1().sendEmailTo("Error in waiting update", "error in looking for Update Receipt ID look up and / or Posting to account Waiting Update " + ex + "\n" + DBSP.localMachine, "errors");
            }
        }
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
        
        
        allAnimations.setOnAction((ActionEvent event) -> {
            AllStartAnimations ASA = new AllStartAnimations(0);
            
           for (int i = 1; i<7; i++) {
               System.out.println(i);
               ASA.main(i);
           }
        } 
        );


        
        
        //contextMenu.getItems().addAll(item4, item2, item7, pinMenu1, item5, chgScrn, emailEditor, item6);
        CONTEXTMENU.getItems().clear();
        CONTEXTMENU.getItems().addAll(item4, item2, item7, pinItem2, item5, chgScrn, empNotes, corpEmail, announce, emailEditor, memUpdate, new SeparatorMenuItem(), lastTran, exit);
        menuItemsLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                CONTEXTMENU.show(menuItemsLabel, boundsInScene3.getMaxX() + 70.0, boundsInScene3.getMaxY() + 50.0);
            }
        });
        menuItemsLabel.setOnContextMenuRequested((ContextMenuEvent event) -> {
            CONTEXTMENU.show(menuItemsLabel, event.getScreenX(), event.getScreenY());
        });
    }

    public void menuItemsButtonMouseOver() {
        CONTEXTMENU.show(menuItemsLabel, boundsInScene3.getMaxX()+70, boundsInScene3.getMaxY()+50);
    }
    
    public void ShowErrorContextMenu(String error, double w, double h) {
        System.out.println("here in context Menu");
        MenuItem item2 = new MenuItem(error);
        CONTEXTMENUError.getItems().clear();
        CONTEXTMENUError.getItems().addAll(item2);
        CONTEXTMENUError.show(menuItemsLabel, w, h);
        
    }

    
    private void AddAPinNumber() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
        if (!newEFX.employeeLevel("Super")) {
            return;
        }
        PinNumberPopUpController wController = (PinNumberPopUpController) FXLOADER.getController();
        wController.typeVar = "1";
        wController.empID = String.valueOf(newEFX.getEmpID());
        try {
            SC.getpassWord(stageV, "/views/counterPopUp/PinNumberPopUp.fxml", null, null, 200.0, 50.0);
            //SC.goToScene("AddPinNumber", stageV, EFX.getNameF(), depPane, boundsInScene2);
        } catch (IOException ex) {
           System.out.println(ex);
        }
    }

    private void SetAPinNumber() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        if (!newEFX.employeeLevel("Super")) {
            return;
        }
        PinNumberPopUpController wController = (PinNumberPopUpController) FXLOADER.getController();
        wController.typeVar = "2";
        wController.empID = String.valueOf(newEFX.getEmpID());
        SC.getpassWord(stageV, "/views/counterPopUp/PinNumberPopUp.fxml", null, null, 200.0, 50.0);
        //SC.goToScene("AddPinNumber", stageV, EFX.getNameF(), depPane, boundsInScene2);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
    }

    private void ChgAPinNumber() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        if (!newEFX.employeeLevel("Super")) {
            return;
        }
        PinNumberPopUpController wController = (PinNumberPopUpController) FXLOADER.getController();
        wController.typeVar = "3";
        wController.empID = String.valueOf(newEFX.getEmpID());
        SC.getpassWord(stageV, "/views/counterPopUp/PinNumberPopUp.fxml", null, null, 200.0, 50.0);
        //SC.goToScene("AddPinNumber", stageV, EFX.getNameF(), depPane, boundsInScene2);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*private void RedeemVoucher() {
        try {
            SC.getpassWord(stageV, "/pWordFX/NewItemNumber.fxml", "Number", "", 550.0, 50.0);

            FXVoucherViewer voucher = null;
            voucher = getVoucherXML(SC.getGameNumber());
            JMAIL.sendEmailToThisEmail("Test", SC.getGameNumber(), "newkirkdean@gmail.com");
            //MBOX.showAlert(Alert.AlertType.ERROR, null, "HEY", "SUCCESS, \nThank You!" + " \n Voucher Amount = " + voucher.getC5() + " \nVoucher Number : " + voucher.getC6());
            int t = MBOX.confirmMakeThisChange(Alert.AlertType.ERROR, null, "SUCCESS!", "SUCCESS, \nThank You!" + " \n Voucher Amount = " + voucher.getC5() + " \nVoucher Number : " + voucher.getC6());

            if (t==0){
                return;
            }
            SC.getpassWord(stageV, "/pWordFX/NewItemNumber.fxml", "Number", "", 550.0, 50.0);
            if (isEMPValidInArrayList(SC.getGameNumber())){
                redeemVoucherXML(voucher.getC6(), String.valueOf(newEFX.getEmpID()));
            } else {
                MBOX.showAlert(Alert.AlertType.ERROR, null, "EMP NOT VALID", "Error, No Employee Found");
                return;
            }
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
            MBOX.showAlert(Alert.AlertType.ERROR, null, ex.toString(), "Error, \nThank You! \n I will fix the error.");
        }
    }*/

    
    /*public void redeemVoucherXML(String vID, String CID) {
        //vList = new readXMLToArrayList().getVoucherDataXML();
        for (int i = 0; i < vList.size(); i++) {
            if (!vID.equals(vList.get(i).getC6())) {
                if (vList.get(i).getC3().equals("Sun") || vList.get(i).getC3().equals("Mon") || vList.get(i).getC3().equals("Tue") || vList.get(i).getC3().equals("Wed") || vList.get(i).getC3().equals("Thu") || vList.get(i).getC3().equals("Fri") || vList.get(i).getC3().equals("Sat")) {
                    new ModifyXMLFileInJava().ModifyXMLVoucherRedeem(DBSP.pathNameXML + "Voucher.xml", vList.get(i).getC7(), CID, vList.get(i).getC6() + " " + fn + " " + LocalDateTime.now());
                } else {
                    //MBOX.showAlert(Alert.AlertType.ERROR, null, "HEY", "Error, \nThank You! \n This voucher redeemed already");
                    System.out.println("voucher iwth day of week not found " + vList.get(i).getC3());
                }
            
            } else {
                //MBOX.showAlert(Alert.AlertType.ERROR, null, "HEY", "Error, \nThank You! \n No Voucher Found");
                System.out.println("no voucher found " + vID);
            }
        }

    } */
     
     
    public FXVoucherViewer getVoucherXML(String vNumber) {
        FXVoucherViewer voucher = null;
        //SC.getpassWord(stageV, "/pWordFX/NewItemNumber.fxml", "Number", "", 550.0, 50.0);
        vList= new readXMLToArrayList().getVoucherDataXML();
        for (int i = 0; i < vList.size(); i++){
            if (vNumber.equals(vList.get(i).getC7())){
                new Mail_JavaFX1().sendEmailToThisEmail("Test", vList.get(i).getC1() + " " + vList.get(i).getC2() + " " + vList.get(i).getC3() + " " + vList.get(i).getC4() + " " + vList.get(i).getC5() + " " + vList.get(i).getC6() + " " + vList.get(i).getC7(), "newkirkdean@gmail.com");
                voucher = new FXVoucherViewer(vList.get(i).getC1(),vList.get(i).getC2(), vList.get(i).getC3(),vList.get(i).getC4(),vList.get(i).getC5(),vList.get(i).getC6(),vList.get(i).getC7());
            }
        }
        return voucher;
    }
     
     
     
     
     
     
     
     
     
     
     
     
     
     

    private void getStageV() {
        stageV = (Stage) voucherButton.getScene().getWindow();
        System.out.println(new settingsFXML().getCounterSettings("stage"));
        if (new settingsFXML().getCounterSettings("stage").equals("1")) {
            counterScene = "1";
        } else {
            counterScene = "2";
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

    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        SC.setGameNumber(null);
        EFX.Flush();
        getStageV();

        SC.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
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

    private void superClockOut(String empID, String empName) {
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (newEFX.getBcarsales() != 1) {
                return;
            }
            System.out.println("Made it this far, Super is " + newEFX.nameF);
            new tClockDB().insertClockOut(Integer.parseInt(empID), "ERROR", 0.0);

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void superIn() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (newEFX.getBcarsales() != 1) {
                return;
            }
            MemberViewController wController = (MemberViewController) FXLOADER.getController();
            wController.SC = SC;
            wController.dbsp = DBSP;
            wController.cssPath = cssPath;
            wController.DB = DB;
            wController.FXLOADER = FXLOADER;
            wController.EFX = EFX;
            wController.rCeipts = Receipts;
            wController.jmail = JMAIL;
            wController.mbox = MBOX;
            wController.chkbalance = CHKBALANCE;
            wController.eID = newEFX.empNumber;
            wController.member = member;
            wController.d = member;
            wController.mt = memtick;
            wController.newEFX = newEFX;
            wController.lastMember = this.lastMember;
            //wController.iLastTran = this.iLastTran;

            getStageV();
            SC.loadSceneRemoveDepPane(stageV, "/views/MemberView.fxml", depPane, 275.0, 50.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.P && ke.isAltDown()) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F1) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F2) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F3) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F4) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F5) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F6) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F7) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F8) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F9) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F10) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F11) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F12) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.ENTER) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
            }
        });
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                //System.out.println("here in F1");
                GOBridgeReceipts();
                //fFocusButton.fire();
                break;
            case P:
                superIn();
                break;
            case F2:
                VIPButton.fire();
                break;
            case F3:
                GOBridgeReceipts();
                //printRButton.fire();
                break;
            case F4:
                GOBridgeReceipts();
                //dropButton.fire();
                break;
            case F5:
                //ShowErrorContextMenu("Erro: there is something wrong with your computer \n you must reboot!", 400, 500);
                GOBridgeReceipts();
                //pdOutButton.fire();
                break;
            case F6:
                birthdayButton.fire();
                break;
            case F7:
                voucherButton.fire();
                break;
            case F8:
                timeClockButton.fire();
                break;
            case F9:
                memSearchButton.fire();
                break;
            case F10:
                prtNewMemberButton.fire();
                break;
            case F11:
                //printThis2();
                break;
            case F12:
                gProblems.fire();
                break;
            case ENTER:
                break;
            case ESCAPE:
                break;
            default:
                break;
        }
    }

    private void exitProcessDO() {
        new writeArrayListToXMLFile().writeLastMemberTransaction(lastMember, "xLM_" + DBSP.localMachine.getHostName());
        Stage stage = (Stage) voucherButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    private void printEscPosBRIDGETEST(String name, String number) throws IOException, PrintException {
        //printEscPos
        PrintService foundService = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob dpj = foundService.createPrintJob();

        System.out.println("This si the Default Printer " + foundService.getName());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(27); // ESC
        outputStream.write('@');

        //PRINT LOGO IF AVAILIBLE
        EscPos escpos = new EscPos(outputStream);
        Style Format = new Style();
        //PRINT EMP NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Employee: " + "DEAN");
        escpos.feed(1);

        //PRINT DATE TIME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, new GetDay().Local_Date_Time_AMPM());
        escpos.feed(1);

        //PRINT THE BAR CODE
        BarCode barcode = new BarCode();
        barcode.setHRIPosition(BarCode.BarCodeHRIPosition.BelowBarCode);
        barcode.setJustification(EscPosConst.Justification.Center);
        escpos.write(barcode, "5444661");

        // feed 5 lines
        outputStream.write(27); // ESC
        outputStream.write('d');
        outputStream.write(5);

        // cut
        outputStream.write(29); // GS
        outputStream.write('V');
        outputStream.write(48);

        // do not forguet to close outputstream
        outputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc doc = new SimpleDoc(inputStream, flavor, null);
        dpj.print(doc, null);
    }

    
    
      private void printEscPosWITHDRAWAL()  throws PrintException, IOException{
        //printEscPos
        PrintService foundService = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob dpj = foundService.createPrintJob();
        
        System.out.println("This si the Default Printer " + foundService.getName());
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        outputStream.write(27); // ESC
        outputStream.write('@');
     
         
        
        //PRINT LOGO IF AVAILIBLE
        EscPos escpos = new EscPos(outputStream);

        RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
        BufferedImage  githubBufferedImage = null;
        try {
            githubBufferedImage = ImageIO.read(new File(DBSP.pathNameImages + "/ReceiptLogo/withdrawalLogo.png"));
            EscPosImage escposImage = new EscPosImage(githubBufferedImage, new BitonalThreshold());         
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, escposImage);
        } catch (IOException ex) {
            //jmail.sendEmailTo("NO RECEIPT LOGO FOUND","There is no logo found in the L://Images//ReceiptLogo/voucherLogo.png Directory", "errors");         
            System.out.println("There is no logo found in the L://Images//ReceiptLogo/withdrawalLogo.png Directory " + ex);
        }
        
        

        //PRINT THE COMPANY NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, getR.getReceipts(Receipts, "CoName"));
        escpos.feed(1);
         
        //PRINT THE SUBCOHEADING
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printSubCoHeading = getR.getReceipts(Receipts, "SubCoHeading");
            escpos.write(Format, printSubCoHeading);
            escpos.feed(1);

        //PRINT THE ADDRESS
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printAddress = getR.getReceipts(Receipts, "Address");
            escpos.write(Format, printAddress);
            escpos.feed(1);
        
        //PRINT THE ADDRESS2
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printAddress2 = getR.getReceipts(Receipts, "Address2");
            escpos.write(Format, printAddress2);
            escpos.feed(1);
         
        //PRINT THE PHONE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printPhone = getR.getReceipts(Receipts, "Phone");
            escpos.write(Format, printPhone);
            escpos.feed(1);
        

        //PRINT THE WWW
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printWWW = getR.getReceipts(Receipts, "WWW");

            escpos.write(Format, printWWW);
            escpos.feed(1);

        //PRINT RECEIPT HEADING (WITHDRAWAL)
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "CLUB WITHDRAWAL");
        escpos.feed(2);

        
        
        //PRINT MEMBER NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "BOB SMITH");
        escpos.feed(1);        
        
        
        //PRINT EMPLOYEE NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Employee: " + "JUNE");
        escpos.feed(1);        

        //PRINT THE DATE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        
        escpos.write(Format, LocalDate.now().toString());
        escpos.feed(1);
        
        
        //PRINT LINE SEPERATOR
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "----------------------------------------");
        escpos.feed(1);     
        

        //PRINT PREV BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Prev Balance" + ":" + "\t" + "400");
        escpos.feed(1);        
        

        //PRINT WITHDRAWAL AMOUNT
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Withdrawal" + ":" + "\t" + "200");
        escpos.feed(1);        

        //PRINT NEW BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "New Balance" + ":" + "\t" + "200");
        escpos.feed(1);        
        
        
            escpos.write(Format, "2" + " " + "FLUFFY BIRD" + " " + "100");
            escpos.write(Format, "1" + " " + "CANDY BAR" + " " + "100");
            escpos.feed(1);        


        escpos.feed(2);        
        
        

        
        //PRINT THE FOOTER1
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        String printFOOTER1 = getR.getReceipts(Receipts, "Footer1");
            escpos.write(Format, "this is a footer note");
            escpos.feed(1);

        
        
        
        
       
        // feed 5 lines
        outputStream.write(27); // ESC
        outputStream.write('d');
        outputStream.write(5);

        // cut
        outputStream.write(29); // GS
        outputStream.write('V');
        outputStream.write(48);


        // do not forguet to close outputstream
        outputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());


        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc doc = new SimpleDoc(inputStream, flavor, null);
        dpj.print(doc, null);

        

    }
}
