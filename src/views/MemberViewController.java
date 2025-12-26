package views;

import views.counterPopUp.EmailEditorPopUpController;
import views.counterPopUp.memSearchTableViewController;
import views.counterPopUp.MemberNotesPopUpController;
import views.counterPopUp.historyViewerController;
import views.counterPopUp.historyPurchViewerController;
import views.counterPopUp.LockMemberReasonPopUp;
import views.counterPopUp.EditBalancePopUpController;
import JavaMail.Mail_JavaFX1;
import UtilConnections.UtilConnections;
import UtilConnections.UtilConnectionsSockets;
import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.BarCode;
import models.club.UtilDBMembers;
import models.club.UtilMember;
//import UtilModels.UtilMemtick;
import dbpathnames.dbStringPath;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messageBox.messageBox;
import models.club.DB;
import models.club.Memtick;
import pReceipts.print;
import commoncodes.ClubFunctions;
import commoncodes.IsItANumber;
import commoncodes.IsItANumber1;
import dbpathnames.GetDay;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SeparatorMenuItem;
import models.club.CheckBalanceDB;
import models.club.Member;
import models.toys.ToysDB;
import models.toys.ToysDetail;
import models.club.rCeipts;
import commoncodes.LabelDemo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.swing.JOptionPane;
import models.club.LastMemberTransaction;
import pWordFX.empFX;
//import pWordFX.employeeFX1;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;
import views.counterPopUp.scanCardAgainController;


/**
 * FXML Controller class
 *
 * @author Hannah
 */
public class MemberViewController implements Initializable {
    @FXML private AnchorPane root;
    //@FXML private VBox vbox1;
    @FXML private TextField cardNumberTextfield;
    @FXML private TextField fNameTextfield;
    @FXML private TextField lNameTextfield;
    @FXML private TextField addressTextfield;
    @FXML private TextField cityTextfield;
    @FXML private TextField stateTextfield;
    @FXML private TextField zipTextfield;
    @FXML private TextField phone1Textfield;
    @FXML private TextField phone2Textfield;
    @FXML private TextField phone3Textfield;
    @FXML private TextField ticketBalance;
    @FXML private Button PRTNumberButton;
    @FXML private Button moreButton;
    @FXML private Button detailButton;
    @FXML private Button searchButton;
    @FXML private Button editButton;
    @FXML private Button cancelButton;
    @FXML private Button printButton;
    @FXML private GridPane gridPane1;
    @FXML private GridPane gridPane3;
    //@FXML private Pane gridPane2;
    @FXML private TextField originalDateTextfield;
    @FXML private DatePicker birthDateTextfield;
    //@FXML private AnchorPane editPane;
    //@FXML private DatePicker editPaneDate;
    //@FXML private TextField editPaneSales;
    //@FXML private TextField editPaneCoins;
    //@FXML private TextField editPaneTickets;
    //@FXML private TextField editPanePrizes;
    //@FXML private TextField editPaneID;
    @FXML private Pane detailPane;
    //@FXML private Button editPaneSave;
    @FXML private static String detailID;
    @FXML private static String staticMemberID;
    @FXML private Pane errorBar;
    @FXML private Label errorLabel;

    
    public static SceneChanger_Main SC;
    public static DB DB;
    public static FXMLLoader FXLOADER;
    public static employeeFX EFX;
    public static dbStringPath dbsp;
    public static ArrayList<rCeipts> rCeipts;
    public static messageBox mbox;
    public static Mail_JavaFX1 jmail;
    public static CheckBalanceDB chkbalance;
    public static Member member = null, d = null;
    public static Memtick mt = null;
    private static final LabelDemo LABELDEMO = new LabelDemo();

    private static UtilMember utilMember = null;
    //private static UtilMemtick utilMemtick;

    
    public static final String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    private static final ClubFunctions CLUBFUNCTIONS = new ClubFunctions();
    private static final IsItANumber ISITANUMBER = new IsItANumber();
    private static final LocalDate NOW = LocalDate.now();
    private static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#");
    private static final UtilDBMembers UTILDBMEMBERS = new UtilDBMembers();
    ContextMenu contextmenu = new ContextMenu();
    //public static int iLastTran;
    public static ArrayList<LastMemberTransaction> lastMember;


    private static Boolean editMode = false, CLOSE = false, EDITBDAY = false;
    private static double prevBalance = 0;
    private static Connection connectionOldDetail = null, connectionMembers = null;
    private static Bounds boundsInScenememButton;
    private static Bounds boundsInScenememMoreButton;
    private static Stage stageV;
    private static String tranTime = new GetDay().getCurrentTimeStamp();
    private static int whichOne = 0;
    public static String cssPath = null, popupReturnMessage= null;
    public static int beenhereonce = 0;
    public static String eID, rNumber;
    private static Double newFixedBalance = 0.0, updateDetail = 0.0, currBalance = 0.0;
    public static empFX newEFX;



    @FXML private TableView<Memtick> memtickDetailTable;
    @FXML private TableColumn<Memtick, LocalDate> memtickDateColumn;
    @FXML private TableColumn<Memtick, String> memtickEmpNumberColumn;
    @FXML private TableColumn<Memtick, String> memtickAddedColumn;
    @FXML private TableColumn<Memtick, String> memtickBonusColumn;
    @FXML private TableColumn<Memtick, String> memtickWithColumn;
    @FXML private TableColumn<Memtick, String> LocationColumn;
    //@FXML private TableColumn<Memtick, String> memtickIDColumn;
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       boundsInScenememButton = root.localToScene(root.getBoundsInLocal());
       boundsInScenememMoreButton = moreButton.localToScene(moreButton.getBoundsInLocal());
       DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
       memtickDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
       memtickDateColumn.setCellFactory(column -> {
            return new TableCell<Memtick, LocalDate>() {
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
        memtickEmpNumberColumn.setCellValueFactory(new PropertyValueFactory<>("MaskedEmpNumber"));
        memtickAddedColumn.setCellValueFactory(new PropertyValueFactory<>("Added"));
        memtickBonusColumn.setCellValueFactory(new PropertyValueFactory<>("Bonus"));
        memtickWithColumn.setCellValueFactory(new PropertyValueFactory<>("Subtracted"));
        LocationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        //memtickIDColumn.setCellValueFactory(new PropertyValueFactory<>("TranID"));
        root.getStylesheets().add(cssPath);
        detailPane.setVisible(false);
        //editPane.setVisible(false);
        setHighLights();
        buildMenuButton();
        setToUpper();
        //new FocusedTextSetToUPPER().setHighlightListener(gridPane2);
        setKeyCodes();
        makeAllNoEdit();
        addPhonefieldListeners();
        mbox.showErrorClear(errorLabel, errorBar);
        Platform.runLater(()->searchButton.requestFocus());
        Platform.runLater(()->getFirstMember());        
    } 
    

    
    
    
    public void buildMenuButton() {
        MenuItem fullTranHistory = new MenuItem(" Full Tran History ");
        MenuItem toyTranHistory = new MenuItem(" Full Purch History ");
        // A SEPARATOR MENU ITEM IS HERE IN THE ADDMENUITEMS COMMAND BELOW
        MenuItem closeAccounts = new MenuItem(" CLOSE ACCOUNT ");
        MenuItem lockAccount = new MenuItem(" LOCK ACCOUNT ");
        MenuItem mNotes = new MenuItem(" MEMBER NOTE ");
        MenuItem newCard = new MenuItem(" NEW CARD NUMBER ");
        MenuItem editTransferBalance = new MenuItem(" EDIT|TRANSFER BALANCE ");
        MenuItem gCard = new MenuItem(" GOLDCARD ");
        // A SEPARATOR MENU ITEM IS HERE IN THE ADDMENUITEMS COMMAND BELOW
        MenuItem wadd = new MenuItem(" WRONG ADDRESS ");
        MenuItem emailEdit = new MenuItem(" Email Editor ");
        MenuItem fixBalance = new MenuItem(" Fix Balance ");
        MenuItem editBday = new MenuItem(" Edit Birthdate ");
        MenuItem trainCard = new MenuItem(" Make Traing Card ");
        // A SEPARATOR MENU ITEM IS HERE IN THE ADDMENUITEMS COMMAND BELOW
        MenuItem item911 = new MenuItem(" CLEAR MENU ");

        contextmenu.getItems().addAll(fullTranHistory, toyTranHistory, new SeparatorMenuItem(), closeAccounts, lockAccount, mNotes, editTransferBalance, newCard, editBday, new SeparatorMenuItem(), wadd, gCard, emailEdit, fixBalance, trainCard, new SeparatorMenuItem(), item911);

        fullTranHistory.setOnAction((ActionEvent event) -> {
            historyViewerController wController = (historyViewerController) FXLOADER.getController();
            wController.sc = SC;
            wController.dbsp = dbsp;
            wController.db = DB;
            wController.mbox = mbox;
            wController.efx = EFX;
            wController.jmail = jmail;
            wController.memID = staticMemberID;
            wController.cName = member.getNameF() + " " + member.getNameL();
            wController.csspath = cssPath;
            try {
                SC.getpassWord(stageV, "/views/counterPopUp/historyViewer.fxml", detailID, cssPath, 300.0, 50.0);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });

        toyTranHistory.setOnAction((ActionEvent event) -> {
            historyPurchViewerController wController = (historyPurchViewerController) FXLOADER.getController();
            wController.sc = SC;
            wController.dbsp = dbsp;
            wController.db = DB;
            wController.mbox = mbox;
            wController.efx = EFX;
            wController.jmail = jmail;
            wController.memID = staticMemberID;
            wController.cName = member.getNameF() + " " + member.getNameL();
            wController.csspath = cssPath;
            try {
                SC.getpassWord(stageV, "/views/counterPopUp/historyPurchViewer.fxml", detailID, cssPath, 300.0, 50.0);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });

        emailEdit.setOnAction((ActionEvent event) -> {
            EmailEditorPopUpController wController = (EmailEditorPopUpController) FXLOADER.getController();
            wController.typeVar = "3";
            wController.empID = eID;
            wController.CCN = member.getCCN();
            wController.cssPath = cssPath;
            wController.rCeipts = rCeipts;
            wController.sc = SC;
            wController.db = DB;
            wController.dbsp = dbsp;
            wController.mBox = mbox;
            wController.chkbalance = chkbalance;
            try {
                SC.getStagestyleUndecorated(stageV, "/views/counterPopUp/EmailEditor.fxml", "EmailEditor", "EmailEditor", boundsInScenememButton.getMinX() + 420.0, boundsInScenememButton.getMinY() + 200.0);
            } catch (IOException ex) {
                System.out.println(ex);
            }
            //SC.getpassWord(stageV, "/views/EmailEditor.fxml", "Number", "Enter Member Number:", boundsInScene.getMinX()+420.0, boundsInScene.getMinY()+200.0);
            //SC.goToScene("EmailEditor", stageV, EFX.getNameF(), gridPane2, boundsInScenememButton);
            Platform.runLater(() -> getMemberAfterUpdate(member.getCCN(), 0));
        });

        //HAS CLEARDETAIL AND DIABLE ENABLE CANCELBUTTON
        mNotes.setOnAction((ActionEvent event) -> {
            clearDetail();
            Platform.runLater(()->disableButtons());
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            MemberNotesPopUpController wController = (MemberNotesPopUpController) FXLOADER.getController();
           
            wController.mfn = member.getNameF();
            wController.mln = member.getNameL();
            wController.sc = SC;
            wController.cssPath = cssPath;
            wController.dbsp = dbsp;
            wController.m = member;
            wController.empID = eID;
            wController.fn = newEFX.getNameF();
            wController.cID = member.getCID();
            wController.m = member;
            wController.memberString = "Member Name: " + member.getNameF() + " " + member.getNameL() + " \nAddress: " + member.getAddress();
            try {
                SC.getpassWord(stageV, "/views/counterPopUp/MemberNotesPopUp.fxml", detailID, cssPath, 400.0, 100.0);
                if (!SC.getGameNumber().equals("Number")) {
                    LABELDEMO.TextDisplayPopup("Adding Member Note", "<html><font size=22 color=blue>Status: Member Note:\n Please Wait!", 3, 475, 225, JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
            Platform.runLater(()->enableButtons());
        });

        
        //HAS CLEARDETAIL AND DIABLE ENABLE CANCELBUTTON
        newCard.setOnAction((ActionEvent event) -> {
            clearDetail();
        //Platform.runLater(()->labeldemo.TextDisplayPopup("New Card Number", "<html><font size=22 color=blue>Status: New Card:\n Checking Pojo Account", 1, 475, 225, JOptionPane.INFORMATION_MESSAGE));
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }

            Platform.runLater(() -> GetPopUp("1", "NEWCRD"));
            Platform.runLater(()->disableButtons());
            try {
            scanCardAgainController wController = new scanCardAgainController();
            wController.lastmember = lastMember;
            wController.newEFX = newEFX;
            wController.sc = SC;
            wController.cssPath = cssPath;
            wController.FXLOADER = FXLOADER;
            wController.dbsp = dbsp;
                SC.getpassWord(stageV, "/views/counterPopUp/scanCardAgain.fxml", "Number", "Enter New Card Number:", boundsInScenememButton.getMinX() + 250.0, boundsInScenememButton.getMinY() + 90.00);
                if (!SC.getGameNumber().equals("Number")) {
        LABELDEMO.TextDisplayPopup("New Card Number", "<html><font size=22 color=blue>Status: New Card:\n Checking for Active and Inactive Members with same Card", 3, 475, 225, JOptionPane.INFORMATION_MESSAGE);
                    if (!DB.isInactiveMemberValid(SC.getGameNumber())) {
                        if (!DB.isMemberValid(SC.getGameNumber())) {
        Platform.runLater(()->LABELDEMO.TextDisplayPopup("New Card Number", "<html><font size=22 color=blue>Status: New Card:\n Updateing Account with new number", 5, 675, 325, JOptionPane.INFORMATION_MESSAGE));
                            if (!DB.updateCardNumber(SC.getGameNumber(), member)) {
                                mbox.showAlert(Alert.AlertType.ERROR, stageV, "ERROR", "New Card Number was not Updated");
                            } else {
                                if (!addUpdateMSG("NEWCRD")) {

                                }
                                Platform.runLater(() -> getMemberAfterUpdate(SC.getGameNumber(), 0));
                                DB.disConnect();
                            }
                            //do some kind of update function in the DB.java
                        } else {
                            mbox.showAlert(Alert.AlertType.INFORMATION, stageV, "Invalid Card Number", "This Card already in use in Active Members");
                            Platform.runLater(() -> enableButtons());
                            DB.disConnect();
                            beenhereonce = 0;
                            return;
                        }
                    } else {
                        Platform.runLater(() -> enableButtons());
                        DB.disConnect();
                        beenhereonce = 0;
                        mbox.showAlert(Alert.AlertType.INFORMATION, stageV, "Inacvtive Member Found", "This Card already in use in Inactive Members");
                        return;
                    }

                } else {
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "", "Make Member a New Card Number Cancelled");
                    Platform.runLater(() -> enableButtons());
                    DB.disConnect();
                    return;
                }

            } catch (SQLException | IOException ex) {
                mbox.showAlert(Alert.AlertType.INFORMATION, stageV, "Member Card Update", "Something went wrong trying to update a card Number " + ex.toString());
            }
            Platform.runLater(()->enableButtons());
            DB.disConnect();
            beenhereonce = 0;

        });

        editBday.setOnAction((ActionEvent event) -> {
            //System.out.println("EMp ID " + eID);
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
                //eID
                System.out.println(newEFX.getArcade() + " " + newEFX.getChangerEdit());
                if (!chkCorpLevel(eID)) {
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "Error", "Not a Valid Employee ID");
                } else {

                    if (!newEFX.employeeLevel("Corporate")) {
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, "Error", "You must have a higher traing level");
                        return;
                    } else {
                        //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Error", "You are In");
                        EDITBDAY = true;
                        searchButton.requestFocus();
                        birthDateTextfield.requestFocus();
                        editButton.setText("Save F9");

                    }
                }
            } catch (Exception e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }

        });

        closeAccounts.setOnAction((ActionEvent event) -> {
            Platform.runLater(() -> cancelButton.setDisable(true));
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            if (GetPopUp("1", "CLOSE")) {
                Platform.runLater(() -> cancelButton.setDisable(false));
            } else {
                try {
                    connectionMembers = getConnectionMembers();
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println(ex);
                }
                utilMember = new UtilMember(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText() + ", " + cityTextfield.getText() + ", " + stateTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                //utilMemtick = new UtilMemtick(utilMember.getCID(), eID, null, LocalDate.now(), null, null, null, "CLOSE", getPopupReturnMessage(), 0);
                mt = new Memtick(utilMember.getCID(), eID, null, LocalDate.now(), null, null, null, "CLOSE", getPopupReturnMessage(), 0);
                UTILDBMEMBERS.setCityNameTypeVar(utilMember.getCCN(), 1, utilMember, connectionMembers);
                getFirstMember();
                //getMemberAfterUpdate("100001", 0);
                Platform.runLater(() -> cancelButton.setDisable(false));
                //CLOSE = true;
                beenhereonce = 0;
            }
        });

        trainCard.setOnAction((ActionEvent event) -> {
            //System.out.println("EMp ID " + eID);
            if (LocalDate.parse(originalDateTextfield.getText()).equals(LocalDate.parse("1899-01-01"))) {
                int x = new messageBox().confirmMakeThisChangeButtonTitles(Alert.AlertType.ERROR, stageV, "Question?", "This card is currently a Training Card. \n Continuing will deactivate the Training Card. \n\n Are you sure you want to do this?", "YES", "NO");
                if (x == 1) {
                    d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.now(), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                    DB.makeTrainingCardSetDate(d);
                    
                    // here is where we would add to the code
                    Platform.runLater(() -> getMemberAfterUpdate(member.getCCN(), 0));
                    System.out.println(LocalDate.parse(originalDateTextfield.getText()));
                    beenhereonce = 0;
                   return;
                } else {
                    return;
                }

            }

            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
                if (!chkCorpLevel(eID)) {
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "Error", "Not a Valid Employee ID");
                } else {
                    if (!newEFX.employeeLevel("Corporate")) {
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, "Error", "You must have a higher traing level");
                        return;
                    } else {
                        int x = new messageBox().confirmMakeThisChangeButtonTitles(Alert.AlertType.ERROR, stageV, "Question?", "You are making this account a training card. \n Training Cards get reset to ZERO every Night and all data is reset to the current data you have in the account at this time. \n\n Are you sure you want to do this?", "YES", "NO");
                        if (x == 1) {
                            d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse("1899-01-01"), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
//                            d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", LocalDate.parse("1899-01-01"), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText(), "");
                            DB.makeTrainingCardSetDate(d);
                            Memtick newDetail = new Memtick(d.getCID(), "679780", new GetDay().getCurrentTimeStamp(), new GetDay().asSQLDate(NOW.minusDays(1)).toLocalDate(), 0.0, 0.0, 0.0, "RS", 0);
                            String thisID = d.getCID();
                            DB.DeleteTrainingCardDetail(thisID);
                            DB.InsertDataCleanDetailTraingingCard(newDetail);
                            DB.updateTicketBalance(0.0, newDetail);
                            DB.SetTrainingCardData(d);
                            String tEmail = DB.getEmail(thisID);
                            System.out.println("here is the last name " + d.getNameL());
                            DB.SetTrainingCardEmail(thisID, tEmail);
                            System.out.println("Here we are going into setTraing Cards" + d.getOdateSQL() + " " + d.getBdateSQL());
                            DB.SetTraingCardDataXML(d);
                            DB.SetTraingCardEmailXML(thisID, tEmail);
                            DB.disConnect();
                            //DB.SetTrainingCardEmail(d.getCID(), tEmail);
                            beenhereonce = 0;

                        }
                    }
                }
            } catch (Exception e) {
                //mbox.showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                System.out.println("here is the error : " + e.toString());
                //return;
            }
            Platform.runLater(() -> getMemberAfterUpdate(member.getCCN(), 0));
        });

        wadd.setOnAction((ActionEvent event) -> {
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                mbox.showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            try {
                d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                Memtick mtt = new Memtick(d.getCID(), eID, null, LocalDate.now(), 0.0, 0.0, 0.0, "WADD", 0);
                DB.CloseAccount(member.getCCN(), 2, mtt);
                //addUpdateMSG("WADD");
                DB.disConnect();
                beenhereonce = 0;
                Platform.runLater(() -> getMemberAfterUpdate(member.getCCN(), 0));
            } catch (SQLException ex) {
                System.out.println("error " + ex.toString());
            }
        });

        gCard.setOnAction((ActionEvent event) -> {
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            try {
                d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                Memtick mtt = new Memtick(d.getCID(), eID, null, LocalDate.now(), 0.0, 0.0, 0.0, "GCARD", 0);
                DB.MakeGoldCard(member.getCCN(), 2, mtt, d);
                //addUpdateMSG("WADD");
                DB.disConnect();
                beenhereonce = 0;             
                getMemberAfterUpdate(member.getCCN(), 0);
            } catch (SQLException ex) {
                System.out.println("error " + ex.toString());
            }
        });

        lockAccount.setOnAction((ActionEvent event) -> {
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                mbox.showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            if (GetPopUp("3", "LOCK")) {
                Platform.runLater(() -> cancelButton.setDisable(false));
            } else {
                UtilMember utilMember = new UtilMember(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                new UtilDBMembers().InsertAccountChangeMessageConnection(utilMember.getCID(), getPopupReturnMessage(), eID, "LOCK", connectionOldDetail);
                mt = new Memtick(utilMember.getCID(), eID, null, LocalDate.now(), null, null, null, "LOCK", 0);
                System.out.println("SHould be the time here " + mt.getDateTime());
                new UtilDBMembers().setCityNameTypeVar(utilMember.getCCN(), 3, utilMember, connectionMembers);
                //getMemberAfterUpdate(utilMember.getCCN(), 0);
                getFirstMember();
                Platform.runLater(() -> cancelButton.setDisable(false));
                CLOSE = true;
                beenhereonce = 0;             
            }
        });

        /*try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            if (CLOSE.equals(true)) {
                if (GetPopUp("0", "LOCK")) {
                    return;
                }
            } else {
                if (GetPopUp("1", "LOCK")) {
                    return;
                } else {
                    d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                    try {
                    Memtick mtt = new Memtick(d.getCID(), eID, null, LocalDate.now(), null, null, null, "LOCK", 0);
                        DB.CloseAccount(m.getCCN(), 3, mtt);
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                    //Memtick mt = new Memtick(d.getCID(), eID, null, LocalDate.now(), null, null, null, "LOCK", 0);
                    //DB.InsertData(mt);
                    DB.disConnect();
                    getGame(m.getCCN(), 0);
                    CLOSE = true;
                }
            }
        //});
         */
 /*try {
            checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch(Exception e) {new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());}
            try {
               if (m.getCity().equals("LOCK")) {
                Platform.runLater(()->GetPopUp("0", "LOCK"));
               } else {
                DB.CloseAccount(m.getCCN(), 3);
                DB.disConnect();
                getGame(m.getCCN(), 0);
                Platform.runLater(()->GetPopUp("1", "LOCK"));
               }
            } catch (SQLException ex) {
                System.out.println("error " + ex.toString());
            }
            CLOSE = true;
         */
        //});
        fixBalance.setOnAction((ActionEvent event) -> {
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                mbox.showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            try {
                currBalance = Double.parseDouble(ticketBalance.getText()); //2000
                if (currBalance < 0) {
                    currBalance = 0.0;
                }
                newFixedBalance = DB.getMemDetailBalanceCHK(staticMemberID); //3000
                if (!newFixedBalance.equals(currBalance)) {
                    if (newFixedBalance < 0) {
                        updateDetail = newFixedBalance * -1.0;
                        newFixedBalance = currBalance;
                    }
                    if (newFixedBalance < currBalance) {
                        //updateDetail = currBalance - newFixedBalance;
                        //newFixedBalance = currBalance - newFixedBalance;   //currBalance;
                    }
                    DB.updateImbalance(newFixedBalance, staticMemberID);
                    Memtick mtt = new Memtick(staticMemberID, eID, tranTime, LocalDate.now(), 0.0, 0.0, 0.0, "BALCHK", 0);
                    //Memtick mt = new Memtick(staticMemberID, eID, LocalDate.now(), updateDetail, "BALCHK");
                    DB.InsertData(mtt);
                    DB.disConnect();

                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "Completed", "Balance Updated");
                } else {
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "Completed", "Ticket Balance and Transactions are equal");
                }
                beenhereonce = 0;             
                Platform.runLater(() -> getMemberAfterUpdate(member.getCCN(), 0));
            } catch (SQLException ex) {
                System.out.println("error " + ex.toString());
            }

        });

        //EDIT / TRANSFER BALANCE
        editTransferBalance.setOnAction((ActionEvent event) -> {
            try {
                checkFirstRecord(cardNumberTextfield.getText().trim());
            } catch (Exception e) {
                mbox.showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            GetBalanceEdit();
            //THIS IS DONE IN THE GETBALANCEEDIT() SCREEN
            //HERE IS WHERE WE NEED TO DO A NEGATIVE BALANCE CHECK
            beenhereonce = 0;             
            Platform.runLater(() -> getMemberAfterUpdate(member.getCCN(), 0));
            Platform.runLater(() -> {
                try {
                    PrintReceipt(member.getCID());
                } catch (FileNotFoundException | SQLException ex) {
                    System.out.println(ex);
                }
            });
        });

        moreButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextmenu.show(moreButton, boundsInScenememMoreButton.getMinX() + 25, boundsInScenememMoreButton.getMinY() + 135);
            }
        });
        moreButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextmenu.show(moreButton, event.getScreenX(), event.getScreenY());
        });
    }

    public Connection getConnectionMembers() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Connection> callable = () -> {
            connectionMembers = new UtilConnections().connectMembers();
            return connectionMembers;
        };
        Future<Connection> future = executor.submit(callable);
        connectionMembers = future.get(); //returns 2 or raises an exception if the thread dies, so safer
        executor.shutdown();
        //System.out.println("in getConnection " + connectionMembers);
        return connectionMembers;
    }

    public Connection getConnectionOldDetail() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Connection> callable = () -> {
            connectionOldDetail = new UtilConnectionsSockets().connectOldDetailSockets();
            return connectionOldDetail;
        };
        Future<Connection> future = executor.submit(callable);
        connectionOldDetail = future.get(); //returns 2 or raises an exception if the thread dies, so safer
        executor.shutdown();
        //System.out.println("in getConnection " + connectionOldDetail);
        return connectionOldDetail;
    }

    public void menuItemsButtonMouseOver() {
        contextmenu.show(moreButton, boundsInScenememMoreButton.getMinX() + 25, boundsInScenememMoreButton.getMinY() + 135);
    }

    private void getStageV() {
        stageV = (Stage) PRTNumberButton.getScene().getWindow();
    }

    private boolean chkCorpLevel(String ID) {
        boolean corpLevel = false;
        System.out.println("Corporate level" + newEFX.getArcade() + " " + newEFX.getChangerEdit());
        if (newEFX.getChangerEdit()>0) {
            corpLevel = true;
        }
        return corpLevel;
    }

   
        
    public void getTableRow() {
    
    Memtick tableRowValue = memtickDetailTable.getSelectionModel().getSelectedItem();
    if (!new employeeFX().isemployeeHistoric(tableRowValue.getEmpNumber())) {
        Platform.runLater(()->jmail.sendEmailTo("No Number Found", "This Number Could Not be Found in THe Employees Data " + tableRowValue.getEmpNumber(), "errors"));
    } else {
        //get items this purchase
        String t = DB.GetAccountChangeMessageForMemDetailDB(tableRowValue.getTranID());
        ArrayList<ToysDetail> toylist = new ToysDB().getThisPurchHistory(tableRowValue.getMemID(), tableRowValue.getTime());
        Iterator<ToysDetail> toylistIterator = toylist.iterator();
        String newToylist = "";
        while (toylistIterator.hasNext()) {
            ToysDetail tvShow = toylistIterator.next();
            newToylist += "(" + tvShow.getQTY() + ") " + tvShow.getName() +  " " + tvShow.getTickets() + " Tickets\n";
        }
        if (t.equals(":null")) {
            t = "";
        }
        if (!newToylist.equals("")) {
            mbox.showAlertMemDetail(Alert.AlertType.ERROR, stageV, "EmpNumber", new employeeFX().getNameF() + " " + new employeeFX().getNameL() + " \n Time: " + tableRowValue.getTime() + " \n\n" + newToylist + "\n\n" + t);
            System.out.println("9/18/23 (1)" + t);
        } else {
            System.out.println("9/18/23 (2)" + t);
            mbox.showAlert(Alert.AlertType.ERROR, stageV, "EmpNumber", new employeeFX().getNameF() + " " + new employeeFX().getNameL() + " \n Time: " + tableRowValue.getTime() + "\n\n" + t);
        }
    }
    }

    private boolean GetPopUp(String updateVar, String typeVar) {
        boolean isCancel = false;
        String newTMessage = "-";
        if (newTMessage.isEmpty()) {
            updateVar = "1";
        }
        LockMemberReasonPopUp wController = (LockMemberReasonPopUp) FXLOADER.getController();
        wController.empName = member.getNameF() + " " + member.getNameL();
        wController.css = cssPath;
        wController.typeVar = typeVar;
        wController.updateVar = updateVar;
        wController.memberPassed = member;
        wController.tMessage = newTMessage;
        wController.empID = eID;
        try {
            SC.getpassWord(stageV, "/views/counterPopUp/LockMemberReasonPopUp.fxml", "Number", "New Card Number:", 520.0, 100.0);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene("LockReason", stageV, eFX.getNameF(), null, boundsInScenememButton);
        String popupRetunMessage = (SC.getGameNumber());
        //System.out.println("1111111111111 " + popupRetunMessage);
        setPopupReturnMessage(popupRetunMessage);
        if (SC.getGameNumber().equals("T")) {
            isCancel = true;
        }
        return isCancel;
    }
    

    public void setPopupReturnMessage(String popupReturnMessage) {
        this.popupReturnMessage = popupReturnMessage;
    }
    
    private String getPopupReturnMessage() {
        return this.popupReturnMessage;
    }
    
    
    
    private void GetBalanceEdit() {
        prevBalance = Double.valueOf(ticketBalance.getText());
        EditBalancePopUpController wController = (EditBalancePopUpController) FXLOADER.getController();
        wController.empName = member.getNameF() + " " + member.getNameL();
        wController.cssPath = cssPath;
        wController.m = member;
        wController.empID = eID;
        wController.db = DB;
        wController.mBox = mbox;
        wController.dbsp = dbsp;
        wController.mFrom = member;
        wController.memtick = mt;
        
        try {
            SC.getpassWord(stageV, "/views/counterPopUp/EditBalancePopUp.fxml", detailID, cssPath, 300.0, 200.0);
        } catch (IOException ex) {
           System.out.println(ex);
        }
        //SC.goToScene("EditBalance", stageV, EFX.getNameF(), null, boundsInScenememButton);

    }
    


    public void addPhonefieldListeners() {
        phone1Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        ISITANUMBER.checkNumbers(newValue);
                    } catch (Exception e) {
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "This Field can only be numbers");
                        phone1Textfield.setText("");
                        phone1Textfield.requestFocus();
                        return;
                    }
                    if (!CLUBFUNCTIONS.testPhone(1, newValue)) {
                        if (newValue.length() > 3) {
                            phone1Textfield.setText(phone1Textfield.getText().substring(0, 3));
                            mbox.showError("This Field cannot have more than 3 digits", errorLabel, errorBar);
                            return;
                        } else {
                            mbox.showError("This Field must have three digits", errorLabel, errorBar);
                            return;
                        }
                    } else {
                        mbox.showErrorClear(errorLabel, errorBar);
                    }
                }
        );
        phone2Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        ISITANUMBER.checkNumbers(newValue);
                    } catch (Exception e) {
                        //mb.showError("This Field can only be Numbers", errorLabel, errorBar);
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "This Field can only be numbers");
                        //JOptionPane.showMessageDialog(null, "This Field can only be Numbers");
                        phone2Textfield.setText("");
                        phone2Textfield.requestFocus();
                        return;
                    }
                    if (!CLUBFUNCTIONS.testPhone(2, newValue)) {
                        if (newValue.length() > 3) {
                            phone2Textfield.setText(phone2Textfield.getText().substring(0, 3));
                            mbox.showError("This Field cannot have more than 3 digits", errorLabel, errorBar);
                        } else {
                            mbox.showError("This Field must have three digits", errorLabel, errorBar);
                            return;
                        }
                    } else {
                        mbox.showErrorClear(errorLabel, errorBar);
                    }
                }
        );
        phone3Textfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        ISITANUMBER.checkNumbers(newValue);
                    } catch (Exception e) {
                        //mb.showError("This Field can only be Numbers", errorLabel, errorBar);
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "This Field can only be numbers");
                        phone3Textfield.setText("");
                        phone3Textfield.requestFocus();
                        return;
                    }
                    if (!CLUBFUNCTIONS.testPhone(3, newValue)) {
                        if (newValue.length() > 4) {
                            phone3Textfield.setText(phone3Textfield.getText().substring(0, 4));
                            mbox.showError("This Field cannot have more than 4 digits", errorLabel, errorBar);
                        } else {
                            mbox.showError("This Field must have four digits", errorLabel, errorBar);
                            return;
                        }
                    } else {
                        mbox.showErrorClear(errorLabel, errorBar);
                    }
                }
        );
        zipTextfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        ISITANUMBER.checkNumbers(newValue);
                    } catch (Exception e) {
                        //mb.showError("This Field can only be Numbers", errorLabel, errorBar);
                        mbox.showAlert(Alert.AlertType.ERROR, stageV, "Stop", "This Field can only be numbers");
                        zipTextfield.setText("");
                        zipTextfield.requestFocus();
                        return;
                    }
                    if (!CLUBFUNCTIONS.testPhone(5, newValue)) {
                        if (newValue.length() > 5) {
                            zipTextfield.setText(zipTextfield.getText().substring(0, 5));
                            mbox.showError("This Field cannot have more than 5 digits", errorLabel, errorBar);
                        } else {
                            mbox.showError("This Field must have five digits", errorLabel, errorBar);
                            return;
                        }
                    } else {
                        mbox.showErrorClear(errorLabel, errorBar);
                    }
                }
        );
    }




    private void getMemberAfterUpdate(String ID, int d) {
        //System.out.println("we re running through here " + SC.getGameNumber().trim());
        try {
            //m = new Member(ID);
            Member memberUpdate = DB.getMemberTransfer(ID);
            if (!memberUpdate.getNameF().equals("inValid")) {
                //JOptionPane.showMessageDialog(null, "Game Number Valid " + m.getCCN()); 
                cardNumberTextfield.setText(memberUpdate.getCCN());
                fNameTextfield.setText(memberUpdate.getNameF());
                lNameTextfield.setText(memberUpdate.getNameL());
                addressTextfield.setText(memberUpdate.getAddress());
                cityTextfield.setText(memberUpdate.getCity());
                stateTextfield.setText(memberUpdate.getState());
                zipTextfield.setText(memberUpdate.getZip());
                birthDateTextfield.setValue(memberUpdate.getBdate());
                originalDateTextfield.setText(memberUpdate.getOdate().toString());
                phone1Textfield.setText(memberUpdate.getAreaCode());
                phone2Textfield.setText(memberUpdate.getPhone1());
                phone3Textfield.setText(memberUpdate.getPhone2());
                staticMemberID = memberUpdate.getCID();
                ticketBalance.setText(String.valueOf(memberUpdate.getBalance()));
                DB.disConnect();
            } else {
                DB.disConnect();
                if (d == 1) {

                } else {
                    //JOptionPane.showMessageDialog(null, "Card Number Not Found " + m.getCCN());
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Card Number Not Found " + member.getCCN() + " " + ID);
                }
                return;
            }
        } catch (HeadlessException | SQLException e) {
            return;
        }

        DB.disConnect();
        Platform.runLater(()->searchButton.requestFocus());
    }
    
    
    
     private void fillMemberFeilds(Member member, int d) {
        //System.out.println("we re running through here " + SC.getGameNumber().trim());
        try {
            //m = new Member(ID);
            //m = DB.getMember(ID);
            if (!member.getNameF().equals("inValid")) {
                //JOptionPane.showMessageDialog(null, "Game Number Valid " + m.getCCN()); 
                cardNumberTextfield.setText(member.getCCN());
                fNameTextfield.setText(member.getNameF());
                lNameTextfield.setText(member.getNameL());
                addressTextfield.setText(member.getAddress());
                cityTextfield.setText(member.getCity());
                stateTextfield.setText(member.getState());
                zipTextfield.setText(member.getZip());
                birthDateTextfield.setValue(member.getBdate());
                originalDateTextfield.setText(member.getOdate().toString());
                phone1Textfield.setText(member.getAreaCode());
                phone2Textfield.setText(member.getPhone1());
                phone3Textfield.setText(member.getPhone2());
                staticMemberID = member.getCID();
                ticketBalance.setText(String.valueOf(member.getBalance()));
                DB.disConnect();
            } else {
                DB.disConnect();
                if (d == 1) {

                } else {
                    //JOptionPane.showMessageDialog(null, "Card Number Not Found " + m.getCCN());
                    mbox.showAlert(Alert.AlertType.ERROR, stageV, "STOP", "Card Number Not Found " + member.getCCN() + " " + member.getCID());
                }
                return;
            }
        } catch (HeadlessException e) {
            return;
        }

        DB.disConnect();
        Platform.runLater(()->searchButton.requestFocus());
    }
    
    private void disableButtons(){
        cancelButton.setDisable(true);
    }
    
    private void enableButtons() {
        cancelButton.setDisable(false);
    }
    
    private void clearDetail() {
        try {
            if (detailPane.isVisible()) {
            memtickDetailTable.getItems().clear();
            detailPane.setVisible(false);                
            }                 
        } catch (HeadlessException e) {
        }
        
    }
    
    
    public void getDetailButtonPushed() {
        try {
            if (detailPane.isVisible()) {
                memtickDetailTable.getItems().clear();
                detailPane.setVisible(false);
            } else {
                //THIS IS GETTING THE MEMBER DETAIL    
                memtickDetailTable.getItems().clear();
                memtickDetailTable.getItems().addAll(DB.getDetail(staticMemberID));
                detailPane.setVisible(true);
                DB.disConnect();
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println(e);
        }

    }
    
    
    private void getFirstMember() {
        System.out.println("here --- - - - - " + newEFX.getNameF());
        cardNumberTextfield.setText("100001");
        fNameTextfield.setText("POJOS");
        lNameTextfield.setText("MEMBERS");
        addressTextfield.setText("7736 FAIRVIEW AVE");
        cityTextfield.setText("BOISE");
        stateTextfield.setText("ID");
        zipTextfield.setText("83642");
        birthDateTextfield.setValue(LocalDate.now());
        originalDateTextfield.setText(LocalDate.now().toString());
        phone1Textfield.setText("208");
        phone2Textfield.setText("376");
        phone3Textfield.setText("6981");
        staticMemberID = "235";
        ticketBalance.setText("0");
        //Platform.runLater(()->{
        //    try {
        //        m = DB.getMember("100001");
        //    } catch (SQLException ex) {
        //        System.out.println(ex);
        //    }
        //});


    }
    
    
    private void clearFeilds() {
        cardNumberTextfield.clear();
        fNameTextfield.clear();
        lNameTextfield.clear();
        addressTextfield.clear();
        cityTextfield.clear();
        stateTextfield.clear();
        zipTextfield.clear();
        birthDateTextfield.setValue(NOW);
        originalDateTextfield.clear();
        phone1Textfield.clear();
        phone2Textfield.clear();
        phone3Textfield.clear();
        ticketBalance.clear();
    }
    
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F3) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.DOWN) {keyListener(ke); ke.consume();}
     });   
    }
    
    private void enterKeyPressed() {
        /*if (editPane.isVisible()) {
            if (editPanePrizes.isFocused()) {
                editPaneDate.requestFocus();
                return;
            }
            if (editPaneTickets.isFocused()) {
                editPanePrizes.requestFocus();
                return;
            }
            if (editPaneCoins.isFocused()) {
                editPaneTickets.requestFocus();
                return;
            }
            if (editPaneSales.isFocused()) {
                editPaneCoins.requestFocus();
                return;
            }
            if (editPaneDate.isFocused()) {
                editPaneSales.requestFocus();
                return;
            }

        } */
        if (!editMode){
            searchButton.fire();
            searchButton.setText("Search F7");
        } else {
            if (phone3Textfield.isFocused()) {
                fNameTextfield.requestFocus();
                return;
            }
            if (fNameTextfield.isFocused()) {
                lNameTextfield.requestFocus();
                return;
            }
            if (lNameTextfield.isFocused()) {
                addressTextfield.requestFocus();
                return;
            }
            if (addressTextfield.isFocused()) {
                cityTextfield.requestFocus();
                return;
            }
            if (cityTextfield.isFocused()) {
                stateTextfield.requestFocus();
                return;
            }
            if (stateTextfield.isFocused()) {
                zipTextfield.requestFocus();
                return;
            }
            //if (zipTextfield.isFocused()) {birthDateTextfield.requestFocus(); return;}
            if (zipTextfield.isFocused()) {
                phone1Textfield.requestFocus();
                return;
            }
            //if (birthDateTextfield.isFocused()) {originalDateTextfield.requestFocus(); return;}
            if (originalDateTextfield.isFocused()) {
                phone1Textfield.requestFocus();
                return;
            }
            if (phone1Textfield.isFocused()) {
                phone2Textfield.requestFocus();
                return;
            }
            if (phone2Textfield.isFocused()) {
                phone3Textfield.requestFocus();
                return;
            }
        }
    }
    
    
    private boolean addUpdateMSG(String MSG) {
        boolean t = false;
        //System.out.println(new GetDay().getCurrentTimeStamp());
        d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText(), "");
        //  public Memtick(String memID, String EmpNumber, LocalDate DateTime, String time, Double added, Double Subtracted, String Location, String explain, String isuper, int tranid) {
        //mt = new Memtick(d.getCID(), eID, NOW, new GetDay().getCurrentTimeStamp(), 0.0, 0.0, MSG, getPopupReturnMessage(), "", 0);
        try {            
        if (!DB.InsertAccountChangeMessage(d.getCID(), getPopupReturnMessage(), new GetDay().getCurrentTimeStamp(), eID, "NEWCRD")) {
            //if (!DB.InsertData(mt)){
            }
            DB.disConnect();
            t = true;
        } catch (Exception e) {
            System.out.println("=================== failed" + e);
        }
        return t;
    }
   
   /* public void editPaneSaveButtonDo() throws SQLException {
       Memtick updateDetail = new Memtick(editPaneDate.getValue(), Double.parseDouble(editPaneSales.getText()), editPaneCoins.getText(), editPaneTickets.getText(), editPanePrizes.getText(), detailID);
        updateDetail.postDetail();
        gameDetailTable.getItems().addAll(getDetail(staticGameID));
        editButton.setDisable(false);
        searchButton.setDisable(false);
        printButton.setDisable(false);
        newButton.setDisable(false);
        editPane.setVisible(false);
        editButton.setText("Edit F9");
        return; 
    } 
    */
    
    
    public void editButtonGo() {
        try {
            checkFirstRecord(member.getCCN());
        } catch (IllegalArgumentException e) {
            mbox.showAlert(Alert.AlertType.ERROR, stageV, eID, e.toString());
            return;
        }
        try {
            if (EDITBDAY) {
                searchButton.requestFocus();
                d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", birthDateTextfield.getValue(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                makeAllNoEdit();
                if (!DB.upDateMemberFile(d)) {
                    DB.disConnect();
                    new Mail_JavaFX1().sendEmailTo("Update Member File Error", "Something went wrong with updating a member file", "errors");
                } else {
                    mt = new Memtick(d.getCID(), eID, new GetDay().getCurrentTimeStamp(), LocalDate.now(), null, null, null, "UPDATE", 0);
                    DB.InsertData(mt);
                    DB.disConnect();
                    getMemberAfterUpdate(d.getCCN(), 0);
                    Platform.runLater(()->searchButton.requestFocus());
                }
                return;
            } else {
            if (!editMode) {
                makeAllEdit();
                return;
            } else {
                if (checkForCloseAccount()) {
                    return;
                }
                //d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", m.getBdate(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText(), "");
                d = new Member(cardNumberTextfield.getText(), staticMemberID, fNameTextfield.getText(), lNameTextfield.getText(), addressTextfield.getText(), cityTextfield.getText(), stateTextfield.getText(), zipTextfield.getText(), "", member.getBdate(), ticketBalance.getText(), false, false, LocalDate.parse(originalDateTextfield.getText()), phone1Textfield.getText(), phone2Textfield.getText(), phone3Textfield.getText());
                makeAllNoEdit();
                if (!DB.upDateMemberFile(d)) {
                    DB.disConnect();
                    new Mail_JavaFX1().sendEmailTo("Update Member File Error", "Something went wrong with updating a member file", "errors");
                } else {
                    mt = new Memtick(d.getCID(), eID, new GetDay().getCurrentTimeStamp(), LocalDate.now(), null, null, null, "UPDATE", 0);
                    DB.InsertData(mt);
                    DB.disConnect();
                    getMemberAfterUpdate(d.getCCN(), 0);
                    Platform.runLater(()->searchButton.requestFocus());
                }
                    beenhereonce = 0;
           System.out.println("===================================== BEEN HERE ONCE 0 = " + beenhereonce);

                return;
            }
        }
        } catch(Exception e) {mbox.showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());}
    DB.disConnect();
    }
    
    
    private void checkFirstRecord(String CCN) {
        //System.out.println("here in checkfirst " + CCN);
        if (CCN.equals("100001")) {
            throw new IllegalArgumentException("\n This account cannot be edited");
        }
    }
    
    private boolean checkForCloseAccount() {
        boolean isClose = false;
        if (cityTextfield.getText().equals("CLOSE")) {
            int alert = mbox.confirmMakeThisChange(Alert.AlertType.ERROR, stageV, "WAIT!", "Please use the CLOSE ACCOUNT Menu Item on the Menu Button. \n Click any button to continue");
            //int alert = new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "WAIT!", "Please use the CLOSE ACCOUNT Menu Item on the Menu Button");
            //cancelButton.fire();
            makeAllNoEdit();
            //clearFields();
            PRTNumberButton.setDisable(false);
            printButton.setDisable(false);
            searchButton.setDisable(false);
            getMemberAfterUpdate(member.getCCN(), 0);
        }
        return isClose;
    }
    
    public void printNumberButtonPressed() {
        try {
            GOPRTNumberButton(cardNumberTextfield.getText());
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        
    }
    
    
    public void GOPRTNumberButton(String ccn) throws FileNotFoundException {

        PrintWriter pw = new PrintWriter(new File(dbsp.pathNameLocal + "MemberCCNPRT.txt"));
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println(" ");
            pw.println("");
            pw.println("");
            pw.println(fNameTextfield.getText() + " " + lNameTextfield.getText());
            pw.println(ccn);
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
            pr.printReceipt("MemberCCNPRT.txt");
    }
    
    public void GOPRTNumberButtonEsc(String ccn) throws IOException, PrintException {
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
        escpos.write(Format, "Employee: " + newEFX.getNameF());
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
        escpos.write(barcode, ccn);
        
       
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
        cardNumberTextfield.setStyle("-fx-background-color: Yellow");
        cardNumberTextfield.setDisable(true);
        originalDateTextfield.setDisable(true);
        ticketBalance.setDisable(true);
        editButton.setText("Save F9");
        searchButton.setDisable(true);
        PRTNumberButton.setDisable(true);
        printButton.setDisable(true);
        searchButton.setDisable(true);
        editMode = true;
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
    
    //taxableRadio.setDisable(true);
    PRTNumberButton.setDisable(false);
    printButton.setDisable(false);
    ticketBalance.setDisable(true);
    searchButton.setDisable(false);
    editButton.setText("Edit F9");
    editMode = false;
}  
    
    private void clearFields() {
        Set<Node> nodes = root.lookupAll(".text-field");
        for (Node node : nodes) {
            ((TextField) node).setText("");
        }
        mbox.showErrorClear(errorLabel, errorBar);
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
    
    private void setToUpper() {
        
        fNameTextfield.textProperty().addListener((ov, oldValue, newValue) -> {fNameTextfield.setText(newValue.toUpperCase());});
        lNameTextfield.textProperty().addListener((ov, oldValue, newValue) -> {lNameTextfield.setText(newValue.toUpperCase());});
        addressTextfield.textProperty().addListener((ov, oldValue, newValue) -> {addressTextfield.setText(newValue.toUpperCase());});
        cityTextfield.textProperty().addListener((ov, oldValue, newValue) -> {cityTextfield.setText(newValue.toUpperCase());});
        stateTextfield.textProperty().addListener((ov, oldValue, newValue) -> {stateTextfield.setText(newValue.toUpperCase());});
        //emailTextfield.textProperty().addListener((ov, oldValue, newValue) -> {emailTextfield.setText(newValue.toUpperCase());});
    }
    
    private void setHighLights() {
        fNameTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        lNameTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        addressTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        cityTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} searchButton.setDisable(false);});
        stateTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        zipTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        stateTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        birthDateTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        originalDateTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        phone1Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        phone2Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        phone3Textfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        
    }
    
    private void GOSearchInactive(ActionEvent event) throws IOException {
        SC.changePopUp(event, "/views/memInaciveTableView.fxml", "List of Inactive Members");
        //birthDateTextfield.requestFocus();
        searchButton.requestFocus();
        if (!SC.getGameNumber().equals("Number")){
            GOPRTNumberButton(SC.getGameNumber()); 
        }


    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
        case F1: break;
        case F2: break;
        case F3: detailButton.fire(); break;
        case F4: break;
        case F5: break;
        case F6: PRTNumberButton.fire(); break;
        case F7: searchButton.fire(); break;
        case F8: printButton.fire(); break;
        case F9: editButton.fire(); break;
        case F10: break;
        case F11: break;
        case F12: break;
        case ESCAPE: cancelButton.fire(); break;
        case ENTER: enterKeyPressed(); break;
        case DOWN: searchButton.fire();
break;
    default:
        break;
    }
    }
    
    public void LookUpButtonPressed(ActionEvent event) throws IOException {
        //SceneChanger SC = new SceneChanger();
        if (CLOSE) {
            //Platform.runLater(() -> RunClose());
            //new FXTimerBinding().start(stageV);
            CLOSE = false;
        }
        if (whichOne==0) {
            whichOne = 1;
        }
        if (cityTextfield.isFocused()) {
            Bounds boundsInScene = cityTextfield.localToScene(cityTextfield.getBoundsInLocal());
            SC.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "City", "Select One:", cityTextfield.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());            
            if (!SC.getGameNumber().equals("Location")) {
                cityTextfield.setText(SC.getGameNumber());
                cityTextfield.requestFocus();
            }
            return;
        }
        
        switch (whichOne) {
            case 1:
                getFirstMember();
                if (detailPane.isVisible()) {
                    detailPane.setVisible(false);
                    memtickDetailTable.getItems().clear();
                }
                    memSearchTableViewController wController = (memSearchTableViewController) FXLOADER.getController();
                    wController.sc = SC;
                    wController.dbsp = dbsp;
                    wController.cssPath = cssPath;
                    wController.conn = connectionMembers;
                    wController.FXLOADER = FXLOADER;
                    wController.lastMember = lastMember;
                    //wController.iLastTran = this.iLastTran;
                    //wController.rs = 
                SC.changePopUp(event, "/views/counterPopUp/memSearchTableView.fxml", "List of Members");
                //birthDateTextfield.requestFocus(); 
                searchButton.requestFocus();
                UtilMember utilmember = SC.GetUtilMember();
                member = new Member(utilmember.getCCN(), utilmember.getCID(), utilmember.getNameF(), utilmember.getNameL(), utilmember.getAddress(), utilmember.getCity(), utilmember.getState(), utilmember.getZip(), null, utilmember.getBdate(), String.valueOf(utilmember.getBalance()), null, null, utilmember.getOdate(), utilmember.getAreaCode(), utilmember.getPhone1(), utilmember.getPhone2(), "");
                //System.out.println("MEMBER = " + member.getCCN());
                fillMemberFeilds(member, 0);
                beenhereonce++;
                
                if (!SC.getGameNumber().trim().equals("Number")) {
                    try {
                    //System.out.println("here1 9/21/2023");
                        if (member.getCity().equals("INACTIVE")) {
                            member = DB.isInactiveMemberValidFromSearch(member.getCCN());
                            fillMemberFeilds(member, 0);
                        }
                        //getGame(SC.getGameNumber(), 1);
                        if (new settingsFXML().getSettings("Css", 2).equals("99")) {
                        //if (new settingsFXML().getCounterSettings("stage").equals("4")) {
                    //getCounterSettings
                    //System.out.println("here1 9/21/2023");
                            //Platform.runLater(()->{
                        //try {
                        //    GOPRTNumberButton(member.getCCN());
                        //} catch (FileNotFoundException ex) {
                        //    System.out.println(ex);
                        //}
                    //});
                        }
                        DB.disConnect();
                    } catch (SQLException ex) {
                        new Mail_JavaFX1().sendEmailTo("Error  DB.Java", "Something went wrong in transfering an inactive to the active " + ex.toString(), "errors");
                        System.out.println(ex);
                    }
                } else {
                    //System.out.println("here2 9/21/2023");
                        Platform.runLater(()->{
                        try {
                            GOPRTNumberButton(member.getCCN());
                        } catch (FileNotFoundException ex) {
                            System.out.println(ex);
                        }
                    });
                    //if (!SC.getGameNumber().trim().equals("Number")) {
                        //getGame(SC.getGameNumber(), 0);
                    //}
                }
                break;

        
        
            case 2:
        SC.changePopUp(event, "/views/memInactiveTableView.fxml", "List of Inactive Members");
        //birthDateTextfield.requestFocus();  
        searchButton.requestFocus();
        if (!SC.getGameNumber().trim().equals("Number")){
            GOPRTNumberButton(SC.getGameNumber());
            try {
                DB.isInactiveMemberValid(SC.getGameNumber().trim());
            } catch (SQLException ex) {
                new Mail_JavaFX1().sendEmailTo("Error  DB.Java", "Something went wrong in transfering an inactive to the active " + ex.toString(), "errors");
                System.out.println(ex);
            }
        }
        break;
        }
        whichOne = 0;
        }
    
    
    private void RunClose() {
        Thread rClose = new Thread() {
            @Override
            public void run() {
                //THIS FILE ONLY CLEANS OUT CLOSE AND LOCK NOT THE TEST ACCOUNTS LIKE GOLD CARD.
                System.out.println("here in RunCLose ");
                //new MoveToInactiveFileIconApp().runITMemberViewer();
            }
        };
        rClose.start();
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
    

    
    
    
    private void PrintReceipt(String cID) throws FileNotFoundException, SQLException {
        ResultSet allDetailRS = new DB().GetLastTransaction(cID);
        allDetailRS.last();
        if (allDetailRS.getDouble("Added")> 0) {
            mt = new Memtick(allDetailRS.getString("Member ID"), allDetailRS.getDate("Date").toLocalDate(), allDetailRS.getString("Time"), allDetailRS.getDouble("Added"), 0.0, "Edit");
        } else {
            mt = new Memtick(allDetailRS.getString("Member ID"), allDetailRS.getDate("Date").toLocalDate(), allDetailRS.getString("Time"), 0.0, allDetailRS.getDouble("Subtracted"), "Edit");            
        }
        PrintWriter pw = new PrintWriter(dbsp.pathNameLocal + "DepReceipt.txt");
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
        pw.println("");
        pw.println("Edit Balance / Transfer Receipt"); // to test if it works.
        pw.println("======================"); // to test if it works.
        pw.println("");
        pw.println("");
        pw.println(fNameTextfield.getText() + " " + lNameTextfield.getText());
        pw.println(" ");
        pw.println("Employee: " + newEFX.getNameF());
        pw.println("");
        pw.println("");
        pw.println("Prev Balance" + ":" + "\t" + DECIMALFORMAT.format(prevBalance));
        if (mt.getAdded() > mt.getBonus()) {
            pw.println("Added" + ":" + "\t" + DECIMALFORMAT.format(mt.getAdded()));
        } else {
            pw.println("Withdrawl" + ":" + "\t" + DECIMALFORMAT.format(mt.getBonus()));
        }
        pw.println("New Balance" +  ":" + "\t" + DECIMALFORMAT.format(Double.valueOf(ticketBalance.getText())));
        pw.println(" ");
        pw.println(" ");
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
        /*if (SCrollTextLable.isVisible()) {
            pw.println(" ");
            pw.println(" ");
            pw.println("You have an Email and we have");
            pw.println("sent a receipt to this Email Address");
            pw.println(isEMail.toUpperCase());
            pw.println(" ");
        }*/
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
        
        mt = null;
        prevBalance = Double.valueOf(ticketBalance.getText());
        allDetailRS.close();
        DB.disConnect();
        print pr = new print();
        pr.printReceipt("DepReceipt.txt");
    }

    
    public void exitButtonPushed(ActionEvent event) throws IOException, SQLException {
    stageV = (Stage) cancelButton.getScene().getWindow();
        if (detailPane.isVisible()) {
            detailPane.setVisible(false);
            memtickDetailTable.getItems().clear();
            return;

        }
        if (editMode) {
            //int n = JOptionPane.showConfirmDialog(null, "This will clear any changes, \n are you sure you want to do this?", "Clear New Changes?", JOptionPane.YES_NO_OPTION);
            //int n = new messageBox().showNewMemberAlert(Alert.AlertType.ERROR, stageV, "STOP", "This will clear any changes, \n are you sure you want to do this?");
            int n = new messageBox().confirmMakeThisChange(Alert.AlertType.ERROR, stageV, "STOP", "This will clear any changes, \n are you sure you want to do this?");
            if (n == 0) {
                makeAllNoEdit();
                clearFields();
                new messageBox().showErrorClear(errorLabel, errorBar);
                PRTNumberButton.setDisable(false);
                printButton.setDisable(false);
                searchButton.setDisable(false);
                getMemberAfterUpdate(member.getCCN(), 0);
                Platform.runLater(()->searchButton.requestFocus());
            } else {
              return;
            }
        } else {
        DB.disConnect();
        try {
            if (CLOSE) {
                stageV.close();
                System.out.println("at the close of the member view screen " + connectionMembers);
                Platform.runLater(() ->  RunClose());
                CLOSE = false;
                connectionMembers.close();
                connectionMembers = null;
                connectionOldDetail.close();
                connectionOldDetail = null;
                stageV.close();
            } else {
                System.out.println("at the close of the member view screen 2 " + connectionMembers);
                connectionMembers.close();
                connectionMembers = null;
                connectionOldDetail.close();
                connectionOldDetail = null;
                stageV.close();

            }

//sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeEFX().titleBar);
//DON'T CHANGE THIS EXCEPTION TO AN SQLEXCEPTION. IT WILL FAIL THE CODE TO CLOSE THE SCREEN
        } catch(Exception e) {  
            root.getChildren().clear();
            stageV.close();    
        }
        
        }
    }
        
}
