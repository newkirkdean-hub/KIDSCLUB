package views.counterPopUp;

import JavaMail.Mail_JavaFX1;
import commoncodes.FocusedTextFieldHighlight;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
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
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.club.DB;
import models.club.Member;
import sceneChangerFX.SceneChanger_Main;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class MemberNotesPopUpController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Label purposeTF; //THIS IS THE NAME FIELD
    @FXML private Label nameLabel;
    @FXML private TextArea notesTF;
    @FXML private AnchorPane root;


    public static String en, fn, ln, empID, empName, MGR, updateVar, cID, mfn, mln;
    public static SceneChanger_Main sc;
    public static String cssPath;
    public static dbStringPath dbsp;
    public static String memberString;
    public static Member m;
    private static String gameNumber = "Number";
    //public static ArrayList<empFX> EE;
    //private static final GetDay gd = new GetDay();
    //IsItANumber iin = new IsItANumber();
    private static String tMessage = "", E = null, V = null, s1 = null;
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
        purposeTF.setText(mfn + " " + mln);
        Platform.runLater(() -> notesTF.requestFocus());
        Platform.runLater(() -> System.out.println("Customer ID: " + cID + " Emp ID" + empID + " EMp Name " + fn));

    }

    public void printButtonPushed(ActionEvent event) {
        java.util.Date dt = new java.util.Date();
        String currentTime = tdf.format(dt);
        if (purposeTF.getText().trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Name Field Cannot be Blank.");
            nameLabel.setText(mfn + " " + mln);
            notesTF.requestFocus();
            return;
        }
        if (notesTF.getText().trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Notes Field Cannot be Blank.");
            notesTF.requestFocus();
            return;
        }
        if (empID.trim().isEmpty()) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "STOP!", "The Employee Name is invalid");

            return;
        }

        //START HERE TO GET THE INFORMATION IN THE OLDDETAIL LIKE YOU DO WITH THE LOCK NOTES.
        if (!purposeTF.getText().isEmpty()) {
            s1 = notesTF.getText().trim();
            memberString = "MEMBER NAME: " + m.getNameF() + " " + m.getNameL() + " \nADDRESS: " + m.getAddress() + " \nCITY, STATE, ZIP: " + m.getCity() + ", " + m.getState() + ", " + m.getZip() + " \nPHONE: " + m.getPhoneCombined() + " \nORIGINAL DATE: " + m.getOdateSQL();
            s1 = s1.replaceAll("'", "`");
            setGameNumber(s1);
            if (s1.length() < 2) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Empty Field", "The field Notes cannot be empty");
            } else {
                if (!new DB().InsertAccountChangeMessage(cID, s1, new GetDay().getCurrentTimeStamp(), empID, "MNOTE")) {
                    Platform.runLater(() -> new Mail_JavaFX1().sendEmailTo("Member Note", "This Member " + purposeTF.getText() + " has had a note added to his/her file: \n Superviosr: " + nameLabel.getText() + "\n\nNotes added to file: " + s1 + "\n\nDate: " + LocalDate.now() + "\nTime: " + LocalTime.now(), "errors"));
                } else {
                    cancelButton.fire();
                    //Platform.runLater(() -> new Mail_JavaFX1().sendEmailTo("Member Note", "This Member " + purposeTF.getText() + " has had a note added to his/her file: \n Superviosr: " + fn + "\n\nNotes added to file: " + s1 + "\n\nDate: " + LocalDate.now() + "\nTime: " + LocalTime.now() + " \n\n\n" + "Member Details: \n" + memberString, "MemberNotes"));
                    Platform.runLater(() -> new Mail_JavaFX1().sendEmailTo("Member Note", "This Member " + purposeTF.getText() + " has had a note added to his/her file: \n Superviosr: " + fn + "\n\nNotes added to file: " + s1 + "\n\nDate: " + LocalDate.now() + "\nTime: " + LocalTime.now() + " \n\n\n" + "Member Details: \n" + memberString, "MemberNotes"));
                }
            }
        } else {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "Empty Field", "The field Member Name cannot be empty");
        }
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
                doEnterKey();
                break;
            default:
                break;
        }
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
        sc.changePopUp1(event, getGameNumber());
        //Stage stageV = (Stage) cancelButton.getScene().getWindow();
        //stageV.close();
    }

    

   
    
}
