package views.settings;

import Css.cssChanger;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;
import messageBox.messageBox;
import models.settings.CSSModelDetail;
import models.settings.CSSModel;
import sceneChangerFX.SceneChanger_Main;

/**
 * FXML Controller class
 *
 * @author Hannah
 */
public class CSSEditRController implements Initializable {
    @FXML private AnchorPane root;
    @FXML private VBox vboxButtonPane;
    @FXML private Pane primaryBox;
    @FXML private Pane secondaryBox;
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
    @FXML private TabPane tPane;
    @FXML private Tab gamesTab;
    @FXML private Tab depositTab;
    
    @FXML private Button editPaneSave;
    @FXML private static String detailID;
    @FXML private static String staticGameID;
    @FXML private Pane cColorPickerPane;
    @FXML private ColorPicker cPickerRoot;
    @FXML private ColorPicker cPickerButton;
    @FXML private ColorPicker cPickerButtonText;
    @FXML private ColorPicker cPickerPrimaryBox;
    @FXML private ColorPicker cPickerPrimaryBoxText;
    @FXML private ColorPicker cPickerSecondaryBox;
    @FXML private ColorPicker cPickerSecondaryBoxText;
    @FXML private ColorPicker cPickerTabPaneHeader;
    @FXML private ColorPicker cPickerTabTitles;
    @FXML private ColorPicker cPickerTabTitlesSelected;
    @FXML private ColorPicker cPickerTableHeader;
    @FXML private ColorPicker cPickerTableRow;
    @FXML private CheckBox tabHeaderTransparent;
    
     private static final String TAB_HEADER_BACKGROUND_KEY = "my-tab-header-background" ;
     private static final String TABLE_HEADER_BACKGROUND_KEY = "my-column-header" ;
     private static final String TABLE_ROW_BACKGROUND_KEY = "my-table-row" ;
    
    //dbStringPath dbsp = new dbStringPath();
    SceneChanger_Main sc = new SceneChanger_Main();
    cssChanger cssC = new cssChanger();
    @FXML private TableView<CSSModelDetail> gameDetailTable;
    @FXML private TableColumn<CSSModelDetail, LocalDate> gameDateColumn;
    @FXML private TableColumn<CSSModelDetail, Double> gameSalesColumn;
    @FXML private TableColumn<CSSModelDetail, String> gameCoinMeterColumn;
    @FXML private TableColumn<CSSModelDetail, String> gameTicketMeterColumn;
    @FXML private TableColumn<CSSModelDetail, String> gamePrizeMeterColumn;
    @FXML private TableColumn<CSSModelDetail, String> gameIDColumn;
    private final String themeUrl = getClass().getResource("/Css/CSSEditR.css").toExternalForm();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
       gameDateColumn.setCellValueFactory(new PropertyValueFactory<>("detailDateColumn"));

       gameDateColumn.setCellFactory(column -> {
            return new TableCell<CSSModelDetail, LocalDate>() {
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
            return new TableCell<CSSModelDetail, Double>() {
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
        root.getStylesheets().add(themeUrl);
        editPane.setVisible(false);
        setHighLights();
        setKeyCodes();
        getGame("01");
        makeAllNoEdit();
        tPane.getSelectionModel().select(gamesTab);
        setStartColors();
        cColorPickerPane.setStyle("-fx-background-color:#000000");
        cColorPickerPane.setTranslateX(+220);
        TranslateTransition menuTranslation = new TranslateTransition(Duration.millis(500), cColorPickerPane);
        menuTranslation.setFromX(+200);
        menuTranslation.setToX(0);

        cColorPickerPane.setOnMouseEntered(evt -> {
            menuTranslation.setRate(1);
            menuTranslation.play();
        });
        cColorPickerPane.setOnMouseExited(evt -> {
            menuTranslation.setRate(-1);
            menuTranslation.play();
        });

    } 
    
    
    private void setStartColors() {
        cPickerRoot.setValue(Color.BLUE);
        cPickerButton.setValue(Color.ANTIQUEWHITE);
        cPickerButtonText.setValue(Color.BLACK);
        cPickerPrimaryBox.setValue(Color.BROWN);
        cPickerSecondaryBox.setValue(Color.CHOCOLATE);
        cPickerTabTitles.setValue(Color.CORAL);
        cPickerTabTitlesSelected.setValue(Color.CORAL);
        cPickerTableHeader.setValue(Color.GRAY);
        cPickerTableRow.setValue(Color.BLUE);
        tabHeaderTransparent.setSelected(true);
        tPane.setStyle(TAB_HEADER_BACKGROUND_KEY+": blue ;");
        //gameDetailTable.setStyle(TABLE_HEADER_BACKGROUND_KEY+": blue ;");
       // gameDetailTable.setStyle(TABLE_ROW_BACKGROUND_KEY+": red :");
        setTable();
        setTabsSelected();
        getCpickerColor();
    }
    
    
    public void getCpickerColor() {
        Color cColorRoot = cPickerRoot.getValue();
        setRoot(cColorRoot);
        setButtons(); //THIS FUNCTION HAS MULTIPLE GETS AND SETS
        setTabPane();
        setTabsSelected();
        setTable();
        Color cColorPrimaryBox = cPickerPrimaryBox.getValue();
        setPrimaryBox(cColorPrimaryBox);
        Color cColorPrimaryBoxText = cPickerPrimaryBoxText.getValue();
        setPrimaryBoxText(cColorPrimaryBoxText);
        Color cColorSecondaryBox = cPickerSecondaryBox.getValue();
        setSecondaryBox(cColorSecondaryBox);
        Color cColorSecondaryBoxText = cPickerSecondaryBoxText.getValue();
        setSecondaryBoxText(cColorSecondaryBoxText);
        
    }
    
    private void setRoot(Color cColor) {
        root.setStyle("-fx-background-color:" + TestColor(cColor));
    }
    private void setButtons() {
        vboxButtonPane.getChildren().stream().filter((node) -> (node instanceof Button)).forEachOrdered((node) -> {
            ((Button) node).setStyle("-fx-background-color:" + TestColor(cPickerButton.getValue()) + ";-fx-text-fill:" + TestColor(cPickerButtonText.getValue()));
        });
    }
    private void setTabPane() {
        if (!tabHeaderTransparent.isSelected()) {
            //tPane.setStyle("my-tab-header-background: blue ;");
            tPane.setStyle("my-tab-header-background:" + TestColor(cPickerTabPaneHeader.getValue()) + ";-fx-text-fill:" + TestColor(cPickerButtonText.getValue()));
        } else {
            tPane.setStyle("-fx-background-color:" + Color.TRANSPARENT );
        }
        //gamesTab.setStyle("-fx-background-color:" + TestColor(cPickerTabPaneHeader.getValue()) + ";-fx-font-color:" + TestColor(cPickerButtonText.getValue()));
    }    
    private void setGamesTabTitle(int wichOne) {
        switch(wichOne) {
            case 1:
                try {
                gamesTab.setStyle("-fx-background-color:" + TestColor(cPickerTabTitlesSelected.getValue()));
                } catch(Exception e) {}
            break;
            case 2:
                gamesTab.setStyle("-fx-background-color:" + TestColor(cPickerTabTitles.getValue()));
            break;
        }
    }    
    private void setDepositTabTitle(int wichOne) {
        switch(wichOne) {
            case 1:                
                depositTab.setStyle("-fx-background-color:" + TestColor(cPickerTabTitlesSelected.getValue()));
            break;
            case 2:
                try {
                depositTab.setStyle("-fx-background-color:" + TestColor(cPickerTabTitles.getValue()));
                }catch(Exception e) {}
            break;
        }
    }    
    private void setTable() {
        gameDetailTable.setStyle("my-column-header:" + TestColor(cPickerTableHeader.getValue()) + ";my-table-row:" + TestColor(cPickerTableRow.getValue()));
    }
    private void setPrimaryBox(Color cColor) {
        primaryBox.setStyle("-fx-background-color:" + TestColor(cColor));
    }
    private void setPrimaryBoxText(Color cColor) {
        gridPane1.getChildren().stream().filter((node) -> (node instanceof Label)).forEachOrdered((node) -> {
            ((Label) node).setStyle("-fx-text-fill:" + TestColor(cColor));
        });
    }
    private void setSecondaryBox(Color cColor) {
        secondaryBox.setStyle("-fx-background-color:" + TestColor(cColor));
    }
    private void setSecondaryBoxText(Color cColor) {
        gridPane2.getChildren().stream().filter((node) -> (node instanceof Label)).forEachOrdered((node) -> {
            ((Label) node).setStyle("-fx-text-fill:" + TestColor(cColor));
        });
    }
    
    public String TestColor(Color cColor) {

        int green = (int) (cColor.getGreen() * 255);
        String greenString = Integer.toHexString(green);

        int red = (int) (cColor.getRed() * 255);
        String redString = Integer.toHexString(red);

        int blue = (int) (cColor.getBlue() * 255);
        String blueString = Integer.toHexString(blue);

        String hexColor = "#" + redString + greenString + blueString;

        return hexColor;
    }
    
    
    public void setTabsSelected() {
        if (gamesTab.isSelected()) {
            setGamesTabTitle(1); 
            setDepositTabTitle(2); 
        } else {
            setGamesTabTitle(2); 
            setDepositTabTitle(1); 
        }
    }
    
    
    
    public void LookUpButtonPressed2(ActionEvent event) throws IOException {
        if (locationTextfield.isFocused()) {
            Bounds boundsInScene = locationTextfield.localToScene(locationTextfield.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "Location", "Select One:", locationTextfield.getText(), boundsInScene.getMinX(), boundsInScene.getMinY());            
            if (!sc.getGameNumber().equals("Location")) {
                locationTextfield.setText(sc.getGameNumber());
                locationTextfield.requestFocus();
            } 
        locationTextfield.setText(sc.getGameNumber());
        locationTextfield.requestFocus();
        }
    }
    
    

    public ObservableList<CSSModelDetail> getDetail(String Id) throws SQLException {
    ObservableList<CSSModelDetail> details = FXCollections.observableArrayList();
    details.clear();
    gameDetailTable.getItems().clear();
    CSSModelDetail newDetail = new CSSModelDetail(new java.sql.Date(System.currentTimeMillis()).toLocalDate(), Double.parseDouble("345.25"), "34536", "66678", "33245", "321");
    details.add(newDetail);
    newDetail = new CSSModelDetail(new java.sql.Date(System.currentTimeMillis()-1).toLocalDate(), Double.parseDouble("222.25"), "34536", "62878", "33342", "322");
    details.add(newDetail);
    newDetail = new CSSModelDetail(new java.sql.Date(System.currentTimeMillis()-2).toLocalDate(), Double.parseDouble("123.00"), "33426", "45378", "88245", "323");
    details.add(newDetail);
    return details;
    
}   
    
    private void getGame(String ID) {
        LocalDate dt = new GetDay().NOW_LOCAL_DATE();
    
    try {
    CSSModel game = new CSSModel("asdfasdf", "123232", "12345", "ffff", "Ddddd", "dddd", "dddd", "9999", dt, "1234", "1234", "1234", "1234"); 
            //Game game = new Game(ID);
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
                if (game.getTaxable() == 0) {
                    taxableRadio.setSelected(false);
                }else{
                    taxableRadio.setSelected(true);
                }

                staticGameID = game.getGameID();
                try {
                //JOptionPane.showMessageDialog(null, game.getTaxable());
                    gameDetailTable.getItems().addAll(getDetail("321"));
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
            searchButton.setDisable(true);
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
        }
    }
   
    public void editPaneSaveButtonDo() throws SQLException {
        CSSModelDetail updateDetail = new CSSModelDetail(editPaneDate.getValue(), Double.parseDouble(editPaneSales.getText()), editPaneCoins.getText(), editPaneTickets.getText(), editPanePrizes.getText(), detailID);
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
        Connection conn = null;
        int gameNumber = 0;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        dbStringPath dbsp = new dbStringPath();
        dbsp.setName();

        
        if (whichOne == 1) {
            sql = "SELECT NewNumber.GameNumber, NewNumber.GameID FROM NewNumber;";
        } else {
            sql = "SELECT NewNumber.GameNumber, NewNumber.GameID FROM NewNumber;";            
        }
        try {
        conn = DriverManager.getConnection("jdbc:ucanaccess://" + dbsp.pathNameClubDBs + "GamesDB.accdb;immediatelyReleaseResources=true");
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
        }
        //JOptionPane.showMessageDialog(null, newGameNumber);
        return newGameNumber;
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
        gameNumberTextfield.setDisable(true);
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
        
        
        sc.changePopUp(event, "", "");
        purchaseDateTextfield.requestFocus();   
        getGame(sc.getGameNumber());
    }
    
    public void exitButtonPushed(ActionEvent event) throws IOException {
    Stage stageV = (Stage) cancelButton.getScene().getWindow();
    int n=0;
    if (editPane.isVisible()) {
            new messageBox().showNewMemberAlert(Alert.AlertType.ERROR, stageV, "?", "This will clear any changes made to the detail record, \n are you sure you want to do this?");
                    /*JOptionPane.showConfirmDialog(null, "This will clear any changes made to the detail record, \n are you sure you want to do this?", "Clear New Changes?", JOptionPane.YES_NO_OPTION);*/
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
            new messageBox().showNewMemberAlert(Alert.AlertType.ERROR, stageV, "?", "This will clear any changes made to the detail record, \n are you sure you want to do this?");
            if (n == 0) {
                makeAllNoEdit();
                clearFields();
                newButton.setDisable(false);
                printButton.setDisable(false);
                searchButton.setDisable(false);
                getGame("01");
            } else {
              return;
            }
        } else {
            //sc.changeScenes(stageV, "/views/Main.fxml", "Pojo Main " + new employeeFX1().titleBar);
            stageV.close();
        }
    }
        
}
