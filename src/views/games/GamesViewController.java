package views.games;

import Css.cssChanger;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.games.Detail;
import models.games.Game;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Hannah
 */
public class GamesViewController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private VBox vbox1;
    @FXML private TextField coinMeterTextfield;
    @FXML private TextField ticketMeterTextfield;
    @FXML private TextField prizeMeterTextfield;
    @FXML private TextField serialNumberTextfield;
    @FXML private RadioButton taxableRadio;
    @FXML private TextField gameNumberTextfield;
    @FXML private DatePicker purchaseDateTextfield;
    @FXML private TextField gameNameTextfield;
    @FXML private TextField locationTextfield;
    @FXML private TextField departmentTextfield;
    @FXML private TextField secondDepartmentTextfield;
    @FXML private TextField vendorTextfield;
    @FXML private Button newButton;
    @FXML private Button searchButton;
    @FXML private Button editButton;
    @FXML private Button cancelButton;
    @FXML private Button printButton;
    @FXML private GridPane gridPane1;
    @FXML private GridPane gridPane2;
    @FXML private Boolean editMode;
    @FXML private AnchorPane editPane;
    @FXML private DatePicker editPaneDate;
    @FXML private TextField editPaneSales;
    @FXML private TextField editPaneCoins;
    @FXML private TextField editPaneTickets;
    @FXML private TextField editPanePrizes;
    @FXML private TextField editPaneID;
    @FXML private Button editPaneSave;
    @FXML private static String detailID;
    @FXML private static String staticGameID;
    
    //dbStringPath dbsp = new dbStringPath();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    @FXML private TableView<Detail> gameDetailTable;
    @FXML private TableColumn<Detail, LocalDate> gameDateColumn;
    @FXML private TableColumn<Detail, Double> gameSalesColumn;
    @FXML private TableColumn<Detail, String> gameCoinMeterColumn;
    @FXML private TableColumn<Detail, String> gameTicketMeterColumn;
    @FXML private TableColumn<Detail, String> gamePrizeMeterColumn;
    @FXML private TableColumn<Detail, String> gameIDColumn;
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
       gameDateColumn.setCellValueFactory(new PropertyValueFactory<>("detailDateColumn"));

       gameDateColumn.setCellFactory(column -> {
            return new TableCell<Detail, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
       NumberFormat $format = DecimalFormat.getCurrencyInstance();
        gameSalesColumn.setCellValueFactory(new PropertyValueFactory<>("detailSalesColumn"));

       gameSalesColumn.setCellFactory(column -> {
            return new TableCell<Detail, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    //System.out.println(item);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText($format.format(item));
                    }
                }
            };
        }); 

        gameCoinMeterColumn.setCellValueFactory(new PropertyValueFactory<>("detailCoinMeterColumn"));
        gameTicketMeterColumn.setCellValueFactory(new PropertyValueFactory<>("detailTicketMeterColumn"));
        gamePrizeMeterColumn.setCellValueFactory(new PropertyValueFactory<>("detailPrizeMeterColumn"));
        gameIDColumn.setCellValueFactory(new PropertyValueFactory<>("DetailGameIDColumn"));
       root.getStylesheets().add(cssC.cssPath()); 
       editPane.setVisible(false);
       setHighLights();
       setKeyCodes();
       getGame("01");
       makeAllNoEdit();
    } 
    
    
    
    public void LookUpButtonPressed2(ActionEvent event) throws IOException {
        if (locationTextfield.isFocused()) {
            //SceneChanger sc = new SceneChanger();
            //sc.getPopUp(event, "selectPopUp.FXML", "Location", "Select One:");            
        locationTextfield.setText(sc.getGameNumber());
        locationTextfield.requestFocus();
        }
    }
    
    public void newGame() {
    makeAllEdit();
    clearFields();
    locationTextfield.setText("Boise");
    purchaseDateTextfield.setValue(LocalDate.now());
    departmentTextfield.setText("Redemption");
    gameDetailTable.getItems().clear();
    newButton.setDisable(true);
    gameNumberTextfield.setText(getNewGameNumber(1));
    //gameNumberTextfield.setStyle("-fx-background-color: Transparent");
    printButton.setDisable(true);
    searchButton.setDisable(false);
    gameNameTextfield.requestFocus();   
    coinMeterTextfield.setText("0");
    prizeMeterTextfield.setText("0");
    ticketMeterTextfield.setText("0");
    taxableRadio.setSelected(false);
    }
        
    public void getTableRow() {
    
    Detail tableRowValue = gameDetailTable.getSelectionModel().getSelectedItem();
    editPane.setVisible(true);
    editPaneDate.setValue(tableRowValue.getDetailDateColumn());
    editPaneSales.setText(tableRowValue.getDetailSalesColumn().toString());
    editPaneCoins.setText(String.valueOf(tableRowValue.getDetailCoinMeterColumn()));
    editPaneTickets.setText(String.valueOf(tableRowValue.getDetailTicketMeterColumn()));
    editPanePrizes.setText(String.valueOf(tableRowValue.getDetailPrizeMeterColumn()));
    editPaneSales.requestFocus();
    detailID = tableRowValue.getDetailGameIDColumn();
    newButton.setDisable(true);
    searchButton.setDisable(true);
    editButton.setText("Save F9");
    printButton.setDisable(true);
    }

    public ObservableList<Detail> getDetail(String Id) throws SQLException {
    ObservableList<Detail> details = FXCollections.observableArrayList();
    details.clear();
    gameDetailTable.getItems().clear();
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;
    try {
            conn=DriverManager.getConnection("jdbc:ucanaccess://"+ dbStringPath.pathNameClubDBs + "GamesDB.accdb");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT Games2.[Game ID], Games2.Date, Games2.Sales, Games2.[Coin Meter], Games2.[Ticket Meter], Games2.[Prize Meter], Games2.[Transaction Number] FROM Games2 WHERE Games2.[Game ID] = '" + Id + "' ORDER BY Games2.Date, Games2.[Transaction Number]");
            while (rs.next()) {
                Detail newDetail = new Detail(rs.getDate("Date").toLocalDate(), rs.getDouble("Sales"), rs.getString("Coin Meter"), rs.getString("Ticket Meter"), rs.getString("Prize Meter"), rs.getString("Transaction Number"));
                details.add(newDetail);
            }
    }
    catch (SQLException e) {
        System.err.println("Error: " + e.getMessage());
    }
    finally {
        if (conn != null);
        conn.close();
    if (st != null);
        st.close();
    if (rs != null);
        rs.close();
}
    Id = null;
   //System.out.println(Id);

    return details;
    
}   
    
    private void getGame(String ID) {
        try {
            Game game = new Game(ID);
            if (game.isGameValid(game.getGameNumber())) {
                //JOptionPane.showMessageDialog(null, "Game Number Valid " + game.getGameNumber()); 
                gameNumberTextfield.setText(game.getGameNumber());
                gameNameTextfield.setText(game.getGameName());
                locationTextfield.setText(game.getLocation());
                departmentTextfield.setText(game.getDepartment());
                secondDepartmentTextfield.setText(game.getSecondDepartment());
                vendorTextfield.setText(game.getVendor());
                purchaseDateTextfield.setValue(game.getPurchaseDate());
                coinMeterTextfield.setText(String.valueOf(game.getLastCoinMeter()));
                ticketMeterTextfield.setText(String.valueOf(game.getLastTicketMeter()));
                prizeMeterTextfield.setText(String.valueOf(game.getLastPrizeMeter()));
                serialNumberTextfield.setText(game.getSerialNumber());
                //JOptionPane.showMessageDialog(null, game.getTaxable());
                if (game.getTaxable() == 0) {
                    taxableRadio.setSelected(false);
                }else{
                    taxableRadio.setSelected(true);
                }

                staticGameID = game.getGameID();
                try {
                    gameDetailTable.getItems().addAll(getDetail(game.getGameID()));
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }           
    
            } else {
                JOptionPane.showMessageDialog(null, "Game Number Not Found " + game.getGameNumber());
                // ResetFields();
                //gameNumberTextfield.setText("");
                //gameNumberTextfield.requestFocus();
                return;
            }
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, e);
            //ResetFields();
            //gameNumberTextfield.setText("");
            //gameNumberTextfield.requestFocus();
            return;
        }

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
    
    private void enterKeyPressed() {
        if (editPane.isVisible()) {
        if (editPanePrizes.isFocused()) {editPaneDate.requestFocus(); return;}
        if (editPaneTickets.isFocused()) {editPanePrizes.requestFocus(); return;}
        if (editPaneCoins.isFocused()) {editPaneTickets.requestFocus(); return;}
        if (editPaneSales.isFocused()) {editPaneCoins.requestFocus(); return;}
        if (editPaneDate.isFocused()) {editPaneSales.requestFocus(); return;}
           
       } else {
            //searchButton.setDisable(true);
            searchButton.setText("Search F7");
        //if (taxableTextfield.isFocused()) {gameNameTextfield.requestFocus(); return;}
        if (serialNumberTextfield.isFocused()) {gameNameTextfield.requestFocus(); return;}
        if (prizeMeterTextfield.isFocused()) {serialNumberTextfield.requestFocus(); return;}
        if (ticketMeterTextfield.isFocused()) {prizeMeterTextfield.requestFocus(); return;}
        if (coinMeterTextfield.isFocused()) {ticketMeterTextfield.requestFocus(); return;}
        if (purchaseDateTextfield.isFocused()) {coinMeterTextfield.requestFocus(); return;}
        if (vendorTextfield.isFocused()) {purchaseDateTextfield.requestFocus(); return;}
        if (secondDepartmentTextfield.isFocused()) {vendorTextfield.requestFocus(); searchButton.setDisable(false); searchButton.setText("Select F7");return;}
        if (departmentTextfield.isFocused()) {secondDepartmentTextfield.requestFocus(); searchButton.setDisable(false); return;}
        if (locationTextfield.isFocused()) {departmentTextfield.requestFocus(); searchButton.setDisable(false); return;}
        if (gameNameTextfield.isFocused()) {locationTextfield.requestFocus(); searchButton.setDisable(false); return;}
        if (gameNumberTextfield.isFocused()) {gameNameTextfield.requestFocus(); searchButton.setDisable(false); return;}
        }
    }
   
    public void editPaneSaveButtonDo() throws SQLException {
        Detail updateDetail = new Detail(editPaneDate.getValue(), Double.parseDouble(editPaneSales.getText()), editPaneCoins.getText(), editPaneTickets.getText(), editPanePrizes.getText(), detailID);
        updateDetail.postDetail();
        gameDetailTable.getItems().addAll(getDetail(staticGameID));
        editButton.setDisable(false);
        searchButton.setDisable(false);
        printButton.setDisable(false);
        newButton.setDisable(false);
        editPane.setVisible(false);
        editButton.setText("Edit F9");
        return;
    }

    private String getNewGameNumber(int whichOne) {
        String newGameNumber = null;
        int gameNumber = 0;
        String sql = null;
        dbStringPath dbsp = new dbStringPath();
        dbsp.setName();

        
        if (whichOne == 1) {
            sql = "SELECT NewNumber.GameNumber, NewNumber.GameID FROM NewNumber;";
        } else {
            sql = "SELECT NewNumber.GameNumber, NewNumber.GameID FROM NewNumber;";            
        }
        try {
        conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "GamesDB.accdb");
        st = conn.createStatement();
        rs = st.executeQuery(sql);
        rs.next();
        if (whichOne ==1){
            gameNumber = Integer.parseInt(rs.getString("GameNumber")) + 1;
        } else {
            gameNumber = Integer.parseInt(rs.getString("GameID")) + 1;            
        }
        newGameNumber = String.valueOf(gameNumber);
        } catch (SQLException ex) {
            System.out.println("newGameNumber Error " + ex);
        } finally {
            try {
               rs.close();
            } catch (SQLException ec) {
            }
            try {
               st.close();
            } catch (SQLException ec) {
            }
        }
        sql = null;
        if (whichOne==1) {
            sql = "UPDATE NewNumber SET NewNumber.[GameNumber] = ? WHERE ((NewNumber.ID) = '3'))";
        } else {
            sql = "UPDATE NewNumber SET NewNumber.[GameID] = ? WHERE ((NewNumber.ID) = '3'))";
        }
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, newGameNumber);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return newGameNumber;
    }
    
    public void editButtonGo() {
        if (editPane.isVisible()) {
            editPaneSave.fire();
         return;
        } else {
        if (editMode) {
            String newGameNumber = "";
            String newGameID = "";
            String taxableIs = "";
            if (taxableRadio.isSelected()){ 
               taxableIs = "1";
            }else{
                taxableIs = "0";
            }
            if (newButton.isDisable()) {
                newGameNumber = getNewGameNumber(1);
                newGameID = getNewGameNumber(2);
               try {
                Game newGame = new Game(gameNameTextfield.getText(), newGameID, newGameNumber, locationTextfield.getText(), departmentTextfield.getText(), secondDepartmentTextfield.getText(), vendorTextfield.getText(), serialNumberTextfield.getText(), purchaseDateTextfield.getValue(), null, null, null, taxableIs); 
                newGame.UpdateGames(1);
               } catch (IllegalArgumentException e) {
                   JOptionPane.showMessageDialog(null, e);
                   return;
               }
            } else {
                try {
                Game newGame = new Game(gameNameTextfield.getText(), staticGameID, gameNumberTextfield.getText(), locationTextfield.getText(), departmentTextfield.getText(), secondDepartmentTextfield.getText(), vendorTextfield.getText(), serialNumberTextfield.getText(), purchaseDateTextfield.getValue(), coinMeterTextfield.getText(), ticketMeterTextfield.getText(), prizeMeterTextfield.getText(), taxableIs);
                newGame.UpdateGames(2);
                newGameNumber = gameNumberTextfield.getText();
               } catch (IllegalArgumentException e) {
                   JOptionPane.showMessageDialog(null, e);
                   return;
               }
            }
            makeAllNoEdit();
            getGame(newGameNumber);
            newButton.setDisable(false);
            printButton.setDisable(false);
            searchButton.setDisable(false);
            return;
        } else {
            makeAllEdit();
            return;
        }
        }
    }
    
    public void makeAllEdit() {
        for (Node node : gridPane1.getChildren()) {
            if (node instanceof TextField) {
                //((TextField) node).setStyle("");
                ((TextField) node).setDisable(false);

            }
        }
        for (Node node : gridPane2.getChildren()) {
            if (node instanceof TextField) {
                //((TextField) node).setText("now what");
                ((TextField) node).setDisable(false);

            }
        }
        gameNameTextfield.requestFocus();
        gameNumberTextfield.setDisable(false);
        editButton.setText("Save F9");
        editMode = true;
    }    
    
    private void makeAllNoEdit() {
    for (Node node : gridPane1.getChildren()) {
        if (node instanceof TextField) {
        ((TextField) node).setStyle("-fx-opacity: 1.0;");
        ((TextField) node).setDisable(true);        
        }
    }
    for (Node node : gridPane2.getChildren()) {
        if (node instanceof TextField) {
        ((TextField) node).setStyle("-fx-opacity: 1.0;");
        ((TextField) node).setDisable(true);        
        }
    }
    //taxableRadio.setDisable(true);
    editButton.setText("Edit F9");
    editMode = false;
}  
    
    private void clearFields() {
        Set<Node> nodes = root.lookupAll(".text-field");
        for (Node node : nodes) {
            ((TextField) node).setText("");
            
        }
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
    
    private void setHighLights() {
        gameNameTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        locationTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        departmentTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        secondDepartmentTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        vendorTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        purchaseDateTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        coinMeterTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        ticketMeterTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        prizeMeterTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        serialNumberTextfield.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal) {highlightTextField();} });
        taxableRadio.focusedProperty().addListener((obs, oldVal, newVal) -> {if (newVal && editMode==false) {JOptionPane.showMessageDialog(null, "You must press F9 to Make changes to Taxable");} });
        
    }
    
    public void keyListener(KeyEvent event){
    switch (event.getCode()) {
        case F1: JOptionPane.showMessageDialog(null, "F1"); break;
        case F2: JOptionPane.showMessageDialog(null, "F2"); break;
        case F3: JOptionPane.showMessageDialog(null, "F3"); break;
        case F4: break;
        case F5: JOptionPane.showMessageDialog(null, "F5"); break;
        case F6: newButton.fire(); break;
        case F7: searchButton.fire(); break;
        case F8: printButton.fire(); break;
        case F9: editButton.fire(); break;
        case F10: JOptionPane.showMessageDialog(null, "F10"); break;
        case F11: JOptionPane.showMessageDialog(null, "F11"); break;
        case F12: JOptionPane.showMessageDialog(null, "F12"); break;
        case ESCAPE: cancelButton.fire(); break;
        case ENTER: enterKeyPressed(); break;
        case DOWN: searchButton.fire();
break;
    default:
        break;
    }
    }
    
    public void LookUpButtonPressed(ActionEvent event) throws IOException {
        //SceneChanger sc = new SceneChanger();
        if (locationTextfield.isFocused()) {
            Bounds boundsInScene = locationTextfield.localToScene(locationTextfield.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "Location", "Select One:", locationTextfield.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());            
            if (!sc.getGameNumber().equals("Location")) {
                locationTextfield.setText(sc.getGameNumber());
                locationTextfield.requestFocus();
            }
            return;
        }
        if (departmentTextfield.isFocused()) {
            Bounds boundsInScene = departmentTextfield.localToScene(departmentTextfield.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "Department", "Select One:", departmentTextfield.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals("Department")) {
                departmentTextfield.setText(sc.getGameNumber());
                departmentTextfield.requestFocus();
            }
            return;
        }
        if (secondDepartmentTextfield.isFocused()) {
            Bounds boundsInScene = secondDepartmentTextfield.localToScene(secondDepartmentTextfield.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "2nd Department", "Select One:", secondDepartmentTextfield.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals("2nd Department")) {
                secondDepartmentTextfield.setText(sc.getGameNumber());
                secondDepartmentTextfield.requestFocus();
            }
            return;
        }
        if (vendorTextfield.isFocused()) {
            Bounds boundsInScene = vendorTextfield.localToScene(vendorTextfield.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "Vendor", "Select One:", vendorTextfield.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());
            if (!sc.getGameNumber().equals("Vendor")) {
                vendorTextfield.setText(sc.getGameNumber());
                vendorTextfield.requestFocus();
            }
            return;
        }
        
        if (!editMode) {
        Stage stageV = (Stage) cancelButton.getScene().getWindow();
        sc.getpassWord(stageV, "/views/games/SearchTableView.fxml", detailID, detailID, 280.00, 75.0);
        //sc.changePopUp(event, "/views/games/SearchTableView.fxml", "List of Games");
        purchaseDateTextfield.requestFocus();   
        getGame(sc.getGameNumber());
        }
    }
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
            Stage stageV = (Stage) cancelButton.getScene().getWindow();
    if (editPane.isVisible()) {
            int n = JOptionPane.showConfirmDialog(null, "This will clear any changes made to the detail record, \n are you sure you want to do this?", "Clear New Changes?", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
               editPane.setVisible(false);
               newButton.setDisable(false);
               editButton.setText("Edit F9");
               searchButton.setDisable(false);
               printButton.setDisable(false);
               return;
            } else {
              return;
            }
            
        }
        if (editMode) {
            int n = JOptionPane.showConfirmDialog(null, "This will clear any changes, \n are you sure you want to do this?", "Clear New Changes?", JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                makeAllNoEdit();
                //clearFields();
                newButton.setDisable(false);
                printButton.setDisable(false);
                searchButton.setDisable(false);
                getGame("01");
            } else {
              return;
            }
        } else {
            stageV.close();
            //sc.changeScenes(stageV, "/views/GameMain.fxml", "Games " + new employeeFX().titleBar);
        }
    }
        
}
