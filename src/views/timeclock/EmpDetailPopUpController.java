
package views.timeclock;

import Css.cssChanger;
import JavaMail.Mail_JavaFX;
import commoncodes.ClubFunctions;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.timeclock.EmpFileFXDB;
import models.timeclock.EmpFileFXDetail;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;


    

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class EmpDetailPopUpController implements Initializable {
    @FXML private Button printButton;
    @FXML private Button cancelButton;
    @FXML private Button BpopUpButton;
    @FXML private TextField purposeTF;
    @FXML private TextField pointTF;
    @FXML private ComboBox departmentTF;
    @FXML private TextField payRateTF;
    @FXML private Label nameLabel;
    @FXML private TextArea notesTF;
    @FXML private AnchorPane root;
    public static String en, fn, ln, empID, empName, MGR, updateVar;
    public static EmpFileFXDetail gg;
    public static int vAmt;
    
    
    //employeeFX eFX = new employeeFX();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    dbStringPath dbsp = new dbStringPath();
    GetDay gd = new GetDay();
    IsItANumber iin = new IsItANumber();
    EmpFileFXDetail g = null;
    static String tMessage = "", cssPath = null, E = null, V = null;
    public static String css = null;
    Bounds bboundsInScene;
    private employeeFX eFXX;
    java.text.SimpleDateFormat tdf = new java.text.SimpleDateFormat("hh:mm a");
    java.text.SimpleDateFormat ddf = new java.text.SimpleDateFormat("MM/dd/yyyy");

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbsp.setName();
        bboundsInScene = purposeTF.localToScene(purposeTF.getBoundsInLocal());
        cssPath = cssC.cssPath();
        root.getStylesheets().add(cssPath);
        setKeyCodes();
        setToUpper();
        fillComboBox();
        new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        addTextfieldListeners();
        nameLabel.setText(empName);
        purposeTF.setText(gg.getDPurpose());
        pointTF.setText(gg.getDPoints());
        departmentTF.setValue(gg.getDDepartment());
        payRateTF.setText(gg.getDPayRate().toString());
        notesTF.setText(gg.getDNotes());
        System.out.println(gg.getDMGR());
        setTextFieldFocus();
        Platform.runLater(()->purposeTF.requestFocus());
    }    
    
    
    public void printButtonPushed(ActionEvent event) {
        java.util.Date dt = new java.util.Date();
        String currentTime = tdf.format(dt);
        try {
            if (pointTF.getText().isEmpty()) {
                pointTF.setText("0");
            }
        } catch (Exception e) {
            pointTF.setText("0");
        }
        payRateTF.setText(new ClubFunctions().clearDollarSigns(payRateTF.getText()));
        if (payRateTF.getText().isEmpty()) {
            payRateTF.setText("0.00");
        }
        if (!purposeTF.getText().isEmpty()) {
            if (updateVar.equals("0")) {
                //System.out.println(ddf.format(dt) + " " + currentTime + " " + MGR + " " + empID + " " + empName + " " + purposeTF.getText() + " " + pointTF.getText() + " " + departmentTF.getValue() + " " + payRateTF.getText() + " " + notesTF.getText());
                g = new EmpFileFXDetail(purposeTF.getText(), String.valueOf(departmentTF.getValue()), gg.getDMGR(), currentTime, notesTF.getText(), pointTF.getText(), Double.parseDouble(payRateTF.getText()), gg.getdDetailID(), LocalDate.now());
                //JOptionPane.showMessageDialog(null, "Purpose " + g.getDPurpose() +  "Notes " +  g.getDNotes() + "detailID " + g.getdDetailID() + "updateVar " + updateVar);
                if (!new EmpFileFXDB().AddEmpDetail(g)) {
                    new Mail_JavaFX().SendMail("Error", "error in Emp New Employee upDateEMP Detail (See EmpViewerController) ");
                } else {
                    cancelButton.fire();
                }
            } else {
                g = new EmpFileFXDetail(purposeTF.getText(), String.valueOf(departmentTF.getValue()), gg.getDMGR(), currentTime, notesTF.getText(), pointTF.getText(), Double.parseDouble(payRateTF.getText()), empID, LocalDate.now());
                if (!new EmpFileFXDB().updateEmpDetail(g, updateVar)) {
                    new Mail_JavaFX().SendMail("Error", "error in Emp New Employee upDateEMP Detail (See EmpViewerController) ");
                } else {
                cancelButton.fire();
                }
            }
        } else {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "Empty Field", "The field Pupose cannot be empty");
            return;
        }
    }
 
    
    
    
    private void setTextFieldFocus() {
        //showErrorClear();
        purposeTF.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                //if (editMode == 1) {
                    //if (purposeTF.getText().length()!=0) {
                    //    purposeTF.clear();
                    //    BpopUpButton.setVisible(true);
                    //    BpopUpButton.fire();
                    //} else {
                        BpopUpButton.setVisible(true);
                        //showError("Press the DOWN ARROW Key for the Selection List");
                    //}
                //}
            } else {
                BpopUpButton.setVisible(false);
            }
        });
    }
    
    public void popUP1(ActionEvent event) throws IOException {
        if (purposeTF.isFocused()) {
            getPopUpScreen(event, 1);
        }
    }
    
    
    private void getPopUpScreen(ActionEvent event, int x) throws IOException {
        switch (x) {
            case 1: // room number
                if (purposeTF.isFocused()) {
                    //if (editMode == 1 || addMode == 1) {
                        //bboundsInScene = bdayParties_11.localToScene(bdayParties_11.getBoundsInLocal());
                        sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "Purpose", "Select One:", purposeTF.getText(), bboundsInScene.getMaxX()+355.0, bboundsInScene.getMaxY()+105.0);
                        if (!sc.getGameNumber().equals("Number")) {
                            purposeTF.setText(sc.getGameNumber());
                            purposeTF.requestFocus();
                        } else {
                            purposeTF.setText("COMMENT");
                            purposeTF.requestFocus();
                        }
                        return;
                    //}
                }
                break;
        }
    }
    
    
    
     private void setToUpper() {
        for (Node node : root.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).textProperty().addListener((ov, oldValue, newValue) -> {((TextField) node).setText(newValue.toUpperCase());});
            }
        }
        notesTF.textProperty().addListener((ov, oldValue, newValue) -> {notesTF.setText(newValue.toUpperCase());});
    }
    
    private void fillComboBox() {
        List<String> myList;
        try {
            myList = Files.lines(Paths.get(dbsp.pathNameTclock + "Depart.txt")).collect(Collectors.toList());
            departmentTF.setItems(FXCollections.observableArrayList(myList));
        } catch (IOException e) {
            System.out.println("Can't find Depart.txt file" + e);
        }
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
     root.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) {
            if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
            //if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
            //if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
            //if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.ENTER) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
        }
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
    
    
    private void doEnterKey() {
        if (purposeTF.isFocused()) {
            pointTF.requestFocus();
            return;
        }
        if (pointTF.isFocused()) {
            departmentTF.requestFocus();
            return;
        }
        if (departmentTF.isFocused()) {
            payRateTF.requestFocus();
            return;
        }
        if (payRateTF.isFocused()) {
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
       fn = null;
       ln = null;
        //JOptionPane.showMessageDialog(null, "here2");
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        stageV.close();
    } 

    

   
    
}
