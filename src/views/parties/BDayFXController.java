package views.parties;


import reports.parties.ReportsB;
import Css.cssChanger;
import JavaMail.Mail_JavaFX1;
import commoncodes.ClubFunctions;
import commoncodes.ClubFunctions1;
import commoncodes.FocusedTextFieldHighlight;
import commoncodes.IsItANumber;
import dbpathnames.GetDay;
import dbpathnames.dbStringPath;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import messageBox.messageBox;
import models.birthdays.BdayDB;
import commoncodes.BDayFormatPhone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import models.birthdays.BdayParties;
import pWordFX.employeeFX;
import sceneChangerFX.SceneChanger_Main;


public class BDayFXController implements Initializable {

    @FXML private AnchorPane root;
    @FXML private Pane contactFeildsPanel;
    @FXML private TextField bdayParties_11;
    @FXML private TextField bdayParties_22;
    @FXML private TextField contactFirstName;
    @FXML private TextField contactLastName;
    @FXML private TextField phone;
    @FXML private TextField address;
    @FXML private TextField city;
    @FXML private TextField state;
    @FXML private TextField zipCode;
    @FXML private TextField birthdayName;
    @FXML private TextField yearsOld;
    @FXML private TextField numberinParty;
    @FXML private TextField bdayParties_33;
    @FXML private TextField additionalGuest;
    @FXML private TextField introduced;
    @FXML private TextField reservationist;
    @FXML private TextField datereservationMade;
    @FXML private TextField bdayEmail;
    @FXML private TextField confirmed;
    @FXML private TextArea Notes;
    @FXML private Label BdayDate;
    @FXML private Label BdayDay;
    @FXML private Label nParties;
    @FXML private PasswordField empNumberField;
    @FXML private Pane tablePane;
    @FXML private Button BgoToDateButton;
    @FXML private Button BcancelledButton;    
    @FXML private Button BfindButton;
    @FXML private Button BnewInsertButton;
    @FXML private Button BexitButton;
    @FXML private Button BeditButton;
    @FXML private Pane errorBar;
    @FXML private Label errorLabel;
    @FXML private Button BmoveButton;
    @FXML private Button Bgogetoldbdays;
    @FXML private Button BpopUpButton;
    @FXML private Button BprevButton;
    @FXML private Button BnextButton;
    @FXML private Button BdropEditMenu;
    @FXML private Button BtodayButton;
    @FXML private Button BmenuItemButton;
    @FXML private MenuItem MdepositReportsMenu;
    @FXML private MenuItem MtodayMenuItem;
    @FXML private MenuItem MsevenDayMenuItem;
    @FXML private MenuItem MpratiesDropEditMenuItem;
    @FXML private MenuItem MpartyTimesMenuItem;
    @FXML private MenuItem MroomNumbersMenuItem;
    @FXML private MenuItem MtodayMenuItem1;
    @FXML private MenuItem MintroDropEditMenuItem;
    @FXML private Stage stageV;
    @FXML private TableView<BdayParties> partiesTable;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _timeColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _roomColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _fNameColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _lNameColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _phoneColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _numberColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _typeColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _bdayNameColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _addedColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _notesColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _IDColumn;
    @FXML private javafx.scene.control.TableColumn<BdayParties, String> _ConfirmedColumn;
    
    
    public static SceneChanger_Main sc;
    public static FXMLLoader fxloader;
    public static employeeFX eFXX;
    public static dbStringPath dbsp;
    public static messageBox mBox;
    public static Mail_JavaFX1 jmail;
    public static String cssPath;

    private static final GetDay GD = new GetDay();
    private static final BdayDB DB = new BdayDB();
    private static final ReportsB BDAYREPORTS = new ReportsB();
    private static SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyy");
    
    ContextMenu contextMenu = new ContextMenu();
    private static BdayParties bdp = null;
    private static Date nDate = null;
    private static TableColumn column1 = null;
    private static Bounds bboundsInScene, boundsInScene, boundsInScene2, bInS;
    private static int size = 0, cancelOK = 0;
    private static Date date_s = null;
    public static int bdayID, newBdayID = 0, newBdayID2 = 0, boundsPlusX, boundsPlusY;
    public static String dfMove = "", oldNotes = null;
    private static int diffInDays = 0, oldNotesCount = 0;
    private static Date findDate = null;
    private static boolean changeMade = false;
    private static int tableRowUpdate = 0;
    private static String bdayID2 = "0";
    private static int editMode = 0, i = 0, addMode = 0;
    private static String en, fn, ln;
    private static int vAmt, ii, r;
    private static ResultSet rr = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    dbsp.setName();
    boundsInScene = BmenuItemButton.localToScene(BmenuItemButton.getBoundsInLocal());
    boundsInScene2 = BgoToDateButton.localToScene(BgoToDateButton.getBoundsInLocal());
    bInS = Notes.localToScene(Notes.getBoundsInLocal());
    root.getStylesheets().add(cssPath);
    _timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
    _roomColumn.setCellValueFactory(new PropertyValueFactory<>("room"));
    _fNameColumn.setCellValueFactory(new PropertyValueFactory<>("Namef"));
    _lNameColumn.setCellValueFactory(new PropertyValueFactory<>("Namel"));
    _phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
    _numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
    _typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    _bdayNameColumn.setCellValueFactory(new PropertyValueFactory<>("Nameb"));
    _addedColumn.setCellValueFactory(new PropertyValueFactory<>("added"));
    _notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
    _ConfirmedColumn.setCellValueFactory(new PropertyValueFactory<>("Confirmed"));
    //_IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    setKeyCodes();   
    new FocusedTextFieldHighlight().setHighlightListenerBdays(root);
    setEmp();
    buildMenuButton();
    addTextfieldListeners();
    setTextFieldFocus();
    addZipfieldListeners();
    setToUpper();
    Platform.runLater(()->showError("Press: F9 To edit, \tF1 to find a day, \tF3 to find a party, \tENTER to move down the list of Parties (or DOWN ARROW)"));
    init(0);
    Platform.runLater(()->setCloseCatch());
    }    
 
    
    
    
    private void init(int whatDay) {
        DB.connect();
        showErrorClear();
        switch (whatDay) {
            case 0: // current day
                nDate = new Date(System.currentTimeMillis());
                tableRowUpdate = 0;
                break;
            case 1: // next day
                nDate = new Date(date_s.getTime() + 24 * 60 * 60 * 1000);
                tableRowUpdate = 0;
                break;
            case 2: // previous day
                nDate = new Date(date_s.getTime() - 24 * 60 * 60 * 1000);
                tableRowUpdate = 0;
                break;
            case 3:  //this is where after updating  a bday party it goes back to this party
                nDate = new Date(date_s.getTime()); 
                break;
            case 4: // this is after using the GoTo Date popup box.
                int newdiffInDays = 0;
                if (diffInDays<0) {
                    newdiffInDays = diffInDays * 24; //+ 24
                } else {
                    newdiffInDays = diffInDays * 24 + 24;
                }
                long newtime = newdiffInDays * 60;
                long newtime2 = newdiffInDays * 60 * 1000;
                tableRowUpdate = 0;
                nDate = new Date(date_s.getTime() + newtime * 60 + newtime2 * 60);
                break;
            case 5: //this is where after searching for a bday party it goes to this date
                if (newBdayID == 0) {
                    init(0);
                    break;
                } else {
                nDate = DB.getNdate(newBdayID);

                tableRowUpdate = 0;
                break;
                }
        }
        date_s = nDate;
        Date date = date_s;        
        
        try {
            rr = null;
            //System.out.println(dt1.format(date));
            rr = DB.get(GD.getSQLDateFromString(dt1.format(date)));
            BdayDate.setText(String.valueOf(dt1.format(rr.getDate("Date"))));
            BdayDay.setText(GD.LongDayWeek(rr.getString("day")));
            bdayID = rr.getInt("ID");            
            datereservationMade.setText(dt1.format(date));
            reservationist.setText(eFXX.nameF);
        } catch (Exception ex) {
            mBox.showAlert(Alert.AlertType.INFORMATION, stageV, "No Date", "The date you have searched for does not exist, Please add more days to the Reservation Calendar. The Current Last Day of the Calendar is: " + DB.getLastDate());
            System.out.println("Here Here Here  " + ex.toString());
            //new Mail_JavaFX1().sendEmailTo("Birthday Error", "Error 1 in the BdayFXCOntroller " + ex.toString(), "errors");
        }
        partiesTable.getItems().clear();
        try {
            //partiesTable.getItems().addAll(DB.getParties(bdayID));
            partiesTable.setItems(DB.getParties(bdayID));

        } catch (SQLException ex) {
            jmail.sendEmailTo("Birthday Error", "Error 2 in the BdayFXCOntroller " + ex.toString(), "errors");
            //System.out.println(ex);
        }

        size = DB.getSize(bdayID);

        nParties.setText(String.valueOf(size) + " Parties");
    resetFields();
    makeAllNoEdit();
    setFieldText();
    setMenuBar();
    oldNotes = "";
    oldNotesCount = 0;

    if (whatDay != 3) {
        if (size > 0) {
            Platform.runLater(() -> {
                partiesTable.requestFocus();
                partiesTable.getSelectionModel().select(0);
                getTableRow();
                partiesTable.getFocusModel().focus(0);
            });
        }
    } else {
    Platform.runLater(() -> {getTableIndexID();});
    }
    DB.disConnect();
    }

   
    private void getTableIndexID() {
        for (i = 0; i < size; i++) {
            if (partiesTable.getItems().get(i).getIDD() == Integer.parseInt(bdayID2)) {
                //System.out.println("here in tableindex " + i);
                partiesTable.requestFocus();
                partiesTable.getSelectionModel().select(i);
                getTableRow();
                partiesTable.getFocusModel().focus(i);
                bdayID2 = "";   
            } else {
                partiesTable.requestFocus();
                partiesTable.getSelectionModel().select(0);
                getTableRow();
                partiesTable.getFocusModel().focus(0);
            }
        }
        partiesTable.requestFocus();
    }

    public void cancelUpdateGO() {
        BeditButton.setText("Edit F9");
        partiesTable.setDisable(false);
        BnewInsertButton.setText("New (INSERT)");
        setFieldText();
        setEditMode(1);
        setToWhite();
        makeAllNoEdit();
        init(3);
    }
    
    public void updateGO() {
        if (getTableRowValue() != 0 ){
        if (BeditButton.getText().equals("Edit F9")) {
            BeditButton.setText("Update F9");
            BmoveButton.setDisable(true);
            showError("Press F9 to SAVE your edit or ESCAPE to CANCEL your Edit");
            setEditMode(0);
            setToWhite();
            bdayID2 = String.valueOf(getTableRowValue());
            partiesTable.setDisable(true);
            //===========8-24-2019
            if (eFXX.bdayresos == 2) {
                makeNotesEdit();
                Notes.requestFocus();
                Notes.setText(Notes.getText() + " ( " + fn + ", " + LocalDate.now() + " )");
                oldNotes = Notes.getText();
                oldNotesCount = Notes.getText().length();
                Notes.end();
                Notes.setText(oldNotes + ",");
                Notes.end();
            } else {
                makeAllEdit();
                contactFirstName.requestFocus();
            }
        } else {
            if (!testTextFields()) {
               return;
            }
            if (addMode == 1) {
                bdayID2 = "";
                addGO();    
            } else {
            if (!UpdateRecord(1)) {return;} 
                BeditButton.setText("Edit F9");
                partiesTable.setDisable(false);
                makeAllNotDisable();
                setFieldText();
                setEditMode(1);
                setToWhite();
                makeAllNoEdit();
                if (eFXX.bdayresos != 1){
                    jmail.sendEmailTo("BDayPartyUpdat", "The Birthday Reservation Program has been Updated. \n Party Contact: " + bdp.getNamef() + ", " + bdp.getNamel() + "\n On : " + BdayDay.getText() + ", " +  bdp.getToday() + "\n At : " + bdp.getTime()    + "\n Updated By : " + fn  + "\n The Notes Feild Before the Update: " + oldNotes  + "\n The New Notes: " + bdp.getNotes(), "errors" );
                }
                init(3);
            }
        }
        } else {
        mBox.showError("You Must select a Party before editing.", errorLabel, errorBar);
        }
    }
    
    
    
    private void setEditMode(int i) {
        switch (i) {
            case 0:
                //"THIS IS EDIT MORE"
        System.out.println("=========================1");
                editMode = 1;
                BprevButton.setDisable(true);
                BtodayButton.setDisable(true);
                BnextButton.setDisable(true);
                BgoToDateButton.setDisable(true);
                BfindButton.setDisable(true);
                BnewInsertButton.setVisible(true);
                BnewInsertButton.setDisable(false);
                BmenuItemButton.setDisable(true);
                BmoveButton.setVisible(true);
                BmoveButton.setDisable(true);
                BcancelledButton.setVisible(true);
                BcancelledButton.setDisable(false);
                break;

            case 1:
                //THIS IS UPDATE MODE;
        System.out.println("=========================2");
                editMode = 0;
                r = 0;
                addMode = 0;
                BprevButton.setDisable(false);
                BtodayButton.setDisable(false);
                BnextButton.setDisable(false);
                BgoToDateButton.setDisable(false);
                BfindButton.setDisable(false);
                BnewInsertButton.setVisible(false);
                BnewInsertButton.setDisable(true);
                BmenuItemButton.setDisable(false);
                BmoveButton.setVisible(true);
                BmoveButton.setDisable(false);
                BcancelledButton.setVisible(false);
                setMenuBar();
                break;
            case 3: //THIS IS FOR SETTING THE INSERT NEW RECORD
                editMode = 1;
                BprevButton.setDisable(true);
                BtodayButton.setDisable(true);
                BnextButton.setDisable(true);
                BgoToDateButton.setDisable(true);
                BfindButton.setDisable(true);
                BnewInsertButton.setVisible(true);
                BnewInsertButton.setDisable(false);
                BmenuItemButton.setDisable(true);
                BmoveButton.setVisible(true);
                BmoveButton.setDisable(true);
                addMode = 1;
                BnewInsertButton.setText("Save F9");
                BeditButton.setText("Edit F9");
                BeditButton.setDisable(true);
                makeAllEdit();
                resetFields();
                setFieldText();
                datereservationMade.setText(dt1.format(new Date()));
                reservationist.setText(eFXX.nameF);
                contactFirstName.requestFocus();
                bdayParties_11.requestFocus();
                confirmed.setText("NO");
                BcancelledButton.setVisible(false);
                //BpopUpButton.fire();
                break;
        }
        setMenuBar(); //8-24-2019
    }

    private boolean UpdateRecord(int w) {
        boolean bdpComplete = false;
        String q;
        contactLastName.requestFocus();
        contactFirstName.requestFocus();
        if (en.isEmpty()) {
            en = "343217";
            jmail.sendEmailTo("Blank Employee Number", "THe employee Number in post or updating a Birthday was blank and we reverted to Tammys Number.", "errors");
        }
        //CheckForApost();
        new ClubFunctions().clearApostophies(contactFeildsPanel);
        //testTextFields();
        String theNewNotes = Notes.getText();
        theNewNotes = theNewNotes.replaceAll("'", "");
        theNewNotes = theNewNotes.toUpperCase();
        String timeData = bdayParties_11.getText();
        String roomData = bdayParties_22.getText();
        String partyNameData = bdayParties_33.getText();
        java.sql.Date dayDate = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date f = new java.sql.Date(dayDate.getTime());
        BdayParties trv = partiesTable.getSelectionModel().getSelectedItem();
        System.out.println();
        switch (w) {
            case 1: //update
            bdayID2 = String.valueOf(trv.getIDD());
            try {
                bdp = new BdayParties(timeData, contactFirstName.getText(), contactLastName.getText(), address.getText(), city.getText(), state.getText(), zipCode.getText(), phone.getText(), partyNameData, birthdayName.getText(), yearsOld.getText(), introduced.getText(), datereservationMade.getText(), reservationist.getText(), bdayEmail.getText(), confirmed.getText(), Notes.getText(), roomData, Integer.parseInt(numberinParty.getText()), Integer.parseInt(additionalGuest.getText()), trv.getID(), trv.getIDD());
            } catch (IllegalArgumentException e) {
                new messageBox().showError(e.toString(), errorLabel, errorBar);
            } 
            q = "UPDATE Bdayprty SET Bdayprty.Time = '" + timeData + "', Bdayprty.[Room #] = '" + roomData + "', Bdayprty.[Contact First] = '" + contactFirstName.getText() + "', Bdayprty.[Contact Last] = '" + contactLastName.getText() + "', Bdayprty.Phone = '" + phone.getText() + "', Bdayprty.Address = '" + address.getText() + "', Bdayprty.City = '" + city.getText() + "', Bdayprty.state = '" + state.getText() + "', Bdayprty.[zip Code] = '" + zipCode.getText() + "', Bdayprty.[Bday person] = '" + birthdayName.getText() + "', Bdayprty.age = '" + yearsOld.getText() + "', Bdayprty.[number in party] = '" + numberinParty.getText() + "', Bdayprty.[Party Type] = '" + partyNameData + "', Bdayprty.Introduced = '" + introduced.getText() + "', Bdayprty.Additional = '" + additionalGuest.getText() + "', Bdayprty.BdayEmail = '" + bdayEmail.getText() + "', Bdayprty.Confirmed = '" + confirmed.getText() + "', Bdayprty.Note = '" + theNewNotes + "' WHERE (((Bdayprty.[Bday ID2])=" + bdayID2 + "));";
            
            if (!DB.updaterecord(q)) {
                new Mail_JavaFX1().sendEmailTo("Bday Update Error", "the Birthday update failed. here is the SQL " + q, "errors");
            } else {
                bdpComplete = true;
            }
            break;
            case 2:
            try {
                //bdp = new BdayParties(timeData, contactFirstName.getText(), contactLastName.getText(), address.getText(), city.getText(), state.getText(), zipCode.getText(), phone.getText(), partyNameData, birthdayName.getText(), yearsOld.getText(), introduced.getText(), datereservationMade.getText(), reservationist.getText(), Notes.getText(), roomData, Integer.parseInt(numberinParty.getText()), Integer.parseInt(additionalGuest.getText()), bdayID, 0);
                bdp = new BdayParties(timeData, contactFirstName.getText(), contactLastName.getText(), address.getText(), city.getText(), state.getText(), zipCode.getText(), phone.getText(), partyNameData, birthdayName.getText(), yearsOld.getText(), introduced.getText(), datereservationMade.getText(), reservationist.getText(), bdayEmail.getText(), confirmed.getText(), theNewNotes, roomData, Integer.parseInt(numberinParty.getText()), Integer.parseInt(additionalGuest.getText()), bdayID, 0);
            } catch (IllegalArgumentException e) {
                mBox.showError(e.toString(), errorLabel, errorBar);
            } 
            q = "INSERT INTO Bdayprty ([Time], [Room #], [Contact First], [Contact Last], Phone, Address, City, State, [Zip Code], [Bday Person], Age, [Number in Party], [Party Type], Introduced, Additional, Today, [Time Now], [EMP Name], BdayEmail, Confirmed, [Note], [bday ID]) VALUES('" + bdp.getTime() + "', '" + bdp.getRoom() + "', '" + bdp.getNamef() + "', '" + bdp.getNamel() + "', '" + bdp.getPhone() + "', '" + bdp.getAddress() + "', '" + bdp.getCity() + "', '" + bdp.getState() + "', '" + bdp.getZip() + "', '" + bdp.getNameb() + "', '" + bdp.getAge() + "', '" + bdp.getNumber() + "', '" + bdp.getType() + "', '" + bdp.getInto() + "', '" + bdp.getAdded() + "', #" + bdp.getToday() + "#, '" + bdp.getTime() + "', '" + bdp.getEname() + "', '" + bdp.getBdayEmail() + "', '" + bdp.getConfirmed() + "', '" + bdp.getNotes() + "', " + bdp.getID() + ")";
            if (!DB.InsertNewParty(q)) {
                jmail.sendEmailTo("Bday Insert Error", "the Birthday Insert failed. here is the SQL " + q, "errors");
            } else {
                bdpComplete = true;
            }
            break;
            
            
        } // end of switch
        
        return bdpComplete;
    }

    public void addGO() {
        if (addMode == 0) {
           setEditMode(3);
          // r++;
        } else {
            //if (r == 0) {
            if (!testTextFields()) {
               return;
            }
            if (!UpdateRecord(2)) {
               jmail.sendEmailTo("Error in Bdays", "There was an error in inserting a new record. (See BdaFXController addGO()", "errors");
            } else {
               addMode = 0;
               editMode = 0;
               BnewInsertButton.setText("New (INSERT)");
               r = 0;
               partiesTable.setDisable(false);
               setEditMode(1);
               init(3);
           }
        //}    
        }
    }
    
    
    private boolean addNewRecord() {
        boolean addNewRecord = false;
        

        
        return addNewRecord;
    }
    
    private void addZipfieldListeners() {
        zipCode.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!zipCode.getText().isEmpty()) {
                    try {
                        new IsItANumber().checkNumbers(newValue);
                    } catch (Exception e) {
                        showError("Zip Code can only be Numbers");
                        zipCode.setText("");
                        zipCode.requestFocus();
                        return;
                    }
                    }
                }
        );
    }
    
    
    public void confirmedClicked(MouseEvent e) {
        System.out.println(e);
        try {
                if (e.getClickCount() == 2) {
                    if (BeditButton.getText().equals("Edit F9")) {
                        BeditButton.fire();
                    }
                    if (editMode > 0 ) {
                        confirmed.requestFocus();
                        BpopUpButton.fire();
                        e.consume();
                    }
                e.consume();
                }
        } catch(Exception ex) {
            
        }
    }
    
    private void addTextfieldListeners() {
        showErrorClear();
        Notes.textProperty().addListener(
                (observable, oldValue, newValue) -> {
            if (editMode == 1 && eFXX.bdayresos!=1) {
                if (newValue.length()<=oldNotesCount) {
                    //showError("You cannot delete any part of the original Notes.");
                    Notes.setText(oldValue);
                    //Notes.requestFocus();
                    Notes.end();
                }
                if (newValue.length() < oldValue.length()) {
                    //showError("You cannot delete any part of the original Notes.");
                    Notes.setText(oldValue);
                    //Notes.positionCaret(0);
                    //Notes.requestFocus();
                    Notes.end();
                }
            }
        });
    }


    private void makeNotesEdit() { // this was the makeAllNoEdit Code //8-24-2019
        contactFeildsPanel.getChildren().stream().map((node) -> {
            if (node instanceof TextField) {
            }
            return node;
        }).forEachOrdered((node) -> {
            if (node instanceof TextField) {
                ((TextField) node).setEditable(false);
                ((TextField) node).setDisable(true);
                ((TextField) node).setStyle("-fx-background-color:#d3d3d3;");
            }
        });
        makeGrayFields();
        Notes.setFocusTraversable(true);
        Notes.setEditable(true);
    }

    private void makeAllNotDisable() {
        contactFeildsPanel.getChildren().stream().map((node) -> {
            if (node instanceof TextField) {
            }
            return node;
        }).forEachOrdered((node) -> {
            if (node instanceof TextField) {
                ((TextField) node).setEditable(true);
                ((TextField) node).setDisable(false);
                ((TextField) node).setStyle("-fx-background-color:#FFFFFF;");
            }
        });
        makeGrayFields();
        Notes.setFocusTraversable(true);
        Notes.setEditable(true);
    }

    private void makeAllNoEdit() {
        contactFeildsPanel.getChildren().stream().map((node) -> {
            // If you're certain all the children ARE TextFields, cast the node now
            if (node instanceof TextField) {
                //((TextField) node).setText("");
            }
            return node;
        }).forEachOrdered((node) -> {
            if (node instanceof TextField) {
                ((TextField) node).setEditable(false);
            }
        });

        makeGrayFields();
        Notes.setEditable(false);
    }


    public void moverecord() {
        if (BmoveButton.getText().equals("Place F7")) {
            String q = "UPDATE Bdayprty SET Bdayprty.[bday ID] = '" + bdayID + "' WHERE (((Bdayprty.[Bday ID2])=" + dfMove + "));";
            DB.moveRecord(q);
            BmoveButton.setText("Move F7");
            BmoveButton.setDisable(true);
            dfMove = "";
            setFieldText();
            
            init(3);
        } else {
            BdayParties trv = partiesTable.getSelectionModel().getSelectedItem();
            dfMove = String.valueOf(trv.getIDD());
            bdayID2 = dfMove;
            BmoveButton.setText("Place F7");
            mBox.showError("Record " + dfMove + " is ready to be moved. Please go to date you want to move to.", errorLabel, errorBar);
        }


}    
    
    
    private void setFieldText() {
        Set<Node> nodes = root.lookupAll(".text-field");
        nodes.stream().map((node) -> {
            node.setStyle("-fx-text-fill: green; -fx-font-family: Tahoma; -fx-font-size: 16px;"); // NOI18N
            return node;
        }).map((node) -> {
            node.setStyle("-fx-foreground-color: Black");
            return node;
        }).map((node) -> {
            node.setStyle("-fx-background-color: White");
            return node;
        }).forEachOrdered((node) -> {
            node.setStyle("-fx-alignment: CENTER_LEFT");
        });

        contactFirstName.setText("First Name");
        contactLastName.setText("Last Name or Company Name");
        numberinParty.setText("# Guest");
        additionalGuest.setText("Added Guest");
        birthdayName.setText("Birthday Name");
        yearsOld.setText("Age");
        address.setText("Address");
        city.setText("BOISE");
        state.setText("ID");
        zipCode.setText("");
        introduced.setText("INTRODUCED");
        bdayParties_33.setText("Party Type?");
        bdayParties_11.setText("RM TIME");
        bdayParties_22.setText("RM #");
        phone.setText("Phone");
        datereservationMade.setText("");
        reservationist.setText("");
        bdayEmail.setText("EMAIL");
        confirmed.setText("");
        Notes.setText(". . .");

    }

    private void setToWhite() {

    }

    private void makeAllEdit() {
        contactFeildsPanel.getChildren().stream().map((node) -> {
            // If you're certain all the children ARE TextFields, cast the node now
            if (node instanceof TextField) {
                //((TextField) node).setText("");
            }
            return node;
        }).forEachOrdered((node) -> {
            if (node instanceof TextField) {
                ((TextField) node).setEditable(true);
                
            }
        });

        makeGrayFields();
        Notes.setEditable(true);
    }

    private void makeGrayFields() {
        reservationist.setStyle("-fx-background-color:#d3d3d3;");
        datereservationMade.setStyle("-fx-background-color:#d3d3d3;");
        reservationist.setDisable(true);
        datereservationMade.setDisable(true);
    }

    private void resetFields() {
        contactFeildsPanel.getChildren().stream().map((node) -> {
            // If you're certain all the children ARE TextFields, cast the node now
            if (node instanceof TextField) {
                ((TextField) node).setText("");
            }
            return node;
        });
        Notes.setText("");
    }

    public void whichdropedit(int g) {
        // the other three frames rewuire this function
        String fName = "";
        switch (g) {
            case 1:
                fName = dbStringPath.pathNameBday + "Times.txt";
                break;
            case 2:
                fName = dbStringPath.pathNameBday + "Parties.txt";
                break;
            case 3:
                fName = dbStringPath.pathNameBday + "Rooms.txt";
                break;
            case 4:
                fName = dbStringPath.pathNameBday + "Intro.txt";
                break;
            case 5:
                fName = dbStringPath.pathNameBday + "Confirmed.txt";
                break;

        }
        Desktop dsk = Desktop.getDesktop();
        try {
            dsk.open(new File(fName));
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

    public void NextDay() {
        init(1);
    }
    public void PrevDay() {
        init(2);
    }
    public void ToDay() {
        init(0);
    }
    
    public void gotoDate() {
        diffInDays = 0;
        try {
            sc.getpassWord(stageV, "/messageBox/getDateBox.fxml", "Number", ":", boundsInScene2.getMinX() + 200.00, boundsInScene2.getMinY());
        } catch (IOException ex) {
            System.out.println(ex);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String strdate = sc.getGameNumber();
        if (strdate.isEmpty()) {
            System.out.println(strdate + " date");
            init(0);
        } else {
            DateFormat format3 = new SimpleDateFormat("MM/dd/yyyy");
            Date date2;
            try {
                date2 = format3.parse(strdate);
                diffInDays = (int) ((date2.getTime() - date_s.getTime()) / (1000 * 60 * 60 * 24));
                init(4);
            } catch (ParseException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
    public void bCancelledButtonGo() {
        contactLastName.setText("CANCELLED" + " " + contactLastName.getText() );
        contactFirstName.setText("CANCELLED" + " " + contactFirstName.getText() );
        confirmed.setText("CANCELLED");
        BeditButton.fire();
        
    }

    private void buildMenuButton() {
        MenuItem today = new MenuItem(" Today ");
        MenuItem single = new MenuItem(" Single ");
        MenuItem add = new MenuItem(" Add Days ");
        MenuItem copy = new MenuItem(" Copy to History ");

        Menu dropEdits = new Menu(" Drop Edits ");
        MenuItem csItem1 = new MenuItem(" Parties ");
        MenuItem csItem2 = new MenuItem(" Introduced ");
        MenuItem csItem3 = new MenuItem(" Party Times ");
        MenuItem csItem4 = new MenuItem(" Room Number ");
        MenuItem csItem5 = new MenuItem(" Confirmed ");
        dropEdits.getItems().addAll(csItem1, csItem2, csItem3, csItem4, csItem5);

        today.setOnAction((ActionEvent event) -> {
            BDAYREPORTS.salesreport(date_s, 1);
        });
        single.setOnAction((ActionEvent event) -> {
            BdayParties trv = partiesTable.getSelectionModel().getSelectedItem();
            BDAYREPORTS.singlereport(date_s, String.valueOf(trv.getIDD()));
        });
        copy.setOnAction((ActionEvent event) -> {
            BdayParties trv = partiesTable.getSelectionModel().getSelectedItem();
            System.out.println(String.valueOf(trv.getIDD()));
            try {
                DB.CopyToHistoricBdayParty(trv.getIDD());
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        });

        add.setOnAction((ActionEvent event) -> {
            BDAYREPORTS.addDays();
            mBox.showAlert(Alert.AlertType.INFORMATION, stageV, "Birthday Calendar", "The new Last day of the Reservation Calendar is: " + DB.getLastDate());
        });
        csItem1.setOnAction((ActionEvent event) -> {
            whichdropedit(2);
        });
        csItem2.setOnAction((ActionEvent event) -> {
            whichdropedit(4);
        });
        csItem3.setOnAction((ActionEvent event) -> {
            whichdropedit(1);
        });
        csItem4.setOnAction((ActionEvent event) -> {
            whichdropedit(3);
        });
        csItem5.setOnAction((ActionEvent event) -> {
            whichdropedit(5);
        });

        contextMenu.getItems().addAll(today, single, dropEdits, add, copy);

        // When user right-click on Circle
        BmenuItemButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boundsInScene = BmenuItemButton.localToScene(BmenuItemButton.getBoundsInLocal());
                contextMenu.show(BmenuItemButton, boundsInScene.getMaxX() + 5.0, boundsInScene.getMaxY() - 42.0);
            }
        });

        BmenuItemButton.setOnContextMenuRequested((ContextMenuEvent event) -> {
            contextMenu.show(BmenuItemButton, event.getScreenX(), event.getScreenY());
        });
    }
    public void menuItemsButtonMouseOver() {
        boundsInScene = BmenuItemButton.localToScene(BmenuItemButton.getBoundsInLocal());
        contextMenu.show(BmenuItemButton, boundsInScene.getMaxX() + 5.0, boundsInScene.getMaxY() - 42.0);
    }

    
    private int getTableRowValue() {
        int tableRowValue = 0;
        BdayParties trv = partiesTable.getSelectionModel().getSelectedItem();
        tableRowValue = trv.getIDD();
        return tableRowValue;
    }
    
    
    
    public void getTableRow() {
        BdayParties trv = partiesTable.getSelectionModel().getSelectedItem();
        contactFirstName.setText(trv.getNamef());
        contactLastName.setText(trv.getNamel());
        numberinParty.setText(String.valueOf(trv.getNumber()));
        additionalGuest.setText(String.valueOf(trv.getAdded()));
        birthdayName.setText(trv.getNameb());
        yearsOld.setText(trv.getAge());
        address.setText(trv.getAddress());
        city.setText(trv.getCity());
        state.setText(trv.getState());
        zipCode.setText(trv.getZip());
        introduced.setText(trv.getInto());
        bdayParties_33.setText(trv.getType());
        bdayParties_11.setText(trv.getTime());
        bdayParties_22.setText(trv.getRoom());
        phone.setText(trv.getPhone());
        datereservationMade.setText(trv.getStringDateTime());
        reservationist.setText(trv.getEname());
        bdayEmail.setText(trv.getBdayEmail());
        confirmed.setText(trv.getConfirmed());
        Notes.setText(trv.getNotes());
        makeGrayFields();
        setMenuBar();
        makeAllNoEdit();
        //BeditButton.setDisable(false);
        //editMode = true;
    }

    private void setMenuBar() {
        if (size > 0) {
            BnewInsertButton.setVisible(true);
            BmoveButton.setVisible(true);
            BpopUpButton.setVisible(false);
            //BcancelledButton.setVisible(false);

            if (eFXX.bdayresos == 3) { //this is fot he employee to view bdays only
        System.out.println("=========================4");
                BeditButton.setDisable(true);
                BnewInsertButton.setDisable(true);
                BmoveButton.setDisable(true);
                BcancelledButton.setVisible(false);
                //BcancelledButton.setDisable(true);
                //BdropEditMenu.setDisable(true);
                BmenuItemButton.setDisable(true);
            }
            if (eFXX.bdayresos == 2) { // this is like for employees on the floor that can update a bday.
        System.out.println("=========================5");
                BeditButton.setDisable(false); //8/24/2019
                BnewInsertButton.setDisable(true);
                BmoveButton.setDisable(true);
                //BdropEditMenu.setDisable(true);
                BmenuItemButton.setDisable(true);
            }
            if (eFXX.bdayresos == 1) { // this is for the people upstairs who can do all the editing.
        System.out.println("=========================6");
                BeditButton.setDisable(false);
                BnewInsertButton.setDisable(false);
                BmoveButton.setDisable(false);
                BmenuItemButton.setDisable(false);
                if (dfMove.equals("")) {
                    BmoveButton.setDisable(false);
                } else {
                    BmoveButton.setDisable(false);
                    BmoveButton.setText("Place F7");
                }
            }
        } else {
            BnewInsertButton.setVisible(true);
            BmoveButton.setVisible(true);
            BpopUpButton.setVisible(false);
            BcancelledButton.setVisible(false);
            //this is if there are no records in the table
            if (eFXX.bdayresos == 3) { //this is fot he employee to view bdays only
                BeditButton.setDisable(true);
                BnewInsertButton.setDisable(true);
                BmoveButton.setDisable(true);
                //BdropEditMenu.setDisable(true);
                BmenuItemButton.setDisable(true);
            }
            if (eFXX.bdayresos == 2) { // this is like for employees on the floor that can update a bday.
                BeditButton.setDisable(true);
                BnewInsertButton.setDisable(true);
                BmoveButton.setDisable(true);
                //BdropEditMenu.setDisable(true);
                BmenuItemButton.setDisable(true);
            }
            if (eFXX.bdayresos == 1) { // this is for the people upstairs who can do all the editing.
                BeditButton.setDisable(true);
                BnewInsertButton.setDisable(false);
                //BdropEditMenu.setDisable(false);
                BmenuItemButton.setDisable(false);
                if (dfMove.equals("")) {
                    BmoveButton.setDisable(true);
                } else {
                    BmoveButton.setVisible(true);
                    BmoveButton.setDisable(false);
                    BmoveButton.setText("Place F7");
                }
            }

        }
    }

    private void getStageV() {
        stageV = (Stage) BexitButton.getScene().getWindow();
    }

    private void setCloseCatch() {
        getStageV();
        stageV.setOnCloseRequest((WindowEvent we) -> {
            //System.out.println("Closing");
            exitButtonPushed();
            /*eFXX.Flush();
            Stage stage = (Stage) BexitButton.getScene().getWindow();
            stage.close();
            System.exit(1);*/
        });
    }

    

    private void setTextFieldFocus() {
        showErrorClear();
        bdayParties_11.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (editMode == 1) {
                    if (bdayParties_11.getText().equals("RM TIME")) {
                        bdayParties_11.clear();
                        BpopUpButton.setVisible(true);
                        BpopUpButton.fire();
                    } else {
                        BpopUpButton.setVisible(true);
                        showError("Press the DOWN ARROW Key for the Selection List");
                    }
                }
            } else {
                BpopUpButton.setVisible(false);
            }
        });
        bdayParties_22.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (editMode == 1) {
                    if (bdayParties_22.getText().equals("RM #")) {
                        bdayParties_22.clear();
                        BpopUpButton.setVisible(true);
                        BpopUpButton.fire();
                    } else {
                        BpopUpButton.setVisible(true);
                        showError("Press the DOWN ARROW Key for the Selection List");
                    }
                }
            } else {
                BpopUpButton.setVisible(false);
            }
        });
        bdayParties_33.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (editMode == 1) {
                    if (bdayParties_33.getText().equals("PARTY TYPE?")) {
                        bdayParties_33.clear();
                        BpopUpButton.setVisible(true);
                        BpopUpButton.fire();
                    } else {
                        BpopUpButton.setVisible(true);
                        showError("Press the DOWN ARROW Key for the Selection List");
                    }
                }
            } else {
                BpopUpButton.setVisible(false);
            }
        });
        introduced.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (editMode == 1) {
                    if (introduced.getText().equals("INTRODUCED")) {
                        introduced.clear();
                        BpopUpButton.setVisible(true);
                        BpopUpButton.fire();
                    } else {
                        BpopUpButton.setVisible(true);
                        showError("Press the DOWN ARROW Key for the Selection List");
                    }
                }
            } else {
                BpopUpButton.setVisible(false);
            }
        });
        Notes.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (editMode == 1) {
                    if (Notes.getText().equals(". . .")) {
                        Notes.clear();
                        BpopUpButton.setVisible(true);
                        BpopUpButton.fire();
                    } else {
                        BpopUpButton.setVisible(true);
                        showError("Press the DOWN ARROW Key for the Selection List");
                    }
                }
            } else {
                BpopUpButton.setVisible(false);
            }
        });
       /* confirmed.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (editMode > 0) {
                    if (confirmed.getText().equals("NO")) {
                        confirmed.clear();
                        BpopUpButton.setVisible(true);
                        BpopUpButton.fire();
                    } else {
                        BpopUpButton.setVisible(true);
                        showError("Press the DOWN ARROW Key for the Selection List");
                    }
                }
            } else {
                BpopUpButton.setVisible(false);
            }
        });*/

    }

    public void popUP1(ActionEvent event) throws IOException {
        if (bdayParties_11.isFocused()) {
            getPopUpScreen(event, 1);
        }
        if (bdayParties_22.isFocused()) {
            getPopUpScreen(event, 2);
        }

        if (bdayParties_33.isFocused()) {
            getPopUpScreen(event, 3);
        }
        if (introduced.isFocused()) {
            getPopUpScreen(event, 4);
        }
        if (confirmed.isFocused()) {
            getPopUpScreen(event, 6);
        }
        if (Notes.isFocused()) {
            showError("This ListBox can have multiple selections. Use SPACE bar to select each Item you want in the notes then Press ENTER");            
            getPopUpScreen(event, 5);
        }
    }

    private void getPopUpScreen(ActionEvent event, int x) throws IOException {
        switch (x) {
            case 1: // room number
                if (bdayParties_11.isFocused()) {
                    if (editMode == 1 || addMode == 1) {
                        bboundsInScene = bdayParties_11.localToScene(bdayParties_11.getBoundsInLocal());
                        sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "BTimes", "Select One:", bdayParties_11.getText(), bboundsInScene.getMaxX()+35.0 + boundsPlusX, bboundsInScene.getMaxY()+105.0 + boundsPlusY);
                        if (!sc.getGameNumber().equals("Number")) {
                            bdayParties_11.setText(sc.getGameNumber());
                            bdayParties_11.requestFocus();
                        } else {
                            bdayParties_11.setText("11:00:00 AM");
                            bdayParties_11.requestFocus();
                        }
                        return;
                    }
                }
                break;
            case 2: // partytype
                if (bdayParties_22.isFocused()) {
                    if (editMode == 1) {
                        bboundsInScene = bdayParties_22.localToScene(bdayParties_22.getBoundsInLocal());
                        sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "BRoom", "Select One:", bdayParties_22.getText(), bboundsInScene.getMaxX()+75.0 + boundsPlusX, bboundsInScene.getMaxY()+105.0 + boundsPlusY);
                        if (!sc.getGameNumber().equals("Number")) {
                            bdayParties_22.setText(sc.getGameNumber());
                            bdayParties_22.requestFocus();
                        } else {
                            bdayParties_22.setText("1");
                            bdayParties_22.requestFocus();
                        }
                    }
                }
                break;
            case 3: // times
                if (bdayParties_33.isFocused()) {
                    if (editMode == 1) {
                        bboundsInScene = bdayParties_33.localToScene(bdayParties_33.getBoundsInLocal());
                        sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "BParties", "Select One:", bdayParties_33.getText(), bboundsInScene.getMaxX()-95.00 + boundsPlusX, bboundsInScene.getMaxY()+105.0 + boundsPlusY);
                        if (!sc.getGameNumber().equals("Number")) {
                            bdayParties_33.setText(sc.getGameNumber());
                            bdayParties_33.requestFocus();
                        } else {
                            bdayParties_33.setText(".");
                            bdayParties_33.requestFocus();
                        }
                        return;
                    }
                }
                break;

            case 4: // intro
                if (introduced.isFocused()) {
                    if (editMode == 1) {
                        bboundsInScene = introduced.localToScene(introduced.getBoundsInLocal());
                        sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "BIntro", "Select One:", introduced.getText(), bboundsInScene.getMaxX()-25.00 + boundsPlusX, bboundsInScene.getMaxY()+105.0 + boundsPlusY);
                        if (!sc.getGameNumber().equals("Number")) {
                            introduced.setText(sc.getGameNumber());
                            introduced.requestFocus();
                        } else {
                            introduced.setText(".");
                            introduced.requestFocus();
                        }
                        return;
                    }
                }
                break;
            case 5: // intro
                if (Notes.isFocused()) {
                    if (editMode == 1) {
                        bboundsInScene = Notes.localToScene(Notes.getBoundsInLocal());
                        sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "BQPick",  "Select Multiple:", Notes.getText(), bboundsInScene.getMinX()+125.00 + boundsPlusX, bboundsInScene.getMinY()+165.0 + boundsPlusY);
                        if (!sc.getGameNumber().equals("Number")) {
                            Notes.setText(sc.getGameNumber());
                            Notes.requestFocus();
                            Notes.end();
                        } else {
                            Notes.setText(".");
                            Notes.requestFocus();
                            Notes.end();
                        }
                        return;
                    }
                }
                break;
            case 6: // intro
                if (confirmed.isFocused()) {
                    if (editMode == 1) {
                        bboundsInScene = confirmed.localToScene(confirmed.getBoundsInLocal());
                        sc.getPopUp(event, "/popUpFX/SelectPopUp.fxml", "BConfirmed", "Select One:", confirmed.getText(), bboundsInScene.getMaxX()-25.00 + boundsPlusX, bboundsInScene.getMaxY()+105.0 + boundsPlusY);
                        if (!sc.getGameNumber().equals("Number")) {
                            confirmed.setText(sc.getGameNumber());
                            confirmed.requestFocus();
                        } else {
                            confirmed.setText("NO");
                            confirmed.requestFocus();
                        }
                        return;
                    }
                }
                break;

        }

    }

    private void setToUpper() {
        //showErrorClear();

        contactFirstName.textProperty().addListener((ov, oldValue, newValue) -> {
            contactFirstName.setText(newValue.toUpperCase());
        });
        contactLastName.textProperty().addListener((ov, oldValue, newValue) -> {
            contactLastName.setText(newValue.toUpperCase());
        });
        address.textProperty().addListener((ov, oldValue, newValue) -> {
            address.setText(newValue.toUpperCase());
        });
        city.textProperty().addListener((ov, oldValue, newValue) -> {
            city.setText(newValue.toUpperCase());
        });
        state.textProperty().addListener((ov, oldValue, newValue) -> {
            state.setText(newValue.toUpperCase());
        });
        introduced.textProperty().addListener((ov, oldValue, newValue) -> {
            introduced.setText(newValue.toUpperCase());
        });
        Notes.textProperty().addListener((ov, oldValue, newValue) -> {
            Notes.setText(newValue.toUpperCase());
        });
        bdayParties_33.textProperty().addListener((ov, oldValue, newValue) -> {
            bdayParties_33.setText(newValue.toUpperCase());
        });
        bdayParties_11.textProperty().addListener((ov, oldValue, newValue) -> {
            bdayParties_11.setText(newValue.toUpperCase());
        });
        bdayParties_22.textProperty().addListener((ov, oldValue, newValue) -> {
            bdayParties_22.setText(newValue.toUpperCase());
        });
        bdayEmail.textProperty().addListener((ov, oldValue, newValue) -> {
            try {
            bdayEmail.setText(newValue.toUpperCase());
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        birthdayName.textProperty().addListener((ov, oldValue, newValue) -> {
            birthdayName.setText(newValue.toUpperCase());
        });
        zipCode.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (zipCode.getText().length() >= 5) {
                    zipCode.setText(zipCode.getText().substring(0, 5));
                }
            }
        });
        //numberinParty.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
        //    if (newValue.intValue() > oldValue.intValue()) {
        //        if (numberinParty.getText().length() >= 2) {
        //            numberinParty.setText(numberinParty.getText().substring(0, 2));
        //        }
        //    }
        //});
        //additionalGuest.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
        //    if (newValue.intValue() > oldValue.intValue()) {
        //        if (additionalGuest.getText().length() >= 2) {
        //            additionalGuest.setText(additionalGuest.getText().substring(0, 2));
        //        }
        //    }
        //});
        yearsOld.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (yearsOld.getText().length() >= 5) {
                    yearsOld.setText(yearsOld.getText().substring(0, 5));
                }
            }
        });
        
    }

    
    private void checkTableFocus() {
        if (partiesTable.isFocused()) {
            enterKeyPressed();
        } else {
            BpopUpButton.fire();
        }
    }
    
    public void LookUpButtonPressed(ActionEvent event) throws IOException {
        SearchTableBdayController wController = (SearchTableBdayController) fxloader.getController();
        wController.cssPath = cssPath;
        wController.sc = sc;
        wController.dbsp = dbsp;
        sc.changePopUp(event, "/views/parties/SearchTableBday.fxml", "List of Birthdays");
        String t = sc.getGameNumber();
        String[] r = t.split("/");
        //System.out.println("this is r1 " + r[0].trim() + " " + r[1].trim());
        try {
        newBdayID = Integer.parseInt(r[0].trim());
        newBdayID2 = Integer.parseInt(r[1].trim());
        } catch (NumberFormatException e) {
            System.out.println(e);
            return;
        }
        try {
            //HERE IS WHERE WE TRANSFER A PARTY BACK
            if (DB.LookInInactive(newBdayID2)) {
                //JOptionPane.showMessageDialog(null, "lookininactive was true" + newBdayID);
                //DB.MoveHPartyBack(newBdayID);
                //return;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        init(5);
    }

    private void CheckForApost() {
        contactFeildsPanel.getChildren().stream().map((node) -> {
            // If you're certain all the children ARE TextFields, cast the node now
            if (node instanceof TextField) {
                String str = ((TextField) node).getText();
                str = str.replaceAll("'", "`");
                str = str.toUpperCase();
                ((TextField) node).setText(str);
            }
            return node;
        });
    }
    
    
    
    private boolean testTextFields() {
        boolean testTextFieldVoid = true;
        ii=0;
        for (Node node : contactFeildsPanel.getChildren()) {
            if (node instanceof TextField) {
                if (ii<14) {
                    ii++;
                    if (!testValue((TextField) node, ii)) {
                        testTextFieldVoid = false;
                        System.out.println(node.getId() + " " + ii + " " + testTextFieldVoid);
                        return testTextFieldVoid;
                    }
                }
            }
        }
       //System.out.println(testTextFieldVoid);
        return testTextFieldVoid;
    }
    
    
    
    private boolean testValue(TextField t, int w) {
    System.out.println(t);
        boolean testedValue = false;
        switch (w) {
            case 1:
                if (t.getText().trim().isEmpty() || t.getText().trim().equals("RM TIME")) {
                    showError("Please select a valid Party Time");
                    t.requestFocus();
                    return testedValue;
                } else {
                    testedValue = true;
                }
                System.out.println(testedValue);
                break;
            case 2:
                if (t.getText().trim().isEmpty() || t.getText().trim().equals("RM #")) {
                    t.setText("1");
                    testedValue = true;
                } else {
                    testedValue = true;
                }
                System.out.println(testedValue);
                break;
            case 3:
                if (t.getText().trim().isEmpty() || t.getText().trim().equals("FIRST NAME")) {
                    showError("Please Enter a valid First Name");
                    t.requestFocus();
                    return testedValue;
                } else {
                   testedValue = true;
                }
                System.out.println(testedValue);
                break;
            case 4:
                if (t.getText().trim().isEmpty() || t.getText().trim().equals("LAST NAME OR COMPANY NAME")) {
                    showError("Please Enter a valid Last Name");
                    t.requestFocus();
                    return testedValue;
                } else {
                   testedValue = true;
                }
                break;
            case 5:
                if (t.getText().trim().isEmpty() || t.getText().trim().equals("Phone")) {
                    testedValue = false;
                    showError("Please Enter a valid Phone Number");
                    t.requestFocus();
                    return testedValue;
                } else {
                   testedValue = true;
                }
                break;
            case 6:
                   testedValue = true;
                break;
            case 7:
                   testedValue = true;
                break;
            case 8:
                   testedValue = true;
                break;
            case 9:
                   if (t.getText().trim().isEmpty() || t.getText().trim().equals("Zip")) {
                    testedValue = false;
                    showError("Please Enter a valid Zip Code");
                    t.requestFocus();
                    return testedValue;
                } else {
                   testedValue = true;
                }
                break;
            case 10:
                   testedValue = true;
                break;
            case 11:
                   testedValue = true;
                break;
            case 12:
                if (t.getText().trim().isEmpty() || t.getText().trim().equals("#")) {
                    testedValue = false;
                    showError("Please Enter a valid number of party guest");
                    t.requestFocus();
                    return testedValue;
                } else {
                   testedValue = true;
                }
                break;
            case 13:
                   testedValue = true;
                break;
            case 14:
                if (t.getText().trim().isEmpty() || t.getText().trim().equals("Ad")) {
                    t.setText("0");
                    testedValue = true;
                } else {
                   testedValue = true;
                }
                break;
        }
        
        return testedValue;
    }
    
    

    private void enterKeyPressed() {
        if (partiesTable.isFocused()) {
            size = partiesTable.getItems().size();
            final int cRow = partiesTable.getSelectionModel().getSelectedIndex();
            if (cRow + 1 == size) {
                partiesTable.getSelectionModel().select(0);
            } else {
                partiesTable.getSelectionModel().selectNext();
            }
            getTableRow();
        } else {
            if (editMode == 1) {
                showErrorClear();
                if (bdayParties_11.isFocused()) {
                    if (!testValue(bdayParties_11, 1)) {
                        return;
                    } else {
                    bdayParties_22.requestFocus();
                    }
                    return;
                }
                if (bdayParties_22.isFocused()) {
                    if (!testValue(bdayParties_22, 2)) {
                        return;
                    } else {
                    contactFirstName.requestFocus();
                    }
                    return;
                }
                if (contactFirstName.isFocused()) {
                    if (!testValue(contactFirstName, 3)) {
                        return;
                    } else {
                    contactLastName.requestFocus();
                    }
                    return;
                }
                if (contactLastName.isFocused()) {
                    if (!testValue(contactLastName, 4)) {
                        return;
                    } else {
                    phone.requestFocus();
                    }
                    return;
                }
                if (phone.isFocused()) {
                    if (!testValue(phone, 5)) {
                        return;
                    } else {
                    phone.setText(new BDayFormatPhone().formatPhone(phone.getText()));
                    address.requestFocus();
                    }
                    return;
                }
                if (address.isFocused()) {
                    city.requestFocus();
                    return;
                }
                if (city.isFocused()) {
                    state.requestFocus();
                    return;
                }
                if (state.isFocused()) {
                    zipCode.requestFocus();
                    return;
                }
                if (zipCode.isFocused()) {
                if (!new ClubFunctions().testPhone(4, zipCode.getText())) {
                    showError("This Field must have five (5) digits");
                    zipCode.requestFocus();
                    return;
                } else {
                    birthdayName.requestFocus();
                    return;
                }                    
                }
                if (birthdayName.isFocused()) {
                    yearsOld.requestFocus();
                    return;
                }
                if (yearsOld.isFocused()) {
                    numberinParty.requestFocus();
                    return;
                }
                if (numberinParty.isFocused()) {
                    if (!testValue(numberinParty, 12)) {
                        return;
                    } else {
                        bdayParties_33.requestFocus();
                    }
                    return;
                }
                if (bdayParties_33.isFocused()) {
                    additionalGuest.requestFocus();
                    return;
                }
                if (additionalGuest.isFocused()) {
                    if (!testValue(additionalGuest, 14)) {
                        return;
                    } else {
                    introduced.requestFocus();
                    }
                    return;
                }
                if (introduced.isFocused()) {
                    bdayEmail.requestFocus();
                    return;
                }
                if (bdayEmail.isFocused()) {
                    Notes.requestFocus();
                    return;
                }                
                if (Notes.isFocused()) {
                    bdayParties_11.requestFocus();
                    return;
                }
            }
        }
    }

    private void setKeyCodes() {
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
            if (ke.getCode() == KeyCode.TAB) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.INSERT) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.LEFT) {
                keyListener(ke);
                ke.consume();
            }
            if (ke.getCode() == KeyCode.RIGHT) {
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
            if (ke.getCode() == KeyCode.DELETE) {
                //keyListener(ke);
                ke.consume();
            }
        });
    }

    public void keyListener(KeyEvent event) {
        switch (event.getCode()) {
            case F1:
                BgoToDateButton.fire();
                break;
            case F2:
                BtodayButton.fire();
                break;
            case F3:
                BfindButton.fire();
                break;
            case F4:
                BprevButton.fire();
                break;
            case F5:
                BnextButton.fire();
                break;
            case F6:
                break;
            case F7:
                BmoveButton.fire();
                break;
            case F8:
                break;
            case F9:
                if (addMode == 1) {
                    BnewInsertButton.fire();
                } else {
                    BeditButton.fire();
                }
                break;
            case F10:
                break;
            case F11:
                break;
            case INSERT:
                BnewInsertButton.fire();
                break;
            case TAB:
                enterKeyPressed();
                break;
            case DOWN:
                checkTableFocus();
                //BpopUpButton.fire();
                break;
            case LEFT:
                goLeftArrow();
                break;
            case RIGHT:
                goRightArrow();
                break;
            case UP:
                if (!partiesTable.isDisabled()) {
                    partiesTable.getSelectionModel().selectPrevious();
                    getTableRow();
                }
                break;
            case ESCAPE:
                exitButtonPushed();
                break;
            case ENTER: ;
                enterKeyPressed();
                break;
            default:
                break;
        }
    }

    private void showErrorClear() {
        mBox.showErrorClear(errorLabel, errorBar);
    }

    private void showError(String error) {
        mBox.showError(error, errorLabel, errorBar);
        errorBar.setLayoutX(bInS.getMinX());
        errorBar.setLayoutY(bInS.getMinY());
    }

    private void goLeftArrow() {
        if (contactFirstName.isFocused()) {
            if (contactFirstName.getCaretPosition() == 0) {
                bdayParties_22.requestFocus();
                return;
            } else {
                contactFirstName.positionCaret(contactFirstName.getCaretPosition() - 1);
            }
        }
        if (contactLastName.isFocused()) {
            if (contactLastName.getCaretPosition() == 0) {
                contactFirstName.requestFocus();
                return;
            } else {
                contactLastName.positionCaret(contactLastName.getCaretPosition() - 1);
            }
        }
        if (phone.isFocused()) {
            if (phone.getCaretPosition() == 0) {
                contactLastName.requestFocus();
                return;
            } else {
                phone.positionCaret(phone.getCaretPosition() - 1);
            }
        }
        if (address.isFocused()) {
            if (address.getCaretPosition() == 0) {
                phone.requestFocus();
                return;
            } else {
                address.positionCaret(address.getCaretPosition() - 1);
            }
        }
        if (city.isFocused()) {
            if (city.getCaretPosition() == 0) {
                address.requestFocus();
                return;
            } else {
                city.positionCaret(city.getCaretPosition() - 1);
            }
        }
        if (state.isFocused()) {
            if (state.getCaretPosition() == 0) {
                city.requestFocus();
                return;
            } else {
                state.positionCaret(state.getCaretPosition() - 1);
            }
        }
        if (zipCode.isFocused()) {
            if (zipCode.getCaretPosition() == 0) {
                state.requestFocus();
                return;
            } else {
                zipCode.positionCaret(zipCode.getCaretPosition() - 1);
            }
        }
        if (birthdayName.isFocused()) {
            if (birthdayName.getCaretPosition() == 0) {
                zipCode.requestFocus();
                return;
            } else {
                birthdayName.positionCaret(birthdayName.getCaretPosition() - 1);
            }
        }
        if (yearsOld.isFocused()) {
            if (yearsOld.getCaretPosition() == 0) {
                birthdayName.requestFocus();
                return;
            } else {
                yearsOld.positionCaret(yearsOld.getCaretPosition() - 1);
            }
        }
        if (numberinParty.isFocused()) {
            if (numberinParty.getCaretPosition() == 0) {
                yearsOld.requestFocus();
                return;
            } else {
                numberinParty.positionCaret(numberinParty.getCaretPosition() - 1);
            }
        }
        if (additionalGuest.isFocused()) {
            if (additionalGuest.getCaretPosition() == 0) {
                bdayParties_33.requestFocus();
                return;
            } else {
                additionalGuest.positionCaret(additionalGuest.getCaretPosition() - 1);
            }
        }
        if (introduced.isFocused()) {
            if (introduced.getCaretPosition() == 0) {
                additionalGuest.requestFocus();
                return;
            } else {
                introduced.positionCaret(introduced.getCaretPosition() - 1);
            }
        }
        if (bdayParties_11.isFocused()) {
            if (bdayParties_11.getCaretPosition() == 0) {
                additionalGuest.requestFocus();
                return;
            } else {
                bdayParties_11.positionCaret(bdayParties_11.getCaretPosition() - 1);
            }
        }
        if (bdayParties_22.isFocused()) {
            if (bdayParties_22.getCaretPosition() == 0) {
                bdayParties_11.requestFocus();
                return;
            } else {
                bdayParties_22.positionCaret(bdayParties_22.getCaretPosition() - 1);
            }
        }
        if (bdayParties_33.isFocused()) {
            if (bdayParties_33.getCaretPosition() == 0) {
                numberinParty.requestFocus();
                return;
            } else {
                bdayParties_33.positionCaret(bdayParties_33.getCaretPosition() - 1);
            }
        }
        if (Notes.isFocused()) {
            if (Notes.getCaretPosition() == 0) {
                introduced.requestFocus();
                return;
            } else {
                Notes.positionCaret(Notes.getCaretPosition() - 1);
            }
        }

    }

    private void checkEnterFeildFocus() {

        if (bdayParties_11.isFocused()) {
            bdayParties_22.requestFocus();
            return;
        }
        if (bdayParties_22.isFocused()) {
            contactFirstName.requestFocus();
            return;
        }
        if (contactFirstName.isFocused()) {
            contactLastName.requestFocus();
            return;
        }
        if (contactLastName.isFocused()) {
            phone.requestFocus();
            return;
        }
        if (phone.isFocused()) {
            address.requestFocus();
            return;
        }
        if (address.isFocused()) {
            city.requestFocus();
            return;
        }
        if (city.isFocused()) {
            state.requestFocus();
            return;
        }
        if (state.isFocused()) {
            zipCode.requestFocus();
            return;
        }
        if (zipCode.isFocused()) {
            birthdayName.requestFocus();
            return;
        }
        if (birthdayName.isFocused()) {
            yearsOld.requestFocus();
            return;
        }
        if (yearsOld.isFocused()) {
            numberinParty.requestFocus();
            return;
        }
        if (numberinParty.isFocused()) {
            bdayParties_33.requestFocus();
            return;
        }
        if (additionalGuest.isFocused()) {
            introduced.requestFocus();
            return;
        }
        if (introduced.isFocused()) {
            Notes.requestFocus();
            return;
        }
        if (bdayParties_33.isFocused()) {
            additionalGuest.requestFocus();
        }

    }

    private void goRightArrow() {
        if (contactFirstName.isFocused()) {
            if (contactFirstName.getCaretPosition() == contactFirstName.getText().length()) {
                //System.out.println(contactFirstName.getText().length());
                checkEnterFeildFocus();
                return;
            } else {
                contactFirstName.positionCaret(contactFirstName.getCaretPosition() + 1);
                //System.out.println(contactFirstName.getCaretPosition());
            }
        }
        if (contactLastName.isFocused()) {
            if (contactLastName.getCaretPosition() == contactLastName.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                contactLastName.positionCaret(contactLastName.getCaretPosition() + 1);
            }
        }
        if (phone.isFocused()) {
            if (phone.getCaretPosition() == phone.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                phone.positionCaret(phone.getCaretPosition() + 1);
            }
        }
        if (address.isFocused()) {
            if (address.getCaretPosition() == address.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                address.positionCaret(address.getCaretPosition() + 1);
            }
        }
        if (city.isFocused()) {
            if (city.getCaretPosition() == city.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                city.positionCaret(city.getCaretPosition() + 1);
            }
        }
        if (state.isFocused()) {
            if (state.getCaretPosition() == state.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                state.positionCaret(state.getCaretPosition() + 1);
            }
        }
        if (zipCode.isFocused()) {
            if (zipCode.getCaretPosition() == zipCode.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                zipCode.positionCaret(zipCode.getCaretPosition() + 1);
            }
        }
        if (birthdayName.isFocused()) {
            if (birthdayName.getCaretPosition() == birthdayName.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                birthdayName.positionCaret(birthdayName.getCaretPosition() + 1);
            }
        }
        if (yearsOld.isFocused()) {
            if (yearsOld.getCaretPosition() == yearsOld.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                yearsOld.positionCaret(yearsOld.getCaretPosition() + 1);
            }
        }
        if (numberinParty.isFocused()) {
            if (numberinParty.getCaretPosition() == numberinParty.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                numberinParty.positionCaret(numberinParty.getCaretPosition() + 1);
            }
        }
        if (additionalGuest.isFocused()) {
            if (additionalGuest.getCaretPosition() == additionalGuest.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                additionalGuest.positionCaret(additionalGuest.getCaretPosition() + 1);
            }
        }
        if (bdayParties_11.isFocused()) {
            if (bdayParties_11.getCaretPosition() == bdayParties_11.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                bdayParties_11.positionCaret(bdayParties_11.getCaretPosition() + 1);
            }
        }
        if (bdayParties_22.isFocused()) {
            if (bdayParties_22.getCaretPosition() == bdayParties_22.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                bdayParties_22.positionCaret(bdayParties_22.getCaretPosition() + 1);
            }
        }
        if (bdayParties_33.isFocused()) {
            if (bdayParties_33.getCaretPosition() == bdayParties_33.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                bdayParties_33.positionCaret(bdayParties_33.getCaretPosition() + 1);
            }
        }

        if (introduced.isFocused()) {
            if (introduced.getCaretPosition() == introduced.getText().length()) {
                checkEnterFeildFocus();
                return;
            } else {
                introduced.positionCaret(introduced.getCaretPosition() + 1);
            }
        }
        if (Notes.isFocused()) {
            Notes.positionCaret(Notes.getCaretPosition() + 1);
        }

    }

    private void setEmp() {
        System.out.println("99999999999999 " + eFXX.getEmpNumber());
        en = eFXX.empNumber;
        fn = eFXX.nameF;
        ln = eFXX.nameL;
    }

    public void exitButtonPushed() {
        if (editMode == 1) {
            if (cancelOK < 2) {
                if (cancelOK > 0) {
                    showError("PRESS ESCAPE AGAIN IF YOU ARE SURE YOU DO NOT WANT TO SAVE THE CHANGES YOU HAVE MADE?!.");
                    cancelOK++;
                } else {
                    showError("Press ESCAPE again to cancel your edit or Press F9 to save your edit.");
                    cancelOK++;
                }
                return;
            } else {
                cancelUpdateGO();
                showErrorClear();
                cancelOK = 0;
                init(3);
            }
        } else {
            DB.disConnect();
            editMode = 0;
            stageV = (Stage) address.getScene().getWindow();
            stageV.close();
        }
    }

    /*
    @Override
    public void preLoadData(employeeFX1 eFX) {
        this.eFXX = eFX;
    }
    */
    
}
