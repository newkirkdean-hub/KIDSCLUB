/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.counterPopUp;

import Css.cssChanger;
import commoncodes.IsItANumber;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.club.LastMemberTransaction;
import pWordFX.empFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class scanCardAgainController implements Initializable {
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Button fullListLMTButton;
    @FXML private TextField passWordTextfield;
    @FXML private Pane root;
    @FXML private Label labelString;
    @FXML private static String tString, ctString;

    
    public static SceneChanger_Main sc;
    public static String cssPath;
    public static FXMLLoader FXLOADER;
    public static dbStringPath dbsp;
    private static settingsFXML sg = new settingsFXML();
    private static IsItANumber iin = new IsItANumber();
    public static ArrayList<LastMemberTransaction> lastmember;
    public static int iLastTran;;
    public static empFX newEFX;
    private static Stage stageV;
    private static boolean SCLMT = false;

    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.getStylesheets().add(cssPath);
        passWordTextfield.setStyle("-fx-background-color: #fdfdaf");
        setKeyCodes();
        addTextfieldListeners();
        selectButton.setDisable(true);
        Platform.runLater(()->labelString.setText(sc.getTitle()));
        Platform.runLater(()->passWordTextfield.requestFocus());        
    }    


    public void addTextfieldListeners() {
        passWordTextfield.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        passWordTextfield.clear();
                        passWordTextfield.requestFocus();
                        //JOptionPane.showMessageDialog(null, "Employee Numbers can only be Numbers");
                        new messageBox().showAlert(Alert.AlertType.ERROR, null, "Login Error", "Employee Numbers can only be Numbers");
                        return;
                    }
                    if (newValue.length() > 0) {
                        selectButton.setDisable(false);
                    } else {
                        selectButton.setDisable(true);
                    }
                }
        );
    }
    
    public void setTitleString(String string) {
        System.out.println(":dshfkjah dfdafffffffffffffffff " + string);
        this.tString = string;
    }
    
    public void setColumnTitleString(String string) {
    System.out.println(":dshfkjah dfdafffffffffffffffff1 " + string);
        this.ctString = string;
    }
    
    public String getctString() {
        return ctString;
    }
    
     private Stage getStageV() {
        stageV = (Stage) selectButton.getScene().getWindow();
        return stageV;
    }
    
    
     public void CCNClicked(MouseEvent me) {
       if (lastmember != null) {
       System.out.println("==================================== " + me.getClickCount());
        if (me.getClickCount() == 2) {
            getLastTransactionMemberVIP();
            return;
        }
       me.consume();
        MouseButton mb = me.getButton();
        if (mb == MouseButton.SECONDARY) {
           fullListLMTButton.fire();
        }
        }
    }
    
    
    
    public void getLastTransactionMemberVIP() {
        String empNumb, lastCCN;
        empNumb = lastmember.get(iLastTran).getlastEmpNumber(); //counter screens
        lastCCN = lastmember.get(iLastTran).getLastMember();
        //sc.getpassWord(getStageV(), "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
        //boolean GO = isEMPValidInArrayList(sc.getGameNumber());
        System.out.println("newEFX emp number " + newEFX.getEmpNumber() + " lastmember empnumb " + empNumb);
        if (newEFX.getEmpNumber().equals(empNumb)) {
            //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Approved!", "Here is the last member number " + lastCCN);
            passWordTextfield.setText(lastCCN);
            setLastMemberVariables();
            Platform.runLater(() -> selectButton.fire());
        }
        empNumb = "";
        lastCCN = "";
    }
    
    
    public void getFullListLastTransactionMemberVIP(ActionEvent ae) {
        try {
            //sc.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", 550.0, 50.0);
            //boolean GO = isEMPValidInArrayList(sc.getGameNumber());
            //if (newEFX.getBcarsales() != 1) {
            //    return;
            //}
            lastMemberTransactionViewController wController = (lastMemberTransactionViewController) FXLOADER.getController();
            wController.sc = sc;
            wController.dbsp = dbsp;
            wController.cssPath = cssPath;
            wController.lastMember = lastmember;
            sc.changePopUp(ae, "/views/counterPopUp/lastMemberTransactionView.fxml", "List of Activity");
            System.out.println("getactivity " + sc.getActivity());
            if (!"1".equals(sc.getActivity())) {
                LastMemberTransaction LMT = sc.GetLastMemberTransaction();
                passWordTextfield.setText(LMT.getLastMember());
                setLastMemberVariables();
                Platform.runLater(() -> selectButton.fire());
            } else {
                return;
            }
        } //counter screens
        catch (IOException ex) {
            System.out.println(ex);
        }
    }

 private void setLastMemberVariables() {
        sc.setCCN(passWordTextfield.getText());
        sc.setEmployee(newEFX.getEmpNumber());
        sc.setEmpName(newEFX.getNameF());
        sc.setActivity("UPDATE");
        SCLMT = true;
        sc.setLastName("");

    }
    
    
    
    
    
    

    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) {
            if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.ENTER) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
            if (ke.getCode() == KeyCode.TAB) {keyListener(ke); ke.consume();}

        }
        });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: ; break;
                    case F2: ; break;
                    case F3: ; break;
                    case F4: break;
                    case F5: ; break;
                    case F6: ; break;
                    case F7: ; break;
                    case F8: ; break;
                    case F9: break;
                    case F10: ; break;
                    case F11: ; break;
                    case F12: ; break;
                    case ESCAPE: cancelButton.fire(); break;
                    case ENTER: selectButton.fire(); break;
                default:
                    break;
                }
    }
    
    public void cancelButtonPushed(ActionEvent event) throws IOException {
        if (!SCLMT) {
            sc.setActivity("1");
        }
        sc.changePopUp(event, "", "");
    }
    
    public void selectButtonPushed(ActionEvent event) throws IOException {
        if (!SCLMT) {
            sc.setActivity("1");
        }
        sc.changePopUp1(event, passWordTextfield.getText().trim());
    }
    


} // END OF CLASS 

