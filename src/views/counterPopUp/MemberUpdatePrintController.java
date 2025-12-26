package views.counterPopUp;

import JavaMail.Mail_JavaFX1;
import commoncodes.ClubFunctions;
import commoncodes.FocusedTextFieldHighlight;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.club.DB;
import models.club.Member;
import models.club.rCeipts;
import pWordFX.empFX;
import sceneChangerFX.SceneChanger_Main;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class MemberUpdatePrintController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Label purposeTF; //THIS IS THE NAME FIELD
    @FXML private Label nameLabel;
    @FXML private Label notesTF;
    @FXML private AnchorPane root;


    public static String en, fn, ln, empID, empName, MGR, updateVar, cID, mfn, mln;
    public static SceneChanger_Main sc;
    public static String cssPath;
    public static dbStringPath dbsp;
    public static String memberString;
    public static Member m;
    public static DB DB;
    public static String MSG;
    private Stage stageV;
    public static PrintWriter pw;
    public static ArrayList<rCeipts> rCeipts;
    private static String gameNumber = "Number";
    public static ArrayList<empFX> E;
    public static String tranTime;
    public empFX newEFX = null;
    //private static final GetDay gd = new GetDay();
    //IsItANumber iin = new IsItANumber();
    private static String tMessage = "", V = null, s1 = null;
    //public static String css = null;
    private static Bounds bboundsInScene;
    //private employeeFX eFXX;
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
        bboundsInScene = nameLabel.localToScene(nameLabel.getBoundsInLocal());
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        setToUpper();
        new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        addTextfieldListeners();
        purposeTF.setText(empName);
        notesTF.setText(MSG);
        Platform.runLater(() -> printButton.requestFocus());
        //Platform.runLater(() -> System.out.println("Customer ID: " + cID + " Emp ID" + empID + " EMp Name " + fn));

    }

    public void printButtonPushed(ActionEvent event) {
        String Email = "";
        
        java.util.Date dt = new java.util.Date();
        String currentTime = tdf.format(dt);

        try {
            if(!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

        
        if (empID.trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Employee Name is invalid");
            return;
        }

        //START HERE TO GET THE INFORMATION IN THE OLDDETAIL LIKE YOU DO WITH THE LOCK NOTES.
            try {
            Email = DB.getEmail(m.getCID()).toUpperCase();
            } catch (Exception e) {
                //System.out.println("=====================================================");
                Email = "NONE";
            }
            if (Email != null || !"".equals(Email)) {
               
            } else {
                Email = "NONE";
            }
        String timeID = new ClubFunctions().formatTimeStringtoNumber(tranTime);
        DB.printMemberUpdate(m, newEFX, Email, pw, rCeipts, timeID);
        if (!DB.InsertHistoricDataOneTimeXplain(m.getCID(), newEFX.getEmpNumber(), tranTime, "UpDPrint", "Printed By: " + newEFX.getNameF(), timeID)) {
        //if (!DB.Insert10Thou(m.getCID(), newEFX.getEmpNumber(), tranTime, "UpDPrint")) {
       
        }
        cancelButton.fire();
    }
    
    
    
    
    
    
    
    
    
    private void setGameNumber(String s1){
        this.gameNumber = s1;
    }

    public String getGameNumber(){
        return gameNumber;
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

    public void addTextfieldListeners() {
        purposeTF.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        //iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Voucher Amount can only be Numbers");
                        //vAmountField.clear();
                        //vAmountField.requestFocus();
                        return;
                    }
                }
        );
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
                printButton.fire();
                break;
            default:
                break;
        }
    }

    
    
    
    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        sc.setGameNumber(null);
        getStage();
        sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 650.0, 250.0);
        if (!isEMPValidInArrayList(sc.getGameNumber())) {
            GO = false;
        } else {
            GO = true;
        }
        return GO;
    }

    
     private void getStage() {
        try {
            stageV = (Stage) printButton.getScene().getWindow();
        } catch (Exception e) {
        }
    }
     
     private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        for (int y = 0; y < E.size(); y++) {
            if (n.equals(E.get(y).getEmpNumber())) {
                empValid = true;
                newEFX = new empFX(E.get(y).getNameF(), E.get(y).getNameL(), E.get(y).getEmpNumber(), E.get(y).getVAmt(), E.get(y).getBdayresos(), E.get(y).getChangerEdit(), E.get(y).getEmpID(), E.get(y).getGProb(), E.get(y).getActive(), E.get(y).getArcade(), E.get(y).getBcarsales());

                en = E.get(y).getEmpNumber();
                fn = E.get(y).getNameF();
                ln = E.get(y).getNameL();
                //enGprob = E.get(y).getGProb();
                //enID = E.get(y).getEmpID();
            }
        }
        return empValid;
    }

    
    
    private void doEnterKey() {
        if (purposeTF.isFocused()) {
            notesTF.requestFocus();
            return;
        }
        if (notesTF.isFocused()) {
            purposeTF.requestFocus();
            return;
        }
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        en = null;
        //fn = null;
        ln = null;
        empID = null;
        //sc.changePopUp1(event, getGameNumber());
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        stageV.close();
    }

    

   
    
}
