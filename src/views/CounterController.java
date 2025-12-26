package views;




import views.counterPopUp.SearchMemberViewController;
import views.counterPopUp.EmailEditorPopUpController;
import views.settings.HelpVideosViewerController;
import views.vouchers.FXVoucherDialogController;
import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import XML_Code.readXMLToArrayList;
import XML_Code.writeArrayListToXMLFile;
import animations.FlyingTigerSwing_Util;
import clockoutatnight.clockOutAtNightFX;
import commoncodes.ClubFunctions;
import commoncodes.GetReceipts;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import messageBox.messageBox;
import models.club.CheckBalanceDB;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;
import models.toys.Toys;
import models.toys.ToysDB;
import models.settings.VIPPromos;
import models.club.rCeipts;
import java.io.PrintWriter;
import java.time.LocalDate;
import models.club.LastMemberTransaction;
import models.settings.DailyBonus;
import models.timeclock.EmpFileFXDB;
import models.voucher.EaterCodes;
import pReceipts.prtReceiptsFX;
import pWordFX.empDB;
import pWordFX.empFX;
import pWordFX.employeeFX;
import views.counterPopUp.scanCardAgainController;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;
import views.counterPopUp.AdditionalCardPopUpController;
import views.timeclock.EmpDetailPopUpCounterController;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class CounterController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private Button voucherButton;    
    @FXML private Button depositButton;    
    @FXML private Button withdrawalButton;    
    @FXML private Button memUpdateButton;
    @FXML private Button VIPButton;
    @FXML private Button printDetailButton;
    @FXML private Button printNewMemberButton;
    @FXML private Button emailButton;
    @FXML private Button memSearchButton;
    @FXML private Button memberBalanceButton;
    @FXML private TextField CCNumberIn;
    @FXML private Label memNameLabel;
    //@FXML private Label percentage;
    @FXML private Label memBalanceLabel;
    @FXML private Label menuItemsLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label customerBalanceLabel;
    @FXML private Label customerAddressLabel;
    @FXML private Label customerAddressLabel1;
    @FXML private Region hBoxSpacer;
    @FXML private HBox titleHBox;
    @FXML private Pane depPane;
    @FXML private Pane customerInfoBox;
    @FXML private Stage stageV;
    

    private static final ContextMenu CONTEXTMENU = new ContextMenu();
    private static final dbStringPath DBSP = new dbStringPath();
    private static final cssChanger CSSC = new cssChanger();
    private static final settingsFXML SG = new settingsFXML();
    private static final employeeFX EFX = new employeeFX();
    private static final SceneChanger_Main SC = new SceneChanger_Main();
    private static final DB DB = new DB();
    private static final CheckBalanceDB CHKBALANCE = new CheckBalanceDB();
    private static final FXMLLoader FXLOADER = new FXMLLoader();
    private static final messageBox MBOX = new messageBox();
    private static final clockOutAtNightFX CLOCKOUTATNIGHT = new clockOutAtNightFX();
    private static final Mail_JavaFX1 JMAIL = new Mail_JavaFX1();
    private static final GetDay GETDAY = new GetDay();
    //private static final MemoryTest MEMORYTEST = new MemoryTest();
    //private static final DecimalFormat DF = new DecimalFormat("#");
    private static LastMemberTransaction LMT = new LastMemberTransaction("2007433", "4793", "DayStart", "Bridge", "DayStart");
    private static int iLastTran = 0;
    private static ArrayList<LastMemberTransaction> lastMember = new ArrayList();
    //private static FlyingTigerSwing_Util FTSU = new FlyingTigerSwing_Util();
    
    private static int VIPMultiplier;
    private static ArrayList<Toys> l;
    private static ArrayList<empFX> EE;
    private static ArrayList<empFX> EENEW;
    private static ArrayList<String> V; //THIS IS VIPPROMOS SHORTLIST OF NAME  / INITIALS
    private static ArrayList<EaterCodes> TEC; //TicketEaterCodes
    private static ArrayList<VIPPromos> V2; //THIS IS THE FULL DETAIL OF vip PROMOS
    private static ArrayList<rCeipts> Receipts;
    private static ArrayList<DailyBonus> Daily;
    private static Member member = null;
    private static Memtick memtick = null;
    private static PrintWriter pw = null;
    private String cssPath = null; 
    public empFX newEFX = null;
    private Bounds boundsInScene2, boundsInScene3;
    private static String newMemberReceipt = null;
    private boolean goAnimation = true;
    private static int firstRun = 0;
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //boundsInScene = depPane.localToScene(depPane.getBoundsInLocal());
        boundsInScene2 = root.localToScene(root.getBoundsInLocal());
        boundsInScene3 = menuItemsLabel.localToScene(menuItemsLabel.getBoundsInLocal());
        titleHBox.setHgrow(hBoxSpacer, Priority.ALWAYS);
        DBSP.setName();
        DB.disConnect();
        setKeyCodes();
        setTimer();
        addTextfieldListeners();
        buildMenuButton();
        cssPath = CSSC.cssPath();
        root.getStylesheets().add(cssPath);
        CCNumberIn.setStyle("-fx-prompt-text-fill:BLACK;");
        customerAddressLabel.setText("");
        customerBalanceLabel.setText("");
        customerAddressLabel1.setVisible(false);
        memNameLabel.setVisible(false);
        memBalanceLabel.setVisible(false);
        printDetailButton.setVisible(false);
        depPane.setVisible(false);
        customerInfoBox.setVisible(true);
        customerInfoBox.setLayoutX(boundsInScene2.getMinX() + 175.0);
        customerInfoBox.setLayoutY(boundsInScene2.getMinY() + 175);
        customerNameLabel.setText("");        
        Platform.runLater(() -> setCloseCatch());
        Platform.runLater(() -> startLastMemberTransaction());
        Platform.runLater(() -> GetLists());
        Platform.runLater(() -> runInit());
        checkTime();
    }

    private void runInit() {
        customerAddressLabel1.setVisible(false);
        depPane.setVisible(true);
        customerInfoBox.setVisible(false);
        customerNameLabel.setText("");
        customerAddressLabel.setText("");
        customerBalanceLabel.setText("");
        memNameLabel.setVisible(true);
        memBalanceLabel.setVisible(true);
        Platform.runLater(() -> CCNumberIn.requestFocus());

    }

    public void enterKeyPressed() {
        if (CCNumberIn.isFocused()) {
            if (CCNumberIn.getText().trim().length() > 9) {
                int msg = MBOX.confirmMakeThisChangeButtonTitles(Alert.AlertType.ERROR, null, "STOP!", "VOUCHER NUMBER? \n Should I open the Voucher Screen?", "YES", "NO");
                if (msg == 1) {
                    GOvoucherButton();
                }
            }
            if (!CCNumberIn.getText().trim().isEmpty()) {
                getMemberInfo();
            }
            return;
        }

    }

    private void getMemberInfo() {
        if (!DB.isMemberPojos(CCNumberIn.getText())) {
            MBOX.showAlert(Alert.AlertType.ERROR, null, "NoValid Number", "This Number is a non usable Number.");
            CCNumberIn.clear();
            return;
        }
        //Timeline tml = new Timeline();
        try {
            
            member = DB.getMember(CCNumberIn.getText());
            if (!member.getNameF().equals("inValid")) {
                DB.disConnect();
                customerInfoBox.setVisible(true);
                depPane.setVisible(false);
                customerInfoBox.setLayoutX(boundsInScene2.getMinX() + 175.0);
                customerInfoBox.setLayoutY(boundsInScene2.getMinY() + 175);
                if (member.getNameL().equals("FASTPASS")) {
                    customerNameLabel.setText(member.getNameF());
                } else {
                    customerNameLabel.setText(member.getNameF() + " " + member.getNameL());
                }
                customerAddressLabel.setText(member.getAddress());
                customerBalanceLabel.setText(String.valueOf(member.getBalance()));
                customerAddressLabel1.setVisible(true);
                printDetailButton.setVisible(true);
                customerNameLabel.requestFocus();
                DB.disConnect();
                
            } else {
                String CCNText = CCNumberIn.getText().trim();
                CCNumberIn.clear();
                CCNumberIn.requestFocus();
            scanCardAgainController wController = new scanCardAgainController();
            wController.lastmember = null;
            wController.newEFX = newEFX;
            wController.sc = SC;
            wController.cssPath = cssPath;
            wController.FXLOADER = FXLOADER;
            wController.dbsp = DBSP;

                SC.getpassWord(stageV, "/views/counterPopUp/scanCardAgain.fxml", "Number", "Scan Card Again:", boundsInScene2.getMinX() + 250.0, boundsInScene2.getMinY() + 90.00);

                if (!CCNText.equals(SC.getGameNumber())) {
                    MBOX.showAlert(Alert.AlertType.ERROR, stageV, "No Match", "The numbers you scanned do not Match");
                    CCNumberIn.clear();
                    CCNumberIn.requestFocus();
                } else {
                    if (!DB.isInactiveMemberValid(SC.getGameNumber())) {
                        DB.disConnect();
                        if (SC.getGameNumber().equals("100001")) {
                            MBOX.showAlert(Alert.AlertType.ERROR, stageV, "No Number", "The number you have entered is a non usable number. \nUse another number.");
                        } else {
                            GOnewMember(SC.getGameNumber());
                        }
                    } else {
                        MBOX.showAlert(Alert.AlertType.INFORMATION, stageV, "Inacvtive Member Found", "This member has been found in the Inactive File and has been transfered back to the active file. please SCAN card again and have them update the receipt with currnet information.");

                    }
                }
            }
        } catch (IOException | SQLException e) {
            //JOptionPane.showMessageDialog(null, e);
            CCNumberIn.setDisable(false);
            CCNumberIn.clear();
            CCNumberIn.requestFocus();
        }
        DB.disConnect();
    }

    
    
    public void printMemberDetail() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (!newEFX.employeeLevel("Vouchers")) {
                return;
            }
            //ObservableList<Memtick> detailList = DB.getDetail(member.getCID());
            DB.printMemberDetail(member, newEFX, V2, EE, pw);
            member = null;

        } catch (IOException ex) {
            System.out.println(ex);
        }
        DB.disConnect();
        closeBalance();
        backToCCNumberIn();
    }

    public void printMemberUpdate() {
        String Email = "";
        try {
            if (!loginButtonPushed()) {
                return;
            }
            if (!newEFX.employeeLevel("Vouchers")) {
                return;
            }
            scanCardAgainController wController = new scanCardAgainController();
            wController.lastmember = lastMember;
            wController.iLastTran = iLastTran;
            wController.newEFX = newEFX;
            wController.sc = SC;
            wController.cssPath = cssPath;
            wController.FXLOADER = FXLOADER;
            wController.dbsp = DBSP;
            SC.getpassWord(stageV, "/views/counterPopUp/scanCardAgain.fxml", "Number", "Scan Member Card:", boundsInScene2.getMinX() + 450.0, boundsInScene2.getMinY() + 250.00);
            
            //DB.connect();
            member = DB.getMember(SC.getGameNumber());
            if (!"1".equals(SC.getActivity())) {
                updateLastMember(true);
            }
            try {
            Email = DB.getEmail(member.getCID()).toUpperCase();
            } catch (Exception e) {
                //System.out.println("=====================================================");
                Email = "Email: NONE";
            }
            if (Email != null || !"".equals(Email) || !"non".equals(Email) || !"NON".equals(Email)) {
            } else {
                Email = "Email: NONE";
            }
            if (member.getNameF().equals("inValid")) {
                MBOX.showAlert(Alert.AlertType.ERROR, stageV, "Error", "No Member Found");
            } else {
                String tranTime = new GetDay().getCurrentTimeStamp();
                String timeID = new ClubFunctions().formatTimeStringtoNumber(tranTime);
                DB.printMemberUpdate(member, newEFX, Email, pw, Receipts, timeID);
                if (!DB.InsertHistoricDataOneTimeXplain(member.getCID(), newEFX.getEmpNumber(), tranTime, "UpDPrint", "Printed By: " + newEFX.getNameF(), timeID)) {
                }

                member = null;
            }
            DB.disConnect();

        } catch (IOException | SQLException ex) {
            System.out.println(ex);
        }
        DB.disConnect();
        backToCCNumberIn();
    }

    ///LASTMEMBER--------------------------------------------------------------
    ///
    ///
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
    
    
    private void updateLastMember(boolean update) {
        System.out.println("=-=-=-=-=-=-=-=-=-=-=- update " + update);
        if (update) {
            LMT = new LastMemberTransaction(SC.getCCN(), SC.getEmployee(), SC.getEmpName(), member.getNameL(), SC.getActivity());
        } else {
            LMT = new LastMemberTransaction(SC.getCCN(), SC.getEmployee(), SC.getEmpName(), SC.getLastName(), SC.getActivity());
        }
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
    
    ///
    ///
    ///
    ///THIS IS THE END OF LASTMEMNBER-------------------------------------------

    public void GOnewMember(String n) throws IOException {
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
        wController.m = member;
        wController.mt = memtick;
        wController.rCeipts = Receipts;
        wController.VIPPromosDailyBonus = Daily;
        wController.pw = pw;
        wController.lastmember = lastMember;
        wController.NEWMEMRECEIPT = newMemberReceipt;
        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/NewMember.fxml", depPane, 200.0, 50.0);
        updateLastMember(false);
        backToCCNumberIn();
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
        wController.m = member;
        wController.mt = memtick;
        wController.eFX = EFX;
        wController.mBox = MBOX;
        wController.jmail = JMAIL;
        wController.FXLOADER = FXLOADER;
        wController.chkBalance = CHKBALANCE;
        wController.VIPMultiplier = this.VIPMultiplier;
        wController.pw = pw;
        wController.lastmember = lastMember;
        wController.iLastTran = this.iLastTran;
        SC.loadSceneRemoveDepPane(stageV, "/views/MemVIP.fxml", depPane, 200.0, 50.0);
        updateLastMember(false);
        checkTime();
        backToCCNumberIn();
    }

    public void GOvoucherButton() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        
        if (!newEFX.employeeLevel("Vouchers")) {
            return;
        }
        EFX.SetEmployeeFX(newEFX.getNameF(), newEFX.getNameL(), newEFX.getEmpNumber(), newEFX.getVAmt(), newEFX.getGProb(), newEFX.getEmpID());
        FXVoucherDialogController wController = (FXVoucherDialogController) FXLOADER.getController();
        String t = new GetReceipts().getReceiptsNumber(Receipts, "Voucher_Reason");
            wController.dbsp = DBSP;
            wController.css = cssPath;
            wController.eFXX = EFX;
            wController.css = cssPath;
            wController.eFXX = EFX;
            wController.mBox = MBOX;
            wController.receipts = Receipts;
            wController.TEC = TEC;
            wController.jmail = JMAIL;
            wController.vAmt = EFX.getVAmt();
            wController.rCeiptvMount = Double.valueOf(t);
            wController.FXLOADER = FXLOADER;
            wController.sc = SC;
            wController.db = DB;     
            wController.db = DB;
            wController.pw = pw;
            wController.chkBalance = CHKBALANCE;
            wController.member = member;
            wController.memtick = memtick;
            wController.Daily = Daily;
            wController.EE = EE;
            wController.l = l;
            
            
        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/vouchers/FXVoucherDialog.fxml", depPane, boundsInScene2.getMinX()+300.0, boundsInScene2.getMinY()+50.0);
        
        } catch (IOException ex) {
        
        }
        backToCCNumberIn();
    }
    
    private void GOEmailEditor() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        EmailEditorPopUpController wController = (EmailEditorPopUpController) FXLOADER.getController();
        wController.typeVar = "3";
        wController.rCeipts = Receipts;
        wController.dbsp = DBSP;
        wController.chkbalance = CHKBALANCE;
        wController.cssPath = cssPath;
        wController.m = member;
        wController.mt = memtick;
        wController.db = DB;
        wController.mBox = MBOX;
        wController.sc = SC;
        wController.empID = String.valueOf(newEFX.getEmpID());
        SC.getpassWord(stageV, "/views/counterPopUp/EmailEditor.fxml", null, null, 420.0, 200.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
    }
    
    private void GOAdditionalCard() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        AdditionalCardPopUpController wController = (AdditionalCardPopUpController) FXLOADER.getController();
        wController.typeVar = "3";
        wController.rCeipts = Receipts;
        wController.dbsp = DBSP;
        wController.chkbalance = CHKBALANCE;
        wController.cssPath = cssPath;
        wController.m = member;
        wController.mt = memtick;
        wController.db = DB;
        wController.mBox = MBOX;
        wController.sc = SC;
        wController.empID = String.valueOf(newEFX.getEmpNumber());
        System.out.println("here we are so far " + newEFX.getEmpNumber());
        SC.getpassWord(stageV, "/views/counterPopUp/AdditionalCard.fxml", null, null, 420.0, 200.0);
        } catch (IOException ex) {
            System.out.println("Login Button Pushed Error: " + ex);
        }
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
        wController.VIPPromosDailyBonus = Daily;
        wController.pw = pw;
        wController.voucherDeposit = "0";
        wController.tec = TEC;
        wController.lastmember = lastMember;
        wController.iLastTran = this.iLastTran;
        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/MemDepositFX.fxml", depPane, 200.0, 50.0);
        updateLastMember(false);
        backToCCNumberIn();
    }

    public void GOwithdrawalButton() throws IOException {
        MemWithdrawalFXController wControllerW = (MemWithdrawalFXController) FXLOADER.getController();
        wControllerW.l = this.l;
        wControllerW.E = this.EE;
        wControllerW.rCeipts = Receipts;
        wControllerW.cssPath = cssPath;
        wControllerW.db = DB;
        wControllerW.dbsp = DBSP;
        wControllerW.empFX = newEFX;
        wControllerW.mBox = MBOX;
        wControllerW.sc = SC;
        wControllerW.jmail = JMAIL;
        wControllerW.chkBalance = CHKBALANCE;
        wControllerW.fxloader = FXLOADER;
        wControllerW.pw = pw;
        wControllerW.lastmember = lastMember;
        wControllerW.iLastTran = this.iLastTran;
        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/MemWithdrawalFX.fxml", depPane, 200.0, 50.0);
        updateLastMember(false);
        backToCCNumberIn();
    }

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
        wController.csspath = cssPath;
        wController.db = DB;
        wController.dbsp = DBSP;
        wController.pw = pw;
        wController.receipts = Receipts;
        SC.loadSceneRemoveDepPane(stageV, "/views/counterPopUp/SearchMemberView.fxml", depPane, 300.0, 100.0);
        backToCCNumberIn();
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
            wController.screen = "COUNTER NOTE";
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
        backToCCNumberIn();
        return;

    }
    

    public void prtNewMember() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("New Member Button Pushed Error: " + ex);
        }
        new prtReceiptsFX().prtNewMember(newEFX.nameF, pw);
        backToCCNumberIn();
    }

    private void setTimer() {
        //System.out.println("we have run through setTimer(" + LocalDateTime.now() + ")");
        //PauseTransition pTransition = new PauseTransition(Duration.minutes(110.0));
        PauseTransition pTransition = new PauseTransition(Duration.minutes(2.0));
        pTransition.setOnFinished((e) -> {
            checkTime();
            pTransition.play();
        });
        pTransition.play();

    }

    public void checkTime() {
        //System.out.println("we have run through checkTime(" + LocalDateTime.now() + ")");
        //if (GETDAY.getDateLocalDateHour()>=3 && GETDAY.getDateLocalDateHour()<=5) {
            CLOCKOUTATNIGHT.closeCafeScreen("xLM_" + DBSP.localMachine.getHostName());
        //}
        //percentage.setText(String.valueOf(DF.format(MEMORYTEST.runGC())) + "%");
    }   

    
    private void GetLists() {
        //Platform.runLater(() -> connLocal = new ToysDB().connectLocal());
        Platform.runLater(() -> l = new ToysDB().getList());
        setToyList(l);
        Platform.runLater(() -> newMemberReceipt = DB.getNewMemberReceipt());
        //setEMPList(EE);
        Platform.runLater(() -> EE = new EmpFileFXDB().getDimDataXML());
        setEMPList(EE);
        Platform.runLater(() -> V = DB.getAllPromos());
        setVIPList(V);
        Platform.runLater(() -> V2 = DB.getAllPromosArrayList());
        setVIPList2(V2);
        Platform.runLater(() -> VIPMultiplier = DB.getVIPMultipier());
        setVIPMultiplier(VIPMultiplier);
        Platform.runLater(() -> Receipts = DB.getReceipts());
        setReceipts(Receipts);
        Platform.runLater(() -> Daily = DB.getAllDailyArrayList());
        setDailyList(Daily);
        Platform.runLater(() ->TEC = new readXMLToArrayList().getTicketEaterCodesXML());
        //setReceipts1(Receipts1);
        //Platform.runLater(() -> runI());
        Platform.runLater(() -> DB.disConnect()); 

    }
    
    
    
    private void setDailyList(ArrayList Daily) {
        this.Daily = Daily;
    }
    
    
    private void setToyList(ArrayList l) {
        this.l = l;
    }
    
   
    
    private void runI() {
        //System.out.println("we are headed into this one " + EE.size());
        for (empFX empList : EE) {
            System.out.println(empList.getEmpNumber() + " " + empList.getNameF() + " " + empList.getNameL());
            //listofDimFile += tvShow.getENumber() + " " + tvShow.getFName() + " " + tvShow.getLName() + " \n";
        }
    }
    
    
    
    
   // private void runI() {
   //     Iterator<Toys> tvShowIterator = l.iterator();
   //     while (tvShowIterator.hasNext()) {
   //         Toys tvShow = tvShowIterator.next();
   //         System.out.println(tvShow.getName());
   //}
   // }
    
    //private void setReceipts1(ArrayList Receipts1) {
    //    this.Receipts1 = Receipts1;
    //}


    private void setVIPMultiplier(int m){
        this.VIPMultiplier = m;
    }
    
    public void actionPerformed() {
        checkTime();
    }

    public ArrayList getToyList() {
        return l;
    }
    
    private void setVIPList(ArrayList V) {
        this.V = V;
    }

    private void setVIPList2(ArrayList V) {
        this.V2 = V;
    }

    //private void PrintReceipts() {
    //    for (int y = 0; y < Receipts.size(); y++) {
            //if (n.trim().equals(Receipts.get(y).getrItem())) {
    //            System.out.println(Receipts.get(y).getrItem() + " " + Receipts.get(y).getrString() + " " + Receipts.get(y).getrNumber());
                //numberItem = Receipts.get(y).getrNumber();
                //StringItem = Receipts.get(y).getrString();
            //}
        //}
    //}

    
    
    
    private void setReceipts(ArrayList Receipts) {
        this.Receipts = Receipts;
    }

    private void setEMPList(ArrayList E) {
        this.EE = E;
    }

    public ArrayList getEMPList() {
        return EE;
    }
    
    
    private String getVIPItemName(String n) {
        String StringItem = null;
        for (int y = 0; y < V2.size(); y++) {
            if (n.trim().equals(V2.get(y).getvInitials())) {
                //System.out.println(rCeipts.get(y).getrItem() + " " + rCeipts.get(y).getrString() + " " + rCeipts.get(y).getrNumber());
                //setrNumber(Receipts.get(y).getrNumber());
                StringItem = V2.get(y).getvName();
            }
        }
        if (n.equals("C")) {
            StringItem = "Cafe Purchase";
        }
        if (n.equals("B")) {
            StringItem = "Game Purchase";
        }
        return StringItem;
    }
    
    
    
    
    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        SC.setGameNumber(null);
        getStageV();
        SC.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", boundsInScene2.getMinX() + 650.0, boundsInScene2.getMinY() + 250);
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

    private void backToCCNumberIn() {
        DB.disConnect();
        newEFX = null;
        CCNumberIn.clear();
        CCNumberIn.requestFocus();
        return;
    }

    private void exitProcessDO() {
        new writeArrayListToXMLFile().writeLastMemberTransaction(lastMember, "xLM_" + DBSP.localMachine.getHostName());
        Stage stage = (Stage) voucherButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

    private void closeBalance() {
        if (customerInfoBox.isVisible()) {
            customerInfoBox.setVisible(false);
            depPane.setVisible(true);
            backToCCNumberIn();
        }
    }        


    private void setCloseCatch() {
        getStageV();
        stageV.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0);
        });
    }

    public void buildMenuButton() {
        MenuItem item2 = new MenuItem(" Refresh List's ");
        MenuItem item4 = new MenuItem("  Supervisor ");
        MenuItem chgScrn = new MenuItem("  Change Screen ");
        MenuItem empNotes = new MenuItem("  Emp - Notes ");
        MenuItem additionalCard = new MenuItem(" Add Card ");
        MenuItem hlpViewr = new MenuItem("  Help Viewer ");
        MenuItem emailEditor = new MenuItem("  Email Editor ");
        MenuItem memUpdate = new MenuItem(" Update Return");
        MenuItem exit = new MenuItem("      Exit     ");


        item4.setOnAction((ActionEvent event) -> {
            superIn();
        } //counter screens
        );

        item2.setOnAction((ActionEvent event) -> {
            l.removeAll(l);
            EE.removeAll(EE);
            V.removeAll(V);
            VIPMultiplier = 0;
            Receipts.removeAll(Receipts);

            GetLists();
        } //counter screens
        );

        exit.setOnAction((ActionEvent event) -> {
            exitProcessDO();
        } //clock in out
        );

        hlpViewr.setOnAction((ActionEvent event) -> {
            try {
                if (!loginButtonPushed()) {
                    return;
                }
            } catch (IOException ex) {
            }
            if (!newEFX.employeeLevel("Corporate")) {
                return;
            }
            HelpVideosViewerController wController = (HelpVideosViewerController) FXLOADER.getController();
            wController.cssPath = cssPath;
            wController.MGR = newEFX.getNameF();
            getStageV();
            try {
                SC.getpassWord(stageV, "/views/settings/HelpVideosViewer.fxml", null, null, 100.0, 50.0);
            } catch (IOException ex) {
               System.out.println(ex);
            }
            //SC.goToScene2("HelpVideos", stageV, newEFX.getNameF(), depPane, boundsInScene2.getMinX() + 230, boundsInScene2.getMinY() - 30);
        }
        );
        
        
        
        empNotes.setOnAction((ActionEvent event) -> {
            GOEmployeeNotes();
        } //counter screens
        );

        emailEditor.setOnAction((ActionEvent event) -> {
           /* try {
                if (!loginButtonPushed()) {
                    return;
                }
            } catch (IOException ex) {
            }
            //if (newEFX.getChangerEdit()<1) {
            //    return;
            //}
            */
            GOEmailEditor();
            /*
            FXMLLoader fxmlLoader = new FXMLLoader();
            HelpVideosViewerController wController = (HelpVideosViewerController) fxmlLoader.getController();
            wController.cssPath = cssPath;
            wController.MGR = newEFX.getNameF();
            getStageV();
            sc.goToScene2("emailEditor", stageV, newEFX.getNameF(), depPane, boundsInScene2.getMinX() + 230, boundsInScene2.getMinY() - 30);
            */
        }
        );


        additionalCard.setOnAction((ActionEvent event) -> {
            GOAdditionalCard();
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

                SC.getpassWord(stageV, "/views/counterPopUp/scanCardAgain.fxml", "Number", "Enter Receipt ID:", boundsInScene2.getMinX() + 250.0, boundsInScene2.getMinY() + 90.00);
                System.out.println(SC.getGameNumber());
                if (!SC.getGameNumber().equals("Number")) {
                String MemberID = DB.GetMarkerForUpdate(SC.getGameNumber());
                String tranTime = new GetDay().getCurrentTimeStamp();
                if  (!MemberID.equals("InValid")){
                    memtick = new Memtick(MemberID, newEFX.getEmpNumber(), tranTime, LocalDate.now(), 0.0, 0.0, 0.0, "WUPDate", 0);
                    DB.WaitingUpdate(MemberID, memtick);
                    MBOX.showAlert(Alert.AlertType.INFORMATION, stageV, "Waiting Update Complete", "Waiting Update Complete!");
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

        CONTEXTMENU.getItems().clear();
        CONTEXTMENU.getItems().addAll(item2, item4, chgScrn, hlpViewr, empNotes, emailEditor, additionalCard, memUpdate,  exit);

        menuItemsLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                //boundsInScene3 = menuItemsLabel.localToScene(menuItemsButton.getBoundsInLocal());
                CONTEXTMENU.show(menuItemsLabel, boundsInScene3.getMaxX() + 70.0, boundsInScene3.getMaxY() + 50.0);
                //contextMenu.show(menuItemsButton,  event.getScreenX(), event.getScreenY());
            }
        });

        menuItemsLabel.setOnContextMenuRequested((ContextMenuEvent event) -> {
            CONTEXTMENU.show(menuItemsLabel, event.getScreenX(), event.getScreenY());
        });
    }

    public void menuItemsButtonMouseOver() {
        //boundsInScene3 = menuItemsLabel.localToScene(menuItemsLabel.getBoundsInLocal());
        CONTEXTMENU.show(menuItemsLabel, boundsInScene3.getMaxX() + 70, boundsInScene3.getMaxY() + 50);

    }

    private void getStageV() {
        stageV = (Stage) voucherButton.getScene().getWindow();
    }

    private String getQTY() {
        String q = null;
        getStageV();
        //boundsInScene2 = CCNumberIn.localToScene(CCNumberIn.getBoundsInLocal());
        try {
            SC.getpassWord(stageV, "/views/counterPopUp/QTY.fxml", "Number", "Enter QTY:", boundsInScene2.getMinX() + 350, boundsInScene2.getMinY() - 100);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        q = SC.getGameNumber();
        return q;
    }

    public void addTextfieldListeners() {
        CCNumberIn.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber().checkNumbers(newValue);
                    } catch (Exception e) {
                        MBOX.showAlert(Alert.AlertType.ERROR, null, "TextField Error", "Member Card Number can only Numbers");
                        CCNumberIn.clear();
                        CCNumberIn.requestFocus();
                        return;
                    }
                }
        );
    }

    private void superIn() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        if (newEFX.getBcarsales() != 1 ) {
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

        getStageV();
        SC.loadSceneRemoveDepPane(stageV, "/views/MemberView.fxml", depPane, 275.0, 50.0);       
        } catch (IOException ex) {
            //System.out.println("Login Button Pushed Error: " + ex);
        }
        backToCCNumberIn();
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
        //System.out.println("Here");
        switch (event.getCode()) {
            case F1:
                memberBalanceButton.fire();
                break;
            case P:
                superIn();
                break;
            case F2:
                emailButton.fire();
                break;
            case F3:
                VIPButton.fire();
                break;
            case F4:
                depositButton.fire();
                break;
            case F5:
                withdrawalButton.fire();
                break;
            case F6:
                printNewMemberButton.fire();
                break;
            case F7:
                voucherButton.fire();
                break;
            case F8:
                memSearchButton.fire();
                break;
            case F9:
                memUpdateButton.fire();
                break;
            case F10:
                break;
            case F11:
                break;
            case F12:
                printDetailButton.fire();
                break;
            case ENTER:
                enterKeyPressed();
                break;
            case ESCAPE:
                closeBalance();
                break;
            default:
                break;
        }
    }

    
    
}
