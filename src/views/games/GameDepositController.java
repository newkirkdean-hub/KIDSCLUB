package views.games;

import Css.cssChanger;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.games.Game;
import sceneChangerFX.SceneChanger_Main;


public class GameDepositController implements Initializable {
    @FXML private TextField gameNumberTextfield;
    @FXML private TextField salesTextfield;
    @FXML private TextField coinMeterTextfield;
    @FXML private TextField ticketMeterTextfield;
    @FXML private TextField prizeMeterTextfield;
    @FXML private Label gameNameTextfield;
    @FXML private Label locationTextfield;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> comboBox;
    @FXML private Button exitButton;
    @FXML private Button postButton;
    @FXML private Button lookUpButton;
    @FXML private AnchorPane root;
    @FXML private VBox vbox;
    @FXML private static String staticGameID;
    @FXML private static String staticLastCoinMeter;
    @FXML private static String staticLastTicketMeter;
    @FXML private static String staticLastPrizeMeter;
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    ObservableList<String> options = FXCollections.observableArrayList("Sales","Coin Meter","Ticker Meter", "Prize Meter");

    //WindowMainController wc = new WindowMainController();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.getStylesheets().add(cssC.cssPath());
        ResetFields();
        setKeyCodes();
        comboBox.setItems(options);
        comboBox.setValue("Sales");
        dateField.setValue(NOW_LOCAL_DATE());
        gameNumberTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        salesTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        coinMeterTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        ticketMeterTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        prizeMeterTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        gameNumberTextfield.requestFocus();
        Platform.runLater(()->gameNumberTextfield.requestFocus());

    }    

    private void setKeyCodes() {
     root.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        public void handle(KeyEvent ke) {
            if (ke.getCode() == KeyCode.F3) {keyListener(ke);}
            if (ke.getCode() == KeyCode.F6) {keyListener(ke);}
        }
        });   
    }
    
    public void highlightTextField() {
        
        Set<Node> nodes = root.lookupAll(".text-field");
        for (Node node : nodes) {
            if (node.isFocused()) {
                node.setStyle("-fx-background-color: Yellow");
            } else {
                node.setStyle("-fx-background-color: White");
            }
        }
    }

    private void focusState(boolean value) {
    System.out.println("Here");
    if (value) {
        System.out.println("Focus Gained");
    }
    else {
        System.out.println("Focus Lost");
    }
}
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
                    case F1: JOptionPane.showMessageDialog(null, "F1"); break;
                    case F2: JOptionPane.showMessageDialog(null, "F1"); break;
                    case F3:
                        lookUpButton.fire(); 
                        break;
                    case F4: ; break;
                    case F5: ; break;
                    case F6: postButton.fire(); break;
                    case F7: ; break;
                    case F8: ; break;
                    case F9: ; break;
                    case F10: ; break;
                    case F11: ; break;
                    case F12: ; break;
                    case ENTER: enterKeyPressed(); break;
                    case ESCAPE: exitButton.fire();break;
                default:
                    break;
                }
}

    public void enterKeyPressed() {

        if (dateField.isFocused()) {
            postButtonPressed();
            return;
        }
        if (prizeMeterTextfield.isFocused()) {
            dateField.requestFocus();
            return;
        }
        if (ticketMeterTextfield.isFocused()) {
            prizeMeterTextfield.requestFocus();
            return;
        }
        if (coinMeterTextfield.isFocused()) {
            ticketMeterTextfield.requestFocus();
            return;
        }
        if (salesTextfield.isFocused()) {
            if (comboBox.getValue().equals("Sales")) {
                postButtonPressed();
            } else {
                coinMeterTextfield.requestFocus();
                return;
            }
        }
        if (gameNumberTextfield.isFocused()) {
            getGame();
            return;
        }

    }

    public void getGame() {
        try {
            Game game = new Game(gameNumberTextfield.getText());
            if (game.isGameValid(game.getGameNumber())) {
                //JOptionPane.showMessageDialog(null, "Game Number Valid " + game.getGameNumber()); 
                gameNameTextfield.setText(game.getGameName());
                locationTextfield.setText(game.getLocation());
                coinMeterTextfield.setText(String.valueOf(game.getLastCoinMeter()));
                ticketMeterTextfield.setText(String.valueOf(game.getLastTicketMeter()));
                prizeMeterTextfield.setText(String.valueOf(game.getLastPrizeMeter()));
                staticLastCoinMeter = String.valueOf(game.getLastCoinMeter());
                staticLastTicketMeter = String.valueOf(game.getLastTicketMeter());
                staticLastPrizeMeter = String.valueOf(game.getLastPrizeMeter());
                staticGameID = String.valueOf(game.getGameID());
                salesTextfield.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Game Number Not Found " + game.getGameNumber());
                ResetFields();
                gameNumberTextfield.setText("");
                gameNumberTextfield.requestFocus();
                return;
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
            ResetFields();
            gameNumberTextfield.setText("");
            gameNumberTextfield.requestFocus();
            return;
        }

}
    
    public void postButtonPressed() {
    //this is where we will procaess the info for the game sales through the object
    try {
    Game g = new Game(gameNameTextfield.getText(), staticGameID, gameNumberTextfield.getText(), null, null, staticLastCoinMeter, staticLastTicketMeter, staticLastPrizeMeter, coinMeterTextfield.getText(), ticketMeterTextfield.getText(), prizeMeterTextfield.getText(), salesTextfield.getText(), dateField.getValue());
            g.putGameDetail();
            ResetFields();
            gameNumberTextfield.setText("");
            gameNumberTextfield.requestFocus();
            gameNumberTextfield.selectAll();
            gameNameTextfield.setText(g.getGameName() + " " + g.getGameNumber());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return;
        } 
}
    
    public void ResetFields() {
        staticLastCoinMeter = null;
        staticGameID = null;
        staticLastTicketMeter = null;
        staticLastPrizeMeter = null;
        //gameNameTextfield.setText("");
        locationTextfield.setText("");
        salesTextfield.setText("0");
        ticketMeterTextfield.setText("");
        coinMeterTextfield.setText("");
        prizeMeterTextfield.setText("");
        

    }

    public static final LocalDate NOW_LOCAL_DATE (){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }
    
    public void LookUpButtonPressed(ActionEvent event) throws IOException {
        sc.changePopUp(event, "/views/games/SearchTableView.fxml", "List of Games");
        gameNumberTextfield.setText(sc.getGameNumber());
        gameNumberTextfield.requestFocus();
        getGame();
    }
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
        Stage stageV = (Stage) exitButton.getScene().getWindow();
        if (!locationTextfield.getText().equals("")) {
            ResetFields();
            gameNumberTextfield.setText("");
            gameNumberTextfield.requestFocus();
        } else {
            stageV.close();
            //sc.changeScenes(stageV, "/views/GameMain.fxml", "Games " + new employeeFX().titleBar);
        }
    }






}
