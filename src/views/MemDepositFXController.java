package views;

import JavaMail.Mail_JavaFX1;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.GetReceipts;
import commoncodes.IsItANumber1;
import commoncodes.PlayText;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.club.CheckBalanceDB;
import models.club.DB;
import models.club.LastMemberTransaction;
import models.settings.DailyBonus;
import models.club.LastTranApproval;
import models.club.Member;
import models.club.Memtick;
import models.toys.Toys;
import models.settings.fWildWed;
import models.club.rCeipts;
import models.voucher.EaterCodes;
import models.voucher.FXVoucherViewer;
import models.voucher.VoucherFunctions;
import models.voucher.redeemVouchers;
import models.voucher.returnEaterCode;
import pReceipts.print;
import pWordFX.empFX;
import sceneChangerFX.SceneChanger_Main;
import views.counterPopUp.MemberUpdatePrintController;
import views.counterPopUp.lastMemberTransactionViewController;


/**
 * FXML Controller class
 *
 * @author Dean
 */
public class MemDepositFXController implements Initializable {
    @FXML private PasswordField empNumberTextfield;
    @FXML private Label customerAddressLabel;
    @FXML private Label customerBalanceLabel;
    @FXML private PasswordField CCNumberIn;
    @FXML private Label customerNameLabel;
    @FXML private TextField depositField;
    @FXML private TextField bonusField;
    @FXML private TextField wWedField;
    @FXML private TextField addingField;
    @FXML private Label addingLabel;
    @FXML private Label scrollTextLable;
    @FXML private VBox customerInfoBox;
    @FXML private Label wWedLabel;
    @FXML private Label dLabel;
    @FXML private Button doButton;
    @FXML private AnchorPane root;
    @FXML private Button fullListLMTButton;
    
    public static SceneChanger_Main sc;
    public static DB db;
    public static dbStringPath dbsp;
    private static DailyBonus TodaysDailyBonus;
    public static Member member;
    public static Memtick memtick;
    public static String cssPath;
    public static messageBox mBox;
    public static Mail_JavaFX1 jmail;
    public static CheckBalanceDB chkBalance;
    public static PrintWriter pw;
    public static FXMLLoader FXLOADER;
    
    public static ArrayList<empFX> E;
    public static ArrayList<rCeipts> rCeipts;
    public static ArrayList<Toys> l; //THIS IS HERE TO CHECK IF THE DEPOSIT AMOUNT IS A TOY ITEM NUMBER
    public static ArrayList<DailyBonus> VIPPromosDailyBonus;
    public static ArrayList<FXVoucherViewer> listToRedeem;
    public static ArrayList<FXVoucherViewer> vList;
    //VIPPromosDailyBonus = new VIPPromos(rs.getString("Name"), rs.getString("Initials"), rs.getString("Other"), rs.getInt("Tickets"), 
    // rs.getInt("Multiply"), rs.getInt("Repeat"), rs.getInt("QTY"), rs.getInt("ID"), rs.getDate("StartDate").toLocalDate(), 
    //rs.getDate("EndDate").toLocalDate(), rs.getInt("Receipt"));

    
    //private static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#");
    private static final DecimalFormat DECIMALFORMATINT = new DecimalFormat(".00");
    private static Double newBalance = 0.0, prevBalance= 0.0;
    private static String en, fn, ln, CID, rNumber;
    private static String superEN, superLN, superFN;
    public static ArrayList<EaterCodes> tec;
    //private List<Memtick> detailList =null;
    private static boolean emailIsTrue = false, voucher = false;
    public static String voucherDeposit, currentTime;
    public static String voucherEmployee;
    //public int vAmt, bal; 
    private static int Super, superID, enGprob, enID, enArcade, baseBonus, MinDep;
    private boolean confirm = false;
    private Window owner;
    private Stage stageV;
    //public String[] lastTranArray;
    private String tranTime = "", gt = "", loc = " ";
    private String rWinner = "", isEMail = "", isEMailUpdate = null;
    private LastTranApproval lta = null;
    //private DailyBonus TodaysDailyBonus;
    //private Double depAmount = 0.0;
    private int depAmount = 0;
    private Double getLastDBTransaction = 0.0;
    //private double newBalanceCHK = 0.0;
    private static boolean lastApproval = false;
    private boolean newBonus = false, wkDayTickets = false, wkDayMultiply = false, wkDayBonus = false; 
    private boolean GtttArray = false, fPass = false;
    private boolean superApprove = false, multipleVouchers =false, SCLMT = false;
    public static ArrayList<LastMemberTransaction> lastmember;
    public static int iLastTran;
    
    private static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    private static final DecimalFormat df = new DecimalFormat("#");
    private static final DateTimeFormatter MYDATEFORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter MYDATEFORMATTERR = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private static final GetReceipts getR = new GetReceipts();
    private static final char THISPAGECHAR = 1;
    private static Style Format = new Style();


   
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        //System.out.println(sc.getGameNumber());
        //scrollTextLable.setText("Urgent Employee Message Coming . . .");
        tranTime = new GetDay().getCurrentTimeStamp();
        scrollTextLable.setVisible(false);
        bonusField.setDisable(true);
        wWedLabel.setVisible(false);
        wWedField.setVisible(false);
        addingField.setVisible(true);
        addingLabel.setVisible(true);
        addingField.setFocusTraversable(false);
        addingField.setDisable(true);
        addTextfieldListeners();
        customerInfoBox.setVisible(false);
        root.getStylesheets().add(cssPath);
        setKeyCodes();        
        new FocusedTextFieldHighlight().setHighlightListener(root);
        setHighlightListenerVIP(root);
        doButton.setDisable(true);
        Platform.runLater(()->getScene());
        Platform.runLater(()->setBaseBonus());
        Platform.runLater(()->getStage());
        Platform.runLater(()->SetGTTT());
        Platform.runLater(()->CCNumberIn.requestFocus());  
        //Platform.runLater(()->PrintReceipts("1"));
    }    
    
    
    
    private void setBaseBonus() {
        
        getReceipts("Bonus", rCeipts);
        baseBonus = Integer.parseInt(DECIMALFORMAT.format(Double.parseDouble(getrNumber())));
        System.out.println("baseBonus = " + baseBonus);
        getReceipts("MinDeposit", rCeipts);
        MinDep = Integer.parseInt(DECIMALFORMAT.format(Double.parseDouble(getrNumber())));
        System.out.println("Minimum Deposit = " + MinDep);
        if (!voucherDeposit.equals("0")) {
            voucher = true;
            isEMPValidInArrayList(en);
            addingField.setText(voucherDeposit);
            empNumberTextfield.setText(voucherEmployee);
        } else {
            depositField.setText("");
            empNumberTextfield.setText("");
        }

        
        
    }
    
    private void getMemberInfo() {
        if (!db.isMemberPojos(CCNumberIn.getText())) { //THIS DOES NOT REQUIRE CONN
            mBox.showAlert(Alert.AlertType.ERROR, null, "NoValid Number", "This Number is a non usable Number.");
            CCNumberIn.clear();
            return;
        }
        try {
            member = db.GetMemberDeposit(CCNumberIn.getText()); //IS NOT CLOSEING CONN
            //m = DB.getMember(CCNumberIn.getText());
            lta = new LastTranApproval(false, 0.0);
            Platform.runLater(()->lta = GetLastTransactionDBThread(member.getCID())); //GETS THE SUM OF ALL THE TRANSACTIONS THIS DAY
            

            //lta = new DB().GetLastTransactionDBModel(m.getCID());
            //Platform.runLater(()->getLastApproval = GetLastApprovalThread(m.getCID())); //GETS THE SUM OF ALL THE TRANSACTIONS THIS DAY

            //Platform.runLater(()->getLastDBTransaction = GetLastTransactionDBThread(m.getCID())); //GETS THE SUM OF ALL THE TRANSACTIONS THIS DAY
            //Platform.runLater(()->getLastDBTransaction = GetLastTransactionDBThread(m.getCID())); //GETS THE SUM OF ALL THE TRANSACTIONS THIS DAY




            if (!member.getNameF().equals("inValid")) {
                customerInfoBox.setVisible(true);
                if (member.getNameL().equals("FASTPASS")) {
                    fPass = true;
                    System.out.println("here in fastpass " + fPass);
                    customerNameLabel.setText(member.getNameF());
                } else {
                    customerNameLabel.setText(member.getNameF() + " " + member.getNameL());
                }
                customerAddressLabel.setText(member.getAddress());
                isEMail = db.getEmail(member.getCID());
                SetEmail();
                if (member.getBalance() < 0) {
                    new Mail_JavaFX1().sendEmailTo("Customer Balance Negative", member.getBalance() + " " + member.getCCN() + "\n" + dbsp.localMachine, "errors");
                    customerBalanceLabel.setText(String.valueOf(0.0));
                } else {
                    customerBalanceLabel.setText(String.valueOf(member.getBalance()));
                }
            } else {
                if (db.isInactiveMemberValid(CCNumberIn.getText())) { //USES CONNI NOT CONN
                    mBox.showAlert(Alert.AlertType.INFORMATION, owner, "Member Found", "This Member was found in the inactive file. Please scan card again.");
                    CCNumberIn.clear();
                    CCNumberIn.requestFocus();
                    return;
                } else {
                    db.disConnect();
                    mBox.showAlert(Alert.AlertType.INFORMATION, owner, "No Such Number", "Member Number Not Found in the ACTIVE and the INACTIVE \n files. Please have a Supervisor do a search.");
                    CCNumberIn.clear();
                    CCNumberIn.requestFocus();
                    return;
                }
            }
        } catch (HeadlessException | SQLException e) {
            mBox.showAlert(Alert.AlertType.ERROR, owner, "Database search Error", e.toString());
            jmail.sendEmailTo("Error in getMember() in Deposit.", "Database search Error " + e.toString() + "\n" + dbsp.localMachine, "errors");
            CCNumberIn.clear();
            CCNumberIn.requestFocus();
            return;
        }


        //THIS IS DONE IN DEPOSIT AND NOW NEED VIP TO BE DONE.. NEED TO ADD UPDATEFOOTERS, need to UPDATE THE TESTBALANCES REPORT
        //SO THAT IT READS THESE FOR WRONG ADDRESS ACCOUTNS AND THEN TRANSFERES THEM TO mEMTICK, ADD mARKERS TABLE TO pOJOS
        // NEED TO CLEAN OUT TABLE EACH NIGHT.
        if (member.getAddress().equals("WRONG ADDRESS")) {
            if (!db.Check10Thousand(member.getCID(), new GetDay().asSQLDate(LocalDate.now()), "UpDPrint")) {
                GetMemberUpdatePrint();            
            }
        }

        //db.disConnect(); TAKING THIS OUT ON 9/23 TO SEE IF WE CAN GET THRU THE WHOLE PRECESS WITHOUT CLOSEING CONN
        depositField.requestFocus();
    }

    //IN ENTERKEYPRESSED() THIS IS USED TO SET THE SCREEN GREATER THAN 10,000
    private boolean TestLastTranDB() {
        boolean testLastTranDB = false;
        if (getLastDBTransaction + depAmount > Double.parseDouble(gt)) {
            testLastTranDB = true;
        }
        if (lastApproval) {
            testLastTranDB = false;
        }
        return testLastTranDB;
    }

    

    public void doButtonPushed() {
        boolean GO = false;
        //String loc = " ";
        
        //if (depositField.getText().trim().equals("")) {
        //    depositField.requestFocus();
         //   return;
        //} else {
            //bonusField.setText(String.valueOf(db.calcBonus(Integer.parseInt(depositField.getText()))));
        //}
        en = empNumberTextfield.getText().trim();
        if (en.isEmpty()) {
            mBox.showAlert(Alert.AlertType.ERROR, null, "Employee Number", "Employee Number cannot be blank()");
            empNumberTextfield.clear();
            empNumberTextfield.requestFocus();
            return;
        } else {
            try {
                if (!loginButtonPushed()) {
                    mBox.showAlert(Alert.AlertType.ERROR, null, "Employee Number", "Employee Number Not Found");
                    empNumberTextfield.clear();
                    empNumberTextfield.requestFocus();
                    return;
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        //GETSUPERVISORAPPROVAL
        //ONE LAST THING IN dAILY REPORT NEED GTTT APPROVALS TO SHOW IN THE REPORT
        getReceipts("GTTTPopup", rCeipts);
        if (getrNumber().equals("1.0")) {
            getReceipts("GreaterThanDepWith", rCeipts);

            if (!fPass) {
                if (Double.valueOf(getrNumber()) < Double.valueOf(addingField.getText().trim()) || GtttArray == true) {
                    //if (enGprob < 2) {
                    System.out.println("arcade = " + enArcade);
                    if (enArcade == 0 || enArcade == 2) {
                        try {
                            sc.getpassWord(stageV, "/pWordFX/withdrawalPassword.fxml", "Number", "Enter Employee Number:", 200.00 + 350.0, 200.00);
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }
                        if (!isSuperValidInArrayList(sc.getGameNumber())) {
                            GO = false;
                            mBox.showAlert(Alert.AlertType.ERROR, stageV, ln, "Not a Valid Employee Number");
                            return;
                        } else {
                            GO = true;
                            System.out.println("Super integer = " + Super);
                            if (Super != 1 && Super != 3) {
                                mBox.showAlert(Alert.AlertType.ERROR, stageV, ln, "not a valid Supervisor number for this task");
                                return;
                            }
                        }
                        //THIS IS WHERE YOU WILL SEND SUPEREN TO CHECK TO SEE IF THEY ARE A VALID EMP THEN SEND THAT TO THE MEMTICK 9/23
                        Platform.runLater(() -> GoPostSuperApprovalDetails(String.valueOf(superID)));
                        superApprove = true;
                    } else {
                        Platform.runLater(() -> GoPostSuperApprovalDetails(String.valueOf(superID)));
                        superApprove = true;
                    }
                }

            } else {//END OF FASTPASS
                if (scrollTextLable.isVisible()) {
                    System.out.println("Here is is fastpass in the dobutton " + fPass);
                    Platform.runLater(() -> GoPostSuperApprovalDetails("1112"));
                }
            }
            //LEFT OFF HERE SATURDAY 12TH DOING THE BELOW THING TWICE ONE ON THE DEPOSIT SCREEN AND ONCE IN THE GREATERTHATTENTHOUGHSAND CODE
        } //THIS IS THE END OF THE GETRNUMBER IF GTTTPopup is on (1)

        doButton.setDisable(true);
        doButton.setVisible(false);
        depAmount = Integer.parseInt(addingField.getText().trim());

        System.out.println(" = = = = = WE ARE THIS FAR STAGE 1 " + newBonus + "LOC = " + loc);
       

       
        //HERE IS WHERE WE MARK OUT EVERYTHING WITH THREE /**/
        /*
        if (bonusField.getText().trim().isEmpty()) {
            System.out.println(" = = = = = HERE IS THE FIRST IF (BONUSFILED IS EMPTY) " + newBonus + "LOC = " + loc);
            //doDailyBonus();
            //doWildWed();
            if (!wWedField.isVisible()) {
                //depAmount = Double.valueOf(depositField.getText().trim());
                if (depAmount >= MinDep) {
                    bonusField.setText(String.valueOf(Integer.parseInt(depositField.getText()) / baseBonus)); 
                }
                //bonusField.setText(String.valueOf(db.calcBonus(Integer.parseInt(depositField.getText())))); //USES CONNOTHER
            } else {
                if (newBonus) {
                    //depAmount = Double.valueOf(depositField.getText().trim());
                if (depAmount >= MinDep) {
                    bonusField.setText(String.valueOf(wWedField.getText()));
                }

                    if (TodaysDailyBonus.getvInits().isEmpty() || TodaysDailyBonus.getvName().equals("NONE")) {
                        loc = " ";
                    } else {
                        loc = TodaysDailyBonus.getvInits();
                    }

                } else {
                    //depAmount = Double.parseDouble(depositField.getText().trim()) + Double.parseDouble(wWedField.getText().trim());
                    if (TodaysDailyBonus.getvInits().isEmpty() || TodaysDailyBonus.getvName().equals("NONE")) {
                        loc = " ";
                    } else {
                        loc = TodaysDailyBonus.getvInits();
                    }
                } //newbonus
            }
        } else {
        
            //doWildWed();
            System.out.println(" = = = = = HERE IS THE FIRST IF ELSE (BONUSFIELD NOT EMPTY " + newBonus);
            if (!wWedField.isVisible()) {
                if (depAmount >= MinDep) {
                    bonusField.setText(String.valueOf(Integer.parseInt(depositField.getText()) / baseBonus)); 
                }
                //bonusField.setText(String.valueOf(db.calcBonus(Integer.parseInt(depositField.getText()))));
                //depAmount = Double.parseDouble(depositField.getText().trim());
            } else {
                if (newBonus) {
                    //depAmount = Double.parseDouble(depositField.getText().trim());
                //if (depAmount >= MinDep) {
                //    bonusField.setText(String.valueOf(Integer.parseInt(depositField.getText()) / baseBonus)); 
                //}
                    //bonusField.setText(String.valueOf(wWedField.getText()));
                    if (TodaysDailyBonus.getvInits().isEmpty() || TodaysDailyBonus.getvName().equals("NONE")) {
                        loc = " ";
                    } else {
                        loc = TodaysDailyBonus.getvInits();
                    }

                } else {
                    //depAmount = Double.parseDouble(depositField.getText().trim()) + Double.parseDouble(wWedField.getText().trim());
                    if (TodaysDailyBonus.getvInits().isEmpty() || TodaysDailyBonus.getvName().equals("NONE")) {
                        loc = " ";
                    } else {
                        loc = TodaysDailyBonus.getvInits();
                    }
                } //newbonus
            }
            System.out.println(" = = = = = DONE WITH THE FIRST IF AND ELSE (IS BONUSFILED EMPTY) " + newBonus + " LOC= " + loc);

        }
        */
        //HERE IS WHERE THE MARKING OUT OF EVERYTHING STOPS
        
        
        
        // I THINK THIS IS WHERE WE PUT GETRANDOMWINNER() which increases if not the rWinner RANDOM WINNER IS IN THE PRINT RECEIPTS
        Platform.runLater(() -> rWinner = db.RandomWinner("Deposit")); //USES CONNOTHER
        if (member.getCID().isEmpty()) {
            new Mail_JavaFX1().sendEmailTo("CID in Deposit Controller", "The CID Number in the getMemberINFO in Depsoit controller is empty" + CID + "\n" + dbsp.localMachine, "errors");
        }
            System.out.println(" = = = = = THIS FAR STAGE 2) " + newBonus + "LOC = " + loc);
        //* Member ID	Employee Number     Transaction Number	Date	Time	Added	Bonus	Subtracted  Location
        if (wkDayTickets) {
            depAmount = depAmount + Integer.parseInt(wWedField.getText());
        }
        if (wkDayMultiply) {
            depAmount = depAmount + Integer.parseInt(wWedField.getText());
            
        }
        if (wkDayBonus) {
            bonusField.setText(String.valueOf(wWedField.getText()));
        }
        if (superApprove) {
            if (loc.trim().length() == 0){
                loc = "GTTTAPP"; //I MOVED SUPER APPROVAL TO THE DETAIL. MOVED IT BACK 9/23, move dec 30/23 trying to get this finished 
            }
            memtick = new Memtick(member.getCID(), en, tranTime, LocalDate.now(), Double.valueOf(depAmount), Double.valueOf(bonusField.getText().trim()), 0.0, loc, 0);
        } else {
            memtick = new Memtick(member.getCID(), en, tranTime, LocalDate.now(), Double.valueOf(depAmount), Double.valueOf(bonusField.getText().trim()), 0.0, loc, 0);
        }
        setLastMemberVariables();
        System.out.println(" = = = = = HERE STAGE 3 " + newBonus + "LOC = " + loc);
        prevBalance = ((double) member.getBalance());
        newBalance = prevBalance + memtick.getAdded() + memtick.getBonus();
        //System.out.println("--------------------------------------------------");
        System.out.println(" = = = = = HERE STAGE 4 " + newBonus + "LOC = " + loc);
        
        if (!db.insertDataTicketBalanceCombined(memtick, newBalance)) { //CLOSES CONN
            System.out.println("error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)");
            jmail.sendEmailTo("Error Depositing Tickets", "error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)" + "\n" + dbsp.localMachine, "errors");
        } else {
            runcheckBalance(member, newBalance, memtick, en, fn, ln, rCeipts, isEMail, GtttArray); //RUNCHECKBALANCE HAS ITS OWN MODEL IN MODELS
            Platform.runLater(() -> {
                try {
                    if (!getIsEmailTrue()) {
                        printEscPosDEPOSIT(newBalance, memtick);
                        //printReceipt(newBalance, memtick);
                    } else {
                        printEscPosDEPOSIT(newBalance, memtick); 
                        //printBadEmailReceipt(newBalance, memtick);
                        printBadEmailReceiptPOSESC(newBalance, memtick);
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                } catch (PrintException ex) {
                    Logger.getLogger(MemDepositFXController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MemDepositFXController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        if (voucher) {
            String vListString = "";
            for (int i = 0; i<listToRedeem.size(); i++ ){
                vListString += listToRedeem.get(i).getC6() + " \n";
            }
            new Mail_JavaFX1().sendEmailToThisEmail("Deposit Voucher", String.valueOf(enID) + " " + vListString + " " +  currentTime + " " + voucherDeposit  +  fn + " " + "Deposit " + member.getCID(), "newkirkdean@gmail.com");
            //Platform.runLater(() -> redeemVouchersThread(listToRedeem, String.valueOf(enID), currentTime));
            new redeemVouchers().redeemVouchersNoThread(listToRedeem, String.valueOf(enID), currentTime, fn, vList, dbsp, "Deposit " + member.getCID());
        }

        Platform.runLater(() -> exitButtonPushed());
    }

    private void runcheckBalance(Member m, Double newBalance, Memtick mt, String empN, String fName, String lName, ArrayList<rCeipts> rCeipts, String isEMail, boolean GtttArray) {
        Thread threadRunCheckBalance = new Thread() {
            @Override
            public void run() {
                getReceipts("GreaterThanDepWith", rCeipts);
                String gt = getrNumber();
                try {
                    //ADDING THIS JAN of 24 SO WE CAN DO THIS ACTION LATER ON THE ICONAPP.
                    //PAUSED ON THIS AS THIS MEANS MY LOGIN COMPUTER HAS TO RUN OR THESE ITEMS WILL GET DONE. HAVE TO THINK ABOUT THIS.
                    //new ClubFunctions().GOPRTTxtFile(dbsp.pathNameBalReport + "chkBalance.txt", member.getCID(), "", "", "", "");
                    chkBalance.checkBalance(m, newBalance, mt); //THIS IS TO CHECK THE BALANCE OF THE MEMBER GOT UPDATED.

                    if (!isEMail.equals("BAD")) {
                        String EmailAdd1 = getReceipts("EmailAdd1", rCeipts);
                        if (getrNumber().equals("1.0")) {

                        } else {
                            EmailAdd1 = "";
                        }
                        if (getIsEmailTrue()) {
                            chkBalance.sendMemberEmail(m, mt, newBalance, prevBalance, fn, EmailAdd1, "D", null, null, isEMail, rCeipts);
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                    jmail.sendEmailTo("Error Balance Check", "Error from Deposit: " + ex + " " + new dbStringPath().localMachine, "errors");
                }
                try {
                    if (GtttArray) { //THIS IS WHERE WE SEND AN EMAIL OF GREATER THAT TEN THOUSAND TO PEOPLE ON THE EMAIL LIST FOR THIS ITEM.
                        chkBalance.RunTenThousand(m, newBalance, mt, empN, fName, lName, "DEPOSIT", gt, E);
                    }
                } catch (SQLException | FileNotFoundException ex) {
                    jmail.sendEmailTo("Error in GTTT Deposit Process", ex.toString() + ex + " " + empN + " " + fName + " " + lName + " " + new dbStringPath().localMachine, "error");
                }

            }
        };
        threadRunCheckBalance.start();
    }

    
    private void redeemVouchersThread(ArrayList<FXVoucherViewer> listToRedeem, String enID, String currentTime) {
        Thread redeemVouchersThread = new Thread() {
            @Override
            public void run() {
                new redeemVouchers().redeemVouchersNoThread(listToRedeem, enID, currentTime, fn, vList, dbsp, "Deposit " + member.getCID());
            }
        };
        redeemVouchersThread.start();
    }
    
    
    
    ///LASTMEMBER---------------------------------------------------------------
    ///
    
    public void CCNClicked(MouseEvent me) {
        me.consume();
        if (me.getClickCount() == 2) {
            getLastTransactionMemberVIP();
            return;
        }
        MouseButton mb = me.getButton();
        if (mb == MouseButton.SECONDARY) {
            me.consume();
           fullListLMTButton.fire();
        }
    }

    private void setLastMemberVariables() {
        sc.setCCN(member.getCCN());
        sc.setEmployee(en);
        sc.setEmpName(fn);
        sc.setActivity("Deposit");
        SCLMT = true;
        sc.setLastName(member.getNameL());

    }

    public void getLastTransactionMemberVIP() {
        String empNumb, lastCCN;
        try {
            empNumb = lastmember.get(iLastTran).getlastEmpNumber();
            lastCCN = lastmember.get(iLastTran).getLastMember();
            sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
            boolean GO = isEMPValidInArrayList(sc.getGameNumber());
            if (en.equals(empNumb)) {
                //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Approved!", "Here is the last member number " + lastCCN);
                CCNumberIn.setText(lastCCN);
                Platform.runLater(() -> enterKeyPressed());
            }
        } //counter screens
        catch (IOException ex) {
            System.out.println(ex);
        }
        empNumb = "";
        lastCCN = "";
    }

    public void getFullListLastTransactionMemberVIP(ActionEvent ae) {
        try {
            sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
            boolean GO = isSuperValidInArrayList(sc.getGameNumber());
            System.out.println("SUPER is " + Super);
            if (Super != 3) {
                return;
            }
            lastMemberTransactionViewController wController = (lastMemberTransactionViewController) FXLOADER.getController();
            wController.sc = sc;
            wController.dbsp = dbsp;
            wController.cssPath = cssPath;
            System.out.println("here is the size of lastmember " + lastmember.size());
            wController.lastMember = lastmember;
            //wController.rs = 
            sc.changePopUp(ae, "/views/counterPopUp/lastMemberTransactionView.fxml", "List of Activity");
            //sc.getpassWord(stageV, "/views/counterPopUp/lastMemberTransactionView.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
            //CCNumberIn.setText(sc.getCCN());
            LastMemberTransaction LMT = sc.GetLastMemberTransaction();
            if (!"1".equals(LMT.getLastActivity())) {
                CCNumberIn.setText(LMT.getLastMember());
                Platform.runLater(() -> enterKeyPressed());
            }
        } //counter screens
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    
    
    
    ///END LASTMEMBER ----------------------------------------------------------
    
    

    private LastTranApproval GetLastTransactionDBThread(String cID) {
        Thread GetLastApprovalThread = new Thread() {
            @Override
            public void run() {
                //getLastDBTransaction = new DB().GetLastTransactionDB(cID);
                lta = db.GetLastTransactionDBModel(cID); //IS NOT CLOSEING CONN
            }
        };
        GetLastApprovalThread.start();
        return lta;
    }



    
    public static boolean GetLastApprovalThread(String cID) {
        Thread GetLastApprovalThread = new Thread() {
            @Override
            public void run() {
                lastApproval = db.GetLastApprovalDB(cID);
            }
        };
        GetLastApprovalThread.start();
        return false;
    }

    
    public void SetGTTT() {
        getReceipts("GreaterThanDepWith", rCeipts);
        gt = getrNumber();
    }
    
    
    //PRINT RECEIPT FOR NO EMAIL OR WITH VALID EMAIL
    private void printReceipt(Double n, Memtick mt) throws FileNotFoundException {

        pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "DepReceipt.txt"));
        pw.println(getReceipts("CoName", rCeipts)); // to test if it works.
        String printthis = getReceipts("SubCoHeading", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Address", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Address2", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Phone", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("WWW", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        pw.println("");
        pw.println("Club Deposit Receipt"); // to test if it works.
        pw.println("======================"); // to test if it works.
        pw.println("");
        pw.println("");
        pw.println(customerNameLabel.getText());
        pw.println(" ");
        pw.println("Employee: " + fn);
        pw.println("");
        pw.println("");
        pw.println("Prev Balance" + ":" + "\t" + DECIMALFORMAT.format(prevBalance));
        pw.println("Tickets Added" + ":" + "\t" + DECIMALFORMAT.format(mt.getAdded()));
        pw.println("BonusTickets" + ":" + "\t" + DECIMALFORMAT.format(mt.getBonus()));
        pw.println("New Balance" + ":" + "\t" + DECIMALFORMAT.format(n));
        pw.println(" ");
        pw.println(" ");
        

        printthis = getReceipts("Footer1", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer2", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer3", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer4", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer5", rCeipts);
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        if (getIsEmailTrue()) {
            pw.println(" ");
            pw.println(" ");
            pw.println("You have an Email and we have");
            pw.println("sent a receipt to this Email Address");
            pw.println(isEMail.toUpperCase());
            pw.println(" ");
        }
        pw.println(" ");
        pw.println(rWinner);
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.close();

        print pr = new print();
        Platform.runLater(()->pr.printReceipt("DepReceipt.txt"));
        //LastTransactionTXT(mt, n);
    }

    
    
  private void printEscPosDEPOSIT(Double n, Memtick mt)  throws PrintException, IOException{
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
            githubBufferedImage = ImageIO.read(new File(dbsp.pathNameImages + "/ReceiptLogo/depositLogo.png"));
            EscPosImage escposImage = new EscPosImage(githubBufferedImage, new BitonalThreshold());         
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            escpos.write(imageWrapper, escposImage);
        } catch (IOException ex) {
            //jmail.sendEmailTo("NO RECEIPT LOGO FOUND","There is no logo found in the L://Images//ReceiptLogo/voucherLogo.png Directory", "errors");         
            System.out.println("There is no logo found in the L://Images//ReceiptLogo/withdrawalLogo.png Directory " + ex);
        }
        
        
        //===================================================HERE ARE THE HEADERS ==================================================

        //PRINT THE COMPANY NAME
        String printthis = getR.getReceipts(rCeipts, "CoName");
        String getReceiptNumber = getR.getReceiptsNumber(rCeipts, "CoName");
        char c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
           //JOptionPane.showMessageDialog(null, "we are here and the thing is " + printthis + " ----" + getReceiptNumber + " === " + c);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }



         
        //PRINT THE SUBCOHEADING
        printthis = getR.getReceipts(rCeipts, "SubCoHeading");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "SubCoHeading");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }

        
        //PRINT THE ADDRESS
        printthis = getR.getReceipts(rCeipts, "Address");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Address");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (c == 1) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }


        
        //PRINT THE ADDRESS2
        printthis = getR.getReceipts(rCeipts, "Address2");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Address2");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }
         
        //PRINT THE PHONE
        printthis = getR.getReceipts(rCeipts, "Phone");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Phone");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }
        

        //PRINT THE WWW
        printthis = getR.getReceipts(rCeipts, "WWW");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "WWW");
        c = getReceiptNumber.charAt(THISPAGECHAR);
        //JOptionPane.showMessageDialog(null, "we are here and the thing is " + printthis + " ----" + getReceiptNumber + " === " + c + " 88888 " + THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }
        
        
        
        
        
        //=========================================== END OF HEADERS ==================================
        
        
        
        
        

        //PRINT RECEIPT HEADING (WITHDRAWAL)
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "CLUB DEPOSIT");
        escpos.feed(2);

        
        
        //PRINT MEMBER NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, customerNameLabel.getText());
        escpos.feed(1);        
        
        
        //PRINT EMPLOYEE NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Employee: " + fn);
        escpos.feed(1);        

        //PRINT THE DATE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Date: " + MYDATEFORMATTERR.format(LocalDateTime.now()));
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
        escpos.write(Format, "Prev Balance" + ":" + "\t" + DECIMALFORMAT.format(prevBalance));
        escpos.feed(1);        
        

        //PRINT TICKETS ADDED
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Tickets Added" + ":" + "\t" + DECIMALFORMAT.format(mt.getAdded()));
        escpos.feed(1);        

        //PRINT BONUS
        if (mt.getBonus()>0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Bonus Tickets" + ":" + "\t" + DECIMALFORMAT.format(mt.getBonus()));
        escpos.feed(1);        
        }
        
        
        //PRINT NEW BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "New Balance" + ":" + "\t" + DECIMALFORMAT.format(n));
        

        
      
     
      
      

        escpos.feed(2);        
        
        

        //  =============================================HERE ARE THE FOOTERS ===================================
        //PRINT THE FOOTER1
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(rCeipts, "Footer1");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Footer1");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }

        
        //PRINT THE FOOTER2
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(rCeipts, "Footer2");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Footer2");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }
        
        //PRINT THE FOOTER3
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(rCeipts, "Footer3");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Footer3");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }
        
        //PRINT THE FOOTER4
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(rCeipts, "Footer4");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Footer4");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }

        //PRINT THE FOOTER5
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                 .setFontSize(Style.FontSize._1, Style.FontSize._1)
                 .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                 .setBold(true)
                 .setJustification(EscPosConst.Justification.Center);
        printthis = getR.getReceipts(rCeipts, "Footer5");
        getReceiptNumber = getR.getReceiptsNumber(rCeipts, "Footer5");
        if (getReceiptNumber.equals("1.0") || Double.parseDouble(getReceiptNumber) >= Double.parseDouble(RECEIPTS_DATE)) {
            escpos.write(Format, printthis);
            escpos.feed(1);
            printthis = "";
            getReceiptNumber = "";
        } else {
            printthis = "";
            getReceiptNumber = "";
        }

        // feed 5 lines
        outputStream.write(27); // ESC
        outputStream.write('d');
        outputStream.write(5);

        // cut
        outputStream.write(29); // GS
        outputStream.write('V');
        outputStream.write(48);

        
        
        // ================================================END OF FOOTERS =============================================
        
         //PRINT RANDOM WINNER
      if (rWinner.length() > 3) {
          escpos.feed(1);
          Format = new Style()
                  .setFontName(Style.FontName.Font_A_Default)
                  .setFontSize(Style.FontSize._1, Style.FontSize._1)
                  .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                  .setJustification(EscPosConst.Justification.Left_Default);
          escpos.write(Format, rWinner);
          escpos.feed(1);
          // feed 5 lines
          outputStream.write(27); // ESC
          outputStream.write('d');
          outputStream.write(5);

          // cut
          outputStream.write(29); // GS
          outputStream.write('V');
          outputStream.write(48);
      }

      
       
        


        // do not forguet to close outputstream
        outputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());


        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc doc = new SimpleDoc(inputStream, flavor, null);
        dpj.print(doc, null);

       
    }    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void PrintBadEmailThread(Double n, Memtick mt) {
        Thread PrtBadEmailReceipt = new Thread() {
            @Override
            public void run() {
                try {
                
                    printBadEmailReceipt(n, mt);
                
                }catch(FileNotFoundException ex) {
                        
                }
            }
        };
        PrtBadEmailReceipt.start();
    }

    
    
    private void printBadEmailReceiptPOSESC(Double n, Memtick mt) throws IOException, PrintException {
         PrintService foundService = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob dpj = foundService.createPrintJob();
        
        System.out.println("This si the Default Printer " + foundService.getName());
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        outputStream.write(27); // ESC
        outputStream.write('@');
     
         
        
        //PRINT LOGO IF AVAILIBLE
        EscPos escpos = new EscPos(outputStream);

  
        
        
        //===================================================HERE ARE THE HEADERS ==================================================

        //PRINT THE COMPANY NAME
        String printthis = getR.getReceipts(rCeipts, "CoName");
        String getReceiptNumber = getR.getReceiptsNumber(rCeipts, "CoName");
        char c = getReceiptNumber.charAt(THISPAGECHAR);
        if (Character.compare(c, '1') == 0) {
           //JOptionPane.showMessageDialog(null, "we are here and the thing is " + printthis + " ----" + getReceiptNumber + " === " + c);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setBold(true)
                    .setJustification(EscPosConst.Justification.Center);
            escpos.write(Format, printthis);
            escpos.feed(1);
            getReceiptNumber = "";
            printthis = "";
        } else {
            getReceiptNumber = "";
            printthis = "";
        }
        
           //PRINT MEMBER NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, customerNameLabel.getText());
        escpos.feed(1);        
        
        
        //PRINT EMPLOYEE NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Employee: " + fn);
        escpos.feed(1);        

        //PRINT THE DATE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Date: " + MYDATEFORMATTERR.format(LocalDateTime.now()));
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
        escpos.write(Format, "Prev Balance" + ":" + "\t" + DECIMALFORMAT.format(prevBalance));
        escpos.feed(1);        
        

        //PRINT TICKETS ADDED
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Tickets Added" + ":" + "\t" + DECIMALFORMAT.format(mt.getAdded()));
        escpos.feed(1);        

        //PRINT BONUS
        if (mt.getBonus()>0) {
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Bonus Tickets" + ":" + "\t" + DECIMALFORMAT.format(mt.getBonus()));
        escpos.feed(1);        
        }
        
        
        //PRINT NEW BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "New Balance" + ":" + "\t" + DECIMALFORMAT.format(n));
        
        escpos.feed(2);        

          
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "- - UNDELIVERABLE EMAIL - -");
        escpos.feed(2);     
            escpos.write(Format, isEMailUpdate.toUpperCase());
        escpos.feed(2);     
            escpos.write(Format, "___________________________________");
        escpos.feed(1);     
            escpos.write(Format, "Print Email Address on line Above");
        escpos.feed(2);     
            escpos.write(Format, "|_| Please Update my Email Address");
        escpos.feed(2);     
            escpos.write(Format, "|_| Please Delete my Email Address");
        escpos.feed(2);     
        
        
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
    
    
    
    
    //PRINT ONLY BAD EMAILS
    private void printBadEmailReceipt(Double n, Memtick mt) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "DepReceipt.txt"));
        pw.println(getReceipts("CoName", rCeipts)); // to test if it works.
        String printthis = getReceipts("SubCoHeading", rCeipts);
        pw.println(member.getNameF());
        pw.println(" ");
        pw.println("Employee: " + fn);
        pw.println("");
        pw.println("Prev Balance" + ":" + "\t" + DECIMALFORMAT.format(prevBalance));
        pw.println("Tickets Added" + ":" + "\t" + DECIMALFORMAT.format(mt.getAdded()));
        pw.println("BonusTickets" + ":" + "\t" + DECIMALFORMAT.format(mt.getBonus()));
        pw.println("New Balance" + ":" + "\t" + DECIMALFORMAT.format(n));
        pw.println(" ");
        pw.println(" ");
        if (isEMail.equals("BAD")) {
            pw.println(" ");
            pw.println(member.getNameF() + " " + member.getNameL());
            pw.println(" ");
            pw.println("- - UNDELIVERABLE EMAIL - -");
            pw.println(isEMailUpdate.toUpperCase());
            pw.println("");
            pw.println("___________________________________");
            pw.println("Print Email Address on line Above");
            pw.println(" ");
            pw.println("|_| Please Update my Email Address");
            pw.println("");
            pw.println("|_| Please Delete my Email Address");
            pw.println(" ");
            pw.println(" ");
        }
        //if (getIsEmailTrue()) {
        //    pw.println(" 4444444444");
        //    pw.println(isEMail.toUpperCase());
        //    pw.println(" ");
        //}
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.close();

        print pr = new print();
        Platform.runLater(()->pr.printReceipt("DepReceipt.txt"));
        //LastTransactionTXT(mt, n);
    }

    
    
    
    private void SetEmail() {
        try {
            if (!isEMail.equals("BAD") && isEMail.trim().length() > 3) {
               // System.out.println("going through set email 1" + isEMail);
                scrollTextLable.setVisible(true);
                String printthis = getReceipts("ClubPaperReceipt", rCeipts);
                if (getrNumber().equals("1.0")) {
                    scrollTextLable.setText("Email and Paper Receipt");
                    setEmailTrue(true);
                    this.setrNumber("");
                } else {
                    scrollTextLable.setText("Email Receipt Only");
                    setEmailTrue(true);
                    this.setrNumber("");
                }
            } else {
                
                if (isEMail.equals("non")) {
                    setEmailTrue(false);
                } else {
                    Platform.runLater(()->isEMailUpdate = db.getEmailForUpdate(member.getCID()));
                    setEmailTrue(true);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            scrollTextLable.setVisible(false);
        }

    }
    
    private void setEmailTrue(boolean t) {
        this.emailIsTrue = t;
    }

    private boolean getIsEmailTrue() {
        return emailIsTrue;
    }

    public void enterKeyPressed() {
        int g = 0;
        if (CCNumberIn.isFocused()) {
            getMemberInfo();
            if (!voucher){
                return;                
            } else {
                depositField.requestFocus();
                enterKeyPressed();
            }
        }
        if (depositField.isFocused()) {
            if (depositField.getText().trim().length() > 9) {
                VoucherFunctions VF = new VoucherFunctions();                    //THIS IS TICKET EATER
                returnEaterCode rec = VF.isTicketEaterCodeValid(tec, depositField.getText().trim());
                if (rec.getValid()) {
                    multipleVouchers = true;
                    addingField.setVisible(true);
                    addingLabel.setVisible(true);

                    if (!addingField.getText().trim().isEmpty()) {
                        g = Integer.parseInt(addingField.getText().trim()) + rec.getEaterCodeValue();
                    } else {
                        g = rec.getEaterCodeValue();
                    }

                    addingField.setText(String.valueOf(g));
                    //String dt = depositField.getText().trim();
                    //runRedeemVoucherThread(dt);
                    depositField.setText("");
                    depositField.requestFocus();
                    return;
                } else {
                    depositField.setText("");
                    depositField.requestFocus();
                    mBox.showAlert(Alert.AlertType.ERROR, null, "WAIT!", "Not a valid scan, please scan again.");
                    return;
                }

            }
            if (!depositField.getText().isEmpty()) {
                if (addingField.getText().isEmpty()) {
                    g = Integer.parseInt(depositField.getText().trim());
                } else {
                    g = Integer.parseInt(addingField.getText().trim()) + Integer.parseInt(depositField.getText().trim());
                }
                addingField.setText(String.valueOf(g));
                depositField.setText("");
                depositField.requestFocus();
                return;
            }
            
            
            //if (multipleVouchers){
                depAmount = Integer.parseInt(addingField.getText().trim());
            //} else {
            //    depAmount = Integer.parseInt(depositField.getText().trim());
            //}
            lastApproval = lta.getLastApproval();
            getLastDBTransaction = lta.getlastTran();
            if (!customerInfoBox.isVisible()) {
                getMemberInfo();
            }
            if (depositField.getText().equals(CCNumberIn.getText())) {
                depositField.clear();
                depositField.requestFocus();
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP", "Customer Account Number and Deposit Amount cannot be the same number");
                return;
            }
            if (depAmount > 4999) {
                if (isToyValidInArrayList(String.valueOf(depAmount))) {
                    int alert = new messageBox().confirmMakeThisChangeButtonTitles(Alert.AlertType.ERROR, owner, "CONFIRM!", "You have entered a \n PRIZE NUMBER ( " + depositField.getText() + " ) \n as a Deposit amount. \n Please Confirm this is the correct Deposit Amount.", "Cancel", "Continue");
                    System.out.println(alert);
                    if (alert == 1) {
                        depositField.clear();
                        depositField.requestFocus();
                        return;
                    }
                }
            }
            getReceipts("GreaterThanDepWith", rCeipts);
            //TestLastTranArray();
            if (TestLastTranDB()) {
                //fPass = true;
                GtttArray = true;
                dLabel.setText("Greater Than 10,000 Tickets");
                dLabel.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 0, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
                scrollTextLable.setText("Greater Than 10,000 Tickets");
                new PlayText().playText(scrollTextLable);
                scrollTextLable.setVisible(true);
            }
            //HERE IS WHERE WE ARE WORKING WITH DAILY BONUS.
            if (depAmount > 50000) {
                depositField.setText("");
                bonusField.setText("0");
                depositField.requestFocus();
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP", "Invalid Deposit Amount: " + depositField.getText());
                return;
            }
            doDailyBonus();
            //doWildWed(); //THIS WOULD BE CHANGED TO TESTfORmULTIPLIER() THEN TESTfORtICKETS() ETC. 
            if (wWedField.isVisible()) {
                empNumberTextfield.requestFocus();
                return;

            } else {
                // HERE IS BACK TO THE ORIGINAL CODE
                System.out.println(" = = = = = HERE IN ENTERKEYPRESSED, THIS SHOULD BE THE BONUS CALULATING");
                try {
                    if (depAmount >= MinDep) {
                    System.out.println(" = = = = = HERE IN ENTERKEYPRESSED, CALCING BONUS " );
                        bonusField.setText(String.valueOf(depAmount * baseBonus / 100));
                    } else {
                        bonusField.setText("0");                        
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" ERROR " + e );
                }
                empNumberTextfield.requestFocus();
                //TestLastTranArray();
                return;
            }
        }


        if (empNumberTextfield.isFocused()) {
            //if (multipleVouchers){
                depAmount = Integer.parseInt(addingField.getText().trim());
            //} else {
            //    depAmount = Integer.parseInt(depositField.getText().trim());
            //}
            SetEmail();
            if (!customerInfoBox.isVisible()) {
                getMemberInfo();
            }
            //if (!multipleVouchers){
            //    if (depositField.getText().trim().equals("")) {
            //        depositField.requestFocus();
            //    }
            //}
            /*else {
                if (newBonus) {
                    bonusField.setText(String.valueOf(wWedField.getText()));
                } else {
                if (depAmount >= MinDep) {
                    bonusField.setText(String.valueOf(Integer.parseInt(depositField.getText()) / baseBonus)); 
                }
                    //bonusField.setText(String.valueOf(db.calcBonus(Integer.parseInt(depositField.getText()))));
                }
            }*/
            if (!confirm) {
                if (empNumberTextfield.getText().equals(depositField.getText())) {
                    depositField.clear();
                    depositField.requestFocus();
                    new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP", "It look like the Employee Number and the Deposit amount are the same number. \n\n please enter the Deposit amount again to confirm it is the correct amount");
                    confirm = true;
                    return;
                }
            }
            try {
                if (!loginButtonPushed()) {
                    empNumberTextfield.clear();
                    empNumberTextfield.requestFocus();
                    new messageBox().showAlert(Alert.AlertType.ERROR, null, "No Number", "No Employee Number Found");
                    return;
                } else {
                    empNumberTextfield.setDisable(true);
                    doButton.requestFocus();
                    return;
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        if (doButton.isFocused()) {
            doButton.fire();
        }

    }


    
    
    
    
    
//------------------------------------------------------------------------------------------    
    

    private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        for (int y = 0; y < E.size(); y++) {
            if (n.trim().equals(E.get(y).getEmpNumber())) {
                empValid = true;
                en = E.get(y).getEmpNumber();
                fn = E.get(y).getNameF();
                superFN = E.get(y).getNameF();
                ln = E.get(y).getNameL();
                enGprob = E.get(y).getGProb();
                enArcade = E.get(y).getArcade();
                enID = E.get(y).getEmpID();
            }
        }
        return empValid;
    }


    private boolean isSuperValidInArrayList(String n) {
        boolean superValid = false;
        for (int y = 0; y < E.size(); y++) {
            if (n.equals(E.get(y).getEmpNumber())) {
                superValid = true;
                superEN = E.get(y).getEmpNumber();
                superFN = E.get(y).getNameF();
                superLN = E.get(y).getNameL();
                Super = E.get(y).getArcade();
                superID = E.get(y).getEmpID();
            }
        }
        //System.out.println(superEN + " " + superFN + " " + superLN + " " + Super + " " + superID);
        return superValid;
    }


    EventHandler<MouseEvent> mouseHandler = (MouseEvent mouseEvent) -> {
        enterKeyPressed();
    };


    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        GO = isEMPValidInArrayList(empNumberTextfield.getText().trim());
        return GO;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void doDailyBonus() { //THIS IS WHERE THE NEW CODE IS SO THAT ALL DAILY BONUS'S WORK 11/24
        //WE MIGHT DO A BOOLEAN HERE IFDODAILYBONUS() THEN DO NOT GO THROUGH ANY OTHER BACK UP TOP 
        System.out.println("===============We are in the (NEW CODE)");
        boolean isDailyBonusToday = false;
        int depositFieldValue = 0;
        try {
            depositFieldValue = depAmount;
        } catch (NumberFormatException e) {
        }
        isDailyBonusToday = getTodaysDailyBonus(new GetDay().LongDayWeek(new GetDay().getday()).toUpperCase()); // NEW WAY
        if (isDailyBonusToday) { //IF THERE IS A DAY THAT MATCHES TODAY THEN GO INTO THE NEXT PART
            if (new GetDay().getDateLocalDateHour() >= TodaysDailyBonus.getvSTime() && new GetDay().getDateLocalDateHour() < TodaysDailyBonus.getvETime()) {
                System.out.println("NEW CODE = = Current Hour =  " + new GetDay().getDateLocalDateHour() + " and the Hour for Start Time " + TodaysDailyBonus.getvSTime() + " End Time " + TodaysDailyBonus.getvETime());


                if (TodaysDailyBonus.getvMultiply() > 0.0) {
                    if (getRepeatSoFar() < TodaysDailyBonus.getvRepeat()) {
                        System.out.println("thisis whxere we go trough for DailyBonus 1 (Multiply) NEW CODE");
                        fWildWed fww = TodaysDailyBonus.calcWildWed2(depositFieldValue, member);
                        if (Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus())) > 0) {
                            wWedField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus()))));
                            wWedLabel.setVisible(true);
                            wWedLabel.setText(TodaysDailyBonus.getvName());
                            wWedField.setVisible(true);
                            int newdepamount = depositFieldValue + Integer.parseInt(wWedField.getText());
                            if (depAmount >= MinDep) {
                                bonusField.setText(String.valueOf(depAmount * baseBonus / 100));
                            } else {
                                bonusField.setText("0");
                            }
                            loc = TodaysDailyBonus.getvInits();
                            wkDayMultiply = true;
                        }
                        System.out.println("================today is MULTIPLY at the end(NEW CODE) " + fww.getWildWedBonus() + " " + fww.getNewBalance());
                    } else {
                        System.out.println("================today is MULTIPLY but the repeat has been reached (NEW CODE)");
                    }
                } else {
                    System.out.println("================today is MULTIPLY at the end(NEW CODE) ");
                }

                
                
                
                
                
                if (TodaysDailyBonus.getvTickets() > 0.0) {
                    if (depositFieldValue >= TodaysDailyBonus.getvMax()) { //THIS IS THE MINIMUM DEPOSIT EVEN THOUGH IT SAYS GETMAX()
                        if (getRepeatSoFar() < TodaysDailyBonus.getvRepeat()) {
                            System.out.println("================today is Tickets (NEW CODE)");
                            fWildWed fww = TodaysDailyBonus.CalcwDayTickets(depositFieldValue, member);
                            System.out.println("thisis where we go trough for DailyBonus 2 (Tickets) NEW CODE");
                            wWedField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(TodaysDailyBonus.getvTickets()))));
                            if (depAmount >= MinDep) {
                                bonusField.setText(String.valueOf(depAmount * baseBonus / 100));
                            } else {
                                bonusField.setText("0");
                            }
                            fww = null;
                            wWedLabel.setVisible(true);
                            wWedLabel.setText(TodaysDailyBonus.getvName());
                            wWedField.setVisible(true);
                            loc = TodaysDailyBonus.getvInits();
                            wkDayTickets = true;
                        } else {
                            System.out.println("================today is Tickets but the repeat has been reached (NEW CODE)");
                        }
                    } else {
                        System.out.println("================today is Tickets but the min depsoit has not been met (NEW CODE)");
                    }
                }
                
                
                /*
                YOU NEED TO BREAK UP OTHER SO IT HAS 0 ININATE AND PER REPEATS
                */

                if (TodaysDailyBonus.getvOther() > 0.0) {
                    if (depositFieldValue >= TodaysDailyBonus.getvMax()) {
                        if (getRepeatSoFar() < TodaysDailyBonus.getvRepeat()) {
                            System.out.println("thisis whxere we go trough for DailyBonus 1 (Multiply) NEW CODE");
                            fWildWed fww = TodaysDailyBonus.CalcwDayOther(depositFieldValue, member);
                            System.out.println("thisis where we go trough for DailyBonus 3 (Other INFINITE) NEW CODE " + fww.getNewBalance());
                            wWedField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus()))));
                            bonusField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(0))));
                            fww = null;
                            wWedLabel.setVisible(true);
                            wWedLabel.setText(TodaysDailyBonus.getvName());
                            wWedField.setVisible(true);
                            this.newBonus = true;
                            loc = TodaysDailyBonus.getvInits();
                            wkDayBonus = true;
                        } else { //END OF REPEATS
                            System.out.println("================today is other but the repeat has been reached (NEW CODE)");
                        }
                    } else { //END OF GETMIN/MAX DEPSOIT AMOUNT
                        System.out.println("================today is other but the min depsoit has not been met (NEW CODE)");
                    }
                } //END OF GETOTHER()

            
            
            
            
            } else {
                System.out.println(" (NEW CODE)================today THE HOURS HAVE NOT BEEN REACHED Start Time: " + TodaysDailyBonus.getvSTime() + " End Time: " + TodaysDailyBonus.getvETime());
            }
        }
    }

    
    
    private int getRepeatSoFar() {
        int soFarRepeat = 0; 
        try {
            soFarRepeat = db.getDailyBonusRepeat(member.getCID(), TodaysDailyBonus.getvInits());
            //System.out.println(" (NEW CODE) Sofarrepeat in the new code " + soFarRepeat + " TodaysDailyBonus.repeat " + TodaysDailyBonus.getvRepeat());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return soFarRepeat;
    }
    
    
    private Boolean getTodaysDailyBonus(String n) {
        //System.out.println(n);
        boolean StringItem = false;
        for (int y = 0; y < VIPPromosDailyBonus.size(); y++) {
            if (n.trim().equals(VIPPromosDailyBonus.get(y).getvName())) {
                //System.out.println("We are in the arraylist 2 " + " Label Name: " + VIPPromosDailyBonus.get(y).getvGroupName() + " Day Name: " + VIPPromosDailyBonus.get(y).getvName() + " Initials: " + VIPPromosDailyBonus.get(y).getvInits() + " Tickets: " + VIPPromosDailyBonus.get(y).getvTickets() + " Multiply: " + VIPPromosDailyBonus.get(y).getvMultiply() + " Other: " + VIPPromosDailyBonus.get(y).getvOther() + " Max: " + VIPPromosDailyBonus.get(y).getvMax() + " Repeat: " + VIPPromosDailyBonus.get(y).getvRepeat() + " Start Time: " + VIPPromosDailyBonus.get(y).getvSTime() + " End Time: " + VIPPromosDailyBonus.get(y).getvETime());
                //                                                        String GroupName,                         String vdayname,                 String vInits,                     Double vTickets,                              Double vMultiply,                Double vOther,                                Double vMax,                                 int vrepeat                       start time                              end time
                TodaysDailyBonus = new DailyBonus(VIPPromosDailyBonus.get(y).getvGroupName(), VIPPromosDailyBonus.get(y).getvName(), VIPPromosDailyBonus.get(y).getvInits(), VIPPromosDailyBonus.get(y).getvTickets(), VIPPromosDailyBonus.get(y).getvMultiply(), VIPPromosDailyBonus.get(y).getvOther(), VIPPromosDailyBonus.get(y).getvMax(), VIPPromosDailyBonus.get(y).getvRepeat(), VIPPromosDailyBonus.get(y).getvSTime(), VIPPromosDailyBonus.get(y).getvETime());
                StringItem = true;
            }
        }
        return StringItem;
    }
    
    
    

    private void doWildWed() { //OLD WAY
        int dFieldValue = 0;
        try {
            dFieldValue = Integer.parseInt(depositField.getText().trim());
        } catch (NumberFormatException e) {
        }
        int soFarRepeat = 0;
        //boolean isDailyBonusToday = getTodaysDailyBonus(new GetDay().NOW_LOCAL_DATE().getDayOfWeek().toString()); 
        if (getTodaysDailyBonus(new GetDay().NOW_LOCAL_DATE().getDayOfWeek().toString())) {
            //System.out.println("Daily Bonus is " + isDailyBonusToday);
            System.out.println("(OLD CODE) Label Name: " + TodaysDailyBonus.getvGroupName() + " Day Name: " + TodaysDailyBonus.getvName() + " other: " + TodaysDailyBonus.getvOther() + " State Time: " + TodaysDailyBonus.getvSTime() + " End Time: " + TodaysDailyBonus.getvETime());
            if (new GetDay().getDateLocalDateHour() >= TodaysDailyBonus.getvSTime() && new GetDay().getDateLocalDateHour() < TodaysDailyBonus.getvETime() ) {
                //System.out.println("Current Hour =  " + new GetDay().getDateLocalDateHour() + " and the Hour for Start Time " + TodaysDailyBonus.getvSTime() + " End Time " + TodaysDailyBonus.getvETime());
            }
        }
        
        //==============================================================
        //NEXT WE FIND OUT WHICH ONE IT IS, TICKETS, MULTIPLY OR OTHER
        //MULTIPLY (WILDWED) DOES NOT NEED MIN IT ONLY NEEDS MAX AND DOES NOT HAVE A MINIMUM DEPSOIT AMOUNT
        //==============================================================
        
        
        TodaysDailyBonus = db.LookForDailyBonus(); //THIS IS DONE
        if (!TodaysDailyBonus.getvName().equals("NONE")) { // THERE IS A DAILYBONUS FROM THE DAILY TABLE IN OTHER DBASE, GO CALCULATE IT   
            //TodaysDailyBonus IS DAILYBONUS
            
            //WILDWED OR MULTIPLY
            //fWildWed fww = TodaysDailyBonus.calcWildWed2(dFieldValue, member);
            //if (TodaysDailyBonus.getvMultiply() > 0.0) {
            //if (fww.getWildWedBonus() > 0.0) {
            //    System.out.println("thisis where we go trough for DailyBonus 1 (Multiply) OLD CODE");
            //    wWedField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus()))));
            //    bonusField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getNewBalance()))));
            //    fww = null;
            //    wWedLabel.setVisible(true);
            //    wWedLabel.setText(TodaysDailyBonus.getvGroupName());
            //    wWedField.setVisible(true);
           // }
            
            
            /*try {
                soFarRepeat = db.getDailyBonusRepeat(member.getCID(), TodaysDailyBonus.getvInits());
                System.out.println("Sofarrepeat " + soFarRepeat + " TodaysDailyBonus.repeat" + TodaysDailyBonus.getvRepeat() + " OLD CODE");
            } catch (SQLException ex) {
                System.out.println(ex);
            }*/
            
            /*
            THIS ONE IS TICKETS, IT HAS A MIN DEPOSIT THAT CAN BE SET, 
            */
            /*
            if (dFieldValue >= TodaysDailyBonus.getvMax()) {
                if (soFarRepeat < TodaysDailyBonus.getvRepeat()) {
                    fww = TodaysDailyBonus.CalcwDayTickets(dFieldValue, member);
                    if (TodaysDailyBonus.getvTickets() > 0.0) {
                        System.out.println("thisis where we go trough for DailyBonus 2 (Tickets) OLD CODE");
                        wWedField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(TodaysDailyBonus.getvTickets()))));
                        bonusField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getNewBalance()))));
                        fww = null;
                        wWedLabel.setVisible(true);
                        wWedLabel.setText(TodaysDailyBonus.getvGroupName());
                        wWedField.setVisible(true);
                    }
                }
            }
            */
            
            
            
            /*
            THIS ONE IS PERCENT OR (OTHER) IT HAS A MIN DEPOSIT AMOUNT AND CAN BE SET
            IT COUNTS THE REPEATS UNLESS SET TO 0 (ZERO) WHICH IS INFINITE
            */

            /*if (dFieldValue >= TodaysDailyBonus.getvMax()) {
                if (TodaysDailyBonus.getvRepeat() == 0) { //MEANS INFINITE
                    fww = TodaysDailyBonus.CalcwDayOther(dFieldValue, member);
                    if (TodaysDailyBonus.getvOther() > 0.0) {
                        System.out.println("thisis where we go trough for DailyBonus 3 (Other INFINITE) OLD CODE");
                        wWedField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus()))));
                        bonusField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(0))));
                        fww = null;
                        wWedLabel.setVisible(true);
                        wWedLabel.setText(TodaysDailyBonus.getvGroupName());
                        wWedField.setVisible(true);
                        this.newBonus = true;
                    }
                    
                } else {
                if (soFarRepeat < TodaysDailyBonus.getvRepeat()) {
                    fww = TodaysDailyBonus.CalcwDayOther(dFieldValue, member);
                    if (TodaysDailyBonus.getvOther() > 0.0) {
                        System.out.println("thisis where we go trough for DailyBonus 3 (Other LIMITED) OLD CODE");
                        wWedField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus()))));
                        bonusField.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(0))));
                        fww = null;
                        wWedLabel.setVisible(true);
                        wWedLabel.setText(TodaysDailyBonus.getvGroupName());
                        wWedField.setVisible(true);
                        this.newBonus = true;
                    }
                }
                }
            }*/
        }
        doDailyBonus();
    }

    
    
    
    
    
    
    
    
    
    
    
    
    public void addTextfieldListeners() {

        /*depositField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber1().checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Amount of Deposit can Only be Numbers");
                        depositField.clear();
                        depositField.requestFocus();
                        return;
                    }
                }
        );
        depositField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        if (newValue.length() == 1) {
                            if (newValue.matches("0")) {
                                mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "ZERO cannot be the first number in the Deposit field.");
                                depositField.clear();
                                depositField.requestFocus();
                                return;
                            }
                        }
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Amount of Deposit can Only be Numbers");
                        depositField.clear();
                        depositField.requestFocus();
                        return;
                    }
                }
        );*/
        depositField.setOnMouseClicked(mouseHandler);
        CCNumberIn.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber1().checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Member Card Number can Only be Numbers");
                        CCNumberIn.clear();
                        CCNumberIn.requestFocus();
                        return;
                    }
                }
        );
        empNumberTextfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber1().checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Employee Number can Only be Numbers");
                        empNumberTextfield.clear();
                        empNumberTextfield.requestFocus();
                        return;
                    }
                    if (empNumberTextfield.getText().length() > 0) {
                        doButton.setDisable(false);
                    } else {
                        doButton.setDisable(true);
                    }
                }
        );
        empNumberTextfield.setOnMouseClicked(mouseHandler);
    }

    private void setrNumber(String r) {

        rNumber = r;
    }

    private String getrNumber() {
        return this.rNumber;
    }



    private boolean isToyValidInArrayList(String n) {
        boolean toyValid = false;

        // HERE IS WHERE I STARTED
        for (Toys l1 : l) {
            if (n.equals(l1.getNumber())) {
                toyValid = true;
            }
        }

        // HERE IS WHERE I ENDED
        return toyValid;
    }

    public void GoPostSuperApprovalDetails(String superNumber) {
        Thread thread34 = new Thread() {
            @Override

            public void run() {
                System.out.println("Super Number is : " + superNumber);
                //try {
                //    Thread.sleep(1000);
                //} catch (InterruptedException ex) {
                //    System.out.println(ex);
                //}
                db.InsertHistoricDataOneTimeXplain(member.getCID(), superNumber, tranTime, "GTTTAPP", "Approved By: " + superFN, "");
                //db.InsertAccountChangeMessage(member.getCID(), "GTTT Approved By: " + superFN, tranTime, superNumber, "GTTTAPP");
                //if (!db.InsertAccountChangeMessage(member.getCID(), "GTTT Approved By: " + superFN, new GetDay().getCurrentTimeStamp(), superNumber, "GTTTAPP")) {
                //    try {
                //        new ClubFunctions().GOPRTTxtFile(dbsp.pathNameBalReport + "MiscItem.txt", member.getCID(), "GTTT Approved By: " + superFN, new GetDay().getCurrentTimeStamp(), en, "GTTTAPP");
                //    } catch (FileNotFoundException ex) {
                //        System.out.println(ex);
                //    }
                //}
            }
        };
        thread34.start();
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F3) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F4) {
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
            if (ke.getCode() == KeyCode.TAB) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.UP) {
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
                doButton.fire();
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
            case TAB:
                break;
            case DOWN:
                break;
            case UP:
                break;
            case ESCAPE:
                exitButtonPushed();
                break;
            case ENTER:
                enterKeyPressed();
                break;
            default:
                break;
        }
    }

    private void getScene() {
        try {
            owner = (Stage) doButton.getScene().getWindow();
        } catch (Exception e) {
        }
    }

    private void getStage() {
        try {
            stageV = (Stage) CCNumberIn.getScene().getWindow();
        } catch (Exception e) {
        }
    }

    private String getReceipts(String n, ArrayList<rCeipts> rCeipts) {
        String StringItem = null;
        for (int y = 0; y < rCeipts.size(); y++) {
            if (n.trim().equals(rCeipts.get(y).getrItem())) {
                //System.out.println(rCeipts.get(y).getrItem() + " " + rCeipts.get(y).getrString() + " " + rCeipts.get(y).getrNumber());
                setrNumber(rCeipts.get(y).getrNumber());
                StringItem = rCeipts.get(y).getrString();
            }
        }
        return StringItem;
    }


    
    
    
    
    
    
    
    public void setHighlightListenerVIP(Pane r) {
        Set<Node> nodes = r.lookupAll(".text-field");
        for (Node node : nodes) {
            node.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    highlightTextFieldVIP(r);
                }
            });
        }
    }

    public void highlightTextFieldVIP(Pane r) {
        Set<Node> nodes = r.lookupAll(".text-field");
        for (Node node : nodes) {
            if (node.isFocused()) {
                node.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF");
                //System.out.println(node.getId());
                checkFocus(node.getId());
            } else {
                if (!node.isDisable()) {
                    node.setStyle("-fx-background-color: White");
                }
            }
        }
    }

    private void checkFocus(String id) {
        //mBox.showErrorClear(errorLabel, errorPane);
        switch (id) {
            case "CCNumberIn":
                System.out.println("this is a test of the" + id + " field");
                //mBox.showErrorLayoutXY("Please scan or Type in the Customer Card Number, press ENTER or touch the next field", errorLabel, errorPane, layoutX, layoutY);
                break;
            case "depositField":
                if (!customerInfoBox.isVisible()) {
                    getMemberInfo();
                }
                System.out.println("this is a test of the" + id + " field");
                //mBox.showError("ENTER 0 (ZERO) for list of all Promos", errorLabel, errorPane);
                //mBox.showErrorLayoutXY("ENTER 0 (ZERO) for list of all Promos", errorLabel, errorPane, 99.00, 275.00);
                break;
            case "empNumber":
                System.out.println("this is a test of the" + id + " field");
                if (!customerInfoBox.isVisible()) {
                    getMemberInfo();
                }
                //if (depositField.getText().trim().equals("")) {
                //    depositField.requestFocus();
                //}
            //empNumber.requestFocus();
            //System.out.println("here");
            //mBox.showError("Type in Your Employee Number then press the Save Button", errorLabel, errorPane);
            //mBox.showErrorLayoutXY("Type in Your Employee Number then press the Save Button", errorLabel, errorPane, 0.0, 619.00);
            //break;
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void exitButtonPushed() {
        System.out.println("WE ARE IN EXIT = = =  = 0");
        try{
            E = null;
            isEMail = "";
            rCeipts = null;
            confirm = false;
            voucher = false;
            multipleVouchers = false;
            //depositField.setText("0");
            //empNumberTextfield.setText("");
            voucherDeposit = null;
            en = null;
            fn = null;
            ln = null;
            CID = null;
            newBalance = 0.0;
            pw.close();
        } catch(Exception e){}
        db.disConnect();
        db.disConnect();
        db.disConnect();
        if (!SCLMT) {
            sc.setActivity("1");
        }
        stageV.close();
    }

    
    
    
    private void GetMemberUpdatePrint() {
        MemberUpdatePrintController wController = (MemberUpdatePrintController) FXLOADER.getController();
        wController.empName = member.getNameF() + " " + member.getNameL();
        wController.cssPath = cssPath;
        wController.m = member;
        wController.empID = "4792";
        wController.dbsp = dbsp;
        wController.sc = sc;
        wController.DB = db;
        wController.E = this.E;
        wController.pw = pw;
        wController.rCeipts = rCeipts;
        wController.tranTime = tranTime;
        wController.MSG = getReceipts("UpdatePrintMessage", rCeipts);
        

        try {
            sc.getpassWord(stageV, "/views/counterPopUp/MemberUpdatePrint.fxml", "", cssPath, 300.0, 300.0);
        } catch (IOException ex) {
           System.out.println(ex);
        }
        //SC.goToScene("EditBalance", stageV, EFX.getNameF(), null, boundsInScenememButton);

    }
    
    
    /*private void PrintReceipts(String i) {
        for (int y = 0; y < rCeipts.size(); y++) {
            //if (n.trim().equals(Receipts.get(y).getrItem())) {
            System.out.println(rCeipts.get(y).getrItem() + " " + rCeipts.get(y).getrString() + " " + rCeipts.get(y).getrNumber());
            //numberItem = Receipts.get(y).getrNumber();
            //StringItem = Receipts.get(y).getrString();
            //}
        }
    } */ 
    
}
