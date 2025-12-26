/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.counterPopUp;

import Css.cssChanger;
import commoncodes.IsItANumber;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import messageBox.messageBox;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class QTYController implements Initializable {
    @FXML private Button cancelButton;
    @FXML private Button selectButton;
    @FXML private Button removeButton;
    @FXML private TextField passWordTextfield;
    @FXML private Pane root;
    @FXML private static String tString, ctString;
    @FXML public static boolean remove;
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    settingsFXML sg = new settingsFXML();
    IsItANumber iin = new IsItANumber();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.getStylesheets().add(cssC.cssPath());
        passWordTextfield.setStyle("-fx-background-color: #fdfdaf");
        setKeyCodes();
        addTextfieldListeners();
        selectButton.setDisable(true);
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
                        new messageBox().showAlert(Alert.AlertType.ERROR, null, "Login Error", "Only Numbers can be used in this Feild");
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
        this.tString = string;
    }
    
    public void setColumnTitleString(String string) {
        this.ctString = string;
    }
    
    public String getctString() {
        return ctString;
    }
    
    public void removeButtonPushed() {
        remove = true;
        passWordTextfield.requestFocus();
    }

    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F1) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F6) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F8) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.F9) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ENTER) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke); ke.consume();}
         if (ke.getCode() == KeyCode.TAB) {keyListener(ke); ke.consume();}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: removeButton.fire(); break;
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
        sc.changePopUp(event, "", "");
    }
    
    public void whatButton(ActionEvent event) {
        passWordTextfield.setText(passWordTextfield.getText() + ((Button)event.getSource()).getText());

    }




    public void selectButtonPushed(ActionEvent event) throws IOException {
        if (remove) {
            System.out.println("remove = " + remove + "here in select button");
            sc.changePopUp1(event, "-"+passWordTextfield.getText().trim());
            remove = false;
        } else {
            System.out.println("remove = " + remove + "here in select button 2");
            sc.changePopUp1(event, passWordTextfield.getText().trim());
        }
    }
    


} // END OF CLASS 

