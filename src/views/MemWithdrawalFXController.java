package views;

import views.counterPopUp.PinNumberPopUpController;
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
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
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
import models.club.Member;
import models.club.Memtick;
import models.toys.PutToysDB;
import models.toys.Toys;
import models.toys.ToysDB;
import models.club.rCeipts;
import pReceipts.print;
import pWordFX.empDB;
import pWordFX.empFX;
import sceneChangerFX.SceneChanger_Main;
import views.counterPopUp.lastMemberTransactionViewController;



public class MemWithdrawalFXController implements Initializable {
    @FXML private PasswordField CCNumberIn;
    //@FXML private TextField depositField;
    @FXML private TextField totalwithdrawalField;
    @FXML private TextField remainingField;
    @FXML private TextField scanField;
    @FXML private PasswordField empNumber;
    @FXML private Button doButton;
    @FXML private Button fullListLMTButton;
    @FXML private Label customerNameLabel;
    @FXML private Label scrollTextLable;
    @FXML private Label customerAddressLabel;
    @FXML private Label customerBalanceLabel;
    @FXML private Label totalwithdrawalLabel;
    @FXML private Label remainingLabel;
    //@FXML private Label scanFieldLabel;
    //@FXML private Label empLabel;
    @FXML private AnchorPane root;
    @FXML private VBox customerInfoBox;
    @FXML private Stage stageV;
    
    @FXML private TableView<Toys> SalesTable;
    @FXML private TableColumn<Toys, Integer> QTYColumn;
    @FXML private TableColumn<Toys, String> ItemNameColumn;
    @FXML private TableColumn<Toys, Integer> TotalColumn;
    //@FXML private TableColumn<Toys, String> IDColumn;

    private static final ToysDB toysdb = new ToysDB();
    private static final PutToysDB putToysDB = new PutToysDB();
    public static ArrayList<LastMemberTransaction> lastmember;
    public static int iLastTran;
   

    public static dbStringPath dbsp;
    public static ArrayList<rCeipts> rCeipts;
    public static empFX empFX;
    public static messageBox mBox; 
    public static String cssPath;
    public static ArrayList<empFX> E;
    public static ArrayList<Toys> l;
    public static SceneChanger_Main sc;
    public static DB db;
    public static Mail_JavaFX1 jmail;
    public static CheckBalanceDB chkBalance;
    public static FXMLLoader fxloader;
    public static Member m;
    public static Memtick mt;
    public static PrintWriter pw;


    public static String en, eID,  fn, ln, CID, q, rNumber, rWinner, isEMail = "", notes="", tranTime = "";
    private static ObservableList<Toys> listMemberSalesItems = null;
    private static List list = new ArrayList();

    private static int customerBalance;
    private static Double newBalance = 0.0, prevBalance = 0.0;
    private static Window owner;
    private static boolean emailIsTrue = false, getSuper = false, postWithDrawalNotes = false;
    //private static String tMessage = "";
    private int printSecondReceipt = 0, toyListIndex = 0;
    private static boolean remove, update, SCLMT = false;
    private static Bounds boundsInScene;
    
    private static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    private static final DecimalFormat df = new DecimalFormat("#");
    private static final DateTimeFormatter MYDATEFORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter MYDATEFORMATTERR = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private static final GetReceipts getR = new GetReceipts();
    private static final char THISPAGECHAR = 2;
    private static Style Format = new Style();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = root.localToScene(root.getBoundsInLocal());
        dbsp.setName();
        getSuper = false;
        tranTime = new GetDay().getCurrentTimeStamp();
        postWithDrawalNotes = false;
        scrollTextLable.setText("Wait . . .");
        scrollTextLable.setVisible(false);
        addTextfieldListeners();
        SetToUpper();
        ItemNameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TotalColumn.setCellValueFactory(new PropertyValueFactory<>("Tickets"));
        QTYColumn.setCellValueFactory(new PropertyValueFactory<>("QTY"));
        //IDColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));
        ShowTable(1);
        root.getStylesheets().add(cssPath);
        setKeyCodes();    
        new FocusedTextFieldHighlight().setHighlightListener(root);
        doButton.setDisable(true);
        setNotes("");
        setHighlightListenerVIP(root);
        notes = "";
        Platform.runLater(()->CCNumberIn.requestFocus()); 

    }    
    
    
    private void runII() {
        Iterator<rCeipts> tvShowIterator = rCeipts.iterator();
        while (tvShowIterator.hasNext()) {
            rCeipts tvShow = tvShowIterator.next();
            System.out.println(tvShow.getrItem() + ", " + tvShow.getrString());
        }
    }

    
    private void runI() {
        Iterator<Toys> tvShowIterator = l.iterator();
        while (tvShowIterator.hasNext()) {
            Toys tvShow = tvShowIterator.next();
            System.out.println(tvShow.getNumber());
        }
    }
    
    
  
    private void hideButton() {
        if (doButton.isVisible()) {
            doButton.setDisable(true);
            doButton.setVisible(false);
        } else {
            doButton.setVisible(true);
        }
    }

    private void setTimer() {
        PauseTransition wait = new PauseTransition(Duration.ZERO);
        wait.setOnFinished((e) -> {
            System.out.println("Running timer");
            playText();
            hideButton();
            //wait.play();
            wait.stop();
        });
        wait.play();
    }

    public void doButtonPushed() {
        doButton.setDisable(true);
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
        Platform.runLater(() -> rWinner = db.RandomWinner("Withdrawal"));
        prevBalance = ((double) m.getBalance());
        mt = new Memtick(m.getCID(), en, tranTime, LocalDate.now(), 0.0, 0.0, Double.parseDouble(totalwithdrawalField.getText().trim()), "", 0);
        newBalance = prevBalance - mt.getSubtracted();
        setLastMemberVariables();
        if (!db.insertDataTicketBalanceCombined(mt, newBalance)) {
            System.out.println("error while in withdrawal insertDataTicketBalanceCombined (See MemwithdrawalFXController)");
            jmail.sendEmailTo("Error withdrawing Tickets", "error while in withdrawal insertDataTicketBalanceCombined (See MemwithdrawalFXController)" + "\n" + dbsp.localMachine, "errors");
        } else {
            //CheckBalance(m, newBalance, mt, en, fn, ln);
            //System.out.println("Item Number " + listMemberSalesItems.get(1).getItemID());
            //listMemberSalesItems = SalesTable.getItems();
            Platform.runLater(() -> putSales(listMemberSalesItems, eID, m.getCID(), tranTime));
            if (postWithDrawalNotes) {
                Platform.runLater(() -> GoPostWithdrawalNotes(en, fn, tranTime));                                
            }
            Platform.runLater(() ->  CheckBalance(m, newBalance, mt, en, fn, ln));
            if (!getIsEmailTrue()) {
                Platform.runLater(() -> {
                    try {
                        printEscPosWITHDRAWAL(newBalance, mt);
                    } catch (PrintException | IOException ex) {
                        System.out.println(ex);
                    }
                });
                //Platform.runLater(() -> printReceipt(newBalance, mt));
            }
            //Platform.runLater(() -> updatePinNumber(m.getCID(), m.getCID(), eID));
        }
        Platform.runLater(()->exitButtonPushed());
    }
    
    
    private void updatePinNumber(String CCN, String cID, String eID) {
        //PutToysDB td = new PutToysDB();
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                db.updatePinNumberData(CCN, cID, eID);
            }
        };
        thread1.start();
        putToysDB.DisConnect();
    }

    
    
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
        sc.setActivity("WITHDRAW");
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
            if (empFX.getBcarsales() != 1) {
                return;
            }
            lastMemberTransactionViewController wController = (lastMemberTransactionViewController) fxloader.getController();
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

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void putSales(ObservableList<Toys> listMemberSalesItems, String empID, String CID, String tranTime) {
        //Thread thread1 = new Thread() {
         //   @Override
         //   public void run() {
                putToysDB.PutSalesItems(listMemberSalesItems, empID, CID, tranTime, list);
         //   }
        //};
        //thread1.start();
        putToysDB.DisConnect();
    }

    private void CheckBalance(Member m, Double newBalance, Memtick mt, String empN, String fName, String lName) {
        //PutToysDB td = new PutToysDB();
        Thread thread2 = new Thread() {
            @Override
            public void run() {
                getReceipts("GreaterThanDepWith");
                String gt = getrNumber();
                //td.PutSalesItems(listMemberSalesItems, empID, CID, tranTime);
                chkBalance.RunTenThousandWithdrawal(m, newBalance, mt, empN, fName, lName, "WHITHDRAWAL", gt, rCeipts);
                    //if (m.getCCN().equals("4000001") || m.getCCN().equals("4000008")) {
                                String EmailAdd1 = getReceipts("EmailAdd1");
                                if (getrNumber().equals("1.0")) {
                                    
                                } else {
                                    EmailAdd1 = "";
                                }

                        chkBalance.sendMemberEmail(m, mt, newBalance, prevBalance, fn, EmailAdd1, "W", listMemberSalesItems, null, isEMail, rCeipts);
                    //}
            }
        };
        thread2.start();
        //td.DisConnect();
    }

    
    private void setEmailTrue(boolean t) {
        this.emailIsTrue = t;
    }
    
    private boolean getIsEmailTrue() {
        return this.emailIsTrue;
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

    public void addTextfieldListeners() {
        /*
        scanField.textProperty().addListener(
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
        */
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
        empNumber.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber1().checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Employee Number can Only be Numbers");
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
    }

    private void getMemberInfo() {
        if (!db.isMemberPojos(CCNumberIn.getText())) {
            mBox.showAlert(Alert.AlertType.ERROR, null, "NoValid Number", "This Number is a non usable Number.");
            CCNumberIn.clear();
            return;
        }
        try {
            m = db.getMember(CCNumberIn.getText());
            try {
                if (!m.getpinNumber().isEmpty()) {
                    q = SetAPinNumber(CCNumberIn.getText());
                    if ("CANCEL".equals(q)) {
                        Platform.runLater(() -> CCNumberIn.clear());
                        Platform.runLater(() -> CCNumberIn.requestFocus());
                        q = "";
                        return;
                    } else {
                        //THIS IS WHERE WE NEED TO VALIDATE THE PIN NUMBER 8/9/2020  
                        /*if (!db.ValidatePinUser(q, CCNumberIn.getText())) {
                            Platform.runLater(() -> CCNumberIn.clear());
                            Platform.runLater(() -> CCNumberIn.requestFocus());
                            System.out.println("4444444 " + q);
                            q = "";
                            return;
                        }*/
                    }

                }
            } catch (Exception e) {
            }

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
                    if (isEMail.length() > 1) {
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
                        }

                    }
                } catch (Exception e) {
                }

                if (m.getBalance() < 0) {
                    jmail.sendEmailTo("Customer Balnace Negative", m.getBalance() + " " + m.getCCN() + "\n" + dbsp.localMachine, "errors");
                    customerBalanceLabel.setText(String.valueOf(m.getBalance()));
                    scanField.requestFocus();
                } else {
                    customerBalance = m.getBalance();
                    customerBalanceLabel.setText(String.valueOf(m.getBalance()));
                    scanField.requestFocus();
                }
            } else {
                if (db.isInactiveMemberValid(CCNumberIn.getText())) {
                    mBox.showAlert(Alert.AlertType.INFORMATION, owner, "Member Found", "This Member was found in the inactive file. Please scan card again.");
                    CCNumberIn.clear();
                    CCNumberIn.requestFocus();
                } else {
                    db.disConnect();
                    mBox.showAlert(Alert.AlertType.INFORMATION, owner, "No Such Number", "Member Number Not Found in the ACTIVE and the INACTIVE \n files. Please have a Supervisor do a search.");
                    CCNumberIn.clear();
                    CCNumberIn.requestFocus();
                }
            }
        } catch (HeadlessException | SQLException e) {
            mBox.showAlert(Alert.AlertType.ERROR, owner, "Database search Error", e.toString());
            jmail.sendEmailTo("Error in getMember() in withdrawal.", "Database search Error " + e.toString() + "\n" + dbsp.localMachine, "errors");
            CCNumberIn.clear();
            CCNumberIn.requestFocus();
        }
        db.disConnect();
    }

    private String SetAPinNumber(String CCN) throws IOException {
        PinNumberPopUpController wController = (PinNumberPopUpController) fxloader.getController();
        wController.typeVar = "2";
        wController.empID = String.valueOf(eID);
        wController.CCN = CCN;
        sc.getpassWord(stageV, "/views/counterPopUp/PinNumberPopUp.fxml", eID, tranTime, 200.0, 200.0);
        //sc.goToScene("AddPinNumber", stageV, null, null, boundsInScene);
        q = sc.getGameNumber();
        return q;

    }

    private String getQTY() {
        //String q = "";
        getStageV();
        Bounds boundsInScene2 = doButton.localToScene(doButton.getBoundsInLocal());
        try {
            sc.getpassWord(stageV, "/views/counterPopUp/QTY.fxml", "0", "Enter QTY:", boundsInScene2.getMinX() + 350, boundsInScene2.getMinY() - 100);
            q = sc.getGameNumber();
            //System.out.println("here we are right after the 1 = sc.getGameNumber() " + q);
        } catch (IOException ex) {
            System.out.println("Here is wheere we get the number format error: " + ex);
        }
        return q;
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

    public Toys getToyFromArrayList(String n, int q) {
        Toys toyList = null;

        // HERE IS WHERE I STARTED
        //for (Toys l1 : l) {
        //System.out.println("HERE IN GETTOYSFROMARRAYLIST() " + l.size());
        for (int y = 0; y < l.size(); y++) {
            if (n.equals(l.get(y).getNumber())) {
                System.out.println(q + " " +  l.get(y).getName() + " " +  l.get(y).getTickets()+ " " + l.get(y).getNumber() + " " + l.get(y).getTag());
                toyList = new Toys(q, l.get(y).getName(), l.get(y).getTickets(), l.get(y).getNumber(), l.get(y).getTag());
            }
        }

        // HERE IS WHERE I ENDED
        return toyList;
    }

    public void enterKeyPressed() {
        if (CCNumberIn.isFocused()) {
            getMemberInfo();
            if (l != null) {
                //System.out.println("L was not null");
                //toysdb.connectLocal();
            } else {
                //System.out.println("L Size less then 1");
                l = toysdb.getList();
            }
            if (E != null) {
                //System.out.println("E was not null");
            } else {
                //System.out.println("E Size less then 1");
                E = new empDB().getList();
            }
            return;
        }

        if (scanField.isFocused()) {
            if (!SalesTable.isVisible() || !scanField.getText().trim().isEmpty()) {
                scanFeildDO();
            } else {
                empNumber.requestFocus();
            }
        }

        if (empNumber.isFocused()) {
            try {
                if (!empNumber.getText().trim().isEmpty()) {
                if (!loginButtonPushed()) {
                    empNumber.clear();
                    empNumber.requestFocus();
                    mBox.showAlert(Alert.AlertType.ERROR, owner, "No Number", "No Employee Number Found");
                    return;
                } else {
                    empNumber.setDisable(true);
                    doButton.requestFocus();
                    return;
                }
                } //else {
                   // return;
                //}
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        if (doButton.isFocused()) {
            doButton.fire();
            Platform.runLater(() -> doButton.setDisable(true));
            scanField.requestFocus();
        }

    }
    
      private ObservableList getTableList(Toys t) {
        if (remove){
            //UPDATE DOES THE WORK OF THIS REMOVE CODE, SO IT IS NOT GOING TO BE USED!!
            int currentQTY = listMemberSalesItems.get(toyListIndex).getQTY();
            int removeQTY = t.getQTY();
        }
        if (update){
            int newQTY = listMemberSalesItems.get(toyListIndex).getQTY() + t.getQTY();
            int newticketsTimesNewQTY = t.getTickets() * t.getQTY();
            int newTickets = listMemberSalesItems.get(toyListIndex).getTickets() + newticketsTimesNewQTY;
            list.set(toyListIndex, new Toys(newQTY, t.getName(), newTickets, t.getNumber(), 0));
            listMemberSalesItems = FXCollections.observableList(list);
            update = false;
            return listMemberSalesItems;
        }
        if (!remove || !update){
            int newticketsTimesNewQTY = t.getTickets() * t.getQTY();
            list.add(new Toys(t.getQTY(), t.getName(), newticketsTimesNewQTY, t.getNumber(), 0));
            listMemberSalesItems = FXCollections.observableList(list);
        }
            return listMemberSalesItems;
    }
    
    private void ShowTable(int whichOne) {
        switch (whichOne) {
            case 1:
                SalesTable.getItems().clear();
                doButton.setDisable(true);
                SalesTable.setVisible(false);
                totalwithdrawalLabel.setVisible(false);
                totalwithdrawalField.setVisible(false);
                customerInfoBox.setVisible(false);
                remainingField.setVisible(false);
                remainingLabel.setVisible(false);
                break;
            case 2:
                
                //System.out.println(E.get(5).getNameF());
                ///SalesTable.getItems().clear();
                ///try {
                    SalesTable.setItems(listMemberSalesItems);
                    ///SalesTable.getItems().addAll(tdb.getGames());
                ///} catch (SQLException ex) {
                ///   System.out.println(ex);
                ///}
                 {
                    //try {
                        totalwithdrawalField.clear();
                        int total = SalesTable.getItems().stream().mapToInt(Toys::getTickets).sum();
                        totalwithdrawalField.setText(String.valueOf(total));
                        
                        //int total = 0;
                        //for (Toys item : SalesTable.getItems()) {
                        //    total += item.getTickets(); // Assuming Item has a getPrice() method  
                        // }
                        //totalwithdrawalField.setText(String.valueOf(total));
                        //totalwithdrawalField.setText(String.valueOf(toysdb.getTotal()));
                        remainingField.setText(String.valueOf(customerBalance - Integer.parseInt(totalwithdrawalField.getText())));
                    //} catch (SQLException ex) {
                    //    System.out.println(ex);
                    //}
                }
                
                //doButton.setDisable(false);
                SalesTable.setVisible(true);
                totalwithdrawalLabel.setVisible(true);
                totalwithdrawalField.setVisible(true);
                customerInfoBox.setVisible(true);
                remainingField.setVisible(true);
                remainingLabel.setVisible(true);
                break;
        }
    }

    
    private void scanFeildDO() {
        //System.out.println("HERE RIGHT beginning of scanFeildDO()");                        
        //if (!SalesTable.isVisible() || !scanField.getText().trim().isEmpty()) {
            remove = false;
            update = false;
            String totalwithdrawal = "0";
                //System.out.println("-9-9-9-9-9-9-9-9-9-9- ");                        
            try {
                if (isToyValidInArrayList(scanField.getText().trim())) {
                    //System.out.println("HERE RIGHT before getQTY");                        
                    q = getQTY();
                    //System.out.println("HERE RIGHT AFTER GETQTY(), HERE IS WHAT THE NUMBER LOOKS LIKE " + Double.valueOf(q));                        
                    if (q.equals("Number")) {
                        mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Quantity can not be 0 (Zero)");
                        returnToScanField();
                        return;
                    }
                    if (isToyValidInArrayList(q)) {
                        mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Invalid Number for Quantity! \n Your Number: " + q);
                        returnToScanField();
                        return;
                    }
                    Toys t = getToyFromArrayList(scanField.getText().trim(), Integer.parseInt(q));
                    if (t.getNumber().equals(q)) {
                        mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Quantity can not be the same as the item number of the prize");
                        returnToScanField();
                        return;
                    }
                    if (Integer.parseInt(q) < 0) {
                        ObservableList<Toys> Toylistremove = SalesTable.getItems();
                        Toys tlist = getToyFromArrayList(t.getNumber(), 1);
                        for(int i= 0; i<Toylistremove.size();i++) {
                            if (Toylistremove.get(i).getName().equals(tlist.getName())) {
                                System.out.println("this is where the item is :" + i + " tlist.getQTY() = " + Toylistremove.get(i).getQTY() +  " " + q);
                                toyListIndex = i;
                                remove = true;
                                //Toylistremove.clear();
                            }
                        }
                        if (remove) {
                            if (Integer.parseInt(q)*-1 > Toylistremove.get(toyListIndex).getQTY()) {
                                mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Invalid Number Type for Quantity \n Your Number: " + q);
                                returnToScanField();
                                return;
                            }
                        } else {
                            mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Invalid Number Type for Quantity \n Your Number: " + q);
                            returnToScanField();
                            return;
                        }
                    }
                    if (Integer.parseInt(q) > 50000) {
                        mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Invalid Number Type for Quantity \n Your Number: " + q);
                        returnToScanField();
                        return;
                    }
                } else {
                    mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Not a Valid Item Number " + scanField.getText().trim());
                    returnToScanField();
                    return;
                }
                
                Toys t = getToyFromArrayList(scanField.getText().trim(), Integer.parseInt(q));
                String maxTickets = getReceipts("WithdrawalMax");
                System.out.println("===================================== maxTickets " + maxTickets);
                if (!t.getName().equals("ErrorFailed")) {
                    if (totalwithdrawalField.isVisible()){
                        totalwithdrawal = totalwithdrawalField.getText();                        
                    }
                   //System.out.println("1 GOING INTO ISTOOMANYTICKETS NAME: " + t.getName() + " ITEM NUMBER: " + t.getNumber() + " bALANCE: " + m.getBalance());
                    if (toysdb.IsTooManyTickets(t, totalwithdrawal, m.getBalance())) {
                        mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Total Tickets exceeds Member Balance");
                        returnToScanField();
                        return;
                    } else {
                        if (TestForNotes(t)) {
                            if (!GetNotes()) {
                                returnToScanField();
                                mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirm?", "You must enter a reason for using " + t.getName());
                                return;
                            }
                        }
                        if (TestPrzApp(t)) {
                            if (!GetPrzApp(t, maxTickets)) {
                                returnToScanField();
                                return;
                            }
                        }
                        //THIS IS WHERE I AM STARTING ON USEING THE SALESTABLE ONLY AND NOT NEED THE LOCALtOYS DATABASE.
                        ObservableList<Toys> Toylistupdate = SalesTable.getItems();
                        Toys tlist = getToyFromArrayList(t.getNumber(), 1);
                        for(int i= 0; i<Toylistupdate.size();i++) {
                       // System.out.println("-9-9-9-9-9-9-9-9-9-9- " + Toylistupdate.get(i).getName() + " toylist size" + Toylistupdate.size());                        
                            if (Toylistupdate.get(i).getName().equals(tlist.getName())) {
                                //System.out.println("this is where the item is :" + i);
                                toyListIndex = i;
                                update = true;   
                                //Toylistupdate.clear();
                            }
                        }
                        listMemberSalesItems = getTableList(t);
                        
                        //if (!tdb.putNewToy(t, m.getBalance())) {
                            //mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "PROBLEM");
                        //    returnToScanField();
                           // return;
                        //} else {
                            ShowTable(2);
                        //}
                    }
                } else {
                    mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Confirmed", "Not a Valid Prize");
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
            returnToScanField();
        //} else {
        //    empNumber.requestFocus();
        //    return;
        //}
    } // end of scanfield

    

    private void SetToUpper() {
        scanField.textProperty().addListener((ov, oldValue, newValue) -> {
            scanField.setText(newValue.toUpperCase());
        });
    }

    private boolean TestForNotes(Toys t) throws IOException {
        boolean T = false;
        if (t.getTag() > 0) {
            if (t.getTag() <= t.getQTY()) {
                T = true;
            }
        }
        return T;
    }

    private boolean GetNotes() throws IOException {
        boolean T = false;
        sc.getpassWord(stageV, "/pWordFX/prizeReqNotes.fxml", "Number", "Enter Employee Number:", boundsInScene.getMinX() + 350.0, boundsInScene.getMinY() + 300.0);
        if ("Number".equals(sc.getGameNumber())) {       
            T = false;
        } else {
            postWithDrawalNotes = true;
            setNotes(sc.getGameNumber());
            T = true;
        }
            return T;
    }

    private boolean TestPrzApp(Toys t) throws IOException {
        System.out.println("---------------------------------------------- getRnumber" + getrNumber());
        boolean T = false;
        if (t.getTickets() > Double.parseDouble(getrNumber())) {
            T = true;
        }
        return T;
    }

    private boolean GetPrzApp(Toys t, String maxTickets) throws IOException {
        setNotes("");
        boolean T = false;
        //if (t.getTickets() > Double.parseDouble(getrNumber())) {
        sc.getpassWord(stageV, "/pWordFX/withdrawalPassword.fxml", "Number", "Enter Employee Number:", boundsInScene.getMinX() + 350.0, boundsInScene.getMinY() + 300.0);
        if (!isSUPERValidInArrayList(sc.getGameNumber())) {
            //SUPERVISOR NUMBER INVALID
            mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Invalid Number", "Invalid Employee Number");
            //returnToScanField();
            T = false;
        } else {
            //NOT A SUPERVISOR
            if (empFX.getGProb() < Integer.parseInt(maxTickets)) {
                mBox.showAlert(Alert.AlertType.CONFIRMATION, null, "Invalid Number", "Not a Valid Supervisor Level");
                //returnToScanField();
                T = false;
            } else {
                //POST SUPERVISOR NAME AND DETAILS OF TRANSACTION SOMEWHERE
                getSuper = true;
                setNotes("Approved By: " + empFX.getNameF());
                T = true;
                Platform.runLater(() -> GoPostWithdrawalDetails(empFX.getEmpNumber()));
                //CONTINUE WITH WITHDRAWAL
                //SUPERVISOR NUMBER VALID FOR REST OF TRANSACTION
            }
        }
        //}
        return T;
    }

    private void returnToScanField() {
        scanField.clear();
        scanField.selectAll();
        scanField.requestFocus();

    }

    private boolean tooManyTickets(Toys t) {
        boolean tooManyTickets = false;
        /*try {
            int totalToys = tdb.getTotal(); //SUM OF THE ITEMS IN THE TOYBOX (THE TABLE)
            int newTotalToys = t.getTickets() + totalToys;
            int tooMany = m.getBalance() - newTotalToys;
            if (tooMany < 0 ) {
                tooManyTickets = true;
            } 

        } catch (SQLException ex) {
            Logger.getLogger(MemWithdrawalFXController.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        return tooManyTickets;
    }

    private void getStageV() {
        stageV = (Stage) doButton.getScene().getWindow();
    }

    
    
    
    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        GO = isEMPValidInArrayList(empNumber.getText().trim());
        return GO;
    }


    private boolean isSUPERValidInArrayList(String n) {
        
        boolean empValid = false;
        for (int y = 0; y < E.size(); y++) {
            if (n.trim().equals(E.get(y).getEmpNumber())) {
                empValid = true;
                empFX = new empFX(E.get(y).getNameF(), E.get(y).getNameL(), E.get(y).getEmpNumber(), E.get(y).getVAmt(), E.get(y).getBdayresos(), E.get(y).getChangerEdit(), E.get(y).getEmpID(), E.get(y).getGProb(), E.get(y).getActive(), E.get(y).getArcade(), E.get(y).getBcarsales());
                //System.out.println("0000000000000000000000 " + E.get(y).getGProb());
                //sID = String.valueOf(E.get(y).getEmpID());
                //sen = E.get(y).getEmpNumber();
                //sfn = E.get(y).getNameF();
                //sln = E.get(y).getNameL();
            }
        }
        return empValid;
    }

    
    //THIS IS FOR PRIZE APRRPVAL. WHEN AN ITEM IS PRICED GREATER THAN 19999 CUREENTLY BUT THEY CAN CHANGE IT THEN IT REQUIRES SUPERVISOR
    //THIS IS NOW POSTING TO THE MEMBER DETAIL TABLE AS OF 10/29/2023
    public void GoPostWithdrawalDetails(String en) {
        Thread thread33 = new Thread() {
            @Override
            
            public void run() {

                //db.InsertHistoricDataOneTime(m.getCID(), String.valueOf(empFX.getEmpID()), tranTime, "PRZAPP");
                db.InsertHistoricDataOneTimeXplain(m.getCID(), en, tranTime, "PRZAPP", getNotes(), "");
                //new CleanUpItems().InsertAccountChangeMessage(m.getCID(), getNotes(), en, tranTime, "PRZAPP");

            }
        };
        thread33.start();
    }
    
    
    private void setNotes(String notes) {
        this.notes += " " + notes;
    }
    
    private String getNotes() {
        return notes;
    }

    //THIS IS MOSTLY FOR MISCITEM BUT THAT COULD BE ANYITME THEY MARK AS SUCH WHEN THEY HAVE AN ITEM SOMETIMES IT CAN BE SOLD IN 
    //ONE PURCHASE. SINGLE ITEMS CAN ONLY BE SOLD 200 TIMES THEN IT REQUIRES NOTES. THEY COUDL DO THE SAME FOR OTHER ITEMS ALSO.
    //THIS IS NOW POSTING TO THE MEMBER DETAIL TABLE AS OF 10/29/2023
    public void GoPostWithdrawalNotes(String en, String fn, String tranTime) {
        Thread thread4 = new Thread() {
            @Override
            
            public void run() {
                System.out.println("we are heading into InsertHistoricDataOneTimeXplain");
                //new DB().InsertHistoricDataOneTime(m.getCID(), en, fn + ", " + getNotes());
                db.InsertHistoricDataOneTimeXplain(m.getCID(), en, tranTime, "MISCITEM", fn + ", " + getNotes(), "");
                //if (!new CleanUpItems().InsertAccountChangeMessage(m.getCID(), fn + ", " + getNotes(), en, tranTime, "MISCITEM")){
                //    try {
                //        new ClubFunctions().GOPRTTxtFile(dbsp.pathNameBalReport + "MiscItem.txt", m.getCID(), fn + ", " + getNotes(), en, tranTime, "MISCITEM");
                //    } catch (FileNotFoundException ex) {
                //        System.out.println(ex);
                //    }
                //}

            }
        };
        thread4.start();
    }
    
    
    //MOVD TO THE CLUBFUNTIONS, NOT USING THIS ONE 12/31/23
    public void GOPRTTxtFile(String stringPath, String CID, String fNameNotes, String empName, String tranTime, String location) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File(stringPath));
            pw.println(CID);
            pw.println(fNameNotes);
            pw.println(empName);
            pw.println(tranTime);
            pw.println(location);
            
            pw.close();

    }

    
    private boolean isEMPValidInArrayList(String n) {
        
        boolean empValid = false;
        for (int y = 0; y < E.size(); y++) {
            if (n.trim().equals(E.get(y).getEmpNumber())) {
                empValid = true;
                empFX = new empFX(E.get(y).getNameF(), E.get(y).getNameL(), E.get(y).getEmpNumber(), E.get(y).getVAmt(), E.get(y).getBdayresos(), E.get(y).getChangerEdit(), E.get(y).getEmpID(), E.get(y).getGProb(), E.get(y).getActive(), E.get(y).getArcade(), E.get(y).getBcarsales());
                //empFX = new empFX(E.get(y).getNameF(), E.get(y).getNameL(), E.get(y).getEmpNumber(), E.get(y).getVAmt(), E.get(y).getBdayresos(), E.get(y).getChangerEdit(), E.get(y).getEmpID(), E.get(y).getGProb(), E.get(y).getActive());
                //System.out.println("0000000000000000000000 " + E.get(y).getGProb());
                eID = String.valueOf(E.get(y).getEmpID());
                en = E.get(y).getEmpNumber();
                fn = E.get(y).getNameF();
                ln = E.get(y).getNameL();
            }
        }
        return empValid;
    }

    public empFX getEMPFromArrayList(String n) {
        empFX empList = null;
        /*
        // HERE IS WHERE I STARTED
        for (empFX E1 : E) {
            if (empNumber.getText().trim().equals(E1.getEmpNumber())) {
                empList = new empFX(E1.getNameF(), E1.getNameL(), E1.getEmpNumber());
            }
        }

        // HERE IS WHERE I ENDED*/
        return empList;
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
                    case TAB: break;
                    case DOWN: break;
                    case UP: break;
                    case ESCAPE: exitButtonPushed(); break;
                    case ENTER: enterKeyPressed(); break;
                default:
                    break;
                }
    } 
    
    private void playText(){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.1), scrollTextLable);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();

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
            case "scanField":
                if (!customerInfoBox.isVisible()) {
                    System.out.println("this is a test of the" + id + " field");
                    getMemberInfo();
                }
                
                //mBox.showError("ENTER 0 (ZERO) for list of all Promos", errorLabel, errorPane);
                //mBox.showErrorLayoutXY("ENTER 0 (ZERO) for list of all Promos", errorLabel, errorPane, 99.00, 275.00);
                break;
            case "empNumber":
                if (!customerInfoBox.isVisible()) {
                    System.out.println("this is a test of the" + id + " field");
                    getMemberInfo();
                }
                if (!SalesTable.isVisible() || !scanField.getText().trim().isEmpty()) {
                    System.out.println("this is a test of the" + id + " field");
                    scanFeildDO();
                }
            //empNumber.requestFocus();
            //System.out.println("here");
            //mBox.showError("Type in Your Employee Number then press the Save Button", errorLabel, errorPane);
            //mBox.showErrorLayoutXY("Type in Your Employee Number then press the Save Button", errorLabel, errorPane, 0.0, 619.00);
            //break;
        }
    }


    
    private void printEscPosWITHDRAWAL(Double n, Memtick mt)  throws PrintException, IOException{
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
            githubBufferedImage = ImageIO.read(new File(dbsp.pathNameImages + "/ReceiptLogo/withdrawalLogo.png"));
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
        escpos.write(Format, "CLUB WITHDRAWAL");
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
        escpos.write(Format, "Prev Balance" + ":" + "\t" + df.format(prevBalance));
        escpos.feed(1);        
        

        //PRINT WITHDRAWAL AMOUNT
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Withdrawal" + ":" + "\t" + df.format(mt.getSubtracted()));
        escpos.feed(1);        

        //PRINT NEW BALANCE
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "New Balance" + ":" + "\t" + df.format(newBalance));
        escpos.feed(2);        
        
        getReceipts("secondWithdrawalReceipt");

        ObservableList<Toys> listToys = SalesTable.getItems();
        listToys.forEach((item) -> {
        try{
            escpos.write(Format, item.getQTY() + " \t" + item.getName() + " \t" + item.getTickets());
            escpos.feed(1);        
            getReceipts("secondWithdrawalReceipt");
            String gt = getrNumber();
            System.out.println("======================================= gt = " + gt);
            if (item.getTickets() > Double.parseDouble(gt)) {
                printSecondReceipt++;
            }

        } catch(IOException e) {}

        });
        
        
        
        
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

        if (printSecondReceipt > 0) {
            try {
                 printReceiptPOSESC2();
                //printReceipt2();
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        }

    }
    
    
    
    
    private void printReceipt(Double n, Memtick mt) {

        try {
            pw = new PrintWriter(new File(dbsp.pathNameLocal + "WithdrawalReceipt.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        pw.println(getReceipts("CoName")); // to test if it works.
        String printthis = getReceipts("SubCoHeading");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Address");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Address2");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Phone");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("WWW");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        pw.println("Club Withdrawal Receipt"); // to test if it works.
        pw.println("======================"); // to test if it works.
        pw.println("");
        pw.println("");
        pw.println(customerNameLabel.getText());
        pw.println(" ");
        pw.println("Employee: " + fn);
        pw.println("");
        pw.println("");
        pw.println("Prev Balance" + ":" + "\t" + df.format(prevBalance));
        pw.println("Withdrawal" + ":" + "\t" + df.format(mt.getSubtracted()));
        pw.println("New Balance" + ":" + "\t" + df.format(newBalance));
        pw.println("");
        pw.println("Items Purchased");
        ObservableList<Toys> list = SalesTable.getItems();
        list.forEach((item) -> {
            pw.println(item.getQTY() + " " + item.getName() + " " + item.getTickets());
            getReceipts("secondWithdrawalReceipt");
            String gt = getrNumber();
            System.out.println("======================================= gt = " + gt);
            if (item.getTickets() > Double.parseDouble(gt)) {
                printSecondReceipt++;
            }

        });
        pw.println("");
        pw.println(" ");
        printthis = getReceipts("Footer1");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer2");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer3");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer4");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        printthis = getReceipts("Footer5");
        if (getrNumber().equals("1.0") || Double.parseDouble(getrNumber()) >= Double.parseDouble(RECEIPTS_DATE)) {
            pw.println(printthis);
            this.setrNumber("");
        } else {
            this.setrNumber("");
        }
        pw.println(" ");
        pw.println(rWinner);
        pw.println(" ");
        //if (scrollTextLable.isVisible()) {
        //    pw.println(" ");
        //    pw.println(" ");
        //    pw.println("You have an Email and we have");
        //    pw.println("sent a receipt to this Email Address");
        //    pw.println(isEMail.toUpperCase());
        //    pw.println(" ");
       // }
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
        pr.printReceipt("WithdrawalReceipt.txt");

        if (printSecondReceipt > 0) {
            try {
                printReceipt2();
            } catch (FileNotFoundException ex) {
                System.out.println(ex);
            }
        }

    }

    
    private void printReceiptPOSESC2() throws PrintException, IOException {
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
        escpos.write(Format, "Withdrawal: \t" + totalwithdrawalField.getText().trim());
        escpos.feed(1);        
        

        //PRINT TICKETS ADDED
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        escpos.write(Format, "Balance: \t" + df.format(newBalance));
        escpos.feed(1);        

     
        escpos.feed(2);        

        
        Format = new Style()
                .setFontName(Style.FontName.Font_A_Default)
                .setFontSize(Style.FontSize._1, Style.FontSize._1)
                .setColorMode(Style.ColorMode.BlackOnWhite_Default)
                .setJustification(EscPosConst.Justification.Left_Default);
        
         ObservableList<Toys> list = SalesTable.getItems();
        list.forEach((item) -> {
             try {
                 escpos.write(Format, item.getQTY() + " \t" + item.getName() + " \t" + item.getTickets());
                 escpos.feed(1);     
             } catch (IOException ex) {
                 System.out.println(ex);
             }
        });
        escpos.feed(3);     
        escpos.write(Format, "========================================");
        escpos.feed(1);     
        escpos.write(Format, "Member Signature");
        escpos.feed(3);     

        
        
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
    
    
    
    private void printReceipt2() throws FileNotFoundException {
        try {
            pw = new PrintWriter(new File(dbsp.pathNameLocal + "WithdrawalReceipt2.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("POJOS FAMILY FUN"); // to test if it works.
        pw.println("Club Withdrawal Receipt"); // to test if it works.
        pw.println("======================"); // to test if it works.
        pw.println("");
        pw.println("");
        pw.println(customerNameLabel.getText());
        pw.println("");
        pw.println("Employee: " + fn);
        pw.println("");
        pw.println("");
        pw.println("");
        //DecimalFormat df = new DecimalFormat("#");

        pw.println("Withdrawal: " + totalwithdrawalField.getText().trim());
        pw.println("Balance: " + df.format(newBalance));
        pw.println("");
        pw.println("Items Purchased");
        ObservableList<Toys> list = SalesTable.getItems();
        list.forEach((item) -> {
            pw.println(item.getQTY() + " " + item.getName() + " " + item.getTickets());
        });
        pw.println("");
        pw.println(" ");
        pw.println("");
        pw.println("");
        pw.println("");
        pw.println("========================================");
        pw.println("Member Signature");
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
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.println(" ");
        pw.close();

        print pr = new print();
        pr.printReceipt("WithdrawalReceipt2.txt");
    }
    
    
    
    
    
    public void exitButtonPushed() {
        //msg = null;
        //tMessage = "";
        newBalance = 0.0;
        en = null;
        fn = null;
        ln = null;
        CID = null;
        q = null;
        //listMemberSalesItems.clear();
        customerBalance = 0;
        try {
            SalesTable.getItems().clear();
            toysdb.disConnect();
            db.disConnect();
        } catch(Exception e) {}
        if (!SCLMT) {
            sc.setActivity("1");
        }

        stageV = (Stage) CCNumberIn.getScene().getWindow();
        stageV.close();
    } 




    
}
