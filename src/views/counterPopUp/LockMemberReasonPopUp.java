
package views.counterPopUp;

import Css.cssChanger;
import UtilConnections.UtilConnections;
import UtilConnections.UtilConnectionsSockets;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import models.club.DB;
import models.club.Member;
import sceneChangerFX.SceneChanger_Main;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class LockMemberReasonPopUp implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Button BpopUpButton;
    @FXML private PasswordField purposeTF;
    @FXML private Label typeTF;
    @FXML private Label upperLabel;
    @FXML private Label lowerLabel;
    //@FXML private ComboBox departmentTF;
    //@FXML private TextField payRateTF;
    @FXML private Label nameLabel;
    @FXML private TextArea notesTF;
    @FXML private TextArea notesTF1;
    @FXML private AnchorPane root;
    
    
    public static String en, fn, ln, empID, empName, MGR, updateVar, typeVar;
    public static Member memberPassed;
    //public static int vAmt;
    
    
    //employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    GetDay gd = new GetDay();
    IsItANumber iin = new IsItANumber();
    public static String tMessage = "", cssPath = null, E = null, V = null;
    public static String css = null;
    Bounds bboundsInScene;
    //private employeeFX eFXX;
    Connection connectionMembers = null, connectionOldDetail = null;
    String wasCancel = "T";
    //private final dbStringPath1 dbsp1 = new dbStringPath1(); 
    java.text.SimpleDateFormat tdf = new java.text.SimpleDateFormat("hh:mm a");
    java.text.SimpleDateFormat ddf = new java.text.SimpleDateFormat("MM/dd/yyyy");

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb) 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        bboundsInScene = purposeTF.localToScene(purposeTF.getBoundsInLocal());
        cssPath = cssC.cssPath();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        setToUpper();
        new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        nameLabel.setText(empName);
        purposeTF.setText(memberPassed.getCCN());
        typeTF.setText(typeVar);
        System.out.println("here is the typevar " + typeVar + " updatevar " + updateVar);
        if (typeVar.equals("CLOSE")) {
            notesTF1.setVisible(false);
            lowerLabel.setVisible(false);
            upperLabel.setText("Reason for Closing this account");
        }
        if (typeVar.equals("NEWCRD")) {
            notesTF1.setVisible(false);
            lowerLabel.setVisible(false);
            upperLabel.setText("Reason for New Account Number");
        }
        notesTF.setText(tMessage);
        notesTF.setEditable(true);
        setTextFieldFocus();
        Platform.runLater(() -> notesTF.requestFocus());
        /*Platform.runLater(() -> {
            try {
                connectionMembers = getConnectionMembers();
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println(ex);
            }
        });*/

    }

    public Connection getConnectionMembers() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Connection> callable = () -> {
            connectionMembers = new UtilConnectionsSockets().connectMembersSockets();
            return connectionMembers;
        };
        Future<Connection> future = executor.submit(callable);
        connectionMembers = future.get(); //returns 2 or raises an exception if the thread dies, so safer
        executor.shutdown();
        System.out.println("in getConnection " + connectionMembers);
        return connectionMembers;
    }

    public Connection getConnectionOldDetail() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<Connection> callable = () -> {
            connectionOldDetail = new UtilConnections().connectOldDetail();
            return connectionOldDetail;
        };
        Future<Connection> future = executor.submit(callable);
        connectionOldDetail = future.get(); //returns 2 or raises an exception if the thread dies, so safer
        executor.shutdown();
        System.out.println("in getConnection " + connectionOldDetail);
        return connectionOldDetail;
    }

    public void printButtonPushed(ActionEvent event) throws SQLException, InterruptedException {
        String txtMsg = null;
        if (!updateVar.equals("0")) {
            if (typeVar.equals("NEWCRD")) {
                String t = notesTF.getText();
                notesTF.setText(t + " Old account Number is: " + memberPassed.getCCN());
            }
            txtMsg = notesTF.getText() + ":" + notesTF1.getText();
            System.out.println(txtMsg);
            setWCancel(txtMsg);
            cancelButton.fire();
        } else {
            if (!new DB().UpdateAccountChangeMessageMemDetailDB(memberPassed.getCID(), notesTF.getText() + ":" + notesTF1.getText())) {
                System.out.println("failure");
                cancelButton.fire();
            } else {
                setWCancel(txtMsg);
                cancelButton.fire();
            }
        }
    }

    public void setWCancel(String message) {
        if (message.isEmpty()) {
            this.wasCancel = "F";
        } else {
            this.wasCancel = message;
        }
    }

    public String getWCancel() {
        return this.wasCancel;
    }

    private void setTextFieldFocus() {
        purposeTF.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {

            } else {
                BpopUpButton.setVisible(false);
            }
        });
    }

    private void setToUpper() {
        root.getChildren().stream().filter((node) -> (node instanceof TextField)).forEachOrdered((node) -> {
            ((TextField) node).textProperty().addListener((ov, oldValue, newValue) -> {
                ((TextField) node).setText(newValue.toUpperCase());
            });
        });
        notesTF.textProperty().addListener((ov, oldValue, newValue) -> {
            notesTF.setText(newValue.toUpperCase());
        });
        notesTF1.textProperty().addListener((ov, oldValue, newValue) -> {
            notesTF1.setText(newValue.toUpperCase());
        });
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F6) {
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
        if (notesTF.isFocused()) {
            notesTF1.requestFocus();
            return;
        }
        
    }
    
    
    
    public void exitButtonPushed(ActionEvent event) throws IOException, SQLException {
        en = null;
        fn = null;
        ln = null;
        //connectionOldDetail.close();
        //connectionOldDetail = null;
        sc.changePopUp1(event, getWCancel());
    }

} //END LOCKMEMBERREASONPOPUP
