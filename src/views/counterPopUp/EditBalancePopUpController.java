
package views.counterPopUp;

import JavaMail.Mail_JavaFX1;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messageBox.messageBox;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class EditBalancePopUpController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private PasswordField accountNumberTF;
    @FXML private TextField addedTF;
    @FXML private Label errorLabel;
    @FXML private Pane errorPane;
    @FXML private TextField subtractedTF;
    @FXML private Label nameLabel;
    @FXML private Label transferToNameLabel;
    @FXML private Label transferFromBalance;
    @FXML private TextArea notesTF;
    @FXML private AnchorPane root;
    
    
    public static String en, fn, ln, empID, cssPath, empName, MGR, updateVar, typeVar;
    public static DB db;
    public static Member m = null;
    public static messageBox mBox;
    public static Member mFrom;
    public static Memtick memtick;
    //public static employeeFX eFXX;
    //public static SceneChanger_Main sc = new SceneChanger_Main();
    //private static final cssChanger cssC = new cssChanger();
    public static dbStringPath dbsp;
    //private static final GetDay gd = new GetDay();
    private static Double layoutX, layoutY;
    //private static final IsItANumber iin = new IsItANumber();
    private static String tMessage = ""; //E = null, V = null;
    private static String s1 = "";
    //private static Bounds bboundsInScene;
    private static Double newBalance = 0.0, prevBalance= 0.0, depAmount = 0.0;
    private static boolean TRANSFER_COMPLETE = false, NEGATIVE_ERROR = false;
    private static final String EMAIL_SUBJECT = "EditBalancePopUpController";
    private static final java.text.SimpleDateFormat tdf = new java.text.SimpleDateFormat("hh:mm a");
    //private static final java.text.SimpleDateFormat ddf = new java.text.SimpleDateFormat("MM/dd/yyyy");

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        layoutX = errorPane.getLayoutX();
        layoutY = errorPane.getLayoutY();

        subtractedTF.setVisible(true);
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        transferToNameLabel.setVisible(false);
        transferFromBalance.setVisible(false);
        setToUpper();
        //new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        //new FocusedTextFieldHighlight().
        setHighlightListenerVIP(root);
        setHighlightListenerVIPNotes(root);
        highlightTextFieldVIPNotes(root);
        highlightTextFieldVIP(root);
        nameLabel.setText(empName);
        notesTF.setText(tMessage);
        //mBox.showError("Enter Account number you are transfering tickets from or got to the next feild.", errorLabel, errorPane);
        Platform.runLater(() -> accountNumberTF.requestFocus());
    }

    public void printButtonPushed(ActionEvent event) {
        java.util.Date dt = new java.util.Date();
        String currentTime = tdf.format(dt);
        //======================================
        if (notesTF.getText().trim().isEmpty()) {  //CHECKING FOR EMPTY REASON
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP", "You must put in a reason for the editing of this account.");
            notesTF.requestFocus();
            return;
        } else {
            s1 = notesTF.getText().trim();
            s1 = s1.replaceAll("'", "");
        } //END OF CHECKING FOR EMPTY REASON


        //===============================================THIS IS A TRANSFER FROM ANOTHER ACCOUNT
        if (!accountNumberTF.getText().trim().isEmpty()) { 
            depAmount = Double.parseDouble(addedTF.getText().trim());
            memtick = new Memtick(m.getCID(), empID, new GetDay().getCurrentTimeStamp(), LocalDate.now(), depAmount, 0.0, 0.0, "EDITBAL", 0);
            prevBalance = ((double) mFrom.getBalance());
            newBalance = prevBalance - memtick.getSubtracted();
            prevBalance = ((double) m.getBalance());
            if (newBalance<0) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "STOP! \nWithdrawal amount creates a negative balance in the Transfer to Account!\n" + m.getNameF() + " " + m.getNameL());
                return;
            }
            Double transferFromNewBalance = Double.parseDouble(transferFromBalance.getText())-depAmount;
            if (transferFromNewBalance<0) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "STOP! \nWithdrawal amount creates a negative balance in the Transfer From Account!");
                addedTF.setDisable(false);
                addedTF.setStyle("-fx-background-color: #D3D3D3");
                addedTF.setText("");
                addedTF.requestFocus();
                NEGATIVE_ERROR = true;
                return;
            }
            newBalance = prevBalance + memtick.getAdded();
            if (!db.insertDataTicketBalanceCombinedTransfer(memtick, newBalance)) {
                System.out.println("error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)");
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in EditBalance insertDataTicketBalanceCombined (See EditBalancePopUpController)" + "\n" + new dbStringPath().localMachine, "errors");
            }
            s1 += "\n To Account: " + m.getCCN() + "\n";
            if (!db.InsertAccountChangeMessage(m.getCID(), s1, new GetDay().getCurrentTimeStamp(), empID, "EDITBAL")) {
                System.out.println("failure");
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in EditBalance insertDataTicketBalanceCombined (See EditBalancePopUpController)" + "\n" + new dbStringPath().localMachine, "errors");
            }

            memtick = new Memtick(mFrom.getCID(), empID, new GetDay().getCurrentTimeStamp(), LocalDate.now(), 0.0, 0.0, depAmount, "EDITBAL", 0);
            if (!db.insertDataTicketBalanceCombinedTransfer(memtick, transferFromNewBalance)) {
                System.out.println("error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)");
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in EditBalance insertDataTicketBalanceCombined (See EditBalancePopUpController)" + "\n" + new dbStringPath().localMachine, "errors");
            }
            s1 += " From Account: " + mFrom.getCCN();
            if (!db.InsertAccountChangeMessage(mFrom.getCID(), s1, new GetDay().getCurrentTimeStamp(), empID, "EDITBAL")) {
                System.out.println("failure");
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in EditBalance insertDataTicketBalanceCombined (See EditBalancePopUpController)" + "\n" + new dbStringPath().localMachine, "errors");
            }
            Reset();
            cancelButton.fire();
        } //END OF IF (ACCOUNT NUMBER TEXT FEILD EMPTY)
        
        
        
        
        //=======================================THIS IS ADDING TICKETS TO AN ACCOUNT
        if (!addedTF.getText().trim().isEmpty()) { 
            depAmount = Double.parseDouble(addedTF.getText().trim());
            memtick = new Memtick(m.getCID(), empID, new GetDay().getCurrentTimeStamp(), LocalDate.now(), depAmount, 0.0, 0.0, "EDITBAL", 0);
            prevBalance = ((double) m.getBalance());
            newBalance = prevBalance + memtick.getAdded();
            if (!db.insertDataTicketBalanceCombined(memtick, newBalance)) {
                System.out.println("error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)");
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in EditBalance insertDataTicketBalanceCombined (See EditBalancePopUpController)" + "\n" + new dbStringPath().localMachine, "errors");
            }
            if (!db.InsertAccountChangeMessage(m.getCID(), s1, new GetDay().getCurrentTimeStamp(), empID, "EDITBAL")) {
                System.out.println("failure");
            }
            Reset();
            cancelButton.fire();
        } //END OF IF (!ADDED TEXT FEILD EMPTY)
        
        
        
        
        //============================================THIS IS WITHDRAWING TICKETS FROM AN ACCOUNT
        if (!subtractedTF.getText().trim().isEmpty()) { 
            depAmount = Double.parseDouble(subtractedTF.getText().trim());
            memtick = new Memtick(m.getCID(), empID, new GetDay().getCurrentTimeStamp(), LocalDate.now(), 0.0, 0.0, depAmount, "EDITBAL", 0);
            prevBalance = ((double) m.getBalance());
            newBalance = prevBalance - memtick.getSubtracted();
            if (newBalance<0) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "STOP! \nWithdrawal amount creates a negative balance!");
                subtractedTF.setDisable(false);
                subtractedTF.setStyle("-fx-background-color: #D3D3D3");
                subtractedTF.clear();
                subtractedTF.requestFocus();
                return;
            }
            if (!db.insertDataTicketBalanceCombined(memtick, newBalance)) {
                System.out.println("error while in Deposit insertDataTicketBalanceCombined (See MemDepositFXController)");
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in EditBalance insertDataTicketBalanceCombined (See EditBalancePopUpController)" + "\n" + new dbStringPath().localMachine, "errors");
            }
            if (!db.InsertAccountChangeMessage(m.getCID(), notesTF.getText(), new GetDay().getCurrentTimeStamp(), empID, "EDITBAL")) {
                System.out.println("failure");
            }
            Reset();
            cancelButton.fire();
        } // END OF IF (!WITHDRAWAL FEILD EMPTY)

    }
    
    
    
    
    

    public void setHighlightListenerVIPNotes(Pane r) {
        Set<Node> nodesa = r.lookupAll(".text-area");
        for (Node node : nodesa) {
            node.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    highlightTextFieldVIPNotes(r);
                }
            });
        }
    }

    public void highlightTextFieldVIPNotes(Pane r) {
        Set<Node> nodesa = r.lookupAll(".text-area");
        for (Node node : nodesa) {
            if (node.isFocused()) {
                //node.setStyle("-fx-background-color: #fdfdaf; -fx-text-fill: #000000");
                notesTF.setStyle("-fx-background-color: #fdfdaf; -fx-text-fill: #000000");

                //System.out.println(node.getId());
                checkFocus(node.getId());
            } else {
                if (!node.isDisable()) {
                    node.setStyle("-fx-background-color: White");
                }
            }
        }
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
                node.setStyle("-fx-background-color: #fdfdaf; -fx-text-fill: #000000");
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
            case "accountNumberTF":
                mBox.showError("Please scan or Type in the Customer Card Number, press ENTER or touch the next field to transfer tickets from another account", errorLabel, errorPane);
                break;
            case "addedTF":
                if (!NEGATIVE_ERROR) {
                    if (!accountNumberTF.getText().isEmpty()) {
                        GetMember(accountNumberTF.getText().trim());
                        mBox.showError("Press Enter to type notes in if you are transfering all tickets from the account. type in the amount you are transfering if the amount is less the the full balance.", errorLabel, errorPane);
                    } else {
                        mBox.showError("Enter the Amount you want to add to this account. This will not transfer any tickets from another account and there will be no BONUS added", errorLabel, errorPane);
                    }
                }
                break;
            case "subtractedTF":
                mBox.showError("Enter the Amount you want to withdrawal from this account. This will not transfer any tickets from another account and no Sales items will be added.", errorLabel, errorPane);
                break;
            case "notesTF":
                if (!addedTF.getText().isEmpty() || !subtractedTF.getText().isEmpty()) {
                    accountNumberTF.setDisable(true);
                    addedTF.setDisable(true);
                    subtractedTF.setDisable(true);
                    addedTF.setStyle("-fx-background-color: #D3D3D3");
                    accountNumberTF.setStyle("-fx-background-color: #D3D3D3");
                    subtractedTF.setStyle("-fx-background-color: #D3D3D3");
                    mBox.showError("Type in the reason you are editing / transfering tickets on this account.", errorLabel, errorPane);
                } else {
                    mBox.showError("You must enter an amount to be added or subtracted from this account.", errorLabel, errorPane);
                }
                break;
        }
    }

    private void setToUpper() {
        for (Node node : root.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).textProperty().addListener((ov, oldValue, newValue) -> {
                    ((TextField) node).setText(newValue.toUpperCase());
                });
            }
        }
        notesTF.textProperty().addListener((ov, oldValue, newValue) -> {
            notesTF.setText(newValue.toUpperCase());
        });
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F6) {
                keyListener(ke);
                ke.consume();
            }
            //if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
            //if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
            //if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.ENTER) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
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
                printButton.fire();
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
            case DOWN:
                break;
            case UP:
                break;
            case ESCAPE:
                cancelButton.fire();
                break;
            case ENTER:
                doEnterKey();
                break;
            default:
                break;
        }
    }

    private void GetMember(String CCN) {
        try {
            //if (!db.isMemberValid(CCN)) {
            //    mBox.showAlert(Alert.AlertType.ERROR, null, "Error", "Invalid account Number. Also you cannot transfer from inactive members. you must first activate the inactive member account.");
            //    accountNumberTF.clear();
            //    Platform.runLater(() -> accountNumberTF.requestFocus());
            //} else {
                mFrom = db.getMemberTransfer(CCN);
                if (!mFrom.getNameF().equals("InValid")) {
                    transferToNameLabel.setText(mFrom.getNameF() + " " + mFrom.getNameL() + " Balance: ");
                    transferToNameLabel.setVisible(true);
                    transferFromBalance.setVisible(true);
                    addedTF.setText(String.valueOf(mFrom.getBalance()));
                    transferFromBalance.setText(String.valueOf(mFrom.getBalance()));
                    accountNumberTF.setStyle("-fx-background-color: #D3D3D3");
                    //subtractedTF.setStyle("-fx-background-color: #D3D3D3");
                    //accountNumberTF.setDisable(true);
                    //addedTF.setDisable(false);
                    subtractedTF.setVisible(false);
                    addedTF.requestFocus();
                } else {
                    mBox.showAlert(Alert.AlertType.ERROR, null, "Error", "Invalid account Number. Also you cannot transfer from inactive members. you must first activate the inactive member account.");
                    accountNumberTF.clear();
                    Platform.runLater(() -> accountNumberTF.requestFocus());
                }
            //}
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    private void doEnterKey() {
        if (accountNumberTF.isFocused()) {
            addedTF.requestFocus();
            return;
        }
        if (accountNumberTF.isFocused() && !accountNumberTF.getText().trim().isEmpty()) {
            GetMember(accountNumberTF.getText().trim());
            addedTF.requestFocus();
            return;
        }
        if (addedTF.isFocused()) {
            if (addedTF.getText().trim().isEmpty()) {
                subtractedTF.requestFocus();
                return;
            } 
            if (addedTF.isFocused() && !addedTF.getText().trim().isEmpty()){
                subtractedTF.setDisable(true);
                subtractedTF.setStyle("-fx-background-color: #D3D3D3");
                accountNumberTF.setStyle("-fx-background-color: #D3D3D3");
                notesTF.requestFocus();
                return;
            }
        }
        if (subtractedTF.isFocused()) {
            if (subtractedTF.getText().trim().isEmpty()) {
                notesTF.requestFocus();
                return;
            } 
            if (subtractedTF.isFocused() && !subtractedTF.getText().trim().isEmpty()) {
                addedTF.setDisable(true);
                addedTF.setStyle("-fx-background-color: #D3D3D3");
                accountNumberTF.setStyle("-fx-background-color: #D3D3D3");
                notesTF.requestFocus();
            }
        }
        if (notesTF.isFocused()) {
            accountNumberTF.requestFocus();
        }

    }

    private void Reset() {
        transferToNameLabel.setText("");
        transferFromBalance.setText("");
        accountNumberTF.setText("");
        addedTF.setText("");
        subtractedTF.setText("");
        accountNumberTF.setDisable(false);
        addedTF.setDisable(false);
        subtractedTF.setDisable(false);
        accountNumberTF.requestFocus();
        addedTF.setStyle("-fx-background-color: #FFFFFF");
        accountNumberTF.setStyle("-fx-background-color: #FFFFFF");
        subtractedTF.setStyle("-fx-background-color: #FFFFFF");
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        if (!accountNumberTF.getText().trim().isEmpty() || !addedTF.getText().trim().isEmpty() || !subtractedTF.getText().trim().isEmpty()) {
            Reset();
        } else {

            en = null;
            fn = null;
            ln = null;
            Stage stageV = (Stage) cancelButton.getScene().getWindow();
            stageV.close();
        }
    }

    
}
