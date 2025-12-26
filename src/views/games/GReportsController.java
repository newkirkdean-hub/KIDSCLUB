
package views.games;

import Css.cssChanger;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import reports.games.XLSCollectionSHeet;
import reports.games.XLS2ndDepartmentReport;
import reports.games.XLSDepartmentReport;
import reports.games.XLSDepartmentReportMonthly;
import sceneChangerFX.SceneChanger_Main;


public class GReportsController implements Initializable {
    @FXML private Pane root;
    @FXML private TextField locationField;
    @FXML private Button cancelButton;
    @FXML private DatePicker beginDate;
    @FXML private DatePicker endDate;
    @FXML private Button collectionListButton;
    @FXML private Button zz;
    @FXML private Label errorLabel; 
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.getStylesheets().add(cssC.cssPath());
        locationField.setText("Boise");
        setKeyCodes();
        beginDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        locationField.requestFocus();
        errorLabel.setText("");
    }    
    
    
    
    
    public void collectionListButton() throws IOException, SQLException {
    XLSCollectionSHeet XRW = new XLSCollectionSHeet();
        errorLabel.setText("");
        if (locationField.getText().trim().isEmpty()) {
            errorLabel.setText("The Location Field cannot be blank, Please select a location");
            errorLabel.requestFocus();
            return;
        } else {
            XRW.collectionListReport(locationField.getText());  
        }
    }
    
    public void departmentReportButton() throws IOException, SQLException {
    XLSDepartmentReport XDR = new XLSDepartmentReport();
        errorLabel.setText("");
        String st = null;
        SimpleDateFormat df = new SimpleDateFormat();
        if (locationField.getText().trim().isEmpty()) {
            errorLabel.setText("The Location Field cannot be blank, Please select a location");
            locationField.requestFocus();
            return;
        }
        XDR.depart_Report(locationField.getText(), beginDate.getValue(), endDate.getValue());  
    }
    
    public void monthlyReportButton() throws IOException, SQLException {
    XLSDepartmentReportMonthly XDRM = new XLSDepartmentReportMonthly();
        errorLabel.setText("");
        String st = null;
        SimpleDateFormat df = new SimpleDateFormat();
        if (locationField.getText().trim().isEmpty()) {
            errorLabel.setText("The Location Field cannot be blank, Please select a location");
            locationField.requestFocus();
            return;
        }
        XDRM.depart_Report(locationField.getText(), beginDate.getValue(), endDate.getValue());  
    }


    public void nddepartmentReportButton() throws IOException, SQLException {
    XLS2ndDepartmentReport X2DR = new XLS2ndDepartmentReport();
        errorLabel.setText("");
        String st = null;
        SimpleDateFormat df = new SimpleDateFormat();
        if (locationField.getText().trim().isEmpty()) {
            errorLabel.setText("The Location Field cannot be blank, Please select a location");
            locationField.requestFocus();
            return;
        }
        X2DR.depart_Report(locationField.getText(), beginDate.getValue(), endDate.getValue());  
    }

    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        stageV.close();
    }
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) {
            if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.DOWN) {keyListener(ke); ke.consume();}
        }
        });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: JOptionPane.showMessageDialog(null, "F1"); break;
                    case F2: JOptionPane.showMessageDialog(null, "F2"); break;
                    case F3: JOptionPane.showMessageDialog(null, "F3"); break;
                    case F4: break;
                    case F5: JOptionPane.showMessageDialog(null, "F5"); break;
                    case F6: JOptionPane.showMessageDialog(null, "F6"); break;
                    case F7: JOptionPane.showMessageDialog(null, "F7"); break;
                    case F8: JOptionPane.showMessageDialog(null, "F8"); break;
                    case F9: break;
                    case F10: JOptionPane.showMessageDialog(null, "F10"); break;
                    case F11: JOptionPane.showMessageDialog(null, "F11"); break;
                    case DOWN: zz.fire(); break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: break;
                default:
                    break;
                }
    } 
    
    public void ZZButtonPressed(ActionEvent event) throws IOException {
        if (locationField.isFocused()) {
            Bounds boundsInScene = locationField.localToScene(locationField.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "Location", "Select One:", locationField.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());            
            locationField.setText(sc.getGameNumber());
            locationField.requestFocus();
            return;
        }
    }
    
} // END of the Class
