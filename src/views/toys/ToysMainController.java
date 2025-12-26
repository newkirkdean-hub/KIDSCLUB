package views.toys;

import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import commoncodes.LabelDemo;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messageBox.messageBox;
import models.toys.Toys;
import models.toys.ToysDB;
import models.toys.ToysDetail;
import pWordFX.empFX;
import pWordFX.employeeFX;
import reports.toys.XLSCollectionSHeet1;
import sceneChangerFX.SceneChanger_Main;

public class ToysMainController implements Initializable {

    @FXML private Pane root;
    @FXML private TextField iItemName;
    @FXML private TextField iVendor;
    @FXML private TextField iItemNumber;
    //@FXML private TextField iPurchaseDate;
    @FXML private DatePicker iPurchaseDate;
    @FXML private TextField iDepartment;
    @FXML private TextField iClass;
    @FXML private TextField iClassII;
    @FXML private TextField iClassIII;
    @FXML private TextField iClassIIII;
    @FXML private TextField iPricePerUnit;
    @FXML private TextField iTicketPrice;
    @FXML private TextField iReqNotesField;
    @FXML private GridPane  gridPane1;
    @FXML private Button bMore;
    @FXML private Button bExit;
    @FXML private Button bWeights;
    @FXML private Button bLabelButton;
    @FXML private Button bClose;
    @FXML private Button bVendors;
    @FXML private Button bDetails;
    @FXML private Button bEdit;
    @FXML private Button bSearch;
    
    public static SceneChanger_Main sc;
    public static messageBox mBox; 
    public static FXMLLoader fxmlLoader;

    private static final java.text.SimpleDateFormat TDF = new java.text.SimpleDateFormat("hh:mm a");
    private static final java.text.SimpleDateFormat DDF = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String EMAIL_SUBJECT = "TOYSMAINCONTROLLER Error";
    private static final IsItANumber iin = new IsItANumber();
    private static Toys toys;
    private static ToysDB TDB = new ToysDB();
    public static Boolean editMode = false, newRec = false, runDimFile = false;
    ContextMenu contextMenu = new ContextMenu();
    private static Bounds boundsInScene;
    private static Bounds boundsInScenememMoreButton;
    private static Stage stageV;
    private static boolean changeWasMade = false;
    public static String tMessage = "", cssPath = null, MGR, detailTableID;
    public static ActionEvent AE = null;
    private static long lastTime = 0;
    //public static int whichOne = 0, beenhereonce = 0;
    //private static employeeFX EFX = null;
    //private static final NumberFormat $format = DecimalFormat.getCurrencyInstance();
    //private static TextField tName = null; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boundsInScene = root.localToScene(root.getBoundsInLocal());
        boundsInScenememMoreButton = bMore.localToScene(bMore.getBoundsInLocal());
        new dbStringPath().setName();
        BuildMenuButton();
        SetToUpper();
        SetKeyCodes();
        SetChangeListener();
        SetNumberListener();
        new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
        root.getStylesheets().add(cssPath); 
        Platform.runLater(() -> GetToy("000991"));
        //Platform.runLater(() -> bLabelButton.setDisable(false));
        //Platform.runLater(() -> IsLabel());
        Platform.runLater(() -> MakeAllNoEdit());
        Platform.runLater(() -> bSearch.requestFocus());
        Platform.runLater(() -> SetChangeWasMade(false));
        
    }    
    
    
    
    
    
    public void EditButtonGo() {
        try {
            CheckFirstRecord(toys.getItemID());
        } catch (Exception e) {
            Platform.runLater(() -> new LabelDemo().TextDisplayPopup("Stop", "<html><font size=5 color=blue>" + e.toString(), 4, 225, 125, 1));
            //bSearch.fire();
            return;
        }
        if (!editMode) {
            MakeAllEdit();
        } else {

            if (!SaveChanges()) {
                return;
            } else {
                MakeAllNoEdit();
                Refresh();
            }
        }
    }
    
    
    public void pLabel() {
        ToysDetail TDS = new ToysDetail(iItemName.getText().trim(),toys.getNumber(),toys.getItemID(),iClassIIII.getText().trim(),"","","",Integer.valueOf(iTicketPrice.getText().trim()),0,Double.valueOf(iPricePerUnit.getText().trim()),0.0,iPurchaseDate.getValue());
        System.out.println(TDS.getName());
        try {
            TDB.InsertLabel(TDS);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        TDS = null;
    }
    
    public void IsLabel() {
        try {
            if (TDB.IsLabel(toys.getItemID())) {
                bLabelButton.setDisable(true);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    

    private boolean SaveChanges() {
        boolean complete = false;
        toys = new Toys(toys.getNumber(), toys.getItemID(), iItemName.getText().trim(), iDepartment.getText().trim(), iClass.getText().trim(), iClassII.getText().trim(), iClassIII.getText().trim(), iClassIIII.getText().trim(), Double.parseDouble(iPricePerUnit.getText().trim()), iPurchaseDate.getValue(), iVendor.getText().trim(), Integer.valueOf(iTicketPrice.getText().trim()), Integer.valueOf(iReqNotesField.getText().trim()));
        try {
            TDB.updateToy(toys);
            complete = true;
        } catch (SQLException e) {
            new messageBox().showAlert(Alert.AlertType.ERROR, null, "Stop", "There was an error in the Update " + e.toString());
            new Mail_JavaFX1().sendEmailTo("Error in ToysDB()", "THere was an error updateing an Item in Toys " + e.toString(), "error");
        }
        return complete;
    }

    private void Refresh() {
        Platform.runLater(() -> GetToy(iItemNumber.getText()));
    }

    public void MakeAllEdit() {
        gridPane1.getChildren().stream().filter((node) -> (node instanceof TextField)).map((node) -> {
            ((TextField) node).setStyle("-fx-opacity: 1.0;");
            return node;
        }).forEachOrdered((node) -> {
            ((TextField) node).setDisable(false);
        });

        iItemNumber.setDisable(true);
        iItemNumber.setStyle("-fx-background-color: #C0C0C0");
        bClose.setDisable(true);
        bDetails.setDisable(true);
        bSearch.setDisable(false);
        //bPrint.setDisable(true);
        //bLabels.setDisable(true);
        bWeights.setDisable(true);
        bMore.setDisable(true);
        bEdit.setText("Save F9");
        editMode = true;
        iItemName.setStyle("-fx-background-color: Yellow");
        iItemName.requestFocus();
    }

    private void MakeAllNoEdit() {
        gridPane1.getChildren().stream().filter((node) -> (node instanceof TextField)).map((node) -> {
            ((TextField) node).setStyle("-fx-opacity: 1.0;");
            return node;
        }).forEachOrdered((node) -> {
            ((TextField) node).setDisable(true);
        });
        bClose.setDisable(false);
        bDetails.setDisable(false);
        bSearch.setDisable(false);
        //bPrint.setDisable(false);
        //bLabels.setDisable(false);
        bWeights.setDisable(false);
        bMore.setDisable(false);
        bEdit.setText("Edit F9");
        editMode = false;

    }

    private void SetKeyCodes() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent ke) -> {
            if (ke.getCode() == KeyCode.F1) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F2) {
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
            if (ke.getCode() == KeyCode.F5) {
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
            if (ke.getCode() == KeyCode.F8) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F9) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F10) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F11) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.F12) {
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
            if (ke.getCode() == KeyCode.DOWN) {
                keyListener(ke);
                ke.consume();
            }
        });
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                break;
            case F2:
                break;
            case F3:
                bDetails.fire();
                break;
            case F4:
                bWeights.fire();
                break;
            case F5:
                break;
            case F6:
                break;
            case F7:
                bSearch.fire();
                break;
            case F8:
                break;
            case F9:
                bEdit.fire();
                break;
            case F10:
                break;
            case F11:
                break;
            case F12:
                break;
            case ESCAPE:
                bExit.fire();
                break;
            case ENTER:
                EnterKeyPressed();
                break;
            case DOWN:
            /*System.out.println(beenhereonce);
                if (beenhereonce > 0) {
                    System.out.println(beenhereonce);
                    beenhereonce = 0;
                    return;
                } else {
                    beenhereonce = +1;
                    System.out.println(beenhereonce);
                }
                System.out.println(beenhereonce); */
                bSearch.fire();
                break;
            default:
                break;
        }
    }

    private void EnterKeyPressed() {
        //bExit.setDisable(true);
        //bExit.setText("Search F7");
        if (iItemName.isFocused()) {
            iVendor.requestFocus();
            return;
        }
        if (iVendor.isFocused()) {
            iPurchaseDate.requestFocus();
            return;
        }
        //if (iItemNumber.isFocused()) {iPurchaseDate.requestFocus(); return;}
        if (iPurchaseDate.isFocused()) {
            iDepartment.requestFocus();
            return;
        }
        if (iDepartment.isFocused()) {
            iClass.requestFocus();
            return;
        }
        if (iClass.isFocused()) {
            iClassII.requestFocus();
            return;
        }
        if (iClassII.isFocused()) {
            iClassIII.requestFocus();
            return;
        }
        if (iClassIII.isFocused()) {
            iClassIIII.requestFocus();
            return;
        }
        if (iClassIIII.isFocused()) {
            iPricePerUnit.requestFocus();
            return;
        }
        if (iPricePerUnit.isFocused()) {
            iTicketPrice.requestFocus();
            return;
        }
        if (iTicketPrice.isFocused()) {
            iItemName.requestFocus();
            return;
        }
        if (editMode) {
            iItemNumber.setDisable(true);
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

        
        
        
        if (editMode) {
            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, MGR, "You cannot search for another Prize while still editing this prize");
            return;            
        } else {
            sc.changePopUp(event, "/views/toys/ToysSearchTableView.fxml", "List of Prizes");
            iItemName.requestFocus();
            

            if (!sc.getGameNumber().trim().equals("Number")) {
                GetToy(sc.getGameNumber());
            }
        }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void OrderButtonPressed(ActionEvent event) throws IOException {
        PurchaseFormController wController = (PurchaseFormController) fxmlLoader.getController();
        wController.css = cssPath;
        wController.en = MGR;
        sc.getpassWord(stageV, "/views/toys/PurchaseForm.fxml", tMessage, tMessage, 220.0, 50.0);
        //sc.ChangeScene("PurchaseForm", stageV, "Order / Reorder", root, boundsInScene, -500.00, -375.00);
    }

    private void GetToy(String ID) {
        try {
            toys = TDB.getToy(ID);
            if (!toys.getName().trim().equals("ErrorFailed")) {
                iItemName.setText(toys.getName());
                iVendor.setText(toys.getNumber());
                iItemNumber.setText(toys.getNumber());
                iPurchaseDate.setValue(toys.getDate());
                iDepartment.setText(toys.getDpart());
                iVendor.setText(toys.getVendor());
                iClass.setText(toys.getClassI());
                iClassII.setText(toys.getClassII());
                iClassIII.setText(toys.getClassIII());
                iClassIIII.setText(toys.getClassIIII());
                iPricePerUnit.setText(toys.getPPU().toString());
                iTicketPrice.setText(String.valueOf(toys.getTickets()));
                iReqNotesField.setText(String.valueOf(toys.getTag()));
                TDB.disConnect();
            } else {
                TDB.disConnect();
                Platform.runLater(() -> new messageBox().showAlert(Alert.AlertType.ERROR, null, "No Such Number", "Item Number Not Found " + ID));
                //JOptionPane.showMessageDialog(null, "Card Number Not Found " + T.GetiNumber());
                return;
            }
        } catch (SQLException ex) {
            System.out.println("Error in TOYSMAINCONTROLLER (GetToy()): " + ex);
            //new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "Error in (GetToys()): " + ex, "error");
        }
        TDB.disConnect();
        //IsLabel();
    }

    private void CheckFirstRecord(String iNumber) {
        if (iNumber.equals("2")) {
            throw new IllegalArgumentException("This item cannot be edited");
        }
    }

    private void SetToUpper() {
        iItemName.textProperty().addListener((ov, oldValue, newValue) -> {
            iItemName.setText(newValue.toUpperCase());
        });
        iVendor.textProperty().addListener((ov, oldValue, newValue) -> {
            iVendor.setText(newValue.toUpperCase());
        });
        iItemNumber.textProperty().addListener((ov, oldValue, newValue) -> {
            iItemNumber.setText(newValue.toUpperCase());
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
    }

    private void SetNumberListener() {
        iTicketPrice.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbers(newValue);
                    } catch (Exception e) {
                        new messageBox().showAlert(Alert.AlertType.ERROR, null, "Wait", "This Field can only be numbers");
                        iTicketPrice.setText(oldValue);
                        iTicketPrice.requestFocus();
                    }
                });
        iPricePerUnit.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        iin.checkNumbersPeriod(newValue);
                    } catch (Exception e) {
                        new messageBox().showAlert(Alert.AlertType.ERROR, null, "Wait", "This Field can only be numbers");
                        iPricePerUnit.setText(oldValue);
                        iPricePerUnit.requestFocus();
                    }
                });
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
        if (editMode) {
            if (TF.isFocused()) {
                if (OV.equals(NV)) {
                    SetChangeWasMade(false);
                } else {
                    SetChangeWasMade(true);
                } //END ELSE
            } //EDITMODE 
        } //END ISFOCUSED
    }

    public void SetChangeWasMade(boolean t) {
        this.changeWasMade = t;
    }

    public boolean GetChangeWasMade() {
        return changeWasMade;
    }
    
    
    
    public void SetAction(ActionEvent ae) {
        AE = ae;
        System.out.println("setting Action" + GetAction());
    }
    
    public ActionEvent GetAction() {
        return AE;
    }
    
    
    public void handle(MouseEvent t) {
        if (GetAction()==null) {
            mBox.showAlert(Alert.AlertType.ERROR, null, null, "Please click on the 'F7 Search Button' one time to use this double click feature");
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
    
    
    
    

    public void BuildMenuButton() {
        // A SEPARATOR MENU ITEM IS HERE IN THE ADDMENUITEMS COMMAND BELOW
        MenuItem item911 = new MenuItem(" CLEAR MENU ");
        MenuItem newItemNumber = new MenuItem(" NEW ITEM NUMBER ");

        Menu popupMenu = new Menu(" Edit PopUps ");
        MenuItem p1 = new MenuItem(" Vendors ");
        MenuItem p2 = new MenuItem(" Departments ");
        MenuItem p3 = new MenuItem(" Class ");
        MenuItem p4 = new MenuItem(" ClassII ");
        MenuItem p5 = new MenuItem(" ClassIII ");
        MenuItem p6 = new MenuItem(" ClassIIII ");

        Menu popupMenu2 = new Menu(" Print ");
        MenuItem countList = new MenuItem(" Inventory Count List ");
        MenuItem labels = new MenuItem(" Print / View Labels ");


        popupMenu.getItems().addAll(p1, p2, p3, p4, p5, p6);
        popupMenu2.getItems().addAll(countList, labels);

        contextMenu.getItems().addAll(newItemNumber, popupMenu, popupMenu2, new SeparatorMenuItem(), item911);

        /*        item1.setOnAction((ActionEvent event) -> {
            historyViewerController wController = (historyViewerController) fxmlLoader.getController();
            wController.memID = staticMemberID ;
            wController.cName = m.getNameF() + " " + m.getNameL();
            wController.css = cssPath;
            sc.goToScene("historyViewer", stageV, eFX.getNameF(), null, boundsInScenememButton);
        });
         */
        p1.setOnAction((ActionEvent event) -> {
            whichdropedit(1);
        });
        p2.setOnAction((ActionEvent event) -> {
            whichdropedit(2);
        });
        p3.setOnAction((ActionEvent event) -> {
            whichdropedit(3);
        });
        p4.setOnAction((ActionEvent event) -> {
            whichdropedit(4);
        });
        p5.setOnAction((ActionEvent event) -> {
            whichdropedit(5);
        });
        p6.setOnAction((ActionEvent event) -> {
            whichdropedit(6);
        });
        newItemNumber.setOnAction((ActionEvent event) -> {
            NewItemNumber();
        });

        countList.setOnAction((ActionEvent event) -> {

            try {
                //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "HEY", "THIE IS WHERE THE COUNT LIST WILL RUN");
                XLSCollectionSHeet1 XRW = new XLSCollectionSHeet1();  
                XRW.collectionListReport();
            } catch (FileNotFoundException | SQLException ex) {
                System.out.println(ex);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        });

        labels.setOnAction((ActionEvent event) -> {

            try {
                //new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "HEY", "THIE IS WHERE THE COUNT LIST WILL RUN");
                //sc.changePopUp(GetAction(), "/views/ToysLabels.fxml", "List of Labels");
                sc.getpassWord(stageV, "/views/toys/ToysLabels.fxml", "Print Labels", null, boundsInScene.getMinX() + 250.0, boundsInScene.getMinY() + 90.00);
            } catch (IOException ex) {
                System.out.println(ex);
            }
            iItemName.requestFocus();
        });


        
        newItemNumber.setOnAction((ActionEvent event) -> {
            try {
                CheckFirstRecord(toys.getItemID());
            } catch (Exception e) {
                new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
                return;
            }
            try {
                sc.getpassWord(stageV, "/pWordFX/NewItemNumber.fxml", "Number", "Scan Item Number:", boundsInScene.getMinX() + 250.0, boundsInScene.getMinY() + 90.00);
                if (!sc.getGameNumber().equals("Number")) {
                    if (!TDB.isItemNumberUsed(sc.getGameNumber())) {
                        if (!TDB.updateItemNumber(toys.getItemID(), sc.getGameNumber())) {
                            new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "ERROR", "New Card Number was not Updated");
                        } else {
                            GetToy(sc.getGameNumber());
                            TDB.disConnect();
                        }
                    } else {
                        new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Invalid Card Number", "This Number already in use");
                        return;
                    }
                } else {
                    new messageBox().showAlert(Alert.AlertType.ERROR, stageV, "", "New Item Number Cancelled");
                    return;
                }

            } catch (SQLException | IOException ex) {
                new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Error", "Something went wrong trying to update a Item Number " + ex.toString());
                new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "Something went wrong trying to update a card Number " + ex.toString(), "error");
            }
            TDB.disConnect();
        });
        

        bMore.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(bMore, boundsInScenememMoreButton.getMinX(), boundsInScenememMoreButton.getMinY());
            }
        });
        bMore.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(bMore, event.getScreenX(), event.getScreenY());
        });
    }

    
    private void getDropBox(ActionEvent event, TextField tx, String txtFile) {
        try {
            Bounds boundsInScene1 = tx.localToScene(tx.getBoundsInLocal());
            sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", txtFile, tx.getId(), tx.getText(), boundsInScene1.getMinX()+50, boundsInScene1.getMinY()+50.0);
            if (!sc.getGameNumber().equals(txtFile)) {
                tx.setText(sc.getGameNumber());
                tx.requestFocus();
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }
    
    
    
    
    public void ShowDetail() throws IOException {
            DetailViewerController wController = (DetailViewerController) fxmlLoader.getController();
            wController.memID = toys.getItemID() ;
            wController.cName = toys.getName();
            wController.css = cssPath;
            //getpassWord(stageV, "/views/" + whichOne + ".fxml", "Number", "Enter Employee Number:", boundsInScene.getMaxX() + X, boundsInScene.getMaxY() + Y);
            sc.getpassWord(stageV, "/views/toys/DetailViewer.fxml", tMessage, tMessage, 250.0, 200.0);
            //sc.ChangeScene("DetailViewer", stageV, null, null, boundsInScene, -485.00, -165.00);
    }
    
    public void CloseItem() {
        try {
            CheckFirstRecord(toys.getItemID());
        } catch (Exception e) {
            Platform.runLater(() -> new LabelDemo().TextDisplayPopup("Stop", "<html><font size=5 color=blue>" + e.toString(), 4, 225, 125, 1));
            //new messageBox().showAlert(Alert.AlertType.ERROR, null, "Notice", e.toString());
            return;
        }
        int g = new messageBox().confirmMakeThisChange(Alert.AlertType.ERROR, null, "WAIT!", "Are you sure you want to procede?\n This is not the EXIT Button");
        System.out.println(g);
        if (g > 0) {
            try {
                if (!TDB.closeItem(toys.getItemID())) {
                } else {
                    GetToy(sc.getGameNumber());
                    TDB.disConnect();
                }
                SetChangeWasMade(true);
                TDB.disConnect();
            } catch (SQLException ex) {
                new messageBox().showAlert(Alert.AlertType.INFORMATION, stageV, "Something Went Wrong", "Here is the Error Code: " + ex);
                System.out.println(ex);
            }
        }
    }

    public void menuItemsButtonMouseOver() {
        contextMenu.show(bMore, boundsInScenememMoreButton.getMinX(), boundsInScenememMoreButton.getMinY());
    }

    public void whichdropedit(int g) {
        String fName = "";
        switch (g) {
            case 1:
                fName = dbStringPath.pathNameToys + "Vendors.txt";
                break;
            case 2:
                fName = dbStringPath.pathNameToys + "Department.txt";
                break;
            case 3:
                fName = dbStringPath.pathNameToys + "Class.txt";
                break;
            case 4:
                fName = dbStringPath.pathNameToys + "ClassII.txt";
                break;
            case 5:
                fName = dbStringPath.pathNameToys + "ClassIII.txt";
                break;
            case 6:
                fName = dbStringPath.pathNameToys + "ClassIIII.txt";
                break;

        }
        Desktop dsk = Desktop.getDesktop();
        try {
            dsk.open(new File(fName));
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    public void NewItemNumber() {
        new messageBox().confirmMakeThisChange(Alert.AlertType.ERROR, null, "stop", "stop");
    }

    private Stage getStageV() {
        stageV = (Stage) iItemName.getScene().getWindow();
        return stageV;
    }

    private void RunBuildFile() {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                System.out.println("here1");
                ArrayList<String> toyIDList = new ArrayList<>(); 
                try {
                    toyIDList = new ToysDB().getAllCloseItems();
                    for(int i = 0; i<toyIDList.size(); i++) {    
                    //System.out.println(toyIDList.get(i));
                    if (!new ToysDB().cleanDetail(toyIDList.get(i))) {
                        System.out.println("Error, Failed to Clean Detail");
                        new Mail_JavaFX1().sendEmailTo(EMAIL_SUBJECT, "error in Clean Detail (TOYS) \n Trying to CLOSE items in Toys the detail Cleanout Filed. " + toyIDList.get(i) + new dbStringPath().localMachine, "error");
                    } else {
                        
                    }                    
                }
                new ToysDB().CleanClosedToys();
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                new ToysDB().disConnect();
            }
        };
        thread1.start();

    }

    public void exit() {
        if (editMode) {
            //System.out.println("1 " + GetChangeWasMade());
            if (GetChangeWasMade()) {
                int y = new messageBox().confirmMakeThisChange(Alert.AlertType.ERROR, null, "WAIT!", "Do you want to save changes?");
                System.out.println(y);
                if (y == 1) {
                    bEdit.fire();
                } else {
                    MakeAllNoEdit();
                    Refresh();
                    //getStageV().close();
                }
            } else {
                MakeAllNoEdit();
                Refresh();
                //getStageV().close();
            }
        } else {
            getStageV().close();
            TDB.disConnect();
            if (GetChangeWasMade()) {
                Platform.runLater(() -> RunBuildFile());
            }
        }
    }




    
}
