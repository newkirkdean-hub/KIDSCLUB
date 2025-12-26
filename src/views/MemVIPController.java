package views;

import JavaMail.Mail_JavaFX1;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import commoncodes.GetReceipts;
import commoncodes.IsItANumber;
import commoncodes.IsItANumber1;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;
import pReceipts.print;
import pWordFX.empFX;
import pWordFX.employeeFX;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import messageBox.messageBox;
import models.club.CheckBalanceDB;
import models.club.LastMemberTransaction;
import models.club.rCeipts;
import models.toys.Toys;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;
import views.counterPopUp.MemberUpdatePrintController;
import views.counterPopUp.lastMemberTransactionViewController;



public class MemVIPController implements Initializable {
    @FXML private PasswordField CCNumberIn;
    @FXML private TextField VIPSpecialsField;
    @FXML private TextField scanField;
    @FXML private PasswordField empNumber;
    @FXML private Button doButton;
    @FXML private Button fullListLMTButton;
    @FXML private Button popUpButton;
    @FXML private Button showHistoryButton;
    @FXML private Label customerNameLabel;
    @FXML private Label customerAddressLabel;
    @FXML private Label scrollTextLable;
    @FXML private Label errorLabel;
    @FXML private Pane errorPane;
    @FXML private TextField bonusTextfield;
    @FXML private AnchorPane root;
    @FXML private VBox customerInfoBox;
    @FXML private Stage stageV;
    
    @FXML private TableView<Memtick> SalesTable;
    @FXML private TableColumn<Memtick, LocalDate> dateColumn;
    @FXML private TableColumn<Memtick, String> ItemNameColumn;
    @FXML private TableColumn<Memtick, Double> bonusColumn;
    @FXML private TableColumn<Memtick, String> timeColumn;




    public static employeeFX eFX;
    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    public static DB db;
    public static ArrayList<empFX> E;
    public static ArrayList<String> V;
    public static ArrayList<rCeipts> rCeipts;
    //public static VIPPromos v;
    public static messageBox mBox;
    public static Mail_JavaFX1 jmail;
    public static CheckBalanceDB chkBalance;
    public static Member m;
    public static Memtick mt;
    public static PrintWriter pw;
    public static int VIPMultiplier;
    public static String css;
    public static FXMLLoader FXLOADER;
    public static ArrayList<LastMemberTransaction> lastmember;
    public static int iLastTran;
    
    private static final DecimalFormat DF = new DecimalFormat("#");
    private static final IsItANumber IIN = new IsItANumber();
    
    //private static String msg = null;
    //private static String tMessage = "";
    private static LocalDate returnvalue = LocalDate.now();
    //private static double newBalanceCHK = 0.0;
    private static empFX newEFX;
    private static String rCiept = "", rCieptB = "", tranTime = "";
    private static Double newBalance = 0.0, prevBalance = 0.0;
    public static String en, fn, ln,  rNumber,  isEMail = "";
    private static int rCeipt;
    private static Double layoutX, layoutY;
    private static String theChoosenSpecial;
    private static boolean emailIsTrue = false, SCLMT = false;
    private static Window owner;
    private static boolean getTodaysVIPDoneFromTable, isFP;
    private static ObservableList<Memtick> topics = null;

    private static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    private static final DecimalFormat df = new DecimalFormat("#");
    private static final DateTimeFormatter MYDATEFORMATTERR = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private static final DateTimeFormatter MYDATEFORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final GetReceipts getR = new GetReceipts();
    private static final char THISPAGECHAR = 4;
    private static Style Format = new Style();
    

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        layoutX = errorPane.getLayoutX();
        layoutY = errorPane.getLayoutY();
        tranTime = new GetDay().getCurrentTimeStamp();
        //System.out.println(layoutX + " " + layoutY);
        showHistoryButton.setVisible(false);
        popUpButton.setVisible(false);
        VIPSpecialsField.setEditable(false);
        scrollTextLable.setVisible(false);
        addTextfieldListeners();
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("DateTime"));
        dateColumn.setCellFactory(column -> {
            return new TableCell<Memtick, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(MYDATEFORMATTER.format(item));
                    }
                }
            };
        });

        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("Bonus"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));
        ShowTable(1);
        root.getStylesheets().add(css);
        setKeyCodes();        
        setHighlightListenerVIP(root);
        doButton.setDisable(true);
        mBox.showErrorClear(errorLabel, errorPane);
        System.out.println("This is iLastTran " + iLastTran + " " + lastmember.get(0).getLastLastName());
        
        Platform.runLater(()->CCNumberIn.requestFocus());  
    }    
    
    private void ShowTable(int whichOne){
        switch (whichOne) {
            case 1:
                SalesTable.getItems().clear();
                doButton.setDisable(true);
                SalesTable.setVisible(false);
                customerInfoBox.setVisible(false);
                break;
            case 2:
                Task<Void> task = new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        SalesTable.getItems().clear();
                        try {
                            SalesTable.getItems().addAll(db.getGames(m.getCID()));
                            topics = SalesTable.getItems();
                        } catch (SQLException ex) {
                            System.out.println(ex);
                        }
                        SalesTable.setVisible(true);
                        customerInfoBox.setVisible(true);
                        return null;
                    }
                };
                new Thread(task).start();
                break;
        }
    }
    
    public void showHistoryButtonPushed() {
        ShowTable(2);
    }
    
    
    ///LAST MEMBER -------------------------------------------------------------
    ///PUT THIS IN THE DOBUTTON CODE AT THE END OF THE TRANSACTION - setLastMemberVariables();
    ///private static boolean emailIsTrue = false, SCLMT = false;
       
    public void CCNClicked(MouseEvent me) {
        me.consume();
        System.out.println("Mouse event = " + me.getButton().SECONDARY);
        if (me.getClickCount() == 2) {
            getLastTransactionMemberVIP();
            return;
        }
        MouseButton mb = me.getButton();
        if (mb == MouseButton.SECONDARY) {
           fullListLMTButton.fire();
        }
    }

    private void setLastMemberVariables() {
        sc.setCCN(m.getCCN());
        System.out.println("here is en " + en + " " + fn);
        sc.setEmployee(en);
        sc.setEmpName(fn);
        sc.setActivity("VIP");
        SCLMT = true;
        sc.setLastName(m.getNameL());
        System.out.println(m.getCCN() + " " + en + " " + fn + " " + m.getNameL());

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
            boolean GO = isEMPValidInArrayList(sc.getGameNumber());
            if (newEFX.getBcarsales() != 1) {
                return;
            }
            lastMemberTransactionViewController wController = (lastMemberTransactionViewController) FXLOADER.getController();
            wController.sc = sc;
            wController.dbsp = dbsp;
            wController.cssPath = css;
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

    //public void getFullListLastMemberTransactions(ActionEvent event) throws IOException {
        
        
    //}
    
    
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
        mBox.showErrorClear(errorLabel, errorPane);
        switch (id) {
            case "CCNumberIn":
                mBox.showErrorLayoutXY("Please scan or Type in the Customer Card Number, press ENTER or touch the next field", errorLabel, errorPane, layoutX, layoutY);
                break;
            case "scanField":
                if (!customerInfoBox.isVisible()) {
                    getMemberInfo();
                }
                //mBox.showError("ENTER 0 (ZERO) for list of all Promos", errorLabel, errorPane);
                mBox.showErrorLayoutXY("ENTER 0 (ZERO) for list of all Promos", errorLabel, errorPane, 99.00, 275.00);
                break;
            case "bonusTextfield":
                if (!customerInfoBox.isVisible()) {
                    getMemberInfo();
                }
                if (scanField.getText().trim().isEmpty()) {
                    scanField.requestFocus();
                } else {
                    VIPSpecialsField.requestFocus();
                    //popUpButton.fire();
                }
                //mBox.showErrorClear(errorLabel, errorPane);
                mBox.showErrorClearLayoutXY(errorLabel, errorPane, layoutX, layoutX);
                break;
            case "VIPSpecialsField":
                if (!customerInfoBox.isVisible()) {
                    getMemberInfo();
                }
                if (scanField.getText().trim().isEmpty()) {
                    scanField.requestFocus();
                } else {
                    if (Double.parseDouble(scanField.getText().trim()) > 500) {
                        new messageBox().showAlert(Alert.AlertType.ERROR, null, "Stop", "The purchase amount is too High, please find a supervisor");
                        scanField.setText("");
                        scanField.requestFocus();
                    } else {
                        bonusTextfield.setText(String.valueOf(db.calcVIPPoints(Double.parseDouble(scanField.getText()), VIPMultiplier)));
                        VIPSpecialsField.requestFocus();
                        //popUpButton.fire();
                        empNumber.requestFocus();
                    }
                }
                mBox.showError("Select the Promotion from the popUpBox. Press Select three Times", errorLabel, errorPane);
                break;
            case "empNumber":
                if (!customerInfoBox.isVisible()) {
                    getMemberInfo();
                }
                if (scanField.getText().trim().isEmpty()) {
                    scanField.requestFocus();
                } else {
                    if (Double.parseDouble(scanField.getText().trim()) > 500) {
                        mBox.showAlert(Alert.AlertType.ERROR, null, "Stop", "The purchase amount is too High, please find a supervisor");
                        scanField.setText("");
                        scanField.requestFocus();
                    } else {
                        if (bonusTextfield.getText().trim().isEmpty()) {
                            bonusTextfield.setText(String.valueOf(db.calcVIPPoints(Double.parseDouble(scanField.getText()), VIPMultiplier)));
                            VIPSpecialsField.requestFocus();
                            //popUpButton.fire();
                            empNumber.requestFocus();
                        } else {
                            reCalcBonus();
                            empNumber.requestFocus();
                        }
                    }
                }
                //empNumber.requestFocus();
                //System.out.println("here");
                //mBox.showError("Type in Your Employee Number then press the Save Button", errorLabel, errorPane);
                mBox.showErrorLayoutXY("Type in Your Employee Number then press the Save Button", errorLabel, errorPane, 0.0, 619.00);
                break;
        }
    }

    public void doButtonPushed() {
        if (!customerInfoBox.isVisible()) {
            getMemberInfo();
        }
        if (scanField.getText().trim().isEmpty()) {
            scanField.requestFocus();
            return;
        }
        if (bonusTextfield.getText().trim().isEmpty()) {
            bonusTextfield.setText(String.valueOf(db.calcVIPPoints(Double.parseDouble(scanField.getText()), VIPMultiplier)));
        }
        reCalcBonus();

        en = empNumber.getText().trim();
        if (en.isEmpty()) {
            mBox.showAlert(Alert.AlertType.ERROR, null, "Employee Number", "Employee Number cannot be blank()");
            empNumber.clear();
            empNumber.requestFocus();
            return;
        } else {
            try {
                if (!loginButtonPushed()) {
                    mBox.showAlert(Alert.AlertType.ERROR, null, "Employee Number", "Employee Number Not Found");
                    empNumber.clear();
                    empNumber.requestFocus();
                    return;
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        String fInitialsFinal = null;
        doButton.setDisable(true);
        doButton.setVisible(false);
        setLastMemberVariables();
        String fInitials = VIPSpecialsField.getText();
        int e = getPropertiesFile();
        switch (e) {
            case 1:
            case 2:
            case 3:
            case 4:
                fInitialsFinal = "B";
                break;
            case 5:
            case 6:
                fInitialsFinal = "C";
                break;
        }

        //PART1 FULL NAME OF ITEM EX. "FIRST PURCHASE", PART 2 INITIALS EX. FP, PART3 MULTIPLYER EX 1   
        ArrayList<String> list = makepartslist();
        if (list.size() > 0) {
            for (String list1 : list) {
                String[] r = list1.split(",");
                if (r[0].trim().equals(fInitials.trim())) {
                    fInitialsFinal = r[1].trim();
                    //System.out.println(r[0] + " " + r[1] + " " + r[2] + " " + r[6].trim() + " " + r[8].trim());
                    setrCeipt(Integer.parseInt(r[6].trim()));
                    rCiept = r[7];
                    rCieptB = r[8];
                    break;
                } else {
                    fInitialsFinal = getfInitialsFinal();
                }
            }
        } else {
            fInitialsFinal = getfInitialsFinal();
        }
        //fInitialsFinal  ARE THE INITIALS GOING IN THE MEMTICK TABLE UNDER THE LOCATION COLUMN.

        
        mt = new Memtick(m.getCID(), en, new GetDay().getCurrentTimeStamp(), LocalDate.now(), 0.0, Double.parseDouble(bonusTextfield.getText().trim()), 0.0, fInitialsFinal, 0);
        prevBalance = ((double)m.getBalance());
        
        newBalance = prevBalance  + mt.getAdded() + mt.getBonus();

            if (!db.insertDataTicketBalanceCombined(mt, newBalance)) {
                System.out.println("error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)");
                jmail.sendEmailTo("Error Depositing Tickets", "error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)" + " " + dbsp.localMachine, "errors");
            } else {
             
            runcheckBalance(m, newBalance, mt, en, fn, ln); //RUNCHECKBALANCE HAS ITS OWN MODEL IN MODELS
         // jmail.sendEmailTo("Member VIP", "This si sthe last VIP Transaction" + " " + m.getCCN() + " " + m.getNameL() + " " + en + " " + dbsp.localMachine, "errors");
            Platform.runLater(() -> {
                try {
                    //printReceipt(newBalance, mt);
                    if (!getIsEmailTrue()) {
                        printEscPosVIP(newBalance, mt, rCiept, rCieptB);
                        //printReceipt(newBalance, mt, rCiept, rCieptB);
                    }
                    db.disConnect();
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                } catch (PrintException ex) {
                    Logger.getLogger(MemVIPController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MemVIPController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            
                
                
                /*
                try {
                    printReceipt(newBalance, mt, rCiept, rCieptB);
                    db.disConnect();
                    exitButtonPushed();
                } catch (FileNotFoundException ex) {
                    System.out.println("error printing Deposit receipt (See MemDepositFXController)\n" + ex);
                    new prtReceiptsFX().prtError("error printing Deposit receipt (See MemDepositFXController)\n" + ex);

                }
                */
            }
        Platform.runLater(() -> exitButtonPushed());
    }
    
    
    
    
    
     private void runcheckBalance(Member m, Double newBalance, Memtick mt, String empN, String fName, String lName) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                getReceipts("GreaterThanDepWith");
                String gt = getrNumber();
                try {
                    chkBalance.checkBalance(m, newBalance, mt);
                    //if (m.getCCN().equals("4000001") || m.getCCN().equals("4000008")) {
                                String EmailAdd1 = getReceipts("EmailAdd1");
                                if (getrNumber().equals("1.0")) {
                                    
                                } else {
                                    EmailAdd1 = "";
                                }

                        chkBalance.sendMemberEmail(m, mt, newBalance, prevBalance, fn, EmailAdd1, "V", null, VIPSpecialsField.getText().trim(), isEMail, rCeipts);
                    //}
                } catch (SQLException ex) {
                    System.out.println(ex);
                    jmail.sendEmailTo("Error Balance Check", "Error from Deposit: " + ex + " " + dbsp.localMachine, "errors");
                }
                /*
                try {
                    new CheckBalanceDB().RunTenThousand(m, newBalance, mt, empN, fName, lName, "DEPOSIT", gt, E);
                    } catch (SQLException | FileNotFoundException ex) {
                        new Mail_JavaFX1().sendEmailTo("Error in GTTT Deposit Process", ex.toString() + ex + " " + empN + " " + fName + " " + lName + " " + new dbStringPath().localMachine, "error");
                }
                */
            }
        };
        thread1.start();
    }    
    
    
    
    

    private void reCalcBonus() {
        if (!VIPSpecialsField.getText().trim().equals("No Promo Selected") && !VIPSpecialsField.getText().trim().equals("No Promos Available")) {
            if (getVIPMultiplier(getSpecial()) > 0) {
                bonusTextfield.setText(String.valueOf(DF.format(getVIPMultiplier(getSpecial()) * Double.parseDouble(scanField.getText().trim()))));
                System.out.println("Here in recalbonus multiplyer" );
            }else{
                bonusTextfield.setText(String.valueOf(DF.format(getVIPTickets(getSpecial()))));
                System.out.println("Here in recalbonus tickets" +  getVIPTickets(getSpecial()));
            }
            empNumber.requestFocus();
        }
    }
    
    
    
    private int getVIPMultiplier(String n) {
        int MMultiplier2 = 0;
        //for (empFX E1 : E) {
        if (V.size() > 0) {
            for (String list1 : V) {
                String[] parts = list1.split(",");
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2].trim();
                String part4 = parts[3].trim();
                //System.out.println("rrrrrrrr" + part4 + " " + part3 + " " + part2 + " " + part1);
                if (parts[0].equals(n)) {
                    MMultiplier2 = Integer.parseInt(part4.trim());
                   // JOptionPane.showMessageDialog(null, parts[0] + " " + MMultiplier2);
                }

            }
    }
        return MMultiplier2;
    }
    
    private int getVIPTickets(String n) {
        int MMultiplier3 = 0;
        //for (empFX E1 : E) {
        if (V.size() > 0) {
            for (String list1 : V) {
                String[] parts = list1.split(",");
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2].trim();
                String part4 = parts[3].trim();
                String part5 = parts[4].trim();
                //System.out.println(n + " " + parts[0]);
                //System.out.println("rrtttttt" + part5 + " " + part3 + " " + part2 + " " + part1);
                if (parts[0].equals(n)) {
                    MMultiplier3 = Integer.parseInt(part5.trim());
                   // JOptionPane.showMessageDialog(null, parts[0] + " " + MMultiplier2);
                }

            }
    }
        //System.out.println(MMultiplier3);
        return MMultiplier3;
    }
    
    //BEGINING OF VIP PROMOS CODE
    //GET MEMBER FILL THE TABLE WITH THE HISTORY
    //CALL THE POPUP BOX FILL THE POPUP BOX WITH THE ITEMS AVAILBLE STILL FOR THE CUSTOMER TODAY AND HISTORICLY
        //IF TABLE EMPTY THEN A CALL TO FILL ALL PROMOS
        //OTHERWISE A CALL TO AVAILABLE PROMOS AND A CALL TO MAKE A LIST
    //SELECT AN ITEM IN THE POPBOX THEN CALLS FOR A RECALC OF THE BONUS.
    
    
    public int getPropertiesFile() {
        dbsp.setName();
        int p = 0;
        File file = new File(new dbStringPath().pathNameLocal + "configCounterFXML.properties");
        if (file.exists() && file.canRead()) {
            p = Integer.parseInt(new settingsFXML().getCounterSettings("stage"));
        }
        return p; 
    }

    public String getfInitialsFinal() {
        String fInitialsFinal = "";
        int k = getPropertiesFile();
        switch (k) {
            case 0:
            case 1:
            case 3:
                fInitialsFinal = "B";
                break;
            case 2:
            case 4:
            case 5:
                fInitialsFinal = "C";
                break;
        }
        return fInitialsFinal;
    }

    private void setSpecial(String b) {
        this.theChoosenSpecial = b;
    }

    private String getSpecial() {
        return this.theChoosenSpecial;
    }

    public void getPopUPFX(ActionEvent event) throws IOException {
        while (!SalesTable.isVisible()) {
            //scanField.clear();
        }
        //VIPSpecialsField.setText("VIPSpecial");
        VIPSpecialsField.setText("No Promo Selected");
        Bounds boundsInScene = VIPSpecialsField.localToScene(VIPSpecialsField.getBoundsInLocal());
        if (!getTableSize()) {
            if (fillAllPromos()) {
                sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "VIPFile", "Select One:", VIPSpecialsField.getText(), boundsInScene.getMaxX(), boundsInScene.getMinY());
                System.out.println(sc.getGameNumber());
                if (!sc.getGameNumber().equals("Number")) {
                    VIPSpecialsField.setText(sc.getGameNumber());
                    setSpecial(sc.getGameNumber());
                    reCalcBonus();
                    empNumber.requestFocus();
                } else {
                    VIPSpecialsField.setText("No Promo Selected");
                    empNumber.requestFocus();
                }
            } else {
                VIPSpecialsField.setText("Promos Failed");
                jmail.sendEmailTo("VIPSpecials Failed", "THe VIP Promo search brought back no promos. and Says FAILED." + " " + dbsp.localMachine, "errors");
            }
        } else {
            if (fillAvailablePromos()) {
                //JOptionPane.showConfirmDialog(null, "we are in fillAvailbepromos");
                if (VIPSpecialsField.isFocused()) {
                    sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "VIPFile", "Select One:", VIPSpecialsField.getText(), boundsInScene.getMaxX(), boundsInScene.getMinY());
                System.out.println(sc.getGameNumber());
                    if (!sc.getGameNumber().equals("Number")) {
                        //System.out.println("here in getpopupFX " + sc.getGameNumber());
                        VIPSpecialsField.setText(sc.getGameNumber());
                        setSpecial(sc.getGameNumber());
                        reCalcBonus();
                        empNumber.requestFocus();
                    } else {
                        VIPSpecialsField.setText("No Promo Selected");
                        VIPSpecialsField.setEditable(false);
                        empNumber.requestFocus();
                    }
                }
            } else {
                VIPSpecialsField.setText("No Promos Available");
                VIPSpecialsField.setEditable(false);
                empNumber.requestFocus();
            }
        }
        empNumber.requestFocus();
        mBox.showError("Type in your employee Number then Press the Save Button", errorLabel, errorPane);
    }

    private void makeLocalList(String s, ArrayList<String> listToTextFile, int whichOne) {
        switch (whichOne) {
            case 1:
                try {
                    pw = new PrintWriter(new File(dbsp.pathNameLocal + "VIPLocal.txt"));
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                }
                pw.println(s);
                pw.close();
                break;
            case 2:
                try {
                    pw = new PrintWriter(new File(dbsp.pathNameLocal + "VIPLocal.txt"));
                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                }
                for (int z = 0; z < listToTextFile.size(); z++) {
                    pw.println(listToTextFile.get(z));
                }
                pw.close();
        }
    }

    //check only if activites have been done today
    private boolean getTodaysVIPDoneFromTable(String s, int x) throws FileNotFoundException, SQLException {
        getTodaysVIPDoneFromTable = false;
        LocalDate today = LocalDate.now();
        //LocalDate returnvalue = today.minusDays(x);
        if (x != 998) {
            returnvalue = today.minusDays(x);        
        } else {
            returnvalue = today.plusDays(1);        
        } 
        topics = SalesTable.getItems();
        if (topics.size() > 0) {
            ObservableList<Memtick> localTable = db.getGames(m.getCID());
            //System.out.println("List of table items is greater then 0");
            localTable.forEach((Memtick each) -> {
                if (s.trim().equals(each.getLocation().trim())) {
                    //System.out.println("in location " + each.getLocation() + " " + s + " " + returnvalue + " " + each.getDateTime());
                    if (x < 0) {
                        //System.out.println("x was < 0");
                        getTodaysVIPDoneFromTable = true;
                    } 
                    if (each.getDateTime().isEqual(returnvalue)) {
                        //System.out.println("in date " + each.getDateTime() + " date equals today");
                        getTodaysVIPDoneFromTable = true;
                    }
                    if (each.getDateTime().isAfter(returnvalue)) {
                        //System.out.println("in date " + each.getDateTime() + " date is after returndate");
                        getTodaysVIPDoneFromTable = true;
                    }
                }
            });
        } 
            //System.out.println("True or False " + getTodaysVIPDoneFromTable);
            return getTodaysVIPDoneFromTable;
    }


    private boolean checkFP(String s, int x) throws FileNotFoundException, SQLException {
        isFP = false;
        LocalDate today = LocalDate.now();
        LocalDate returnvalue = today.minusDays(x);
        topics = SalesTable.getItems();
        if (topics.size() > 0) {
            ObservableList<Memtick> localTable = db.getGames(m.getCID());
            //System.out.println("List of table items is greater then 0");
            localTable.forEach((Memtick each) -> {
                if (s.trim().equals(each.getLocation().trim())) {
                    //System.out.println("in location " + each.getLocation() + " " + s + " " + returnvalue + " " + each.getDateTime());
                    if (x < 0) {
                        //System.out.println("x was < 0");
                        isFP = true;
                    } 
                    if (each.getDateTime().isEqual(returnvalue)) {
                        //System.out.println("in date " + each.getDateTime() + " date equals today");
                        isFP = true;
                    }
                    if (each.getDateTime().isAfter(returnvalue)) {
                        //System.out.println("in date " + each.getDateTime() + " date is after returndate");
                        isFP = true;
                    }
                }
            });
        } 
            //System.out.println("checkFP is True or False " + isFP);
            return isFP;
    }
    
    
    //if the member has done an activity
    private boolean fillAvailablePromos() {
        boolean t = false;
        int i = 0;
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> listToTextFile = new ArrayList<>();
        list = V;
        if (list.size() > 0) {
            for (String list1 : list) {
                String[] parts = list1.split(",");
                String part1 = parts[0]; //Name
                String part2 = parts[1]; //Initials
                String part3 = parts[2].trim(); //Repeat
                String part4 = parts[3].trim(); //Multiplyer
                String part5 = parts[4]; //Tickets
                String part6 = parts[5]; //Item
                String part7 = parts[6]; //Receipt
                
                    //System.out.println("part1 (Name): " + part1 + " part2 (Initials):"+ part2 + " part3 (Repeat): " + part3 + " part4 (Multiplyer): " + part4 + " part5 (Tickets): " + part5 + " part6 (Item): " + part6);
                try {
                    if (checkFP("FP", 0)) {
                      //System.out.println("checkFP was false");  
                        if (Double.parseDouble(scanField.getText())> 0) {
                            //System.out.println("Here is where we make a list that if purcahse is greater then 0 then give list else fill with other items " + list1);
                            i=0;
                        } else {
                            if (!getTodaysVIPDoneFromTable(part2, Integer.parseInt(part3) - 1)) {
                                //listToTextFile.add(list1);
                                listToTextFile.add(part1);
                                //System.out.println("Here is list one that is approved for the list " + list1);
                                i++;
                            }
                        }
                    } else {
                        if (!getTodaysVIPDoneFromTable(part2, Integer.parseInt(part3) - 1)) {
                            //listToTextFile.add(list1);
                            listToTextFile.add(part1);
                            //System.out.println("Here is list one that is approved for the list " + list1);
                            i++;
                        }
                    }
                } catch (FileNotFoundException | SQLException ex) {
                    System.out.println(ex);
                }
            } 
                if (i > 0) {
                        makeLocalList("", listToTextFile, 2);
                    t = true;
                } else {
                    makeLocalList("", null, 1);
                    t = false;
                }
        } else { 
            makeLocalList("", null, 1);
            t = false;
        }
        return t;
    }

    private ArrayList<String> makepartslist() {
        ArrayList<String> list = new ArrayList<>();
        //try {
            //list = db.getAllPromos();
            list = V;
        //} catch (SQLException ex) {
        //    Logger.getLogger(MemVIPController.class.getName()).log(Level.SEVERE, null, ex);
        //}
        if (list.size() > 0) {
            for (String list1 : list) {
                String[] parts = list1.split(",");
                String part1 = parts[0];
                String part2 = parts[1];
                String part3 = parts[2].trim();
                String part4 = parts[6];
                
                //HERE WE NEED TO ADD A 4TH ITEM IN THE PARTS LIST SO THAT WE CAN NOT RECALL THE DB FOR THE 
                
                //PART1 FULL NAME OF ITEM EX. "FIRST PURCHASE", PART 2 INITIALS EX. FP, PART3 MULTIPLYER EX 1
                //System.out.println("parts " + part1 + " " + part2 + " " + part3 + " " + part4);

            }
        }
        return list;
    }

    private boolean getTableSize() {
        boolean tablesize = false;
        if (topics.size() > 0) {
            tablesize = true;
        }
        return tablesize;
    }

    //if the member has not ever done one of the activities
    //YOU LEFT OFF HERE
    private boolean fillAllPromos() {

        boolean t = false;
        int i = 0;
        ArrayList<String> list = new ArrayList<>();
        try {
            pw = new PrintWriter(new File(dbsp.pathNameLocal + "VIPLocal.txt"));
            //list = db.getAllPromos();
            list = V;
        } catch (FileNotFoundException ex) {
        //} catch (SQLException ex) {
            System.out.println(ex);
        }
        if (list.size() > 0) {
            for (String list1 : list) {
                String[] parts = list1.split(",");
                String part1 = parts[0];
                //String part2 = parts[1];
                //String part3 = parts[2].trim();
                pw.println(part1);
                i++;
                t = true;
            }
        }
        pw.close();
        return t;
    }
    
    
    
    //END OF VIP PROMOS GET AND SET.
    
    
    private void setEmailTrue(boolean t) {
        this.emailIsTrue = t;
    }
    
    private boolean getIsEmailTrue() {
        return this.emailIsTrue;
    }
    
    
    private void printReceipt(Double n, Memtick mt, String rCiept, String rCieptB) throws FileNotFoundException {
        try {
        pw = new PrintWriter(new File(dbsp.pathNameLocal + "VIPMember.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        pw.println(getReceipts("CoName")); // to test if it works.
        String printthis = getReceipts("SubCoHeading");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Address");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Address2");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Phone");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("WWW");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        pw.println("VIP Member Receipt"); // to test if it works.
        pw.println("======================"); // to test if it works.
        pw.println();
        pw.println(" ");
        pw.println("Employee: " + fn);
        pw.println("Date: " + MYDATEFORMATTERR.format(LocalDateTime.now()));
        pw.println("");
        pw.println("");

        pw.println("Puchase Amount: $" + scanField.getText().trim());
        pw.println("");
        pw.println("Prev Balance" + ":" +  "\t" + DF.format(prevBalance));
        pw.println("Points Added" +  ":" + "\t" + DF.format(mt.getBonus()));
        pw.println("New Balance" + ":" +  "\t" + DF.format(n));
        pw.println("");
        pw.println("");
        if (!VIPSpecialsField.getText().trim().equals("No Promos Available")) {
            pw.println("VIP Promo Used:");
            pw.println(VIPSpecialsField.getText().trim());
            pw.println("");
        }
        printthis = getReceipts("Footer1");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer2");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer3");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer4");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer5");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber())>=Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis); this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        pw.println(" ");
        if (scrollTextLable.isVisible()) {
            pw.println(" ");
            pw.println(" ");
            pw.println("You have an Email and we have");
            pw.println("sent a receipt to this Email Address");
            pw.println(isEMail.toUpperCase());
            pw.println(" ");
        }
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
        pr.printReceipt("VIPMember.txt");
        
        if (getrCeipt() == 1 || getrCeipt() == 2) {
            try {
                pw = new PrintWriter(new File(dbsp.pathNameLocal + "VIPTILL.txt"));
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }

            pw.println("CAFE RECIEPT"); // to test if it works.
            pw.println("======================"); // to test if it works.
            pw.println();
            pw.println(" ");
            pw.println("Employee: " + fn);
            pw.println("Date: " + MYDATEFORMATTERR.format(LocalDateTime.now()));
            pw.println("");
            pw.println(rCieptB);

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
            
            //print pr = new print();
            pr.printReceipt("VIPTILL.txt");
            int e =  mBox.confirmNextReciept(Alert.AlertType.ERROR, null, "Alert", "Take reciept then click NEXT RECIEPT Button or type the SPACE BAR");
            System.out.println("=========================" + e);
            //if (e>0) {
                //System.out.println("9999999999999999999999");
            //}
        }

        if (getrCeipt() == 2 || getrCeipt() == 3) {
            try {
                pw = new PrintWriter(new File(dbsp.pathNameLocal + "VIPCAFE.txt"));
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }

            pw.println("BRIDGE RECIEPT"); // to test if it works.
            pw.println("======================"); // to test if it works.
            pw.println();
            pw.println(" ");
            pw.println("Employee: " + fn);
            //pw.println("Date: " + myDateFormatter.format(LocalDate.now()) + " " + LocalTime.now());
            pw.println("Date: " + MYDATEFORMATTERR.format(LocalDateTime.now()));
            pw.println(" ");
            pw.println(" ");
            pw.println(rCiept);

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
            
            //print pr = new print();
            pr.printReceipt("VIPCAFE.txt");
            //new messageBox4().confirmNextReciept(Alert.AlertType.ERROR, null, "Alert", "Take reciept then click NEXT RECIEPT Button or type the SPACE BAR");

        }
        



    }
    
    
    
    
       private void printEscPosVIP(Double n, Memtick mt, String rCiept, String rCieptB)  throws PrintException, IOException{
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
            githubBufferedImage = ImageIO.read(new File(dbsp.pathNameImages + "/ReceiptLogo/vipLogo.png"));
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
        
        
        
        

      
        

        //PRINT RECEIPT HEADING (WITHDRAWAL)
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Center);
        escpos.write(Format, "VIP RECEIPT");
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
        

        //PRINT $PURCHASE AMOUNT
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Puchase Amount: $" + scanField.getText().trim());
        escpos.feed(2);        


        //PRINT PREV BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Prev Balance" + ":" + "\t" + df.format(prevBalance));
        escpos.feed(1);        
        

        //PRINT WITHDRAWAL AMOUNT
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Points Added" + ":" + "\t" + df.format(mt.getBonus()));
        escpos.feed(1);        

        //PRINT NEW BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "New Balance" + ":" + "\t" + df.format(n));
        escpos.feed(2);        
        

           if (!VIPSpecialsField.getText().trim().equals("No Promos Available")) {
               //PRINT PROMO TYPE
               Format = new Style()
                       .setFontName(Style.FontName.Font_A_Default)
                       .setFontSize(Style.FontSize._1, Style.FontSize._1)
                       .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                       .setJustification(EscPosConst.Justification.Center);
               escpos.write(Format, "VIP PROMO:");
               escpos.feed(1);
               Format = new Style()
                       .setFontName(Style.FontName.Font_A_Default)
                       .setFontSize(Style.FontSize._1, Style.FontSize._1)
                       .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                       .setJustification(EscPosConst.Justification.Center);
               escpos.write(Format, VIPSpecialsField.getText().trim());
               escpos.feed(1);
           }

           
           
           if (getrCeipt() == 1 || getrCeipt() == 2) {
               //PRINT SECOND RECEIPT FOR VIP
               escpos.feed(5);
               Format = new Style()
                       .setFontName(Style.FontName.Font_A_Default)
                       .setFontSize(Style.FontSize._1, Style.FontSize._1)
                       .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                       .setJustification(EscPosConst.Justification.Center);
               escpos.write(Format, "CAFE RECIEPT");
               escpos.feed(3);

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

               //PRINT SECOND RECEIPT INFO
               Format = new Style()
                       .setFontName(Style.FontName.Font_A_Default)
                       .setFontSize(Style.FontSize._1, Style.FontSize._1)
                       .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                       .setJustification(EscPosConst.Justification.Center);
               escpos.write(Format, rCieptB);
               escpos.feed(6);

           }

        if (getrCeipt() == 2 || getrCeipt() == 3) {
               //PRINT SECOND RECEIPT FOR VIP
               escpos.feed(5);
               Format = new Style()
                       .setFontName(Style.FontName.Font_A_Default)
                       .setFontSize(Style.FontSize._1, Style.FontSize._1)
                       .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                       .setJustification(EscPosConst.Justification.Center);
               escpos.write(Format, "BRIDGE RECIEPT");
               escpos.feed(3);

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

               //PRINT SECOND RECEIPT INFO
               Format = new Style()
                       .setFontName(Style.FontName.Font_A_Default)
                       .setFontSize(Style.FontSize._1, Style.FontSize._1)
                       .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                       .setJustification(EscPosConst.Justification.Center);
               escpos.write(Format, rCiept);
               escpos.feed(6);
            
        }

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
        
        
        // ================================================END OF FOOTERS =============================================
        
         
       
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public static String padRight(String s, int n) {
     return String.format("%1$-10s", s);
    }
    
    public static String padLeft(String s, int n) {
     return String.format("%1$" +n+ "s", s);
    }
    
    
    
     private String getReceipts(String n) {
        String StringItem = null;
        for (int y = 0; y < rCeipts.size(); y++) {
            if (n.trim().equals(rCeipts.get(y).getrItem())) {
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
    

     private void setrCeipt(int r) {
        this.rCeipt = r;
    }
    
    private int getrCeipt() {
        return this.rCeipt;
    }



    
    
    public void addTextfieldListeners() {

        scanField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        //new IsItANumber().checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Amount of Deposit can Only be Numbers");
                        scanField.clear();
                        scanField.requestFocus();
                        return;
                    }
                }
        );
        CCNumberIn.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        IIN.checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Member Card Number can Only be Numbers");
                        CCNumberIn.clear();
                        CCNumberIn.requestFocus();
                        return;
                    }
                }
        );
        empNumber.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        IIN.checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "EMployee Number can Only be Numbers");
                        empNumber.clear();
                        empNumber.requestFocus();
                        return;
                    }
                    if (empNumber.getText().length() > 0) {
                        doButton.setDisable(false);
                    } else {
                        doButton.setDisable(true);
                    }
                }
        );
        VIPSpecialsField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //System.out.println("newvalue " + newVal);
                if (VIPSpecialsField.getText().trim().isEmpty()) {
                   popUpButton.fire();
                }
            } 
        });
    }
    
    
    
    
    
    
    
    private void getMemberInfo() {
        if (!db.isMemberPojos(CCNumberIn.getText())) {
            mBox.showAlert(Alert.AlertType.ERROR, null, "NoValid Number", "This Number is a non usable Number.");
            CCNumberIn.clear();
            return;
        }

        try {
            //m = new Member(CCNumberIn.getText());
            //if (db.isMemberValid(m.getCCN())) {
            m = db.getMember(CCNumberIn.getText());
            if (!m.getNameF().equals("inValid")) {
                customerInfoBox.setVisible(true);
                if (m.getNameL().equals("FASTPASS")) {
                    customerNameLabel.setText(m.getNameF());
                } else {
                    customerNameLabel.setText(m.getNameF() + " " + m.getNameL());
                }
                customerAddressLabel.setText(m.getAddress());
                isEMail = db.getEmail(m.getCID());
                try {
                if (isEMail.length() > 1){
                    scrollTextLable.setVisible(true);
                    String printthis = getReceipts("ClubPaperReceipt");
                    if (getrNumber().equals("1.0")) {
                        scrollTextLable.setText("Email and Paper Receipt");
                        setEmailTrue(false);
                        this.setrNumber("");
                    } else {
                        scrollTextLable.setText("Email Receipt Only");
                        setEmailTrue(true);
                        this.setrNumber("");
                    }                }
                } catch (Exception e) {}

                if (m.getBalance() < 0) {
                     jmail.sendEmailTo("Customer Balance Negative", m.getBalance() + " " + m.getCCN(), "errors");
                }                    
            } else {
                //new messageBox2().showErrorLayoutXY("Searching Inactive Members", errorLabel, errorPane, 100.0, 65.0);
                //mBox.showError("Searching Inactive Members", errorLabel, errorPane);
                mBox.showAlert(Alert.AlertType.INFORMATION, owner, "Searching", "Searching Inactive Members");
                if (!db.isInactiveMemberValid(CCNumberIn.getText())) {
                    db.disConnect();
                    mBox.showErrorClearLayoutXY(errorLabel, errorPane, layoutX, layoutY);
                    mBox.showAlert(Alert.AlertType.INFORMATION, owner, "No Such Number", "Member Number Not Found");
                    CCNumberIn.clear();
                    CCNumberIn.requestFocus();
                    return;
                } else {
                //new messageBox2().showErrorLayoutXY("Inactive Member Found", errorLabel, errorPane, 100.0, 65.0);
                    //mBox.showAlert(Alert.AlertType.INFORMATION, stageV, "Inacvtive Member Found", "This member has been found in the Inactive File and has been transfered back to the active file.");
                    getMemberInfo();
                    //mBox.showErrorClearLayoutXY(errorLabel, errorPane, layoutX, layoutX);
                }
                //db.disConnect();
                //mBox.showAlert(Alert.AlertType.INFORMATION, owner, "No Such Number", "Member Number Not Found");
                //CCNumberIn.clear();
                //CCNumberIn.requestFocus();
                //return;
            }
        } catch (HeadlessException | SQLException e) {
            mBox.showAlert(Alert.AlertType.ERROR, owner, "Database search Error", e.toString());
            CCNumberIn.clear();
            CCNumberIn.requestFocus();
            return;
        }
        Platform.runLater(()->ShowTable(2));
        showHistoryButton.setVisible(false);
        scanField.requestFocus();
        
        
        //THIS IS DONE NOW IN VIP.. 2/17/24
        if (m.getAddress().equals("WRONG ADDRESS")) {
            if (!db.Check10Thousand(m.getCID(), new GetDay().asSQLDate(LocalDate.now()), "UpDPrint")) {
                GetMemberUpdatePrint();            
            }
        }
        
        
    }
    
    
    
    
    private void GetMemberUpdatePrint() {
        MemberUpdatePrintController wController = (MemberUpdatePrintController) FXLOADER.getController();
        wController.empName = m.getNameF() + " " + m.getNameL();
        wController.cssPath = css;
        wController.m = m;
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
            sc.getpassWord(stageV, "/views/counterPopUp/MemberUpdatePrint.fxml", "", css, 300.0, 300.0);
        } catch (IOException ex) {
           System.out.println(ex);
        }
        //SC.goToScene("EditBalance", stageV, EFX.getNameF(), null, boundsInScenememButton);

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

    
    
    
    
    
    

    public void enterKeyPressed() {
        if (CCNumberIn.isFocused()) {
            getMemberInfo();
            return;
        }
        
        if (scanField.isFocused()) {
            //if (Integer.parseInt(scanField.getText().trim()) > 500) {
            if (Double.parseDouble(scanField.getText().trim()) > 500) {
                mBox.showAlert(Alert.AlertType.ERROR, null, "Stop", "The purchase amount is too High, please find a supervisor");
                scanField.setText("");
                return;
            } else {
                bonusTextfield.setText(String.valueOf(db.calcVIPPoints(Double.parseDouble(scanField.getText()), VIPMultiplier)));
                VIPSpecialsField.requestFocus();
                //popUpButton.fire();
                return;
            }
        } // end of scanfield
        
        if (VIPSpecialsField.isFocused() && VIPSpecialsField.getText().isEmpty()) {
            //popUpButton.fire();
            //System.out.println("here in enter button VIP Special Focus");
            reCalcBonus();
            empNumber.requestFocus();
            return;
        } // end of scanfield
        
        if (empNumber.isFocused()) {
            try {
                if (!loginButtonPushed()) {
                    empNumber.clear();
                    empNumber.requestFocus();
                    new messageBox().showAlert(Alert.AlertType.ERROR, owner, "No Number", "No Employee Number Found");
                    return;
                } else {
                    setEmp();
                    empNumber.setDisable(true);
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
    
    private void getStageV() {
        stageV = (Stage) doButton.getScene().getWindow();
    }
    
    
    private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        //for (empFX E1 : E) {
        for (int y = 0; y < E.size(); y++) {

            //System.out.println("E SIze " + E.size() + " " + y + " " + E.get(y).getEmpNumber());

            if (n.equals(E.get(y).getEmpNumber())) {
                //System.out.println(E.get(y).getEmpNumber());
                empValid = true;
                en = E.get(y).getEmpNumber();
                fn = E.get(y).getNameF();
                ln = E.get(y).getNameL();
                newEFX = new empFX(E.get(y).getNameF(), E.get(y).getNameL(), E.get(y).getEmpNumber(), E.get(y).getVAmt(), E.get(y).getBdayresos(), E.get(y).getChangerEdit(), E.get(y).getEmpID(), E.get(y).getGProb(), E.get(y).getActive(), E.get(y).getArcade(), E.get(y).getBcarsales());
                System.out.println("here is en 2 " + E.get(y).getEmpNumber() + " here is n " + n + "here is en " + en);
            }
        }
        return empValid;
    }
    
    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        GO = isEMPValidInArrayList(empNumber.getText().trim());
        return GO;
    }
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F1) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F3) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F4) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.TAB) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.UP) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.DOWN) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ENTER) {keyListener(ke); ke.consume();}
     });   
    }
    
    private void setEmp(){ //set employee number in the empTextfield
        //empTextfield.setText(eFXX.empNumber);
        //empTextfield.setDisable(true);
        //empTextfield.setStyle("-fx-background-color: #7e7e7e;");
        //JOptionPane.showMessageDialog(null, eFX.empNumber);
        en =eFX.empNumber;
        fn = eFX.nameF;
        ln = eFX.nameL;
        //vAmt = eFXX.VAmt;
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: doButton.fire(); break;
                    case F7: break;
                    case F8: break;
                    case F9: break;
                    case F10: break;
                    case F11: break;
                    case TAB: enterKeyPressed(); break;
                    case DOWN: popUpButton.fire(); break;
                    case UP: break;
                    case ESCAPE: exitButtonPushed(); break;
                    case ENTER: enterKeyPressed(); break;
                default:
                    break;
                }
    } 
        
    public void exitButtonPushed() {
        if (SalesTable.isVisible()) {
            topics.clear();
            SalesTable.getItems().clear();
        }
        db.disConnect();
        try{
            eFX.Flush();
            SalesTable.getItems().clear();
            topics.clear();
        } catch(Exception e) {}
        if (!SCLMT) {
            sc.setActivity("1");
        }
            SCLMT = false;
            stageV = (Stage) CCNumberIn.getScene().getWindow();
            stageV.close();
    } 
    
    
    //check for all activites and if they are REPEATABLE
    //THE BELOW ITEM IS OBSOLETE 6/2020
    /*
    private boolean havetheyaverdoneit(String s) throws SQLException {
        havetheyaverdoneit = false;
        ObservableList<Memtick> localTable = db.getGames(m.getCID());
        topics = SalesTable.getItems();
        if (topics.size() > 0) {
            localTable.forEach((Memtick each) -> {
                if (s.trim().equals(each.getLocation().trim())) {
                    havetheyaverdoneit = true;
                }
            });
        }
        return havetheyaverdoneit;
    }
    */
}
