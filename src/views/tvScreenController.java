package views;




//import views.vouchers.FXVoucherDialogController;
import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import XML_Code.readXMLToArrayList;
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
import dbpathnames.GetDay;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import messageBox.HTMLViewerController_Page;
import messageBox.messageBox;
import static messageBox.messageBoxFXController.tString;
import models.club.DB;
import models.club.rCeipts;
import models.timeclock.EmpFileFXDB;
import models.timeclock.message;
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
public class tvScreenController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private Button vButton; 
    @FXML private Label menuItemsLabel;
    @FXML private Label tvNameLabel;
    @FXML private Pane depPane;
    @FXML private Stage stageV;
    @FXML private WebView webView;

    
    ContextMenu CONTEXTMENU = new ContextMenu();
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
    private static String tvName = "";
    private static WebEngine wEngine = new WebEngine();
    private static Double height, width;
    private static Double tMinutes = 0.2;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = vButton.localToScene(vButton.getBoundsInLocal());
        DBSP.setName();
        setKeyCodes();
        buildMenuButton();
        setTimer();
        cssPath = CSS.cssPath();
        root.getStylesheets().add(cssPath);   
        webView.isResizable();
        webView.setVisible(false);
        wEngine = webView.getEngine();
        Platform.runLater(()->setCloseCatch());
        Platform.runLater(() -> getStageV());
        Platform.runLater(() -> GetLists());
        Platform.runLater(()->wEngine.loadContent(tMessage, "text/html"));
        //Platform.runLater(() -> playMessage());

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
    
    private void setTimer() {
        PauseTransition pTransition = new PauseTransition(Duration.minutes(tMinutes));
        pTransition.setOnFinished((e) -> {
            Reload();
            checkTime();
            pTransition.play();
        });
        pTransition.play();

    }
    
    
    private void playMessage() {
        System.out.println("here is the message: " + tMessage);
        //if (!tMessage.equals("")) {
            //Platform.runLater(()->new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "Important Message", tMessage));
            Platform.runLater(() -> MBOX.showTimeClockMessage(Alert.AlertType.INFORMATION, stageV, "Important Message", tMessage));
            //Platform.runLater(()->dp.justDialog(tMessage));
        //}
    }
    
     private void Reload() {
        Platform.runLater(() -> {
            wEngine.getLoadWorker().progressProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                if (newValue.doubleValue() == 1D) {
                    String heightText = wEngine.executeScript("window.getComputedStyle(document.body, null).getPropertyValue('height')").toString();
                    height = Double.valueOf(heightText.replace("px", ""));
                    
                    String widthText = wEngine.executeScript("window.getComputedStyle(document.body, null).getPropertyValue('width')").toString();
                    width = Double.valueOf(widthText.replace("px", ""));
                    
                    Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
                     // width will store the width of the screen
                    int intWidth = (int)size.getWidth();
      
                    // height will store the height of the screen
                    int intHeight = (int)size.getHeight();
                    
                    System.out.println(width + "*" + height + " intW"  + intWidth + " intH" + intHeight);
                    //wViewer.setPrefSize(width + 50, height + 1.0);
                    root.setPrefSize(intWidth, intHeight - 10);
                    webView.setMinSize(intWidth, intHeight - 10);
                    webView.setPrefSize(intWidth, intHeight - 10);
                    webView.setMaxSize(intWidth, intHeight - 10);
                    stageV.sizeToScene();
                    //dialogPane.setPrefSize(width + 50, height + 50.0);
                    //alert.hide();
                    //alert.show();
                }
            });
            wEngine.loadContent(tMessage, "text/html");
            webView.setVisible(true);
            System.out.println(tMinutes);
        });
    }

    public void checkTime() {
        System.out.println("here is the message: " + tMessage);
        Reload();
        //RUN THE WEB PAGE
        //Platform.runLater(() -> CLOCKOUTATNIGHT.closeCafeScreen(DBSP.localMachine.toString()));
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
        //Platform.runLater(() -> EE = new empDB().getList());
        //setEMPList(EE);
        Platform.runLater(() -> EE = new EmpFileFXDB().getDimDataXML());
        setEMPList(EE);
        //Platform.runLater(() -> V = DB.getAllPromos());
        //setVIPList(V);
        //Platform.runLater(() -> VIPMultiplier = DB.getVIPMultipier());
        //setVIPMultiplier(VIPMultiplier);
        //Platform.runLater(() -> Receipts = DB.getReceipts());
        //setReceipts(Receipts);
        //Platform.runLater(() -> Receipts = DB.getReceipts());
        //setReceipts2(Receipts);
        //Platform.runLater(() -> DB.disConnect());
        new Thread(taskGetMessageXML).start();


    }
    
    
         Task<Void> taskGetMessageXML = new Task<Void>() {
        @Override
        protected Void call() throws Exception {

            ArrayList<message> messageList = new readXMLToArrayList().getempMessageDataXML();
            System.out.println("This would be the date to search for; " + LocalDate.now());
            System.out.println(" BEFORE: " + messageList.size());
            for (int i = 0; i < messageList.size(); i++) {
                String startDatefirstTenChars = messageList.get(i).getDateStart().toString().substring(0, 10);
                String endDatefirstTenChars = messageList.get(i).getDateEnd().toString().substring(0, 10);
                //if (new GetDay().setToLocalDateforXML(startDatefirstTenChars).isEqual(LocalDate.now()) || new GetDay().setToLocalDateforXML(startDatefirstTenChars).isBefore(LocalDate.now())) {
                    //if (new GetDay().setToLocalDateforXML(endDatefirstTenChars).isEqual(LocalDate.now()) || new GetDay().setToLocalDateforXML(endDatefirstTenChars).isAfter(LocalDate.now())) {
                        if(messageList.get(i).getTitle().equals(tvName)) {
                        System.out.println("======================================== enddate" + endDatefirstTenChars);
                        System.out.println(" Winner: startDate = to LocalDate.now()" + messageList.size());
                        tMessage = messageList.get(i).getHTML();
                        }
                    //}
                //} //END OF IF
            } //END OF FOR
            return null;
        }
    };

    

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
        tvName = DBSP.localMachine.toString();
        tvNameLabel.setText(tvName);
        stageV = (Stage) vButton.getScene().getWindow();
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
                //VIPButton.fire();
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
                //voucherButton.fire();
                break;
            case F8:
                break;
            case F9:
                //refill.fire();
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
            if (!newEFX.employeeLevel("Corporate")) {
                return;
            }

            new settingsFXML().setCounterSettings();
            new settingsFXML().setSettings(2);
            new settingsFXML().setProp("Css", "99", 6);
            new settingsFXML().setCounterProp("stage", "6");

            new SceneChanger_Main().MainStage(stageV, "/views/CounterChooser.fxml", "Counter Chooser");
            } catch (IOException ex) {
            }
            //exitProcessDO();
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
        Stage stage = (Stage) vButton.getScene().getWindow();
        stage.close();
        Platform.exit();
        System.exit(0);
    }

}
