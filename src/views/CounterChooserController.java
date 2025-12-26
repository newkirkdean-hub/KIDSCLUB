package views;




import Css.cssChanger;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;
import models.timeclock.EmpFileFXDB;
import pWordFX.empDB;
import pWordFX.empFX;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;

/**
 * FXML Controller class
 *
 * @author Dean
 */
public class CounterChooserController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private Button bridgeButton;    
    @FXML private Button bridgeIIButton;    
    @FXML private Button counterButton;    
    @FXML private Button tvButton;    
    @FXML private Button mainButton;    
    @FXML private Button menuItemsButton;
    @FXML private Button cafeButton;
    @FXML private Button touchButton;
    @FXML private Button exitButton;
    @FXML private Label menuItemsLabel;
    @FXML private Pane depPane;
    @FXML private Stage stageV;

    
    private static final ContextMenu CONTEXTMENU = new ContextMenu();
    private static final dbStringPath DBSP = new dbStringPath();
    private static final cssChanger CSS = new cssChanger();
    private static final SceneChanger_Main SC = new SceneChanger_Main();
    private static final employeeFX EFX = new employeeFX();
    private static Bounds boundsInScene;
    private static ArrayList<empFX> EE;
    private static empFX newEFX = null;

    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = bridgeButton.localToScene(bridgeButton.getBoundsInLocal());
        DBSP.setName();
        setKeyCodes();
        //buildMenuButton();
        root.getStylesheets().add(CSS.cssPath()); 
        Platform.runLater(()->setCloseCatch());
        Platform.runLater(() -> GetLists());
        Platform.runLater(()->getStageV());
    }    
 
    
    
    private void setCloseCatch() {
        getStageV();
        stageV.setOnCloseRequest((WindowEvent we) -> {
            System.out.println("Stage is closing");
            System.exit(0);
        }); 
    }
    
    public void buildMenuButton() {
        MenuItem item2 = new MenuItem(" Voucher Viewer ");
        MenuItem item4 = new MenuItem(" Settings ");
        MenuItem item6 = new MenuItem("      Exit     ");


        Menu menu1 = new Menu(" Counter Screens ");
        MenuItem csItem1 = new MenuItem(" Cafe ");
        MenuItem csItem2 = new MenuItem(" Bridge ");
        menu1.getItems().addAll(csItem1, csItem2);
        
        
        Menu menu5 = new Menu(" Time Clock ");
        MenuItem cItem1 = new MenuItem("Clock In / Out");
        MenuItem cItem2 = new MenuItem("Employee Message");
        menu5.getItems().addAll(cItem1, cItem2);


        Menu menu6 = new Menu(" Mail Outs ");
        MenuItem rItem1 = new MenuItem(" New Members Last Month ");
        MenuItem rItem2 = new MenuItem(" Birthdays Next Month ");
        menu6.getItems().addAll(rItem1, rItem2);


        Menu menu7 = new Menu(" Club Settings ");
        MenuItem m7item1 = new MenuItem(" Bonus Table ");
        MenuItem m7item2 = new MenuItem(" Greetings ");
        MenuItem m7item3 = new MenuItem(" Random Winner ");
        menu7.getItems().addAll(m7item1, m7item2, m7item3);
        
        
        menu1.setOnAction((ActionEvent event) -> {
            JOptionPane.showMessageDialog(null, "Select Menu Item 1");
        } //counter screens
        );
        
        item6.setOnAction((ActionEvent event) -> {
            exitProcessDO();
        } //clock in out
        );
        
        
 
        CONTEXTMENU.getItems().addAll(item6);
 
        // When user right-click on Circle
        menuItemsLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
            boundsInScene = menuItemsLabel.localToScene(menuItemsButton.getBoundsInLocal());
                CONTEXTMENU.show(menuItemsLabel, boundsInScene.getMaxX()+ 5.0, boundsInScene.getMaxY() + 10.0);
                //contextMenu.show(menuItemsButton,  event.getScreenX(), event.getScreenY());
            }
        });
        //menuItemsButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
        //    contextMenu.show(menuItemsButton, event.getScreenX(), event.getScreenY());
        //});
        menuItemsLabel.setOnContextMenuRequested((ContextMenuEvent event) -> {
            CONTEXTMENU.show(menuItemsLabel, event.getScreenX(), event.getScreenY());
        });
   }
    
    
    
   
    public void menuItemsButtonMouseOver() {
        Bounds boundsInScene2 = menuItemsLabel.localToScene(menuItemsLabel.getBoundsInLocal());
        CONTEXTMENU.show(menuItemsLabel, boundsInScene2.getMaxX() -110.0, boundsInScene2.getMaxY() +20.0);
        
    }
    
    
    
    
     private void GetLists() {
        //Platform.runLater(() -> EE = new empDB().getList());
        //setEMPList(EE);
        Platform.runLater(() -> EE = new EmpFileFXDB().getDimDataXML());
        setEMPList(EE);
    }
    
    
    private void setEMPList(ArrayList E) {
        this.EE = E;
    }
    
    
    private void getStageV() {
        stageV = (Stage) bridgeButton.getScene().getWindow();    
    }
    
    private Stage getStage() {
        Stage stage = (Stage) bridgeButton.getScene().getWindow();    
        return stage;
    }


    public void bridgeButtonActionPerformed(ActionEvent evt) {  
       
       try {
            if (!loginButtonPushed()) {
                return; 
            }
        if (!newEFX.employeeLevel("Games")) {
            return;
        }
        new settingsFXML().setCounterProp("stage", "1");
        SC.changeScenes(getStage(), "/views/Bridge.fxml", "Bridge" + " - " + "Active Employee: Bridge");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene("Bridge", stageV, eFX.getNameF(), depPane, boundsInScene);
        EFX.Flush();
         return;
    }   
    
    public void counterButtonActionPerformed(ActionEvent evt) {  
       
       try {
            if (!loginButtonPushed()) {
                return; 
            }
        if (!newEFX.employeeLevel("Games")) {
            return;
        }
        new settingsFXML().setCounterProp("stage", "3");
        //SC.SetCounterScenesII(getStage(), "/views/counterSockets.fxml", "CounterSockets" + " - " + "Active Employee: CounterSockets", "5");
        SC.changeScenes(stageV, "/views/Counter.fxml", "Counter" + " - " + "Active Employee: Counter");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene("Counter", stageV, eFX.getNameF(), depPane, boundsInScene);
        EFX.Flush();
         return;
    }   
    
 
    
    public void cafeButtonActionPerformed(ActionEvent evt) {  
       
       try {
            if (!loginButtonPushed()) {
                return; 
            }
        if (!newEFX.employeeLevel("Games")) {
            return;
        }

        new settingsFXML().setCounterProp("stage", "2");
            SC.changeScenes(getStage(), "/views/Cafe.fxml", "Cafe" + " - " + "Active Employee: Cafe");
        } catch(IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene("Cafe", stageV, eFX.getNameF(), depPane, boundsInScene);
        EFX.Flush();
         return;
    }   
    
    
    public void MainButtonActionPerformed(ActionEvent evt) {  
       
       try {
            if (!loginButtonPushed()) {
                return; 
            }
        if (!newEFX.employeeLevel("Corporate")) {
            return;
        }

        new settingsFXML().setCounterProp("stage", "4");
            //SC.changeScenes(getStage(), "/views/Main.fxml", "Cafe" + " - " + "Active Employee: Main");
            new SceneChanger_Main().MainStage(stageV, "/views/Main.fxml", "Pojo Main");        
            //exitProcessDO();
       } catch(IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene("Cafe", stageV, eFX.getNameF(), depPane, boundsInScene);
        EFX.Flush();
         return;
    }   
  
    public void tvButtonActionPerformed(ActionEvent evt) {  
       
       try {
            if (!loginButtonPushed()) {
                return; 
            }
        if (!newEFX.employeeLevel("Games")) {
            return;
        }
        
        new settingsFXML().setCounterProp("stage", "9");
        SC.changeScenes(getStage(), "/views/tvScreen.fxml", "TV" + " - " + "Active Employee: TV");
        } catch(IOException ex) {
            System.out.println(ex);
        }
        //SC.goToScene("Cafe", stageV, eFX.getNameF(), depPane, boundsInScene);
        EFX.Flush();
         return;
    }   
    
    
    public boolean loginButtonPushed() throws IOException { 
        Boolean GO = false;
        SC.setGameNumber(null);
        EFX.Flush();
        getStageV();

        SC.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", boundsInScene.getMinX() + 350.0, boundsInScene.getMinY());
        if (!isEMPValidInArrayList(SC.getGameNumber())) {
            GO = false;
        } else {
            if (!newEFX.getEmpNumber().equals("Number") || !newEFX.getEmpNumber().isEmpty()) {
                GO = true;
            } else {
                GO = false;
            }
        }
        return GO;

    }
    
     private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        for (int y = 0; y < EE.size(); y++) {
            if (n.trim().equals(EE.get(y).getEmpNumber())) {
                empValid = true;
                newEFX = new empFX(EE.get(y).getNameF(), EE.get(y).getNameL(), EE.get(y).getEmpNumber(), EE.get(y).getVAmt(), EE.get(y).getBdayresos(), EE.get(y).getChangerEdit(), EE.get(y).getEmpID(), EE.get(y).getGProb(), EE.get(y).getActive(), EE.get(y).getArcade(), EE.get(y).getBcarsales());
            }
        }
        return empValid;
    }
    
    /*
    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        SC.setGameNumber(null);
        EFX.Flush();
        getStageV();

        Bounds boundsInScene2 = bridgeButton.localToScene(bridgeButton.getBoundsInLocal());
        SC.getpassWord(stageV, "/pWordFX/passWord.fxml", "Number", "Enter Employee Number:", boundsInScene2.getMinX() + 350.0, boundsInScene2.getMinY());
        //if (!EFX.isemployeevalid(SC.getGameNumber())) {
            GO = false;
            System.out.println(GO);
        } else {
            if (!EFX.getEmpNumber().equals("Number") && !EFX.getEmpNumber().isEmpty()) {
                GO = true;
                System.out.println(GO);
            } else {
                GO = false;
                System.out.println(GO);
            }
        }
        System.out.println("1" + GO);
        return GO;
    }
    */
    
    
    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
         if (ke.getCode() == KeyCode.F5) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F6) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F7) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F8) {keyListener(ke);}
         if (ke.getCode() == KeyCode.F9) {keyListener(ke);}
         if (ke.getCode() == KeyCode.ESCAPE) {keyListener(ke);}
     });   
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: break;
                    case F2: break;
                    case F3: break;
                    case F4: break;
                    case F5: bridgeButton.fire(); break;
                    case F6: cafeButton.fire(); break;
                    case F7: counterButton.fire(); break;
                    case F8: mainButton.fire(); break;
                    case F9: tvButton.fire(); break;
                    case F10: touchButton.fire();break;
                    case F11: break;
                    case F12: break;
                    case ENTER: break;
                    case ESCAPE: exitProcessDO() ;break;
                default:
                    break;
                }
}
    
    private void exitProcessDO() {
    //if (depPane.isVisible()) { 
    //  SC.resetPanes(depPane);
    //} else {
        Stage stage = (Stage) bridgeButton.getScene().getWindow();
        stage.close();
        System.exit(0);
    //}
  }



    /* I THINK THIS IS OBSOLETE, IT WAS A TEST A LONG TIME AGO
    public void bridgeIIButtonActionPerformed(ActionEvent evt) {  
       
       try {
            if (!loginButtonPushed()) {
                return; 
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        if (!eFX.employeeLevel("Games")) {
            return;
        }
        getStageV();
        SC.goToScene("Cafe", stageV, eFX.getNameF(), depPane, boundsInScene);
        eFX.Flush();
         return;
    } 
    */
    
    /*
    public void counterIIButtonActionPerformed(ActionEvent evt) {  
       
       try {
            if (!loginButtonPushed()) {
                return; 
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        if (!eFX.employeeLevel("Games")) {
            return;
        }
        getStageV();
        SC.goToScene("Touch", stageV, eFX.getNameF(), depPane, boundsInScene);
        eFX.Flush();
         return;
    }   
    */










    
}
