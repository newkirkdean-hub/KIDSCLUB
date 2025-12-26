package views;




//import views.vouchers.FXVoucherDialogController;
import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
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
import clockoutatnight.clockOutAtNightFX;
import commoncodes.GetReceipts;
import java.awt.Desktop;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.fxml.FXMLLoader;
import messageBox.HTMLViewerController_Page;
import messageBox.messageBox;
import models.club.DB;
import models.club.rCeipts;
import pReceipts.prtReceiptsFX;
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
public class TouchController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private Button voucherButton;    
    @FXML private Button timeClockButton;    
    @FXML private Button birthdayButton;    
    @FXML private Button VIPButton;   
    @FXML private Button refill;
    @FXML private Button menuItemsButton;
    @FXML private Button exitButton;
    @FXML private Label menuItemsLabel;
    @FXML private Pane depPane;
    @FXML private Stage stageV;

    
    private static final ContextMenu CONTEXTMENU = new ContextMenu();
    private static final dbStringPath DBSP = new dbStringPath();
    private static final FXMLLoader FXLOADER = new FXMLLoader();
    private static final SceneChanger_Main SC = new SceneChanger_Main();
    private static final employeeFX EFX = new employeeFX();
    private static final DB DB = new DB();
    private static final messageBox MBOX = new messageBox();
    private static final Mail_JavaFX1 JMAIL = new Mail_JavaFX1();
    
    private static int VIPMultiplier;
    private static ArrayList<empFX> EE;
    private static ArrayList<String> V;
    private static ArrayList<rCeipts> Receipts;
    

    private static final prtReceiptsFX PRTRECEIPT = new prtReceiptsFX();
    private static final clockOutAtNightFX CLOCKOUTATNIGHT = new clockOutAtNightFX();
    private static final cssChanger CSS = new cssChanger();
    private static empFX newEFX = null;
    private static String tMessage = "", cssPath = null, counterScene = null;
    private static Bounds boundsInScene;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = voucherButton.localToScene(voucherButton.getBoundsInLocal());
        DBSP.setName();
        setKeyCodes();
        buildMenuButton();
        cssPath = CSS.cssPath();
        root.getStylesheets().add(cssPath);   
        Platform.runLater(()->setCloseCatch());
        Platform.runLater(() -> getStageV());
        Platform.runLater(() -> checkTime());

    }    
 
    
    
    public void GOVoucherButton(ActionEvent evt) {
        try {
            HTMLViewerController_Page wController = (HTMLViewerController_Page) FXLOADER.getController();
            wController.COUNTERSTAGE = new settingsFXML().getCounterSettings("Stage");
            wController.Receipts = Receipts;
            SC.loadSceneRemoveDepPane(stageV, "/messageBox/HTMLViewer_Page.fxml", depPane, 20.0, 20.0);
         //Desktop.getDesktop().browse(new URL("https://pojosadmin.venuesumo.app/bookings/calendar/bookings/").toURI());
        } catch (IOException e) {
            
        }       
        
        
        
    }

    public void GOVIPButton(ActionEvent evt) throws IOException {
        MemVIPController wController = (MemVIPController) FXLOADER.getController();
        wController.E = this.EE;
        wController.V = this.V;
        wController.rCeipts = Receipts;
        wController.css = cssPath;
        wController.VIPMultiplier = this.VIPMultiplier;
        wController.db = DB;
        wController.eFX = EFX;
        wController.dbsp = DBSP;
        wController.mBox = MBOX;
        SC.loadSceneRemoveDepPane(stageV, "/views/MemVIP.fxml", depPane, 500.0, 100.0);
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

    public void checkTime() {
        Platform.runLater(() -> CLOCKOUTATNIGHT.closeCafeScreen(DBSP.localMachine.toString()));
    }

    public void prtPitcher() {
        try {
            if (!loginButtonPushed()) {
                return;
            }
        } catch (IOException ex) {
            System.out.println("Family Focus Button Pushed Error: " + ex);
        }
        PRTRECEIPT.printPitcherSoda(newEFX.getNameF());
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

    private void GetLists() {
        Platform.runLater(() -> EE = new empDB().getList());
        setEMPList(EE);
        Platform.runLater(() -> V = DB.getAllPromos());
        setVIPList(V);
        Platform.runLater(() -> VIPMultiplier = DB.getVIPMultipier());
        setVIPMultiplier(VIPMultiplier);
        Platform.runLater(() -> Receipts = DB.getReceipts());
        setReceipts(Receipts);
        Platform.runLater(() -> Receipts = DB.getReceipts());
        setReceipts2(Receipts);
        Platform.runLater(() -> DB.disConnect());

    }

    public void actionPerformed(ActionEvent e) {
        checkTime();
    }

    private void setReceipts(ArrayList Receipts) {
        this.Receipts = Receipts;
    }

    private void setReceipts2(ArrayList Receipts2) {
        this.Receipts = Receipts;
    }

    private void setVIPList(ArrayList V) {
        this.V = V;
    }

    private void setEMPList(ArrayList E) {
        this.EE = E;
    }

    private void setVIPMultiplier(int m) {
        this.VIPMultiplier = m;
    }

    private void getStageV() {
        stageV = (Stage) voucherButton.getScene().getWindow();
        System.out.println(new settingsFXML().getCounterSettings("stage"));
        if (new settingsFXML().getCounterSettings("stage").equals("1")) {
            counterScene = "Bridge";
        } else {
            counterScene = "Cafe";
        }
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F2) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F6) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F7) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F8) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.F9) {
                keyListener(ke);
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
            }
        });
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                break;
            case F2:
                VIPButton.fire();
                break;
            case F3:
                break;
            case F4:
                break;
            case F5:
                break;
            case F6:
                break;
            case F7:
                voucherButton.fire();
                break;
            case F8:
                break;
            case F9:
                refill.fire();
                break;
            case F10:
                break;
            case F11:
                break;
            case F12:
                break;
            case ENTER:
                break;
            case ESCAPE:
                break;
            default:
                break;
        }
    }

    public void buildMenuButton() {
        MenuItem refreshList = new MenuItem(" Refresh List's ");
        MenuItem chgScrn = new MenuItem(" Change Screen ");
        MenuItem exit = new MenuItem("      Exit     ");

        exit.setOnAction((ActionEvent event) -> {
            exitProcessDO();
        }
        );

        refreshList.setOnAction((ActionEvent event) -> {
            EE.removeAll(EE);
            V.removeAll(V);
            VIPMultiplier = 0;
            Receipts.removeAll(Receipts);
            GetLists();
        }
        );

        chgScrn.setOnAction((ActionEvent event) -> {
            try {
                if (!loginButtonPushed()) {
                    return;
                }
            } catch (IOException ex) {
            }
            if (!newEFX.employeeLevel("Corporate")) {
                return;
            }

            new settingsFXML().setCounterSettings();
            new settingsFXML().setSettings(2);
            new settingsFXML().setProp("Css", "99", 6);
            new settingsFXML().setCounterProp("stage", "6");
            exitProcessDO();
        }
        );

        CONTEXTMENU.getItems().addAll(refreshList, chgScrn, exit);
        menuItemsLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boundsInScene = menuItemsLabel.localToScene(menuItemsLabel.getBoundsInLocal());
                CONTEXTMENU.show(menuItemsLabel, boundsInScene.getMaxX() + 5.0, boundsInScene.getMaxY() + 10.0);
            }
        });
        menuItemsLabel.setOnContextMenuRequested((ContextMenuEvent event) -> {
            CONTEXTMENU.show(menuItemsLabel, event.getScreenX(), event.getScreenY());
        });
    }

    public void menuItemsButtonMouseOver() {
        Bounds boundsInScene2 = menuItemsLabel.localToScene(menuItemsLabel.getBoundsInLocal());
        CONTEXTMENU.show(menuItemsLabel, boundsInScene2.getMaxX() - 110.0, boundsInScene2.getMaxY() + 20.0);

    }

    private void setCloseCatch() {
        getStageV();
        stageV.setOnCloseRequest((WindowEvent we) -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void exitProcessDO() {
        Stage stage = (Stage) voucherButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

}
