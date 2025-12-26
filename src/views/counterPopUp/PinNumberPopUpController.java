
package views.counterPopUp;

import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import messageBox.messageBox;
import models.club.DB;
import models.club.Member;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class PinNumberPopUpController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Button addPinNumberButton;
    @FXML private PasswordField accountNumberTF;
    @FXML private PasswordField newPinNumberTF;
    @FXML private PasswordField newPinNumberTF1;
    @FXML private Label transferToNameLabel;
    @FXML private Label transferToNameLabelAddress;  
    @FXML private HBox secondPinBox; 
    @FXML private Label nameLabel;
    @FXML private AnchorPane root;
    public static String en, fn, ln, empID, empName, MGR, updateVar, typeVar, CCN;
    public static Member gg;
    public static int vAmt;
    
    
    //employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    GetDay gd = new GetDay();
    IsItANumber iin = new IsItANumber();
    static String tMessage = "", cssPath = null, E = null, V = null;
    public static String css = null, s1 = "";
    Bounds bboundsInScene;
    private employeeFX eFXX;
    Double newBalance = 0.0, prevBalance= 0.0, depAmount = 0.0;
    public Member m = null;
    public Member mFrom = null;
    public static String TRANSFER_COMPLETE = "";
    private final String EMAIL_SUBJECT = "EditBalancePopUpController";
    private final String CLASS_NAME = PinNumberPopUpController.class.getName();
    java.text.SimpleDateFormat tdf = new java.text.SimpleDateFormat("hh:mm a");
    java.text.SimpleDateFormat ddf = new java.text.SimpleDateFormat("MM/dd/yyyy");

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        cssPath = cssC.cssPath();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        nameLabel.setText(empName);
        transferToNameLabel.setText("");
        accountNumberTF.setText(CCN);
        switch (typeVar) {
            case "1":
            addPinNumberButton.setText("Add Pin Number to Account");
            secondPinBox.setVisible(true);
            break;
            case "2":
            addPinNumberButton.setText("Validate Pin Number to Account");
            secondPinBox.setVisible(false);
            break;
            case "3":
            addPinNumberButton.setText("Change Pin Number to Account");
            secondPinBox.setVisible(true);
            break;
        }
        Platform.runLater(()->accountNumberTF.requestFocus());
        Platform.runLater(()->doEnterKey());
    }    
    
    
    public void AddPinNumberButtonPushed(ActionEvent event) {
        TRANSFER_COMPLETE = "";
        String FUNCTION_NAME = "AddPinNumberButtonPushed";
        dbsp.setName();
        FileInputStream pinNumberTxt;
        boolean foundPinNumber = false;
        String newPinNumber = newPinNumberTF.getText().trim();
        File pinNumberTxtDoc = new File(dbsp.pathNameExcel + "PinNumber.txt");
        
        switch (typeVar) {
            case "1": //THIS ONE IS NOT BEING USED AT THIS TIME
                try {
                    if (!new DB().SetPinNumberStart(m.getCID(), newPinNumber)) {
                        System.out.println("error while in " + FUNCTION_NAME + "(CLASS_NAME)");
                    } else {
                        Platform.runLater(() -> addPinNumberButton.setText("Pin Number Added"));
                        new messageBox().showAlert(Alert.AlertType.ERROR, null, "COMPLETE", "Member pin number Added");
                        Reset("T");
                        new DB().InsertAccountChangeMessage(m.getCID(), "Pin Number Added to this Account", new GetDay().getCurrentTimeStamp(), empID, "PINCHG");
                        cancelButton.fire();
                    }
                } catch (SQLException ex) {
                    System.out.println("error while in " + FUNCTION_NAME + "(CLASS_NAME)" + ex);
                    new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in " + FUNCTION_NAME + "(See " + CLASS_NAME + ")" + "\n" + new dbStringPath().localMachine + " " + ex, "errors");
                }
                break;
            case "2": //THIS IS THE ONE TO VALIDATE THE PIN NUMBER WHEN DOING A WITHDRAWAL
                if (new DB().ValidatePinUser(newPinNumber, accountNumberTF.getText().trim())) {
                    Reset("T");
                    cancelButton.fire();
                } else {
                    new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "11error while in " + FUNCTION_NAME + "(See " + CLASS_NAME + " Pin Number Entered: " + newPinNumber + " INVALID PIN NUMBER ENTERED)" + "\n" + new dbStringPath().localMachine, "errors");
                    new messageBox().showAlert(Alert.AlertType.ERROR, null, "ERROR", "NOT A VALID PIN NUMBER");
                    newPinNumberTF.clear();
                    newPinNumberTF.requestFocus();
                    //newPinNumberTF1.clear();
                    return;
                }
                break;
            case "3": //THIS IS THE ONE FOR ADDING OR CHANGING A PIN NUMBER
                        try {
                    if (!new DB().SetPinNumberStart(m.getCID(), newPinNumber)) {
                        System.out.println("error while in " + FUNCTION_NAME + "(CLASS_NAME)");
                        new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in " + FUNCTION_NAME + "(See " + CLASS_NAME + "Something When wrong with setting the new Pin Number " + newPinNumber + ")" + "\n" + new dbStringPath().localMachine, "errors");
                    } else {
                        Platform.runLater(() -> addPinNumberButton.setText("Pin Number Added"));
                        if (new DB().ValidatePinUser(newPinNumber, accountNumberTF.getText().trim())) {
                            //new messageBox().showAlert(Alert.AlertType.ERROR, null, "COMPLETE", "Member pin number Validated");
                        } else {
                            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in " + FUNCTION_NAME + "(See " + CLASS_NAME + " Pin Number: " + newPinNumber + " INVALID PIN NUMBER ENTERED)" + "\n" + new dbStringPath().localMachine, "errors");
                            new messageBox().showAlert(Alert.AlertType.ERROR, null, "COMPLETE", "INVALID PIN NUMBER");
                        }
                        Reset("T");
                        new DB().InsertAccountChangeMessage(m.getCID(), "Pin Number Changed on this Account", new GetDay().getCurrentTimeStamp(), empID, "PINCHG");
                        cancelButton.fire();
                    }
                } catch (SQLException ex) {
                    System.out.println("error while in " + FUNCTION_NAME + "(CLASS_NAME)" + ex);
                    new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error while in " + FUNCTION_NAME + "(See " + CLASS_NAME + ")" + "\n" + new dbStringPath().localMachine + " " + ex, "errors");
                }
                break;
        }

    }
    
    
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         //if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         //if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
         //if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ENTER) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: break;
                    case F4: break;
                    case F5: break;
                    case F6: printButton.fire(); break;
                    case F7: break;
                    case F8: break;
                    case F9: break;
                    case F10: break;
                    case F11: break;
                    case DOWN: break;
                    case UP: break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: doEnterKey(); break;
                default:
                    break;
                }
    } 
    
    private void GetMember(String CCN) {
        try {
            if (!new DB().isMemberValid(CCN)) {
                accountNumberTF.setText("");
                accountNumberTF.requestFocus();
            } else {
               m = new DB().getMember(CCN);
                if (!m.getNameF().equals("InValid")) {
                    transferToNameLabel.setVisible(true);
                    transferToNameLabel.setText(m.getNameF() + " " + m.getNameL() + "  / Phone: " + m.getPhoneCombined());
                    transferToNameLabelAddress.setText(m.getAddress() + " / Birthdate: " + ddf.format(m.getBdateSQL()));
                    accountNumberTF.setStyle("-fx-background-color: #D3D3D3");
                    accountNumberTF.setDisable(true);
                } else {
                    accountNumberTF.setText("");
                    accountNumberTF.requestFocus();
                    accountNumberTF.setDisable(false);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    
    
    public void setTransfer(String st) {
        this.TRANSFER_COMPLETE = st; 
    }
    
    public String getTransfer() {
        return TRANSFER_COMPLETE;
    }
    
    
    private void doEnterKey() {
        if (accountNumberTF.isFocused()) {
            GetMember(accountNumberTF.getText().trim());
            if (!typeVar.equals("2")) {
                if (!new DB().IsPinNumberExsist(m.getCCN())) {
                    addPinNumberButton.setText("ADD Pin Number to Account");
                } else {
                    addPinNumberButton.setText("CHG Pin Number to Account");
                }
            }
            newPinNumberTF.requestFocus();
            return;
        }
        if (newPinNumberTF.isFocused()) {
            if (typeVar.equals("2")) {
                addPinNumberButton.requestFocus();
                addPinNumberButton.fire();
            } else {
            newPinNumberTF1.requestFocus();
            }
            return;
        }
        if (newPinNumberTF1.isFocused()) {
            if (newPinNumberTF.getText().trim().equals(newPinNumberTF1.getText().trim())) {
                addPinNumberButton.requestFocus();
                addPinNumberButton.fire();
            } else {
                newPinNumberTF.clear();
                newPinNumberTF1.clear();
                newPinNumberTF.requestFocus();
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP", "The two pin numbers do not match! please try again.");
            }
            return;
        }
    }
    
    
    private void Reset(String st) {
        transferToNameLabel.setText("");
        accountNumberTF.setText("");
        accountNumberTF.setDisable(false);
        accountNumberTF.setStyle("-fx-background-color: #FFFFFF");
        accountNumberTF.requestFocus();
        transferToNameLabelAddress.setText("");
        newPinNumberTF1.clear();
        setTransfer(st);
    }
    
    
    
   public void exitButtonPushed(ActionEvent event) throws IOException {
       if (!accountNumberTF.getText().trim().isEmpty()) {
           Reset("CANCEL");
       } else {
           en = null;
           fn = null;
           ln = null;
           sc.changePopUp1(event, getTransfer());
           //sc.changePopUp(event, "", "CANCEL");
           //Stage stageV = (Stage) cancelButton.getScene().getWindow();
           //stageV.close();
       }
     } 

    
}
