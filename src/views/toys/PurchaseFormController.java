package views.toys;

import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import commoncodes.IsItANumber;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.scene.control.Label;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import pWordFX.empFX;
import pWordFX.employeeFX;
import commoncodes.IsItANumber1;
import dbpathnames.dbStringPath;
import java.sql.Date;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import messageBox.messageBox;
import models.games.Game;
import models.toys.Toys;
import models.toys.ToysDB;
import models.toys.ToysDetail;
import popUpFX.SelectPopUpController;
import reports.toys.XLSGameReport;
import reports.toys.XLSGameReportALL;
import reports.toys.XLSInventoryReport;
import reports.toys.XLSNotCountedReport;
import sceneChangerFX.SceneChanger_Main;
import settings.settingsFXML;
import views.counterPopUp.QTYTouchScreenController;



public class PurchaseFormController implements Initializable {
    @FXML private TextField iItemNumber;
    @FXML private TextField iItemName;
    @FXML private TextField iVendor;
    @FXML private TextField iDepartment;
    @FXML private TextField iClass;
    @FXML private TextField iClassII;
    @FXML private TextField iClassIII;
    @FXML private TextField iClassIIII;
    @FXML private TextField iTicketPrice;
    @FXML private DatePicker iPurchaseDate;
    @FXML private TextField iInvoiceNumber;
    @FXML private TextField iNumberOfUnits;
    @FXML private TextField iUnitType;
    @FXML private TextField iPricePerUnit;
    @FXML private TextField iTotal;
    
    @FXML private Button reOrderButton;
    @FXML private Button newOrderButton;
    @FXML private Button searchButton;
    @FXML private Button exitButton;
    @FXML private Button reportButton;
    @FXML private Button NOTCountedreportButton;
    @FXML private Button merchandizerReportButton;
    @FXML private Button merchandizerReportButtonALL;
    @FXML private Label scrollTextLable;
    @FXML private Label errorLabel;
    @FXML private Label ticketPriceLabel;
    @FXML private Pane errorPane;
    @FXML private Pane buttonPane;
    @FXML private Pane iItemNumberPane;
    @FXML private AnchorPane root;
    @FXML private CheckBox prtLabel;
    @FXML private CheckBox inventoryCountCheckBox;
    @FXML private CheckBox priceChangeCheckBox;
    @FXML private CheckBox metersCheckBox;

    @FXML private TextField VIPSpecialsField; //THIS IS JUST A PLACE HOLDER GET RID OF IT LATER
    @FXML private TextField scanField;  //THIS IS JUST A PLACE HOLDER GET RID OF IT LATER
    @FXML private PasswordField empNumber;
    
    @FXML private TableView<ToysDetail> inventoryTable;
    @FXML private TableColumn<ToysDetail, String> ID;
    @FXML private TableColumn<ToysDetail, String> ItemLastScanned;
    @FXML private TableColumn<ToysDetail, String> QTY;
    
    

    String RECEIPTS_DATE = "" + String.valueOf(LocalDate.now().getMonthValue()) + "" + new IsItANumber1().isLessThenTen(String.valueOf(LocalDate.now().getDayOfMonth())) + "";
    DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    DateTimeFormatter myDateFormatterR = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    public static String en, fn, ln, q, css, rNumber, rWinner, isEMail = "";
    public static int vAmt, bal, customerBalance, VIPMultiplier, rCeipt, lastItemSortNumber;
    private static String EMAIL_SUBJECT = "Puchase Controller Error";
    SceneChanger_Main sc = new SceneChanger_Main();
    DecimalFormat df = new DecimalFormat("###.00");
    dbStringPath dbsp1 = new dbStringPath();
    messageBox mBox = new messageBox();
    public static Double layoutX, layoutY;
    private boolean changeWasMade = false, UPDATE = false;
    employeeFX eFX = new employeeFX();
    cssChanger cssC = new cssChanger();
    LocalDate NOW = LocalDate.now();
    public static ArrayList<empFX> E;
    public static ArrayList<String> V;
    public static ArrayList<Toys> forScanList;
    public static ArrayList<Game> gamesForScanList;
    static String tMessage = "";
    ToysDB TDB = new ToysDB();
    public static Stage stageV;
    public static Window owner;
    PrintWriter pw = null;
    static String msg = null;
    static String orderType, currentGame = "654654654654654654";
    Toys T, TID;
    long lastTime = 0;
    ToysDetail TD;
    Bounds boundsInScene;
    private static ActionEvent AE;
    //WE NEED TO PASS THE EMPNUMBER
    


    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = root.localToScene(root.getBoundsInLocal());
        dbsp1.setName();
        layoutX = errorPane.getLayoutX();
        layoutY = errorPane.getLayoutY();
        scrollTextLable.setVisible(false);
        addTextfieldListeners();
        root.getStylesheets().add(css);
        ItemLastScanned.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        ItemLastScanned.setCellValueFactory(new PropertyValueFactory<>("Name"));
        QTY.setCellValueFactory(new PropertyValueFactory<>("QTY"));
        inventoryTable.setVisible(false);
        setKeyCodes();
        SetChangeListener();
        setToUpper();
        setHighlightListenerVIP(root);
        ClearFields();
        newOrderButton.setDisable(true);
        reOrderButton.setDisable(true);
        iPurchaseDate.setValue(NOW);
        mBox.showErrorClear(errorLabel, errorPane);
        Platform.runLater(() -> getForScanList());
        Platform.runLater(() -> getGamesForScanList());
        Platform.runLater(() -> iItemNumber.requestFocus());
    }

    private boolean GetToy(String ID) {
        boolean gotToy = false;
        ClearFields();
        try {
            T = TDB.getToy(ID);
            if (T.getName().trim().equals("ErrorFailed")) {

            }
            if (!T.getName().trim().equals("ErrorFailed")) {
                setOrderReorder(0);
                iItemName.setText(T.getName());
                iVendor.setText(T.getNumber());
                iItemNumber.setText(T.getNumber());
                iDepartment.setText(T.getDpart());
                iVendor.setText(T.getVendor());
                iClass.setText(T.getClassI());
                iClassII.setText(T.getClassII());
                iClassIII.setText(T.getClassIII());
                iClassIIII.setText(T.getClassIIII());
                iPricePerUnit.setText(T.getPPU().toString());
                iTicketPrice.setText(String.valueOf(T.getTickets()));
                iUnitType.setText(T.getClassIIII());
                System.out.println("here in T.deparment " + T.getDpart());
                if (T.getDpart().equals("MERCHANDIZER")) {
                    System.out.println("here in T.deparment " + T.getDpart());
                    ticketPriceLabel.setText("Coins Per Play");
                }
                TDB.disConnect();
                gotToy = true;
            } else {
                setOrderReorder(1);
                iDepartment.setText("REDEMPTION");
                iClass.setText("REDEMPTION");
                iClassII.setText("0-100");
                iClassIII.setText("TOYS");
                iClassIIII.setText("EACH");
                iUnitType.setText("EACH");
                TDB.disConnect();
            }
        } catch (SQLException ex) {
            System.out.println("Error in TOYSMAINCONTROLLER (GetToy()): " + ex);
            //new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "Error in (GetToys()): " + ex, "error");
        }
        TDB.disConnect();
        return gotToy;
    }

    private void getForScanList() {
        buttonPane.setVisible(false);
        forScanList = TDB.GetAllToysForScanFeild();
    }

    private void getGamesForScanList() {
        gamesForScanList = TDB.GetAllGamesForScanFeild();
    }

    private boolean isToyValidInArrayList(String n) {
        boolean toyValid = false;

        // HERE IS WHERE I STARTED
        for (Toys l1 : forScanList) {
            if (n.equals(l1.getNumber())) {
                setOrderReorder(0);
                T = new Toys(l1.getNumber(), l1.getItemID(), l1.getName(), l1.getDpart(), l1.getClassI(), l1.getClassII(), l1.getClassIII(), l1.getClassIIII(), Double.parseDouble(l1.getPPU().toString()), iPurchaseDate.getValue(), l1.getVendor(), Integer.valueOf(l1.getTickets()), 0);
                iItemName.setText(l1.getName());
                iVendor.setText(l1.getVendor());
                iItemNumber.setText(l1.getNumber());
                iDepartment.setText(l1.getDpart());
                iVendor.setText(l1.getVendor());
                iClass.setText(l1.getClassI());
                iClassII.setText(l1.getClassII());
                iClassIII.setText(l1.getClassIII());
                iClassIIII.setText(l1.getClassIIII());
                iPricePerUnit.setText(l1.getPPU().toString());
                iTicketPrice.setText(String.valueOf(l1.getTickets()));
                iUnitType.setText(l1.getClassIIII());
                toyValid = true;
                if (T.getDpart().equals("MERCHANDIZER")) {
                    ticketPriceLabel.setText("Coins Per Play");
                }

            }
        }
        // HERE IS WHERE I ENDED
        return toyValid;
    }
    
    
    
    
    
     private boolean isToyValidInArrayListID(String ID) {
        boolean toyValid = false;
        System.out.println("asgdfgsdfgsdfgsdfgsdfgsfdgsdf " + ID);
        // HERE IS WHERE I STARTED
        for (Toys toyID : forScanList) {
            if (ID.equals(toyID.getItemID())) {
                setOrderReorder(0);
        System.out.println("we are in the arraylist " + toyID.getItemID());
                TID = new Toys(toyID.getNumber(), toyID.getItemID(), toyID.getName(), toyID.getDpart(), toyID.getClassI(), toyID.getClassII(), toyID.getClassIII(), toyID.getClassIIII(), Double.parseDouble(toyID.getPPU().toString()), iPurchaseDate.getValue(), toyID.getVendor(), Integer.valueOf(toyID.getTickets()), 0);
                toyValid = true;
            }
        }

        // HERE IS WHERE I ENDED
        return toyValid;
    }
    

    private boolean isGameValidInArrayList(String n) {
        boolean toyValid = false;

        // HERE IS WHERE I STARTED
        for (Game l1 : gamesForScanList) {
            if (n.equals(l1.getGameNumber())) {
                setOrderReorder(1);
                T = new Toys(l1.getGameNumber(), l1.getGameID(), l1.getGameName(), l1.getDepartment(), l1.getLocation(), "", "", "", 0.0, LocalDate.now(), l1.getVendor(), 0, 0);
                iItemName.setText(l1.getGameName());
                iVendor.setText(l1.getVendor());
                iItemNumber.setText(l1.getGameNumber());
                iDepartment.setText(l1.getDepartment());
                iVendor.setText(l1.getVendor());
                iClass.setText(l1.getSecondDepartment());
                iClassII.setText("");
                iClassIII.setText("");
                iClassIIII.setText("");
                iPricePerUnit.setText("0");
                iTicketPrice.setText("0");
                iUnitType.setText("EACH");
                toyValid = true;
            }
            /*else {
                setOrderReorder(1);
                iItemName.setText("");
                iDepartment.setText("REDEMPTION");
                iClass.setText("REDEMPTION");
                iClassII.setText("0-100");
                iClassIII.setText("TOYS");
                iClassIIII.setText("EACH");
                iUnitType.setText("EACH");
            }*/
        }

        // HERE IS WHERE I ENDED
        return toyValid;
    }


    
    
     private void getTableItemsForCranes() {
        inventoryTable.getItems().clear();
        inventoryTable.getItems().addAll(TDB.getAllPrizesAttached(iItemNumber.getText().trim()));
    }
    
     private void getTableItemsForCranesInventory() {
        inventoryTable.getItems().clear();
        inventoryTable.getItems().addAll(TDB.getAllPrizesPreviousEnties(T.getItemID()));
    }
    
    
    private void getTableItems() {
        inventoryTable.getItems().clear();
        inventoryTable.getItems().addAll(TDB.getAllDetailForLastItemScanned(iInvoiceNumber.getText().trim()));
    }
    
    public void tableItemClicked() {
        TablePosition pos = inventoryTable.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        lastItemSortNumber = inventoryTable.getItems().get(row).getTickets();
        iPurchaseDate.setValue(inventoryTable.getItems().get(row).getPurchDate());
        iInvoiceNumber.setText(inventoryTable.getItems().get(row).getInvoiceNumb());
        int lastItemQty = inventoryTable.getItems().get(row).getQTY();
        iNumberOfUnits.setText(String.valueOf(lastItemQty));
        System.out.println(" here is the Inumber " + inventoryTable.getItems().get(row).getItemID());
        isToyValidInArrayListID(inventoryTable.getItems().get(row).getItemID());
        iItemNumber.setText(TID.getNumber());
        //iItemNumber.setText("");
        iItemNumber.requestFocus();
        UPDATE = true;
        enterKeyPressed();
        
    }
    
    private void getMeterItems() {
        inventoryTable.getItems().addAll(TDB.getAllDetailForLastMeterScanned(iInvoiceNumber.getText().trim(), iPurchaseDate.getValue()));
    }
    
    
    
    
    public void setOrderReorder(int tt) {
        if (tt>0) {
            //NEW Order
            orderType = "NEW";
            reOrderButton.setDisable(true);
            newOrderButton.setDisable(true);
        } else {
            //RE Order
            orderType = "RE";
            reOrderButton.setDisable(true);
            newOrderButton.setDisable(true);
        }
    }

    public void setHighlightListenerVIP(Pane r) {
        Set<Node> nodes = r.lookupAll(".text-field");
        for (Node node : nodes) {
            node.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    highlightTextFieldVIP(r);
                }
            });
        }
    }

    public void highlightTextFieldVIP(Pane r) {
        Set<Node> nodes = r.lookupAll(".text-field");
        for (Node node : nodes) {
            if (node.isFocused()) {
                node.setStyle("-fx-background-color: #fdfdaf; -fx-text-fill: #000000");
                //System.out.println(node.getId());
                //checkFocus(node.getId());
            } else {
                if (!node.isDisable()) {
                    node.setStyle("-fx-background-color: White");
                }
            }
        }
    }

    public void ReButtonPushed() {
        if (UPDATE) {
            System.out.println("OUPDATE is TRUE");
            TD = new ToysDetail(iItemName.getText().trim(),T.getNumber(),T.getItemID(),iUnitType.getText().trim(),iDepartment.getText().trim(),iInvoiceNumber.getText().trim(),en,Integer.valueOf(iTicketPrice.getText().trim()),Integer.valueOf(iNumberOfUnits.getText().trim()),Double.valueOf(iPricePerUnit.getText().trim()),Double.valueOf(iTotal.getText().trim()),iPurchaseDate.getValue());
            TDB.updateDetailPurchase(TD, lastItemSortNumber);
            Platform.runLater(() -> ClearFields());
            Platform.runLater(() -> iItemNumber.requestFocus());
            if (inventoryCountCheckBox.isSelected()) {
                Platform.runLater(() -> inventoryTable.getItems().clear());
                Platform.runLater(() -> getTableItems());
                Platform.runLater(() -> buttonPane.setVisible(true));
            }
            if (metersCheckBox.isSelected()) {
                Platform.runLater(() -> inventoryTable.getItems().clear());
                Platform.runLater(() -> getMeterItems());
                Platform.runLater(() -> buttonPane.setVisible(true));
            }
            TDB.disConnect();
            UPDATE = false;
            Platform.runLater(() -> iItemNumber.requestFocus());
            return;
        }
        try {
            if (GetChangeWasMade()) {
                T = new Toys(T.getNumber(), T.getItemID(), iItemName.getText().trim(), iDepartment.getText().trim(), iClass.getText().trim(), iClassII.getText().trim(), iClassIII.getText().trim(), iClassIIII.getText().trim(), Double.parseDouble(iPricePerUnit.getText().trim()), iPurchaseDate.getValue(), iVendor.getText().trim(), Integer.valueOf(iTicketPrice.getText().trim()), 0);
                TDB.updateToy(T);
                System.out.println("Here in ReButtonPuched and changeWasMade is " + this.changeWasMade);
            }
            //T = new Toys(T.getNumber(), T.getItemID(), iItemName.getText().trim(), iDepartment.getText().trim(), iClass.getText().trim(), iClassII.getText().trim(), iClassIII.getText().trim(), iClassIIII.getText().trim(), Double.parseDouble(iPricePerUnit.getText().trim()), iPurchaseDate.getValue(), iVendor.getText().trim(), Integer.valueOf(iTicketPrice.getText().trim()), 0);
            //if (!TDB.updateToy(T)) {
                //new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "TDB.updateToy() Incomplete " + T.getNumber() + " " + T.getName() + " " + T.getPPU() + " " + T.getTickets(), "error");
            //} else {
                TD = new ToysDetail(iItemName.getText().trim(),T.getNumber(),T.getItemID(),iUnitType.getText().trim(),iDepartment.getText().trim(),iInvoiceNumber.getText().trim(),en,Integer.valueOf(iTicketPrice.getText().trim()),Integer.valueOf(iNumberOfUnits.getText().trim()),Double.valueOf(iPricePerUnit.getText().trim()),Double.valueOf(iTotal.getText().trim()),iPurchaseDate.getValue());
                TDB.putDetailPurchase(TD, iItemName.getText().trim(), iTicketPrice.getText().trim());
                ifPrintLabel(TD);
                iItemNumber.clear();
                Platform.runLater(() -> ClearFields());
                Platform.runLater(() -> iItemNumber.requestFocus());
                if (inventoryCountCheckBox.isSelected()) {
                    Platform.runLater(() -> inventoryTable.getItems().clear());
                    Platform.runLater(() -> getTableItems());
                    Platform.runLater(() -> buttonPane.setVisible(true));
                }
                if (metersCheckBox.isSelected()) {
                    Platform.runLater(() -> inventoryTable.getItems().clear());
                    Platform.runLater(() -> getMeterItems());
                    Platform.runLater(() -> buttonPane.setVisible(true));
                }
                TDB.disConnect();
            //}
        } catch (SQLException ex) {
            mBox.showAlert(Alert.AlertType.ERROR, owner, ln, "An Error has occurred. \n\n" +  ex.toString());
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "TDB.updateToy() Incomplete " + ex.toString(), "error");
        }
    }

    public void NewButtonPushed() {
        try {
            //new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "TDB.InsertNewToy() Incomplete ", "errors");
            T = new Toys(iItemNumber.getText().trim(), TDB.getNewToyID(), iItemName.getText().trim(), iDepartment.getText().trim(), iClass.getText().trim(), iClassII.getText().trim(), iClassIII.getText().trim(), iClassIIII.getText().trim(), Double.parseDouble(iPricePerUnit.getText().trim()), iPurchaseDate.getValue(), iVendor.getText().trim(), Integer.valueOf(iTicketPrice.getText().trim()), 0);
            //System.out.println(T.getNumber()+ " " + T.getItemID()+ " " +  iItemName.getText().trim()+ " " +  iDepartment.getText().trim()+ " " +  iClass.getText().trim()+ " " +  iClassII.getText().trim()+ " " +  iClassIII.getText().trim()+ " " +  iClassIIII.getText().trim()+ " " +  Double.parseDouble(iPricePerUnit.getText().trim())+ " " +  iPurchaseDate.getValue()+ " " +  iVendor.getText().trim()+ " " +  Integer.valueOf(iTicketPrice.getText().trim()));
            // THIS NEEDS TO BE INSERTNEWITEM
            TDB.InsertNewToy(T);
            TD = new ToysDetail(iItemName.getText().trim(), T.getNumber(), T.getItemID(), iUnitType.getText().trim(),iDepartment.getText().trim(), iInvoiceNumber.getText().trim(), en, Integer.valueOf(iTicketPrice.getText().trim()), Integer.valueOf(iNumberOfUnits.getText().trim()), Double.valueOf(iPricePerUnit.getText().trim()), Double.valueOf(iTotal.getText().trim()), iPurchaseDate.getValue());
            TDB.putDetailPurchase(TD, iItemName.getText().trim(), iTicketPrice.getText().trim());
            ifPrintLabel(TD);
            iItemNumber.clear();
            Platform.runLater(() -> ClearFields());
            Platform.runLater(() -> iItemNumber.requestFocus());
            TDB.disConnect();
            Platform.runLater(() -> getForScanList());
        } catch (SQLException ex) {
            mBox.showAlert(Alert.AlertType.ERROR, owner, ln, "An Error has occurred. \n\n" +  ex.toString());
            new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "TDB.InsertNewToy() Incomplete " + ex.toString(), "errors");
        }
    }

    
    private void ifPrintLabel(ToysDetail TD) {
        if (prtLabel.isSelected()) {
            //mBox.showAlert(Alert.AlertType.ERROR, null, "Hey", "we are here");
            try {
                TDB.InsertLabel(TD);
            } catch (SQLException ex) {
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "Insert Label Error " + ex, "errors");
            }
        }
    }
    
    public void whatButton(ActionEvent event) {
        iNumberOfUnits.setText(iNumberOfUnits.getText() + ((Button)event.getSource()).getText());

    }
    
    public void whatButtonBackSpace(ActionEvent event) {
        String str = iNumberOfUnits.getText();
        if (str != null && str.length() > 0) {
            System.out.println(str + " " + str.substring(0, str.length() - 1));
            iNumberOfUnits.setText(str.substring(0, str.length() - 1));
    }
    }
    
    public void whatButtonEnter(ActionEvent event) {
        if (!iNumberOfUnits.getText().isEmpty()) {
            enterKeyPressed();
        };

    }
    
    
    
    public void getLastInvoice(MouseEvent e) {
        if (e.getClickCount() == 2) {
            try {
                //sc.getpassWord(getStageV(), "/pWordFX/NewItemNumber.fxml", "Number", "How Many Days?:", boundsInScene.getMinX()+100.0, boundsInScene.getMinY()+100.0);
            FXMLLoader Loader = new FXMLLoader();
            QTYTouchScreenController controller = Loader.<QTYTouchScreenController>getController();
            controller.tString = "How Many Days?";
            controller.ctString = "How Many Days?";
            //controller.setPreviousText(prevText);

                sc.getpassWord(stageV, "/views/counterPopUp/QTYTouchScreen.fxml", "How Many Days", "How Many Days:", boundsInScene.getMinX()+100.0, boundsInScene.getMinY()+100.0);
            } catch (IOException ex) {
                new Mail_JavaFX1().sendEmailTo("SelectPopUp Error 1", "SelectPopFX error. " + ex.toString(), "errors");
                System.out.println(ex);
            }
            String days = sc.getGameNumber();
            TDB.getLastInvoiceNumbers(Integer.parseInt(days));
            getDropBox(AE, iInvoiceNumber, "iInvoiceNumber");
        }
    }
    
    
    
    
    public int getPropertiesFile() {
        new dbStringPath().setName();
        int p = 0;
        File file = new File(new dbStringPath().pathNameLocal + "configCounterFXML.properties");
        if (file.exists() && file.canRead()) {
            p = Integer.parseInt(new settingsFXML().getCounterSettings("stage"));
        }
        return p;
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-10s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    private void setrNumber(String r) {
        this.rNumber = r;
    }

    private String getrNumber() {
        return this.rNumber;
    }

    
    private void setToUpper() {
        //showErrorClear();
        iItemNumber.textProperty().addListener((ov, oldValue, newValue) -> {
            iItemNumber.setText(newValue.toUpperCase());
        });
        iItemName.textProperty().addListener((ov, oldValue, newValue) -> {
            iItemName.setText(newValue.toUpperCase());
        });
        iVendor.textProperty().addListener((ov, oldValue, newValue) -> {
            iVendor.setText(newValue.toUpperCase());
        });
        iDepartment.textProperty().addListener((ov, oldValue, newValue) -> {
            iDepartment.setText(newValue.toUpperCase());
        });
        iClass.textProperty().addListener((ov, oldValue, newValue) -> {
            iClass.setText(newValue.toUpperCase());
        });
        iClassII.textProperty().addListener((ov, oldValue, newValue) -> {
            iClassII.setText(newValue.toUpperCase());
        });
        iClassIII.textProperty().addListener((ov, oldValue, newValue) -> {
            iClassIII.setText(newValue.toUpperCase());
        });
        iClassIIII.textProperty().addListener((ov, oldValue, newValue) -> {
            iClassIIII.setText(newValue.toUpperCase());
        });
        iUnitType.textProperty().addListener((ov, oldValue, newValue) -> {
            iClassIIII.setText(iUnitType.getText().trim());
            iUnitType.setText(newValue.toUpperCase());
        });
        iInvoiceNumber.textProperty().addListener((ov, oldValue, newValue) -> {
            iInvoiceNumber.setText(newValue.toUpperCase());
        });
        //zipCode.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
        //    if (newValue.intValue() > oldValue.intValue()) {
        //        if (zipCode.getText().length() >= 5) {
        //            zipCode.setText(zipCode.getText().substring(0, 5));
        //        }
        //    }
        //});
        
    }
    
    private void setrCeipt(int r) {
        this.rCeipt = r;
    }

    private int getrCeipt() {
        return this.rCeipt;
    }

    public void addTextfieldListeners() {
        iTicketPrice.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber().checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Amount of Ticket Price can Only be Numbers");
                        iTicketPrice.clear();
                        iTicketPrice.requestFocus();
                        return;
                    }
                }
        );
        iNumberOfUnits.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber().checkNumbers(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Number of Units can Only be Numbers");
                        iNumberOfUnits.clear();
                        iNumberOfUnits.requestFocus();
                        return;
                    }
                }
        );
        iPricePerUnit.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        new IsItANumber().checkNumbersPeriod(newValue);
                    } catch (Exception e) {
                        mBox.showAlert(Alert.AlertType.ERROR, owner, "TextField Error", "Price Per Unit can Only be Numbers " + e);
                        iPricePerUnit.clear();
                        iPricePerUnit.requestFocus();
                        return;
                    }
                }
        );
    }

    
    public void inventoryCheckBoxSelected() {
        if (inventoryCountCheckBox.isSelected()) {
            inventoryTable.setVisible(true);
            Platform.runLater(() -> buttonPane.setVisible(true));
        } else {
            inventoryTable.setVisible(false);
            Platform.runLater(() -> buttonPane.setVisible(false));            
        }
    }
    
    public void metersCheckBoxSelected() {
        if (metersCheckBox.isSelected()) {
            inventoryTable.setVisible(true);
            Platform.runLater(() -> buttonPane.setVisible(true));
            iInvoiceNumber.setText("COINMETER");
           Platform.runLater(() -> inventoryTable.getItems().clear());
           Platform.runLater(() -> getMeterItems());
            iItemNumber.requestFocus();
        } else {
            inventoryTable.setVisible(false);
            Platform.runLater(() -> buttonPane.setVisible(false));
        }
    }
    
    public void lastItemButtonPushed() {
        iItemNumber.setText(TD.getiNumber());
        iItemNumber.requestFocus();
        enterKeyPressed();
        iNumberOfUnits.setText(String.valueOf(TD.getQTY()));
        //sortNumber = getLastItemSortNumber();
        UPDATE = true;
        
    }
    
    public void enterKeyPressed() {
        /*if (iItemNumber.isFocused()) {
            if (GetToy(iItemNumber.getText())){
                if (inventoryCountCheckBox.isSelected()) {
                    iNumberOfUnits.requestFocus();
                    return;
                }
                if (priceChangeCheckBox.isSelected()) {
                    iTicketPrice.requestFocus();
                    return;
                }
                iItemName.requestFocus();
                return;
            } else {
                iItemName.requestFocus();
            }
            return;
        }*/
        if (iItemNumber.isFocused()) {
            if (isToyValidInArrayList(iItemNumber.getText())){
                if (inventoryCountCheckBox.isSelected()) {
                    getTableItemsForCranesInventory();
                    iNumberOfUnits.requestFocus();
                    return;
                }
                if (priceChangeCheckBox.isSelected()) {
                    iTicketPrice.requestFocus();
                    return;
                }
                if (metersCheckBox.isSelected()) {
                    if (!currentGame.equals(iItemNumber.getText())) {
                        iInvoiceNumber.setText("COINMETER");
                        getTableItemsForCranes();
                    } else {
                    if (iInvoiceNumber.getText().equals("COINMETER")) {
                        iInvoiceNumber.setText("PRIZEMETER");
                        getTableItemsForCranes();
                    } else {
                        iInvoiceNumber.setText("COINMETER");
                        getTableItemsForCranes();
                    }
                    }
                    currentGame = iItemNumber.getText();
                    iNumberOfUnits.requestFocus();
                    return;
                }
                iItemName.requestFocus();
                return;
            } else {
                if (isGameValidInArrayList(iItemNumber.getText())) {
                    int mBoxi = mBox.confirmMakeThisChangeButtonTitles(Alert.AlertType.ERROR, null, "STOP!", "This could be a game number?", "Use Game", "New Item");
                    System.out.println(mBoxi);
                    if (mBoxi == 1) {
                        iItemName.requestFocus();
                        return;
                    } else {
                        setOrderReorder(1);
                        iItemName.setText("");
                        iDepartment.setText("REDEMPTION");
                        iClass.setText("REDEMPTION");
                        iClassII.setText("0-100");
                        iClassIII.setText("TOYS");
                        iClassIIII.setText("EACH");
                        iUnitType.setText("EACH");
                        iItemName.requestFocus();

                    }
                } else {
                    setOrderReorder(1);
                    iItemName.setText("");
                    iDepartment.setText("REDEMPTION");
                    iClass.setText("REDEMPTION");
                    iClassII.setText("0-100");
                    iClassIII.setText("TOYS");
                    iClassIIII.setText("EACH");
                    iUnitType.setText("EACH");
                    iItemName.requestFocus();
                    iItemName.requestFocus();
                    return;
                 
                }
            }
            return;
        }
        if(iItemName.isFocused()) {
            iVendor.requestFocus();
            return;
        }
        if(iVendor.isFocused()) {
            iDepartment.requestFocus();
            return;
        }
        if(iDepartment.isFocused()) {
            iClass.requestFocus();
            return;
        }
        if(iClass.isFocused()) {
            iClassII.requestFocus();
            return;
        }
        if(iClassII.isFocused()) {
            iClassIII.requestFocus();
            return;
        }
        if(iClassIII.isFocused()) {
            iClassIIII.requestFocus();
            return;
        }
        if(iClassIIII.isFocused()) {
            iTicketPrice.requestFocus();
            iUnitType.setText(iClassIIII.getText().trim());
            return;
        }
        if(iTicketPrice.isFocused()) {
            if (priceChangeCheckBox.isSelected()) {
                iNumberOfUnits.setText("0");
                iTotal.setText(String.valueOf(CalcTotal()));
                newOrderButton.setDisable(true);
                reOrderButton.setDisable(false);
                reOrderButton.fire();
            } else {
                iPurchaseDate.requestFocus();
            }
            return;
        }
        if(iPurchaseDate.isFocused()) {
            iInvoiceNumber.requestFocus();
            return;
        }
        if(iInvoiceNumber.isFocused()) {
            iNumberOfUnits.requestFocus();
            return;
        }
        if (iNumberOfUnits.isFocused()) {
            if (inventoryCountCheckBox.isSelected()) {
                iTotal.setText(String.valueOf(CalcTotal()));
                newOrderButton.setDisable(true);
                reOrderButton.setDisable(false);
                reOrderButton.fire();
            } else {
                iUnitType.requestFocus();
            }
            if (metersCheckBox.isSelected()) {
                iTotal.setText(String.valueOf(CalcTotal()));
                newOrderButton.setDisable(true);
                reOrderButton.setDisable(false);
                reOrderButton.fire();
            } else {
                iUnitType.requestFocus();
            }
            return;
            
        }
        if(iUnitType.isFocused()) {
            iPricePerUnit.requestFocus();
            iClassIIII.setText(iUnitType.getText().trim());
            return;
        }
        if (iPricePerUnit.isFocused()) {
            prtLabel.requestFocus();
            try {
                iTotal.setText(String.valueOf(CalcTotal()));
            } catch(Exception e) {
                mBox.showAlert(Alert.AlertType.ERROR, owner, "Wait", e.toString());
            }
            return;
        }
        if(prtLabel.isFocused()) {
            //HERE IS WHERE WE NEED A VALIDITY CHECK ON ALL THE FIELDS - - - - - - - - - - - -
            if (orderType.equals("NEW")) {
                newOrderButton.setDisable(false);
                newOrderButton.requestFocus();
                reOrderButton.setDisable(true);
            } else {
                reOrderButton.setDisable(false);
                reOrderButton.requestFocus();
                newOrderButton.setDisable(true);
            }
            return;
        }

        if (newOrderButton.isFocused()) {
            newOrderButton.fire();
        }
        if (reOrderButton.isFocused()) {
            reOrderButton.fire();
        }

    }
    
    private double CalcTotal() {
        
        //private void checkFirstRecord(String CCN) {
        //if (CCN.equals("100001")) {
        //    throw new IllegalArgumentException("\n This account cannot be edited");
        //}
    //}
        
        
        
        
        double total = 0.00;
        total = Double.parseDouble(iNumberOfUnits.getText()) * Double.parseDouble(iPricePerUnit.getText());
        try {
            df.format(total);             
        } catch(NumberFormatException e) {
             throw new IllegalArgumentException("\n The numbers entered cannot be multiplied \n Please check your numbers.");
        }
        return total;
    }
    
    private void ClearFields() {
        //iItemNumber.clear();
        //iItemName.clear();
        iVendor.clear();
        //iDepartment.clear();
        iClass.clear();
        iClassII.clear();
        iClassIII.clear();
        iClassIIII.clear();
        iTicketPrice.clear();
        iNumberOfUnits.clear();
        iPricePerUnit.clear();
        iUnitType.clear();
        orderType = "NONE";
        newOrderButton.setDisable(true);
        reOrderButton.setDisable(true);
       // mBox.showErrorClear(errorLabel, errorPane);


    }

    private Stage getStageV() {
        stageV = (Stage) iItemNumber.getScene().getWindow();
        return stageV;
    }

    private boolean isEMPValidInArrayList(String n) {
        boolean empValid = false;
        //for (empFX E1 : E) {
        for (int y = 0; y < E.size(); y++) {

            //System.out.println("E SIze " + E.size() + " " + y + " " + E.get(y).getEmpNumber());
            if (n.equals(E.get(y).getEmpNumber())) {
                //System.out.println(E.get(y).getEmpNumber());
                empValid = true;
                en = E.get(y).getEmpNumber();
                fn = E.get(y).getNameF();
                ln = E.get(y).getNameL();
            }
        }
        return empValid;
    }

    public boolean loginButtonPushed() throws IOException {
        Boolean GO = false;
        GO = isEMPValidInArrayList(empNumber.getText().trim());
        return GO;
    }

    private void setKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F1) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F3) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F4) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F6) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F7) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.TAB) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.UP) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ESCAPE) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.ENTER) {
                keyListener(ke);
                ke.consume();
            }
        });
    }

    private void setEmp() { //set employee number in the empTextfield
        //empTextfield.setText(eFXX.empNumber);
        //empTextfield.setDisable(true);
        //empTextfield.setStyle("-fx-background-color: #7e7e7e;");
        //JOptionPane.showMessageDialog(null, eFX.empNumber);
        en = eFX.empNumber;
        fn = eFX.nameF;
        ln = eFX.nameL;
        //vAmt = eFXX.VAmt;
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                reOrderButton.fire();
                break;
            case F2:
                break;
            case F3:
                searchButton.fire();
                break;
            case F4:
                break;
            case F5:
                break;
            case F6:
                break;
            case F7:
                newOrderButton.fire();
                break;
            case F8:
                break;
            case F9:
                break;
            case F10:
                break;
            case F11:
                break;
            case TAB:
                enterKeyPressed();
                break;
            case DOWN:
                //exitButton.fire();
                break;
            case UP:
                break;
            case ESCAPE:
                exitButtonPushed();
                break;
            case ENTER:
                enterKeyPressed();
                break;
            default:
                break;
        }
    }
    
    
    public void SearchButtonPressed(ActionEvent event) {
        SetAction(event);
        try {
            if (iVendor.isFocused()) {
                getDropBox(event, iVendor, "iVendor");
                return;
            }
            if (iDepartment.isFocused()) {
                getDropBox(event, iDepartment, "iDepartment");
                return;
            }
            if (iClass.isFocused()) {
                getDropBox(event, iClass, "iClass");
                return;
            }
            if (iClassII.isFocused()) {
                getDropBox(event, iClassII, "iClassII");
                return;
            }
            if (iClassIII.isFocused()) {
                getDropBox(event, iClassIII, "iClassIII");
                return;
            }
            if (iClassIIII.isFocused()) {
                getDropBox(event, iClassIIII, "iClassIIII");
                return;
            }
            /*if (iInvoiceNumber.isFocused()) {
                TDB.getLastInvoiceNumbers();
                getDropBox(event, iInvoiceNumber, "iInvoiceNumber");
                return;
            }*/
            if (iItemNumber.isFocused()) {
                sc.changePopUp(event, "/views/toys/ToysSearchTableView.fxml", "List of Prizes");
                iItemNumber.requestFocus();

                if (!sc.getGameNumber().trim().equals("Number")) {
                    GetToy(sc.getGameNumber());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    
    private void getDropBox(ActionEvent event, TextField tx, String txtFile) {
        try {
            boundsInScene = tx.localToScene(tx.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", txtFile, tx.getId(), tx.getText(), boundsInScene.getMinX()+50, boundsInScene.getMinY()+50.0);
            if (!sc.getGameNumber().equals(txtFile)) {
                tx.setText(sc.getGameNumber());
                tx.requestFocus();
            }
        } catch (IOException ex) {
            new Mail_JavaFX1().sendEmailTo("SelectPopUp Error 2", "SelectPopFX error. " + ex.toString(), "errors");            
            System.out.println(ex);
        }

    }
    
    
        
    
    
    public void SetAction(ActionEvent ae) {
        AE = ae;
        System.out.println("setting Action" + GetAction());
    }

    public ActionEvent GetAction() {
        return AE;
    }

    public void handle(MouseEvent t) {
        if (GetAction() == null) {
            mBox.showAlert(Alert.AlertType.ERROR, owner, ln, "Please click on the 'Click Here Button' one time to use this double click feature");
            return;
        }
        long diff = 0;
        long currentTime = System.currentTimeMillis();
        boolean isdblClicked = false;
        if (lastTime != 0 && currentTime != 0) {
            diff = currentTime - lastTime;

            if (diff <= 215) {
                isdblClicked = true;
                SearchButtonPressed(GetAction());

            } else {
                isdblClicked = false;
            }
        }
        lastTime = currentTime;
    }

    private void SetChangeListener() {
        iItemName.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iItemName, oldValue, newValue);
        });
        iVendor.textProperty().addListener((observable, oldValue, newValue) -> {
            SetChangeListenerDo(iVendor, oldValue, newValue);
        });
        iDepartment.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iDepartment, oldValue, newValue);
        });
        iClass.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iClass, oldValue, newValue);
        });
        iClassII.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iClassII, oldValue, newValue);
        });
        iClassIII.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iClassIII, oldValue, newValue);
        });
        iClassIIII.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iClassIIII, oldValue, newValue);
        });
        iPricePerUnit.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iPricePerUnit, oldValue, newValue);
        });
        iTicketPrice.textProperty().addListener((ov, oldValue, newValue) -> {
            SetChangeListenerDo(iTicketPrice, oldValue, newValue);
        });
    }

    private void SetChangeListenerDo(TextField TF, String OV, String NV) {
        if (TF.isFocused()) {
            if (OV.equals(NV)) {
                SetChangeWasMade(false);
            } else {
                SetChangeWasMade(true);
            } //END ELSE
        } //ISFOCUSED 
    }

    public void SetChangeWasMade(boolean t) {
        this.changeWasMade = t;
    }

    public boolean GetChangeWasMade() {
        return changeWasMade;
    }

    public void reportButtonPushed() {
        String invoiceNumber = "";
        if (iInvoiceNumber.getText().chars().count() > 0 && iDepartment.getText().chars().count() > 0) {
            if (iInvoiceNumber.getText().substring(0, 1).equals("#")) {
                invoiceNumber = iInvoiceNumber.getText().substring(1, iInvoiceNumber.getText().length()).trim() + "*";
                System.out.println("--=-=-=-=-=-=-=-=-=-=-=- " + invoiceNumber);
            } else {
                invoiceNumber = iInvoiceNumber.getText();
            }
            ArrayList<ToysDetail> pHistory = new ToysDB().getInventoryPriceChange(invoiceNumber, iDepartment.getText().trim());
            XLSInventoryReport report = new XLSInventoryReport();
            try {
                report.InventoryReport(pHistory);
                //for (int i = 0; i < pHistory.size(); i++) {
                //    System.out.println(pHistory.get(i).getName());
                // }
            } catch (IOException | SQLException ex) {
                System.out.println(ex);
            }
        } else {
            mBox.showAlert(Alert.AlertType.ERROR, null, "Alert", "The field INVOICE NUMBER AND DEPARMENT cannot be blank.");
        }
    }

    public void invoiceReportButtonPushed() {
        String invoiceNumber = "";
        if (iInvoiceNumber.getText().chars().count() > 0) {
            invoiceNumber = iInvoiceNumber.getText();
            ArrayList<ToysDetail> pHistory = new ToysDB().getInvoiceReport(invoiceNumber);
            XLSInventoryReport report = new XLSInventoryReport();
            try {
                report.InventoryReport(pHistory);
                //for (int i = 0; i < pHistory.size(); i++) {
                //    System.out.println(pHistory.get(i).getName());
                // }
            } catch (IOException | SQLException ex) {
                System.out.println(ex);
            }
        } else {
            mBox.showAlert(Alert.AlertType.ERROR, null, "Alert", "The field INVOICE NUMBER cannot be blank.");
        }
    }

    public void NOTCountedreportButtonPushed() {
        if (iInvoiceNumber.getText().chars().count() > 0 && iDepartment.getText().chars().count() > 0) {
            try {
                String invoiceNumber = iInvoiceNumber.getText();
                XLSNotCountedReport report = new XLSNotCountedReport();
                ArrayList<ToysDetail> pHistory = new ToysDB().getInventoryPriceChange(invoiceNumber, iDepartment.getText().trim());
                ArrayList<Toys> NotCounted = new ToysDB().GetAllToysForNotCounted(iDepartment.getText().trim());

                for (int i = 0; i < pHistory.size(); i++) {
                    for (int d = 0; d < NotCounted.size(); d++) {
                        if (pHistory.get(i).getItemID().equals(NotCounted.get(d).getItemID())) {
                            System.out.println(pHistory.get(i).getItemID() + " " + NotCounted.get(d).getItemID() + " " + NotCounted.get(d).getName());
                            NotCounted.remove(d);
                        }
                    }
                }
                report.InventoryReport(NotCounted);
            } catch (SQLException | IOException ex) {
                System.out.println(ex);
            }
        } else {
            mBox.showAlert(Alert.AlertType.ERROR, null, "Alert", "The field INVOICE NUMBER AND DEPARTMENT cannot be blank.");
        }
    }

    public void exitButtonPushed() {
        if (orderType.equals("NEW") || orderType.equals("RE")) {
            ClearFields();
            iItemNumber.clear();
            iItemNumber.requestFocus();
        } else {
            getStageV().close();
        }
    }

    public void gameReportButtonPushed() {
        String iGameNumber = "";
        if (iItemNumber.getText().chars().count() > 0) {
            iGameNumber = T.getItemID();
            System.out.println("=========================================== " + T.getItemID());

            ArrayList<ToysDetail> pGamePrizes = TDB.getGamePrizes(iItemNumber.getText().trim(), java.sql.Date.valueOf(iPurchaseDate.getValue()));
            ArrayList<ToysDetail> pCoinMeters = TDB.getGameCoinMeters(iGameNumber, java.sql.Date.valueOf(iPurchaseDate.getValue()));
            ArrayList<ToysDetail> pPrizeMeters = TDB.getGamePrizeMeters(iGameNumber, java.sql.Date.valueOf(iPurchaseDate.getValue()));

            XLSGameReport report = new XLSGameReport();
            try {
                report.gameReport(pGamePrizes, pCoinMeters, pPrizeMeters, iGameNumber, iItemName.getText().trim());
                //for (int i = 0; i < pHistory.size(); i++) {
                //    System.out.println(pHistory.get(i).getName());
                // }
            } catch (IOException | SQLException ex) {
                System.out.println(ex);
            }
        } else {
            mBox.showAlert(Alert.AlertType.ERROR, null, "Alert", "The field ITEM NUMBER AND PURCHASE DATE cannot be blank.");
        }
    }

    public void gameReportButtonALLPushed() {
        //String iGameNumber = "";
        //if (iPurchaseDate.getValue().>0) {
        //iGameNumber = T.getItemID();
        //System.out.println("=========================================== " + T.getItemID());

        
        File myObj = new File(dbsp1.pathNameToys + "XLSGameReportALL.xlsx");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
        
        
        
        
        ArrayList<Toys> gamesMerchandizers = TDB.getMerchandizers();
        
        XLSGameReportALL report = new XLSGameReportALL();
        try {
            System.out.println("hfhfhfhffhfhfhfhhhhhhhhhhhhhhhhhh55555555555555555555555555555 " + iPurchaseDate.getValue());
            report.startAllGameReport(gamesMerchandizers, iPurchaseDate.getValue());
            //for (int i = 0; i < pHistory.size(); i++) {
            //    System.out.println(pHistory.get(i).getName());
            // }
        } catch (IOException | SQLException ex) {
            System.out.println(ex);
        }
        gamesMerchandizers.clear();
        //} else {
        //    mBox.showAlert(Alert.AlertType.ERROR, null, "Alert", "The field PURCHASE DATE cannot be blank.");
        //}
    }





}
