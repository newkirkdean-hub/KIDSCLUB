package views.games;


import dbpathnames.dbStringPath;
import static dbpathnames.dbStringPath.pathNameLocal;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;


public class GameMainController implements Initializable {
    @FXML private Button exitButton;
    @FXML private Button gamesButton;
    @FXML private Button depositButton;
    @FXML private Button userButton;
    @FXML private Button colorsButton;
    @FXML private Button reportsButton;
    //@FXML private Button notesButton;
    //@FXML private Button printButton;
    @FXML private Pane root;
    @FXML private Pane depPane;
    
    
    public static SceneChanger_Main sc;
    public static dbStringPath dbsp;
    public static String cssPath;
    public static employeeFX eFXX;

    private static final File file = new File(pathNameLocal + "configFXML.properties");
    //settingsFXML sg = new settingsFXML();
    private static ContextMenu contextMenu = new ContextMenu();
    //cssChanger cssC = new cssChanger();
    public static String empFN;
    @Override
   
    
    
    
    public void initialize(URL url, ResourceBundle rb) {
        //JOptionPane.showMessageDialog(null,);
        resetPanes();
        //buildMenuButton();
        dbsp.setName();
        colorsButton.setVisible(false);
        if (file.exists() && file.canRead()) {
            root.getStylesheets().add(cssPath);
        } else {
            root.getStylesheets().add(cssPath);
        }
    }    
    
   /* public void LookUpButtonPressed(ActionEvent event) throws IOException {
        ImagePrinter IPP = new ImagePrinter();
        IPP.main(null);
    }*/

    public void gamesButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) gamesButton.getScene().getWindow();
        sc.getStagestyleUndecorated(stageV, "/views/games/GamesView.fxml", empFN, empFN, 220.0, 50.0);
        //sc.changeScenes(stageV, "/views/games/GamesView.fxml", "Games " + eFXX.titleBar);
        
    }
    
    public void depositButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) gamesButton.getScene().getWindow();
        sc.getpassWord(stageV, "/views/games/GameDeposit.fxml", empFN, empFN, 280.0, 75.0);
        //sc.changeScenes(stageV, "/views/games/GameDeposit.fxml", "Games " + eFXX.titleBar);
        
    }
    
   
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: JOptionPane.showMessageDialog(null, "F1"); break;
                    case F2: JOptionPane.showMessageDialog(null, "F2"); break;
                    case F3: JOptionPane.showMessageDialog(null, "F3"); break;
                    case F4:
                        depositButton.fire();
                        break;
                    case F5: JOptionPane.showMessageDialog(null, "F5"); break;
                    case F6: gamesButton.fire(); break;
                    case F7: reportsButton.fire(); break;
                    case F8: JOptionPane.showMessageDialog(null, "F8"); break;
                    case F9: exitButton.fire(); break;
                    case F10: JOptionPane.showMessageDialog(null, "F10"); break;
                    case F11: JOptionPane.showMessageDialog(null, "F11"); break;
                    case F12: JOptionPane.showMessageDialog(null, "F12"); break;
                    case ESCAPE: exitButton.fire();
                    default:
                    break;
                }
    }
    
    
    public void reportsButtonPressed(ActionEvent event)  throws IOException {
         Stage stageV = (Stage) gamesButton.getScene().getWindow();
         sc.getpassWord(stageV, "/views/games/GReports.fxml", empFN, empFN, 220.0, 50.0);
       //sc.changeScenes(stageV, "/views/games/GReports.fxml", "Games " + eFXX.titleBar);        
    }
    
    private void resetPanes() {
        depPane.getChildren().clear();
        depPane.setVisible(false);
    }

    /*public void UserUpButtonPressed(ActionEvent event) throws IOException {
        ImagePrinter IPP = new ImagePrinter();
        Stage stage = (Stage) userButton.getScene().getWindow();
        IPP.start(stage);
        //IPP.main(null);
    }*/

    public void notesButtonPushed(ActionEvent event) throws IOException {
        resetPanes();
        Pane newLoadedPane = (Pane) FXMLLoader.load(getClass().getResource("GameDeposit.fxml"));
        depPane.getChildren().add(newLoadedPane);
        depPane.setVisible(true);
    }

    public void reportsButtonPushed(ActionEvent event) throws IOException {
        resetPanes();
        Pane newLoadedPane = (Pane) FXMLLoader.load(getClass().getResource("/views/games/GReports.fxml"));
        depPane.getChildren().add(newLoadedPane);
        depPane.setVisible(true);
        depPane.applyCss();
    }

    public void PrintButtonPushed(ActionEvent event) throws IOException {
        resetPanes();
        Pane newLoadedPane = (Pane) FXMLLoader.load(getClass().getResource("GamesView.fxml"));
        depPane.getChildren().add(newLoadedPane);
        depPane.setVisible(true);
    }

    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) gamesButton.getScene().getWindow();
        if (depPane.isVisible()) {
            resetPanes();
        } else {
            //stageV.close();
            //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + eFXX.titleBar);
            stageV.close();
            //eFXX.Flush();
        }
    }




    
    public void buildMenuButton() {
        //Menu item1 = new Menu(" Games ");
        MenuItem item2 = new MenuItem(" Games ");
        MenuItem item3 = new MenuItem(" Deposit ");
        MenuItem item4 = new MenuItem(" Reports ");
        //Menu item5 = new Menu("Time Clock");
        //MenuItem item6 = new MenuItem(" Exit ");

        MenuItem cItem1 = new MenuItem("Clock In / Out");
        MenuItem cItem2 = new MenuItem("Employee Message");
        //item5.getItems().addAll(cItem1, cItem2);
        
        
        MenuItem csItem1 = new MenuItem(" Cafe ");
        MenuItem csItem2 = new MenuItem(" Bridge ");
        //item1.getItems().addAll(csItem1, csItem2);

        
        item2.setOnAction((ActionEvent event) -> {
            try {
                PrintButtonPushed(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //clock in out
        );
        
        item3.setOnAction((ActionEvent event) -> {
            try {
                notesButtonPushed(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //timeclock msg
        );
        
        item4.setOnAction((ActionEvent event) -> {
            try {
                reportsButtonPushed(event);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        } //settings
        );
        
 
        contextMenu.getItems().addAll(item2, item3, item4);
 
        // When user right-click on Circle
        userButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
            Bounds boundsInScene = userButton.localToScene(userButton.getBoundsInLocal());
                contextMenu.show(userButton, boundsInScene.getMinX(), boundsInScene.getMinY());
                //contextMenu.show(menuItemsButton,  event.getScreenX(), event.getScreenY());
            }
        });
        userButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(userButton, event.getScreenX(), event.getScreenY());
        });
   }

    public void menuItemsButtonMouseOver() {
        Bounds boundsInScene = userButton.localToScene(userButton.getBoundsInLocal());
        contextMenu.show(userButton, boundsInScene.getMinX(), boundsInScene.getMinY());
    }
    
    public void menuItemsButtonMouseOut() {
        contextMenu.hide();
    }

    
    
    
    

}
