
package views.counterPopUp;

import XML_Code.AddXmlNode;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import messageBox.messageBox;
import models.club.CheckBalanceDB;
import models.club.DB;
import models.club.Member;
import models.club.Memtick;
import models.club.rCeipts;
import models.club.AdditionalCards;
import sceneChangerFX.SceneChanger_Main;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class AdditionalCardPopUpController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Button addPinNumberButton;
    @FXML private PasswordField accountNumberTF;
    @FXML private TextField newPinNumberTF;
    @FXML private Label transferToNameLabel;
    @FXML private Label nameLabel;
    @FXML private AnchorPane root; 


    private static final java.text.SimpleDateFormat tdf = new java.text.SimpleDateFormat("hh:mm a");
    private static final java.text.SimpleDateFormat ddf = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final GetDay gd = new GetDay();
    private static final IsItANumber iin = new IsItANumber();


    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    public static ArrayList<rCeipts> rCeipts;
    public static DB db;
    public static messageBox mBox;
    public static CheckBalanceDB chkbalance;
    public static Member m;
    public static Memtick mt;
    public static String cssPath; 
    public static String CCN;

    //public static Member gg;



    private static String en, fn, ln;
    private static String  empName, rNumber;
    public static String empID, typeVar;
    //public static int vAmt;
    private static String tMessage = "",atf = "";
    //private static String  E = null, V = null;
    //private static String s1 = "";
    //private static Bounds bboundsInScene;
    private static Double newBalance = 0.0, prevBalance= 0.0, depAmount = 0.0;
    //public static Member mFrom = null;
    public static String TRANSFER_COMPLETE = "";
    //private final String EMAIL_SUBJECT = "EditEmailPopUpController";
    //private final String CLASS_NAME = EmailEditorPopUpController.class.getName();

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        System.out.println(empID);
        new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        nameLabel.setText(empName);
        transferToNameLabel.setText("");
        accountNumberTF.setText(CCN);
        System.out.println("Customer Card Number " + CCN);
        Platform.runLater(()->accountNumberTF.requestFocus());
        try {
            if (!CCN.isEmpty()) {
                Platform.runLater(()->doEnterKey());
            }
        } catch(Exception e) {}
    }    
    
    
    public void AddPinNumberButtonPushed(ActionEvent event) {
        TRANSFER_COMPLETE = "";
        checkFeilds();
        dbsp.setName();
        String newEmail = "";
        Member mm = null;
        try {
            newEmail = newPinNumberTF.getText().trim();
        } catch(Exception e) {
            newEmail = "";
        }
        try {
            mm = db.getMember(newEmail);
            System.out.println(mm.getNameF());
        } catch(Exception e) {
            newEmail = "";
        }
        
        if (!mm.getNameF().equals("inValid")) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Card you are adding to this account is already in use! Try another Card.");
            newPinNumberTF.clear();
            return;
        }
        mt = new Memtick(m.getCID(), gd.getCurrentTimeStamp(), LocalDate.now(),0.0, "");
        switch(typeVar) {
            case "1":
                //CHANGE CURRENT EMAIL
            break;
            case "2":
                //ADD NEW Card Number
                if (newEmail.isEmpty()) {
                    new messageBox().showAlert(Alert.AlertType.ERROR, null, "Alert!", "You must enter a Valid Card Number \nto add an Additional Card to this account.");
                    newPinNumberTF.requestFocus();
                } else {
                //db.putNewMemberEmail(m, newEmail);
                //HERE IS WEHRE THE NEW CODE GOES
                AdditionalCards newCard = new AdditionalCards(m.getCCN(), m.getCID(), String.valueOf(newEmail));
            try {
                new AddXmlNode().addXMLAdditionalCard(new dbStringPath().pathNameClubDBs + "\\XML\\AdditionalCards.xml", newCard);
            } catch (Exception ex) {
                System.out.println(ex);
            }
                db.InsertAccountChangeMessage(m.getCID(), "Additional Card Number: " + newEmail, new GetDay().getCurrentTimeStamp(), empID, "ADDEDCARD");
                Memtick newDetail = new Memtick(m.getCID(), empID, gd.getCurrentTimeStamp(), LocalDate.now(), 0.0, 0.0, 0.0, "ADDEDCARD", 0);
                db.InsertDataCleanDetailTraingingCard(newDetail);
                runcheckBalance(m, newBalance, mt, en, fn, ln, "I"); //RUNCHECKBALANCE HAS ITS OWN MODEL IN MODELS
                Reset("CANCEL");
                cancelButton.fire();
                }
            break;
        }
        
    }
    
    
    
    
    private void runcheckBalance(Member m, Double newBalance, Memtick mt, String empN, String fName, String lName, String type) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {

                if (m.getCCN().equals("4000001") || m.getCCN().equals("4000008")) {
                    String EmailAdd1 = getReceipts("EmailAdd1");
                    if (getrNumber().equals("1.0")) {
                        
                    } else {
                        EmailAdd1 = "";
                    }
                    
                    chkbalance.sendEmailUpdate(m, newBalance, prevBalance, fn, EmailAdd1, type, null, null, rCeipts);
                }
            }
        };
        thread1.start();
    }    
    
    
    private void setrNumber(String r) {
        
        this.rNumber = r;
    }
    
    private String getrNumber() {
        return this.rNumber;
    }
    
    
    private String getReceipts(String n) {
        String StringItem = null;
        for (int y = 0; y < rCeipts.size(); y++) {
            if (n.trim().equals(rCeipts.get(y).getrItem())) {
                System.out.println(rCeipts.get(y).getrItem() + " " + rCeipts.get(y).getrString() + " " + rCeipts.get(y).getrNumber());
                setrNumber(rCeipts.get(y).getrNumber());
                StringItem = rCeipts.get(y).getrString();
            }
        }
        return StringItem;
    }
    
    
    
    
    
    
    
    
    
    
    private void checkFeilds() {
        try {
            atf = accountNumberTF.getText().trim();            
        } catch(Exception e) {
            
        }
        if (!atf.isEmpty()) {
        
        } else {
            mBox.showAlert(Alert.AlertType.ERROR, null, "Error", "You Must first enter an account number.");
            accountNumberTF.requestFocus();
            return;
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
            //if (!new DB().isMemberValid(CCN.trim())) {
            //    accountNumberTF.setText("");
            //    accountNumberTF.requestFocus();
            //    new messageBox().showAlert(Alert.AlertType.ERROR, null, "Error", "Not a Valid Account Number, Please try again.");
            //    return;
            //} else {
               m = db.getMember(CCN);
                if (!m.getNameF().equals("inValid")) {
                    transferToNameLabel.setVisible(true);
                    if (!m.getNameL().equals("FASTPASS")) {
                        if (!m.getPhoneCombined().equals("()-")) {
                            System.out.println("this is phone1 " + m.getPhoneCombined() + " " + m.getNameF());
                            transferToNameLabel.setText(m.getNameF() + " " + m.getNameL() + " Phone: " + m.getPhoneCombined());
                        } else {
                            System.out.println("this is phone2 " + m.getPhoneCombined() + " " + m.getNameF());
                            transferToNameLabel.setText(m.getNameF() + " " + m.getNameL() + " Phone: 000-000-0000");
                        }
                    } else {
                        transferToNameLabel.setText(m.getNameF() + " Phone: " + m.getPhoneCombined());
                    }
                    newPinNumberTF.setText("");
                    
                    
                    try {
                    if (!newPinNumberTF.getText().isEmpty()) {
                        addPinNumberButton.setText("Change / Delete Current Email");
                        tMessage = newPinNumberTF.getText();
                        typeVar = "1"; //CHANGE CURRENT EMAIL
                    } else {
                        addPinNumberButton.setText("Add New Card Number");
                        tMessage = "NONE";
                        typeVar = "2"; //ADD NEW EMAIL                                
                    }                        
                    }catch(Exception e) {
                        addPinNumberButton.setText("Add New Card Number");                    
                        tMessage = newPinNumberTF.getText();
                        typeVar = "2"; //ADD NEW EMAIL                                
                    }
                
                    accountNumberTF.setStyle("-fx-background-color: #D3D3D3");
                    accountNumberTF.setDisable(true);
                } else {
                    Reset(null);
                    cancelButton.fire();
                }
            //}
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
            checkFeilds();
            GetMember(accountNumberTF.getText().trim());
            //newPinNumberTF.requestFocus();
            return;
        }
        if (newPinNumberTF.isFocused()) {
            addPinNumberButton.requestFocus();
            addPinNumberButton.fire();
            return;
        }
    }
    
    
    private void Reset(String st) {
        transferToNameLabel.setText("");
        accountNumberTF.setText("");
        accountNumberTF.setDisable(false);
        accountNumberTF.setStyle("-fx-background-color: #FFFFFF");
        newPinNumberTF.clear();
        setTransfer(st);
    }
    
    
    
   public void exitButtonPushed(ActionEvent event) throws IOException {
       try {
           if (!accountNumberTF.getText().trim().isEmpty()) {
               Reset("CANCEL");
           } else {
               en = null;
               fn = null;
               ln = null;
               sc.changePopUp1(event, getTransfer());
           }
       } catch (Exception e) {
           Reset("CANCEL");
           en = null;
           fn = null;
           ln = null;
           sc.changePopUp1(event, getTransfer());

       }
     } 

    
}
