package views;


import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import commoncodes.ClubFunctions;
import commoncodes.ClubFunctions3;
import commoncodes.GetReceipts;
import commoncodes.IsItANumber;
import commoncodes.IsItANumber1;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.club.DB;
import models.settings.DailyBonus;
import models.club.Member;
import models.club.Memtick;
import models.settings.fWildWed;
import pReceipts.print;
import pReceipts.prtReceiptsFX;
import pWordFX.empFX;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import models.club.CheckFirstLastName;
import models.club.LastMemberTransaction;
import models.club.rCeipts;
import models.toys.Toys;
import pWordFX.employeeFX;
import views.counterPopUp.DuplicateMemberPopUpController;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Hannah
 */
public class NewMemberController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private VBox vbox1;
    @FXML private PasswordField cardNumberTextfield;
    @FXML private PasswordField employeeNumber;
    @FXML private TextField fNameTextfield;
    @FXML private TextField lNameTextfield;
    @FXML private TextField addressTextfield;
    @FXML private TextField cityTextfield;
    @FXML private TextField stateTextfield;
    @FXML private TextField zipTextfield;
    @FXML private TextField phone1Textfield;
    @FXML private TextField phone2Textfield;
    @FXML private TextField phone3Textfield;
    @FXML private TextField bday1Textfield;
    @FXML private TextField bday2Textfield;
    @FXML private TextField bday3Textfield;
    @FXML private TextField emailTextfield;
    @FXML private TextField wildWedTickets;
    @FXML private TextField aptTextFeild;
    @FXML private Label wwLabel;
    @FXML private PasswordField card1Field;
    @FXML private PasswordField card2Field;
    @FXML private TextField ticketsTextfield;
    @FXML private TextField bonusTickets;
    @FXML private Button postButton;
    @FXML private Button cancelButton;
    @FXML private GridPane gridPane1;
    @FXML private GridPane gridPane3;
    @FXML private Pane bottomPane;
    @FXML private Pane topPane;
    @FXML private Pane depositPane;
    @FXML private Pane errorBar;
    @FXML private Label errorLabel;
    @FXML private static String staticMemberID;
    @FXML private static String newBonus;
    

    
    
    private static final IsItANumber IIN = new IsItANumber();
    private static final ClubFunctions CF = new ClubFunctions();
    private static final GetDay GETDAY = new GetDay();
    private static final CheckFirstLastName CFLN = new CheckFirstLastName();
    private static final LocalDateTime TRANTIMEFINAL = LocalDateTime.now();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("hh:mm:ss");
    private static final String FINALTIME = TRANTIMEFINAL.format(FORMATTER);
    private static final DecimalFormat DF = new DecimalFormat("#");


    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    public static String csspath;
    public static DB db;
    public static messageBox mbox;
    public static employeeFX efx;
    public static FXMLLoader fxloader;
    public static String cn; // CARD NUMBER PASSED FROM COUNTER SCREEN
    public static ArrayList<empFX> E; 
    public static ArrayList<rCeipts> rCeipts;
    public static Member m;
    public static Memtick mt;
    public static PrintWriter pw;
    private static DailyBonus TodaysDailyBonus;
    public static ArrayList<DailyBonus> VIPPromosDailyBonus;

    
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#");
    private static Bounds boundsInScene;
    private static String en, fn, ln, wasFoundAgain = "False", tNumber = "", tranTime, rNumber, rWinner, superEN, superFN, superLN, newMemberID = "";
    private static String loc = "", eMail = "";
    public static ArrayList<LastMemberTransaction> lastmember;
    private static ArrayList<String> cCardNumbers;
    private static int vAmt, enGprob, enID, Super, superID, baseBonus, MinDep;
    private static Double bal;
    private boolean wasFound = false, GO = false, SCLMT = false;
    private static Stage stageV;
    private static DailyBonus ddb = null;
    private static Double depAmount = 0.0;
    private static Double freeTickets = 0.0;
    private boolean wkDayTickets = false, wkDayMultiply = false, wkDayBonus = false; 
    public static String NEWMEMRECEIPT;
    
    
    private static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    private static final DecimalFormat df = new DecimalFormat("#");
    private static final DateTimeFormatter MYDATEFORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter MYDATEFORMATTERR = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private static final GetReceipts getR = new GetReceipts();
    private static final char THISPAGECHAR = 3;
    private static Style Format = new Style();

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
       boundsInScene = bottomPane.localToScene(bottomPane.getBoundsInLocal());
        root.getStylesheets().add(csspath);
        bday1Textfield.setStyle("-fx-prompt-text-fill:BLACK;");
        topPane.setVisible(false);
        depositPane.setVisible(false);
        setCardNumberFromController(cn);
        wildWedTickets.setVisible(false);
        wwLabel.setVisible(false);
        freeTickets = db.getFreeTickets();
        //topPane.setLayoutX(boundsInScene.getMinX());
        //topPane.setLayoutY(boundsInScene.getMinY());
        //bottomPane.setVisible(false);
        postButton.setVisible(false);
        setHighLights();
        setKeyCodes();
        setToUpper();
        addPhonefieldListeners();
        showErrorClear();
        Platform.runLater(()->setBaseBonus());  
        Platform.runLater(()->setBottomPane());  
        //Platform.runLater(()->db.connect());  
    } 
    
    
     private void setBaseBonus() {
        
        getReceipts("Bonus", rCeipts);
        baseBonus = Integer.parseInt(DECIMALFORMAT.format(Double.parseDouble(getrNumber())));
        System.out.println("baseBonus = " + baseBonus);
        getReceipts("MinDeposit", rCeipts);
        MinDep = Integer.parseInt(DECIMALFORMAT.format(Double.parseDouble(getrNumber())));
        System.out.println("Minimum Deposit = " + MinDep);
        
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
    
    
    
    private void setBaseBonusSAVE() {
        
        getReceipts("Bonus");
        baseBonus = Integer.parseInt(DF.format(Double.parseDouble(getrNumber())));
    }
   
    public void postButtonGO() {
    //String loc = " ";
    String addressWithApt ="";
    showErrorClear();
    CF.clearApostophies(gridPane1);
    CF.clearApostophies(bottomPane);
    en = employeeNumber.getText().trim();
    if(en.isEmpty()) {
        mbox.showError("Employee Number cannot be blank", errorLabel, errorBar);
        return;
    } else {
    if (!isEMPValidInArrayList(employeeNumber.getText().trim())) {
        mbox.showError("Employee Number cannot be blank", errorLabel, errorBar);
        return;
    } 
    Platform.runLater(() -> rWinner = db.RandomWinner("New Member")); //USES CONNOTHER
    String bdayString = bday1Textfield.getText() + "/" + bday2Textfield.getText() + "/" + bday3Textfield.getText();
    LocalDate d = GETDAY.setToLocalDate(bdayString);
    showErrorClear();
    if (bottomPane.isVisible()) {
        System.out.println("here is aptTextFeild.getText() " + aptTextFeild.getText());
        if (!aptTextFeild.getText().isEmpty()){
            addressWithApt = addressTextfield.getText() + "  #" + aptTextFeild.getText();
        } else {
            addressWithApt = addressTextfield.getText();
        }
        newMemberID = db.getNewMemberNumber();
        m = new Member(cardNumberTextfield.getText(), newMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressWithApt, cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", d, "0", false, false, GETDAY.NOW_LOCAL_DATE(), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText(), "");
        if (!db.putNewMember(m)) {
            showError("Error creating the New Member " + m.getNameF() + " " + m.getNameL());
            new prtReceiptsFX().prtError("error Creating New Member " + m.getNameF() + " " + m.getNameL());
            return;
        } else {
            if (!emailTextfield.getText().trim().isEmpty()) {
                eMail = emailTextfield.getText();
                db.putNewMemberEmail(m, emailTextfield.getText());                
            }
            setLastMemberVariables();
            showError("Please enter ticket amount for New Member");
            staticMemberID = m.getCID();
            topPane.setVisible(false);
            bottomPane.setVisible(false);
            depositPane.setLayoutX(boundsInScene.getMinX());
            depositPane.setLayoutY(boundsInScene.getMinY());
            depositPane.setVisible(true);  
            postButton.setVisible(true);
            postButton.setDisable(true);
            wildWedTickets.setVisible(false);
            wwLabel.setVisible(false);
            ticketsTextfield.requestFocus();
            cancelButton.setDisable(true);
        db.disConnect();
        }
    db.disConnect();
    return;
    } else {
        if (errorLabel.getText().equals("Please enter ticket amount for New Member")) {
            
            return;
        }
        showErrorClear();
        
        if (ticketsTextfield.getText().trim().isEmpty()) {
            showError("You must enter Tickets in the Ticket Field");
            return;
        }
        if (ticketsTextfield.getText().equals(m.getCCN())) {
            ticketsTextfield.setText("");
            mbox.showAlert(Alert.AlertType.ERROR, null, "STOP!", "Deposit amount and Card number cannot be the same");
            return;
        }
        
        //HERE IS WEHRE WE ADDED WILD WED CODE
        depAmount = Double.valueOf(ticketsTextfield.getText().trim());
        //depAmount = Double.parseDouble(ticketsTextfield.getText().trim()) + Double.parseDouble(bonusTickets.getText().trim());
        if (wkDayTickets) {
            depAmount = depAmount + Double.valueOf(wildWedTickets.getText());
        }
        if (wkDayMultiply) {
            depAmount = depAmount + Double.valueOf(wildWedTickets.getText());
            
        }
        if (wkDayBonus) {
            bonusTickets.setText(String.valueOf(wildWedTickets.getText()));
        }
        
        ///if (bonusTickets.getText().trim().isEmpty()) {
            ///doDailyBonus();
            ///if (!wildWedTickets.isVisible()) {
               /// doBonus();
                //bonusTickets.setText(String.valueOf(Integer.parseInt(ticketsTextfield.getText().trim()) / baseBonus)); 
                //bonusTickets.setText(String.valueOf(db.calcBonus(Integer.parseInt(ticketsTextfield.getText()))));
                ///depAmount = Double.parseDouble(ticketsTextfield.getText().trim());
            ///}/// else {
               /// depAmount = Double.parseDouble(ticketsTextfield.getText().trim()) + Double.parseDouble(bonusTickets.getText().trim());
                //if (ddb.getvInits().isEmpty() || ddb.getvName().equals("NONE")) {
                //    loc = " ";
                //} else {
                //    loc = ddb.getvInits();
                //}///
            ///}
        ///} else {
            //doWildWed();
            ///if (!wildWedTickets.isVisible()) {
               /// doBonus();
                //bonusTickets.setText(String.valueOf(Integer.parseInt(ticketsTextfield.getText().trim()) / baseBonus)); 
                //bonusTickets.setText(String.valueOf(db.calcBonus(Integer.parseInt(ticketsTextfield.getText()))));
                ///depAmount = Double.parseDouble(ticketsTextfield.getText().trim());
            ///} else {
                ///depAmount = Double.parseDouble(ticketsTextfield.getText().trim()) + Double.parseDouble(wildWedTickets.getText().trim());
                //if (ddb.getvInits().isEmpty() || ddb.getvName().equals("NONE")) {
                 //   loc = " ";
                //} else {
                //    loc = ddb.getvInits();
                //}
            ///}
        ///}
        
        
        
        //NOTE ALSO YOU ADDED THE DOwILDwED CODE BELOW.
        //HERE IS WHERE THE WILD WED CODE ENDED.
        
        if (!en.isEmpty()) {
            
        } else {
            try {
                if (!getEmpNumber()) {
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "Error", "Something wrong here");
                    return;
                }
            } catch (IOException ex) {
                new prtReceiptsFX().prtError("Emp Number Blank and Can't get anotherOne. (See NewMemberController)");
                System.out.println(ex);
            }
        }
        
            System.out.println("====================================================== " + FINALTIME);

        //THIS MIGHT BE WHERE WE ADD IN THE CODE FOR GTTTSUPERAPP
        
        getReceipts("GTTTPopup");
        if (getrNumber().equals("1.0")) {
            getReceipts("GreaterThanDepWith");
            System.out.println(Double.valueOf(getrNumber()) + " " + Double.valueOf(ticketsTextfield.getText().trim()));
            if (Double.valueOf(getrNumber()) < Double.valueOf(ticketsTextfield.getText().trim())) {
                if (enGprob < 2) {
                    try {
                        sc.getpassWord(stageV, "/pWordFX/withdrawalPassword.fxml", "Number", "Enter Employee Number:", 200.00 + 350.0, 200.00);
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    if (!isSuperValidInArrayList(sc.getGameNumber())) {
                        GO = false;
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, ln, "Not a Valid Employee Number");
                        return;
                    } else {
                        GO = true;
                        if (Super != 1 && Super != 3) {
                        //if (Super < 2) {
                            mbox.showAlert(Alert.AlertType.ERROR, stageV, ln, "not a valid Supervisor Number");
                            return;
                        }
                    }
                    Platform.runLater(() -> GoPostSuperApprovalDetails(String.valueOf(superID), newMemberID, FINALTIME));
                } else {
                    Platform.runLater(() -> GoPostSuperApprovalDetails(String.valueOf(enID), newMemberID, FINALTIME));
                }
            }
            //LEFT OFF HERE SATURDAY 12TH DOING THE BELOW THING TWICE ONE ON THE DEPOSIT SCREEN AND ONCE IN THE GREATERTHATTENTHOUGHSAND CODE
        } //THIS IS THE END OF THE GETRNUMBER IF GTTTPopup is on (1)

        
        //END
        
        
        
        //* Member ID	Employee Number     Transaction Number	Date	Time	Added	Bonus	Subtracted  Location
        mt = new Memtick(staticMemberID, en, FINALTIME, LocalDate.now(), depAmount, Double.valueOf(bonusTickets.getText().trim()) + freeTickets, 0.0, loc, 0);
        //db.connect();
        if (!db.InsertData(mt)) {
            System.out.println("error depositing member detail");
            new prtReceiptsFX().prtError("error depositing member detail");
        } else {
            bal = 0.0 + mt.getAdded() + mt.getBonus();
            if (!db.updateTicketBalance(bal, mt)) {
                System.out.println("error depositing member balance update (See MemDepositFXController)");   
                new prtReceiptsFX().prtError("error depositing member balance update (See MemDepositFXController)");
            } else {
                try {
                    //printReceipt();
                    printEscPosNEWMEMBER();
                    showErrorClear();
                    db.disConnect();
                    cancelButton.setDisable(false);
                    cancelButton.fire();
                } catch (FileNotFoundException ex) {
                    System.out.println("error printing Deposit receipt (See MemDepositFXController)\n" + ex);   
                    new prtReceiptsFX().prtError("error printing Deposit receipt (See MemDepositFXController)\n" + ex);
                    
                //} catch (PrintException | IOException ex) {
                //    System.out.println("error printing Deposit receipt (See MemDepositFXController)\n" + ex);   
                } catch (PrintException ex) {
                    Logger.getLogger(NewMemberController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NewMemberController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        //db.connect();
        //cancelButton.setDisable(false);
        //cancelButton.fire();
        }
    }
    try {
    db.disConnect();
    } catch(Exception e) {}
    }
    
    
    /*private void doWildWed() {
        int dFieldValue = Integer.parseInt(ticketsTextfield.getText().trim());
        ddb = db.LookForDailyBonus();
        if (!ddb.getvName().equals("NONE")) { // THERE IS A DAILYBONUS GO CALCULATE IT            
            fWildWed fww = ddb.calcWildWed2(dFieldValue, m);
            if (fww.getWildWedBonus() > 0.0) {
                //System.out.println("wild wed bonus is greater then 0");
                //System.out.println("getNewBalance " + fww.getNewBalance());
                wildWedTickets.setText(String.valueOf(Integer.parseInt(DF.format(fww.getWildWedBonus()))));
                bonusTickets.setText(String.valueOf(Integer.parseInt(DF.format(fww.getNewBalance()))));
                fww = null;
                wwLabel.setVisible(true);
                wildWedTickets.setVisible(true);
            }
            if (dFieldValue > 99) {
                fww = ddb.CalcwDayOther(dFieldValue, m);
                if (ddb.getvOther() > 0.0) {
                System.out.println("thisis where we go trough for tuesday 3");
                    wildWedTickets.setText(String.valueOf(Integer.parseInt(DF.format(fww.getWildWedBonus()))));
                    bonusTickets.setText(String.valueOf(Integer.parseInt(DF.format(0))));
                    fww = null;
                    wwLabel.setVisible(true);
                    wwLabel.setText(ddb.getvGroupName());
                    wildWedTickets.setVisible(true);
                    //this.newBonus = true;
                }
            }

        }
    }*/
    
    
    /* private int getRepeatSoFar() {
        int soFarRepeat = 0; 
        try {
            soFarRepeat = db.getDailyBonusRepeat(member.getCID(), TodaysDailyBonus.getvInits());
            //System.out.println(" (NEW CODE) Sofarrepeat in the new code " + soFarRepeat + " TodaysDailyBonus.repeat " + TodaysDailyBonus.getvRepeat());
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return soFarRepeat;
    }*/
    
    
    
    private void setLastMemberVariables() {
        sc.setCCN(m.getCCN());
        sc.setEmployee(en);
        sc.setEmpName(fn);
        sc.setActivity("NEWMEM");
        SCLMT = true;
        sc.setLastName(m.getNameL());
        System.out.println(m.getCCN() + " " + en + " " + fn + " " + m.getNameL());

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
    
    
    //YOU NEED TO BRING vippromosdailybonus OVER TO NEW MEMBERS FROM COUNTER LIKE IN dEPOSIT
    
    
      private void doDailyBonus() { //THIS IS WHERE THE NEW CODE IS SO THAT ALL DAILY BONUS'S WORK 11/24
        //WE MIGHT DO A BOOLEAN HERE IFDODAILYBONUS() THEN DO NOT GO THROUGH ANY OTHER BACK UP TOP 
        System.out.println("===============We are in the (NEW CODE)");
        boolean isDailyBonusToday = false;
        int depositFieldValue = 0;
        try {
            depositFieldValue = Integer.parseInt(ticketsTextfield.getText().trim());
        } catch (NumberFormatException e) {
        }
        isDailyBonusToday = getTodaysDailyBonus(new GetDay().LongDayWeek(new GetDay().getday()).toUpperCase()); // NEW WAY
        if (isDailyBonusToday) { //IF THERE IS A DAY THAT MATCHES TODAY THEN GO INTO THE NEXT PART
            if (new GetDay().getDateLocalDateHour() >= TodaysDailyBonus.getvSTime() && new GetDay().getDateLocalDateHour() < TodaysDailyBonus.getvETime()) {
                System.out.println("NEW CODE = = Current Hour =  " + new GetDay().getDateLocalDateHour() + " and the Hour for Start Time " + TodaysDailyBonus.getvSTime() + " End Time " + TodaysDailyBonus.getvETime());


                if (TodaysDailyBonus.getvMultiply() > 0.0) {
                    //if (getRepeatSoFar() < TodaysDailyBonus.getvRepeat()) {
                        System.out.println("thisis whxere we go trough for DailyBonus 1 (Multiply) NEW CODE");
                        fWildWed fww = TodaysDailyBonus.calcWildWed2(depositFieldValue, m);
                        if (Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus())) > 0) {
                            wildWedTickets.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus()))));
                            wwLabel.setVisible(true);
                            wwLabel.setText(TodaysDailyBonus.getvName());
                            wildWedTickets.setVisible(true);
                            int newdepamount = depositFieldValue + Integer.parseInt(wildWedTickets.getText());
                            bonusTickets.setText(String.valueOf(newdepamount * baseBonus / 100));
                            loc = TodaysDailyBonus.getvInits();
                            wkDayMultiply = true;

                        }
                        System.out.println("================today is MULTIPLY at the end(NEW CODE) " + fww.getWildWedBonus() + " " + fww.getNewBalance());
                    //} else {
                        //System.out.println("================today is MULTIPLY but the repeat has been reached (NEW CODE)");
                    //}
                } else {
                    System.out.println("================today is MULTIPLY at the end(NEW CODE) ");
                }

                
                
                
                
                
                if (TodaysDailyBonus.getvTickets() > 0.0) {
                    if (depositFieldValue >= TodaysDailyBonus.getvMax()) { //THIS IS THE MINIMUM DEPOSIT EVEN THOUGH IT SAYS GETMAX()
                        //if (getRepeatSoFar() < TodaysDailyBonus.getvRepeat()) {
                            System.out.println("================today is Tickets (NEW CODE)");
                            fWildWed fww = TodaysDailyBonus.CalcwDayTickets(depositFieldValue, m);
                            System.out.println("thisis where we go trough for DailyBonus 2 (Tickets) NEW CODE");
                            wildWedTickets.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(TodaysDailyBonus.getvTickets()))));
                            bonusTickets.setText(String.valueOf(depositFieldValue * baseBonus / 100));
                            fww = null;
                            wwLabel.setVisible(true);
                            wwLabel.setText(TodaysDailyBonus.getvName());
                            wildWedTickets.setVisible(true);
                            loc = TodaysDailyBonus.getvInits();
                            wkDayTickets = true;
                        //} else {
                            //System.out.println("================today is Tickets but the repeat has been reached (NEW CODE)");
                        //}
                    } else {
                        System.out.println("================today is Tickets but the min depsoit has not been met (NEW CODE)");
                    }
                }
                
                
                /*
                YOU NEED TO BREAK UP OTHER SO IT HAS 0 ININATE AND PER REPEATS
                */

                if (TodaysDailyBonus.getvOther() > 0.0) {
                    if (depositFieldValue >= TodaysDailyBonus.getvMax()) {
                        //if (getRepeatSoFar() < TodaysDailyBonus.getvRepeat()) {
                            System.out.println("thisis whxere we go trough for DailyBonus 1 (Multiply) NEW CODE");
                            fWildWed fww = TodaysDailyBonus.CalcwDayOther(depositFieldValue, m);
                            System.out.println("thisis where we go trough for DailyBonus 3 (Other INFINITE) NEW CODE " + fww.getNewBalance());
                            wildWedTickets.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(fww.getWildWedBonus()))));
                            bonusTickets.setText(String.valueOf(Integer.parseInt(DECIMALFORMAT.format(0))));
                            fww = null;
                            wwLabel.setVisible(true);
                            wwLabel.setText(TodaysDailyBonus.getvName());
                            wildWedTickets.setVisible(true);
                            //this.newBonus = true;
                            loc = TodaysDailyBonus.getvInits();
                            wkDayBonus = true;
                        //} else { //END OF REPEATS
                        //    System.out.println("================today is other but the repeat has been reached (NEW CODE)");
                        //}
                    } else { //END OF GETMIN/MAX DEPSOIT AMOUNT
                        System.out.println("================today is other but the min depsoit has not been met (NEW CODE)");
                    }
                } //END OF GETOTHER()

            
            
            
            
            } else { //THIS IS THE END OF GET HOURS
                System.out.println(" (NEW CODE)================today THE HOURS HAVE NOT BEEN REACHED Start Time: " + TodaysDailyBonus.getvSTime() + " End Time: " + TodaysDailyBonus.getvETime());
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public void GoPostSuperApprovalDetails(String superNumber, String n, String tranTimeFinal) {
        Thread thread34 = new Thread() {
            @Override
            
            public void run() {
                //new DB().InsertHistoricDataOneTime(n, superNumber, tranTime, "GTTTAPP");
                db.InsertHistoricDataOneTimeXplain(n, superNumber, tranTimeFinal, "GTTTAPP", "Approved By: " + superFN, "");

            }
        };
        thread34.start();
    }
    
    
    private void printReceipt() throws FileNotFoundException {
        pw = new PrintWriter(new File(dbStringPath.pathNameLocal + "NewMemberReceipt.txt"));
        pw.println(LocalDate.now() + " " + LocalTime.now());
        pw.println("POJOS FAMILY FUN"); // to test if it works.
        pw.println("Club New Member Receipt"); // to test if it works.
        pw.println("======================"); // to test if it works.
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("Welcome " + m.getNameF() + " " + m.getNameL());
        pw.println("");
        pw.println("Deposit     :\t" + depAmount);
        if (!freeTickets.equals(0)){
            pw.println("New Member  :\t" + DF.format(freeTickets));
        }
        pw.println("Bonus       :\t" + bonusTickets.getText().trim());
        pw.println("Balance     :\t" + DF.format(bal));
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("Thank You, " + fn);
        pw.println("");
        pw.println("");
        pw.println(db.getNewMemberReceipt());
        pw.println("");
        /*pw.println("IMPORTANT ! \n"
                + "You must bring this card in order to use \n"
                + "your account. Lost cards may be \n"
                + "replaced for 150 tickets.\n"
                + "Deposits of 100 Tickets or More \n"
                + "get a 10% Bonus addrd free.\n"
                + "Accounts will be closed after (7) \n"
                + "years of inactivity.\n"
                + "Pojos is not responible for tickets \n"
                + "withdrawn with lost or stolen cards.\n"
                + "Pojos provides members with an account \n"
                + "lock if your card is lost. Call 376-6981 \n"
                + "Questions?, 376-6981");
        pw.println("");*/
        pw.println("Account Number: " + m.getCCN());
        pw.println(" ");
        pw.println(" ");
        getReceipts("NewMemberFreeCandy");
        if (rNumber.equals("1.0")) {
            pw.println(new ClubFunctions3().getSplitString(getReceipts("NewMemberFreeCandy")));
        }
        pw.println("");
        pw.println("");
        pw.println("");
        //pw.println(new ClubFunctions3().getSplitString(db.RandomWinner("New Member")));
        pw.println(db.RandomWinner("New Member"));
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println("EMPLOYEE:");
        pw.println("PLEASE VERIFY THIS MEMBER's INFORMATION");
        pw.println(" ");
        pw.println(" ");
        pw.println(m.getNameF() + " " + m.getNameL());
        pw.println(m.getAddress());
        pw.println(m.getCity() + " " + m.getState());
        pw.println(m.getZip());
        pw.println(eMail);
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
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
        pr.printReceipt("NewMemberReceipt.txt");
    }
    
    
    
    private void printEscPosNEWMEMBER()  throws PrintException, IOException{
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
            githubBufferedImage = ImageIO.read(new File(dbsp.pathNameImages + "/ReceiptLogo/newmemberLogo.png"));
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
            //JOptionPane.showMessageDialog(null, "we are here and the thing is " + printthis + " ----" + getReceiptNumber);
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
        
        
        
        
        

        //PRINT RECEIPT HEADING (NEW MEMBER)
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "NEW MEMBER");
        escpos.feed(2);

        
        
        //PRINT WELCOME NEW MEMBER NAME
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "Welcome " + m.getNameF() + " " + m.getNameL());
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
        

        //PRINT FIRST DEPOSIT
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "DEPOSIT: \t" + DF.format(depAmount));
        escpos.feed(1);        
        

        //PRINT NEW MEMBER FREE TICKETS
        if (!freeTickets.equals(0)){
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "New Member: \t" + DF.format(freeTickets));
        escpos.feed(1);        
        }

        //PRINT NEW MEMBER BONUS
        
        if (!bonusTickets.getText().equals("0")){
            System.out.println("========================== " + mt.getBonus());
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Bonus: \t\t" + bonusTickets.getText().trim());
        escpos.feed(1);        
        }
        
        //PRINT NEW BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Balance: \t" + DF.format(bal));
        escpos.feed(1);        
        
        
        
        
        //NEW MEMBER FREE CANDY
        if (getR.getReceiptsNumber(rCeipts, "NewMemberFreeCandy").equals("1.0")) {
            escpos.feed(2);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, new ClubFunctions3().getSplitString(getR.getReceipts(rCeipts, "NewMemberFreeCandy")));
            escpos.feed(2);
        }
        
        //NEW MEMBER CLUB INFO
        if (!NEWMEMRECEIPT.isEmpty()) {
            escpos.feed(2);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, NEWMEMRECEIPT);
            escpos.feed(8);
        }

        
        
        
          // cut
        outputStream.write(29); // GS
        outputStream.write('V');
        outputStream.write(48);


        escpos.feed(4);
            escpos.feed(2);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, "EMPLOYEE:");
            escpos.feed(1);
            escpos.feed(2);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, "PLEASE VERIFY THIS MEMBER's INFORMATION");
            escpos.feed(3);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, m.getNameF() + " " + m.getNameL());
            escpos.feed(1);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, m.getAddress());
            escpos.feed(1);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, m.getCity() + " " + m.getState());
            escpos.feed(1);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, m.getZip());
            escpos.feed(1);
            Format = new Style()
                    .setFontName(Style.FontName.Font_A_Default)
                    .setFontSize(Style.FontSize._1, Style.FontSize._1)
                    .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                    .setJustification(EscPosConst.Justification.Left_Default);
            escpos.write(Format, eMail);
 
 
        
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
            escpos.feed(2);
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
    
    
    
    
    
    
    
    
    
    
    
    
    private void doBonus() {
                System.out.println(" = = = = = HERE IN ENTERKEYPRESSED, CALCING BONUS " + depAmount + " " + MinDep);
        try {
            if (depAmount >= MinDep) {
                System.out.println(" = = = = = HERE IN ENTERKEYPRESSED, CALCING BONUS ");
                bonusTickets.setText(String.valueOf(Integer.parseInt(ticketsTextfield.getText()) * baseBonus / 100));
            } else {
                bonusTickets.setText("0");
            }
        } catch (NumberFormatException e) {
            System.out.println(" ERROR " + e);
        }



        //newBonus = String.valueOf(Integer.parseInt(ticketsTextfield.getText().trim()) / baseBonus); 
        //newBonus = String.valueOf(db.calcBonus(Integer.parseInt(ticketsTextfield.getText().trim())));
        //bonusTickets.setText(newBonus);
    }
    
    
    private void CheckOtherAccount() {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                cCardNumbers = CFLN.SearchOtherAccountReturnAcctNumber(fNameTextfield.getText(), lNameTextfield.getText());
            }
        };
        thread1.start();
    }
    
    
    private void getAccount(String acctNumber) {
        DuplicateMemberPopUpController wController = (DuplicateMemberPopUpController) fxloader.getController();
        wController.CID = acctNumber;
        wController.sc = sc;
        wController.dbsp = dbsp;
        wController.cCardNumbers = cCardNumbers;
        wController.css = csspath;
        getStageV();
        try {
            sc.getpassWord(stageV, "/views/counterPopUp/DuplicateMemberPopUp.fxml", newBonus, tranTime, 200.0, 200.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    
    private void CheckOtherAccountAgain() {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                wasFoundAgain = CFLN.SearchOtherAccount4(fNameTextfield.getText(), lNameTextfield.getText(), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText(), bday1Textfield.getText() + "/" +  bday2Textfield.getText() + "/" + bday3Textfield.getText(), addressTextfield.getText(), zipTextfield.getText());
                //cfln.SearchOtherAccount(fNameTextfield.getText(), lNameTextfield.getText());
                //cfln.SearchOtherAccount4(fNameTextfield.getText(), lNameTextfield.getText(), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText(), bday1Textfield.getText() + "/" +  bday2Textfield.getText() + "/" + bday3Textfield.getText(), addressTextfield.getText(), zipTextfield.getText());
            }
        };
        thread1.start();
    }
    
    
    

    private void enterKeyPressed() {
        showErrorClear();
        if (depositPane.isVisible()) {
            if (ticketsTextfield.isFocused()) {
                if (ticketsTextfield.getText().equals(cn)) {
                    ticketsTextfield.setText("");
                    ticketsTextfield.requestFocus();
                    mbox.showAlert(Alert.AlertType.ERROR, null, "STOP!", "Deposit amount and Card number cannot be the same");
                    return;
                }
                if (ticketsTextfield.getText().trim().isEmpty()) {
                    showError("Amount of Tickets can be Numbers Only and Cannot be Blank");
                    return;
                } else {
                    depAmount = Double.valueOf(ticketsTextfield.getText().trim());
                    System.out.println("========================== here is do Bonus");
                    doBonus();
                    doDailyBonus();
                    
            }
            if (postButton.isFocused()) {
                postButton.fire();
            } else {
                postButton.requestFocus();
                return;
            }
        }

        if (topPane.isVisible()) {
            if (card1Field.isFocused()) {
                if (card1Field.getText().isEmpty() || card1Field.getText().trim().length() < 5) {
                    showError("Card Number cannot be Blank AND must me more then 4 numbers");
                    card1Field.clear();
                    card1Field.requestFocus();
                    return;
                }
                try {
                    if (!db.isMemberValid(card1Field.getText())) {
                        card2Field.requestFocus();
                        return;
                    } else {
                        card1Field.clear();
                        card1Field.requestFocus();
                        showError("This Number is not Available");
                        //JOptionPane.showMessageDialog(null, "This Member Number is in use.");
                        return;

                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
            if (card2Field.isFocused()) {
                if (card1Field.getText() == null ? card2Field.getText() == null : card1Field.getText().equals(card2Field.getText())) {
                    cardNumberTextfield.setText(card1Field.getText().trim());
                    setBottomPane();
                    return;
                } else {
                    showError("The Numbers do NOT match");
                    //JOptionPane.showMessageDialog(null, "The Numbers do NOT match.");
                    card2Field.clear();
                    card1Field.clear();
                    card1Field.requestFocus();
                    return;
                }
            }
            if (ticketsTextfield.isFocused()) {
                //mbox.showAlert(Alert.AlertType.ERROR, null, "STOP", "Hey");
                if (ticketsTextfield.getText().equals(cn)) {
                    ticketsTextfield.setText("");
                    ticketsTextfield.requestFocus();
                    mbox.showAlert(Alert.AlertType.ERROR, null, "STOP!", "Deposit amount and Card number cannot be the same");
                    return;
                }
                doDailyBonus();
                
          if (wildWedTickets.isVisible()) {
                postButton.requestFocus();
                return;

            } else {
                // HERE IS BACK TO THE ORIGINAL CODE
                System.out.println(" = = = = = HERE IN ENTERKEYPRESSED, THIS SHOULD BE THE BONUS CALULATING");
                try {
                    if (depAmount >= MinDep) {
                    System.out.println(" = = = = = HERE IN ENTERKEYPRESSED, CALCING BONUS " );
                        bonusTickets.setText(String.valueOf(Integer.parseInt(ticketsTextfield.getText()) * baseBonus / 100));
                    } else {
                        bonusTickets.setText("0");                        
                    }
                } catch (NumberFormatException e) {
                    System.out.println(" ERROR " + e );
                }
                 postButton.requestFocus();
                //TestLastTranArray();
                //return;
            }    
            }
                
                /*
                           wildWedTickets.setText(String.valueOf(Integer.parseInt(DF.format(fww.getWildWedBonus()))));
                    bonusTickets.setText(String.valueOf(Integer.parseInt(DF.format(0))));
                    fww = null;
                    wwLabel.setVisible(true);
                    wwLabel.setText(ddb.getvGroupName());
                    wildWedTickets.setVisible(true);
          */
                
                //doBonus();
                //newBonus = String.valueOf(Integer.parseInt(ticketsTextfield.getText().trim()) / baseBonus);         
                //newBonus = String.valueOf(db.calcBonus(Integer.parseInt(ticketsTextfield.getText().trim())));
                //bonusTickets.setText(newBonus);
                //doWildWed();
                postButton.requestFocus();
                return;
            }
            if (postButton.isFocused()) {
                postButton.fire();
            }

        } else {
            if (fNameTextfield.isFocused()) {
                if (fNameTextfield.getText().equals(cardNumberTextfield.getText())) {
                    fNameTextfield.clear();
                    fNameTextfield.requestFocus();
                } else {
                    lNameTextfield.requestFocus();
                }
                if (fNameTextfield.getText().trim().isEmpty()) {
                    showError("First Name Field cannot be blank");
                    Platform.runLater(()->fNameTextfield.requestFocus());
                }
                return;
            }
            if (lNameTextfield.isFocused()) {
                addressTextfield.requestFocus();
                Platform.runLater(()->CheckOtherAccount());
                if (lNameTextfield.getText().trim().isEmpty()) {
                    showError("Last Name Field cannot be blank");
                    Platform.runLater(()->lNameTextfield.requestFocus());
                }
                return;
            }
            if (addressTextfield.isFocused()) {
                cityTextfield.requestFocus();
                if (addressTextfield.getText().trim().isEmpty()) {
                    showError("Address Field cannot be blank");
                    Platform.runLater(()->addressTextfield.requestFocus());
                }
                return;
            }
            if (aptTextFeild.isFocused()) {
                cityTextfield.requestFocus();
                return;
            }
            if (cityTextfield.isFocused()) {
                if (!cCardNumbers.isEmpty()) {
                    getAccount(wasFoundAgain);
                    CFLN.SearchOtherAccount(fNameTextfield.getText(), lNameTextfield.getText());
                }
                stateTextfield.requestFocus();
                if (cityTextfield.getText().trim().isEmpty()) {
                    showError("City Field cannot be blank");
                    Platform.runLater(()->cityTextfield.requestFocus());
                }
                return;
            }
            if (stateTextfield.isFocused()) {
                zipTextfield.requestFocus();
                if (stateTextfield.getText().trim().isEmpty()) {
                    showError("State Field cannot be blank");
                    Platform.runLater(()->stateTextfield.requestFocus());
                }
                return;
            }
            if (zipTextfield.isFocused()) {
                if (!CF.testPhone(4, zipTextfield.getText())) {
                    showError("This Field must have five (5) digits");
                    zipTextfield.requestFocus();
                    return;
                } else {
                    //bday1Textfield.requestFocus();
                    //emailTextfield.requestFocus();
                    phone1Textfield.requestFocus();
                    return;
                }
            }

            if (phone1Textfield.isFocused()) {
                if (!CF.testPhone(1, phone1Textfield.getText())) {
                    showError("This Field must have three (3) digits");
                    phone1Textfield.requestFocus();
                    return;
                } else {
                    phone2Textfield.requestFocus();
                    return;
                }
            }
            if (phone2Textfield.isFocused()) {
                if (!CF.testPhone(2, phone2Textfield.getText())) {
                    showError("This Field must have three (3) digits");
                    phone2Textfield.requestFocus();
                    return;
                } else {
                    phone3Textfield.requestFocus();
                    return;
                }
            }
            if (phone3Textfield.isFocused()) {
                if (!CF.testPhone(1, phone1Textfield.getText())) {
                    showError("This Field must have four (4) digits");
                    phone3Textfield.requestFocus();
                    return;
                } else {
                    bday1Textfield.requestFocus();
                    return;
                }
            }

            if (bday1Textfield.isFocused()) {
                if (checkBDAY1(bday1Textfield.getText())) {
                    bday2Textfield.requestFocus();
                } else {
                    bday1Textfield.clear();
                    bday1Textfield.requestFocus();
                }
                return;
            }
            if (bday2Textfield.isFocused()) {
                if (checkBDAY2(bday2Textfield.getText())) {
                    bday3Textfield.requestFocus();
                } else {
                    bday2Textfield.clear();
                    bday2Textfield.requestFocus();
                }
                return;
            }

            if (bday3Textfield.isFocused()) {
                
                if (!checkBDAY3(bday3Textfield.getText())) {
                    //JOptionPane.showMessageDialog(null, "Here");
                    bday3Textfield.clear();
                    bday3Textfield.requestFocus();
                    bday3Textfield.selectAll();
                    return;
                } else {
                    if (!testYearDate()) {
                        showError("YEAR of the Birthday cannot be greater then this year or more then 100 years ago.");
                        bday3Textfield.clear();
                        bday3Textfield.requestFocus();
                        bday3Textfield.selectAll();
                    } else {
                        emailTextfield.requestFocus();
                    }
                }
                return;
            }
            if (emailTextfield.isFocused()) {
                            if (!wasFoundAgain.equals("False")) {
                                new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "WAIT !!", wasFoundAgain);
                            }
                employeeNumber.requestFocus();
                return;
            }
            if (employeeNumber.isFocused()) {
                try {
                    if (!loginButtonPushed()) {
                        employeeNumber.clear();
                        employeeNumber.requestFocus();
                        return;
                    } else {
                        setEmp();
                        employeeNumber.setDisable(true);
                        postButton.requestFocus();
                        return;
                    }
                } catch (IOException ex) {
            System.out.println(ex);
                }
            }
            if (postButton.isFocused()) {
                postButton.fire();
            }
        }
    }

    public boolean getEmpNumber() throws IOException {
        //employeeFX eFX = new employeeFX();
        Boolean GO = false;
        sc.setGameNumber(null);
        efx.Flush();
        getStageV();

        Bounds boundsInScene2 = ticketsTextfield.localToScene(ticketsTextfield.getBoundsInLocal());
        sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", boundsInScene2.getMinX(), boundsInScene2.getMinY());
        if (!loginButtonPushed()){
            GO = false;
            mbox.showAlert(Alert.AlertType.ERROR, stageV, "Error", "Employee Number Not Found");
            //System.out.println(GO);
        } else {
            if (!efx.getEmpNumber().equals("Number") && !efx.getEmpNumber().isEmpty()) {
                GO = true;
                //System.out.println(GO);
            } else {
                GO = false;
                //System.out.println(GO);
            }
        }
        efx.Flush();
        return GO;
    }

    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        //sc.setGameNumber(null);
        //eFX.Flush();
        GO = isEMPValidInArrayList(employeeNumber.getText().trim());
        //setEmp();
        return GO;
    }

    private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        //for (empFX E1 : E) {
        for (int y = 0; y < E.size(); y++) {

            //System.out.println("E SIze " + E.size() + " " + y + " " + E.get(y).getEmpNumber());

            if (employeeNumber.getText().trim().equals(E.get(y).getEmpNumber())) {
                System.out.println(E.get(y).getEmpNumber());
                empValid = true;
                en = E.get(y).getEmpNumber();
                fn = E.get(y).getNameF();
                ln = E.get(y).getNameL();
                enID = E.get(y).getEmpID();
                enGprob = E.get(y).getGProb();
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


    private boolean checkBDAY1(String d) {
        showErrorClear();
        boolean t = false;
        try {
            IIN.checkNumbers(d);
        } catch (Exception e) {
            showError("This Field can only be Numbers" + e);
        }
        if (GETDAY.testMonth(Integer.parseInt(bday1Textfield.getText())) == false) {
            showError("MONTH of the Birthday fields cannot be less then 0 nor greater then 12");
            return t;
        } else {
            if (bday1Textfield.getText().length() == 1) {
                bday1Textfield.setText(GETDAY.getTwoDigitMonthDay(bday1Textfield.getText()));
            }
            t = true;
        }
        return t;
    }

    private boolean checkBDAY2(String d) {
        showErrorClear();
        boolean t = false;
        try {
            IIN.checkNumbers(d);
        } catch (Exception e) {
            showError("This Field can only be Numbers" + e);
        }
        if (GETDAY.testDay(Integer.parseInt(bday2Textfield.getText())) == false) {
            showError("DAY of the Birthday fields cannot be less then 0 nor greater then 31");
        } else {
            if (bday2Textfield.getText().length() == 1) {
                bday2Textfield.setText(GETDAY.getTwoDigitMonthDay(bday2Textfield.getText()));
            }
            t = true;
        }
        return t;
    }

    private boolean checkBDAY3(String d) {
        showErrorClear();
        boolean t = false;
        try {
            IIN.checkNumbers(d);
        } catch (Exception e) {
            showError("This Field can only be Numbers" + e);
        }
        if (GETDAY.testYear(bday3Textfield.getText()) == false) {
            showError("YEAR of the Bithday fields connot be less then four(4) digits nor greater then four (4) digits");
        } else {
            t = true;
        }
        return t;
    }

    private boolean testYearDate() {
        boolean t = false;
        String bdayString = bday1Textfield.getText() + "/" + bday2Textfield.getText() + "/" + bday3Textfield.getText();
        if (GETDAY.testDateValidity(bdayString)) {
            t = true;
        }
        return t;
    }

    private void setToUpper() {
        showErrorClear();

        fNameTextfield.textProperty().addListener((ov, oldValue, newValue) -> {
            fNameTextfield.setText(newValue.toUpperCase());
        });
        lNameTextfield.textProperty().addListener((ov, oldValue, newValue) -> {
            lNameTextfield.setText(newValue.toUpperCase());
        });
        addressTextfield.textProperty().addListener((ov, oldValue, newValue) -> {
            addressTextfield.setText(newValue.toUpperCase());
        });
        aptTextFeild.textProperty().addListener((ov, oldValue, newValue) -> {
            aptTextFeild.setText(newValue.toUpperCase());
        });
        cityTextfield.textProperty().addListener((ov, oldValue, newValue) -> {
            cityTextfield.setText(newValue.toUpperCase());
        });
        stateTextfield.textProperty().addListener((ov, oldValue, newValue) -> {
            stateTextfield.setText(newValue.toUpperCase());
        });
        emailTextfield.textProperty().addListener((ov, oldValue, newValue) -> {
            emailTextfield.setText(newValue.toUpperCase());
        });
        zipTextfield.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (zipTextfield.getText().length() >= 5) {
                    zipTextfield.setText(zipTextfield.getText().substring(0, 5));
                }
            }
        });
        bday1Textfield.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (bday1Textfield.getText().length() >= 2) {
                    bday1Textfield.setText(bday1Textfield.getText().substring(0, 2));
                }
            }
        });
        bday2Textfield.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (bday2Textfield.getText().length() >= 2) {
                    bday2Textfield.setText(bday2Textfield.getText().substring(0, 2));
                }
            }
        });
        bday3Textfield.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (bday3Textfield.getText().length() >= 4) {
                    bday3Textfield.setText(bday3Textfield.getText().substring(0, 4));
                }
            }
        });
        phone3Textfield.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (phone3Textfield.getText().length() >= 4) {
                    phone3Textfield.setText(phone3Textfield.getText().substring(0, 4));
                }
            }
        });
        phone1Textfield.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (phone1Textfield.getText().length() >= 3) {
                    phone1Textfield.setText(phone1Textfield.getText().substring(0, 3));
                }
            }
        });
        phone2Textfield.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (phone2Textfield.getText().length() >= 3) {
                    phone2Textfield.setText(phone2Textfield.getText().substring(0, 3));
                }
            }
        });

        zipTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                new IsItANumber().checkNumbers(newValue);
            } catch (Exception e) {
                mbox.showError("This Field can only be Numbers", errorLabel, errorBar);
                zipTextfield.setText("");
                zipTextfield.requestFocus();
            }
            if (!CF.testPhone(4, newValue)) {
                showError("This Field must have five digits");
                return;
            } else {
                showErrorClear();
            }
            /*if (newValue.length() == 5) {
                bday1Textfield.requestFocus();
            }*/
        });

        card1Field.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                new IsItANumber().checkNumbers(newValue);
            } catch (Exception e) {
                mbox.showError("This Field can only be Numbers", errorLabel, errorBar);
                card1Field.setText("");
                card1Field.requestFocus();
            }
        });
        card2Field.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                new IsItANumber().checkNumbers(newValue);
            } catch (Exception e) {
                mbox.showError("This Field can only be Numbers", errorLabel, errorBar);
                card2Field.setText("");
                card2Field.requestFocus();
            }
        });

    }

    public void addPhonefieldListeners() {
        phone1Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        IIN.checkNumbers(newValue);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "This Field can only be Numbers");
                        phone1Textfield.setText("");
                        phone1Textfield.requestFocus();
                        return;
                    }
                    if (!CF.testPhone(1, newValue)) {
                        showError("This Field must have three digits");
                        //phone1Textfield.requestFocus();
                        return;
                    } else {
                        showErrorClear();
                    }
                    if (newValue.length() > 3) {
                        phone1Textfield.setText(phone1Textfield.getText().substring(0, 3));
                        //phone1Textfield.setText(oldValue);
                    }
                }
        );
        phone2Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        IIN.checkNumbers(newValue);
                    } catch (Exception e) {
                        mbox.showError("This Field can only be Numbers", errorLabel, errorBar);
                        //JOptionPane.showMessageDialog(null, "This Field can only be Numbers");
                        phone2Textfield.setText("");
                        phone2Textfield.requestFocus();
                        return;
                    }
                    if (!CF.testPhone(2, newValue)) {
                        showError("This Field must have three digits");
                        //phone1Textfield.requestFocus();
                        return;
                    } else {
                        showErrorClear();
                    }
                    if (newValue.length() > 3) {
                        phone2Textfield.setText(phone2Textfield.getText().substring(0, 3));
                    }
                }
        );
        phone3Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        IIN.checkNumbers(newValue);
                    } catch (Exception e) {
                        mbox.showError("This Field can only be Numbers", errorLabel, errorBar);
                        phone3Textfield.setText("");
                        phone3Textfield.requestFocus();
                        return;
                    }
                    if (!CF.testPhone(3, newValue)) {
                        showError("This Field must have four digits");
                        //phone1Textfield.requestFocus();
                        return;
                    } else {
                        showErrorClear();
                    }
                    if (newValue.length() > 4) {
                        phone3Textfield.setText(phone3Textfield.getText().substring(0, 4));
                    }
                }
        );
        employeeNumber.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        IIN.checkNumbers(newValue);
                    } catch (Exception e) {
                        mbox.showError("This Field can only be Numbers", errorLabel, errorBar);
                        employeeNumber.setText("");
                        employeeNumber.requestFocus();
                        return;
                    }
                    /*if (!cf.testPhone(3, newValue)) {
                        showError("This Field must have four digits");
                        //phone1Textfield.requestFocus();
                        return;
                    } else {
                        showErrorClear();
                    } */
                    if (newValue.length() > 1) {
                        postButton.setDisable(false);
                    } else {
                        postButton.setDisable(true);
                    }
                }
        );
        ticketsTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                new IsItANumber().checkNumbers(newValue);
            } catch (Exception e) {
                mbox.showError("This Field can only be Numbers", errorLabel, errorBar);
                ticketsTextfield.setText("");
                ticketsTextfield.requestFocus();
            }
            if (newValue.length() > 0) {
                postButton.setDisable(false);
            } else {
                postButton.setDisable(true);
            }
        });

    }

    public void makeAllEdit() {
        for (Node node : gridPane1.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setStyle("-fx-opacity: 1.0;");
                ((TextField) node).setDisable(false);
            }
        }
        for (Node node : gridPane3.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setStyle("-fx-opacity: 1.0;");
                ((TextField) node).setDisable(false);
            }
        }
        //cardNumberTextfield.setStyle("-fx-background-color: Yellow");
        //cardNumberTextfield.setDisable(true);
        fNameTextfield.requestFocus();
    }

    private void makeAllNoEdit() {
        for (Node node : gridPane1.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setStyle("-fx-opacity: 1.0;");
                ((TextField) node).setDisable(true);
            }
        }
        for (Node node : gridPane3.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setStyle("-fx-opacity: 1.0;");
                ((TextField) node).setDisable(true);
            }
        }

    }

    private void clearFields() {
        Set<Node> nodes = root.lookupAll(".text-field");
        for (Node node : nodes) {
            ((TextField) node).setText("");

        }
    }

    public void highlightTextField() {

        Set<Node> nodes = root.lookupAll(".text-field");
        for (Node node : nodes) {
            if (node.isFocused()) {
                node.setStyle("-fx-background-color: Yellow");
            } else {
                node.setStyle("-fx-background-color: White");
            }
        }
    }

    private void setHighLights() {
        showErrorClear();
        fNameTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        lNameTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        addressTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        aptTextFeild.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        cityTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        stateTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        zipTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        stateTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        bday1Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        bday2Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        bday3Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        phone1Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        phone2Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        phone3Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        emailTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });
        employeeNumber.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                highlightTextField();
            }
        });

    }

    private void showErrorClear() {
        new messageBox().showErrorClear(errorLabel, errorBar);
    }

    private void showError(String error) {
        new messageBox().showError(error, errorLabel, errorBar);
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
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

    
    private String getReceipts(String n) {
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

    private void setrNumber(String r) {
        
        this.rNumber = r;
    }
    
    private String getrNumber() {
        return this.rNumber;
    }
    

    
    
    private void setBottomPane() {
        showErrorClear();
        stageV = (Stage) cancelButton.getScene().getWindow();
        stageV.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            //System.out.println("mouse click detected! " + mouseEvent.getSource());
            enterKeyPressed();
       });

        //card1Field.clear();
        //card2Field.clear();
        topPane.setVisible(false);
        bottomPane.setVisible(true);
        phone1Textfield.setText("208");
        cityTextfield.setText("BOISE");
        stateTextfield.setText("ID");
        postButton.setVisible(true);
        postButton.setDisable(true);
        fNameTextfield.requestFocus();
        //setCardNumber();
        //cardNumberTextfield.setText(cn);
        cardNumberTextfield.setDisable(true);
        cardNumberTextfield.setStyle("-fx-background-color: #7e7e7e");
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
                postButton.fire();
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
            case F12:
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case ENTER:
                enterKeyPressed();
                break;
            case DOWN:
                break;
            case TAB:
                enterKeyPressed();
                break;
            default:
                break;
        }
    }

    private void getStageV() {
        stageV = (Stage) ticketsTextfield.getScene().getWindow();
    }

    public void setCardNumberFromController(String n) {
        cardNumberTextfield.setText(n);
    }

    

    private void setEmp() {
        //System.out.println(this.pCNN.getCard1());
        
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        en = null;
        fn = null;
        ln = null;
        vAmt = 0;
        bal = 0.0;
        pw = null;
        cn = null;
        m = null;
        if (!SCLMT) {
            sc.setActivity("1");
        }

        try{
            cCardNumbers.clear();
        } catch(Exception e) {System.out.println(e + "ffffffffffffffffff");}
        db.disConnect();
        stageV.close();
        
    }

}
